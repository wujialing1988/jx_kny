package com.yunda.jx.jxgc.workplanmanage.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.freight.zb.detain.entity.DetainGztp;
import com.yunda.freight.zb.detain.entity.DetainTrain;
import com.yunda.freight.zb.detain.manager.DetainGztpManager;
import com.yunda.freight.zb.detain.manager.DetainTrainManager;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.jczl.attachmanage.entity.TrainStatusChange;
import com.yunda.jx.jczl.attachmanage.manager.TrainStatusChangeManager;
import com.yunda.jx.jxgc.common.JxgcConstants;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultManager;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketManager;
import com.yunda.jx.jxgc.webservice.entity.TrainYearPlanAndClassBean;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlanDTO;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.scdd.repairplan.manager.RepairWarningHCManager;
import com.yunda.jx.scdd.repairplan.manager.RepairWarningKCManager;
import com.yunda.jx.third.edo.entity.Result;
import com.yunda.jx.util.MixedUtils;
import com.yunda.jx.util.PerformanceMonitor;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
import com.yunda.zb.rdp.zbbill.manager.ZbglRdpManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainWorkPlan业务类,机车检修作业计划
 * <li>创建人：程锐
 * <li>创建日期：2015-04-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */ 
@Service(value = "trainWorkPlanManager")
public class TrainWorkPlanManager extends JXBaseManager<TrainWorkPlan, TrainWorkPlan> {

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 机车检修作业计划甘特图业务类 */
    @Resource
    private WorkPlanGanttManager workPlanGanttManager;

    /** 机车检修作业计划查询业务类 */
    @Resource
    private TrainWorkPlanQueryManager trainWorkPlanQueryManager;
    
    /** 机车检修计划流程节点业务类 */
    @Resource
    private JobProcessNodeManager jobProcessNodeManager;
    
    /** 业务节点查询业务类 */
    @Resource
    private JobProcessNodeQueryManager jobProcessNodeQueryManager;
    
    
    /** 机车检修计划-流水线排程业务类 */
    @Resource
    private RepairLineGroupNewManager repairLineGroupNewManager;    
    
    /** 机车检修作业计划-计算流程节点工期及计划完工时间业务类 */
    @Resource
    private CalcNodeWorkDateManager calcNodeWorkDateManager;
    
    /** 质检结果业务类 */
    @Resource
    private QCResultManager qcResultManager;
    
    /** 提票管理业务类 */
    @Resource
    private FaultTicketManager faultTicketManager;  
    /** TrainAccessAccount业务类,机车出入段台账 */
    @Resource
    private TrainAccessAccountManager trainAccessAccountManager;  
    
    /** 机车入段台账查询 */
    @Resource
    private TrainAccessAccountQueryManager trainAccessAccountQueryManager ;

    /** 整备管理业务类 */
    @Resource
    private ZbglRdpManager zbglRdpManager;
    
    /**
     * 车辆状态业务类
     */
    @Resource
    private TrainStatusChangeManager trainStatusChangeManager;
    
    /**
     * 车辆扣车登记
     */
    @Resource
    private DetainTrainManager detainTrainManager;
    
    /**
     * 车辆扣车故障登记
     */
    @Resource
    private DetainGztpManager detainGztpManager;
    
    /**
     * 货车修程提醒
     */
    @Resource
    private RepairWarningHCManager repairWarningHCManager ;
    
    /**
     * 客车修程提醒
     */
    @Resource
    private RepairWarningKCManager repairWarningKCManager ;
    
    /**
     * 检修作业工单
     */
    @Resource
    private WorkCardManager workCardManager ;
    
    
    
    private static final String STATUS = "#status#";
    
    private static final String WORKPLANIDX = "#workPlanIDX#";
    
    private String statusCH = "状态";
    
    private String siteIDCH = "站点";
    
    private String operaterIdCH = "创建人";
    
    private String createTimeCH = "创建时间";
    
    private String rdpIdxCH = "任务单id";
    
