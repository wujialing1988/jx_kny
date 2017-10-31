package com.yunda.jx.webservice.stationTerminal.base.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于获取提票信息实体包装
 * <li>说明: 用于getFaultInfo接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-26
 * <li>修改人: 王斌
 * <li>修改日期：2014-1-3
 * <li>修改内容：增加字段fixPlaceFullCode
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class FaultInfoBean implements java.io.Serializable{
	
	private String idx ; //主键
	private String trainTypeIDX ; //车型IDX
	private String trainNo ; //车号
	private String trainTypeShortName ; //车型简称	
	private String noticePersonName ; //提票人
	private String partsName ; //配件名称
	private String partsNo ; //配件编号
	private String specificationModel ; //配件型号
	private String fixPlaceFullName ; //故障位置
	private String faultName ; //故障现象
	private String faultDesc ; //故障描述
	private String faultOccurDateStr ; //故障发生时间
	private String realPartsName ; //实际配件名称
	private String realSpecificationModel ; //实际配件规格型号
	private String realFaultName ; //实际故障现象
	private String methodName ; //处理方法名称
	private String methodDesc ; //处理方法描述
	private String repairResult ; //处理结果
	private String realFixPlaceFullName ; //实际故障位置全名
	private String faultID ; //故障ID
	private String faultFixPlaceIDX ; //故障位置主键
	private String fixPlaceFullCode;//故障位置编码全名
	private Integer status;// 提票状态（10：未处理、20：处理中、30：已处理） 
	private String faultType;//提票类型
	public String getFaultDesc() {
		return faultDesc;
	}
	public void setFaultDesc(String faultDesc) {
		this.faultDesc = faultDesc;
	}
	public String getFaultFixPlaceIDX() {
		return faultFixPlaceIDX;
	}
	public void setFaultFixPlaceIDX(String faultFixPlaceIDX) {
		this.faultFixPlaceIDX = faultFixPlaceIDX;
	}
	public String getFaultID() {
		return faultID;
	}
	public void setFaultID(String faultID) {
		this.faultID = faultID;
	}
	public String getFaultName() {
		return faultName;
	}
	public void setFaultName(String faultName) {
		this.faultName = faultName;
	}
	public String getFaultOccurDateStr() {
		return faultOccurDateStr;
	}
	public void setFaultOccurDateStr(String faultOccurDateStr) {
		this.faultOccurDateStr = faultOccurDateStr;
	}
	public String getFixPlaceFullName() {
		return fixPlaceFullName;
	}
	public void setFixPlaceFullName(String fixPlaceFullName) {
		this.fixPlaceFullName = fixPlaceFullName;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getMethodDesc() {
		return methodDesc;
	}
	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getNoticePersonName() {
		return noticePersonName;
	}
	public void setNoticePersonName(String noticePersonName) {
		this.noticePersonName = noticePersonName;
	}
	public String getPartsName() {
		return partsName;
	}
	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}
	public String getPartsNo() {
		return partsNo;
	}
	public void setPartsNo(String partsNo) {
		this.partsNo = partsNo;
	}
	public String getRealFaultName() {
		return realFaultName;
	}
	public void setRealFaultName(String realFaultName) {
		this.realFaultName = realFaultName;
	}
	public String getRealFixPlaceFullName() {
		return realFixPlaceFullName;
	}
	public void setRealFixPlaceFullName(String realFixPlaceFullName) {
		this.realFixPlaceFullName = realFixPlaceFullName;
	}
	public String getRealPartsName() {
		return realPartsName;
	}
	public void setRealPartsName(String realPartsName) {
		this.realPartsName = realPartsName;
	}
	public String getRealSpecificationModel() {
		return realSpecificationModel;
	}
	public void setRealSpecificationModel(String realSpecificationModel) {
		this.realSpecificationModel = realSpecificationModel;
	}
	public String getRepairResult() {
		return repairResult;
	}
	public void setRepairResult(String repairResult) {
		this.repairResult = repairResult;
	}
	public String getSpecificationModel() {
		return specificationModel;
	}
	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel;
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public String getTrainTypeIDX() {
		return trainTypeIDX;
	}
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}
	public String getTrainTypeShortName() {
		return trainTypeShortName;
	}
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	public String getFixPlaceFullCode() {
		return fixPlaceFullCode;
	}
	public void setFixPlaceFullCode(String fixPlaceFullCode) {
		this.fixPlaceFullCode = fixPlaceFullCode;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getFaultType() {
		return faultType;
	}
	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}
    
	
}
