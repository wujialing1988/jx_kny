package com.yunda.jx.util;

import java.util.Date;
import java.util.List;

import com.yunda.Application;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarDetailManager;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;


/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 计算流程节点计划开完工时间、工期等逻辑的工具类
 * <li>创建人：程锐
 * <li>创建日期：2015-11-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
public class CalcWorkDateUtil {
    
    public static String workPlanCalendarIDX = "";  
    
    /**
     * <li>说明：对时间操作，为解决“当完成时间在工作日历之外时，后置节点的开始时间需重置在工作日历之内”的问题
     * <li>创建人：程锐
     * <li>创建日期：2014-1-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param time 日期
     * @param workCalendarIDX 日历IDX
     * @return 日期
     * @throws Exception
     */
    public static Date getCalDate(Date time, String workCalendarIDX) throws Exception {
        if (StringUtil.isNullOrBlank(workCalendarIDX))
            workCalendarIDX = workPlanCalendarIDX;
//        WorkCalendarInfoQueryManager workCalendarInfoQueryManager =
//            (WorkCalendarInfoQueryManager) Application.getSpringApplicationContext().getBean("workCalendarInfoQueryManager");
//        if (!workCalendarInfoQueryManager.isUseWorkCalendar(workCalendarIDX))
//            return time;
        time = getAddDateByCal(time, Double.valueOf(59), workCalendarIDX);
        time = getMinusDate(time, Double.valueOf(59));
        return time;
    }
    
    /**
     * <li>说明：根据开工时间和工期获取完工时间（下一节点开工时间）
     * <li>创建人：程锐
     * <li>创建日期：2014-1-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param startDate 开工时间
     * @param seconds 工期 (秒数)
     * @param workCalendarIDX 日历IDX
     * @return 完工时间
     */
    public static Date getAddDateByCal(Date startDate, Double seconds, String workCalendarIDX) throws Exception {
        if (startDate == null)
            return null;
        long startTime = startDate.getTime();// 开始时间毫秒数
        long timeInterval = 0L;
        if (seconds != null) {
            Double ratedWorkMinutes = seconds * 1000;
            timeInterval = ratedWorkMinutes.longValue();
        }
        long endTime = startTime + timeInterval;
        WorkCalendarDetailManager workCalendarDetailManager =
            (WorkCalendarDetailManager) Application.getSpringApplicationContext().getBean("workCalendarDetailManager");
        if (workCalendarDetailManager != null) {
            if (StringUtil.isNullOrBlank(workCalendarIDX))
                workCalendarIDX = workPlanCalendarIDX;
            endTime = workCalendarDetailManager.getFinalTime(startTime, timeInterval, workCalendarIDX);// 根据bps工作日历计算出转入时间+前置节点工期获取相应时间的毫秒数
        }
        return DateUtil.getDateByMillSeconds(endTime);
    }
    
    /**
     * <li>说明：根据开始日期和秒数计算日期（不使用工作日历）
     * <li>创建人：程锐
     * <li>创建日期：2014-1-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param startDate 开始日期
     * @param seconds 工期(秒数)
     * @return 计算出的日期
     */
    
    public static Date getMinusDate(Date startDate, Double seconds) {
        return getOpDate(startDate, seconds, "minus");
    }
    
    /**
     * <li>说明：计算日期
     * <li>创建人：程锐
     * <li>创建日期：2014-4-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param startDate 日期
     * @param seconds 工期毫秒数
     * @param op 操作符 add 增加 minus 减少
     * @return 计算后的日期
     */
    public static Date getOpDate(Date startDate, Double seconds, String op) {
        if (startDate != null) {
            long startTime = startDate.getTime();// 开始时间毫秒数
            long timeInterval = 0L;
            if (seconds != null) {
                Double ratedWorkMinutes = seconds * 1000;
                timeInterval = ratedWorkMinutes.longValue();
            }
            long endTime = 0L;
            if ("add".equals(op))
                endTime = startTime + timeInterval;
            else if ("minus".equals(op))
                endTime = startTime - timeInterval;
            Date planBeginTime = DateUtil.getDateByMillSeconds(endTime);
            return planBeginTime;
        }
        return null;
    }
    
    /**
     * <li>说明：根据开工时间和工期获取完工时间（下一节点开工时间）
     * <li>创建人：程锐
     * <li>创建日期：2013-3-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param startDate 开工时间
     * @param ratedMinutes 工期(分钟)
     * @param workCalendarIDX 日历IDX
     * @return 完工时间（下一节点开工时间）
     */
    public static Date getDateByRatedMinutes(Date startDate, Double ratedMinutes, String workCalendarIDX) throws Exception {
        if (StringUtil.isNullOrBlank(workCalendarIDX))
            workCalendarIDX = workPlanCalendarIDX;
        ratedMinutes = ratedMinutes == null ? 0D : ratedMinutes;
        return getAddDateByCal(startDate, ratedMinutes * 60, workCalendarIDX);
    }
    

    
    /**
     * <li>说明：构造节点IDX数组
     * <li>创建人：程锐
     * <li>创建日期：2015-5-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeList 节点IDX列表
     * @return 节点IDX数组
     */
    public static String[] buildNodeArray(List<String> nodeList) {
        String[] nextNodeIDXS = null;
        if (nodeList != null && nodeList.size() > 0) {
            nextNodeIDXS = new String[nodeList.size()];            
            for (int i = 0; i < nodeList.size(); i++) {
                nextNodeIDXS[i] = nodeList.get(i);
            }
        }
        return nextNodeIDXS;
    }
    
    /**
     * <li>说明：比较两个日期的大小,日期1比日期2小返回true，反之返回false
     * <li>创建人：程锐
     * <li>创建日期：2014-4-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param newDate 新日期
     * @param oldDate 旧日期
     * @return 布尔值 日期1比日期2小返回true，反之返回false
     */
    public static boolean compareTwoDate(Date newDate, Date oldDate) {
        if (newDate != null && oldDate != null && newDate.compareTo(oldDate) < 0)
            return true;
        return false;
    }
}
