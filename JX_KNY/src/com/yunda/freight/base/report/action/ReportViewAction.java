package com.yunda.freight.base.report.action;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.JSONUtil;
import com.yunda.freight.base.report.entity.ReportView;
import com.yunda.freight.base.report.manager.ReportViewManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 报表统计Action
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-12-27 11:36:09
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class ReportViewAction extends JXBaseAction<ReportView, ReportView, ReportViewManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
	/**
     * <li>说明：树形列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param reportType 报表分类
     * @return
     */
    public void findReportViewForTree() throws Exception {
        String reportType = getRequest().getParameter("reportType");
        List<HashMap<String, Object>>  children = manager.findReportViewForTree(reportType);
        JSONUtil.write(getResponse(), children);
    }
   
    
    
}
