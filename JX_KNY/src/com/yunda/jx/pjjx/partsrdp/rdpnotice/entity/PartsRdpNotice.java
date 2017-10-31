package com.yunda.jx.pjjx.partsrdp.rdpnotice.entity;

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
 * <li>说明：PartsRdpNotice实体类, 数据表：提票单
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJJX_Parts_Rdp_Notice")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpNotice implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/** 提票单状态 - 已终止*/
	public static final String STATUS_YZZ = "10";
	/** 提票类型 - 返工修*/
	public static final String type_fgx = "10";
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 作业主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
	/* 作业节点主键 */
	@Column(name="Rdp_Node_IDX")
	private String rdpNodeIDX;
	/* 提票单编号 */
	@Column(name="Notice_No")
	private String noticeNo;
	/* 问题描述 */
	@Column(name="Notice_Desc")
	private String noticeDesc;
	/* 提报人 */
	@Column(name="Notice_EmpID")
	private Long noticeEmpID;
	/* 提报人名称 */
	@Column(name="Notice_EmpName")
	private String noticeEmpName;
	/* 提报时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Notice_Time")
	private java.util.Date noticeTime;
	/* 处理方案描述 */
	private String solution;
	/* 作业人 */
	@Column(name="Work_EmpID")
	private String workEmpID;
	/* 作业人名称 */
	@Column(name="Work_EmpName")
	private String workEmpName;
	/* 作业开始时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Work_StartTime")
	private java.util.Date workStartTime;
	/* 作业结束时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Work_EndTime")
	private java.util.Date workEndTime;
	/* 领活人 */
	@Column(name="Handle_EmpID")
	private Long handleEmpID;
	/* 领活人名称 */
	@Column(name="Handle_EmpName")
	private String handleEmpName;
	/* 状态 */
	private String status;
	/* 回退标识 */
	@Column(name="Is_Back")
	private Integer isBack;
	/* 回退次数 */
	@Column(name="Back_Count")
	private Integer backCount;
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
    /* 提示类型 */
    @Column(name = "type")
    private String type;
    
	
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    /** Deafault Constructor */
	public PartsRdpNotice() {
		super();
	}
	/**
	 * @param rdpIDX 作业主键
	 * @param rdpNodeIDX 作业节点主键
	 * @param noticeNo 提票单编号
	 * @param noticeDesc 问题描述
	 */
	public PartsRdpNotice(String rdpIDX, String rdpNodeIDX, String noticeNo, String noticeDesc) {
		this.rdpIDX = rdpIDX;
		this.rdpNodeIDX = rdpNodeIDX;
		this.noticeNo = noticeNo;
		this.noticeDesc = noticeDesc;
	}
	/**
	 * @return String 获取作业主键
	 */
	public String getRdpIDX(){
		return rdpIDX;
	}
	/**
	 * @param rdpIDX 设置作业主键
	 */
	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
	}
	/**
	 * @return String 获取作业节点主键
	 */
	public String getRdpNodeIDX(){
		return rdpNodeIDX;
	}
	/**
	 * @param rdpNodeIDX 设置作业节点主键
	 */
	public void setRdpNodeIDX(String rdpNodeIDX) {
		this.rdpNodeIDX = rdpNodeIDX;
	}
	/**
	 * @return String 获取提票单编号
	 */
	public String getNoticeNo(){
		return noticeNo;
	}
	/**
	 * @param noticeNo 设置提票单编号
	 */
	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}
	/**
	 * @return String 获取问题描述
	 */
	public String getNoticeDesc(){
		return noticeDesc;
	}
	/**
	 * @param noticeDesc 设置问题描述
	 */
	public void setNoticeDesc(String noticeDesc) {
		this.noticeDesc = noticeDesc;
	}
	/**
	 * @return Long 获取提报人
	 */
	public Long getNoticeEmpID(){
		return noticeEmpID;
	}
	/**
	 * @param noticeEmpID 设置提报人
	 */
	public void setNoticeEmpID(Long noticeEmpID) {
		this.noticeEmpID = noticeEmpID;
	}
	/**
	 * @return String 获取提报人名称
	 */
	public String getNoticeEmpName(){
		return noticeEmpName;
	}
	/**
	 * @param noticeEmpName 设置提报人名称
	 */
	public void setNoticeEmpName(String noticeEmpName) {
		this.noticeEmpName = noticeEmpName;
	}
	/**
	 * @return java.util.Date 获取提报时间
	 */
	public java.util.Date getNoticeTime(){
		return noticeTime;
	}
	/**
	 * @param noticeTime 设置提报时间
	 */
	public void setNoticeTime(java.util.Date noticeTime) {
		this.noticeTime = noticeTime;
	}
	/**
	 * @return String 获取处理方案描述
	 */
	public String getSolution(){
		return solution;
	}
	/**
	 * @param solution 设置处理方案描述
	 */
	public void setSolution(String solution) {
		this.solution = solution;
	}
	/**
	 * @return String 获取作业人
	 */
	public String getWorkEmpID(){
		return workEmpID;
	}
	/**
	 * @param workEmpID 设置作业人
	 */
	public void setWorkEmpID(String workEmpID) {
		this.workEmpID = workEmpID;
	}
	/**
	 * @return String 获取作业人名称
	 */
	public String getWorkEmpName(){
		return workEmpName;
	}
	/**
	 * @param workEmpName 设置作业人名称
	 */
	public void setWorkEmpName(String workEmpName) {
		this.workEmpName = workEmpName;
	}
	/**
	 * @return java.util.Date 获取作业开始时间
	 */
	public java.util.Date getWorkStartTime(){
		return workStartTime;
	}
	/**
	 * @param workStartTime 设置作业开始时间
	 */
	public void setWorkStartTime(java.util.Date workStartTime) {
		this.workStartTime = workStartTime;
	}
	/**
	 * @return java.util.Date 获取作业结束时间
	 */
	public java.util.Date getWorkEndTime(){
		return workEndTime;
	}
	/**
	 * @param workEndTime 设置作业结束时间
	 */
	public void setWorkEndTime(java.util.Date workEndTime) {
		this.workEndTime = workEndTime;
	}
	/**
	 * @return Long 获取领活人
	 */
	public Long getHandleEmpID(){
		return handleEmpID;
	}
	/**
	 * @param handleEmpID 设置领活人
	 */
	public void setHandleEmpID(Long handleEmpID) {
		this.handleEmpID = handleEmpID;
	}
	/**
	 * @return String 获取领活人名称
	 */
	public String getHandleEmpName(){
		return handleEmpName;
	}
	/**
	 * @param handleEmpName 设置领活人名称
	 */
	public void setHandleEmpName(String handleEmpName) {
		this.handleEmpName = handleEmpName;
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
	 * @return Integer 获取回退标识
	 */
	public Integer getIsBack(){
		return isBack;
	}
	/**
	 * @param isBack 设置回退标识
	 */
	public void setIsBack(Integer isBack) {
		this.isBack = isBack;
	}
	/**
	 * @return Integer 获取回退次数
	 */
	public Integer getBackCount(){
		return backCount;
	}
	/**
	 * @param backCount 设置回退次数
	 */
	public void setBackCount(Integer backCount) {
		this.backCount = backCount;
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
}