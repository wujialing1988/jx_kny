package com.yunda.sb.pointcheck.action;

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
import com.yunda.sb.pointcheck.entity.EquipmentShutdown;
import com.yunda.sb.pointcheck.entity.EquipmentShutdownBean;
import com.yunda.sb.pointcheck.manager.EquipmentShutdownManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：EquipmentShutdownAction，数据表：SBJX_EQUIPMENT_SHUTDOWN
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class EquipmentShutdownAction extends JXBaseAction<EquipmentShutdown, EquipmentShutdown, EquipmentShutdownManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * <li>说明：重写保存或更新方法，修改使用JSONUtil解析entityJson对象，Fastjson.toObject方法对不规则日期解析存在问题
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdate() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			EquipmentShutdown t = (EquipmentShutdown) JSONUtil.read(req, entityClass);
			String[] errMsg = this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t);
				//返回记录保存成功的实体对象
				map.put(Constants.ENTITY, t);
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：重写保存或更新方法，用于PDA端设备停机登记保存
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void saveOrUpdate2PDA() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			EquipmentShutdown t = JSONUtil.read(req.getParameter("entityJson"), EquipmentShutdown.class);
			String[] errMsg = this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t);
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：更新设备停机记录的开始、结束时间
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
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
			EquipmentShutdown[] entityList = JSONUtil.read(req, EquipmentShutdown[].class);
			this.manager.update(entityList);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：根据idx主键获取设备停机记录实体对象
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void getModelByIdx() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String idx = req.getParameter("idx");
			EquipmentShutdown entity = this.manager.getModelById(idx);
			map.put(Constants.ENTITY, entity);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：分页查询，统计设备停机总停时
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException
	 */
	public void queryPageStatistics() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			EquipmentShutdownBean entity = JSONUtil.read(entityJson, EquipmentShutdownBean.class);
			SearchEntity<EquipmentShutdownBean> searchEntity = new SearchEntity<EquipmentShutdownBean>(entity, getStart(), getLimit(), getOrders());
			// 分页查询
			map = this.manager.queryPageStatistics(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：更新设备类别信息
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void updatEquipmentClass() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse resp = getResponse();
		try {
			this.manager.updatEquipmentClass();
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
