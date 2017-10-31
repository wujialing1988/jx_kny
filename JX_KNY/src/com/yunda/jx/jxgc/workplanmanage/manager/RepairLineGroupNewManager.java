package com.yunda.jx.jxgc.workplanmanage.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.dispatchmanage.entity.RepairLine;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation;
import com.yunda.jx.jxgc.dispatchmanage.manager.WorkStationManager;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeRel;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 流水线排程业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-4-29
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service("repairLineGroupNewManager")
public class RepairLineGroupNewManager extends JXBaseManager<JobProcessNode, JobProcessNode> implements IbaseCombo {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    private static int groupId = 0;// 流水线分组id
    
    private static Map<String, List<RepairLine>> repairMap = new HashMap<String, List<RepairLine>>();// 流水线分组map
    
    private static Map<String, List<JobProcessNode>> nodeMap = new HashMap<String, List<JobProcessNode>>();// 流水线分组的流程节点map
    
    private static List<JobProcessNodeRel> seqList = new ArrayList<JobProcessNodeRel>();// 流程节点顺序列表
    
    /** 流程节点查询业务类 */
    @Autowired
    private JobProcessNodeQueryManager jobProcessNodeQueryManager;
    
    /** 流程流程节点实例顺序业务类 */
    @Resource
    private JobProcessNodeRelManager jobProcessNodeRelManager;
    
    /** 流程节点业务类 */
    @Autowired
    private JobProcessNodeManager jobProcessNodeManager;
    
    /** 作业卡业务类 */
    @Resource
    private WorkCardManager workCardManager;
    
    /** 工位业务类 */
    @Resource
    private WorkStationManager workStationManager;
    
