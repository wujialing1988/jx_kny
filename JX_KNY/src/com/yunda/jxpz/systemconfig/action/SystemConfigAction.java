package com.yunda.jxpz.systemconfig.action; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jxpz.systemconfig.entity.SystemConfig;
import com.yunda.jxpz.systemconfig.manager.SystemConfigManager;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: SystemConfig控制类     系统配置项
 * <li>创建人：程梅
 * <li>创建日期：2013-7-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@SuppressWarnings(value="serial")
public class SystemConfigAction extends JXBaseAction<SystemConfig, SystemConfig, SystemConfigManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * 
	 * <li>说明：系统配置树
	 * <li>创建人：程梅
	 * <li>创建日期：2013-7-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void tree(){
		List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
		String parentKey = StringUtil.nvlTrim(getRequest().getParameter("parentKey"), "ROOT_0"); 
		children = this.manager.tree(parentKey);
		try {
			JSONUtil.write(getResponse(), children);
		} catch (JsonMappingException e) {
			ExceptionUtil.process(e, logger);
		} catch (IOException e) {
		    ExceptionUtil.process(e, logger);
		}
	}
	/*
	 * 
	 * <li>说明：根据键查询键值
	 * <li>创建人：程梅
	 * <li>创建日期：2013-7-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	/*public void getKeyValueByKey() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String key = getRequest().getParameter("key");
			String keyValue = this.manager.getKeyValueByKey(key);
			map.put("keyValue", keyValue);  //返回键值
			map.put("success", "true");
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}
	}*/
}