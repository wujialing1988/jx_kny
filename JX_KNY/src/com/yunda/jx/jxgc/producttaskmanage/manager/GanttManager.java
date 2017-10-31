package com.yunda.jx.jxgc.producttaskmanage.manager;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.yunda.jx.third.edo.entity.Calendar;
import com.yunda.jx.third.edo.entity.WeekDay;
import com.yunda.jx.third.edo.entity.WorkingTime;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 甘特图展示公共业务类
 * <li>创建人：程锐
 * <li>创建日期：2014-3-27
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service("ganttManager")
public class GanttManager {
    
    /**
     * <li>说明：构造甘特图-工作时间实体对象数组
     * <li>创建人：程锐
     * <li>创建日期：2013-4-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param
     * @return WorkingTime[] 甘特图-工作时间实体对象数组
     */
    public WorkingTime[] getWorkingTimes() {
        WorkingTime workingTime = new WorkingTime("09:00:00", "12:00:00");
        WorkingTime workingTime1 = new WorkingTime("13:00:00", "17:30:00");
        WorkingTime[] workingTimes = new WorkingTime[2];
        Arrays.fill(workingTimes, workingTime);
        Arrays.fill(workingTimes, workingTime1);
        return workingTimes;
    }
    
    /**
     * <li>说明：构造甘特图-项目日历实体对象数组
     * <li>创建人：程锐
     * <li>创建日期：2013-4-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workingTimes 甘特图-工作时间实体对象数组
     * @return Calendar[] 甘特图-项目日历实体对象数组
     */
    public Calendar[] getCalendars(WorkingTime[] workingTimes) {
        WeekDay[] weekDays = new WeekDay[7];
        weekDays[0] = new WeekDay(1, "0");
        for (int j = 2; j < 7; j++) {
            weekDays[j - 1] = new WeekDay(j, "1", workingTimes);
        }
        weekDays[6] = new WeekDay(7, "0");
        Calendar[] calendars = new Calendar[1];
        calendars[0] = new Calendar("1", "标准", 1, -1.0, weekDays);
        return calendars;
    }
}
