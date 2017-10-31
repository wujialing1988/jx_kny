
package com.yunda.jx.component.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.component.manager.PartsSelectManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 通用弹出grid控件控制器类
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
public class PartsSelectAction extends JXBaseAction<Object, Object, PartsSelectManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());    
    
    /**
	 * <li>说明：查询列表
	 * <li>创建人：程锐
	 * <li>创建日期：2012-9-21
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param
	 * @return void
	 * @throws Exception
	 */
    public void pageList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String entity = req.getParameter("entity");
            String queryHql = req.getParameter("queryHql");
            String partsname = StringUtil.nvlTrim(req.getParameter("partsname"), "");
            String specificationModel = StringUtil.nvlTrim(req.getParameter("specificationModel"), "");
            String nameplateNo = StringUtil.nvlTrim(req.getParameter("nameplateNo"), "");
            String partsNo = StringUtil.nvlTrim(req.getParameter("partsNo"), "");
            map = this.manager.page(entity, getStart(), getLimit(), queryHql, partsname, specificationModel, nameplateNo, partsNo, getOrders());
        } catch (Exception e) {
        	ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>方法说明：根据HQL查询列表 
     * <li>方法名称：pageList2
     * <li>@throws Exception
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-7-15 上午10:50:56
     * <li>修改人：
     * <li>修改内容：此方法配件中在使用，请勿注销。
     */
    public void pageList2() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String queryHql = req.getParameter("queryHql");
            String partsname = StringUtil.nvlTrim(req.getParameter("partsname"), "");
            String specificationModel = StringUtil.nvlTrim(req.getParameter("specificationModel"), "");
            String nameplateNo = StringUtil.nvlTrim(req.getParameter("nameplateNo"), "");
            String partsNo = StringUtil.nvlTrim(req.getParameter("partsNo"), "");
            map = this.manager.page2(getStart(), getLimit(), queryHql, partsname, specificationModel, nameplateNo, partsNo, getOrders());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
