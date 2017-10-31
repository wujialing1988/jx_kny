package com.yunda.zb.trainonsand.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车上砂实体bean
 * <li>创建人：王利成
 * <li>创建日期：2015-2-5
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
//@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglSandingBean {
    
    /**  类型：long  */
    private static final long serialVersionUID = 1L;

    /**  idx主键  */
    private String idx;
    
    /* 机车出入段台账主键 */
    @Id
    @Column(name = "Train_Access_Account_IDX")
    private String trainAccessAccountIDX;
    
    /* 车型简称 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;
    
    /* 车型主键 */
    @Column(name = "Train_Type_IDX")
    private String trainTypeIDX;
    
    /* 车号 */
    @Column(name = "Train_No")
    private String trainNo;

    /* 车型车号 */
    @Column(name = "Train_Type_No")
    private String trainTypeNo;
    
    /* 责任人 */
    @Column(name = "Duty_PersonId")
    private Long dutyPersonId;
    
    /* 责任人名称 */
    @Column(name = "Duty_PersonName")
    private String dutyPersonName;
    
    /* 上砂开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Start_Time")
    private java.util.Date startTime;
    
    /* 上砂结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "End_Time")
    private java.util.Date endTime;
    
    /* 上砂时间（StartTime-EndTime） */
    @Column(name = "Sanding_Time")
    private Integer sandingTime;
    
    /* 标准上砂时间(配置项预设) */
    @Column(name = "Standard_Sanding_Time")
    private Integer standardSandingTime;

    /* 入库时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "in_time")
    private java.util.Date inTime;
    
    /* 是否超时，0：:否；1：是 */
    @Column(name="Is_OverTime")
    private Integer isOverTime;
    
    /* 入段站场ID */
    @Column(name = "SiteID")
    private String siteID;
    
    /* 入段站场名称 */
    @Column(name = "siteName")
    private String siteName;
     
    /*完成状态--未完成、未完成*/
    @Transient
    private String isFinnish;
    
    /*操作员ID*/
    @Transient
    private Long operatorid;
    
    /* 上砂量 */
    @Column(name = "sand_Num")
    private String sandNum;

    public String getIsFinnish() {
        return isFinnish;
    }

    
    public void setIsFinnish(String isFinnish) {
        this.isFinnish = isFinnish;
    }

    
    public Long getOperatorid() {
        return operatorid;
    }

    
    public void setOperatorid(Long operatorid) {
        this.operatorid = operatorid;
    }


    
    public Long getDutyPersonId() {
        return dutyPersonId;
    }

    
    public Integer getIsOverTime() {
        return isOverTime;
    }


    
    public void setIsOverTime(Integer isOverTime) {
        this.isOverTime = isOverTime;
    }


    public void setDutyPersonId(Long dutyPersonId) {
        this.dutyPersonId = dutyPersonId;
    }


    
    public String getDutyPersonName() {
        return dutyPersonName;
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


    public void setDutyPersonName(String dutyPersonName) {
        this.dutyPersonName = dutyPersonName;
    }


    
    public java.util.Date getEndTime() {
        return endTime;
    }


    
    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
    }


    
    public String getIdx() {
        return idx;
    }


    
    public void setIdx(String idx) {
        this.idx = idx;
    }


    
    public java.util.Date getInTime() {
        return inTime;
    }


    
    public void setInTime(java.util.Date inTime) {
        this.inTime = inTime;
    }


    
    public Integer getSandingTime() {
        return sandingTime;
    }


    
    public void setSandingTime(Integer sandingTime) {
        this.sandingTime = sandingTime;
    }


    
    public Integer getStandardSandingTime() {
        return standardSandingTime;
    }


    
    public void setStandardSandingTime(Integer standardSandingTime) {
        this.standardSandingTime = standardSandingTime;
    }


    
    public java.util.Date getStartTime() {
        return startTime;
    }


    
    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }


    
    public String getTrainAccessAccountIDX() {
        return trainAccessAccountIDX;
    }


    
    public void setTrainAccessAccountIDX(String trainAccessAccountIDX) {
        this.trainAccessAccountIDX = trainAccessAccountIDX;
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


    
    public String getTrainTypeNo() {
        return trainTypeNo;
    }


    
    public void setTrainTypeNo(String trainTypeNo) {
        this.trainTypeNo = trainTypeNo;
    }


    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }


    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }
    
    public String getSandNum() {
        return sandNum;
    }
    
    public void setSandNum(String sandNum) {
        this.sandNum = sandNum;
    }   

}
