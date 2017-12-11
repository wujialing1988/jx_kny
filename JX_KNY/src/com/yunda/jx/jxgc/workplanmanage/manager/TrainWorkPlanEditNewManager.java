package com.yunda.jx.jxgc.workplanmanage.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jx.jczl.attachmanage.entity.JczlTrain;
import com.yunda.jx.jczl.attachmanage.manager.TrainStatusChangeManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketManager;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeFlowSheetBean;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlanFlowSheetBean;
import com.yunda.jx.pjwz.common.PjwzConstants;
import com.yunda.jx.pjwz.turnover.entity.OffPartListFlowSheetBean;
import com.yunda.jx.pjwz.turnover.manager.OffPartListManager;
import com.yunda.jx.third.edo.entity.Result;
import com.yunda.jx.util.MixedUtils;
import com.yunda.jx.util.PerformanceMonitor;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车检修作业计划编制（新）
 * <li>创建人：张迪
 * <li>创建日期：2017-1-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="trainWorkPlanEditNewManager")
public class TrainWorkPlanEditNewManager extends JXBaseManager<TrainWorkPlan, TrainWorkPlan> {
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
        private JobProcessNodeNewManager jobProcessNodeNewManager;
        
        /** 机车检修计划-流水线排程业务类 */
        @Resource
        private RepairLineGroupNewManager repairLineGroupNewManager;    
        
        /** 机车检修作业计划-计算流程节点工期及计划完工时间业务类(新) */
        @Resource
        private CalcNodeWorkDateNewManager calcNodeWorkDateNewManager;
        /** 机车入段台账查询 */
        @Resource
        private TrainAccessAccountQueryManager trainAccessAccountQueryManager ;
        @Resource
        private TrainAccessAccountManager trainAccessAccountManager;  
        
        @Resource
        private JobProcessNodeQueryManager jobProcessNodeQueryManager;
        /** 提票管理业务类 */
        @Resource
        private FaultTicketManager faultTicketManager; 
        
        /** 下车配件实例化业务类 */
        @Resource
        private OffPartListManager offPartListManager; 
        
        
        /** 节点流程图显示业务类 */
        @Resource
        private JobProcessNodeFlowSheetManager jobProcessNodeFlowSheetManager; 
        
        /**
         * 车辆状态业务类
         */
        @Resource
        private TrainStatusChangeManager trainStatusChangeManager;
        

//        private static final String STATUS = "#status#";
//        
//        private static final String WORKPLANIDX = "#workPlanIDX#";
        
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
                entity.setFromStatus(0);    // 设置验收状态
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
            jobProcessNodeNewManager.saveNodeAndSeq(workPlan);            
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
            
            //保存之前判断是否传入的车型车号在机车维护中存在，如果不存在，新增
//            if (!trainAccessAccountManager.isExistInJczlTrain(workPlan.getTrainTypeIDX(), workPlan.getTrainNo())) {
//                TrainAccessAccount trainAccessAccount = new TrainAccessAccount();
//                trainAccessAccount.setTrainNo(workPlan.getTrainNo());
//                trainAccessAccount.setTrainTypeIDX(workPlan.getTrainTypeIDX());
//                trainAccessAccount.setTrainTypeShortName(workPlan.getTrainTypeShortName());
//                trainAccessAccount.setDID(workPlan.getDID());
//                trainAccessAccountManager.saveJczlTrain(trainAccessAccount);
//            }
            
