package com.yunda.jx.pjjx.partsrdp.tecinst.entity;

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
 * <li>说明：PartsRdpTecCard实体类, 数据表：配件检修工艺工单
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
@Table(name="PJJX_Parts_Rdp_Tec_Card")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpTecCard implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/** 检修工艺工单状态 - 已终止*/
	public static final String STATUS_YZZ = "10";
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
	/* 工艺实例主键 */
	@Column(name="Rdp_Tec_IDX")
	private String rdpTecIDX;
	/* 工艺卡主键 */
	@Column(name="Tec_Card_IDX")
	private String tecCardIDX;
	/* 工艺卡编号 */
	@Column(name="Tec_Card_No")
	private String tecCardNo;
	/* 工艺卡描述 */
	@Column(name="Tec_Card_Desc")
	private String tecCardDesc;
	/* 顺序号 */
	@Column(name="Seq_No")
	private Integer seqNo;
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
	 * @return String 获取工艺实例主键
	 */
	public String getRdpTecIDX(){
		return rdpTecIDX;
	}
	/**
	 * @param rdpTecIDX 设置工艺实例主键
	 */
	public void setRdpTecIDX(String rdpTecIDX) {
		this.rdpTecIDX = rdpTecIDX;
	}
	/**
	 * @return String 获取工艺卡主键
	 */
	public String getTecCardIDX(){
		return tecCardIDX;
	}
	/**
	 * @param tecCardIDX 设置工艺卡主键
	 */
	public void setTecCardIDX(String tecCardIDX) {
		this.tecCardIDX = tecCardIDX;
	}
	/**
	 * @return String 获取工艺卡编号
	 */
	public String getTecCardNo(){
		return tecCardNo;
	}
	/**
	 * @param tecCardNo 设置工艺卡编号
	 */
	public void setTecCardNo(String tecCardNo) {
		this.tecCardNo = tecCardNo;
	}
	/**
	 * @return String 获取工艺卡描述
	 */
	public String getTecCardDesc(){
		return tecCardDesc;
	}
	/**
	 * @param tecCardDesc 设置工艺卡描述
	 */
	public void setTecCardDesc(String tecCardDesc) {
		this.tecCardDesc = tecCardDesc;
	}
	/**
	 * @return Integer 获取顺序号
	 */
	public Integer getSeqNo(){
		return seqNo;
	}
	/**
	 * @param seqNo 设置顺序号
	 */
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
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