    /**
     * <li>说明：甘特图展示
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @param displayMode 甘特图显示模式：default默认（第一次加载、刷新等）、expanded（对节点操作后加载）
     * @param nodeIdx 操作的节点IDX
     * @return 甘特图Result对象
     * @throws Exception
     */
    public Result planOrderGantt(String workPlanIDX, String displayMode, String nodeIdx) throws Exception {
        TrainWorkPlan workPlan = getModelById(workPlanIDX);
        if (workPlan == null)
            return null;         
        return workPlanGanttManager.planOrderGantt(workPlan, displayMode, nodeIdx);
    }
    
    /**
     * <li>说明：更新前验证
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人： 
     * <li>修改日期：
     * @param entity 机车检修作业计划实体对象
     * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public String[] validateUpdate(TrainWorkPlan entity) throws BusinessException {
        if(StringUtil.isNullOrBlank(entity.getIdx())){
            if(StringUtil.isNullOrBlank(entity.getIdx())){
                
                String msg = trainStatusChangeManager.verificationOperation(entity.getTrainTypeIDX(), entity.getTrainNo(), new Integer[]{JczlTrain.TRAIN_STATE_REPAIR,JczlTrain.TRAIN_STATE_LIEJIAN}, "生成检修作业计划");
                if (!StringUtil.isNullOrBlank(msg)) {
                    String[] errMsg = new String[1];
                    errMsg[0] = msg;
                    return errMsg;
                }
                
                List<TrainWorkPlan> list = trainWorkPlanQueryManager.getTrainWorkPlanListByTrain(entity.getTrainTypeIDX(), entity.getTrainNo());
                if (list != null && list.size() > 0) {
                    String[] errMsg = new String[1];
                    errMsg[0] = "车号为【" + entity.getTrainNo() + "】的【" + entity.getTrainTypeShortName() + "】车辆已有施修任务在处理中，不能重复生成作业计划！";
                    return errMsg;
                }
            }
        }
        return null;
    }
    
    /**
     * <li>说明：校验前验证
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity
     * @return
     * @throws BusinessException
     */
    public String[] validateJY(TrainWorkPlan entity) throws BusinessException {
        if(!StringUtil.isNullOrBlank(entity.getIdx())){
            TrainWorkPlan plan = getModelById(entity.getIdx()) ;
            if (plan == null) {
                String[] errMsg = new String[1];
                errMsg[0] = "该数据不存在，请刷新列表重试！";
                return errMsg;
            }
            if (!plan.getWorkPlanStatus().equals(TrainWorkPlan.STATUS_HANDLED)) {
                String[] errMsg = new String[1];
                errMsg[0] = "只能校验已完成的计划！";
                return errMsg;
            }            
        }
        return null;
    }
    
    
    /**
     * 
     * <li>说明：生成作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 机车检修作业计划实体对象
     * @return 机车检修作业计划实体对象
     * @throws Exception
     */
    public TrainWorkPlan generateWorkPlan(TrainWorkPlan entity) throws Exception {
        if (StringUtil.isNullOrBlank(entity.getIdx())) {
            PerformanceMonitor.begin(logger, true, "TrainWorkPlanManager.generateWorkPlan");
            entity.setWorkPlanStatus(TrainWorkPlan.STATUS_NEW);
            entity.setWorkPlanTime(new Date());
            entity.setFromStatus(0);	// 校验状态 未校验
            entity = generateBizLogicAndWork(entity);                 
            trainWorkPlanQueryManager.updateWorkPlanBeginEndTime(entity);
            PerformanceMonitor.end(logger, "【兑现】", true, "TrainWorkPlanManager.generateWorkPlan");
        } else {
            TrainWorkPlan workPlan = getModelById(entity.getIdx());
            updateRdpForTime(entity, workPlan);
            workPlan = buildEntity(entity, workPlan);
            saveOrUpdate(workPlan);            
        }  
        return entity;
    }
    
    /**
     * <li>说明：保存作业计划，生成对应节点、节点前置关系、生成节点计划时间,调用存储过程生成检修活动、作业工单。。。,根据流水线对作业卡默认派工
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 机车检修作业计划实体对象
     * @return 机车检修作业计划实体对象
     * @throws Exception
     */
    public TrainWorkPlan generateBizLogicAndWork(TrainWorkPlan workPlan) throws Exception {
        PerformanceMonitor.begin(logger, true, "TrainWorkPlanManager.saveBizLogic");
        workPlan = saveBizLogic(workPlan);
        PerformanceMonitor.end(logger, "【保存作业计划，生成对应节点、节点前置关系】", true, "TrainWorkPlanManager.saveBizLogic");
        
        PerformanceMonitor.begin(logger, true, "TrainWorkPlanManager.saveWork");
        saveWork(workPlan); 
        PerformanceMonitor.end(logger, "【调用存储过程生成检修活动、作业工单。。。,根据流水线对作业卡默认派工】", true, "TrainWorkPlanManager.saveWork");
        return workPlan;
    }
    
