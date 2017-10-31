package com.yunda.sb.pointcheck.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.yunda.sb.pointcheck.entity.CalendarEventBean;
import com.yunda.sb.pointcheck.entity.PointCheck;
import com.yunda.sb.pointcheck.entity.PointCheckBean;
import com.yunda.sb.pointcheck.entity.PointCheckOmitBean;
import com.yunda.sb.pointcheck.entity.PointCheckStatistic;
import com.yunda.sb.pointcheck.manager.PointCheckManager;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: PointCheck控制器
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月15日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@SuppressWarnings("serial")
public class PointCheckAction extends JXBaseAction<PointCheck, PointCheck, PointCheckManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * <li>说明：启动设备
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 */
	public void startUpEquipment() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String idx = req.getParameter("idx");
			this.manager.startUpEquipment(idx);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：停止（暂停）设备
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 */
	public void endUpEquipment() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String idx = req.getParameter("idx");
			PointCheck entity = this.manager.endUpEquipment(idx);
			map.put(Constants.ENTITY, entity);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：根据设备编码查询设备信息、点检内容，用于PDA端显示
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 */
	public void startUp() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 设备编码
			String equipmentCode = req.getParameter("equipmentCode");
			PointCheck entity = this.manager.startUp(equipmentCode);
			map.put(Constants.ENTITY, entity);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：提交设备点检单
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void updateFinish() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String entityJson = req.getParameter("entityJson");
			PointCheck entity = JSONUtil.read(entityJson, PointCheck.class);
			this.manager.updateFinish(entity);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：设备点检统计分页查询，按月统计每台设备点检发生次数
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void queryPage2Statistic() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			PointCheckStatistic entity = JSONUtil.read(entityJson, PointCheckStatistic.class);
			SearchEntity<PointCheckStatistic> searchEntity = new SearchEntity<PointCheckStatistic>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.queryPage2Statistic(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：设备点检任务单分页查询分页查询
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
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
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			PointCheckBean entity = JSONUtil.read(entityJson, PointCheckBean.class);
			SearchEntity<PointCheckBean> searchEntity = new SearchEntity<PointCheckBean>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.queryPageList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：一键处理设备点检任务单
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void updateFinishedByOneKey() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String idx = req.getParameter("idx");
			String runningTime = req.getParameter("runningTime");
			this.manager.updateFinishedByOneKey(idx, StringUtil.isNullOrBlank(runningTime) ? null : Float.valueOf(runningTime));
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：分页查询，统计设备在当月内漏检次数
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void queryOmitCount() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse resp = getResponse();
		try {
			SearchEntity<PointCheckOmitBean> searchEntity = new SearchEntity<PointCheckOmitBean>(null, getStart(), getLimit(), getOrders());
			// 分页查询
			map = this.manager.queryOmitCount(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：设备点检统计，以日历方式显示点检情况
	 * <li>创建人：黄杨
	 * <li>创建日期：2017-05-15
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws IOException 
	 */
	public void queryToStatistic() throws IOException {
		List<CalendarEventBean> list = null;
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String equipmentCode = req.getParameter("equipmentCode");
			String queryDate = req.getParameter("queryDate");
			list = manager.queryToStatistic(equipmentCode, queryDate);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, new HashMap<String, Object>());
		} finally {
			JSONUtil.write(resp, list);
		}
	}
}
