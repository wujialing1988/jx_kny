package com.yunda.sb.inspect.plan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: InspectPlanEquipmentBean实体类，用于联合分页查询
 * <li>创建人：何涛
 * <li>创建日期：2016年6月16日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
public final class InspectPlanEquipmentBean2 implements Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/** idx主键 */
	@Id
	private String idx;
	/** 巡检周期计划idx主键 */
	@Column(name = "PLAN_IDX")
	private String planIdx;
	/** 设备主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;
	/** 序号 */
	@Column(name = "SEQ_NO")
	private Integer seqNo;
	/** 使用人 */
	@Column(name = "USE_WORKER")
	private String useWorker;
	/** 使用人ID */
	@Column(name = "USE_WORKER_ID")
	private Long useWorkerId;
	/** 实际开工时间 */
	@Column(name = "REAL_BEGIN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date realBeginTime;
	/** 实际完工时间 */
	@Column(name = "REAL_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date realEndTime;
	/** 巡检结果（已巡检、未巡检） */
	@Column(name = "CHECK_RESULT")
	private String checkResult;
	/** 巡检情况描述 */
	@Column(name = "CHECK_RESULT_DESC")
	private String checkResultDesc;
	/** 设备类别编码 */
	@Column(name = "class_code")
	private String classCode;
	/** 设备类别名称 */
	@Column(name = "class_name")
	private String className;
	/** 设备名称 */
	@Column(name = "equipment_name")
	private String equipmentName;
	/** 设备编码 */
	@Column(name = "equipment_code")
	private String equipmentCode;
	/** 购入日期 */
	@Column(name = "buy_date")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date buyDate;
	/** 固资编号 */
	@Column(name = "fixed_asset_no")
	private String fixedAssetNo;
	/** 固资原值 */
	@Column(name = "fixed_asset_value")
	private Float fixedAssetValue;
	/** 型号 */
	private String model;
	/** 规格 */
	private String specification;
	/** 机械系数 */
	@Column(name = "mechanical_coefficient")
	private Integer mechanicalCoefficient;
	/** 电气系数 */
	@Column(name = "electric_coefficient")
	private Integer electricCoefficient;
	/** 制造工厂 */
	@Column(name = "make_factory")
	private String makeFactory;
	/** 制造年月 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "make_date")
	private java.util.Date makeDate;
	/** 设置地点(来源码表) */
	@Column(name = "use_place")
	private String usePlace;
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

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getPlanIdx() {
		return planIdx;
	}

	public void setPlanIdx(String planIdx) {
		this.planIdx = planIdx;
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

	public java.util.Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(java.util.Date buyDate) {
		this.buyDate = buyDate;
	}

	public String getFixedAssetNo() {
		return fixedAssetNo;
	}

	public void setFixedAssetNo(String fixedAssetNo) {
		this.fixedAssetNo = fixedAssetNo;
	}

	public Float getFixedAssetValue() {
		return fixedAssetValue;
	}

	public void setFixedAssetValue(Float fixedAssetValue) {
		this.fixedAssetValue = fixedAssetValue;
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

	public String getUsePlace() {
		return usePlace;
	}

	public void setUsePlace(String usePlace) {
		this.usePlace = usePlace;
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