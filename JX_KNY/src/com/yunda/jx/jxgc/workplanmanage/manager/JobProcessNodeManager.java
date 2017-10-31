package com.yunda.jx.jxgc.workplanmanage.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarDetailManager;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarInfoUtil;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.common.JxgcConstants;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation;
import com.yunda.jx.jxgc.dispatchmanage.manager.WorkStationManager;
import com.yunda.jx.jxgc.processdef.entity.JobNodeExtConfigDef;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.manager.Dispatcher4WorkCardManager;
import com.yunda.jx.jxgc.workplanmanage.entity.JobNodeExtConfig;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeRel;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.pjjx.base.wpdefine.entity.WPNode;
import com.yunda.jx.util.CalcWorkDateUtil;
import com.yunda.jx.util.PerformanceMonitor;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
import com.yunda.twt.twtinfo.entity.SiteStation;
import com.yunda.twt.twtinfo.manager.SiteStationManager;
import com.yunda.twt.webservice.client.IMoveTrainService;
import com.yunda.util.BeanUtils;
import com.yunda.webservice.common.util.DefaultUserUtilManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：JobProcessNode业务类,机车检修计划流程节点
 * <li>创建人：程锐
 * <li>创建日期：2015-04-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */
@Service(value = "jobProcessNodeManager")
public class JobProcessNodeManager extends JXBaseManager<JobProcessNode, JobProcessNode> implements IbaseCombo{
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 机车检修作业计划-前置节点业务类 */
    @Resource
    private JobProcessNodeRelManager jobProcessNodeRelManager;
    
    /** 机车检修作业计划-流程节点查询业务类 */
    @Resource
    private JobProcessNodeQueryManager jobProcessNodeQueryManager;
    
    /** 机车检修作业计划-计算流程节点工期及计划完工时间业务类 */
    @Resource
    private CalcNodeWorkDateManager calcNodeWorkDateManager;
    
    /** 机车检修作业计划查询业务类 */
    @Resource
    private TrainWorkPlanQueryManager trainWorkPlanQueryManager;
    
    /** 流程节点扩展配置业务类 */
    @Resource
    private JobNodeExtConfigManager jobNodeExtConfigManager;
    
    /** 流程节点卡控业务类 */
    @Resource
    private JobNodeCtrlManager jobNodeCtrlManager;
    
    /** 机车出入段业务类 */
    @Resource
    private TrainAccessAccountManager trainAccessAccountManager;
    
    /** 机车出入段查询业务类 */
    @Resource
    private TrainAccessAccountQueryManager trainAccessAccountQueryManager;
    
    /** 流水线排程业务类 */
    @Resource    
    private RepairLineGroupNewManager repairLineGroupNewManager;
    
    /** 台位图移动机车业务类 */
    @Autowired    
    private IMoveTrainService moveTrainService;
    
    /** 作业工位业务类 */
    @Resource 
    private WorkStationManager workStationManager;
    
    /** 派工业务类 */
    @Resource
    private Dispatcher4WorkCardManager dispatcher4WorkCardManager;

    /** 台位业务类 */
    @Resource
    private SiteStationManager siteStationManager;
    
    /** queryHql常量字符串 */
    private static final String QUERYHQL = "queryHql";
    
    private static final String STATUS = "#status#";
    
    /**
     * <li>说明：生成作业计划-生成流程节点及前置关系
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 作业计划实体
     * @throws Exception
     */
    public void saveNodeAndSeq(TrainWorkPlan workPlan) throws Exception {
        PerformanceMonitor.begin(logger, false, "JobProcessNodeManager.saveNode");
        saveNode(workPlan, "ROOT_0", "");
        PerformanceMonitor.end(logger, "【生成节点】", false, "JobProcessNodeManager.saveNode");
        daoUtils.flush();
        List<JobProcessNode> nodeList = jobProcessNodeQueryManager.getNodeListByWorkPlan(workPlan.getIdx());
        saveSeq(workPlan, nodeList);
        jobNodeExtConfigManager.saveByNode(nodeList);
    }
    
    /**
     * <li>说明：新增编辑流程节点及前置关系
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param node 流程节点实体
     * @param rels 节点前置关系实体数组
     * @throws Exception
     */
    public void editNodeAndRel(JobProcessNode node, JobProcessNodeRel[] rels) throws Exception {
        boolean isDispatch = true;
        if (StringUtil.isNullOrBlank(node.getIdx())) {
            node.setStatus(JobProcessNode.STATUS_UNSTART);
            node.setIsLeaf(JobProcessNodeDef.CONST_INT_IS_LEAF_YES);            
            saveOrUpdate(node);
        } else {
            JobProcessNode oldNode = jobProcessNodeQueryManager.getModelById(node.getIdx());
            if (dispatcher4WorkCardManager.isSameDispatch(oldNode, node)) 
                isDispatch = false;
            saveOrUpdate(node);
        }
        if (isDispatch)
            repairLineGroupNewManager.updateForDispatch(node);
        if (!StringUtil.isNullOrBlank(node.getParentIDX())) {
            String sql = SqlMapUtil.getSql("jxgc-processNode:changeNodeIsLeaf")
                                   .replace("#nodeIDX#", node.getParentIDX())
                                   .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                                   .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                                   .replace("#IS_LEAF_NO#", String.valueOf(JobProcessNodeDef.CONST_INT_IS_LEAF_NO))
                                   .replace("#IS_LEAF_YES#", String.valueOf(JobProcessNodeDef.CONST_INT_IS_LEAF_YES));
            daoUtils.executeSql(sql);
        }
        
        if (node.getIsLeaf() == JobProcessNodeDef.CONST_INT_IS_LEAF_NO && !StringUtil.isNullOrBlank(node.getWorkCalendarIDX())) {
            List<JobProcessNode> allChildNodeList = jobProcessNodeQueryManager.getAllChildNodeExceptThisList(node.getIdx());
            if (allChildNodeList != null && allChildNodeList.size() > 0) {
                for (JobProcessNode childNode : allChildNodeList) {
                    childNode.setWorkCalendarIDX(node.getWorkCalendarIDX());
                }
                saveOrUpdate(allChildNodeList);
            }            
        }
        
        String nodeIDX = node.getIdx();
        jobProcessNodeRelManager.saveByNode(nodeIDX, rels);
        node.setThisChange(true);
        calcNodeWorkDateManager.updatePlanBeginEndTime(node, false, false);
        TrainWorkPlan workPlan = trainWorkPlanQueryManager.getModelById(node.getWorkPlanIDX());
        trainWorkPlanQueryManager.updateWorkPlanBeginEndTime(workPlan);
    }
    
