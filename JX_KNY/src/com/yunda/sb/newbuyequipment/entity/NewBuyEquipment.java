package com.yunda.sb.newbuyequipment.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：新购设备实体类
 * <li>创建人：黄杨
 * <li>创建日期：2017-4-26
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "E_NEW_BUY_EQUIPMENT")
public class NewBuyEquipment implements java.io.Serializable {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/* 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/* 主设备主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

	/* 电气维修班组ID */
	@Transient
	private String electricRepairTeamId;

	/* 电气维修班组 */
	@Transient
	private String electricRepairTeam;

	/* 电气维修人 */
	@Transient
	private String electricRepairPerson;

	/* 电气维修人编号 */
	@Transient
	private String electricRepairPersonId;

	/* 机械维修班组ID */
	@Transient
	private String mechanicalRepairTeamId;

	/* 机械维修班组 */
	@Transient
	private String mechanicalRepairTeam;

	/* 机械维修人 */
	@Transient
	private String mechanicalRepairPerson;

	/* 机械维修人编号 */
	@Transient
	private String mechanicalRepairPersonId;

	/* 经办人 */
	@Column(name = "RESPONSIBLE_PERSON")
	private String responsiblePerson;

	/* 经办人编号 */
	@Column(name = "RESPONSIBLE_PERSON_ID")
	private String responsiblePersonId;

	/* 批准单位 */
	@Column(name = "RATIFY_ORGNAME")
	private String ratifyOrgname;

	/* 批准单位ID */
	@Column(name = "RATIFY_ORGID")
	private String ratifyOrgid;

	/* 使用车间 */
	@Transient
	private String useWorkshop;

	/* 使用车间ID */
	@Transient
	private String useWorkshopId;

	/* 使用年月 */
	@Transient
	private Date useDate;

	/* 使用人 */
	@Transient
	private String usePerson;

	/* 使用人ID */
	@Transient
	private String usePersonId;

	/* 所属单位 */
	@Column(name = "AFFILIATED_ORGNAME")
	private String affiliatedOrgname;

	/* 所属单位ID */
	@Column(name = "AFFILIATED_ORGID")
	private String affiliatedOrgid;

	/* 新购批号 */
	@Column(name = "BUY_BATCH_NUM")
	private String buyBatchNum;

	/* 新购日期 */
	@Column(name = "BUY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date buyDate;

	/* 备注 */
	@Column(name = "REMARK")
	private String remark;

	/* 数据状态 */
	@Column(name = "RECORD_STATUS")
	private Integer recordStatus;

	/* 创建人 */
	@Column(updatable = false)
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

	/* 设备类别编码 */
	@Transient
	private String classCode;

	@Transient
	private String className;

	/* 设备名称 */
	@Transient
	private String equipmentName;

	/* 设备编码 */
	@Transient
	private String equipmentCode;

	/* 固资编号 */
	@Transient
	private String fixedAssetNo;

	/* 型号 */
	@Transient
	private String model;

	/* 规格 */
	@Transient
	private String specification;

	/* 机械系数 */
	@Transient
	private Float mechanicalCoefficient;

	/* 电气系数 */
	@Transient
	private Float electricCoefficient;

	/* 制造工厂 */
	@Transient
	private String makeFactory;

	/* 制造年月 */
	@Transient
	private java.util.Date makeDate;

	/* 固资原值 */
	@Transient
	private Float fixedAssetValue;

	/* 设置地点(来源码表) */
	@Transient
	private String usePlace;

	/* 管理级别 */
	@Transient
	private String manageLevel;

	/* 管理类别 */
	@Transient
	private String manageClass;

	/* 重量 */
	@Transient
	private Float weight;

	/* 最大修年度 */
	@Transient
	private Integer maxRepairYear;

	/* 出厂编号 */
	@Transient
	private String leaveFactoryNo;

	/* 电气总功率 */
	@Transient
	private String eletricTotalPower;

	/* 外形尺寸 */
	@Transient
	private String shapeSize;

	/* 是否专用设备 */
	@Transient
	private Integer isDedicated;

	/* 是否特种设备 */
	@Transient
	private Integer isSpecialType;

	/* 是否大精设备 */
	@Transient
	private Integer isExactness;

	/* 设备运行班制 */
	@Transient
	private Integer runingShifts;

	/* 外型尺寸（主设备） */
	@Transient
	private String orgName;

	@Transient
	private String orgId;

	/*
	 * 是否主设备
	 */
	@Transient
	private Integer isPrimaryDevice;

	/*
	 * 是否工装设备
	 */
	@Transient
	private Integer isFrock;

	/*
	 * 动态
	 */
	@Transient
	private Integer dynamic;

	/*
	 * 封存状态
	 */
	@Transient
	private Integer fcState;

	/*
	 * 闲置状态
	 */
	@Transient
	private Integer xzState;

	/*
	 * 出租状态
	 */
	@Transient
	private Integer czState;

	/* 技术等级 */
	@Transient
	private String tecLevel;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	/**
	 * @return String 获取创建人
	 */
	public Long getCreator() {
		return this.creator;
	}

	/**
	 * @param creator
	 *            设置创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}

	/**
	 * @return String 获取创建时间
	 */
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * @param createTime
	 *            设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return String 获取修改人
	 */
	public Long getUpdator() {
		return this.updator;
	}

	/**
	 * @param updator
	 *            设置修改人
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	/**
	 * @return String 获取修改时间
	 */
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	/**
	 * @param updateTime
	 *            设置修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return String 获取主设备主键
	 */
	public String getEquipmentIdx() {
		return this.equipmentIdx;
	}

	/**
	 * @param equipmentIdx
	 *            设置主设备主键
	 */
	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
	}

