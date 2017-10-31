package com.yunda.jx.jxgc.workhis.workcard.entity;

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
 * <li>说明：视图JXGC_WORK_TASK_HIS实体类, 数据表：作业任务历史
 * <li>创建人：程梅
 * <li>创建日期：2015年8月17日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_WORK_TASK_HIS")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkTaskHis implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /** 状态-待领取 */
    public static final String STATUS_WAITINGFORGET = "WAITING_RECEIVE";
    /** 状态-待处理 */
    public static final String STATUS_WAITINGFORHANDLE = "TODO";
    /** 状态-初始化 */
    public static final String STATUS_INIT = "INITIALIZE";
    /** 状态-已处理 */
    public static final String STATUS_HANDLED = "COMPLETE";
    /** 状态-终止 */
    public static final String STATUS_FINISHED = "TERMINATED";
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 作业卡主键 */
	@Column(name="Work_Card_IDX")
	private String workCardIDX;
	/* 工步主键 */
	@Column(name="Work_Step_IDX")
	private String workStepIDX;
	/* 作业任务编码 */
	@Column(name="Work_Task_Code")
	private String workTaskCode;
	/* 作业任务名称 */
	@Column(name="Work_Task_Name")
	private String workTaskName;
//	/* 作业类型：一般检查、组装、拆卸（通过数据字段选择） */
//	@Column(name="Work_Task_Type")
//	private String workTaskType;
	/* 检修内容 */
	@Column(name="Repair_Content")
	private String repairContent;
	/* 检修标准 */
	@Column(name="Repair_Standard")
	private String repairStandard;
	/* 检修结果 */
	@Column(name="Repair_Result")
	private String repairResult;
//	/* 施修人员主键 */
//	@Column(name="Repair_Worker")
//	private Long repairWorker;
//	/* 施修人员名称 */
//	@Column(name="Repair_Worker_Name")
//	private String repairWorkerName;
	/* 状态:10待领取、20待处理、30已处理、40终止 */
	private String status = STATUS_WAITINGFORGET;
	/* 备注 */
	private String remarks;
	
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="RECORD_STATUS")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private java.util.Date updateTime;
	/* 检修方法 */
	@Column(name="Repair_Method")
	private String repairMethod;
	/* 检修结果主键 */
	@Column(name="Repair_Result_Idx")
	private String repairResultIdx;
    /* 作业任务顺序 */
    @Column(name="WORK_TASK_SEQ")
    private Integer workTaskSeq;
	@Transient
	private String repairMethodIdx;//预留字段，作业方法主键

	@Transient
    private String mutualCheckPerson;//互检员
    @Transient
    private String checkPerson;//检查员
    @Transient
    private String spotCheckPerson;//抽检
	
	/**
	 * @return String 获取作业卡主键
	 */
	public String getWorkCardIDX(){
		return workCardIDX;
	}
	/**
	 * @param workCardIDX 设置作业卡主键
	 */
	public void setWorkCardIDX(String workCardIDX) {
		this.workCardIDX = workCardIDX;
	}
	/**
	 * @return String 获取工步主键
	 */
	public String getWorkStepIDX(){
		return workStepIDX;
	}
	/**
	 * @param workStepIDX 设置工步主键
	 */
	public void setWorkStepIDX(String workStepIDX) {
		this.workStepIDX = workStepIDX;
	}
	/**
	 * @return String 获取作业任务编码
	 */
	public String getWorkTaskCode(){
		return workTaskCode;
	}
	/**
	 * @param workTaskCode 设置作业任务编码
	 */
	public void setWorkTaskCode(String workTaskCode) {
		this.workTaskCode = workTaskCode;
	}
	/**
	 * @return String 获取作业任务名称
	 */
	public String getWorkTaskName(){
		return workTaskName;
	}
	/**
	 * @param workTaskName 设置作业任务名称
	 */
	public void setWorkTaskName(String workTaskName) {
		this.workTaskName = workTaskName;
	}
	/**
	 * @return String 获取检修内容
	 */
	public String getRepairContent(){
		return repairContent;
	}
	/**
	 * @param repairContent 设置检修内容
	 */
	public void setRepairContent(String repairContent) {
		this.repairContent = repairContent;
	}
	/**
	 * @return String 获取检修标准
	 */
	public String getRepairStandard(){
		return repairStandard;
	}
	/**
	 * @param repairStandard 设置检修标准
	 */
	public void setRepairStandard(String repairStandard) {
		this.repairStandard = repairStandard;
	}
	/**
	 * @return String 获取检修结果
	 */
	public String getRepairResult(){
		return repairResult;
	}
	/**
	 * @param repairResult 设置检修结果
	 */
	public void setRepairResult(String repairResult) {
		this.repairResult = repairResult;
	}
	/**
	 * @return String 获取状态
	 */
	public String getStatus(){
		return status;
	}
	/**
	 * @param status 设置状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return String 获取备注
	 */
	public String getRemarks(){
		return remarks;
	}
	/**
	 * @param remarks 设置备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
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
	public String getSiteID(){
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
	public Long getCreator(){
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
	public java.util.Date getCreateTime(){
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
	public Long getUpdator(){
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
	public java.util.Date getUpdateTime(){
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
    
    public String getRepairMethod() {
        return repairMethod;
    }
    
    public void setRepairMethod(String repairMethod) {
        this.repairMethod = repairMethod;
    }
    
    public String getRepairResultIdx() {
        return repairResultIdx;
    }
    
    public void setRepairResultIdx(String repairResultIdx) {
        this.repairResultIdx = repairResultIdx;
    }
    
    public String getRepairMethodIdx() {
        return repairMethodIdx;
    }
    
    public void setRepairMethodIdx(String repairMethodIdx) {
        this.repairMethodIdx = repairMethodIdx;
    }
    
    public Integer getWorkTaskSeq() {
        return workTaskSeq;
    }
    
    public void setWorkTaskSeq(Integer workTaskSeq) {
        this.workTaskSeq = workTaskSeq;
    }
    
    public String getMutualCheckPerson() {
        return mutualCheckPerson;
    }
    
    public void setMutualCheckPerson(String mutualCheckPerson) {
        this.mutualCheckPerson = mutualCheckPerson;
    }
    
    public String getCheckPerson() {
        return checkPerson;
    }
    
    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }
    
    public String getSpotCheckPerson() {
        return spotCheckPerson;
    }
    
    public void setSpotCheckPerson(String spotCheckPerson) {
        this.spotCheckPerson = spotCheckPerson;
    }
    
}