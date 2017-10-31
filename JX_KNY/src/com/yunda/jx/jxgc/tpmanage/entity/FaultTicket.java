package com.yunda.jx.jxgc.tpmanage.entity;

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
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明：FaultTicket实体类, 数据表：故障提票
 * <li>创建人：程锐
 * <li>创建日期：2015-06-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@Entity
@Table(name = "JXGC_Fault_Ticket")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class FaultTicket implements java.io.Serializable {
    
    /** 业务状态：10未处理 */
    public static final int STATUS_DRAFT = 10;
    
    public static final String STATUS_DRAFT_CH = "未处理";
    
    /** 业务状态：20处理中 */
    public static final int STATUS_OPEN = 20;
    
    public static final String STATUS_OPEN_CH = "处理中";
    
    /** 业务状态：30已处理 */
    public static final int STATUS_OVER = 30;
    
    public static final String STATUS_OVER_CH = "已处理";
    
    /** 确认状态：0 未确认 */
    public static final int AFFIRM_STATUS_UNDO = 0 ;
    
    /** 确认状态：1 已确认 */
    public static final int AFFIRM_STATUS_DONE = 1 ;
    
    /** 确认状态：2 已验收 */
    public static final int AFFIRM_STATUS_CHECK = 2 ;
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 机车检修作业计划主键 */
    @Column(name = "Work_Plan_IDX")
    private String workPlanIDX;
    
    /* 作业流程节点主键 */
    @Column(name = "Node_IDX")
    private String nodeIDX;
    
    /* 提票单号 */
    @Column(name = "Ticket_Code")
    private String ticketCode;
    
    /* 车型主键 */
    @Column(name = "Train_Type_IDX")
    private String trainTypeIDX;
    
    /* 车号 */
    @Column(name = "Train_No")
    private String trainNo;
    
    /* 车型英文简称 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;
    
    /* 提票状态（10：未处理、20：处理中、30：已处理） */
    private Integer status;
    
    /* 提票类型（数据字典项：如JT6，JT28等） */
    private String type;
    
    /* 确认状态 1：已确认；2：已验收 */
    @Column(name = "STATUS_AFFIRM")
    private Integer statusAffirm;
    
    /* 提票时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Ticket_Time")
    private java.util.Date ticketTime;
    
    /* 故障ID （基础码表） */
    @Column(name = "Fault_ID")
    private String faultID;
    
    /* 故障现象（故障名称） */
    @Column(name = "Fault_Name")
    private String faultName;
    
    /* 故障描述 */
    @Column(name = "Fault_Desc")
    private String faultDesc;
    
    /* 系统分类编码 */
    @Column(name = "Fault_Fix_Place_IDX")
    private String faultFixPlaceIDX;
    
    /* 故障位置编码全名 */
    @Column(name = "FixPlace_FullCode")
    private String fixPlaceFullCode;
    
    /* 故障位置名称全名 */
    @Column(name = "FixPlace_FullName")
    private String fixPlaceFullName;
    
    /* 故障发生日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Fault_Occur_Date")
    private java.util.Date faultOccurDate;
    
    /* 提票班组ID */
    @Column(name = "Ticket_Orgid")
    private Long ticketOrgid;
    
    /* 提票班组序列 */
    @Column(name = "Ticket_Orgseq")
    private String ticketOrgseq;
    
    /* 提票班组名称 */
    @Column(name = "Ticket_Orgname")
    private String ticketOrgname;
    
    /* 提票人ID */
    @Column(name = "Ticket_Emp_Id")
    private Long ticketEmpId;
    
    /* 提票人名称 */
    @Column(name = "Ticket_Emp")
    private String ticketEmp;
    
    /* 发现人 */
    @Column(name = "DISCOVERER_ID")
    private Long discoverID;
    
    /* 发现人名称 */
    @Column(name = "DISCOVERER")
    private String discover;
    
    /* 处理方法编码 */
    @Column(name = "METHOD_ID")
    private String methodID;
    
    /* 处理方法名称 */
    @Column(name = "METHOD_NAME")
    private String methodName;
    
    /* 处理方法描述 */
    @Column(name = "METHOD_DESC")
    private String methodDesc;
    
    /* 处理结果 */
    @Column(name = "Repair_Result")
    private String repairResult;
    
    /* 修竣时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Complete_Time")
    private java.util.Date completeTime;
    
    /* 处理人ID（多人人员之间用","号分隔开） */
    @Column(name = "Repair_Emp_ID")
    private String repairEmpID;
    
    /* 处理人名称（多人人员之间用","号分隔开） */
    @Column(name = "Repair_Emp")
    private String repairEmp;
    
    /* 处理班组ID （班组";"号分隔开） */
    @Column(name = "REPAIR_TEAM")
    private String repairTeam;
    
    /* 处理班组名称 （班组";"号分隔开） */
    @Column(name = "REPAIR_TEAM_NAME")
    private String repairTeamName;
    
    /* 处理班组序列 */
    @Column(name = "Repair_Team_Orgseq")
    private String repairTeamOrgseq;
    
    /* 销票人ID */
    @Column(name = "Complete_Emp_ID")
    private Long completeEmpID;
    
    /* 销票人 */
    @Column(name = "Complete_Emp")
    private String completeEmp;
    
    /* 责任人ID */
    @Column(name = "Response_Emp_ID")
    private String responseEmpID;
    
    /* 责任人 */
    @Column(name = "response_Emp")
    private String responseEmp;
    
    /* 专业类型ID, 是PJWZ_Professional_Type表的主键字段,不是Professional_Type_ID */
    @Column(name = "PROFESSIONAL_TYPE_IDX")
    private String professionalTypeIdx;
    
    /* 专业类型名称 */
    @Column(name = "PROFESSIONAL_TYPE_NAME")
    private String professionalTypeName;
    
    /* 处理开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Repair_Start_Time")
    private java.util.Date repairStartTime;
    
    /* 工时 */
    @Column(name = "Work_Time")
    private Integer workTime;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 默认为0 */
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
    /* 故障原因 */
    @Column(name = "FAULT_REASON")
    private String faultReason;
    
    /* 工长派工人ID（多人人员之间用","号分隔开） */
    @Column(name = "DISPATCH_EMP_ID")
    private String dispatchEmpID;
    
    /* 工长派工人名称（多人人员之间用","号分隔开） */
    @Column(name = "DISPATCH_EMP")
    private String dispatchEmp;
    
    /* 原因分析ID（用","号分隔开） */
    @Column(name = "REASON_ANALYSIS_ID")
    private String reasonAnalysisID;
    
    /* 原因分析名称（用","号分隔开） */
    @Column(name = "REASON_ANALYSIS")
    private String reasonAnalysis;
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;   
    
    @Transient
    private String FaultOccurDateStr;
    
    @Transient
    private String ticketTimeStr;
    
    @Transient
    private String trainTypeAndNo;
    
    @Transient
    private String completeTimeStr;
    
    /* 确认人 */
    @Transient
    private String affirmUser;
    
    /* 验收人 */
    @Transient
    private String checkUser;
    
    /* 是否需要做确认 */
    @Transient
    private String isAffirm;
    
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
     * @return String 获取提票单号
     */
    public String getTicketCode() {
        return ticketCode;
    }
    
    /**
     * @param ticketCode 设置提票单号
     */
    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
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
     * @return Integer 获取提票状态
     */
    public Integer getStatus() {
        return status;
    }
    
    /**
     * @param status 设置提票状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * @return String 获取提票类型
     */
    public String getType() {
        return type;
    }
    
    /**
     * @param type 设置提票类型
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * @return java.util.Date 获取提票时间
     */
    public java.util.Date getTicketTime() {
        return ticketTime;
    }
    
    /**
     * @param ticketTime 设置提票时间
     */
    public void setTicketTime(java.util.Date ticketTime) {
        this.ticketTime = ticketTime;
    }
    
    /**
     * @return String 获取故障ID
     */
    public String getFaultID() {
        return faultID;
    }
    
    /**
     * @param faultID 设置故障ID
     */
    public void setFaultID(String faultID) {
        this.faultID = faultID;
    }
    
    /**
     * @return String 获取故障现象
     */
    public String getFaultName() {
        return faultName;
    }
    
    /**
     * @param faultName 设置故障现象
     */
    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }
    
    /**
     * @return String 获取故障描述
     */
    public String getFaultDesc() {
        return faultDesc;
    }
    
    /**
     * @param faultDesc 设置故障描述
     */
    public void setFaultDesc(String faultDesc) {
        this.faultDesc = faultDesc;
    }
    
    /**
     * @return String 获取故障位置主键
     */
    public String getFaultFixPlaceIDX() {
        return faultFixPlaceIDX;
    }
    
    /**
     * @param faultFixPlaceIDX 设置故障位置主键
     */
    public void setFaultFixPlaceIDX(String faultFixPlaceIDX) {
        this.faultFixPlaceIDX = faultFixPlaceIDX;
    }
    
    /**
     * @return String 获取故障位置编码全名
     */
    public String getFixPlaceFullCode() {
        return fixPlaceFullCode;
    }
    
    /**
     * @param fixPlaceFullCode 设置故障位置编码全名
     */
    public void setFixPlaceFullCode(String fixPlaceFullCode) {
        this.fixPlaceFullCode = fixPlaceFullCode;
    }
    
    /**
     * @return String 获取故障位置名称全名
     */
    public String getFixPlaceFullName() {
        return fixPlaceFullName;
    }
    
    /**
     * @param fixPlaceFullName 设置故障位置名称全名
     */
    public void setFixPlaceFullName(String fixPlaceFullName) {
        this.fixPlaceFullName = fixPlaceFullName;
    }
    
    /**
     * @return java.util.Date 获取故障发生日期
     */
    public java.util.Date getFaultOccurDate() {
        return faultOccurDate;
    }
    
    /**
     * @param faultOccurDate 设置故障发生日期
     */
    public void setFaultOccurDate(java.util.Date faultOccurDate) {
        this.faultOccurDate = faultOccurDate;
    }
    
    /**
     * @return Long 获取提票班组ID
     */
    public Long getTicketOrgid() {
        return ticketOrgid;
    }
    
    /**
     * @param ticketOrgid 设置提票班组ID
     */
    public void setTicketOrgid(Long ticketOrgid) {
        this.ticketOrgid = ticketOrgid;
    }
    
    /**
     * @return String 获取提票班组序列
     */
    public String getTicketOrgseq() {
        return ticketOrgseq;
    }
    
    /**
     * @param ticketOrgseq 设置提票班组序列
     */
    public void setTicketOrgseq(String ticketOrgseq) {
        this.ticketOrgseq = ticketOrgseq;
    }
    
    /**
     * @return String 获取提票班组名称
     */
    public String getTicketOrgname() {
        return ticketOrgname;
    }
    
    /**
     * @param ticketOrgname 设置提票班组名称
     */
    public void setTicketOrgname(String ticketOrgname) {
        this.ticketOrgname = ticketOrgname;
    }
    
    /**
     * @return Long 获取提票人ID
     */
    public Long getTicketEmpId() {
        return ticketEmpId;
    }
    
    /**
     * @param ticketEmpId 设置提票人ID
     */
    public void setTicketEmpId(Long ticketEmpId) {
        this.ticketEmpId = ticketEmpId;
    }
    
    /**
     * @return String 获取提票人名称
     */
    public String getTicketEmp() {
        return ticketEmp;
    }
    
    /**
     * @param ticketEmp 设置提票人名称
     */
    public void setTicketEmp(String ticketEmp) {
        this.ticketEmp = ticketEmp;
    }
    
    /**
     * @return Long 获取发现人ID
     */
    public Long getDiscoverID() {
        return discoverID;
    }
    
    /**
     * @param discoverID 设置发现人ID
     */
    public void setDiscoverID(Long discoverID) {
        this.discoverID = discoverID;
    }
    
    /**
     * @return String 获取发现人名称
     */
    public String getDiscover() {
        return discover;
    }
    
    /**
     * @param discover 设置发现人名称
     */
    public void setDiscover(String discover) {
        this.discover = discover;
    }
    
    /**
     * @return String 获取处理方法编码
     */
    public String getMethodID() {
        return methodID;
    }
    
    /**
     * @param methodID 设置处理方法编码
     */
    public void setMethodID(String methodID) {
        this.methodID = methodID;
    }
    
    /**
     * @return String 获取处理方法名称
     */
    public String getMethodName() {
        return methodName;
    }
    
    /**
     * @param methodName 设置处理方法名称
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    /**
     * @return String 获取处理方法描述
     */
    public String getMethodDesc() {
        return methodDesc;
    }
    
    /**
     * @param methodDesc 设置处理方法描述
     */
    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }
    
    /**
     * @return String 获取处理结果
     */
    public String getRepairResult() {
        return repairResult;
    }
    
    /**
     * @param repairResult 设置处理结果
     */
    public void setRepairResult(String repairResult) {
        this.repairResult = repairResult;
    }
    
    /**
     * @return java.util.Date 获取修竣时间
     */
    public java.util.Date getCompleteTime() {
        return completeTime;
    }
    
    /**
     * @param completeTime 设置修竣时间
     */
    public void setCompleteTime(java.util.Date completeTime) {
        this.completeTime = completeTime;
    }
    
    /**
     * @return String 获取处理人ID
     */
    public String getRepairEmpID() {
        return repairEmpID;
    }
    
    /**
     * @param repairEmpID 设置处理人ID
     */
    public void setRepairEmpID(String repairEmpID) {
        this.repairEmpID = repairEmpID;
    }
    
    /**
     * @return String 获取处理人名称
     */
    public String getRepairEmp() {
        return repairEmp;
    }
    
    /**
     * @param repairEmp 设置处理人名称
     */
    public void setRepairEmp(String repairEmp) {
        this.repairEmp = repairEmp;
    }
    
    /**
     * @return Long 获取处理班组ID
     */
    public String getRepairTeam() {
        return repairTeam;
    }
    
    /**
     * @param repairTeam 设置处理班组ID
     */
    public void setRepairTeam(String repairTeam) {
        this.repairTeam = repairTeam;
    }
    
    /**
     * @return String 获取处理班组名称
     */
    public String getRepairTeamName() {
        return repairTeamName;
    }
    
    /**
     * @param repairTeamName 设置处理班组名称
     */
    public void setRepairTeamName(String repairTeamName) {
        this.repairTeamName = repairTeamName;
    }
    
    /**
     * @return String 获取处理班组序列
     */
    public String getRepairTeamOrgseq() {
        return repairTeamOrgseq;
    }
    
    /**
     * @param repairTeamOrgseq 设置处理班组序列
     */
    public void setRepairTeamOrgseq(String repairTeamOrgseq) {
        this.repairTeamOrgseq = repairTeamOrgseq;
    }
    
    /**
     * @return Long 获取销票人ID
     */
    public Long getCompleteEmpID() {
        return completeEmpID;
    }
    
    /**
     * @param completeEmpID 设置销票人ID
     */
    public void setCompleteEmpID(Long completeEmpID) {
        this.completeEmpID = completeEmpID;
    }
    
    /**
     * @return String 获取销票人
     */
    public String getCompleteEmp() {
        return completeEmp;
    }
    
    /**
     * @param completeEmp 设置销票人
     */
    public void setCompleteEmp(String completeEmp) {
        this.completeEmp = completeEmp;
    }
    
    /**
     * @return String 获取专业类型ID
     */
    public String getProfessionalTypeIdx() {
        return professionalTypeIdx;
    }
    
    /**
     * @param professionalTypeIdx 设置专业类型ID
     */
    public void setProfessionalTypeIdx(String professionalTypeIdx) {
        this.professionalTypeIdx = professionalTypeIdx;
    }
    
    /**
     * @return String 获取专业类型名称
     */
    public String getProfessionalTypeName() {
        return professionalTypeName;
    }
    
    /**
     * @param professionalTypeName 设置专业类型名称
     */
    public void setProfessionalTypeName(String professionalTypeName) {
        this.professionalTypeName = professionalTypeName;
    }
    
    /**
     * @return java.util.Date 获取处理开始时间
     */
    public java.util.Date getRepairStartTime() {
        return repairStartTime;
    }
    
    /**
     * @param repairStartTime 设置处理开始时间
     */
    public void setRepairStartTime(java.util.Date repairStartTime) {
        this.repairStartTime = repairStartTime;
    }
    
    /**
     * @return Integer 获取工时
     */
    public Integer getWorkTime() {
        return workTime;
    }
    
    /**
     * @param workTime 设置工时
     */
    public void setWorkTime(Integer workTime) {
        this.workTime = workTime;
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
    
    public String getFaultOccurDateStr() {
        return FaultOccurDateStr;
    }
    
    public void setFaultOccurDateStr(String faultOccurDateStr) {
        FaultOccurDateStr = faultOccurDateStr;
    }
    
    public String getTrainTypeAndNo() {
        return trainTypeAndNo;
    }
    
    public void setTrainTypeAndNo(String trainTypeAndNo) {
        this.trainTypeAndNo = trainTypeAndNo;
    }
    
    public String getCompleteTimeStr() {
        return completeTimeStr;
    }

    
    public void setCompleteTimeStr(String completeTimeStr) {
        this.completeTimeStr = completeTimeStr;
    }
    
    public String getDispatchEmp() {
        return dispatchEmp;
    }
    
    public void setDispatchEmp(String dispatchEmp) {
        this.dispatchEmp = dispatchEmp;
    }
    
    public String getDispatchEmpID() {
        return dispatchEmpID;
    }
    
    public void setDispatchEmpID(String dispatchEmpID) {
        this.dispatchEmpID = dispatchEmpID;
    }

    
    public String getFaultReason() {
        return faultReason;
    }

    
    public void setFaultReason(String faultReason) {
        this.faultReason = faultReason;
    }

	public String getReasonAnalysis() {
		return reasonAnalysis;
	}

	public void setReasonAnalysis(String reasonAnalysis) {
		this.reasonAnalysis = reasonAnalysis;
	}

	public String getReasonAnalysisID() {
		return reasonAnalysisID;
	}

	public void setReasonAnalysisID(String reasonAnalysisID) {
		this.reasonAnalysisID = reasonAnalysisID;
	}

    
    public String getTicketTimeStr() {
        return ticketTimeStr;
    }

    
    public void setTicketTimeStr(String ticketTimeStr) {
        this.ticketTimeStr = ticketTimeStr;
    }

    
    public Integer getStatusAffirm() {
        return statusAffirm;
    }

    
    public void setStatusAffirm(Integer statusAffirm) {
        this.statusAffirm = statusAffirm;
    }

    
    public String getAffirmUser() {
        return affirmUser;
    }

    
    public void setAffirmUser(String affirmUser) {
        this.affirmUser = affirmUser;
    }

    
    public String getCheckUser() {
        return checkUser;
    }

    
    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    
    public String getResponseEmp() {
        return responseEmp;
    }

    
    public void setResponseEmp(String responseEmp) {
        this.responseEmp = responseEmp;
    }

    
    public String getResponseEmpID() {
        return responseEmpID;
    }

    
    public void setResponseEmpID(String responseEmpID) {
        this.responseEmpID = responseEmpID;
    }
    
    public String getIsAffirm() {
        return isAffirm;
    }

    
    public void setIsAffirm(String isAffirm) {
        this.isAffirm = isAffirm;
    }

    
    public String getVehicleType() {
        return vehicleType;
    }

    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    
    
    
}
