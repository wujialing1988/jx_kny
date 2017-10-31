package com.yunda.jx.jxgc.tpmanage.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.frame.yhgl.manager.IOmOrganizationManager;
import com.yunda.frame.yhgl.manager.OperatorManager;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.component.manager.OmOrganizationSelectManager;
import com.yunda.jx.jxgc.buildupmanage.entity.PlaceFault;
import com.yunda.jx.jxgc.common.JxgcConstants;
import com.yunda.jx.jxgc.processdef.entity.JobNodeExtConfigDef;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCResult;
import com.yunda.jx.jxgc.tpmanage.entity.FaultQCResultVO;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;
import com.yunda.jx.jxgc.tpmanage.entity.RepairEmpBean;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeManager;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeQueryManager;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanQueryManager;
import com.yunda.jxpz.utils.CodeRuleUtil;
import com.yunda.webservice.common.util.DefaultUserUtilManager;
import com.yunda.webservice.message.MessageService;
import com.yunda.webservice.message.thread.ThreadFaultTicket;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明：FaultTicket业务类,故障提票
 * <li>创建人：程锐
 * <li>创建日期：2015-06-25
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.1
 */
@Service(value = "faultTicketManager")
public class FaultTicketManager extends JXBaseManager<FaultTicket, FaultTicket> {
    
    @Resource
    TrainWorkPlanQueryManager trainWorkPlanQueryManager;

    /** 人员业务类 */
    @Resource
    private IOmEmployeeManager omEmployeeManager;

    /** 组织机构业务类 */
    @Resource
    private IOmOrganizationManager omOrganizationManager;
    
    /** 提票质检业务类 */
    @Resource
    private FaultQCResultManager faultQCResultManager;
    
    /** 作业工单业务类 */
    @Resource
    private WorkCardManager workCardManager;
    
    /** 节点查询管理类 */
    @Resource
    private JobProcessNodeQueryManager jobProcessNodeQueryManager;
    /** 节点管理类 */
    @Resource
    private JobProcessNodeManager jobProcessNodeManager;
    
    /** 线程池 */
    @Resource
    private ThreadPoolTaskExecutor taskExecutor;
    
    /** 消息发送服务 */
    @Resource
    private MessageService messageService ;
    
    /** 人员服务服务 */
    @Resource
    private OperatorManager operatorManager;
    
    
    private static final String WORKPLANIDX = "#workPlanIDX#";
    
    private static final String STATUS = "status";
    
    /**
     * <li>说明：检查有无同车同位置同故障现象的未处理的碎修票
     * <li>创建人：程锐
     * <li>创建日期：2015-6-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 提票实体对象
     * @return true 有 false 无
     */
    public boolean checkData(FaultTicket entity) {
        List<FaultTicket> tpList = getModelList(entity.getTrainTypeIDX(), 
                                                entity.getTrainNo(),
                                                entity.getFaultID(), 
                                                entity.getFixPlaceFullName(),
                                                entity.getFaultDesc());      
        return tpList != null && tpList.size() > 0; 
    }
    
    /**
     * <li>说明：保存并实例化提票
     * <li>创建人：程锐
     * <li>创建日期：2015-6-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entities 提票实体列表
     * @param entity 提票实体对象
     * @throws Exception
     */
    public void saveTpAndInst(FaultTicket[] entities, FaultTicket entity) throws Exception {
        for (FaultTicket tp : entities) {
            tp = buildEntity(tp);
            saveOrUpdate(tp);
        }
        daoUtils.flush();
        TrainWorkPlan workPlan = trainWorkPlanQueryManager.getTrainWorkPlanByTrain(entity.getTrainTypeShortName(), entity.getTrainNo());
        if (workPlan != null) {
            saveForInstanceTp(workPlan);
        }
    }
    
    /**
     * <li>说明：发送消息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entities 提票集合
     */
    private void sendMsg(TrainWorkPlan workPlan){
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("status", String.valueOf(FaultTicket.STATUS_DRAFT));
        paramMap.put("trainTypeShortName", workPlan.getTrainTypeShortName());
        paramMap.put("trainNo", workPlan.getTrainNo());
        List<FaultTicket> entities = getTpList(paramMap);
        String phones = operatorManager.getAcOperatorsPhoneByfuncnameAndappcode("提票处理", "Terminal");
        for (FaultTicket ticket : entities) {
            // faultTicket.message
           // String content = I18nPropertiesUtil.getMessageFormat("faultTicket.message", new Object[]{ticket.getTicketEmp(),ticket.getType(),ticket.getTrainTypeShortName()+ticket.getTrainNo(),ticket.getFaultDesc()});
            String content = ticket.getTicketEmp()+","+ticket.getType()+"(" + ticket.getTrainNo() +"):" + ticket.getFaultDesc();
            taskExecutor.execute(new ThreadFaultTicket(messageService,ticket.getTicketCode(),content,phones));
        }
    }
    
