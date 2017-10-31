package com.yunda.jx.jxgc.workplanmanage.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.NodeCaseDelay;
import com.yunda.jx.jxgc.producttaskmanage.manager.NodeCaseDelayManager;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeStatistics;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 生产任务统计历史
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-2-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "jobProcessNodeStatisticsManager")
public class JobProcessNodeStatisticsManager extends JXBaseManager<JobProcessNodeStatistics, JobProcessNodeStatistics>{
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 机车作业节点业务类 */
    @Resource
    private JobProcessNodeManager jobProcessNodeManager;
    
    /** 工期延误业务类 */
    @Resource
    private NodeCaseDelayManager nodeCaseDelayManager;
    
    /**
     * <li>说明：保存生产进度统计日报表历史
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-2-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public void savePlanTodayStatisticsHis() {
       String errorMessage = "";
       Map<String, Object> map = new HashMap<String, Object>();
       // 当前日期
       String nowDateStr = DateUtil.getToday();
       try {
           List<JobProcessNode> nodes = jobProcessNodeManager.getPlanTodayStatistics(nowDateStr);
           if(nodes != null){
               // 先删除当前天的数据
               this.daoUtils.execute(" delete from JobProcessNodeStatistics where dateStr = ? ", new Object[]{nowDateStr});
               for (JobProcessNode node : nodes) {
                   JobProcessNodeStatistics statistics = new JobProcessNodeStatistics();
                   BeanUtils.copyProperties(statistics, node);
                   statistics.setIdx("");
                   statistics.setDateStr(nowDateStr);
                   List<NodeCaseDelay> caseDelays = nodeCaseDelayManager.getNodeCaseDelaysByNodedx(node.getNodeIDX(),node.getWorkPlanIDX());
                   String delayReasons = "" ;
                   for (NodeCaseDelay caseDelay : caseDelays) {
                       if(caseDelay != null && !StringUtil.isNullOrBlank(caseDelay.getDelayReason())){
                           delayReasons += "," + caseDelay.getDelayReason();
                       }
                   }
                   if(!StringUtil.isNullOrBlank(delayReasons)){
                       delayReasons = delayReasons.substring(0,delayReasons.length() - 1);
                   }
                   statistics.setDelayReason(delayReasons);
                   this.saveOrUpdate(statistics);
                   
               }
           }
       } catch (Exception e) {
           ExceptionUtil.process(e, logger, map, errorMessage);
       }
   }
    
    
}
