package com.yunda.jx.third.edo.entity;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 描述项目进度的数据，任务实体类，适用于易度项目管理（甘特图）控件从服务端返回给客户端的json数据结构
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-1-14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class Task {
    
    /** 任务唯一ID */
    private String UID;
    
    /** 序号ID */
    private Integer ID;
    
    /** 任务名称 */
    private String Name;
    
    /** 任务类型:0固定单位,1固定工期 */
    private Integer Type;
    
    /** 创建日期 */
    private String CreateDate;
    
    /** 大纲字段:任务层次以及顺序 */
    private String OutlineNumber;
    
    /** 大纲层级 */
    private Integer OutlineLevel;
    
    /** 任务开始日期 */
    private String Start;
    
    /** 任务完成日期 */
    private String Finish;
    
    /** 工时 */
    private Integer Duration;
    
    /** 工时显示格式化 */
    private Integer DurationFormat;
    
    /** 进度，完成百分比 */
    private Integer PercentComplete;
    
    /** 限制类型 */
    private Integer ConstraintType;
    
    /** 限制日期 */
    private String ConstraintDate;
    
    /** 备注 */
    private String Notes;
    
    /** 里程碑:1是0否 */
    private Integer Milestone;
    
    /** 摘要任务 */
    private Integer Summary;
    
    /** 关键路径 */
    private Integer Critical;
    
    /** 重要级别 */
    private Integer Priority;
    
    /** 节点任务类型，根据检修系统业务自定义扩展 */
    private String NodeType;
    
    /** 处理情况，根据检修系统业务自定义扩展 */
    private String ProcessInfo;
    
    /** 前置任务数组 */
    private PredecessorLink[] PredecessorLink;
    
    /** 任务与资源分配关系 */
    private Assignment[] Assignments;
    
    /** 业务id-节点实例id */
    private String nodeCaseIdx;
    
    /** 任务实际开始日期 */
    private String realStart;
    
    /** 任务实际完成日期 */
    private String realFinish;
    
    /** 工期（甘特图上显示） */
    private BigDecimal workDate;
    
    /** 作业班组（资源名称） */
    private String workTeam;
    
    /** 流水线 */
    private String repairLineName;
    
    /** 工位 */
    private String workStationName;
    
    /** 工位主键 */
    private String workStationIDX;
    
    /** 流程实例主键 */
    private String tecProcessCaseIDX;
    
    /** 流水线排程主键 */
    private String cascadingIDX;
    
    /** 流程节点主键 */
    private String nodeIDX;
    
    /** 父节点IDX */
    private String parentIdx;
    
    /** 是否是最后一级 1是 0否 */
    private Integer isLastLevel;
    
    /** 状态 */
    private String status;
    
    /** 节点来源类型 */
    private String sourceType;
    
    /* 额定工期(单位：分钟) */
    private Double ratedWorkMinutes;
    
    /* 前置延隔时间（分钟） */
    private Double beforeDelayTime;
    
    /* 影响工期 1 影响 0 不影响 */
    private Integer influWorkDate;
    
    /* 计划模式 AUTO:自动，MANUAL：手动 */
    private String planMode;

    /* 工作日历 */
    private String workCalendar;
    
    private Baseline[] Baseline;
    
    private Boolean expanded = true;
    
    /* 前置任务序号 */
    private String PredecessorLinkStr;
    
    /* 延期原因 */
    private String delayReason;
    
    /* 下级工位是否设置完成 1 完成 0 未完成 */
    private Integer isWsComplete ;
    
    public Assignment[] getAssignments() {
        return Assignments;
    }
    
    public void setAssignments(Assignment[] assignments) {
        Assignments = assignments;
    }
    
    public Integer getConstraintType() {
        return ConstraintType;
    }
    
    public void setConstraintType(Integer constraintType) {
        ConstraintType = constraintType;
    }
    
    public Integer getCritical() {
        return Critical;
    }
    
    public void setCritical(Integer critical) {
        Critical = critical;
    }
    
    public Integer getDuration() {
        return Duration;
    }
    
    public void setDuration(Integer duration) {
        Duration = duration;
    }
    
    public Integer getDurationFormat() {
        return DurationFormat;
    }
    
    public void setDurationFormat(Integer durationFormat) {
        DurationFormat = durationFormat;
    }
    
    public Integer getID() {
        return ID;
    }
    
    public void setID(Integer id) {
        ID = id;
    }
    
    public Integer getMilestone() {
        return Milestone;
    }
    
    public void setMilestone(Integer milestone) {
        Milestone = milestone;
    }
    
    public String getName() {
        return Name;
    }
    
    public void setName(String name) {
        Name = name;
    }
    
    public String getNotes() {
        return Notes;
    }
    
    public void setNotes(String notes) {
        Notes = notes;
    }
    
    public Integer getOutlineLevel() {
        return OutlineLevel;
    }
    
    public void setOutlineLevel(Integer outlineLevel) {
        OutlineLevel = outlineLevel;
    }
    
    public String getOutlineNumber() {
        return OutlineNumber;
    }
    
    public void setOutlineNumber(String outlineNumber) {
        OutlineNumber = outlineNumber;
    }
    
    public Integer getPercentComplete() {
        return PercentComplete;
    }
    
    public void setPercentComplete(Integer percentComplete) {
        PercentComplete = percentComplete;
    }
    
    public Integer getPriority() {
        return Priority;
    }
    
    public void setPriority(Integer priority) {
        Priority = priority;
    }
    
    public Integer getSummary() {
        return Summary;
    }
    
    public void setSummary(Integer summary) {
        Summary = summary;
    }
    
    public Integer getType() {
        return Type;
    }
    
    public void setType(Integer type) {
        Type = type;
    }
    
    public String getUID() {
        return UID;
    }
    
    public void setUID(String uid) {
        UID = uid;
    }
    
    public String getNodeType() {
        return NodeType;
    }
    
    public void setNodeType(String nodeType) {
        NodeType = nodeType;
    }
    
    public String getProcessInfo() {
        return ProcessInfo;
    }
    
    public void setProcessInfo(String processInfo) {
        ProcessInfo = processInfo;
    }
    
    public PredecessorLink[] getPredecessorLink() {
        return PredecessorLink;
    }
    
    public void setPredecessorLink(PredecessorLink[] predecessorLink) {
        PredecessorLink = predecessorLink;
    }
    
    public String getConstraintDate() {
        return ConstraintDate;
    }
    
    public void setConstraintDate(String constraintDate) {
        ConstraintDate = constraintDate;
    }
    
    public String getCreateDate() {
        return CreateDate;
    }
    
    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }
    
    public String getFinish() {
        return Finish;
    }
    
    public void setFinish(String finish) {
        Finish = finish;
    }
    
    public String getStart() {
        return Start;
    }
    
    public void setStart(String start) {
        Start = start;
    }
    
    public String getNodeCaseIdx() {
        return nodeCaseIdx;
    }
    
    public void setNodeCaseIdx(String nodeCaseIdx) {
        this.nodeCaseIdx = nodeCaseIdx;
    }
    
    public String getRealFinish() {
        return realFinish;
    }
    
    public void setRealFinish(String realFinish) {
        this.realFinish = realFinish;
    }
    
    public String getRealStart() {
        return realStart;
    }
    
    public void setRealStart(String realStart) {
        this.realStart = realStart;
    }
    
    public BigDecimal getWorkDate() {
        return workDate;
    }
    
    public void setWorkDate(BigDecimal workDate) {
        this.workDate = workDate;
    }
    
    public String getRepairLineName() {
        return repairLineName;
    }
    
    public void setRepairLineName(String repairLineName) {
        this.repairLineName = repairLineName;
    }
    
    public String getWorkStationName() {
        return workStationName;
    }
    
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }
    
    public String getWorkTeam() {
        return workTeam;
    }
    
    public void setWorkTeam(String workTeam) {
        this.workTeam = workTeam;
    }
    
    public String getCascadingIDX() {
        return cascadingIDX;
    }
    
    public void setCascadingIDX(String cascadingIDX) {
        this.cascadingIDX = cascadingIDX;
    }
    
    public String getNodeIDX() {
        return nodeIDX;
    }
    
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }
    
    public String getTecProcessCaseIDX() {
        return tecProcessCaseIDX;
    }
    
    public void setTecProcessCaseIDX(String tecProcessCaseIDX) {
        this.tecProcessCaseIDX = tecProcessCaseIDX;
    }
    
    public String getWorkStationIDX() {
        return workStationIDX;
    }
    
    public void setWorkStationIDX(String workStationIDX) {
        this.workStationIDX = workStationIDX;
    }
    
    public Integer getIsLastLevel() {
        return isLastLevel;
    }
    
    public void setIsLastLevel(Integer isLastLevel) {
        this.isLastLevel = isLastLevel;
    }
    
    public String getParentIdx() {
        return parentIdx;
    }
    
    public void setParentIdx(String parentIdx) {
        this.parentIdx = parentIdx;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getSourceType() {
        return sourceType;
    }
    
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
    
    public Double getBeforeDelayTime() {
        return beforeDelayTime;
    }
    
    public void setBeforeDelayTime(Double beforeDelayTime) {
        this.beforeDelayTime = beforeDelayTime;
    }
    
    public Double getRatedWorkMinutes() {
        return ratedWorkMinutes;
    }
    
    public void setRatedWorkMinutes(Double ratedWorkMinutes) {
        this.ratedWorkMinutes = ratedWorkMinutes;
    }
    
    public Integer getInfluWorkDate() {
        return influWorkDate;
    }
    
    public void setInfluWorkDate(Integer influWorkDate) {
        this.influWorkDate = influWorkDate;
    }
    
    public String getPlanMode() {
        return planMode;
    }
    
    public void setPlanMode(String planMode) {
        this.planMode = planMode;
    }
    
    public String getWorkCalendar() {
        return workCalendar;
    }
    
    public void setWorkCalendar(String workCalendar) {
        this.workCalendar = workCalendar;
    }
    
    public Baseline[] getBaseline() {
        return Baseline;
    }
    
    public void setBaseline(Baseline[] baseline) {
        Baseline = baseline;
    }
    
    
    public Boolean getExpanded() {
        return expanded;
    }

    
    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-8-25
     * <li>修改人： 
     * <li>修改日期：
     */
    public Task() {
        
    }
    
    /**
     * <li>说明：甘特图任务对象带参构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-8-25
     * <li>修改人： 
     * <li>修改日期：
     * @param uid uid
     * @param name 任务名称
     * @param outlineNumber 任务层次以及顺序
     * @param start 计划开始时间
     * @param finish 计划完成时间
     * @param duration 工期
     * @param durationFormat 格式化工期
     * @param percentComplete 进度
     * @param milestone 里程碑
     * @param nodeType 节点任务类型
     * @param processInfo 处理情况
     */
    public Task(String uid, String name, String outlineNumber, String start, String finish, Integer duration, Integer durationFormat, Integer percentComplete, Integer milestone, String nodeType, String processInfo) {
        super();
        UID = uid;
        Name = name;
        OutlineNumber = outlineNumber;
        Start = start;
        Finish = finish;
        Duration = duration;
        DurationFormat = durationFormat;
        PercentComplete = percentComplete;
        Milestone = milestone;
        NodeType = nodeType;
        ProcessInfo = processInfo;
    }
    /**
     * <li>说明：甘特图任务对象带参构造方法(生产计划)
     * <li>创建人：程锐
     * <li>创建日期：2015-8-25
     * <li>修改人： 
     * <li>修改日期：
     * @param uid uid
     * @param name 任务名称
     * @param outlineNumber 任务层次以及顺序
     * @param start 计划开始时间
     * @param finish 计划完成时间
     * @param duration 工期
     * @param durationFormat 格式化工期
     * @param percentComplete 进度
     * @param milestone 里程碑
     */
    public Task(String uid, String name, String outlineNumber, String start, String finish, Integer duration, Integer durationFormat, Integer percentComplete, Integer milestone) {
        super();
        UID = uid;
        Name = name;
        OutlineNumber = outlineNumber;
        Start = start;
        Finish = finish;
        Duration = duration;
        DurationFormat = durationFormat;
        PercentComplete = percentComplete;
        Milestone = milestone;
    }

    
    public String getPredecessorLinkStr() {
        return PredecessorLinkStr;
    }

    
    public void setPredecessorLinkStr(String predecessorLinkStr) {
        PredecessorLinkStr = predecessorLinkStr;
    }

    
    public String getDelayReason() {
        return delayReason;
    }

    
    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }
    
    public Integer getIsWsComplete() {
        return isWsComplete;
    }

    
    public void setIsWsComplete(Integer isWsComplete) {
        this.isWsComplete = isWsComplete;
    }
}	