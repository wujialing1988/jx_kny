package com.yunda.sb.inspect.plan.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;
import com.yunda.sb.inspect.plan.entity.InspectPlanEquipment;
import com.yunda.sb.inspect.plan.entity.InspectPlanEquipmentEmp;
import com.yunda.sb.inspect.record.entity.InspectRecord;
import com.yunda.sb.inspect.record.manager.InspectRecordManager;
import com.yunda.sb.inspect.route.entity.InspectRouteDetails;
import com.yunda.sb.inspect.scope.entity.InspectScope;
import com.yunda.sb.inspect.scope.manager.InspectScopeManager;
import com.yunda.sb.repair.scope.entity.RepairScope;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectPlanEquipmentEmp管理器，数据表：E_INSPECT_PLAN_EQUIPMENT_EMP
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "inspectPlanEquipmentEmpManager")
public class InspectPlanEquipmentEmpManager extends JXBaseManager<InspectPlanEquipmentEmp, InspectPlanEquipmentEmp> {

	/** EquipmentPrimaryInfo管理器，数据表：E_EQUIPMENT_PRIMARY_INFO */
	@Resource
	private EquipmentPrimaryInfoManager equipmentPrimaryInfoManager;

	/** InspectRecord业务类，数据表：E_INSPECT_RECORD */
	@Resource
	private InspectRecordManager inspectRecordManager;

	/** InspectPlanEquipment业务类，数据表：E_INSPECT_PLAN_EQUIPMENT */
	@Resource
	private InspectPlanEquipmentManager inspectPlanEquipmentManager;

	/** InspectScope业务类，数据表：E_INSPECT_SCOPE */
	@Resource
	private InspectScopeManager inspectScopeManager;

	/**
	 * <li>说明：实例化周期设备巡检人员，定时器自动生成巡检计划时
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planEquipment 巡检设备实体对象
	 * @param t 设备巡检线路明细实体对象
	 * @throws NoSuchFieldException 
	 */
	public void startUp(InspectPlanEquipment planEquipment, InspectRouteDetails t) throws NoSuchFieldException {
		// 保存机械巡检人员
		this.save(planEquipment, RepairScope.REPAIR_TYPE_JX, t.getMacInspectEmp(), t.getMacInspectEmpid());
		// 保存电气巡检人员
		this.save(planEquipment, RepairScope.REPAIR_TYPE_DQ, t.getElcInspectEmp(), t.getElcInspectEmpid());
	}

	/**
	 * <li>说明：实例化周期设备巡检人员，人为手动添加巡检设备时
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planEquipment 巡检设备实体对象
	 * @throws NoSuchFieldException
	 */
	public void startUp(InspectPlanEquipment planEquipment) throws NoSuchFieldException {
		// 保存机械巡检人员
		this.save(planEquipment, RepairScope.REPAIR_TYPE_JX, null, null);
		// 保存机械电气人员
		this.save(planEquipment, RepairScope.REPAIR_TYPE_DQ, null, null);
	}

