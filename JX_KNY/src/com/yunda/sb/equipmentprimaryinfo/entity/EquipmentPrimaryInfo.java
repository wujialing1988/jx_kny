package com.yunda.sb.equipmentprimaryinfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：设备主要信息
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
@Table(name = "E_EQUIPMENT_PRIMARY_INFO")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentPrimaryInfo implements java.io.Serializable {

	public static enum Dynamic {

		IN("调入", 1), OUT("调出", 2), NEW_BUY("新购", 3), SCRAPPED("报废", 4), RENT(
				"出租", 5), HIRE("租用", 6);

		/** 设备动态名称 */
		private String name;

		/** 设备动态代码 */
		private int code;

		private Dynamic(String name, int code) {
			this.name = name;
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		@Override
		public String toString() {
			return String.format("动态：%s，代码：%d", this.name, this.code);
		}

	}

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 设备动态-调入【1】 */
	public static final int DYNAMIC_IN = 1;

	/** 设备动态-调出【2】 */
	public static final int DYNAMIC_OUT = 2;

	/** 设备动态-新购【3】 */
	public static final int DYNAMIC_NEW_BUY = 3;

	/** 设备动态-报废【4】 */
	public static final int DYNAMIC_SCRAPPED = 4;

	/** 设备动态-出租【5】 */
	public static final int DYNAMIC_RENT = 5;

	/** 设备动态-租用【6】 */
	public static final int DYNAMIC_HIRE = 6;

	/* 添加动态时建议更新Dynamic.js里的dynamic方法 */

	/** 设备运行班制 - 一班制 */
	public static final int RUNING_SHIFTS_1BZ = 0;

	/** 设备运行班制 - 二班制 */
	public static final int RUNING_SHIFTS_2BZ = 1;

	/** 设备运行班制 - 三班制 */
	public static final int RUNING_SHIFTS_3BZ = 2;

	/** 设备运行班制 - 四班制 */
	public static final int RUNING_SHIFTS_4BZ = 3;

	/* idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

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

	/* 购入日期 */
	@Column(name = "buy_date")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date buyDate;

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
	private Integer tecStatus;

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

	/* 备注 */
	private String remark;

	/* 数据状态 */
	@Column(name = "record_status")
	private Integer recordStatus;

	/* 创建人 */
	@Column(updatable = false)
	private Long creator;

	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", updatable = false)
	private java.util.Date createTime;

	/* 修改人 */
	private Long updator;

	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private java.util.Date updateTime;

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

	@Transient
	private String makeYear;// 制造年度

	@Transient
	private String useYear;// 使用年度

	/*
	 * 是否新购调用
	 */
	@Transient
	private boolean newBuyInvoke = false;

	/** 设备净值查询页面* */
	// 固资净值
	@Transient
	private Float netValue;

	public Float getNetValue() {
		return netValue;
	}

	public void setNetValue(Float netValue) {
		this.netValue = netValue;
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

	/**
	 * @return String 获取设备类别编码
	 */
	public String getClassCode() {
		return classCode;
	}

	/**
	 * @param equipmentClassCode
	 *            设置设备类别编码
	 */
	public void setClassCode(String equipmentClassCode) {
		this.classCode = equipmentClassCode;
	}

	/**
	 * @return String 获取设备名称
	 */
	public String getEquipmentName() {
		return equipmentName;
	}

	/**
	 * @param equipmentName
	 *            设置设备名称
	 */
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	/**
	 * @return String 获取设备编码
	 */
	public String getEquipmentCode() {
		return equipmentCode;
	}

	/**
	 * @param equipmentCode
	 *            设置设备编码
	 */
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	/**
	 * @return String 获取固资编号
	 */
	public String getFixedAssetNo() {
		return fixedAssetNo;
	}

	/**
	 * @param fixedAssetNo
	 *            设置固资编号
	 */
	public void setFixedAssetNo(String fixedAssetNo) {
		this.fixedAssetNo = fixedAssetNo;
	}

	/**
	 * @return String 获取型号
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param modal
	 *            设置型号
	 */
	public void setModel(String modal) {
		this.model = modal;
	}

	/**
	 * @return String 获取规格
	 */
	public String getSpecification() {
		return specification;
	}

	/**
	 * @param specification
	 *            设置规格
	 */
	public void setSpecification(String specification) {
		this.specification = specification;
	}

	/**
	 * @return Float 获取机械系数
	 */
	public Float getMechanicalCoefficient() {
		return mechanicalCoefficient;
	}

	/**
	 * @param mechanicalCoefficient
	 *            设置机械系数
	 */
	public void setMechanicalCoefficient(Float mechanicalCoefficient) {
		this.mechanicalCoefficient = mechanicalCoefficient;
	}

	/**
	 * @return Float 获取电气系数
	 */
	public Float getElectricCoefficient() {
		return electricCoefficient;
	}

	/**
	 * @param electricCoefficient
	 *            设置电气系数
	 */
	public void setElectricCoefficient(Float electricCoefficient) {
		this.electricCoefficient = electricCoefficient;
	}

	/**
	 * @return String 获取制造工厂
	 */
	public String getMakeFactory() {
		return makeFactory;
	}

	/**
	 * @param makeFactory
	 *            设置制造工厂
	 */
	public void setMakeFactory(String makeFactory) {
		this.makeFactory = makeFactory;
	}

	/**
	 * @return java.util.Date 获取制造年月
	 */
	public java.util.Date getMakeDate() {
		return makeDate;
	}

	/**
	 * @param makedate
	 *            设置制造年月
	 */
	public void setMakeDate(java.util.Date makedate) {
		this.makeDate = makedate;
	}

	/**
	 * @return Integer 获取固资原值
	 */
	public Float getFixedAssetValue() {
		return fixedAssetValue;
	}

	/**
	 * @param fixedAssetValue
	 *            设置固资原值
	 */
	public void setFixedAssetValue(Float fixedAssetValue) {
		this.fixedAssetValue = fixedAssetValue;
	}

	/**
	 * @return java.util.Date 获取使用年月
	 */
	public java.util.Date getUseDate() {
		return useDate;
	}

	/**
	 * @param useDate
	 *            设置使用年月
	 */
	public void setUseDate(java.util.Date useDate) {
		this.useDate = useDate;
	}

	/**
	 * @return String 获取设置地点
	 */
	public String getUsePlace() {
		return usePlace;
	}

	/**
	 * @param usePlace
	 *            设置设置地点
	 */
	public void setUsePlace(String usePlace) {
		this.usePlace = usePlace;
	}

	/**
	 * @return String 获取管理级别
	 */
	public String getManageLevel() {
		return manageLevel;
	}

	/**
	 * @param manageLevel
	 *            设置管理级别
	 */
	public void setManageLevel(String manageLevel) {
		this.manageLevel = manageLevel;
	}

	/**
	 * @return String 获取管理类别
	 */
	public String getManageClass() {
		return manageClass;
	}

	/**
	 * @param manageClass
	 *            设置管理类别
	 */
	public void setManageClass(String manageClass) {
		this.manageClass = manageClass;
	}

	/**
	 * @return Integer 获取重量
	 */
	public Float getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            设置重量
	 */
	public void setWeight(Float weight) {
		this.weight = weight;
	}

	/**
	 * @return Integer 获取最大修年度
	 */
	public Integer getMaxRepairYear() {
		return maxRepairYear;
	}

	/**
	 * @param maxRepairYear
	 *            设置最大修年度
	 */
	public void setMaxRepairYear(Integer maxRepairYear) {
		this.maxRepairYear = maxRepairYear;
	}

	/**
	 * @return String 获取出厂编号
	 */
	public String getLeaveFactoryNo() {
		return leaveFactoryNo;
	}

	/**
	 * @param leaveFactoryNo
	 *            设置出厂编号
	 */
	public void setLeaveFactoryNo(String leaveFactoryNo) {
		this.leaveFactoryNo = leaveFactoryNo;
	}

	/**
	 * @return String 获取电气总功率
	 */
	public String getEletricTotalPower() {
		return eletricTotalPower;
	}

	/**
	 * @param eletricTotalPower
	 *            设置电气总功率
	 */
	public void setEletricTotalPower(String eletricTotalPower) {
		this.eletricTotalPower = eletricTotalPower;
	}

	/**
	 * @return String 获取技术等级
	 */
	public String getTecLevel() {
		return tecLevel;
	}

	/**
	 * @param tecLevel
	 *            设置技术等级
	 */
	public void setTecLevel(String tecLevel) {
		this.tecLevel = tecLevel;
	}

	/**
	 * @return String 获取外形尺寸
	 */
	public String getShapeSize() {
		return shapeSize;
	}

	/**
	 * @param shapeSize
	 *            设置外形尺寸
	 */
	public void setShapeSize(String shapeSize) {
		this.shapeSize = shapeSize;
	}

	/**
	 * @return Integer 获取是否主设备
	 */
	public Integer getIsPrimaryDevice() {
		return isPrimaryDevice;
	}

	/**
	 * @param isprimarydevice
	 *            设置是否主设备
	 */
	public void setIsPrimaryDevice(Integer isprimarydevice) {
		this.isPrimaryDevice = isprimarydevice;
	}

	/**
	 * @return Integer 获取是否专用设备
	 */
	public Integer getIsDedicated() {
		return isDedicated;
	}

	/**
	 * @param isdedicated
	 *            设置是否专用设备
	 */
	public void setIsDedicated(Integer isdedicated) {
		this.isDedicated = isdedicated;
	}

	/**
	 * @return Integer 获取是否大精设备
	 */
	public Integer getIsExactness() {
		return isExactness;
	}

	/**
	 * @param isexactness
	 *            设置是否大精设备
	 */
	public void setIsExactness(Integer isexactness) {
		this.isExactness = isexactness;
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

	/**
	 * @return Integer 获取设备动态
	 */
	public Integer getDynamic() {
		return dynamic;
	}

	/**
	 * @param dynamic
	 *            设置设备动态
	 */
	public void setDynamic(Integer dynamic) {
		this.dynamic = dynamic;
	}

	/**
	 * @return String 获取备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            设置备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return Integer 获取数据状态
	 */
	public Integer getRecordStatus() {
		return recordStatus;
	}

	/**
	 * @param recordStatus
	 *            设置数据状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	/**
	 * @return Long 获取创建人
	 */
	public Long getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            设置创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}

	/**
	 * @return java.util.Date 获取创建时间
	 */
	public java.util.Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return Long 获取修改人
	 */
	public Long getUpdator() {
		return updator;
	}

	/**
	 * @param updator
	 *            设置修改人
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	/**
	 * @return java.util.Date 获取修改时间
	 */
	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            设置修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}

	/**
	 * @param idx
	 *            设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
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

	public String getMechanicalRepairPerson() {
		return mechanicalRepairPerson;
	}

	public void setMechanicalRepairPerson(String mechanicalRepairPeson) {
		this.mechanicalRepairPerson = mechanicalRepairPeson;
	}

	public String getMechanicalRepairPersonId() {
		return mechanicalRepairPersonId;
	}

	public void setMechanicalRepairPersonId(String mechanicalRepairPersonId) {
		this.mechanicalRepairPersonId = mechanicalRepairPersonId;
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

	public void setElectricRepairPerson(String electricRepairPeson) {
		this.electricRepairPerson = electricRepairPeson;
	}

	public String getElectricRepairTeam() {
		return electricRepairTeam;
	}

	public void setElectricRepairTeam(String electricRepairTeam) {
		this.electricRepairTeam = electricRepairTeam;
	}

	public String getElectricRepairTeamId() {
		return electricRepairTeamId;
	}

	public void setElectricRepairTeamId(String electricRepairTeamId) {
		this.electricRepairTeamId = electricRepairTeamId;
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

	public Integer getIsSpecialType() {
		return isSpecialType;
	}

	public void setIsSpecialType(Integer isSpecialType) {
		this.isSpecialType = isSpecialType;
	}

	public String getMakeYear() {
		return makeYear;
	}

	public void setMakeYear(String makeYear) {
		this.makeYear = makeYear;
	}

	public String getUseYear() {
		return useYear;
	}

	public void setUseYear(String useYear) {
		this.useYear = useYear;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getTecStatus() {
		return tecStatus;
	}

	public void setTecStatus(Integer tecStatus) {
		this.tecStatus = tecStatus;
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

	public java.util.Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(java.util.Date buyDate) {
		this.buyDate = buyDate;
	}

	public boolean isNewBuyInvoke() {
		return newBuyInvoke;
	}

	public void setNewBuyInvoke(boolean newBuyInvoke) {
		this.newBuyInvoke = newBuyInvoke;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buyDate == null) ? 0 : buyDate.hashCode());
		result = prime * result
				+ ((classCode == null) ? 0 : classCode.hashCode());
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((czState == null) ? 0 : czState.hashCode());
		result = prime * result + ((dynamic == null) ? 0 : dynamic.hashCode());
		result = prime
				* result
				+ ((electricCoefficient == null) ? 0 : electricCoefficient
						.hashCode());
		result = prime
				* result
				+ ((electricRepairPerson == null) ? 0 : electricRepairPerson
						.hashCode());
		result = prime
				* result
				+ ((electricRepairPersonId == null) ? 0
						: electricRepairPersonId.hashCode());
		result = prime
				* result
				+ ((electricRepairTeam == null) ? 0 : electricRepairTeam
						.hashCode());
		result = prime
				* result
				+ ((electricRepairTeamId == null) ? 0 : electricRepairTeamId
						.hashCode());
		result = prime
				* result
				+ ((eletricTotalPower == null) ? 0 : eletricTotalPower
						.hashCode());
		result = prime * result
				+ ((equipmentCode == null) ? 0 : equipmentCode.hashCode());
		result = prime * result
				+ ((equipmentName == null) ? 0 : equipmentName.hashCode());
		result = prime * result + ((fcState == null) ? 0 : fcState.hashCode());
		result = prime * result
				+ ((fixedAssetNo == null) ? 0 : fixedAssetNo.hashCode());
		result = prime * result
				+ ((fixedAssetValue == null) ? 0 : fixedAssetValue.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result
				+ ((isDedicated == null) ? 0 : isDedicated.hashCode());
		result = prime * result
				+ ((isExactness == null) ? 0 : isExactness.hashCode());
		result = prime * result + ((isFrock == null) ? 0 : isFrock.hashCode());
		result = prime * result
				+ ((isPrimaryDevice == null) ? 0 : isPrimaryDevice.hashCode());
		result = prime * result
				+ ((isSpecialType == null) ? 0 : isSpecialType.hashCode());
		result = prime * result
				+ ((leaveFactoryNo == null) ? 0 : leaveFactoryNo.hashCode());
		result = prime * result
				+ ((makeDate == null) ? 0 : makeDate.hashCode());
		result = prime * result
				+ ((makeFactory == null) ? 0 : makeFactory.hashCode());
		result = prime * result
				+ ((makeYear == null) ? 0 : makeYear.hashCode());
		result = prime * result
				+ ((manageClass == null) ? 0 : manageClass.hashCode());
		result = prime * result
				+ ((manageLevel == null) ? 0 : manageLevel.hashCode());
		result = prime * result
				+ ((maxRepairYear == null) ? 0 : maxRepairYear.hashCode());
		result = prime
				* result
				+ ((mechanicalCoefficient == null) ? 0 : mechanicalCoefficient
						.hashCode());
		result = prime
				* result
				+ ((mechanicalRepairPerson == null) ? 0
						: mechanicalRepairPerson.hashCode());
		result = prime
				* result
				+ ((mechanicalRepairPersonId == null) ? 0
						: mechanicalRepairPersonId.hashCode());
		result = prime
				* result
				+ ((mechanicalRepairTeam == null) ? 0 : mechanicalRepairTeam
						.hashCode());
		result = prime
				* result
				+ ((mechanicalRepairTeamId == null) ? 0
						: mechanicalRepairTeamId.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result
				+ ((netValue == null) ? 0 : netValue.hashCode());
		result = prime * result + (newBuyInvoke ? 1231 : 1237);
		result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
		result = prime * result + ((orgName == null) ? 0 : orgName.hashCode());
		result = prime * result
				+ ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result
				+ ((runingShifts == null) ? 0 : runingShifts.hashCode());
		result = prime * result
				+ ((shapeSize == null) ? 0 : shapeSize.hashCode());
		result = prime * result
				+ ((specification == null) ? 0 : specification.hashCode());
		result = prime * result
				+ ((tecLevel == null) ? 0 : tecLevel.hashCode());
		result = prime * result
				+ ((tecStatus == null) ? 0 : tecStatus.hashCode());
		result = prime * result
				+ ((updateTime == null) ? 0 : updateTime.hashCode());
		result = prime * result + ((updator == null) ? 0 : updator.hashCode());
		result = prime * result + ((useDate == null) ? 0 : useDate.hashCode());
		result = prime * result
				+ ((usePerson == null) ? 0 : usePerson.hashCode());
		result = prime * result
				+ ((usePersonId == null) ? 0 : usePersonId.hashCode());
		result = prime * result
				+ ((usePlace == null) ? 0 : usePlace.hashCode());
		result = prime * result
				+ ((useWorkshop == null) ? 0 : useWorkshop.hashCode());
		result = prime * result
				+ ((useWorkshopId == null) ? 0 : useWorkshopId.hashCode());
		result = prime * result + ((useYear == null) ? 0 : useYear.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		result = prime * result + ((xzState == null) ? 0 : xzState.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EquipmentPrimaryInfo other = (EquipmentPrimaryInfo) obj;
		if (buyDate == null) {
			if (other.buyDate != null)
				return false;
		} else if (!buyDate.equals(other.buyDate))
			return false;
		if (classCode == null) {
			if (other.classCode != null)
				return false;
		} else if (!classCode.equals(other.classCode))
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (czState == null) {
			if (other.czState != null)
				return false;
		} else if (!czState.equals(other.czState))
			return false;
		if (dynamic == null) {
			if (other.dynamic != null)
				return false;
		} else if (!dynamic.equals(other.dynamic))
			return false;
		if (electricCoefficient == null) {
			if (other.electricCoefficient != null)
				return false;
		} else if (!electricCoefficient.equals(other.electricCoefficient))
			return false;
		if (electricRepairPerson == null) {
			if (other.electricRepairPerson != null)
				return false;
		} else if (!electricRepairPerson.equals(other.electricRepairPerson))
			return false;
		if (electricRepairPersonId == null) {
			if (other.electricRepairPersonId != null)
				return false;
		} else if (!electricRepairPersonId.equals(other.electricRepairPersonId))
			return false;
		if (electricRepairTeam == null) {
			if (other.electricRepairTeam != null)
				return false;
		} else if (!electricRepairTeam.equals(other.electricRepairTeam))
			return false;
		if (electricRepairTeamId == null) {
			if (other.electricRepairTeamId != null)
				return false;
		} else if (!electricRepairTeamId.equals(other.electricRepairTeamId))
			return false;
		if (eletricTotalPower == null) {
			if (other.eletricTotalPower != null)
				return false;
		} else if (!eletricTotalPower.equals(other.eletricTotalPower))
			return false;
		if (equipmentCode == null) {
			if (other.equipmentCode != null)
				return false;
		} else if (!equipmentCode.equals(other.equipmentCode))
			return false;
		if (equipmentName == null) {
			if (other.equipmentName != null)
				return false;
		} else if (!equipmentName.equals(other.equipmentName))
			return false;
		if (fcState == null) {
			if (other.fcState != null)
				return false;
		} else if (!fcState.equals(other.fcState))
			return false;
		if (fixedAssetNo == null) {
			if (other.fixedAssetNo != null)
				return false;
		} else if (!fixedAssetNo.equals(other.fixedAssetNo))
			return false;
		if (fixedAssetValue == null) {
			if (other.fixedAssetValue != null)
				return false;
		} else if (!fixedAssetValue.equals(other.fixedAssetValue))
			return false;
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (isDedicated == null) {
			if (other.isDedicated != null)
				return false;
		} else if (!isDedicated.equals(other.isDedicated))
			return false;
		if (isExactness == null) {
			if (other.isExactness != null)
				return false;
		} else if (!isExactness.equals(other.isExactness))
			return false;
		if (isFrock == null) {
			if (other.isFrock != null)
				return false;
		} else if (!isFrock.equals(other.isFrock))
			return false;
		if (isPrimaryDevice == null) {
			if (other.isPrimaryDevice != null)
				return false;
		} else if (!isPrimaryDevice.equals(other.isPrimaryDevice))
			return false;
		if (isSpecialType == null) {
			if (other.isSpecialType != null)
				return false;
		} else if (!isSpecialType.equals(other.isSpecialType))
			return false;
		if (leaveFactoryNo == null) {
			if (other.leaveFactoryNo != null)
				return false;
		} else if (!leaveFactoryNo.equals(other.leaveFactoryNo))
			return false;
		if (makeDate == null) {
			if (other.makeDate != null)
				return false;
		} else if (!makeDate.equals(other.makeDate))
			return false;
		if (makeFactory == null) {
			if (other.makeFactory != null)
				return false;
		} else if (!makeFactory.equals(other.makeFactory))
			return false;
		if (makeYear == null) {
			if (other.makeYear != null)
				return false;
		} else if (!makeYear.equals(other.makeYear))
			return false;
		if (manageClass == null) {
			if (other.manageClass != null)
				return false;
		} else if (!manageClass.equals(other.manageClass))
			return false;
		if (manageLevel == null) {
			if (other.manageLevel != null)
				return false;
		} else if (!manageLevel.equals(other.manageLevel))
			return false;
		if (maxRepairYear == null) {
			if (other.maxRepairYear != null)
				return false;
		} else if (!maxRepairYear.equals(other.maxRepairYear))
			return false;
		if (mechanicalCoefficient == null) {
			if (other.mechanicalCoefficient != null)
				return false;
		} else if (!mechanicalCoefficient.equals(other.mechanicalCoefficient))
			return false;
		if (mechanicalRepairPerson == null) {
			if (other.mechanicalRepairPerson != null)
				return false;
		} else if (!mechanicalRepairPerson.equals(other.mechanicalRepairPerson))
			return false;
		if (mechanicalRepairPersonId == null) {
			if (other.mechanicalRepairPersonId != null)
				return false;
		} else if (!mechanicalRepairPersonId
				.equals(other.mechanicalRepairPersonId))
			return false;
		if (mechanicalRepairTeam == null) {
			if (other.mechanicalRepairTeam != null)
				return false;
		} else if (!mechanicalRepairTeam.equals(other.mechanicalRepairTeam))
			return false;
		if (mechanicalRepairTeamId == null) {
			if (other.mechanicalRepairTeamId != null)
				return false;
		} else if (!mechanicalRepairTeamId.equals(other.mechanicalRepairTeamId))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (netValue == null) {
			if (other.netValue != null)
				return false;
		} else if (!netValue.equals(other.netValue))
			return false;
		if (newBuyInvoke != other.newBuyInvoke)
			return false;
		if (orgId == null) {
			if (other.orgId != null)
				return false;
		} else if (!orgId.equals(other.orgId))
			return false;
		if (orgName == null) {
			if (other.orgName != null)
				return false;
		} else if (!orgName.equals(other.orgName))
			return false;
		if (recordStatus == null) {
			if (other.recordStatus != null)
				return false;
		} else if (!recordStatus.equals(other.recordStatus))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (runingShifts == null) {
			if (other.runingShifts != null)
				return false;
		} else if (!runingShifts.equals(other.runingShifts))
			return false;
		if (shapeSize == null) {
			if (other.shapeSize != null)
				return false;
		} else if (!shapeSize.equals(other.shapeSize))
			return false;
		if (specification == null) {
			if (other.specification != null)
				return false;
		} else if (!specification.equals(other.specification))
			return false;
		if (tecLevel == null) {
			if (other.tecLevel != null)
				return false;
		} else if (!tecLevel.equals(other.tecLevel))
			return false;
		if (tecStatus == null) {
			if (other.tecStatus != null)
				return false;
		} else if (!tecStatus.equals(other.tecStatus))
			return false;
		if (updateTime == null) {
			if (other.updateTime != null)
				return false;
		} else if (!updateTime.equals(other.updateTime))
			return false;
		if (updator == null) {
			if (other.updator != null)
				return false;
		} else if (!updator.equals(other.updator))
			return false;
		if (useDate == null) {
			if (other.useDate != null)
				return false;
		} else if (!useDate.equals(other.useDate))
			return false;
		if (usePerson == null) {
			if (other.usePerson != null)
				return false;
		} else if (!usePerson.equals(other.usePerson))
			return false;
		if (usePersonId == null) {
			if (other.usePersonId != null)
				return false;
		} else if (!usePersonId.equals(other.usePersonId))
			return false;
		if (usePlace == null) {
			if (other.usePlace != null)
				return false;
		} else if (!usePlace.equals(other.usePlace))
			return false;
		if (useWorkshop == null) {
			if (other.useWorkshop != null)
				return false;
		} else if (!useWorkshop.equals(other.useWorkshop))
			return false;
		if (useWorkshopId == null) {
			if (other.useWorkshopId != null)
				return false;
		} else if (!useWorkshopId.equals(other.useWorkshopId))
			return false;
		if (useYear == null) {
			if (other.useYear != null)
				return false;
		} else if (!useYear.equals(other.useYear))
			return false;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		if (xzState == null) {
			if (other.xzState != null)
				return false;
		} else if (!xzState.equals(other.xzState))
			return false;
		return true;
	}

}
