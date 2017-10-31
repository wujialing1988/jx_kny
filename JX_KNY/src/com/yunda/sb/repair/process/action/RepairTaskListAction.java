package com.yunda.sb.repair.process.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.repair.process.entity.RepairTaskList;
import com.yunda.sb.repair.process.entity.RepairTaskListBean;
import com.yunda.sb.repair.process.entity.RepairTaskListTeam;
import com.yunda.sb.repair.process.manager.RepairTaskListManager;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairTaskList控制器，数据表：E_REPAIR_TASK_LIST
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
public class RepairTaskListAction extends JXBaseAction<RepairTaskList, RepairTaskList, RepairTaskListManager> {

	/** 日志工具 */
	Logger logger = Logger.getLogger(this.getClass());

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
			String isHanding = req.getParameter("isHanding");
			RepairTaskListBean entity = JSONUtil.read(entityJson, RepairTaskListBean.class);
			SearchEntity<RepairTaskListBean> searchEntity = new SearchEntity<RepairTaskListBean>(entity, getStart(), getLimit(), getOrders());
			// 分页查询
			map = this.manager.queryPageList(searchEntity, Boolean.parseBoolean(isHanding)).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：下发计划
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void insert4Publish() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String elcTeam = req.getParameter("elcTeam"); // 电气班组
			String macTeam = req.getParameter("macTeam"); // 机械班组
			String[] planMonthIdxs = JSONUtil.read(req.getParameter("planMonthIdxs"), String[].class);
			RepairTaskListTeam team2Elc = StringUtil.isNullOrBlank(elcTeam) ? null : JSONUtil.read(elcTeam, RepairTaskListTeam.class);
			RepairTaskListTeam team2Mac = StringUtil.isNullOrBlank(macTeam) ? null : JSONUtil.read(macTeam, RepairTaskListTeam.class);
			// 是否需要验收，0：无需验收人验收、1：需要验收人验收，默认为需要验收
			String isNeedAcceptance = StringUtil.nvl(req.getParameter("isNeedAcceptance"), "1");
			manager.insert4Publish(planMonthIdxs, team2Elc, team2Mac, "1".equals(isNeedAcceptance));
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}
		JSONUtil.write(resp, map);
	}

	/**
	 * <li>说明：分页查询-作业人员待处理的设备检修任务单
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void taskList() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter("entityJson"), "{}");
			RepairTaskListBean entity = JSONUtil.read(entityJson, RepairTaskListBean.class);
			// 查询指定班组的检修任务单
			String orgId = req.getParameter("orgId");
			SearchEntity<RepairTaskListBean> searchEntity = new SearchEntity<RepairTaskListBean>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.taskList(searchEntity, orgId).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：检修任务单分页查询-工长确认
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void taskListForGzSign() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter("entityJson"), "{}");
			RepairTaskListBean entity = JSONUtil.read(entityJson, RepairTaskListBean.class);
			// 查询指定班组的检修任务单
			String orgId = req.getParameter("orgId");
			SearchEntity<RepairTaskListBean> searchEntity = new SearchEntity<RepairTaskListBean>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.taskListForGzSign(searchEntity, orgId).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：工长确认
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param req http请求对象
	 * @param req http响应对象
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void updateGzSign() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String ids = StringUtil.nvlTrim(req.getParameter("ids"), "[]");
			this.manager.updateGzSign(JSONUtil.read(ids, String[].class));
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}
		JSONUtil.write(resp, map);
	}

	/**
	 * <li>说明：检修任务单分页查询-验收人确认
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void taskListForAcceptor() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter("entityJson"), "{}");
			RepairTaskListBean entity = JSONUtil.read(entityJson, RepairTaskListBean.class);
			// 查询指定班组的检修任务单
			SearchEntity<RepairTaskListBean> searchEntity = new SearchEntity<RepairTaskListBean>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.taskListForAcceptor(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：验收人确认
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	
	public void updateAcceptor() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String ids = StringUtil.nvlTrim(req.getParameter("ids"), "[]");
			String acceptanceReviews = req.getParameter("acceptanceReviews");
			// Modified by hetao on 2017-01-09
			// 因试用单位（安康机务段）提出设备检修验收要区分机械和电气，经沟通，修改方案为：在原设计基础上，让验收处理时可以输入多个验收人员名称
			String acceptorNames = req.getParameter("acceptorNames");
			this.manager.updateAcceptor(JSONUtil.read(ids, String[].class), acceptanceReviews, acceptorNames);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}
		JSONUtil.write(resp, map);
	}

	/**
	 * <li>说明：检修任务单分页查询-使用人确认
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void taskListForUser() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter("entityJson"), "{}");
			RepairTaskListBean entity = JSONUtil.read(entityJson, RepairTaskListBean.class);
			// 查询指定班组的检修任务单
			SearchEntity<RepairTaskListBean> searchEntity = new SearchEntity<RepairTaskListBean>(entity, getStart(), getLimit(), getOrders());
			if (null == searchEntity.getOrders() || 0 >= searchEntity.getOrders().length) {
				searchEntity.setOrders(new Order[] { Order.asc("realBeginTime") });
			}
			map = this.manager.taskListForUser(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：使用人确认
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年7月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void updateUser() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String ids = StringUtil.nvlTrim(req.getParameter("ids"), "[]");
			this.manager.updateUser(JSONUtil.read(ids, String[].class));
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}
		JSONUtil.write(resp, map);
	}

	/**
	 * <li>说明：开工
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void updateRealBeginTime() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String ids = StringUtil.nvlTrim(req.getParameter("ids"), "[]");
			this.manager.updateRealBeginTime(JSONUtil.read(ids, String[].class));
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}
		JSONUtil.write(resp, map);
	}

	/**
	 * <li>说明：完工
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param req http请求对象
	 * @param req http响应对象
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Deprecated
	public void updateRealEndTime() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String ids = StringUtil.nvlTrim(req.getParameter("ids"), "[]");
			this.manager.updateRealEndTime(JSONUtil.read(ids, String[].class));
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		}
		JSONUtil.write(resp, map);
	}

}
