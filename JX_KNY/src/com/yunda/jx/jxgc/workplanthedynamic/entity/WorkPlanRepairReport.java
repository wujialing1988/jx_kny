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
 * <li>说明: 任务完成修程统计表
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
@Table(name = "jxgc_work_plan_repair_report")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WorkPlanRepairReport implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;

    
    /* 动态生成时间*/
    @Column(name = "Plan_Generate_Date")
    private String planGenerateDate;
    
   
    /* 车型主键 */
    @Column(name = "Train_Type_IDX")
    private String trainTypeIDX;
    
    /* 车型英文简称 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;
    
    
    /* 修程编码 */
    @Column(name = "Repair_Class_IDX")
    private String repairClassIDX;
    
    /* 修程名称 */
    @Column(name = "Repair_Class_Name")
    private String repairClassName;
    
    
    /* 修程数量 */
    @Column(name = "COUNT")
    private Integer count;
    /* 年计划数量 */
    @Column(name = "YEAR_PLAN_COUNT")
    private Integer yearPlanCount;
    
    /* 提交状态 */
    @Column(name = "SAVE_STATUS")
    private Integer saveStatus;
    
     
     
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

    
    public String getSiteID() {
        return siteID;
    }

    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
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


    
    public Integer getCount() {
        return count;
    }


    
    public void setCount(Integer count) {
        this.count = count;
    }


    
    public Integer getSaveStatus() {
        return saveStatus;
    }


    
    public void setSaveStatus(Integer saveStatus) {
        this.saveStatus = saveStatus;
    }


    
    public Integer getYearPlanCount() {
        return yearPlanCount;
    }


    
    public void setYearPlanCount(Integer yearPlanCount) {
        this.yearPlanCount = yearPlanCount;
    }
   
  
  
}
