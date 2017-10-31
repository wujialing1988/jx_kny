package com.yunda.jx.jxgc.workplanthedynamic.action;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.workplanthedynamic.entity.WorkPlanRepairReport;
import com.yunda.jx.jxgc.workplanthedynamic.manager.WorkPlanRepairReportManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 任务完成统计
 * <li>创建人：张迪
 * <li>创建日期：2017-3-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WorkPlanRepairReportAction extends JXBaseAction<WorkPlanRepairReport, WorkPlanRepairReport, WorkPlanRepairReportManager>{
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
 
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2017-3-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void insertWorkPlanRepairReport() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String planGenerateDateStr = getRequest().getParameter("planGenerateDate");
        String errMsg = "";
        try {
            this.manager.insertWorkPlanRepairReport(planGenerateDateStr);
            map.put(Constants.SUCCESS, true);
         
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errMsg);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
  
}
