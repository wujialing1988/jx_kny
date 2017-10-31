package com.yunda.baseapp.workcalendar.manager;

import org.springframework.stereotype.Service;

import com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo;
import com.yunda.frame.common.JXBaseManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 工作日历明细查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2016-3-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@Service(value="workCalendarInfoQueryManager")
public class WorkCalendarInfoQueryManager extends JXBaseManager<WorkCalendarInfo, WorkCalendarInfo>{
    
    private String unUseWorkCalendar = "24小时工作制（7x24）";
    
    private String unUseWorkCalendar1 = "24小时工作制(7x24)";
    
    /**
     * <li>说明：特殊处理：是否有效的工作日历
     * <li>创建人：程锐
     * <li>创建日期：2016-3-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param infoIdx 工作日历IDX
     * @return true 是有效的工作日历 false 否
     */
    public boolean isUseWorkCalendar(String infoIdx) {
        WorkCalendarInfo info = getModelById(infoIdx);
        if (info == null)
            return true;
        else if (unUseWorkCalendar.equals(info.getCalendarName()) || unUseWorkCalendar1.equals(info.getCalendarName()))
            return false;
        return true;
    }
}
