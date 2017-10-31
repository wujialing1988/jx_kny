package com.yunda.jx.jxgc.workplanmanage.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarDetailManager;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarInfoUtil;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeRel;
import com.yunda.jx.jxgc.workplanmanage.entity.NodeBean;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.util.CalcWorkDateUtil;
import com.yunda.jx.util.PerformanceMonitor;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 计算流程节点计划开完工时间、工期等逻辑的业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-4-28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "calcNodeWorkDateNewManager")
public class CalcNodeWorkDateNewManager extends JXBaseManager<JobProcessNode, JobProcessNode> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 流程节点查询业务类 */
    @Autowired
    private JobProcessNodeQueryManager jobProcessNodeQueryManager;
    
    /** 机车作业计划查询业务类 */
    @Autowired
    private TrainWorkPlanQueryManager trainWorkPlanQueryManager;
    
    private Map<String, NodeBean> nodeBeanMap = new HashMap<String, NodeBean>();
    
    private Map<String, List<JobProcessNodeRel>> preNodeMap = new HashMap<String, List<JobProcessNodeRel>>();
    private Map<String, List<JobProcessNodeRel>> nextNodeMap = new HashMap<String, List<JobProcessNodeRel>>();
    
//    private static String workPlanCalendarIDX = "";   
    
    private static boolean isEditOrInit = false;//是否初始化生成作业计划或编辑作业计划， true 是， false 否
    
    /**
     * <li>说明：构造节点Map
     * <li>创建人：程锐
     * <li>创建日期：2015-5-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    public void buildNodeMap(String workPlanIDX) throws IllegalAccessException, InvocationTargetException {  
        preNodeMap.clear();
        nextNodeMap.clear();
        nodeBeanMap.clear();
        
        buildPreAndNextMap(workPlanIDX);
        
        List<JobProcessNode> nodeList = jobProcessNodeQueryManager.getNodeListByWorkPlan(workPlanIDX);
        Map<String, JobProcessNode> nodeMap = buildNodeMap(nodeList);
        
        for (JobProcessNode node : nodeList) {
            String nodeIDX = node.getIdx();
            NodeBean nodeBean = new NodeBean();
            BeanUtils.copyProperties(nodeBean, node);
            nodeBean.setChanged(false);
            nodeBean.setHasCal(false);
            nodeBean.setThisChange(false);
            nodeBean = buildNextNodeIDXS(node, nodeBean);
            nodeBean = buildPreNodeIDXS(node, nodeBean);
            nodeBean = buildChildNodeIDX(node, nodeBean, nodeMap);            
            nodeBeanMap.put(nodeIDX, nodeBean);
        }
        TrainWorkPlan workPlan = trainWorkPlanQueryManager.getModelById(workPlanIDX);
        if (workPlan != null)
            CalcWorkDateUtil.workPlanCalendarIDX = workPlan.getWorkCalendarIDX();
        
        WorkCalendarInfoUtil wcInfoUtil = WorkCalendarInfoUtil.getInstance(workPlan.getPlanBeginTime());
        wcInfoUtil.buildMap();
    }
    
    /**
     * <li>说明：更新作业计划的计划时间，级联更新所有关联流程节点的计划时间
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 机车作业计划实体
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updatePlanTimeByWorkPlan(TrainWorkPlan workPlan) throws Exception {
        if (workPlan.getPlanBeginTime() == null)
            return;
        List<JobProcessNode> nodeList = jobProcessNodeQueryManager.getFirstNodeListByWorkPlan(workPlan.getIdx());
        if (nodeList == null || nodeList.size() < 1)
            return;
        buildNodeMap(workPlan.getIdx());
        for (JobProcessNode node : nodeList) {
            node.setPlanBeginTime(workPlan.getPlanBeginTime());// 对选定节点设置计划开始时间
            updatePlanBeginEndTime(node, true, false);
        }
    }
    
    /**
     * <li>说明：更新作业计划的计划时间，级联更新所有关联流程节点的计划时间(生成作业计划及编辑作业计划调用)
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 机车作业计划实体
     * @param isEdit 是否初始化生成作业计划或编辑作业计划， true 是， false 否
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updatePlanTimeByWorkPlan(TrainWorkPlan workPlan, boolean isEdit) throws Exception {
        if (workPlan.getPlanBeginTime() == null)
            return;
        List<JobProcessNode> nodeList = jobProcessNodeQueryManager.getFirstNodeListByWorkPlan(workPlan.getIdx());
        if (nodeList == null || nodeList.size() < 1)
            return;
        buildNodeMap(workPlan.getIdx());
        for (JobProcessNode node : nodeList) {
            node.setPlanBeginTime(workPlan.getPlanBeginTime());// 对选定节点设置计划开始时间
            updatePlanBeginEndTime(node, true, isEdit);
        }
    }
    
    /**
     * <li>说明：更新流程节点的计划时间，级联更新其后续节点、父级节点等关联节点的计划时间
     * <li>创建人：张迪
     * <li>创建日期：2017-1-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObj 流程节点实体
     * @param isBuild 是否已构建nodeBeanMap
     * @param isEdit 是否初始化生成作业计划或编辑作业计划， true 是， false 否
     * @throws Exception
     */
    public void updatePlanBeginEndTime(JobProcessNode jsonObj, boolean isBuild, boolean isEdit) throws Exception {
        PerformanceMonitor.begin(logger, true, "CalcNodeWorkDateNewManager.updatePlanBeginEndTime");
        isEditOrInit = isEdit;
        if (!isBuild) {
            PerformanceMonitor.begin(logger, false, "CalcNodeWorkDateNewManager.buildNodeMap");
            buildNodeMap(jsonObj.getWorkPlanIDX());  
            PerformanceMonitor.end(logger, "【构造节点Map】", false, "CalcNodeWorkDateNewManager.buildNodeMap");
        }
        
        String nodeCaseIdx = jsonObj.getIdx();
        
        PerformanceMonitor.begin(logger, false, "CalcNodeWorkDateNewManager.buildOldNode");
        NodeBean oldNode = buildOldNode(jsonObj, nodeCaseIdx);
        PerformanceMonitor.end(logger, "【构造节点】", false, "CalcNodeWorkDateNewManager.buildOldNode");
        
        PerformanceMonitor.begin(logger, false, "CalcNodeWorkDateNewManager.getCalDate");
        Date newBeginTime = CalcWorkDateUtil.getCalDate(oldNode.getPlanBeginTime(), oldNode.getWorkCalendarIDX());
        PerformanceMonitor.end(logger, "【计算工作日历的开始时间】", false, "CalcNodeWorkDateNewManager.getCalDate");

        PerformanceMonitor.begin(logger, false, "CalcNodeWorkDateNewManager.saveSelfNodeCase");
        Double ratedMinutes = jsonObj.getRatedWorkMinutes();// 页面设置的工期
        Date newEndTime = getNewEndTime(newBeginTime, ratedMinutes, oldNode);// 2 根据节点的最新的开始时间及工期更新本节点的计划完成时间
        saveSelfNodeCase(newBeginTime, newEndTime, oldNode);// 3 更新本节点工期、计划完成时间
        PerformanceMonitor.end(logger, "【更新本节点工期、计划完成时间】", false, "CalcNodeWorkDateNewManager.saveSelfNodeCase");
        
        PerformanceMonitor.begin(logger, false, "CalcNodeWorkDateNewManager.updateChildNodeCaseTime");
        getChildListBySequence(nodeCaseIdx, newBeginTime);// 1  更新本节点实例下级节点的计划开完工时间并保存至nodeCaseMap中并返回dateList(下级所有节点的计划完成时间list列表)
        PerformanceMonitor.end(logger, "【更新下级节点时间并记录nodeCaseMap】", false, "CalcNodeWorkDateNewManager.updateChildNodeCaseTime");
       
        
        PerformanceMonitor.begin(logger, false, "CalcNodeWorkDateNewManager.updatePlanBeginEndTime1");
        updatePlanBeginEndTime(nodeCaseIdx, newEndTime);// 4 推算后续节点、上级节点及所有可能影响到节点的计划开始完成时间并更新节点实例，保存到nodeCaseMap中
        PerformanceMonitor.end(logger, "【更新后续节点、上级节点及所有可能影响到节点的时间并记录nodeCaseMap】", false, "CalcNodeWorkDateNewManager.updatePlanBeginEndTime1");
        
//        PerformanceMonitor.begin(logger, false, "CalcNodeWorkDateNewManager.updateParentWorkDate");
//        updateParentWorkDate(oldNode.getParentIDX());// 5 递归更新父节点的工期
//        PerformanceMonitor.end(logger, "【递归更新父节点的工期并记录nodeCaseMap】", false, "CalcNodeWorkDateNewManager.updateParentWorkDate");
        
        PerformanceMonitor.begin(logger, false, "CalcNodeWorkDateNewManager.saveNodeCaseByNodeCaseMap");
        saveNodeCaseByNodeCaseMap();// 6 迭代nodeCaseMap集合，保存节点实例至数据库
        PerformanceMonitor.end(logger, "【迭代nodeCaseMap集合，保存节点实例至数据库】", false, "CalcNodeWorkDateNewManager.saveNodeCaseByNodeCaseMap");
                
        PerformanceMonitor.end(logger, "【调整时间】", true, "CalcNodeWorkDateNewManager.updatePlanBeginEndTime");
    }
    
    
    

    /**
     * <li>说明：根据前台传递的节点对象重新构造节点对象
     * <li>创建人：张迪
     * <li>创建日期：2017-2-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObj 节点
     * @param nodeCaseIdx 节点idx
     * @return 节点实体
     * @throws Exception
     */
    private NodeBean buildOldNode(JobProcessNode jsonObj, String nodeCaseIdx) throws Exception {
        String planMode = jsonObj.getPlanMode();
        Date jsonBeginTime = jsonObj.getPlanBeginTime();
        NodeBean oldNode = nodeBeanMap.get(nodeCaseIdx);
        oldNode.setThisChange(jsonObj.isThisChange());
        Date planBeginTime = jsonObj.getPlanBeginTime();
        Date maxPreEndTime = getMaxPreEndTimeByCalNode(jsonObj.getIdx()); // 获取最大前置节点结束时间
        planBeginTime = jsonBeginTime;
        if(maxPreEndTime != null && CalcWorkDateUtil.compareTwoDate(planBeginTime,maxPreEndTime)){
            planBeginTime = maxPreEndTime;
        }
        //节点开始时间不能比父节点开始时间早
        if (!StringUtil.isNullOrBlank(oldNode.getParentIDX()) && (oldNode.getPreNodeIDXS() == null || oldNode.getPreNodeIDXS().length < 1)) {
            NodeBean parentNode = nodeBeanMap.get(oldNode.getParentIDX());
            if (CalcWorkDateUtil.compareTwoDate(planBeginTime, parentNode.getPlanBeginTime())) {
                planBeginTime = parentNode.getPlanBeginTime();
            }
        }
        //节点开始时间不能比作业计划开始时间早
        TrainWorkPlan workPlan = trainWorkPlanQueryManager.getModelById(oldNode.getWorkPlanIDX());
        if (CalcWorkDateUtil.compareTwoDate(planBeginTime, workPlan.getPlanBeginTime()))
            planBeginTime = workPlan.getPlanBeginTime();
        
        oldNode.setPlanMode(planMode);
        oldNode.setPlanBeginTime(planBeginTime);
        if (!StringUtil.isNullOrBlank(jsonObj.getWorkCalendarIDX()))
            oldNode.setWorkCalendarIDX(jsonObj.getWorkCalendarIDX());        
        return oldNode;
    }
    
  
    /**
     * <li>说明：根据 父节点开完成时间更新子节点时间
     * <li>创建人：张迪
     * <li>创建日期：2017-1-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param dateList
     * @param parentIdx
     * @param planBeginTime
     * @param planBeginTime
     * @return
     * @throws Exception
     */
    private List<Date> updateChildNodeCaseTime(List<Date> dateList, String parentIdx, Date planBeginTime) throws Exception {
        if (!StringUtil.isNullOrBlank(parentIdx) && planBeginTime != null) {
            List<NodeBean> list = getChildListBySequence(parentIdx, planBeginTime);
           
        }
        return dateList;
    }
    
    /**
     * <li>说明：工期(节点计划开始时间)改变后推算后续节点、同一父节点下节点实例的下层节点（节点计划开始时间改变）计划开始完成时间
     * <li>创建人：张迪
     * <li>创建日期：2017-1-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCaseIdx 节点实例IDX
     * @param newBeginTime 此节点工期(计划开始时间)改变后的计划完工时间
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void updatePlanBeginEndTime(String nodeCaseIdx, Date newBeginTime) throws Exception {
        NodeBean node = nodeBeanMap.get(nodeCaseIdx);
        String[] nextNodeIDXS = node.getNextNodeIDXS();
        if (nextNodeIDXS != null && nextNodeIDXS.length > 0) {
            for (int i = 0; i < nextNodeIDXS.length; i++) {
                NodeBean nextNode = nodeBeanMap.get(nextNodeIDXS[i]);                
                if (nextNode == null)
                    continue;
                nextNode.setHasCal(true);
                nodeBeanMap.put(nextNode.getIdx(), nextNode);                
                Double delayTime = getDelayTime(nodeCaseIdx, nextNode.getIdx());
                if (nextNode.getPlanBeginTime() != null && !isEditOrInit) {
//                    if (JobProcessNode.PLANMODE_MUNAUL.equals(nextNode.getPlanMode()))
//                        continue;
                    newBeginTime = CalcWorkDateUtil.getCalDate(newBeginTime, nextNode.getWorkCalendarIDX());
                    newBeginTime = CalcWorkDateUtil.getDateByRatedMinutes(newBeginTime, delayTime, nextNode.getWorkCalendarIDX());// 根据前置节点结束时间和本节点前置延隔时间计算出本节点开始时间
                    Date maxPreEndTime = getMaxPreEndTime(nextNode.getIdx());
                    if (maxPreEndTime != null && CalcWorkDateUtil.compareTwoDate(newBeginTime, maxPreEndTime))
                        newBeginTime = maxPreEndTime;
                } else {    
                    newBeginTime = CalcWorkDateUtil.getCalDate(newBeginTime, nextNode.getWorkCalendarIDX());
                    newBeginTime = CalcWorkDateUtil.getDateByRatedMinutes(newBeginTime, delayTime, nextNode.getWorkCalendarIDX());// 根据前置节点结束时间和本节点前置延隔时间计算出本节点开始时间
                }
                calNodeBeginEndTime(newBeginTime, nextNode);
            }
        } 
    }
    
    /**
     * <li>说明：构建前置和后置节点map
     * <li>创建人：程锐
     * <li>创建日期：2015-6-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     */
    private void buildPreAndNextMap(String workPlanIDX) {
        List<JobProcessNodeRel> relList = jobProcessNodeQueryManager.getRelListByWorkPlan(workPlanIDX);        
        for (JobProcessNodeRel rel : relList) {
            if (!preNodeMap.containsKey(rel.getNodeIDX())) {
                List<JobProcessNodeRel> tempList = new ArrayList<JobProcessNodeRel>();
                tempList.add(rel);
                preNodeMap.put(rel.getNodeIDX(), tempList);
            } else {
                preNodeMap.get(rel.getNodeIDX()).add(rel);
            }
            if (StringUtil.isNullOrBlank(rel.getPreNodeIDX()))
                continue;
            if (!nextNodeMap.containsKey(rel.getPreNodeIDX())) {
                List<JobProcessNodeRel> tempList = new ArrayList<JobProcessNodeRel>();
                tempList.add(rel);
                nextNodeMap.put(rel.getPreNodeIDX(), tempList);
            } else {
                nextNodeMap.get(rel.getPreNodeIDX()).add(rel);
            }
        }
    }
    
    /**
     * <li>说明：构建节点Map
     * <li>创建人：程锐
     * <li>创建日期：2015-6-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeList 节点列表
     * @return 节点Map
     */
    private Map<String, JobProcessNode> buildNodeMap(List<JobProcessNode> nodeList) {
        Map<String, JobProcessNode> nodeMap = new HashMap<String, JobProcessNode>();
        for (JobProcessNode node : nodeList) {
            nodeMap.put(node.getIdx(), node);            
        }
        return nodeMap;
    }
    
    /**
     * <li>说明：构建节点的后置节点IDX数组
     * <li>创建人：程锐
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param node 流程节点
     * @param nodeBean 节点
     * @return 节点对象
     */
    private NodeBean buildNextNodeIDXS(JobProcessNode node, NodeBean nodeBean) {
        List<JobProcessNodeRel> nextRelList = nextNodeMap.get(node.getIdx());            
        String[] nextNodeIDXS = null;
        if (nextRelList != null && nextRelList.size() > 0) {
            nextNodeIDXS = new String[nextRelList.size()];            
            for (int i = 0; i < nextRelList.size(); i++) {
                nextNodeIDXS[i] = nextRelList.get(i).getNodeIDX();
            }
        }
        if (nextNodeIDXS != null)
            nodeBean.setNextNodeIDXS(nextNodeIDXS);
        return nodeBean;
    }    

    /**
     * <li>说明：构建节点的前置节点IDX数组
     * <li>创建人：程锐
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param node 流程节点
     * @param nodeBean 节点
     * @return 节点对象
     */
    private NodeBean buildPreNodeIDXS(JobProcessNode node, NodeBean nodeBean) {
        List<JobProcessNodeRel> preRelList = preNodeMap.get(node.getIdx());            
        String[] preNodeIDXS = null;
        if (preRelList != null && preRelList.size() > 0) {
            preNodeIDXS = new String[preRelList.size()];            
            for (int i = 0; i < preRelList.size(); i++) {
                preNodeIDXS[i] = preRelList.get(i).getPreNodeIDX();
            }
        }
        if (preNodeIDXS != null)
            nodeBean.setPreNodeIDXS(preNodeIDXS);
        return nodeBean;
    }
    
    /**
     * <li>说明：构建节点的下级第一顺序子节点idx数组及下级节点idx数组
     * <li>创建人：程锐
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param node 流程节点
     * @param nodeBean 节点
     * @param nodeMap 流程节点Map
     * @return 节点对象
     */
    @SuppressWarnings("unchecked")
    private NodeBean buildChildNodeIDX(JobProcessNode node, NodeBean nodeBean, Map<String, JobProcessNode> nodeMap) {
        List<String> childNodeIDXList = new ArrayList<String>();
        List<String> allChildNodeIDXList = new ArrayList<String>();
        if (!nodeMap.isEmpty()) {                
            Set<Map.Entry<String, JobProcessNode>> set = nodeMap.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, JobProcessNode> entry = (Map.Entry<String, JobProcessNode>) it.next();
                JobProcessNode value = entry.getValue();
                if (StringUtil.isNullOrBlank(value.getParentIDX()))
                    continue;
                if (value.getParentIDX().equals(node.getIdx())) {
                    List<JobProcessNodeRel> preList = preNodeMap.get(value.getIdx());
                    if (preList != null && preList.size() == 1 && StringUtil.isNullOrBlank(preList.get(0).getPreNodeIDX())) {
                        childNodeIDXList.add(value.getIdx());
                    }                            
                    allChildNodeIDXList.add(value.getIdx());
                }
            }
        }
        String[] childNodeIDXS = CalcWorkDateUtil.buildNodeArray(childNodeIDXList);
        String[] allChildNodeIDXS = CalcWorkDateUtil.buildNodeArray(allChildNodeIDXList);
        if (childNodeIDXS != null)
            nodeBean.setChildNodeIDXS(childNodeIDXS);
        if (allChildNodeIDXS != null)
            nodeBean.setAllChildNodeIDXS(allChildNodeIDXS);            
        return nodeBean;
    }


    /**
     * <li>说明：计算子节点的计划开始，计划结束时间
     * <li>创建人：张迪
     * <li>创建日期：2017-1-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIdx 父节点id
     * @param planBeginTime 父节点计划开始时间
     * @return 返回节点列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private List<NodeBean> getChildListBySequence(String parentIdx, Date planBeginTime) throws Exception {
        List<NodeBean> list = new ArrayList<NodeBean>();
        String[] childNodeIDXS = nodeBeanMap.get(parentIdx).getAllChildNodeIDXS(); // 获取所有下级子节点idx数组，取消前后置关系
        if (childNodeIDXS == null || childNodeIDXS.length < 1)
            return list;
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < childNodeIDXS.length; i++) {
            NodeBean nodeCase = nodeBeanMap.get(childNodeIDXS[i]);
            nodeCase.setHasCal(true);
            nodeBeanMap.put(nodeCase.getIdx(), nodeCase);
            // 计算计划开始时间
            calendar.setTime(planBeginTime);
            Date newPlanBeginTime = nodeCase.getPlanBeginTime();
            if(0 != nodeCase.getStartDay()&& !StringUtil.isNullOrBlank(nodeCase.getStartTime())){
                calendar.add(Calendar.DATE, nodeCase.getStartDay()-1);
                Date beginTimeTemp = calendar.getTime();   
                newPlanBeginTime = DateUtil.yyyy_MM_dd_HH_mm.parse(DateUtil.yyyy_MM_dd.format(beginTimeTemp) + " " + nodeCase.getStartTime());
            }
            nodeCase.setPlanBeginTime(newPlanBeginTime);
            // 计算计划结束时间
            calendar.setTime(planBeginTime);
            Date newPlanEndTime = nodeCase.getPlanEndTime();
            if(0 != nodeCase.getEndDay()&& !StringUtil.isNullOrBlank(nodeCase.getEndTime())){
                calendar.add(Calendar.DATE, nodeCase.getEndDay()-1);
                Date endTimeTemp = calendar.getTime(); 
                newPlanEndTime = DateUtil.yyyy_MM_dd_HH_mm.parse(DateUtil.yyyy_MM_dd.format(endTimeTemp) + " " + nodeCase.getEndTime());
            }
            nodeCase.setPlanEndTime(newPlanEndTime);
            nodeCase.setChanged(true);
            saveSelfNodeCase(newPlanBeginTime, newPlanEndTime,nodeCase);
//            planEndTime = getSelfEndTime(dateList, planBeginTime, planEndTime, nodeCase);// 根据下级节点的最大计划完成时间更新本节点的计划完成时间及工期
            list.add(nodeCase);// 向list插入第一顺序节点
            getChildListBySequence(nodeCase.getIdx(), planBeginTime); // 递归设置下级节点时间
        }
        return list;
    }
    
    /**
     * <li>说明：根据工艺流程节点实例顺序、上一节点实例ID、上一节点完成时间递归生成排序后的工艺流程节点实例集合
     * <li>创建人：程锐
     * <li>创建日期：2013-3-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 工艺流程节点实例集合
     * @param nodeCaseIdx 工艺流程节点实例主键
     * @param preEndTime 上一节点完成时间
     * @return List<JobProcessNode> 工艺流程节点实例集合
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private List<NodeBean> getNextListBySequence(List<NodeBean> list, String nodeCaseIdx, Date preEndTime) throws Exception {
        String[] nextNodeIDXS = nodeBeanMap.get(nodeCaseIdx).getNextNodeIDXS();
        if (nextNodeIDXS == null || nextNodeIDXS.length < 1) 
            return list;
        for (int i = 0; i < nextNodeIDXS.length; i++) {            
            NodeBean nodeCase = nodeBeanMap.get(nextNodeIDXS[i]);
            nodeCase.setHasCal(true);
            nodeBeanMap.put(nodeCase.getIdx(), nodeCase);
            if (JobProcessNode.PLANMODE_MUNAUL.equals(nodeCase.getPlanMode()) && nodeCase.getPlanBeginTime() != null && !isEditOrInit)
                continue;
            
            NodeBean newNodeCase = new NodeBean();// 构造新节点实例对象（因list的add时保存重复对象有问题）            
            BeanUtils.copyProperties(newNodeCase, nodeCase);// 复制流程节点实例对象至新节点实例对象
            preEndTime = CalcWorkDateUtil.getCalDate(preEndTime, nodeCase.getWorkCalendarIDX());
            Date planBeginTime = CalcWorkDateUtil.getDateByRatedMinutes(preEndTime, getDelayTime(nodeCaseIdx, nodeCase.getIdx()), nodeCase.getWorkCalendarIDX());
            Date maxPreEndTime = getMaxPreEndTime(nextNodeIDXS[i]);
            if (maxPreEndTime != null && CalcWorkDateUtil.compareTwoDate(planBeginTime, maxPreEndTime)) 
                planBeginTime = maxPreEndTime;
            Date planEndTime = getNewEndTime(planBeginTime, nodeCase.getRatedWorkMinutes(), nodeCase);
            @SuppressWarnings("unused")
            List<Date> dateList = updateChildNodeCaseTime(new ArrayList<Date>(), newNodeCase.getIdx(), planBeginTime);// 更新本节点实例下级节点的计划开完工时间并保存至nodeCaseMap中，dateList下级所有节点的计划完成时间list列表
            saveSelfNodeCase(planBeginTime, planEndTime, newNodeCase); // 更新本节点的计划完成时间及工期
            //            planEndTime = getSelfEndTime(dateList, planBeginTime, planEndTime, newNodeCase);// 根据下级节点的最大计划完成时间更新本节点的计划完成时间及工期
            list.add(newNodeCase);
            planEndTime = CalcWorkDateUtil.getCalDate(planEndTime, newNodeCase.getWorkCalendarIDX());
            list = getNextListBySequence(list, nodeCase.getIdx(), planEndTime);// 递归
        }
        return list;
    }
    
    /**
     * <li>说明：根据前置节点IDX和节点IDX获取延搁时间
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param preNodeIDX 前置节点IDX
     * @param nodeIDX 节点IDX
     * @return 延搁时间
     */
    private Double getDelayTime(String preNodeIDX, String nodeIDX) {
        List<JobProcessNodeRel> relList = preNodeMap.get(nodeIDX);
        if (relList == null || relList.size() < 1)
            return 0D;
        for (JobProcessNodeRel rel : relList) {
            if (rel.getPreNodeIDX().equals(preNodeIDX))
                return rel.getDelayTime() != null ? rel.getDelayTime() : 0D;
        }
        return 0D;
    }
    
    /**
     * <li>说明：根据开始时间、工期获取完成时间
     * <li>创建人：程锐
     * <li>创建日期：2014-1-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param newBeginTime 开工时间
     * @param ratedMinutes 工期
     * @param nodeCase 流程节点实体
     * @return 完成时间
     * @throws Exception
     */
    private Date getNewEndTime(Date newBeginTime, Double ratedMinutes, NodeBean nodeCase) throws Exception {
        return CalcWorkDateUtil.getDateByRatedMinutes(newBeginTime, ratedMinutes, nodeCase.getWorkCalendarIDX());// 根据计划开始时间和工期及工作日历计算出计划完工时间
    }
    

    
    /**
     * <li>说明：计算节点工期
     * <li>创建人：程锐
     * <li>创建日期：2014-1-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeCase 流程节点实体
     * @return 工期
     * @throws Exception
     */
    private Double calWorkMinutes(NodeBean nodeCase) throws Exception {
        WorkCalendarDetailManager workCalendarDetailManager =
            (WorkCalendarDetailManager) Application.getSpringApplicationContext().getBean("workCalendarDetailManager");
        String workCalendarIDX = nodeCase.getWorkCalendarIDX();
        if (StringUtil.isNullOrBlank(workCalendarIDX))
            workCalendarIDX = CalcWorkDateUtil.workPlanCalendarIDX;
        if (nodeCase.getPlanBeginTime() == null || nodeCase.getPlanEndTime() == null)
            return 0D;
        return Double.valueOf(workCalendarDetailManager.getRealWorkminutes(nodeCase.getPlanBeginTime(), nodeCase.getPlanEndTime(), workCalendarIDX) / (60000));        
    }
    
    /**
     * <li>说明：保存本节点
     * <li>创建人：张迪
     * <li>创建日期：2017-1-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param newBeginTime 计划开工时间
     * @param newEndTime 计划完工时间
     * @param nodeCase 流程节点实体
     * @throws Exception
     */
    private void saveSelfNodeCase(Date newBeginTime, Date newEndTime, NodeBean nodeCase) throws Exception {
        nodeCase.setHasCal(true);
        nodeBeanMap.put(nodeCase.getIdx(), nodeCase);
        nodeCase.setPlanBeginTime(newBeginTime);
        nodeCase.setPlanEndTime(newEndTime);
        nodeCase.setRatedWorkMinutes(calWorkMinutes(nodeCase));
        nodeCase.setChanged(true);
        nodeBeanMap.put(nodeCase.getIdx(), nodeCase);
    }
    
    /**
     * <li>说明：推算本节点及相关节点的开完工时间
     * <li>创建人：张迪
     * <li>创建日期：2017-1-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param newBeginTime 开工时间
     * @param nextNodeCase 下一节点
     * @throws Exception
     */
    private void calNodeBeginEndTime(Date newBeginTime, NodeBean nextNodeCase) throws Exception {
        Date newNextEndTime = getNewEndTime(newBeginTime, nextNodeCase.getRatedWorkMinutes(), nextNodeCase);
        getChildListBySequence(nextNodeCase.getIdx(), newBeginTime);// 更新本节点实例下级节点的计划开完工时间并保存至nodeCaseMap中，dateList下级所有节点的计划完成时间list列表
        // 保存后续结点的开完工时间
        saveSelfNodeCase(newBeginTime, newNextEndTime, nextNodeCase);
        //        newNextEndTime = getSelfEndTime(dateList, newBeginTime, newNextEndTime, nextNodeCase);  
        updatePlanBeginEndTime(nextNodeCase.getIdx(), newNextEndTime);
    }
    
    /**
     * <li>说明：迭代nodeCaseMap集合，保存节点实例至数据库
     * <li>创建人：程锐
     * <li>创建日期：2013-8-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("all")
    private void saveNodeCaseByNodeCaseMap() throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException {
        // 迭代map集合，向list插入以下顺序节点
        if (!nodeBeanMap.isEmpty()) {
            List<JobProcessNode> saveList = new ArrayList<JobProcessNode>();// 需更新的节点实例列表
            Set<Map.Entry<String, NodeBean>> set = nodeBeanMap.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, NodeBean> entry = (Map.Entry<String, NodeBean>) it.next();
                NodeBean value = entry.getValue();
                JobProcessNode nodeCase = getModelById(value.getIdx());
                BeanUtils.copyProperties(nodeCase, value);// 复制map中节点实例对象至流程节点实例对象（hibernate保存中session不能存在相同id的不同对象）
                saveList.add(nodeCase);
            }
            saveOrUpdate(saveList);
        }
    }
    
    /**
     * <li>说明：根据前置节点及延搁时间计算出的最大时间为本节点的计划开始时间
     * <li>创建人：程锐
     * <li>创建日期：2015-5-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 本节点的计划开始时间
     * @throws Exception
     */
    private Date getMaxPreEndTime(String nodeIDX) throws Exception {
        String[] preNodeIDXS = nodeBeanMap.get(nodeIDX).getPreNodeIDXS();        
        //无前置节点：获取父节点的计划开始时间作为本节点的计划开始时间
        if (preNodeIDXS == null || preNodeIDXS.length < 1) {
            String parentIDX = nodeBeanMap.get(nodeIDX).getParentIDX();            
            NodeBean parentNode = nodeBeanMap.get(parentIDX);
            if (parentNode == null)
                return null;
            return parentNode.getPlanBeginTime();
        }  
        //前置节点不是本次调整时间的节点：则需判断前置节点是否都走过，有未走过的前置节点则返回null 
        if (!isPreIsChange(preNodeIDXS)) {   
            //前置节点都已走过才计算最大时间
            for (int i = 0; i < preNodeIDXS.length; i++) {
                NodeBean preNode = nodeBeanMap.get(preNodeIDXS[i]);
                if (preNode == null)
                    return null;
                if (!preNode.isHasCal() ) {                    
                    return null;
                }                
            }
        }
        //前置节点是本次调整时间的节点或所有前置节点都走过：根据前置节点及延搁时间计算出的最大时间为本节点的计划开始时间
        List<Date> dateList = new ArrayList<Date>();
        for (int i = 0; i < preNodeIDXS.length; i++) {
            NodeBean preNode = nodeBeanMap.get(preNodeIDXS[i]);
            if (preNode == null)
                continue;
            Double delayTime = getDelayTime(preNode.getIdx(), nodeIDX);
            Date newBeginTime = CalcWorkDateUtil.getDateByRatedMinutes(preNode.getPlanEndTime(), delayTime, preNode.getWorkCalendarIDX());
            if (newBeginTime == null)
                continue;
            dateList.add(newBeginTime);
        }
        if (dateList != null && dateList.size() > 0)
            return Collections.max(dateList);
        return null;
    }
    
    /**
     * <li>说明：根据前置节点计算出的最大时间为本次调整节点的计划开始时间
     * <li>创建人：张迪
     * <li>创建日期：2017-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 本次调整的节点
     * @return 本次调整节点的计划开始时间
     * @throws Exception
     */
    private Date getMaxPreEndTimeByCalNode(String nodeIDX) throws Exception {
        Date planBeginTime = null;
        String[] preNodeIDXS = nodeBeanMap.get(nodeIDX).getPreNodeIDXS();        
        
        //无前置节点：获取父节点的计划开始时间作为本节点的计划开始时间
        if (preNodeIDXS == null || preNodeIDXS.length < 1) {
            String parentIDX = nodeBeanMap.get(nodeIDX).getParentIDX();            
            NodeBean parentNode = nodeBeanMap.get(parentIDX);
            if (parentNode == null)
                return planBeginTime;
            return parentNode.getPlanBeginTime();
        }  
        //根据前置节点及延搁时间计算出的最大时间为本节点的计划开始时间
        List<Date> dateList = new ArrayList<Date>();
        for (int i = 0; i < preNodeIDXS.length; i++) {
            NodeBean preNode = nodeBeanMap.get(preNodeIDXS[i]);
            if (preNode == null)
                continue;
            Date newBeginTime = preNode.getPlanEndTime();
            if (newBeginTime == null)
                continue;
            dateList.add(newBeginTime);
        }
        if (dateList != null && dateList.size() > 0)
            planBeginTime =  Collections.max(dateList);
        return planBeginTime;
    }
    
    /**
     * <li>说明：前置节点是否本次调整时间的节点
     * <li>创建人：程锐
     * <li>创建日期：2015-6-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param preNodeIDXS 前置节点IDX
     * @return true 前置节点是本次调整时间的节点， false 否
     */
    private boolean isPreIsChange(String[] preNodeIDXS) {
        for (int i = 0; i < preNodeIDXS.length; i++) {
            NodeBean preNode = nodeBeanMap.get(preNodeIDXS[i]);
            if (preNode == null)
                continue;
            if (preNode.isThisChange())
                return true;
                
        }
        return false;
    }
     
}
