package com.yunda.freight.zb.qualitycontrol.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.EntityUtil;
import com.yunda.freight.zb.gztp.entity.Gztp;
import com.yunda.freight.zb.gztp.manager.GztpManager;
import com.yunda.freight.zb.qualitycontrol.entity.ZbglQualityControl;
import com.yunda.freight.zb.qualitycontrol.entity.ZbglQualityControlDTO;
import com.yunda.freight.zb.qualitycontrol.entity.ZbglQualityControlItemDefine;
import com.yunda.freight.zb.qualitycontrol.entity.ZbglQualityControlItemDefineAndEmpDTO;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglQualityControlManager业务类,质量检查
 * <li>创建人：林欢
 * <li>创建日期：2016-10-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbglQualityControlManager")
public class ZbglQualityControlManager extends JXBaseManager<ZbglQualityControl, ZbglQualityControl>{
	
	/** ZbglQualityControlItemDefineManager业务类,质量检验配置 */
	@Resource
	private ZbglQualityControlItemDefineManager zbglQualityControlItemDefineManager;
	
	/** GztpManager业务类, 列检_故障提票业务类 */
	@Resource
	private GztpManager gztpManager;

	/**
     * <li>说明：保存质量检查实例
     * <li>创建人：何东
     * <li>创建日期：2017-04-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param gztp 故障提票对象
     * @param qcItemDefineIDX 质量检查项目idx
     * @throws Exception
     */
	public void saveZbglQualityControl(Gztp gztp, String[] qcItemDefineIDXs) throws Exception {
		// 公共数据设置
		ZbglQualityControl zbglQualityControl = new ZbglQualityControl();
		zbglQualityControl.setSiteID(gztp.getSiteId());//站点标示
		zbglQualityControl.setCreateTime(new Date());//创建时间
		if (SystemContext.getAcOperator() != null) {
			zbglQualityControl.setCreator(SystemContext.getAcOperator().getOperatorid());//创建人
		}
		zbglQualityControl.setStatus(ZbglQualityControl.UNCHECKED);//状态 默认为未检查
		zbglQualityControl.setBusinessIDX(gztp.getIdx());//业务数据主键
		
		//如果传递过来的数组为null，系统进行自动三检一验指派
		if (qcItemDefineIDXs == null || (qcItemDefineIDXs != null && qcItemDefineIDXs.length < 1)) {
			
			List<String> tempQcItemDefineIDXList = new ArrayList<String>();
			
			// 通过查询默认配置项进行系统自动默认三检一验指派
			Integer isDefault = ZbglQualityControlItemDefine.IS_DEFAULT_YES; //是否默认 0=是 1=否
			
			Map<String, String> pMap = new HashMap<String, String>();
			pMap.put("siteID", gztp.getSiteId());//站场idx
			pMap.put("isDefault", isDefault.toString());//是否默认 0=是 1=否
			List<ZbglQualityControlItemDefine> zbglQualityControlItemDefineList = zbglQualityControlItemDefineManager.findZbglQualityControlItemDefineList(pMap);
			
			//获取默认的三检一验项目idx集合
			for (ZbglQualityControlItemDefine define : zbglQualityControlItemDefineList) {
				tempQcItemDefineIDXList.add(define.getIdx());
			}
			
			//重新封装qcItemDefineIDXs对象
			qcItemDefineIDXs = tempQcItemDefineIDXList.toArray(new String[tempQcItemDefineIDXList.size()]);//list转换为数组
		}
		
		//遍历质量检查项目
		for (String qcItemDefineIDX : qcItemDefineIDXs) {
			
			//获取所有的检测人员
			List<ZbglQualityControlItemDefineAndEmpDTO> zbglQualityControlItemDefineList = zbglQualityControlItemDefineManager.findZbglQualityControlItemDefineAndEmpList(qcItemDefineIDX);
			//判断是否为null，如果为null说明是自检
			if (zbglQualityControlItemDefineList == null || zbglQualityControlItemDefineList.isEmpty()) {
				
				ZbglQualityControl entity = (ZbglQualityControl) BeanUtils.cloneBean(zbglQualityControl);
				//获取质量检查项目
				ZbglQualityControlItemDefine zbglQualityControlItemDefine = zbglQualityControlItemDefineManager.getModelById(qcItemDefineIDX);
				
				entity.setCheckEmpID(gztp.getNoticePersonId());//检查人id 提票人
				entity.setCheckEmpName(gztp.getNoticePersonName());//检查人id 提票人
				entity.setQcItemNo(zbglQualityControlItemDefine.getQcItemNo());//质量检查编码
				entity.setQcItemName(zbglQualityControlItemDefine.getQcItemName());//质量检查名称
				
				this.daoUtils.getHibernateTemplate().saveOrUpdate(entity);
			}else {
				//为所有可以处理的人员生成质量检验单
				for (ZbglQualityControlItemDefineAndEmpDTO dto : zbglQualityControlItemDefineList) {
					ZbglQualityControl entity = (ZbglQualityControl) BeanUtils.cloneBean(zbglQualityControl);
					entity.setQcItemNo(dto.getQcItemNo());//质量检查编码
					entity.setQcItemName(dto.getQcItemName());//质量检查名称
					entity.setCheckEmpID(dto.getCheckEmpID());//检查人员
					entity.setCheckEmpName(dto.getCheckEmpName());//检查人员名称
					this.daoUtils.getHibernateTemplate().saveOrUpdate(entity);
				}
			}
		}
	}
	
