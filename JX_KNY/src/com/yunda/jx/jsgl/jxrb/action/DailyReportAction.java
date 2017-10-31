package com.yunda.jx.jsgl.jxrb.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jsgl.jxrb.entity.DailyReport;
import com.yunda.jx.jsgl.jxrb.manager.DailyReportManager;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: DailyReport控制器,生产调度—机车检修日报
 * <li>创建人：何涛
 * <li>创建日期：2016-5-27
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class DailyReportAction extends JXBaseAction<DailyReport, DailyReport, DailyReportManager>{
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：新增机车检修日报
     * <li>创建人：何涛
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void insertDailyReport() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = this.getRequest();
        try {
            String workPlanIDs = StringUtil.nvl(req.getParameter("workPlanIDs"), "[]");
            this.manager.insertDailyReport(JSONUtil.read(workPlanIDs, String[].class));
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
    /**
     * <li>说明：保存机车检修日报
     * <li>创建人：何涛
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public void saveDailyReport() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = this.getRequest();
        try {
            DailyReport report = JSONUtil.read(req, DailyReport.class);
            this.manager.saveDailyReport(report);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    
}
