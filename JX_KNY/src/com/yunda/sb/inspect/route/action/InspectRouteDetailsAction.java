package com.yunda.sb.inspect.route.action;

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
import com.yunda.jx.util.Fastjson;
import com.yunda.sb.inspect.route.entity.InspectRouteDetails;
import com.yunda.sb.inspect.route.entity.InspectRouteDetailsBean;
import com.yunda.sb.inspect.route.manager.InspectRouteDetailsManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectRouteDetails控制器，数据表：E_INSPECT_ROUTE_DETAILS
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
public class InspectRouteDetailsAction extends JXBaseAction<InspectRouteDetails, InspectRouteDetails, InspectRouteDetailsManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * <li>说明： 设备巡检明细的联合分页查询
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
			String entityJson = StringUtil.nvl(req.getParameter("entityJson"), "{}");
			InspectRouteDetailsBean entity = Fastjson.toObject(entityJson, InspectRouteDetailsBean.class);
			SearchEntity<InspectRouteDetailsBean> searchEntity = new SearchEntity<InspectRouteDetailsBean>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.queryPageList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 保存巡检线路明细，添加已选择的设备到巡检计划中
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void save() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String routeIdx = req.getParameter("routeIdx");
			String[] equipmentIds = JSONUtil.read(req.getParameter("equipmentIds"), String[].class);
			this.manager.save(routeIdx, equipmentIds);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 保存设备巡检人员
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void saveInspectEmp() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String[] ids = JSONUtil.read(req.getParameter("ids"), String[].class);
			String entityJson = req.getParameter(Constants.ENTITY_JSON);
			InspectRouteDetails entity = JSONUtil.read(entityJson, InspectRouteDetails.class);
			this.manager.saveInspectEmp(ids, entity);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
