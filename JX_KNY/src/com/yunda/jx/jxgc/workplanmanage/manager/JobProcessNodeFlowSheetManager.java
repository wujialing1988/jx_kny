package com.yunda.jx.jxgc.workplanmanage.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeFlowSheetBean;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车检修计划流程节点
 * <li>创建人：张迪
 * <li>创建日期：2017-2-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "jobProcessNodeFlowSheetManager")
public class JobProcessNodeFlowSheetManager extends JXBaseManager<JobProcessNodeFlowSheetBean, JobProcessNodeFlowSheetBean>{

    /** 根节点 */
    private String ROOT_0 = "ROOT_0";

    /**
     * <li>说明：流程图 查询机车检修作业计划第一层节点信息，及子节点申请延期的个数
     * <li>创建人：张迪
     * <li>创建日期：2017-3-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 计划idx
     * @param parentIDX 父节点idx
     * @return 第一层节点
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
     public List<JobProcessNodeFlowSheetBean> getFirstNodeByWorkPlanIDX(String workPlanIDX, String parentIDX) throws Exception {
         StringBuilder sb = new StringBuilder(" SELECT T.*, R.PRE_NODE_IDX from (SELECT A.*,(SELECT COUNT(IDX) FROM JXGC_JOB_PROCESS_NODE WHERE EDIT_STATUS = 1 AND Parent_IDX = A.IDX) AS Apply_Count FROM JXGC_JOB_PROCESS_NODE A WHERE A.RECORD_STATUS = 0 AND A.WORK_PLAN_IDX = '")
         .append(workPlanIDX).append("'")     
         .append(" And (A.Parent_IDX Is Null Or A.Parent_IDX = '")
         .append(ROOT_0).append("')) T left join JXGC_Job_Process_Node_Rel R ON T.IDX = R.Node_IDX AND R.RECORD_STATUS = 0 ")
         .append(" Order By T.PLAN_BEGIN_TIME, T.SEQ_NO ASC ");
         return daoUtils.executeSqlQueryEntity(sb.toString(),JobProcessNodeFlowSheetBean.class);
     }
    
  
    
}
