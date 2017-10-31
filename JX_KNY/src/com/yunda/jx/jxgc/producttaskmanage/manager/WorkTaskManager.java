package com.yunda.jx.jxgc.producttaskmanage.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.DetectResult;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultManager;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkStepResult;
import com.yunda.jx.jxgc.repairrequirement.manager.WorkStepResultManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkTaskBean;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkTask业务类,作业任务
 * <li>创建人：程锐
 * <li>创建日期：2012-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="workTaskManager")
public class WorkTaskManager extends JXBaseManager<WorkTask, WorkTask>{
    
    /** 质量检验结果业务类 */
    @Resource
    private QCResultManager qCResultManager;
    
    /** 员工服务类 */
    @Resource
    private OmEmployeeManager omEmployeeManager;
    
    /** DetectResult业务类,检测结果 */
    @Resource
    private DetectResultManager detectResultManager;
    
    /** 作业项对应的处理结果业务类 */
    @Resource
    private WorkStepResultManager workStepResultManager;
       
    /**
     * <li>方法名称：checkCompleteWorkTask
     * <li>方法说明：检查作业任务未完成数量 
     * <li>@param workCardIdx
     * <li>@return
     * <li>return: int
     * <li>创建人：张凡
     * <li>创建时间：2013-3-7 下午01:33:50
     * <li>修改人：
     * <li>修改内容：
     */
    public String checkCompleteWorkTask(String workCardIdx){        
        String sql= SqlMapUtil.getSql("jxgc-gdgl2:checkWorkTask").replaceAll("待领取", WorkTask.STATUS_WAITINGFORGET)
                    .replace("作业工单", workCardIdx.replaceAll(",", "','"));
        
        return daoUtils.executeSqlQuery(sql).iterator().next() + "";
    }
    
    /**
     * <li>说明：根据作业工单关联的作业任务的完成情况判断作业工单能否完工
     * <li>创建人：程锐
     * <li>创建日期：2015-7-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIdx 作业工单IDX
     * @return "true" 能完工 "false" 不能完工
     */
    public String canCompleteWorkCard(String workCardIdx) {
        if (workCardIdx.length() < 50) { // 大于50表示是几个ID，查询没有意义
            String[] checkResult = checkCompleteWorkTask(workCardIdx).split("\\|");
            if (checkResult.length == 3) {
                if (!checkResult[0].equals("0")) { // 当[0]不等于“0”， 即有未完成的检测/修项目
                    if (!checkResult[2].equals(checkResult[0])) { // 当[2]即有默认结果 , 不等于[0]，即未完成的检测/修项目数量
                        return "false";
                    } else if (!checkResult[1].equals("0")) { // [1]不等于0，即有未完成的有录入数据项的检测/修项目
                        return "false";
                    }
                }
            }
        }
        return "true";
    }
    
    /**
     * <li>方法名称：getRepairTypeDict
     * <li>方法说明：根据字典类型ID获取字典项 
     * <li>@param dicttypeid
     * <li>@return
     * <li>return: Map<Object,Object>
     * <li>创建人：张凡
     * <li>创建时间：2013-3-7 下午04:19:21
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public Map<Object,Object> getRepairTypeDict(String dicttypeid){
        String sql = "select dictid,dictname from eos_dict_entry where dicttypeid in ('" + dicttypeid.replace(",", "','") + "')";
        
        List<Object[]> list = daoUtils.executeSqlQuery(sql);
        Map<Object,Object> map = new HashMap<Object,Object>();
        for(Object[] iterater : list){
            map.put(iterater[0], iterater[1]);
        }
        return map;
    }
    /**
     * 
     * <li>说明：根据作业卡ID获取作业任务列表
     * <li>创建人：程锐
     * <li>创建日期：2013-3-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业卡ID
     * @return List<WorkTask> 作业任务列表
     */
    @SuppressWarnings("unchecked")
    public List<WorkTask> getListByWorkCard(String workCardIDX){
        String hql = "from WorkTask where recordStatus = 0 and workCardIDX = '" + workCardIDX + "'";
        return daoUtils.find(hql);
    }
        
