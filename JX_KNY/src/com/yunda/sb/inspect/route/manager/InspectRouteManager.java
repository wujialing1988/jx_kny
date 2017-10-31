package com.yunda.sb.inspect.route.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.sb.inspect.route.entity.InspectRoute;
import com.yunda.sb.inspect.route.entity.InspectRouteDetails;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectlRoute业务类，数据表：E_INSPECTL_ROUTE
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Service(value = "inspectRouteManager")
public class InspectRouteManager extends JXBaseManager<InspectRoute, InspectRoute> {

	/** InspectRouteDetails业务类，数据表：E_INSPECT_ROUTE_DETAILS */
	@Resource
	InspectRouteDetailsManager inspectRouteDetailsManager;

	/**
	 * <li>说明：数据唯一性验证
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月13日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param t 要存储的巡检线路实体对象
	 * @return 验证信息
	 */
	@Override
	public String[] validateUpdate(InspectRoute t) {
		String hql = "From InspectRoute Where recordStatus = 0 And routeName = ? And periodType = ?";
		InspectRoute entity = (InspectRoute) this.daoUtils.findSingle(hql, new Object[] { t.getRouteName(), t.getPeriodType() });
		if (null != entity && !entity.getIdx().equals(t.getIdx())) {
			return new String[] { "已存在巡检线路名称为“" + t.getRouteName() + "”的记录，请重新添加！" };
		}
		return null;
	}

	/**
	 * <li>说明：查询设备巡检线路树
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月13日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param expirible 是否过期，true：表示查询结果包含已过期的记录；false：表示查询结果不包含已过期的记录
	 * @param states 巡检线路状态，多个状态以英文状态下的逗号进行分隔，例如："0,1"
	 * @return 巡检线路树结构实体集合
	 */
	public List<Map<String, Object>> tree(String states, boolean expirible) {
		StringBuilder sb = new StringBuilder("From InspectRoute Where recordStatus = 0 Order By updateTime Desc");
		@SuppressWarnings("unchecked")
		List<InspectRoute> list = this.daoUtils.find(sb.toString());
		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>(list.size());
		Map<String, Object> node = null;
		for (InspectRoute route : list) {
			node = new HashMap<String, Object>(5);
			node.put("id", route.getIdx());
			node.put("text", route.toString());
			node.put("leaf", true);
			node.put("idx", route.getIdx());
			node.put("routeName", route.getRouteName());
			node.put("partrolWorker", route.getPartrolWorker());
			node.put("partrolWorkerId", route.getPartrolWorkerId());
			node.put("periodType", route.getPeriodType());
			node.put("planPublishDate", route.getPlanPublishDate());
			node.put("expiryDate", route.getExpiryDate());
			node.put("state", route.getState());
			nodeList.add(node);
		}
		return nodeList;
	}

	/**
	 * <li>说明：级联删除巡检线路下属的巡检设备
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月17日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param ids 巡检线路idx主键数组
	 */
	@Override
	public void logicDelete(Serializable... ids) throws NoSuchFieldException {
		for (Serializable idx : ids) {
			List<InspectRouteDetails> details = this.inspectRouteDetailsManager.getModelsByRouteIdx((String) idx);
			for (InspectRouteDetails t : details) {
				EntityUtil.setSysinfo(t);
				//设置逻辑删除字段状态为已删除
				EntityUtil.setDeleted(t);
			}
			this.inspectRouteDetailsManager.saveOrUpdate(details);
		}
		super.logicDelete(ids);
	}

	/**
	 * <li>说明：验证该巡检线路是否有效，如果该线路下未维护任何巡检设备，则视为该线路是一条无线的巡检线路
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月22日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param routeIdx 巡检线路idx主键
	 * @return 
	 * <li>true：有效，表现该线路下有巡检设备；
	 * <li>false：无效，表示该线路下没有巡检设备;
	 */
	public boolean isValid(String routeIdx) {
		List<InspectRouteDetails> list = this.inspectRouteDetailsManager.getModelsByRouteIdx(routeIdx);
		if (null == list || list.isEmpty()) {
			return false;
		}
		return true;
	}

}
