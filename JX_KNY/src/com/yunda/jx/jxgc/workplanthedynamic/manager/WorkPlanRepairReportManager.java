package com.yunda.jx.jxgc.workplanthedynamic.manager;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.workplanthedynamic.entity.WorkPlanRepairReport;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 年计划任务完成统计业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-3-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "workPlanRepairReportManager")
public class WorkPlanRepairReportManager  extends JXBaseManager<WorkPlanRepairReport, WorkPlanRepairReport> {
   

    /**
     * <li>说明：生成任务完成统计表
     * <li>创建人：张迪
     * <li>创建日期：2017-3-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchDate 生成日期
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void insertWorkPlanRepairReport(String searchDate) throws Exception {
        Long operaterId = SystemContext.getAcOperator().getOperatorid();
        this.deleteTodayRepairReport(searchDate);
        String sql = SqlMapUtil.getSql("scdd:generateTheRepairReport")
                                .replace("#creator#", operaterId.toString());
        daoUtils.executeSql(sql);
    }
    /**
     * <li>说明：删除年兑现统计报表 
     * <li>创建人：张迪
     * <li>创建日期：2017-3-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchDate 当前时间
     */
    private void deleteTodayRepairReport(String searchDate) {
        String sql =  "Delete jxgc_work_plan_repair_report where Plan_Generate_Date = '"+ searchDate + "'";
        daoUtils.executeSql(sql);
        
    }
    /**
     * <li>说明：更新提交年兑现统计报表
     * <li>创建人：张迪
     * <li>创建日期：2017-3-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public void updateWorkPlanRepairReport() {
        String sql = "Update jxgc_work_plan_repair_report set save_status = 1";
        daoUtils.executeSql(sql);
        
    }
  
}
