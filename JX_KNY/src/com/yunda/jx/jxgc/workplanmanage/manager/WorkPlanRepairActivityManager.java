package com.yunda.jx.jxgc.workplanmanage.manager;

import java.lang.reflect.Field;

import javax.annotation.Resource;
import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultQueryManager;
import com.yunda.jx.jxgc.workhis.repairactivity.manager.WorkPlanActivityHisManager;
import com.yunda.jx.jxgc.workplanmanage.entity.WorkPlanRepairActivity;
import com.yunda.jx.jxgc.workplanmanage.entity.WorkPlanRepairActivityDTO;
import com.yunda.jx.jxgc.workplanmanage.entity.WorkPlanRepairRecord;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkPlanRepairActivity业务类,机车检修作业计划-检修活动
 * <li>创建人：程锐
 * <li>创建日期：2015-04-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */ 
@Service(value = "workPlanRepairActivityManager")
public class WorkPlanRepairActivityManager extends JXBaseManager<WorkPlanRepairActivity, WorkPlanRepairActivity> {
//    /** 日志工具 */
//    private Logger logger = Logger.getLogger(getClass().getName());
//    
//    /** 作业卡业务类 */
//    @Resource
//    private WorkCardManager workCardManager;
//    
//    /** 节点查询业务类 */
//    @Resource
//    private JobProcessNodeQueryManager jobProcessNodeQueryManager;
//
//    /** 作业工单查询业务类 */
//    @Resource
//    private WorkCardQueryManager workCardQueryManager;
    @Resource
    private OmEmployeeManager  omEmployeeManager; 
    /** 历史记录单业务类 */
    @Resource
    private WorkPlanActivityHisManager workPlanActivityHisManager;
    
    /** 质量检验结果管理类 */
    @Resource
    private QCResultQueryManager qcResultQueryManager;

    /**
     * <li>说明：根据检修项目生成检修活动及关联的作业工单
     * <li>创建人：程锐
     * <li>创建日期：2015-4-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param repairActivities 检修活动实体数组
     * @throws Exception
     */
    public void saveOrUpdateBatch(WorkPlanRepairActivity[] repairActivities) throws Exception {
//        AcOperator ac = SystemContext.getAcOperator();
//        String nodeCaseIDX = repairActivities[0].getNodeCaseIDX();
//        for (WorkPlanRepairActivity repairActivity : repairActivities) {
//            String proc = "pkg_jxgc_workplan_add_activity.sp_add_repair_activity";
//            String[] param = {
//                               repairActivity.getNodeCaseIDX(), 
//                               repairActivity.getRepairProjectIDX(), 
//                               JXConfig.getInstance().getSynSiteID(),
//                               ac.getOperatorid().toString() 
//                             };
//            daoUtils.executeProc(proc, param);
//        }
//        daoUtils.flush();
//        JobProcessNode node = jobProcessNodeQueryManager.getModelById(nodeCaseIDX);
//        if (node == null)
//            return;
//        if (JobProcessNode.STATUS_GOING.equals(node.getStatus())) {
//            //更新节点下状态不为【处理中】的工单的状态为处理中
//            String sql = SqlMapUtil.getSql("jxgc-processNode:startWorkCardByNode")
//                                   .replace("#status#", WorkCard.STATUS_HANDLING)
//                                   .replace(JxgcConstants.UPDATETIME, DateUtil.getToday(DateUtil.yyyy_MM_dd_HH_mm_ss))
//                                   .replace(JxgcConstants.UPDATOR, String.valueOf(SystemContext.getAcOperator().getOperatorid()))
//                                   .replace(JxgcConstants.IDXS, CommonUtil.buildInSqlStr(nodeCaseIDX));
//            sql.concat(" AND STATUS IN ('").concat(WorkCard.STATUS_NEW).concat("','").concat(WorkCard.STATUS_OPEN).concat("')");
//            daoUtils.executeSql(sql);            
//        }
//        if (StringUtil.isNullOrBlank(node.getWorkStationIDX()) || StringUtil.isNullOrBlank(node.getWorkStationName())) {
//            logger.info("选择检修项目的派工--节点无工位，不能对作业工单派工");
//            return ;
//        }
//        
//        //只对无工位信息的作业工单默认派工
//        //找到节点下无工位及班组信息的作业工单
//        List<WorkCard> workCardList = workCardQueryManager.getNoStationAndTeamListByNode(nodeCaseIDX);
//        workCardManager.getDispatcher().updateWorkCardDispatchByCard(node, 
//                                                                     workCardQueryManager.buildIDXBuilder(workCardList),
//                                                                     workCardQueryManager.buildSqlIDXStr(workCardList), 
//                                                                     workCardQueryManager.buildIDXArray(workCardList));
    }

