package com.yunda.frame.yhgl.action;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.yhgl.entity.LoginLog;
import com.yunda.frame.yhgl.manager.LoginLogManager;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: LoginLog控制类，登录日志
 * <li>创建人：程梅
 * <li>创建日期：2016-4-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class LoginLogAction extends JXBaseAction <LoginLog,LoginLog,LoginLogManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
}
