package com.yunda.frame.baseapp.sbtodojob.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.baseapp.sbtodojob.manager.SbTodoJobManager;
import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.TodoJobFunction;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：设备待办项
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SbTodoJobAction extends JXBaseAction<TodoJob, TodoJob, SbTodoJobManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * <li>说明： 获取系统操作员相关任务代办项
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-23
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void getToDoListContext() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse resp = getResponse();
		map.put("success", false);
		try {
			OmEmployee userData = SystemContext.getOmEmployee();
			if (null == userData) {
				map.put("err", "系统异常，请重新登录！");
				return;
			}
			// 获取系统操作员负责的代办项
			List<TodoJob> toDoListContext = TodoJobFunction.getInstance().getToDoListContext(userData.getOperatorid().toString());
			map.put("success", true);
			map.put("todoJob", toDoListContext);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}
		JSONUtil.write(resp, map);
	}

}