    /**
     * <li>说明：根据检修作业计划主键idx查询机车检修作业计划-检修活动
     * <li>创建人：林欢
     * <li>创建日期：2016-6-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param whereListJson 查询条件，json格式
     * @param start 开始页码
     * @param limit 结束页码
     * @param orders 排序字段
     * @return Page<WorkPlanRepairActivityDTO> 检修记录单分页page
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws Exception
     */
    public Page<WorkPlanRepairActivityDTO> findWorkPlanRepairActivityByWorkPlanIDX(String whereListJson, Integer start, Integer limit, Order[]  orders,String workPlanStatus) throws SecurityException, NoSuchFieldException {
        Page<WorkPlanRepairActivityDTO> page = null;
        
//        JSONObject ob = JSONObject.parseObject(whereListJson);       
        //获取数据
//        String rdpIDX = ob.getString("rdpIDX");//检修作业计划主键idx        
//      查询finish状态的工单下面的质量检查是否完成，如果没有完成返回true 完成返回false
//        //当工单完成，质量检测没有完成的时候，查询当前表，否则查询历史记录表
//        Integer count = qCResultManager.findFinishCountByRdpIDX(rdpIDX);
        
        if("Complete".equals(workPlanStatus)){
            page = workPlanActivityHisManager.findWorkPlanRepairActivityByWorkPlanIDX(whereListJson, start, limit, orders);           
        }else {
            page = searchWorkPlanRepairActivityByWorkPlanIDX(whereListJson, start, limit, orders);
        }
        
        return page;
    }
    
