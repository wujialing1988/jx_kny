package com.yunda.jx.jxgc.workplanmanage.entity;

import java.util.Date;
import java.util.List;

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
 * <li>说明：TrainWorkPlan实体类, 数据表：机车检修作业计划
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
@Table(name = "JXGC_Train_Work_Plan")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainWorkPlan implements java.io.Serializable {
    
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
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 作业流程主键 */
    @Column(name = "Process_IDX")
    private String processIDX;
    
    /* 作业流程名称 */
    @Column(name = "Process_Name")
    private String processName;
    
    /* 作业流程额定工期（小时） */
    @Column(name = "RATED_WORKDAY")
    private Double ratedWorkDay;
    
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
    private String workCalendarIDX;
    
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
    
    /* 扣车实体id */
    @Column(name = "DETAIN_IDX")
    private String detainIdx;
    
    
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
    private String enforcePlanDetailIDX;
    
    /* 机车检修作业计划下属的机车检修计划流程节点（第一层） */
    @Transient
    private List<JobProcessNode> jobProcessNodes;
    
    
    /* 机车检修作业计划下属的机车检修计划流程节点（第一层） */
    @Transient
    private List<JobProcessNodeBean> jobProcessNodeBeans;
    
    /* 工长派工查询-生产任务单: 兑现单的车型简称+车号+修程名称+修次+状态*/
    @Transient
    private String groupRdpInfo;
    
    /* 兑现单文本，用于填充数据 */
    @Transient
    private String rdpText;
    /* 进段时间 */
    @Transient
    private Date inTime;
    @Transient
    private Date minRealTime;
    @Transient
    private Date maxRealTime;
    @Transient
    private String months;
    
    /* 机车出入段台账主键 添加检修任务与机车入段台账关联，在新增作业计划时查看在段机车，并关联；在机车入段时，查看当前车是否正在检修，并且没有关联台账，则进行关联 */
    @Column(name = "Train_Access_Account_IDX")
    private String trainAccessAccountIDX;
    
    /* 客货类型 10 货车 20 客车 30 柴油发电机组*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;     
    
    
    /* 交验交车人 */
    @Column(name = "From_PersonId")
    private Long fromPersonId;
    
    /* 交验交车人名称 */
    @Column(name = "From_PersonName")
    private String fromPersonName;
    
    /* 交验接车人 */
    @Column(name = "To_PersonId")
    private Long toPersonId;
    
    /* 交验接车人名称 */
    @Column(name = "To_PersonName")
    private String toPersonName;
    
    /* 交验时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "From_Time")
    private java.util.Date fromTime;
    
    /* 校验状态 0 未检验 1 已校验  */
    @Column(name = "FROM_STATUS")
    private Integer fromStatus;
    
    /* 交验情况描述 */
    @Column(name = "FROM_REMARK")
    private String fromRemark;
    
    
    public Date getMaxRealTime() {
        return maxRealTime;
    }


    
    public void setMaxRealTime(Date maxRealTime) {
        this.maxRealTime = maxRealTime;
    }


    
    public Date getMinRealTime() {
        return minRealTime;
    }


    
    public void setMinRealTime(Date minRealTime) {
        this.minRealTime = minRealTime;
    }


    public Date getInTime() {
        return inTime;
    }

    
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    /**
     * <li>说明：【机车检修台位占用计划】模块查询条件basecombo数据集构造方法
     * <li>创建人：何涛
     * <li>创建日期：2015-4-29
     * <li>修改人： 
     * <li>修改日期：
     * @param idx idx主键
     * @param trainNo 车型 + 车号
     */
    public TrainWorkPlan(String idx, String trainNo) {
        super();
        this.idx = idx;
        this.trainNo = trainNo;
    }

    /**
     * <li>说明：默认构造方法
     * <li>创建人：何涛
     * <li>创建日期：2015-4-29
     * <li>修改人： 
     * <li>修改日期：
     */
    public TrainWorkPlan() {
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
     * @return String 获取作业流程主键
     */
    public String getProcessIDX() {
        return processIDX;
    }
    
    /**
     * @param processIDX 设置作业流程主键
     */
    public void setProcessIDX(String processIDX) {
        this.processIDX = processIDX;
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
     * @return String 获取车型主键
     */
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }
    
    /**
     * @param trainTypeIDX 设置车型主键
     */
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
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
     * @return String 获取修程主键
     */
    public String getRepairClassIDX() {
        return repairClassIDX;
    }
    
    /**
     * @param repairClassIDX 设置修程主键
     */
    public void setRepairClassIDX(String repairClassIDX) {
        this.repairClassIDX = repairClassIDX;
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
     * @return String 获取修次主键
     */
    public String getRepairtimeIDX() {
        return repairtimeIDX;
    }
    
    /**
     * @param repairtimeIDX 设置修次主键
     */
    public void setRepairtimeIDX(String repairtimeIDX) {
        this.repairtimeIDX = repairtimeIDX;
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
     * @return String 获取配属段ID
     */
    public String getDID() {
        return dID;
    }
    
    /**
     * @param dID 设置配属段ID
     */
    public void setDID(String dID) {
        this.dID = dID;
    }
    
    /**
     * @return String 获取配属段名称
     */
    public String getDNAME() {
        return dNAME;
    }
    
    /**
     * @param dNAME 设置配属段名称
     */
    public void setDNAME(String dNAME) {
        this.dNAME = dNAME;
    }
    
    /**
     * @return String 获取委托维修段ID
     */
    public String getDelegateDID() {
        return delegateDID;
    }
    
    /**
     * @param delegateDID 设置委托维修段ID
     */
    public void setDelegateDID(String delegateDID) {
        this.delegateDID = delegateDID;
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
    
    public String getRdpText() {
        return rdpText;
    }
    
    public void setRdpText(String rdpText) {
        this.rdpText = rdpText;
    }
    
    public String getEnforcePlanDetailIDX() {
        return enforcePlanDetailIDX;
    }
    
    public void setEnforcePlanDetailIDX(String enforcePlanDetailIDX) {
        this.enforcePlanDetailIDX = enforcePlanDetailIDX;
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
            return "在修";
        } else if (status.equals(STATUS_HANDLED)) {
            return "修竣";
        } else if (status.equals(STATUS_NULLIFY)) {
            return "终止";
        }
        return "";
    }

    
    public String getTrainAccessAccountIDX() {
        return trainAccessAccountIDX;
    }

    
    public void setTrainAccessAccountIDX(String trainAccessAccountIDX) {
        this.trainAccessAccountIDX = trainAccessAccountIDX;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }



    
    public List<JobProcessNodeBean> getJobProcessNodeBeans() {
        return jobProcessNodeBeans;
    }



    
    public void setJobProcessNodeBeans(List<JobProcessNodeBean> jobProcessNodeBeans) {
        this.jobProcessNodeBeans = jobProcessNodeBeans;
    }



    
    public String getVehicleType() {
        return vehicleType;
    }



    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }



    
    public Long getFromPersonId() {
        return fromPersonId;
    }



    
    public void setFromPersonId(Long fromPersonId) {
        this.fromPersonId = fromPersonId;
    }



    
    public String getFromPersonName() {
        return fromPersonName;
    }



    
    public void setFromPersonName(String fromPersonName) {
        this.fromPersonName = fromPersonName;
    }



    
    public String getFromRemark() {
        return fromRemark;
    }



    
    public void setFromRemark(String fromRemark) {
        this.fromRemark = fromRemark;
    }



    
    public Integer getFromStatus() {
        return fromStatus;
    }



    
    public void setFromStatus(Integer fromStatus) {
        this.fromStatus = fromStatus;
    }



    
    public java.util.Date getFromTime() {
        return fromTime;
    }



    
    public void setFromTime(java.util.Date fromTime) {
        this.fromTime = fromTime;
    }



    
    public Long getToPersonId() {
        return toPersonId;
    }



    
    public void setToPersonId(Long toPersonId) {
        this.toPersonId = toPersonId;
    }



    
    public String getToPersonName() {
        return toPersonName;
    }



    
    public void setToPersonName(String toPersonName) {
        this.toPersonName = toPersonName;
    }



	public String getDetainIdx() {
		return detainIdx;
	}



	public void setDetainIdx(String detainIdx) {
		this.detainIdx = detainIdx;
	}
    
    
    
    
}
