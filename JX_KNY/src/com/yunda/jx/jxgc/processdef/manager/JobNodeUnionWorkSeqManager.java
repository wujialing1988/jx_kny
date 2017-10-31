package com.yunda.jx.jxgc.processdef.manager;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.processdef.entity.JobNodeUnionWorkSeq;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 业务类 作业节点所挂记录卡
 * <li>创建人：林欢
 * <li>创建日期：2016-6-4
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */
@Service(value = "jobNodeUnionWorkSeqManager")
public class JobNodeUnionWorkSeqManager extends JXBaseManager<JobNodeUnionWorkSeq, JobNodeUnionWorkSeq> {
    
    /**
     * <li>说明：批量保存实体时的验证方法
     * <li>创建人：林欢
     * <li>创建日期：2016-6-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param array 作业节点所挂记录卡实体数组
     * @return String[] 验证消息
     */
    public String[] validateUpdate(JobNodeUnionWorkSeq[] array) {
        for (int i = 0; i < array.length; i++) {
            JobNodeUnionWorkSeq jobWorkSeq = array[i];
            JobNodeUnionWorkSeq model = this.getModel(jobWorkSeq.getNodeIDX(), jobWorkSeq.getRecordCardIDX());
            if (null != model) {
                array[i] = null;
            }
        }
        return null;
        
    }
    
    /**
     * <li>说明：根据作业流程节点主键、作业项目主键获取实体对象
     * <li>创建人：林欢
     * <li>创建日期：2016-6-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 作业流程节点主键
     * @param projectIDX 作业项目主键
     * @return 实体对象
     */
    private JobNodeUnionWorkSeq getModel(String nodeIDX, String recordCardIDX) {
        String hql = "From JobNodeUnionWorkSeq Where nodeIDX = ? And recordCardIDX = ?";
        return (JobNodeUnionWorkSeq) this.daoUtils.findSingle(hql, new Object[] { nodeIDX, recordCardIDX });
    }
    
    /**
     * <li>说明：批量删除【作业节点所挂记录卡】
     * <li>创建人：林欢
     * <li>创建日期：2016-6-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void deleteJobNodeUnionWorkSeqByRecordCardIDX(String[] ids, String nodeIDX) {
        for (String id : ids) {
//          通过流程节点idx和工序卡idx查询中间对象
            JobNodeUnionWorkSeq jobNodeUnionWorkSeq = getModel(nodeIDX, id);
            // 物理删除中间对象
            this.getDaoUtils().getHibernateTemplate().delete(jobNodeUnionWorkSeq);
        }
    }

    /**
     * 
     * <li>说明：复制节点关联的检修的记录卡信息
     * <li>创建人：林欢
     * <li>创建日期：2016-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param oldNodeIdx 旧节点id
     * @param newNodeIdx 复制后的新节点id
     */
    public List<JobNodeUnionWorkSeq> getJobNodeUnionWorkSeqListById(String oldNodeIdx) {
        String hql = "From JobNodeUnionWorkSeq Where nodeIDX = ? ";
        return this.daoUtils.find(hql, new Object[] { oldNodeIdx });
    }

    /**
     * <li>说明：保存机车作业流程节点定义
     * 业务逻辑：如果是修改车型，那么就会把该流程下所有节点下的工单全部清除（因为节点下的作业卡是根据车型选择出来的）
     * <li>创建人：林欢   
     * <li>创建日期：2016-7-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public List<JobNodeUnionWorkSeq> findWorkSeqListByJobProcessDefIDX(String jobProcessDefIDX) {
        StringBuffer sb = new StringBuffer();
        
        sb.append(" select c.* ");
        sb.append(" from jxgc_job_process_def       a, ");
        sb.append(" jxgc_job_process_node_def  b, ");
        sb.append(" jxgc_jobnode_union_workseq c ");
        sb.append(" where a.record_status = 0 ");
        sb.append(" and b.record_status = 0 ");
        sb.append(" and b.process_idx = a.idx ");
        sb.append(" and c.node_idx = b.idx ");
        sb.append(" and a.idx = '").append(jobProcessDefIDX).append("'");

        return this.getDaoUtils().executeSqlQueryEntity(sb.toString(), JobNodeUnionWorkSeq.class);
    }
}
