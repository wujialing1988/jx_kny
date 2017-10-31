package com.yunda.freight.zb.qualitycontrol.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.freight.zb.qualitycontrol.entity.ZbglQualityControlItemDefine;
import com.yunda.freight.zb.qualitycontrol.entity.ZbglQualityControlItemDefineAndEmpDTO;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglQualityControlItemDefineManager业务类,质量检验配置
 * <li>创建人：林欢
 * <li>创建日期：2016-10-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */ 
@Service(value="zbglQualityControlItemDefineManager")
public class ZbglQualityControlItemDefineManager extends JXBaseManager<ZbglQualityControlItemDefine, ZbglQualityControlItemDefine>{

	/**
     * 
     * <li>说明：根据条件获取所有的质量检验项
     * <li>创建人：林欢
     * <li>创建日期：2016-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param paramsMap 查询条件
     * @return List<ZbglQualityControlItemDefine> 返回的质量检验项目list
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<ZbglQualityControlItemDefine> findZbglQualityControlItemDefineList(Map<String, String> paramsMap) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(" from ZbglQualityControlItemDefine a where 1=1 ");
		
		//判断是否传递业务编码
		if (StringUtils.isNotBlank(paramsMap.get("businessCode"))) {
			sb.append(" and a.businessCode = '").append(paramsMap.get("businessCode")).append("'");
		}
		
//		判断是否传递是否默认标示
		if (StringUtils.isNotBlank(paramsMap.get("isDefault"))) {
			sb.append(" and a.isDefault = ").append(Integer.valueOf(paramsMap.get("isDefault")));
		}
		
//		判断是否传递战场
		if (StringUtils.isNotBlank(paramsMap.get("siteID"))) {
			sb.append(" and a.siteID = '").append(paramsMap.get("siteID")).append("' ");
		}
//		判断是否传递检查项编码
		if (StringUtils.isNotBlank(paramsMap.get("qcItemNo"))) {
			sb.append(" and a.qcItemNo = '").append(paramsMap.get("qcItemNo")).append("' ");
		}
		
		return (List<ZbglQualityControlItemDefine>) this.find(sb.toString());
	}
	
	/**
     * 
     * <li>说明：根据条件获取质量检验项和人员信息
     * <li>创建人：林欢
     * <li>创建日期：2016-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param businessCode 业务菜单名称
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<ZbglQualityControlItemDefineAndEmpDTO> findZbglQualityControlItemDefineAndEmpList(String qcItemDefineIDX) {
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select b.idx, a.qc_item_no, a.qc_item_name, b.check_empid, b.check_empname ");
		sb.append(" from K_QC_ITEM_DEFINE a, K_QC_ITEM_EMEP_DEFINE b ");
		sb.append(" where a.idx = b.qc_item_idx ");
		sb.append(" and a.idx = '").append(qcItemDefineIDX).append("'");
		
		return this.getDaoUtils().executeSqlQueryEntity(sb.toString(), ZbglQualityControlItemDefineAndEmpDTO.class);
	}
	
	/**
     * 
     * <li>说明：重写更新方法
     * <li>创建人：李杜涛
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param businessCode 业务菜单名称
     * @throws Exception
     */
	public void saveOrUpdate(ZbglQualityControlItemDefine zbglQualityControlItemDefine) throws BusinessException, NoSuchFieldException {
		Map<String,String> paramsMap = new HashMap<String,String>();
		paramsMap.put("qcItemNo", zbglQualityControlItemDefine.getQcItemNo());
		List<ZbglQualityControlItemDefine> tempList= this.findZbglQualityControlItemDefineList(paramsMap);
		if(!tempList.isEmpty() && tempList.size()>0){//已经存在相同编码的数据，数据项编码不能重复
			throw new BusinessException("数据项编码已经存在，不能保存成功。请修改数据项编码！");
		}
		super.saveOrUpdate(zbglQualityControlItemDefine);
	}
}