    /**
     * <li>说明：作业计划编辑-流水线排程-流水线下拉选择列表
     * <li>创建人：程锐
     * <li>创建日期：2013-10-16
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param req HttpServletRequest对象
     * @param start 开始行
     * @param limit 每页记录数
     * @return 流水线下拉选择列表Map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) {
        Page<RepairLine> page = new Page<RepairLine>();
        String queryParams = req.getParameter("queryParams");
        Map queryParamsMap = null;
        if (!StringUtil.isNullOrBlank(queryParams)) {
            try {
                queryParamsMap = JSONUtil.read(queryParams, Map.class);
            } catch (JsonParseException e) {
                ExceptionUtil.process(e, logger);
            } catch (JsonMappingException e) {
                ExceptionUtil.process(e, logger);
            } catch (IOException e) {
                ExceptionUtil.process(e, logger);
            }
        }
        String groupId = String.valueOf(queryParamsMap.get("groupId"));
        if (!repairMap.isEmpty()) {
            List<RepairLine> repairLineList = repairMap.get(groupId);
            Collections.sort(repairLineList, new Comparator<RepairLine>(){
                public int compare(RepairLine r1, RepairLine r2) {             
                    if (r1.getRepairLineName().compareTo(r2.getRepairLineName()) > 0)
                        return 1;
                    else if (r1.getRepairLineName().compareTo(r2.getRepairLineName()) < 0)
                        return -1;
                    return 0;
                }
            });
            if (repairLineList != null && repairLineList.size() > 0) {
                page.setList(repairLineList);
                page.setTotal(repairLineList.size());
            }
        }
        return page.extjsStore();
    }
    
    /**
     * <li>说明：流水线排程-确定方法
     * <li>创建人：程锐
     * <li>创建日期：2015-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCases 节点实体数组
     * @param workPlanIDX 作业计划IDX
     * @param tecProcessCaseIDX 流程IDX
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateForDispatch(JobProcessNode[] nodeCases, String workPlanIDX, String tecProcessCaseIDX) throws Exception {
        if (nodeCases == null || nodeCases.length < 1)
            throw new BusinessException("无可排程的流程节点");
        for (JobProcessNode node : nodeCases) {
            if (StringUtil.isNullOrBlank(node.getIdxs()))
                throw new BusinessException("流程节点主键为空");
            String[] nodeIDXS = StringUtil.tokenizer(node.getIdxs(), ",");
            for (int i = 0; i < nodeIDXS.length; i++) {
                WorkCard workCard = new WorkCard();
                workCard.setRdpIDX(workPlanIDX);
                workCard.setNodeCaseIDX(nodeIDXS[i]);
                workCard.setRepairLineIDX(node.getRepairLineIDX());
                List nodeList = findWorkStationDispatchList(node.getRepairLineIDX(), nodeIDXS[i]);
                if (nodeList == null || nodeList.size() < 1)
                    continue;
                Object[] objs = (Object[]) nodeList.get(0);
                // 增加对流程节点工位的操作
                JobProcessNode processNode = jobProcessNodeQueryManager.getModelById(nodeIDXS[i]);
                
                
                workCard.setWorkStationIDX(objs[7] != null ? objs[7].toString() : "");
                workCard.setWorkStationCode(objs[8] != null ? objs[8].toString() : "");
                workCard.setWorkStationName(objs[5] != null ? objs[5].toString() : "");
                String oldStation = processNode.getWorkStationIDX();
                String newStation = workCard.getWorkStationIDX();
                Long oldTeam = processNode.getWorkStationBelongTeam();
                processNode.setWorkStationIDX(objs[7] != null ? objs[7].toString() : "");
                processNode.setWorkStationName(objs[5] != null ? objs[5].toString() : "");                 
                
                WorkStation station = workStationManager.getModelById(workCard.getWorkStationIDX());
                if (station != null) {
                    workCard.setWorkStationBelongTeam(station.getTeamOrgId());
                    //增加对流程节点作业班组的操作
                    processNode.setWorkStationBelongTeam(station.getTeamOrgId());
                    processNode.setWorkStationBelongTeamName(station.getTeamOrgName());
                    workCard.setWorkStationBelongTeamName(station.getTeamOrgName());
                    workCard.setWorkStationBelongTeamSeq(station.getTeamOrgSeq());
                }     
                
                Long newTeam = workCard.getWorkStationBelongTeam(); 
                if (workCardManager.getDispatcher().isSameDispatch(oldStation, newStation, oldTeam, newTeam))
                    continue;
                jobProcessNodeQueryManager.saveOrUpdate(processNode);
                if (newTeam == null)//如无默认施修班组，则不派工
                    continue;
                String[] errMsg = workCardManager.getDispatcher().updateWorkCardByNode(workCard);
                if (errMsg != null && errMsg.length > 0)
                    logger.info(errMsg[0]);
            }
        }
        
    }  
    
    /**
     * <li>说明：生成作业计划-默认批量派工
     * <li>创建人：程锐
     * <li>创建日期：2014-6-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修作业计划主键
     * @param processIDX 流程ID
     * @throws Exception
     */
    public void updateForBatchDispatch(String workPlanIDX, String processIDX) throws Exception {
        List nodeList = null;
        nodeList = jobProcessNodeQueryManager.findOnlyOneSameWorkStationNodeList(workPlanIDX);
        if (nodeList == null || nodeList.size() < 1) {
            logger.info("--------------未派工原因：1指定流水线：无关联指定流水线的同一工位的工艺节点分组列表2未指定流水线：无只关联一个工位的同一工位的工艺节点分组列表");
            return;
        }
        for (int i = 0; i < nodeList.size(); i++) {
            Object[] objs = (Object[]) nodeList.get(i);
            String nodeCaseIDXS = objs[0] != null ? objs[0].toString() : "";
            String nodeIDXSStr = CommonUtil.buildInSqlStr(nodeCaseIDXS);
            if (StringUtil.isNullOrBlank(nodeIDXSStr))
                continue;
            WorkCard workCard = new WorkCard();
            workCard.setRdpIDX(workPlanIDX);
            workCard.setNodeCaseIDX(nodeCaseIDXS);
            String workStationIDX = objs[1] != null ? objs[1].toString() : "";
            String workStationName = objs[2] != null ? objs[2].toString() : "";
            workCard.setWorkStationIDX(workStationIDX);
            workCard.setWorkStationName(workStationName);
            workCard.setWorkStationCode(objs[3] != null ? objs[3].toString() : "");
            workCard.setRepairLineIDX(objs[4] != null ? objs[4].toString() : "");
            
            // 增加对流程节点工位的操作
            if (!StringUtil.isNullOrBlank(workStationIDX) && !StringUtil.isNullOrBlank(workStationName))
                logger.info(workStationName + "工位条数：" + jobProcessNodeManager.updateBatchNodeForWorkStation(nodeIDXSStr, workStationIDX, workStationName));
            if (objs[5] == null)
                continue;
            Long teamId = Long.valueOf(objs[5].toString());
            String teamName = objs[6] != null ? objs[6].toString() : "";
            String teamOrgSeq = objs[7] != null ? objs[7].toString() : "";
            workCard.setWorkStationBelongTeam(teamId);
            workCard.setWorkStationBelongTeamName(teamName);
            workCard.setWorkStationBelongTeamSeq(teamOrgSeq);
            
            // 增加对流程节点作业班组的操作            
            if (teamId != null && !StringUtil.isNullOrBlank(teamName))
                logger.info(teamName + "班组条数：" + jobProcessNodeManager.updateBatchNodeForWorkTeam(nodeIDXSStr, teamId, teamName));
            String[] errMsg = workCardManager.getDispatcher().updateWorkCardByNodes(workCard);
            if (errMsg != null && errMsg.length > 0)
                logger.info(errMsg[0]);
            
        }
    }
    
