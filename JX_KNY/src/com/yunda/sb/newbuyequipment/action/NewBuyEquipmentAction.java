package com.yunda.sb.newbuyequipment.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.newbuyequipment.entity.NewBuyEquipment;
import com.yunda.sb.newbuyequipment.entity.NewBuyEquipmentBean;
import com.yunda.sb.newbuyequipment.manager.NewBuyEquipmentManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：新购设备控制器
 * <li>创建人：黄杨
 * <li>创建日期：2017-4-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class NewBuyEquipmentAction extends JXBaseAction<NewBuyEquipment, NewBuyEquipment, NewBuyEquipmentManager> {

	/** 日志工具 */
	Logger logger = Logger.getLogger(getClass());

	/**
	 * <li>说明：新购设备联合分页查询
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void queryPageList() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpServletRequest req = getRequest();
			String entityJson = StringUtil.nvl(req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON);
			NewBuyEquipmentBean entity = JSONUtil.read(entityJson, NewBuyEquipmentBean.class);
			SearchEntity<NewBuyEquipmentBean> searchEntity = new SearchEntity<NewBuyEquipmentBean>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.queryPageList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}

	/**
	 * <li>说明：保存新购设备
	 * <li>创建人：黄杨
	 * <li>创建日期：2017年5月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void save() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			HttpServletRequest req = getRequest();
			NewBuyEquipmentBean t = JSONUtil.read(req, NewBuyEquipmentBean.class);
			NewBuyEquipment entity = this.manager.save(t);
			// 返回记录保存成功的实体对象
			map.put(Constants.ENTITY, entity);
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
}
