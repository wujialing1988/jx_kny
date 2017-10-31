package com.yunda.jx.jxgc.workhis.workplan.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarInfoManager;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;
import com.yunda.jx.jxgc.producttaskmanage.entity.NodeCaseDelay;
import com.yunda.jx.jxgc.producttaskmanage.manager.GanttManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.NodeCaseDelayManager;
import com.yunda.jx.jxgc.workhis.workplan.entity.JobProcessNodeHis;
import com.yunda.jx.jxgc.workhis.workplan.entity.TrainWorkPlanHis;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.third.edo.entity.Baseline;
import com.yunda.jx.third.edo.entity.Calendar;
import com.yunda.jx.third.edo.entity.PredecessorLink;
import com.yunda.jx.third.edo.entity.Result;
import com.yunda.jx.third.edo.entity.Task;
import com.yunda.jx.third.edo.entity.WorkingTime;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车检修计划历史查看甘特图业务类
 * <li>创建人：程锐
 * <li>创建日期：2016-2-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@Service(value = "workPlanHisGanttManager")
public class WorkPlanHisGanttManager extends JXBaseManager<Object, Object> {

    /** 甘特图展示公共业务类 */
    @Resource
    private GanttManager ganttManager;
    
    /** 工作日历业务类 */
    @Resource
    private WorkCalendarInfoManager workCalendarInfoManager;

    /** 流程节点历史业务类 */
    @Resource
    private JobProcessNodeHisManager jobProcessNodeHisManager;
    
    @Resource
    private NodeCaseDelayManager nodeCaseDelayManager;
    
    public static final String NODETYPE = "流程节点";
    
    public static final String PROCESSTYPE = "流程";
    
    public static final SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    public static final String GANTTFORMATSTR = "yyyy-MM-dd'T'HH:mm:ss";
    
    public static boolean isExpanded = false;
    
    public static String nodeIDX = "";
    
    /**
     * <li>说明：甘特图展示
     * <li>创建人：程锐
     * <li>创建日期：2016-1-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 作业计划实体
     * @param displayMode 甘特图显示模式：default默认（第一次加载、刷新等）、expanded（对节点操作后加载）
     * @param nodeIdx 操作的节点IDX
     * @return 甘特图Result对象
     * @throws Exception
     */
    public Result planOrderGantt(TrainWorkPlanHis workPlan, String displayMode, String nodeIdx) throws Exception {
        Result result = new Result();
        WorkingTime[] workingTimes = ganttManager.getWorkingTimes();
        Calendar[] calendars = ganttManager.getCalendars(workingTimes);
        nodeIDX = "";
        if ("expanded".equals(displayMode) && !StringUtil.isNullOrBlank(nodeIdx)) {
            isExpanded = true;
            nodeIDX = nodeIdx; 
        }            
        else
            isExpanded = false;
        Task[] tasks = getTasks(workPlan); // 预计划排程任务数组
        String startDate = "";
        Date planBeginTime = workPlan.getPlanBeginTime();
        if (planBeginTime != null) {
            startDate = new SimpleDateFormat(GANTTFORMATSTR).format(planBeginTime);
        }
        String endDate = "";
        if (workPlan.getPlanEndTime() != null)
            endDate = new SimpleDateFormat(GANTTFORMATSTR).format(workPlan.getPlanEndTime());
        result.setUID("110");
        result.setName("检修活动");
        result.setStartDate(startDate);
        result.setFinishDate(endDate);
        result.setWeekStartDay(2);
        result.setCalendars(calendars);
        result.setTasks(tasks);
        result.setEnableDurationLimit(true);
        return result;
    }
    