    /**
     * <li>说明：流程节点编辑-如设有工位或班组信息则会对作业工单派工
     * <li>创建人：程锐
     * <li>创建日期：2015-5-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param node 流程节点实体
     * @throws Exception 
     */
    public void updateForDispatch(JobProcessNode node) throws Exception {
        WorkCard workCard = workCardManager.getDispatcher().buildWorkCard(node);        
        if (node.getWorkStationBelongTeam() != null && !StringUtil.isNullOrBlank(node.getWorkStationBelongTeamName())) {
            workCard.setWorkStationBelongTeam(node.getWorkStationBelongTeam());
            workCard.setWorkStationBelongTeamName(node.getWorkStationBelongTeamName());
            String[] errMsg = workCardManager.getDispatcher().updateWorkCardByNode(workCard);
            if (errMsg != null && errMsg.length > 0)
                logger.info(errMsg[0]);
        }
    }
    
    /**
     * <li>说明：作业计划编辑-流水线排程-流水线选择列表
     * <li>创建人：程锐
     * <li>创建日期：2013-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点IDX
     * @param type 前台传递的类型：1 parentIDX为父节点id，0 parentIDX为机车检修作业计划id
     * @return 流水线选择分页列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Page getRepairInfoForChild(String parentIDX, String type) throws Exception{
        init(); // 初始化数据
        fillNodeMap(type, parentIDX);// 填充repairMap、nodeMap、groupId数据   
        return getGroupInfoPage(); // 根据repairMap、nodeMap、groupId构建分组列表分页对象
    }
    
    /**
     * <li>说明：流水线排程-根据流程节点id查询列表
     * <li>创建人：程锐
     * <li>创建日期：2015-1-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param repairLineIDX 流水线IDX
     * @param nodeIDX 节点IDX
     * @return 列表
     * [{
     *   0 流程节点实例idx
     *   1 流程节点idx
     *   2 流程节点实例名称
     *   3 流程实例名称
     *   4 流水线名称
     *   5 工位名称
     *   6 作业班组名称
     *   7 工位idx
     *   8 工位编码
     *   9 流程节点实例状态
     *   10流程节点实例-额定工期
     *   11流程节点实例-计划开始时间
     *   12流程节点实例-计划完成时间
     *   13流程节点实例-实际开始时间
     *   14流程节点实例-实际完成时间
     *   15流水线idx
     * }]
     */
    private List findWorkStationDispatchList(String repairLineIDX, String nodeIDX) {
        String selectSql = SqlMapUtil.getSql("jxgc-repairlineNew:findWorkStationDispatchList-select");
        String fromSql = SqlMapUtil.getSql("jxgc-repairlineNew:findWorkStationDispatchList-from")
                                   .replace("#repairLineIDX#", repairLineIDX)
                                   .replace("#nodeCaseIDX#", nodeIDX);
        String sql = selectSql + " " + fromSql;
        return daoUtils.executeSqlQuery(sql);
    }
    
    /**
     * <li>说明：初始化
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     */
    private void init() {
        repairMap.clear();
        nodeMap.clear();
        groupId = 0;
        seqList = new ArrayList<JobProcessNodeRel>();
    }
    
    /**
     * <li>说明：填充repairMap、nodeMap、groupId数据
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param type 前台传递的类型：1 parentIDX为父节点id，0 parentIDX为机车检修作业计划id
     * @param parentIDX 父节点idx
     * @throws Exception
     */
    private void fillNodeMap(String type, String parentIDX) throws Exception {
        if (!StringUtil.isNullOrBlank(type) && "1".equals(type))
            fillMapForChildNode(parentIDX);// parentIDX为父节点id
        if (!StringUtil.isNullOrBlank(type) && "0".equals(type))
            fillMapForWorkPlanChildNode(parentIDX);// parentIDX为机车检修作业计划id,查找机车检修作业计划（流程实例）下第一级
    }
    
    /**
     * <li>说明：根据repairMap、nodeMap、groupId构建分组列表分页对象
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 分组列表分页对象
     */
    @SuppressWarnings("unchecked")
    private Page getGroupInfoPage() throws Exception {
        Page<JobProcessNode> page = new Page<JobProcessNode>();
        List<JobProcessNode> nodeCaseList = getNodeListByRepairMap();
        page.setList(nodeCaseList);
        page.setTotal(nodeCaseList.size());
        return page;
    }
    
