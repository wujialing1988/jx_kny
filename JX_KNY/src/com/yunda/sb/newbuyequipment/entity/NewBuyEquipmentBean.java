package com.yunda.sb.newbuyequipment.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明： 查询封装
 * <li>创建人：黄杨
 * <li>创建日期：2017-4-26
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewBuyEquipmentBean {
	/** 主键 */
	@Id
	private String idx;

	/** 主设备主键 */
	@Column(name = "equipment_idx")
	private String equipmentIdx;

	/** 备注 */
	private String remark;

	/** 经办人 */
	@Column(name = "responsible_person")
	private String responsiblePerson;

	/** 经办人编号 */
	@Column(name = "responsible_person_id")
	private String responsiblePersonId;

	/** 批准单位 */
	@Column(name = "ratify_orgname")
	private String ratifyOrgname;

	/** 批准单位id */
	@Column(name = "ratify_orgid")
	private String ratifyOrgid;

	/** 所属单位 */
	@Column(name = "affiliated_orgname")
	private String affiliatedOrgname;

	/** 所属单位id */
	@Column(name = "AFFILIATED_ORGID")
	private String affiliatedOrgid;

	/** 新购批号 */
	@Column(name = "buy_batch_num")
	private String buyBatchNum;

	/** 新购日期 */
	@Temporal(TemporalType.DATE)
	@Column(name = "buy_date")
	private Date buyDate;

	/** 数据状态 */
	@Column(name = "record_status")
	private Integer recordStatus;

	/** 创建人 */
	private Long creator;

	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createTime;

	/** 修改人 */
	private Long updator;

	/** 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private Date updateTime;

	/** 以下字段来源于“设备主要信息”，用于关联查询 */
	/* 单位名称 */
	@Column(name = "org_name")
	private String orgName;

	/* 单位ID */
	@Column(name = "org_id")
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

	/* 机械系数 */
	@Column(name = "mechanical_coefficient")
	private Float mechanicalCoefficient;

	/* 电气系数 */
	@Column(name = "electric_coefficient")
	private Float electricCoefficient;

	/* 制造工厂 */
	@Column(name = "make_factory")
	private String makeFactory;

	/* 制造年月 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "make_date")
	private java.util.Date makeDate;

	/* 固资原值 */
	@Column(name = "fixed_asset_value")
	private Float fixedAssetValue;

	/* 使用年月 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "use_date")
	private java.util.Date useDate;

	/* 设置地点(来源码表) */
	@Column(name = "use_place")
	private String usePlace;

	/* 管理级别 */
	@Column(name = "manage_level")
	private String manageLevel;

	/* 管理类别（A、B、C） */
	@Column(name = "manage_class")
	private String manageClass;

	/* 重量 */
	private Float weight;

	/* 最大修年度 */
	@Column(name = "max_repair_year")
	private Integer maxRepairYear;

	/* 出厂编号 */
	@Column(name = "leave_factory_no")
	private String leaveFactoryNo;

	/* 电气总功率 */
	@Column(name = "eletric_total_power")
	private String eletricTotalPower;

	/* 技术等级 */
	@Column(name = "tec_level")
	private String tecLevel;

	/* 技术状态 */
	@Column(name = "tec_status")
	private String tecstatus;

	/* 外形尺寸 */
	@Column(name = "shape_size")
	private String shapeSize;

	/* 是否主设备 */
	@Column(name = "is_primary_device")
	private Integer isPrimaryDevice;

	/* 是否专用设备 */
	@Column(name = "is_dedicated")
	private Integer isDedicated;

	/* 是否特种设备 */
	@Column(name = "is_special_type")
	private Integer isSpecialType;

	/* 是否大精设备 */
	@Column(name = "is_exactness")
	private Integer isExactness;

	/* 是否工装设备 */
	@Column(name = "is_frock")
	private Integer isFrock;

	/* 设备动态(来源码表) */
	private Integer dynamic;

	/* 封存状态(来源码表) */
	@Column(name = "fc_state", updatable = false)
	private Integer fcState;

	/* 闲置状态(来源码表) */
	@Column(name = "xz_state", updatable = false)
	private Integer xzState;

	/* 出租状态(来源码表) */
	@Column(name = "cz_state", updatable = false)
	private Integer czState;

	/* 设备运行班制 */
	@Column(name = "runing_shifts")
	private Integer runingShifts;

	/*------设备两定三包临时字段----------*/
	/* 使用车间 */
	@Column(name = "USE_WORKSHOP_ID")
	private String useWorkshopId;

	@Column(name = "USE_WORKSHOP")
	private String useWorkshop;

	/* 使用人 */
	@Column(name = "USE_PERSON_ID")
	private String usePersonId;

	@Column(name = "USE_PERSON")
	private String usePerson;

	/* 机械维修人 */
	@Column(name = "MECHANICAL_REPAIR_PERSON_ID")
	private String mechanicalRepairPersonId;

	@Column(name = "MECHANICAL_REPAIR_PERSON")
	private String mechanicalRepairPerson;

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

	/* 机械维修班组 */
	@Column(name = "MECHANICAL_REPAIR_TEAM_ID")
	private String mechanicalRepairTeamId;

	@Column(name = "MECHANICAL_REPAIR_TEAM")
	private String mechanicalRepairTeam;

	/* 查询条件 - 新购开始日期 */
	@Transient
	private Date startDate;

	/* 查询条件 - 新购结束日期 */
	@Transient
	private Date endDate;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public String getResponsiblePersonId() {
		return responsiblePersonId;
	}

	public void setResponsiblePersonId(String responsiblePersonId) {
		this.responsiblePersonId = responsiblePersonId;
	}

	public String getRatifyOrgname() {
		return ratifyOrgname;
	}

	public void setRatifyOrgname(String ratifyOrgname) {
		this.ratifyOrgname = ratifyOrgname;
	}

	public String getRatifyOrgid() {
		return ratifyOrgid;
	}

	public void setRatifyOrgid(String ratifyOrgid) {
		this.ratifyOrgid = ratifyOrgid;
	}

	public String getAffiliatedOrgname() {
		return affiliatedOrgname;
	}

	public void setAffiliatedOrgname(String affiliatedOrgname) {
		this.affiliatedOrgname = affiliatedOrgname;
	}

	public String getAffiliatedOrgid() {
		return affiliatedOrgid;
	}

	public void setAffiliatedOrgid(String affiliatedOrgid) {
		this.affiliatedOrgid = affiliatedOrgid;
	}

	public String getBuyBatchNum() {
		return buyBatchNum;
	}

	public void setBuyBatchNum(String buyBatchNum) {
		this.buyBatchNum = buyBatchNum;
	}

	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdator() {
		return updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public String getMakeFactory() {
		return makeFactory;
	}

	public void setMakeFactory(String makeFactory) {
		this.makeFactory = makeFactory;
	}

	public java.util.Date getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(java.util.Date makeDate) {
		this.makeDate = makeDate;
	}

	public Float getFixedAssetValue() {
		return fixedAssetValue;
	}

	public void setFixedAssetValue(Float fixedAssetValue) {
		this.fixedAssetValue = fixedAssetValue;
	}

	public java.util.Date getUseDate() {
		return useDate;
	}

	public void setUseDate(java.util.Date useDate) {
		this.useDate = useDate;
	}

	public String getUsePlace() {
		return usePlace;
	}

	public void setUsePlace(String usePlace) {
		this.usePlace = usePlace;
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

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Integer getMaxRepairYear() {
		return maxRepairYear;
	}

	public void setMaxRepairYear(Integer maxRepairYear) {
		this.maxRepairYear = maxRepairYear;
	}

	public String getLeaveFactoryNo() {
		return leaveFactoryNo;
	}

	public void setLeaveFactoryNo(String leaveFactoryNo) {
		this.leaveFactoryNo = leaveFactoryNo;
	}

	public String getEletricTotalPower() {
		return eletricTotalPower;
	}

	public void setEletricTotalPower(String eletricTotalPower) {
		this.eletricTotalPower = eletricTotalPower;
	}

	public String getTecLevel() {
		return tecLevel;
	}

	public void setTecLevel(String tecLevel) {
		this.tecLevel = tecLevel;
	}

	public String getTecstatus() {
		return tecstatus;
	}

	public void setTecstatus(String tecstatus) {
		this.tecstatus = tecstatus;
	}

	public String getShapeSize() {
		return shapeSize;
	}

	public void setShapeSize(String shapeSize) {
		this.shapeSize = shapeSize;
	}

	public Integer getIsPrimaryDevice() {
		return isPrimaryDevice;
	}

	public void setIsPrimaryDevice(Integer isPrimaryDevice) {
		this.isPrimaryDevice = isPrimaryDevice;
	}

	public Integer getIsDedicated() {
		return isDedicated;
	}

	public void setIsDedicated(Integer isDedicated) {
		this.isDedicated = isDedicated;
	}

	public Integer getIsSpecialType() {
		return isSpecialType;
	}

	public void setIsSpecialType(Integer isSpecialType) {
		this.isSpecialType = isSpecialType;
	}

	public Integer getIsExactness() {
		return isExactness;
	}

	public void setIsExactness(Integer isExactness) {
		this.isExactness = isExactness;
	}

	public Integer getIsFrock() {
		return isFrock;
	}

	public void setIsFrock(Integer isFrock) {
		this.isFrock = isFrock;
	}

	public Integer getDynamic() {
		return dynamic;
	}

	public void setDynamic(Integer dynamic) {
		this.dynamic = dynamic;
	}

	public Integer getFcState() {
		return fcState;
	}

	public void setFcState(Integer fcState) {
		this.fcState = fcState;
	}

	public Integer getXzState() {
		return xzState;
	}

	public void setXzState(Integer xzState) {
		this.xzState = xzState;
	}

	public Integer getCzState() {
		return czState;
	}

	public void setCzState(Integer czState) {
		this.czState = czState;
	}

	public Integer getRuningShifts() {
		return runingShifts;
	}

	public void setRuningShifts(Integer runingShifts) {
		this.runingShifts = runingShifts;
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

}
