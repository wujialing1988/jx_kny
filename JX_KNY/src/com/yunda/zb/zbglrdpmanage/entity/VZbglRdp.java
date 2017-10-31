package com.yunda.zb.zbglrdpmanage.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 下车配件登记 - 机车整备单查询视图类
 * <li>创建人：黄杨
 * <li>创建日期：2016-9-5
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "V_ZB_ZBGL_RDP")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class VZbglRdp implements Serializable{
	
	/* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 状态-整备中 */
    public static final String STATUS_HANDLING = "ONGOING";
    
    /* 状态-整备完成 */
    public static final String STATUS_HANDLED = "COMPLETE";
    
    /* idx主键 */
    @Id
    private String idx;
    
    /* 车型主键 */
    @Column(name = "Train_Type_IDX")
    private String trainTypeIDX;
    
    /* 车型简称 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;
    
    /* 车号 */
    @Column(name = "Train_No")
    private String trainNo;
    
    /* 配属段编码 */
    @Column(name = "D_ID")
    private String dID;
    
    /* 配属段名称 */
    @Column(name = "D_Name")
    private String dName;
    
    /* 整备站场 */
    @Column(updatable = false)
    private String siteID;
    
    /* 整备站场名称 */
    private String siteName;
    
    /* 整备开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Rdp_Start_Time")
    private java.util.Date rdpStartTime;
    
    /* 整备结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Rdp_End_Time")
    private java.util.Date rdpEndTime;
    
    /* 整备单状态 */
    @Column(name = "Rdp_Status")
    private String rdpStatus;
    
    /* 记录状态，1：删除；0：未删除； */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 最新更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /* 下车登记情况统计 */
    @Column(name = "ONLOAD_REGISTER_COUNT")
    private Integer onloadRegisterCount;

    /* 上车登记情况统计 */
    @Column(name = "FIX_REGISTER_COUNT")
    private Integer fixRegisterCount;
    
	public String getDID() {
		return dID;
	}

	public void setDID(String did) {
		dID = did;
	}

	public String getDName() {
		return dName;
	}

	public void setDName(String name) {
		dName = name;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public java.util.Date getRdpEndTime() {
		return rdpEndTime;
	}

	public void setRdpEndTime(java.util.Date rdpEndTime) {
		this.rdpEndTime = rdpEndTime;
	}

	public java.util.Date getRdpStartTime() {
		return rdpStartTime;
	}

	public void setRdpStartTime(java.util.Date rdpStartTime) {
		this.rdpStartTime = rdpStartTime;
	}

	public String getRdpStatus() {
		return rdpStatus;
	}

	public void setRdpStatus(String rdpStatus) {
		this.rdpStatus = rdpStatus;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getOnloadRegisterCount() {
		return onloadRegisterCount;
	}

	public void setOnloadRegisterCount(Integer onloadRegisterCount) {
		this.onloadRegisterCount = onloadRegisterCount;
	}

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
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

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getFixRegisterCount() {
		return fixRegisterCount;
	}

	public void setFixRegisterCount(Integer fixRegisterCount) {
		this.fixRegisterCount = fixRegisterCount;
	}
}
