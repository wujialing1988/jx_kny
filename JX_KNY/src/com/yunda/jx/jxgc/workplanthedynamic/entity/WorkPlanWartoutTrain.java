package com.yunda.jx.jxgc.workplanthedynamic.entity;
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
 * <li>说明: 修峻待离段机车
 * <li>创建人：张迪
 * <li>创建日期：2017-3-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_Work_plan_wartout_train")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WorkPlanWartoutTrain implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 机车检修作业计划主键 */
    @Column(name = "Work_Plan_IDX")
    private String workPlanIDX;

    
    /* 动态生成时间*/
    @Column(name = "Plan_Generate_Date")
    private String planGenerateDate;
    
    /* 入段时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "In_Time")
    private java.util.Date inTime;
    
    /* 修峻时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_Time")
    private java.util.Date endTime;
    
    /* 车型主键 */
    @Column(name = "Train_Type_IDX")
    private String trainTypeIDX;
    
    /* 车号 */
    @Column(name = "Train_No")
    private String trainNo;
    
    /* 车型英文简称 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;
    
    /* 修程编码 */
    @Column(name = "Repair_Class_IDX")
    private String repairClassIDX;
    
    /* 修程名称 */
    @Column(name = "Repair_Class_Name")
    private String repairClassName;
    
    /* 修次 */
    @Column(name = "Repair_time_IDX")
    private String repairtimeIDX;
    
    /* 修次名称 */
    @Column(name = "Repair_time_Name")
    private String repairtimeName;
    
    /* 备注 */
    private String remarks;
    /* 提交状态 */
    @Column(name = "SAVE_STATUS")
    private Integer saveStatus;
    
    /* 配属段ID */
    @Column(name = "D_ID")
    private String dID;
    
    /* 配属段名称 */
    @Column(name = "D_NAME")
    private String dNAME;
    
    /* 委托维修段ID */
    @Column(name = "Delegate_D_ID")
    private String delegateDID;
    
    /* 委托维修段名称 */
    @Column(name = "Delegate_D_Name")
    private String delegateDName;
    
    /* 修改人 */
    private Long updator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
   
   
    /**
     * @return String 获取机车检修作业计划主键
     */
    public String getWorkPlanIDX() {
        return workPlanIDX;
    }
    
    /**
     * @param workPlanIDX 设置机车检修作业计划主键
     */
    public void setWorkPlanIDX(String workPlanIDX) {
        this.workPlanIDX = workPlanIDX;
    }
    
    
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

    
    public String getDelegateDID() {
        return delegateDID;
    }

    
    public void setDelegateDID(String delegateDID) {
        this.delegateDID = delegateDID;
    }

    
    public String getDelegateDName() {
        return delegateDName;
    }

    
    public void setDelegateDName(String delegateDName) {
        this.delegateDName = delegateDName;
    }

    
    public String getDID() {
        return dID;
    }

    
    public void setDID(String did) {
        dID = did;
    }

    
    public String getDNAME() {
        return dNAME;
    }

    
    public void setDNAME(String dname) {
        dNAME = dname;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getPlanGenerateDate() {
        return planGenerateDate;
    }

    
    public void setPlanGenerateDate(String planGenerateDate) {
        this.planGenerateDate = planGenerateDate;
    }

    
    public Integer getRecordStatus() {
        return recordStatus;
    }

    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    
    public String getRemarks() {
        return remarks;
    }

    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    
    public String getRepairClassIDX() {
        return repairClassIDX;
    }

    
    public void setRepairClassIDX(String repairClassIDX) {
        this.repairClassIDX = repairClassIDX;
    }

    
    public String getRepairClassName() {
        return repairClassName;
    }

    
    public void setRepairClassName(String repairClassName) {
        this.repairClassName = repairClassName;
    }

    
    public String getRepairtimeIDX() {
        return repairtimeIDX;
    }

    
    public void setRepairtimeIDX(String repairtimeIDX) {
        this.repairtimeIDX = repairtimeIDX;
    }

    
    public String getRepairtimeName() {
        return repairtimeName;
    }

    
    public void setRepairtimeName(String repairtimeName) {
        this.repairtimeName = repairtimeName;
    }

    
    public String getSiteID() {
        return siteID;
    }

    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
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

    
    public Long getUpdator() {
        return updator;
    }

    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }

    
    public java.util.Date getInTime() {
        return inTime;
    }

    
    public void setInTime(java.util.Date inTime) {
        this.inTime = inTime;
    }

    
    public java.util.Date getEndTime() {
        return endTime;
    }

    
    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
    }

    
    public Integer getSaveStatus() {
        return saveStatus;
    }

    
    public void setSaveStatus(Integer saveStatus) {
        this.saveStatus = saveStatus;
    }
    
  
}