    /**
     * <li>说明：构造甘特图-任务实体对象数组
     * <li>创建人：程锐
     * <li>创建日期：2016-1-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 作业计划实体
     * @return 任务实体对象数组
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Task[] getTasks(TrainWorkPlanHis workPlan) throws Exception {
        int i = 1;
        List<Task> taskList = new ArrayList<Task>();
        String startDate = "";// 开始时间
        String endDate = "";// 完成时间
        String realStartDate = "";
        String realEndDate = "";
        BigDecimal workDate = new BigDecimal("0"); // 工期
        Date planBeginTime = workPlan.getPlanBeginTime();
        if (planBeginTime != null) {
            startDate = new SimpleDateFormat(GANTTFORMATSTR).format(planBeginTime);
            if (workPlan.getRatedWorkDay() != null) {
                Double ratedMinutes = workPlan.getRatedWorkDay();
                workDate = new BigDecimal(ratedMinutes).setScale(2, BigDecimal.ROUND_HALF_UP);
            }            
            Date planEndTime = workPlan.getPlanEndTime();
            if (planEndTime != null)
                endDate = new SimpleDateFormat(GANTTFORMATSTR).format(planEndTime);
            if (workPlan.getBeginTime() != null)
                realStartDate = YYYY_MM_DD_HH_MM.format(workPlan.getBeginTime());
            if (workPlan.getEndTime() != null)
                realEndDate = YYYY_MM_DD_HH_MM.format(workPlan.getEndTime());
            
        }
        Integer durationFormat = 5;// 工时显示格式化
        Integer milestone = 0;// 里程碑1是0否
        String outlineNumber = "1";// 大纲字段:任务层次以及顺序        
        // 流程节点任务
        List<JobProcessNodeHis> list = getNodeListByWorkPlanForGantt(workPlan.getIdx(), null);// parentIdx传null查询第一层节点
        Integer percentComplete = calPercentCompleteByList(list);// 完成百分比
        List<JobProcessNodeHis> nodeList = new LinkedList<JobProcessNodeHis>();
        // 流程任务
        Task task = new Task(String.valueOf(i), 
                             workPlan.getProcessName(), 
                             outlineNumber, 
                             startDate, 
                             endDate, 
                             workDate.intValue(), 
                             durationFormat, 
                             0,
                             milestone);
        task.setWorkDate(workDate);
        task.setNodeType(PROCESSTYPE);
        task.setPercentComplete(percentComplete);
        task.setTecProcessCaseIDX(workPlan.getProcessIDX());
        task.setStatus(workPlan.getWorkPlanStatus());
        task.setProcessInfo(TrainWorkPlanHis.getStatusMeaning(workPlan.getWorkPlanStatus()));
        task.setRatedWorkMinutes(workPlan.getRatedWorkDay());
        task.setBeforeDelayTime(0d);
        task.setWorkCalendar(getWorkCalendar(workPlan.getWorkCalendarIDX()));
        task.setRealStart(realStartDate);
        task.setRealFinish(realEndDate);
        taskList.add(task);        
        int k = 1;
        // 父子节点
        for (JobProcessNodeHis node : list) {
            i++;
            String nodeOutlineNumber = outlineNumber + "." + k;
            String nodeCaseName = node.getNodeName();
            startDate = "";
            if (node.getPlanBeginTime() != null)
                startDate = new SimpleDateFormat(GANTTFORMATSTR).format(node.getPlanBeginTime());
            endDate = "";
            if (node.getPlanEndTime() != null)
                endDate = new SimpleDateFormat(GANTTFORMATSTR).format(node.getPlanEndTime());
            workDate = new BigDecimal("0"); // 工期            
            if (node.getIsLeaf() == JobProcessNodeDef.CONST_INT_IS_LEAF_NO)                
                percentComplete = calPercentCompleteByList(jobProcessNodeHisManager.getChildNodeList(node.getIdx()), node.getStatus());
            else
                percentComplete = node.getStatus().equals(JobProcessNodeHis.STATUS_COMPLETE) ? 100 : 0;
            if (node.getRatedWorkMinutes() != null) {
                Double ratedWorkMinutes = node.getRatedWorkMinutes() / 60;
                workDate = new BigDecimal(ratedWorkMinutes.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            realStartDate = "";
            if (node.getRealBeginTime() != null)
                realStartDate = YYYY_MM_DD_HH_MM.format(node.getRealBeginTime());
            realEndDate = "";
            if (node.getRealEndTime() != null)
                realEndDate = YYYY_MM_DD_HH_MM.format(node.getRealEndTime());
            task = new Task(String.valueOf(i),
                            nodeCaseName, 
                            nodeOutlineNumber, 
                            startDate, 
                            endDate,
                            workDate.intValue(),
                            durationFormat, 
                            0, 
                            milestone);
            task.setWorkDate(workDate);
            task.setNodeType(NODETYPE);
            task.setNodeCaseIdx(node.getIdx());
            task.setWorkTeam(node.getWorkStationBelongTeamName());
            task.setWorkStationName(node.getWorkStationName());
            task.setPercentComplete(percentComplete);
            task.setTecProcessCaseIDX(workPlan.getProcessIDX());
            task.setWorkStationIDX(node.getWorkStationIDX());
            task.setNodeIDX(node.getNodeIDX());
            task.setParentIdx(node.getParentIDX());
            task.setIsLastLevel(node.getIsLeaf());
            task.setStatus(node.getStatus());
            task.setProcessInfo(JobProcessNodeHis.getStatusMeaning(node.getStatus()));
            task.setRatedWorkMinutes(node.getRatedWorkMinutes());
            String planMode = "";
            if(JobProcessNode.PLANMODE_AUTO.equals(node.getPlanMode())){
                planMode ="自动";
            }else if(JobProcessNode.PLANMODE_MUNAUL.equals(node.getPlanMode())){
                planMode ="手动";
            }else {
                planMode ="定时";
            }
            task.setPlanMode(planMode);
            task.setWorkCalendar(displayNodeCalendar(workPlan.getWorkCalendarIDX(), node.getWorkCalendarIDX()));
            task.setRealStart(realStartDate);
            task.setRealFinish(realEndDate);
            
            String realGanttStartDate = "";
            if (node.getRealBeginTime() != null)
                realGanttStartDate = new SimpleDateFormat(GANTTFORMATSTR).format(node.getRealBeginTime());
            String realGanttEndDate = "";
            if (node.getRealEndTime() != null)
                realGanttEndDate = new SimpleDateFormat(GANTTFORMATSTR).format(node.getRealEndTime());
            if (!StringUtil.isNullOrBlank(realGanttStartDate) && !StringUtil.isNullOrBlank(realGanttEndDate) && (node.getIsLeaf() != null && node.getIsLeaf() == JobProcessNodeDef.CONST_INT_IS_LEAF_YES)) {
                Baseline[] baselines = new Baseline[1];
                Baseline baseline = new Baseline();
                baseline.setStart(realGanttStartDate);
                baseline.setFinish(realGanttEndDate);
                baselines[0] = baseline;
                task.setBaseline(baselines);
            }
            if (node.getIsLeaf() != null && node.getIsLeaf() == JobProcessNodeDef.CONST_INT_IS_LEAF_NO) 
                task.setExpanded(getTaskExpanded(node.getIdx()));
            else {
                NodeCaseDelay nodeDelay = nodeCaseDelayManager.getEntityByNodeCaseIdx(node.getIdx());
                if (nodeDelay != null && !StringUtil.isNullOrBlank(nodeDelay.getDelayReason()))
                    task.setDelayReason(nodeDelay.getDelayReason());
            }
            taskList.add(task);
            nodeList.add(node);
            k++;
            // 查询下级节点
            if (node.getIsLeaf() != null && node.getIsLeaf() == JobProcessNodeDef.CONST_INT_IS_LEAF_NO)
                i = nextNodeList(node.getIdx(), 
                                 nodeOutlineNumber, 
                                 i, 
                                 workPlan, 
                                 durationFormat, 
                                 milestone,
                                 taskList, 
                                 nodeList);
        }
        // 构造任务数组
        Task[] tasks = new Task[taskList.size()];
        taskList.toArray(tasks);
        tasks = addPredecessorLinks(nodeList, tasks, workPlan.getIdx());// 设置任务的前置任务数组
        tasks = addPredecessorLinkStr(nodeList, tasks, workPlan.getIdx());// 设置任务的前置任务编号字符串
        return tasks;
    }
    
    /**
     * <li>说明：递归查出下级节点信息并保存至任务数组中
     * <li>创建人：程锐
     * <li>创建日期：2016-1-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 父节点ID
     * @param outlineNumber wbs
     * @param i wbs父节点值
     * @param workPlan 作业计划实体
     * @param durationFormat 格式化工期
     * @param milestone 里程碑
     * @param taskList 任务列表
     * @param nodeList 节点列表
     * @return wbs父节点值
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public int nextNodeList(String parentIDX, 
                            String outlineNumber, 
                            int i, 
                            TrainWorkPlanHis workPlan,
                            Integer durationFormat, 
                            Integer milestone, 
                            List<Task> taskList, 
                            List<JobProcessNodeHis> nodeList) throws Exception{
        List<JobProcessNodeHis> nextNodeList = getNodeListByWorkPlanForGantt(workPlan.getIdx(), parentIDX);
        if (nextNodeList != null && nextNodeList.size() > 0) {
            int k = 1;
            for (JobProcessNodeHis node : nextNodeList) {
                i++;
                String nodeOutlineNumber = outlineNumber + "." + k; 
                String nodeCaseName = node.getNodeName();
                String startDate = "";
                if (node.getPlanBeginTime() != null)
                    startDate = new SimpleDateFormat(GANTTFORMATSTR).format(node.getPlanBeginTime());
                String endDate = "";
                if (node.getPlanEndTime() != null)
                    endDate = new SimpleDateFormat(GANTTFORMATSTR).format(node.getPlanEndTime());                
                BigDecimal workDate = new BigDecimal("0"); //工期
                Integer percentComplete1 = 0;// 完成百分比
                if (node.getIsLeaf() == JobProcessNodeDef.CONST_INT_IS_LEAF_NO)                
                    percentComplete1 = calPercentCompleteByList(jobProcessNodeHisManager.getChildNodeList(node.getIdx()), node.getStatus());
                else
                    percentComplete1 = node.getStatus().equals(JobProcessNodeHis.STATUS_COMPLETE) ? 100 : 0;
                if (node.getRatedWorkMinutes() != null) {
                    Double ratedWorkMinutes = node.getRatedWorkMinutes() / 60;
                    workDate = new BigDecimal(ratedWorkMinutes.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                } 
                String realStartDate = "";
                if (node.getRealBeginTime() != null)
                    realStartDate = YYYY_MM_DD_HH_MM.format(node.getRealBeginTime());
                String realEndDate = "";
                if (node.getRealEndTime() != null)
                    realEndDate = YYYY_MM_DD_HH_MM.format(node.getRealEndTime());
                Task task = new Task(String.valueOf(i),
                                     nodeCaseName, 
                                     nodeOutlineNumber, 
                                     startDate, 
                                     endDate,
                                     workDate.intValue(),
                                     durationFormat, 
                                     0, 
                                     milestone);
                task.setWorkDate(workDate);
                task.setNodeType(NODETYPE);
                task.setNodeCaseIdx(node.getIdx());
                task.setWorkTeam(node.getWorkStationBelongTeamName());
//                task.setRepairLineName(node.getRepairLineName());
                task.setWorkStationName(node.getWorkStationName());
                task.setPercentComplete(percentComplete1);
                task.setTecProcessCaseIDX(workPlan.getProcessIDX());
                task.setWorkStationIDX(node.getWorkStationIDX());
//                task.setCascadingIDX(null);
                task.setNodeIDX(node.getNodeIDX());
                task.setParentIdx(node.getParentIDX());
                task.setIsLastLevel(node.getIsLeaf());
                task.setStatus(node.getStatus());
                task.setProcessInfo(JobProcessNodeHis.getStatusMeaning(node.getStatus()));
//                task.setSourceType("");
                task.setRatedWorkMinutes(node.getRatedWorkMinutes());
                String planMode = "";
                if(JobProcessNode.PLANMODE_AUTO.equals(node.getPlanMode())){
                    planMode ="自动";
                }else if(JobProcessNode.PLANMODE_MUNAUL.equals(node.getPlanMode())){
                    planMode ="手动";
                }else {
                    planMode ="定时";
                }
                task.setPlanMode(planMode);
                task.setWorkCalendar(displayNodeCalendar(workPlan.getWorkCalendarIDX(), node.getWorkCalendarIDX()));
                task.setRealStart(realStartDate);
                task.setRealFinish(realEndDate);
                
                String realGanttStartDate = "";
                if (node.getRealBeginTime() != null)
                    realGanttStartDate = new SimpleDateFormat(GANTTFORMATSTR).format(node.getRealBeginTime());
                String realGanttEndDate = "";
                if (node.getRealEndTime() != null)
                    realGanttEndDate = new SimpleDateFormat(GANTTFORMATSTR).format(node.getRealEndTime());
                if (!StringUtil.isNullOrBlank(realGanttStartDate) && !StringUtil.isNullOrBlank(realGanttEndDate) && (node.getIsLeaf() != null && node.getIsLeaf() == JobProcessNodeDef.CONST_INT_IS_LEAF_YES)) {
                    Baseline[] baselines = new Baseline[1];
                    Baseline baseline = new Baseline();
                    baseline.setStart(realGanttStartDate);
                    baseline.setFinish(realGanttEndDate);
                    baselines[0] = baseline;
                    task.setBaseline(baselines);
                }
                if (node.getIsLeaf() != null && node.getIsLeaf() == JobProcessNodeDef.CONST_INT_IS_LEAF_NO) 
                    task.setExpanded(getTaskExpanded(node.getIdx()));
                else {
                    NodeCaseDelay nodeDelay = nodeCaseDelayManager.getEntityByNodeCaseIdx(node.getIdx());
                    if (nodeDelay != null && !StringUtil.isNullOrBlank(nodeDelay.getDelayReason()))
                        task.setDelayReason(nodeDelay.getDelayReason());
                }
                taskList.add(task);
                nodeList.add(node);        
                k++;
                if(node.getIsLeaf() != null && node.getIsLeaf() == JobProcessNodeDef.CONST_INT_IS_LEAF_NO)
                    i = nextNodeList(node.getIdx(), 
                                     nodeOutlineNumber, 
                                     i, 
                                     workPlan,  
                                     durationFormat, 
                                     milestone, 
                                     taskList, 
                                     nodeList);//递归
            }
        }  
        return i;
    }
    
    /**
     * <li>说明：根据作业计划获取其相关的节点列表(按计划开工完工时间排序),供甘特图使用
     * <li>创建人：程锐
     * <li>创建日期：2016-1-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @param parentIDX 父节点IDX
     * @return 节点列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private List<JobProcessNodeHis> getNodeListByWorkPlanForGantt(String workPlanIDX, String parentIDX) throws Exception {
        StringBuffer hql = new StringBuffer("from JobProcessNodeHis where recordStatus = 0 and workPlanIDX = '").append(workPlanIDX).append("'");
        if (StringUtil.isNullOrBlank(parentIDX)) {
            hql.append(" and parentIDX is null");
        } else {
            hql.append(" and parentIDX  = '").append(parentIDX).append("'");
        }
        hql.append(" order by planBeginTime, seqNo asc");
        return daoUtils.find(hql.toString());
    }
    
    /**
     * <li>说明：根据节点的完成数计算流程的完成百分比
     * <li>创建人：程锐
     * <li>创建日期：2016-1-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 节点列表
     * @return 流程的完成百分比
     */
    @SuppressWarnings("unchecked")
    public Integer calPercentCompleteByList(List<JobProcessNodeHis> list) {
        Integer percentComplete = 0;
        if (list == null || list.size() < 1)
            return 100;
        int count = 0;
        for (JobProcessNodeHis node : list) {
            if (JobProcessNodeHis.STATUS_COMPLETE.equals(node.getStatus()))
                count++;
        }
        percentComplete = count * 100 / list.size();
        return percentComplete;
    }
    
