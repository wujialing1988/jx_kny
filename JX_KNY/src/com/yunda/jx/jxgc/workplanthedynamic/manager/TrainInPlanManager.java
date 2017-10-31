package com.yunda.jx.jxgc.workplanthedynamic.manager;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.workplanthedynamic.entity.TrainInPlan;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车入段业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-3-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainInPlanManager")
public class TrainInPlanManager  extends JXBaseManager<TrainInPlan, TrainInPlan> {
   

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2017-3-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchDate 生成日期
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void insertTheTrainInPlan(String searchDate) throws Exception {
        Long operaterId = SystemContext.getAcOperator().getOperatorid();
        this.deleteTodayTrainInPlan(searchDate);
        String sql = SqlMapUtil.getSql("scdd:generateTheTrainInPlan")
                                .replace("#creator#", operaterId.toString());
        daoUtils.executeSql(sql);
    }


    /**
     * <li>说明：删除当前时间的入段计划
     * <li>创建人：张迪
     * <li>创建日期：2017-3-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchDate 当前时间
     */
    private void deleteTodayTrainInPlan(String searchDate) {
        String sql = "Delete JXGC_TRAIN_IN_PLAN where Plan_Generate_Date = '" + searchDate +"'";
        daoUtils.executeSql(sql);
        
    }


    /**
     * <li>说明：提交入段计划
     * <li>创建人：张迪
     * <li>创建日期：2017-3-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public void updateTheTrainInPlan() {
        String sql = "Update JXGC_TRAIN_IN_PLAN SET save_status = 1";
        daoUtils.executeSql(sql);
    }
  
}
