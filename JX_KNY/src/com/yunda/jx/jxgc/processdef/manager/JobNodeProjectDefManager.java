package com.yunda.jx.jxgc.processdef.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.processdef.entity.JobNodeProjectDef;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobNodeProjectDef业务类,关联作业项目
 * <li>创建人：何涛
 * <li>创建日期：2015-4-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="jobNodeProjectDefManager")
public class JobNodeProjectDefManager extends JXBaseManager<JobNodeProjectDef, JobNodeProjectDef>{
    
//    /**
//     * <li>说明：批量保存实体时的验证方法
//     * <li>创建人：何涛
//     * <li>创建日期：2015-04-15
//     * <li>修改人：何涛
//     * <li>修改日期：2015-05-15
//     * <li>修改内容：验证数据库是否已经存在了相同的记录
//     * 
//     * @param array 作业节点所挂作业项目实体数组
//     * @return String[] 验证消息
//     */
//    public String[] validateUpdate(JobNodeProjectDef[] array) {
//        for (int i = 0; i < array.length; i++) {
//            JobNodeProjectDef projectDef = array[i];
//            JobNodeProjectDef model = this.getModel(projectDef.getNodeIDX(), projectDef.getProjectIDX());
//            if (null != model) {
//                array[i] = null;
//            }
//        }
//        return null;
//    
//    }
//    
//    /**
//     * <li>说明：批量逻辑删除
//     * <li>创建人：何涛
//     * <li>创建日期：2015-4-16
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param nodeIDX 作业流程节点主键
//     * @param projectIdxs 作业项目主键数组
//     * @throws NoSuchFieldException 
//     * @throws BusinessException 
//     */
//    public void logicDelete(String nodeIDX, String[] projectIdxs) throws BusinessException, NoSuchFieldException {
//        for (String projectIDX : projectIdxs) {
//            JobNodeProjectDef entity = this.getModel(nodeIDX, projectIDX);
//            this.logicDelete(entity.getIdx());
//        }
//    }
//    
//    /**
//     * <li>说明：根据作业流程节点主键、作业项目主键获取实体对象
//     * <li>创建人：何涛
//     * <li>创建日期：2015-4-16
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param nodeIDX 作业流程节点主键
//     * @param projectIDX 作业项目主键
//     * @return 实体对象
//     */
//    private JobNodeProjectDef getModel(String nodeIDX, String projectIDX) {
//        String hql = "From JobNodeProjectDef Where recordStatus = 0 And nodeIDX = ? And projectIDX = ?";
//        return (JobNodeProjectDef) this.daoUtils.findSingle(hql, new Object[]{nodeIDX, projectIDX});
//    }
//    
//    /**
//     * <li>说明：根据作业流程节点主键实体对象集
//     * <li>创建人：何涛
//     * <li>创建日期：2015-4-16
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param nodeIDX 作业流程节点主键
//     * @return 实体对象
//     */
//    @SuppressWarnings("unchecked")
//    public List<JobNodeProjectDef> getModelByNodeIDX(Serializable nodeIDX) {
//        String hql = "From JobNodeProjectDef Where recordStatus = 0 And nodeIDX = ?";
//        return this.daoUtils.find(hql, new Object[]{nodeIDX});
//    }
    
}