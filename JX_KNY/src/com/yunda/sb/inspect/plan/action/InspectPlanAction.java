package com.yunda.sb.inspect.plan.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.util.Fastjson;
import com.yunda.sb.inspect.plan.entity.InspectPlan;
import com.yunda.sb.inspect.plan.manager.InspectPlanManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectPlanAction控制器，数据表：E_INSPECT_PLAN
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
public class InspectPlanAction extends JXBaseAction<InspectPlan, InspectPlan, InspectPlanManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());

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
			// 巡检计划类型数组 [周检, 半月检, 月检, 季检]
			String periodTypes = req.getParameter("periodTypes");
			// 查询条件
			String entityJson = req.getParameter(Constants.ENTITY_JSON);
			tree = this.manager.tree(JSONUtil.read(periodTypes, String[].class), JSONUtil.read(entityJson, InspectPlan.class));
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(resp, tree);
		}
	}

	/**
	 * <li>说明： 手动启动设备巡检计划，用于临时巡检需求
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void startUp() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			InspectPlan plan = Fastjson.toObject(req, InspectPlan.class);
			this.manager.startUp(plan.getRouteIdx(), plan.getPlanStartDate(), plan.getPlanEndDate(), plan.getRouteName());
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 分页查询，查询巡检人或者委托巡检人为当前系统操作人的巡检计划 - pda
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void queryPageList() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			// 是否只查询属于自己的巡检任务
			String onlyMe = StringUtil.nvl(req.getParameter("onlyMe"), "false");
			InspectPlan entity = JSONUtil.read(entityJson, InspectPlan.class);
			SearchEntity<InspectPlan> searchEntity = new SearchEntity<InspectPlan>(entity, getStart(), getLimit(), getOrders());
			// 分页查询
			map = this.manager.queryPageList(searchEntity, Boolean.parseBoolean(onlyMe)).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 分页查询，查询需要使用人确认的巡检计划 - pda
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void queryPageList2User() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			InspectPlan entity = JSONUtil.read(entityJson, InspectPlan.class);
			SearchEntity<InspectPlan> searchEntity = new SearchEntity<InspectPlan>(entity, getStart(), getLimit(), getOrders());
			if (null == searchEntity.getOrders() || 0 >= searchEntity.getOrders().length) {
				searchEntity.setOrders(new Order[] { Order.asc("realStartDate") });
			}
			// 分页查询
			map = this.manager.queryPageList2User(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
