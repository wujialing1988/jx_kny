package com.yunda.jx.scdd.taskprogress.entity;

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
 * <li>说明：TrainWP实体类, 数据表：机车作业进度
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="SCDD_Train_WP")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TrainWP implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 车型主键 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	/* 车型英文简称 */
	@Column(name="Train_Type_ShortName")
	private String trainTypeShortName;
	/* 车号 */
	@Column(name="Train_No")
	private String trainNo;
	/* 配属局简称 */
	@Column(name="B_ShortName")
	private String bShortName;
	/* 配属段简称 */
	@Column(name="D_ShortName")
	private String dShortName;
	/* 委修段简称 */
	@Column(name="Delegate_D_Shortname")
	private String delegateDShortname;
	/* 兑现单主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
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
	 * @return String 获取车型主键
	 */
	public String getTrainTypeIDX(){
		return trainTypeIDX;
	}
	/**
	 * @param 设置车型主键
	 */
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}
	/**
	 * @return String 获取车型简称
	 */
	public String getTrainTypeShortName(){
		return trainTypeShortName;
	}
	/**
	 * @param 设置车型简称
	 */
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
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
	 * @return String 获取配属局简称
	 */
	public String getBShortName(){
		return bShortName;
	}
	/**
	 * @param 设置配属局简称
	 */
	public void setBShortName(String bShortName) {
		this.bShortName = bShortName;
	}
	/**
	 * @return String 获取配属段简称
	 */
	public String getDShortName(){
		return dShortName;
	}
	/**
	 * @param 设置配属段简称
	 */
	public void setDShortName(String dShortName) {
		this.dShortName = dShortName;
	}
	/**
	 * @return String 获取委修段简称
	 */
	public String getDelegateDShortname(){
		return delegateDShortname;
	}
	/**
	 * @param 设置委修段简称
	 */
	public void setDelegateDShortname(String delegateDShortname) {
		this.delegateDShortname = delegateDShortname;
	}
	/**
	 * @return String 获取兑现单主键
	 */
	public String getRdpIDX(){
		return rdpIDX;
	}
	/**
	 * @param 设置兑现单主键
	 */
	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
	}
	/**
	 * @return String 获取备注
	 */
	public String getRemarks(){
		return remarks;
	}
	/**
	 * @param 设置备注
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
}