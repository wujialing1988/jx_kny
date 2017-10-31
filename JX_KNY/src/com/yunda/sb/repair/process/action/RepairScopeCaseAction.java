package com.yunda.sb.repair.process.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.repair.process.entity.RepairScopeCase;
import com.yunda.sb.repair.process.manager.RepairScopeCaseManager;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairScopeCase控制器，数据表：E_REPAIR_SCOPE_CASE
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
public class RepairScopeCaseAction extends JXBaseAction<RepairScopeCase, RepairScopeCase, RepairScopeCaseManager> {

	/** 日志工具 */
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * <li>说明：分页查询-作业人员待处理的设备检修范围活
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void queryPageList() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter("entityJson"), "{}");
			RepairScopeCase entity = JSONUtil.read(entityJson, RepairScopeCase.class);
			// 查询指定班组的检修任务单
			String orgId = req.getParameter("orgId");
			SearchEntity<RepairScopeCase> searchEntity = new SearchEntity<RepairScopeCase>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.queryPageList(searchEntity, orgId).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：获取检修作业处理情况饼图数据源
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月5日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void queryChartData() throws IOException {
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		//  检修任务单主键
		String taskListIdx = req.getParameter("taskListIdx");
		// 获取巡检记录处理情况饼图数据源，获取指定巡检设备下已巡检、未巡检记录数
		List<Map<String, Object>> result = this.manager.queryChartData(taskListIdx);
		// 返回结果集
		JSONUtil.write(resp, result);
	}

}
