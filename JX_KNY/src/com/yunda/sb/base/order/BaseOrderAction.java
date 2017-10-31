package com.yunda.sb.base.order;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.sb.base.IOrder;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：排序控制器，用以响应前端页面进行Ajax请求
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
public abstract class BaseOrderAction<T extends IOrder, S, M extends AbstractOrderManager2<T, S>> extends JXBaseAction<T, S, M> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * <li>说明：排序
	 * <li>创建人：黄杨
	 * <li>创建日期：2017-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void moveOrder() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpServletResponse resp = ServletActionContext.getResponse();
		try {
			// 被排序记录的idx主键
			String idx = req.getParameter("idx");
			// 记录的排序方式
			String orderType = req.getParameter("orderType");
			String[] errMsg = this.manager.validateMoveOrder(idx, Integer.parseInt(orderType));
			if (errMsg == null || errMsg.length < 1) {
				this.manager.updateMoveOrder(idx, Integer.parseInt(orderType));
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("errMsg", errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
