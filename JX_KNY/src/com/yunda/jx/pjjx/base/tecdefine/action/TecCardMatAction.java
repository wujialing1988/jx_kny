package com.yunda.jx.pjjx.base.tecdefine.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.base.tecdefine.entity.TecCardMat;
import com.yunda.jx.pjjx.base.tecdefine.manager.TecCardMatManager;
/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明： TecCardMat控制器, 配件检修所需物料
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-22 上午11:19:42
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class TecCardMatAction extends JXBaseAction<TecCardMat, TecCardMat, TecCardMatManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：保存 配件检修所需物料
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception
	 */
	public void saveTecCardMats() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			TecCardMat[] list = (TecCardMat[])JSONUtil.read(getRequest(), TecCardMat[].class);
			String[] errMsg = this.manager.saveTecCardMats(list);
			if (errMsg == null || errMsg.length < 1) {
				map.put(Constants.SUCCESS, true);
			}else{
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：分页查询，联合查询物料信息表，用以查询物料的最新单价
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-11
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void queryPageList() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String entityJson = StringUtil.nvl(getRequest().getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT);
			entity = JSONUtil.read(entityJson, TecCardMat.class); 
			SearchEntity<TecCardMat> searchEntity = new SearchEntity<TecCardMat>(entity, getStart(), getLimit(), getOrders());
			Page<TecCardMat> page = this.manager.queryPageList(searchEntity);
			map = page.extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
		
	}
	
}