    /**
     * <li>说明：保存作业计划，生成对应节点、节点前置关系
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 机车检修作业计划实体对象
     * @return 机车检修作业计划实体对象
     * @throws Exception
     */
    public TrainWorkPlan saveBizLogic(TrainWorkPlan workPlan) throws Exception {
        saveOrUpdate(workPlan); 
        jobProcessNodeManager.saveNodeAndSeq(workPlan);            
        return workPlan;
    }
    
    /**
     * <li>说明：调用存储过程生成检修活动、作业工单。。。,根据流水线对作业卡默认派工
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 机车检修作业计划实体对象
     * @throws Exception
     */
    public void saveWork(TrainWorkPlan workPlan) throws Exception{
        
        // 查询在段机车，如果存在，则更新作业计划中与入段台账关联 by wujl 20161013
        TrainAccessAccount trainAccessAccountIn = trainAccessAccountQueryManager.findInAccountByTrainName(workPlan.getTrainTypeShortName(), workPlan.getTrainNo());
        if(trainAccessAccountIn != null && StringUtil.isNullOrBlank(workPlan.getTrainAccessAccountIDX())){
            workPlan.setTrainAccessAccountIDX(trainAccessAccountIn.getIdx());
        }
        
        AcOperator ac = SystemContext.getAcOperator();        
        String proc = "pkg_jxgc_train_workplan.sp_create_train_workplan";
        String[] param = {workPlan.getIdx(), workPlan.getSiteID(), ac.getOperatorid().toString()};
        daoUtils.executeProc(proc, param);
        //根据配件清单类型生成下车、上车、不下车配件记录 
        insertPartsList(workPlan);
        // 保存扣车带过来的故障信息
        saveDefineWorkByDetain(workPlan);
        repairLineGroupNewManager.updateForBatchDispatch(workPlan.getIdx(), workPlan.getProcessIDX());//根据流水线对作业卡默认派工
    }
    
    /**
     * <li>说明：编辑作业计划-根据前台的计划开始时间或日历IDX，更新机车检修作业计划的计划完成时间及流程节点的计划开完工时间
     * <li>创建人：程锐
     * <li>创建日期：2013-11-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 前台页面传递的机车检修作业计划对象
     * @param workPlan 数据库已存的作业计划对象
     * @throws Exception
     */
    public void updateRdpForTime(TrainWorkPlan entity, TrainWorkPlan workPlan) throws Exception{
        Date planBeginTime = entity.getPlanBeginTime();
        String workCalendarIDX = entity.getWorkCalendarIDX();
        if (!(DateUtil.yyyy_MM_dd_HH_mm_ss.format(workPlan.getPlanBeginTime())).equals(DateUtil.yyyy_MM_dd_HH_mm_ss.format(planBeginTime))
            || !workCalendarIDX.equals(workPlan.getWorkCalendarIDX())) { 
            workPlan.setPlanBeginTime(planBeginTime);
            workPlan.setWorkCalendarIDX(entity.getWorkCalendarIDX());
            saveOrUpdate(workPlan);
            calcNodeWorkDateManager.updatePlanTimeByWorkPlan(workPlan, true);
            daoUtils.flush();
            trainWorkPlanQueryManager.updateWorkPlanBeginEndTime(workPlan);
        }
    }
    