    /**
     * <li>说明：生成作业计划-批量设置节点的工位
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDXSStr 流程节点IDX字符串
     * @param workStationIDX 工位主键
     * @param workStationName 工位名称
     * @return 执行条数
     */
    public int updateBatchNodeForWorkStation(String nodeIDXSStr, String workStationIDX, String workStationName) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:updateBatchNodeForWorkStation")
                               .replace("#updateTime#", DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace("#workStationIDX#", workStationIDX)
                               .replace("#workStationName#", workStationName)
                               .replace("#IDXS#", nodeIDXSStr);
        return daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：生成作业计划-批量设置节点的作业班组
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDXSStr 流程节点IDX字符串
     * @param teamId 作业班组ID
     * @param teamName 作业班组名称
     * @return 执行条数
     */
    public int updateBatchNodeForWorkTeam(String nodeIDXSStr, Long teamId, String teamName) {
        String sql = SqlMapUtil.getSql("jxgc-processNode:updateBatchNodeForWorkTeam")
                               .replace("#updateTime#", DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace("#teamId#", String.valueOf(teamId))
                               .replace("#teamName#", teamName)
                               .replace("#IDXS#", nodeIDXSStr);
        return daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：删除流程节点
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 错误信息
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String delNode(String nodeIDX) throws Exception {
        JobProcessNode node = getModelById(nodeIDX);
        String workPlanIDX = node.getWorkPlanIDX();
        List<JobProcessNode> list = jobProcessNodeQueryManager.getAllChildNodeList(nodeIDX);
        if (list == null || list.size() < 1)
            return "可删除的流程节点为空";
        String nodeIDXSStr = buildSqlIDXStr(list);
        if (StringUtil.isNullOrBlank(nodeIDXSStr))
            return "可删除的流程节点idx为空";
        handleNextNodeRelForDel(list);
        String sql = SqlMapUtil.getSql("jxgc-processNode:logicDeleteNode")
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                               .replace(JxgcConstants.IDXS, nodeIDXSStr);
        daoUtils.executeSql(sql);
        
        //用于超链接节点树，当删除当前节点的时候，判断父节点是否没有子节点，如果没有子节点，更新isleaf为1
        //获取当前父节点下的所有子节点list
//      获取父节点信息
        if (node != null && StringUtils.isNotBlank(node.getParentIDX())) {
            List<JobProcessNode> listParent = jobProcessNodeQueryManager.getAllChildNodeExceptThisList(node.getParentIDX());
            if (listParent != null && listParent.size() == 0) {
                /* 是否叶子节点,0:否；1：是 */
                JobProcessNode parentNode = getModelById(node.getParentIDX());
                parentNode.setIsLeaf(1);
                update(parentNode);
            }
        }
        
        
        sql = SqlMapUtil.getSql("jxgc-processNode:logicDeleteNodeRel")
                        .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                        .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                        .replace(JxgcConstants.IDXS, nodeIDXSStr);
        daoUtils.executeSql(sql);
        
        
        sql = SqlMapUtil.getSql("jxgc-processNode:logicDeleteWorkCard")
                        .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                        .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                        .replace(JxgcConstants.IDXS, nodeIDXSStr);
        daoUtils.executeSql(sql);
        sql = SqlMapUtil.getSql("jxgc-processNode:logicDeleteQC")
                        .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                        .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                        .replace(JxgcConstants.IDXS, nodeIDXSStr);
        daoUtils.executeSql(sql);
        sql = SqlMapUtil.getSql("jxgc-processNode:logicDeleteWorkTask")
                        .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                        .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                        .replace(JxgcConstants.IDXS, nodeIDXSStr);
        daoUtils.executeSql(sql);
        sql = SqlMapUtil.getSql("jxgc-processNode:logicDeleteDetect")
                        .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                        .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                        .replace(JxgcConstants.IDXS, nodeIDXSStr);
        daoUtils.executeSql(sql);        
        TrainWorkPlan workPlan = trainWorkPlanQueryManager.getModelById(workPlanIDX);
        calcNodeWorkDateManager.updatePlanTimeByWorkPlan(workPlan); 
        // 重新计算作业计划的时间
        trainWorkPlanQueryManager.updateWorkPlanBeginEndTime(workPlan);
        return null;
    }
    
    /**
     * <li>说明：启动作业计划-启动相关节点
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @throws Exception
     */
    public void startWorkPlan(String workPlanIDX) throws Exception {
        List<JobProcessNode> list = jobProcessNodeQueryManager.getFirstNodeListByWorkPlan(workPlanIDX);
        if (list == null || list.size() < 1)
            return;
        for (JobProcessNode node : list) {
            if (notAutoNode(node))
                continue;
            startNode(node.getIdx());
        }
    }
    
    /**
     * <li>说明：启动流程节点
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 错误信息
     * @throws Exception
     */
    public String startNode(String nodeIDX) throws Exception {
        List<JobProcessNode> childList = jobProcessNodeQueryManager.getAllFirstChildNodeList(nodeIDX);
        List<JobProcessNode> parentList = jobProcessNodeQueryManager.getAllParentNodeList(nodeIDX);
        List<JobProcessNode> list = new ArrayList<JobProcessNode>();
        for (JobProcessNode childNode : childList) {
            if (!childNode.getIdx().equals(nodeIDX) && (notAutoNode(childNode) || !JobProcessNode.STATUS_UNSTART.equals(childNode.getStatus())))
                continue;
           
            list.add(childNode);
        }

        for (JobProcessNode parentNode : parentList) {   
            boolean hasThisNode = false;
        	for (JobProcessNode node : list) {
        		if (node.getIdx().equals(nodeIDX) && parentNode.getIdx().equals(nodeIDX))
        			hasThisNode = true;
			}
        	if (hasThisNode)
        		continue;
        	hasThisNode = false;
            if (!JobProcessNode.STATUS_UNSTART.equals(parentNode.getStatus()))
                continue; 
            list.add(parentNode);
        }
        if (list == null || list.size() < 1)
            return "待启动的流程节点为空";   
        String nodeIDXSStr = buildSqlIDXStr(list);
        String sql = SqlMapUtil.getSql("jxgc-processNode:updateNodeStatusForStart")
                               .replace(STATUS, JobProcessNode.STATUS_GOING)
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                               .replace(JxgcConstants.IDXS, nodeIDXSStr);
        daoUtils.executeSql(sql);
        sql = SqlMapUtil.getSql("jxgc-processNode:startWorkCardByNode")
                        .replace(STATUS, WorkCard.STATUS_HANDLING)
                        .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                        .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                        .replace(JxgcConstants.IDXS, nodeIDXSStr);
        daoUtils.executeSql(sql);
        moveTrain(nodeIDX);//移动机车
        changeTrainStatus(nodeIDX);//更改机车状态
        //本节点和所有下级节点无初始化和处理中的作业工单的节点自动完成
        if (!jobProcessNodeQueryManager.hasWorkCardOfAllChildNode(nodeIDX)) {
            JobProcessNode node = getModelById(nodeIDX);
            node.setRealBeginTime(new Date());
            updateFinishNodeCase(node);
            completeAllChildNodeList(nodeIDX, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));
        } else {
            //下面第一层节点中无初始化和处理中的作业工单的节点自动完成
            List<JobProcessNode> childNodeExceptThisList = jobProcessNodeQueryManager.getAllChildNodeExceptThisList(nodeIDX);
            for (JobProcessNode node : childNodeExceptThisList) {
                if (!jobProcessNodeQueryManager.hasWorkCardOfAllChildNode(node.getIdx())) {
                    node.setRealBeginTime(new Date());
                    updateFinishNodeCase(node);
                    completeAllChildNodeList(node.getIdx(), DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss));
                }                    
            }
        }
        return null;
    }
    
    /**
     * <li>说明：定时启动节点（定时启动模式为定时，当前时间之后的十分钟的节点）
     * <li>创建人：程锐
     * <li>创建日期：2015-8-27
     * <li>修改人： 张迪
     * <li>修改日期：2016-7-15
     * <li>修改内容：修改启动模式为定时
     */
    public void startManualNode() {
         String errorMessage = "";
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            StringBuilder sql = new StringBuilder("SELECT T.IDX, TO_CHAR(T.PLAN_BEGIN_TIME, 'yyyy-mm-dd hh24:mi:ss'), T.PLAN_BEGIN_TIME\n")
                                          .append("  FROM JXGC_JOB_PROCESS_NODE T\n") 
                                          .append(" WHERE T.RECORD_STATUS = 0\n" ) 
                                          .append("   AND T.PLAN_MODE = '" + JobProcessNode.PLANMODE_TIMER + "'\n") 
                                          .append("   AND T.STATUS = '" + JobProcessNode.STATUS_UNSTART + "'\n") 
                                          .append("   AND T.PLAN_BEGIN_TIME <= TO_DATE('")
                                          .append(DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss)) 
                                          .append("', 'yyyy-mm-dd hh24:mi:ss') + 10/60/24");
            List list = daoUtils.executeSqlQuery(sql.toString());//得到计划开始时间早于或等于晚于当前时间十分钟的定时计划模式的节点IDX列表
            if (list == null || list.size() < 1) 
                return;
            for (int i = 0; i < list.size(); i++) {
                Object[] objs = (Object[]) list.get(i);
                String nodeIDX = objs[0].toString();
                String planBeginTimeStr = objs[1].toString();
                Date planBeginTime = (Date) objs[2];
                errorMessage = startManualNode(nodeIDX, planBeginTimeStr, planBeginTime);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errorMessage);
        }
    }
    
    /**
     * <li>说明：定时启动节点（手工计划模式及计划开始时间在当前时间之前的节点）
     * <li>说明：待业务明确，如定时启动节点逻辑与自动启动节点逻辑一致则与startNode方法合并重构
     * <li>创建人：程锐
     * <li>创建日期：2015-8-27
     * <li>修改人： 张迪
     * <li>修改日期：2016-7-17
     * <li>修改内容： 修改定时启动时启动模式为'定时'的节点
     * @param nodeIDX 节点IDX
     * @param planBeginTimeStr 计划开始时间字符串
     * @param planBeginTime 计划开始时间
     * @return 错误信息
     * @throws Exception
     */
    private String startManualNode(String nodeIDX, String planBeginTimeStr, Date planBeginTime) throws Exception {
        JobProcessNode oldNode = getModelById(nodeIDX);
        TrainWorkPlan workPlan = trainWorkPlanQueryManager.getModelById(oldNode.getWorkPlanIDX());
        if (!TrainWorkPlan.STATUS_HANDLING.equals(workPlan.getWorkPlanStatus()))
            return "待启动的节点关联的作业计划未启动";
        DefaultUserUtilManager.setDefaultOperator();
        return startNode(nodeIDX);
//        List<JobProcessNode> childList = jobProcessNodeQueryManager.getAllFirstChildNodeList(nodeIDX);
//        List<JobProcessNode> parentList = jobProcessNodeQueryManager.getAllParentNodeList(nodeIDX);
//        List<JobProcessNode> list = new ArrayList<JobProcessNode>();
//        for (JobProcessNode childNode : childList) {
//            if (!childNode.getIdx().equals(nodeIDX) &&(notAutoNode(childNode) || !JobProcessNode.STATUS_UNSTART.equals(childNode.getStatus())))
//                continue;
//            list.add(childNode);
//        }
//        for (JobProcessNode parentNode : parentList) {
//            if (notAutoNode(parentNode) || !JobProcessNode.STATUS_UNSTART.equals(parentNode.getStatus()))
//                continue;
//            list.add(parentNode);
//        }
//        if (list == null || list.size() < 1)
//            return "待启动的流程节点为空";
//        DefaultUserUtilManager.setDefaultOperator();
//        String nodeIDXSStr = buildSqlIDXStr(list);
//        String sql = SqlMapUtil.getSql("jxgc-processNode:updateNodeStatusForStart")
//                               .replace(STATUS, JobProcessNode.STATUS_GOING)
//                               .replace(JxgcConstants.UPDATETIME, planBeginTimeStr)
//                               .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
//                               .replace(JxgcConstants.IDXS, nodeIDXSStr);
//        daoUtils.executeSql(sql);
//        sql = SqlMapUtil.getSql("jxgc-processNode:startWorkCardByNode")
//                        .replace(STATUS, WorkCard.STATUS_HANDLING)
//                        .replace(JxgcConstants.UPDATETIME, planBeginTimeStr)
//                        .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
//                        .replace(JxgcConstants.IDXS, nodeIDXSStr);
//        daoUtils.executeSql(sql);
//        moveTrain(nodeIDX);//移动机车
//        changeTrainStatus(nodeIDX);//更改机车状态
//        //本节点和所有下级节点无初始化和处理中的作业工单的节点自动完成
//        if (!jobProcessNodeQueryManager.hasWorkCardOfAllChildNode(nodeIDX)) {
//            JobProcessNode node = getModelById(nodeIDX);
//            node.setRealBeginTime(planBeginTime);
//            updateFinishNodeCase(node);
//            completeAllChildNodeList(nodeIDX, planBeginTimeStr);
//        } else {
//            //下面第一层节点中无初始化和处理中的作业工单的节点自动完成
//            List<JobProcessNode> childNodeExceptThisList = jobProcessNodeQueryManager.getAllChildNodeExceptThisList(nodeIDX);
//            for (JobProcessNode node : childNodeExceptThisList) {
//                if (!jobProcessNodeQueryManager.hasWorkCardOfAllChildNode(node.getIdx())) {
//                    node.setRealBeginTime(planBeginTime);
//                    updateFinishNodeCase(node);
//                    completeAllChildNodeList(node.getIdx(), planBeginTimeStr);
//                }                    
//            }
//        }
//        return null;
    }
    
    /**
     * <li>说明：完成无初始化和处理中工单的所有子节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-5-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @param beginTimeStr 开始时间字符串
     * @throws Exception
     */
    public void completeAllChildNodeList(String nodeIDX, String beginTimeStr) throws Exception {
        List<JobProcessNode> childNodeExceptThisList = jobProcessNodeQueryManager.getAllChildNodeExceptThisList(nodeIDX);
        String nodeIDXSStr = buildSqlIDXStr(childNodeExceptThisList);
        if (StringUtil.isNullOrBlank(nodeIDXSStr))
            return;
        String sql = SqlMapUtil.getSql("jxgc-processNode:updateNodeStatusForEnd")
                                .replace(STATUS, JobProcessNode.STATUS_COMPLETE)
                                .replace(JxgcConstants.UPDATETIME, beginTimeStr)
                                .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                                .replace(JxgcConstants.IDXS, nodeIDXSStr);
        daoUtils.executeSql(sql);
        JobProcessNode node = getModelById(nodeIDX);
        completeWorkPlan(node.getWorkPlanIDX());
    }
    
    /**
     * <li>说明：得到本节点及所有子节点的节点IDX，以,号分隔的节点IDX的sql字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-5-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 以,号分隔的节点IDX的sql字符串
     * @throws Exception
     */
    public String getAllChildNodeSqlIDXSStr(String nodeIDX) throws Exception {
        return buildSqlIDXStr(jobProcessNodeQueryManager.getAllChildNodeList(nodeIDX));
    }
    
    /**
     * <li>说明：得到本节点及之前的所有子节点的节点IDX，以,号分隔的sql字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-5-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 以,号分隔的节点IDX的sql字符串
     * @throws Exception
     */
    public String getAllPreNodeSqlIDXSStr(String nodeIDX) throws Exception {
        return buildSqlIDXStr(jobProcessNodeQueryManager.getAllPreNodeList(nodeIDX));
    }
    
    /**
     * <li>说明：根据流程节点列表构造流程节点IDX为以,号分隔的sql字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 流程节点列表
     * @return 以,号分隔的节点IDX的sql字符串
     * @throws Exception
     */
    public String buildSqlIDXStr(List<JobProcessNode> list) throws Exception {
        StringBuilder idxs = new StringBuilder();
        if (list == null || list.size() < 1)
            return "";
        for (JobProcessNode node : list) {
            idxs.append(node.getIdx()).append(Constants.JOINSTR);
        }
        idxs.deleteCharAt(idxs.length() - 1);
        return CommonUtil.buildInSqlStr(idxs.toString());
    }
    
    /**
     * <li>说明：更新节点的层级关系
     * <li>创建人：程锐
     * <li>创建日期：2015-5-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 需更新的节点IDX
     * @param changeNodeIDX 更新至的父节点IDX
     * @return 错误信息
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String changeNodeParent(String nodeIDX, String changeNodeIDX) throws Exception {
        JobProcessNode node = getModelById(nodeIDX);
        String workPlanIDX = node.getWorkPlanIDX();
        TrainWorkPlan workPlan = trainWorkPlanQueryManager.getModelById(workPlanIDX);
        if ("ROOT_0".equals(changeNodeIDX))
            changeNodeIDX = "";
        handleNextNodeRelForChange(nodeIDX);
        //移动节点到相应父节点
        String sql = SqlMapUtil.getSql("jxgc-processNode:changeNodeParent")
                               .replace("#changeNodeIDX#", changeNodeIDX)
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                               .replace("#nodeIDX#", nodeIDX);
        daoUtils.executeSql(sql);
        
        //更新移至的节点的是否子节点为否
        sql = SqlMapUtil.getSql("jxgc-processNode:changeNodeIsLeaf")
                        .replace("#nodeIDX#", changeNodeIDX)
                        .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                        .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                        .replace("#IS_LEAF_NO#", String.valueOf(JobProcessNodeDef.CONST_INT_IS_LEAF_NO))
                        .replace("#IS_LEAF_YES#", String.valueOf(JobProcessNodeDef.CONST_INT_IS_LEAF_YES));
        daoUtils.executeSql(sql);
        //删除节点的所有前置关系
        jobProcessNodeRelManager.updateForDeleteByNodeIDX(nodeIDX);
        
        calcNodeWorkDateManager.updatePlanTimeByWorkPlan(workPlan);
        // 重新计算作业计划的时间
        trainWorkPlanQueryManager.updateWorkPlanBeginEndTime(workPlan);
        return null;
    }
    
    /**
     * <li>说明：获取节点编辑-选择前置节点的数据
     * <li>创建人：程锐
     * <li>创建日期：2015-5-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param req HttpServletRequest对象
     * @param start 开始行
     * @param limit 每页记录数
     * @return map
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception {
        String queryParams = req.getParameter("queryParams");
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            queryParamsMap = JSONUtil.read(queryParams, Map.class);
        }
        String queryHql = CommonUtil.getMapValue(queryParamsMap, QUERYHQL);//自定义hql
        StringBuffer hql = new StringBuffer(); 
        if (!StringUtil.isNullOrBlank(queryHql))
            queryParamsMap.remove(QUERYHQL);
        String workPlanIDX = CommonUtil.getMapValue(queryParamsMap, "workPlanIDX");
        String parentIDX = CommonUtil.getMapValue(queryParamsMap, "parentIDX");
        String nodeIDX = CommonUtil.getMapValue(queryParamsMap, "nodeIDX");
        if (StringUtil.isNullOrBlank(queryHql)) {
            hql = new StringBuffer("from JobProcessNode where recordStatus = 0");            
            if (!StringUtil.isNullOrBlank(workPlanIDX)) {
                hql.append(" and workPlanIDX = '")
                   .append(workPlanIDX)
                   .append("'");
            }            
            if (!StringUtil.isNullOrBlank(parentIDX)) {
                hql.append(" and parentIDX = '")
                   .append(parentIDX)
                   .append("'");
            } else {
                hql.append(" and parentIDX is null");
            }            
            if (!StringUtil.isNullOrBlank(nodeIDX)) {
                hql.append(" and idx != '")
                   .append(nodeIDX)
                   .append("'")
                   .append(" and idx not in (select nodeIDX from JobProcessNodeRel where recordStatus = 0 and preNodeIDX = '")
                   .append(nodeIDX)
                   .append("' and nodeIDX is not null)")
                   .append(" and idx not in (select preNodeIDX from JobProcessNodeRel where recordStatus = 0 and nodeIDX = '")
                   .append(nodeIDX)
                   .append("' and preNodeIDX is not null)");
            }
        } else
            hql.append(queryHql);
        
        List<JobProcessNode> nodeList = daoUtils.find(hql.toString());
        List<JobProcessNode> pageList = new ArrayList<JobProcessNode>();
        List<JobProcessNode> nextNodeList = jobProcessNodeQueryManager.getNextNodeList(nodeIDX, new ArrayList<JobProcessNode>());
        for (JobProcessNode node : nodeList) {
            boolean isCurcle = false;
            for (JobProcessNode nextNode : nextNodeList) {
                if (node.getIdx().equals(nextNode.getIdx())) {
                    isCurcle = true;
                    break;
                }
            }
            if (!isCurcle)
                pageList.add(node);
        }   
        int pageSize = pageList.size() > (limit + start)? (limit + start) : pageList.size(); 
        List<JobProcessNode> newPageList = new ArrayList<JobProcessNode>();
        for(int i = start; i < pageSize; i++){
            newPageList.add(pageList.get(i));
        }
        return new Page(pageList.size(), newPageList).extjsStore();
    }
    
    /**
     * <li>说明：检查节点卡控（当前只有质量卡控），完成节点 
     * <li>创建人：程锐
     * <li>创建日期：2015-5-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param node 流程节点 
     * @return 卡控信息
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String updateFinishNodeCase(JobProcessNode node) throws Exception {
        JobNodeExtConfig config = jobNodeExtConfigManager.getQCConfig(node.getIdx());
        JobNodeExtConfig configTicket = jobNodeExtConfigManager.getTicketConfig(node.getIdx());
        Map controlMap = new HashMap<String, String>();
        Map operateMap = new HashMap<String, String>();
//        Map operateTicketMap = new HashMap<String, String>();
        String errMsg = "";
        if (config != null) {    // 质量检查卡控
            controlMap.put(JobNodeExtConfigDef.EXT_CHECK_CONTROL, config.getConfigValue());
            if (JobNodeExtConfigDef.EXT_CHECK_CONTROL_CURRENT.equals(config.getConfigValue())) {
                controlMap.put(JobNodeExtConfigDef.EXT_CHECK_CONTROL_CURRENT, getAllChildNodeSqlIDXSStr(node.getIdx()));
            }
            else if (JobNodeExtConfigDef.EXT_CHECK_CONTROL_ALL.equals(config.getConfigValue())) {
                controlMap.put(JobNodeExtConfigDef.EXT_CHECK_CONTROL_ALL, getAllPreNodeSqlIDXSStr(node.getIdx()));
            }
           
        }
        if(null != configTicket){ //  检查提票卡控
            controlMap.put(JobNodeExtConfigDef.EXT_CHECK_TICKET, configTicket.getConfigValue());
            if (JobNodeExtConfigDef.EXT_CHECK_TICKET_CURRENT.equals(configTicket.getConfigValue())) {
                controlMap.put(JobNodeExtConfigDef.EXT_CHECK_TICKET_CURRENT, getRdpIDX(node.getIdx()));
            }           
//            operateTicketMap = jobNodeCtrlManager.finishNodeCtrl(controlMap);
        }
        operateMap = jobNodeCtrlManager.finishNodeCtrl(controlMap);
        if ("true".equals(CommonUtil.getMapValue(operateMap, Constants.SUCCESS))) {
            finishNode(node.getIdx());
        } else {
            errMsg = CommonUtil.getMapValue(operateMap, Constants.ERRMSG);
        }
        if(null == config && null == configTicket){
            finishNode(node.getIdx());
        }
        completeWorkPlan(node.getWorkPlanIDX());
        return errMsg;
    }
    
    /**
     * <li>说明：完成流程节点并启动后置节点
     * <li>创建人：程锐
     * <li>创建日期：2015-5-8
     * <li>修改人：张迪
     * <li>修改日期：2016-7-17
     * <li>修改内容：完成流程节点并启动后置节点为自动模式的节点
     * @param nodeIDX 节点IDX
     * @throws Exception
     */
    public void finishNode(String nodeIDX) throws Exception {        
        
        JobProcessNode node = getModelById(nodeIDX);
        node.setStatus(JobProcessNode.STATUS_COMPLETE);
        node.setRealEndTime(new Date());
        node.setUpdateTime(new Date());
        node.setUpdator(SystemContext.getAcOperator().getOperatorid());
        updateRealWorkminutes(node); 
        
        //启动后置节点
        List<JobProcessNode> nextNodeList = jobProcessNodeQueryManager.getNextNodeList(node.getIdx());
        for (JobProcessNode nextNode : nextNodeList) {
            int unCompletePreNodes = jobProcessNodeQueryManager.getUnCompletePreNodesForNode(nextNode.getIdx());
            if (unCompletePreNodes > 0)
                continue;
            if (!JobProcessNode.STATUS_UNSTART.equals(nextNode.getStatus())) 
                continue;
            if (notAutoNode(nextNode))
                continue;
            startNode(nextNode.getIdx());
        }        
        if (!StringUtil.isNullOrBlank(node.getParentIDX())) {            
            List<JobProcessNode> unCompleteNodeListByParent = jobProcessNodeQueryManager.getUnCompleteNodeListByParent(node.getParentIDX());
            boolean canCompleteParent = true;
            for (JobProcessNode unCompleteNode : unCompleteNodeListByParent) {
                if (!unCompleteNode.getIdx().equals(node.getIdx())) {
                    canCompleteParent = false;
                    break;
                }
            }
            if (canCompleteParent) {
                JobProcessNode parentNode = getModelById(node.getParentIDX());
                if (parentNode.getRealBeginTime() != null)
                    updateFinishNodeCase(parentNode);
            }
        }        
    }
    
    /**
     * <li>说明：更新节点的实际工期
     * <li>创建人：程锐
     * <li>创建日期：2015-5-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param node 节点对象
     */
    public void updateRealWorkminutes(JobProcessNode node) {
        if (node != null) {
            Date realBeginTime = node.getRealBeginTime();
            Double realWorkminutes;
            try {
                if (realBeginTime != null) {
                    WorkCalendarInfoUtil wcInfoUtil = WorkCalendarInfoUtil.getInstance(realBeginTime);
                    wcInfoUtil.buildMap();
                    WorkCalendarDetailManager workCalendarDetailManager =
                        (WorkCalendarDetailManager) Application.getSpringApplicationContext().getBean("workCalendarDetailManager");
                    realWorkminutes = Double.valueOf(workCalendarDetailManager.getRealWorkminutes(realBeginTime, node.getRealEndTime(), node.getWorkCalendarIDX()) / (60000));                    
                    node.setRealWorkMinutes(realWorkminutes);
                    saveOrUpdate(node);
                }
            } catch (Exception e) {
                ExceptionUtil.process(e, logger);
            }
        }
    }
    
    /**
     * <li>说明：流水线排程-对单个节点派工
     * <li>创建人：程锐
     * <li>创建日期：2015-5-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 前台封装的实体对象
     * @return 未能派工的信息
     * @throws Exception
     */
    public String[] dispatchNode(WorkCard entity) throws Exception {
        JobProcessNode node = getModelById(entity.getNodeCaseIDX());
        String oldStation = node.getWorkStationIDX();
        String newStation = entity.getWorkStationIDX();
        Long oldTeam = node.getWorkStationBelongTeam();
        Long newTeam = entity.getWorkStationBelongTeam();
        if (dispatcher4WorkCardManager.isSameDispatch(oldStation, newStation, oldTeam, newTeam)) {
            String[] errMsg = new String[1];
            errMsg[0] = "流程节点【" + node.getNodeName() + "】：本次派工和上次一致，不用重派工！";
            return errMsg;
        }
        node.setWorkStationIDX(entity.getWorkStationIDX());
        node.setWorkStationName(entity.getWorkStationName());
        node.setWorkStationBelongTeam(entity.getWorkStationBelongTeam());
        node.setWorkStationBelongTeamName(entity.getWorkStationBelongTeamName());
        node.setUpdateTime(new Date());
        node.setUpdator(SystemContext.getAcOperator().getOperatorid());
        saveOrUpdate(node);
        return dispatcher4WorkCardManager.updateWorkCardByNode(entity);
    }
    
    /**
     * <li>说明：修改节点实际时间
     * <li>创建人：程锐
     * <li>创建日期：2015-8-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param node 节点对象
     * @throws Exception
     */
    public void updateRealTime(JobProcessNode node) throws Exception {
        JobProcessNode oldNode = getModelById(node.getIdx());
        oldNode.setRealBeginTime(node.getRealBeginTime() != null ? node.getRealBeginTime() : null);
        oldNode.setRealEndTime(node.getRealEndTime() != null ? node.getRealEndTime() : null);
        saveOrUpdate(oldNode);
    }
    
    /**
     * <li>说明：生成作业计划-根据流程节点定义递归新增流程节点
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 作业计划实体
     * @param parentNodeIDX 父节点定义IDX
     * @param parentIDX 父节点IDX
     * @throws Exception
     */
    private void saveNode(TrainWorkPlan workPlan, String parentNodeIDX, String parentIDX) throws Exception {
        List<JobProcessNodeDef> list = getSameProcessAndSameLevelList(workPlan.getProcessIDX(), parentNodeIDX);
        if (list == null || list.size() < 1) {
            JobProcessNode parentNode = getModelById(parentIDX);
            if (parentNode != null) {
                parentNode.setIsLeaf(JobProcessNodeDef.CONST_INT_IS_LEAF_YES);
                saveOrUpdate(parentNode);
            }
            return;
        }
        for (JobProcessNodeDef def : list) {
            JobProcessNode node = new JobProcessNode();
            BeanUtils.copyProperties(node, def);
            node.setIdx("");
            node.setNodeIDX(def.getIdx());
            node.setParentIDX(parentIDX);
            node.setWorkPlanIDX(workPlan.getIdx());
            node.setStatus(JobProcessNode.STATUS_UNSTART);
            saveOrUpdate(node);
            if (def.getIsLeaf() == JobProcessNodeDef.CONST_INT_IS_LEAF_NO)
                saveNode(workPlan, def.getIdx(), node.getIdx());
        }
    }
    
    /**
     * <li>说明：得到同一作业流程下同一级节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param processIDX 流程IDX
     * @param parentIDX 父节点IDX
     * @return 同一作业流程下同一级节点列表
     */
    @SuppressWarnings("unchecked")
    private List<JobProcessNodeDef> getSameProcessAndSameLevelList(String processIDX, String parentIDX) {
        String hql = SqlMapUtil.getSql("jxgc-processNode:getSameProcessAndSameLevelList")
                               .replace("#processIDX#", processIDX)
                               .replace("#parentIDX#", parentIDX);
        return daoUtils.find(hql);
    }
    
    /**
     * <li>说明：生成作业计划-根据流程前置节点定义新增流程前置节点并计算流程下所有节点的计划时间
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 作业计划实体
     * @param nodeList 作业计划关联的所有节点列表
     * @throws Exception
     */
    private void saveSeq(TrainWorkPlan workPlan, List<JobProcessNode> nodeList) throws Exception {  
        PerformanceMonitor.begin(logger, false, "jobProcessNodeRelManager.saveByNode");
        jobProcessNodeRelManager.saveByNode(workPlan, nodeList);
        PerformanceMonitor.end(logger, "【生成节点前置关系】", false, "jobProcessNodeRelManager.saveByNode");
        daoUtils.flush();
        calcNodeWorkDateManager.updatePlanTimeByWorkPlan(workPlan, true);
    }
    
    /**
     * <li>说明：启动节点时，根据节点扩展配置的机车状态修改机车入段台账的状态
     * <li>逻辑：根据本节点的最顶层父节点设置机车状态 ，如顶层父节点未设置状态，往下找此子节点的同源父节点的状态设置，找到配置就修改机车入段台账的状态，都没找到则找其下面启动的子节点，有设置就修改
     * <li>创建人：程锐
     * <li>创建日期：2015-5-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @throws Exception
     */
    private void changeTrainStatus(String nodeIDX) throws Exception {
        List<JobProcessNode> allParentNodeList = jobProcessNodeQueryManager.getAllParentNodeList(nodeIDX);
        String status = "";
        JobProcessNode firstParentNode = null;//本节点的最顶层父节点
        for (JobProcessNode parentNode : allParentNodeList) {
            if (StringUtil.isNullOrBlank(parentNode.getParentIDX())) {
                firstParentNode = parentNode;
                JobNodeExtConfig config = jobNodeExtConfigManager.getTrainStatusConfig(parentNode.getIdx());
                if (config != null && !StringUtil.isNullOrBlank(config.getConfigValue())) 
                    status = config.getConfigValue();      
                break;
            }
        }
        if (StringUtil.isNullOrBlank(status) && firstParentNode != null) {
            status = getStatusConfigByChildNode(status, firstParentNode, allParentNodeList);
        }
        
        if (StringUtil.isNullOrBlank(status)) {
            List<JobProcessNode> childList = jobProcessNodeQueryManager.getAllFirstChildNodeList(nodeIDX);
            for (JobProcessNode node : childList) {
                JobNodeExtConfig config = jobNodeExtConfigManager.getTrainStatusConfig(node.getIdx());
                if (config != null && !StringUtil.isNullOrBlank(config.getConfigValue())) {
                    status = config.getConfigValue();
                    break;
                }
            }
        }
        if (StringUtil.isNullOrBlank(status))
            return;
        TrainAccessAccount account = getTrainAccessAccount(nodeIDX);
        if (account == null)
            return;
        trainAccessAccountManager.updateTrainStatus(account, status);
    }
    
    /**
     * <li>说明：根据节点完成情况判断能否完成机车检修作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-5-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @return true 能完成机车检修作业计划 false 不能
     */
    @SuppressWarnings({ "unchecked" })
    private boolean canCompleteWorkPlan(String workPlanIDX) {
        int allNodeCount = jobProcessNodeQueryManager.getAllNodeCountByWorkPlan(workPlanIDX);
        int completeNodeCount = jobProcessNodeQueryManager.getCompleteNodeCountByWorkPlan(workPlanIDX);
        if (allNodeCount == completeNodeCount)
            return true;
        return false;
    }
    
    /**
     * <li>说明：节点启动时驱动台位图-移动机车
     * <li>创建人：程锐
     * <li>创建日期：2015-5-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @throws Exception
     */    
    private void moveTrain(String nodeIDX) throws Exception {
        String positionGuid = "";
        JobProcessNode node = getModelById(nodeIDX);
        //父节点无工位就找第一层子节点的工位
        if (node.getIsLeaf() == JobProcessNodeDef.CONST_INT_IS_LEAF_NO) {
            List<JobProcessNode> childList = jobProcessNodeQueryManager.getAllFirstChildNodeList(nodeIDX);
            for (JobProcessNode childNode : childList) {
                if (!childNode.getIdx().equals(node.getIdx()) && !StringUtil.isNullOrBlank(childNode.getWorkStationIDX())) {
                    node = childNode;
                    break;
                }
            }
        }      
        
        if (StringUtil.isNullOrBlank(node.getWorkStationIDX()))
            return;
        WorkStation station = workStationManager.getModelById(node.getWorkStationIDX());
        if (station == null)
            return;
        String stationCode = station.getDeskCode();
        if (StringUtil.isNullOrBlank(stationCode))
            return;
        SiteStation siteStation = siteStationManager.getStationByCodeAndSite(stationCode, EntityUtil.findSysSiteId(null));
        if (siteStation == null)
            return;
        positionGuid = siteStation.getIdx();
        if (!StringUtil.isNullOrBlank(positionGuid))
            moveTrainService.moveTrainStation(getTrainAccessAccount(nodeIDX), positionGuid);
    }
    
    /**
     * <li>说明：根据节点IDX获取相关的机车出入段台账实体
     * <li>创建人：程锐
     * <li>创建日期：2015-5-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 机车出入段台账实体
     */
    private TrainAccessAccount getTrainAccessAccount(String nodeIDX) {
        JobProcessNode node = getModelById(nodeIDX);
        if (node == null) {
            logger.info("节点为空");
            return null;
        }
        TrainWorkPlan workPlan = trainWorkPlanQueryManager.getModelById(node.getWorkPlanIDX());
        if (workPlan == null) {
            logger.info("节点关联的机车检修作业计划为空");
            return null;
        }
        TrainAccessAccount account = trainAccessAccountQueryManager.findInAccountByTrainName(workPlan.getTrainTypeShortName(), workPlan.getTrainNo());
        if (account == null){
            logger.info("节点关联的机车入段记录为空");
            return null;
        }
        return account;        
    } 
    
    /**
     * <li>说明：递归查找下级同源节点的状态配置
     * <li>创建人：程锐
     * <li>创建日期：2015-5-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param status 状态配置
     * @param firstParentNode 父节点
     * @param allParentNodeList 所有父节点列表
     * @return 下级同源节点的状态配置
     */
    private String getStatusConfigByChildNode(String status, JobProcessNode firstParentNode, List<JobProcessNode> allParentNodeList) {
        JobProcessNode nextParentNode = null;
        List<JobProcessNode> childList = jobProcessNodeQueryManager.getChildNodeList(firstParentNode.getIdx());
        for (JobProcessNode childNode : childList) {
            for (JobProcessNode parentNode : allParentNodeList) {
                if (childNode.getIdx().equals(parentNode.getIdx())) {
                    nextParentNode = childNode;
                    JobNodeExtConfig config = jobNodeExtConfigManager.getTrainStatusConfig(parentNode.getIdx());
                    if (config != null && !StringUtil.isNullOrBlank(config.getConfigValue())) 
                        status = config.getConfigValue();      
                    break; 
                }                        
            }
        }
        if (StringUtil.isNullOrBlank(status) && nextParentNode != null) 
            status = getStatusConfigByChildNode(status, nextParentNode, allParentNodeList);    
        return status;
    }
    
    /**
     * <li>说明：【删除节点】时处理后置节点与本节点的前置关系
     * <li>创建人：程锐
     * <li>创建日期：2015-6-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 节点列表
     * @throws Exception
     */
    private void handleNextNodeRelForDel(List<JobProcessNode> list) throws Exception {
        // 处理此节点的后置关系时需判断后置节点是否只有与此节点的前置关系，如有多条，则删除，如只有一条，要把此后置关系的preNodeIDX设为空
        StringBuilder delRelIDX = new StringBuilder();
        StringBuilder updateRelIDX = new StringBuilder();
        for (JobProcessNode allChildNode : list) {
            List<JobProcessNode> nextNodeList = jobProcessNodeQueryManager.getNextNodeList(allChildNode.getIdx());
            for (JobProcessNode nextNode : nextNodeList) {
                List<JobProcessNodeRel> nodeRelList = jobProcessNodeQueryManager.getRelListByNodeIDX(nextNode.getIdx());
                if (nodeRelList != null && nodeRelList.size() == 1)
                    updateRelIDX.append(nodeRelList.get(0).getIdx()).append(Constants.JOINSTR);
                else if (nodeRelList != null && nodeRelList.size() > 1) {
                    JobProcessNodeRel rel = jobProcessNodeQueryManager.getNodeRelByPreAndThisNode(allChildNode.getIdx(), nextNode.getIdx());
                    delRelIDX.append(rel.getIdx()).append(Constants.JOINSTR);
                }
            }
        }
        if (delRelIDX != null && delRelIDX.length() > 1) {
            delRelIDX.deleteCharAt(delRelIDX.length() - 1);            
            String sql = SqlMapUtil.getSql("jxgc-processNode:logicDeleteNodeRel1")
                            .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                            .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                            .replace(JxgcConstants.IDXS, CommonUtil.buildInSqlStr(delRelIDX.toString()));
            daoUtils.executeSql(sql);
        }
        if (updateRelIDX != null && updateRelIDX.length() > 1) {
            updateRelIDX.deleteCharAt(updateRelIDX.length() - 1);            
            String sql = SqlMapUtil.getSql("jxgc-processNode:updateNodeRel1")
                            .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                            .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                            .replace(JxgcConstants.IDXS, CommonUtil.buildInSqlStr(updateRelIDX.toString()));
            daoUtils.executeSql(sql);
        }
    }
    
    /**
     * <li>说明：【调整节点层级关系】时处理后置节点与本节点的前置关系
     * <li>创建人：程锐
     * <li>创建日期：2015-6-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @throws Exception
     */
    private void handleNextNodeRelForChange(String nodeIDX) throws Exception {
        //处理此节点的后置关系时需判断后置节点是否只有与此节点的前置关系，如有多条，则删除，如只有一条，要把此后置关系的preNodeIDX设为空
        StringBuilder delRelIDX = new StringBuilder();
        StringBuilder updateRelIDX = new StringBuilder();

        List<JobProcessNode> nextNodeList = jobProcessNodeQueryManager.getNextNodeList(nodeIDX);
        for (JobProcessNode nextNode : nextNodeList) {
            List<JobProcessNodeRel> nodeRelList = jobProcessNodeQueryManager.getRelListByNodeIDX(nextNode.getIdx());
            if (nodeRelList != null && nodeRelList.size() == 1)
                updateRelIDX.append(nodeRelList.get(0).getIdx()).append(Constants.JOINSTR);
            else if (nodeRelList != null && nodeRelList.size() > 1) {
                JobProcessNodeRel rel = jobProcessNodeQueryManager.getNodeRelByPreAndThisNode(nodeIDX, nextNode.getIdx());
                delRelIDX.append(rel.getIdx()).append(Constants.JOINSTR);
            }
        }
    
        if (delRelIDX != null && delRelIDX.length() > 1) {
            delRelIDX.deleteCharAt(delRelIDX.length() - 1);            
            String sql = SqlMapUtil.getSql("jxgc-processNode:logicDeleteNodeRel1")
                            .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                            .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                            .replace(JxgcConstants.IDXS, CommonUtil.buildInSqlStr(delRelIDX.toString()));
            daoUtils.executeSql(sql);
        }
        if (updateRelIDX != null && updateRelIDX.length() > 1) {
            updateRelIDX.deleteCharAt(updateRelIDX.length() - 1);            
            String sql = SqlMapUtil.getSql("jxgc-processNode:updateNodeRel1")
                            .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                            .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                            .replace(JxgcConstants.IDXS, CommonUtil.buildInSqlStr(updateRelIDX.toString()));
            daoUtils.executeSql(sql);
        }
    }
    
    /**
     * <li>说明：判断节点是否是手动计划模式，且计划开始时间晚于当前时间
     * <li>创建人：程锐
     * <li>创建日期：2015-8-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param node 节点对象
     * @return true 节点是手动计划模式，且计划开始时间晚于当前时间
     */
    private boolean isManualNode(JobProcessNode node) {
        if (JobProcessNode.PLANMODE_MUNAUL.equals(node.getPlanMode()) && CalcWorkDateUtil.compareTwoDate(new Date(), node.getPlanBeginTime()))
            return true;
        return false;
    }
    
    /**
     * <li>说明：判断节点是否是自动计划模式(替换之前的方法isManualNode)
     * <li>创建人：张迪
     * <li>创建日期：2016-7-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param node 节点对象
     * @return true 节点为非自动计划模式
     */
    private boolean notAutoNode(JobProcessNode node) {
        if (!JobProcessNode.PLANMODE_AUTO.equals(node.getPlanMode()))
            return true;
        return false;
    }
    /**
     * <li>说明：完成机车作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-11-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    private void completeWorkPlan(String workPlanIDX) throws BusinessException, NoSuchFieldException {
        if (canCompleteWorkPlan(workPlanIDX)) {
            TrainWorkPlan workPlan = trainWorkPlanQueryManager.getModelById(workPlanIDX);
            workPlan.setEndTime(new Date());
            workPlan.setWorkPlanStatus(TrainWorkPlan.STATUS_HANDLED);
            workPlan.setUpdateTime(new Date());
            workPlan.setUpdator(SystemContext.getAcOperator().getOperatorid());
            trainWorkPlanQueryManager.saveOrUpdate(workPlan);
            trainWorkPlanQueryManager.updatePlanStatus(workPlan);
        }
    }
    
    /**
     * <li>说明：检验【机车检修作业节点】的完成状态
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity【机车检修作业节点】实体
     * @return 提示信息
     */
    public String validateFinishedStatus(JobProcessNode entity) {
        if (entity.getIsLeaf() == WPNode.CONST_INT_IS_LEAF_NO) {
            return "不是子节点!";
        }
//        if(jobProcessNodeQueryManager.hasWorkCardOfAllChildNode(entity.getIdx())){
          StringBuffer sql = new StringBuffer(SqlMapUtil.getSql("jxgc-processNode:getWorkCardOfAllChildNode").replace("#nodeIDX#", entity.getIdx()));
//          String sql = SqlMapUtil.getSql("jxgc-processNode:getWorkCardOfAllChildNode")
//                                 .replace("#nodeIDX#", nodeIDX);
//          sql.concat(" AND STATUS IN ('").concat(JobProcessNode.STATUS_UNSTART)
//             .concat("','").concat(JobProcessNode.STATUS_GOING).concat("')");
         sql.append(" AND STATUS IN ('").append(JobProcessNode.STATUS_UNSTART).append("','").append(JobProcessNode.STATUS_GOING).append("')");
         List list = daoUtils.executeSqlQuery(sql.toString());
         if(list != null && list.size() > 0){
            return "检修工单未全部处理!";
        }
        return null;
    }
    /**
     * <li>说明：机车检修修竣提交节点前的验证
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 结点id
     * @return 验证信息
     */
    public String validateFinishedStatus(String idx) {
        if (null == idx || idx.trim().length() <= 0) {
            throw new BusinessException("机车检修作业节点主键为空");
        }
        JobProcessNode entity = this.getModelById(idx);
        if (null == entity) {
            throw new BusinessException("数据异常-未查询到【机车检修作业节点】对象 - idx[" + idx + "]");
        }
        // 检验【机车检修作业节点】的完成状态
        String validateFinishedStatus = validateFinishedStatus(entity);
        return validateFinishedStatus;
    }

    /**
     * <li>说明：根据时间查询节点信息(今日机车动态使用)
     * <li>创建人：林欢
     * <li>创建日期：2016-7-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 结点id
     * @return 验证信息
     */
    public String findTodayNodeNameByDate(String trainWorkPlanIDX,String date) {
        StringBuffer sb = new StringBuffer();
        StringBuffer workStationName = new StringBuffer();
        
        sb.append(" select t.Work_Plan_IDX,t.Node_Name ");
        sb.append(" from ( ");
        sb.append(" select c.Work_Plan_IDX, c.Node_Name,c.Parent_IDX ");                  
        sb.append(" from JXGC_Job_Process_Node c ");
        sb.append(" where c.Record_Status = 0 ");
        sb.append(" and c.Work_Plan_IDX ='").append(trainWorkPlanIDX).append("'");
        sb.append(" and to_char(c.Plan_Begin_Time, 'yyyy-mm-dd') <= ");
        sb.append(" '").append(date).append("'");
        sb.append(" and to_char(c.Plan_End_Time, 'yyyy-mm-dd') >= ");
        sb.append(" '").append(date).append("'");
        sb.append(" or (to_char(c.Real_Begin_Time, ");
        sb.append(" 'yyyy-mm-dd') >= '").append(date).append("'");                          
        sb.append(" and to_char(c.Real_Begin_Time, 'yyyy-mm-dd') <= '").append(date).append("' ) ");                       
        sb.append(" group by c.Work_Plan_IDX, c.Node_Name,c.Parent_IDX ");
        sb.append(" ) t ");
        sb.append(" where t.Parent_IDX is null ");
        sb.append(" and t.Work_Plan_IDX = '").append(trainWorkPlanIDX).append("'");
        sb.append(" group by t.Work_Plan_IDX,t.Node_Name ");
        
        List<Object[]> objectList =  daoUtils.executeSqlQuery(sb.toString());
        for (int i = 0; i < objectList.size(); i++) {
            
            Object[] node = objectList.get(i);
            
//          判断最后一个还是第一个
            if (node[1] != null && StringUtils.isNotBlank(node[1].toString())) {
                if (i == objectList.size() - 1) {
                    workStationName.append(node[1]);
                }else {
                    workStationName.append(node[1]).append(",");
                }
            }
        }
        
        return workStationName.toString();
    }

    /**
     * <li>说明：根据时间查询节点信息(今日机车动态使用)
     * <li>创建人：林欢
     * <li>创建日期：2016-7-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 结点id
     * @return 验证信息
     */
    public String findTodayWorkStationNameByDate(String trainWorkPlanIDX,String date) {
        StringBuffer sb = new StringBuffer();
        StringBuffer workStationName = new StringBuffer();
        
        sb.append(" select t.Work_Plan_IDX,t.Work_Station_Name ");
        sb.append(" from ( ");
        sb.append(" select c.Work_Plan_IDX, c.Work_Station_Name ");                  
        sb.append(" from JXGC_Job_Process_Node c ");
        sb.append(" where c.Record_Status = 0 ");
        sb.append(" and to_char(c.Plan_Begin_Time, 'yyyy-mm-dd') <= ");
        sb.append(" '").append(date).append("'");
        sb.append(" and to_char(c.Plan_End_Time, 'yyyy-mm-dd') >= ");
        sb.append(" '").append(date).append("'");
        sb.append(" or (to_char(c.Real_Begin_Time, ");
        sb.append(" 'yyyy-mm-dd') >= '").append(date).append("'");                          
        sb.append(" and to_char(c.Real_Begin_Time, 'yyyy-mm-dd') <= '").append(date).append("' ) ");                       
        sb.append(" group by c.Work_Plan_IDX, c.Work_Station_Name ");
        sb.append(" ) t ");
        sb.append(" where t.Work_Plan_IDX = '").append(trainWorkPlanIDX).append("'");
        sb.append(" group by t.Work_Plan_IDX,t.Work_Station_Name ");
        
        List<Object[]> objectList = daoUtils.executeSqlQuery(sb.toString());
        for (int i = 0; i < objectList.size(); i++) {
            
            Object[] node = objectList.get(i);
            
            //判断最后一个还是第一个
            if (node[1] != null && StringUtils.isNotBlank(node[1].toString())) {
                if (i == objectList.size() - 1) {
                    workStationName.append(node[1]);
                }else {
                    workStationName.append(node[1]).append(",");
                }
            }
        }
        
        return workStationName.toString();
    }

    /**
     * <li>说明：当启动生产的时候设置基线时间
     * <li>创建人：张迪
     * <li>创建日期：2016-7-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 计划作业主键
     */
    public void saveBaseTime(String workPlanIDX) {
        // 基线当启动生产的时候，以该时间为基线点
        String hql = "update  JobProcessNode set baseLineStartTime = planBeginTime, baseLineEndTime = planEndTime where workPlanIDX ='";
        hql += workPlanIDX + "'";
        daoUtils.executUpdateOrDelete(hql);
        daoUtils.flush();
    }
    

    public String getRdpIDX(String nodeIDX) {
        // 基线当启动生产的时候，以该时间为基线点
        String sql = "select Work_Plan_IDX from JXGC_Job_Process_Node where idx ='" + nodeIDX + "'";
        return daoUtils.executeSqlQuery(sql).get(0).toString();
    }

    /**
     * <li>说明：作业节点树
     * <li>创建人：张迪
     * <li>创建日期：2016-10-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param parentIDX 上级节点idx主键(预留)
     * @param processIDX 作业主键
     * @return List<HashMap<String, Object>> 实体集合
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> findFirstNodeTree(String parentIDX, String workPlanIDX) {
        StringBuilder sb = new StringBuilder("From JobProcessNode Where recordStatus = 0");
        sb.append(" And workPlanIDX = '").append(workPlanIDX).append("'");
        
        if (null == parentIDX || "ROOT_0".equals(parentIDX)) {
            sb.append(" And (parentIDX Is Null Or parentIDX ='ROOT_0')");
        } else {
            sb.append(" And parentIDX = '").append(parentIDX).append("'");
        }
        sb.append(" Order By planBeginTime, seqNo ASC");
        List<JobProcessNode> list = this.daoUtils.find(sb.toString());
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        for (JobProcessNode t : list) {
            HashMap<String, Object> nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getIdx()); // 节点idx主键
            nodeMap.put("text", t.getNodeName()); // 树节点显示名称
            nodeMap.put("leaf",  true); // 是否是叶子节点 0:否；1：是
            nodeMap.put("nodeName", t.getNodeName()); // 节点名称
            nodeMap.put("nodeDesc", t.getNodeDesc()); // 节点描述
            nodeMap.put("seqNo", t.getSeqNo()); // 顺序号
            nodeMap.put("editable ", true); // 顺序号
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：获取某一天的计划任务
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-2-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nowDateStr
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<JobProcessNode> getPlanTodayStatistics(String dateStr){
        if(StringUtil.isNullOrBlank(dateStr)){
            return null ;
        }
        StringBuffer hql = new StringBuffer(" From JobProcessNode where recordStatus = 0 and isLeaf = 1 and status <> 'TERMINATED' and workStationBelongTeam is not null ");
        hql.append(" and to_char(planBeginTime,'yyyy-MM-dd HH24:mi:ss') <= '"+dateStr+" 23:59:59' ");
        hql.append(" and to_char(planEndTime,'yyyy-MM-dd HH24:mi:ss') >= '"+dateStr+" 00:00:00' ");
        return (List<JobProcessNode>)this.find(hql.toString());
    }
    
    
}
