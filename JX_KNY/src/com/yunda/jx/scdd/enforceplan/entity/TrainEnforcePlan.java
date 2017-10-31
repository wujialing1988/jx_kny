package com.yunda.jx.scdd.enforceplan.entity;

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
 * <li>说明：TrainEnforcePlan实体类, 数据表：机车生产计划
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-05-01
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="SCDD_Train_Enforce_Plan")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TrainEnforcePlan implements java.io.Serializable{
    /* 计划状态-编制完成 */
    public static final int STATUS_COMPLETEPLAN = 10;
    /* 计划状态-审核中 */
    public static final int STATUS_AUDITING = 20;
    /* 计划状态-审核完成 */
    public static final int STATUS_AUDITED = 30;
    /* 计划状态-审核拒绝 */
    public static final int STATUS_REFUSED = 60;
    /* 计划状态-已经兑现 */
    public static final int STATUS_REDEMPTION = 40;
    /* 计划状态-计划完成 */
    public static final int STATUS_COMPLETE = 50;
    
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    
    // 月计划流程定义名称
    public static final String SNAKER_FLOW_DEF_TRAINENFORCE = "ck.jxgc.SNAKER_FLOW_DEF_TRAINENFORCE";
    
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 计划名称 */
	@Column(name="Plan_Name")
	private String planName;
	/* 计划开始日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Plan_Start_Date")
	private java.util.Date planStartDate;
	/* 计划结束日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Plan_End_Date")
	private java.util.Date planEndDate;
	/* 编制人 */
	@Column(name="Plan_Person")
	private Long planPerson;
	/* 编制人名称 */
	@Column(name="Plan_Person_Name")
	private String planPersonName;
	/* 编制日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Plan_Time")
	private java.util.Date planTime;
	/* 编制单位ID */
	@Column(name="Plan_ORGID")
	private Long planOrgId;
	/* 编制单位部门序列 */
	@Column(name="Plan_ORGSEQ")
	private String planOrgSeq;
	/* 编制单位名称 */
	@Column(name="Plan_ORGName")
	private String planOrgName;
	/* 计划状态，10：编制完成；20：审核中；30：审核完成；40：已经兑现；50：计划完成 */
	@Column(name="Plan_Status")
	private Integer planStatus;
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
    
    /* 流程实例ID */
    @Column(name="PROCESS_INST_ID")
    private String processInstId;
    
    /* 任务ID */
    @Transient
    private String workId ;
    
    /* 任务名称 */
    @Transient
    private String workName ;
    
    /* 流程模板ID */
    @Transient
    private String processId ;
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;    

	/**
	 * @return String 获取计划名称
	 */
	public String getPlanName(){
		return planName;
	}
	/**
	 * @param 设置计划名称
	 */
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	/**
	 * @return java.util.Date 获取计划开始日期
	 */
	public java.util.Date getPlanStartDate(){
		return planStartDate;
	}
	/**
	 * @param 设置计划开始日期
	 */
	public void setPlanStartDate(java.util.Date planStartDate) {
		this.planStartDate = planStartDate;
	}
	/**
	 * @return java.util.Date 获取计划结束日期
	 */
	public java.util.Date getPlanEndDate(){
		return planEndDate;
	}
	/**
	 * @param 设置计划结束日期
	 */
	public void setPlanEndDate(java.util.Date planEndDate) {
		this.planEndDate = planEndDate;
	}
	/**
	 * @return Long 获取编制人
	 */
	public Long getPlanPerson(){
		return planPerson;
	}
	/**
	 * @param 设置编制人
	 */
	public void setPlanPerson(Long planPerson) {
		this.planPerson = planPerson;
	}
	/**
	 * @return String 获取编制人名称
	 */
	public String getPlanPersonName(){
		return planPersonName;
	}
	/**
	 * @param 设置编制人名称
	 */
	public void setPlanPersonName(String planPersonName) {
		this.planPersonName = planPersonName;
	}
	/**
	 * @return java.util.Date 获取编制时间
	 */
	public java.util.Date getPlanTime(){
		return planTime;
	}
	/**
	 * @param 设置编制时间
	 */
	public void setPlanTime(java.util.Date planTime) {
		this.planTime = planTime;
	}
	/**
	 * @return Long 获取编制单位ID
	 */
	public Long getPlanOrgId(){
		return planOrgId;
	}
	/**
	 * @param 设置编制单位ID
	 */
	public void setPlanOrgId(Long planOrgId) {
		this.planOrgId = planOrgId;
	}
	/**
	 * @return String 获取编制单位部门序列
	 */
	public String getPlanOrgSeq(){
		return planOrgSeq;
	}
	/**
	 * @param 设置编制单位部门序列
	 */
	public void setPlanOrgSeq(String planOrgSeq) {
		this.planOrgSeq = planOrgSeq;
	}
	/**
	 * @return String 获取编制单位名称
	 */
	public String getPlanOrgName(){
		return planOrgName;
	}
	/**
	 * @param 设置编制单位名称
	 */
	public void setPlanOrgName(String planOrgName) {
		this.planOrgName = planOrgName;
	}
	/**
	 * @return Integer 获取计划状态
	 */
	public Integer getPlanStatus(){
		return planStatus;
	}
	/**
	 * @param 设置计划状态
	 */
	public void setPlanStatus(Integer planStatus) {
		this.planStatus = planStatus;
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
    
    public String getProcessInstId() {
        return processInstId;
    }
    
    public void setProcessInstId(String processInstId) {
        this.processInstId = processInstId;
    }
    
    public String getWorkId() {
        return workId;
    }
    
    public void setWorkId(String workId) {
        this.workId = workId;
    }
    
    public String getWorkName() {
        return workName;
    }
    
    public void setWorkName(String workName) {
        this.workName = workName;
    }
    
	public String getProcessId() {
		return processId;
	}
	
	public void setProcessId(String processId) {
		this.processId = processId;
	}
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    
}