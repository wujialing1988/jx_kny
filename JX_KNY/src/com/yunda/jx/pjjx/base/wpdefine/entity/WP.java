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
 * <li>说明：WP实体类, 数据表：检修作业流程
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
@Table(name="PJJX_WP")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WP implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 编号 */
	@Column(name="WP_No")
	private String wPNo;
	/* 描述 */
	@Column(name="WP_Desc")
	private String wPDesc;
	/* 额定工期 */
	@Column(name="Rated_Period")
	private Double ratedPeriod;
	/* 额定工时 */
	@Column(name="Rated_WorkTime")
	private Double ratedWorkTime;
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

	/**
	 * @return String 获取编号
	 */
	public String getWPNo(){
		return this.wPNo;
	}
	/**
	 * @param wPNo 设置编号
	 */
	public void setWPNo(String wPNo) {
		this.wPNo = wPNo;
	}
	/**
	 * @return String 获取描述
	 */
	public String getWPDesc(){
		return this.wPDesc;
	}
	/**
	 * @param wPDesc 设置描述
	 */
	public void setWPDesc(String wPDesc) {
		this.wPDesc = wPDesc;
	}
	/**
	 * @return Double 获取额定工期
	 */
	public Double getRatedPeriod(){
		return this.ratedPeriod;
	}
	/**
	 * @param ratedPeriod 设置额定工期
	 */
	public void setRatedPeriod(Double ratedPeriod) {
		this.ratedPeriod = ratedPeriod;
	}
	/**
	 * @return Double 获取额定工时
	 */
	public Double getRatedWorkTime(){
		return this.ratedWorkTime;
	}
	/**
	 * @param ratedWorkTime 设置额定工时
	 */
	public void setRatedWorkTime(Double ratedWorkTime) {
		this.ratedWorkTime = ratedWorkTime;
	}
	/**
	 * @return String 获取备注
	 */
	public String getRemarks(){
		return this.remarks;
	}
	/**
	 * @param remarks 设置备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus(){
		return this.recordStatus;
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
		return this.siteID;
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
		return this.creator;
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
		return this.createTime;
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
		return this.updator;
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
		return this.updateTime;
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
		return this.idx;
	}
	/**
	 * @param idx 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
	
	@Transient
	/** 查询字段 - 额定工期下限 */
	private Long ratedPeriodFrom;
	
	@Transient
	/** 查询字段 - 额定工期上限 */
	private Long ratedPeriodTo;

	public Long getRatedPeriodFrom() {
		return this.ratedPeriodFrom;
	}
	public void setRatedPeriodFrom(Long ratedPeriodFrom) {
		this.ratedPeriodFrom = ratedPeriodFrom;
	}
	public Long getRatedPeriodTo() {
		return this.ratedPeriodTo;
	}
	public void setRatedPeriodTo(Long ratedPeriodTo) {
		this.ratedPeriodTo = ratedPeriodTo;
	}
	
}