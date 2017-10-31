package com.yunda.jx.jxgc.producttaskmanage.entity;

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

import com.yunda.jx.webservice.stationTerminal.base.entity.WorkTaskBean;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkCard实体类, 数据表：作业卡
 * <li>创建人：程锐
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_Work_Card")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkCard implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /** 状态-初始化 */
    public static final String STATUS_NEW = "INITIALIZE";
    /** 状态-已开放 */
    public static final String STATUS_OPEN = "OPENED";
    /** 状态-处理中 */
    public static final String STATUS_HANDLING = "ONGOING";
    /** 状态-已处理 */
    public static final String STATUS_HANDLED = "COMPLETE";
    /** 状态-工单处理完成，质量检查处理中 */
    public static final String STATUS_FINISHED = "FINISHED";
    /** 状态-终止 */
    public static final String STATUS_TERMINATED = "TERMINATED";
    /** 工序分类-拆卸 */
    public static final String WORK_SEQ_TYPE_UNINST="disassembly";
    /** 工序分类-组装 */
    public static final String WORK_SEQ_TYPE_INSTALL="buildUp";    

    /** 扩展分类 - 自定义工单 */
    public static final String EXT_CLASS_DEFINE = "Define";
    
    /* 是否有默认作业人员 */
    public static final int HASDEFAULTPERSON = 1;
    public static final int HASNOTDEFAULTPERSON = 0;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 工序卡主键 */
	@Column(name="Work_Seq_Card_IDX")
	private String workSeqCardIDX;
	/* 工序卡类型: 检修; 试验 (通过数据字典获取值) */
//	@Column(name="Work_Seq_Type")
//	private String workSeqType;
	/* 检修活动主键 */
	@Column(name="Repair_Activity_IDX")
	private String repairActivityIDX;
    /* 工艺流程节点实例主键 */
    @Column(name="Node_Case_IDX")
    private String nodeCaseIDX;
    /* 工艺流程实例主键 */
//    @Column(name="Tec_Process_Case_IDX")
//    private String tecProcessCaseIDX;
	/* 施修任务兑现单主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
//	/* 组成型号主键 */
//	@Column(name="BuildUp_Type_IDX")
//	private String buildUpTypeIDX;
//	/* 组成型号名称 */
//	@Column(name="BuildUp_Type_Name")
//	private String buildUpTypeName;
	/* 作业卡编码 */
	@Column(name="Work_Card_Code")
	private String workCardCode;
	/* 作业卡名称 */
	@Column(name="Work_Card_Name")
	private String workCardName;
//	/* 额定工时（单位：分钟） */
//	@Column(name="Rated_WorkHours")
//	private Integer ratedWorkHours;
	/* 作业范围 */
	@Column(name="Work_Scope")
	private String workScope;	
	/* 安全注意事项 */
	@Column(name="Safe_Announcements")
	private String safeAnnouncements;
	/* 互换配件信息主键 */
//	@Column(name="Parts_Account_IDX")
//	private String partsAccountIDX;
//	/* 互换配件型号主键 */
//	@Column(name="Parts_Type_IDX")
//	private String partsTypeIDX;
//	/* 配件名称 */
//	@Column(name="Parts_Name")
//	private String partsName;
//	/* 规格型号 */
//	@Column(name="Specification_Model")
//	private String specificationModel;
	/* 铭牌号 */
//	@Column(name="Nameplate_No")
//	private String nameplateNo;
//	/* 配件编号 */
//	@Column(name="Parts_No")
//	private String partsNo;
//	/* 安装位置主键 */
//	@Column(name="FixPlace_IDX")
//	private String fixPlaceIDX;
//	/* 安装位置编码全名 */
//	@Column(name="FixPlace_FullCode")
//	private String fixPlaceFullCode;
//	/* 安装位置全名 */
//	@Column(name="FixPlace_FullName")
//	private String fixPlaceFullName;
	/* 工位主键 */
	@Column(name="Work_Station_IDX")
	private String workStationIDX;
	/* 工位名称 */
	@Column(name="Work_Station_Name")
	private String workStationName;
	/* 工位负责人主键 */
//	@Column(name="Duty_PersonId")
//	private Long dutyPersonId;
	/* 工位负责人名称 */