	/**
	 * <li>说明：保存设备巡检人员信息，如果机械（电气）系数为0，则没有机械（电气）巡检人员信息
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月17日
	 * <li>修改人：何涛
	 * <li>修改内容：修改巡检线路下添加的设备，在设备主要信息被删除时，会出现空指针异常的错误
	 * <li>修改日期：2016年9月8日
	 * @param planEquipment 巡检设备实体对象
	 * @param repairType 检修类型（1：机械、2：电气、3：其它）
	 * @param inspectEmp 巡检人员，如果为空，则使用设备主要信息表中维护的设备包修人
	 * @param inspectEmpid 巡检人员id，如果为空，则使用设备主要信息表中维护的设备包修人id
	 * @throws NoSuchFieldException
	 */
	private void save(InspectPlanEquipment planEquipment, int repairType, String inspectEmp, String inspectEmpid) throws NoSuchFieldException {
		EquipmentPrimaryInfo epi = this.equipmentPrimaryInfoManager.getModelById(planEquipment.getEquipmentIdx());
		if (null == epi) {
			return;
		}
		// Modified by hetao on 2017-04-05 如果添加的设备未维护设备巡检标准（项目），则不生成“设备巡检人员”记录
		List<InspectScope> scopes = this.inspectScopeManager.getModelsByClassCode(epi.getEquipmentCode(), epi.getClassCode(), repairType);
		if (null == scopes || scopes.isEmpty()) {
			return;
		}
		// 如果机械（电气）系数为零，则不生成机械（电气）巡检项目
		if ((RepairScope.REPAIR_TYPE_JX == repairType && (null == epi.getMechanicalCoefficient() || 0 >= epi.getMechanicalCoefficient()))
				|| (RepairScope.REPAIR_TYPE_DQ == repairType && (null == epi.getElectricCoefficient() || 0 >= epi.getElectricCoefficient()))) {
			return;
		}
		InspectPlanEquipmentEmp entity = new InspectPlanEquipmentEmp();
		entity.setPlanEquipmentIdx(planEquipment.getIdx());
		entity.setCheckResult(InspectPlanEquipment.CHECK_RESULT_WXJ);
		// 机械巡检人员
		if (RepairScope.REPAIR_TYPE_JX == repairType) {
			entity.setInspectEmp(StringUtil.isNullOrBlank(inspectEmp) ? epi.getMechanicalRepairPerson() : inspectEmp);
			entity.setInspectEmpid(StringUtil.isNullOrBlank(inspectEmpid) ? epi.getMechanicalRepairPersonId() : inspectEmpid);
			// 电气巡检人员
		} else if (RepairScope.REPAIR_TYPE_DQ == repairType) {
			entity.setInspectEmp(StringUtil.isNullOrBlank(inspectEmp) ? epi.getElectricRepairPerson() : inspectEmp);
			entity.setInspectEmpid(StringUtil.isNullOrBlank(inspectEmpid) ? epi.getElectricRepairPersonId() : inspectEmpid);
		}
		// 设置巡检班组信息
		this.setInspectOrg(entity);
		entity.setRepairType(repairType);

		// 定时器不能获取登录用户信息，所以不能使用manager的saveOrUpdate()方法
		EntityUtil.setNoDelete(entity);
		this.saveOrUpdate(entity);
	}

	/**
	 * <li>说明：根据人员id获取该人员所在班组id
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月10日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param empid 人员id，多个人员使用英文状态下的逗号进行分隔
	 * @return 班组id，多个班组使用英文状态下的逗号进行分隔
	 */
	private String getOrgidByEmpid(String empid) {
		if (StringUtil.isNullOrBlank(empid)) {
			return null;
		}
		empid = ("'" + empid + "'").replace(";", "','");
		String sql = "SELECT WMSYS.WM_CONCAT(DISTINCT ORGID) FROM OM_EMPLOYEE WHERE EMPID IN (" + empid + ")";
		List<?> list = this.daoUtils.executeSqlQuery(sql);
		if (null == list || list.isEmpty()) {
			return null;
		}
		return list.get(0).toString();
	}

	/**
	 * <li>说明：根据人员id获取该人员所在班组名称
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月10日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param empid 人员id，多个人员使用英文状态下的逗号进行分隔
	 * @return 班组名称，多个班组使用英文状态下的逗号进行分隔
	 */
	private String getOrgnameByEmpid(String empid) {
		if (StringUtil.isNullOrBlank(empid)) {
			return null;
		}
		empid = ("'" + empid + "'").replace(";", "','");
		String sql = "SELECT WMSYS.WM_CONCAT(DISTINCT ORGNAME) FROM OM_EMPLOYEE A, OM_ORGANIZATION B WHERE A.ORGID = B.ORGID AND EMPID IN (" + empid + ")";
		List<?> list = this.daoUtils.executeSqlQuery(sql);
		if (null == list || list.isEmpty()) {
			return null;
		}
		return list.get(0).toString();
	}

	/**
	 * <li>说明：设置巡检班组信息
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月10日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 巡检设备人员实体对象
	 */
	private void setInspectOrg(InspectPlanEquipmentEmp entity) {
		String inspectOrgid = this.getOrgidByEmpid(entity.getInspectEmpid());
		entity.setInspectOrgid(inspectOrgid);
		String inspectOrgname = this.getOrgnameByEmpid(entity.getInspectEmpid());
		entity.setInspectOrgname(inspectOrgname);
	}

