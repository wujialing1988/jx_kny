package com.yunda.jx.scdd.taskprogress.entity;

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
 * <li>说明：TrainWPDetail实体类, 数据表：机车作业进度项
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
@Table(name="SCDD_Train_WP_Detail")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TrainWPDetail implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/**
	 * 数据来源，Auto：自动生成
	 */
	public static final String DATASOURCE_A = "Auto";
	/**
	 * 数据来源，Manual：手动维护
	 */
	public static final String DATASOURCE_M = "Manual";
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 作业进度主键 */
	@Column(name="Train_WP_IDX")
	private String trainWPIDX;
	/* 进度项代码 */
	@Column(name="Progress_Code")
	private String progressCode;
	/* 进度项名称 */
	@Column(name="Progress_Name")
	private String progressName;
	/* 进度项值 */
	@Column(name="Progress_Value")
	private String progressValue;
	/* 数据来源，Auto：自动生成；Manual：手动维护 */
	@Column(name="Data_Source")
	private String dataSource;
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
	 *  数据类型
	 */
	@Transient
	private String dataType;
	/**
	 * @return String 获取作业进度主键
	 */
	public String getTrainWPIDX(){
		return trainWPIDX;
	}
	/**
	 * @param 设置作业进度主键
	 */
	public void setTrainWPIDX(String trainWPIDX) {
		this.trainWPIDX = trainWPIDX;
	}
	/**
	 * @return String 获取进度项代码
	 */
	public String getProgressCode(){
		return progressCode;
	}
	/**
	 * @param 设置进度项代码
	 */
	public void setProgressCode(String progressCode) {
		this.progressCode = progressCode;
	}
	/**
	 * @return String 获取进度项名称
	 */
	public String getProgressName(){
		return progressName;
	}
	/**
	 * @param 设置进度项名称
	 */
	public void setProgressName(String progressName) {
		this.progressName = progressName;
	}
	public String getProgressValue() {
		return progressValue;
	}
	public void setProgressValue(String progressValue) {
		this.progressValue = progressValue;
	}
	/**
	 * @return String 获取数据来源
	 */
	public String getDataSource(){
		return dataSource;
	}
	/**
	 * @param 设置数据来源
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
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
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}