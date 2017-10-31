package com.yunda.jx.jxgc.workplanmanage.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeRelDef;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeRel;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：JobProcessNodeRel业务类,机车检修作业计划-前置节点
 * <li>创建人：程锐
 * <li>创建日期：2015-04-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */ 
@Service(value = "jobProcessNodeRelManager")
public class JobProcessNodeRelManager extends JXBaseManager<JobProcessNodeRel, JobProcessNodeRel> {
    
    /** 作业流程节点查询业务类 */
    @Resource
    private JobProcessNodeQueryManager jobProcessNodeQueryManager;
    
    /**
     * 批量查询所有定义存入内存
     * <li>说明：生成作业计划-根据前置节点定义生成前置节点关系
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 作业计划实体
     * @param nodeList 作业计划关联的所有节点列表
     * @throws Exception
     */
    //如无前置节点定义则按原逻辑也生成一条无前置节点的记录
    @SuppressWarnings("unchecked")
    public void saveByNode(TrainWorkPlan workPlan, List<JobProcessNode> nodeList) throws Exception{
        List<JobProcessNodeRel> list = new ArrayList<JobProcessNodeRel>();
        String hql = SqlMapUtil.getSql("jxgc-processNode:getRelDefListByProcess").replace("#processIDX#", workPlan.getProcessIDX());
        List<JobProcessNodeRelDef> relDefList = daoUtils.find(hql);
        Map<String, List<JobProcessNodeRelDef>> relDefMap = new HashMap<String, List<JobProcessNodeRelDef>>();
        for (JobProcessNodeRelDef def : relDefList) {
            if (!relDefMap.containsKey(def.getNodeIDX())) {
                List<JobProcessNodeRelDef> tempList = new ArrayList<JobProcessNodeRelDef>();
                tempList.add(def);
                relDefMap.put(def.getNodeIDX(), tempList);
            } else {
                relDefMap.get(def.getNodeIDX()).add(def);
            }
        }
        Map<String, JobProcessNode> nodeMap = new HashMap<String, JobProcessNode>();
        for (JobProcessNode node : nodeList) {
            nodeMap.put(node.getNodeIDX(), node);
        }
        for (JobProcessNode node : nodeList) {
            List<JobProcessNodeRelDef> relDefListByNode = relDefMap.get(node.getNodeIDX());
            if (relDefListByNode == null || relDefListByNode.isEmpty()) {
                JobProcessNodeRel rel = new JobProcessNodeRel();
                rel.setNodeIDX(node.getIdx());
                rel.setSeqClass(JobProcessNodeDef.CONST_STR_SEQ_CLASS_FS);
                rel.setDelayTime(0d);
                list.add(rel);
            } else {
                for (JobProcessNodeRelDef relDef : relDefListByNode) {
                    JobProcessNodeRel rel = new JobProcessNodeRel();
                    BeanUtils.copyProperties(rel, relDef);
                    rel.setIdx("");
                    rel.setNodeIDX(node.getIdx());
                    JobProcessNode preNode = nodeMap.get(relDef.getPreNodeIDX());
                    if (preNode == null)
                        throw new BusinessException("生成前置节点关系时，前置节点为空");
                    rel.setPreNodeIDX(preNode.getIdx());
                    list.add(rel);
                }  
            }
        }
        saveOrUpdate(list);
    }
    
    /**
     * <li>说明：新增编辑节点前置关系
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @param rels 前置关系实体数组
     * @throws Exception
     */
    public void saveByNode(String nodeIDX, JobProcessNodeRel[] rels) throws Exception{        
        String sql = "DELETE  JXGC_JOB_PROCESS_NODE_REL WHERE RECORD_STATUS = 0 AND NODE_IDX = '" + nodeIDX + "'";
        daoUtils.executeSql(sql);
        if (rels == null || rels.length < 1) {
            rels = new JobProcessNodeRel[1];
            JobProcessNodeRel rel = new JobProcessNodeRel();
            rel.setIdx("");
            rel.setNodeIDX(nodeIDX);
            rel.setSeqClass(JobProcessNodeDef.CONST_STR_SEQ_CLASS_FS);
            rel.setDelayTime(0D);
            rels[0] = rel;
        } else {
            for (JobProcessNodeRel rel : rels) {  
                rel.setIdx("");
                rel.setNodeIDX(nodeIDX);
                if (StringUtil.isNullOrBlank(rel.getSeqClass()))
                    rel.setSeqClass(JobProcessNodeDef.CONST_STR_SEQ_CLASS_FS);
            }
        }
        
        saveOrUpdate(Arrays.asList(rels));
    }
    
