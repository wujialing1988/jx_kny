package com.yunda.zb.zbfw.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.order.AbstractOrderAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.zb.zbfw.entity.ZbFwWi;
import com.yunda.zb.zbfw.manager.ZbFwWiManager;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbFwWi控制器, 整备作业项目
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class ZbFwWiAction extends AbstractOrderAction<ZbFwWi, ZbFwWi, ZbFwWiManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：：获得整备范围作业项信息
	 * <li>创建人：王利成
	 * <li>创建日期：2015-1-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void getZbFwWiList() throws Exception{
	       Map<String,Object> map = new HashMap<String,Object>();
			try {
				HttpServletRequest req = getRequest();
				String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), "{}");
				ZbFwWi entity = (ZbFwWi)JSONUtil.read(searchJson, entitySearch.getClass());
				SearchEntity<ZbFwWi> searchEntity = new SearchEntity<ZbFwWi>(entity, getStart(), getLimit(), getOrders());
				map = this.manager.getZbFwWiList(searchEntity).extjsStore();
			} catch (Exception e) {
				ExceptionUtil.process(e, logger, map);
			} finally {
				JSONUtil.write(this.getResponse(), map);
			}	         
		}
	
}