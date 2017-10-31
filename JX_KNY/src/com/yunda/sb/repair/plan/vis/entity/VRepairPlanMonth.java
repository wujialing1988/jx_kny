package com.yunda.sb.repair.plan.vis.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: VEquipmentPlan，数据表：设备检修月计划编制VIS方式 - VIS分组对象
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
public class VRepairPlanMonth implements Serializable {

	/** 主键，设备idx主键 */
	@Id
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;
	/** 设备名称 */
	@Column(name = "EQUIPMENT_NAME")
	private String equipmentName;
	/** 设备名称 */
	@Column(name = "EQUIPMENT_CODE")
	private String equipmentCode;
	/** 设备类别 */
	@Column(name = "CLASS_CODE")
	private String classCode;
	/** 设备类别名称 */
	@Column(name = "CLASS_NAME")
	private String className;
	/** 修次（小、中、项修的总次数） */
	@Column(name = "REPAIR_COUNT")
	private Integer repairCount;
	/** 检修计划明细 */
	@Transient
	List<VRepairPlanMonthItems> items;
	
    /* 计划年度 */
	@Transient
    private Integer planYear;
    /* 计划月份 */
	@Transient
    private Integer planMonth;
	/* 设备管辖单位序列（orgSeq） */
	@Transient
	private String orgId;
	
	/** 查询条件 - VIS时间轴开始时间 */
	@Transient
	private Date startTime;
	/** 查询条件 - VIS时间轴结束时间 */
	@Transient
	private Date endTime;

	public String getEquipmentIdx() {
		return equipmentIdx;
	}

	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
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

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getRepairCount() {
		return repairCount;
	}

	public void setRepairCount(Integer repairCount) {
		this.repairCount = repairCount;
	}

	public List<VRepairPlanMonthItems> getItems() {
		return items;
	}

	public void setItems(List<VRepairPlanMonthItems> items) {
		this.items = items;
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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
