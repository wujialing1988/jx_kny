package com.yunda.freight.zb.qualitycontrol.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明：ZbglQualityControlDTO实体类, 数据表：质量检查数据对象
 * <li>创建人：何东
 * <li>创建日期：2017-04-12 10:48:28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */

@Entity
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglQualityControlDTO implements Serializable{

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/* idx主键 */
	@Id
	private String idx;
	
	/* 业务编码 */
	@Column(name="business_code")
	private String businessCode;
	
	/* 业务主键 */
	@Column(name="business_idx")
	private String businessIDX;
	
	/* 检查项编码 */
	@Column(name="qc_item_no")
	private String qcItemNo;
	
	/* 检查项名称 */
	@Column(name="qc_item_name")
	private String qcItemName;
	
	/* 检查人员 */
	@Column(name="check_empid")
	private Long checkEmpID;
	
	/* 检查人员名称 */
	@Column(name="check_empname")
	private String checkEmpName;
	
	/* 其他作业人员 */
	@Column(name="other_woker")
	private String otherWoker;
	
	/* 站点标识 */
	@Column(name="siteid")
	private String siteID;
	
	/* 创建人 */
	@Column(name="creator")
	private Long creator;

	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;
	
	/* 备注 */
	@Column(name="remark")
	private String remark;
	
	/* 状态 */
	@Column(name="status")
	private String status;
	
    /* 车型拼音码 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;
    
    /* 车号 */
    @Column(name = "Train_No")
    private String trainNo;
    
    /* 入段时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "In_Time")
    private Date inTime;
    
    /* 功能名称 */
	@Column(name="function_name")
	private String functionName;
	
	/* 状态 */
	@Column(name="business_desc")
	private String businessDesc;
	
	/** 故障信息 S */
	/* 故障类型字典值 */
	@Column(name = "FAULT_TYPE_VALUE")
	private java.lang.String faultTypeValue;
	
	/* 作业范围全称 */
	@Column(name = "SCOPE_WORK_FULLNAME")
	private java.lang.String scopeWorkFullname;
	
	/* 故障部件全称 */
	@Column(name = "VEHICLE_COMPONENT_FULLNAME")
	private java.lang.String vehicleComponentFullname;

	/* 故障描述 */
	@Column(name = "FAULT_DESC")
	private java.lang.String faultDesc;
	
	/* 提票人名称 */
	@Column(name = "NOTICE_PERSON_NAME")
	private java.lang.String noticePersonName;
	
	/* 处理方式 */
	@Column(name = "HANDLE_WAY")
	private java.lang.String handleWay;
	/** 故障信息 E */
	
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getTrainTypeShortName() {
		return trainTypeShortName;
	}

	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getBusinessIDX() {
		return businessIDX;
	}

	public void setBusinessIDX(String businessIDX) {
		this.businessIDX = businessIDX;
	}

	public Long getCheckEmpID() {
		return checkEmpID;
	}

	public void setCheckEmpID(Long checkEmpID) {
		this.checkEmpID = checkEmpID;
	}

	public String getCheckEmpName() {
		return checkEmpName;
	}

	public void setCheckEmpName(String checkEmpName) {
		this.checkEmpName = checkEmpName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getOtherWoker() {
		return otherWoker;
	}

	public void setOtherWoker(String otherWoker) {
		this.otherWoker = otherWoker;
	}

	public String getQcItemName() {
		return qcItemName;
	}

	public void setQcItemName(String qcItemName) {
		this.qcItemName = qcItemName;
	}

	public String getQcItemNo() {
		return qcItemNo;
	}

	public void setQcItemNo(String qcItemNo) {
		this.qcItemNo = qcItemNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBusinessDesc() {
		return businessDesc;
	}

	public void setBusinessDesc(String businessDesc) {
		this.businessDesc = businessDesc;
	}

	public java.lang.String getFaultTypeValue() {
		return faultTypeValue;
	}

	public void setFaultTypeValue(java.lang.String faultTypeValue) {
		this.faultTypeValue = faultTypeValue;
	}

	public java.lang.String getScopeWorkFullname() {
		return scopeWorkFullname;
	}

	public void setScopeWorkFullname(java.lang.String scopeWorkFullname) {
		this.scopeWorkFullname = scopeWorkFullname;
	}

	public java.lang.String getVehicleComponentFullname() {
		return vehicleComponentFullname;
	}

	public void setVehicleComponentFullname(
			java.lang.String vehicleComponentFullname) {
		this.vehicleComponentFullname = vehicleComponentFullname;
	}

	public java.lang.String getFaultDesc() {
		return faultDesc;
	}

	public void setFaultDesc(java.lang.String faultDesc) {
		this.faultDesc = faultDesc;
	}

	public java.lang.String getNoticePersonName() {
		return noticePersonName;
	}

	public void setNoticePersonName(java.lang.String noticePersonName) {
		this.noticePersonName = noticePersonName;
	}

	public java.lang.String getHandleWay() {
		return handleWay;
	}

	public void setHandleWay(java.lang.String handleWay) {
		this.handleWay = handleWay;
	}
	
}
