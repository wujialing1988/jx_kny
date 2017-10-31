package com.yunda.jx.pjjx.base.wpdefine.action; 

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPUnionRecord;
import com.yunda.jx.pjjx.base.wpdefine.manager.WPUnionRecordManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WPUnionRecord控制器, 作业流程所用记录单
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
public class WPUnionRecordAction extends JXBaseAction<WPUnionRecord, WPUnionRecord, WPUnionRecordManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * <li>说明：接受逻辑删除记录请求，向客户端返回操作结果（JSON格式）
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception
	 */	
	@SuppressWarnings("all")
	public void logicDelete() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String wPIDX = getRequest().getParameter("wPIDX");
			String[] errMsg = this.manager.validateDelete(ids, wPIDX);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.logicDelete(ids, wPIDX);
				map.put(Constants.SUCCESS, true);
			} else {
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
	 * <li>说明：批量保存新增的【作业流程适用配件】
	 * <li>创建人：何涛
	 * <li>创建日期：2014-11-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void save() throws JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			WPUnionRecord[] parts = JSONUtil.read(getRequest(), WPUnionRecord[].class);
			String[] errMsg = this.manager.validateUpdate(parts);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(parts);
				map.put(Constants.SUCCESS, true);
			} else {
				map.put(Constants.SUCCESS, false);
				map.put(Constants.ERRMSG, errMsg);
			}			
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
}