    /**
     * <li>说明：填充流程实例下节点map
     * <li>创建人：程锐
     * <li>创建日期：2013-10-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修作业计划ID
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void fillMapForWorkPlanChildNode(String workPlanIDX) throws Exception{
        List<JobProcessNode> firstChildList = jobProcessNodeQueryManager.firstWorkPlanChildList(workPlanIDX);// 同一机车检修作业计划下第一级节点列表
        fillMapForNode(firstChildList); 
    }

    /**
     * <li>说明：递归填充父节点map
     * <li>创建人：程锐
     * <li>创建日期：2013-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点ID
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void fillMapForChildNode(String parentIDX) throws Exception{ 
        List<JobProcessNode> firstChildList = jobProcessNodeQueryManager.firstChildList(parentIDX);// 获取下级第一顺序节点       
        fillMapForNode(firstChildList); 
    }
    
    /**
     * <li>说明：填充节点map
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param firstChildList 节点列表
     * @throws Exception
     */
    private void fillMapForNode(List<JobProcessNode> firstChildList) throws Exception {
        if (firstChildList == null || firstChildList.size() < 1)
            return;
        for (JobProcessNode nodeCase : firstChildList) {            
            if (isChildNode(nodeCase))// 本节点为子节点则查找后置节点并填充map
                fillThisAndNextNodeMap(nodeCase);           
            else// 本节点为父节点则查找下级节点填充map
                fillMapForChildNode(nodeCase.getIdx());
                
        }
    }
    
    /**
     * <li>说明：是否是子节点
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 节点实体
     * @return true 是子节点 false 父节点
     */
    private boolean isChildNode(JobProcessNode nodeCase) {
        return nodeCase.getIsLeaf() == JobProcessNodeDef.CONST_INT_IS_LEAF_YES;
    }
    
    /**
     * <li>说明：填充后置节点Map
     * <li>创建人：程锐
     * <li>创建日期：2013-10-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 节点实体
     * @throws Exception
     */
    private void fillThisAndNextNodeMap(JobProcessNode nodeCase) throws Exception{
        fillThisNodeMap(nodeCase);
        fillNextNodeMap(nodeCase);
    }
    
    /**
     * <li>说明：填充本节点Map
     * <li>创建人：程锐
     * <li>创建日期：2013-10-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 节点实体
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void fillThisNodeMap(JobProcessNode nodeCase) throws Exception{
        List<RepairLine> repairLineListByThisNode = getRepairLineListByThisNode(nodeCase);// 本节点关联的流水线列表
        if (repairLineListByThisNode != null && repairLineListByThisNode.size() > 0) {
            boolean hasGroupNode = false;// nodeMap是否已包含此节点对象
            // 1.nodeMap已包含此节点对象
            if (!nodeMap.isEmpty()) {
                List<JobProcessNode> preNodeList = jobProcessNodeQueryManager.getPrevNodeList(nodeCase);// 本节点的前置节点列表
                List<JobProcessNode> nextNodeList = jobProcessNodeQueryManager.getNextNodeList(nodeCase);// 本节点的后续节点列表

                Set<Map.Entry<String, List<JobProcessNode>>> set = nodeMap.entrySet();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, List<JobProcessNode>> entry = (Map.Entry<String, List<JobProcessNode>>) it.next();
                    List<JobProcessNode> oldNodeList = entry.getValue();
                    String oldGroupId = entry.getKey();// 已存在节点分组id
                    // 如已存在此节点
                    if (oldNodeList.contains(nodeCase)) {
                        hasGroupNode = true;
                        if (siftNodeByPrevNode(nodeCase, repairLineListByThisNode, oldGroupId, oldNodeList))
                            return;
                        return;
                    }
                    // 和并行节点比较是否加入分组,如流水线相同则加入并行节点分组
                    for (JobProcessNode oldNode : oldNodeList) {
                        if (isSameRepairWithSameTimeNode(oldNode, nextNodeList, preNodeList, oldGroupId, repairLineListByThisNode, nodeCase)) {
                            oldNodeList.add(nodeCase);
                            nodeMap.put(oldGroupId, oldNodeList);
                            return;
                        }
                    }
                }
            }

            // 2.nodeMap不包含此节点对象
            if (!hasGroupNode && !repairMap.isEmpty() && siftNodeByPrevNode(nodeCase, repairLineListByThisNode, null, null)) {
                return;
            }
            // 3.上面两种情况都不存在，则新增分组并填充至map
            fillNewGroupToMap(repairLineListByThisNode, nodeCase);
        }
        return;
    }
    
    /**
     * <li>说明：是否并行节点且两个节点关联的流水线列表也相同
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param oldNode 已存在的节点
     * @param nextNodeList 本节点的后续节点列表
     * @param preNodeList 本节点的前置节点列表
     * @param oldGroupId 已存在的节点分组id
     * @param repairLineListByThisNode 本节点关联的流水线列表
     * @param nodeCase 本节点对象
     * @return 布尔值： true 是并行节点且两个节点关联的流水线列表也相同
     */
    private boolean isSameRepairWithSameTimeNode(JobProcessNode oldNode, 
                                                 List<JobProcessNode> nextNodeList,
                                                 List<JobProcessNode> preNodeList, 
                                                 String oldGroupId, 
                                                 List<RepairLine> repairLineListByThisNode, 
                                                 JobProcessNode nodeCase) {
        if (CommonUtil.isEqualsByTwoStr(oldNode.getParentIDX(), nodeCase.getParentIDX())) {
            List<JobProcessNode> preOldNodeList = jobProcessNodeQueryManager.getPrevNodeList(oldNode);// 已有节点的前置节点列表
            List<JobProcessNode> nextOldNodeList = jobProcessNodeQueryManager.getNextNodeList(oldNode);// 已有节点的后续节点列表
            if ((preOldNodeList.equals(preNodeList) && nextOldNodeList.equals(nextNodeList))
                    && repairLineListByThisNode.equals(repairMap.get(oldGroupId)))
                return true;
        }
        return false;
    }
    
    
    /**
     * <li>说明：填充后置节点map
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 节点实体
     * @throws Exception
     */
    private void fillNextNodeMap(JobProcessNode nodeCase) throws Exception {
        List<JobProcessNode> nextNodeList = jobProcessNodeQueryManager.getNextNodeList(nodeCase);// 同一父节点下的后续节点列表
        if (nextNodeList != null && nextNodeList.size() > 0) {
            for (JobProcessNode nextNodeCase : nextNodeList) {
                JobProcessNodeRel seq = new JobProcessNodeRel();
                seq.setNodeIDX(nextNodeCase.getIdx());
                seq.setPreNodeIDX(nodeCase.getIdx());
                if (!jobProcessNodeRelManager.containTecNodeCaseSequence(seqList, seq)) {
                    seqList.add(seq);
                    if (isChildNode(nextNodeCase))
                        fillThisAndNextNodeMap(nextNodeCase);
                    else
                        fillMapForChildNode(nextNodeCase.getIdx());
                }
            }
        }
    }
    
