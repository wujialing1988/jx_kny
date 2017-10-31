package com.yunda.baseapp.message.link;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 凌凯短信数据库引擎-消息发送表
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-8-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="mobilesend")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class LinkMobileSend {
	/** 未提交 */
	public static int NO_CONFIRM = 0;
	/** 发送成功 */
	public static int SEND_SUCCESS = 1;
	/** 发送失败 */
	public static int SEND_FAIL = 2;
//	  `ID` int(11) NOT NULL AUTO_INCREMENT,
//	  `Mobile` varchar(100) NOT NULL,
//	  `Content` varchar(500) NOT NULL,
//	  `Flag` int(11) NOT NULL DEFAULT '0',
//	  `CreateDate` datetime NOT NULL,
//	  `DelaySendTime` datetime DEFAULT NULL,
//	  `RealSendTime` datetime DEFAULT NULL,
//	  `sCell` varchar(50) DEFAULT NULL,
//	  `Status` varchar(200) DEFAULT NULL,	
	/** 发送记录ID */
	@Id @GeneratedValue(strategy=GenerationType.AUTO) @Column(name="ID") 
	private Long id;
	/** 发送手机号 */
	@Column(name="Mobile",updatable=false,nullable=false)
	private String mobile;
	/** 发送内容 */
	@Column(name="Content",updatable=false,nullable=false,length=500)
	private String content;
	/** 发送标志 0：未提交,1：发送成功,2:发送失败 */
	@Column(name="Flag",updatable=false)
	private int flag = NO_CONFIRM;
//	/** 发送时间 */
//	@Column(name="sendDate",updatable=false) @Temporal(TemporalType.TIMESTAMP)
//	private Date sendDate;
	/** 消息创建时间 */
	@Column(name="CreateDate",updatable=false,nullable=false) @Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();
	/** 定时发送时间 固定14位长度字符串，比如：20060912152435代表2006年9月12日15时24分35秒，为空表示立即发送 */
	@Column(name="DelaySendTime",updatable=false) @Temporal(TemporalType.TIMESTAMP)
	private Date delaySendTime;
	/** 消息实际提交时间 */
	@Column(name="RealSendTime",updatable=false) @Temporal(TemporalType.TIMESTAMP)
	private Date realSendTime;
	@Column(name="sCell",updatable=false)
	private String scell;
	@Column(name="Status",updatable=false)
	private String status;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getDelaySendTime() {
		return delaySendTime;
	}
	public void setDelaySendTime(Date delaySendTime) {
		this.delaySendTime = delaySendTime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getRealSendTime() {
		return realSendTime;
	}
	public void setRealSendTime(Date realSendTime) {
		this.realSendTime = realSendTime;
	}
	public String getScell() {
		return scell;
	}
	public void setScell(String scell) {
		this.scell = scell;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
