package com.yunda.zb.schedule.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.zb.schedule.entity.ScheduleProposeAndApprove;
import com.yunda.zb.schedule.manager.ScheduleProposeAndApproveManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：调度命令单
 * <li>创建人：黄杨
 * <li>创建日期：2017-6-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ScheduleProposeAndApproveAction extends JXBaseAction<ScheduleProposeAndApprove, ScheduleProposeAndApprove, ScheduleProposeAndApproveManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明： 发送申请
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-6-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void sendApplication() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);
		String ids = getRequest().getParameter("ids");
		try {
			// 调度命令单主键数组
			String[] idxs = JSONUtil.read(ids, String[].class);
			this.manager.sendApplication(idxs);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}
		JSONUtil.write(getResponse(), map);
	}

	/**
	 * <li>说明： 撤销申请
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-6-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void rovokeApplication() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);
		String ids = getRequest().getParameter("ids");
		try {
			// 调度命令单主键数组
			String[] idxs = JSONUtil.read(ids, String[].class);
			this.manager.rovokeApplication(idxs);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}
		JSONUtil.write(getResponse(), map);
	}

	/**
	 * <li>说明： 审批
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-6-27
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws JsonMappingException
	 *@throws IOException
	 */
	public void approveApplication() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Success", false);
		String ids = getRequest().getParameter("ids");
		try {
			// 调度命令单主键数组
			String[] idxs = JSONUtil.read(ids, String[].class);
			this.manager.approveApplication(idxs);
			map.put("Success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}
		JSONUtil.write(getResponse(), map);
	}

}
