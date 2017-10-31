package com.yunda.jx.jxgc.workplanmanage.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.IbaseComboTree;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.dispatchmanage.manager.WorkStationManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.entity.VJobProcessNode;
import com.yunda.jx.util.IWorkCalendar;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobProcessNode业务类, 机车检修计划流程节点Vis视图查询
 * <li>创建人：何涛
 * <li>创建日期：2015-4-21
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="vJobProcessNodeManager")
public class VJobProcessNodeManager extends JXBaseManager<JobProcessNode, JobProcessNode> implements IbaseComboTree {
    
    /** TrainWorkPlan业务类,机车检修作业计划 */
    @Resource
    private TrainWorkPlanManager trainWorkPlanManager;
    
    /** WorkStation业务类,工位 */
    @Resource
    private WorkStationManager workStationManager;
    
    /** WorkCalendarInfo业务类,工作日历接口 */
    @Resource
    private IWorkCalendar workCalendarDetailManager;
    
    /** 根节点 */
    private String ROOT_0 = "ROOT_0";
    
    
    /**
     * <li>说明：根据机车检修作业计划主键查询机车检修作业计划下属的所有作业流程节点实体
     * <li>创建人：何涛
     * <li>创建日期：2015-4-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修作业计划主键
     * @return List<JobProcessNode>
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getModelsByWorkPlanIDX(String workPlanIDX) {
        String hql = "From JobProcessNode Where recordStatus = 0 And workPlanIDX = ?";
        return this.daoUtils.find(hql, new Object[]{workPlanIDX});
    }
    
    /**
     * <li>说明：根据机车检修作业计划主键查询机车检修作业计划下属指定层级的作业流程节点实体
     * <li>创建人：何涛
     * <li>创建日期：2015-4-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修作业计划主键
     * @param parentIDX 机车检修作业流程节点上级主键（第一级主键为<code>ROOT_0</code>或者为空）
     * @return List<JobProcessNode>
     */
    @SuppressWarnings("unchecked")
    private List<JobProcessNode> getModelsByWorkPlanIDX(String workPlanIDX, String parentIDX) {
        StringBuilder sb = new StringBuilder("From JobProcessNode Where recordStatus = 0");
        sb.append(" And workPlanIDX = '").append(workPlanIDX).append("'");
        
        if (null == parentIDX || ROOT_0.equals(parentIDX)) {
            sb.append(" And (parentIDX Is Null Or parentIDX ='ROOT_0')");
        } else {
            sb.append(" And parentIDX = '").append(parentIDX).append("'");
        }
        sb.append(" Order By planBeginTime,seqNo ASC");
        return this.daoUtils.find(sb.toString());
    }
    
    /**
     * <li>说明：查询机车检修作业计划第一层节点信息，并计算第一层节点的完成百分比
     * <li>创建人：何涛
     * <li>创建日期：2015-5-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修作业计划主键
     * @return List<JobProcessNode> 机车检修作业计划第一层节点信息
     * @throws Exception 
     */
    public List<JobProcessNode> getFirstLevelNode(String workPlanIDX) throws Exception {
        Date currentTime = Calendar.getInstance().getTime();
        List<JobProcessNode> entityList = this.getModelsByWorkPlanIDX(workPlanIDX, ROOT_0);
        for (JobProcessNode node : entityList) {
            // 计算该作业节点的完成百分比
            Float completePercent = this.calculateCompletePercent(node);
            if (0L == completePercent) {
                node.setCompletePercent("0.00%");
            } else {
                node.setCompletePercent(completePercent * 100 + "%");
            }
            // 获取一个机车检修作业流程节点的延期时间
            long delayTime = this.getDelayTime(currentTime, node);
            if (delayTime > 0) {
                node.setDelayTime(Double.valueOf(delayTime));
            }
            // 如果流程节点已启动，则查询节点可能已维护的延误信息
            node.setDelayReason(this.getAllDelayReason(node));
        }
        return entityList;
    }
    