	/**
	 * @return String 获取电气维修班组ID
	 */
	public String getElectricRepairTeamId() {
		return this.electricRepairTeamId;
	}

	/**
	 * @param electricRepairTeamId
	 *            设置电气维修班组ID
	 */
	public void setElectricRepairTeamId(String electricRepairTeamId) {
		this.electricRepairTeamId = electricRepairTeamId;
	}

	/**
	 * @return String 获取电气维修班组
	 */
	public String getElectricRepairTeam() {
		return this.electricRepairTeam;
	}

	/**
	 * @param electricRepairTeam
	 *            设置电气维修班组
	 */
	public void setElectricRepairTeam(String electricRepairTeam) {
		this.electricRepairTeam = electricRepairTeam;
	}

	/**
	 * @return String 获取电气维修人
	 */
	public String getElectricRepairPerson() {
		return this.electricRepairPerson;
	}

	/**
	 * @param electricRepairPerson
	 *            设置电气维修人
	 */
	public void setElectricRepairPerson(String electricRepairPerson) {
		this.electricRepairPerson = electricRepairPerson;
	}

	/**
	 * @return String 获取电气维修人编号
	 */
	public String getElectricRepairPersonId() {
		return this.electricRepairPersonId;
	}

	/**
	 * @param electricRepairPersonId
	 *            设置电气维修人编号
	 */
	public void setElectricRepairPersonId(String electricRepairPersonId) {
		this.electricRepairPersonId = electricRepairPersonId;
	}

	/**
	 * @return String 获取备注
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * @param remark
	 *            设置备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return String 获取机械维修班组ID
	 */
	public String getMechanicalRepairTeamId() {
		return this.mechanicalRepairTeamId;
	}

	/**
	 * @param mechanicalRepairTeamId
	 *            设置机械维修班组ID
	 */
	public void setMechanicalRepairTeamId(String mechanicalRepairTeamId) {
		this.mechanicalRepairTeamId = mechanicalRepairTeamId;
	}

	/**
	 * @return String 获取机械维修班组
	 */
	public String getMechanicalRepairTeam() {
		return this.mechanicalRepairTeam;
	}

	/**
	 * @param mechanicalRepairTeam
	 *            设置机械维修班组
	 */
	public void setMechanicalRepairTeam(String mechanicalRepairTeam) {
		this.mechanicalRepairTeam = mechanicalRepairTeam;
	}

	/**
	 * @return String 获取机械维修人
	 */
	public String getMechanicalRepairPerson() {
		return this.mechanicalRepairPerson;
	}

	/**
	 * @param mechanicalRepairPerson
	 *            设置机械维修人
	 */
	public void setMechanicalRepairPerson(String mechanicalRepairPerson) {
		this.mechanicalRepairPerson = mechanicalRepairPerson;
	}

	/**
	 * @return String 获取机械维修人编号
	 */
	public String getMechanicalRepairPersonId() {
		return this.mechanicalRepairPersonId;
	}

	/**
	 * @param mechanicalRepairPersonId
	 *            设置机械维修人编号
	 */
	public void setMechanicalRepairPersonId(String mechanicalRepairPersonId) {
		this.mechanicalRepairPersonId = mechanicalRepairPersonId;
	}

	/**
	 * @return String 获取经办人
	 */
	public String getResponsiblePerson() {
		return this.responsiblePerson;
	}

	/**
	 * @param responsiblePerson
	 *            设置经办人
	 */
	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	/**
	 * @return String 获取经办人编号
	 */
	public String getResponsiblePersonId() {
		return this.responsiblePersonId;
	}

	/**
	 * @param responsiblePersonId
	 *            设置经办人编号
	 */
	public void setResponsiblePersonId(String responsiblePersonId) {
		this.responsiblePersonId = responsiblePersonId;
	}

	/**
	 * @return String 获取批准单位
	 */
	public String getRatifyOrgname() {
		return this.ratifyOrgname;
	}