//	@Column(name="Duty_PersonName")
//	private String dutyPersonName;
	/* 计划开工时间 */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="Plan_Begin_Time")
//	private java.util.Date planBeginTime;
	/* 计划完工时间 */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="Plan_End_Time")
//	private java.util.Date planEndTime;
	/* 实际开工时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Real_Begin_Time")
	private java.util.Date realBeginTime;
	/* 实际完工时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Real_End_Time")
	private java.util.Date realEndTime;
	/* 系统开始时间 */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="System_Begin_Time")
//	private java.util.Date systemBeginTime;
//	/* 系统完工时间 */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="System_End_Time")
//	private java.util.Date systemEndTime;
    /* 状态:10初始化、20已开放、30处理中、40已处理、50终止 */
    private String status;
	/* 备注 */
	private String remarks;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;
	
	/* 是否有默认作业人员 */
	@Column(name="HAVE_DEFAULT_PERSON")
	private Integer haveDefaultPerson;
	/* 工位所属班组 */
	@Column(name="work_station_belong_team")
	private Long workStationBelongTeam;
    /* 工位所属班组名称 */
    @Column(name="work_station_belong_team_name")
    private String workStationBelongTeamName;
    /* 图号 */
//	@Column(name="chart_no")
//	private String chartNo;
	/* 工序卡分类 */
	@Column(name="work_seq_class")
	private String workSeqClass;
	/* 是否已操作过（配件组装、拆卸） 0：未操作 ，1：已操作 */
//	@Column(name="is_complete")
//	private Integer isComplete;
    /* 是否加班完成，1：是；0：否*/
//    @Column(name="IS_OVERTIME")
//	private Integer isOvertime;
//    /* 加班时间 */
//    @Column(name="OVERTIME")
//    private Long overtime;
    /* 扩展分类 用于区分质量记录单还是作业工单,通过数据字典设置 */
	@Column(name="EXTENSION_CLASS")
	private String extensionClass;
	/* 兑现单类型 */
//	@Column(name="RDP_TYPE")
//	private String rdpType;
    /* 以,号分隔的作业人员ID */
    @Column(name="WORKER_ID")
    private String workerID;
    /* 以,号分隔的作业人员姓名 */
    private String worker;
    /* 质检项名称 */
    @Column(name="Qc_Name")
    private String qcName;
    /* 顺序号 */
    private Long seq;
    /*检修活动名称*/
    @Transient
    private String repairActivityName;
    /*检修活动类型名称*/    
    @Transient
    private String repairActivityTypeName;
    /*车型*/
    @Transient
    private String trainSortName;
    @Transient
    private String trainTypeIdx;
    /*车号*/
    @Transient
    private String trainNo;
    /*修程名称*/
    @Transient
    private String repairClassName;
    /* 作业人员 */
    @Transient
    private String workers;
    /* 转入时间 */
    @Transient
    private String transinTimeStr;
    /* 计划交车时间 */
    @Transient
    private String planTrainTimeStr;
    /*计划开工时间*/
    @Transient
    private String planBeginTimeStr;
    /*计划完工时间*/
    @Transient
    private String planEndTimeStr;
    /* 车型车号 */
    @Transient
    private String trainTypeTrainNo;
    /* 修程修次 */
    @Transient
    private String repairClassRepairTime;
    /* 系统开工时间 */
    @Transient
    private String systemBeginTimeStr;
    /* 实际开工时间 */
    @Transient
    private String realBeginTimeStr;
    /* 实际完工时间 */
    @Transient
    private String realEndTimeStr;
    /* 修程主键 */
    @Transient
    private String repairClassIdx;
    /* 修次主键 */
    @Transient
    private String repairTimeIdx;
    /* 能否批量 */
    @Transient
    private String batch;
    /* 处理意见 */
    @Transient
    private String judgment;
    /* 节点实例名称 */
	@Transient
	private String nodeCaseName;
	/* 上次作业人员 */
	@Transient
	private String lastTimeWorker;
    /* 工位所属班组序列 */
    @Transient
    private String workStationBelongTeamSeq;
    /* 流水线主键 */
    @Transient
    private String repairLineIDX;
    /* 工位编码 */
    @Transient
    private String workStationCode;
    /* 工单类型 （train 机车、parts 配件） */
    @Transient
    private String workType;
    
    /* 质量记录单的锁定状态(已兑现质量记录单维护功能中使用)*/
