package com.yunda.jx.jxgc.workhis.workplan.manager;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.DateUtil;
import com.yunda.jx.jxgc.workhis.workplan.entity.TrainWorkPlanHis;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeQueryManager;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanQueryManager;
import com.yunda.jx.jxgc.workplanmanage.manager.WorkPlanGanttManager;
import com.yunda.jx.third.edo.entity.Result;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainWorkPlanHis业务类,机车检修作业计划历史
 * <li>创建人：程梅
 * <li>创建日期：2015年8月17日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */ 
@Service(value = "trainWorkPlanHisManager")
public class TrainWorkPlanHisManager extends JXBaseManager<TrainWorkPlanHis, TrainWorkPlanHis> {
    
    @Resource
    private WorkPlanHisGanttManager workPlanHisGanttManager;
    
    @Resource
    private WorkPlanGanttManager workPlanGanttManager;
    
    @Resource
    private TrainWorkPlanQueryManager trainWorkPlanQueryManager;
    @Resource
    private JobProcessNodeQueryManager jobProcessNodeQueryManager;
    /**
     * <li>说明：甘特图展示
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @param displayMode 甘特图显示模式：default默认（第一次加载、刷新等）、expanded（对节点操作后加载）
     * @param nodeIdx 操作的节点IDX
     * @return 甘特图Result对象
     * @throws Exception
     */
    public Result planOrderGantt(String workPlanIDX, String displayMode, String nodeIdx) throws Exception {
        TrainWorkPlanHis workPlan = getModelById(workPlanIDX);
        if (workPlan == null)
            return null;  
        TrainWorkPlan trainWorkPlan = trainWorkPlanQueryManager.getModelById(workPlanIDX);
        if ("Complete".equals(workPlan.getTransLogStatus()))
            return workPlanHisGanttManager.planOrderGantt(workPlan, displayMode, nodeIdx);
        else
            return workPlanGanttManager.planOrderGantt(trainWorkPlan, displayMode, nodeIdx);
    }
    
    
    /**
     * <li>说明：查找实际时间的最小最大值
     * <li>创建人：张迪
     * <li>创建日期：2016-8-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划idx
     * @return 最大最小时间数组
     * @throws ParseException
     */
    public Date[] getMinAndMaxRealTime(String workPlanIDX) throws ParseException {
        List<Object[]> timeList = jobProcessNodeQueryManager.getMinBeginAndMaxEndRealTime(workPlanIDX); 
        Date[] time= new Date[2];
        for (Object[] obj : timeList) {
            if (obj[0] == null || obj[1] == null)
                continue;
            time[0] = DateUtil.yyyy_MM_dd.parse(obj[0].toString());
            time[1] = DateUtil.yyyy_MM_dd.parse(obj[1].toString());
        }
        return time;
    }

}