package com.yunda.sb.repair.plan.action;

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
import com.yunda.sb.repair.plan.entity.RepairPlanYear;
import com.yunda.sb.repair.plan.entity.RepairPlanYearBean;
import com.yunda.sb.repair.plan.manager.RepairPlanYearManager;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairPlanYear控制器，数据表：SBJX_REPAIR_PLAN_YEAR
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
public class RepairPlanYearAction extends JXBaseAction<RepairPlanYear, RepairPlanYear, RepairPlanYearManager> {
	
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
			RepairPlanYearBean entity = JSONUtil.read(entityJson, RepairPlanYearBean.class);
			SearchEntity<RepairPlanYearBean> searchEntity = new SearchEntity<RepairPlanYearBean>(entity, getStart(), getLimit(), getOrders());
			// 分页查询
			map = this.manager.queryPageList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger  , map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}
	
	/**
	 * <li>说明：保存设备检修年计划
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void save() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String planYear = req.getParameter("planYear");
			String[] equipmentIds = JSONUtil.read(req.getParameter("equipmentIds"), String[].class);
			this.manager.save(Integer.parseInt(planYear), equipmentIds);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
