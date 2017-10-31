package com.yunda.webservice.device.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件生产计划信息
 * <li>创建人：王治龙
 * <li>创建日期：2014-6-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XStreamAlias("partRepairPlan")
public class PartRepairPlan {
	
	/** 配件规格型号  */
	@XStreamAlias("specificationModel")
	private String specificationModel;
	/** 配件规格型号代码  */
	@XStreamAlias("specificationModelCode")
	private String specificationModelCode;
	/** 配件名称  */
	@XStreamAlias("partsName")
	private String partsName;
	/** 配件编码  */
	@XStreamAlias("partsCode")
	private String partsCode;
	/** 下车车型  */
	@XStreamAlias("unloadTrainType")
	private String unloadTrainType;
	/** 下车车号  */
	@XStreamAlias("unloadTrainNo")
	private String unloadTrainNo;
	/** 下车位置  */
	@XStreamAlias("unloadPosition")
	private String unloadPosition;
	/** 计划检修时间  */
	@XStreamAlias("planRepairTime")
	private String planRepairTime;
	
	public String getPartsCode() {
		return partsCode;
	}
	public void setPartsCode(String partsCode) {
		this.partsCode = partsCode;
	}
	public String getPartsName() {
		return partsName;
	}
	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}
	public String getPlanRepairTime() {
		return planRepairTime;
	}
	public void setPlanRepairTime(String planRepairTime) {
		this.planRepairTime = planRepairTime;
	}
	public String getSpecificationModel() {
		return specificationModel;
	}
	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel;
	}
	public String getSpecificationModelCode() {
		return specificationModelCode;
	}
	public void setSpecificationModelCode(String specificationModelCode) {
		this.specificationModelCode = specificationModelCode;
	}
	public String getUnloadPosition() {
		return unloadPosition;
	}
	public void setUnloadPosition(String unloadPosition) {
		this.unloadPosition = unloadPosition;
	}
	public String getUnloadTrainNo() {
		return unloadTrainNo;
	}
	public void setUnloadTrainNo(String unloadTrainNo) {
		this.unloadTrainNo = unloadTrainNo;
	}
	public String getUnloadTrainType() {
		return unloadTrainType;
	}
	public void setUnloadTrainType(String unloadTrainType) {
		this.unloadTrainType = unloadTrainType;
	}
	
	
}
