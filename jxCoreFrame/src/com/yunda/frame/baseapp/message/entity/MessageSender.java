package com.yunda.frame.baseapp.message.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
 * <li>说明：MessageSender实体类, 数据表：消息记录发送者表
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-07-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="X_MessageSender")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MessageSender implements java.io.Serializable{
	/** 消息发送模式  第0位为1表示在线发送*/
	private static int ONLINE = 1;
	/** 消息发送模式  第1位为1表示短信发送*/
	private static int SMS = 2;
	/** 消息发送模式  第2位为1表示邮件发送*/
	private static int EMAIL = 4;
	/** 链接页面打开方式 1在选项卡tab中打开*/
	public static int OPEN_TAB = 1;
	/** 链接页面打开方式 2打开新的独立页面*/
	public static int OPEN_NEW = 2;
    
    /** 是否自动关闭窗体 - 否 */
	public static final int AUTO_CLOSED_F = 0;
	/** 是否自动关闭窗体 - 是 */
    public static final int AUTO_CLOSED_T = 1;
    
    /** 接收对象类型 - 部门 */
    public static final int RECEIVER_TYPE_DEPART = 1;
    /** 接收对象类型 - 多个接收者仅需一个人接收即可 */
    public static final int RECEIVER_TYPE_MANY_TO_ONE = 2;
    /** 接收对象类型 - 多个接收者需所有人接收 */
    public static final int RECEIVER_TYPE_MANY_TO_ALL = 3;
	
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    
	/* idx主键 */
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
    
    /* 是否自动关闭窗体(0表示不自动关闭，1表示自动关闭) */
    private Integer autoClosed;
    
    /* 接收对象类型(0表示个人，1表示部门)，
        如果接收对象类型为部门，则消息接收者（receiver）存部门序列（orgSeq）；
        如果接收对象类型为个人，则消息接收者（receiver）存用户id（empId）；*/
    private Integer receiverType;
    
    /** 消息接收者数组 */
    @Transient
    private List<MessageReceiver> receiverArray;

	/**
     * @return 获取消息接收者数组
	 */
    public List<MessageReceiver> getReceiverArray() {
        return receiverArray;
    }
    
    /**
     * @param receiverArray 设置消息接收者数组
     */
    public void setReceiverArray(List<MessageReceiver> receiverArray) {
        this.receiverArray = receiverArray;
    }
    
    /**
	 * <li>说明：默认设置不自动关闭窗体、接收对象类型为个人
	 * <li>创建人：何涛
	 * <li>创建日期：2015-3-4
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public MessageSender() {
        super();
        this.autoClosed = AUTO_CLOSED_F;
        this.receiverType = RECEIVER_TYPE_MANY_TO_ONE;
    }
    /**
	 * <li>说明：设置在线发送模式
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void setOnlineMode(){
		sendMode = sendMode == null ? ONLINE : (sendMode | ONLINE);
	}
	/**
	 * <li>说明：是否为在线发送模式
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public boolean isOnlineMode(){
		if(sendMode == null)	return false;
		return (sendMode & ONLINE) == 1;
	}	
	/**
	 * <li>说明：设置手机短信发送模式
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void setSMSMode(){
		sendMode = sendMode == null ? SMS : (sendMode | SMS);
	}
	/**
	 * <li>说明：是否为手机短信发送模式
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public boolean isSMSMode(){
		if(sendMode == null)	return false;
		return (sendMode & SMS) == 1;
	}	
	/**
	 * <li>说明：设置邮件发送模式
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void setEmailMode(){
		sendMode = sendMode == null ? EMAIL : (sendMode | EMAIL);
	}	
	/**
	 * <li>说明：是否为邮件发送模式
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public boolean isEmailMode(){
		if(sendMode == null)	return false;
		return (sendMode & EMAIL) == 1;
	}	
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
	 * @return Integer 获取发送模式
	 */
	public Integer getSendMode(){
		return sendMode;
	}
	/**
	 * @param 设置发送模式
	 */
	public void setSendMode(Integer sendMode) {
		this.sendMode = sendMode;
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
	 * @return true 获取内容
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
	 * @return String 获取链接页面标题
	 */
	public String getPageTitle(){
		return pageTitle;
	}
	/**
	 * @param 设置链接页面标题
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
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
	 * @return Integer 获取显示页面模式
	 */
	public Integer getShowPageMode(){
		return showPageMode;
	}
	/**
	 * @param 设置显示页面模式
	 */
	public void setShowPageMode(Integer showPageMode) {
		this.showPageMode = showPageMode;
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
    
    /**
     * @return 获取是否自动关闭
     */
    public Integer getAutoClosed() {
        return autoClosed;
    }
    
    /**
     * @param autoClosed 设置是否自动关闭
     */
    public void setAutoClosed(Integer autoClosed) {
        this.autoClosed = autoClosed;
    }
    
    /**
     * @return 获取接收对象类型
     */
    public Integer getReceiverType() {
        return receiverType;
    }
    
    /**
     * @param receiverType 设置接收对象类型
     */
    public void setReceiverType(Integer receiverType) {
        this.receiverType = receiverType;
    }
    
    /**
     * <li>说明：根据消息的发送时间和消息有效时间验证该条消息是否继续有效
     * <li>创建人：何涛
     * <li>创建日期：2015-3-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param validTime
     * @return
     */
    public boolean isValid(int validTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.sendTime);
        calendar.add(Calendar.HOUR, validTime);
        return calendar.getTime().after(new Date());
    }
    
}