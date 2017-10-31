package com.yunda.jxpz.smsnotify.entity;

import java.util.Date;

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
 * <li>说明：SMSRECEIVER实体类, 数据表：通知人员
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
@Table(name="JXPZ_SMS_RECEIVER")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class SmsReceiver implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	public SmsReceiver() {
	}
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 人员编号 */
	@Column(name="RECEIVER_ID")
	private String receiverId;
	/* 人员名称 */
	@Column(name="RECEIVER_NAME")
	private String receiverName;
	/* 手机号码 */
	@Column(name="RECEIVER_PHONE_NUM")
	private String receiverPhoneNum;
	/* 消息通知主键 */
	@Column(name="NOTICE_IDX")
	private String noticeIdx;
	/* 通知时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="NOTIFY_DATE")
	private java.util.Date notifyDate;
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
	 * @return String 获取人员编号
	 */
	public String getReceiverId(){
		return receiverId;
	}
	/**
	 * @param 设置人员编号
	 */
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	/**
	 * @return String 获取人员名称
	 */
	public String getReceiverName(){
		return receiverName;
	}
	/**
	 * @param 设置人员名称
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	/**
	 * @return String 获取手机号码
	 */
	public String getReceiverPhoneNum(){
		return receiverPhoneNum;
	}
	/**
	 * @param 设置手机号码
	 */
	public void setReceiverPhoneNum(String receiverPhoneNum) {
		this.receiverPhoneNum = receiverPhoneNum;
	}
	/**
	 * @return String 获取消息通知主键
	 */
	public String getNoticeIdx(){
		return noticeIdx;
	}
	/**
	 * @param 设置消息通知主键
	 */
	public void setNoticeIdx(String noticeIdx) {
		this.noticeIdx = noticeIdx;
	}
	/**
	 * @return java.util.Date 获取通知时间
	 */
	public java.util.Date getNotifyDate(){
		return notifyDate;
	}
	/**
	 * @param 设置通知时间
	 */
	public void setNotifyDate(java.util.Date notifyDate) {
		this.notifyDate = notifyDate;
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
	public SmsReceiver(String receiverId, String receiverName, String receiverPhoneNum) {
		super();
		this.receiverId = receiverId;
		this.receiverName = receiverName;
		this.receiverPhoneNum = receiverPhoneNum;
		this.notifyDate = new Date();
	}
}