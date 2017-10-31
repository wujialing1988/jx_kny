package com.yunda.frame.baseapp.message.entity;

import javax.persistence.*;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MessageReceiver实体类, 数据表：消息接收者表
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
@Table(name="X_MessageReceiver")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MessageReceiver implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 消息记录及发送表主键（X_MessageSender） */
	private String msgSendIDX;
	/* 如果接收对象类型为部门，则该字段为部门序列（orgSeq）；
        如果接收对象类型为个人，则该字段为用户id（empId）；*/
	private String receiver;
	/* 接收者姓名 */
	private String receiverName;
	/* 接收时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date receiveTime;
    /* 备注 */
    private String remark;
    
    /**
	 * @return String 获取消息记录及发送表主键
	 */
	public String getMsgSendIDX(){
		return msgSendIDX;
	}
	/**
	 * @param 设置消息记录及发送表主键
	 */
	public void setMsgSendIDX(String msgSendIDX) {
		this.msgSendIDX = msgSendIDX;
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
     * @return 获取备注
     */
    public String getRemark() {
        return remark;
    }
    
    /**
     * @param remark 设置备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
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