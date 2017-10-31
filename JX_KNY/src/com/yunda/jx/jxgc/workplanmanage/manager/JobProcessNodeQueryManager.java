package com.yunda.jx.jxgc.workplanmanage.manager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.manager.EosDictEntrySelectManager;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.webservice.entity.TrainWorkPlanNodeBean;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeBean;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeRel;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 流程节点查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-4-28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "jobProcessNodeQueryManager")
public class JobProcessNodeQueryManager extends JXBaseManager<JobProcessNode, JobProcessNode> implements IbaseComboTree{
    
    private static final String NODEIDX = "#nodeIDX#";
    
    private static final String ROOT = "ROOT_0";
    
    private static final String NODE_IDX = "nodeIDX";
    
    private static final String WORKPLANIDX = "#workPlanIDX#";
    
    private static final String ID = "id";
    
    private static final String TEXT = "text";
    
    private static final String LEAF = "leaf";
    
    private static final String WORK_PLAN_IDX = "workPlanIDX";
    
    private static final String TREAM = "tream";
    
    private static final String SQLSTR = "###";
    
    private static final String SQLPARAMSTR = "[?]";
    
    private static final String TRUE = "true";
    /** 根节点 */
    private String ROOT_0 = "ROOT_0";
    
    /** 作业工单查询业务类 */
    @Resource
    WorkCardQueryManager workCardQueryManager;
    @Resource
    EosDictEntrySelectManager eosDictEntrySelectManager;
    
    /** 车检修作业计划查询业务类 */
    @Resource
    private TrainWorkPlanQueryManager trainWorkPlanQueryManager ;
    
    
    /**
     * <li>说明：获取流程节点的前置节点实体列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @param workPlanIDX 作业计划IDX
     * @return 流程节点的前置节点实体列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getPreNodeCaseList(String nodeIDX, String workPlanIDX) {
        List<JobProcessNode> nodeCaseList = new ArrayList<JobProcessNode>();
        StringBuilder hql = new StringBuilder("from JobProcessNodeRel where recordStatus = 0 and nodeIDX = '")
                                      .append(nodeIDX).append("'")
                                      .append(" and nodeIDX in (select idx from JobProcessNode where workPlanIDX = '")
                                      .append(workPlanIDX)
                                      .append("' and recordStatus = 0) order by preNodeIDX");
        List<JobProcessNodeRel> sequenceList = daoUtils.find(hql.toString());
        if (sequenceList == null || sequenceList.size() < 1)
            return nodeCaseList;
        for (JobProcessNodeRel sequence : sequenceList) {
            if (StringUtil.isNullOrBlank(sequence.getPreNodeIDX()))
                continue;
            JobProcessNode nodeCase = getModelById(sequence.getPreNodeIDX());
            if (nodeCase != null)
                nodeCaseList.add(nodeCase);
        }
        return nodeCaseList;
    }
    
    /**
     * <li>说明：获取同一机车检修作业计划下第一级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修作业计划主键
     * @return 同一机车检修作业计划下第一级节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> firstWorkPlanChildList(String workPlanIDX) {
        String hql = SqlMapUtil.getSql("jxgc-processNode:getFirstChildNodeListByWorkPlan").replace(WORKPLANIDX, workPlanIDX);
        return daoUtils.find(hql);
    }
    
    /**
     * <li>说明：获取下级第一顺序节点
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点主键
     * @return 下级第一顺序节点
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> firstChildList(String parentIDX) {
        String replaceStr = "";
        if (StringUtil.isNullOrBlank(parentIDX)) {
            replaceStr = " parentIDX is null ";
        } else {
            replaceStr = " parentIDX = '" + parentIDX + "' ";
        }
        String hql = SqlMapUtil.getSql("jxgc-processNode:getFirstChildNodeList").replace("#replaceStr#", replaceStr);
        return daoUtils.find(hql);
    }
    
    /**
     * <li>说明：获取前置节点
     * <li>创建人：程锐
     * <li>创建日期：2014-1-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 流程节点
     * @return 获取前置节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getPrevNodeList(JobProcessNode nodeCase) {
        return getPrevNodeList(nodeCase.getIdx());
    }
    
    /**
     * <li>说明：获取前置节点
     * <li>创建人：程锐
     * <li>创建日期：2014-4-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 流程节点主键
     * @return 前置节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getPrevNodeList(String nodeIDX) {
        String hql = SqlMapUtil.getSql("jxgc-processNode:getPrevNodeList").replace(NODEIDX, nodeIDX);
        return daoUtils.find(hql);
    }
    
    /**
     * <li>说明：获取后续节点
     * <li>创建人：程锐
     * <li>创建日期：2014-1-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 流程节点
     * @return 后续节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getNextNodeList(JobProcessNode nodeCase) {
        return getNextNodeList(nodeCase.getIdx());
    }
    
    /**
     * <li>说明：获取后续节点
     * <li>创建人：程锐
     * <li>创建日期：2014-4-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 流程节点主键
     * @return 后续节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getNextNodeList(String nodeIDX) {
        String hql = SqlMapUtil.getSql("jxgc-processNode:getNextNodeList").replace(NODEIDX, nodeIDX);
        return daoUtils.find(hql);
    }
    
    /**
     * 
     * <li>说明：获取机车检修作业计划下第一层且无前置节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @return 机车检修作业计划下第一层且无前置节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getFirstNodeListByWorkPlan(String workPlanIDX) {
        String hql = SqlMapUtil.getSql("jxgc-processNode:getFirstNodeListByWorkPlan").replace(WORKPLANIDX, workPlanIDX);
        return daoUtils.find(hql);
    }
    
    /**
     * <li>说明：获取父节点
     * <li>创建人：程锐
     * <li>创建日期：2014-4-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 流程节点
     * @return 父节点
     */
    public JobProcessNode getParentNode(JobProcessNode nodeCase) {
        return getParentNode(nodeCase.getIdx());
    }
    
