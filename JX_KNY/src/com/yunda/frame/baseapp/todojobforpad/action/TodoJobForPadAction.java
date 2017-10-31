package com.yunda.frame.baseapp.todojobforpad.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.baseapp.todojobforpad.entity.TodoJobForPad;
import com.yunda.frame.baseapp.todojobforpad.manager.TodoJobForPadManager;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：pad移动终端待办事项控制器
 * <li>创建人：程锐
 * <li>创建日期：2014-12-8
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class TodoJobForPadAction extends JXBaseAction<TodoJobForPad, TodoJobForPad, TodoJobForPadManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());	
	/**
	 * <li>说明：查询待办事项统计列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-12-8
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void queryTodoJobList() throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		List<TodoJobForPad> list = new ArrayList<TodoJobForPad>();
		try {			
            String workStationIDX = getRequest().getParameter("partsWorkStationIDX");
			list = this.getManager().queryTodoJobList(workStationIDX);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), list);
		}
	}
}