    /**
     * <li>说明：新增分组并填充至map
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param repairLineListByThisNode 本节点关联的流水线列表
     * @param nodeCase 本节点实例对象
     */
    private void fillNewGroupToMap(List<RepairLine> repairLineListByThisNode, JobProcessNode nodeCase) {
        groupId++;
        repairMap.put(String.valueOf(groupId), repairLineListByThisNode);// 填充repairMap
        List<JobProcessNode> nodeList = new ArrayList<JobProcessNode>();
        nodeList.add(nodeCase);
        nodeMap.put(String.valueOf(groupId), nodeList);// 填充nodeMap
    }
    
    /**
     * <li>说明：获取本节点关联的流水线列表
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 节点实体
     * @return 本节点关联的流水线列表
     */
    @SuppressWarnings("unchecked")
    private List<RepairLine> getRepairLineListByThisNode(JobProcessNode nodeCase) {
        String hql = SqlMapUtil.getSql("jxgc-repairlineNew:repairListForNodeCase").replace("#nodeCaseIDX#", nodeCase.getIdx());
        return daoUtils.find(hql);
    }
    
    /**
     * <li>说明：根据前置节点筛选节点
     * <li>创建人：程锐
     * <li>创建日期：2013-10-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 本节点对象
     * @param repairLineList 本节点关联的流水线列表
     * @param oldGroupId 已包含本节点对象的节点列表分组id
     * @param oldNodeList 已包含本节点对象的节点列表
     * @return 如将本节点加入已有nodeMap则返回true,如未加入则返回false
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private boolean siftNodeByPrevNode(JobProcessNode nodeCase, 
                                      List<RepairLine> repairLineList,
                                      String oldGroupId, 
                                      List<JobProcessNode> oldNodeList) throws Exception{
        // 找前置节点的分组1.前置节点不为null，2前置节点为null则找其同一父节点下前置节点也为null的其他节点
        List<JobProcessNode> prevNodeList = jobProcessNodeQueryManager.getPrevNodeList(nodeCase);
        // 1.前置节点不为null
//      if (prevNodeList != null && prevNodeList.size() > 0) {
//          for (JobProcessNode prevNodeCase : prevNodeList) {
//              if (siftNodeByLevel(prevNodeCase, nodeCase, repairLineList, oldGroupId, oldNodeList))
//                  return true;
//          }
//      }
        //此处判断可能有bug，如出错用上面注释的代码替换
        if (isJoinOldNodeMap(prevNodeList, oldNodeList, repairLineList, nodeCase, oldGroupId)) {
            return true;
        }
        // 2.前置节点为null则先找其父节点的前置节点,如前置节点非子节点，则查找前置节点下属节点的最后子节点进行；
        // 如未找到分组则找其同一父节点下前置节点也为null的其他节点作为前置节点,如此前置节点非子节点，则查找前置节点下属节点的最后子节点进行
        else {
            List<JobProcessNode> prevParentNodeList = getParentPrevNodeList(nodeCase);// 获取父节点的前置节点
            if (isJoinOldNodeMap(prevParentNodeList, oldNodeList, repairLineList, nodeCase, oldGroupId)) {
                return true;
            }
            List<JobProcessNode> sameLevelNodeList = getSameLevelNodeList(nodeCase);// 获取其同一父节点下前置节点也为null的其他节点
            if (isJoinOldNodeMap(sameLevelNodeList, oldNodeList, repairLineList, nodeCase, oldGroupId)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * <li>说明：将本节点加入已有nodeMap
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param prevNodeList 前置节点列表
     * @param oldNodeList 已包含本节点对象的节点列表
     * @param repairLineList 本节点关联的流水线列表
     * @param nodeCase 本节点实例对象
     * @param oldGroupId 已包含本节点对象的节点列表分组id
     * @return 如将本节点加入已有nodeMap则返回true,如未加入则返回false
     * @throws Exception
     */
    private boolean isJoinOldNodeMap(List<JobProcessNode> prevNodeList, 
                                     List<JobProcessNode> oldNodeList, 
                                     List<RepairLine> repairLineList,
                                     JobProcessNode nodeCase, 
                                     String oldGroupId) throws Exception {
        if (prevNodeList != null && prevNodeList.size() > 0) {
            for (JobProcessNode prevNodeCase : prevNodeList) {
                if (siftNodeByLevel(prevNodeCase, nodeCase, repairLineList, oldNodeList, oldGroupId))
                    return true;
            }
        }
        return false;
    }
    
