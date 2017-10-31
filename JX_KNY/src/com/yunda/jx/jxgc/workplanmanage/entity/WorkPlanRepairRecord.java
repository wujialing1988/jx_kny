package com.yunda.jx.jxgc.workplanmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

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
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WorkPlanRepairRecord implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    private String idx;
    

    /* 施修任务兑现单主键 */
    @Column(name = "Work_Plan_IDX")
    private String workPlanIDX;
    
    /* 检修活动编码 */
    @Column(name = "Activity_Code")
    private String activityCode;
    
    /* 检修活动名称 */
    @Column(name = "Activity_Name")
    private String activityName;
    
    
    /* 检修活动定义 */
    @Column(name = "repair_project_idx")
    private String repairProjectIDX;
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "record_date")
    private java.util.Date recordDate;


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

    
    public String getWorkPlanIDX() {
        return workPlanIDX;
    }

    
    public void setWorkPlanIDX(String workPlanIDX) {
        this.workPlanIDX = workPlanIDX;
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

    
    public java.util.Date getRecordDate() {
        return recordDate;
    }

    
    public void setRecordDate(java.util.Date recordDate) {
        this.recordDate = recordDate;
    }

    
    public String getRepairProjectIDX() {
        return repairProjectIDX;
    }

    
    public void setRepairProjectIDX(String repairProjectIDX) {
        this.repairProjectIDX = repairProjectIDX;
    }


}
