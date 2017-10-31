package com.yunda.sb.repair.process.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairTaskList查询封装实体
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
public class RepairTaskListBean implements java.io.Serializable {
	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	private String idx;

	/** 月计划主键 */
	@Column(name = "plan_month_idx")
	private String planMonthIdx;

	/** 设备主键 */
	@Column(name = "equipment_idx")
	private String equipmentIdx;

	/** 修别 */
	@Column(name = "repair_class_name")
	private String repairClassName;

	/** 机械工长签名 */
	@Column(name = "gz_sign_mac")
	private String gzSignMac;

	/** 电气工长签名 */
	@Column(name = "gz_sign_elc")
	private String gzSignElc;

	/** 实际开工时间 */
	@Column(name = "real_begin_time")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date realBeginTime;

	/** 实际完工时间 */
	@Column(name = "real_end_time")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date realEndTime;

	/** 计划开工时间 */
	@Column(name = "begin_time")
	@Temporal(TemporalType.DATE)
	private java.util.Date beginTime;

	/** 计划完工时间 */
	@Column(name = "end_time")
	@Temporal(TemporalType.DATE)
	private java.util.Date endTime;

	/** 是否需要验收，0：无需验收人验收、1：需要验收人验收 */
	@Column(name = "is_need_acceptance")
	private Boolean isNeedAcceptance;

	/** 验收人ID */
	@Column(name = "acceptor_id")
	private Long acceptorId;

	/** 验收人名称 */
	@Column(name = "acceptor_name")
	private String acceptorName;

	/** 验收评语 */
	@Column(name = "acceptance_reviews")
	private String acceptanceReviews;

	/** 验收时间 */
	@Column(name = "acceptance_time")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date acceptanceTime;

	/** 使用人ID */
	@Column(name = "user_id")
	private Long userId;

	/** 使用人名称 */
	@Column(name = "user_name")
	private String userName;

	/** 处理状态：(未处理、已处理) */
	private String state;

	/** 创建时间 */
	@Column(name = "CREATE_TIME", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date createTime;

	/* 设备编码 */
	@Column(name = "equipment_code")
	private String equipmentCode;

	/* 设备名称 */
	@Column(name = "equipment_name")
	private String equipmentName;

	/* 规格 */
	private String specification;

	/* 型号 */
	private String model;

	/* 设置地点(来源码表) */
	@Column(name = "use_place")
	private String usePlace;

	/* 电气系数 */
	@Column(name = "electric_coefficient")
	private Integer electricCoefficient;

	/* 机械系数 */
	@Column(name = "mechanical_coefficient")
	private Integer mechanicalCoefficient;

	/* 使用人 */
	@Column(name = "use_person")
	private String usePerson;

	/** 未处理的设备检修范围实例树 */
	@Column(name = "WCL_COUNT")
	private Integer wclCount;

	/** 已处理的设备检修范围实例树 */
	@Column(name = "YCL_COUNT")
	private Integer yclCount;

	/** 机械维修班组 */
	@Column(name = "REPAIR_TEAM_MAC")
	private String repairTeamMac;

	/** 电气维修班组 */
	@Column(name = "REPAIR_TEAM_ELC")
	private String repairTeamElc;

	/** 单位名称 */
	@Column(name = "ORG_NAME")
	private String orgName;

	/** 单位id */
	@Column(name = "ORG_ID")
	private String orgId;

	/** 附件（照片）数量 */
	@Column(name = "IMAGE_COUNT")
	private Integer imageCount;

	/* 计划年度 */
	@Transient
	private Integer planYear;

	/* 计划月份 */
	@Transient
	private Integer planMonth;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getPlanMonthIdx() {
		return planMonthIdx;
	}

	public void setPlanMonthIdx(String planMonthIdx) {
		this.planMonthIdx = planMonthIdx;
	}

	public String getEquipmentIdx() {
		return equipmentIdx;
	}

	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
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

	public Boolean getIsNeedAcceptance() {
		return isNeedAcceptance;
	}

	public void setIsNeedAcceptance(Boolean isNeedAcceptance) {
		this.isNeedAcceptance = isNeedAcceptance;
	}

	public Long getAcceptorId() {
		return acceptorId;
	}

	public void setAcceptorId(Long acceptorId) {
		this.acceptorId = acceptorId;
	}

	public String getAcceptorName() {
		return acceptorName;
	}

	public void setAcceptorName(String acceptorName) {
		this.acceptorName = acceptorName;
	}

	public String getAcceptanceReviews() {
		return acceptanceReviews;
	}

	public void setAcceptanceReviews(String acceptanceReviews) {
		this.acceptanceReviews = acceptanceReviews;
	}

	public java.util.Date getAcceptanceTime() {
		return acceptanceTime;
	}

	public void setAcceptanceTime(java.util.Date acceptanceTime) {
		this.acceptanceTime = acceptanceTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getState() {
		return state;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRepairClassName() {
		return repairClassName;
	}

	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}

	public String getGzSignMac() {
		return gzSignMac;
	}

	public void setGzSignMac(String gzSignMac) {
		this.gzSignMac = gzSignMac;
	}

	public String getGzSignElc() {
		return gzSignElc;
	}

	public void setGzSignElc(String gzSignElc) {
		this.gzSignElc = gzSignElc;
	}

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getUsePlace() {
		return usePlace;
	}

	public void setUsePlace(String usePlace) {
		this.usePlace = usePlace;
	}

	public Integer getElectricCoefficient() {
		return electricCoefficient;
	}

	public void setElectricCoefficient(Integer electricCoefficient) {
		this.electricCoefficient = electricCoefficient;
	}

	public Integer getMechanicalCoefficient() {
		return mechanicalCoefficient;
	}

	public void setMechanicalCoefficient(Integer mechanicalCoefficient) {
		this.mechanicalCoefficient = mechanicalCoefficient;
	}

	public String getUsePerson() {
		return usePerson;
	}

	public void setUsePerson(String usePerson) {
		this.usePerson = usePerson;
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

	public String getRepairTeamMac() {
		return repairTeamMac;
	}

	public void setRepairTeamMac(String repairTeamMac) {
		this.repairTeamMac = repairTeamMac;
	}

	public String getRepairTeamElc() {
		return repairTeamElc;
	}

	public void setRepairTeamElc(String repairTeamElc) {
		this.repairTeamElc = repairTeamElc;
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

	public Integer getImageCount() {
		return imageCount;
	}

	public void setImageCount(Integer imageCount) {
		this.imageCount = imageCount;
	}

}
