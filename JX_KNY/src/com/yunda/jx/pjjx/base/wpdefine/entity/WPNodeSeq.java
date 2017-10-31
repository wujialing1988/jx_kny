package com.yunda.jx.pjjx.base.wpdefine.entity;

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
 * <li>说明：WPNodeSeq实体类, 数据表：节点前后置关系
 * <li>创建人：何涛
 * <li>创建日期：2014-11-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJJX_WP_Node_Seq")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WPNodeSeq implements java.io.Serializable{
	
	/** 前置节点类型 - FS */
	public static final String CONST_STR_SEQ_CLASS_FS = "FS";
	public static final String CONST_STR_SEQ_CLASS_FS_NAME = "完成-开始";
	/** 前置节点类型 - SS */
	public static final String CONST_STR_SEQ_CLASS_SS = "SS";
	public static final String CONST_STR_SEQ_CLASS_SS_NAME = "开始-开始";
	/** 前置节点类型 - FF */
	public static final String CONST_STR_SEQ_CLASS_FF = "FF";
	public static final String CONST_STR_SEQ_CLASS_FF_NAME = "完成-完成";
	/** 前置节点类型 - SF */
	public static final String CONST_STR_SEQ_CLASS_SF = "SF";
	public static final String CONST_STR_SEQ_CLASS_SF_NAME = "开始-完成";
	
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 节点主键 */
	@Column(name="WP_Node_IDX")
	private String wpNodeIDX;
	/* 前置节点主键 */
	@Column(name="Pre_WP_Node_IDX")
	private String preWPNodeIDX;
	/* 前置节点名称 */
	@Transient
	private String preWPNodeName;
	/* 类型 */
	@Column(name="Seq_Class")
	private String seqClass;
	/* 延隔时间 */
	@Column(name="BEFORE_DELAY_TIME")
	private Integer beforeDelayTime;
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
	 * Default Constructor
	 */
	public WPNodeSeq() {
		super();
	}
	
	/**
	 * @param idx 主键
	 * @param wpNodeIDX 作业节点主键
	 * @param preWPNodeIDX 上级作业节点主键
	 * @param preWPNodeName 上级作业节点名称
	 * @param seqClass 类型
	 * @param beforeDelayTime 延隔时间
	 */
	public WPNodeSeq(String idx, String wpNodeIDX, String preWPNodeIDX, String preWPNodeName, String seqClass, Integer beforeDelayTime) {
		super();
		this.idx = idx;
		this.wpNodeIDX = wpNodeIDX;
		this.preWPNodeIDX = preWPNodeIDX;
		this.preWPNodeName = preWPNodeName;
		this.seqClass = seqClass;
		this.beforeDelayTime = beforeDelayTime;
	}

	/**
	 * @return String 获取节点主键
	 */
	public String getWpNodeIDX(){
		return wpNodeIDX;
	}
	/**
	 * @param wpNodeIDX 设置节点主键
	 */
	public void setWpNodeIDX(String wpNodeIDX) {
		this.wpNodeIDX = wpNodeIDX;
	}
	
	/**
	 * @return 获取前置节点名称
	 */
	public String getPreWPNodeName() {
		return preWPNodeName;
	}
	/**
	 * @param preWPNodeName 设置前置节点名称
	 */
	public void setPreWPNodeName(String preWPNodeName) {
		this.preWPNodeName = preWPNodeName;
	}
	/**
	 * @return String 获取前置节点主键
	 */
	public String getPreWPNodeIDX(){
		return preWPNodeIDX;
	}
	/**
	 * @param preWPNodeIDX 设置前置节点主键
	 */
	public void setPreWPNodeIDX(String preWPNodeIDX) {
		this.preWPNodeIDX = preWPNodeIDX;
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
	/**
	 * @return 获取延隔时间
	 */
	public Integer getBeforeDelayTime() {
		return beforeDelayTime;
	}
	/**
	 * @param beforeDelayTime 设置延隔时间
	 */
	public void setBeforeDelayTime(Integer beforeDelayTime) {
		this.beforeDelayTime = beforeDelayTime;
	}
	/**
	 * @return 获取类型
	 */
	public String getSeqClass() {
		return seqClass;
	}
	/**
	 * @param seqClass 设置类型
	 */
	public void setSeqClass(String seqClass) {
		this.seqClass = seqClass;
	}
	
}