    /**
     * <li>说明：启动生产
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @return 错误信息
     * @throws Exception
     */
    public String updateForStartPlan(String workPlanIDX) throws Exception{
        TrainWorkPlan workPlan = getModelById(workPlanIDX);
        workPlan.setWorkPlanStatus(TrainWorkPlan.STATUS_HANDLING);
        workPlan.setBeginTime(new Date());
        saveOrUpdate(workPlan);
        jobProcessNodeManager.startWorkPlan(workPlanIDX);
        faultTicketManager.saveForInstanceTp(workPlan);
        trainWorkPlanQueryManager.updatePlanStatus(workPlan);
        //基线当启动生产的时候，以该时间为基线点
        jobProcessNodeManager.saveBaseTime(workPlanIDX);
        // 更新扣车记录状态
        detainTrainManager.changeDetainStatus(workPlan, DetainTrain.TRAIN_STATE_HANDLING);
        // 更改机车状态
        trainStatusChangeManager.saveChangeRecords(workPlan.getTrainTypeIDX(), workPlan.getTrainNo(), JczlTrain.TRAIN_STATE_REPAIR, workPlan.getIdx(), TrainStatusChange.START_JX); 
        return null;
    }
    
    /**
     * <li>说明：终止作业计划
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIDX 作业计划IDX
     * @throws Exception
     */
    public void updateForTerminatePlan(String workPlanIDX) throws Exception {
        TrainWorkPlan workPlan = getModelById(workPlanIDX);
        workPlan.setWorkPlanStatus(TrainWorkPlan.STATUS_NULLIFY);
        saveOrUpdate(workPlan);
        String sql = SqlMapUtil.getSql("jxgc-processNode:updateNodeStatusByPlan")
                               .replace(STATUS, JobProcessNode.STATUS_STOP)
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                               .replace(WORKPLANIDX, workPlanIDX);
        sql.concat(" AND STATUS != '").concat(JobProcessNode.STATUS_COMPLETE).concat("'");
        daoUtils.executeSql(sql);  
        sql = SqlMapUtil.getSql("jxgc-processNode:updateWorkCardStatusByPlan")
                        .replace(STATUS, WorkCard.STATUS_TERMINATED)
                        .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                        .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                        .replace(WORKPLANIDX, workPlanIDX);
        sql.concat(" AND STATUS not in ('").concat(WorkCard.STATUS_HANDLED).concat("','").concat(WorkCard.STATUS_FINISHED).concat("')");
        daoUtils.executeSql(sql);
        sql = SqlMapUtil.getSql("jxgc-processNode:updateWorkTaskStatusByPlan")
                        .replace(STATUS, WorkTask.STATUS_FINISHED)
                        .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                        .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                        .replace(WORKPLANIDX, workPlanIDX);
        sql.concat(" AND STATUS != '").concat(WorkTask.STATUS_HANDLED).concat("'");
        daoUtils.executeSql(sql);
        sql = SqlMapUtil.getSql("jxgc-tp:terminalTpByPlan")
                        .replace("#STATUS_DRAFT#", FaultTicket.STATUS_DRAFT + "")
                        .replace("#STATUS_OVER#", FaultTicket.STATUS_OVER + "")
                        .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                        .replace("#NO_DELETE#", Constants.NO_DELETE + "")
                        .replace(WORKPLANIDX, workPlanIDX);
        daoUtils.executeSql(sql);        
        qcResultManager.updateTerminateQCResult(workPlanIDX);//终止质量检验结果
        trainWorkPlanQueryManager.updatePlanStatus(workPlan);
        // 回滚扣车信息
        detainTrainManager.changeDetainStatus(workPlan, DetainTrain.TRAIN_STATE_NEW);
        // 回滚车辆状态
        trainStatusChangeManager.robackChangeRecords(workPlan.getTrainTypeIDX(), workPlan.getTrainNo(), workPlanIDX, TrainStatusChange.DEL_JX);
    }
    
    /**
     * <li>说明：作业计划编辑-构造作业计划实体
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 前台传的作业计划实体
     * @param workPlan 数据库中已有的作业计划实体
     * @return 作业计划实体
     */
    private TrainWorkPlan buildEntity(TrainWorkPlan entity, TrainWorkPlan workPlan) {
        workPlan.setDelegateDID(entity.getDelegateDID());
        workPlan.setDelegateDName(entity.getDelegateDName());
        workPlan.setDID(entity.getDID());
        workPlan.setDNAME(entity.getDNAME());
        workPlan.setWorkCalendarIDX(entity.getWorkCalendarIDX());
        workPlan.setRemarks(entity.getRemarks());
        return workPlan;
    }

