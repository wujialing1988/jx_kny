
package com.yunda.jx.jxgc.producttaskmanage.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.freight.zb.detain.entity.DetainGztp;
import com.yunda.jx.jxgc.common.JxgcConstants;
import com.yunda.jx.jxgc.producttaskmanage.entity.DetectResult;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardBean;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardHandle;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask;
import com.yunda.jx.jxgc.producttaskmanage.entity.Worker;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCParticipant;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResultVO;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCParticipantManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultManager;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkStepResult;
import com.yunda.jx.jxgc.repairrequirement.manager.WorkStepResultManager;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeManager;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeQueryManager;
import com.yunda.jx.jxgc.workplanmanage.manager.WorkCardQueryManager;
import com.yunda.jx.util.MixedUtils;
import com.yunda.jx.webservice.stationTerminal.base.entity.DataItemBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkTaskBean;
import com.yunda.jxpz.coderule.manager.CodeRuleConfigManager;
import com.yunda.jxpz.utils.SystemConfigUtil;
import com.yunda.util.BeanUtils;

/**
 * FIXME linh(已处理，部分字符串出现3次情况未修改): 代码审查[何涛2016-04-14]：代码提交前，请使用Checkstyle工具检查代码审查项，检查通过后再提交到SVN
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkCard业务类,作业工单
 * <li>创建人：程锐
 * <li>创建日期：2012-12-19
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "workCardManager")
public class WorkCardManager extends JXBaseManager<WorkCard, WorkCard> {
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 派工业务类 */
    @Resource
    private Dispatcher4WorkCardManager dispatcher4WorkCardManager;    
    /** 依赖注入：人员组件实例对象 */
    @Resource
