package com.yunda.jx.jxgc.processdef.manager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeRelDef;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobProcessNodeRelDef业务类,节点前后置关系
 * <li>创建人：何涛
 * <li>创建日期：2015-4-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="jobProcessNodeRelDefManager")
public class JobProcessNodeRelDefManager extends JXBaseManager<JobProcessNodeRelDef, JobProcessNodeRelDef>{
    
    /** WPNode业务类,作业节点 */
    @Resource
    private JobProcessNodeDefManager jobProcessNodeDefManager;
    
    /**
     * <li>说明：唯一性验证，一个作业节点不能添加多个重复的前置作业节点
     * <li>创建人：何涛
     * <li>创建日期：2014-14-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */     
    @Override
    public String[] validateUpdate(JobProcessNodeRelDef entity) throws BusinessException {
        // 获取某一节点的所有前置节点关系
        List<JobProcessNodeRelDef> models = getModelsByWPNodeIDX(entity.getNodeIDX());
        for(JobProcessNodeRelDef t: models) {
            if (t.getIdx().equals(entity.getIdx()) && t.getPreNodeIDX().equals(entity.getPreNodeIDX())) {
                return null;
            }
            if (t.getPreNodeIDX().equals(entity.getPreNodeIDX())) {
                String preWPNodeName = jobProcessNodeDefManager.getModelById(t.getPreNodeIDX()).getNodeName();
                String nodeName = jobProcessNodeDefManager.getModelById(t.getNodeIDX()).getNodeName();
                return new String[]{"作业节点：" + nodeName + "已经配置了前置节点[" + preWPNodeName + "]，不能重复添加！"};
            }
        }
        
        // 验证是否有循环依赖（即：如有作业节点A、B、C，C的前置节点有A和B，则A或者B的前置节点不能有C）
        // 获取所有已该节点设置了前置节点的节点信息
        List<JobProcessNodeDef> list = new ArrayList<JobProcessNodeDef>();
        this.jobProcessNodeDefManager.listAfterNodes(entity.getNodeIDX(), list);
        if (null != list && 0 < list.size()) {
            for (JobProcessNodeDef node : list) {
                if (node.getIdx().equals(entity.getPreNodeIDX())) {
                    return new String[]{"前置节点设置存在循环依赖，请检查！"};
                }
            }
        }
        
        // 验证是否有重复的前置节点关系（即：如有作业节点A、B、C，B的前置节点为A，C的前置节点为B，则C不能设置前置节点为A）
        list = new ArrayList<JobProcessNodeDef>();
        this.jobProcessNodeDefManager.listBeforeNodes(entity.getNodeIDX(), list);
        if (null != list && 0 < list.size()) {
            for (JobProcessNodeDef nodeDef : list) {
                if (nodeDef.getIdx().equals(entity.getPreNodeIDX())) {
                    return new String[]{"不能设置重叠的前置节点关系，请检查！"};
                }
                // 验证新增的节点是否是前置节点的后置节点（即：如有作业节点A、B、C，B、C的前置节点均为A，则C(B)不能设置前置节点为B(C)）
//                List<JobProcessNodeDef> tempList = new ArrayList<JobProcessNodeDef>();
//                this.jobProcessNodeDefManager.listAfterNodes(nodeDef.getIdx(), tempList);
//                for (JobProcessNodeDef node : tempList) {
//                    if (node.getIdx().equals(entity.getPreNodeIDX())) {
//                        return new String[]{"前置节点设置存在循环依赖，请检查！"};
//                    }
//                }
            }
        }
        
        return super.validateUpdate(entity);
    }
    
    /**
     * <li>说明：重写保存方法，更新前后置节点关系时同步更新父节点的工期
     * <li>创建人：何涛
     * <li>创建日期：2015-04-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param t 检修作业流程节点前后置关系实例对象
     */
    @Override
    public void saveOrUpdate(JobProcessNodeRelDef t) throws BusinessException, NoSuchFieldException {
        super.saveOrUpdate(t);
        // 同步更新父节点的工期
        JobProcessNodeDef nodeDef = this.jobProcessNodeDefManager.getModelById(t.getNodeIDX());
        if (null != nodeDef) {
            this.jobProcessNodeDefManager.updateParentWPNodes(nodeDef.getParentIDX());
        }
    }
    
