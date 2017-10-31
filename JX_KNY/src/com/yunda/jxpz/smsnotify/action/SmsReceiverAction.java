package com.yunda.jxpz.smsnotify.action; 

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.jxpz.smsnotify.entity.SmsReceiver;
import com.yunda.jxpz.smsnotify.manager.SmsReceiverManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：SMSRECEIVER控制器, 短信通知
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-08-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class SmsReceiverAction extends JXBaseAction<SmsReceiver, SmsReceiver, SmsReceiverManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
}