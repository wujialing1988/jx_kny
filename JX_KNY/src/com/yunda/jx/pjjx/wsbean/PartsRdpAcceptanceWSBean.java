package com.yunda.jx.pjjx.wsbean;

import java.io.Serializable;

/**
 * <li>说明： 配件修竣合格验收WS返回实体
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-19
 * <li>成都运达科技股份有限公司
 */
public class PartsRdpAcceptanceWSBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idx;
	/* 规格型号 */
	private String specificationModel;
	/* 配件名称 */
	private String partsName;
	/* 物料编码 */
	private String matCode;
	/* 配件编号 */
	private String partsNo;
    /* 配件识别码 */
    private String identificationCode;
	/* 扩展编号 */
	private String extendNo;
	/* 下车车型 */
	private String unloadTrainType;
	/* 下车车号 */
	private String unloadTrainNo;
	/* 下车修程 */
	private String unloadRepairClass;
	/* 下车修次 */
	private String unloadRepairTime;
	/* 计划开始时间 */
	private java.util.Date planStartTime;
	/* 计划结束时间 */
	private java.util.Date planEndTime;
	/* 实际开始时间 */
	private java.util.Date realStartTime;
	/* 实际结束时间 */
	private java.util.Date realEndTime;
	/* 承修班组名称 */
	private String repairOrgName;
	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getSpecificationModel() {
		return specificationModel;
	}
	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel;
	}
	public String getPartsName() {
		return partsName;
	}
	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}
	public String getMatCode() {
		return matCode;
	}
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
	public String getPartsNo() {
		return partsNo;
	}
	public void setPartsNo(String partsNo) {
		this.partsNo = partsNo;
	}
	public String getIdentificationCode() {
		return identificationCode;
	}
	public void setIdentificationCode(String identificationCode) {
		this.identificationCode = identificationCode;
	}
	public String getExtendNo() {
		return extendNo;
	}
	public void setExtendNo(String extendNo) {
		this.extendNo = extendNo;
	}
	public String getUnloadTrainType() {
		return unloadTrainType;
	}
	public void setUnloadTrainType(String unloadTrainType) {
		this.unloadTrainType = unloadTrainType;
	}
	public String getUnloadTrainNo() {
		return unloadTrainNo;
	}
	public void setUnloadTrainNo(String unloadTrainNo) {
		this.unloadTrainNo = unloadTrainNo;
	}
	public String getUnloadRepairClass() {
		return unloadRepairClass;
	}
	public void setUnloadRepairClass(String unloadRepairClass) {
		this.unloadRepairClass = unloadRepairClass;
	}
	public String getUnloadRepairTime() {
		return unloadRepairTime;
	}
	public void setUnloadRepairTime(String unloadRepairTime) {
		this.unloadRepairTime = unloadRepairTime;
	}
	public java.util.Date getPlanStartTime() {
		return planStartTime;
	}
	public void setPlanStartTime(java.util.Date planStartTime) {
		this.planStartTime = planStartTime;
	}
	public java.util.Date getPlanEndTime() {
		return planEndTime;
	}
	public void setPlanEndTime(java.util.Date planEndTime) {
		this.planEndTime = planEndTime;
	}
	public String getRepairOrgName() {
		return repairOrgName;
	}
	public void setRepairOrgName(String repairOrgName) {
		this.repairOrgName = repairOrgName;
	}
	public java.util.Date getRealStartTime() {
		return realStartTime;
	}
	public void setRealStartTime(java.util.Date realStartTime) {
		this.realStartTime = realStartTime;
	}
	public java.util.Date getRealEndTime() {
		return realEndTime;
	}
	public void setRealEndTime(java.util.Date realEndTime) {
		this.realEndTime = realEndTime;
	}
}
