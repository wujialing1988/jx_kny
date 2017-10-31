package com.yunda.zb.tp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpTrackRdpDTO 查询实体
 * <li>创建人：林欢
 * <li>创建日期：2016-08-5
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglTpTrackRdpDTO {

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/* idx主键 */
	@Id
	private String idx;

	/* 车型编码 */
	@Column(name = "Train_Type_IDX")
	private String trainTypeIDX;

	/* 车型拼音码 */
	@Column(name = "Train_Type_ShortName")
	private String trainTypeShortName;

	/* 车号 */
	@Column(name = "Train_No")
	private String trainNo;

	/* jt6提票主键idx */
	@Column(name = "jt6_idx")
	private String jt6IDX;

	/* 跟踪原因 */
	@Column(name = "track_reason")
	private String trackReason;

	/* 跟踪人员 */
	@Column(name = "track_person_idx")
	private String trackPersonIDX;

	/* 跟踪人员姓名 */
	@Column(name = "track_person_name")
	private String trackPersonName;

	/* 跟踪状态(1==结束跟踪 0==正在跟踪) */
	@Column(name = "status")
	private Integer status;

	/* 已记录次数 */
	@Column(name = "record_count")
	private Integer recordCount;

	/* 提票单号 */
	@Column(name = "Fault_Notice_Code")
	private String faultNoticeCode;

	/* 跟踪时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "track_date")
	private Date trackDate;

	/* 故障ID */
	@Column(name = "Fault_ID")
	private String faultID;

	/* 系统分类编码 */
	@Column(name = "Fault_Fix_FullCode")
	private String faultFixFullCode;

	/* 故障部件全名 */
	@Column(name = "Fault_Fix_FullName")
	private String faultFixFullName;

	/* 故障现象 */
	@Column(name = "Fault_Name")
	private String faultName;

	/* 处理结果，1：修复；2：观察运用；3：转JT28；4：转临修；5：返本段修；6：扣车等件； */
	@Column(name = "Repair_Result")
	private Integer repairResult;

	/* 处理描述 */
	@Column(name = "REPAIR_DESC")
	private String repairDesc;
	
	/* 配属段idx */
	private String dId;
	
	/* 配属段名称 */
	private String dName;
	
	
	/* 机车出入段台账状态颜色 */
    @Transient
    private String color;
    
    /* 整备单idx */
	@Column(name = "rdp_idx")
	private String rdpIDX;
	
	/* 单次跟踪操作状态 */
	@Column(name = "single_status")
	private Integer singleStatus;
	
	/* 入段时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "in_time")
	private Date inTime;
	
	public java.util.Date getInTime() {
		return inTime;
	}

	public void setInTime(java.util.Date inTime) {
		this.inTime = inTime;
	}

	public String getRdpIDX() {
		return rdpIDX;
	}

	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
	}

	public Integer getSingleStatus() {
		return singleStatus;
	}

	public void setSingleStatus(Integer singleStatus) {
		this.singleStatus = singleStatus;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFaultFixFullCode() {
		return faultFixFullCode;
	}

	public void setFaultFixFullCode(String faultFixFullCode) {
		this.faultFixFullCode = faultFixFullCode;
	}

	public String getFaultFixFullName() {
		return faultFixFullName;
	}

	public void setFaultFixFullName(String faultFixFullName) {
		this.faultFixFullName = faultFixFullName;
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

	public String getFaultNoticeCode() {
		return faultNoticeCode;
	}

	public void setFaultNoticeCode(String faultNoticeCode) {
		this.faultNoticeCode = faultNoticeCode;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getJt6IDX() {
		return jt6IDX;
	}

	public void setJt6IDX(String jt6IDX) {
		this.jt6IDX = jt6IDX;
	}

	public Integer getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}

	public String getRepairDesc() {
		return repairDesc;
	}

	public void setRepairDesc(String repairDesc) {
		this.repairDesc = repairDesc;
	}

	public Integer getRepairResult() {
		return repairResult;
	}

	public void setRepairResult(Integer repairResult) {
		this.repairResult = repairResult;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getTrackDate() {
		return trackDate;
	}

	public void setTrackDate(Date trackDate) {
		this.trackDate = trackDate;
	}

	public String getTrackPersonIDX() {
		return trackPersonIDX;
	}

	public void setTrackPersonIDX(String trackPersonIDX) {
		this.trackPersonIDX = trackPersonIDX;
	}

	public String getTrackPersonName() {
		return trackPersonName;
	}

	public void setTrackPersonName(String trackPersonName) {
		this.trackPersonName = trackPersonName;
	}

	public String getTrackReason() {
		return trackReason;
	}

	public void setTrackReason(String trackReason) {
		this.trackReason = trackReason;
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

	public String getDId() {
		return dId;
	}

	public void setDId(String id) {
		dId = id;
	}

	public String getDName() {
		return dName;
	}

	public void setDName(String name) {
		dName = name;
	}

}
