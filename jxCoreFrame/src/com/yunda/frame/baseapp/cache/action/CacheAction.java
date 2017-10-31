package com.yunda.frame.baseapp.cache.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.baseapp.cache.entity.CacheInfo;
import com.yunda.frame.baseapp.cache.manager.CacheManager;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: hibernate缓存管理 接收请求控制器类
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-11-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class CacheAction extends JXBaseAction<Object, Object, CacheManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * 
	 * <li>说明：获取系统中CacheInfo.json中使用hibernate二级缓存和查询缓存的数据库表（实体类）等描述信息
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 * @throws Exception
	 */
	public void getCacheInfo() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			List<CacheInfo> list = manager.getCacheInfo();
			map = Page.extjsStore(list);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			map.put("id", "tableName");
			JSONUtil.write(this.getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：接收请求，清空所有查询缓存, 并根据全路径类名称（className指定的实体对象的hibernate二级缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 * @throws Exception
	 */
	public void evictEntityAndQueries() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String tableName = getRequest().getParameter("tableName");
			manager.evictEntityAndQueries(tableName);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}			
	}
	/**
	 * <li>说明：接收请求，清空所有注册的实体对象的hibernate二级缓存和查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return void
	 * @throws Exception
	 */
	public void evictAll() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			manager.evictAll();
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}				
	}
	
}