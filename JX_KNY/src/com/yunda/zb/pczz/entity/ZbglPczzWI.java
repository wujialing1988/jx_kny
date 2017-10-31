package com.yunda.zb.pczz.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.*;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczzWI实体类, 数据表：普查整治任务单
 * <li>创建人：王利成
 * <li>创建日期：2015-03-04
 * <li>修改人: 林欢
 * <li>修改日期：2016-08-18
 * <li>修改内容：普查整治功能重做
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_PCZZ_WI")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglPczzWI implements java.io.Serializable{
    
    /* 状态-待领活 */
    public static final String STATUS_TODO = "TODO";
    public static final String STATUS_TODO_CH = "待领取";
    
    /* 状态-待销活、处理中 */
    public static final String STATUS_HANDLING = "ONGOING";
    public static final String STATUS_HANDLING_CH = "处理中";
    
    /* 状态-销活 */
    public static final String STATUS_HANDLED = "COMPLETE";
    public static final String STATUS_HANDLED_CH = "已处理";
    
    /* 状态-已检查 */
    public static final String STATUS_CHECKED = "CHECKED";
    public static final String STATUS_CHECKED_CH = "已检查";
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 车型主键 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	/* 车型简称 */
	@Column(name="Train_Type_ShortName")
	private String trainTypeShortName;
	/* 车号 */
	@Column(name="Train_No")
	private String trainNo;
	/* 普查整治计划主键 */
	@Column(name="ZBGL_PCZZ_IDX")
	private String zbglPczzIDX;
//	/* 普查整治项主键 */
//	@Column(name="ZBGL_PCZZ_Item_IDX")
//	private String zbglPczzItemIDX;
//	/* 项目名称 */
//	@Column(name="Item_Name")
//	private String itemName;
//	/* 项目内容 */
//	@Column(name="Item_Content")
//	private String itemContent;
//	/* 作业班组代码 */
//	@Column(name="Handle_OrgID")
//	private Long handleOrgID;
//	/* 作业班组名称 */
//	@Column(name="Handle_OrgName")
//	private String handleOrgName;
//	/* 作业人编码 */
//	@Column(name="Handle_Person_ID")
//	private Long handlePersonID;
//	/* 作业人名称 */
//	@Column(name="Handle_Person_Name")
//	private String handlePersonName;
//	/* 领活时间 */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="Fetch_Time")
//	private java.util.Date fetchTime;
//	/* 销活时间 */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="Handle_Time")
//	private java.util.Date handleTime;
	/* 任务单状态 */
	@Column(name="WI_Status")
	private String wIStatus;
	/* 记录状态 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 更新时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;
    /* 整备单id */
    @Column(name="Rdp_Idx")
    private String rdpIdx;
    /* 任务要求 */
    @Column(name="PCZZ_Req")
    private String pczzReq;
    /* 开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Start_Date")
    private Date startDate;
    /* 结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="End_Date")
    private Date endDate;
    /* 普查计划名称 */
    @Column(name="pczz_name")
    private String pczzName;
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getPczzName() {
        return pczzName;
    }
    
    public void setPczzName(String pczzName) {
        this.pczzName = pczzName;
    }
    
    public String getPczzReq() {
        return pczzReq;
    }
    
    public void setPczzReq(String pczzReq) {
        this.pczzReq = pczzReq;
    }
    
    public String getRdpIdx() {
        return rdpIdx;
    }
    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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
    
    public String getWIStatus() {
        return wIStatus;
    }
    
    public void setWIStatus(String status) {
        wIStatus = status;
    }
    
    public String getZbglPczzIDX() {
        return zbglPczzIDX;
    }
    
    public void setZbglPczzIDX(String zbglPczzIDX) {
        this.zbglPczzIDX = zbglPczzIDX;
    }

    
}