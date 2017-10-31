package com.yunda.jx.pjjx.base.tecdefine.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.order.AbstractOrderAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.tecdefine.entity.TecCard;
import com.yunda.jx.pjjx.base.tecdefine.manager.TecCardManager;
/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: Tec控制器, 配件检修工艺卡卡
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-22 上午11:21:46
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class TecCardAction extends AbstractOrderAction<TecCard, TecCard, TecCardManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：查询作业流程节点所用工艺卡
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
     * @throws IOException 
     * @throws JsonMappingException 
	 */
	@SuppressWarnings("unchecked")
	public void findPageListForWPNode() throws JsonMappingException, IOException {
		HttpServletRequest req = getRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询实体
			String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
			// 作业流程主键
			String wPIDX = req.getParameter("wPIDX");
			// 作业流程节点主键
			String wPNodeIDX = req.getParameter("wPNodeIDX");
			entity = (TecCard)JSONUtil.read(searchJson, entity.getClass());
			SearchEntity<TecCard> searchEntity = new SearchEntity<TecCard>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageListForWPNode(searchEntity, wPNodeIDX, wPIDX).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：查询作业流程节点所用工艺卡 - 候选工艺卡
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	@SuppressWarnings("unchecked")
	public void findPageListForWPNodeSelect() throws JsonMappingException, IOException {
		HttpServletRequest req = getRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询实体
			String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
			// 作业流程主键
			String wPIDX = req.getParameter("wPIDX");
			// 作业流程节点主键
			String wPNodeIDX = req.getParameter("wPNodeIDX");
			entity = (TecCard)JSONUtil.read(searchJson, entity.getClass());
			SearchEntity<TecCard> searchEntity = new SearchEntity<TecCard>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageListForWPNodeSelect(searchEntity, wPNodeIDX, wPIDX).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
	
}