    /**
     * <li>说明：查询机车检修作业计划第一层节点信息，并按节点顺序依次推算节点计划开始完成时间，用于机车检修作业计划编制（不按工期）查询检修作业节点，按节点顺序，依次显示检修作业节点
     * <li>创建人：何涛
     * <li>创建日期：2016-03-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修作业计划主键
     * @return List<JobProcessNode> 机车检修作业计划第一层节点信息
     * @throws Exception
     */
    public List<JobProcessNode> getFirstPlanNode(String workPlanIDX) throws Exception {
        List<JobProcessNode> entityList = this.getModelsByWorkPlanIDX(workPlanIDX, ROOT_0);
        int i = 0;
        Date begin = null;
        Date currentTime = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        for (JobProcessNode node : entityList) {
            
            // 计算该作业节点的完成百分比
            Float completePercent = this.calculateCompletePercent(node);
            if (0L == completePercent) {
                node.setCompletePercent("0.00%");
            } else {
                node.setCompletePercent(completePercent * 100 + "%");
            }
            // 获取一个机车检修作业流程节点的延期时间
            long delayTime = this.getDelayTime(currentTime, node);
            if (delayTime > 0) {
                node.setDelayTime(Double.valueOf(delayTime));
            }
            // 如果流程节点已启动，则查询节点可能已维护的延误信息
            node.setDelayReason(this.getAllDelayReason(node));
            
            if (null == begin) {
                begin = node.getPlanBeginTime();
            }
            calendar.setTime(begin);
            calendar.add(Calendar.HOUR, 6*i);
            node.setRealBeginTime(calendar.getTime());
            calendar.add(Calendar.MINUTE, 350);
            node.setRealEndTime(calendar.getTime());
            i++;
        }
        return entityList;
    }
    
    /**
     * <li>说明：查询机车检修作业计划第一层节点信息，并按节点顺序依次推算节点计划开始完成时间(最新修改)，
     * <li>创建人：张迪
     * <li>创建日期：2016-7-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 机车检修作业计划主键
     * @return 机车检修作业计划第一层节点信息
     * @throws Exception
     */
    public List<JobProcessNode> getFirstPlanNodeNew(String workPlanIDX) throws Exception {
        List<JobProcessNode> entityList = this.getModelsByWorkPlanIDX(workPlanIDX, ROOT_0);  
        Date currentTime = Calendar.getInstance().getTime();
        for (JobProcessNode node : entityList) {          
            // 计算该作业节点的完成百分比
            Float completePercent = this.calculateCompletePercent(node);
            if (0L == completePercent) {
                node.setCompletePercent("0.00%");
            } else {
                node.setCompletePercent(completePercent * 100 + "%");
            }
            // 获取一个机车检修作业流程节点的延期时间
            long delayTime = this.getDelayTime(currentTime, node);
            if (delayTime > 0) {
                node.setDelayTime(Double.valueOf(delayTime));
            }
            // 如果流程节点已启动，则查询节点可能已维护的延误信息
            node.setDelayReason(this.getAllDelayReason(node));
             
        }
        return entityList;
    }
   
   
    
    /**
     * <li>说明：获取一个机车检修作业流程节点的延期时间
     * <li>创建人：何涛
     * <li>创建日期：2015-5-13
     * <li>修改人：何涛
     * <li>修改日期：2016-1-18
     * <li>修改内容：加入工作日历计算机车检修作业流程节点的延期时间
     * @param currentTime 当前时间
     * @param node 机车检修作业计划-流程节点
     * @return 延期时间(单位分钟)
     * @throws Exception 
     */
    public long getDelayTime(Date currentTime, JobProcessNode node) throws Exception {
        long delayTime = -1;
        // 未开工的不计算延期时间
        if (JobProcessNode.STATUS_UNSTART.equals(node.getStatus())) {
            return delayTime;
        }
        
        /* Modified by hetao at 2015-09-21 修改延期时间的计算方式：
         * 算法依据：【bug：2282】: 实际开始时间+工期 内未完成
         */
        if (JobProcessNode.STATUS_COMPLETE.equals(node.getStatus()) && null != node.getRealWorkMinutes()) {
            delayTime = (node.getRealWorkMinutes().longValue() - node.getRatedWorkMinutes().longValue()) * 60 * 1000;
        } else if (JobProcessNode.STATUS_GOING.equals(node.getStatus())) {
            if (null == node.getRealBeginTime()) {
                throw new BusinessException("流程节点：" + node.getNodeName() +"(" + node.getIdx() +")实际开工时间为空！");
            }
            
            // 获取作业节点的工作日历
            String workCalendarIDX = node.getWorkCalendarIDX();
            // 如果节点没有工作日历，则向上获取作业计划的工作日历
            if (null == workCalendarIDX) {
                TrainWorkPlan trainWorkPlan = this.trainWorkPlanManager.getModelById(node.getWorkPlanIDX());
                if (null != trainWorkPlan) {
                    workCalendarIDX = trainWorkPlan.getWorkCalendarIDX();
                }
            }
            
            if (null == workCalendarIDX) {
                // 如果未获取到工作日历，则按实际开始日期 + 额定工期计算延期时间
                delayTime = currentTime.getTime() - (node.getRealBeginTime().getTime() + node.getRatedWorkMinutes().longValue() * 60 * 1000);
            } else {
                // 如果获取到了工作日历，则加入工作日历计算延期时间
                Date finalDate = this.workCalendarDetailManager.getFinalDate(node.getRealBeginTime(), node.getRatedWorkMinutes(), workCalendarIDX);
                delayTime = currentTime.getTime() - finalDate.getTime();
            }
        }
        return delayTime <= 0 ? delayTime : BigDecimal.valueOf(delayTime).divide(BigDecimal.valueOf(1000 * 60), 0, BigDecimal.ROUND_HALF_UP).longValue();
    }
    
