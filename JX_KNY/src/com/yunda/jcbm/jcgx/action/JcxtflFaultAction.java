package com.yunda.jcbm.jcgx.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.order.AbstractOrderAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jcbm.jcgx.entity.JcxtflFault;
import com.yunda.jcbm.jcgx.manager.JcxtflFaultManager;

/**
 * <li>类型名称：分类编码故障现象控制类
 * <li>说明：
 * <li>创建人： 何东
 * <li>创建日期：2016-5-16
 * <li>修改人： 
 * <li>修改日期：
 */
@SuppressWarnings(value="serial")
public class JcxtflFaultAction extends AbstractOrderAction<JcxtflFault, JcxtflFault, JcxtflFaultManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>方法名：保存分类编码的故障现象
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：
	 * <li>创建人：何东
	 * <li>创建日期：2016-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unused")
	public void saveFlbmFault() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String searchJson = StringUtil.nvlTrim(getRequest().getParameter("entityJson"), "{}");
            JcxtflFault[] entitys = JSONUtil.read(searchJson, JcxtflFault[].class);
            
            if (entitys != null && entitys.length > 0) {     
        	    map = manager.saveFlbmFault(entitys);
            }
        }catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
            map.put("success", false);
            map.put("errMsg", "保存分类编码的故障现象失败");
        } finally{
            JSONUtil.write(getResponse(), map);
        }
	}
    
	/**
	 * <li>说明：自定义故障现象
	 * <li>创建人：张迪
	 * <li>创建日期：2016-9-2
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public void saveFlbmFaultCus() throws Exception {
	    Map<String, Object> map = new HashMap<String, Object>();
	    try {
            JcxtflFault t = (JcxtflFault) JSONUtil.read(getRequest(), entity.getClass());
            JcxtflFault[] entitys = new JcxtflFault[1];          
            String[] errMsg = this.manager.validateInsert(t);
            if (errMsg == null || errMsg.length < 1) {
                entitys[0]=t;
                map = manager.saveFlbmFault(entitys);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
	    }catch (RuntimeException e) {
	        ExceptionUtil.process(e, logger);
	        map.put("success", false);
	        map.put("errMsg", "保存分类编码的故障现象失败");
	    } finally{
	        JSONUtil.write(getResponse(), map);
	    }
	}

  
}