    /**
     * <li>方法说明：查询作业任务 
     * <li>创建人：张凡
     * <li>创建时间：2013-4-28 下午03:44:11
     * <li>修改人：何涛
     * <li>修改日期：2016-04-14
     * <li>修改内容：不再建议使用该方法，该方法用于查询作业工单下“未处理”或“已处理”的作业任务，可以使用pageQuery替换
     * <li>@param searchJson
     * @param start 查询开始索引
     * @param limit 查询记录条数
     * @param orders 排序字段
     * <li>@return 作业任务实体集合
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public Page findWorkTask(String searchJson,int start,int limit,Order[] orders){        
        String qSql = "select idx as \"idx\"," + 
                        " remarks as \"remarks\"," +
                        " work_step_idx as \"workStepIDX\"," +
                        " Repair_Content as \"repairContent\"," +
                        " Repair_Method as \"repairMethod\"," +
                        "  Repair_Result as \"repairResult\"," +
                        "  Repair_Standard as \"repairStandard\"," +
                        "  T.status as \"status\"," +
                        "  Work_Task_Name as \"workTaskName\"," +
                        " Work_task_code as \"workTaskCode\" ";
        String fromSql = "from JXGC_Work_Task t where 1=1 and 2=2 order by UPDATE_TIME desc, Work_task_code ASC " ;
        String totalSql = "select count(1) " + fromSql;
        String sql =qSql + fromSql;
        return super.findPageList(totalSql, sql, start , limit, searchJson, orders);
    }
    
    /**
     * <li>说明：分页查询，查询机车检修作业工单下的作业任务
     * <li>创建人：何涛
     * <li>创建日期：2016-4-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单idx主键
     * @param statusArray 查询的作业任务状态，
     *                    <br/>未处理:[WorkTask.STATUS_WAITINGFORGET, WorkTask.STATUS_INIT];
     *                    <br/>已处理:[STATUS_HANDLED];
     * @param start 查询开始索引
     * @param limit 查询记录条数
     * @param orders 排序字段
     * @return 作业任务实体集合
     */
    public Page<WorkTask> pageQuery(String workCardIDX, String[] statusArray, int start, int limit, Order[] orders) {
        List<Condition> whereList = new ArrayList<Condition>();
        Condition con = null;
        // 查询条件 - 作业工单idx主键
        if (!StringUtil.isNullOrBlank(workCardIDX)) {
            con = new Condition("workCardIDX", Condition.EQ, workCardIDX);
            whereList.add(con);
        }
        // 查询条件 - 作业任务状态
        if (null != statusArray) {
            if (1 == statusArray.length) {
                con = new Condition("status", Condition.EQ, statusArray[0]);
                con.setStringLike(false);
            } else {
                con = new Condition("status", Condition.IN, statusArray);
            }
            whereList.add(con);
        }
        
        List<Order> orderList = new ArrayList<Order>();
        // 排序
        if (null != orders) {
            for (Order order : orders) {
                orderList.add(order);
            }
        }
        QueryCriteria<WorkTask> query = new QueryCriteria<WorkTask>(WorkTask.class, whereList, orderList, start, limit);
        return this.findPageList(query);
    }
    
    /**
     * <li>说明：分页查询，查询机车检修作业工单下的作业任务，返回作业任务实体封装bean集合
     * <li>创建人：何涛
     * <li>创建日期：2016-4-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 作业工单idx主键
     * @param statusArray 查询的作业任务状态，
     *                    <br/>未处理:[WorkTask.STATUS_WAITINGFORGET, WorkTask.STATUS_INIT];
     *                    <br/>已处理:[STATUS_HANDLED];
     * @param start 查询开始索引
     * @param limit 查询记录条数
     * @param orders 排序字段
     * @return 作业任务实体封装bean集合
     */
    @SuppressWarnings("unchecked")
    public Page<WorkTaskBean> pageQuery2(String workCardIDX, String[] statusArray, int start, int limit, Order[] orders) {
        Page<WorkTask> page = this.pageQuery(workCardIDX, statusArray, start, limit, orders);
        
        /** 返回结果包装：将查询对象page中的集合数据用指定的WorkTaskBean对象进行封装 */
        List<WorkTaskBean> workTaskList = new ArrayList<WorkTaskBean>();
        try {
            workTaskList = BeanUtils.copyListToList(WorkTaskBean.class, page.getList());
        } catch (Exception e) {
            throw new BusinessException("机车检修作业任务类型转换错误！");
        }
        return new Page<WorkTaskBean>(page.getTotal(), workTaskList);
    }
    