    /**
     * <li>说明：获取机车检修作业计划-流程节点及其下属所有节点的延误原因
     * <li>创建人：何涛
     * <li>创建日期：2015-9-1
     * <li>修改人：何涛
     * <li>修改日期：2016-05-04
     * <li>修改内容：使用oracle的递归查询sql，减少数据库查询次数
     * @param node 机车检修作业计划-流程节点实体
     * @return 该节点及其下属所有节点的延误原因
     */
    public String getAllDelayReason(JobProcessNode node) {
        // 递归查询作业流程节点及其下属所有子节点的主键
        String ids = "SELECT IDX FROM JXGC_JOB_PROCESS_NODE T WHERE T.RECORD_STATUS = 0 START WITH T.IDX ='" + node.getIdx() + "' CONNECT BY PRIOR T.IDX = T.PARENT_IDX";
        
        String sql = "SELECT TO_CHAR(WM_CONCAT(DELAY_REASON)) FROM JXGC_NODE_CASE_DELAY WHERE RECORD_STATUS = 0 AND NODE_CASE_IDX IN (" + ids + ")";
        List delayReasonList = this.daoUtils.executeSqlQuery(sql);
        if (delayReasonList.isEmpty()) {
            return null;
        }
        Object object = delayReasonList.get(0);
        return null == object ? null : object.toString();
    }
    
    /**
     * <li>说明：计算机车检修作业流程节点的完成百分比
     * <li>创建人：何涛
     * <li>创建日期：2015-5-4
     * <li>修改人：何涛
     * <li>修改日期：2016-05-04
     * <li>修改内容：修改机车检修作业流程节点完成百分比计算的错误
     * @param node 机车检修作业流程节点实体
     * @return float 作业流程节点的完成百分比
     */
    private float calculateCompletePercent(JobProcessNode node) {
        // 如果节点状态为已完成，则说明已完成100%
        if (JobProcessNode.STATUS_COMPLETE.equals(node.getStatus())) {
            return 1F;
        }
        // 如果节点状态不为处理中，则说明节点作业还未开始，进度为0%
        if (!JobProcessNode.STATUS_GOING.equals(node.getStatus())) {
            return 0F;
        }
        // 1 获取所有“已完成”的工单总数
        Integer handledCount = this.countForWorkCard(node, WorkCard.STATUS_HANDLED);
        if (0 >= handledCount) {
            return 0F;
        }
        // 2 获取所有的工单总数
        Integer totalCount = this.countForWorkCard(node, null);
        if (0 >= totalCount) {
            return 1F;
        }
        return BigDecimal.valueOf(handledCount).divide(BigDecimal.valueOf(totalCount), 3, RoundingMode.HALF_UP).floatValue();
    }
    
    /**
     * <li>说明：统计指定作业流程节点集合下的所有工单数
     * <li>创建人：何涛
     * <li>创建日期：2015-5-4
     * <li>修改人：何涛
     * <li>修改日期：2016-05-04
     * <li>修改内容：优化代码
     * @param node 机车检修作业流程节点实体
     * @param status 要统计的作业工单状态，可以为空
     * @return Integer 统计结果数
     */
    private Integer countForWorkCard(JobProcessNode node, String status) {
        // 递归查询指定作业流程节点及其下属所有子节点的主键
        StringBuilder sb = new StringBuilder(80);
        sb.append("SELECT IDX FROM JXGC_JOB_PROCESS_NODE WHERE RECORD_STATUS = 0");
        sb.append(" AND WORK_PLAN_IDX = '").append(node.getWorkPlanIDX()).append("'");
        sb.append(" START WITH IDX = '").append(node.getIdx()).append("'");
        sb.append(" CONNECT BY PRIOR IDX = PARENT_IDX");
        
        // 查询该节点及其下属所有节点挂接的工单处理数量
        StringBuilder hql = new StringBuilder();
        hql.append("SELECT COUNT(*) FROM JXGC_WORK_CARD WHERE RECORD_STATUS = 0");
        hql.append(" AND NODE_CASE_IDX IN (").append(sb.toString()).append(")");
        if (null != status && status.trim().length() > 0) {
            hql.append(" AND STATUS = '").append(status).append("'");
        }
        List list = this.daoUtils.executeSqlQuery(hql.toString());
        if (null == list) {
            return 0;
        }
        BigDecimal bd = (BigDecimal) list.get(0);
        return null == bd ? 0 : bd.intValue();
    }
    
