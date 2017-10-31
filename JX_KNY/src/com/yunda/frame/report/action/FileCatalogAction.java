package com.yunda.frame.report.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.report.entity.FileCatalog;
import com.yunda.frame.report.manager.FileCatalogManager;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：FileCatalog控制器, 报表模板管理类型标识目录
 * <li>创建人：何涛
 * <li>创建日期：2015-01-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class FileCatalogAction extends JXBaseAction<FileCatalog, FileCatalog, FileCatalogManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：报表模板管理类型标识目录树
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void tree() throws JsonMappingException, IOException {
		List<HashMap> children = null;
		try {
			String parentIdx = this.getParameter("parentIdx");
			children = this.manager.tree(parentIdx, false);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(getResponse(), children);
		}
		
	}
	
	/**
	 * <li>说明：接受逻辑删除记录请求，向客户端返回操作结果（JSON格式）
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-27
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param 
	 * @throws Exception
	 */	
	@SuppressWarnings("all")
	public void logicDelete() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String[] errMsg = this.manager.validateDelete(ids);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.logicDelete(ids);
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}			
		} catch (Exception e) {
			map.put(Constants.SUCCESS, false);
			map.put(Constants.ERRMSG, e.getMessage());
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
    
	/**
	 * <li>说明：初始化目录
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-27
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param 
	 * @throws Exception
	 */	
	@SuppressWarnings("all")
	public void initialize() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			this.manager.initialize();
			map.put(Constants.SUCCESS, true);
		} catch (Exception e) {
			map.put(Constants.SUCCESS, false);
			map.put(Constants.ERRMSG, e.getMessage());
			ExceptionUtil.process(e, logger);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
}