package com.yunda.jwpt.business.job;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jwpt.business.entity.JwptJcjxjh;
import com.yunda.jwpt.business.manager.JwptJcjxjhManager;
import com.yunda.jwpt.common.AbstractBaseBusinessDataUpdateJob;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlan;
import com.yunda.jx.scdd.enforceplan.manager.TrainEnforcePlanManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车生产计划 更新业务类
 * <li>创建人：何涛
 * <li>创建日期：2016-06-01
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "scdd_train_enforce_plan")
public class JwptJcjxjhUpdateJob extends AbstractBaseBusinessDataUpdateJob<TrainEnforcePlan, JwptJcjxjh> {
    
    /** JxglJcjxjh业务类,机车检修日报 */
    @Resource
    private JwptJcjxjhManager jxglJcjxjhManager;
    
    /** TrainEnforcePlan业务类,生产调度—机车检修日报 */
    @Resource
    private TrainEnforcePlanManager trainEnforcePlanManager;
    
    /**
     * <li>说明：获取数据同步实体业务管理器
     * <li>创建人：何涛
     * <li>创建日期：2016-6-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 数据同步实体管理器
     */
    @Override
    protected JXBaseManager<JwptJcjxjh, JwptJcjxjh> getEManager() {
        return jxglJcjxjhManager;
    }
    
    /**
     * <li>说明：获取检修业务实体业务管理器
     * <li>创建人：何涛
     * <li>创建日期：2016-6-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 检修业务实体管理器
     */
    @Override
    protected JXBaseManager<TrainEnforcePlan, TrainEnforcePlan> getTManager() {
        return trainEnforcePlanManager;
    }
    
}
