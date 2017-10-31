package com.yunda.sb.pointcheck.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：PointCheckBean，数据表：设备点检任务单查询封装实体
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
public class PointCheckBean implements Serializable {

	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	private String idx;

	/** 设备idx主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

	/** 点检人 */
	@Column(name = "CHECK_EMP_ID")
	private String checkEmpId;

	/** 点检人名称 */
	@Column(name = "CHECK_EMP")
	private String checkEmp;

	/** 点检日期 */
	@Column(name = "CHECK_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkDate;

	/** 点检时间，该字段用于计算“设备运转时间”的临时字段 */
	@Column(name = "CHECK_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkTime;

	/** 设备运转时间，单位：小时，运转时间 = 点检结束时间 - 点检开始时间 */
	@Column(name = "RUNNING_TIME")
	private Float runningTime;

	/** 计算设备运转时间 */
	@Column(name = "cal_running_time")
	private Float calRunningTime;

	/** 处理状态 */
	@Column(name = "state")
	private String state;

	/** 设备运行状态状态 */
	@Column(name = "EQUIPMENT_STATE")
	private String equipmentState;

	/* 单位名称 */
	@Column(name = "ORG_NAME")
	private String orgName;

	/* 单位ID */
	@Column(name = "ORG_ID")
	private String orgId;

	/* 设备类别编码 */
	@Column(name = "class_code")
	private String classCode;

	/* 设备类别名称 */
	@Column(name = "class_name")
	private String className;

	/* 设备名称 */
	@Column(name = "equipment_name")
	private String equipmentName;

	/* 设备编码 */
	@Column(name = "equipment_code")
	private String equipmentCode;

	/* 固资编号 */
	@Column(name = "fixed_asset_no")
	private String fixedAssetNo;

	/* 型号 */
	private String model;

	/* 规格 */
	private String specification;

	/* 设备运行班制 */
	@Column(name = "runing_shifts")
	private Integer runingShifts;

	@Column(name = "manage_level")
	private String manageLevel;

	/* 管理类别（A、B、C） */
	@Column(name = "manage_class")
	private String manageClass;

	/* 电气维修人 */
	@Column(name = "ELECTRIC_REPAIR_PERSON_ID")
	private String electricRepairPersonId;

	@Column(name = "ELECTRIC_REPAIR_PERSON")
	private String electricRepairPerson;

	/* 电气维修班组 */
	@Column(name = "ELECTRIC_REPAIR_TEAM_ID")
	private String electricRepairTeamId;

	@Column(name = "ELECTRIC_REPAIR_TEAM")
	private String electricRepairTeam;

	/* 机械维修人 */
	@Column(name = "MECHANICAL_REPAIR_PERSON_ID")
	private String mechanicalRepairPersonId;

	@Column(name = "MECHANICAL_REPAIR_PERSON")
	private String mechanicalRepairPerson;

	/* 机械维修班组 */
	@Column(name = "MECHANICAL_REPAIR_TEAM_ID")
	private String mechanicalRepairTeamId;

	@Column(name = "MECHANICAL_REPAIR_TEAM")
	private String mechanicalRepairTeam;

	/* 设置地点(来源码表) */
	@Column(name = "use_place")
	private String usePlace;

	@Column(name = "USE_WORKSHOP_ID")
	private String useWorkshopId;

	@Column(name = "USE_WORKSHOP")
	private String useWorkshop;

	/* 使用人 */
	@Column(name = "USE_PERSON_ID")
	private String usePersonId;

	@Column(name = "USE_PERSON")
	private String usePerson;

	/* 机械系数 */
	@Column(name = "MECHANICAL_COEFFICIENT")
	private Float mechanicalCoefficient;

	/* 电气系数 */
	@Column(name = "ELECTRIC_COEFFICIENT")
	private Float electricCoefficient;

	/** 查询条件 - 年 */
	@Transient
	private Short year;

	/** 查询条件 - 月 */
	@Transient
	private Short month;

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

	public String getCheckEmpId() {
		return checkEmpId;
	}

	public void setCheckEmpId(String checkEmpId) {
		this.checkEmpId = checkEmpId;
	}

	public String getCheckEmp() {
		return checkEmp;
	}

	public void setCheckEmp(String checkEmp) {
		this.checkEmp = checkEmp;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public Float getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(Float runningTime) {
		this.runningTime = runningTime;
	}

	public Float getCalRunningTime() {
		return calRunningTime;
	}

	public void setCalRunningTime(Float calRunningTime) {
		this.calRunningTime = calRunningTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEquipmentState() {
		return equipmentState;
	}

	public void setEquipmentState(String equipmentState) {
		this.equipmentState = equipmentState;
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

	public String getFixedAssetNo() {
		return fixedAssetNo;
	}

	public void setFixedAssetNo(String fixedAssetNo) {
		this.fixedAssetNo = fixedAssetNo;
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

	public Integer getRuningShifts() {
		return runingShifts;
	}

	public void setRuningShifts(Integer runingShifts) {
		this.runingShifts = runingShifts;
	}

	public String getManageLevel() {
		return manageLevel;
	}

	public void setManageLevel(String manageLevel) {
		this.manageLevel = manageLevel;
	}

	public String getManageClass() {
		return manageClass;
	}

	public void setManageClass(String manageClass) {
		this.manageClass = manageClass;
	}

	public String getElectricRepairPersonId() {
		return electricRepairPersonId;
	}

	public void setElectricRepairPersonId(String electricRepairPersonId) {
		this.electricRepairPersonId = electricRepairPersonId;
	}

	public String getElectricRepairPerson() {
		return electricRepairPerson;
	}

	public void setElectricRepairPerson(String electricRepairPerson) {
		this.electricRepairPerson = electricRepairPerson;
	}

	public String getElectricRepairTeamId() {
		return electricRepairTeamId;
	}

	public void setElectricRepairTeamId(String electricRepairTeamId) {
		this.electricRepairTeamId = electricRepairTeamId;
	}

	public String getElectricRepairTeam() {
		return electricRepairTeam;
	}

	public void setElectricRepairTeam(String electricRepairTeam) {
		this.electricRepairTeam = electricRepairTeam;
	}

	public String getMechanicalRepairPersonId() {
		return mechanicalRepairPersonId;
	}

	public void setMechanicalRepairPersonId(String mechanicalRepairPersonId) {
		this.mechanicalRepairPersonId = mechanicalRepairPersonId;
	}

	public String getMechanicalRepairPerson() {
		return mechanicalRepairPerson;
	}

	public void setMechanicalRepairPerson(String mechanicalRepairPerson) {
		this.mechanicalRepairPerson = mechanicalRepairPerson;
	}

	public String getMechanicalRepairTeamId() {
		return mechanicalRepairTeamId;
	}

	public void setMechanicalRepairTeamId(String mechanicalRepairTeamId) {
		this.mechanicalRepairTeamId = mechanicalRepairTeamId;
	}

	public String getMechanicalRepairTeam() {
		return mechanicalRepairTeam;
	}

	public void setMechanicalRepairTeam(String mechanicalRepairTeam) {
		this.mechanicalRepairTeam = mechanicalRepairTeam;
	}

	public String getUsePlace() {
		return usePlace;
	}

	public void setUsePlace(String usePlace) {
		this.usePlace = usePlace;
	}

	public String getUseWorkshopId() {
		return useWorkshopId;
	}

	public void setUseWorkshopId(String useWorkshopId) {
		this.useWorkshopId = useWorkshopId;
	}

	public String getUseWorkshop() {
		return useWorkshop;
	}

	public void setUseWorkshop(String useWorkshop) {
		this.useWorkshop = useWorkshop;
	}

	public String getUsePersonId() {
		return usePersonId;
	}

	public void setUsePersonId(String usePersonId) {
		this.usePersonId = usePersonId;
	}

	public String getUsePerson() {
		return usePerson;
	}

	public void setUsePerson(String usePerson) {
		this.usePerson = usePerson;
	}

	public Float getMechanicalCoefficient() {
		return mechanicalCoefficient;
	}

	public void setMechanicalCoefficient(Float mechanicalCoefficient) {
		this.mechanicalCoefficient = mechanicalCoefficient;
	}

	public Float getElectricCoefficient() {
		return electricCoefficient;
	}

	public void setElectricCoefficient(Float electricCoefficient) {
		this.electricCoefficient = electricCoefficient;
	}

	public Short getYear() {
		return year;
	}

	public void setYear(Short year) {
		this.year = year;
	}

	public Short getMonth() {
		return month;
	}

	public void setMonth(Short month) {
		this.month = month;
	}

}