//    @Column(name="locking")
//    private String locking;
    
    /* 工单状态，判断这个工单是否已经完工，数据库对应的在工单查询时被人员处理状态占用了 */
    @Transient
    private String thisStatus;
    /* 调度派工人员 */
    @Transient
    private String dWorkerName;
    
    /* 调度派工日期 */
    @Transient
    private java.util.Date dDesignateTime;
    /* 工长派工人员 */
    @Transient
    private String hWorkerName;
    /* 工长派工日期 */
    @Transient
    private java.util.Date hDesignateTime;
    /* 所属节点的状态 */
    @Transient
    private String nodeStatus;
    /* 车型车号 */
    @Transient
    private String trainTypeAndNo;
    
    /* 任务集合 */
    @Transient
    private List<WorkTaskBean> workTaskBeanlist ;
    
    
    /**
	 * @return String 获取工序卡主键
	 */
	public String getWorkSeqCardIDX(){
		return workSeqCardIDX;
	}
	
	public void setWorkSeqCardIDX(String workSeqCardIDX) {
		this.workSeqCardIDX = workSeqCardIDX;
	}
	
	/**
	 * @return String 获取检修活动主键
	 */
	public String getRepairActivityIDX(){
		return repairActivityIDX;
	}
	
	public void setRepairActivityIDX(String repairActivityIDX) {
		this.repairActivityIDX = repairActivityIDX;
	}
    /**
     * @return String 获取工艺流程节点实例主键
     */
    public String getNodeCaseIDX(){
        return nodeCaseIDX;
    }
    
    public void setNodeCaseIDX(String nodeCaseIDX) {
        this.nodeCaseIDX = nodeCaseIDX;
    }
    /**
     * @return String 获取工艺流程实例主键
     */
//    public String getTecProcessCaseIDX(){
//        return tecProcessCaseIDX;
//    }
//    /**
//     * @param 设置工艺流程实例主键
//     */
//    public void setTecProcessCaseIDX(String tecProcessCaseIDX) {
//        this.tecProcessCaseIDX = tecProcessCaseIDX;
//    }
	/**
	 * @return String 获取施修任务兑现单主键
	 */
	public String getRdpIDX(){
		return rdpIDX;
	}
	
	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
	}
//	/**
//	 * @return String 获取组成型号主键
//	 */
//	public String getBuildUpTypeIDX(){
//		return buildUpTypeIDX;
//	}
//	
//	public void setBuildUpTypeIDX(String buildUpTypeIDX) {
//		this.buildUpTypeIDX = buildUpTypeIDX;
//	}
//	/**
//	 * @return String 获取组成型号名称
//	 */
//	public String getBuildUpTypeName(){
//		return buildUpTypeName;
//	}
//	
//	public void setBuildUpTypeName(String buildUpTypeName) {
//		this.buildUpTypeName = buildUpTypeName;
//	}
	/**
	 * @return String 获取作业卡编码
	 */
	public String getWorkCardCode(){
		return workCardCode;
	}
	
	public void setWorkCardCode(String workCardCode) {
		this.workCardCode = workCardCode;
	}
	/**
	 * @return String 获取作业卡名称
	 */
	public String getWorkCardName(){
		return workCardName;
	}
	
	public void setWorkCardName(String workCardName) {
		this.workCardName = workCardName;
	}
	
//	/**
//	 * @return Long 获取额定工时
//	 */
//	public Integer getRatedWorkHours(){
//		return ratedWorkHours;
//	}
//	
//	public void setRatedWorkHours(Integer ratedWorkHours) {
//		this.ratedWorkHours = ratedWorkHours;
//	}
	/**
	 * @return String 获取作业范围
	 */
	public String getWorkScope(){
		return workScope;
	}
	
	public void setWorkScope(String workScope) {
		this.workScope = workScope;
	}
	/**
	 * @return String 获取安全注意事项
	 */
	public String getSafeAnnouncements(){
		return safeAnnouncements;
	}
	
	public void setSafeAnnouncements(String safeAnnouncements) {
		this.safeAnnouncements = safeAnnouncements;
	}
	
