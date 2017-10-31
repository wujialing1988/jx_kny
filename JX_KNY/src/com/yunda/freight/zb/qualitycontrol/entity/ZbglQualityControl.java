package com.yunda.freight.zb.qualitycontrol.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglQualityControl实体类, 数据表：质量检查
 * <li>创建人：林欢
 * <li>创建日期：2016-10-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */

@Entity
@Table(name="k_quality_control")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglQualityControl implements Serializable{

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/** 未检查 */
	public static final String UNCHECKED = "UNCHECKED";
	
	/** 合格 */
	public static final String QUALIFIED = "QUALIFIED";
	
	/** 不合格 */
	public static final String UNQUALIFIED = "UNQUALIFIED";
	
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
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
	
	
}
