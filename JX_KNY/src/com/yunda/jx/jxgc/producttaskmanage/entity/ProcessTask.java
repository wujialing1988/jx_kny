package com.yunda.jx.jxgc.producttaskmanage.entity;

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
 * <li>说明：ProcessTask实体类, 数据表：流程任务单
 * <li>创建人：王治龙
 * <li>创建日期：2013-02-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_Process_Task")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ProcessTask implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 业务类型-临修审核
	 * add by easy 2013/5/13
	 */
	public static final String TYPE_APPLY_LX = "applyLX";//临修审核
	
	/**
	 * 业务类型-转临修
	 * add by easy 2013/5/13
	 */
	public static final String TYPE_LX = "LX";//临修
	
	/**
	 * 业务类型-上砂
	 * add by easy 2013/5/13
	 */
	public static final String TYPE_SANDING = "TrainSanding";//上砂
	
	/**
	 * 业务类型-保洁
	 * add by easy 2013/5/13
	 */
	public static final String TYPE_CLEANING = "TrainCleaning";//保洁
	
	/**
	 * 业务类型-质量检查
	 */
	public static final String TYPE_WORKCARD = "QualityControlCheck";//质量检查
	/**
	 * 业务类型-提票
	 */
	public static final String TYPE_TP = "Fault";//提票
	/**
	 * 业务类型-技术改造
	 */
	public static final String TYPE_TEC = "TechnologyAlteration";//技术改造
	/**
	 * 业务类型-系统
	 */
	public static final String TYPE_SYS = "System";
	/**
	 * 业务类型-异步三检一验
	 */
	public static final String TYPE_ASY_QUA = "AsyncQua";
	/**
	 * 业务类型-提票质量检查
	 */
	public static final String TYPE_TP_QUA = "TpQuality";
	/**
	 * 业务类型-提票异步检查
	 */
	public static final String TYPE_TP_ASY_QUA = "TpAsyncQua";
	/**
	 * 业务状态-处理中
	 */
	public static final String STATE_RUNNING = "RUNNING";
	/**
	 * 业务状态-完成
	 */
	public static final String STATE_COMPLETE = "COMPLETE";	
    /**
     * 业务状态-终止
     */
    public static final String STATE_TERMINATED = "TERMINATED";
    
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 车型 */
	@Column(name="Train_Type")
	private String trainType;
	/* 车号 */
	@Column(name="Train_No")
	private String trainNo;
	/* 关联工艺节点实例主键 */
	@Column(name="Tec_Node_Case_Idx")
	private String tecNodeCaseIdx;
    /* 关联业务工单名称 */
	@Column(name="source_name")
	private String sourceName;
	/* 关联业务工单主键(作业卡、提票、技术改造等) */
	@Column(name="Source_Idx")
	private String sourceIdx;
	/* 兑现单主键 */
	@Column(name="rdp_Idx")
	private String rdpIdx;
	/* 业务类型（1.作业卡、2.提票、3.技术改造等） */
	private String token;
	/* 任务状态 */
	@Column(name="current_State")
	private String currentState;
	/* 规格型号 */
	@Column(name="Specification_Model")
	private String specificationModel;
	/* 铭牌号 */
	@Column(name="Nameplate_No")
	private String nameplateNo;
	/* 配件编号 */
	@Column(name="Parts_No")
	private String partsNo;
	/* 配件名称 */
	@Column(name="Parts_Name")
	private String partsName;	
	/* 任务描述 */
	@Column(name="Task_Depict")
	private String taskDepict;
	/* 修程 */
	@Column(name="Repair_Class_Name")
	private String repairClassName;
	/* 修次 */
	@Column(name="Repair_time_Name")
	private String repairtimeName;
	/* 业务流程实例ID */
	private Long processInstID;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;

	@Transient
	private String actionUrl;//响应URL
	@Transient
	private String workItemName;//工作项名称
	@Transient
	private Character isTimeout;//是否超时
	@Transient
	private Long workItemID;//工作项ID
	@Transient
	private String key; //存放主键
	@Transient
	private String processInstName;//流程实例名称
	@Transient
	private Long activityInstID;//活动实例ID
	@Transient
	private String xcIdx;//修程主键
	@Transient
	private String trainTypeIdx;//车型主键
    @Transient
    private String undertakeDepName;//承修部门
    @Transient
    private String undertake_dep;//承修部门ID
    @Transient
    private String trainrukuTime;//做显示用，工位终端上，需要显示入库时间，在不修改查询框架的情况下，加入此字段 easy
    @Transient
    private String participantStr ;//作业人员
    @Transient
    private String workcardId;//作业卡主键
    @Transient
    private String workItemIdStr;//工作项主键字符串
    @Transient
    private String processInstIDStr;//流程实例主键字符串   
    @Transient
	private String rdpIDX;//施修任务兑现单主键
    @Transient
    private String rdpText; //查询条件存储兑现单显示字段   
    @Transient
    private String nodeCaseName;//工艺节点名称，做显示用 程锐添加
    @Transient
    private String planEndTimeStr;//计划出库时间，做显示用 程锐添加
    @Transient
    private String trainTypeShortName;//车型简称 程锐添加
	public ProcessTask(){}
	
	public ProcessTask(ProcessTask task){
	    this.idx = task.idx;	    
	    this.trainType = task.trainType;
	    this.trainNo = task.trainNo;
	    this.tecNodeCaseIdx = task.tecNodeCaseIdx;
	    this.sourceName = task.sourceName;
	    this.sourceIdx = task.sourceIdx;
	    this.token = task.token;
	    this.currentState = task.currentState;
	    this.specificationModel = task.specificationModel;
	    this.nameplateNo = task.nameplateNo;
	    this.partsNo = task.partsNo;
	    this.partsName = task.partsName;
	    this.taskDepict = task.taskDepict;
	    this.repairClassName = task.repairClassName;
	    this.repairtimeName = task.repairtimeName;
	    this.processInstID = task.processInstID;
	    this.siteID = task.siteID;
	    this.recordStatus = task.recordStatus;
	    this.createTime = task.createTime;
	    this.creator = task.creator;
	    this.updator = task.updator;
	    this.updateTime = task.updateTime;
	    this.activityInstID = task.activityInstID;
	    this.processInstName = task.processInstName;
	}
	
	/**
	 * @return String 获取车型
	 */
	public String getTrainType(){
		return trainType;
	}
	/**
	 * @param 设置车型
	 */
	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}
	/**
	 * @return String 获取车号
	 */
	public String getTrainNo(){
		return trainNo;
	}
	/**
	 * @param 设置车号
	 */
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	/**
	 * @return String 获取关联业务工单
	 */
	public String getSourceIdx(){
		return sourceIdx;
	}
	/**
	 * @param 设置关联业务工单
	 */
	public void setSourceIdx(String sourceIdx) {
		this.sourceIdx = sourceIdx;
	}
	/**
	 * @return Integer 获取业务类型
	 */
	public String getToken(){
		return token;
	}
	/**
	 * @param 设置业务类型
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * @return String 获取规格型号
	 */
	public String getSpecificationModel(){
		return specificationModel;
	}
	/**
	 * @param 设置规格型号
	 */
	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel;
	}
	/**
	 * @return String 获取铭牌号
	 */
	public String getNameplateNo(){
		return nameplateNo;
	}
	/**
	 * @param 设置铭牌号
	 */
	public void setNameplateNo(String nameplateNo) {
		this.nameplateNo = nameplateNo;
	}
	/**
	 * @return String 获取配件编号
	 */
	public String getPartsNo(){
		return partsNo;
	}
	/**
	 * @param 设置配件编号
	 */
	public void setPartsNo(String partsNo) {
		this.partsNo = partsNo;
	}
	/**
	 * @return String 获取配件名称
	 */
	public String getPartsName(){
		return partsName;
	}
	/**
	 * @param 设置配件名称
	 */
	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}
	/**
	 * @return String 获取任务描述
	 */
	public String getTaskDepict(){
		return taskDepict;
	}
	/**
	 * @param 设置任务描述
	 */
	public void setTaskDepict(String taskDepict) {
		this.taskDepict = taskDepict;
	}
	/**
	 * @return String 获取修程
	 */
	public String getRepairClassName(){
		return repairClassName;
	}
	/**
	 * @param 设置修程
	 */
	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}
	/**
	 * @return String 获取修次
	 */
	public String getRepairtimeName(){
		return repairtimeName;
	}
	/**
	 * @param 设置修次
	 */
	public void setRepairtimeName(String repairtimeName) {
		this.repairtimeName = repairtimeName;
	}
	/**
	 * @return Integer 获取业务流程实例ID
	 */
	public Long getProcessInstID(){
		return processInstID;
	}
	/**
	 * @param 设置业务流程实例ID
	 */
	public void setProcessInstID(Long processInstID) {
		this.processInstID = processInstID;
	}
	/**
	 * @return String 获取站点标识
	 */
	public String getSiteID(){
		return siteID;
	}
	/**
	 * @param 设置站点标识
	 */
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param 设置记录状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * @return java.util.Date 获取创建时间
	 */
	public java.util.Date getCreateTime(){
		return createTime;
	}
	/**
	 * @param 设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return Long 获取创建人
	 */
	public Long getCreator(){
		return creator;
	}
	/**
	 * @param 设置创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * @return Long 获取修改人
	 */
	public Long getUpdator(){
		return updator;
	}
	/**
	 * @param 设置修改人
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	/**
	 * @return java.util.Date 获取修改时间
	 */
	public java.util.Date getUpdateTime(){
		return updateTime;
	}
	/**
	 * @param 设置修改时间
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
	 * @param 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
        
    
    public String getCurrentState() {
        return currentState;
    }
    
    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
    
    public String getActionUrl() {
        return actionUrl;
    }
    
    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
    
    public String getWorkItemName() {
        return workItemName;
    }
    
    public void setWorkItemName(String workItemName) {
        this.workItemName = workItemName;
    }
    
    public Character getIsTimeout() {
        return isTimeout;
    }
    
    public void setIsTimeout(Character isTimeout) {
        this.isTimeout = isTimeout;
    }
    
    public Long getWorkItemID() {
        return workItemID;
    }
    
    public void setWorkItemID(Long workItemID) {
        this.workItemID = workItemID;
    }
    
    public String getKey() {
        return key;
    }

    
    public void setKey(String key) {
        this.key = key;
    }

	public String getProcessInstName() {
		return processInstName;
	}

	public void setProcessInstName(String processInstName) {
		this.processInstName = processInstName;
	}

    
    public Long getActivityInstID() {
        return activityInstID;
    }

    
    public void setActivityInstID(Long activityInstID) {
        this.activityInstID = activityInstID;
    }

    
    public String getRdpIdx() {
        return rdpIdx;
    }

    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }

    
    public String getXcIdx() {
        return xcIdx;
    }

    
    public void setXcIdx(String xcIdx) {
        this.xcIdx = xcIdx;
    }

    
    public String getTrainTypeIdx() {
        return trainTypeIdx;
    }

    
    public void setTrainTypeIdx(String trainTypeIdx) {
        this.trainTypeIdx = trainTypeIdx;
    }

    public String getTecNodeCaseIdx() {
        return tecNodeCaseIdx;
    }

    
    public void setTecNodeCaseIdx(String tecNodeCaseIdx) {
        this.tecNodeCaseIdx = tecNodeCaseIdx;
    }

    
    public String getSourceName() {
        return sourceName;
    }

    
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    
    public String getUndertakeDepName() {
        return undertakeDepName;
    }

    
    public void setUndertakeDepName(String undertakeDepName) {
        this.undertakeDepName = undertakeDepName;
    }

    
    public String getUndertake_dep() {
        return undertake_dep;
    }

    
    public void setUndertake_dep(String undertake_dep) {
        this.undertake_dep = undertake_dep;
    }

	public String getTrainrukuTime() {
		return trainrukuTime;
	}

	public void setTrainrukuTime(String trainrukuTime) {
		this.trainrukuTime = trainrukuTime;
	}

	public String getParticipantStr() {
		return participantStr;
	}

	public void setParticipantStr(String participantStr) {
		this.participantStr = participantStr;
	}

    
    public String getWorkcardId() {
        return workcardId;
    }

    
    public void setWorkcardId(String workcardId) {
        this.workcardId = workcardId;
    }

    
    public String getProcessInstIDStr() {
        return processInstIDStr;
    }

    
    public void setProcessInstIDStr(String processInstIDStr) {
        this.processInstIDStr = processInstIDStr;
    }

    
    public String getWorkItemIdStr() {
        return workItemIdStr;
    }

    
    public void setWorkItemIdStr(String workItemIdStr) {
        this.workItemIdStr = workItemIdStr;
    }

    public String getRdpIDX() {
        return rdpIDX;
    }

    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
    public String getRdpText() {
        return rdpText;
    }
    
    public void setRdpText(String rdpText) {
        this.rdpText = rdpText;
    }

	public String getNodeCaseName() {
		return nodeCaseName;
	}

	public void setNodeCaseName(String nodeCaseName) {
		this.nodeCaseName = nodeCaseName;
	}

	public String getPlanEndTimeStr() {
		return planEndTimeStr;
	}

	public void setPlanEndTimeStr(String planEndTimeStr) {
		this.planEndTimeStr = planEndTimeStr;
	}

	public String getTrainTypeShortName() {
		return trainTypeShortName;
	}

	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
    
}