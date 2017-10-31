package com.yunda.sb.inspect.route.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: InspectRouteDetailsBean实体类，用于联合分页查询
 * <li>创建人：何涛
 * <li>创建日期：2016年6月13日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
public final class InspectRouteDetailsBean implements Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@Id
	private String idx;

	/** 设备主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

	/** 序号 */
	@Column(name = "SEQ_NO")
	private Integer seqNo;

	/** 设备巡检线路主键 */
	@Column(name = "ROUTE_IDX")
	private String routeIdx;

	/** 机械巡检人 */
	@Column(name = "MAC_INSPECT_EMPID")
	private String macInspectEmpid;

	/** 机械巡检人名称 */
	@Column(name = "MAC_INSPECT_EMP")
	private String macInspectEmp;

	/** 电气巡检人 */
	@Column(name = "ELC_INSPECT_EMPID")
	private String elcInspectEmpid;

	/** 电气巡检人名称 */
	@Column(name = "ELC_INSPECT_EMP")
	private String elcInspectEmp;

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

	/** 管理类别 */
	@Column(name = "manage_class")
	private String manageClass;

	public String getManageClass() {
		return manageClass;
	}

	public void setManageClass(String manageClass) {
		this.manageClass = manageClass;
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

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getRouteIdx() {
		return routeIdx;
	}

	public void setRouteIdx(String routeIdx) {
		this.routeIdx = routeIdx;
	}

	public String getMacInspectEmpid() {
		return macInspectEmpid;
	}

	public void setMacInspectEmpid(String macInspectEmpid) {
		this.macInspectEmpid = macInspectEmpid;
	}

	public String getMacInspectEmp() {
		return macInspectEmp;
	}

	public void setMacInspectEmp(String macInspectEmp) {
		this.macInspectEmp = macInspectEmp;
	}

	public String getElcInspectEmpid() {
		return elcInspectEmpid;
	}

	public void setElcInspectEmpid(String elcInspectEmpid) {
		this.elcInspectEmpid = elcInspectEmpid;
	}

	public String getElcInspectEmp() {
		return elcInspectEmp;
	}

	public void setElcInspectEmp(String elcInspectEmp) {
		this.elcInspectEmp = elcInspectEmp;
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

}