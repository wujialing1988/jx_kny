package com.yunda.jx.jxgc.workhis.workplan.entity;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：视图JXGC_JOB_PROCESS_NODE_HIS实体类, 数据表：机车检修作业计划-流程节点历史
 * <li>创建人：程梅
 * <li>创建日期：2015年8月17日
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */
@Entity
@Table(name = "JXGC_JOB_PROCESS_NODE_HIS")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JobProcessNodeHis implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 状态 - 未启动 */
    public static final String STATUS_UNSTART = "NOTSTARTED";
    /** 状态 - 运行 */
    public static final String STATUS_GOING = "RUNNING";
    /** 状态 - 完成 */
    public static final String STATUS_COMPLETE = "COMPLETED";
    /** 状态 - 终止 */
    public static final String STATUS_STOP = "TERMINATED";
    
    /** 计划模式 - 自动 */
    public static final String PLANMODE_AUTO = "AUTO";
    /** 计划模式 - 手动 */
    public static final String PLANMODE_MUNAUL = "MUNAUL";
    /** 计划模式 - 定时 */
    public static final String  PLANMODE_TIMER = "TIMER";
    /* idx主键 */
    @Id
    private String idx;
    
    /* 检修作业流程主键 */
    @Column(name = "Process_IDX")
    private String processIDX;
    
    /* 作业流程节点主键 */
    @Column(name = "Node_IDX")
    private String nodeIDX;
    
    /* 机车检修作业计划主键 */
    @Column(name = "Work_Plan_IDX")
    private String workPlanIDX;
    
    /* 节点名称 */
    @Column(name = "Node_Name")
    private String nodeName;
    
    /* 节点描述 */
    @Column(name = "Node_Desc")
    private String nodeDesc;
    
    /* 节点序号 */
    @Column(name = "Seq_No")
    private Integer seqNo;
    
    /* 额定工期(单位：分钟) */
    @Column(name = "Rated_WorkMinutes")
    private Double ratedWorkMinutes;
    
    /* 实际工期(单位：分钟) */
    @Column(name = "Real_WorkMinutes")
    private Double realWorkMinutes;
    
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
    private java.util.Date realEndTime;
    
    /* 状态:未启动、运行、完成、终止（数据字典） */
    private String status;
    
    /* 作业班组 */
    @Column(name = "WORK_STATION_BELONG_TEAM_NAME")
    private String workStationBelongTeamName;
    
    /* 作业班组主键 */
    @Column(name = "work_station_belong_team")
    private Long workStationBelongTeam;
    
    /* 工位主键 */
    @Column(name = "WORK_STATION_IDX")
    private String workStationIDX;
    
    /* 工位名称 */
    @Column(name = "Work_Station_Name")
    private String workStationName;
    
    /* 延迟消息提醒（1：已提醒， 0：未提醒） */
    @Column(name = "MSG_REMIND")
    private Integer msgRemind;
    
    /* 父级节点 */
    @Column(name = "Parent_IDX")
    private String parentIDX;
    
    /* 是否叶子节点,0:否；1：是 */
    @Column(name = "Is_Leaf")
    private Integer isLeaf;
    
    /* 日历 */
    @Column(name = "Work_Calendar_IDX")
    private String workCalendarIDX;
    
    /* 计划模式 AUTO:自动，MANUAL：手动 */
    @Column(name = "Plan_Mode")
    private String planMode;
    
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
    
    /* 流水线分组ID */
    @Transient
    private String groupId;
    
    /* 多节点实例ID字符串 */
    @Transient
    private String idxs;
    
    /* 流水线主键 */
    @Transient
    private String repairLineIDX;
    
    // 流水线名称
    @Transient
    private String repairLineName;
    
    // 延搁时间
    @Transient
    private Double delayTime;
    
    // 该节点作业的完成百分比
    @Transient
    private String completePercent;
    
    /* 作业流程名称 */
    @Transient
    private String processCaseName;
    
    /* 修次名称 */
    @Transient
    private String repairTimeName;
    
    /* 修程名称 */
    @Transient
    private String repairClassName;
    
    /* 计划开工时间 */
    @Transient
    private String planBeginTimeStr;
    
    /* 计划完工时间 */
    @Transient
    private String planEndTimeStr;
    
    /* 实际开工时间 */
    @Transient
    private String realBeginTimeStr;
    
    /* 实际完工时间 */
    @Transient
    private String realEndTimeStr;
    
    /* 工序延误时间 */
    @Transient
    private String delayTimeStr;
    
    /* 推迟原因 */
    @Transient
    private String delayReason;
    
    /* 延迟类型 */
    @Transient
    private String delayType;
    
    // 班组
    @Transient
    private String teamOrgName;
    
    /* 机车检修作业计划状态 */
    @Transient
    private String workPlanStatus;
    
    /* 作业人员 */
    @Transient
    private String workerNames;
    
    /* 生产任务单主键 */
    @Transient
    private String rdpIDX;
    
    /* 作业流程主键 */
    @Transient
    private String tecProcessCaseIDX;
    
    /*在未填写延误时间前存储延误时间*/
    @Transient
    private String tempDelayTime;
    
    /*记录是修改的这个节点，默认为false未修改，true为修改*/
    @Transient
    private boolean isThisChange = false;
    
    /**
     * @return String 获取检修作业流程主键
     */
    public String getProcessIDX() {
        return processIDX;
    }
    
    /**
     * @param processIDX 设置检修作业流程主键
     */
    public void setProcessIDX(String processIDX) {
        this.processIDX = processIDX;
    }
    
    /**
     * @return String 获取作业流程节点主键
     */
    public String getNodeIDX() {
        return nodeIDX;
    }
    
    /**
     * @param nodeIDX 设置作业流程节点主键
     */
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }
    
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
     * @return String 获取节点名称
     */
    public String getNodeName() {
        return nodeName;
    }
    
    /**
     * @param nodeName 设置节点名称
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
    /**
     * @return String 获取节点描述
     */
    public String getNodeDesc() {
        return nodeDesc;
    }
    
    /**
     * @param nodeDesc 设置节点描述
     */
    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }
    
    /**
     * @return Integer 获取顺序号
     */
    public Integer getSeqNo() {
        return seqNo;
    }
    
    /**
     * @param seqNo 设置顺序号
     */
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
    /**
     * @return Double 获取额定工期
     */
    public Double getRatedWorkMinutes() {
        return ratedWorkMinutes;
    }
    
    /**
     * @param ratedWorkMinutes 设置额定工期
     */
    public void setRatedWorkMinutes(Double ratedWorkMinutes) {
        this.ratedWorkMinutes = ratedWorkMinutes;
    }
    
    /**
     * @return Double 获取实际工期
     */
    public Double getRealWorkMinutes() {
        return realWorkMinutes;
    }
    
    /**
     * @param realWorkMinutes 设置实际工期
     */
    public void setRealWorkMinutes(Double realWorkMinutes) {
        this.realWorkMinutes = realWorkMinutes;
    }
    
    /**
     * @return java.util.Date 获取计划开工时间
     */
    public java.util.Date getPlanBeginTime() {
        return planBeginTime;
    }
    
    /**
     * @param planBeginTime 设置计划开工时间
     */
    public void setPlanBeginTime(java.util.Date planBeginTime) {
        this.planBeginTime = planBeginTime;
    }
    
    /**
     * @return java.util.Date 获取计划完工时间
     */
    public java.util.Date getPlanEndTime() {
        return planEndTime;
    }
    
    /**
     * @param planEndTime 设置计划完工时间
     */
    public void setPlanEndTime(java.util.Date planEndTime) {
        this.planEndTime = planEndTime;
    }
    
    /**
     * @return java.util.Date 获取实际开工时间
     */
    public java.util.Date getRealBeginTime() {
        return realBeginTime;
    }
    
    /**
     * @param realBeginTime 设置实际开工时间
     */
    public void setRealBeginTime(java.util.Date realBeginTime) {
        this.realBeginTime = realBeginTime;
    }
    
    /**
     * @return java.util.Date 获取实际完工时间
     */
    public java.util.Date getRealEndTime() {
        return realEndTime;
    }
    
    /**
     * @param realEndTime 设置实际完工时间
     */
    public void setRealEndTime(java.util.Date realEndTime) {
        this.realEndTime = realEndTime;
    }
    
    /**
     * @return String 获取状态
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * @param status 设置状态
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * @return String 获取作业班组
     */
    public String getWorkStationBelongTeamName() {
        return workStationBelongTeamName;
    }
    
    /**
     * @param workStationBelongTeamName 设置作业班组
     */
    public void setWorkStationBelongTeamName(String workStationBelongTeamName) {
        this.workStationBelongTeamName = workStationBelongTeamName;
    }
    
    /**
     * @return Long 获取作业班组主键
     */
    public Long getWorkStationBelongTeam() {
        return workStationBelongTeam;
    }
    
    /**
     * @param workStationBelongTeam 设置作业班组主键
     */
    public void setWorkStationBelongTeam(Long workStationBelongTeam) {
        this.workStationBelongTeam = workStationBelongTeam;
    }
    
    /**
     * @return String 获取工位主键
     */
    public String getWorkStationIDX() {
        return workStationIDX;
    }
    
    /**
     * @param workStationIDX 设置工位主键
     */
    public void setWorkStationIDX(String workStationIDX) {
        this.workStationIDX = workStationIDX;
    }
    
    /**
     * @return String 获取工位名称
     */
    public String getWorkStationName() {
        return workStationName;
    }
    
    /**
     * @param workStationName 设置工位名称
     */
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }
    
    /**
     * @return Integer 获取延迟消息提醒
     */
    public Integer getMsgRemind() {
        return msgRemind;
    }
    
    /**
     * @param msgRemind 设置延迟消息提醒
     */
    public void setMsgRemind(Integer msgRemind) {
        this.msgRemind = msgRemind;
    }
    
    /**
     * @return String 获取父级节点
     */
    public String getParentIDX() {
        return parentIDX;
    }
    
    /**
     * @param parentIDX 设置父级节点
     */
    public void setParentIDX(String parentIDX) {
        this.parentIDX = parentIDX;
    }
    
    /**
     * @return Integer 获取是否叶子节点
     */
    public Integer getIsLeaf() {
        return isLeaf;
    }
    
    /**
     * @param isLeaf 设置是否叶子节点
     */
    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }
    
    /**
     * @return String 获取日历
     */
    public String getWorkCalendarIDX() {
        return workCalendarIDX;
    }
    
    /**
     * @param workCalendarIDX 设置日历
     */
    public void setWorkCalendarIDX(String workCalendarIDX) {
        this.workCalendarIDX = workCalendarIDX;
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
    
    public String getGroupId() {
        return groupId;
    }
    
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    public String getIdxs() {
        return idxs;
    }
    
    public void setIdxs(String idxs) {
        this.idxs = idxs;
    }
    
    public String getRepairLineIDX() {
        return repairLineIDX;
    }
    
    public void setRepairLineIDX(String repairLineIDX) {
        this.repairLineIDX = repairLineIDX;
    }
    
    public String getRepairLineName() {
        return repairLineName;
    }
    
    public void setRepairLineName(String repairLineName) {
        this.repairLineName = repairLineName;
    }
    
    public String getCompletePercent() {
        return completePercent;
    }
    
    public void setCompletePercent(String completePercent) {
        this.completePercent = completePercent;
    }
    
    public String getPlanMode() {
        return planMode;
    }
    
    public void setPlanMode(String planMode) {
        this.planMode = planMode;
    }
    
    public Double getDelayTime() {
        return delayTime;
    }
    
    public void setDelayTime(Double delayTime) {
        this.delayTime = delayTime;
    }
    
    public String getDelayReason() {
        return delayReason;
    }
    
    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }
    
    public String getDelayTimeStr() {
        return delayTimeStr;
    }
    
    public void setDelayTimeStr(String delayTimeStr) {
        this.delayTimeStr = delayTimeStr;
    }
    
    public String getDelayType() {
        return delayType;
    }
    
    public void setDelayType(String delayType) {
        this.delayType = delayType;
    }
    
    public String getPlanBeginTimeStr() {
        return planBeginTimeStr;
    }
    
    public void setPlanBeginTimeStr(String planBeginTimeStr) {
        this.planBeginTimeStr = planBeginTimeStr;
    }
    
    public String getPlanEndTimeStr() {
        return planEndTimeStr;
    }
    
    public void setPlanEndTimeStr(String planEndTimeStr) {
        this.planEndTimeStr = planEndTimeStr;
    }
    
    public String getProcessCaseName() {
        return processCaseName;
    }
    
    public void setProcessCaseName(String processCaseName) {
        this.processCaseName = processCaseName;
    }
    
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
    public String getRealBeginTimeStr() {
        return realBeginTimeStr;
    }
    
    public void setRealBeginTimeStr(String realBeginTimeStr) {
        this.realBeginTimeStr = realBeginTimeStr;
    }
    
    public String getRealEndTimeStr() {
        return realEndTimeStr;
    }
    
    public void setRealEndTimeStr(String realEndTimeStr) {
        this.realEndTimeStr = realEndTimeStr;
    }
    
    public String getRepairClassName() {
        return repairClassName;
    }
    
    public void setRepairClassName(String repairClassName) {
        this.repairClassName = repairClassName;
    }
    
    public String getRepairTimeName() {
        return repairTimeName;
    }
    
    public void setRepairTimeName(String repairTimeName) {
        this.repairTimeName = repairTimeName;
    }
    
    public String getTeamOrgName() {
        return teamOrgName;
    }
    
    public void setTeamOrgName(String teamOrgName) {
        this.teamOrgName = teamOrgName;
    }
    
    public String getWorkerNames() {
        return workerNames;
    }
    
    public void setWorkerNames(String workerNames) {
        this.workerNames = workerNames;
    }
    
    public String getWorkPlanStatus() {
        return workPlanStatus;
    }
    
    public void setWorkPlanStatus(String workPlanStatus) {
        this.workPlanStatus = workPlanStatus;
    }
    
    public String getTecProcessCaseIDX() {
        return tecProcessCaseIDX;
    }
    
    public void setTecProcessCaseIDX(String tecProcessCaseIDX) {
        this.tecProcessCaseIDX = tecProcessCaseIDX;
    }
    
    public String getTempDelayTime() {
        return tempDelayTime;
    }
    
    public void setTempDelayTime(String tempDelayTime) {
        this.tempDelayTime = tempDelayTime;
    }
    
    public boolean isThisChange() {
        return isThisChange;
    }
    
    public void setThisChange(boolean isThisChange) {
        this.isThisChange = isThisChange;
    }

    /**
     * <li>说明：获取流程节点状态中文含义
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param status 状态值
     * @return 状态中文含义
     */
    public static String getStatusMeaning(String status) {
        if (status.equals(STATUS_UNSTART)) {
            return "未启动";
        } else if (status.equals(STATUS_GOING)) {
            return "运行";
        } else if (status.equals(STATUS_COMPLETE)) {
            return "完成";
        } else if (status.equals(STATUS_STOP)) {
            return "终止";
        }
        return "";
    }
    
    /** **************** added by hetao: start **************** */
    /* 车型英文简称 */
    @Transient
    private String trainTypeShortName;
    
    /* 车号 */
    @Transient
    private String trainNo;
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：何涛
     * <li>创建日期：2015-4-29
     * <li>修改人： 
     * <li>修改日期：
     */
    public JobProcessNodeHis() {
        super();
    }

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：何涛
     * <li>创建日期：2015-4-29
     * <li>修改人： 
     * <li>修改日期：
     * @param idx idx主键
     * @param processIDX 流程定义主键 
     * @param nodeIDX 节点定义主键
     * @param workPlanIDX 机车检修作业计划主键
     * @param nodeName 节点名称
     * @param nodeDesc 节点描述
     * @param seqNo 顺序号
     * @param ratedWorkMinutes 额定工期(单位：分钟) 
     * @param realWorkMinutes 实际工期
     * @param planBeginTime 计划开始时间
     * @param planEndTime 计划结束时间
     * @param realBeginTime 实际开始时间
     * @param realEndTime 时间结束时间
     * @param status 状态
     * @param isLeaf 是否叶子节点
     * @param planMode 计划模式 AUTO:自动，MANUAL：手动
     * @param workStationIDX 工位主键
     * @param trainTypeShortName 车型简称
     * @param trainNo 车号
     */
    public JobProcessNodeHis(String idx, String processIDX, String nodeIDX, String workPlanIDX, String nodeName, String nodeDesc, Integer seqNo, Double ratedWorkMinutes, Double realWorkMinutes, Date planBeginTime, Date planEndTime, Date realBeginTime, Date realEndTime, String status, Integer isLeaf, String planMode, String workStationIDX, String trainTypeShortName, String trainNo) {
        super();
        this.idx = idx;
        this.processIDX = processIDX;
        this.nodeIDX = nodeIDX;
        this.workPlanIDX = workPlanIDX;
        this.nodeName = nodeName;
        this.nodeDesc = nodeDesc;
        this.seqNo = seqNo;
        this.ratedWorkMinutes = ratedWorkMinutes;
        this.realWorkMinutes = realWorkMinutes;
        this.planBeginTime = planBeginTime;
        this.planEndTime = planEndTime;
        this.realBeginTime = realBeginTime;
        this.realEndTime = realEndTime;
        this.status = status;
        this.isLeaf = isLeaf;
        this.planMode = planMode;
        this.workStationIDX = workStationIDX;
        this.trainTypeShortName = trainTypeShortName;
        this.trainNo = trainNo;
    }

    /**
     * @return 获取车号
     */
    public String getTrainNo() {
        return trainNo;
    }

    /**
     * @param trainNo 设置车号
     */
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    /**
     * @return 设置车型简称
     */
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    /**
     * @param trainTypeShortName 设置车型简称
     */
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }
    
    /**
     * <li>说明： 判断两个节点实体是否可以合并
     * <li>创建人：何涛
     * <li>创建日期：2015-4-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param o 流程节点历史对象
     * @return 是否可以合并 true:是 false:否
     */
    public boolean canCombined(JobProcessNodeHis o) {
        // 必须是同一个工作计划、同一个台位的作业节点才可以合并
        if (!this.workPlanIDX.equals(o.workPlanIDX) || !this.workStationIDX.equals(o.workStationIDX)) {
            return false;
        }
        // 1 对象o的开始时间在this的时间段内
        if (o.planBeginTime.after(this.planBeginTime) && o.planBeginTime.before(this.planEndTime)) {
            return true;
        }
        // 2 对象o的结束时间在this的时间段内
        if (o.planEndTime.after(this.planBeginTime) && o.planEndTime.before(this.planEndTime)) {
            return true;
        }
        // 3 this的开始时间在对象o的时间段内
        if (this.planBeginTime.after(o.planBeginTime) && this.planBeginTime.before(o.planEndTime)) {
            return true;
        }
        // 4 this的结束时间在对象o的时间段内
        if (this.planEndTime.after(o.planBeginTime) && this.planEndTime.before(o.planEndTime)) {
            return true;
        }
        // 5 this的结束时间等于对象o的开始时间、或者this的开始时间等于对象o的结束时间
        if (this.planEndTime.equals(o.planBeginTime) || this.planBeginTime.equals(o.planEndTime)) {
            return true;
        }
        // 6 this的开始结束时间等于对象o的开始结束时间
        if (this.planBeginTime.equals(o.planBeginTime) && this.planEndTime.equals(o.planEndTime)) {
            return true;
        }
        return false;
    }
    
    /**
     * <li>说明：获取一个机车检修作业流程节点的延期时间
     * <li>创建人：何涛
     * <li>创建日期：2015-5-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param currentTime 当前时间
     * @return 延期时间(单位分钟)
     */
    public long getDelayTime(Date currentTime) {
        // 未开工的不计算延期时间
        @SuppressWarnings("hiding")
        long delayTime = -1;
        if (STATUS_UNSTART.equals(status)) {
            return delayTime;
        }
        // 已完工的按实际结束时间和计划结束时间做对比
        if (STATUS_COMPLETE.equals(status)) {
            if (null != realEndTime && null != planEndTime && realEndTime.after(planEndTime)) {
                delayTime = realEndTime.getTime() - planEndTime.getTime();
            }
        // 未完工的按计划结束时间和当前时间做对比
        } else if (null != planEndTime && currentTime.after(planEndTime) && !STATUS_STOP.equals(status)) {
            delayTime = currentTime.getTime() - planEndTime.getTime();
        }
        return delayTime <= 0 ? delayTime : BigDecimal.valueOf(delayTime).divide(BigDecimal.valueOf(1000 * 60), 0, BigDecimal.ROUND_HALF_UP).longValue();
    }
    /** **************** added by hetao: end **************** */
    
}