	/**
	 * @param ratifyOrgname
	 *            设置批准单位
	 */
	public void setRatifyOrgname(String ratifyOrgname) {
		this.ratifyOrgname = ratifyOrgname;
	}

	/**
	 * @return String 获取批准单位ID
	 */
	public String getRatifyOrgid() {
		return this.ratifyOrgid;
	}

	/**
	 * @param ratifyOrgid
	 *            设置批准单位ID
	 */
	public void setRatifyOrgid(String ratifyOrgid) {
		this.ratifyOrgid = ratifyOrgid;
	}

	/**
	 * @return String 获取使用车间
	 */
	public String getUseWorkshop() {
		return this.useWorkshop;
	}

	/**
	 * @param useWorkshop
	 *            设置使用车间
	 */
	public void setUseWorkshop(String useWorkshop) {
		this.useWorkshop = useWorkshop;
	}

	/**
	 * @return String 获取使用车间ID
	 */
	public String getUseWorkshopId() {
		return this.useWorkshopId;
	}

	/**
	 * @param useWorkshopId
	 *            设置使用车间ID
	 */
	public void setUseWorkshopId(String useWorkshopId) {
		this.useWorkshopId = useWorkshopId;
	}

	/**
	 * @return String 获取使用年月
	 */
	public Date getUseDate() {
		return this.useDate;
	}

	/**
	 * @param useYear
	 *            设置使用年月
	 */
	public void setUseDate(Date useYear) {
		this.useDate = useYear;
	}

	/**
	 * @return String 获取使用人
	 */
	public String getUsePerson() {
		return this.usePerson;
	}

	/**
	 * @param user
	 *            设置使用人
	 */
	public void setUsePerson(String user) {
		this.usePerson = user;
	}

	/**
	 * @return String 获取使用人编号
	 */
	public String getUsePersonId() {
		return this.usePersonId;
	}

	/**
	 * @param userId
	 *            设置使用人编号
	 */
	public void setUsePersonId(String userId) {
		this.usePersonId = userId;
	}

	/**
	 * @return String 获取所属单位
	 */
	public String getAffiliatedOrgname() {
		return this.affiliatedOrgname;
	}

	/**
	 * @param affiliatedOrgname
	 *            设置所属单位
	 */
	public void setAffiliatedOrgname(String affiliatedOrgname) {
		this.affiliatedOrgname = affiliatedOrgname;
	}

	/**
	 * @return String 获取所属单位ID
	 */
	public String getAffiliatedOrgid() {
		return this.affiliatedOrgid;
	}

	/**
	 * @param affiliatedOrgid
	 *            设置所属单位ID
	 */
	public void setAffiliatedOrgid(String affiliatedOrgid) {
		this.affiliatedOrgid = affiliatedOrgid;
	}

	/**
	 * @return String 获取新购批号
	 */
	public String getBuyBatchNum() {
		return this.buyBatchNum;
	}

	/**
	 * @param buyBatchNum
	 *            设置新购批号
	 */
	public void setBuyBatchNum(String buyBatchNum) {
		this.buyBatchNum = buyBatchNum;
	}

	/**
	 * @return String 获取新购日期
	 */
	public java.util.Date getBuyDate() {
		return this.buyDate;
	}

	/**
	 * @param buyDate
	 *            设置新购日期
	 */
	public void setBuyDate(java.util.Date buyDate) {
		this.buyDate = buyDate;
	}

	/**
	 * @return String 获取数据状态
	 */
	public Integer getRecordStatus() {
		return this.recordStatus;
	}

	/**
	 * @param recordStatus
	 *            设置数据状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
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

	public void setModel(String modal) {
		this.model = modal;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getMakeFactory() {
		return makeFactory;
	}

	public void setMakeFactory(String makeFactory) {
		this.makeFactory = makeFactory;
	}

	public Integer getRuningShifts() {
		return runingShifts;
	}

	public void setRuningShifts(Integer runingShifts) {
		this.runingShifts = runingShifts;
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

	public void setClassName(String classCodeName) {
		this.className = classCodeName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getShapeSize() {
		return shapeSize;
	}

	public void setShapeSize(String shapeSize) {
		this.shapeSize = shapeSize;
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

	/**
	 * @return Integer 获取是否工装设备
	 */
	public Integer getIsFrock() {
		return isFrock;
	}

	/**
	 * @param isFrock
	 *            设置是否工装设备
	 */
	public void setIsFrock(Integer isFrock) {
		this.isFrock = isFrock;
	}

	public Integer getIsPrimaryDevice() {
		return isPrimaryDevice;
	}

	public void setIsPrimaryDevice(Integer isPrimaryDevice) {
		this.isPrimaryDevice = isPrimaryDevice;
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

	public String getTecLevel() {
		return tecLevel;
	}

	public void setTecLevel(String tecLevel) {
		this.tecLevel = tecLevel;
	}
}
