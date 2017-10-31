package com.yunda.sb.equipmentprimaryinfo.action;

import java.io.IOException;
import java.util.HashMap;
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
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentUnionRFID;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentUnionRFIDBean;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentUnionRFIDManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：EquipmentUnionRFID控制器，数据表：设备RFID关联
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class EquipmentUnionRFIDAction extends JXBaseAction<EquipmentUnionRFID, EquipmentUnionRFID, EquipmentUnionRFIDManager> {

	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * <li>说明：分页查询，查询进行RFID绑定的设备列表
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月28日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param req http请求对象
	 * @param resp http响应对象
	 * @throws IOException
	 */
	public void queryPageList() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 查询条件封装实体
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			// 是否只显示已登记设备，true：只查询已登记设备，false：查询所有固资设备
			String isRegistered = StringUtil.nvl(req.getParameter("isRegistered"), "false");

			EquipmentUnionRFIDBean entity = JSONUtil.read(entityJson, EquipmentUnionRFIDBean.class);
			SearchEntity<EquipmentUnionRFIDBean> searchEntity = new SearchEntity<EquipmentUnionRFIDBean>(entity, getStart(), getLimit(), getOrders());
			// 默认按设备编码升序排列
			if (null == searchEntity.getOrders() || 0 >= searchEntity.getOrders().length) {
				searchEntity.setOrders(new Order[] { Order.asc("equipmentCode") });
			}
			// 分页查询
			map = this.manager.queryPageList(searchEntity, Boolean.parseBoolean(isRegistered)).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：验证扫描到的RFID识别码是否是一个设备编码，换句话说：该RFID是否已经和设备进行了绑定
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月28日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param req http请求对象
	 * @param resp http响应对象
	 * @throws IOException
	 */
	public void checkIsBind() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			// 设备编码
			String equipmentCode = req.getParameter("equipmentCode");
			EquipmentPrimaryInfo entity = this.manager.checkIsBind(equipmentCode);
			map.put(Constants.SUCCESS, true);
			map.put(Constants.ENTITY, entity);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

	/**
	 * <li>说明：保存设备RFID保存
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月28日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param req http请求对象
	 * @param resp http响应对象
	 * @throws IOException
	 */
	public void save() throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		HttpServletRequest req = getRequest();
		HttpServletResponse resp = getResponse();
		try {
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			EquipmentUnionRFID entity = JSONUtil.read(entityJson, EquipmentUnionRFID.class);
			String[] errMsg = this.manager.validateUpdate(entity);
			if (null != errMsg) {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			} else {
				this.manager.saveOrUpdate(entity);
				map.put(Constants.SUCCESS, true);
				map.put(Constants.ENTITY, entity);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(resp, map);
		}
	}

}
