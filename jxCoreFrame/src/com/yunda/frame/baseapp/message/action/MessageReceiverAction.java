package com.yunda.frame.baseapp.message.action; 

import org.apache.log4j.Logger;

import com.yunda.frame.baseapp.message.entity.MessageReceiver;
import com.yunda.frame.baseapp.message.manager.MessageReceiverManager;
import com.yunda.frame.common.*;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MessageReceiver控制器, 消息接收者表
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-07-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class MessageReceiverAction extends JXBaseAction<MessageReceiver, MessageReceiver, MessageReceiverManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
}