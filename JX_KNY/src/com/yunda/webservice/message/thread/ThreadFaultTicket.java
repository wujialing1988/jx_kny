package com.yunda.webservice.message.thread;

import com.yunda.webservice.message.MessageService;
import org.apache.log4j.Logger;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 提票消息线程
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-7
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class ThreadFaultTicket implements Runnable {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    // 消息类型
    private static String MSG_TYPE = "FaultTicket" ;
    
    private MessageService messageService ;
    
    private String title ;
    
    private String content ;
    
    private String receiver ;
    
    /**
     * <li>说明：构造函数
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-7
     * <li>修改人： 
     * <li>修改日期：
     * @param messageService 消息发送业务
     * @param title 消息头
     * @param content 消息内容
     * @param receiver 消息接收者
     */
    public ThreadFaultTicket(MessageService messageService,String title,String content,String receiver) {
        this.messageService = messageService;
        this.title = title;
        this.content = content;
        this.receiver = receiver;
    }
    
    public void run() {
        try {
            messageService.sendTpMsg(title, content, receiver,MSG_TYPE);
        } catch (Exception e) {
            logger.error("发送提票推送消息失败：", e);
        }
        
    }
    
}
