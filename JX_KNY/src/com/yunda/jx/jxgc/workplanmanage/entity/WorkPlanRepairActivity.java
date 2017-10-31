package com.yunda.jx.jxgc.workplanmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkPlanRepairActivity实体类, 数据表：机车检修作业计划-检修活动
 * <li>创建人：程锐
 * <li>创建日期：2015-04-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */
@Entity
@Table(name = "JXGC_Work_Plan_Repair_Activity")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WorkPlanRepairActivity implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 检修活动对应主键 */
    @Column(name = "REPAIR_PROJECT_IDX")
    private String repairProjectIDX;
    
//    /* 作业流程节点主键 */
//    @Column(name = "Node_Case_IDX")
//    private String nodeCaseIDX;
    
    /* 施修任务兑现单主键 */
    @Column(name = "Work_Plan_IDX")
    private String workPlanIDX;
    
    /* 检修活动编码 */
    @Column(name = "Activity_Code")
    private String activityCode;
    
    /* 检修活动名称 */
    @Column(name = "Activity_Name")
    private String activityName;
    
    /* 备注 */
    private String remarks;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    /* 作业任务状态 */
    @Transient
    private String workPlanStatus;
    
    /**
     * @return String 获取检修活动对应主键
     */
    public String getRepairProjectIDX() {
        return repairProjectIDX;
    }
    
    /**
     * @param repairProjectIDX 设置检修活动对应主键
     */
    public void setRepairProjectIDX(String repairProjectIDX) {
        this.repairProjectIDX = repairProjectIDX;
    }
    
//    /**
//     * @return String 获取作业流程节点主键
//     */
//    public String getNodeCaseIDX() {
//        return nodeCaseIDX;
//    }
//    
//    /**
//     * @param nodeCaseIDX 设置作业流程节点主键
//     */
//    public void setNodeCaseIDX(String nodeCaseIDX) {
//        this.nodeCaseIDX = nodeCaseIDX;
//    }
    
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
    
    /**
     * @return String 获取检修活动编码
     */
    public String getActivityCode() {
        return activityCode;
    }
    
    /**
     * @param activityCode 设置检修活动编码
     */
    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }
    
    /**
     * @return String 获取检修活动名称
     */
    public String getActivityName() {
        return activityName;
    }
    
    /**
     * @param activityName 设置检修活动名称
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    
    /**
     * @return String 获取备注
     */
    public String getRemarks() {
        return remarks;
    }
    
    /**
     * @param remarks 设置备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * @return Integer 获取记录状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    /**
     * @param recordStatus 设置记录状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    /**
     * @return String 获取站点标识
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @param siteID 设置站点标识
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @return Long 获取创建人
     */
    public Long getCreator() {
        return creator;
    }
    
    /**
     * @param creator 设置创建人
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    /**
     * @return java.util.Date 获取创建时间
     */
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    /**
     * @param createTime 设置创建时间
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    /**
     * @return Long 获取修改人
     */
    public Long getUpdator() {
        return updator;
    }
    
    /**
     * @param updator 设置修改人
     */
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    
    /**
     * @return java.util.Date 获取修改时间
     */
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * @param updateTime 设置修改时间
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    
    /**
     * @return String idx主键
     */
    public String getIdx() {
        return idx;
    }
    
    /**
     * @param idx 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getWorkPlanStatus() {
        return workPlanStatus;
    }

    
    public void setWorkPlanStatus(String workPlanStatus) {
        this.workPlanStatus = workPlanStatus;
    }
}