    /**
     * <li>方法说明：工位终端查询reamrks(处理意见) 
     * <li>方法名称：getReamrks
     * <li>@param workCardId
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2013-6-1 下午04:35:07
     * <li>修改人：
     * <li>修改内容：
     */
//    @SuppressWarnings("unchecked")
//    public String getReamrks(String workCardId){
//        String sql = "select worker_name,remarks from jxgc_worker where work_card_idx='" + workCardId + "'";
//        List<Object[]> list = daoUtils.executeSqlQuery(sql);
//        StringBuffer sb = new StringBuffer("");
//        for(Object[] o : list){
//            if(o[1]!=null && !"".equals(o[1])){
//                sb.append("【");
//                sb.append(o[0]);
//                sb.append("】:");
//                sb.append(o[1]+";");
//            }
//        }
//        return sb.toString();
//    }
    
    /**
     * <li>方法说明：获取最大序号 
     * <li>方法名称：getMaxSeq
     * <li>@param workCardIdx
     * <li>@return
     * <li>return: int
     * <li>创建人：张凡
     * <li>创建时间：2013-10-11 下午03:47:16
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public int getMaxSeq(String workCardIdx){
        String sql = "select max(work_task_seq) from JXGC_WORK_Task where WORK_card_IDX = '" + workCardIdx + "' and record_status = 0";
        int seq = 1;
        List list = daoUtils.executeSqlQuery(sql);
        if(list != null && list.size() > 0){
            if(list.get(0) != null &&  list.get(0) instanceof BigDecimal) {
                BigDecimal maxSeq = (BigDecimal) list.get(0);
                seq = Integer.parseInt(maxSeq.toString()) + 1;
            }
        }
        return seq;
    }
        
    
    /**
     * <li>方法说明：自定义工单时新增检测项 
     * <li>方法名称：saveOfDefineWork
     * <li>@param t
     * <li>@param qc
     * <li>@return
     * <li>@throws Exception
     * <li>return: WorkTask
     * <li>创建人：张凡
     * <li>创建时间：2014-2-20 下午01:56:40
     * <li>修改人：
     * <li>修改内容：
     */
    public WorkTask saveOfDefineWork(WorkTask t, QCResult[] qc) throws Exception{
        saveOrUpdate(t);
        if(qc != null){
            qCResultManager.saveQCData(qc, t.getIdx(), t.getWorkCardIDX());
        }
        return t;
    }
    
    /**
     * <li>方法说明：终止未完成的作业任务 
     * <li>方法名称：updateTerminateTask
     * <li>@param rdpIdx
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-4-2 上午10:36:29
     * <li>修改人：
     * <li>修改内容：
     */
    public void updateTerminateTask(String rdpIdx){
        String hql = "update WorkTask set status = ? where  workCardIDX in (select idx from WorkCard t where t.rdpIDX = ?" +
        		" and status != ? and recordStatus = 0) and recordStatus = 0 and status != ?";
        daoUtils.execute(hql, WorkTask.STATUS_FINISHED, rdpIdx, WorkCard.STATUS_HANDLED, WorkTask.STATUS_HANDLED);
    }
    
    /**
     * <li>说明：作业工单-完工弹出页面时获取开工时间及备注信息
     * <li>创建人：程锐
     * <li>创建日期：2015-7-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX 以,号分隔的作业工单IDX字符串
     * @param operatorId 操作者ID
     * @return 开工时间及备注信息实体
     */
    public Object[] getWorkCardBeginTime(String workCardIDX, Long operatorId) {
        OmEmployee emp = omEmployeeManager.findByOperator(operatorId);
        String sql = "select to_char(min(t.real_begin_time),'yyyy-MM-dd HH24:mi')," +
                    "t.remarks from jxgc_work_card t, jxgc_worker c where t.idx = c.work_card_idx and t.idx in ('" + workCardIDX.replaceAll(",", "','") + "') and c.worker_id=" + 
                    emp.getEmpid() + " group by t.remarks";
        if (daoUtils.executeSqlQuery(sql) != null && daoUtils.executeSqlQuery(sql).size() > 0)
            return (Object[]) daoUtils.executeSqlQuery(sql).iterator().next();
        return null;
    }
    
