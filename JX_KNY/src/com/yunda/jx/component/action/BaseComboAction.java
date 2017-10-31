
package com.yunda.jx.component.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.component.manager.BaseComboManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 通用下拉列表控件控制器类
 * <li>创建人：程锐
 * <li>创建日期：2012-10-12
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class BaseComboAction extends JXBaseAction<Object, Object, BaseComboManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：baseCombo调用后台方法
     * <li>创建人：程锐
     * <li>创建日期：2012-9-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param
     * @return void
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void pageList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String entity = req.getParameter("entity");
            String queryHql = req.getParameter("queryHql");
            String queryParams = req.getParameter("queryParams");
            String queryValue = StringUtil.nvlTrim(req.getParameter("query"), "");
            String queryName = StringUtil.nvlTrim(req.getParameter("queryName"), "");
            String manager = req.getParameter("manager");
            
            if(!StringUtil.isNullOrBlank(manager)){
                IbaseCombo bm = (IbaseCombo)getManager(manager +"Manager");
                map = bm.getBaseComboData(req, getStart(), getLimit());
            }else{
                Map queryParamsMap = new HashMap();
                if (!StringUtil.isNullOrBlank(queryParams)) {
                    queryParamsMap = JSONUtil.read(queryParams, Map.class);
                }
                map = this.manager.page(entity, queryName,queryValue,queryParamsMap, getStart(), getLimit(), queryHql);
            }
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * 
     * <li>说明：baseComboTree和baseMultyComboTree调用后台方法
     * <li>创建人：程锐
     * <li>创建日期：2014-1-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getBaseComboTree() throws Exception{
    	List<HashMap> children = new ArrayList<HashMap>();
    	Map<String, Object> map = new HashMap<String, Object>();
    	try {
    		HttpServletRequest req = getRequest();
    		String manager = req.getParameter("manager");
    		if(!StringUtil.isNullOrBlank(manager)){
    			IbaseComboTree bm = (IbaseComboTree)getManager(manager +"Manager");
    			children = bm.getBaseComboTree(req);
    		}
    	} catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), children);
        }
    }
}
