package com.yunda.baseapp.workcalendar.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunda.Application;
import com.yunda.baseapp.workcalendar.entity.WorkCalendarBean;
import com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 工作日历缓存业务类（单例）
 * <li>创建人：程锐
 * <li>创建日期：2015-6-3
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
public class WorkCalendarInfoUtil {
    
    private static WorkCalendarInfoUtil wcInstance = null;
    
    public static Map<String, WorkCalendarBean> wcInfoMap = new HashMap<String, WorkCalendarBean>();
    
    private Date beginDate;
    
    private static final String YYYYMMDD = "yyyyMMdd";
    
    /**
     * <li>说明：构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * @param beginDate 开始日期
     */
    private WorkCalendarInfoUtil(Date beginDate) {
        this.beginDate = beginDate;
    }
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     */
    private WorkCalendarInfoUtil() {
        
    }
    
    /**
     * <li>说明：获取工作日历缓存业务类实例
     * <li>创建人：程锐
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param beginDate 开始日期
     * @return 工作日历缓存业务类实例
     */
    public static WorkCalendarInfoUtil getInstance(Date beginDate) {
        if (wcInstance == null)
            return new WorkCalendarInfoUtil(beginDate);
        return wcInstance;
    }
    
    /**
     * <li>说明：构造工作日历查询Map
     * <li>创建人：程锐
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 工作日历查询Map
     */
    @SuppressWarnings("unchecked")
    public Map<String, WorkCalendarBean> buildMap() {
        wcInfoMap.clear();
        Date newDate = new Date();
        if (newDate.compareTo(beginDate) < 0)
            beginDate = newDate;
        Date startDate = beforeYearDate(beginDate, 1);
        Date endDate = afterYearDate(beginDate, 1);
        
        WorkCalendarInfoManager infoManager = (WorkCalendarInfoManager) Application.getSpringApplicationContext().getBean("workCalendarInfoManager");
        WorkCalendarDetailManager detailManager = (WorkCalendarDetailManager) Application.getSpringApplicationContext().getBean("workCalendarDetailManager");
        List<WorkCalendarInfo> infoList = infoManager.findList(new WorkCalendarInfo());
        
        for (WorkCalendarInfo info : infoList) {
            WorkCalendarBean calendarBean = new WorkCalendarBean();
            Object[] defaultEveryDayWorkTime = detailManager.getDefaultEveryDayWorkTime(info.getIdx());
            calendarBean.setDefaultEveryDayWorkTime(defaultEveryDayWorkTime);
            String beginDateStr = detailManager.dateConvertToString(startDate, YYYYMMDD);
            String endDateStr = detailManager.dateConvertToString(endDate, YYYYMMDD);
            List searchScopeList = detailManager.getStartToEndWorkTimeInfo(beginDateStr, endDateStr, info.getIdx());
            calendarBean.setSearchScopeList(searchScopeList);
            calendarBean.setBeginDate(beginDateStr);
            calendarBean.setEndDate(endDateStr);
            wcInfoMap.put(info.getIdx(), calendarBean);
        }
        return wcInfoMap;
    }
    
    public Date getBeginDate() {
        return beginDate;
    }
    
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    
    /**
     * <li>说明：获取提前N年的日期
     * <li>创建人：程锐
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param beginDate 开始日期
     * @param beforeYears 提前多少年
     * @return 提前N年的日期
     */
    private Date beforeYearDate(Date beginDate, int beforeYears) {
        Calendar first = new GregorianCalendar();
        first.setTime(beginDate);
        first.set(Calendar.YEAR, first.get(Calendar.YEAR) - beforeYears);
        return first.getTime();
    }
    
    /**
     * <li>说明：获取延后N年的日期
     * <li>创建人：程锐
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param beginDate 开始日期
     * @param afterYears 延后多少年
     * @return 提前N年的日期
     */
    private Date afterYearDate(Date beginDate, int afterYears) {
        Calendar first = new GregorianCalendar();
        first.setTime(beginDate);
        first.set(Calendar.YEAR, first.get(Calendar.YEAR) + afterYears);
        return first.getTime();
    }
    
}
