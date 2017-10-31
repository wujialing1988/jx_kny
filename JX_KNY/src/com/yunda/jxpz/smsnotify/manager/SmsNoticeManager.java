package com.yunda.jxpz.smsnotify.manager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jxpz.smsnotify.entity.SmsNotice;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：SMSNOTICE业务类,短信通知
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-08-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="smsNoticeManager")
public class SmsNoticeManager extends JXBaseManager<SmsNotice, SmsNotice>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
}