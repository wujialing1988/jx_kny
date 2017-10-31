package com.yunda.sb.repair.plan.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.repair.plan.entity.RepairPlanMonth;
import com.yunda.sb.repair.plan.entity.RepairPlanMonthBean;
import com.yunda.sb.repair.plan.manager.RepairPlanMonthManager;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairPlanMonth控制器，数据表：SBJX_REPAIR_PLAN_MONTH
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
public class RepairPlanMonthAction extends JXBaseAction<RepairPlanMonth, RepairPlanMonth, RepairPlanMonthManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * <li>说明：分页查询
	 * <li>创建人：黄杨
	 * <li>创建日期：2016年7月23日
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
			RepairPlanMonthBean entity = JSONUtil.read(entityJson, RepairPlanMonthBean.class);
			SearchEntity<RepairPlanMonthBean> searchEntity = new SearchEntity<RepairPlanMonthBean>(entity, getStart(), getLimit(), getOrders());
			// 查询条件 - 计划状态，多个状态已逗号进行分隔，如：-1,0,1
			String planStatus = req.getParameter("planStatus");
			// 分页查询
			map = this.manager.queryPageList(searchEntity, planStatus).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			ObjectMapper mapper = new ObjectMapper();
			mapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
			mapper.writeValue(resp.getOutputStream(), map);
		}
	}

	/**
	 * <li>说明：生成全年计划
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void insertWholeMonthPlan() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 计划年度
			String planYear = req.getParameter("planYear");
			this.manager.insertWholeMonthPlan(Integer.parseInt(planYear));
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：生成下月计划
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void insertNextMonthPlan() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse resp = getResponse();
		try {
			this.manager.insertNextMonthPlan();
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：生成月计划
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void insertMonthPlan() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String month = req.getParameter("month");
			String year = req.getParameter("year");
			this.manager.insertMonthPlan(Integer.parseInt(month), Integer.parseInt(year));
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：更新设备检修月计划的开工、完工时间
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void update() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			RepairPlanMonth[] entityList = JSONUtil.read(req, RepairPlanMonth[].class);
			this.manager.update(entityList);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：根据设备的检修历史记录，设置默认的设备检修班组，但对于批量下发的月计划，此默认值可能是不确定的
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 */
	public void defaultRepairRaskListTeam() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String[] ids = JSONUtil.read(req, String[].class);
			this.manager.defaultRepairRaskListTeam(ids, map);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：撤销设备检修月计划下发
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 */
	public void updateForRescind() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String[] ids = JSONUtil.read(req, String[].class);
			this.manager.updateForRescind(ids);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：一键删除，用于系统调试
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void deleteByOneKey() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse resp = getResponse();
		try {
			this.manager.deleteByOneKey();
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