    /**
     * <li>说明：根据实体查询机车检修作业计划
     * <li>创建人：何涛
     * <li>创建日期：2015-5-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param plan 机车检修作业计划实体
     * @return TrainWorkPlan 机车检修作业计划实体
     */
    public TrainWorkPlan getModelByEntiy(TrainWorkPlan plan) {
        StringBuilder sb = new StringBuilder("From TrainWorkPlan Where recordStatus = 0");
        // 查询条件 - 车型主键
        if (!StringUtil.isNullOrBlank(plan.getTrainTypeIDX())) {
            sb.append(" And trainTypeIDX ='").append(plan.getTrainTypeIDX()).append("'");
        }
        // 查询条件 - 车号
        if (!StringUtil.isNullOrBlank(plan.getTrainNo())) {
            sb.append(" And trainNo ='").append(plan.getTrainNo()).append("'");
        }
        // 查询条件 - 机车作业计划状态
        if (!StringUtil.isNullOrBlank(plan.getWorkPlanStatus())) {
            sb.append(" And workPlanStatus ='").append(plan.getWorkPlanStatus()).append("'");
        }
        sb.append(" order by createTime desc ");
        return (TrainWorkPlan) this.daoUtils.findSingle(sb.toString());
    }
    
    /**
     * <li>说明：通过ID获取对象实体
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanIdx
     * @return
     */
    public TrainWorkPlan getModelByWorkPlanIdx(String workPlanIdx) {
        StringBuilder sb = new StringBuilder("From TrainWorkPlan Where recordStatus = 0");
        // 查询条件 - 主键
        if (!StringUtil.isNullOrBlank(workPlanIdx)) {
            sb.append(" And idx ='").append(workPlanIdx).append("'");
        }
        sb.append(" order by createTime desc ");
        return (TrainWorkPlan) this.daoUtils.findSingle(sb.toString());
    }
    
    /**
     * <li>说明：根据修程查询最近的一次作业计划
     * <li>创建人：何涛
     * <li>创建日期：2016-08-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param repairClassIDX 修程编码
     * @param trainNo 车号
     * @param trainType 车型
     * @return TrainWorkPlan 机车检修作业计划实体
     */
    public TrainWorkPlan getModelByRepairClass(String repairClassIDX, String trainType, String trainNo) {
        StringBuilder sb = new StringBuilder("From TrainWorkPlan Where recordStatus = 0");
        // 查询条件 - 修程
        if (!StringUtil.isNullOrBlank(repairClassIDX)) {
            sb.append(" And repairClassIDX ='").append(repairClassIDX).append("'");
        }
        // 车型
        if (!StringUtil.isNullOrBlank(trainType)) {
            sb.append(" And trainTypeShortName ='").append(trainType).append("'");
        }
        // 车号
        if (!StringUtil.isNullOrBlank(trainNo)) {
            sb.append(" And trainNo ='").append(trainNo).append("'");
        }
        sb.append(" order by createTime desc ");
        return (TrainWorkPlan) this.daoUtils.findSingle(sb.toString());
    }
    