    /**
     * <li>说明：实例化提票
     * <li>创建人：程锐
     * <li>创建日期：2015-6-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 机车检修作业计划对象
     */
    public void saveForInstanceTp(TrainWorkPlan workPlan) {
        // 先对未实例化的票进行推送
        sendMsg(workPlan);
        // 实例化提票
        String sql = SqlMapUtil.getSql("jxgc-tp:saveForInstanceTp")
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace("#STATUS_OPEN#", String.valueOf(FaultTicket.STATUS_OPEN))
                               .replace(WORKPLANIDX, workPlan.getIdx())                            
                               .replace("#STATUS_DRAFT#", String.valueOf(FaultTicket.STATUS_DRAFT))
                               .replace("#NO_DELETE#", Constants.NO_DELETE + "")
                               .replace("#trainTypeShortName#", workPlan.getTrainTypeShortName())
                               .replace("#trainNo#", workPlan.getTrainNo());
        daoUtils.executeSql(sql);
        List list = getNotComAndQCTpIDXListByTrain(workPlan.getTrainTypeShortName(), workPlan.getTrainNo());
        if (list == null || list.size() < 1)
            return;
        for (int i = 0; i < list.size(); i++) {
            Object obj = (Object) list.get(i);
            String idx = obj.toString();
            sql = SqlMapUtil.getSql("jxgc-tp:insertQCResult")
                            .replace("#faultIDX#", idx)
                            .replace("#STATUS_WKF#", FaultQCResult.STATUS_WKF + "");
            daoUtils.executeSql(sql);
        }
        
    }
    