//	/**
//	 * @return String 获取互换配件型号主键
//	 */
//	public String getPartsTypeIDX(){
//		return partsTypeIDX;
//	}
//	
//	public void setPartsTypeIDX(String partsTypeIDX) {
//		this.partsTypeIDX = partsTypeIDX;
//	}
//	/**
//	 * @return String 获取配件名称
//	 */
//	public String getPartsName(){
//		return partsName;
//	}
//	
//	public void setPartsName(String partsName) {
//		this.partsName = partsName;
//	}
//	/**
//	 * @return String 获取规格型号
//	 */
//	public String getSpecificationModel(){
//		return specificationModel;
//	}
//	
//	public void setSpecificationModel(String specificationModel) {
//		this.specificationModel = specificationModel;
//	}
//	
//	/**
//	 * @return String 获取配件编号
//	 */
//	public String getPartsNo(){
//		return partsNo;
//	}
//	
//	public void setPartsNo(String partsNo) {
//		this.partsNo = partsNo;
//	}
//	/**
//	 * @return String 获取安装位置编码
//	 */
//	public String getFixPlaceIDX(){
//		return fixPlaceIDX;
//	}
//	
//	public void setFixPlaceIDX(String fixPlaceIDX) {
//		this.fixPlaceIDX = fixPlaceIDX;
//	}
//	/**
//	 * @return String 获取安装位置编码全名
//	 */
//	public String getFixPlaceFullCode(){
//		return fixPlaceFullCode;
//	}
//	
//	public void setFixPlaceFullCode(String fixPlaceFullCode) {
//		this.fixPlaceFullCode = fixPlaceFullCode;
//	}
//	/**
//	 * @return String 获取安装位置名称全名
//	 */
//	public String getFixPlaceFullName(){
//		return fixPlaceFullName;
//	}
//	
//	public void setFixPlaceFullName(String fixPlaceFullName) {
//		this.fixPlaceFullName = fixPlaceFullName;
//	}
	/**
	 * @return String 获取工位
	 */
	public String getWorkStationIDX(){
		return workStationIDX;
	}
	
	public void setWorkStationIDX(String workStationIDX) {
		this.workStationIDX = workStationIDX;
	}
	/**
	 * @return String 获取工位名称
	 */
	public String getWorkStationName(){
		return workStationName;
	}
	
	public void setWorkStationName(String workStationName) {
		this.workStationName = workStationName;
	}
	
	/**
	 * @return java.util.Date 获取实际开工时间
	 */
	public java.util.Date getRealBeginTime(){
		return realBeginTime;
	}
	
	public void setRealBeginTime(java.util.Date realBeginTime) {
		this.realBeginTime = realBeginTime;
	}
	/**
	 * @return java.util.Date 获取实际完工时间
	 */
	public java.util.Date getRealEndTime(){
		return realEndTime;
	}
	
	public void setRealEndTime(java.util.Date realEndTime) {
		this.realEndTime = realEndTime;
	}
	/**
	 * @return java.util.Date 获取系统开始时间
	 */
