package com.yunda.freight.zb.gztp.manager;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.freight.zb.gztp.entity.Gztp;
import com.yunda.freight.zb.qualitycontrol.entity.ZbglQualityControl;
import com.yunda.freight.zb.qualitycontrol.manager.ZbglQualityControlManager;
import com.yunda.jx.pjwz.partsBase.entity.MatTypeUse;
import com.yunda.jx.pjwz.partsBase.manager.MatTypeUseManager;
import com.yunda.jxpz.utils.CodeRuleUtil;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检_故障提票业务类
 * <li>创建人：何东
 * <li>创建日期：2017-04-12 10:48:28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("gztpManager")
public class GztpManager extends JXBaseManager<Gztp, Gztp> {
    
	/** 人员业务类 */
    @Resource
    private IOmEmployeeManager omEmployeeManager;
    /** 质量检查 */
    @Resource
    private ZbglQualityControlManager zbglQualityControlManager;
    /** 物料消耗业务类 */
    @Resource
    private MatTypeUseManager matTypeUseManager ;
   
    /**
	 * <li>说明：判断相同车次、相同车辆、相同范围活、相同构型位置、相同处理类型是否已经登记过
	 * <li>创建人：何东
	 * <li>创建日期：2017-04-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public String[] validateUpdate(Gztp t){
		if (StringUtils.isNotBlank(t.getScopeWorkIdx()) && StringUtils.isNotBlank(t.getVehicleComponentFlbm())) {
			StringBuilder hql = new StringBuilder(" from Gztp where recordStatus = 0");
			hql.append(" and rdpPlanIdx = ?");
			hql.append(" and rdpRecordPlanIdx = ?");
			hql.append(" and scopeWorkIdx = ?");
			hql.append(" and vehicleComponentFlbm = ?");
			hql.append(" and handleType = ?");
			
			List<Gztp> tpLst = (List<Gztp>)this.daoUtils.find(hql.toString(), t.getRdpPlanIdx(), t.getRdpRecordPlanIdx(),
					t.getScopeWorkIdx(), t.getVehicleComponentFlbm(), t.getHandleType());
			
			if (StringUtils.isBlank(t.getIdx()) && !CollectionUtils.isEmpty(tpLst)) {
				return new String[]{"该车辆相同作业范围、相同故障部件已经进行了登记！"};
			} else if (StringUtils.isNotBlank(t.getIdx()) && !CollectionUtils.isEmpty(tpLst)) {
				Gztp newTp = null;
				for (Gztp tp : tpLst) {
					if (!t.getIdx().equals(tp.getIdx())) {
						return new String[]{"该车辆相同专业、相同故障部件已经进行了登记！"};
					}
					if (t.getIdx().equals(tp.getIdx())) {
						newTp = tp;
					}
				}
				
				// 判断是否已完成质量检查，若已完成则不能进行修改了，防止多终端操作数据不同步问题
				if (Gztp.STATUS_CHECKED.equals(newTp.getFaultNoticeStatus())) {
					return new String[]{"质量检验已完成，不能修改！"};
				}
				
				// 如果是上报数据，且上报已处理，也不能修改
				if (Gztp.STATUS_OVER.equals(newTp.getFaultNoticeStatus()) && Gztp.HANDLE_TYPE_REP == newTp.getHandleType()) {
					return new String[]{"上报数据已处理，不能修改！"};
				}
				
				// 如果有一人以上已经做了质量检验，则提示不能修改
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("businessIDX", t.getIdx());
				paramsMap.put("status", ZbglQualityControl.QUALIFIED + "," + ZbglQualityControl.UNQUALIFIED);
				List<ZbglQualityControl> ctls = zbglQualityControlManager.findZbglQualityControlListByParams(paramsMap);
				if (!CollectionUtils.isEmpty(ctls)) {
					return new String[]{"质量检验处理中，不能修改！"};
				}
			}
		}
		
		return null;
	}
    
    /**
	 * <li>说明：重写保存修改方法，添加提票单号、提票人等信息
	 * <li>创建人：何东
	 * <li>创建日期：2017-04-14
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param T t 实体对象
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */
	@SuppressWarnings("all")
    @Override
	public void saveOrUpdate(Gztp t) throws BusinessException, NoSuchFieldException {
    	if (StringUtils.isBlank(t.getIdx())) {
    		buildEntity(t);
    	}
		super.saveOrUpdate(t);
		
		// 查询判断 该故障登记是否已经生成了质量检验数据，若已生成则不再生成，若已生成且状态为 “初始化”则删除生成的质量检验数据
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("businessIDX", t.getIdx());
		List<ZbglQualityControl> ctls = zbglQualityControlManager.findZbglQualityControlListByParams(paramsMap);
		if (!CollectionUtils.isEmpty(ctls)) {
			if (Gztp.STATUS_INIT.equals(t.getFaultNoticeStatus())) {
				String[] ids = new String[ctls.size()];
				int i = 0;
				for (ZbglQualityControl qc : ctls) {
					ids[i] = qc.getIdx();
					i++;
				}
				zbglQualityControlManager.deleteByIds(ids);
			}
		} else if (Gztp.STATUS_OVER.equals(t.getFaultNoticeStatus())) {
			try {
				// 创建质量检验单
				zbglQualityControlManager.saveZbglQualityControl(t, null);
			} catch (Exception e) {
				throw new BusinessException(e);
			}
		}
	}
    