	/**
	 * <li>说明：设置委托巡检班组信息
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月10日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param entity 巡检设备人员实体对象
	 */
	private void setEntrustInspectOrg(InspectPlanEquipmentEmp entity) {
		String entrustInspectOrgid = this.getOrgidByEmpid(entity.getEntrustInspectEmpid());
		entity.setEntrustInspectOrgid(entrustInspectOrgid);
		String entrustInspectOrgname = this.getOrgnameByEmpid(entity.getInspectEmpid());
		entity.setEntrustInspectOrgname(entrustInspectOrgname);
	}

	/**
	 * <li>说明：保存委托设备巡检人员
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentIds 巡检设备idx主键数组
	 * @param t 巡检设备数据封装实体
	 * @throws NoSuchFieldException
	 */
	public void saveEntrustInspectEmp(String[] planEquipmentIds, EntrustInspectEmp t) throws NoSuchFieldException {
		InspectPlanEquipmentEmp entity = null;
		for (String planEquipmentIdx : planEquipmentIds) {
			// 机械巡检人员
			entity = this.getModel(planEquipmentIdx, RepairScope.REPAIR_TYPE_JX);
			if (null != entity && !StringUtil.isNullOrBlank(t.getEntrustMacInspectEmpid())) {
				entity.setEntrustInspectEmp(t.getEntrustMacInspectEmp());
				entity.setEntrustInspectEmpid(t.getEntrustMacInspectEmpid());
				// 设置委托巡检班组信息
				this.setEntrustInspectOrg(entity);
				this.saveOrUpdate(entity);
			}
			// 电气巡检人员
			entity = this.getModel(planEquipmentIdx, RepairScope.REPAIR_TYPE_DQ);
			if (null != entity && !StringUtil.isNullOrBlank(t.getEntrustElcInspectEmpid())) {
				entity.setEntrustInspectEmp(t.getEntrustElcInspectEmp());
				entity.setEntrustInspectEmpid(t.getEntrustElcInspectEmpid());
				// 设置委托巡检班组信息
				this.setEntrustInspectOrg(entity);
				this.saveOrUpdate(entity);
			}
		}
	}

	/**
	 * <li>说明：根据“巡检设备idx主键”和“检修类型”获取巡检设备人员
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planEquipmentIdx 巡检设备idx主键
	 * @param repairType 检修类型
	 * @return 巡检设备人员实体对象
	 */
	public InspectPlanEquipmentEmp getModel(String planEquipmentIdx, int repairType) {
		String hql = "From InspectPlanEquipmentEmp Where planEquipmentIdx = ? And repairType = ?";
		return (InspectPlanEquipmentEmp) this.daoUtils.findSingle(hql, planEquipmentIdx, repairType);
	}

	/**
	 * <li>说明：根据“巡检设备idx主键”获取巡检设备人员集合
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planEquipmentIdx  巡检设备idx主键
	 * @return 巡检设备人员集合
	 */
	@SuppressWarnings("unchecked")
	public List<InspectPlanEquipmentEmp> getModelsByPlanEquipmentIdx(String planEquipmentIdx) {
		String hql = "From InspectPlanEquipmentEmp Where planEquipmentIdx = ?";
		return this.daoUtils.find(hql, planEquipmentIdx);
	}