//	public java.util.Date getSystemBeginTime(){
//		return systemBeginTime;
//	}
//	
//	public void setSystemBeginTime(java.util.Date systemBeginTime) {
//		this.systemBeginTime = systemBeginTime;
//	}
//	/**
//	 * @return java.util.Date 获取系统完工时间
//	 */
//	public java.util.Date getSystemEndTime(){
//		return systemEndTime;
//	}
//	
//	public void setSystemEndTime(java.util.Date systemEndTime) {
//		this.systemEndTime = systemEndTime;
//	}
    /**
     * @return String 获取状态
     */
    public String getStatus(){
        return status;
    }
   
    public void setStatus(String status) {
        this.status = status;
    }
	/**
	 * @return String 获取备注
	 */
	public String getRemarks(){
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * @return String 获取站点标识
	 */
	public String getSiteID(){
		return siteID;
	}
	
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	/**
	 * @return Long 获取创建人
	 */
	public Long getCreator(){
		return creator;
	}
	
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * @return java.util.Date 获取创建时间
	 */
	public java.util.Date getCreateTime(){
		return createTime;
	}
	
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return Long 获取修改人
	 */
	public Long getUpdator(){
		return updator;
	}
	
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	/**
	 * @return java.util.Date 获取修改时间
	 */
	public java.util.Date getUpdateTime(){
		return updateTime;
	}
	
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	
	public void setIdx(String idx) {
		this.idx = idx;
	}
	
	
	public Integer getHaveDefaultPerson() {
	    return haveDefaultPerson;
	}
	
	public void setHaveDefaultPerson(Integer haveDefaultPerson) {
	    this.haveDefaultPerson = haveDefaultPerson;
	}
	
	
	public Long getWorkStationBelongTeam() {
	    return workStationBelongTeam;
	}
	
	public void setWorkStationBelongTeam(Long workStationBelongTeam) {
	    this.workStationBelongTeam = workStationBelongTeam;
	}
    
    public String getWorkStationBelongTeamName() {
        return workStationBelongTeamName;
    }
    
    public void setWorkStationBelongTeamName(String workStationBelongTeamName) {
        this.workStationBelongTeamName = workStationBelongTeamName;
    }
    /**
     * 
     * <li>说明：根据状态值获取其状态意义
     * <li>创建人：程锐
     * <li>创建日期：2013-1-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param status 状态值
     * @return 状态意义
     */
    public static String getStatusMeaning(String status){
        if(status.equals(STATUS_NEW)){
            return "初始化";
        }else if(status.equals(STATUS_HANDLING)){
            return "处理中";
        }else if(status.equals(STATUS_HANDLED)){
            return "已处理";
        }else if(status.equals(STATUS_FINISHED)){
            return "质检中";
        }else if(status.equals(STATUS_OPEN)){
            return "已开放";
        }else if(status.equals(STATUS_TERMINATED)){
            return "终止";
        }
        return "";
    }

    public String getRepairActivityName() {
        return repairActivityName;
    }
    
    public void setRepairActivityName(String repairActivityName) {
        this.repairActivityName = repairActivityName;
    }
    
    public String getRepairActivityTypeName() {
        return repairActivityTypeName;
    }
    
    public void setRepairActivityTypeName(String repairActivityTypeName) {
        this.repairActivityTypeName = repairActivityTypeName;
    }
    
    public String getTrainSortName() {
        return trainSortName;
    }
    
    public void setTrainSortName(String trainSortName) {
        this.trainSortName = trainSortName;
    }
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    public String getRepairClassName() {
        return repairClassName;
    }
    
    public void setRepairClassName(String repairClassName) {
        this.repairClassName = repairClassName;
    }
    
    public String getWorkers() {
        return workers;
    }
    
    public void setWorkers(String workers) {
        this.workers = workers;
    }
    
    public String getTransinTime() {
        return transinTimeStr;
    }
    
    public void setTransinTime(String transinTime) {
        this.transinTimeStr = transinTime;
    }
    
    public String getTransinTimeStr() {
        return transinTimeStr;
    }
    
    public void setTransinTimeStr(String transinTimeStr) {
        this.transinTimeStr = transinTimeStr;
    }
    
    public String getPlanTrainTimeStr() {
        return planTrainTimeStr;
    }
    
    public void setPlanTrainTimeStr(String planTrainTimeStr) {
        this.planTrainTimeStr = planTrainTimeStr;
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
    
    public String getTrainTypeTrainNo() {
        return trainTypeTrainNo;
    }
    
    public void setTrainTypeTrainNo(String trainTypeTrainNo) {
        this.trainTypeTrainNo = trainTypeTrainNo;
    }
    
    public String getRepairClassRepairTime() {
        return repairClassRepairTime;
    }
    
    public void setRepairClassRepairTime(String repairClassRepairTime) {
        this.repairClassRepairTime = repairClassRepairTime;
    }
    
    public String getSystemBeginTimeStr() {
        return systemBeginTimeStr;
    }
    
    public void setSystemBeginTimeStr(String systemBeginTimeStr) {
        this.systemBeginTimeStr = systemBeginTimeStr;
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
    
    public String getTrainTypeIdx() {
        return trainTypeIdx;
    }
    
    public void setTrainTypeIdx(String trainTypeIdx) {
        this.trainTypeIdx = trainTypeIdx;
    }
    
    public String getRepairClassIdx() {
        return repairClassIdx;
    }
    
    public void setRepairClassIdx(String repairClassIdx) {
        this.repairClassIdx = repairClassIdx;
    }
    
    public String getRepairTimeIdx() {
        return repairTimeIdx;
    }
    
    public void setRepairTimeIdx(String repairTimeIdx) {
        this.repairTimeIdx = repairTimeIdx;
    }
    
    public String getBatch() {
        return batch;
    }
    
    public void setBatch(String batch) {
        this.batch = batch;
    }
    
    public String getWorkSeqClass() {
        return workSeqClass;
    }
    
    public void setWorkSeqClass(String workSeqClass) {
        this.workSeqClass = workSeqClass;
    }
        
    public String getJudgment() {
        return judgment;
    }
    
    public void setJudgment(String judgment) {
        this.judgment = judgment;
    }
	public String getExtensionClass() {
		return extensionClass;
	}
	public void setExtensionClass(String extensionClass) {
		this.extensionClass = extensionClass;
	}
	public String getNodeCaseName() {
		return nodeCaseName;
	}
	public void setNodeCaseName(String nodeCaseName) {
		this.nodeCaseName = nodeCaseName;
	}
    
    public String getLastTimeWorker() {
        return lastTimeWorker;
    }
    
    public void setLastTimeWorker(String lastTimeWorker) {
        this.lastTimeWorker = lastTimeWorker;
    }
    
    public String getWorkStationBelongTeamSeq() {
        return workStationBelongTeamSeq;
    }
    
    public void setWorkStationBelongTeamSeq(String workStationBelongTeamSeq) {
        this.workStationBelongTeamSeq = workStationBelongTeamSeq;
    }
    
    public String getRepairLineIDX() {
        return repairLineIDX;
    }
    
    public void setRepairLineIDX(String repairLineIDX) {
        this.repairLineIDX = repairLineIDX;
    }
    
    public String getWorkStationCode() {
        return workStationCode;
    }
    
    public void setWorkStationCode(String workStationCode) {
        this.workStationCode = workStationCode;
    }
    
    public String getWorkType() {
        return workType;
    }
    
    public void setWorkType(String workType) {
        this.workType = workType;
    }
	
    
    public String getThisStatus() {
        return thisStatus;
    }
    
    public void setThisStatus(String thisStatus) {
        this.thisStatus = thisStatus;
    }
    
    public java.util.Date getDDesignateTime() {
        return dDesignateTime;
    }
    
    public void setDDesignateTime(java.util.Date designateTime) {
        dDesignateTime = designateTime;
    }
    
    public String getDWorkerName() {
        return dWorkerName;
    }
    
    public void setDWorkerName(String workerName) {
        dWorkerName = workerName;
    }
    
    public java.util.Date getHDesignateTime() {
        return hDesignateTime;
    }
    
    public void setHDesignateTime(java.util.Date designateTime) {
        hDesignateTime = designateTime;
    }
    
    public String getHWorkerName() {
        return hWorkerName;
    }
    
    public void setHWorkerName(String workerName) {
        hWorkerName = workerName;
    }
    
    public String getQcName() {
        return qcName;
    }
    
    public void setQcName(String qcName) {
        this.qcName = qcName;
    }
    
    public String getNodeStatus() {
        return nodeStatus;
    }
    
    public void setNodeStatus(String nodeStatus) {
        this.nodeStatus = nodeStatus;
    }
    
    public Long getSeq() {
        return seq;
    }
    
    public void setSeq(Long seq) {
        this.seq = seq;
    }
    
    public String getWorker() {
        return worker;
    }
    
    public void setWorker(String worker) {
        this.worker = worker;
    }
    
    public String getWorkerID() {
        return workerID;
    }
    
    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }
    
    public String getTrainTypeAndNo() {
        return trainTypeAndNo;
    }
    
    public void setTrainTypeAndNo(String trainTypeAndNo) {
        this.trainTypeAndNo = trainTypeAndNo;
    }

    
    public List<WorkTaskBean> getWorkTaskBeanlist() {
        return workTaskBeanlist;
    }

    
    public void setWorkTaskBeanlist(List<WorkTaskBean> workTaskBeanlist) {
        this.workTaskBeanlist = workTaskBeanlist;
    }  
    
    
    
}