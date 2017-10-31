package com.yunda.jx.jxgc.workplanmanage.entity;
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
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 生产任务进度统计历史
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-2-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_JOB_PROCESS_NODES")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JobProcessNodeStatistics implements java.io.Serializable {
    
    /**  类型：long  */
    private static final long serialVersionUID = 1L;

    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
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
    
    /* 计划开工时间 --- 修改后待确认时间*/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "new_Plan_Begin_Time")
    private java.util.Date newPlanBeginTime;
    
    /* 计划完工时间 -- 修改后待确认时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "new_Plan_End_Time")
    private java.util.Date newPlanEndTime;
    
    /* 状态:0 确认、1 待确认 */
    @Column(name = "Edit_Status")
    private Integer editStatus;
    
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
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
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
    
    /* 定义的开始时间 */
    @Column(name="START_TIME")
    private String startTime;
    /* 定义的结束时间 */
    @Column(name="END_TIME")
    private String endTime;
    /* 设定延误时间 */
    @Column(name="RELAY_TIME")
    private String relayTime;
    /* 开始天数 */
    @Column(name="START_DAY")
    private Integer startDay;
    /* 结束天数 */
    @Column(name="END_DAY")
    private Integer endDay;
    
    /* 基线开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "base_line_start_time")
    private java.util.Date baseLineStartTime;
    
    /* 基线结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "base_line_end_time")
    private java.util.Date baseLineEndTime;
    
    /* 保存日期 */
    @Column(name="DATE_STR")
    private String dateStr;
    
    /* 延期原因 */
    @Column(name = "delay_reason")
    private String delayReason;
    
    
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
    
  
    
    public String getPlanMode() {
        return planMode;
    }
    
    public void setPlanMode(String planMode) {
        this.planMode = planMode;
    }
    
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：何涛
     * <li>创建日期：2015-4-29
     * <li>修改人： 
     * <li>修改日期：
     */
    public JobProcessNodeStatistics() {
        super();
    }

    
    public java.util.Date getBaseLineEndTime() {
        return baseLineEndTime;
    }


    
    public void setBaseLineEndTime(java.util.Date baseLineEndTime) {
        this.baseLineEndTime = baseLineEndTime;
    }


    
    public java.util.Date getBaseLineStartTime() {
        return baseLineStartTime;
    }


    
    public void setBaseLineStartTime(java.util.Date baseLineStartTime) {
        this.baseLineStartTime = baseLineStartTime;
    }

    
    public Integer getEndDay() {
        return endDay;
    }


    
    public void setEndDay(Integer endDay) {
        this.endDay = endDay;
    }


    
    public String getEndTime() {
        return endTime;
    }


    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    
    public Integer getStartDay() {
        return startDay;
    }


    
    public void setStartDay(Integer startDay) {
        this.startDay = startDay;
    }


    
    public String getStartTime() {
        return startTime;
    }


    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    
    public String getRelayTime() {
        return relayTime;
    }


    
    public void setRelayTime(String relayTime) {
        this.relayTime = relayTime;
    }


    
    public Integer getEditStatus() {
        return editStatus;
    }


    
    public void setEditStatus(Integer editStatus) {
        this.editStatus = editStatus;
    }


    
    public java.util.Date getNewPlanBeginTime() {
        return newPlanBeginTime;
    }


    
    public void setNewPlanBeginTime(java.util.Date newPlanBeginTime) {
        this.newPlanBeginTime = newPlanBeginTime;
    }


    
    public java.util.Date getNewPlanEndTime() {
        return newPlanEndTime;
    }


    
    public void setNewPlanEndTime(java.util.Date newPlanEndTime) {
        this.newPlanEndTime = newPlanEndTime;
    }

    
    public String getDateStr() {
        return dateStr;
    }

    
    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    
    public String getDelayReason() {
        return delayReason;
    }

    
    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }
    
    

}