    /**
     * <li>说明：根据下级节点的完成数计算节点的完成百分比
     * <li>创建人：程锐
     * <li>创建日期：2016-1-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param list 下级节点列表
     * @param status 节点状态
     * @return 节点的完成百分比
     */
    @SuppressWarnings("unchecked")
    public Integer calPercentCompleteByList(List<JobProcessNodeHis> list, String status) {
        Integer percentComplete = 0;
        if (list == null || list.size() < 1)
            return status.equals(JobProcessNodeHis.STATUS_COMPLETE) ? 100 : 0;
        int count = 0;
        for (JobProcessNodeHis node : list) {
            if (JobProcessNodeHis.STATUS_COMPLETE.equals(node.getStatus()))
                count++;
        }
        percentComplete = count * 100 / list.size();
        return percentComplete;
    }
    
    /**
     * <li>说明：获取工作日历名称
     * <li>创建人：程锐
     * <li>创建日期：2016-1-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCalendarIDX 工作日历IDX
     * @return 工作日历名称
     */
    private String getWorkCalendar(String workCalendarIDX) {
        if (StringUtil.isNullOrBlank(workCalendarIDX))
            return "";
        WorkCalendarInfo workCalendarInfo = workCalendarInfoManager.getModelById(workCalendarIDX);
        if (workCalendarInfo == null)
            return "";
        return workCalendarInfo.getCalendarName();
    }
    
