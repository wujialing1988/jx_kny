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
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.dispatchmanage.entity.RepairLine;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation;
import com.yunda.jx.jxgc.dispatchmanage.manager.WorkStationManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 流水线排程业务类
 * <li>创建人：程锐
 * <li>创建日期：2016-6-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：程锐
 * <li>创建日期：2016-6-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@Service("repairLineGroupManager")
public class RepairLineGroupManager extends JXBaseManager<JobProcessNode, JobProcessNode> implements IbaseCombo {

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    private static int groupId = 0;// 流水线分组id
    
    private static Map<String, List<RepairLine>> repairMap = new HashMap<String, List<RepairLine>>();// 流水线分组map
    
    private static Map<String, List<JobProcessNode>> nodeMap = new HashMap<String, List<JobProcessNode>>();// 流水线分组的流程节点map
        
    /** 流程节点查询业务类 */
    @Autowired
    private JobProcessNodeQueryManager jobProcessNodeQueryManager;
    
    /** 作业卡业务类 */
    @Resource
    private WorkCardManager workCardManager;
    
    /** 工位业务类 */
    @Resource
    private WorkStationManager workStationManager;    

    /**
     * <li>说明：作业计划编辑-流水线排程-流水线下拉选择列表
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
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
     * <li>说明：作业计划编辑-流水线排程-流水线选择列表
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
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
     * <li>说明：流水线排程-确定方法
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
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
     * <li>说明：流水线排程-根据流程节点id查询列表
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
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
     * <li>创建日期：2016-6-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     */
    private void init() {
        repairMap.clear();
        nodeMap.clear();
        groupId = 0;
    }
    
    /**
     * <li>说明：填充repairMap、nodeMap、groupId数据
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
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
     * <li>说明：填充流程实例下所有子节点map
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修作业计划ID
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void fillMapForWorkPlanChildNode(String workPlanIDX) throws Exception{
        List<JobProcessNode> childList = jobProcessNodeQueryManager.getLeafNodeListByWorkPlan(workPlanIDX);// 同一机车检修作业计划下所有子节点列表
        fillMapForNode(childList); 
    }

    /**
     * <li>说明:填充父节点下所有子节点map
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点ID
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void fillMapForChildNode(String parentIDX) throws Exception{ 
        List<JobProcessNode> childList = jobProcessNodeQueryManager.getAllChildLeafNodeExceptThisList(parentIDX);// 获取下级所有子节点       
        fillMapForNode(childList); 
    }
    
    /**
     * <li>说明：将流水线列表相同的节点放入map中同一个key的list
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param childList 子节点列表
     */
    @SuppressWarnings("unchecked")
    private void fillMapForNode(List<JobProcessNode> childList) {
        if (childList == null || childList.size() < 1)
            return;
        for (JobProcessNode node : childList) {
            boolean hasThisRepairLine = false;
            List<RepairLine> repairLineListByThisNode = getRepairLineListByThisNode(node);// 本节点关联的流水线列表
            if (!repairMap.isEmpty()) {
                hasThisRepairLine = hasThisRepairLine(repairLineListByThisNode, node);
            }
            if (!hasThisRepairLine) {
                groupId++;            
                repairMap.put(String.valueOf(groupId), repairLineListByThisNode);// 填充repairMap
                List<JobProcessNode> nodeList = new ArrayList<JobProcessNode>();
                nodeList.add(node);
                nodeMap.put(String.valueOf(groupId), nodeList);// 填充nodeMap
            }
        }
    }
    
    /**
     * <li>说明：判断repairMap中是否已包含相同流水线列表
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param repairLineListByThisNode 此节点关联的流水线列表 
     * @param node 节点
     * @return true repairMap中已包含相同流水线列表 false 未包含
     */
    @SuppressWarnings("unchecked")
    private boolean hasThisRepairLine(List<RepairLine> repairLineListByThisNode, JobProcessNode node) {
        Set<Map.Entry<String, List<RepairLine>>> set = repairMap.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<RepairLine>> entry = (Map.Entry<String, List<RepairLine>>) it.next();
            List<RepairLine> repairLineList = entry.getValue();
            String oldGroupId = entry.getKey();// 已存在节点分组id
            if (repairLineList.equals(repairLineListByThisNode)) {
                List<JobProcessNode> nodeList = nodeMap.get(oldGroupId);
                if (nodeList != null && nodeList.size() > 0) {
                    nodeList.add(node);
                    nodeMap.put(oldGroupId, nodeList);
                    return true;
                }  
            }
        } 
        return false;
    }
    
    /**
     * <li>说明：获取本节点关联的流水线列表
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
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
     * <li>说明：根据repairMap、nodeMap、groupId构建分组列表分页对象
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
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
     * <li>说明：从repairMap中获取节点列表
     * <li>创建人：程锐
     * <li>创建日期：2016-6-14
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
     * <li>创建日期：2016-6-14
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
     * <li>创建日期：2016-6-14
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
