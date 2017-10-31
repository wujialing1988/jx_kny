package com.yunda.jx.pjjx.base.wpdefine.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.order.AbstractOrderAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPNode;
import com.yunda.jx.pjjx.base.wpdefine.manager.WPNodeManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WPNode控制器, 作业节点
 * <li>创建人：何涛
 * <li>创建日期：2014-11-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WPNodeAction extends AbstractOrderAction<WPNode, WPNode, WPNodeManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：作业节点树
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
    public void tree() throws Exception{
		List<HashMap<String, Object>> children = null ;
	    try {
			String parentIDX = getRequest().getParameter("parentIDX");  	// 上级作业节点主键
			String wPIDX = getRequest().getParameter("wPIDX");  			// 作业流程主键
		    children = manager.tree(parentIDX, wPIDX);
		} catch (RuntimeException e) {
			ExceptionUtil.process(e, logger);
		} finally{
			JSONUtil.write(getResponse(), children);
		}
    }
	
	/**
	 * <li>说明：分页查询，关联查询节点前置节点
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-25
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void findList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
			entity = (WPNode)JSONUtil.read(searchJson, entitySearch.getClass());
			SearchEntity<WPNode> searchEntity = new SearchEntity<WPNode>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findList(searchEntity).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
}