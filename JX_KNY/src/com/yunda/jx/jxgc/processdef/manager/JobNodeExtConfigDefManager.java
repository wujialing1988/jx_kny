package com.yunda.jx.jxgc.processdef.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.processdef.entity.JobNodeExtConfigDef;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobNodeExtConfigDef业务类,扩展配置
 * <li>创建人：何涛
 * <li>创建日期：2015-5-4
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="jobNodeExtConfigDefManager")
public class JobNodeExtConfigDefManager extends JXBaseManager<JobNodeExtConfigDef, JobNodeExtConfigDef> {
    
    /**
     * <li>保存方法前的更新验证
     * <li>创建人：何涛
     * <li>创建日期：2012-08-07
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param t 实体对象
     * @return String[] 验证消息数组
     */ 
    @Override
    public String[] validateUpdate(JobNodeExtConfigDef t) {
        if (null != t.getIdx() && t.getIdx().trim().length() <= 0) {
            t.setIdx(null);
        }
        return super.validateUpdate(t);
    }
    /**
     * 
     * <li>说明：获取同一个作业流程的所有节点扩展配置信息
     * <li>创建人：程梅
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processIDX 流程id
     * @return List<JobNodeExtConfigDef> 扩展配置信息list
     */
    @SuppressWarnings("unchecked")
    public List<JobNodeExtConfigDef> getModelsByProcessIDX(String processIDX) {
        String hql = "From JobNodeExtConfigDef Where nodeIDX in (select idx From JobProcessNodeDef Where recordStatus = 0 And processIDX = ?)";
        return this.daoUtils.find(hql, new Object[]{processIDX});
    }
}
