package com.yunda.sb.inspect.route.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.sb.inspect.route.entity.InspectRoute;
import com.yunda.sb.inspect.route.manager.InspectRouteManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectRoute控制器，数据表：E_INSPECT_ROUTE
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class InspectRouteAction extends JXBaseAction<InspectRoute, InspectRoute, InspectRouteManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * <li>说明： 查询设备巡检线路树
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void tree() throws IOException {
		List<Map<String, Object>> tree = null;
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String states = req.getParameter("states");
			String expirible = req.getParameter("expirible");
			tree = this.manager.tree(states, Boolean.parseBoolean(expirible));
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(resp, tree);
		}
	}

}