    /**
     * <li>说明：判断流程节点实例顺序列表中是否已经存在这个对象，判定规则是两个对象的NodeIdx和PreNodeIdx都相等，true存在，false不存在
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-3-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param seqList 流程节点顺序列表
     * @param seq 流程节点顺序对象
     * @return boolean: true存在，false不存在
     */
    public boolean containTecNodeCaseSequence(List<JobProcessNodeRel> seqList, JobProcessNodeRel seq){
        for (JobProcessNodeRel rel : seqList) {
            if(seq.getPreNodeIDX() == null){
                if(rel.getPreNodeIDX() == null 
                        && seq.getNodeIDX().equals(rel.getNodeIDX()))   return true;
            } else {
                if(seq.getPreNodeIDX().equals(rel.getPreNodeIDX())    
                    && seq.getNodeIDX().equals(rel.getNodeIDX()))   return true;
            }
        }
        return false;
    }
    
    
    
    /**
     * <li>说明：节点编辑-获取前置关系列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 前置关系列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeRel> getPreNodeList(String nodeIDX) {
        StringBuilder hql = new StringBuilder("select new JobProcessNodeRel(a.idx, a.nodeIDX, a.preNodeIDX, a.seqClass, a.delayTime, b.nodeName, c.dictname)")
                                      .append(" from JobProcessNodeRel a, JobProcessNode b, EosDictEntry c where a.recordStatus = 0 and b.recordStatus = 0")
                                      .append(" and a.preNodeIDX = b.idx and a.nodeIDX = '")
                                      .append(nodeIDX)
                                      .append("'")
                                      .append(" and c.id.dicttypeid = 'PJJX_WP_NODE_SEQ_TYPE' and c.status = 1 and c.id.dictid = a.seqClass");
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：节点编辑-获取前置关系分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 前置关系分页列表
     * @throws Exception
     */
    public Page getList(String nodeIDX) throws Exception {
        List<JobProcessNodeRel> relList = new ArrayList<JobProcessNodeRel>();
        if (!StringUtil.isNullOrBlank(nodeIDX)) {
            relList = getPreNodeList(nodeIDX);            
        }
        List<JobProcessNodeRel> list = new ArrayList<JobProcessNodeRel>();
        for (int i = 0; i < 20; i++) {            
            if (relList != null && relList.size() > 0 && i < relList.size()) {
                list.add(relList.get(i));
                continue;
            }
            JobProcessNodeRel rel = new JobProcessNodeRel();
//            rel.setDelayTime(0D);
            list.add(rel);
        }
        return new Page<JobProcessNodeRel>(list.size(), list);
    }
    
    /**
     * <li>说明：节点编辑-删除前置关系
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 前置关系id数组 
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updateForDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<JobProcessNodeRel> entityList = new ArrayList<JobProcessNodeRel>();
        for (Serializable id : ids) {
            JobProcessNodeRel nodeRel = getModelById(id);
            nodeRel.setPreNodeIDX("");
            nodeRel = EntityUtil.setSysinfo(nodeRel);
            entityList.add(nodeRel);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }
    
    /**
     * <li>说明：调整节点层级关系-删除此节点关联的前置关系,将第一条前置关系的preNodeIDX设为空，其他关系作逻辑删除
     * <li>创建人：程锐
     * <li>创建日期：2015-5-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public void updateForDeleteByNodeIDX(String nodeIDX) throws BusinessException, NoSuchFieldException {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("nodeIDX", nodeIDX);
        List<JobProcessNodeRel> list = new ArrayList<JobProcessNodeRel>();
        List<JobProcessNodeRel> nodeRelList = jobProcessNodeQueryManager.getNodeRelList(paramMap);
        if (nodeRelList != null && nodeRelList.size() == 1) {
            JobProcessNodeRel nodeRel = nodeRelList.get(0);
            nodeRel.setPreNodeIDX("");
            nodeRel = EntityUtil.setSysinfo(nodeRel);
            list.add(nodeRel);
        }
        else if (nodeRelList != null && nodeRelList.size() > 1) {
            for (int i = 0; i < nodeRelList.size(); i++) {
                JobProcessNodeRel nodeRel = nodeRelList.get(i);
                if (i == 0)                     
                    nodeRel.setPreNodeIDX("");
                else
                    nodeRel.setRecordStatus(Constants.DELETED);
                nodeRel = EntityUtil.setSysinfo(nodeRel);
                list.add(nodeRel);    
            }
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(list);
    }    
}