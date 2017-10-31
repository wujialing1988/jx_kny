package com.yunda.baseapp.workcalendar.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 工作日历包装类实体
 * <li>创建人：程锐
 * <li>创建日期：2015-7-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@SuppressWarnings("serial")
public class WorkCalendarBean implements Serializable {
    
    /**
     * 00:00:00
     */
    public static final String ZEROTIME = "00:00:00";
    
    /**
     * 23:59:59
     */
    public static final String TWENTYFOURTIME = "23:59:59";
    
    private Object[] defaultEveryDayWorkTime;
    
    private List<Object[]> searchScopeList;
    
    private String beginDate;
    
    private String endDate;
    
    public String getBeginDate() {
        return beginDate;
    }
    
    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    public Object[] getDefaultEveryDayWorkTime() {
        return defaultEveryDayWorkTime;
    }
    
    public void setDefaultEveryDayWorkTime(Object[] defaultEveryDayWorkTime) {
        this.defaultEveryDayWorkTime = defaultEveryDayWorkTime;
    }
    
    public List<Object[]> getSearchScopeList() {
        return searchScopeList;
    }
    
    public void setSearchScopeList(List<Object[]> searchScopeList) {
        this.searchScopeList = searchScopeList;
    }
    
}