//    private IOmEmployeeManager omEmployeeManager;
    private OmEmployeeManager omEmployeeManager;

    /** 质量检验结果管理类 */
    @Resource
    private QCResultManager qCResultManager;
    /** 节点查询管理类 */
    @Resource
    private JobProcessNodeQueryManager jobProcessNodeQueryManager;
    
    /** 节点管理类 */
    @Resource
    private JobProcessNodeManager jobProcessNodeManager;
    
    /** 作业项对应的处理结果 业务类 */
    @Resource
    private WorkStepResultManager workStepResultManager;

    /** 作业工单查询业务类 */
    @Resource
    private WorkCardQueryManager workCardQueryManager;
    
    /**WorkTask业务类,作业任务*/
    @Resource
    private WorkTaskManager workTaskManager;
    
    /**DetectResult业务类,检测结果*/
    @Resource
    private DetectResultManager detectResultManager;
    
    /**作业工单webservice接口实现类*/
    @Resource
    private QCParticipantManager qcParticipantManager;
    
    /** Worker业务类,作业人员 */
    @Resource
    private WorkerManager workerManager;
    
    /** 自定义编码 */
    @Resource
    private CodeRuleConfigManager codeRuleConfigManager ;
    
    private static final String WAITINGFORGET = "待领取";
    
    private static final String HANDLING = "处理中";
    
    private static final String ZERO = "0";
    
    private static final String DOUBLE_QUOTE_MARK = "','";
    
    //存放返回状态信息
    private static Map<String,String> statusMap = new HashMap<String, String>();
    static {
        statusMap.put(WorkTask.STATUS_WAITINGFORGET, "待领取");
        statusMap.put(WorkTask.STATUS_WAITINGFORHANDLE, "待处理");
        statusMap.put(WorkTask.STATUS_INIT, "初始化");
        statusMap.put(WorkTask.STATUS_HANDLED, "已处理");
        statusMap.put(WorkTask.STATUS_FINISHED, "终止");
    }
    
    /**
     * <li>说明：查询作业工单分页列表（待处理/已处理）
     * <li>创建人：张凡
     * <li>创建日期：2013-1-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件json字符串
     * @param start 开始行
     * @param limit 查询每页结果数量
     * @param orders 排序对象数组
     * @param empid 人员id
     * @return 作业工单分页列表
     */
    @SuppressWarnings("unchecked")
    public Page findWorkCardByWork(String searchJson,int start,int limit,Order[] orders, Long empid){        
        String qSql = SqlMapUtil.getSql("jxgc-gdgl2:findWorkCard_work_select").replaceAll(WAITINGFORGET, WorkTask.STATUS_WAITINGFORGET);                    
        String fromSql = SqlMapUtil.getSql("jxgc-gdgl2:findWorkCard_work_from").replace("人员ID", empid.toString());
        StringBuffer sqlOrder =  new StringBuffer(" order by t.update_Time DESC, WORK_CARD_CODE ASC");
        String totalSql = "select count(1) " + fromSql;
        String sql = qSql+ fromSql + sqlOrder;
        return super.findPageList(totalSql, sql, start , limit, searchJson, orders);
    }
    
    /**
     * <li>说明：查询作业工单分页列表（待处理/已处理）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-6-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param serrch 模糊查询字段
     * @param searchJson 查询条件json字符串
     * @param start 开始行
     * @param limit 查询每页结果数量
     * @param orders 排序对象数组
     * @param empid 人员id
     * @return 作业工单分页列表
     */
    public Page findWorkCardByWorkBySearch(String searchJson,String serrch ,int start,int limit,Order[] orders, Long empid){        
        String qSql = SqlMapUtil.getSql("jxgc-gdgl2:findWorkCard_work_select").replaceAll(WAITINGFORGET, WorkTask.STATUS_WAITINGFORGET);                    
        String fromSql = SqlMapUtil.getSql("jxgc-gdgl2:findWorkCard_work_from").replace("人员ID", empid.toString());
        StringBuffer sqlOrder =  new StringBuffer(" order by t.update_Time DESC, WORK_CARD_CODE ASC");
        // 根据页面传入的值，组装模糊查询条件 by wujl 2016-06-20 JX-489
        if (StringUtils.isNotBlank(serrch)) {
                fromSql = fromSql.concat(" and (r.TRAIN_TYPE_SHORTNAME like '%" +serrch + "%'");
                fromSql = fromSql.concat(" or r.TRAIN_NO like '%" + serrch + "%') ");
        }
        String totalSql = "select count(1) " + fromSql;
        String sql = qSql+ fromSql + sqlOrder;
        return super.findPageList(totalSql, sql, start , limit, searchJson, orders);
    }
    
    /**
     * <li>说明：查询工长派工分页列表
     * <li>创建人：张凡
     * <li>创建日期：2013-3-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件json字符串
     * @param start 开始行
     * @param limit 查询每页结果数量
     * @param orders 排序对象数组
     * @param isForeman 是否已派工（0 未派工）
     * @param queryString 查询条件JSON字符串
     * @return 工长派工分页列表
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public Page findWorkCard(String searchJson,int start,int limit,Order[] orders, String isForeman, String queryString) throws JsonParseException, JsonMappingException, IOException{        
        String qSql = "";        
        String fromSql = "";        
        if (ZERO.equals(isForeman)) {
        	boolean foremanShowInit = "show".equalsIgnoreCase(SystemConfigUtil.getValue("ck.jxgc.producttaskmanage.workcard.foremanShowInit"));//在工长派工页面是否能显示初始化状态的作业工单
        	String addSql = foremanShowInit ? "' or t.status = '" + WorkCard.STATUS_NEW : "";
            qSql = SqlMapUtil.getSql("jxgc-gdgl2:findWorkCard_Foremanselect");            
            fromSql = SqlMapUtil.getSql("jxgc-gdgl2:foremanWorkCard_form") + WorkCard.STATUS_OPEN + "' or t.status = '" + WorkCard.STATUS_HANDLING  + addSql + "')";
        } else {
            qSql = SqlMapUtil.getSql("jxgc-gdgl2:findWorkCard_Foremanselect2");            
            fromSql = SqlMapUtil.getSql("jxgc-gdgl2:foremanWorkCard_form2")
            					.replace("初始化", WorkCard.STATUS_NEW)
            					.replace("已开放", WorkCard.STATUS_OPEN)
            					.replace(HANDLING, WorkCard.STATUS_HANDLING);
        }
        
        Map queryMap = JSONUtil.read(searchJson, Map.class); 
        StringBuilder multyAwhere = new StringBuilder();
        //添加工艺节点多选过滤条件
        if(queryMap.containsKey("nodeCaseIDX")) {
        	String nodeCaseString = String.valueOf(queryMap.get("nodeCaseIDX"));
        	if (!StringUtil.isNullOrBlank(nodeCaseString)) {
                // 增加只勾选父节点时将其所有子节点查出作为过滤条件
                StringBuffer sql = new StringBuffer("select idx from jxgc_job_process_node where record_status = 0 and IS_LEAF = 1 START WITH IDX in ('")
                                                    .append(nodeCaseString.replaceAll(";", DOUBLE_QUOTE_MARK)).append("') CONNECT BY PRIOR IDX = PARENT_IDX");
				multyAwhere.append(" and n.idx in (").append(sql.toString()).append(") ");
			}
        	queryMap.remove("nodeCaseIDX");
        }
        
        if(queryMap.containsKey("workPlanIDX")) {
            String workPlanString = String.valueOf(queryMap.get("workPlanIDX"));
            if (!StringUtil.isNullOrBlank(workPlanString)) {
                multyAwhere.append(" and r.idx = '").append(workPlanString).append("'");
            }
            queryMap.remove("workPlanIDX");
        }
        // 添加工单状态过滤条件
        if (queryMap.containsKey("status")) {
			String statusString = String.valueOf(queryMap.get("status"));
			if (!StringUtil.isNullOrBlank(statusString)) {
				multyAwhere.append(" and t.status in ('").append(statusString.replaceAll(Constants.JOINSTR, DOUBLE_QUOTE_MARK)).append("') ");
			}
			queryMap.remove("status");
		}
        if(queryMap.containsKey("workers")) {
            String workerString = String.valueOf(queryMap.get("workers"));
            if (!StringUtil.isNullOrBlank(workerString)) {
                multyAwhere.append(" and exists (select * from jxgc_worker where work_card_idx  = t.idx and worker_name like '%").append(workerString).append("%')");
            }
            queryMap.remove("workers");
        }
        fromSql = fromSql + multyAwhere.toString();
        searchJson = JSONUtil.write(queryMap);
        
        StringBuffer sqlOrder = new StringBuffer(" order by t.status desc, t.update_Time DESC, WORK_CARD_CODE ASC");
        String totalSql = "select count(distinct t.idx) " + fromSql;
        String sql = qSql + " " + fromSql + sqlOrder;
        Page page = super.findPageList(totalSql, sql, start , limit, searchJson, orders);
        if (ZERO.equals(isForeman)) 
            return page;
        else {        
            List<WorkCard> list = page.getList(); 
            for (WorkCard card : list) {                
                String hql = "from Worker where workCardIDX = ?";                
                List<Worker> workerList = daoUtils.find(hql, new Object[] {card.getIdx()});
                if (workerList == null || workerList.size() < 1)
                    continue;
                StringBuilder workers = new StringBuilder();
                for (Worker worker : workerList) {
                    workers.append(worker.getWorkerName()).append(Constants.JOINSTR);
                }
                workers.deleteCharAt(workers.length() - 1);
                card.setWorkers(workers.toString());
            } 
            return new Page<WorkCard>(page.getTotal(), list);
        }
    }
    
    /**
     * <li>说明：全部完工
     * <li>创建人：张凡
     * <li>创建日期：2013-10-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 人员ID
     * @param orgid 组织机构ID
     * @param result 质检项对象数组
     * @param otherWorkerIDS 其他处理人员的id字符串（,分隔的id字符串）
     * @throws Exception
     */
    public void updateCompleteAllWorkCard(Long empid, Long orgid, QCResultVO[] result, String otherWorkerIDS) throws Exception{
        String workCardIdxSql = SqlMapUtil.getSql("jxgc-gdgl:findWaitReceiveIdx")
                                          .replace("开放", WorkCard.STATUS_OPEN)
                                          .replace(HANDLING, WorkCard.STATUS_HANDLING)
                                          .replace("班组", orgid.toString())
                                          .replace("人员", empid.toString());
        String workCardIDXS = filterWorkCardIDX(workCardIdxSql);
        OmEmployee emp = omEmployeeManager.getModelById(empid);  
        updateWorkTaskToComplate(workCardIDXS); 
        WorkCardHandle workCardEntity = new WorkCardHandle();
        workCardEntity.setRealBeginTime(new Date());
        workCardEntity.setRealEndTime(new Date());
        workCardEntity.setWorkerID(otherWorkerIDS);
        updateWorkToComplate(workCardIDXS, workCardEntity, emp, result);
    }
    
    /**
     * <li>说明：完工
     *     处理逻辑：1 查询出可以完工的工单集合
     *             2 循环可以完工的工单集合，根据过滤条件过滤出最多100条工单，并用,分隔连接成idx字符串
     *             3 修改作业人员和作业任务数据
     *             4 修改工单数据，并对其质量检查处理，如是节点的最后一个工单则过工序。
     * <li>创建人：张凡
     * <li>创建日期：2013-10-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardEntity 作业工单处理实体
     * @param empid 人员ID
     * @param result 质检项对象数组
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void updateCompleteWorkCard (WorkCardHandle workCardEntity, 
    		                            Long empid,
    		                            QCResultVO[] result) throws Exception {
        String workCardIdxSql = "'" + workCardEntity.getIdx().replace(",", "','") + "'";
        String workCardIDXS = filterWorkCardIDX(workCardIdxSql);
        OmEmployee emp = omEmployeeManager.getModelById(empid);  
        updateWorkTaskToComplate(workCardIDXS);   		
        updateWorkToComplate(workCardIDXS, workCardEntity, emp, result);
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
    public boolean canFinishNode(String nodeIDX) {
        int completeWorkCardCount = workCardQueryManager.getCompleteWorkCardCountByNode(nodeIDX);
        int workCardCount = workCardQueryManager.getWorkCardCountByNode(nodeIDX);
        if (completeWorkCardCount == workCardCount)
            return true;
        return false;
    }
    
    /**
     * <li>说明：过滤不能完工的作业工单
     * <li>创建人：张凡
     * <li>创建日期：2013-8-12
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIdxs 以,号分隔的作业工单主键字符串
     * @param empid 人员ID
     * @return 能完工的以,号分隔的作业工单主键字符串
     */
    @SuppressWarnings("unchecked")
    public String filterWorkCard(String workCardIdxs, String empid){
        String sql= SqlMapUtil.getSql("jxgc-gdgl2:filterWorkCard").replace(WAITINGFORGET, WorkTask.STATUS_WAITINGFORGET)
                                                                  .replace("作业工单主键", workCardIdxs.replace(Constants.JOINSTR, DOUBLE_QUOTE_MARK))
                                                                  .replace("人员ID", empid);
        List<Object[]> list = daoUtils.executeSqlQuery(sql);        
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (Object[] o : list) {
            if (finishCheck(o))
                continue;
            i++;
            sb.append(o[0]);
            sb.append(Constants.JOINSTR);
        }
        return i != 0 ? sb.deleteCharAt(sb.length() - 1).toString() : "";
    }
    
    /**
     * <li>说明：查询工单及作业计划信息
     * <li>创建人：张凡
     * <li>创建日期：2014-3-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIdx 作业工单IDX
     * @return 作业工单对象
     */
    public WorkCard getEntityRdpInfo(String workCardIdx){
        WorkCard card = getModelById(workCardIdx);
        TrainWorkPlan workPlan = (TrainWorkPlan)daoUtils.findSingle("from TrainWorkPlan where idx = ?", card.getRdpIDX());
        card.setRepairClassName(workPlan.getRepairClassName());
        card.setTrainTypeTrainNo(MixedUtils.spliceStr(workPlan.getTrainTypeShortName(), workPlan.getTrainNo(), null));
        card.setRepairClassRepairTime(MixedUtils.spliceStr(workPlan.getRepairClassName(), workPlan.getRepairtimeName(), null));
        return card;
    }
    
    /**
     * <li>说明：查询作业工单信息
     * <li>创建人：张凡
     * <li>创建日期：2014-3-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 作业工单IDX
     * @return 作业工单信息
     */
    @SuppressWarnings("unchecked")
    public String[] getInfo(String idx){
        String sql = "select to_char(r.PLAN_BEGIN_TIME, 'yyyy-MM-dd HH24:mi'), to_char(r.PLAN_END_TIME, 'yyyy-MM-dd HH24:mi'),"
                + " a.activity_name, n.NODE_NAME from jxgc_work_card t, JXGC_Work_Plan_Repair_Activity a, jxgc_train_work_plan r,"
                + " jxgc_job_process_node n " + "where t.idx='" + idx
                + "' and r.idx = t.rdp_idx and a.idx = t.repair_activity_idx and t.node_case_idx = n.idx";
        
        List<Object[]> list = daoUtils.executeSqlQuery(sql);
        String[] va = new String[5];
        for (Object[] o : list) {
            va[0] = StringUtil.nvl(o[0]);
            va[1] = StringUtil.nvl(o[1]);
            va[2] = StringUtil.nvl(o[2]);
            va[3] = StringUtil.nvl(o[3]);
        }
        return va;
    }
        
    /**
     * <li>说明：统计待处理的作业工单数量（工位终端）
     * <li>创建人：张凡
     * <li>创建日期：2013-5-31
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param empid 人员ID
     * @param teamId 人员所属组织ID
     * @return 待处理的作业工单数量
     */
    @SuppressWarnings("unchecked")
    public Integer findTaskCount(Long empid, Long teamId){
        try{
            String team = StringUtil.nvl(teamId);
            if(team.equals("")){
                return 0;
            }
            String sql = SqlMapUtil.getSql("jxgc-gdgl:statisticsWorkCardTaskCount")
                                   .replace("WORKER", empid.toString()); 
            List list = daoUtils.executeSqlQuery(sql);
            BigDecimal count = (BigDecimal)list.get(0);
            if (null != count) {
                return count.intValue();
            }
            return 0;
        }catch(Exception ex){
            ExceptionUtil.process(ex, logger);
            return 0;
        }
    }
    
    /**
     * <li>说明：逻辑删除作业工单
     * <li>创建人：张凡
     * <li>创建日期：2013-9-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 以,号分隔的作业工单IDX字符串
     */
    @SuppressWarnings("unchecked")
    public void updateDeleteWork(String ids){
        
        String sql = SqlMapUtil.getSql("jxgc-gdgl3:findCanDeleteWork")
                               .replace("作业工单主键", ids.replace(Constants.JOINSTR, DOUBLE_QUOTE_MARK))
                               .replace(HANDLING, WorkCard.STATUS_HANDLING)
                               .replace("完成", WorkCard.STATUS_HANDLED);
        List<String> list = daoUtils.executeSqlQuery(sql);    //查询可以删除的工单
        
        sql = "update jxgc_work_card t set t.record_status = 1 where t.idx in (";
        MixedUtils.execInSql(sql, ")", list, daoUtils);    //逻辑删除作业工单
        sql = "delete jxgc_worker t where t.work_card_idx in(";
        MixedUtils.execInSql(sql, ")", list, daoUtils);    //物理删除作业人员
        sql = "update jxgc_work_task t set t.record_status = 1 where t.work_card_idx in(";
        MixedUtils.execInSql(sql, ")", list, daoUtils);    //逻辑删除作业任务
    }
    
    /**
     * <li>说明：作业工单完工更新状态
     * <li>创建人：张凡
     * <li>创建日期：2013-9-24
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 作业工单IDX
     */
    public void updateWorkCardStatusToComplete(String idx){
        //更新作业卡状态        
        String hql = "update WorkCard t set status=?, updator = ?, updateTime = ? where idx = ? " +
        		     "and recordStatus='0'";
        daoUtils.execute(hql, WorkCard.STATUS_HANDLED, SystemContext.getAcOperator().getOperatorid(), new Date(), idx);
    }
    
    /**
     * <li>方法说明：获取派工业务类 
     * <li>方法名称：getDispatcher
     * <li>@return
     * <li>return: Dispatcher4WorkCardManager
     * <li>创建人：张凡
     * <li>创建时间：2014-3-27 下午03:18:03
     * <li>修改人：
     * <li>修改内容：
     */
    public Dispatcher4WorkCardManager getDispatcher(){        
        return this.dispatcher4WorkCardManager;
    }   
    
    /**
     * <li>说明：新增编辑自定义工单
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIDX 作业计划id
     * @param nodeCaseIDX 流程节点id
     * @param gztps 扣车故障列表
     * @throws Exception
     */
    public void saveDefineWorkByDetain(String rdpIDX , String nodeCaseIDX,List<DetainGztp> gztps) throws Exception{
    	WorkCard[] workCards = new WorkCard[gztps.size()];
    	int i = 0 ;
    	for (DetainGztp gztp : gztps) {
    		String workCardCode = codeRuleConfigManager.makeConfigRule("xcode");
    		WorkCard card = new WorkCard();
    		card.setRdpIDX(rdpIDX);
    		card.setNodeCaseIDX(nodeCaseIDX);
    		card.setWorkCardCode(workCardCode);
    		card.setWorkCardName(gztp.getGztpName());
    		card.setWorkScope(gztp.getGztpDesc());
    		card.setWorkSeqClass("99");
    		workCards[i] = card ;
    		i++;
		}
    	editDefineWork(workCards, null);
    }
    
    /**
     * <li>说明：新增编辑自定义工单
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCards 作业工单实体数组
     * @param qCResults 质量检查结果实体数组
     * @throws Exception
     */
    public void editDefineWork(WorkCard[] workCards, QCResult[] qCResults) throws Exception {
        if (workCards == null || workCards.length < 1)
            return;
        JobProcessNode node = jobProcessNodeQueryManager.getModelById(workCards[0].getNodeCaseIDX());
        if (JobProcessNode.STATUS_COMPLETE.equals(node.getStatus()))
            throw new BusinessException("节点已完成，不能再新增自定义工单，请刷新页面");
        if (JobProcessNode.STATUS_STOP.equals(node.getStatus()))
            throw new BusinessException("节点已终止，不能再新增自定义工单，请刷新页面");
        List<WorkCard> workCardList = new ArrayList<WorkCard>();
        for (WorkCard workCard : workCards) {
            if (StringUtil.isNullOrBlank(workCard.getIdx())) {
                String sql = SqlMapUtil.getSql("jxgc-processNode:getNodeAndWorkStation").replace("节点主键", workCard.getNodeCaseIDX());
                Object[] o = (Object[]) daoUtils.executeSqlQuery(sql).iterator().next();
                workCard.setWorkStationIDX(StringUtil.nvl(o[1]));
                workCard.setWorkStationName(StringUtil.nvl(o[2]));
                workCard.setWorkStationBelongTeam(MixedUtils.string2Long(o[3]));
                workCard.setWorkStationBelongTeamName(StringUtil.nvl(o[4]));
                if (JobProcessNode.STATUS_GOING.equals(node.getStatus())) {
                    workCard.setStatus(WorkCard.STATUS_HANDLING);
                    workCard.setRealBeginTime(new Date());
//                    workCard.setSystemBeginTime(new Date());
                } else 
                    workCard.setStatus(WorkCard.STATUS_NEW);
                workCard.setExtensionClass(WorkCard.EXT_CLASS_DEFINE);
                saveOrUpdate(workCard);
                workCardList.add(workCard);
            } else {
                WorkCard oldWorkCard = getModelById(workCard.getIdx());
                boolean isSameDispatch = getDispatcher().isSameDispatch(oldWorkCard.getWorkStationIDX(), node.getWorkStationIDX(), oldWorkCard.getWorkStationBelongTeam(), node.getWorkStationBelongTeam());
                if (!isSameDispatch)
                    workCardList.add(workCard);
                saveOrUpdate(workCard);
            }
            
            if (qCResults != null && qCResults.length > 0) {
                qCResultManager.saveQCData(qCResults, workCard.getIdx(), workCard.getIdx()); // 新增质量检查
                daoUtils.flush();
                updateWorkCardQC(workCard.getIdx());
            }   
        }
        if (workCardList == null || workCardList.size() < 1)
            return;
        getDispatcher().updateWorkCardDispatchByCard(node, 
                                                     workCardQueryManager.buildIDXBuilder(workCardList),
                                                     workCardQueryManager.buildSqlIDXStr(workCardList), 
                                                     workCardQueryManager.buildIDXArray(workCardList));
    }
    
    /**
     * <li>说明：更新作业工单所属流程节点
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 作业工单IDX数组
     * @param nodeIDX 移至流程节点IDX
     * @param oldNodeIDX 原流程节点IDX
     * @return 错误信息
     * @throws Exception
     */
    public String changeNode(String[] ids, String nodeIDX, String oldNodeIDX) throws Exception {
        String idxs = StringUtil.join(ids, Constants.JOINSTR);
        String workCardIDXSStr = CommonUtil.buildInSqlStr(idxs);
        JobProcessNode newNode = jobProcessNodeQueryManager.getModelById(nodeIDX);
        JobProcessNode oldNode = jobProcessNodeQueryManager.getModelById(oldNodeIDX);
        String status = WorkCard.STATUS_NEW;
        String realBeginTime = "null";
        if (!StringUtil.isNullOrBlank(newNode.getStatus()) && JobProcessNode.STATUS_GOING.equals(newNode.getStatus())) {
            status = WorkCard.STATUS_HANDLING;
            realBeginTime = "TO_DATE('" + DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss) + "', 'yyyy-mm-dd hh24:mi:ss')";
        }
        String sql = SqlMapUtil.getSql("jxgc-workcard:updateForChangeNode")
                               .replace("#nodeIDX#", nodeIDX)
                               .replace("#status#", status)
                               .replace("#realBeginTime#", realBeginTime)
                               .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
                               .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
                               .replace(JxgcConstants.IDXS, workCardIDXSStr);
        daoUtils.executeSql(sql);
                
        if (StringUtil.isNullOrBlank(newNode.getWorkStationIDX()) || StringUtil.isNullOrBlank(newNode.getWorkStationName())) {
            logger.info("节点无工位，不能对作业工单派工");
            return null;
        }
        boolean isSameDispatch = getDispatcher().isSameDispatch(oldNode, newNode);
        if (isSameDispatch) {
            logger.info("移动前后节点的派工信息一致，不用再对作业工单派工");
            return null;
        }
        StringBuilder idsBuilder = new StringBuilder(idxs);
        getDispatcher().updateWorkCardDispatchByCard(newNode, idsBuilder, workCardIDXSStr, ids);        
        return null;
    }
    
    /**
     * <li>说明：完成作业工单数据修改
     *     处理逻辑：1 查询出可以完工的工单集合
     *             2 循环可以完工的工单集合：如有质量检查的必检：将工单状态改为【质检中】;如是节点的最后一个工单则完成工序节点。
     * <li>创建人：张凡
     * <li>创建日期：2013-10-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDXS 作业工单IDX
     * @param workCardEntity 作业工单处理实体
     * @param emp 人员对象
     * @param result 质检项对象数组
     * @throws Exception
     */    
    @SuppressWarnings("unchecked")
    private void updateWorkToComplate(String workCardIDXS,        
                                      WorkCardHandle workCardEntity,
                                      OmEmployee emp,
                                      QCResultVO[] result) throws Exception {        
        String[] workers = getWorkerInfo(emp, workCardEntity);
        String workerIDS = workers[0];
        String workerNames = workers[1];
        String[] idxArray = StringUtil.tokenizer(workCardIDXS.replace("'", ""), Constants.JOINSTR);
        
        for (String cardIdx : idxArray) {
            WorkCard workCard = getModelById(cardIdx);
            if (!WorkCard.STATUS_HANDLING.equals(workCard.getStatus()))
                continue;
            workCard = buildEntityForHandle(workCard, workCardEntity, workerIDS, workerNames);
            
            if (qCResultManager.checkHasQCFlow(cardIdx)) {          
                qCResultManager.updateOpenQCResultByWorkCardIDX(cardIdx, result, emp.getEmpid());                
                if (qCResultManager.checkHasBJQC(cardIdx)) // 有必检的情况：更新作业卡，但不将状态更新成完成，更新为质量检查处理中
                    workCard.setStatus(WorkCard.STATUS_FINISHED);
            }
            saveOrUpdate(workCard);            
            deleteWorkersByWorkCard(cardIdx);         
            if (canFinishNode(workCard.getNodeCaseIDX())) {
//              NodeRunner.runner(workCard.getNodeCaseIDX());
            JobProcessNode nodeCase = jobProcessNodeQueryManager.getModelById(workCard.getNodeCaseIDX());
              if (nodeCase != null) {
                  jobProcessNodeManager.updateFinishNodeCase(nodeCase);
              }
          }

        }
    }
    
    /**
     * <li>方法说明：作业工单完成，作业任务数据修改 
     * <li>方法名称：updateWorkerToComplate
     * <li>@param idx
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-10-30 上午09:51:03
     * <li>修改人：
     * <li>修改内容：
     */
    private void updateWorkTaskToComplate(String idx) { 
        String sql = SqlMapUtil.getSql("jxgc-gdgl:updateWorkTaskComplete")
                               .replace("已处理", WorkTask.STATUS_HANDLED)
                               .replace("工单主键", idx)
                               .replace(WAITINGFORGET, WorkTask.STATUS_WAITINGFORGET);
        this.updateSql(sql);
    }
    
    /**
     * <li>说明：更新作业工单的质量检查项字段
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单IDX
     */
    private void updateWorkCardQC(String workCardIDX) {
        StringBuilder sql = new StringBuilder("UPDATE JXGC_WORK_CARD")
                                       .append(" SET  QC_NAME = (SELECT TO_CHAR(WMSYS.WM_CONCAT(CHECK_ITEM_NAME))")
                                       .append(" FROM JXGC_QC_RESULT")
                                       .append(" WHERE WORK_CARD_IDX = '")
                                       .append(workCardIDX).append("' AND RECORD_STATUS = 0)")
                                       .append(" WHERE IDX = '").append(workCardIDX).append("' AND RECORD_STATUS = 0");
        daoUtils.executeSql(sql.toString());
    }
    
    /**
     * <li>说明：检查工单是否可以完工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param o 对象数组
     * @return true 不能完工， false 能完工
     */
    private boolean finishCheck(Object[] o) {
        String[] checkResult = o[1].toString().split("\\|");
        
        if(!checkResult[0].equals(ZERO)){    //当[0]不等于“0”， 即有未完成的检测/修项目
            if(!checkResult[2].equals(checkResult[0])){ //当[2]即有默认结果 , 不等于[0]，即未完成的检测/修项目数量
                return true;
            }else if(!checkResult[1].equals(ZERO)){  //[1]不等于0，即有未完成的有录入数据项的检测/修项目
                return true;
            }
        }
        return false;
    }
    
    /**
     * <li>说明：1 查询出可以完工的工单集合
     *          2 循环可以完工的工单集合，根据过滤条件过滤出最多100条工单，并用,分隔连接成idx字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-9-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIdxSql 作业工单主键sql字符串
     * @return 以,号分隔的作业工单主键字符串
     */
   
    private String filterWorkCardIDX(String workCardIdxSql) {
        String sql = SqlMapUtil.getSql("jxgc-gdgl3:filterWork").replace("工单主键", workCardIdxSql).replace(WAITINGFORGET, WorkTask.STATUS_WAITINGFORGET);
        List<Object[]> list = daoUtils.executeSqlQuery(sql);
        //记录可以完工的工单主键 
        StringBuffer workCardIDXS = new StringBuffer();
        int unComTaskCount; //未完成的检测/修项目数量
        int unComTaskAndDetectCount; //未完成的检测/修项目有需要录入检测项数据的数量
        int hasDefaultTaskCount; //有默认结果的检测/修项目数量
        int completeCount = 0; //可以完成的数量
        for (Object[] obj : list) {
            unComTaskCount = Integer.parseInt(obj[1].toString());
            unComTaskAndDetectCount = Integer.parseInt(obj[2].toString());
            hasDefaultTaskCount = Integer.parseInt(obj[3].toString());
            if (unComTaskCount != 0) { // 当unComTaskCount不等于0， 即有未完成的检测/修项目
                // 当hasDefaultTaskCount（有默认结果）不等于unComTaskCount（未完成的检测/修项目数量）或 unComTaskAndDetectCount(需录入检测项)不等于0，即有未完成的有录入数据项的检测/修项目
                if ((hasDefaultTaskCount != unComTaskCount) || unComTaskAndDetectCount != 0) 
                    continue;
            }
            workCardIDXS.append("'").append(obj[0]).append("',");
            completeCount ++;// 记录可以完成的数量
            if (completeCount == 100)  // 最多执行一百条，多余的不执行
                break;
        }
        logger.info("【完成工单的数量：】" + completeCount);
        if (workCardIDXS.length() == 0) 
            throw new BusinessException("工单无法完工");
        else 
            workCardIDXS.deleteCharAt(workCardIDXS.length() - 1);
        return workCardIDXS.toString();
    }
    
    /**
     * <li>说明：组装处理人员ID和作业人员名称
     * <li>创建人：程锐
     * <li>创建日期：2015-9-22
     * <li>修改人：何涛
     * <li>修改日期：2016-04-13
     * <li>修改内容：代码重构
     * @param emp 当前处理人员对象
     * @param workCardEntity 作业工单处理实体
     * @return 处理人员ID和作业人员名称数组
     */
    private String[] getWorkerInfo(OmEmployee emp, WorkCardHandle workCardEntity) {
        StringBuilder workerIDS = new StringBuilder(100);
        StringBuilder workerNames = new StringBuilder(100);
        
        workerIDS.append(emp.getEmpid());               // 添加当前系统操作人员
        workerNames.append(emp.getEmpname());
        
        String workerID = workCardEntity.getWorkerID();
        if (!StringUtil.isNullOrBlank(workerID)) {       // 页面选择的其他作业处理人员
            String[] otherWorkerIDArray = workerID.split(Constants.JOINSTR);
            for (String empid : otherWorkerIDArray) {
                if (StringUtil.isNullOrBlank(empid)) {
                    continue;
                }
                OmEmployee otherEmp = omEmployeeManager.getModelById(Long.valueOf(empid));
                if (null != otherEmp) {
                    workerIDS.append(Constants.JOINSTR).append(empid);
                    workerNames.append(Constants.JOINSTR).append(otherEmp.getEmpname());
                }
            }
        }
        return new String[]{workerIDS.toString(), workerNames.toString()};
    }
    
    /**
     * <li>说明：构造作业工单处理实体
     * <li>创建人：程锐
     * <li>创建日期：2015-9-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 作业工单实体
     * @param workCardEntity 作业工单处理实体
     * @param workerIDS 处理人员IDX字符串
     * @param workerNames 处理人员名称字符串
     * @return 作业工单处理实体
     */
    private WorkCard buildEntityForHandle(WorkCard workCard, WorkCardHandle workCardEntity, String workerIDS, String workerNames) {
//        workCard.setSystemEndTime(new Date());
        workCard.setStatus(WorkCard.STATUS_HANDLED);
        if (workCardEntity.getRealBeginTime() != null)
            workCard.setRealBeginTime(workCardEntity.getRealBeginTime());
        workCard.setRealEndTime(workCardEntity.getRealEndTime());
        workCard.setWorkerID(workerIDS);
        workCard.setWorker(workerNames);
        workCard.setUpdateTime(new Date());
        if (!StringUtil.isNullOrBlank(workCardEntity.getRemarks()))
            workCard.setRemarks(workCardEntity.getRemarks());
//        if (!StringUtil.isNullOrBlank(workCardEntity.getPartsNo()))
//            workCard.setPartsNo(workCardEntity.getPartsNo());
        return workCard;
    }
    
    /**
     * <li>说明：删除非处理人员
     * <li>创建人：程锐
     * <li>创建日期：2015-9-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param cardIdx 作业工单IDX
     * @throws Exception
     */
    private void deleteWorkersByWorkCard(String cardIdx) throws Exception {
        String sql = "DELETE JXGC_WORKER T WHERE T.WORK_CARD_IDX = '" + cardIdx + "'";        
        updateSql(sql);
    }
    
    /**
     * FIXME linh(已处理): 代码审查[何涛2016-04-08]：较差的方法逻辑实现，例如：存在较多硬编码，处理逻辑不明朗等，请认真梳理业务逻辑进行重构
     * <li>说明：作业工单显示检测检修项目List列表
     * <li>创建人：林欢
     * <li>创建日期：2016-4-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员主键
     * @param workCardIdx 作业工单主键
     * @param workTaskIdx 作业任务主键
     * @param start 查询开始页
     * @param limit 查询每页结果数量
     * @return 检测检修项目List json对象
     * Json数据模拟：
       [{
            idx:’FB2F86C0C566487C8A5D65C89EE6D253’,
            workTaskName:尺寸检查2,
            repairStandard:检查防跳作用并测量前端防跳间隙；测量前端车钩中心距轨面的高度；测量前端车钩开度；测量钩尾间隙；检查尾框厚度尺寸,
            workStepIDX:402886814d8c3bbe014d8f7cbe8f0bdd,
            repairResult:null,
            remarks:null,
            repairContent:null,
            repairMethod:null,
            status:’INITIALIZE’,
            workTaskCode:’ZYX-000583’，
            dataItemList:[{
                detectItemContent:’eee’,
                isNotBlank:’0’,
                detectResulttype:”sd”,
                detectResult:”22”,
                Idx:”8a8283a84f4e3c1b014f4e418d090003”,
                detectItemStandard:”xcvxcvcvxvxc”,
                minResult:”null”,
                maxResult:”null”
            }]
        }]
     */
    @SuppressWarnings("unchecked")
    public Page getWorkTaskAndDetectResultList(Long operatorid, String workCardIdx,String workTaskIdx, int start, int limit) {
        start--;
        try {
//            
            // 查询条件json
//            String searchJson = "{\"workCardIDX\":\"" + workCardIdx + "\"";
//            
//            searchJson += ",\"status\":\"#[t]#$[IN]$" + WorkTask.STATUS_WAITINGFORGET + "]|[" + WorkTask.STATUS_INIT + "]|[" + WorkTask.STATUS_HANDLED + "\"}";
//            
//            WorkTaskManager m = wor getWorkTaskManager();
//            Page<WorkTask> page = workTaskManager.findWorkTask(searchJson, start * limit , limit, null);
            
            String[] statusArray = new String[]{WorkTask.STATUS_WAITINGFORGET,WorkTask.STATUS_INIT,WorkTask.STATUS_HANDLED};

            Order[] order= new Order[1];
            order[0] = Order.asc("workTaskSeq");
            Page<WorkTask> page = workTaskManager.pageQuery(workCardIdx, statusArray, start * limit, limit, order);
            
            /** 返回结果包装：将查询对象page中的集合数据用指定的WorkTaskBean对象进行封装 */
            List<WorkTaskBean> workTaskList = new ArrayList<WorkTaskBean>();
            workTaskList = BeanUtils.copyListToList(WorkTaskBean.class, page.getList());
            
            String isDefailtResult = null;
            
            for (WorkTaskBean bean : workTaskList) {
                
                bean.setStatus(statusMap.get(bean.getStatus()));
                
                workTaskIdx = bean.getIdx();
                
//              通过workTaskIdx获取配置的结果项
                List<WorkStepResult> eosList = workStepResultManager.getModelsByWorkTaskIDX(workTaskIdx);
                List<String> resultList = new ArrayList<String>(eosList.size());
                for (WorkStepResult entry : eosList) {
                    if (entry.getIsDefault() == 1) {
                        isDefailtResult = entry.getResultName();
                    }
                    resultList.add(entry.getResultName());
                }
                
                if (StringUtil.isNullOrBlank(bean.getRepairResult())) {
                    bean.setRepairResult(isDefailtResult);
                }
                
//              获取检测检修列表
                if (workTaskIdx == null) {
                    workTaskIdx = "-1";
                }
                DetectResult entity = new DetectResult();
                entity.setWorkTaskIDX(workTaskIdx);
                entity.setRecordStatus(0);
                SearchEntity<DetectResult> searchEntity = new SearchEntity<DetectResult>(entity, 0, 999999999, null);
                Page<DetectResult> pageList = detectResultManager.findPageList(searchEntity);
                
                List<DataItemBean> dataItemList = new ArrayList<DataItemBean>();
                if (pageList.getList() != null && pageList.getList().size() > 0) {
                    dataItemList = BeanUtils.copyListToList(DataItemBean.class, pageList.getList());
                }
                bean.setDataItemList(dataItemList);
                bean.setResultList(resultList);
                
            }
            
            Page packList = new Page<WorkTaskBean>(page.getTotal(), workTaskList); // 构造Page对象
            
            return packList;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            throw new BusinessException(e.getMessage());
        }
    }
    
    
    /**
     * <li>说明：保存作业工单
     * <li>创建人：何涛
     * <li>创建日期：2016-4-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 作业工单处理实体
     * @param workCard 作业工单实体
     * @throws NoSuchFieldException
     */
    private void saveWorkCard(WorkCardHandle entity, WorkCard workCard) throws NoSuchFieldException {
        if (null == workCard.getRealBeginTime()) {
            workCard.setRealBeginTime(entity.getRealBeginTime());       // 实际开工时间
        }
        workCard.setRealEndTime(entity.getRealEndTime() == null ? new Date() : entity.getRealEndTime());               // 实际完工时间
//        workCard.setPartsNo(entity.getPartsNo());                       // 配件编号
        workCard.setRemarks(entity.getRemarks());                       // 备注
        String[] workerInfo = this.getWorkerInfo(SystemContext.getOmEmployee(), entity);
        workCard.setWorkerID(workerInfo[0]);                            // 以逗号分隔的作业人员ID
        workCard.setWorker(workerInfo[1]);                              // 以逗号分隔的作业人员姓名
        this.saveOrUpdate(workCard);
    }
    
    /**
     * <li>说明：提交工单
     * <li>创建人：何涛
     * <li>创建日期：2016-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 作业工单处理实体
     * @param result 质量检验结果值对象
     * @param workTaskAndDetects 封装作业任务及检测结果的数据对象
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void finishWorkCard(WorkCardHandle entity, QCResultVO[] result, WorkTaskBean[] workTaskAndDetects) throws BusinessException, NoSuchFieldException {
        // 验证实际开始时间不能为空
        Date realBeginTime = entity.getRealBeginTime();
        if (null == realBeginTime) {
            throw new NullPointerException("工单处理实际开始时间为空！");
        }
        // 验证实际结束时间不能为空
        Date realEndTime = entity.getRealEndTime();
        if (null == realEndTime) {
            throw new NullPointerException("工单处理实际结束时间为空！");
        }
        
        OmEmployee omEmployee = SystemContext.getOmEmployee();
        
        // 兼容老版本未同时提交检修结果数据的处理
        if (null == workTaskAndDetects || 0 >= workTaskAndDetects.length) {
            try {
                this.updateCompleteWorkCard(entity, omEmployee.getEmpid(), result);
                return;
            } catch (Exception e) {
                throw new BusinessException(e);
            }
        }
        
        // 作业工单和检修检测项同时提交的数据处理
        // 保存作业任务
        this.workTaskManager.saveWorkTaskBean(workTaskAndDetects, true);
        
        // 作业工单信息
        String workCardIDX = entity.getIdx();           // 作业工单主键
        WorkCard workCard = this.getModelById(workCardIDX);
        
        // 有必检的情况：更新作业卡，但不将状态更新成完成，更新为质量检查处理中
        if (this.qCResultManager.checkHasBJQC(workCardIDX)) {
            workCard.setStatus(WorkCard.STATUS_FINISHED);               // 质量检查处理中
        } else {
            workCard.setStatus(WorkCard.STATUS_HANDLED);                // 已处理
        }
//        workCard.setSystemEndTime(Calendar.getInstance().getTime());    // 系统完工时间
        // 保存作业工单
        this.saveWorkCard(entity, workCard); 
        
        // 保存质量检查相关信息，开放质量检查，包含对质量检查可参与人员的存储
        this.qCResultManager.saveAndOpenQCResult(workCard, result);
        
        // 删除冗余的派工人员信息
        this.workerManager.deleteWorkersByWorkCard(workCard.getIdx());         
        
        if (canFinishNode(workCard.getNodeCaseIDX())) {
//              NodeRunner.runner(workCard.getNodeCaseIDX());
            JobProcessNode nodeCase = jobProcessNodeQueryManager.getModelById(workCard.getNodeCaseIDX());
            if (nodeCase != null) {
                try {
                    jobProcessNodeManager.updateFinishNodeCase(nodeCase);
                } catch (Exception e) {
                    throw new BusinessException(e);
                }
            }
        }
    }

    /**
     * <li>说明：保存工单（暂存）
     * <li>创建人：何涛
     * <li>创建日期：2016-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 作业工单处理实体
     * @param result 质量检验结果值对象
     * @param workTaskAndDetects 封装作业任务及检测结果的数据对象
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveWorkCard(WorkCardHandle entity, QCResultVO[] result, WorkTaskBean[] workTaskAndDetects) throws BusinessException, NoSuchFieldException {
        // 保存作业工单相关数据
        WorkCard workCard = this.getModelById(entity.getIdx());
        this.saveWorkCard(entity, workCard);                                
        
        // 保存质量检查参与人员信息
        this.qcParticipantManager.saveIsAssignParticiant(workCard, result);
        
        // 保存作业任务及检修检测结果
        this.workTaskManager.saveWorkTaskBean(workTaskAndDetects, false);
    }

    /**
     * FIXME(已处理) 代码评审点
     * <li>说明：更具作业工单IDX，查询QC_RESULT和jxgc_qc_participant的质检人员处理情况
     * <li>创建人：林欢
     * <li>创建日期：2016-4-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单IDX
     * @return List<Map<String, Object>> 结果数据
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findQCNameByWorkCardIDX(String workCardIDX){
        
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> objList = new ArrayList<Map<String,Object>>();

        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" (case ");
        sb.append(" when t1.qcName = '' then ");
        sb.append(" '' ");
        sb.append(" when t1.qcName is null then ");
        sb.append(" '' ");
        sb.append(" else ");
        sb.append(" (case ");
        sb.append(" when qr.qc_empname = '' then ");
        sb.append(" t1.qcName ");
        sb.append(" when qr.qc_empname is null then ");
        sb.append(" t1.qcName ");
        sb.append(" else ");
        sb.append(" qr.qc_empname ");
        sb.append(" end) end) as qcEmpName, ");
        sb.append(" (case ");
        sb.append(" when t1.qcName = '' then ");
        sb.append(" '' ");
        sb.append(" when t1.qcName is null then ");
        sb.append(" '' ");
        sb.append(" else ");
        sb.append(" (case ");
        sb.append(" when qr.qc_empname = '' then ");
        sb.append(" (select a.check_item_name ");
        sb.append(" from jxgc_qc_result a ");
        sb.append(" where a.check_item_code = t1.check_item_code) || '(未处理)' ");
        sb.append(" when qr.qc_empname is null then ");
        sb.append(" (select a.check_item_name ");
        sb.append(" from jxgc_qc_result a ");
        sb.append(" where a.check_item_code = t1.check_item_code) || '(未处理)' ");
        sb.append(" else ");
        sb.append(" qr.check_item_name || '(已处理)' ");
        sb.append(" end) end) as checkItemCode ");
        
        sb.append(" from jxgc_qc_result qr ");
        sb.append(" left join ");
        sb.append(" (select qp.check_item_code, ");
        sb.append(" wm_concat(qp.check_person_name) qcName, ");
        sb.append(" qp.work_card_idx ");
        sb.append(" from jxgc_qc_participant qp ");
        sb.append(" where qp.work_card_idx = '").append(workCardIDX).append("' ");
        sb.append(" group by qp.check_item_code, qp.work_card_idx) t1 on qr.work_card_idx = ");
        sb.append(" t1.work_card_idx ");
        sb.append(" and t1.check_item_code = ");
        sb.append(" qr.check_item_code ");
        sb.append(" where qr.work_card_idx = '").append(workCardIDX).append("' ");
        
        list = this.getSQLQuery(sb.toString());
        Object[] objArry = list.toArray();
        for (int i = 0; i < objArry.length; i++) {
            Object[] arr = (Object[]) objArry[i];
            
            Map<String, Object> map = new HashMap<String, Object>();
            String qcEmpName = String.valueOf(arr[0]);
            String checkItemCode = String.valueOf(arr[1]);
            
            if (StringUtils.isNotBlank(qcEmpName) && !"null".equals(qcEmpName)) {
                map.put("qcEmpName", String.valueOf(arr[0]));
            }
            if (StringUtils.isNotBlank(checkItemCode) && !"null".equals(qcEmpName)) {
                map.put("checkItemCode", String.valueOf(arr[1]));
            }
            
            if (map.get("qcEmpName") != null || map.get("checkItemCode") != null) {
                objList.add(map);
            }
        }
        
//        //封装checkItemCode 对应的checkItemName
//        Map<String, String> checkItemMap = new HashMap<String, String>();
//        List<QCResult> qcList = qCResultManager.getAll();
//        for (QCResult result : qcList) {
//            checkItemMap.put(result.getCheckItemCode(), result.getCheckItemName());
//        }
//            
//        //通过工单IDQCresult对象
//        List<QCResult> qcResultList = qCResultManager.getModelList(workCardIDX);
//        
//        if (qcResultList != null && qcResultList.size() > 0) {
////          封装单条对象
//            //装备拼装质检人的名称，用逗号分隔
//            for (int i = 0; i < qcResultList.size(); i++) {
//                Map<String, Object> map = new HashMap<String, Object>();
//                QCResult qc = qcResultList.get(i);
//                if (qc.getStatus() == 2 || qc.getStatus() == 3 ) {
//                    map.put("checkItemCode", checkItemMap.get(qc.getCheckItemCode()) + "(已处理)");
//                    map.put("qcEmpName", qc.getQcEmpName());
//                    list.add(map);
//                }
//            }
//        }
//            
//        //通过工单查询QCParticipant质检项目
//        List<String> checkItemCodeQCParticipant = qcParticipantManager.getCheckItemCodeGroupBy(workCardIDX);
//        
//        if (checkItemCodeQCParticipant != null && checkItemCodeQCParticipant.size() > 0) {
//            for (String checkItemCode : checkItemCodeQCParticipant) {
//                
//                //获取QCParticipant对象，过滤当前工单下的QCParticipant中有的checkItemCode不在QCresult出现过
//                List<QCParticipant> qcParticipantList = qcParticipantManager.getModelListNotInQCResultByWorkCardIDX(workCardIDX);
//                
//                if (qcParticipantList != null && qcParticipantList.size() > 0) {
//                    //装备拼装质检人的名称，用逗号分隔
//                    StringBuffer resultSb = new StringBuffer("");
////                  封装单条独享
//                    Map<String, Object> resutlMapObj = new HashMap<String, Object>();
//                    for (int i = 0; i < qcParticipantList.size(); i++) {
//                        if (checkItemCode.equals(qcParticipantList.get(i).getCheckItemCode())) {
//                            String name = qcParticipantList.get(i).getCheckPersonName() == null ? "" : qcParticipantList.get(i).getCheckPersonName();
//                            
//                            if (i == qcParticipantList.size()) {
//                                resultSb.append(name);
//                            }else {
//                                resultSb.append(name).append("，");
//                            }
//                        }
//                    }
//                    
//                    //通过checkItemCode获取对应的名称
//                    resutlMapObj.put("checkItemCode", checkItemMap.get(checkItemCode) + "(未处理)");
//                    resutlMapObj.put("qcEmpName", resultSb.toString());
//                    list.add(resutlMapObj);
//                }
//            }
//        }
        
        return objList;
    }

    /**
     * <li>说明：批量完工（iPad应用），该方法不能将不能完工的工单提示页面
     * <li>创建人：何涛
     * <li>创建日期：2016-4-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 要执行批量完工的机车检修作业工单主键idx数组
     * @param entity 作业工单处理实体
     * @param result 质量检验结果值对象
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void finishBatchWorkCards(String[] ids, WorkCard entity, QCResultVO[] result) throws BusinessException, NoSuchFieldException {
        for (String idx : ids) {
            WorkCard workCard = this.getModelById(idx);
            // 验证机车检修作业工单是否能够完工
            if (null == workCard || !canFinished(workCard)) {
                continue;
            }
            // 完工
            this.finishWorkCard(workCard, entity, result);
        }
    }
    
    /**
     * <li>说明：批量完工（iPad应用），单条作业工单完工
     * <li>创建人：何涛
     * <li>创建日期：2016-4-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 要完工的机车检修作业工单实体对象
     * @param entity 封装页面端保存信息的实体对象 {
     *      partsNo: "8888",
     *      remarks: "批量完工",
     *      worker: "王谦,张锦,姚增荣",             // 以逗号分割的作业人员名称字符串,不包含当前系统操作人员
     *      workerID: "109,118,110",     
     * }
     * @param result 质量检验结果值对象数组
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    private void finishWorkCard(WorkCard workCard, WorkCard entity, QCResultVO[] result) throws BusinessException, NoSuchFieldException {
        // 作业工单和检修检测项同时提交的数据处理
        // 保存作业任务
        List<WorkTask> workTasks = this.workTaskManager.getListByWorkCard(workCard.getIdx());
        if (null != workTasks && !workTasks.isEmpty()) {
            for (WorkTask workTask : workTasks) {
                // 完成机车检修作业任务
                this.workTaskManager.finishWorkTask(workTask);
            }
        }
        
        // 作业工单信息
        String workCardIDX = workCard.getIdx();           // 作业工单主键
        
        // 有必检的情况：更新作业卡，但不将状态更新成完成，更新为质量检查处理中
        if (this.qCResultManager.checkHasBJQC(workCardIDX)) {
            workCard.setStatus(WorkCard.STATUS_FINISHED);               // 质量检查处理中
        } else {
            workCard.setStatus(WorkCard.STATUS_HANDLED);                // 已处理
        }
//        workCard.setSystemEndTime(Calendar.getInstance().getTime());    // 系统完工时间

        workCard.setRealBeginTime(Calendar.getInstance().getTime());
        workCard.setRealEndTime(Calendar.getInstance().getTime());
        
//        workCard.setPartsNo(entity.getPartsNo());                       // 配件编号
        workCard.setRemarks(entity.getRemarks());                       // 备注
        
        String worker = SystemContext.getOmEmployee().getEmpname();     // 作业人员
        if (!StringUtil.isNullOrBlank(entity.getWorker())) {
            worker = worker + Constants.JOINSTR + entity.getWorker().replace(";", Constants.JOINSTR);
        }
        workCard.setWorker(worker);                                     // 作业人员
        String workerID = SystemContext.getOmEmployee().getEmpid().toString();  
        if (!StringUtil.isNullOrBlank(entity.getWorkerID())) {
            workerID = workerID + Constants.JOINSTR + entity.getWorkerID().replace(";", Constants.JOINSTR);
        }
        workCard.setWorkerID(workerID);                                  // 作业人员id
        
        // 保存作业工单
        this.saveOrUpdate(workCard);
        
        // 保存质量检查相关信息，开放质量检查，包含对质量检查可参与人员的存储
        this.qCResultManager.saveAndOpenQCResult(workCard, result);
        
        // 删除冗余的派工人员信息
        this.workerManager.deleteWorkersByWorkCard(workCard.getIdx());         
        
        if (canFinishNode(workCard.getNodeCaseIDX())) {
//              NodeRunner.runner(workCard.getNodeCaseIDX());
            JobProcessNode nodeCase = jobProcessNodeQueryManager.getModelById(workCard.getNodeCaseIDX());
            if (nodeCase != null) {
                try {
                    jobProcessNodeManager.updateFinishNodeCase(nodeCase);
                } catch (Exception e) {
                    throw new BusinessException(e);
                }
            }
        }
    }

    /**
     * <li>说明：批量完工时，验证机车检修作业工单是否能够完工
     * <li>创建人：何涛
     * <li>创建日期：2016-4-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCard 机车检修作业工单实体
     * @return true:可以完工，false:不能完工
     */
    private boolean canFinished(WorkCard workCard) {
        // 获取作业工单下的作业任务
        List<WorkTask> workTasks = this.workTaskManager.getListByWorkCard(workCard.getIdx());
        // 如果没有作业任务，则可以完工
        if (null == workTasks || workTasks.isEmpty()) {
            return true;
        }
        
        // 验证作业工单下属的作业任务是否可以完工
        for (WorkTask workTask : workTasks) {
            if (this.workTaskManager.canFinished(workTask)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * <li>说明：分页查询已处理的作业工单
     * <li>创建人：何涛
     * <li>创建日期：2016-5-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询条件封装实体
     * @return 已处理的作业工单分页列表
     */
    public Page<WorkCardBean> findFinishedWorkCard(SearchEntity<WorkCard> searchEntity) {
        StringBuffer sb = new StringBuffer();
        
        String sql = SqlMapUtil.getSql("jxgc-gdgl2:findFinishedWorkCard");
        // 查询条件 - 查询当前系统操作人员参与处理的作业工单
        sql = sql.replace("?", SystemContext.getOmEmployee().getEmpid().toString());  
        if(null != searchEntity.getEntity()){
            WorkCard wc = searchEntity.getEntity(); 
            //过滤车型,
            if (StringUtils.isNotBlank(wc.getTrainSortName())) {
                sb.append(" and P.TRAIN_TYPE_SHORTNAME like '%").append(wc.getTrainSortName()).append("%'");
            }
            //过滤车号
            if (StringUtils.isNotBlank(wc.getTrainNo())) {
                sb.append(" and P.TRAIN_NO like '%").append(wc.getTrainNo()).append("%'");
            }
            //过滤修程
            if (StringUtils.isNotBlank(wc.getRepairClassName())) {
                sb.append(" and P.REPAIR_CLASS_NAME like '%").append(wc.getRepairClassName()).append("%'");
            }
            //作业工单名称
            if (StringUtils.isNotBlank(wc.getWorkCardName())) {
                sb.append(" and C.WORK_CARD_NAME like '%").append(wc.getWorkCardName()).append("%'");
            }
        }
        sb.append(" ORDER BY C.UPDATE_TIME DESC ");
        
        sql += sb.toString();
        
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sql.substring(sql.indexOf("FROM"));
        return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, WorkCardBean.class);
    }
    
    /**
     * <li>说明：pad端添加车型车号查询条件
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-6-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 查询条件封装实体
     * @param search 模糊查询条件
     * @return 已处理的作业工单分页列表
     */
    public Page<WorkCardBean> findFinishedWorkCardBySearch(SearchEntity<WorkCard> searchEntity,String search) {
        StringBuffer sb = new StringBuffer();
        
        String sql = SqlMapUtil.getSql("jxgc-gdgl2:findFinishedWorkCard");
        // 查询条件 - 查询当前系统操作人员参与处理的作业工单
        sql = sql.replace("?", SystemContext.getOmEmployee().getEmpid().toString());  
        if(null != searchEntity.getEntity()){
            WorkCard wc = searchEntity.getEntity(); 
            //过滤车型,
            if (StringUtils.isNotBlank(wc.getTrainSortName())) {
                sb.append(" and P.TRAIN_TYPE_SHORTNAME like '%").append(wc.getTrainSortName()).append("%'");
            }
            //过滤车号
            if (StringUtils.isNotBlank(wc.getTrainNo())) {
                sb.append(" and P.TRAIN_NO like '%").append(wc.getTrainNo()).append("%'");
            }
            //过滤修程
            if (StringUtils.isNotBlank(wc.getRepairClassName())) {
                sb.append(" and P.REPAIR_CLASS_NAME like '%").append(wc.getRepairClassName()).append("%'");
            }
            //作业工单名称
            if (StringUtils.isNotBlank(wc.getWorkCardName())) {
                sb.append(" and C.WORK_CARD_NAME like '%").append(wc.getWorkCardName()).append("%'");
            }
        }
        // 模糊查询条件
        if(StringUtils.isNotBlank(search)){
            sb.append(" and (P.TRAIN_TYPE_SHORTNAME like '%").append(search).append("%'");
            sb.append(" or P.TRAIN_NO like '%").append(search).append("%') ");
        }
        sb.append(" ORDER BY C.UPDATE_TIME DESC ");
        
        sql += sb.toString();
        
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sql.substring(sql.indexOf("FROM"));
        return this.queryPageList(totalSql, sql, searchEntity.getStart(), searchEntity.getLimit(), false, WorkCardBean.class);
    }
    
    /**
     * <li>说明：通过作业工单ID返回作业工单的woker值的worker
     * <li>创建人：林欢
     * <li>创建日期：2016-5-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单IDX
     * @return String 拼接好的woker
     */
    public String findWorkCardWorkerByIDX(String workCardIDX) {
        WorkCard workCard = this.getModelById(workCardIDX);
        String workNameArray = workCard.getWorker();
        return workNameArray;
    }

    /**
     * <li>说明：更具机车检修作业计划-检修活动IDX查询机车检修作业计划-检修活动下的所有检修记录卡详情
     * <li>创建人：林欢
     * <li>创建日期：2016-4-25
     * <li>修改人： 张迪
     * <li>修改日期：2016-9-3
     * <li>修改内容：不使用copyListToList，影响效率
     * @throws Exception 
     * @throws BusinessException 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<WorkCardBean> findWorkCardInfoByWorkPlanRepairActivityIDX(String workPlanRepairActivityIDX) throws BusinessException, Exception {
        OmEmployee emp = SystemContext.getOmEmployee();
        //获取机车检修记录卡list
        List<WorkCardBean> workCardBeanList = findWorkCardInfoByRepairActivityIDX(workPlanRepairActivityIDX);
        //循环遍历准备封装任务单
        for (WorkCardBean workCardBean : workCardBeanList) {
            
            //通过机车检修记录卡id查询作业任务
            List<WorkTaskBean> workTaskBeanList = workTaskManager.getWorkTaskByWorkCard(workCardBean.getIdx());
//            List<WorkTaskBean> workTaskBeanList = BeanUtils.copyListToList(WorkTaskBean.class, workTaskList);
            
            //遍历循环，封装检修检查项
            for (WorkTaskBean workTaskBean : workTaskBeanList) {
                //查询检查结果集
                List<DataItemBean> detectResultList = detectResultManager.findListByWorkTask(workTaskBean.getIdx());
//                List<DataItemBean> dataItemList = BeanUtils.copyListToList(DataItemBean.class, detectResultList);
                workTaskBean.setDataItemList(detectResultList);
                
            }
            //任务单赋值
            workCardBean.setWorkTaskBeanList(workTaskBeanList);
            //通过机车检修记录单idx查询机车检修质量检验结果
            List<QCResult> qCResultList = qCResultManager.getListByWorkTask(workCardBean.getIdx());
            
            for (QCResult rdpQR : qCResultList) {
                if(rdpQR.getStatus().equals(QCResult.STATUS_DCL)){
                    // 判断当前用户是否能够做对应检验项 
                    QCParticipant participant = qcParticipantManager.getModel(workCardBean.getIdx(), rdpQR.getCheckItemCode(), emp.getEmpid()+"");
                    if(participant != null){
                        rdpQR.setIsCanSubmit("true");
                    }
                }
            }
            
            //机车检修质量检验结果赋值
            workCardBean.setQCResultList(qCResultList);
            
            //自检人员赋值 修改人
            OmEmployee omEmployee = omEmployeeManager.findByOperator(workCardBean.getUpdator());   
            if(null != omEmployee){
                workCardBean.setUpdatorName(omEmployee.getEmpname());
            }
            
        }
        
        return workCardBeanList;
    }

    /**
     * <li>说明：更具机车检修作业计划-检修活动IDX查询机车检修作业计划-检修活动下的所有检修记录卡详情
     * <li>创建人：林欢
     * <li>创建日期：2016-4-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception 
     * @throws BusinessException 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<WorkCardBean> findWorkCardInfoByRepairActivityIDX(String workPlanRepairActivityIDX) throws BusinessException, Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(" select new WorkCardBean(c.idx, c.workCardName, c.worker, c.rdpIDX ,c.updator) from WorkCard c where repairActivityIDX = ? and recordStatus = 0 order by updateTime asc ");
        //获取到数据list
        List<WorkCardBean> workCardList = getDaoUtils().find(sb.toString(),new Object[]{workPlanRepairActivityIDX});
        return workCardList;
    }
   
    /**
     * <li>说明：提交工单（最后一条工单处理完成后不完成提交节点）
     * <li>创建人：张迪
     * <li>创建日期：2016-9-22
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 作业工单处理实体
     * @param result 质量检验结果值对象
     * @param workTaskAndDetects 封装作业任务及检测结果的数据对象
     * @throws NoSuchFieldException 
     * @throws BusinessException
     */
    public void finishWorkCardNew(WorkCardHandle entity, QCResultVO[] result, WorkTaskBean[] workTaskAndDetects) throws BusinessException, NoSuchFieldException {
        // 验证实际开始时间不能为空
        Date realBeginTime = entity.getRealBeginTime();
        if (null == realBeginTime) {
            throw new NullPointerException("工单处理实际开始时间为空！");
        }
        // 验证实际结束时间不能为空
        Date realEndTime = entity.getRealEndTime();
        if (null == realEndTime) {
            throw new NullPointerException("工单处理实际结束时间为空！");
        }
        
        OmEmployee omEmployee = SystemContext.getOmEmployee();
        
        // 兼容老版本未同时提交检修结果数据的处理
        if (null == workTaskAndDetects || 0 >= workTaskAndDetects.length) {
            try {
                this.updateCompleteWorkCard(entity, omEmployee.getEmpid(), result);
                return;
            } catch (Exception e) {
                throw new BusinessException(e);
            }
        }
        
        // 作业工单和检修检测项同时提交的数据处理
        // 保存作业任务
        this.workTaskManager.saveWorkTaskBean(workTaskAndDetects, true);
        
        // 作业工单信息
        String workCardIDX = entity.getIdx();           // 作业工单主键
        WorkCard workCard = this.getModelById(workCardIDX);
        
        // 有必检的情况：更新作业卡，但不将状态更新成完成，更新为质量检查处理中
        if (this.qCResultManager.checkHasBJQC(workCardIDX)) {
            workCard.setStatus(WorkCard.STATUS_FINISHED);               // 质量检查处理中
        } else {
            workCard.setStatus(WorkCard.STATUS_HANDLED);                // 已处理
        }
//        workCard.setSystemEndTime(Calendar.getInstance().getTime());    // 系统完工时间
        // 保存作业工单
        this.saveWorkCard(entity, workCard); 
        
        // 保存质量检查相关信息，开放质量检查，包含对质量检查可参与人员的存储
        this.qCResultManager.saveAndOpenQCResult(workCard, result);
        
        // 删除冗余的派工人员信息
        this.workerManager.deleteWorkersByWorkCard(workCard.getIdx());         
    }

    
}
