package com.yunda.jx.jxgc.workplanmanage.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarDetailManager;
import com.yunda.baseapp.workcalendar.manager.WorkCalendarInfoUtil;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.manager.IOmEmployeeManager;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlanCount;
import com.yunda.jx.pjwz.unloadparts.entity.PartsUnloadRegisterNewBean;
import com.yunda.jx.scdd.enforceplan.manager.TrainEnforcePlanDetailManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车检修作业计划查询业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-4-29
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "trainWorkPlanQueryManager")
public class TrainWorkPlanQueryManager extends JXBaseManager<TrainWorkPlan, TrainWorkPlan> implements IbaseCombo {

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 人员表查询接口类 */
    @Resource(name="omEmployeeManager")
    private IOmEmployeeManager omEmployeeManager;
    
    /** 机车检修计划流程节点查询业务类 */
    @Resource
    private JobProcessNodeQueryManager jobProcessNodeQueryManager;
    
    /** 工作日历业务类 */
    @Resource
    private WorkCalendarDetailManager workCalendarDetailManager;
    
    /** 生产计划明细业务类 */
    @Resource
    private TrainEnforcePlanDetailManager trainEnforcePlanDetailManager;
    
    private static final String SQLSTR = "###";
    
    /**
     * <li>说明：根据机车IDX和车号获取【初始化】和【处理中】状态的机车作业计划列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIDX 机车IDX
     * @param trainNo 车号
     * @return 【初始化】和【处理中】状态的机车作业计划列表
     */
    public List<TrainWorkPlan> getTrainWorkPlanListByTrain(String trainTypeIDX, String trainNo) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("trainTypeIDX", trainTypeIDX);
        paramMap.put("trainNo", trainNo);
        paramMap.put("workPlanStatus", Condition.IN_STR.concat(Constants.SINGLE_QUOTE_MARK).concat(TrainWorkPlan.STATUS_NEW).concat(Constants.SINGLE_QUOTE_MARK).concat(Constants.JOINSTR).concat(Constants.SINGLE_QUOTE_MARK).concat(TrainWorkPlan.STATUS_HANDLING).concat(Constants.SINGLE_QUOTE_MARK));
        return getTrainWorkPlanList(paramMap);        
    }
    
    /**
     * <li>说明：根据机车简称和车号获取【处理中】状态的机车作业计划列表
     * <li>创建人：程锐
     * <li>创建日期：2015-6-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 车型简称
     * @param trainNo 车号
     * @return 【处理中】状态的机车作业计划列表
     */
    public TrainWorkPlan getTrainWorkPlanByTrain(String trainTypeShortName, String trainNo) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("trainTypeShortName", trainTypeShortName);
        paramMap.put("trainNo", trainNo);
        paramMap.put("workPlanStatus", TrainWorkPlan.STATUS_HANDLING);
        return getTrainWorkPlan(paramMap);        
    }
    
    /**
     * <li>说明：获取机车检修作业计划列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 机车检修作业计划列表
     */
    @SuppressWarnings("unchecked")
    public List<TrainWorkPlan> getTrainWorkPlanList(Map paramMap) {
        return daoUtils.find(getTrainWorkPlanHql(paramMap));
    }
    
    
    /**
     * <li>说明：获取在修机车范围活，提票活对应数量
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     */
    public List<TrainWorkPlanCount> getTrainWorkPlanCount(){
        StringBuffer sql = new StringBuffer();
        sql.append(" select wp.idx,wp.train_type_shortname,wp.train_no,wp.repair_time_name,wp.repair_class_name,nvl(t1.fwhUndoCount,0) as fwhUndoCount,nvl(t2.tphUndoCount,0) as tphUndoCount From ");
        sql.append(" JXGC_Train_Work_Plan wp left join ");
        sql.append(" ( ");
        sql.append(" select n.work_plan_idx, count(1) as fwhUndoCount from JXGC_Job_Process_Node n ");
        sql.append(" inner join JXGC_Train_Work_Plan p on n.Work_Plan_IDX = p.idx ");
        sql.append(" inner join JCGY_WORK_CALENDAR_INFO i on p.Work_Calendar_IDX = i.idx  ");
        sql.append(" left join (select * from ( SELECT T.*,ROW_NUMBER() OVER (PARTITION BY  T.NODE_CASE_IDX ORDER BY T.IDX)RV fROM JXGC_Node_Case_Delay T)  where RV=1) d on d.NODE_CASE_IDX = n.idx ");
        sql.append(" WHERE n.record_status = 0 and n.Is_Leaf = 1 AND  n.status IN ('RUNNING', 'NOTSTARTED')  ");
        sql.append(" group by n.work_plan_idx ");
        sql.append(" ) t1 on t1.work_plan_idx = wp.idx ");
        sql.append(" left join  ");
        sql.append(" ( ");
        sql.append(" select t.work_plan_idx ,count(1) as tphUndoCount from JXGC_Fault_Ticket t where t.record_status = 0 and t.status in (10,20)  ");
        sql.append(" group by t.work_plan_idx ");
        sql.append(" ) t2 on t2.work_plan_idx = wp.idx ");
        sql.append(" where wp.record_status = 0 and wp.work_plan_status = 'ONGOING' ");
        sql.append(" order by wp.train_type_shortname , wp.train_no ");
        String totalSql = "Select count(*) as rowcount " + sql.substring(sql.indexOf("From"));
        Page<TrainWorkPlanCount> page = this.queryPageList(totalSql, sql.toString(), 0, 9999, false, TrainWorkPlanCount.class);
        return page.getList();
    }
    
    
    /**
     * <li>说明：获取机车检修作业计划实体
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 机车检修作业计划实体
     */
    public TrainWorkPlan getTrainWorkPlan(Map paramMap) {
        return (TrainWorkPlan) daoUtils.findSingle(getTrainWorkPlanHql(paramMap));
    }

    /**
     * <li>说明：工长派工-查询生产任务单
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param req HttpServletRequest对象
     * @param start 开始行
     * @param limit 本页记录数
     * @return map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit){
        String nDispatcher = "have_default_person = '0' or have_default_person is null";  //未派工查询条件
        String yDispatcher = "have_default_person = '1'"; //已派工查询条件
        String queryParams = req.getParameter("queryParams"); //获取查询参数,并解析为Map
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(queryParams)) {
            try {
                queryParamsMap = JSONUtil.read(queryParams, Map.class);
            } catch (JsonParseException e) {
                ExceptionUtil.process(e,logger);
            } catch (JsonMappingException e) {
                ExceptionUtil.process(e,logger);
            } catch (IOException e) {
                ExceptionUtil.process(e,logger);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        HttpSession session = req.getSession();
        String team = null;
        if(session != null && session.getAttribute("tream") != null){
            team = session.getAttribute("tream").toString();
        }
        if(team == null){   //修正team为空抛错的问题
            return map;
        }
        String sql = SqlMapUtil.getSql("jxgc-workcardquery:findByTrainAndParts").replace("?", team);
        //如果当前需查询的是已派工数据
        if(queryParamsMap!=null&&"y".equals(queryParamsMap.get("isDispatcher"))){
            sql = sql.replace(SQLSTR, yDispatcher);
        } else {
            sql = sql.replace(SQLSTR, nDispatcher);
        }
  
        String totalSql = "select count(*) as \"rowcount\" from ("+sql+")";
        Page page = findPageList(totalSql, sql, start, limit, null, null);
        map = page.extjsStore();
        return map;
    }
    
    /**
     * TODO 和getBaseComboData合并重构
     * <li>说明：与getBaseComboData()功能相同，本方法为工位终端接口调用，查询工长派工中生产任务单选择控件
     * <li>创建人：谭诚
     * <li>创建日期：2013-10-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson {"isDispatcher":"n","operatorid":ddd}  /  {'isDispatcher':'y',"operatorid":ddd}}
     *        isDispatcher=y //已派工
     *        isDispatcher=n //未派工
     *        operatorid     //当前登录操作员Id
     * @param start 起始页
     * @param limit 每页条数
     * @return 返回值说明
     * @throws 抛出异常列表    
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getWorkPlanByForeman(String searchJson,int start, int limit){
        String nDispatcher = "have_default_person = '0' or have_default_person is null";  //未派工查询条件
        String yDispatcher = "have_default_person = '1'"; //已派工查询条件
        Map queryParamsMap = new HashMap();
        if (!StringUtil.isNullOrBlank(searchJson)) {
            try {
                queryParamsMap = JSONUtil.read(searchJson, Map.class);
            } catch (JsonParseException e) {
                ExceptionUtil.process(e,logger);
            } catch (JsonMappingException e) {
                ExceptionUtil.process(e,logger);
            } catch (IOException e) {
                ExceptionUtil.process(e,logger);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        String operatorid = String.valueOf(queryParamsMap.get("operatorid"));
        Long teamId = omEmployeeManager.findByOperator(Long.parseLong(operatorid)).getOrgid();
        String sql = SqlMapUtil.getSql("jxgc-workcardquery:findByTrainAndParts").replace("?", String.valueOf(teamId));
        //如果当前需查询的是已派工数据
        if(queryParamsMap!=null&&"y".equals(queryParamsMap.get("isDispatcher"))){
            sql = sql.replace(SQLSTR, yDispatcher);
        } else {
            sql = sql.replace(SQLSTR, nDispatcher);
        }
  
        String totalSql = "select count(*) as \"rowcount\" from ("+sql+")";
        Page<TrainWorkPlan> page = findPageList(totalSql,sql,start,limit,null,null);
        map = page.extjsStore();
        return map;
    }
    
    /**
     * <li>方法说明：作业工单查询条件查询生产任务单数据 
     * <li>方法名称：getComboDataByWorkCardQuery
     * <li>@param start
     * <li>@param limt
     * <li>@return
     * <li>return: Map<String,Object>
     * <li>创建人：张凡
     * <li>创建时间：2013-9-2 下午04:27:03
     * <li>修改人：
     * <li>修改内容：
     */
    public Map<String, Object> getComboDataByWorkCardQuery(int start, int limt){
        
        Map<String, Object> map = new HashMap<String, Object>();
        String sql = SqlMapUtil.getSql("jxgc-workcardquery:workFindRunRdpForBaseCombo")
                .replace("人员ID", SystemContext.getOmEmployee().getEmpid().toString());
        String totalSql = "select count(*) from ("+sql+")";
        Page<TrainWorkPlan> page = findPageList(totalSql, sql + " order by 2" ,start,limt,null,null);
        map = page.extjsStore();
        return map;
    }
    
    /**
     * <li>说明：更新作业计划的计划开完工时间、计划工期
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 机车检修作业计划实体对象
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateWorkPlanBeginEndTime(TrainWorkPlan workPlan) throws Exception {
        List<Object[]> list = jobProcessNodeQueryManager.getMinBeginAndMaxEndTimeByNode(workPlan.getIdx());        
        for (Object[] obj : list) {
            if (obj[0] == null || obj[1] == null)
                return;
            Date start = DateUtil.yyyy_MM_dd_HH_mm_ss.parse(obj[0].toString());
            Date end = DateUtil.yyyy_MM_dd_HH_mm_ss.parse(obj[1].toString());
            WorkCalendarInfoUtil wcInfoUtil = WorkCalendarInfoUtil.getInstance(start);
            wcInfoUtil.buildMap();
            long realWorkmunutes = workCalendarDetailManager.getRealWorkminutes(start, end, workPlan.getWorkCalendarIDX());
            Double ratedWorkMinutes = Double.valueOf(realWorkmunutes / (60 * 60 * 1000));
            String ratedWorkDay = String.valueOf(new BigDecimal(ratedWorkMinutes.toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
            workPlan.setRatedWorkDay(Double.valueOf(ratedWorkDay));
            workPlan.setPlanBeginTime(start);
            workPlan.setPlanEndTime(end);
            saveOrUpdate(workPlan);
        }
    }
    
    /**
     * <li>说明：更新机车生产计划明细的状态
     * <li>创建人：程锐
     * <li>创建日期：2015-7-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlan 作业计划实体
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void updatePlanStatus(TrainWorkPlan workPlan) throws BusinessException, NoSuchFieldException{
        //将作业计划单关联的机车生产计划明细状态设为“已兑现”
        if (!StringUtil.isNullOrBlank(workPlan.getEnforcePlanDetailIDX())) 
            trainEnforcePlanDetailManager.updatePlanDetialForRdp(workPlan.getEnforcePlanDetailIDX(),workPlan.getWorkPlanStatus());        
    }
    
    /**
     * <li>说明：获取机车检修作业计划hql
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 机车检修作业计划hql
     */
    private String getTrainWorkPlanHql(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from TrainWorkPlan where 1 = 1 ").append(CommonUtil.buildParamsHql(paramMap)).append(" and recordStatus = 0 order by trainTypeShortName, trainNo ");
        return hql.toString();
    }
    

    /**
     * <li>说明：根据机车检修作业计划状态查询机车检修作业计划
     * <li>创建人：何涛
     * <li>创建日期：2016-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workPlanStatus 机车检修作业计划状态，如果为空则默认查询处理中的机车检修作业计划
     * @return 机车检修作业计划集合
     */
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> queryByWorkPlanStatus(String workPlanStatus) {
        String hql = "From TrainWorkPlan Where recordStatus = 0 And workPlanStatus = ? And idx Not In (Select workPlanID From DailyReport Where recordStatus = 0) Order By processName ASC";
        if (StringUtil.isNullOrBlank(workPlanStatus)) {
            workPlanStatus = TrainWorkPlan.STATUS_HANDLING;
        }
        List<TrainWorkPlan> list = (List<TrainWorkPlan>) this.daoUtils.find(hql, new Object[] { workPlanStatus });
        List<HashMap<String, Object>> children = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> nodeMap = null;
        for (TrainWorkPlan t : list) {
            nodeMap = new HashMap<String, Object>();
            nodeMap.put("id", t.getIdx());         // 节点idx主键
            nodeMap.put("text", t.getProcessName());       // 树节点显示名称
            nodeMap.put("leaf", true);
//            nodeMap.put("checked", false);
            nodeMap.put("trainTypeShortName", t.getTrainTypeShortName());
            nodeMap.put("trainNo", t.getTrainNo());
            nodeMap.put("repairClassName", t.getRepairClassName());
            nodeMap.put("repairtimeName", t.getRepairtimeName());
            nodeMap.put("delegateDName", t.getDelegateDName());
            nodeMap.put("dNAME", t.getDNAME());
            children.add(nodeMap);
        }
        return children;
    }
    
    /**
     * <li>说明：通过车型车号，查询检修计划中最近的一条数据
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 机车检修作业计划hql
     */
    public TrainWorkPlan findCurrentTrainWorkPlanInfo(String trainNo,String trainTypeShortName) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from TrainWorkPlan a where a.recordStatus = 0 ");
        hql.append(" and a.trainNo = '").append(trainNo).append("'");
        hql.append(" and a.trainTypeShortName = '").append(trainTypeShortName).append("'");
        hql.append(" order by a.updateTime desc ");
        
        return this.findSingle(hql.toString());
    }
}
