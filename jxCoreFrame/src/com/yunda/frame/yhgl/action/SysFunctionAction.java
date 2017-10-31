package com.yunda.frame.yhgl.action;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcFunction;
import com.yunda.frame.yhgl.manager.SysFunctionManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统应用功能管理-应用功能action
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class SysFunctionAction extends JXBaseAction <AcFunction, AcFunction, SysFunctionManager> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：功能选择控件
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-23
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void pageList() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			HttpServletRequest req = getRequest();
			String funcname = StringUtil.nvlTrim(req.getParameter("funcname"), "");  //查询条件： 岗位名称 
            SearchEntity<AcFunction> searchEntity = new SearchEntity<AcFunction>(entity, getStart(), getLimit(), getOrders());
			map = this.manager.findFunctions(searchEntity,funcname).extjsStore();
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}	
	}
	
	@SuppressWarnings("unchecked")
	public void saveOrUpdate() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			AcFunction t = (AcFunction)JSONUtil.read(getRequest(), entity.getClass());
			String isAdd = StringUtil.isNullOrBlank(getRequest().getParameter("isAdd"))?"true":getRequest().getParameter("isAdd");
			String[] errMsg = null;
			if(Boolean.valueOf(isAdd)){
				errMsg = this.manager.validateInsert(t); //新增验证
			} else {
				errMsg = this.manager.validateUpdate(t); //编辑验证
			}
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdate(t, Boolean.valueOf(isAdd));
//				返回记录保存成功的实体对象
				map.put("entity", t);  
				map.put("success", true);
			} else {
				map.put("success", false);
				map.put("errMsg", errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
}
