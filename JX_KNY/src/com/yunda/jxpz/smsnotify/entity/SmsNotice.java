package com.yunda.jxpz.smsnotify.entity;

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
 * <li>说明：SMSNOTICE实体类, 数据表：短信通知
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-08-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXPZ_SMS_NOTICE")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class SmsNotice implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 提票派工短信通知
	 */
	public static final String SMSNOTICE_TYPE_FAULT_CH = "提票派工短信通知";
	public static final String SMSNOTICE_TYPE_FAULT = "smsnotice_type_fault";
	/**
	 * 工单派工短信通知
	 */
	public static final String SMSNOTICE_TYPE_WORK_CARD_CH = "工单派工短信通知";
	public static final String SMSNOTICE_TYPE_WORK_CARD = "smsnotice_type_work_card";
	
	public SmsNotice() {
	}
	
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 短信通知内容 */
	@Column(name="SMS_MESSAGE_CONTENT")
	private String smsMessageContent;
	/* 短信通知类型 */
	@Column(name="SMS_NOTIFY_TYPE")
	private String smsNotifyType;
	/* 业务数据主键 */
	@Column(name="SOURCE_IDX")
	private String sourceIdx;
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

	public SmsNotice(String smsMessageContent, String smsNotifyType, String sourceIdx) {
		super();
		this.smsMessageContent = smsMessageContent;
		this.smsNotifyType = smsNotifyType;
		this.sourceIdx = sourceIdx;
	}
	/**
	 * @return String 获取短信通知内容
	 */
	public String getSmsMessageContent(){
		return smsMessageContent;
	}
	/**
	 * @param 设置短信通知内容
	 */
	public void setSmsMessageContent(String smsMessageContent) {
		this.smsMessageContent = smsMessageContent;
	}
	/**
	 * @return String 获取短信通知类型
	 */
	public String getSmsNotifyType(){
		return smsNotifyType;
	}
	/**
	 * @param 设置短信通知类型
	 */
	public void setSmsNotifyType(String smsNotifyType) {
		this.smsNotifyType = smsNotifyType;
	}
	/**
	 * @return String 获取业务数据主键
	 */
	public String getSourceIdx(){
		return sourceIdx;
	}
	/**
	 * @param 设置业务数据主键
	 */
	public void setSourceIdx(String sourceIdx) {
		this.sourceIdx = sourceIdx;
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