    /**
     * <li>说明：获取父节点
     * <li>创建人：程锐
     * <li>创建日期：2014-4-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCaseIDX 流程节点IDX 
     * @return 父节点
     */
    public JobProcessNode getParentNode(String nodeCaseIDX) {
        String hql = SqlMapUtil.getSql("jxgc-processNode:getParentNode").replace("#nodeCaseIDX#", nodeCaseIDX);
        return (JobProcessNode) daoUtils.findSingle(hql);
    }
    
    /**
     * <li>说明：获取同一父节点下的节点实例的最大完成时间列表
     * <li>创建人：程锐
     * <li>创建日期：2014-4-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIdx 父节点IDX
     * @return 最大完成时间列表
     */
    public List getMaxChildNodeEndTimeList(String parentIdx) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:getMaxChildNodeEndTime").replace("#parentIdx#", parentIdx);
        return daoUtils.executeSqlQuery(sql);
    }
    
    /**
     * <li>说明：获取同一父节点下的节点实例的最小开始时间
     * <li>创建人：程锐
     * <li>创建日期：2014-4-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIdx 父节点IDX
     * @return 同一父节点下的节点实例的最小开始时间
     * @throws ParseException
     */
    public Date getMinChildNodeBeginTime(String parentIdx) throws ParseException{
        String sql = SqlMapUtil.getSql("jxgc-processNode:getMinChildNodeBeginTime").replace("#parentIdx#", parentIdx);
        List list = daoUtils.executeSqlQuery(sql);
        if(list == null || list.size() < 1) 
            return null;
        if(list.get(0) != null && !StringUtil.isNullOrBlank(list.get(0).toString()))  
            return DateUtil.parse(list.get(0).toString(), "yyyy-MM-dd HH:mm:ss");
        return null;
    }
    
    /**
     * <li>说明：获取只关联一个工位的同一工位的工艺节点分组列表
     * <li>创建人：程锐
     * <li>创建日期：2014-6-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修作业计划IDX
     * @return 列表
     * [{
     *      0 以,分隔的流程节点idx
     *      1 工位idx
     *      2 工位名称
     *      3 工位编码
     *      4 流水线idx
     * }]
     */
    public List findOnlyOneSameWorkStationNodeList(String workPlanIDX) {
        String sql = SqlMapUtil.getSql("jxgc-repairlineNew:findOnlyOneSameWorkStationNodeList").replace(WORKPLANIDX, workPlanIDX);
        return daoUtils.executeSqlQuery(sql);
    }
    
    /**
     * <li>说明：得到同一机车检修作业计划下的节点的计划最小开工时间和最大完工时间
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @return 同一机车检修作业计划下的节点的最小开工时间和最大完工时间 Object[0] 最小开工时间 Object[1] 最大完工时间
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getMinBeginAndMaxEndTimeByNode(String workPlanIDX) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:getMinBeginAndMaxEndTimeByNode")
                               .replace(WORKPLANIDX, workPlanIDX);
        return daoUtils.executeSqlQuery(sql);
    }
    
    /**
     * <li>说明：得到同一机车检修作业下的节点的实际最小时间和实际最大时间
     * <li>创建人：张迪
     * <li>创建日期：2016-8-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @return 同一机车检修作业计划下的节点的实际最小时间和实际最大时间 Object[0] 实际最小开工时间 Object[1] 实际最大完工时间
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getMinBeginAndMaxEndRealTime(String workPlanIDX){
        String sql = SqlMapUtil.getSql("jxgc-processNode:getMinBeginAndMaxEndRealTimeByNode").replace(WORKPLANIDX, workPlanIDX);
        return daoUtils.executeSqlQuery(sql);
    }
    /**
     * <li>说明：选择流程节点树查询
     * <li>创建人：王利成
     * <li>创建日期：2015-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX  父节点主键
     * @param workPlanIDX 作业计划ID
     * @param nodeIDX 流程节点ID
     * @return  children
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> getTree(String parentIDX, String workPlanIDX, String nodeIDX){
        String idx = "";
        if (ROOT.equals(parentIDX)) {
            idx = parentIDX;
        } else {
            JobProcessNode parentObj = this.getModelById(parentIDX); // 获取父类对象
            idx = parentObj.getIdx();
        }        
        StringBuilder hql = new StringBuilder("from JobProcessNode where parentIDX = ? And workPlanIDX = ? And status in ('RUNNING' , 'NOTSTARTED') And recordStatus = ")
                                      .append(Constants.NO_DELETE);
        if (ROOT.equals(parentIDX)) {
            hql = new StringBuilder("from JobProcessNode where (parentIDX Is Null Or parentIDX = ?) And workPlanIDX = ? And status in ('RUNNING' , 'NOTSTARTED') And recordStatus = ")
                            .append(Constants.NO_DELETE); 
        }
        hql.append("and idx != ? order by seqNo");
        List<JobProcessNode> list = (List<JobProcessNode>) this.daoUtils.find(hql.toString(), new Object[] { idx, workPlanIDX, nodeIDX });
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        for (JobProcessNode t : list) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put(ID, t.getIdx()); // 节点idx主键
//            nodeMap.put(TEXT, formatDisplayInfo(t)); // 树节点显示名称
            nodeMap.put(TEXT, t.getNodeName()); // 树节点显示名称
            nodeMap.put(LEAF, t.getIsLeaf() == 0 ? false : true); // 是否是叶子节点 0:否；1：是
            nodeMap.put("nodeName", t.getNodeName()); // 节点名称
            nodeMap.put("nodeDesc", t.getNodeDesc()); // 节点描述
            nodeMap.put("seqNo", t.getSeqNo()); // 顺序号
            nodeMap.put("disabled", t.getIsLeaf() == 0 ? true : false); // 顺序号
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：获取只有父节点的树
     * <li>创建人：程锐
     * <li>创建日期：2015-5-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点IDX
     * @param workPlanIDX 作业计划IDX
     * @param nodeIDX 节点IDX
     * @param parentNodeIDX 节点所属的父节点IDX
     * @return children
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> getParentTree(String parentIDX, String workPlanIDX, String nodeIDX, String parentNodeIDX){
        String idx = "";
        if (ROOT.equals(parentIDX)) {
            idx = parentIDX;
        } else {
            JobProcessNode parentObj = this.getModelById(parentIDX); // 获取父类对象
            idx = parentObj.getIdx();
        }
        StringBuilder hql = new StringBuilder("from JobProcessNode where parentIDX = ? And workPlanIDX = ? And status in ('RUNNING' , 'NOTSTARTED') And recordStatus = ");
        if (ROOT.equals(parentIDX)) {
            hql = new StringBuilder("from JobProcessNode where (parentIDX Is Null Or parentIDX = ?) And workPlanIDX = ? And status in ('RUNNING' , 'NOTSTARTED') And recordStatus = ");
        }
        hql.append(Constants.NO_DELETE)
           .append(" and idx != ?");    
        hql.append (" order by seqNo");
        List<JobProcessNode> list = new ArrayList<JobProcessNode>();
        list = (List<JobProcessNode>) this.daoUtils.find(hql.toString(), new Object[] { idx, workPlanIDX, nodeIDX });
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        for (JobProcessNode t : list) {
            if (hasWorkCardOrActivity(t.getIdx()))
                continue;
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            if (!StringUtil.isNullOrBlank(parentNodeIDX) && parentNodeIDX.equals(t.getIdx())) {
                nodeMap.put("disabled", true);
            }
            nodeMap.put(ID, t.getIdx()); // 节点idx主键
            nodeMap.put(TEXT, t.getNodeName()); // 树节点显示名称
            nodeMap.put(LEAF, t.getIsLeaf() == 0 ? false : true);
            nodeMap.put("nodeName", t.getNodeName()); // 节点名称
            nodeMap.put("nodeDesc", t.getNodeDesc()); // 节点描述
            nodeMap.put("seqNo", t.getSeqNo()); // 顺序号
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：获取本节点在内的所有下级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 本节点在内的所有下级节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getAllChildNodeList(String nodeIDX) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:getAllChildNodeList")
                               .replace(NODEIDX, nodeIDX);
        return daoUtils.executeSqlQueryEntity(sql, JobProcessNode.class);
    }
    
    /**
     * <li>说明：获取除本节点之外的所有下级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 除本节点之外的所有下级节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getAllChildNodeExceptThisList(String nodeIDX) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:getAllChildNodeExceptThisList")
                               .replace(NODEIDX, nodeIDX);
        return daoUtils.executeSqlQueryEntity(sql, JobProcessNode.class);
    }
    
    /**
     * <li>说明：获取除本节点之外的所有下级子节点列表
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 除本节点之外的所有下级子节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getAllChildLeafNodeExceptThisList(String nodeIDX) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:getAllChildLeafNodeExceptThisList")
                               .replace(NODEIDX, nodeIDX);
        return daoUtils.executeSqlQueryEntity(sql, JobProcessNode.class);
    }
    
    /**
     * <li>说明：获取本节点在内的所有父级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 本节点在内的所有父级节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getAllParentNodeList(String nodeIDX) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:getAllParentNodeList")
                               .replace(NODEIDX, nodeIDX);
        return daoUtils.executeSqlQueryEntity(sql, JobProcessNode.class);
    }
    
    /**
     * <li>说明：获取本节点在内的所有第一层下级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 本节点在内的所有第一层下级节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getAllFirstChildNodeList(String nodeIDX) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:getAllFirstChildNodeList").replace(NODEIDX, nodeIDX);
        return daoUtils.executeSqlQueryEntity(sql, JobProcessNode.class);
    }
    
    /**
     * <li>说明：得到前置节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-5-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 前置节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getPreNodeListBySQL(String nodeIDX) {
        List<JobProcessNode> preNodeList = getPrevNodeList(nodeIDX);
        List<JobProcessNode> list = new ArrayList<JobProcessNode>();
        
        for (JobProcessNode preNode : preNodeList) {
            JobProcessNodeRel nodeRel = getNodeRelByPreAndThisNode(preNode.getIdx(), nodeIDX);
            Double delayTime = 0D;
            if (nodeRel != null)
                delayTime = nodeRel.getDelayTime();
            preNode.setDelayTime(delayTime);
            list.add(preNode);
        }
        return list;
    }
    
    /**
     * <li>说明：获取作业计划的所有节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @return 作业计划的所有节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getNodeListByWorkPlan(String workPlanIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put(WORK_PLAN_IDX, workPlanIDX);
        return getNodeList(paramMap);
    }

    /**
     * <li>说明：获取作业计划的所有子节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @return 作业计划的所有子节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getLeafNodeListByWorkPlan(String workPlanIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put(WORK_PLAN_IDX, workPlanIDX);
        paramMap.put("isLeaf", String.valueOf(JobProcessNodeDef.CONST_INT_IS_LEAF_YES));
        return getNodeList(paramMap);
    }
    /**
     * <li>说明：获取作业计划的完成或终止的节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @return 作业计划的所有节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getCompleteNodeListByWorkPlan(String workPlanIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put(WORK_PLAN_IDX, workPlanIDX);
        paramMap.put("status", "in'".concat(JobProcessNode.STATUS_COMPLETE).concat("','").concat(JobProcessNode.STATUS_STOP).concat("'"));
        return getNodeList(paramMap);
    }
    
    /**
     * <li>说明：获取父节点下未启动或运行中的节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-5-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点IDX
     * @return 父节点下未启动或运行中的节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getUnCompleteNodeListByParent(String parentIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("parentIDX", parentIDX);
        paramMap.put("status", "in'".concat(JobProcessNode.STATUS_UNSTART).concat("','").concat(JobProcessNode.STATUS_GOING).concat("'"));
        return getNodeList(paramMap);
    }
    
    /**
     * <li>说明：得到机车检修计划的所有节点数量
     * <li>创建人：程锐
     * <li>创建日期：2015-5-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @return 机车检修计划的所有节点数量
     */
    public int getAllNodeCountByWorkPlan(String workPlanIDX) {
        return CommonUtil.getListSize(getNodeListByWorkPlan(workPlanIDX));
    }
    
    /**
     * <li>说明：得到机车检修计划的所有已完成或已终止的节点数量
     * <li>创建人：程锐
     * <li>创建日期：2015-5-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @return 机车检修计划的所有已完成或已终止的节点数量
     */
    public int getCompleteNodeCountByWorkPlan(String workPlanIDX) {
        return CommonUtil.getListSize(getCompleteNodeListByWorkPlan(workPlanIDX));
    }
    
    /**
     * <li>说明：根据节点定义IDX获取此作业计划对应流程节点
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @param workPlanIDX 作业计划IDX
     * @return 此作业计划对应流程节点
     */    
    @SuppressWarnings("unchecked")
    public JobProcessNode getNodeByNodeIDX(String nodeIDX, String workPlanIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put(NODE_IDX, nodeIDX);
        paramMap.put(WORK_PLAN_IDX, workPlanIDX);
        return getNode(paramMap);
    }
    
    /**
     * <li>说明：获取下级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 下级节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getChildNodeList(String nodeIDX) {
        StringBuffer stringBuffer = new StringBuffer(500);
        Map paramMap = new HashMap<String, String>();
        paramMap.put("parentIDX", nodeIDX);
        //获取grid LIst列表
        List<JobProcessNode> nowNode = getNodeList(paramMap);
        for (JobProcessNode node : nowNode) {
            //获取当前节点的前置节点信息
            List<JobProcessNode> lastNodeList = getPreNodeCaseList(node.getIdx(), node.getWorkPlanIDX());
            for (int i = 0; i < lastNodeList.size(); i++) {
                JobProcessNode n = lastNodeList.get(i);
                //拼装前置节点信息
                if (i == lastNodeList.size() - 1) {
                    stringBuffer.append(n.getNodeName());
                }else {
                    stringBuffer.append(n.getNodeName()).append(",");
                }
            }
            node.setLastChildNodeNames(stringBuffer.toString());
        }
        return nowNode;
    }
    /**
     * <li>说明：获取节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 节点列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getNodeList(Map paramMap) {
        return daoUtils.find(getNodeHql(paramMap));
    }
    
    /**
     * <li>说明：获取流程节点实体
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 流程节点实体
     */
    public JobProcessNode getNode(Map paramMap) {        
        return (JobProcessNode) daoUtils.findSingle(getNodeHql(paramMap));
    }
    
    /**
     * <li>说明：根据前置节点和本节点获取前置关系
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param preNodeIDX 前置节点IDX
     * @param nodeIDX 节点IDX
     * @return 前置关系实体
     */
    @SuppressWarnings("unchecked")
    public JobProcessNodeRel getNodeRelByPreAndThisNode(String preNodeIDX, String nodeIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("preNodeIDX", preNodeIDX);
        paramMap.put(NODE_IDX, nodeIDX);
        return getNodeRel(paramMap);
    }
    
    /**
     * <li>说明：根据节点IDX获取前置关系列表
     * <li>创建人：程锐
     * <li>创建日期：2015-6-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 前置关系列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeRel> getRelListByNodeIDX(String nodeIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put(NODE_IDX, nodeIDX);
        return getNodeRelList(paramMap);
    }
    
    /**
     * <li>说明：获取作业计划下节点前置关系列表
     * <li>创建人：程锐
     * <li>创建日期：2015-6-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @return 节点前置关系列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeRel> getRelListByWorkPlan(String workPlanIDX) {
        String hql = SqlMapUtil.getSql("jxgc-processNode:getRelListByWorkPlan").replace(WORKPLANIDX, workPlanIDX);
        return daoUtils.find(hql);
    }
    
    /**
     * <li>说明：获取前置关系实体
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 前置关系实体
     */
    public JobProcessNodeRel getNodeRel(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from JobProcessNodeRel where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return (JobProcessNodeRel) daoUtils.findSingle(hql.toString());
    }
    
    /**
     * <li>说明：获取前置关系列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 前置关系列表
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNodeRel> getNodeRelList(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from JobProcessNodeRel where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：此节点是否关联工单
     * <li>创建人：程锐
     * <li>创建日期：2015-5-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return true 有关联工单，false 无
     */
    public boolean hasWorkCardOrActivity(String nodeIDX) {
        List<WorkCard> workCardList = workCardQueryManager.getWorkCardByNode(nodeIDX);
        return workCardList != null && workCardList.size() > 0;
    }
    
    /**
     * <li>说明：递归找到本节点的所有同级后置节点
     * <li>创建人：程锐
     * <li>创建日期：2015-5-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @param list 后置节点列表
     * @return 本节点的所有后置节点
     */
    public List<JobProcessNode> getNextNodeList(String nodeIDX, List<JobProcessNode> list) {
        List<JobProcessNode> nextNodeList = getNextNodeList(nodeIDX);
        for (JobProcessNode node : nextNodeList) {
            list.add(node);
            list = getNextNodeList(node.getIdx(), list);
        }
        return list;
    }
    
    /**
     * <li>说明：得到本节点及之前的所有子节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-5-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX 
     * @return 本节点及之前的所有子节点列表
     */ 
    public List<JobProcessNode> getAllPreNodeList(String nodeIDX) {
        List<JobProcessNode> allPreNodeList = new ArrayList<JobProcessNode>();
        //递归父节点的所有前置节点
        allPreNodeList.addAll(getAllPreByParentNode(getAllParentNodeList(nodeIDX), allPreNodeList));
        JobProcessNode node = getModelById(nodeIDX);
        allPreNodeList.add(getModelById(nodeIDX));
        //获取前面得到的节点的所有的子节点（包括自身）
        List<JobProcessNode> list = new ArrayList<JobProcessNode>();
        if (JobProcessNodeDef.CONST_INT_IS_LEAF_YES == node.getIsLeaf())
            list.add(node);
        for (JobProcessNode allPreNode : allPreNodeList) {
            List<JobProcessNode> childList = getAllChildNodeList(allPreNode.getIdx());
            for (JobProcessNode child : childList) {
                if (JobProcessNodeDef.CONST_INT_IS_LEAF_NO == child.getIsLeaf())
                    continue;
                list.add(child);
            }
        }
        return list;
    }
    
    /**
     * <li>说明：此节点及其所有子节点是否关联有初始化和处理中的工单
     * <li>创建人：程锐
     * <li>创建日期：2015-5-11
     * <li>修改人：修改
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return true 此节点及其所有子节点有关联工单， false 无
     */
    public boolean hasWorkCardOfAllChildNode(String nodeIDX) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:getWorkCardOfAllChildNode")
                               .replace("#nodeIDX#", nodeIDX);
        sql.concat(" AND STATUS IN ('").concat(JobProcessNode.STATUS_UNSTART)
           .concat("','").concat(JobProcessNode.STATUS_GOING).concat("')");
        List list = daoUtils.executeSqlQuery(sql);
        return list != null && list.size() > 0;
    }

    /**
     * <li>说明：工长派工-查询流程节点
     * <li>创建人：程锐
     * <li>创建日期：2015-5-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param req HttpServletRequest对象
     * @return List<HashMap>
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> getBaseComboTree(HttpServletRequest req) throws Exception{
        String parentIDX = req.getParameter("parentIDX");// 上级节点IDX
        parentIDX = (StringUtil.isNullOrBlank(parentIDX) || parentIDX.startsWith("ROOT_0")) ? null : parentIDX;
        String queryMaps = req.getParameter("queryParams");
        Map queryParamsMap = JSONUtil.read(queryMaps, Map.class);
        if (!queryParamsMap.isEmpty()) {
            String workPlanIDX = String.valueOf(queryParamsMap.get(WORK_PLAN_IDX));
            String isDispatcher = String.valueOf(queryParamsMap.get("isDispatcher"));
            HttpSession session = req.getSession();
            String tream = null;
            if (session != null && session.getAttribute(TREAM) != null) {
                tream = session.getAttribute(TREAM).toString(); // 获取session中当前登录人员的班组
            }
            String isChecked = StringUtil.nvlTrim(req.getParameter("isChecked"), null);
            return getNodeTree(workPlanIDX, tream, parentIDX, isDispatcher, isChecked);
        }
        return null;
    }

    /**
     * TODO 与getNodeTree方法合并重构
     * <li>说明：工长派工-查询流程节点
     * <li>创建人：程锐
     * <li>创建日期：2015-5-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @param tream 登陆人班组id
     * @param parentIDX 父节点idx
     * @param isDispatcher 是否已派工
     * @param isChecked 有无勾选
     * @return List<HashMap>
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> getNodeTree(String workPlanIDX, String tream,String parentIDX,String isDispatcher, String isChecked){
        String sql = null;
        List <Object []> list = null;
        List<HashMap> children = new ArrayList<HashMap>();
        //是否查询根节点
        if(StringUtil.isNullOrBlank(parentIDX)){
            sql = SqlMapUtil.getSql("jxgc-processNode:getNodeTreeRoot").replaceFirst(SQLPARAMSTR, workPlanIDX.replace("\"", "")).replaceFirst(SQLPARAMSTR, tream);
        } else {
            sql = SqlMapUtil.getSql("jxgc-processNode:getNodeTreeChilds");
            sql = sql.replace(WORK_PLAN_IDX, workPlanIDX).replace("pid", parentIDX).replace(TREAM, tream);
        }
        //已处理
        if(!StringUtil.isNullOrBlank(isDispatcher)&&"y".equals(isDispatcher)){
            sql = sql.replace(SQLSTR, "have_default_person = '1'");
        } 
        //未处理
        else{
            sql = sql.replace(SQLSTR, "have_default_person = '0' or have_default_person is null");
        }
        
        list = daoUtils.executeSqlQuery(sql);
        for(Object [] obj : list){
            HashMap nodeMap = new HashMap();
            nodeMap.put(ID, obj[0]); //idx
            nodeMap.put(WORK_PLAN_IDX, obj[1]); //兑现单主键
            nodeMap.put(NODE_IDX, obj[2]); //工艺节点主键
            nodeMap.put(TEXT, obj[3]); //工艺节点实例名称
            String hasLeaf = StringUtil.isNullOrBlank(String.valueOf(obj[5]))?TRUE:("0".equals(String.valueOf(obj[5]))?TRUE:"false");
            nodeMap.put(LEAF, Boolean.valueOf(hasLeaf)); //是否可以展开下级
            if(!StringUtil.isNullOrBlank(isChecked)){
                nodeMap.put("checked", false);
            }
            children.add(nodeMap);
        }
        return children;
    }

    /**
     * TODO 与getNodeTree方法合并重构
     * <li>说明：工长派工查询-流程节点名称选择控件
     * <li>创建人：程锐
     * <li>创建日期：2015-5-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @param tream 登陆人班组id
     * @param parentIDX 父节点idx
     * @param isDispatcher 是否已派工
     * @return List<HashMap>
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> getNodeTree(String workPlanIDX, String tream,String parentIDX,String isDispatcher){
        String sql = null;
        List <Object []> list = null;
        List<HashMap> children = new ArrayList<HashMap>();
        //是否查询根节点
        if(StringUtil.isNullOrBlank(parentIDX)){
            sql = SqlMapUtil.getSql("jxgc-processNode:getNodeTreeRoot").replaceFirst(SQLPARAMSTR, workPlanIDX.replace("\"", "")).replaceFirst(SQLPARAMSTR, tream);
        } else {
            sql = SqlMapUtil.getSql("jxgc-processNode:getNodeTreeChilds");
            sql = sql.replace(WORK_PLAN_IDX, workPlanIDX).replace("pid", parentIDX).replace(TREAM, tream);
        }
        //已处理
        if(!StringUtil.isNullOrBlank(isDispatcher)&&"y".equals(isDispatcher)){
            sql = sql.replace(SQLSTR, "have_default_person = '1'");
        } 
        //未处理
        else{
            sql = sql.replace(SQLSTR, "have_default_person = '0' or have_default_person is null");
        }
        
        list = daoUtils.executeSqlQuery(sql);
        for(Object [] obj : list){
            HashMap nodeMap = new HashMap();
            nodeMap.put(ID, obj[0]); //idx
            nodeMap.put("rdpIDX", obj[1]); //兑现单主键
            nodeMap.put(NODE_IDX, obj[2]); //工艺节点主键
            nodeMap.put(TEXT, obj[3]); //工艺节点实例名称
            String hasLeaf = StringUtil.isNullOrBlank(String.valueOf(obj[5]))?TRUE:("0".equals(String.valueOf(obj[5]))?TRUE:"false");
            nodeMap.put(LEAF, Boolean.valueOf(hasLeaf)); //是否可以展开下级
            children.add(nodeMap);
        }
        return children;
    }

    /**
     * <li>说明：作业工单查询条件-查询流程节点
     * <li>创建人：程锐
     * <li>创建日期：2015-5-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIdx 父节点IDX
     * @param rdpIdx 作业计划IDX
     * @return List<HashMap>
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> findNodeTreeByWorkQuery(String parentIdx, String rdpIdx) throws BusinessException {
        
        String sql = SqlMapUtil.getSql("jxgc-processNode:workFindNode")
                               .replace("人员ID", SystemContext.getOmEmployee().getEmpid().toString())
                               .replace("兑现单", rdpIdx);
        if (StringUtil.isNullOrBlank(parentIdx) || parentIdx.indexOf("xnode") != -1) {
            sql = sql.replace("父节点", "is null");
        } else {
            sql = sql.replace("父节点", " = '" + parentIdx + "'");
        }
        List<Object[]> list = daoUtils.executeSqlQuery(sql);
        List<HashMap> children = new ArrayList<HashMap>();
        for (Object[] obj : list) {
            HashMap nodeMap = new HashMap();
            nodeMap.put(ID, StringUtil.nvl(obj[0]));
            nodeMap.put(TEXT, StringUtil.nvl(obj[1]));
            nodeMap.put(LEAF, !StringUtil.nvl(obj[2]).equals("0"));
            nodeMap.put("parentIdx", StringUtil.nvl(obj[3]));
            children.add(nodeMap);
        }
        return children;
    } 
    
    /**
     * 
     * <li>说明：得到此节点的所有未完成的前置节点数量
     * <li>创建人：程锐
     * <li>创建日期：2015-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 此节点的所有未完成的前置节点数量
     */
    public int getUnCompletePreNodesForNode(String nodeIDX) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:getUnCompletePreNodesForNode")
                               .replace("#nodeIDX#", nodeIDX);
        return daoUtils.getCountSQL(sql);
    }
    
    /**
     * <li>说明：递归得到节点的所有同级前置节点
     * <li>创建人：程锐
     * <li>创建日期：2015-5-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @param list 前置节点列表
     * @return 节点的所有同级前置节点
     */
    private List<JobProcessNode> getPreNodeList(String nodeIDX, List<JobProcessNode> list) {
        List<JobProcessNode> preNodeList = getPrevNodeList(nodeIDX);
        for (JobProcessNode node : preNodeList) {
            list.add(node);
            list = getPreNodeList(node.getIdx(), list);
        }
        return list;
    }
    
    /**
     * <li>说明：递归得到节点及所有父节点的同级前置节点
     * <li>创建人：程锐
     * <li>创建日期：2015-5-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentList 所有父节点列表
     * @param list 前置节点列表
     * @return 节点及所有父节点的同级前置节点
     */
    private List<JobProcessNode> getAllPreByParentNode(List<JobProcessNode> parentList, List<JobProcessNode> list) {
        for (JobProcessNode parentNode : parentList) {
            list = getPreNodeList(parentNode.getIdx(), list);
        }
        return list;
    }
    
    /**
     * <li>说明：查询流程节点hql
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 查询流程节点hql
     */
    private String getNodeHql(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from JobProcessNode where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return hql.toString();
    }
    
    /**
     * <li>说明：格式化作业节点顺序号显示
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 当前节点的
     * @return 实体界面显示文本
     */
    private String formatDisplayInfo(JobProcessNode entity) {
        List<Integer> list = new ArrayList<Integer>();
        this.listParentsSeqNo(entity, list);
        int length = list.size();
        StringBuilder sb = new StringBuilder();
        for (int i = length - 1; i >= 0; i--) {
            sb.append(list.get(i)).append(".");
        }
        // 节点名称
        sb.append(entity.getNodeName());
        return sb.toString();
    }
    
    /**
     * <li>说明：递归获取作业节点的顺序号列
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 当前节点的【作业节点】
     * @param list 顺序号集合
     */
    private void listParentsSeqNo(JobProcessNode entity, List<Integer> list) {
        list.add(entity.getSeqNo());
        if (null != entity.getParentIDX() && !entity.getParentIDX().equals(ROOT)) {
            listParentsSeqNo(this.getModelById(entity.getParentIDX()), list);
        }
    }
    
    /**
     * <li>说明：选择流程节点树查询
     * <li>创建人：林欢
     * <li>创建日期：2016-3-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX  父节点主键
     * @return  children
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> getChildTree(String parentIDX){
        List<JobProcessNode> list = this.getChildNodeList(parentIDX);
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        for (JobProcessNode t : list) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put(ID, t.getIdx()); // 节点idx主键
            nodeMap.put(TEXT, t.getNodeName()); // 树节点显示名称
//            nodeMap.put(TEXT, formatDisplayInfo(t)); // 树节点显示名称
            nodeMap.put(LEAF, t.getIsLeaf() == 0 ? false : true); // 是否是叶子节点 0:否；1：是
            nodeMap.put("nodeName", t.getNodeName()); // 节点名称
            nodeMap.put("nodeDesc", t.getNodeDesc()); // 节点描述
            nodeMap.put("seqNo", t.getSeqNo()); // 顺序号
            nodeMap.put("status", t.getStatus()); // 状态序号
//            nodeMap.put("disabled", t.getIsLeaf() == 0 ? true : false); // 顺序号
            children.add(nodeMap);
        }
        return children;
    }
    

    /**
     * <li>说明：查询当前工位检修作业节点(子节点)任务的列表
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 流程节点
     * @param vehicleType 客货类型 10 货车 20 客车
     * @return 当前工位检修作业节点任务的列表
     * @throws BusinessException
     */
    public Page<TrainWorkPlanNodeBean> queryNodeListByWorkStation(SearchEntity<JobProcessNode> searchEntity, String vehicleType) throws BusinessException {        
        StringBuilder sb = new StringBuilder("SELECT * FROM (");     
        sb.append(SqlMapUtil.getSql("jxgc-processNode:selectRdpNodeAll"));
        sb.append(") WHERE 0 = 0 ");     
        JobProcessNode bean = searchEntity.getEntity();
        
        // 未处理的检修作业节点，含（未启动、运行中）
        if ("1".equals(bean.getStatus())) {
            sb.append(" AND  STATUS IN ('");
            sb.append(JobProcessNode.STATUS_GOING).append("', '");  // 运行中
            sb.append(JobProcessNode.STATUS_UNSTART);       // 未启动      
            sb.append("')");
        // 已处理的检修作业节点
        } else if ("2".equals(bean.getStatus())) {
            sb.append(" AND STATUS = '").append(JobProcessNode.STATUS_COMPLETE).append("'"); //已完成
        }
        
        // 查询条件 - 工位
        if (!StringUtil.isNullOrBlank(bean.getWorkStationIDX())) {
            sb.append(" AND WORK_STATION_IDX = '").append(bean.getWorkStationIDX()).append("'");
        }
 
        // 查询条件 - 节点名称
        if (!StringUtil.isNullOrBlank(bean.getNodeName())) {
            sb.append(" AND NODE_NAME LIKE '%").append(bean.getNodeName()).append("%'");
        }
        
        // 查询条件 - 客货类型
        if (!StringUtil.isNullOrBlank(vehicleType)) {
            sb.append(" AND T_VEHICLE_TYPE = '").append(vehicleType).append("'");
        }
        
        // 查询条件 - 机车检修作业计划主键
        if (!StringUtil.isNullOrBlank(bean.getWorkPlanIDX())) {
            sb.append(" AND Work_Plan_IDX = '").append(bean.getWorkPlanIDX()).append("'");
        }else{
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("workPlanStatus", Condition.IN_STR.concat(Constants.SINGLE_QUOTE_MARK.concat(TrainWorkPlan.STATUS_HANDLING).concat(Constants.SINGLE_QUOTE_MARK)));
            List<TrainWorkPlan> workPlanList = trainWorkPlanQueryManager.getTrainWorkPlanList(paramMap);
            String inIdxs = "" ;
            for (TrainWorkPlan plan : workPlanList) {
                inIdxs += "'"+plan.getIdx()+"',";
            }
            if(!StringUtil.isNullOrBlank(inIdxs)){
                sb.append(" AND Work_Plan_IDX in (").append(inIdxs.substring(0, inIdxs.length()-1)).append(")");
            }
        }
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("FROM"));
        Page<TrainWorkPlanNodeBean>  page = this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, TrainWorkPlanNodeBean.class);         
