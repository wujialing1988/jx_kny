package com.yunda.jx.jxgc.webservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车检修作业计划流程节点包装类
 * <li>创建人：张迪
 * <li>创建日期：2016-6-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
public class TrainWorkPlanNodeBean implements java.io.Serializable {

    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /* 节点主键 */
    @Id
    private String idx;
    /* 节点名称 */
    @Column(name = "Node_Name")
    private String nodeName;   
    /* 作业流程名称 */
    @Column(name = "Process_Name")
    private String processName;
    
    /* 额定工期(单位：小时) */
    @Column(name = "Rated_WorkHours")
    private Double ratedWorkHours;
    
    /* 车号 */
    @Column(name = "Train_No")
    private String trainNo;
    /* 车型id */
    @Column(name = "TRAIN_TYPE_IDX")
    private String trainTypeIdx;
    
    /* 车型英文简称 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;

    
    /* 修程名称 */
    @Column(name = "Repair_Class_Name")
    private String repairClassName;

    
    /* 修次名称 */
    @Column(name = "Repair_time_Name")
    private String repairtimeName;

    /* 节点状态 状态：NOTSTARTED未启动RUNNING运行中COMPLETED已处理 */
    @Column(name = "Status")
    private String status;
    
    /* 计划开工时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Plan_Begin_Time")
    private java.util.Date planBeginTime;
    
    /* 计划完工时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Plan_End_Time")
    private java.util.Date planEndTime;
    
    /* 实际开工时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Real_Begin_Time")
    private java.util.Date realBeginTime;
    
    /* 实际完工时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Real_End_Time")
    private java.util.Date realEndTime;;
 
    /* 日历 */
    @Column(name = "Work_Calendar_Name")
    private String workCalendarName;

    
    /* 配属段名称 */
    @Column(name = "D_NAME")
    private String dNAME;
    /* 工单数量 */
    @Column(name = "Card_Count")
    private String CardCount;

    
    /* 委托维修段名称 */
    @Column(name = "Delegate_D_Name")
    private String delegateDName;
    
    /* 延期类型*/
    @Column(name = "DELAY_Type")
    private String delayType;
    
    /* 延期原因 */
    @Column(name = "DELAY_REASON")
    private String delayReason;
    /* 延期实体id */
    @Column(name = "Delay_Idx")
    private String delayIdx;
 
    
    /* 机车检修作业计划计划开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Rdp_Plan_Begin_Time")
    private java.util.Date rdpPlanBeginTime;
    
    /* 机车检修作业计划计划完成时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Rdp_Plan_End_Time")
    private java.util.Date rdpPlanEndTime;
    /* 工位主键 */
    @Column(name = "WORK_STATION_IDX")
    private String workStationIDX;
    
    /* 工位名称 */
    @Column(name = "Work_Station_Name")
    private String workStationName;
    
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType; 
    
    
    public String getWorkStationIDX() {
        return workStationIDX;
    }


    
    public void setWorkStationIDX(String workStationIDX) {
        this.workStationIDX = workStationIDX;
    }


    
    public String getWorkStationName() {
        return workStationName;
    }


    
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }


    public String getDelegateDName() {
        return delegateDName;
    }

    
    public void setDelegateDName(String delegateDName) {
        this.delegateDName = delegateDName;
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

    
    public String getNodeName() {
        return nodeName;
    }

    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    
    public java.util.Date getPlanBeginTime() {
        return planBeginTime;
    }

    
    public void setPlanBeginTime(java.util.Date planBeginTime) {
        this.planBeginTime = planBeginTime;
    }

    
    public java.util.Date getPlanEndTime() {
        return planEndTime;
    }

    
    public void setPlanEndTime(java.util.Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    
    public String getProcessName() {
        return processName;
    }

    
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    
    public java.util.Date getRdpPlanBeginTime() {
        return rdpPlanBeginTime;
    }

    
    public void setRdpPlanBeginTime(java.util.Date rdpPlanBeginTime) {
        this.rdpPlanBeginTime = rdpPlanBeginTime;
    }

    
    public java.util.Date getRdpPlanEndTime() {
        return rdpPlanEndTime;
    }

    
    public void setRdpPlanEndTime(java.util.Date rdpPlanEndTime) {
        this.rdpPlanEndTime = rdpPlanEndTime;
    }

    
    public java.util.Date getRealBeginTime() {
        return realBeginTime;
    }

    
    public void setRealBeginTime(java.util.Date realBeginTime) {
        this.realBeginTime = realBeginTime;
    }

    
    public java.util.Date getRealEndTime() {
        return realEndTime;
    }

    
    public void setRealEndTime(java.util.Date realEndTime) {
        this.realEndTime = realEndTime;
    }

    
    public String getRepairClassName() {
        return repairClassName;
    }

    
    public void setRepairClassName(String repairClassName) {
        this.repairClassName = repairClassName;
    }

    
    public String getRepairtimeName() {
        return repairtimeName;
    }

    
    public void setRepairtimeName(String repairtimeName) {
        this.repairtimeName = repairtimeName;
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

    
    public String getWorkCalendarName() {
        return workCalendarName;
    }

    
    public void setWorkCalendarName(String workCalendarName) {
        this.workCalendarName = workCalendarName;
    }

    public Double getRatedWorkHours() {
        return ratedWorkHours;
    }

  
    public void setRatedWorkHours(Double ratedWorkHours) {
        this.ratedWorkHours = ratedWorkHours;
    }

  
    public String getDelayReason() {
        return delayReason;
    }
    
    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
    }


    
    public String getDelayIdx() {
        return delayIdx;
    }


    
    public void setDelayIdx(String delayIdx) {
        this.delayIdx = delayIdx;
    }


    
    public String getTrainTypeIdx() {
        return trainTypeIdx;
    }


    
    public void setTrainTypeIdx(String trainTypeIdx) {
        this.trainTypeIdx = trainTypeIdx;
    }


    
    public String getCardCount() {
        return CardCount;
    }


    
    public void setCardCount(String cardCount) {
        CardCount = cardCount;
    }



    
    public String getDelayType() {
        return delayType;
    }



    
    public void setDelayType(String delayType) {
        this.delayType = delayType;
    }



    
    public String getVehicleType() {
        return vehicleType;
    }



    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    

}
