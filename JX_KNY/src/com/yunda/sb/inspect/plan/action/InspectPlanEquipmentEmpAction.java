package com.yunda.sb.inspect.plan.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.sb.inspect.plan.entity.InspectPlanEquipmentEmp;
import com.yunda.sb.inspect.plan.manager.InspectPlanEquipmentEmpManager;
import com.yunda.sb.inspect.plan.manager.InspectPlanEquipmentEmpManager.EntrustInspectEmp;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectPlanEquipmentEmp控制器，数据表：E_INSPECT_PLAN_EQUIPMENT_EMP
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
public class InspectPlanEquipmentEmpAction extends JXBaseAction<InspectPlanEquipmentEmp, InspectPlanEquipmentEmp, InspectPlanEquipmentEmpManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * <li>说明： 保存委托设备巡检人员
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void saveEntrustInspectEmp() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String[] ids = JSONUtil.read(req.getParameter("ids"), String[].class);
			String entityJson = req.getParameter(Constants.ENTITY_JSON);
			EntrustInspectEmp entrustInspectEmp = JSONUtil.read(entityJson, InspectPlanEquipmentEmpManager.EntrustInspectEmp.class);
			this.manager.saveEntrustInspectEmp(ids, entrustInspectEmp);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 巡检人员提交设备巡检结果
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void updateFinish() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String entityJson = req.getParameter("entityJson");
			// 巡检记录处理
			this.manager.updateFinish(JSONUtil.read(entityJson, InspectPlanEquipmentEmp.class));
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明： 根据“巡检设备idx主键”获取巡检设备人员集合
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-9
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 *@throws IOException
	 */
	public void getModelsByPlanEquipmentIdx() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String planEquipmentIdx = req.getParameter("planEquipmentIdx");
			// 根据“巡检设备idx主键”获取巡检设备人员集合
			List<InspectPlanEquipmentEmp> list = this.manager.getModelsByPlanEquipmentIdx(planEquipmentIdx);
			map.put("list", list);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
