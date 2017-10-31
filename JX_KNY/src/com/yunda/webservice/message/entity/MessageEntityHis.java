package com.yunda.webservice.message.entity;

import java.io.Serializable;
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
 * <li>说明: 消息推送记录实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-7
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JX_MESSAGE_ENTITY_HIS")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MessageEntityHis implements Serializable {

    /**  类型：long  */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
    private String idx;
    
    /* 关键字 */
    @Column(name="KEY_WORD")
    private String keyWord;
    
    /* 消息内容 */
    @Column(name="CONTENT")
    private String content ;
    
    /* 接收人 */
    @Column(name="RECEIVER")
    private String receiver ;
    
    /* 消息类型 */
    @Column(name="MSG_TYPE")
    private String msgType;
    
    /* 返回消息*/
    @Column(name="RETURN_MSG")
    private String returnMsg ;
    
    /* 是否发送成功*/
    @Column(name="IS_SUCCESS")
    private Boolean isSuccess ;
    
    /* 发送时间*/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="SEND_TIME")
    private Date sendTime;
    
    
    /**
     * <li>说明：默认构造函数
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-7
     * <li>修改人： 
     * <li>修改日期：
     */
    public MessageEntityHis() {
        this.setIsSuccess(true);
        this.setSendTime(new Date());
    }

    
    public String getContent() {
        return content;
    }

    
    public void setContent(String content) {
        this.content = content;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public Boolean getIsSuccess() {
        return isSuccess;
    }

    
    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    
    public String getKeyWord() {
        return keyWord;
    }

    
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    
    public String getReceiver() {
        return receiver;
    }

    
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    
    public String getReturnMsg() {
        return returnMsg;
    }

    
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    
    public Date getSendTime() {
        return sendTime;
    }

    
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
    
}
