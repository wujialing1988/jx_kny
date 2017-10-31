package com.yunda.jx.webservice.stationTerminal.base.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于位置可安装配件选择实体包装
 * <li>说明: 用于findPlaceParts接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PlacePartsBean implements java.io.Serializable{
	
	private String partsNo ; //配件名称
	private String nameplateNo ; //配件编号
	private String specificationModel ; //配件序列号
	private String repairClassName ; //规格型号
	
	public String getNameplateNo() {
		return nameplateNo;
	}
	public void setNameplateNo(String nameplateNo) {
		this.nameplateNo = nameplateNo;
	}
	public String getPartsNo() {
		return partsNo;
	}
	public void setPartsNo(String partsNo) {
		this.partsNo = partsNo;
	}
	public String getRepairClassName() {
		return repairClassName;
	}
	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}
	public String getSpecificationModel() {
		return specificationModel;
	}
	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel;
	}
}
