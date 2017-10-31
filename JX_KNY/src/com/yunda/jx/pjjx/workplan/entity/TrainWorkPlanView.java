package com.yunda.jx.pjjx.workplan.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：视图V_JXGC_TRAIN_WORK_PLAN实体类, 数据表：机车检修作业计划
 * <li>创建人：程梅
 * <li>创建日期：2015年11月3日
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */
@Entity
@Table(name = "V_JXGC_TRAIN_WORK_PLAN")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainWorkPlanView implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 状态-未启动 */
    public static final String STATUS_NEW = "INITIALIZE";
    
    /* 状态-处理中 */
    public static final String STATUS_HANDLING = "ONGOING";
    
    /* 状态-已处理 */
    public static final String STATUS_HANDLED = "COMPLETE";
    
    /* 状态-终止 */
    public static final String STATUS_NULLIFY = "TERMINATED";
    
    /* idx主键 */
    @Id
    private String idx;
    
    /* 作业流程主键 */
    @Column(name = "Process_IDX")
    private String processIdx;
    
    /* 作业流程名称 */
    @Column(name = "Process_Name")
    private String processName;
    
    /* 作业流程额定工期（小时） */
    @Column(name = "RATED_WORKDAY")
    private Double ratedWorkDay;
    
    /* 车型主键 */
    @Column(name = "Train_Type_IDX")
    private String trainTypeIdx;
    
    /* 车号 */
    @Column(name = "Train_No")
    private String trainNo;
    
    /* 车型英文简称 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;
    
    /* 修程编码 */
    @Column(name = "Repair_Class_IDX")
    private String repairClassIdx;
    
    /* 修程名称 */
    @Column(name = "Repair_Class_Name")
    private String repairClassName;
    
    /* 修次 */
    @Column(name = "Repair_time_IDX")
    private String repairtimeIdx;
    
    /* 修次名称 */
    @Column(name = "Repair_time_Name")
    private String repairtimeName;
    
    /* 机车作业计划状态，新增；处理中；已处理；作废（数据字典） */
    @Column(name = "Work_Plan_Status")
    private String workPlanStatus;
    
    /* 计划开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Plan_Begin_Time")
    private java.util.Date planBeginTime;
    
    /* 计划完成时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Plan_End_Time")
    private java.util.Date planEndTime;
    
    /* 实际开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Begin_Time")
    private java.util.Date beginTime;
    
    /* 实际完成时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "End_Time")
    private java.util.Date endTime;
    
    /* 计划生成时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Work_Plan_Time")
    private java.util.Date workPlanTime;
    
    /* 备注 */
    private String remarks;
    
    /* 日历 */
    @Column(name = "Work_Calendar_IDX")
    private String workCalendarIdx;
    
    /* 配属段ID */
    @Column(name = "D_ID")
    private String dId;
    
    /* 配属段名称 */
    @Column(name = "D_NAME")
    private String dName;
    
    /* 委托维修段ID */
    @Column(name = "Delegate_D_ID")
    private String delegateDId;
    
    /* 委托维修段名称 */
    @Column(name = "Delegate_D_Name")
    private String delegateDName;
    
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
    
    /* 施修计划明细主键 */
    @Column(name="Enforce_Plan_Detail_IDX")
    private String enforcePlanDetailIdx;
    
    /* 机车检修作业计划下属的机车检修计划流程节点（第一层） */
    @Transient
    private List<JobProcessNode> jobProcessNodes;
    
    /* 工长派工查询-生产任务单: 兑现单的车型简称+车号+修程名称+修次+状态*/
    @Transient
    private String groupRdpInfo;
    
    /* 登记情况*/
    @Transient
    private String num;

    /**
     * <li>说明：默认构造方法
     * <li>创建人：何涛
     * <li>创建日期：2015-4-29
     * <li>修改人： 
     * <li>修改日期：
     */
    public TrainWorkPlanView() {
        super();
    }

    /**
     * @return 获取机车检修作业计划下属的机车检修计划流程节点（第一层）
     */
    public List<JobProcessNode> getJobProcessNodes() {
        return jobProcessNodes;
    }
    
    /**
     * @param jobProcessNodes 设置机车检修作业计划下属的机车检修计划流程节点（第一层）
     */
    public void setJobProcessNodes(List<JobProcessNode> jobProcessNodes) {
        this.jobProcessNodes = jobProcessNodes;
    }
    /**
     * @return String 获取作业流程名称
     */
    public String getProcessName() {
        return processName;
    }
    
    /**
     * @param processName 设置作业流程名称
     */
    public void setProcessName(String processName) {
        this.processName = processName;
    }
    
    /**
     * @return String 获取车号
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
     * @return String 获取车型简称
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
     * @return String 获取修程名称
     */
    public String getRepairClassName() {
        return repairClassName;
    }
    
    /**
     * @param repairClassName 设置修程名称
     */
    public void setRepairClassName(String repairClassName) {
        this.repairClassName = repairClassName;
    }
    
    /**
     * @return String 获取修次名称
     */
    public String getRepairtimeName() {
        return repairtimeName;
    }
    
    /**
     * @param repairtimeName 设置修次名称
     */
    public void setRepairtimeName(String repairtimeName) {
        this.repairtimeName = repairtimeName;
    }
    
    /**
     * @return String 获取机车作业计划状态
     */
    public String getWorkPlanStatus() {
        return workPlanStatus;
    }
    
    /**
     * @param workPlanStatus 设置机车作业计划状态
     */
    public void setWorkPlanStatus(String workPlanStatus) {
        this.workPlanStatus = workPlanStatus;
    }
    
    /**
     * @return java.util.Date 获取计划开始时间
     */
    public java.util.Date getPlanBeginTime() {
        return planBeginTime;
    }
    
    /**
     * @param planBeginTime 设置计划开始时间
     */
    public void setPlanBeginTime(java.util.Date planBeginTime) {
        this.planBeginTime = planBeginTime;
    }
    
    /**
     * @return java.util.Date 获取计划完成时间
     */
    public java.util.Date getPlanEndTime() {
        return planEndTime;
    }
    
    /**
     * @param planEndTime 设置计划完成时间
     */
    public void setPlanEndTime(java.util.Date planEndTime) {
        this.planEndTime = planEndTime;
    }
    
    /**
     * @return java.util.Date 获取实际开始时间
     */
    public java.util.Date getBeginTime() {
        return beginTime;
    }
    
    /**
     * @param beginTime 设置实际开始时间
     */
    public void setBeginTime(java.util.Date beginTime) {
        this.beginTime = beginTime;
    }
    
    /**
     * @return java.util.Date 获取实际完成时间
     */
    public java.util.Date getEndTime() {
        return endTime;
    }
    
    /**
     * @param endTime 设置实际完成时间
     */
    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
    }
    
    /**
     * @return java.util.Date 获取计划生成时间
     */
    public java.util.Date getWorkPlanTime() {
        return workPlanTime;
    }
    
    /**
     * @param workPlanTime 设置计划生成时间
     */
    public void setWorkPlanTime(java.util.Date workPlanTime) {
        this.workPlanTime = workPlanTime;
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
     * @return String 获取委托维修段名称
     */
    public String getDelegateDName() {
        return delegateDName;
    }
    
    /**
     * @param delegateDName 设置委托维修段名称
     */
    public void setDelegateDName(String delegateDName) {
        this.delegateDName = delegateDName;
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
    
    public Double getRatedWorkDay() {
        return ratedWorkDay;
    }
    
    public void setRatedWorkDay(Double ratedWorkDay) {
        this.ratedWorkDay = ratedWorkDay;
    }
    
    public String getGroupRdpInfo() {
        return groupRdpInfo;
    }
    
    public void setGroupRdpInfo(String groupRdpInfo) {
        this.groupRdpInfo = groupRdpInfo;
    }
    
    public String getDelegateDId() {
        return delegateDId;
    }

    
    public void setDelegateDId(String delegateDId) {
        this.delegateDId = delegateDId;
    }

    
    public String getDId() {
        return dId;
    }

    
    public void setDId(String id) {
        dId = id;
    }

    
    public String getDName() {
        return dName;
    }

    
    public void setDName(String name) {
        dName = name;
    }

    
    public String getEnforcePlanDetailIdx() {
        return enforcePlanDetailIdx;
    }

    
    public void setEnforcePlanDetailIdx(String enforcePlanDetailIdx) {
        this.enforcePlanDetailIdx = enforcePlanDetailIdx;
    }

    
    public String getProcessIdx() {
        return processIdx;
    }

    
    public void setProcessIdx(String processIdx) {
        this.processIdx = processIdx;
    }

    
    public String getRepairClassIdx() {
        return repairClassIdx;
    }

    
    public void setRepairClassIdx(String repairClassIdx) {
        this.repairClassIdx = repairClassIdx;
    }

    
    public String getRepairtimeIdx() {
        return repairtimeIdx;
    }

    
    public void setRepairtimeIdx(String repairtimeIdx) {
        this.repairtimeIdx = repairtimeIdx;
    }

    
    public String getTrainTypeIdx() {
        return trainTypeIdx;
    }

    
    public void setTrainTypeIdx(String trainTypeIdx) {
        this.trainTypeIdx = trainTypeIdx;
    }

    
    public String getWorkCalendarIdx() {
        return workCalendarIdx;
    }

    
    public void setWorkCalendarIdx(String workCalendarIdx) {
        this.workCalendarIdx = workCalendarIdx;
    }

    /**
     * <li>说明：获取机车作业计划状态中文含义
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param status 机车作业计划状态
     * @return 机车作业计划状态中文含义
     */
    public static String getStatusMeaning(String status) {
        if (status.equals(STATUS_NEW)) {
            return "未启动";
        } else if (status.equals(STATUS_HANDLING)) {
            return "处理中";
        } else if (status.equals(STATUS_HANDLED)) {
            return "已处理";
        } else if (status.equals(STATUS_NULLIFY)) {
            return "终止";
        }
        return "";
    }

    
    public String getNum() {
        return num;
    }

    
    public void setNum(String num) {
        this.num = num;
    }
}
