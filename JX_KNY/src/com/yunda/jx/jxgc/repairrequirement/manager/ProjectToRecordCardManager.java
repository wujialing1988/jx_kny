package com.yunda.jx.jxgc.repairrequirement.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.processdef.entity.JobNodeProjectDef;
import com.yunda.jx.jxgc.processdef.entity.JobNodeUnionWorkSeq;
import com.yunda.jx.jxgc.processdef.manager.JobNodeUnionWorkSeqManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkTaskManager;
import com.yunda.jx.jxgc.repairrequirement.entity.DetectItem;
import com.yunda.jx.jxgc.repairrequirement.entity.PartsList;
import com.yunda.jx.jxgc.repairrequirement.entity.QualityControl;
import com.yunda.jx.jxgc.repairrequirement.entity.RPToWS;
import com.yunda.jx.jxgc.repairrequirement.entity.RepairProject;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkSeq;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkStep;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkStepResult;
import com.yunda.jx.jxgc.workplanmanage.entity.WorkPlanRepairActivity;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车检修作业工单处理优化-转化以前的表关系至新的表关系 
 * <li>创建人：程锐
 * <li>创建日期：2016-6-7
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@Service("projectToRecordCardManager")
public class ProjectToRecordCardManager  extends JXBaseManager<RepairProject, RepairProject>{
    
    @Resource
    private WorkSeqManager workSeqManager;
    
    @Resource
    private WorkStepManager workStepManager;
    
    @Resource
    private DetectItemManager detectItemManager;
    
    @Resource
    private WorkStepResultManager workStepResultManager;
    
    @Resource
    private QualityControlManager qualityControlManager;
    
    @Resource
    private PartsListManager partsListManager;
    
    @Resource
    private JobNodeUnionWorkSeqManager jobNodeUnionWorkSeqManager;
    
    @Resource
    private WorkCardManager workCardManager;
    
    @Resource
    private WorkTaskManager workTaskManager;
    
    Map<String, WorkSeq> seqMap = new HashMap<String, WorkSeq>(); // workSeqIDX, WorkSeq
    
    Map<String, List<PartsList>> partsListMap = new HashMap<String, List<PartsList>>();// workSeqIDX, List<PartsList>
    
    Map<String, List<WorkStep>> stepMap = new HashMap<String, List<WorkStep>>(); // workSeqIDX, List<WorkStep>
    
    Map<String, List<DetectItem>> detectItemMap = new HashMap<String, List<DetectItem>>(); // workStepIDX, List<DetectItem>
    
    Map<String, List<QualityControl>> qualityMap = new HashMap<String, List<QualityControl>>(); // workSeqIDX, List<QualityControl>
    
    Map<String, List<WorkStepResult>> stepResultMap = new HashMap<String, List<WorkStepResult>>(); // workStepIDX, List<WorkStepResult>
    
    Map<String, List<RPToWS>> rpToWSMap = new HashMap<String, List<RPToWS>>();// workSeqIDX, List<RPToWS>

    Map<String, List<JobNodeProjectDef>> nodeProjectMap = new HashMap<String, List<JobNodeProjectDef>>();

    Map<String, List<WorkSeq>> seqByRecordMap = new HashMap<String, List<WorkSeq>>(); // recordIDX, WorkSeq

    Map<String, WorkPlanRepairActivity> repairActivityMap = new HashMap<String, WorkPlanRepairActivity>();

    Map<String, WorkCard> workCardMap = new HashMap<String, WorkCard>();
    
    List<WorkSeq> saveSeqList = new ArrayList<WorkSeq>();
    
    List<WorkStep> saveWorkStepList = new ArrayList<WorkStep>();
    
    List<WorkCard> updateWorkCardList = new ArrayList<WorkCard>();
    
