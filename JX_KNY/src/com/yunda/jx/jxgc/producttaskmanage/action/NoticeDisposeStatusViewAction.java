package com.yunda.jx.jxgc.producttaskmanage.action; 


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.NoticeDisposeStatusView;
import com.yunda.jx.jxgc.producttaskmanage.manager.NoticeDisposeStatusViewManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: NoticeDisposeStatusView 控制器    提票单处理情况
 * <li>创建人：程梅
 * <li>创建日期：2013-7-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@SuppressWarnings(value="serial")
public class NoticeDisposeStatusViewAction extends JXBaseAction<NoticeDisposeStatusView, NoticeDisposeStatusView, NoticeDisposeStatusViewManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：重写PageQuery方法
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-12
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容： SM20141107014在“修程机车/临碎修作业处理情况查询”中，提票单默认按故障描述进行排序
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void pageQuery() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			List<Order> orderList = getOrderList();
			
			if (getOrderList() == null || getOrderList().size() < 1) {
				orderList = new ArrayList<Order>();
				orderList.add(Order.asc("faultDesc"));
			}
			QueryCriteria<NoticeDisposeStatusView> query = new QueryCriteria<NoticeDisposeStatusView>(getQueryClass(),getWhereList(), orderList, getStart(), getLimit());
			map = this.manager.findPageList(query).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}	
	
}