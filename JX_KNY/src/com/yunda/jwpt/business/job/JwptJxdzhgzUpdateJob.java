package com.yunda.jwpt.business.job;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jwpt.business.entity.JwptJxdzhgz;
import com.yunda.jwpt.business.manager.JwptJxdzhgzManager;
import com.yunda.jwpt.common.AbstractBaseBusinessDataUpdateJob;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车检修作业计划 更新业务类
 * <li>创建人：何涛
 * <li>创建日期：2016-05-31
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "jxgc_train_work_plan")
public class JwptJxdzhgzUpdateJob extends AbstractBaseBusinessDataUpdateJob<TrainWorkPlan, JwptJxdzhgz> {
    
    /** JwptJxdzhgz业务类,机车检修电子合格证（主表） */
    @Resource
    private JwptJxdzhgzManager jwptJxdzhgzManager;
    
    /** TrainWorkPlan业务类,机车检修作业计划 */
    @Resource
    private TrainWorkPlanManager trainWorkPlanManager;

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
    protected JXBaseManager<TrainWorkPlan, TrainWorkPlan> getTManager(){
        return trainWorkPlanManager;
    }
    
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
    protected JXBaseManager<JwptJxdzhgz, JwptJxdzhgz> getEManager() {
        return jwptJxdzhgzManager;
    }
    
}
