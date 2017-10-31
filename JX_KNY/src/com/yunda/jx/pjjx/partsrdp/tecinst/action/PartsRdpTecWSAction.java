package com.yunda.jx.pjjx.partsrdp.tecinst.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.partsrdp.tecinst.entity.PartsRdpTecWS;
import com.yunda.jx.pjjx.partsrdp.tecinst.manager.PartsRdpTecWSManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdpTecWP控制器, 配件检修工序实例
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsRdpTecWSAction extends JXBaseAction<PartsRdpTecWS, PartsRdpTecWS, PartsRdpTecWSManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：批量处理
	 * <li>创建人：何涛
	 * <li>创建日期：2014-12-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void finishBatchWP() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String[] errMsg = this.manager.validateStatus(ids, PartsRdpTecWS.CONST_STR_STATUS_WCL);
			if (null == errMsg) {
				this.manager.finishBatchWS(ids);
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
			JSONUtil.write(getResponse(), map);
		}
	}
	
	/**
	 * <li>说明：查询配件检修工序树
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
    public void tree() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		List<HashMap<String, Object>> children = null ;
	    try {
			String parentIDX = getRequest().getParameter("parentIDX");  			// 上级工序idx主键
			String rdpTecCardIDX = getRequest().getParameter("rdpTecCardIDX");  	// 检修工艺工单idx主键
		    children = manager.tree(parentIDX, rdpTecCardIDX);
		} catch (RuntimeException e) {
			ExceptionUtil.process(e, logger, map);
		} finally{
			JSONUtil.write(getResponse(), children);
		}
    }
	
}