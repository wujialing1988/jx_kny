package com.yunda.frame.order;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;

/**
 * <li>标题：机车配件检修管理信息系统
 * <li>说明：排序控制器，用以响应前端页面进行Ajax请求
 * <li>创建人： 何涛
 * <li>创建日期： 2014-11-18 下午03:49:26
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @param <T> 实体类
 * @param <S> 查询实体类
 * @param <M> 管理类
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public abstract class AbstractOrderAction<T, S, M extends AbstractOrderManager<T, S>> extends JXBaseAction<T, S, M> {
	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * <li>说明：排序
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public final void moveOrder() throws JsonMappingException, IOException {
		HttpServletRequest req = getRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 被排序记录的idx主键
			String idx = req.getParameter("idx");
			// 记录的排序方式
			String orderType =  req.getParameter("orderType");
			String[] errMsg = this.manager.validateMoveOrder(idx, Integer.parseInt(orderType));
			if (errMsg == null || errMsg.length < 1) {
				this.manager.updateMoveOrder(idx, Integer.parseInt(orderType));
				map.put("success", "true");
			}else{
				map.put("success", "false");
				map.put("errMsg", errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
}