	/**
     * 
     * <li>说明：根据条件查询质量检查单
     * <li>创建人：林欢
     * <li>创建日期：2016-10-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param paramsMap 参数map
     * @return List<ZbglQualityControl> 质量检查数据对象list
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<ZbglQualityControl> findZbglQualityControlListByParams(Map<String, Object> paramsMap) {

		StringBuffer sb = new StringBuffer();
		
		sb.append(" from ZbglQualityControl a where 1=1 ");

		//  业务主键id
        if (paramsMap.get("businessIDX") != null) {
            sb.append(" and a.businessIDX = '").append(paramsMap.get("businessIDX").toString()).append("'");
        }
        
        // 质量编码
        if (paramsMap.get("qcItemNo") != null) {
            sb.append(" and a.qcItemNo = '").append(paramsMap.get("qcItemNo").toString()).append("'");
        }
        
        // 状态
        if (paramsMap.get("status") != null) {
        	sb.append(" and a.status in ('").append(paramsMap.get("status").toString().replace(",", "','")).append("')");
        }
        
        // 检查人员empid
        if (paramsMap.get("checkEmpID") != null) {
            sb.append(" and a.checkEmpID = '").append(paramsMap.get("checkEmpID").toString()).append("'");
        }
		
		return (List<ZbglQualityControl>) this.find(sb.toString());
	}
	
	
	public Page<ZbglQualityControlDTO> queryQualityControl(int start, int limit, Gztp tp) {
		// 总条数查询语句
		StringBuilder countSql = new StringBuilder(" select count(t1.idx) as rowcount");
		// 列表查询语句
		StringBuilder sql = new StringBuilder(" select");
//		sql.append(" t1.idx,                             ");
//		sql.append(" t1.business_idx,                    ");
//		sql.append(" t1.qc_item_no,                      ");
//		sql.append(" t1.qc_item_name,                    ");
//		sql.append(" t1.check_empid,                     ");
//		sql.append(" t1.check_empname,                   ");
//		sql.append(" t1.status,                          ");
//		sql.append(" t1.remark,                          ");
		sql.append(" t1.*,                ");
		sql.append(" t2.fault_type_value,                ");
		sql.append(" t2.scope_work_fullname,             ");
		sql.append(" t2.vehicle_component_fullname,      ");
		sql.append(" t2.fault_desc,                      ");
		sql.append(" t2.notice_person_name,              ");
		sql.append(" t2.handle_way                       ");
		
		// 查询条件语句
		StringBuilder whereSql = new StringBuilder();
		whereSql.append(" from k_quality_control t1, k_gztp t2");
		whereSql.append(" where t1.business_idx = t2.idx      ");
		whereSql.append("   and t2.record_status = 0          ");
		whereSql.append("   and t2.site_id = '").append(EntityUtil.findSysSiteId(null)).append("'");
		whereSql.append("   and t1.siteid = '").append(EntityUtil.findSysSiteId(null)).append("'");
		whereSql.append("   and t1.check_empid = ").append(SystemContext.getOmEmployee().getEmpid());
		
		if (tp != null) {
			if (StringUtils.isNotBlank(tp.getRdpRecordPlanIdx())) {
				whereSql.append("   and t2.RDP_RECORD_PLAN_IDX = '").append(tp.getRdpRecordPlanIdx()).append("'");
			}
			
			if (StringUtils.isNotBlank(tp.getStatus())) {
				whereSql.append("   and t1.status = '").append(tp.getStatus()).append("'");
			}
            
            // 客货类型 10 货车，20 客车
            if (StringUtils.isNotBlank(tp.getVehicleType())) {
                whereSql.append("   and t2.T_VEHICLE_TYPE = '").append(tp.getVehicleType()).append("'");
            }            
		}
		
		// 为总条数语句拼接条件
		countSql.append(whereSql);
		
		// 为列表查询语句拼接条件
		sql.append(whereSql);
		sql.append(" order by t2.create_time desc ");
		
		return this.queryPageList(countSql.toString(), sql.toString(), start, limit, false, ZbglQualityControlDTO.class);
	}
	
	/**
     * <li>说明：完成质量检查
     * <li>创建人：何东
     * <li>创建日期：2017-04-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param qcs 质量检查任务对象
     * @param checkResult 检测结果
     */
	public void finishQualityControl(ZbglQualityControl[] qcs, String checkResult) throws BusinessException, NoSuchFieldException {
		
		if (qcs == null || qcs.length <= 0) return;
		
		// 当同一质量检测项中有一个人完成检验，删除其他人的质量检验任务
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", ZbglQualityControl.UNCHECKED);// 检查状态，未检验
		
		// 循环更新质量检验任务
		ZbglQualityControl qcInDb = null;
		Gztp gztp = null;
		for (ZbglQualityControl qc : qcs) {
			qcInDb = this.getModelById(qc.getIdx());
			
			if (StringUtils.isNotBlank(qc.getStatus())) {
				qcInDb.setStatus(qc.getStatus());
			}
			
			if (StringUtils.isNotBlank(checkResult)) {
				qcInDb.setRemark(checkResult);
			}
			
			this.saveOrUpdate(qcInDb);
			
			// 设置删除其他人质量检验任务数据的参数
			params.put("businessIDX", qcInDb.getBusinessIDX()); // 业务主键
			params.put("qcItemNo", qcInDb.getQcItemNo()); // 质量检查项
			
			List<ZbglQualityControl> qcListUnCheckedSame = findZbglQualityControlListByParams(params);
			if (qcListUnCheckedSame != null && qcListUnCheckedSame.size() > 0) {
				for (ZbglQualityControl control : qcListUnCheckedSame) {
					this.daoUtils.remove(control);
				}
			}
			
			// 判断该业务数据（故障登记）所关联的质量检测任务是否全部完成，若全部完成更新该业务数据（故障登记）的状态为已检验
			params.remove("qcItemNo");
			
			List<ZbglQualityControl> qcListAll = findZbglQualityControlListByParams(params);
			if (qcListAll != null && qcListAll.size() > 0) {
				boolean allOver = true;
				for (ZbglQualityControl control : qcListAll) {
					if (ZbglQualityControl.UNCHECKED.equals(control.getStatus())) {
						allOver = false;
						break;
					}
				}
				
				if (allOver) {
					gztp = gztpManager.getModelById(qcInDb.getBusinessIDX());
					gztpManager.saveOrUpdate(gztp);
				}
			}
		}
	}
}