    /**
     * <li>说明：根据作业工单idx获取需要指派的质量检查项列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDXS  作业工单idx,多个idx用,分隔
     * @return 需要指派的质量检查项列表
     * @throws Exception
     */
    public List<QCResult> getIsAssignCheckItems(String workCardIDXS) throws Exception {
        return qCResultManager.getIsAssignCheckItems(workCardIDXS);
    }

    /**
     * <li>说明：完成作业任务
     * <li>创建人：何涛
     * <li>创建日期：2015-7-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workTask 作业任务保存实体
     * @param detectResultArray 作业任务下属检测项保存数组
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void saveCompleteWorkTask(WorkTask workTask, DetectResult[] detectResultArray) throws BusinessException, NoSuchFieldException {
        // 保存作业任务下属检测项保存数组
        List<DetectResult> drList = new ArrayList<DetectResult>();
        DetectResult drEntity = null;
        for (DetectResult dr : detectResultArray) {
            drEntity = this.detectResultManager.getModelById(dr.getIdx());
            // 验证必填项（0表示必填）
            if (drEntity.getIsNotBlank().equals(0) && StringUtil.isNullOrBlank(dr.getDetectResult())) {
                throw new BusinessException("[" + drEntity.getDetectItemContent() + "]的检测结果不能为空！");
            }
            drEntity.setDetectResult(dr.getDetectResult());
            drList.add(drEntity);
        }
        if (drList.size() > 0) {
            this.detectResultManager.saveOrUpdate(drList);
        }
        // 保存作业任务
        WorkTask entity = this.getModelById(workTask.getIdx());
        entity.setStatus(WorkTask.STATUS_HANDLED);              // 状态-已处理 
        entity.setRepairResult(workTask.getRepairResult());     // 检修结果
        entity.setRemarks(workTask.getRemarks());               // 备注(iPad应用暂时未保存该字段值)
        this.saveOrUpdate(entity);
    }
    
    /**
     * <li>说明：获取检修检测页面table的表头部分
     * <li>创建人：何涛
     * <li>创建日期：2016-4-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param sb html代码段
     */
    private void getTableHead(StringBuilder sb) {
        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<td width=\"5%\">").append("序号").append("</td>");
        sb.append("<td width=\"15%\">").append("检修/检测项目").append("</td>");
        sb.append("<td width=\"45%\">").append("技术要求或标准规定").append("</td>");
        sb.append("<td width=\"20%\" colspan=\"2\">").append("检测结果（数据项）").append("</td>");
        sb.append("<td width=\"15%\">").append("备注").append("</td>");
        sb.append("</tr>");
        sb.append("</thead>");
    }
    
    /**
     * <li>说明：检修检测项查询，并将查询结果以HTML形式进行返回，以返回结果在页面进行显示
     * <li>创建人：何涛
     * <li>创建日期：2016-04-30
     * <li>修改人：何涛
     * <li>修改日期：2016-05-18
     * <li>修改内容：增加作业任务备注的编辑后，在页面端显示备注信息
     * @param workCardIDX 记录卡实例主键
     * @return  以HTML表格形式显示的检修检测项处理详情
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
    public String queryInHTML(String workCardIDX) throws IOException {
        String hql = "From WorkTask Where recordStatus = 0 And workCardIDX = ?";
        List<WorkTask> entityList = this.daoUtils.find(hql, new Object[]{ workCardIDX });
        // Modified by hetao on 2016-04-19 优化界面显示，在无检修检测项时不显示空提示信息
        if (null == entityList || 0 >= entityList.size()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<table cellpadding=\"0\" cellspacing=\"0\">");
        getTableHead(sb);
        sb.append("<tbody>");
        
        // 检修数据项数据模型
        int i = 0;          // 序号
        for (WorkTask task : entityList) {
            // 查询数据项
            int rowspan = 1;            // 表格默认的【行】合并数量
            List<DetectResult> diList = detectResultManager.getModelsByworkTaskIDX(task.getIdx());
            if (null != diList && 0 < diList.size()) {
                rowspan += diList.size();
            }
            sb.append("<tr>");
            sb.append("<td rowspan =\"").append(rowspan).append("\" align=\"center\">").append(++i).append("</td>");                // 序号
            sb.append("<td rowspan =\"").append(rowspan).append("\">").append(task.getWorkTaskName()).append("</td>");              // 检修/检测项目
            sb.append("<td rowspan =\"").append(rowspan).append("\">").append(task.getRepairStandard()).append("</td>");            // 技术要求或标准规定
            if (1 == rowspan) {
                sb.append("<td align=\"center\" colspan=\"2\">");
                sb.append(null == task.getRepairResult() ? "" : task.getRepairResult());
                sb.append("</td>");
                // 备注
                sb.append("<td>");
                sb.append(null == task.getRemarks()? "" : task.getRemarks());
                sb.append("</td>");
            } else {
                // 加载数据项
                for (DetectResult dr : diList) {
                    sb.append("<tr>");
                    // 数据项名称 + 检测值
                    // 检验输入的数据项是否超出了预设的范围值
                    // 如果输入值超出预设的范围，则以红色字体进行显示
                    sb.append("<td style=\"text-align:right;").append(dr.isOutOfRange() ? "color:red;" : "").append("\">").append(dr.getDetectItemContent()).append("：").append(dr.getIsNotBlank() == 0 ? "*" : "").append("</td>");
                    
                    sb.append("<td style=\"width:55px;").append(dr.isOutOfRange() ? "color:red;" : "").append("\">");
                    sb.append(null == dr.getDetectResult()? "&nbsp" : dr.getDetectResult());
                    sb.append("</td>");
                    
//                    StringBuilder temp = new StringBuilder();
//                    // 备注（值域）
//                    if (null != dr.getMinResult()) {
//                        temp.append("大于等于&nbsp;").append(dr.getMinResult()).append("<br/>");
//                    }
//                    if (null != dr.getMaxResult()) {
//                        temp.append("小于等于&nbsp;").append(dr.getMaxResult());
//                    }
//                    sb.append("<td>").append(temp.length() <= 0 ? "&nbsp;" : temp.toString()).append("</td>");

                    // 备注
                    sb.append("<td>");
                    sb.append(null == task.getRemarks()? "" : task.getRemarks());
                    sb.append("</td>");
                    sb.append("</tr>");
                }
            }
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        sb.append("</table>");
        return sb.toString();
    }
    
    /**
     * <li>说明：动态获取检修检测项的页面输入组件
     * <li>创建人：何涛
     * <li>创建日期：2016-04-07
     * <li>修改人：何涛
     * <li>修改日期：2016-05-18
     * <li>修改内容：增加作业任务备注的编辑
     * @param workCardIDX 记录卡实例主键
     * @return 动态html代码片段和检修数据项数据模型 {
     *      html: "<table>...</table>",
     *      data: [{
     *          idx:"E1AC1965F5F647C3A98F1C261366F5F5",
     *          status:"01",
     *          repairResult:"合格",
     *          remarks:"备注",           
     *          diList:[{
     *                  idx:"9069B8786F7842B5BC9DC6E0023214CE",
     *                  detectResult:"合格"
     *           }]
     *      }]
     * }
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> createInHTML(String workCardIDX) throws IOException {
        String hql = "From WorkTask Where recordStatus = 0 And workCardIDX = ?";
        List<WorkTask> entityList = this.daoUtils.find(hql, new Object[]{ workCardIDX });
        // Modified by hetao on 2016-04-19 优化界面显示，在无检修检测项时不显示空提示信息
        if (null == entityList || 0 >= entityList.size()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<table cellpadding=\"0\" cellspacing=\"0\">");
        getTableHead(sb);
        sb.append("<tbody>");
        
        // 检修数据项数据模型
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(entityList.size());
        Map<String, Object> taskJson = null;
        int i = 0;          // 序号
        for (WorkTask task : entityList) {
            taskJson = new HashMap<String, Object>();
            taskJson.put("idx", task.getIdx());
//            riJson.put("status", task.getStatus());
            taskJson.put("repairResult", task.getRepairResult());
            taskJson.put("remarks", task.getRemarks());
            list.add(taskJson);
            // 查询数据项
            int rowspan = 1;            // 表格默认的【行】合并数量
            List<DetectResult> diList = detectResultManager.getModelsByworkTaskIDX(task.getIdx());
            if (null != diList && 0 < diList.size()) {
                rowspan += diList.size();
            }
            sb.append("<tr>");
            sb.append("<td rowspan =\"").append(rowspan).append("\" align=\"center\">").append(++i).append("</td>");                // 序号
            sb.append("<td rowspan =\"").append(rowspan).append("\">").append(task.getWorkTaskName()).append("</td>");              // 检修/检测项目
            sb.append("<td rowspan =\"").append(rowspan).append("\">").append(task.getRepairStandard()).append("</td>");            // 技术要求或标准规定
            if (1 == rowspan) {
                sb.append("<td align=\"center\" colspan=\"2\">");
                // 可输入的下拉框开始
                sb.append("<div style=\"position:relative;\">");
                sb.append("<span style=\"margin-left:100px;width:18px;overflow:hidden;\">");
                sb.append("<select selectedIndex=\"-1\" style=\"height:30px;width:118px;margin-left:-130px;\" onchange=\"document.getElementById('" + task.getIdx() + "').value=this.value\">");
                // Modefied by hetao on 2016-05-09 增加一个空选项，规避select默认选择第一个下拉元素，导致在选择第一个选项不能触发change事件的缺陷
                sb.append("<option></option>");
                // 查询作业任务默认的处理结果
                List<WorkStepResult> workStepResults = this.workStepResultManager.getModelsByWorkTaskIDX(task.getIdx());
                // 设置默认的作业任务处理结果
                String defaultResult = "";
                if (null != workStepResults && !workStepResults.isEmpty()) {
                    for (WorkStepResult result : workStepResults) {
                        sb.append("<option vlaue=\"" + result.getResultName() + "\">" + result.getResultName() + "</option>");
                        if (WorkStepResult.ISDEFAULT_YES.equals(result.getIsDefault())) {
                            if (0 >= defaultResult.length()) defaultResult = result.getResultName();
                        }
                    }
                }
//                sb.append("<option vlaue=\"合格\">合格</option>");
//                sb.append("<option vlaue=\"不合格\">不合格</option>");
                sb.append("</select>");
//                sb.append("<input id=\""+ task.getIdx() +"\" name=\""+ task.getWorkTaskName() +"\" style=\"height:30px;width:140px;margin-left:-158px;\" value=\"" + (null == task.getRepairResult()? "合格" : task.getRepairResult()) + "\"/>");
                sb.append("<input id=\""+ task.getIdx() +"\" name=\""+ task.getWorkTaskName() +"\" style=\"height:30px;width:100px;margin-left:-118px;\" value=\"" + (null == task.getRepairResult()? defaultResult : task.getRepairResult()) + "\"/>");
                sb.append("</span>");
                sb.append("</div>");
                // 可输入的下拉框结束
                sb.append("</td>");
                // 备注
                sb.append("<td>");
                sb.append("<textarea style=\"height:40px;width:140px;\" id=\""+ task.getIdx() + "_bz" + "\">");
                sb.append(null == task.getRemarks()? "" : task.getRemarks());
                sb.append("</textarea>");
                sb.append("</td>");
            } else {
                List<Map<String, Object>> innerList = new ArrayList<Map<String,Object>>();
                Map<String, Object> diJson = null;
                // 加载数据项
                for (DetectResult dr : diList) {
                    diJson = new HashMap<String, Object>();
                    diJson.put("idx", dr.getIdx());
                    // Modified by hetao on 2016-03-04 22:30 增加数据项的必填项验证功能
                    diJson.put("required", (0 == dr.getIsNotBlank() ? true : false));
                    diJson.put("detectResult", dr.getDetectResult());
                    innerList.add(diJson);
                    sb.append("<tr>");
                    // 数据项名称 + 检测值
                    // 检验输入的数据项是否超出了预设的范围值
                    // 如果输入值超出预设的范围，则以红色字体进行显示
                    sb.append("<td style=\"text-align:right;").append(dr.isOutOfRange() ? "color:red;" : "").append("\">").append(dr.getDetectItemContent()).append("：").append(dr.getIsNotBlank() == 0 ? "*" : "").append("</td>");
                    sb.append("<td>");
                    // jxm.view.pjjxcl.PartsRdpRecordCardEditView.validate() 函数用于验证输入的数据项是否超出预设的范围
                    sb.append("<input onchange=\"jxm.view.pjjxcl.PartsRdpRecordCardEditView.validate('" + dr.getIdx() + "')\" ");
                    sb.append("min=\"" + dr.getMinResult() + "\" max=\"" + dr.getMaxResult() + "\" ");
                    sb.append("style=\"height:30px;width:65px;border:0;border-bottom:1px solid gray;border-radius:0;").append(dr.isOutOfRange() ? "color:red;" : "").append("\" ");
                    sb.append("type=\"text\" id=\""+ dr.getIdx() +"\" ");
                    sb.append("title=\"").append(dr.getDetectItemContent()).append("\"");
                    sb.append("name=\"").append(dr.getDetectItemContent()).append("\"");
                    sb.append("required=\"").append(0 == dr.getIsNotBlank() ? true : false).append("\" ");
                    sb.append("value=\"").append(null == dr.getDetectResult()? "" : dr.getDetectResult()).append("\" ");
                    sb.append("/>");
                    sb.append("</td>");
                    // html的input输入框提示信息 <仅用于备忘，于实际业务无关 hetao on 2016-03-04 23:50>
                    // <input type="text" name="textfield" value="请输入..." onclick="if(value==defaultValue){value='';this.style.color='#000'}" onBlur="if(!value){value=defaultValue;this.style.color='#999'}" style="color:#999"/>
//                    StringBuilder temp = new StringBuilder();
//                    // 备注（值域）
//                    if (null != dr.getMinResult()) {
//                        temp.append("大于等于&nbsp;").append(dr.getMinResult()).append("<br/>");
//                    }
//                    if (null != dr.getMaxResult()) {
//                        temp.append("小于等于&nbsp;").append(dr.getMaxResult());
//                    }
//                    sb.append("<td>").append(temp.length() <= 0 ? "&nbsp;" : temp.toString()).append("</td>");

                    // 备注
                    sb.append("<td>");
                    sb.append("<textarea style=\"height:40px;width:140px;margin:auto;\" id=\""+ task.getIdx() + "_bz" + "\">");
                    sb.append(null == task.getRemarks()? "" : task.getRemarks());
                    sb.append("</textarea>");
                    sb.append("</td>");
                    sb.append("</tr>");
                }
                taskJson.put("dataItemList", innerList);
            }
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        sb.append("</table>");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("html", sb.toString());                 // 动态html代码片段
        map.put("data", JSONUtil.write(list));          // 检修检测数据项数据模型
        return map;
    }

    /**
     * <li>说明：保存作业工单任务及检测结果
     * <li>创建人：何涛
     * <li>创建日期：2016-4-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workTaskAndDetects 作业工单任务及检测结果封装对象
     * @param isFinished 是否完工。true:更新作业任务状态为已处理
     * @throws BusinessException
     * @throws NoSuchFieldException
     */
    public void saveWorkTaskBean(WorkTaskBean[] workTaskAndDetects, boolean isFinished) throws BusinessException, NoSuchFieldException {
        WorkTask task = null;
        for (WorkTaskBean bean : workTaskAndDetects) {
            task = this.getModelById(bean.getIdx());
            
            // 保存作业工单任务
            task.setRepairResult(bean.getRepairResult());
            task.setRemarks(bean.getRemarks());
            if (isFinished) {
                task.setStatus(WorkTask.STATUS_HANDLED);
            }
            this.saveOrUpdate(task);
            
            // 保存检测项检测结果
            if (null != bean.getDataItemList() && !bean.getDataItemList().isEmpty()) {
                this.detectResultManager.save(bean.getDataItemList());
            }
        }
        
    }

    /**
     * <li>说明：验证作业任务是否可以完工
     * <li>创建人：何涛
     * <li>创建日期：2016-4-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workTask 作业任务实体
     * @return true:可以完工，false:不能完工
     */
    public boolean canFinished(WorkTask workTask) {
        String status = workTask.getStatus();
        // 如果作业任务状态为“已处理”或者“终止”则说明已经被处理，是已经完工的状态
        if (WorkTask.STATUS_HANDLED.equals(status) || WorkTask.STATUS_FINISHED.equals(status)) {
            return true;
        }
        // 验证作业任务是否有检修检测项未处理
        List<DetectResult> detectResults = this.detectResultManager.getModelsByworkTaskIDX(workTask.getIdx());
        // 如果没有检修检测项
        if (null == detectResults || detectResults.isEmpty()) {
            // 并且“检修结果”已经填写，则表示可以完工
            if (!StringUtil.isNullOrBlank(workTask.getRepairResult())) {
                return true;
            }
            // 如果“检修结果”未填写，则验证是否设置了默认的“检修结果”
            List<WorkStepResult> workStepResults = this.workStepResultManager.getModelsByWorkTaskIDX(workTask.getIdx());
            // 如果没有设置默认的“检修结果”，则表示不可以完工
            if (null == workStepResults || workStepResults.isEmpty()) {
                return false;
            }
            return true;
        }
        // 如果有检修检测项
        for (DetectResult detectResult : detectResults) {
            // 并且有必填的数据项未填写，则表示不能完工
            if (StringUtil.isNullOrBlank(detectResult.getDetectResult()) &&
                DetectResult.IS_NOT_BLANK_YES.equals(detectResult.getIsNotBlank())) {
                return false;
            }
        }
        return true;
    }

    /**
     * <li>说明：机车检修作业工单批量完工时，完成检修作业任务
     * <li>创建人：何涛
     * <li>创建日期：2016-4-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workTask 机车检修作业任务实体对象
     * @throws NoSuchFieldException 
     * @throws BusinessException 
     */
    public void finishWorkTask(WorkTask workTask) throws BusinessException, NoSuchFieldException {
        String status = workTask.getStatus();
        // 如果作业任务状态为“已处理”或者“终止”则说明已经被处理，是已经完工的状态
        if (WorkTask.STATUS_HANDLED.equals(status) || WorkTask.STATUS_FINISHED.equals(status)) {
            return;
        }
        
        workTask.setStatus(WorkTask.STATUS_HANDLED);            // 设置作业任务为“已处理”状态
        
        // 如果“检修结果”没有填写，则保存作业任务处理的默认值
        if (StringUtil.isNullOrBlank(workTask.getRepairResult())) {
            // 如果“检修结果”未填写，则验证是否设置了默认的“检修结果”
            List<WorkStepResult> workStepResults = this.workStepResultManager.getModelsByWorkTaskIDX(workTask.getIdx());
            if (null != workStepResults || !workStepResults.isEmpty()) {
                for (WorkStepResult workStepResult : workStepResults) {
                    if (WorkStepResult.ISDEFAULT_YES.equals(workStepResult.getIsDefault())) {
                        workTask.setRepairResult(workStepResult.getResultName());
                    }
                }
            }
        }
        this.saveOrUpdate(workTask);
    }
    

    /**
     * <li>说明：记录卡idx查询检修任务
     * <li>创建人：张迪
     * <li>创建日期：2016-9-3
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workCardIDX  记录卡idx
     * @return 检修任任务集合
     */
    @SuppressWarnings("unchecked")
    public List<WorkTaskBean> getWorkTaskByWorkCard(String workCardIDX){
        String hql = "select new WorkTaskBean(a.idx, a.workTaskName, a.repairStandard, a.repairResult) from WorkTask a where recordStatus = 0 and workCardIDX = '" + workCardIDX + "'";
        return daoUtils.find(hql);
    }
}