    /**
     * <li>说明：甘特图上显示节点的工作日历：如节点工作日历和作业计划工作日历一样，则不显示
     * <li>创建人：程锐
     * <li>创建日期：2016-1-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planCalendarIDX 作业计划工作日历 
     * @param nodeCalendarIDX 节点工作日历
     * @return 甘特图上显示节点的工作日历
     */
    private String displayNodeCalendar(String planCalendarIDX, String nodeCalendarIDX) {
        if (StringUtil.isNullOrBlank(nodeCalendarIDX)) 
            return "";
        if (planCalendarIDX.equals(nodeCalendarIDX))
            return "";
        return getWorkCalendar(nodeCalendarIDX);
    }
    
    /**
     * <li>说明：根据节点IDX判断是否展开节点
     * <li>创建人：程锐
     * <li>创建日期：2016-1-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIdx 节点IDX
     * @return false 不展开节点， true 展开节点
     */
    private boolean getTaskExpanded(String nodeIdx) {
        if (!StringUtil.isNullOrBlank(nodeIDX)) {
            List<JobProcessNodeHis> childList = jobProcessNodeHisManager.getAllFirstChildNodeList(nodeIdx);
            List<JobProcessNodeHis> parentList = jobProcessNodeHisManager.getAllParentNodeList(nodeIdx);
            List<JobProcessNodeHis> allList = new ArrayList<JobProcessNodeHis>();
            allList.addAll(childList);
            allList.addAll(parentList);
            for (JobProcessNodeHis node2 : allList) {
                if (nodeIDX.equals(node2.getIdx())) {
                    return true;
                }
            }                    
        }
        return false;
    }
    /**
     * <li>说明：构造甘特图-设置任务的前置任务数组
     * <li>创建人：程锐
     * <li>创建日期：2016-1-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeList 节点列表
     * @param tasks 任务实体对象数组
     * @param workPlanIDX 作业计划IDX
     * @return 任务的前置任务数组
     */
    public Task[] addPredecessorLinks(List<JobProcessNodeHis> nodeList, Task[] tasks, String workPlanIDX) {
        if (nodeList == null || nodeList.size() < 1)
            return tasks;
        for (JobProcessNodeHis node : nodeList) {
            List<JobProcessNodeHis> preNodeCaseList = jobProcessNodeHisManager.getPreNodeCaseList(node.getIdx(), workPlanIDX);
            PredecessorLink[] predecessorLinks = getPredecessorLinks(preNodeCaseList, tasks);
            if (predecessorLinks == null)
                continue;
            for (Task task : tasks) {
                if (!StringUtil.isNullOrBlank(task.getNodeCaseIdx()) && task.getNodeCaseIdx().equals(node.getIdx())
                    && NODETYPE.equals(task.getNodeType())) {
                    task.setPredecessorLink(predecessorLinks);
                }
            }
        }
        return tasks;
    }