    /**
     * <li>说明：获取在修机车树列表
     * <li>创建人：程锐
     * <li>创建日期：2014-5-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param params 参数
     * @return 在修机车树列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> findRdpTreeData(Map<String,String> params)throws BusinessException {
        boolean isLeaf = true;  
        List<HashMap> children = new ArrayList<HashMap>();
        if (params.get("parentIDX") != null && "ROOT_0".equals(params.get("parentIDX"))){//为第一级，查出下级所有承修车型          
            isLeaf = false ;
            List<Object> list = findRdpGroupByTrain();          
            for (Object obj : list) {           
                HashMap nodeMap = new HashMap();
                nodeMap.put("text", obj.toString());         //车型简称
                nodeMap.put("leaf", isLeaf);
                children.add(nodeMap);
            }
        }else if (params.get("trainTypeName") != null ){    
            List<Object[]> list = findRdpByTrain(params.get("trainTypeName").toString());           
            for (Object[] obj : list) { 
                HashMap nodeMap = new HashMap();
                nodeMap.put("id", obj[0].toString());
                String planDay = "(" + (null == obj[9]?"":obj[9].toString()) + ")";
                String billStatus = "【" + TrainWorkPlan.getStatusMeaning(obj[8].toString()) + "】";
                nodeMap.put("text", (null==obj[2]?"":obj[2].toString()) + (null==obj[5]?"":obj[5].toString()) + (null==obj[7]?"":obj[7].toString()) + billStatus + planDay);         //车型简称
                nodeMap.put("trainTypeIDX", obj[1].toString());
                nodeMap.put("trainNo", obj[2].toString());
                nodeMap.put("trainTypeShortName", obj[3].toString());
                nodeMap.put("repairClassIDX", obj[4].toString());
                nodeMap.put("repairClassName", obj[5].toString());
                nodeMap.put("repairTimeIDX",null == obj[6]?"":obj[6].toString());
                nodeMap.put("repairTimeName", null == obj[7]?"":obj[7].toString());
                nodeMap.put("leaf", isLeaf);
                children.add(nodeMap);
            }               
        }
        return children;
    }
    /**
     * 
     * <li>说明：根据车型分组查询未启动和处理中的机车兑现单列表
     * <li>创建人：程锐
     * <li>创建日期：2014-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 机车兑现单列表
     */
    @SuppressWarnings(value="unchecked")
    private List<Object> findRdpGroupByTrain() {
        String sql = SqlMapUtil.getSql("jxgc-rdp:findRdpGroupByTrain")
                                .replace("#ONGOING#", TrainWorkPlan.STATUS_HANDLING)
                                .replace("#INITIALIZE#", TrainWorkPlan.STATUS_NEW)
                                .replace("#COMPLETE", TrainWorkPlan.STATUS_HANDLED);
        return daoUtils.executeSqlQuery(sql);
    }
    
    /**
     * <li>说明：根据车型查询未启动和处理中的机车兑现单列表
     * <li>创建人：程锐
     * <li>创建日期：2014-5-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeName 车型简称
     * @return 未启动和处理中的机车兑现单列表
     */
    @SuppressWarnings(value="unchecked")
    private List<Object[]> findRdpByTrain(String trainTypeName) {
        String sql = SqlMapUtil.getSql("jxgc-rdp:findRdpByTrain")
                                .replace("#ONGOING#", TrainWorkPlan.STATUS_HANDLING)
                                .replace("#INITIALIZE#", TrainWorkPlan.STATUS_NEW)
                                .replace("#COMPLETE", TrainWorkPlan.STATUS_HANDLED)
                                .replace("#trainTypeShortName#", trainTypeName);    
        return daoUtils.executeSqlQuery(sql);
    }
    
    /**
     * <li>说明：生成配件清单
     * <li>创建人：程锐
     * <li>创建日期：2016-2-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainWorkPlan 机车作业计划实体
     */
    private void insertPartsList(TrainWorkPlan trainWorkPlan) {
    	Long operaterId = SystemContext.getAcOperator().getOperatorid();
    	
    	//生成不下车配件记录
        String inseparablPartsSql = SqlMapUtil.getSql("jxgc-rdp:insertInseparablParts")
        									  .replace(rdpIdxCH, trainWorkPlan.getIdx());
        daoUtils.executeSql(inseparablPartsSql);
        //生成下车配件登记
        String unloadPartsSql = SqlMapUtil.getSql("jxgc-rdp:insertUnloadParts")
									        .replace(statusCH, PjwzConstants.STATUS_WAIT)
									        .replace(siteIDCH, trainWorkPlan.getSiteID())
									        .replace(operaterIdCH, operaterId.toString())
									        .replace(createTimeCH, MixedUtils.dateToStr(null, 2))
									        .replace(rdpIdxCH, trainWorkPlan.getIdx());
        daoUtils.executeSql(unloadPartsSql);
        //生成上车配件登记
        String installPartsSql = SqlMapUtil.getSql("jxgc-rdp:insertFixParts")
									        .replace(statusCH, PjwzConstants.STATUS_WAIT)
									        .replace(siteIDCH, trainWorkPlan.getSiteID())
									        .replace(operaterIdCH, operaterId.toString())
									        .replace(createTimeCH, MixedUtils.dateToStr(null, 2))
									        .replace(rdpIdxCH, trainWorkPlan.getIdx());
		daoUtils.executeSql(installPartsSql);
    }
    