	/**
	 * <li>说明：巡检人员提交设备巡检结果
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月17日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 巡检设备人员参数封装实体，{
	 * 		"planEquipmentIdx": "8a8284f2569228190156922f9f8d0014",
	 * 		"checkResultDesc": "电气巡检完成！"
	 * }
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	public void updateFinish(InspectPlanEquipmentEmp t) throws NoSuchFieldException {
		long empid = SystemContext.getOmEmployee().getEmpid();
		String sql = "SELECT * FROM  E_INSPECT_PLAN_EQUIPMENT_EMP WHERE RECORD_STATUS = 0 AND PLAN_EQUIPMENT_IDX = '" + t.getPlanEquipmentIdx()
				+ "' AND ((','||INSPECT_EMPID||',') LIKE '%," + empid + ",%' OR (','||ENTRUST_INSPECT_EMPID||',') LIKE '%," + empid + ",%')";
		List<InspectPlanEquipmentEmp> entityList = this.daoUtils.executeSqlQueryEntity(sql, InspectPlanEquipmentEmp.class);
		List<InspectRecord> list = null;
		for (InspectPlanEquipmentEmp entity : entityList) {
			// 验证对应类型机械（或者电气）的巡检记录是否都已处理
			list = this.inspectRecordManager.getUndoModels(t.getPlanEquipmentIdx(), entity.getRepairType());
			if (null != list && !list.isEmpty()) {
				throw new RuntimeException("该设备下还有【" + list.size() + "】条检查项未处理，不能提交！");
			}
			entity.setCheckResult(InspectPlanEquipment.CHECK_RESULT_YXJ);
			entity.setCheckResultDesc(t.getCheckResultDesc());
			this.saveOrUpdate(entity);
		}

		// 如果该条巡检下属所有机械和电气巡检项目已巡检完成，则自动更新巡检设备状态为已巡检
		this.updateFinsih2InspectPlanEquipment(t.getPlanEquipmentIdx());
	}

	/**
	 * <li>说明：更新巡检设备状态为已巡检
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月17日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planEquipmentIdx 巡检设备idx主键
	 * @throws NoSuchFieldException
	 */
	private void updateFinsih2InspectPlanEquipment(String planEquipmentIdx) throws NoSuchFieldException {
		List<InspectPlanEquipmentEmp> entityList = this.getModelsByPlanEquipmentIdx(planEquipmentIdx);
		StringBuilder sb = new StringBuilder();
		for (InspectPlanEquipmentEmp entity : entityList) {
			if (InspectPlanEquipment.CHECK_RESULT_WXJ.equals(entity.getCheckResult())) {
				return;
			}
			if (StringUtil.isNullOrBlank(entity.getCheckResultDesc())) {
				continue;
			}
			sb.append(";").append(entity.getCheckResultDesc());
		}
		String checkResultDesc = sb.length() > 0 ? sb.substring(1) : null;
		this.inspectPlanEquipmentManager.updateFinish(planEquipmentIdx, checkResultDesc);
	}

	/**
	 * <li>标题: 设备管理信息系统
	 * <li>说明: 委托巡检人员封装数据结构
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月16日
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * <li>版权: Copyright (c) 2008 运达科技公司
	 * @author 信息系统事业部设备管理系统项目组
	 * @version 3.0.1
	 */
	public static class EntrustInspectEmp {

		/** 委托机械巡检人 */
		private String entrustMacInspectEmp;

		/** 委托机械巡检人id */
		private String entrustMacInspectEmpid;

		/** 委托电气巡检人 */
		private String entrustElcInspectEmp;

		/** 委托电气巡检人id */
		private String entrustElcInspectEmpid;

		public String getEntrustMacInspectEmp() {
			return entrustMacInspectEmp;
		}

		public void setEntrustMacInspectEmp(String entrustMacInspectEmp) {
			this.entrustMacInspectEmp = entrustMacInspectEmp;
		}

		public String getEntrustMacInspectEmpid() {
			return entrustMacInspectEmpid;
		}

		public void setEntrustMacInspectEmpid(String entrustMacInspectEmpid) {
			this.entrustMacInspectEmpid = entrustMacInspectEmpid;
		}

		public String getEntrustElcInspectEmp() {
			return entrustElcInspectEmp;
		}

		public void setEntrustElcInspectEmp(String entrustElcInspectEmp) {
			this.entrustElcInspectEmp = entrustElcInspectEmp;
		}

		public String getEntrustElcInspectEmpid() {
			return entrustElcInspectEmpid;
		}

		public void setEntrustElcInspectEmpid(String entrustElcInspectEmpid) {
			this.entrustElcInspectEmpid = entrustElcInspectEmpid;
		}
	}

	/**
	 * <li>说明：如果设备的某一类型（机械、电气）的巡检记录已处理完成，则更新同类型的“设备巡检人员”的巡检结果为：已巡检
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planEquipmentIdx 巡检设备idx主键
	 * @param repairType 检修类型
	 * @throws NoSuchFieldException
	 */
	public void updateFinishByIR(String planEquipmentIdx, Integer repairType) throws NoSuchFieldException {
		List<InspectRecord> list = this.inspectRecordManager.getUndoModels(planEquipmentIdx, repairType);
		if (null != list && !list.isEmpty()) {
			return;
		}
		InspectPlanEquipmentEmp entity = this.getModel(planEquipmentIdx, repairType);
		if (InspectPlanEquipment.CHECK_RESULT_YXJ.equals(entity.getCheckResult())) {
			return;
		}
		entity.setCheckResult(InspectPlanEquipment.CHECK_RESULT_YXJ);
		this.saveOrUpdate(entity);
	}

}
