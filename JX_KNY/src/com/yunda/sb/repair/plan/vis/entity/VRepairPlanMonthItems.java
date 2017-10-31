package com.yunda.sb.repair.plan.vis.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: VEquipmentPlanItems，数据表：设备检修月计划编制VIS方式 - VIS分组明细
 * <li>创建人：何涛
 * <li>创建日期：2016年7月24日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@SuppressWarnings("serial")
@Entity
public class VRepairPlanMonthItems implements Serializable {
	/** 主键 */
	@Id
	private String idx;

	/** 计划年度 */
	@Column(name = "PLAN_YEAR")
	private Integer planYear;

	/** 计划月份 */
	@Column(name = "PLAN_MONTH")
	private Integer planMonth;

	/** 计划开始时间 */
	@Column(name = "BEGIN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date beginTime;

	/** 计划结束时间 */
	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date endTime;

	/** 修程，1:小修、2：中修、3：项修 */
	@Column(name = "repair_class")
	private Short repairClass;

	/** 设备名称 */
	@Column(name = "EQUIPMENT_NAME")
	private String equipmentName;

	/** 设备名称 */
	@Column(name = "EQUIPMENT_CODE")
	private String equipmentCode;

	/** 计划状态，0：未下发、1：已下发 */
	@Column(name = "PLAN_STATUS")
	private Integer planStatus;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public Integer getPlanYear() {
		return planYear;
	}

	public void setPlanYear(Integer planYear) {
		this.planYear = planYear;
	}

	public Integer getPlanMonth() {
		return planMonth;
	}

	public void setPlanMonth(Integer planMonth) {
		this.planMonth = planMonth;
	}

	public java.util.Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(java.util.Date beginTime) {
		this.beginTime = beginTime;
	}

	public java.util.Date getEndTime() {
		return endTime;
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	public Short getRepairClass() {
		return repairClass;
	}

	public void setRepairClass(Short repairClass) {
		this.repairClass = repairClass;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public Integer getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(Integer planStatus) {
		this.planStatus = planStatus;
	}

}