    /**
     * <li>说明：保存故障提票
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param t
     * @return
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public Gztp saveGztp(Gztp t) throws BusinessException, NoSuchFieldException {
        if (StringUtils.isBlank(t.getIdx())) {
            buildEntity(t);
        }
        super.saveOrUpdate(t);
        
        // 查询判断 该故障登记是否已经生成了质量检验数据，若已生成则不再生成，若已生成且状态为 “初始化”则删除生成的质量检验数据
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("businessIDX", t.getIdx());
        List<ZbglQualityControl> ctls = zbglQualityControlManager.findZbglQualityControlListByParams(paramsMap);
        if (!CollectionUtils.isEmpty(ctls)) {
            if (Gztp.STATUS_INIT.equals(t.getFaultNoticeStatus())) {
                String[] ids = new String[ctls.size()];
                int i = 0;
                for (ZbglQualityControl qc : ctls) {
                    ids[i] = qc.getIdx();
                    i++;
                }
                zbglQualityControlManager.deleteByIds(ids);
            }
        } else if (Gztp.STATUS_OVER.equals(t.getFaultNoticeStatus())) {
            try {
                // 创建质量检验单
                zbglQualityControlManager.saveZbglQualityControl(t, null);
            } catch (Exception e) {
                throw new BusinessException(e);
            }
        }
        return t ;
    }
    
	/**
     * <li>说明：添加提票单号、提票人等信息
     * <li>创建人：何东
     * <li>创建日期：2017-04-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 提票实体对象
     * @return 提票实体对象
     */
    private Gztp buildEntity(Gztp entity) {
        entity.setFaultNoticeCode(CodeRuleUtil.getRuleCode("JCZL_FAULT_NOTICE_FAULT_NOTICE_CODE"));// FIXME 使用检修系统的提票编码
        OmEmployee emp = omEmployeeManager.findByOperator(SystemContext.getAcOperator().getOperatorid());
        entity.setNoticePersonId(emp != null ? emp.getEmpid() : null);
        entity.setNoticePersonName(emp != null ? emp.getEmpname() : "");
        entity.setNoticeTime(new Date());
        String siteId = EntityUtil.findSysSiteId(null);
        entity.setSiteId(siteId);
        entity.setSiteName(EntityUtil.findSysSiteName(siteId, null));
        return entity;
    }
    
    /**
     * <li>说明：删除前判断能否删除
     * <li>创建人：何东
     * <li>创建日期：2017-04-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids
     * @return 验证消息
     */
    @SuppressWarnings("all")
	@Override
    public String[] validateDelete(Serializable... ids){
    	if (ids == null || ids.length <= 0) return null;
    	
    	StringBuilder hql = new StringBuilder(" from Gztp where recordStatus = 0");
		hql.append(" and idx in (?)");
		
		List<Gztp> tpLst = (List<Gztp>)this.daoUtils.find(hql.toString(), ids);
		
		if (!CollectionUtils.isEmpty(tpLst)) {
			for (Gztp t : tpLst) {
				// 判断是否已完成质量检查，若已完成则不能进行修改了，防止多终端操作数据不同步问题
				if (Gztp.STATUS_CHECKED.equals(t.getFaultNoticeStatus())) {
					return new String[]{"质量检验已完成，不能删除！"};
				}
				
				// 如果是上报数据，且上报已处理，也不能修改
				if (Gztp.STATUS_OVER.equals(t.getFaultNoticeStatus()) && Gztp.HANDLE_TYPE_REP == t.getHandleType()) {
					return new String[]{"上报数据已处理，不能删除！"};
				}
				
				// 如果有一人以上已经做了质量检验，则提示不能修改
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("businessIDX", t.getIdx());
				paramsMap.put("status", ZbglQualityControl.QUALIFIED + "," + ZbglQualityControl.UNQUALIFIED);
				List<ZbglQualityControl> ctls = zbglQualityControlManager.findZbglQualityControlListByParams(paramsMap);
				if (!CollectionUtils.isEmpty(ctls)) {
					return new String[]{"质量检验处理中，不能删除！"};
				}
			}
		}
    	
    	return null;
    }

    /**
     * <li>说明：保存故障提票
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param gztp
     * @param matUses
     * @throws NoSuchFieldException 
     * @throws BusinessException  
     */
    public void saveGztps(Gztp gztp, MatTypeUse[] matUses) throws BusinessException, NoSuchFieldException {
            Gztp tp = this.saveGztp(gztp);
            if(tp != null && matUses != null && matUses.length > 0){
                matTypeUseManager.saveMatUsesForGZTP(matUses,tp.getIdx()); // 保存物料信息
            }
    }
    
    /**
     * <li>说明：故障提票分类统计
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-11-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public List<Map<String, Object>> findGzFlStatistics(){
        String sql = SqlMapUtil.getSql("zb-tp:findGzFlStatistics");
        return this.queryListMap(sql);
    }  
    
}