    /**
     * <li>说明：根据“节点主键”获取【节点前后置关系】
     * <li>创建人：何涛
     * <li>创建日期：2015-04-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param nodeIDX 节点主键
     * @return List<JobProcessNodeRelDef>实体集合
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeRelDef> getModelsByWPNodeIDX(Serializable nodeIDX) {
        String hql = "From JobProcessNodeRelDef Where recordStatus = 0 And nodeIDX = ?";
        return this.daoUtils.find(hql, new Object[]{nodeIDX});
    }
    
    /**
     * <li>说明：根据“节点主键”获取该节点参与的所有前后置关系
     * <li>创建人：何涛
     * <li>创建日期：2015-04-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param nodeIDX 节点主键
     * @return List<JobProcessNodeRelDef>实体集合
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeRelDef> listModels(Serializable nodeIDX) {
        String hql = "From JobProcessNodeRelDef Where recordStatus = 0 And (nodeIDX = ? Or preNodeIDX = ?)";
        return this.daoUtils.find(hql, new Object[]{nodeIDX, nodeIDX});
    }
    
    /**
     * <li>说明：分页查询，因为数据表只存储了前置节点的主键，为了在节目显示节点名称，所以对前置节点的名称进行联合查询
     * <li>创建人：何涛
     * <li>创建日期：2015-01-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param searchEntity 查询对象实体
     * @return Page<JobProcessNodeRelDef> 实体集合
     */
    @Override
    public Page<JobProcessNodeRelDef> findPageList(SearchEntity<JobProcessNodeRelDef> searchEntity) throws BusinessException {
        StringBuilder sb =  new StringBuilder();
        sb.append("Select new JobProcessNodeRelDef(a.idx, a.nodeIDX, a.preNodeIDX, b.nodeName, b.seqNo, a.seqClass, a.delayTime) From JobProcessNodeRelDef a, JobProcessNodeDef b Where a.preNodeIDX = b.idx And a.recordStatus = 0 And b.recordStatus = 0");
        JobProcessNodeRelDef entity = searchEntity.getEntity();
        // 查询条件 - 节点主键
        if (!StringUtil.isNullOrBlank(entity.getNodeIDX())) {
            sb.append(" And a.nodeIDX = '").append(entity.getNodeIDX()).append("'");
        }
        if (null != searchEntity.getOrders()) {
            Order order = searchEntity.getOrders()[0];
            if (order.toString().contains("preNodeName")) {
                sb.append(" Order By ").append(order.toString().replace("preNodeName", "b.nodeName"));
            } else if (order.toString().contains("preSeqNo")){
                sb.append(" Order By ").append(order.toString().replace("preSeqNo", "b.seqNo"));
            } else {
                sb.append(" Order By ").append("a." + order);
            }
        } else {
            sb.append(" Order By b.seqNo");
        }
        String hql = sb.toString();
        String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
        return this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
    }
    
    /**
     * <li>说明：逻辑删除
     * <li>创建人：何涛
     * <li>创建日期：2015-05-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @param ids 要删除的实例idx主键数组
     */
    @Override
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        for (Serializable idx : ids) {
            JobProcessNodeRelDef entity = this.getModelById(idx);
            // 逻辑删除
            this.logicDelete(idx);
            if (null != entity) {
                JobProcessNodeDef nodeDef = this.jobProcessNodeDefManager.getModelById(entity.getNodeIDX());
                if (null != nodeDef) {
                    // 同步更新父节点对象实例的工期
                    this.jobProcessNodeDefManager.updateParentWPNodes(nodeDef.getParentIDX());
                }
            }
        }
    }
    
    /**
     * <li>说明：获取节点的前置节点顺序号
     * <li>创建人：何涛
     * <li>创建日期：2015-6-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点主键
     * @return 节点的前置节点顺序号，例如（1,2）
     */
    @SuppressWarnings("unchecked")
    public String getPreNodeSeqNo(String nodeIDX) {
        StringBuilder sb = new StringBuilder("select a.seq_no from jxgc_job_process_node_def a, jxgc_job_process_node_rel_def b where a.record_status = 0 and a.idx = b.pre_node_idx and b.record_status=0");
        sb.append(" and b.node_idx = '").append(nodeIDX).append("' order by a.seq_no");
        List<BigDecimal> list = this.daoUtils.executeSqlQuery(sb.toString());
        if (null == list || list.size() <= 0) {
            return null;
        }
        StringBuilder sBuilder = new StringBuilder();
        for (BigDecimal seqNo : list) {
            sBuilder.append(",").append(seqNo.intValue());
        }
        return sBuilder.substring(1);
    }
    /**
     * 
     * <li>说明：获取同一个作业流程的所有节点前后置关系
     * <li>创建人：程梅
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processIDX 流程id
     * @return List<JobProcessNodeRelDef> 前后置关系list
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeRelDef> getModelsByProcessIDX(String processIDX) {
        String hql = "From JobProcessNodeRelDef Where recordStatus = 0 And nodeIDX in (select idx From JobProcessNodeDef Where recordStatus = 0 And processIDX = ?)";
        return this.daoUtils.find(hql, new Object[]{processIDX});
    }
    
    /**
     * <li>说明：不同层次间的拖拽完成后，清空可能存在的节点前后置关系
     * <li>创建人：何涛
     * <li>创建日期：2015-9-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点主键
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    @SuppressWarnings("unchecked")
    public void logicDeleteByMoveNode(String nodeIDX) throws BusinessException, NoSuchFieldException {
        String hql = "From JobProcessNodeRelDef Where recordStatus = 0 And (nodeIDX = ? Or preNodeIDX = ?)";
        List<JobProcessNodeRelDef> list = this.daoUtils.find(hql, new Object[]{ nodeIDX, nodeIDX });
        this.logicDelete(list);
    }
    
}