    /**
     * <li>说明：保存扣车带过来的故障信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2018-01-05
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainWorkPlan 机车作业计划实体
     * @throws Exception 
     */
    private void saveDefineWorkByDetain(TrainWorkPlan trainWorkPlan) throws Exception {
    	// 扣车登记的故障
    	List<DetainGztp>  gztps = detainGztpManager.findDetainGztp(trainWorkPlan.getDetainIdx());
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("workPlanIDX", trainWorkPlan.getIdx());
    	params.put("isLxNode", "1");
    	JobProcessNode node = jobProcessNodeQueryManager.getNode(params);
    	if(node != null && gztps != null){
    		workCardManager.saveDefineWorkByDetain(trainWorkPlan.getIdx(), node.getIdx(), gztps);
    	}
    }
    

    /**
     * <li>说明：机车检修安全生产日报查询
     * <li>创建人：林欢
     * <li>创建日期：2016-6-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map，目前查询参数【searchDate】
     * @return List<TrainWorkPlanDTO> 机车检修安全生产日报List
     * @throws ParseException 
     */
    @SuppressWarnings("unchecked")
    public List<TrainWorkPlanDTO> searchTrainWorkPlanDailtReportInfo(Map<String, Object> paramMap) throws ParseException {
        List<TrainWorkPlanDTO> returnList = new ArrayList<TrainWorkPlanDTO>();
        //判断传入是否是否为空，如果为空，默认查询条件为当前时间
        String searchDate;
        //明天时间
        if (paramMap.get("searchDate") == null || StringUtil.isNullOrBlank(paramMap.get("searchDate").toString())) {
            searchDate = DateUtil.yyyy_MM_dd.format(new Date());
        }else {
            searchDate = paramMap.get("searchDate").toString();
        }
        
        //必须这样写（weblogic才能使用,具体的问林欢）
        String tomorrowDate = DateUtil.yyyy_MM_dd.format((DateUtil.yyyy_MM_dd.parse(searchDate).getTime()+24*60*60*1000)); 
        String sql = SqlMapUtil.getSql("jxgc-rdp:searchTrainWorkPlanDailtReportInfo")
                .replace("#searchDate#", searchDate);
        List<Object[]> list =  daoUtils.executeSqlQuery(sql);
        for (Object[] objects : list) {
            TrainWorkPlanDTO dto = new TrainWorkPlanDTO();
            
            //机车作业计划主键idx
            String trainWorkPlanIDX = objects[0].toString();
            
            dto.setIdx(objects[0] == null ? "" : trainWorkPlanIDX);
            dto.setTrainTypeAndNo(objects[1] == null ? "" : objects[1].toString());
            dto.setRepairClassNameAndTime(objects[2] == null ? "" : objects[2].toString());
            dto.setDeserveName(objects[3] == null ? "" : objects[3].toString());
            dto.setDelegateDName(objects[4] == null ? "" : objects[4].toString());
            dto.setInTime(objects[5] == null ? "" : objects[5].toString());
            dto.setBeginTime(objects[6] == null ? "" : objects[6].toString());
            
            //根据检修计划ID查询今日计划
            String nodeName = jobProcessNodeManager.findTodayNodeNameByDate(trainWorkPlanIDX,searchDate);
            dto.setNodeName(nodeName);
            
//          根据检修计划ID查询当前工位
            String workStationName = jobProcessNodeManager.findTodayWorkStationNameByDate(trainWorkPlanIDX,searchDate);
            dto.setWorkStationName(workStationName);
            
            dto.setPlanStateTime(objects[7] == null ? "0" : String.valueOf(new BigDecimal(objects[7].toString()).intValue()));
            dto.setRealStateTime(objects[8] == null ? "0" : String.valueOf(new BigDecimal(objects[8].toString()).intValue()));
            dto.setTrainNo(objects[9] == null ? "" : objects[9].toString());
            dto.setTrainTypeShortName(objects[10] == null ? "" : objects[10].toString());
            dto.setRepairClassName(objects[11] == null ? "" : objects[11].toString());
            dto.setRepairtimeName(objects[12] == null ? "" : objects[12].toString());
            dto.setPlanBeginTime(objects[13] == null ? "" : objects[13].toString());
            dto.setPlanEndTime(objects[14] == null ? "" : objects[14].toString());
            
//          根据检修计划ID查询明日计划
            String tomorrowNodeName = jobProcessNodeManager.findTodayNodeNameByDate(trainWorkPlanIDX,tomorrowDate);
            dto.setTomorrowNodeName(tomorrowNodeName);
            
            returnList.add(dto);
        }
        return returnList;
    }
    
