package com.yunda.webservice.device.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.webservice.device.entity.PartRepairPlanList;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：机车检修管理系统为机务设备系统（或其他外部系统）提供根据查询条件查询未处理完的配件生产计划的接口服务 管理器
 * <li>创建人： 何涛
 * <li>创建日期： 2014-8-26 下午03:59:31
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="partRepairPlanManager")
public class PartRepairPlanManager extends JXBaseManager<PartRepairPlanManager.PartRepairPlan, PartRepairPlanManager.PartRepairPlan> {
	
	/**
     * <li>说明：根据”规格型号编码“查询配件生产计划信息
     * <li>创建人：何涛
     * <li>创建日期：2014-08-26
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
	 * @param specificationModelCode 配件“规格型号编码”
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<PartRepairPlan> findBySpecificationModelCode(String specificationModelCode) {
//        V3.2.1代码重构
//		StringBuilder sb = new StringBuilder();
//		sb.append("select a.idx as idx, b.specification_model as specificationModel, a.nameplate_no as specificationModelCode," +
//				" a.Parts_Name as partsName, a.parts_no as partsCode, a.unloadTrain_Type_ShortName as unloadTrainType," +
//				" a.UnloadTrain_No as unloadTrainNo, to_char(a.plan_begin_Time, 'yyyy-MM-dd HH24:MI:SS') as planRepairTime" +
//				" from JXGC_Parts_Enforce_Plan_Rdp a, pjwz_parts_type b where a.parts_type_idx = b.idx" +
//				" and a.record_status =0 and b.record_status=0 and a.bill_status = '" + PartsEnforcePlanRdp.STATUS_HANDLING + "'");
//		sb.append(" and b.specification_model_code = '");
//		// 查询条件  - 配件“规格型号编码”
//		sb.append(specificationModelCode);
//		sb.append("'");
//		return this.daoUtils.executeSqlQueryEntity(sb.toString(), PartRepairPlanManager.PartRepairPlan.class);
        return new ArrayList<PartRepairPlan>();
	}
	
	/**
	 * <li>说明：机车检修管理系统为机务设备系统（或其他外部系统）提供根据查询条件查询未处理完的配件生产计划的接口服务实现
	 * <li>创建人：何涛
	 * <li>创建日期：2014-08-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param XMLData： 请求参数，XML格式的字符串
	 * @return String :返回处理结果的XML格式的字符串
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws
	 */
	public String getPartRepairPlan(String specificationModelCode) throws IllegalAccessException, InvocationTargetException {
		// 根据“配件规格型号编号”查询出的配件生产计划列表
		List<PartRepairPlan> list = this.findBySpecificationModelCode(specificationModelCode);
		// 构造返回结果集列表 - 配件检修计划列表
		List<com.yunda.webservice.device.entity.PartRepairPlan> planList = new ArrayList<com.yunda.webservice.device.entity.PartRepairPlan>();
		com.yunda.webservice.device.entity.PartRepairPlan prp = null;
		for (PartRepairPlan plan : list) {
			prp = new com.yunda.webservice.device.entity.PartRepairPlan();
			prp.setPartsCode(plan.getPartsCode());										// 配件编号
			prp.setPartsName(plan.getPartsName());										// 配件名称
			prp.setPlanRepairTime(plan.getPlanRepairTime());							// 计划检修时间 
			prp.setSpecificationModel(plan.getSpecificationModel());					// 规格型号
			prp.setSpecificationModelCode(plan.getSpecificationModelCode());			// 规格型号编码
			prp.setUnloadTrainType(plan.getUnloadTrainType());							// 下车车型
			prp.setUnloadTrainNo(plan.getUnloadTrainNo());								// 下车车号
			// 数据表中暂时不能查询下车位置信息
			prp.setUnloadPosition(null);
			planList.add(prp);
		}
		// 构造返回结果集列表 - 配件检修计划
		PartRepairPlanList prpl = new PartRepairPlanList();
		prpl.setPartRepairPlanList(planList);
		return prpl.toResponseDataXML();
	}
	
	/**
	 * <li>标题: 机车检修管理信息系统
	 * <li>说明：配件生产计划信息 - 数据库查询实体
	 * <li>创建人： 何涛
	 * <li>创建日期： 2014-8-26 下午03:59:31
	 * <li>修改人: 
	 * <li>修改日期： 
	 * <li>修改内容： 
	 * <li>版权: Copyright (c) 2008 运达科技公司
	 * 
	 * @author 测控部检修系统项目组
	 * @version 1.0
	 */
	@Entity
	static class PartRepairPlan{
		
		/** idx主键 */
		@Id
		private String idx;
		
		/** 规格型号 */
		private String specificationModel;
		
		/** 规格型号编码 */
		private String specificationModelCode;
		
		
        /** 配件名称 */
		private String partsName;
		
		/** 配件编号 */
		private String partsCode;
		
		/** 下车车型 */
		private String unloadTrainType;
		
		/** 下车车号 */
		private String unloadTrainNo;
		
		/** 计划开始时间 */
		private String planRepairTime;
		
		public PartRepairPlan() {
			super();
		}
		
		/**
		 * @return 获取idx主键
		 */
		public String getIdx() {
			return idx;
		}

		/**
		 * @param idx 设置idx主键
		 */
		public void setIdx(String idx) {
			this.idx = idx;
		}

		/**
		 * @return 获取配件编号
		 */
		public String getPartsCode() {
			return partsCode;
		}

		/**
		 * @param partsCode 设置配件编号
		 */
		public void setPartsCode(String partsCode) {
			this.partsCode = partsCode;
		}

		/**
		 * @return 获取配件名称
		 */
		public String getPartsName() {
			return partsName;
		}

		/**
		 * @param partsName 设置配件名称
		 */
		public void setPartsName(String partsName) {
			this.partsName = partsName;
		}

		/**
		 * @return 获取计划开始时间
		 */
		public String getPlanRepairTime() {
			return planRepairTime;
		}

		/**
		 * @param planRepairTime 设置计划开始时间
		 */
		public void setPlanRepairTime(String planRepairTime) {
			this.planRepairTime = planRepairTime;
		}

		/**
		 * @return 获取规格型号
		 */
		public String getSpecificationModel() {
			return specificationModel;
		}

		/**
		 * @param specificationModel 设置规格型号
		 */
		public void setSpecificationModel(String specificationModel) {
			this.specificationModel = specificationModel;
		}

		/**
		 * @return 获取规格型号编码
		 */
		public String getSpecificationModelCode() {
			return specificationModelCode;
		}

		/**
		 * @param specificationModelCode 设置规格型号编码
		 */
		public void setSpecificationModelCode(String specificationModelCode) {
			this.specificationModelCode = specificationModelCode;
		}

		/**
		 * @return 获取下车车号
		 */
		public String getUnloadTrainNo() {
			return unloadTrainNo;
		}

		/**
		 * @param unloadTrainNo 设置下车车号
		 */
		public void setUnloadTrainNo(String unloadTrainNo) {
			this.unloadTrainNo = unloadTrainNo;
		}

		/**
		 * @return 获取下车车型
		 */
		public String getUnloadTrainType() {
			return unloadTrainType;
		}

		/**
		 * @param unloadTrainType 设置下车车型
		 */
		public void setUnloadTrainType(String unloadTrainType) {
			this.unloadTrainType = unloadTrainType;
		}
		
	}

}
