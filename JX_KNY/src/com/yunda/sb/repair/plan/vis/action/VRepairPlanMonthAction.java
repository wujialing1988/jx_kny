package com.yunda.sb.repair.plan.vis.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.repair.plan.vis.entity.VRepairPlanMonth;
import com.yunda.sb.repair.plan.vis.manager.VRepairPlanMonthManager;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: VRepairPlanMonth控制器，设备检修月计划的VIS实现
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@SuppressWarnings(value = "serial")
public class VRepairPlanMonthAction extends JXBaseAction<VRepairPlanMonth, VRepairPlanMonth, VRepairPlanMonthManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * <li>说明：分页查询
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void queryPageList() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			VRepairPlanMonth entity = JSONUtil.read(entityJson, VRepairPlanMonth.class);
			SearchEntity<VRepairPlanMonth> searchEntity = new SearchEntity<VRepairPlanMonth>(entity, getStart(), getLimit(), getOrders());
			// 查询条件 - 计划状态，多个状态已逗号进行分隔，如：-1,0,1
			String planStatus = req.getParameter("planStatus");
			// 分页查询
			map = this.manager.queryPageList(searchEntity, planStatus).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