    /**
     * <li>说明：获取父节点的前置节点列表
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 节点对象
     * @return 父节点的前置节点列表
     */
    @SuppressWarnings("unchecked")
    private List<JobProcessNode> getParentPrevNodeList(JobProcessNode nodeCase) {
        String hql = SqlMapUtil.getSql("jxgc-processNode:getParentPrevNodeList").replace("#nodeCaseIDX#", nodeCase.getIdx());
        return daoUtils.find(hql);// 获取父节点的前置节点
    }
    
    /**
     * <li>说明：获取其同一父节点下前置节点也为null的其他节点列表
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 节点实体
     * @return 与本节点同一父节点下前置节点也为null的其他节点列表
     */
    @SuppressWarnings("unchecked")
    private List<JobProcessNode> getSameLevelNodeList(JobProcessNode nodeCase) {
        String replaceStr = " ";
        if (StringUtil.isNullOrBlank(nodeCase.getParentIDX())) {
            replaceStr = " parentIDX is null ";
        } else {
            replaceStr = " parentIDX = '" + nodeCase.getParentIDX() + "'";
        }
        String hql = SqlMapUtil.getSql("jxgc-processNode:getFirstChildNodeList").replace("#replaceStr#", replaceStr);
        hql += " and idx != '" + nodeCase.getIdx() + "'";
        return daoUtils.find(hql);// 获取其同一父节点下前置节点也为null的其他节点
    }
    
    /**
     * <li>说明：根据前置节点是否有下级节点（level）筛选节点
     * <li>创建人：程锐
     * <li>创建日期：2013-10-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param prevNodeCase 前置节点实体对象
     * @param nodeCase 本节点对象
     * @param repairLineList 本节点关联的流水线列表
     * @param oldGroupId 已包含本节点对象的节点列表分组id
     * @param oldNodeList 已包含本节点对象的节点列表
     * @return 如将本节点加入已有nodeMap则返回true,如未加入则返回false
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private boolean siftNodeByLevel(JobProcessNode prevNodeCase,
                                    JobProcessNode nodeCase, 
                                    List<RepairLine> repairLineList, 
                                    List<JobProcessNode> oldNodeList,
                                    String oldGroupId) throws Exception{
        // 前置节点非子节点，则查找前置节点下属节点的最后子节点列表
        if (!isChildNode(prevNodeCase)) {
            List prevLastChildNodeList = getLastChildNodeList(prevNodeCase);
            if (prevLastChildNodeList != null && prevLastChildNodeList.size() > 0) {
                for (int i = 0; i < prevLastChildNodeList.size(); i++) {
                    Object[] objs = (Object[]) prevLastChildNodeList.get(i);
                    JobProcessNode prevLastChildNodeCase = getPrevLastChildNodeCase(objs);
                    if (siftNode(prevLastChildNodeCase, nodeCase, repairLineList, oldGroupId, oldNodeList))
                        return true;
                }
            }
        }
        // 前置节点为子节点
        else if (siftNode(prevNodeCase, nodeCase, repairLineList, oldGroupId, oldNodeList))
            return true;
        return false;
    }
    
    /**
     * <li>说明：获取前置节点下属节点的最后子节点对象
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param objs 节点实体数组 objs[0]为节点IDX
     * @return 前置节点下属节点的最后子节点对象
     */
    private JobProcessNode getPrevLastChildNodeCase(Object[] objs) {
        String idx = objs[0] != null ? objs[0].toString() : "";
        return getModelById(idx);
    }
    
