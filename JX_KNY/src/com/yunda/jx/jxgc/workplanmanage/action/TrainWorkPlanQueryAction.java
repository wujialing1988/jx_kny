package com.yunda.jx.jxgc.workplanmanage.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanQueryManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车检修作业计划查询控制器
 * <li>创建人：程锐
 * <li>创建日期：2015-5-13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class TrainWorkPlanQueryAction extends JXBaseAction<TrainWorkPlan, TrainWorkPlan, TrainWorkPlanQueryManager>{

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：作业工单-查询生产任务单组件
     * <li>创建人：程锐
     * <li>创建日期：2015-5-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findComboDataByWorkCardQuery() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = this.manager.getComboDataByWorkCardQuery(getStart(), getLimit());            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据机车检修作业计划状态查询机车检修作业计划
     * <li>创建人：何涛
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void queryByWorkPlanStatus() throws JsonMappingException, IOException {
        List<HashMap<String, Object>> children = null;
        try {
            String workPlanStatus = getRequest().getParameter("workPlanStatus");
            children = manager.queryByWorkPlanStatus(workPlanStatus);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), children);
        }
    }
    
}
