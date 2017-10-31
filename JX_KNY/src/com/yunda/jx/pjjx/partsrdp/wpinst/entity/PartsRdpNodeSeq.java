package com.yunda.jx.pjjx.partsrdp.wpinst.entity;

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
 * <li>说明：PartsRdpNodeSeq实体类, 数据表：作业节点前后置关系
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
@Table(name="PJJX_Parts_Rdp_Node_Seq")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpNodeSeq implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 作业主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
	/* 流程节点主键 */
	@Column(name="WP_Node_IDX")
	private String wPNodeIDX;
	/* 前置流程节点主键 */
	@Column(name="Pre_WP_Node_IDX")
	private String preWPNodeIDX;
	/* 类型 */
	@Column(name="Seq_Class")
	private String seqClass;
	/* 延隔时间 */
	@Column(name="BEFORE_DELAY_TIME")
	private Long beforeDelayTime;
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
	 * @return String 获取流程节点主键
	 */
	public String getWPNodeIDX(){
		return wPNodeIDX;
	}
	/**
	 * @param wPNodeIDX 设置流程节点主键
	 */
	public void setWPNodeIDX(String wPNodeIDX) {
		this.wPNodeIDX = wPNodeIDX;
	}
	/**
	 * @return String 获取前置流程节点主键
	 */
	public String getPreWPNodeIDX(){
		return preWPNodeIDX;
	}
	/**
	 * @param preWPNodeIDX 设置前置流程节点主键
	 */
	public void setPreWPNodeIDX(String preWPNodeIDX) {
		this.preWPNodeIDX = preWPNodeIDX;
	}
	/**
	 * @return String 获取类型
	 */
	public String getSeqClass(){
		return seqClass;
	}
	/**
	 * @param seqClass 设置类型
	 */
	public void setSeqClass(String seqClass) {
		this.seqClass = seqClass;
	}
	/**
	 * @return true 获取延隔时间
	 */
	public Long getBeforeDelayTime(){
		return beforeDelayTime;
	}
	/**
	 * @param beforeDelayTime 设置延隔时间
	 */
	public void setBeforeDelayTime(Long beforeDelayTime) {
		this.beforeDelayTime = beforeDelayTime;
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