    /**
     * <li>说明：提票调度派工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param data 提票单实体
     * @param tpIDXAry 提票单id数组
     * @throws Exception
     */
    public void updateForDdpg(FaultTicket data, String[] tpIDXAry) throws Exception {
        if (tpIDXAry == null || tpIDXAry.length < 1)
            throw new BusinessException("提票实体IDX为空");
        String idxs = StringUtil.join(tpIDXAry);
        String idxsStr = CommonUtil.buildInSqlStr(idxs);
        if (StringUtil.isNullOrBlank(idxsStr))
            throw new BusinessException("以,号分隔的提票idx字符串为空");
        String sql = SqlMapUtil.getSql("jxgc-tp:updateForDdpg")
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace("#repairTeam#", data.getRepairTeam().toString())
                               .replace("#repairTeamName#", data.getRepairTeamName())
                               .replace("#repairTeamOrgseq#", data.getRepairTeamOrgseq())
                               .replace(JxgcConstants.IDXS, idxsStr);
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：提票工长派工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param empids 人员id数组
     * @param ids 提票单id数组
     * @throws Exception
     */
    public void updateForGzpg(String[] empids, String[] ids) throws Exception {
        String repairEmpID = StringUtil.join(empids, Constants.JOINSTR);
        StringBuilder repairEmp = new StringBuilder();
        for (String empid : empids) {
            OmEmployee emp = omEmployeeManager.getModelById(Long.valueOf(empid));
            if (emp != null)
                repairEmp.append(emp.getEmpname()).append(Constants.JOINSTR);
        }
        if (repairEmp.toString().endsWith(Constants.JOINSTR))
            repairEmp.deleteCharAt(repairEmp.length() - 1);
        String idxs = StringUtil.join(ids);
        String idxsStr = CommonUtil.buildInSqlStr(idxs);
        if (StringUtil.isNullOrBlank(idxsStr))
            throw new BusinessException("以,号分隔的提票idx字符串为空");
        String sql = SqlMapUtil.getSql("jxgc-tp:updateForGzpg")
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace("#repairEmpID#", repairEmpID)
                               .replace("#repairEmp#", repairEmp)
                               .replace(JxgcConstants.IDXS, idxsStr);
        daoUtils.executeSql(sql);
    }
    
    /**
     * <li>说明：获取其他作业人员列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param id 提票idx
     * @param empid 当前人员id
     * @return 其他作业人员列表
     */
    public List<RepairEmpBean> getOtherWorkerByTP(String id, String empid) {
        FaultTicket tp = getModelById(id);
        String repairEmpID = tp.getDispatchEmpID();
        if (StringUtil.isNullOrBlank(repairEmpID)) {
            return new ArrayList<RepairEmpBean>();
        }
        String[] repairEmpIDArray = StringUtil.tokenizer(repairEmpID, Constants.JOINSTR);
        List<RepairEmpBean> list = new ArrayList<RepairEmpBean>();
        for (String empID : repairEmpIDArray) {
            if (empID.equals(empid))
                continue;
            OmEmployee emp = omEmployeeManager.getModelById(Long.valueOf(empID));
            if (emp == null)
                continue;
            RepairEmpBean repairEmpBean = new RepairEmpBean();
            repairEmpBean.setWorkerID(emp.getEmpid());
            repairEmpBean.setWorkerName(emp.getEmpname());
            list.add(repairEmpBean);
        }
        return list;
    }
    
    /**
     * <li>说明：获取需要指派的提票质量检查项列表
     * <li>创建人：程锐
     * <li>创建日期：2014-11-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 提票单主键
     * @return 需要指派的提票质量检查项列表
     * @throws Exception
     */
    public List<FaultQCResult> getIsAssignCheckItems(String idx) throws Exception {
        return faultQCResultManager.getIsAssignCheckItems(idx);
    }
    
    /**
     * <li>说明：销票
     * <li>创建人：程锐
     * <li>创建日期：2015-7-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 提票实体
     * @param qcResult 指派的提票质检项实体数组
     * @throws Exception
     */
    public void handle(FaultTicket entity, FaultQCResultVO[] qcResult) throws Exception {
        OmEmployee emp = SystemContext.getOmEmployee();
        if (emp == null) 
            return;
        updateForHandle(entity, qcResult, emp);        
    }
    
    /**
     * <li>说明：销票
     * <li>创建人：程锐
     * <li>创建日期：2015-7-13
     * <li>修改人：何涛
     * <li>修改日期：2016-05-07
     * <li>修改内容：提票处理时可以修改故障现象
     * @param entity 提票实体
     * @param qcResult 指派的提票质检项实体数组
     * @param emp 当前人员实体
     * @throws Exception
     */
    public void updateForHandle(FaultTicket entity, FaultQCResultVO[] qcResult, OmEmployee emp) throws Exception {
        FaultTicket tp = getModelById(entity.getIdx());
        if (tp == null)
            return;
        // Modified by hetao on 2016-05-07 提票处理时可以修改故障现象
        if(!StringUtil.isNullOrBlank(entity.getFaultID())){
            tp.setFaultID(entity.getFaultID());
        }
        tp.setFaultName(entity.getFaultName()); //web端只修改故障现象名称
        if(!StringUtil.isNullOrBlank(entity.getFaultDesc())){
            tp.setFaultDesc(entity.getFaultDesc());
        }
        //  提票处理时可以修改故障原因
        if(!StringUtil.isNullOrBlank(entity.getFaultReason())){
            tp.setFaultReason(entity.getFaultReason());
        }
        tp.setCompleteTime(entity.getCompleteTime());
        tp.setMethodDesc(StringUtil.nvlTrim(entity.getMethodDesc(), ""));
        tp.setMethodID(StringUtil.nvlTrim(entity.getMethodID(), ""));
        tp.setMethodName(StringUtil.nvlTrim(entity.getMethodName(), ""));
        tp.setRepairResult(entity.getRepairResult());
        if (!StringUtil.isNullOrBlank(entity.getProfessionalTypeIdx()))
            tp.setProfessionalTypeIdx(entity.getProfessionalTypeIdx());
        if (!StringUtil.isNullOrBlank(entity.getProfessionalTypeName()))
            tp.setProfessionalTypeName(entity.getProfessionalTypeName());
        if (!StringUtil.isNullOrBlank(entity.getFixPlaceFullName()))
            tp.setFixPlaceFullName(entity.getFixPlaceFullName());
        
        OmOrganization team= omOrganizationManager.getModelById(emp.getOrgid()); // 获取当前人员班级信息
        
        String otherEmp = entity.getCompleteEmp();
        StringBuilder workerIDS = new StringBuilder().append(emp.getEmpid());
        StringBuilder workerNames = new StringBuilder().append(emp.getEmpname()).append(Constants.JOINSTR);
        
        if (!StringUtil.isNullOrBlank(otherEmp)) {
            workerIDS.append(Constants.JOINSTR).append(otherEmp);
            String[] otherWorkerIDArray = StringUtil.tokenizer(otherEmp, Constants.JOINSTR);
            for (String otherWorkerID : otherWorkerIDArray) {
                OmEmployee otherEmpEntity = omEmployeeManager.getModelById(Long.valueOf(otherWorkerID));
                if (otherEmpEntity != null){
                    workerNames.append(otherEmpEntity.getEmpname()).append(Constants.JOINSTR);                                   
                }
            }
        }
        if (workerIDS.toString().endsWith(Constants.JOINSTR))
            workerIDS.deleteCharAt(workerIDS.length() - 1);
        if (workerNames.toString().endsWith(Constants.JOINSTR))
            workerNames.deleteCharAt(workerNames.length() - 1);      
//        StringBuilder completeEmp = new StringBuilder().append(emp.getEmpname());
//        if (!StringUtil.isNullOrBlank(otherEmp)) 
//            completeEmp.append(Constants.JOINSTR).append(otherEmp);
        tp.setCompleteEmpID(emp.getEmpid());
        tp.setCompleteEmp(emp.getEmpname());
        tp.setRepairEmpID(workerIDS.toString());
        tp.setRepairEmp(workerNames.toString());
        tp.setStatus(FaultTicket.STATUS_OVER);
        tp.setRepairTeam(team.getOrgid().toString());
        tp.setRepairTeamName(team.getOrgname());
        tp.setRepairTeamOrgseq(team.getOrgname());
        saveOrUpdate(tp);
//        if (qcResult == null || qcResult.length < 1)
//            return;
        faultQCResultManager.updateOpenQCResultByTp(entity.getIdx(), qcResult, emp.getEmpid());
        if (canFinishNodeByWorkPlanIDX(tp.getWorkPlanIDX())) {
            try {
                finishConfigTicketNode(tp);
            } catch (Exception e) {
                throw new BusinessException(e);
            }
        }      
    }
    /**
     * <li>说明：能否完成节点
     * <li>创建人：程锐
     * <li>创建日期：2015-5-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return true 能， false 不能
     */
    public boolean canFinishNodeByWorkPlanIDX(String workPlanIDX) {
        int completeFaultTicketCount = getCompleteFaultTicketCountByWorkPlanIDX(workPlanIDX);
        int FaultTicketCount = getFaultTicketCountByWorkPlanIDX(workPlanIDX);
        if (completeFaultTicketCount == FaultTicketCount)
            return true;
        return false;
    }
    /**
     * <li>说明：全部销票后自动完成带有卡控的节点
     * <li>创建人：张迪
     * <li>创建日期：2016-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param tp 提票单
     * @throws Exception 
     */
    private void finishConfigTicketNode(FaultTicket tp) throws Exception {
        String sql = SqlMapUtil.getSql("jxgc-processNode:getConfigTicketNode")
        .replace("#configName#", JobNodeExtConfigDef.EXT_CHECK_TICKET)
        .replace("#configValue#", JobNodeExtConfigDef.EXT_CHECK_TICKET_CURRENT)
        .replace("#workPlanIDX#", tp.getWorkPlanIDX());
        List nodeList = daoUtils.executeSqlQuery(sql);
        if(null != nodeList && nodeList.size()>0){
            for (int i = 0; i < nodeList.size(); i++) {
                Object obj = (Object) nodeList.get(i);
                String idx = obj.toString();
                if (workCardManager.canFinishNode(idx)) {
                JobProcessNode nodeCase = jobProcessNodeQueryManager.getModelById(idx);
                  if (nodeCase != null) {
                      jobProcessNodeManager.updateFinishNodeCase(nodeCase);
                  }
              }
                 
            }
        }
    }

    /**
     * <li>说明：获取提票调度派工数量
     * <li>创建人：程锐
     * <li>创建日期：2015-7-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param isDispatch 是否派工（true 已派工，false 未派工）
     * @return 提票调度派工数量
     */
    public int queryDdpgCount(String isDispatch) {        
        return CommonUtil.getListSize(findList(getDdpgQueryCriteria(isDispatch)));
    }
    
    /**
     * <li>说明：获取提票工长派工数量
     * <li>创建人：程锐
     * <li>创建日期：2015-7-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param isDispatch 是否派工（true 已派工，false 未派工）
     * @param operatorid 操作者ID
     * @return 提票工长派工数量
     */
    public int queryGzpgCount(boolean isDispatch, long operatorid) {        
        return CommonUtil.getListSize(findList(getGzpgQueryCriteria(isDispatch, operatorid)));
    }
    
    /**
     * <li>说明：获取提票处理数量
     * <li>创建人：程锐
     * <li>创建日期：2015-7-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return 提票处理数量
     */
    public int queryHandleCount(long operatorid) {        
        return CommonUtil.getListSize(findList(getHandleQueryCriteria(operatorid)));
    }
    
    /**
     * <li>说明：获取提票调度派工查询实体对象
     * <li>创建人：程锐
     * <li>创建日期：2015-7-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param isDispatch 是否派工（true 已派工，false 未派工）
     * @return 提票调度派工查询实体对象
     */
    public QueryCriteria<FaultTicket> getDdpgQueryCriteria(String isDispatch) {
        QueryCriteria<FaultTicket> query = new QueryCriteria<FaultTicket>();
        query.setEntityClass(FaultTicket.class);
        List<Condition> whereList = new ArrayList<Condition>();
        Object[] statusArray = new Object[2];
        statusArray[0] = FaultTicket.STATUS_DRAFT;
        statusArray[1] = FaultTicket.STATUS_OPEN ;            
        whereList.add(new Condition(STATUS, Condition.IN, statusArray));   
        if (null != isDispatch && !"".equals(isDispatch)){
            String sqlStr = " REPAIR_TEAM IS NULL" ;
            boolean isDispatchBoo =  Boolean.valueOf(isDispatch);
            if(isDispatchBoo){
                sqlStr = " REPAIR_TEAM IS NOT NULL AND DISPATCH_EMP_ID IS NULL";
            }
            whereList.add(new Condition(sqlStr));
        }
           
     
        query.setWhereList(whereList);
        return query;
    }
    
    /**
     * <li>说明：获取提票工长派工查询实体对象
     * <li>创建人：程锐
     * <li>创建日期：2015-7-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param isDispatch 是否派工（true 已派工，false 未派工）
     * @param operatorid 操作者ID
     * @return 提票工长派工查询实体对象
     */
    public QueryCriteria<FaultTicket> getGzpgQueryCriteria(boolean isDispatch, long operatorid) {
        QueryCriteria<FaultTicket> query = new QueryCriteria<FaultTicket>();
        query.setEntityClass(FaultTicket.class);
        List<Condition> whereList = new ArrayList<Condition>(); 
        OmOrganization org = getOmOrganizationSelectManager().getOrgByAcOperator(operatorid);
        whereList.add(new Condition("repairTeam", Condition.EQ, org.getOrgid()));
        Object[] statusArray = new Object[2];
        statusArray[0] = FaultTicket.STATUS_DRAFT;
        statusArray[1] = FaultTicket.STATUS_OPEN ;             
        whereList.add(new Condition(STATUS, Condition.IN, statusArray));
        String sqlStr = " DISPATCH_EMP_ID IS NULL" ;
        if (isDispatch)
            sqlStr = " DISPATCH_EMP_ID IS NOT NULL ";
        whereList.add(new Condition(sqlStr));
        query.setWhereList(whereList);
        return query;
    }
    
    /**
     * <li>说明：获取提票处理查询实体对象
     * <li>创建人：程锐
     * <li>创建日期：2015-7-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @return 提票处理查询实体对象
     */
    public QueryCriteria<FaultTicket> getHandleQueryCriteria(long operatorid) {
        QueryCriteria<FaultTicket> query = new QueryCriteria<FaultTicket>();
        query.setEntityClass(FaultTicket.class);
        List<Condition> whereList = new ArrayList<Condition>(); 
        whereList.add(new Condition(STATUS, Condition.EQ, FaultTicket.STATUS_OPEN));
        OmOrganization org = getOmOrganizationSelectManager().getOrgByAcOperator(operatorid);
        OmEmployee emp = getOmEmployeeSelectManager().findEmpByOperator(operatorid);
        String sqlStr = " REPAIR_TEAM = " + org.getOrgid() + " AND DISPATCH_EMP_ID LIKE '%" + emp.getEmpid() + "%'" ;
        whereList.add(new Condition(sqlStr));
        query.setWhereList(whereList);
        return query;
    }
    
    /**
     * <li>说明：构建提票实体对象的默认通用属性值
     * <li>创建人：程锐
     * <li>创建日期：2015-6-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 提票实体对象
     * @return 提票实体对象
     */
    private FaultTicket buildEntity(FaultTicket entity) {
        if(null != entity.getIdx() && !"".equals(entity.getIdx())){
            FaultTicket entityOld = this.getModelById(entity.getIdx());
            entityOld.setFaultID(entity.getFaultID());
            entityOld.setFaultName(entity.getFaultName());
            entityOld.setFaultDesc(entity.getFaultDesc());
            entityOld.setReasonAnalysis(entity.getReasonAnalysis());
            entityOld.setReasonAnalysisID(entity.getReasonAnalysisID());
            entityOld.setFaultReason(entity.getFaultReason());
            return entityOld;
        }else{  
            entity.setStatus(FaultTicket.STATUS_DRAFT);
            entity.setTicketCode(CodeRuleUtil.getRuleCode("JCZL_FAULT_NOTICE_FAULT_NOTICE_CODE"));
            DefaultUserUtilManager.setDefaultOperator();
            Long operatorid = SystemContext.getAcOperator().getOperatorid();
            OmEmployee emp = omEmployeeManager.findByOperator(operatorid);
            entity.setTicketEmpId(emp != null ? emp.getEmpid() : null);
            entity.setTicketEmp(emp != null ? emp.getEmpname() : "");
            entity.setTicketTime(new Date());
            entity.setFaultOccurDate(entity.getFaultOccurDate() != null ? entity.getFaultOccurDate() : new Date());
            OmOrganization org = omOrganizationManager.findByOperator(operatorid);
            entity.setTicketOrgid(org != null ? org.getOrgid() : null);
            entity.setTicketOrgname(org != null ? org.getOrgname() : "");
            entity.setTicketOrgseq(org != null ? org.getOrgseq() : "");  
            return entity;  
        } 
    }
    
    /**
     * <li>说明：获取同车同位置同故障现象的未处理的提票列表
     * <li>创建人：程锐
     * <li>创建日期：2015-6-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 车型主键
     * @param trainNo 车号
     * @param faultID 故障现象ID
     * @param fixPlaceFullName 故障位置全名
     * @param faultDesc 故障描述
     * @return 同车同位置同故障现象的未处理的提票列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    private List<FaultTicket> getModelList(String trainTypeIDX, 
                                           String trainNo, 
                                           String faultID,
                                           String fixPlaceFullName, 
                                           String faultDesc) throws BusinessException {
        Map paramMap = new HashMap<String, String>();        
        if (!StringUtil.isNullOrBlank(trainTypeIDX)) {
            paramMap.put("trainTypeIDX", trainTypeIDX);
        }
        if (!StringUtil.isNullOrBlank(trainNo)) {
            paramMap.put("trainNo", trainNo);
        }        
        if (!StringUtil.isNullOrBlank(faultID)) {
            if (PlaceFault.OTHERID.equals(faultID)) {                
                paramMap.put("faultDesc", faultDesc);
            } else {
                paramMap.put("faultID", faultID);
            }
        }
        if (!StringUtil.isNullOrBlank(fixPlaceFullName)) {
            paramMap.put("fixPlaceFullName", fixPlaceFullName);
        }
        paramMap.put(STATUS, FaultTicket.STATUS_DRAFT + "");
        return getTpList(paramMap);
    }
    
    /**
     * <li>说明：获取无提票质量检查的未处理的对应车型车号的提票IDX列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 车型简称
     * @param trainNo 车号
     * @return 无提票质量检查的未处理的对应车型车号的提票IDX列表
     */
    private List getNotComAndQCTpIDXListByTrain(String trainTypeShortName, String trainNo) {
        String sql = SqlMapUtil.getSql("jxgc-tp:getNotComAndQCTpIDXListByTrain")
                               .replace("#STATUS_OVER#", FaultTicket.STATUS_OVER + "")
                               .replace("#trainTypeShortName#", trainTypeShortName)
                               .replace("#trainNo#", trainNo);
        return daoUtils.executeSqlQuery(sql);
    }
    
    /**
     * <li>说明：获取提票列表
     * <li>创建人：程锐
     * <li>创建日期：2015-6-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 提票列表
     */
    @SuppressWarnings("unchecked")
    private List<FaultTicket> getTpList(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from FaultTicket where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0");
        return daoUtils.find(hql.toString());
    }
    
    protected OmOrganizationSelectManager getOmOrganizationSelectManager() {
        return (OmOrganizationSelectManager) Application.getSpringApplicationContext().getBean("omOrganizationSelectManager");
    }
    
    protected OmEmployeeSelectManager getOmEmployeeSelectManager() {
        return (OmEmployeeSelectManager) Application.getSpringApplicationContext().getBean("omEmployeeSelectManager");
    }
    
    /**
     * <li>说明：获取未进行调度派工的提票单数量（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 提票单数量
     */
    public int getCountForDdpg() {
        StringBuilder sb = new StringBuilder("From FaultTicket Where recordStatus = 0");
        // 处理班组为空
        sb.append(" And repairTeam IS NULL");
        sb.append(" And status In ('").append(FaultTicket.STATUS_DRAFT).append("', '").append(FaultTicket.STATUS_OPEN).append("')");
        return this.daoUtils.getCount(sb.toString());
    }
    
    
    /**
     * <li>说明：获取未进行工长派工的提票单数量（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 提票单数量
     */
    public int getCountForGzpg() {
        StringBuilder sb = new StringBuilder("From FaultTicket Where recordStatus = 0");
        // 处理班组为当前系统操作员所在班组
        sb.append(" And repairTeam = '").append(SystemContext.getOmOrganization().getOrgid()).append("'");
        // 处理人为空
        sb.append(" And dispatchEmpID IS NULL");
        sb.append(" And status In ('").append(FaultTicket.STATUS_DRAFT).append("', '").append(FaultTicket.STATUS_OPEN).append("')");
        return this.daoUtils.getCount(sb.toString());
    }
    
    /**
     * <li>说明：获取检查提票处理提票单数量（iPad应用）
     * <li>创建人：何涛
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 提票单数量
     */
    public int getCountForJctpcl() {
        StringBuilder sb = new StringBuilder("From FaultTicket Where recordStatus = 0");
        // 处理班组为当前系统操作员所在班组
        sb.append(" And repairTeam = '").append(SystemContext.getOmOrganization().getOrgid()).append("'");
        // 处理人为当前系统操作员
        sb.append(" And dispatchEmpID Like '%").append(SystemContext.getOmEmployee().getEmpid()).append("%'");
        sb.append(" And status In ('").append(FaultTicket.STATUS_OPEN).append("')");
        return this.daoUtils.getCount(sb.toString());
    }
    
    /**
     * <li>说明：重写分页查询，用于格式化提票单的提票日期
     * <li>创建人：何涛
     * <li>创建日期：2015-7-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param query 查询条件
     * @return Page
     */
    @Override
    public Page<FaultTicket> findPageList(QueryCriteria<FaultTicket> query) throws BusinessException {
        Page<FaultTicket> page = super.findPageList(query);
        List<FaultTicket> list = page.getList();
        for (FaultTicket ft : list) {
            ft.setFaultOccurDateStr(DateUtil.yyyy_MM_dd.format(ft.getFaultOccurDate()));
            ft.setTicketTimeStr(DateUtil.yyyy_MM_dd_HH_mm.format(ft.getTicketTime()));
        }
        return page;
    }

    /**
     * <li>说明：根据在修机车，当前班组查询提票活
     * <li>创建人：张迪
     * <li>创建日期：2016-8-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询封装参数
     * @return  提票信息
     */
    @SuppressWarnings("unchecked")
    public List<FaultTicket> queryFaultTicketList(FaultTicket bean) {
        StringBuilder sb = new StringBuilder(" From FaultTicket Where recordStatus = 0 ");         
//        FaultTicket bean = searchEntity.getEntity();     
        // 未处理的提票活，含（未处理、处理中）
        if(bean.getStatus() != null){
            if (1 == bean.getStatus()) {
                sb.append(" AND  status IN (");
                sb.append(FaultTicket.STATUS_DRAFT).append(", ");  // 运行中
                sb.append(FaultTicket.STATUS_OPEN);       // 未启动      
                sb.append(")");
            // 已处理的提票活
            } else if ( 2 ==bean.getStatus()) {
                sb.append(" AND status = ").append(FaultTicket.STATUS_OVER); //已完成
            }
        }
        
        // 确认、验收状态 20160105
        if(bean.getStatusAffirm() != null){
            if (1 == bean.getStatusAffirm()) {
                sb.append(" AND  statusAffirm  = 1 ");
            } else if ( 2 ==bean.getStatusAffirm()) {
                sb.append(" AND statusAffirm = 2 "); 
            }
        }
        // 查询条件 - 机车检修作业计划主键
        if (!StringUtil.isNullOrBlank(bean.getWorkPlanIDX())) {
            sb.append(" AND workPlanIDX = '").append(bean.getWorkPlanIDX()).append("'");
        }else{
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("workPlanStatus", Condition.IN_STR.concat(Constants.SINGLE_QUOTE_MARK.concat(TrainWorkPlan.STATUS_HANDLING).concat(Constants.SINGLE_QUOTE_MARK)));
            List<TrainWorkPlan> workPlanList = trainWorkPlanQueryManager.getTrainWorkPlanList(paramMap);
            String inIdxs = "" ;
            for (TrainWorkPlan plan : workPlanList) {
                inIdxs += "'"+plan.getIdx()+"',";
            }
            if(!StringUtil.isNullOrBlank(inIdxs)){
                sb.append(" AND workPlanIDX in (").append(inIdxs.substring(0, inIdxs.length()-1)).append(")");
            }
        }
        // 查询条件 - 提票类型
        if (!StringUtil.isNullOrBlank(bean.getType())) {
            sb.append(" AND type = '").append(bean.getType()).append("'");
        }
        // 查询条件 - 当前班组过滤
        if (null != bean.getRepairTeam() && !"".equals(bean.getRepairTeam())) {
            sb.append(" AND repairTeam like '%").append(bean.getRepairTeam()).append("%'");
        }
        // 查询条件 - 客货类型
        if (!StringUtil.isNullOrBlank(bean.getVehicleType())) {
            sb.append(" AND vehicleType = '").append(bean.getVehicleType()).append("'");
        }
        sb.append(" order by status,statusAffirm");
        return  this.daoUtils.find(sb.toString());         
    }
    
    /**
     * <li>说明：根据在修机车，查询验收提票信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询封装参数
     * @return  提票信息
     */
    @SuppressWarnings("unchecked")
    public List<FaultTicket> queryFaultTicketListForCheck(FaultTicket bean) {
        StringBuilder sb = new StringBuilder(" From FaultTicket Where recordStatus = 0 ");         
        // 查询条件 - 机车检修作业计划主键
        if (!StringUtil.isNullOrBlank(bean.getWorkPlanIDX())) {
            sb.append(" AND workPlanIDX = '").append(bean.getWorkPlanIDX()).append("'");
        }else{
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("workPlanStatus", Condition.IN_STR.concat(Constants.SINGLE_QUOTE_MARK.concat(TrainWorkPlan.STATUS_HANDLING).concat(Constants.SINGLE_QUOTE_MARK)));
            List<TrainWorkPlan> workPlanList = trainWorkPlanQueryManager.getTrainWorkPlanList(paramMap);
            String inIdxs = "" ;
            for (TrainWorkPlan plan : workPlanList) {
                inIdxs += "'"+plan.getIdx()+"',";
            }
            if(!StringUtil.isNullOrBlank(inIdxs)){
                sb.append(" AND workPlanIDX in (").append(inIdxs.substring(0, inIdxs.length()-1)).append(")");
            }
        }
        // 提票类型
        sb.append(" AND type in ( select faultTicketType from FaultTicketCheckRule where recordStatus = 0 and isCheck = 1)");
        sb.append(" order by type,status,statusAffirm");
        return  this.daoUtils.find(sb.toString());         
    }
    
    /**
     * <li>说明：只能删除为当前用户的记录
     * <li>创建人：张迪
     * <li>创建日期：2016-09-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 主键数组
     * @return String[] 消息数组
     */ 
    @Override
    public String[] validateDelete(Serializable... ids) {
        // 当前登录用户基本信息
        OmEmployee omEmployee = SystemContext.getOmEmployee();
        for (Serializable idx : ids) {
            FaultTicket entity = this.getModelById(idx);
            if (entity.getTicketEmpId().intValue() != omEmployee.getEmpid().intValue()) {
                return new String[]{"操作失败，您不可以删除其他人员的票活！"};
            }
        }
        return super.validateDelete(ids);
    }

    /**
     * <li>说明：通过兑现idx查询未处理提票数量
     * <li>创建人：张迪
     * <li>创建日期：2016-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 兑现单idx
     * @return 数量 
     */ 
    public int getWClTicketCountByRdpIdx(String workPlanIDX) {
        StringBuilder sb = new StringBuilder(" select IDX From JXGC_Fault_Ticket Where record_Status = 0 and status != ");   
        sb.append(FaultTicket.STATUS_OVER).append(" and work_plan_idx ='").append(workPlanIDX).append("'");
        return   daoUtils.getCountSQL(sb.toString());
    }
    
    /**
     * <li>说明：获取完成的提票数量
     * <li>创建人：张迪
     * <li>创建日期：2016-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 兑现单
     * @return 提票数量
     */
    public int getCompleteFaultTicketCountByWorkPlanIDX(String workPlanIDX) {
        return CommonUtil.getListSize(getCompleteFaultTicketListByWorkPlanIDX(workPlanIDX));
    
    }

    /**
     * <li>说明：获取所有的提票数量
     * <li>创建人：张迪
     * <li>创建日期：2016-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 兑现单
     * @return 提票数量
     */
    public int getFaultTicketCountByWorkPlanIDX(String workPlanIDX) {
        return CommonUtil.getListSize(getFaultTicketListByWorkPlanIDX(workPlanIDX));
   
    }
    
    /**
     * <li>说明：获取兑现单对应的已经销活的提票单
     * <li>创建人：张迪
     * <li>创建日期：2016-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 兑现单
     * @return 提票单列表
     */
    @SuppressWarnings("unchecked")
    public List<FaultTicket> getCompleteFaultTicketListByWorkPlanIDX(String workPlanIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("workPlanIDX", workPlanIDX);        
        paramMap.put("status", FaultTicket.STATUS_OVER);
        return getFaultTicketList(paramMap);
    }
    
    /**
     * <li>说明：获取兑现单对应的全部提票单
     * <li>创建人：张迪
     * <li>创建日期：2016-10-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 兑现单
     * @return 全部提票单
     */
    @SuppressWarnings("unchecked")
    public List<FaultTicket> getFaultTicketListByWorkPlanIDX(String workPlanIDX) {
        Map paramMap = new HashMap<String, String>();
        paramMap.put("workPlanIDX", workPlanIDX);
        return getFaultTicketList(paramMap);
    }
    /**
     * <li>说明：获取提票列表
     * <li>创建人：张迪
     * <li>创建日期：2016-10-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 提票单列表
     */
    @SuppressWarnings("unchecked")
    private List<FaultTicket> getFaultTicketList(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from FaultTicket where 1 = 1 ");
        if(null != paramMap.get("status")){
            String status = paramMap.get("status").toString();
            paramMap.remove("status");
            hql.append(" and status = ").append(Integer.parseInt(status)).append(" ");
        }
      
        hql.append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0 ");
        
        return daoUtils.find(hql.toString());
    }

    /**
     * <li>说明：指派责任人
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityList 提票信息数组
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void assignResponser(FaultTicket[] entityList) throws BusinessException, NoSuchFieldException {
        for (FaultTicket ticket : entityList) {
            FaultTicket entity = this.getModelById(ticket.getIdx());
            if(entity != null){
                entity.setResponseEmpID(ticket.getResponseEmpID());
                entity.setResponseEmp(ticket.getResponseEmp());
                this.saveOrUpdate(entity);
            }
        }
    }

}