//        List<TrainWorkPlanNodeBean> nodeList = page.getList();
//        if(null == nodeList || nodeList.size()<1 )return page;
//        for(TrainWorkPlanNodeBean node: nodeList){
//            //  查询延误类型字典名称
//            String typeId[] = node.getDelayType().split(",");
//            String delayName = "";
//            for(int i=0; i < typeId.length; i++){      
//               EosDictEntry dictEntry=  eosDictEntrySelectManager.getEosDictEntry("JXGC_WORK_SEQ_DELAY",typeId[i]);
//               if(null == dictEntry)continue;
//               delayName += dictEntry.getDictname()+",";
//           }
//           node.setDelayReason(delayName.substring(0, delayName.length()-1)+ " "+ node.getDelayReason());
//        }
        return page;
//        this.queryPageList(totalSql, sb.toString(), searchEntity.getStart(), searchEntity.getLimit(), false, TrainWorkPlanNodeBean.class);        
    } 
    
    /**
     * <li>说明：查询当前工位检修作业节点任务的数量
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 流程节点
     * @return 当前工位检修作业节点任务的数量
     * @throws BusinessException
     */
    public int queryNodeCountByWorkStation(SearchEntity<JobProcessNode> searchEntity,String vehicleType) throws BusinessException{
        return this.queryNodeListByWorkStation(searchEntity,vehicleType).getTotal();
    }
    

}
