package com.yunda.jx.jxgc.workplanthedynamic.manager;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.workplanthedynamic.entity.WorkPlanWartoutTrain;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车检修修峻待离段机车业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-3-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "workPlanWartoutTrainManager")
public class WorkPlanWartoutTrainManager  extends JXBaseManager<WorkPlanWartoutTrain, WorkPlanWartoutTrain> {
    

    /**
     * <li>说明： 生成机车检修修峻待离段机车信息
     * <li>创建人：张迪
     * <li>创建日期：2017-3-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planGenerateDateStr
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void insertTheWartoutTrain(String searchDate) throws Exception {
        Long operaterId = SystemContext.getAcOperator().getOperatorid();
        this.deleteTodayWartoutTrain(searchDate);
        String sql = SqlMapUtil.getSql("jxgc-rdp:generateTheWaitoutTrain")
                                .replace("#creator#", operaterId.toString());
        daoUtils.executeSql(sql);
    }
    /**
     * <li>说明：删除修峻待离段数据
     * <li>创建人：张迪
     * <li>创建日期：2017-3-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchDate 当前时间
     */
    private void deleteTodayWartoutTrain(String searchDate) {
        String sql = "Delete JXGC_Work_plan_wartout_train where Plan_Generate_Date = '"+searchDate +"'";
        daoUtils.executeSql(sql);
        
    }
    /**
     * <li>说明：提交修峻待离段数据
     * <li>创建人：张迪
     * <li>创建日期：2017-3-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public void updateTheWartoutTrain() {
        String sql = "Update JXGC_Work_plan_wartout_train set save_status = 1 " ;
        daoUtils.executeSql(sql);
        
    }
}