    /**
     * <li>说明：根据工位主键查询该工位上的作业流程节点实体
     * <li>创建人：何涛
     * <li>创建日期：2015-4-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workStationIDX 工位主键
     * @param workPlanStatus 机车检修计划状态
     * @param trainWorkPlanIDX 机车检修作业计划主键
     * @param currentTime 当前时间
     * @return List<JobProcessNode>
     */
    @SuppressWarnings("unchecked")
    public List<VJobProcessNode> getModelsByWorkStationIDX(String workStationIDX, String workPlanStatus, String trainWorkPlanIDX, Date currentTime) {
        StringBuilder sb = new StringBuilder("Select new JobProcessNode(" +
            "a.idx, a.processIDX, a.nodeIDX, a.workPlanIDX, a.nodeName, a.nodeDesc, a.seqNo, a.ratedWorkMinutes, " +
            "a.realWorkMinutes, a.planBeginTime, a.planEndTime, a.realBeginTime, a.realEndTime, a.status, a.isLeaf, " +
            "a.planMode, a.workStationIDX, b.trainTypeShortName, b.trainNo" +
            ") From JobProcessNode a, TrainWorkPlan b Where a.planBeginTime Is Not Null And a.planEndTime Is Not Null And a.workPlanIDX = b.idx And a.recordStatus = 0 And a.workStationIDX In (?) And b.recordStatus = 0");
        // 查询条件 - 机车检修作业计划状态
        if (!StringUtil.isNullOrBlank(workPlanStatus)) {
            sb.append(" And b.workPlanStatus In('").append(workPlanStatus.replace(Constants.JOINSTR, "','")).append("')");
        }
        // 查询条件 - 机车检修作业计划主键
        if (!StringUtil.isNullOrBlank(trainWorkPlanIDX)) {
            sb.append(" And a.workPlanIDX = '").append(trainWorkPlanIDX).append("'");
        }
        // 默认按照作业节点计划开始时间升序排序
        sb.append(" Order By a.planBeginTime ASC");
        
        // 获取工位下可能存在的子工位
        List<String> listIdx = new ArrayList<String>();
        this.workStationManager.findChildrenIds(workStationIDX, listIdx);
        StringBuilder ids = new StringBuilder();
        for (String idx : listIdx) {
            ids.append(",'").append(idx).append("'");
        }
        List<JobProcessNode> list = this.daoUtils.find(sb.toString().replace("?", ids.substring(1)));
        // 对同一个台位上隶属于相同作业计划、且有时间交集的作业流程节点进行合并
        List<VJobProcessNode> combinedList = this.combine(list, workStationIDX);
        for (VJobProcessNode node : combinedList) {
            // 获取一个机车检修作业流程节点的延期时间
            long delayTime = node.getDelayTime(currentTime);
            if (delayTime > 0) {
                node.setDelayTime(Double.valueOf(delayTime));
            }
        }
        return combinedList;
    }
    
    
    /**
     * <li>说明：以“机车检修作业计划主键”进行分组
     * <li>创建人：何涛
     * <li>创建日期：2015-4-29
     * <li>修改人：何涛
     * <li>修改日期：2015-09-24
     * <li>修改内容：联合工位主键和作任务节点主键-——重设节点主键，否则在存在父子工位的情况下，更新vis时间节点时，会因为id相同导致父工位不能显示下属子工位的作业节点信息
     * @param entityList 作业节点实体集合
     * @param workStationIDX 工位主键
     * @return Map 以“机车检修作业计划主键”进行分组的map实例
     */
    private Map<String, List<JobProcessNode>> groupBy(List<JobProcessNode> entityList, String workStationIDX) {
        Map<String, List<JobProcessNode>> map = new HashMap<String, List<JobProcessNode>>();
        for(JobProcessNode node : entityList){
            // 重设节点主键，否则在存在父子工位的情况下，更新vis时间节点时，会因为id相同导致父工位不能显示下属子工位的作业节点信息
            node.setIdx(workStationIDX + "/" +  node.getIdx());
            // 机车检修作业计划主键
            String workPlanIDX = node.getWorkPlanIDX();
            List<JobProcessNode> tempList = map.get(workPlanIDX);
            if (null == tempList) {
                tempList = new ArrayList<JobProcessNode>();
                map.put(workPlanIDX, tempList);
            }
            tempList.add(node);
        }
        return map;
    }
    
