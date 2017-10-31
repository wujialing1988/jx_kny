package com.yunda.freight.zb.plan.manager;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanInterrupt;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 中断记录业务
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("zbglRdpPlanInterruptManager")
public class ZbglRdpPlanInterruptManager extends JXBaseManager<ZbglRdpPlanInterrupt, ZbglRdpPlanInterrupt> {



    /**
     * <li>说明：中断方法（创建记录，记录中断开始时间）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx 计划ID
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void interruptPlan(String planIdx) throws BusinessException, NoSuchFieldException {
        ZbglRdpPlanInterrupt interrupt = new ZbglRdpPlanInterrupt();
        interrupt.setInterruptStartTime(new Date());
        interrupt.setRdpPlanIdx(planIdx);
        this.saveOrUpdate(interrupt);
    }
    
    /**
     * <li>说明：恢复方法（查询最近一条已中断的记录，记录中断结束时间）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx 计划ID
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void regainPlan(String planIdx) throws BusinessException, NoSuchFieldException {
        ZbglRdpPlanInterrupt entity = this.getZbglRdpPlanInterruptByPlan(planIdx);
        if(entity != null){
            entity.setInterruptEndTime(new Date());
            this.saveOrUpdate(entity);
        }
    }
    
    /**
     * <li>说明：通过计划ID查询最近一条中断计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planIdx
     * @return
     */
    public ZbglRdpPlanInterrupt getZbglRdpPlanInterruptByPlan(String planIdx){
        StringBuffer hql = new StringBuffer(" From ZbglRdpPlanInterrupt where recordStatus = 0 and rdpPlanIdx = ? and interruptEndTime is null order by interruptStartTime ");
        return (ZbglRdpPlanInterrupt)this.daoUtils.findSingle(hql.toString(), new Object[]{planIdx});
    }
    
}