    /**
     * <li>说明：根据年份查询机车年计划下修程中的计划台数、进站台数、竣修台数的集合
     * <li>创建人：陈志刚
     * <li>创建日期：2016-11-09
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param year 年份
     * @return
     */
    public Page<TrainYearPlanAndClassBean> searchClassAndWorkPlan(String year){
        String sql = SqlMapUtil.getSql("jxgc-rdp:searchClassAndWorkPlan")
        .replace("#Year#", year);
        String totalSql = "Select count(*) as rowcount "+ sql.substring(sql.indexOf("from"));
        return this.queryPageList(totalSql, sql, 0, 10000, false, TrainYearPlanAndClassBean.class);
    }

    /**
     * <li>说明：校验计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan
     * @return
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    public TrainWorkPlan jyTrainWorkPlan(TrainWorkPlan workPlan) throws BusinessException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        TrainWorkPlan plan = getModelByWorkPlanIdx(workPlan.getIdx());
        if(plan != null){
            plan.setFromPersonId(workPlan.getFromPersonId());
            plan.setFromPersonName(workPlan.getFromPersonName());
            plan.setFromRemark(workPlan.getFromRemark());
            plan.setFromTime(new Date());
            plan.setFromStatus(1);
            // 获取当前人员
            OmEmployee employee = SystemContext.getOmEmployee();
            if(employee != null){
                plan.setToPersonId(employee.getEmpid());
                plan.setToPersonName(employee.getEmpname());
            }
            saveOrUpdate(plan);
            // 车辆状态改变
            trainStatusChangeManager.saveChangeRecords(plan.getTrainTypeIDX(), plan.getTrainNo(), JczlTrain.TRAIN_STATE_USE, plan.getIdx(), TrainStatusChange.COM_JX);
            // 更新扣车登记信息
            detainTrainManager.changeDetainStatus(plan, DetainTrain.TRAIN_STATE_HANDLED);
            // 清空货车修程提醒
            repairWarningHCManager.clearHCBeForeDate(plan.getTrainTypeIDX(), plan.getTrainNo(), plan.getRepairClassIDX(),new Date());
            // 更新客车修程走行及修程时间
            repairWarningKCManager.clearKCBeForeDate(plan.getTrainTypeIDX(), plan.getTrainNo(), plan.getRepairClassIDX(),new Date());
            
        }
        return plan;
    }
    
    /**
     * <li>说明：查询正在检修的车辆
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-11-25
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public List<TrainWorkPlan> findTrainWorkPlanListByStutas(String vehicleType,String planStutas){
    	StringBuffer hql = new StringBuffer(" From TrainWorkPlan where recordStatus = 0 and workPlanStatus = ? ");
    	// 客货类型
    	if(!StringUtil.isNullOrBlank(vehicleType)){
    		hql.append(" and vehicleType = '"+vehicleType+"'");
    	}
    	return (List<TrainWorkPlan>)this.daoUtils.find(hql.toString(), new Object[]{planStutas});
    }
    
    /**
     * <li>说明：查询车辆检修列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-02
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     */
    public List<TrainWorkPlan> findTrainWorkPlanListByTrain(String trainTypeIDX, String trainNo,String vehicleType){
    	StringBuffer hql = new StringBuffer(" From TrainWorkPlan where recordStatus = 0 and workPlanStatus in ('ONGOING','COMPLETE') and trainTypeIDX = ? and trainNo = ? ");
    	// 客货类型
    	if(!StringUtil.isNullOrBlank(vehicleType)){
    		hql.append(" and vehicleType = '"+vehicleType+"'");
    	}
    	return (List<TrainWorkPlan>)this.daoUtils.find(hql.toString(), new Object[]{trainTypeIDX,trainNo});
    }
    
    
    
}