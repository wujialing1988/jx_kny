package com.yunda.sb.inspect.plan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.yunda.frame.common.IgnoreOrder;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: InspectPlanEquipmentBean实体类，用于设备巡检日志联合分页查询
 * <li>创建人：何涛
 * <li>创建日期：2017年3月2日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
public final class InspectPlanEquipmentBean3 implements Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@Id
	private String idx;

	/** 设备主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

	/** 巡检计划名称 */
	@Column(name = "ROUTE_NAME")
	private String routeName;

	/** 计划开始日期 */
	@Column(name = "PLAN_START_DATE")
	@Temporal(TemporalType.DATE)
	private java.util.Date planStartDate;

	/** 计划结束日期 */
	@Column(name = "PLAN_END_DATE")
	@Temporal(TemporalType.DATE)
	private java.util.Date planEndDate;

	/** 实际开工时间 */
	@Column(name = "REAL_BEGIN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date realBeginTime;

	/** 实际完工时间 */
	@Column(name = "REAL_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date realEndTime;

	/** 使用人 */
	@Column(name = "USE_WORKER")
	private String useWorker;

	/** 使用人ID */
	@Column(name = "USE_WORKER_ID")
	private Long useWorkerId;

	/** 巡检结果（已巡检、未巡检） */
	@Column(name = "CHECK_RESULT")
	private String checkResult;

	/** 巡检情况描述 */
	@Column(name = "CHECK_RESULT_DESC")
	private String checkResultDesc;

	/** 实际机械巡检人名称 */
	@Column(name = "MAC_INSPECT_EMP")
	@IgnoreOrder
	private String macInspectEmp;

	/** 实际电气巡检人名称 */
	@Column(name = "ELC_INSPECT_EMP")
	@IgnoreOrder
	private String elcInspectEmp;

	/** 未处理的巡检记录数 */
	@Column(name = "wcl_count")
	private Integer wclCount;

	/** 已处理的巡检记录数 */
	@Column(name = "ycl_count")
	private Integer yclCount;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getEquipmentIdx() {
		return equipmentIdx;
	}

	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public java.util.Date getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(java.util.Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	public java.util.Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(java.util.Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	public java.util.Date getRealBeginTime() {
		return realBeginTime;
	}

	public void setRealBeginTime(java.util.Date realBeginTime) {
		this.realBeginTime = realBeginTime;
	}

	public java.util.Date getRealEndTime() {
		return realEndTime;
	}

	public void setRealEndTime(java.util.Date realEndTime) {
		this.realEndTime = realEndTime;
	}

	public String getUseWorker() {
		return useWorker;
	}

	public void setUseWorker(String useWorker) {
		this.useWorker = useWorker;
	}

	public Long getUseWorkerId() {
		return useWorkerId;
	}

	public void setUseWorkerId(Long useWorkerId) {
		this.useWorkerId = useWorkerId;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getCheckResultDesc() {
		return checkResultDesc;
	}

	public void setCheckResultDesc(String checkResultDesc) {
		this.checkResultDesc = checkResultDesc;
	}

	public String getMacInspectEmp() {
		return macInspectEmp;
	}

	public void setMacInspectEmp(String macInspectEmp) {
		this.macInspectEmp = macInspectEmp;
	}

	public String getElcInspectEmp() {
		return elcInspectEmp;
	}

	public void setElcInspectEmp(String elcInspectEmp) {
		this.elcInspectEmp = elcInspectEmp;
	}

	public Integer getWclCount() {
		return wclCount;
	}

	public void setWclCount(Integer wclCount) {
		this.wclCount = wclCount;
	}

	public Integer getYclCount() {
		return yclCount;
	}

	public void setYclCount(Integer yclCount) {
		this.yclCount = yclCount;
	}

}