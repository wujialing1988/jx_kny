package com.yunda.jx.jxgc.repairrequirement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 查询结果类
 * <li>创建人：林欢
 * <li>创建日期：2016-6-8
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WorkSeqDTO implements java.io.Serializable {
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /** idx主键 */
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
    private String idx;
    /** 工序卡编码 (编号（天津基地）)*/
    @Column(name="Work_Seq_Code")
    private String workSeqCode;
    /** 工序卡名称 （检修工序（天津基地））*/
    @Column(name="Work_Seq_Name")
    private String workSeqName;
    /** 检修范围 */
    @Column(name="Repair_Scope")
    private String repairScope; 
    /** 安全注意事项 */
    @Column(name="Safe_Announcements")
    private String safeAnnouncements;
    /** 备注 */
    private String remark;
    /** 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name="Record_Status")
    private Integer recordStatus;
    /** 站点标识，为了同步数据而使用 */
    @Column(updatable=false)
    private String siteID;
    /** 创建人 */
    @Column(updatable=false)
    private Long creator;
    /** 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Create_Time",updatable=false)
    private java.util.Date createTime;
    /** 修改人 */
    private Long updator;
    /** 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Update_Time")
    private java.util.Date updateTime;
    /** 检修车型主键 */
    @Column(name="P_Train_Type_IDX")
    private String pTrainTypeIDX;
    /** 检修车型简称 */
    @Column(name="P_Train_Type_ShortName")
    private String pTrainTypeShortName;
    
    /** 检修记录单idx */
    @Column(name="record_idx")
    private String recordIDX;
    
    /* 工位主键（JXGC_WORK_STATION） */
    @Transient
    @Column(name="Work_Station_IDX")
    private String workStationIDX;
    
    /* 工位对应工序卡实体主键（WorkStationSet的idx） */
    @Transient
    private String workStationSetIDX;
    /** 顺序号*/
    private Integer seq ;
    /* 检修项目主键 */
    @Transient
    private String repairProjectIdx;
    /* 检修项目对应作业工单主键 */
    @Transient
    private String rpToWsIdx;
    
    /* 检修项目编码 */
    @Column(name="Repair_Project_Code")
    private String repairProjectCode;
    /* 检修项目名称 */
    @Column(name="Repair_Project_Name")
    private String repairProjectName;
    
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(java.util.Date createTime) {
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
    
    public String getPTrainTypeIDX() {
        return pTrainTypeIDX;
    }
    
    public void setPTrainTypeIDX(String trainTypeIDX) {
        pTrainTypeIDX = trainTypeIDX;
    }
    
    public String getPTrainTypeShortName() {
        return pTrainTypeShortName;
    }
    
    public void setPTrainTypeShortName(String trainTypeShortName) {
        pTrainTypeShortName = trainTypeShortName;
    }
    
    public String getRecordIDX() {
        return recordIDX;
    }
    
    public void setRecordIDX(String recordIDX) {
        this.recordIDX = recordIDX;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getRepairProjectCode() {
        return repairProjectCode;
    }
    
    public void setRepairProjectCode(String repairProjectCode) {
        this.repairProjectCode = repairProjectCode;
    }
    
    public String getRepairProjectIdx() {
        return repairProjectIdx;
    }
    
    public void setRepairProjectIdx(String repairProjectIdx) {
        this.repairProjectIdx = repairProjectIdx;
    }
    
    public String getRepairProjectName() {
        return repairProjectName;
    }
    
    public void setRepairProjectName(String repairProjectName) {
        this.repairProjectName = repairProjectName;
    }
    
    public String getRepairScope() {
        return repairScope;
    }
    
    public void setRepairScope(String repairScope) {
        this.repairScope = repairScope;
    }
    
    public String getRpToWsIdx() {
        return rpToWsIdx;
    }
    
    public void setRpToWsIdx(String rpToWsIdx) {
        this.rpToWsIdx = rpToWsIdx;
    }
    
    public String getSafeAnnouncements() {
        return safeAnnouncements;
    }
    
    public void setSafeAnnouncements(String safeAnnouncements) {
        this.safeAnnouncements = safeAnnouncements;
    }
    
    public Integer getSeq() {
        return seq;
    }
    
    public void setSeq(Integer seq) {
        this.seq = seq;
    }
    
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Long getUpdator() {
        return updator;
    }
    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    
    public String getWorkSeqCode() {
        return workSeqCode;
    }
    
    public void setWorkSeqCode(String workSeqCode) {
        this.workSeqCode = workSeqCode;
    }
    
    public String getWorkSeqName() {
        return workSeqName;
    }
    
    public void setWorkSeqName(String workSeqName) {
        this.workSeqName = workSeqName;
    }
    
    public String getWorkStationIDX() {
        return workStationIDX;
    }
    
    public void setWorkStationIDX(String workStationIDX) {
        this.workStationIDX = workStationIDX;
    }
    
    public String getWorkStationSetIDX() {
        return workStationSetIDX;
    }
    
    public void setWorkStationSetIDX(String workStationSetIDX) {
        this.workStationSetIDX = workStationSetIDX;
    }
    
    
    
    
}
