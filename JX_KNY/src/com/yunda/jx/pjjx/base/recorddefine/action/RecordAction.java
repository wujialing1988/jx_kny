package com.yunda.jx.pjjx.base.recorddefine.action; 

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
import com.yunda.jx.pjjx.base.recorddefine.entity.Record;
import com.yunda.jx.pjjx.base.recorddefine.manager.RecordManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Record控制器, 记录单
 * <li>创建人：何涛
 * <li>创建日期：2014-11-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class RecordAction extends JXBaseAction<Record, Record, RecordManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：查询作业流程所用记录单
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
     * @throws IOException 
     * @throws JsonMappingException 
	 */
	@SuppressWarnings("unchecked")
	public void findPageListForWP() throws JsonMappingException, IOException {
		HttpServletRequest req = getRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询实体
			String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
			// 作业流程主键
			String wPIDX = req.getParameter("wPIDX");
			entity = (Record)JSONUtil.read(searchJson, entity.getClass());
			SearchEntity<Record> searchEntity = new SearchEntity<Record>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageListForWP(searchEntity, wPIDX).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：查询作业流程所用记录单 - 候选记录单
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws IOException 
	 * @throws JsonMappingException
	 */
	@SuppressWarnings("unchecked")
	public void findPageListForWPSelect() throws JsonMappingException, IOException {
		HttpServletRequest req = getRequest();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// 查询实体
			String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
			// 作业流程主键
			String wPIDX = req.getParameter("wPIDX");
			entity = (Record)JSONUtil.read(searchJson, entity.getClass());
			SearchEntity<Record> searchEntity = new SearchEntity<Record>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findPageListForWPSelect(searchEntity, wPIDX).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), map);
		}
	}
	
}