    /**
     * <li>说明：根据检修作业计划主键idx查询机车检修作业计划-检修活动
     * <li>创建人：林欢
     * <li>创建日期：2016-6-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param whereListJson 查询条件，json格式
     * @param start 开始页码
     * @param limit 结束页码
     * @param orders 排序字段
     * @return Page<WorkPlanRepairActivityDTO> 检修记录单分页page
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws Exception
     */
    public Page<WorkPlanRepairActivityDTO> searchWorkPlanRepairActivityByWorkPlanIDX(String whereListJson, Integer start, Integer limit, Order[]  orders) throws SecurityException, NoSuchFieldException {
        
        JSONObject ob = JSONObject.parseObject(whereListJson);        
        //获取数据
        String activityCode = ob.getString("activityCode");// 检修活动编码,活动名称
//        String activityName = ob.getString("activityName");// 检修活动名称
        String rdpIDX = ob.getString("rdpIDX");// 检修作业计划主键idx
        Boolean isAll = ob.getBoolean("isAll");// 是否显示当前人的质检项
        StringBuilder sb = new StringBuilder("");
        
        sb.append(" select t.idx,t.activityCode,t.activityName,t.repairProjectIDX, ");
        sb.append(" (case totleCount when 0 then 100 || '%' else round(oneCount/totleCount * 100,2) || '%' end) as completPercent ");
        sb.append(" from (select a.idx,a.activity_code activityCode, a.repair_project_idx repairProjectIDX ,a.activity_name activityName,");
        sb.append(" nvl((select count(*) from jxgc_work_card t1,jxgc_work_seq t2 where t1.work_seq_card_idx = t2.idx and t2.record_idx = b.idx and (t1.status = 'COMPLETE' or t1.status = 'FINISHED') and t1.record_status = 0 and t2.record_status = 0 and t1.rdp_idx = a.work_plan_idx),0) as oneCount, ");
        sb.append(" nvl((select count(*) from jxgc_work_card t1,jxgc_work_seq t2 where t1.work_seq_card_idx = t2.idx and t2.record_idx = b.idx and t1.record_status = 0 and t2.record_status = 0 and t1.rdp_idx = a.work_plan_idx),0) as totleCount ");
        sb.append(" from jxgc_work_plan_repair_activity a, jxgc_repair_project b ");
        sb.append(" where a.repair_project_idx = b.idx ");
        sb.append(" and b.record_status = 0 ");
        sb.append(" and a.work_plan_idx = '").append(rdpIDX).append("' ");
        //检修活动编码
        if (StringUtils.isNotBlank(activityCode)) {
            sb.append(" and a.activity_code || a.activity_name like '%").append(activityCode).append("%' ");
        }
//        //检修活动名称
//        if (StringUtils.isNotBlank(activityName)) {
//            sb.append(" and a.activity_name like '%").append(activityName).append("%' ");
//        }
//        
        //  判断是否查询当前存在质量检查的记录单
        if (null != isAll && isAll) {
            Long operatorId = ob.getLong(Constants.OPERATOR_ID); // 操作人idx
            Long empid = omEmployeeManager.findByOperator(operatorId).getEmpid(); 
            //  获取检修活动的idx 
            String recordIdxs = qcResultQueryManager.getIDXSql(empid, "getRepairActivityIDXList"); 
            sb.append(" and  a.idx in (").append(recordIdxs).append(") ");
        }
        
        // 排序处理
        if (null != orders && orders.length > 0) {
            String[] order = orders[0].toString().split(" ");
            String sort = order[0];
            //前台传递过来的排序方式 desc或者asc
            String dir = order[1];
            Class clazz = WorkPlanRepairActivity.class;
            //通过传递过来需要排序的字段反射字段对象
            Field field = clazz.getDeclaredField(sort);
            //获取字段上，标签上的列名
            Column annotation = field.getAnnotation(Column.class);
            if (null != annotation) {
                sb.append(" ORDER BY a.").append(annotation.name()).append(" ").append(dir);
            } else {
                sb.append(" ORDER BY a.").append(sort).append(" ").append(dir);
            }
        } else {
            sb.append(" ORDER BY a.CREATE_TIME");
        }
        
        sb.append(" ) t ");
       
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("from"));
        return this.queryPageList(totalSql, sb.toString(), start, limit, false,WorkPlanRepairActivityDTO.class);       
         
    }

    /**
     * <li>说明：通过节点查询检修记录单
     * <li>创建人：张迪
     * <li>创建日期：2016-10-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param ob 查询条件
     * @param start 开始页码
     * @param limit  结束页码
     * @param orders 排序字段
     * @return Page<WorkPlanRepairActivityDTO> 检修记录单分页page
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    public Page<WorkPlanRepairRecord> findWorkPlanRecordByNodeIDX(JSONObject ob , Integer start, Integer limit, Order[]  orders) throws SecurityException, NoSuchFieldException {      
        // 检修节点
        String nodeIdx = ob.getString("nodeIdx");       
        //检修活动编码
        String activityCode = ob.getString("activityCode");          
        StringBuilder sb = new StringBuilder("");           
            sb.append(" select  a.idx,a.work_plan_idx, a.repair_project_idx, a.activity_code,a.activity_name, c.update_time as record_date  " +
                    "From jxgc_work_plan_repair_activity a  left join   (select repair_activity_idx, update_time "
                    +" from (select t.repair_activity_idx,t.update_time,ROW_NUMBER() OVER (PARTITION BY   t.repair_activity_idx ORDER BY t.update_time  desc)RV "
                    +" from jxgc_work_card  t where t.record_status= 0  and t.status = 'COMPLETE' ) where   RV = 1) c on a.idx = c.repair_activity_idx "
                    +" where  a.idx in(select c.repair_activity_idx from jxgc_work_card   c where c.node_case_idx in(" 
                    + "SELECT IDX FROM JXGC_JOB_PROCESS_NODE T WHERE T.RECORD_STATUS = 0 START WITH T.IDX ='"); 
            sb.append(nodeIdx).append("' CONNECT BY PRIOR T.IDX = T.PARENT_IDX)) ");
            //  检修活动编码
            if (StringUtils.isNotBlank(activityCode)) {
                sb.append(" and a.activity_code || a.activity_name like '%").append(activityCode).append("%' ");
            }     
            sb.append(" ORDER BY a.activity_code ");

           
            //此处的总数别名必须是ROWCOUNT，封装方法有规定
            String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("From"));
            return this.queryPageList(totalSql, sb.toString(), start, limit, false, WorkPlanRepairRecord.class);       
             
        }
}