    /**
     * <li>说明：构造甘特图-设置任务的前置任务序号
     * <li>创建人：程锐
     * <li>创建日期：2016-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeList 节点列表
     * @param tasks 任务实体对象数组
     * @param workPlanIDX 作业计划IDX
     * @return 任务的前置任务序号
     */
    public Task[] addPredecessorLinkStr(List<JobProcessNodeHis> nodeList, Task[] tasks, String workPlanIDX) {
        if (nodeList == null || nodeList.size() < 1)
            return tasks;
        for (JobProcessNodeHis node : nodeList) {
            List<JobProcessNodeHis> preNodeCaseList = jobProcessNodeHisManager.getPreNodeCaseList(node.getIdx(), workPlanIDX);
            String precessorLinkStr = getPrecessorLinkStr(preNodeCaseList, tasks);
            if (StringUtil.isNullOrBlank(precessorLinkStr))
                continue;
            for (Task task : tasks) {
                if (!StringUtil.isNullOrBlank(task.getNodeCaseIdx()) && task.getNodeCaseIdx().equals(node.getIdx())
                    && NODETYPE.equals(task.getNodeType())) {
                    task.setPredecessorLinkStr(precessorLinkStr);
                }
            }
        }
        return tasks;
    }
    /**
     * <li>说明：构造甘特图-获取前置任务序号
     * <li>创建人：程锐
     * <li>创建日期：2016-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeList 节点列表
     * @param tasks 任务实体对象数组
     * @return 前置任务序号
     */
    public String getPrecessorLinkStr(List<JobProcessNodeHis> nodeList, Task[] tasks) {
        StringBuffer precessorLinkStr = new StringBuffer();
        if (nodeList == null || nodeList.size() < 1)
            return null;
        for (JobProcessNodeHis nodeCase : nodeList) {
            for (Task task : tasks) {
                if (StringUtil.isNullOrBlank(task.getNodeCaseIdx()))
                    continue;
                if (task.getNodeCaseIdx().equals(nodeCase.getIdx()) && NODETYPE.equals(task.getNodeType())) {                    
                    precessorLinkStr.append(task.getUID()).append(Constants.JOINSTR);
                }
            }
        }
        if (precessorLinkStr.toString().endsWith(Constants.JOINSTR)) {
            precessorLinkStr.deleteCharAt(precessorLinkStr.length() - 1);
        }
        return precessorLinkStr.toString();
    }
    
