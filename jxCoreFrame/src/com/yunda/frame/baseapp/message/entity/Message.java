package com.yunda.frame.baseapp.message.entity;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Message实体类, 数据表：消息记录表
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-06-04
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="V_X_Message")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class Message implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/* 消息接收者表主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 标题 */
	private String title;
	/* 发送者empid（OM_Employee表主键），-1表示发送者为系统 */
	private Long sender;
	/* 发送者姓名 */
	private String senderName;
	/* 发送模式，按位来识别。第0位为1表示在线发送，第1位为1表示短信发送，第2位为1表示邮件发送，以此类推。 */
	private Integer sendMode;
	/* 发送时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date sendTime;
	/* 内容 */
	private String content;
	/* 链接页面标题 */
	private String pageTitle;
	/* 链接页面地址 */
	private String url;
	/* 显示url链接页面的方式，1在选项卡tab中打开，2打开新的独立页面 */
	private Integer showPageMode;
    /* 如果接收对象类型为部门，则该字段为部门序列（orgSeq）；
    如果接收对象类型为个人，则该字段为用户id（empId）；*/
	private String receiver;
	/* 接收者姓名 */
	private String receiverName;
	/* 接收时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date receiveTime;
	/**
	 * @return String 获取标题
	 */
	public String getTitle(){
		return title;
	}
	/**
	 * @param 设置标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return Long 获取发送者
	 */
	public Long getSender(){
		return sender;
	}
	/**
	 * @param 设置发送者
	 */
	public void setSender(Long sender) {
		this.sender = sender;
	}
	/**
	 * @return String 获取发送者姓名
	 */
	public String getSenderName(){
		return senderName;
	}
	/**
	 * @param 设置发送者姓名
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	/**
	 * @return java.util.Date 获取发送时间
	 */
	public java.util.Date getSendTime(){
		return sendTime;
	}
	/**
	 * @param 设置发送时间
	 */
	public void setSendTime(java.util.Date sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 * @return Long 获取接收者
	 */
	public String getReceiver(){
		return receiver;
	}
	/**
	 * @param 设置接收者
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	/**
	 * @return String 获取接收者姓名
	 */
	public String getReceiverName(){
		return receiverName;
	}
	/**
	 * @param 设置接收者姓名
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	/**
	 * @return java.util.Date 获取接收时间
	 */
	public java.util.Date getReceiveTime(){
		return receiveTime;
	}
	/**
	 * @param 设置接收时间
	 */
	public void setReceiveTime(java.util.Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	/**
	 * @return String 获取内容
	 */
	public String getContent(){
		return content;
	}
	/**
	 * @param 设置内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return String 获取链接页面地址
	 */
	public String getUrl(){
		return url;
	}
	/**
	 * @param 设置链接页面地址
	 */
	public void setUrl(String url) {
		this.url = url;
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
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public Integer getSendMode() {
		return sendMode;
	}
	public void setSendMode(Integer sendMode) {
		this.sendMode = sendMode;
	}
	public Integer getShowPageMode() {
		return showPageMode;
	}
	public void setShowPageMode(Integer showPageMode) {
		this.showPageMode = showPageMode;
	}
}