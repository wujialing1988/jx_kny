package com.yunda.jx.jxgc.processdef.entity;

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
 * <li>说明: JobProcessDef实体 数据表：检修作业流程
 * <li>创建人：何涛
 * <li>创建日期：2015-4-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_Job_Process_Def")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class JobProcessDef implements java.io.Serializable{

    /** 机车检修作业流程编码规则 */
    public static final String CODE_RULE_JOB_PROCESS_CODE = "JXGC_JOB_PROCESS_DEF_CODE";
    
    /** 流程状态 - 新增 */
    public static final int CONST_INT_STATUS_XZ = 0;
    /** 流程状态 - 新增（字符） */
    public static final String CONST_INT_STATUS_XZ_CH = "新增";
    /** 流程状态 - 启用 */
    public static final int CONST_INT_STATUS_QY = 1;
    /** 流程状态 - 启用（字符） */
    public static final String CONST_INT_STATUS_QY_CH = "启用";
    /** 流程状态 - 作废 */
    public static final int CONST_INT_STATUS_ZF = 2;
    /** 流程状态 - 作废（字符）  */
    public static final String CONST_INT_STATUS_ZF_CH = "作废";
    
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 流程编码 */
	@Column(name="Process_Code")
	private String processCode;
	/* 流程名称 */
	@Column(name="Process_Name")
	private String processName;
	/* 车型主键 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	/* 车型名称 */
	@Column(name="Train_Type_Name")
	private String trainTypeName;
	/* 车型简称 */
	@Column(name="Train_Type_Short_Name")
	private String trainTypeShortName;
	/* 修程主键 */
	@Column(name="RC_IDX")
	private String rcIDX;
	/* 修程名称 */
	@Column(name="RC_Name")
	private String rcName;
	/* 额定工期（小时） */
	@Column(name="Rated_WorkDay")
	private Double ratedWorkDay;
	/* 流程状态 新增：0、启用：1、作废：2 */
	private Integer status;
	/* 流程描述 */
	private String description;
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 站点标识 */
	@Column(updatable=false)
	private String siteID;
	/* 创建者 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;
	/* 更新者*/
	private Long updator;
	/* 更新时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;
    /* 日历 */
    @Column(name = "Work_Calendar_IDX")
    private String workCalendarIDX;
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;   
    
	/**
	 * @return String 获取流程编码
     */
	public String getProcessCode(){
		return processCode;
	}
	/**
	 * @param processCode 设置流程编码
	 */
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}
	/**
	 * @return String 获取流程名称
	 */
	public String getProcessName(){
		return processName;
	}
	/**
	 * @param processName 设置流程名称
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	/**
	 * @return String 获取车型主键
	 */
	public String getTrainTypeIDX(){
		return trainTypeIDX;
	}
	/**
	 * @param trainTypeIDX 设置车型主键
	 */
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}
	/**
	 * @return String 获取车型名称
	 */
	public String getTrainTypeName(){
		return trainTypeName;
	}
	/**
	 * @param trainTypeName 设置车型名称
	 */
	public void setTrainTypeName(String trainTypeName) {
		this.trainTypeName = trainTypeName;
	}
	/**
	 * @return String 获取车型简称
	 */
	public String getTrainTypeShortName(){
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
	public String getRcIDX(){
		return rcIDX;
	}
	/**
	 * @param rcIDX 设置修程主键
	 */
	public void setRcIDX(String rcIDX) {
		this.rcIDX = rcIDX;
	}
	/**
	 * @return String 获取修程名称
	 */
	public String getRcName(){
		return rcName;
	}
	/**
	 * @param rcName 设置修程名称
	 */
	public void setRcName(String rcName) {
		this.rcName = rcName;
	}
	/**
	 * @return Double 获取额定工期
	 */
	public Double getRatedWorkDay(){
		return ratedWorkDay;
	}
	/**
	 * @param ratedWorkDay 设置额定工期
	 */
	public void setRatedWorkDay(Double ratedWorkDay) {
		this.ratedWorkDay = ratedWorkDay;
	}
	/**
	 * @return Integer 获取流程状态
	 */
	public Integer getStatus(){
		return status;
	}
	/**
	 * @param status 设置流程状态
     */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return String 获取流程描述
	 */
	public String getDescription(){
		return description;
	}
	/**
	 * @param description 设置流程描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param recordStatus 设置记录的状态
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
	 * @return Long 获取创建者
	 */
	public Long getCreator(){
		return creator;
	}
	/**
	 * @param creator 设置创建者
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
    
    public String getWorkCalendarIDX() {
        return workCalendarIDX;
    }
    
    public void setWorkCalendarIDX(String workCalendarIDX) {
        this.workCalendarIDX = workCalendarIDX;
    }
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    
    
}