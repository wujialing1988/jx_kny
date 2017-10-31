package com.yunda.webservice.device.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.webservice.device.entity.PartRepairPlan;
import com.yunda.webservice.device.entity.PartRepairPlanList;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：机车检修管理系统为机务设备系统（或其他外部系统）提供根据查询条件查询未处理完的配件生产计划的接口服务 管理器
 * <li>创建人： 王治龙
 * <li>创建日期： 2015-2-10
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="partRdpPlanManager")
public class PartRdpPlanManager extends JXBaseManager<PartRepairPlan, PartRepairPlan> {
	
	/**
     * <li>说明：根据”规格型号编码“查询配件生产计划信息（兑现单信息）
     * <li>创建人：王治龙
     * <li>创建日期：2015-2-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
	 * @param specificationModelCode 配件“规格型号编码”
	 * @return List<PartsRdp>
	 */
	@SuppressWarnings("unchecked")
	private List<PartsRdp> findBySpecificationModelCode(String specificationModelCode) {
		StringBuilder sb = new StringBuilder();
		sb.append(" From PartsRdp t where t.recordStatus = ").append(Constants.NO_DELETE) ;
		sb.append(" and t.specificationModel = '").append(specificationModelCode).append("'");
		sb.append(" and t.status in ('").append(PartsRdp.STATUS_WQD).append("','");
		sb.append(PartsRdp.STATUS_JXZ).append("','");
		sb.append(PartsRdp.STATUS_DYS).append("')");
		return (List<PartsRdp>)this.daoUtils.find(sb.toString());
	}
	
	/**
	 * <li>说明：机车检修管理系统为机务设备系统（或其他外部系统）提供根据查询条件查询未处理完的配件生产计划的接口服务实现
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-10
	 * 
	 * @param specificationModelCode 规格型号
	 * @return String 返回处理结果的XML格式的字符串
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public String getPartRepairPlan(String specificationModelCode) throws IllegalAccessException, InvocationTargetException {
		// 根据“配件规格型号编号”查询出的配件生产计划列表
		List<PartsRdp> list = this.findBySpecificationModelCode(specificationModelCode);
		// 构造返回结果集列表 - 配件检修计划列表
		List<PartRepairPlan> planList = new ArrayList<PartRepairPlan>();
		PartRepairPlan prp = null;
		for (PartsRdp rdp : list) {
			prp = new PartRepairPlan();
			prp.setPartsCode(rdp.getPartsNo());										// 配件编号
			prp.setPartsName(rdp.getPartsName());										// 配件名称
			prp.setPlanRepairTime(rdp.getPlanStartTime().toString().replace(".0", ""));	// 计划检修时间 
			prp.setSpecificationModel(rdp.getSpecificationModel());					// 规格型号
			prp.setSpecificationModelCode(null);			// 规格型号编码
			prp.setUnloadTrainType(rdp.getUnloadTrainType());							// 下车车型
			prp.setUnloadTrainNo(rdp.getUnloadTrainNo());								// 下车车号
			// 数据表中暂时不能查询下车位置信息
			prp.setUnloadPosition(null);
			planList.add(prp);
		}
		// 构造返回结果集列表 - 配件检修计划
		PartRepairPlanList prpl = new PartRepairPlanList();
		prpl.setPartRepairPlanList(planList);
		return prpl.toResponseDataXML();
	}
	
}
