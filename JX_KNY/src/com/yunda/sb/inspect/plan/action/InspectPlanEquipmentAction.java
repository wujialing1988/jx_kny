package com.yunda.sb.inspect.plan.action;

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
import com.yunda.jx.util.Fastjson;
import com.yunda.sb.inspect.plan.entity.InspectPlanEquipment;
import com.yunda.sb.inspect.plan.entity.InspectPlanEquipmentBean3;
import com.yunda.sb.inspect.plan.manager.InspectPlanEquipmentManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectPlanEquipment控制器，数据表：E_INSPECT_PLAN_EQUIPMENT
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
public class InspectPlanEquipmentAction extends JXBaseAction<InspectPlanEquipment, InspectPlanEquipment, InspectPlanEquipmentManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * <li>说明： 巡检设备联合分页查询
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
			InspectPlanEquipment entity = Fastjson.toObject(entityJson, InspectPlanEquipment.class);
			SearchEntity<InspectPlanEquipment> searchEntity = new SearchEntity<InspectPlanEquipment>(entity, getStart(), getLimit(), getOrders());
			// 分页查询
			map = this.manager.queryPageList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 巡检设备联合分页查询，用于pda任务处理
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void queryPageList2() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			// 是否只查询属于自己的巡检任务
			String onlyMe = StringUtil.nvl(req.getParameter("onlyMe"), "false");
			InspectPlanEquipment entity = Fastjson.toObject(entityJson, InspectPlanEquipment.class);
			SearchEntity<InspectPlanEquipment> searchEntity = new SearchEntity<InspectPlanEquipment>(entity, getStart(), getLimit(), getOrders());
			// 分页查询
			map = this.manager.queryPageList2(searchEntity, Boolean.parseBoolean(onlyMe)).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 设备巡检日志联合分页查询
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void queryPageList3() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			InspectPlanEquipmentBean3 entity = Fastjson.toObject(entityJson, InspectPlanEquipmentBean3.class);
			SearchEntity<InspectPlanEquipmentBean3> searchEntity = new SearchEntity<InspectPlanEquipmentBean3>(entity, getStart(), getLimit(), getOrders());
			if (null == searchEntity.getOrders() || 0 >= searchEntity.getOrders().length) {
				Order[] orders = new Order[] { Order.desc("planStartDate"), Order.asc("routeName") };
				searchEntity.setOrders(orders);
			}
			// 分页查询
			map = this.manager.queryPageList3(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 分页查询，查询需要使用人确认的巡检设备 - pda
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
			InspectPlanEquipment entity = Fastjson.toObject(entityJson, InspectPlanEquipment.class);
			SearchEntity<InspectPlanEquipment> searchEntity = new SearchEntity<InspectPlanEquipment>(entity, getStart(), getLimit(), getOrders());
			// 分页查询
			map = this.manager.queryPageList2User(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 设备使用人确认巡检设备处理结果
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws JsonMappingException
	 *@throws IOException
	 */
	public void confirm() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String ids = req.getParameter("ids");
			// 设备使用人确认巡检设备处理结果
			this.manager.confirm(JSONUtil.read(ids, String[].class));
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 保存巡检设备
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
			String planIdx = req.getParameter("planIdx");
			String[] equipmentIds = JSONUtil.read(req.getParameter("equipmentIds"), String[].class);
			this.manager.save(planIdx, equipmentIds);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 一键处理巡检设备，适用于web端处理巡检结果
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void updateFinishedByOneKey() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 巡检设备idx主键
			String idx = req.getParameter("idx");
			// 巡检记录的检查结果（合格，不合格） ，如果为null，则默认为：合格
			String checkResult = req.getParameter("checkResult");
			this.manager.updateFinishedByOneKey(idx, checkResult);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 批量处理巡检设备，适用于web端处理巡检结果
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void updateFinished() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 巡检设备idx主键数组
			String ids = StringUtil.nvl(req.getParameter("ids"), "[]");
			// 巡检记录的检查结果（合格，不合格） ，如果为null，则默认为：合格
			String checkResult = req.getParameter("checkResult");
			this.manager.updateFinished(JSONUtil.read(ids, String[].class), checkResult);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 开工
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws JsonMappingException
	 *@throws IOException
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

}