    /**
     * <li>说明：查找前置节点下属节点的最后子节点列表
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param prevNodeCase 前置节点实体对象
     * @return 前置节点下属节点的最后子节点列表
     */
    private List getLastChildNodeList(JobProcessNode prevNodeCase) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:getLastChildNodeList").replace("#parentIDX#", prevNodeCase.getIdx());
        return daoUtils.executeSqlQuery(sql);
    }
    
    /**
     * <li>说明：筛选节点
     * <li>创建人：程锐
     * <li>创建日期：2013-10-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param prevNodeCase 前置节点实体对象
     * @param nodeCase 本节点对象
     * @param repairLineList 本节点关联的流水线列表
     * @param oldGroupId 已包含本节点对象的节点列表分组id
     * @param oldNodeList 已包含本节点对象的节点列表
     * @return 如将本节点加入已有nodeMap则返回true,如未加入则返回false
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private boolean siftNode(JobProcessNode prevNodeCase, 
                             JobProcessNode nodeCase, 
                             List<RepairLine> repairLineList,
                             String oldGroupId,
                             List<JobProcessNode> oldNodeList) throws Exception{
        if (prevNodeCase == null) 
            return false;
        List<RepairLine> prevRepairLineList = getRepairLineListByThisNode(prevNodeCase);//前置节点关联的流水线列表
        //前置节点关联的流水线列表与本节点关联的流水线列表相同：如前置节点流水线分组id与已存在节点分组id不同，则从已存在节点列表删除已存在节点并将本节点增加至前置节点流水线分组id关联的节点列表中
        if (prevRepairLineList.equals(repairLineList) && !nodeMap.isEmpty()) {
            Set<Map.Entry<String, List<JobProcessNode>>> set = nodeMap.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<JobProcessNode>> entry = (Map.Entry<String, List<JobProcessNode>>) it.next();
                List<JobProcessNode> nodeList = entry.getValue();
                //找到前置节点对应的分组
                if (nodeList.contains(prevNodeCase)) {
                    String repairGroupId = entry.getKey();
                    //已存在节点分组id、前置节点流水线分组id关联的节点列表不为空((nodeMap中存在包含本节点的情况))；前置节点流水线分组id与已存在节点分组id不同
                    /// nodeMap中包含本节点且前置节点流水线分组id与已存在节点分组id不同
                    if (isNodeMapContainsNode(oldGroupId, oldNodeList, repairGroupId)) {
                        delOldNodeGroupFromNodeMap(nodeCase, oldNodeList, oldGroupId);// 从已存在节点列表删除本节点，同时重新填充已存在节点列表
                        joinNodeMap(nodeList, nodeCase, repairGroupId);// 将本节点增加至前置节点流水线分组id关联的节点列表中
                        return true;
                    }
                    // nodeMap中不存在包含本节点的情况
                    else if (isNotNodeMapContainsNode(oldGroupId, oldNodeList)) {
                        joinNodeMap(nodeList, nodeCase, repairGroupId);// 将本节点增加至前置节点流水线分组id关联的节点列表中
                        return true;
                    }
                }
            }       
        }
        return false;
    }
    
    /**
     * <li>说明：是否存在"nodeMap中包含本节点且前置节点流水线分组id与已存在节点分组id不同"的情况
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param oldGroupId 已包含本节点对象的节点列表分组id
     * @param oldNodeList 已包含本节点对象的节点列表
     * @param repairGroupId 前置节点流水线分组id
     * @return 布尔值 ：true nodeMap中存在包含本节点的情况且前置节点流水线分组id与已存在节点分组id不同，反之 false
     */
    private boolean isNodeMapContainsNode(String oldGroupId, List<JobProcessNode> oldNodeList, String repairGroupId) {
        // 已存在节点分组id、前置节点流水线分组id关联的节点列表不为空((nodeMap中包含本节点))；前置节点流水线分组id与已存在节点分组id不同
        if (!StringUtil.isNullOrBlank(oldGroupId) && (oldNodeList != null && oldNodeList.size() > 0) && !repairGroupId.equals(oldGroupId))
            return true;
        return false;
    }
    
    /**
     * <li>说明：是否存在"nodeMap中不包含本节点的情况"
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param oldGroupId 已包含本节点对象的节点列表分组id
     * @param oldNodeList 已包含本节点对象的节点列表
     * @return 布尔值 ：true nodeMap中不存在包含本节点的情况，反之false
     */
    private boolean isNotNodeMapContainsNode(String oldGroupId, List<JobProcessNode> oldNodeList) {
        // 已存在节点分组id、前置节点流水线分组id关联的节点列表为空
        if (StringUtil.isNullOrBlank(oldGroupId) || (oldNodeList == null || oldNodeList.size() < 1))
            return true;
        return false;
    }
    
    /**
     * <li>说明：加入nodeMap
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeList 流程节点列表
     * @param nodeCase 流程节点对象
     * @param repairGroupId 流程节点列表对应的groupId
     */
    private void joinNodeMap(List<JobProcessNode> nodeList, JobProcessNode nodeCase, String repairGroupId) {
        nodeList.add(nodeCase);
        nodeMap.put(repairGroupId, nodeList);
    }

    /**
     * <li>说明：从已存在节点列表删除本节点，同时重新填充已存在节点列表：如已存在节点列表为空则删除nodeMap和repairMap的对应映射关系
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 节点对象
     * @param oldNodeList 已包含本节点对象的节点列表
     * @param oldGroupId 已包含本节点对象的节点列表分组id
     */
    private void delOldNodeGroupFromNodeMap(JobProcessNode nodeCase, List<JobProcessNode> oldNodeList, String oldGroupId) {
        oldNodeList.remove(nodeCase);// 从已存在节点列表删除已存在节点
        // 如已存在节点列表为空则删除nodeMap和repairMap的对应映射关系
        if (oldNodeList.size() < 1) {
            nodeMap.remove(oldGroupId);
            repairMap.remove(oldGroupId);
        } else {
            nodeMap.put(oldGroupId, oldNodeList);
        }
    }
    
    /**
     * <li>说明：从repairMap中获取节点列表
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 节点列表
     */
    @SuppressWarnings("unchecked")
    private List<JobProcessNode> getNodeListByRepairMap() {
        List<JobProcessNode> nodeCaseList = new ArrayList<JobProcessNode>();
        if (!repairMap.isEmpty()) {
            Set<Map.Entry<String, List<RepairLine>>> set = repairMap.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<RepairLine>> entry = (Map.Entry<String, List<RepairLine>>) it.next();
                List<RepairLine> repairLineList = entry.getValue();// 流水线列表
                String repairGroupId = entry.getKey();// 前置节点流水线分组id
                JobProcessNode nodeCase = getNodeInfoByRepairMap(repairGroupId, repairLineList);
                nodeCase = getNodeInfoByNodeMap(repairGroupId, nodeCase);
                nodeCaseList.add(nodeCase);
            }
        }
        return nodeCaseList;
    }
    
    /**
     * <li>说明：从repairMap中获取nodeCase所需数据
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param repairGroupId 流水线分组Id
     * @param repairLineList 流水线分组Id对应的流水线列表
     * @return nodeCase json实体对象 { idx："", groupId: "", repairLineIDX: "", repairLineName: "" }
     */
    private JobProcessNode getNodeInfoByRepairMap(String repairGroupId, List<RepairLine> repairLineList) {
        JobProcessNode nodeCase = new JobProcessNode();
        nodeCase.setIdx(repairGroupId);
        nodeCase.setGroupId(repairGroupId);
        if (repairLineList != null && repairLineList.size() > 0) {
            nodeCase.setRepairLineIDX(repairLineList.get(0).getIdx());
            nodeCase.setRepairLineName(repairLineList.get(0).getRepairLineName());
        }
        return nodeCase;
    }

    /**
     * <li>说明：从nodeMap中获取nodeCase所需数据
     * <li>创建人：程锐
     * <li>创建日期：2014-4-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param repairGroupId 流水线分组Id
     * @param nodeCase json实体对象 { idx："", groupId: "", repairLineIDX: "", repairLineName: "" }
     * @return nodeCase json实体对象 { idx："", groupId: "", repairLineIDX: "", repairLineName: "" nodeCaseName: "", idxs: "" }
     */
    private JobProcessNode getNodeInfoByNodeMap(String repairGroupId, JobProcessNode nodeCase) {
        List<JobProcessNode> nodeList = nodeMap.get(repairGroupId);
        if (nodeList != null && nodeList.size() > 0) {
            StringBuilder nodeNames = new StringBuilder();
            StringBuilder nodeIDXS = new StringBuilder();
            for (JobProcessNode node : nodeList) {
                nodeNames.append(node.getNodeName()).append(",");
                nodeIDXS.append(node.getIdx()).append(",");
            }
            nodeNames.deleteCharAt(nodeNames.length() - 1);
            nodeIDXS.deleteCharAt(nodeIDXS.length() - 1);
            nodeCase.setNodeName(nodeNames.toString());
            nodeCase.setIdxs(nodeIDXS.toString());
        }
        return nodeCase;
    }
}