            AcOperator ac = SystemContext.getAcOperator();        
            String proc = "pkg_jxgc_train_workplan.sp_create_train_workplan";
            String[] param = {workPlan.getIdx(), workPlan.getSiteID(), ac.getOperatorid().toString()};
            daoUtils.executeProc(proc, param);
            //根据配件清单类型生成下车、上车、不下车配件记录 
            insertPartsList(workPlan);
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
                calcNodeWorkDateNewManager.updatePlanTimeByWorkPlan(workPlan, true);
                daoUtils.flush();
                trainWorkPlanQueryManager.updateWorkPlanBeginEndTime(workPlan);
            }
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
         * <li>说明：机车检修作业计划编制查询检修作业节点，按节点顺序，依次显示检修作业节点(最新修改)
         * <li>创建人：张迪
         * <li>创建日期：2016-7-9
         * <li>修改人： 
         * <li>修改日期：
         * <li>修改内容：
         * @param searchEntity 包装了实体类查询条件的对象
         * @return 分页查询列表
         * @throws BusinessException
         */
        public Page<TrainWorkPlan> queryWorkPlanList(SearchEntity<TrainWorkPlan> searchEntity) throws BusinessException {
            TrainWorkPlan entity = searchEntity.getEntity();
            StringBuilder sb = new StringBuilder("From TrainWorkPlan Where recordStatus = 0");
            if (!StringUtil.isNullOrBlank(entity.getWorkPlanStatus())) {
                sb.append(" And workPlanStatus In ('").append(entity.getWorkPlanStatus().replace(",", "','")).append("')");
            }
            // 根据“车型”“车号”“修程”“修次”模糊匹配查询
            String trainNo = StringUtil.nvl(entity.getTrainNo(), "").trim();
            if (!StringUtil.isNullOrBlank(trainNo)) {
                sb.append(" And trainTypeShortName || trainNo || repairClassName || repairtimeName Like '%").append(trainNo.toUpperCase()).append("%'");
            }
            
            // 客货类型 10 货车 20 客车
            String vehicleType = StringUtil.nvl(entity.getVehicleType(), "").trim();
            if (!StringUtil.isNullOrBlank(vehicleType)) {
                sb.append(" And vehicleType = '").append(vehicleType).append("'");
            }
            
            // 以“计划开始日期”进行升序排序
            sb.append(" Order By planBeginTime DESC");
            String hql = sb.toString();
            String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
            Page<TrainWorkPlan> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
            List<TrainWorkPlan> list = page.getList();    
           
            for (TrainWorkPlan plan : list) {
                // 查询机车检修作业计划下属第一层级的作业流程节点实体
                try {
                    plan.setJobProcessNodeBeans(jobProcessNodeNewManager.getFirstNodeForPlan(plan.getIdx()));
                    List<Object[]> timeList = jobProcessNodeQueryManager.getMinBeginAndMaxEndRealTime(plan.getIdx()); 
                    for (Object[] obj : timeList) {
                        if (obj[0] == null || obj[1] == null)
                            continue;
                        plan.setMinRealTime(DateUtil.yyyy_MM_dd_HH_mm_ss.parse(obj[0].toString()));
                        plan.setMaxRealTime(DateUtil.yyyy_MM_dd_HH_mm_ss.parse(obj[1].toString()));
                    }
                  
                } catch (Exception e) {
                    throw new BusinessException(e);
                }
            }
            return page;
        }

        /**
         * <li>说明：机车检修作业计划编制查询检修作业流程
         * <li>创建人：张迪
         * <li>创建日期：2017-1-10
         * <li>修改人： 
         * <li>修改日期：
         * <li>修改内容：
         * @param searchEntity 查询参数
         * @return
         */
        public Page<TrainWorkPlan> queryTrainWorkPlan(SearchEntity<TrainWorkPlan> searchEntity) {
            TrainWorkPlan entity = searchEntity.getEntity();
            StringBuilder sb = new StringBuilder("From TrainWorkPlan Where recordStatus = 0 And workPlanStatus In('");  
            sb.append(TrainWorkPlan.STATUS_NEW).append("', '").append(TrainWorkPlan.STATUS_HANDLING).append("')");

            // 根据“车型”“车号”“修程”“修次”模糊匹配查询
            String trainNo = StringUtil.nvl(entity.getTrainNo(), "").trim();
            if (!StringUtil.isNullOrBlank(trainNo)) {
                sb.append(" And trainTypeShortName || trainNo || repairClassName || repairtimeName Like '%").append(trainNo.toUpperCase()).append("%'");
            }
            
            // 客货类型 10 货车 20 客车
            String vehicleType = StringUtil.nvl(entity.getVehicleType(), "").trim();
            if (!StringUtil.isNullOrBlank(vehicleType)) {
                sb.append(" And vehicleType = '").append(vehicleType).append("'");
            }
            
            // 以“计划开始日期”进行升序排序
            sb.append(" Order By planBeginTime DESC");
            String hql = sb.toString();
            String totalHql = "Select Count(*) As rowcount " + hql.substring(hql.indexOf("From"));
            Page<TrainWorkPlan> page = this.findPageList(totalHql, hql, searchEntity.getStart(), searchEntity.getLimit());
            return page;
        }
        
        /**
         * <li>说明：启动相关节点- 启动作业计划
         * <li>创建人：张迪
         * <li>创建日期：2017-1-14
         * <li>修改人： 
         * <li>修改日期：
         * <li>修改内容：
         * @param workPlanIDX 计划IDX
         * @return NULL
         * @throws Exception
         */
        public String updateForStartPlan(String workPlanIDX) throws Exception{
            TrainWorkPlan workPlan = getModelById(workPlanIDX);
            if(workPlan.getWorkPlanStatus().equals(TrainWorkPlan.STATUS_NEW)){
                workPlan.setWorkPlanStatus(TrainWorkPlan.STATUS_HANDLING);
                workPlan.setBeginTime(new Date());
                saveOrUpdate(workPlan);
                faultTicketManager.saveForInstanceTp(workPlan);
                trainWorkPlanQueryManager.updatePlanStatus(workPlan);
                //基线当启动生产的时候，以该时间为基线点
                jobProcessNodeNewManager.saveBaseTime(workPlanIDX);
            }
            return null;
        } 
        
        /**
         * <li>说明：查询机车检修作业计划第一层节点信息
         * <li>创建人：张迪
         * <li>创建日期：2017-2-9
         * <li>修改人： 
         * <li>修改日期：
         * <li>修改内容：
         * @param workPlanIDX 机车检修作业计划主键
         * @return 机车检修作业计划第一层节点信息
         * @throws Exception
         */
        @SuppressWarnings("unchecked")
        public Page<TrainWorkPlanFlowSheetBean> getFirstNodeListByRdpIDX(SearchEntity<TrainWorkPlan> searchEntity) throws Exception {
            TrainWorkPlan entity = searchEntity.getEntity();
            List< TrainWorkPlanFlowSheetBean>  workPlanFlowSheet = new ArrayList<TrainWorkPlanFlowSheetBean>();
            List<JobProcessNodeFlowSheetBean> fisrtNodes = jobProcessNodeFlowSheetManager.getFirstNodeByWorkPlanIDX(entity.getIdx(),null);           
            if( fisrtNodes !=null && fisrtNodes.size()>0){
                for (JobProcessNodeFlowSheetBean node : fisrtNodes) {
                    // 查询第一层级的作业流程节点的下级节点实体
                    node.setLeafNodes(jobProcessNodeNewManager.getChildNodeListByParentNode(node.getIdx()));  
                    node.setDelayCount(jobProcessNodeNewManager.getChildNodeCountByParentNode(node.getIdx()));
                }
            }
            TrainWorkPlanFlowSheetBean bean = new TrainWorkPlanFlowSheetBean();
            bean.setIdx(entity.getIdx());
            bean.setFisrtNodes(fisrtNodes);
            //  获取已经兑现的下车配件
            List<OffPartListFlowSheetBean> offPartList = offPartListManager.getOffPartListByWorkPlanIDX(entity.getIdx());
            bean.setOffPartList(offPartList);
            workPlanFlowSheet.add(bean);
            return new Page(workPlanFlowSheet);
        }
      
    }
