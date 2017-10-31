package com.yunda.sb.base.common.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.sb.base.common.entity.SystemMessageReceiver;
import com.yunda.sb.base.common.manager.SystemMessageReceiverManger;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: SystemMessageReceiver控制器，数据表：T_SYSTEM_MESSAGE_RECEIVER
 * <li>创建人：何涛
 * <li>创建日期：2016年9月19日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@SuppressWarnings("serial")
public class SystemMessageReceiverAction extends JXBaseAction<SystemMessageReceiver, SystemMessageReceiver, SystemMessageReceiverManger> {
	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());
	
	/**
	 * <li>说明：系统操作者确认接收消息后，记录消息的接收人、班组等信息
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 */
	public void receive() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 系统消息idx主键
			String systemMessageIdx = req.getParameter("systemMessageIdx");
			// 接收状态，0：忽略，1：确认（查看）
			String receiveState = req.getParameter("receiveState");
			this.manager.receive(systemMessageIdx, Integer.parseInt(receiveState));
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
