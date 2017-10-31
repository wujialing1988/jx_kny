package com.yunda.webservice.device.action; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.webservice.device.entity.CallLog;
import com.yunda.webservice.device.manager.CallLogManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：CallLog控制器, 设备接口-工单维护
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-01-06
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class CallLogAction extends JXBaseAction<CallLog, CallLog, CallLogManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：接受物理删除记录请求，向客户端返回操作结果（JSON格式）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-01-08
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("all")
	public void delete() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String[] errMsg = this.manager.validateDelete(ids);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.deleteByIds(ids);
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("errMsg", errMsg);
			}			
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}	
	
	/**
	 * <li>说明：该私有方法，该方法用于获取HTTP请求中的排序字段信息，返回设置排序规则列表
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-1-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return List<Order> 排序规则列表
	 * @throws 
	 */
	protected List<Order> getOrderList(){
		//根据请求参数设置排序规则
		HttpServletRequest req = getRequest();
		String sort = StringUtil.nvlTrim(req.getParameter("sort"), null);
		String dir = StringUtil.nvlTrim(req.getParameter("dir"), null);
		List<Order> orderList = new ArrayList<Order>();
		if (sort != null && dir != null) {
			if ("ASC".equalsIgnoreCase(dir)) {
				orderList.add(Order.asc(sort));
			} else if("DESC".equalsIgnoreCase(dir)){
				orderList.add(Order.desc(sort));
			}
		}
		if(orderList.size() < 1){
			orderList.add(Order.desc("callTime"));
		}
		return orderList;
	}	
	
}