    /**
     * <li>说明：构造甘特图-获取前置任务数组
     * <li>创建人：程锐
     * <li>创建日期：2016-1-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeList 节点列表
     * @param tasks 任务实体对象数组
     * @return 前置任务数组
     */
    public PredecessorLink[] getPredecessorLinks(List<JobProcessNodeHis> nodeList, Task[] tasks) {
        List<PredecessorLink> list = new ArrayList<PredecessorLink>();
        if (nodeList == null || nodeList.size() < 1)
            return null;
        for (JobProcessNodeHis nodeCase : nodeList) {
            for (Task task : tasks) {
                if (StringUtil.isNullOrBlank(task.getNodeCaseIdx()))
                    continue;
                if (task.getNodeCaseIdx().equals(nodeCase.getIdx()) && NODETYPE.equals(task.getNodeType())) {
                    PredecessorLink predecessorLink = new PredecessorLink();
                    predecessorLink.setType(1);
                    predecessorLink.setPredecessorUID(task.getUID());// 设置序号
                    list.add(predecessorLink);
                }
            }
        }
        if (list.size() > 0) {
            PredecessorLink[] predecessorLinks = new PredecessorLink[list.size()];
            list.toArray(predecessorLinks);
            return predecessorLinks;
        }
        return null;
    }
}
