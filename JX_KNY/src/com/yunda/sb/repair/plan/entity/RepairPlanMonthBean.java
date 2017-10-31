package com.yunda.sb.repair.plan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairPlanYear，数据表：设备检修年计划查询封装实体
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
public class RepairPlanMonthBean implements Serializable {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	private String idx;

	/** 设备主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

	/** 计划年度 */
	@Column(name = "plan_year")
	private Integer planYear;

	/** 计划月份 */
	@Column(name = "plan_month")
	private Integer planMonth;

	/** 修程，1:小修、2：中修、3：项修  */
	@Column(name = "repair_class")
	private Short repairClass;

	/** 计划开始时间 */
	@Column(name = "BEGIN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date beginTime;

	/** 计划结束时间 */
	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date endTime;

	/** 计划状态，0：未下发、1：已下发 */
	@Column(name = "PLAN_STATUS")
	private Integer planStatus;

	/* 设备类别编码 */
	@Column(name = "class_code")
	private String classCode;

	/* 设备类别名称*/
	@Column(name = "class_name")
	private String className;

	/* 型号 */
	private String model;

	/* 规格 */
	private String specification;

	/* 设置地点(来源码表) */
	@Column(name = "use_place")
	private String usePlace;

	/* 机械系数 */
	@Column(name = "mechanical_coefficient")
	private Integer mechanicalCoefficient;

	/* 电气系数 */
	@Column(name = "electric_coefficient")
	private Integer electricCoefficient;

	/* 机械维修班组 */
	@Column(name = "MECHANICAL_REPAIR_TEAM")
	private String mechanicalRepairTeam;

	/* 电气维修班组 */
	@Column(name = "ELECTRIC_REPAIR_TEAM")
	private String electricRepairTeam;

	/* 设备名称 */
	@Column(name = "equipment_name")
	private String equipmentName;

	/* 设备编码 */
	@Column(name = "equipment_code")
	private String equipmentCode;

	/* 单位名称 */
	@Column(name = "org_name")
	private String orgName;

	/* 单位id */
	@Column(name = "org_id")
	private String orgId;

	/* 数据状态 */
	@Column(name = "RECORD_STATUS")
	private Integer recordStatus;

	/* 创建人 */
	@Column(name = "CREATOR", updatable = false)
	private Long creator;

	/* 创建时间 */
	@Column(name = "CREATE_TIME", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date createTime;

	/* 修改人 */
	@Column(name = "UPDATOR")
	private Long updator;

	/* 修改时间 */
	@Column(name = "UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date updateTime;

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

	public Short getRepairClass() {
		return repairClass;
	}

	public void setRepairClass(Short repairClass) {
		this.repairClass = repairClass;
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

	public Integer getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(Integer planStatus) {
		this.planStatus = planStatus;
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getUsePlace() {
		return usePlace;
	}

	public void setUsePlace(String usePlace) {
		this.usePlace = usePlace;
	}

	public String getElectricRepairTeam() {
		return electricRepairTeam;
	}

	public void setElectricRepairTeam(String electricRepairTeam) {
		this.electricRepairTeam = electricRepairTeam;
	}

	public String getMechanicalRepairTeam() {
		return mechanicalRepairTeam;
	}

	public void setMechanicalRepairTeam(String mechanicalRepairTeam) {
		this.mechanicalRepairTeam = mechanicalRepairTeam;
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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getMechanicalCoefficient() {
		return mechanicalCoefficient;
	}

	public void setMechanicalCoefficient(Integer mechanicalCoefficient) {
		this.mechanicalCoefficient = mechanicalCoefficient;
	}

	public Integer getElectricCoefficient() {
		return electricCoefficient;
	}

	public void setElectricCoefficient(Integer electricCoefficient) {
		this.electricCoefficient = electricCoefficient;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdator() {
		return updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

}
