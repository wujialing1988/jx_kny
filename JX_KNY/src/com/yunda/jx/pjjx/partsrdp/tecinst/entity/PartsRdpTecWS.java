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
 * <li>说明：PartsRdpTecWS实体类, 数据表：配件检修工序实例
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
@Table(name="PJJX_Parts_Rdp_Tec_WS")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpTecWS implements java.io.Serializable{
	
	/** 状态 - 未处理 */
	public static final String CONST_STR_STATUS_WCL = "01";
	/** 状态 - 已处理 */
	public static final String CONST_STR_STATUS_YCL = "02";
	
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
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
	/* 工艺工单主键 */
	@Column(name="RDP_TEC_CARD_IDX")
	private String rdpTecCardIDX;
	/* 工序主键 */
	@Column(name="WS_IDX")
	private String wsIDX;
	/* 工序编号 */
	@Column(name="WS_No")
	private String wsNo;
	/* 工序名称 */
	@Column(name="WS_Name")
	private String wsName;
	/* 工序描述 */
	@Column(name="WS_Desc")
	private String wsDesc;
	/* 上级工序主键 */
	@Column(name="WS_PARENT_IDX")
	private String wsParentIDX;
	/* 顺序号 */
	@Column(name="Seq_No")
	private Integer seqNo;
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
	 * @return String 获取工艺工单主键
	 */
	public String getRdpTecCardIDX(){
		return rdpTecCardIDX;
	}
	/**
	 * @param rdpTecCardIDX 设置工艺工单主键
	 */
	public void setRdpTecCardIDX(String rdpTecCardIDX) {
		this.rdpTecCardIDX = rdpTecCardIDX;
	}
	/**
	 * @return String 获取工序主键
	 */
	public String getWsIDX(){
		return wsIDX;
	}
	/**
	 * @param wSIDX 设置工序主键
	 */
	public void setWsIDX(String wSIDX) {
		this.wsIDX = wSIDX;
	}
	/**
	 * @return String 获取工序编号
	 */
	public String getWsNo(){
		return wsNo;
	}
	/**
	 * @param wSNo 设置工序编号
	 */
	public void setWsNo(String wSNo) {
		this.wsNo = wSNo;
	}
	/**
	 * @return String 获取工序名称
	 */
	public String getWsName(){
		return wsName;
	}
	/**
	 * @param wsName 设置工序名称
	 */
	public void setWsName(String wsName) {
		this.wsName = wsName;
	}
	/**
	 * @return String 获取工序描述
	 */
	public String getWsDesc(){
		return wsDesc;
	}
	/**
	 * @param wSDesc 设置工序描述
	 */
	public void setWsDesc(String wSDesc) {
		this.wsDesc = wSDesc;
	}
	/**
	 * @return 获取上级工序主键
	 */
	public String getWsParentIDX() {
		return wsParentIDX;
	}
	/**
	 * @param wsParentIDX 设置上级工序主键
	 */
	public void setWsParentIDX(String wsParentIDX) {
		this.wsParentIDX = wsParentIDX;
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