    /**
     * <li>说明：机车检修作业工单处理优化-转化以前的表关系至新的表关系 
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    public void updateProject() throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException {        
        initMap();        
        saveOrUpdateWorkSeq();
        saveWorkSeqRelations();
        saveWorkStepRelations();
        initSeqByRecordMap();
        saveNodeSeqRelation();
        updateWorkCard();
        updateWorkTask();
    }    
    
    /**
     * <li>说明：初始化Map
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    private void initMap() {
        initSeqMap();
        initPartsListMap();
        initStepMap();
        initDetectItemMap();
        initQualityMap();
        initStepResultMap();
        initRPToWSMap();
        initNodeProjectMap();
    }
    
    /**
     * <li>说明：初始化检修记录卡seqMap<workSeqIDX, WorkSeq>
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private void initSeqMap() {
        seqMap.clear();
        String hql = "from WorkSeq where recordStatus = 0";
        List<WorkSeq> seqList = daoUtils.find(hql);        
        for (WorkSeq seq : seqList) {
            seqMap.put(seq.getIdx(), seq);
        }
    }
    
    /**
     * <li>说明：初始化配件清单partsListMap<workSeqIDX, List<PartsList>>
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private void initPartsListMap() {
        partsListMap.clear();
        String hql = "from PartsList";
        List<PartsList> partsList = daoUtils.find(hql);        
        for (PartsList jxgcPartsList : partsList) {
            List<PartsList> newPartsList = new ArrayList<PartsList>();
            if (partsListMap.containsKey(jxgcPartsList.getRelationIdx())) {
                newPartsList = partsListMap.get(jxgcPartsList.getRelationIdx());                                
            }
            newPartsList.add(jxgcPartsList);    
            partsListMap.put(jxgcPartsList.getRelationIdx(), newPartsList);
        }
    }   
    
    /**
     * <li>说明：初始化检修检测项WorkStepMap<workSeqIDX, List<WorkStep>>
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private void initStepMap() {
        stepMap.clear();
        String hql = "from WorkStep where recordStatus = 0";
        List<WorkStep> stepList = daoUtils.find(hql);        
        for (WorkStep step : stepList) {
            List<WorkStep> newStepList = new ArrayList<WorkStep>();
            if (stepMap.containsKey(step.getWorkSeqIDX())) {
                newStepList = stepMap.get(step.getWorkSeqIDX());                                
            }
            newStepList.add(step);    
            stepMap.put(step.getWorkSeqIDX(), newStepList);
        }
    }
    
    /**
     * <li>说明：初始化数据检测项DetectItemMap<workStepIDX, List<DetectItem>>
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private void initDetectItemMap() {
        detectItemMap.clear();
        String hql = "from DetectItem where recordStatus = 0";
        List<DetectItem> detectItemList = daoUtils.find(hql);        
        for (DetectItem item : detectItemList) {
            List<DetectItem> newItemList = new ArrayList<DetectItem>();
            if (detectItemMap.containsKey(item.getWorkStepIDX())) {
                newItemList = detectItemMap.get(item.getWorkStepIDX());                                
            }
            newItemList.add(item);    
            detectItemMap.put(item.getWorkStepIDX(), newItemList);
        }
    }
    
    /**
     * <li>说明：初始化质检项QualityControlMap<workSeqIDX, List<QualityControl>>
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private void initQualityMap() {
        qualityMap.clear();
        String hql = "from QualityControl where recordStatus = 0";
        List<QualityControl> qualityList = daoUtils.find(hql);        
        for (QualityControl qualityControl : qualityList) {
            List<QualityControl> newQualityList = new ArrayList<QualityControl>();
            if (qualityMap.containsKey(qualityControl.getRelationIDX())) {
                newQualityList = qualityMap.get(qualityControl.getRelationIDX());                                
            }
            newQualityList.add(qualityControl);    
            qualityMap.put(qualityControl.getRelationIDX(), newQualityList);
        }
    }
    
    /**
     * <li>说明：初始化检测项默认结果WorkStepResultMap<workStepIDX, List<WorkStepResult>>
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private void initStepResultMap() {
        stepResultMap.clear();
        String hql = "from WorkStepResult where recordStatus = 0";
        List<WorkStepResult> stepResultList = daoUtils.find(hql);
        for (WorkStepResult workStepResult : stepResultList) {
            List<WorkStepResult> newWorkStepResultList = new ArrayList<WorkStepResult>();
            if (stepResultMap.containsKey(workStepResult.getWorkStepIDX())) {
                newWorkStepResultList = stepResultMap.get(workStepResult.getWorkStepIDX());                                
            }
            newWorkStepResultList.add(workStepResult);    
            stepResultMap.put(workStepResult.getWorkStepIDX(), newWorkStepResultList);
        }
    }
    
    /**
     * <li>说明：初始化检修项目关联作业工单关系RPToWSMap<workSeqIDX, List<RPToWS>>
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private void initRPToWSMap() {
        rpToWSMap.clear();
        String hql = "from RPToWS where recordStatus = 0";
        List<RPToWS> rpToWSList = daoUtils.find(hql);
        for (RPToWS toWS : rpToWSList) {
            String workSeqIDX = toWS.getWorkSeqIDX();
            List<RPToWS> newList = new ArrayList<RPToWS>();
            if (rpToWSMap.containsKey(workSeqIDX)) {
                newList = rpToWSMap.get(workSeqIDX);
            }
            newList.add(toWS);
            rpToWSMap.put(workSeqIDX, newList);
        }
    }
    
    /**
     * <li>说明：初始化节点与检修项目关联关系JobNodeProjectDefMap<nodeIDX, List<JobNodeProjectDef>>
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private void initNodeProjectMap() {
        nodeProjectMap.clear();
        String hql = "from JobNodeProjectDef where recordStatus = 0";
        List<JobNodeProjectDef> nodeProjectList = daoUtils.find(hql);
        for (JobNodeProjectDef def : nodeProjectList) {
            String nodeIDX = def.getNodeIDX();
            List<JobNodeProjectDef> newList = new ArrayList<JobNodeProjectDef>();
            if (nodeProjectMap.containsKey(nodeIDX)) {
                newList = nodeProjectMap.get(nodeIDX);
            }
            newList.add(def);
            nodeProjectMap.put(nodeIDX, newList);
        }
    }
    
    /**
     * <li>说明：初始化检修记录卡Map<recordIDX, List<WorkSeq>>
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private void initSeqByRecordMap() {
        seqByRecordMap.clear();
        String hql = "from WorkSeq where recordStatus = 0";
        List<WorkSeq> seqList = daoUtils.find(hql);
        for (WorkSeq seq : seqList) {
            String recordIDX = seq.getRecordIDX();
            if (StringUtil.isNullOrBlank(recordIDX))
                continue;
            List<WorkSeq> newList = new ArrayList<WorkSeq>();
            if (seqByRecordMap.containsKey(recordIDX)) {
                newList = seqByRecordMap.get(recordIDX);                
            }
            newList.add(seq);
            seqByRecordMap.put(recordIDX, newList);
        }
    }
    
    /**
     * <li>说明：新增或更新检修记录卡
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    private void saveOrUpdateWorkSeq() throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException {
        saveSeqList = new ArrayList<WorkSeq>();
        List<WorkSeq> updateSeqList = new ArrayList<WorkSeq>();        
        Iterator iter = rpToWSMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, List<RPToWS>> entry = (Map.Entry<String, List<RPToWS>>) iter.next();
            String seqIDX = entry.getKey();
            WorkSeq workSeq = seqMap.get(seqIDX);
            if (workSeq != null) {
                List<RPToWS> list = entry.getValue();
                if (list != null && list.size() == 1) {
                    workSeq.setRecordIDX(list.get(0).getRepairProjectIDX());
                    updateSeqList.add(workSeq);
                } else if (list != null && list.size() > 1){                    
                    for (int i = 0; i < list.size(); i++) {
                        if (i == 0) {
                            workSeq.setRecordIDX(list.get(0).getRepairProjectIDX());
                            updateSeqList.add(workSeq);
                            continue;
                        }                            
                        WorkSeq seq = new WorkSeq();
                        BeanUtils.copyProperties(seq, workSeq);                  
                        seq.setIdx("");
                        seq.setRecordIDX(list.get(i).getRepairProjectIDX());
                        seq.setSourceSeqIDX(seqIDX);
                        saveSeqList.add(seq);
                    }
                }        
            }   
        }
        workSeqManager.saveOrUpdate(saveSeqList);
        workSeqManager.saveOrUpdate(updateSeqList);
    }
    
    /**
     * <li>说明：新增检修记录卡关联的检测检修项、质检项、配件清单等
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void saveWorkSeqRelations() throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException {
        saveWorkStepList = new ArrayList<WorkStep>();
        List<PartsList> savePartsList = new ArrayList<PartsList>();                
        List<QualityControl> saveQualityList = new ArrayList<QualityControl>();
        for (WorkSeq seq : saveSeqList) {
            String sourceSeqIDX = seq.getSourceSeqIDX();
            String seqIDX = seq.getIdx();
            if (StringUtil.isNullOrBlank(sourceSeqIDX) || StringUtil.isNullOrBlank(seqIDX))
                continue;
            // 复制配件清单
            List<PartsList> newPartsList = partsListMap.get(sourceSeqIDX);
            if (newPartsList == null || newPartsList.size() < 1)
                continue;
            for (PartsList list : newPartsList) {
                PartsList saveParts = new PartsList();
                BeanUtils.copyProperties(saveParts, list);
                saveParts.setIdx("");
                saveParts.setRelationIdx(seqIDX);
                savePartsList.add(saveParts);
            }
            // 复制检测检修项目
            List<WorkStep> newStepList = stepMap.get(sourceSeqIDX);
            if (newStepList == null || newStepList.size() < 1)
                continue;
            for (WorkStep step : newStepList) {
                WorkStep saveStep = new WorkStep();
                BeanUtils.copyProperties(saveStep, step);
                saveStep.setIdx("");
                saveStep.setWorkSeqIDX(seqIDX);
                saveStep.setSourceStepIDX(step.getIdx());
                saveWorkStepList.add(saveStep);
            }
            // 复制质量检验
            List<QualityControl> newQualityControlList = qualityMap.get(sourceSeqIDX);
            if (newQualityControlList == null || newQualityControlList.size() < 1)
                continue;
            for (QualityControl control : newQualityControlList) {
                QualityControl saveQualityControl = new QualityControl();
                BeanUtils.copyProperties(saveQualityControl, control);
                saveQualityControl.setIdx("");
                saveQualityControl.setRelationIDX(seqIDX);
                saveQualityList.add(saveQualityControl);
            }
        }        
        partsListManager.saveOrUpdate(savePartsList);
        workStepManager.saveOrUpdate(saveWorkStepList);
        qualityControlManager.saveOrUpdate(saveQualityList);
    }
    
    /**
     * <li>说明：新增检测检修项关联的数据检测项、默认结果等
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void saveWorkStepRelations() throws IllegalAccessException, InvocationTargetException, BusinessException, NoSuchFieldException {
        List<DetectItem> saveDetectItemList = new ArrayList<DetectItem>();
        List<WorkStepResult> saveResultList = new ArrayList<WorkStepResult>();
        for (WorkStep workStep : saveWorkStepList) {
            String sourceStepIDX = workStep.getSourceStepIDX();
            String stepIDX = workStep.getIdx();
            if (StringUtil.isNullOrBlank(stepIDX) || StringUtil.isNullOrBlank(sourceStepIDX))
                continue;
            
            //复制数据项
            List<DetectItem> newDetectItemList = detectItemMap.get(sourceStepIDX);
            if (newDetectItemList == null || newDetectItemList.size() < 1)
                continue;
            for (DetectItem item : newDetectItemList) {
                DetectItem saveItem = new DetectItem();
                BeanUtils.copyProperties(saveItem, item);
                saveItem.setIdx("");
                saveItem.setWorkStepIDX(stepIDX);
                saveDetectItemList.add(saveItem);
            }
            //复制作业任务默认结果
            List<WorkStepResult> newWorkStepResultList = stepResultMap.get(sourceStepIDX);
            if (newWorkStepResultList == null || newWorkStepResultList.size() < 1)
                continue;
            for (WorkStepResult result : newWorkStepResultList) {
                WorkStepResult saveResult = new WorkStepResult();
                BeanUtils.copyProperties(saveResult, result);
                saveResult.setIdx("");
                saveResult.setWorkStepIDX(stepIDX);
                saveResultList.add(saveResult);
            }
        }
        detectItemManager.saveOrUpdate(saveDetectItemList);
        workStepResultManager.saveOrUpdate(saveResultList);
    }
    
    /**
     * <li>说明：新增节点与检修记录卡的关联关系
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    private void saveNodeSeqRelation() throws BusinessException, NoSuchFieldException {
        // 节点与检修记录单的关联关系（JXGC_JobNode_Project_Def）转为节点与检修记录卡的关联关系（JXGC_JobNode_Union_WorkSeq）        
        List<JobNodeUnionWorkSeq> saveNodeUnionSeqList = new ArrayList<JobNodeUnionWorkSeq>();
        Iterator it = nodeProjectMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<JobNodeProjectDef>> entry = (Map.Entry<String, List<JobNodeProjectDef>>) it.next();
            String nodeIDX = entry.getKey();
            List<JobNodeProjectDef> list = entry.getValue();
            if (list != null && list.size() > 0) {
                for (JobNodeProjectDef def : list) {
                    String recordIDX = def.getProjectIDX();
                    if (StringUtil.isNullOrBlank(recordIDX))
                        continue;
                    List<WorkSeq> seqByRecordList = seqByRecordMap.get(recordIDX);
                    if (seqByRecordList == null || seqByRecordList.size() < 1) {
                        continue;
                    }
                    for (WorkSeq seq : seqByRecordList) {
                        JobNodeUnionWorkSeq nodeSeq = new JobNodeUnionWorkSeq();
                        nodeSeq.setNodeIDX(nodeIDX);
                        nodeSeq.setRecordCardIDX(seq.getIdx());
                        saveNodeUnionSeqList.add(nodeSeq);
                    }
                }
            }            
        }
        jobNodeUnionWorkSeqManager.saveOrUpdate(saveNodeUnionSeqList);
    }
    
    /**
     * <li>说明：初始化机车检修作业计划-检修记录单Map<idx, WorkPlanRepairActivity>
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    private void initRepairActivityMap() {
        repairActivityMap.clear();
        String hql = "from WorkPlanRepairActivity where recordStatus = 0";
        List<WorkPlanRepairActivity> repairActivityList = daoUtils.find(hql);
        for (WorkPlanRepairActivity activity : repairActivityList) {
            repairActivityMap.put(activity.getIdx(), activity);
        }
    }
    
    /**
     * <li>说明：更新机车检修作业计划-检修记录卡的Work_Seq_Card_IDX
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    private void updateWorkCard() throws BusinessException, NoSuchFieldException {
        initRepairActivityMap();        
        String hql = "from WorkCard where recordStatus = 0";
        List<WorkCard> workCardList = daoUtils.find(hql);
        updateWorkCardList = new ArrayList<WorkCard>();
        
        for (WorkCard card : workCardList) {
            if (StringUtil.isNullOrBlank(card.getRepairActivityIDX()))
                continue;
            WorkPlanRepairActivity activity = repairActivityMap.get(card.getRepairActivityIDX());
            if (activity == null || StringUtil.isNullOrBlank(activity.getRepairProjectIDX()))
                continue;
            List<WorkSeq> seqByRecordList = seqByRecordMap.get(activity.getRepairProjectIDX());
            if (seqByRecordList == null || seqByRecordList.size() < 1) {
                continue;
            }
            for (WorkSeq seq : seqByRecordList) {
                if (StringUtil.isNullOrBlank(seq.getWorkSeqCode()) || StringUtil.isNullOrBlank(card.getWorkCardCode())) 
                    continue;
                if (seq.getWorkSeqCode().equals(card.getWorkCardCode())) {
                    card.setWorkSeqCardIDX(seq.getIdx());
                    updateWorkCardList.add(card);
                    break;
                }
            }
        }
        workCardManager.saveOrUpdate(updateWorkCardList);
    }
    
    /**
     * <li>说明：初始化机车检修作业计划-检修记录卡Map<idx, WorkCard>
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    private void initWorkCardMap() {
        workCardMap.clear();
        for (WorkCard card : updateWorkCardList) {
            workCardMap.put(card.getIdx(), card);
        }
    }
    
    /**
     * <li>说明：更新机车检修作业计划-检测检修项的Work_Step_IDX
     * <li>创建人：程锐
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    @SuppressWarnings("unchecked")
    private void updateWorkTask() throws BusinessException, NoSuchFieldException {
        initWorkCardMap();
        initStepMap();        
        
        String hql = "from WorkTask where recordStatus = 0";
        List<WorkTask> workTaskList = daoUtils.find(hql);
        List<WorkTask> updateWorkTaskList = daoUtils.find(hql);
        for (WorkTask task : workTaskList) {
            String workCardIDX = task.getWorkCardIDX();
            if (StringUtil.isNullOrBlank(workCardIDX))
                continue;
            if (!workCardMap.containsKey(workCardIDX))
                continue;
            WorkCard workCard = workCardMap.get(workCardIDX);
            if (workCard == null || StringUtil.isNullOrBlank(workCard.getWorkSeqCardIDX()))
                continue;
            List<WorkStep> stepBySeqList = stepMap.get(workCard.getWorkSeqCardIDX());
            if (stepBySeqList == null || stepBySeqList.size() < 1)
                continue;
            for (WorkStep step : stepBySeqList) {
                if (StringUtil.isNullOrBlank(step.getWorkStepCode()) || StringUtil.isNullOrBlank(task.getWorkTaskCode())) 
                    continue;
                if (step.getWorkStepCode().equals(task.getWorkTaskCode())) {
                    task.setWorkStepIDX(step.getIdx());
                    updateWorkTaskList.add(task);
                    break;
                }
            }
        }
        workTaskManager.saveOrUpdate(updateWorkTaskList);
    }
}