    /**
     * <li>说明：对以“机车检修作业计划主键”进行分组后的数据进行排序
     * <li>创建人：何涛
     * <li>创建日期：2015-4-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param map 以“机车检修作业计划主键”进行分组的map实例
     */
    @SuppressWarnings("unused")
    private void sort(Map<String, List<JobProcessNode>> map) {
        Set<Entry<String, List<JobProcessNode>>> set = map.entrySet();
        for (Iterator<Entry<String, List<JobProcessNode>>> i = set.iterator(); i.hasNext(); ) {
            Entry<String, List<JobProcessNode>> entry = i.next();
            List<JobProcessNode> list = entry.getValue();
            // 以计划开始日期进行升序排序
            Collections.sort(list, new Comparator<JobProcessNode>(){
                public int compare(JobProcessNode o1, JobProcessNode o2) {
                    if (o1.getPlanBeginTime().before(o2.getPlanBeginTime())) {
                        return -1;
                    } else if (o1.getPlanBeginTime().after(o2.getPlanBeginTime())) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
    }
    
    /**
     * <li>说明：对同一个台位上隶属于相同作业计划、且有时间交集的作业流程节点进行合并
     * <li>创建人：何涛
     * <li>创建日期：2015-4-30
     * <li>修改人：何涛
     * <li>修改日期：2015-09-23
     * <li>修改内容：增加一个形参工位主键（workStationIDX），因为该方法用于合并的是同一个工位及其下属工位上的所有作业节点数据
     * @param entityList 未合并前的作业节点列表
     * @param workStationIDX 工位主键
     * @return List<JobProcessNode> 合并后的作业节点列表
     */
    private List<VJobProcessNode> combine(List<JobProcessNode> entityList, String workStationIDX) {
        Map<String, List<JobProcessNode>> map = this.groupBy(entityList, workStationIDX);
        // 排序
//        this.sort(map);
        List<VJobProcessNode> list = new ArrayList<VJobProcessNode>();
        // 合并
        Set<Entry<String, List<JobProcessNode>>> set = map.entrySet();
        for (Iterator<Entry<String, List<JobProcessNode>>> i = set.iterator(); i.hasNext(); ) {
            Entry<String, List<JobProcessNode>> entry = i.next();
            // 获取TrainWorkPlan实体类, 数据表：机车检修作业计划
            String workPlanIDX = entry.getKey();
            TrainWorkPlan trainWorkPlan = trainWorkPlanManager.getModelById(workPlanIDX);
            
            List<JobProcessNode> tempList = entry.getValue();
            JobProcessNode node = null;
            VJobProcessNode vNode = null;
            for (int j = 0; j < tempList.size(); j++) {
                // 如果两个节点可以合并，并且当前List的索引不是最后一个对象的索引，则合并这两个节点
                if (j != tempList.size() - 1 && tempList.get(j).canCombined(tempList.get(j + 1))) {
                    if (null == node) {
                        node = new JobProcessNode();
                        node.setIdx(tempList.get(j).getIdx());
                        node.setTrainTypeShortName(tempList.get(j).getTrainTypeShortName());             // 车型简称
                        node.setTrainNo(tempList.get(j).getTrainNo());                                   // 车号
                        node.setWorkStationIDX(workStationIDX);                                          // 工位主键
                        node.setWorkPlanIDX(workPlanIDX);                                                // 工作计划主键
                    }
                    node.setPlanBeginTime(tempList.get(j).getPlanBeginTime());                       // 计划开始时间
                    // 组合节点的计划结束日期设置为两个节点最晚的结束日期
                    node.setPlanEndTime(tempList.get(j).getPlanEndTime().after(tempList.get(j + 1).getPlanEndTime()) ? tempList.get(j).getPlanEndTime() : tempList.get(j + 1).getPlanEndTime());                       // 计划结束时间
                    node.setStatus(this.combineStatus(tempList.get(j).getStatus(), tempList.get(j + 1).getStatus()));
                    tempList.set(j + 1, node);
                } else {
                    vNode = new VJobProcessNode();
                    vNode.adaptFrom(tempList.get(j), trainWorkPlan);
                    list.add(vNode);
                }
            }
        }
        return list;
    }
    
    /**
     * <li>说明：获取两个节点合并后的组合状态，通常只要两个节点的状态不同，则合并后的节点状态都可以设置为处理中的状态（已开工）
     * <li>创建人：何涛
     * <li>创建日期：2015-4-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param s0 作业节点1的状态
     * @param s1 作业节点2的状态
     * @return 两个节点合并后的组合状态
     */
    private String combineStatus(String s0, String s1) {
        // 如果两个节点的状态相同，则返回其中一个即可
        if (s0.equals(s1)) {
            return s1;
        }
        // 如果有一个节点的状态为开工，则合并后的状态即为开工
        if (JobProcessNode.STATUS_GOING.equals(s0) || JobProcessNode.STATUS_GOING.equals(s1)) {
            return JobProcessNode.STATUS_GOING;
        }
        return JobProcessNode.STATUS_GOING;
    }
    
    /**
     * 
     * <li>说明：公共查询Base_comboTree和Base_multyComboTree方法，子类重写该方法返回查询数据
     * <li>创建人：程锐
     * <li>创建日期：2014-1-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param req HttpServletRequest对象
     * @return List<HashMap> 前台树所需数据列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> getBaseComboTree(HttpServletRequest req) throws Exception {
        // 上级主键
        String parentIDX = StringUtil.nvl(req.getParameter("parentIDX"), ROOT_0);
        // 查询条件
        String queryParams = StringUtil.nvl(req.getParameter("queryParams"), "{}");
        Map<String, String> queryMap = JSONUtil.read(queryParams, Map.class);
        String workPlanIDX = queryMap.get("workPlanIDX");
        List<JobProcessNode> list = getProcessNodeList(parentIDX, workPlanIDX);
        List<HashMap> result = new ArrayList<HashMap>();
        HashMap<String, Object> map = null;
        for (JobProcessNode node : list) {
            map = new HashMap<String, Object>();
            map.put("id", node.getIdx());
            map.put("text", node.getNodeName());
            map.put("leaf", isLeaf(node));
            map.put("realBeginTime", node.getRealBeginTime());
            map.put("planBeginTime", node.getPlanBeginTime());
            map.put("planEndTime", node.getPlanEndTime());
            map.put("nodeIDX", node.getNodeIDX());
            map.put("workPlanIDX", node.getWorkPlanIDX());
            result.add(map);
        }
        return result;
    }

    /**
     * <li>说明：获取父节点
     * <li>创建人：张迪
     * <li>创建日期：2017-3-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点
     * @param workPlanIDX 作业计划
     * @return
     */
    public List<JobProcessNode> getProcessNodeList(String parentIDX,String workPlanIDX) {
        StringBuilder sb = new StringBuilder("From JobProcessNode Where recordStatus = 0");
        sb.append(" And status = '").append(JobProcessNode.STATUS_GOING).append("'");
        if (ROOT_0.equals(parentIDX)) {
            sb.append(" And (").append(" parentIDX Is Null Or parentIDX = '").append(ROOT_0).append("')");
        } else {
            sb.append(" And parentIDX = '").append(parentIDX).append("'");
        }
        if (!StringUtil.isNullOrBlank(workPlanIDX)) {
            sb.append(" And workPlanIDX = '").append(workPlanIDX).append("'");
        }
        // 按顺序号排序
        sb.append(" Order By seqNo ASC");
        List<JobProcessNode> list = this.daoUtils.find(sb.toString());
        return list;
    }

    /**
     * <li>说明：判断一个机车检修作业计划-流程节点是否为叶子节点
     * <li>创建人：何涛
     * <li>创建日期：2015-8-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param node 机车检修作业计划-流程节点实体
     * @return true:叶子节点、false:非叶子节点
     */
    private boolean isLeaf(JobProcessNode node) {
        String hql = "From JobProcessNode Where recordStatus = 0 And parentIDX = ? And workPlanIDX = ?";
        int count = this.daoUtils.getCount(hql, new Object[]{node.getIdx(), node.getWorkPlanIDX()});
        return count <= 0 ? true : false;
    }
    
}
