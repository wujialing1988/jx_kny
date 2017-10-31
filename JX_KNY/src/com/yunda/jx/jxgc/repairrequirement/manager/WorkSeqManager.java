package com.yunda.jx.jxgc.repairrequirement.manager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.repairrequirement.entity.QualityControl;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkSeq;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkSeqDTO;
import com.yunda.jx.jxgc.repairrequirement.entity.WorkStep;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkSeq业务类,工序卡
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="workSeqManager")
public class WorkSeqManager extends JXBaseManager<WorkSeq, WorkSeq>{
	/**定义质量控制业务类*/
	@Resource
    private QualityControlManager qualityControlManager ;
    /**定义工步业务类*/
	@Resource
    private WorkStepManager workStepManager ;
    
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-12-19
	 * <li>修改人： 程锐
	 * <li>修改日期：2013-5-9
	 * <li>修改内容：验证编号是否重复
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@SuppressWarnings("unchecked")
    @Override
	public String[] validateUpdate(WorkSeq entity) throws BusinessException {
        List<String> errsList = new ArrayList<String>();
        String workSeqCode = entity.getWorkSeqCode();
        StringBuilder hql = new StringBuilder("from WorkSeq where recordStatus = 0 and workSeqCode = '" + workSeqCode + "'");
        if(StringUtils.isNotBlank(entity.getIdx())){
            hql.append(" and idx != '" + entity.getIdx() + "'");
        }
        List list = daoUtils.find(hql.toString());
        if(list != null && list.size() > 0){
            errsList.add("编号已存在，请重新输入编号！");
        }
        
        //查询传递过来的序号是否已经存在
        Integer seq = entity.getSeq();
        if (seq != null) {
            StringBuilder hql1 = new StringBuilder("from WorkSeq where recordStatus = 0 and seq = " + seq + "");
            hql1.append(" and recordIDX = '"+entity.getRecordIDX()+"'");
            if(!StringUtil.isNullOrBlank(entity.getIdx())){
                hql1.append(" and idx != '" + entity.getIdx() + "'");
            }
            List list1 = daoUtils.find(hql1.toString());
            if(list1 != null && list1.size() > 0){
                errsList.add("序号已存在，请重新输入序号！");
            }
        }
        
        if(errsList == null || errsList.size() == 0){
            return null ;
        }
        
        String[] errs = new String[errsList.size()];
        for (int i = 0; i < errsList.size(); i++) {
            errs[i] = errsList.get(i);
        }
		return errs;
	}
    
    /**
     * <li>说明：根据workSeqIDX获取工序卡质量控制情况
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param workSeqIDX 工序卡主键
     * @return List<QualityControl> 工序卡质量控制情况集合
     */
    public static List<QualityControl> getWorkSeqQualityControl(String workSeqIDX) {
        QualityControlManager qualityControlManager =
            (QualityControlManager) Application.getSpringApplicationContext().getBean("qualityControlManager");
        return qualityControlManager.findWorkSeqQC(workSeqIDX);
    }
    /**
     * 
     * <li>说明：根据workSeqIDX和业务字典id获取工序卡质量控制情况【配件质量检查项】
     * <li>创建人：程梅
     * <li>创建日期：2013-11-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param workSeqIDX 工序卡IDX
     * @param dicttypeid 数据字典ID
     * @return 工序卡质量控制情况
     */
    public static List<QualityControl> getWorkSeqQualityControl(String workSeqIDX,String dicttypeid) {
        QualityControlManager qualityControlManager =
            (QualityControlManager) Application.getSpringApplicationContext().getBean("qualityControlManager");
        return qualityControlManager.findWorkSeqQC(workSeqIDX,dicttypeid);
    }
    
    /**
     * 
     * <li>说明：新增作业工单基本信息
     * <li>创建人：程锐
     * <li>创建日期：2013-5-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entity 作业工单实体对象
     * @throws BusinessException, NoSuchFieldException
     */
    public void saveWorkSeq(WorkSeq entity) throws BusinessException, NoSuchFieldException{    
        this.saveOrUpdate(entity);        
    }
    
	/**
	 * <li>方法名：saveWorkSeqForQrKey
	 * <li>@param workSeq
	 * <li>@param qualityControlList
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：保存作业工单时保存质量控制记录数组
	 * <li>创建人：袁健
	 * <li>创建日期：2013-6-18
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public void saveWorkSeqForQrKey(WorkSeq workSeq, QualityControl[] qualityControlList) throws Exception {
        if (!StringUtil.isNullOrBlank(workSeq.getIdx())) { // 不等于空说明是修改
			qualityControlManager.deleteModelList(workSeq.getIdx()); // 修改时将数据删除后再创建
        }
        
        //保存工序卡对象（workCard的模板）
		this.saveOrUpdate(workSeq);
		List<QualityControl> entityList = new ArrayList<QualityControl>();
		for (QualityControl obj : qualityControlList) {
			obj.setRelationIDX(workSeq.getIdx()); // 设置关联的作业项（检测/检修项目）主键
			entityList.add(obj);
		}
		this.qualityControlManager.saveOrUpdate(entityList);
	}
   
    /**
     * 
     * <li>说明：删除作业工单并且级联删除质量控制情况、检测/检修项目及项目关联的检测结果、检测项
     * <li>创建人：程锐
     * <li>创建日期：2013-5-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @params ids 实体类主键idx数组
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */
    @SuppressWarnings("all")
    public void deleteWorkSeq(Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<WorkSeq> entityList = new ArrayList<WorkSeq>();
        List<String> stepIds = new ArrayList<String>() ;  //工步列表主键
        for (Serializable id : ids) {
            qualityControlManager.deleteModelList(id.toString()); //删除外键关联的质量控制数据
            List<WorkStep> stepList = workStepManager.getModelList(id.toString());
            if(stepList != null && stepList.size() > 0){
                for(WorkStep step : stepList){
                    stepIds.add(step.getIdx());
                }
            }
            WorkSeq t = getModelById(id);
            t = EntityUtil.setSysinfo(t);
//          设置逻辑删除字段状态为已删除
            t = EntityUtil.setDeleted(t);
            entityList.add(t);
        }
        workStepManager.logicDeleteWorkStep(stepIds.toArray(new String[0])); //删除工步对象和它关联的对象
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }
    /**
     * <li>方法名称：getParentProcessInstID
     * <li>方法说明：查询某流程实例的父流程实例 
     * <li>@param processInstID
     * <li>@return
     * <li>return: Object 流程实例ID
     * <li>创建人：张凡
     * <li>创建时间：2013-3-7 上午10:03:01
     * <li>修改人：
     * <li>修改内容：
     */
    public String getParentProcessInstID(String processInstID){
        
        String sql = "select parentprocid from wfprocessinst where processinstid=" + processInstID;
        
        return daoUtils.executeSqlQuery(sql).iterator().next().toString();
    } 
    
    
    /**
     * <li>方法说明：查询检修项目关联工序 
     * <li>方法名称：findWorkSeqByProject
     * <li>@param searchJson
     * <li>@param start
     * <li>@param limit
     * <li>@param orders
     * <li>@return
     * <li>@throws BusinessException
     * <li>return: Page
     * <li>创建人：张凡
     * <li>创建时间：2013-9-9 下午04:49:58
     * <li>修改人：林欢
     * <li>修改内容：工序卡和检修项目多对1关联，根据检修项目idx查询工序卡list
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     */
    public Page<WorkSeq> findWorkSeqByProject(String searchJson,int start,int limit, Order[] orders) throws BusinessException, SecurityException, NoSuchFieldException{
        
        //获取查询条件
        JSONObject ob = JSONObject.parseObject(searchJson);
        
        //获取记录单idx jxgc_repair_project
        String recordIDX = ob.getString("recordIDX");//
        StringBuilder sb = new StringBuilder(1000);
        
        sb.append(" select ");
        sb.append(" s.idx as idx, ");
        sb.append(" s.record_idx as recordIDX, ");
        sb.append(" s.work_seq_code as workSeqCode, ");
        sb.append(" s.work_seq_name as workSeqName, ");
        sb.append(" s.P_TRAIN_TYPE_IDX as pTrainTypeIDX, ");
        sb.append(" s.P_TRAIN_TYPE_SHORTNAME as pTrainTypeShortName, ");
        sb.append(" s.REPAIR_SCOPE as repairScope, ");
        sb.append(" s.SAFE_ANNOUNCEMENTS as safeAnnouncements, ");
        sb.append(" s.REMARK as remark, ");
        sb.append(" s.seq as seq ");
        sb.append(" from jxgc_work_seq s ");
        sb.append(" where s.record_status = 0  ");
        
        //当检修项目维护模块使用的时候，recordIDX不会为空
        if (StringUtils.isNotBlank(recordIDX)) {
            sb.append(" and s.record_idx = '").append(recordIDX).append("'");
        }
        sb.append(" order by s.seq desc  ");
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("from"));
//        String select = SqlMapUtil.getSql("jxgc-workseq:findWorkSeqByProject_select");
//        String from = SqlMapUtil.getSql("jxgc-workseq:findWorkSeqByProject_from");
//        String totalSql = "select count(1) " + from;
//        String sql =select+ from;
        return super.findPageList(totalSql, sb.toString(), start , limit, null, orders);
    }
    
    /**
     * <li>方法说明：作业计划编辑查询新增作业工单 
     * <li>方法名称：searchWorkSeq
     * <li>@param searchJson
     * <li>@param start
     * <li>@param limit
     * <li>@param orders
     * <li>@param activityIdx
     * <li>@return
     * <li>@throws BusinessException
     * <li>return: Page
     * <li>创建人：张凡
     * <li>创建时间：2013-9-22 下午04:04:51
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public Page searchWorkSeq(String searchJson,int start,int limit, Order[] orders, String activityIdx) throws BusinessException{
        String select = SqlMapUtil.getSql("jxgc-workseq:searchWorkSeq_select");
        String from = null;
        if( StringUtil.nvl(activityIdx).equals("")){
            from = SqlMapUtil.getSql("jxgc-workseq:searchWorkSeq_from2");
        }else{
            from = SqlMapUtil.getSql("jxgc-workseq:searchWorkSeq_from").replace("检修活动主键", activityIdx);
        }
        String totalSql = "select count(1) " + from;
        String sql =select+ from;
        return super.findPageList(totalSql, sql, start , limit, searchJson, orders);
    }

    /**
     * <li>说明：删除检修项目并且级联删除检修项目关联的工序卡
     * <li>创建人：林欢
     * <li>创建日期：2016-6-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public List<WorkSeq> findWorkSeqByRepairProjectIDX(Serializable id) {
        
        StringBuffer sb = new StringBuffer();
        
        sb.append(" from WorkSeq where recordStatus = 0 and recordIDX = ? ");
        List<WorkSeq> list = daoUtils.find(sb.toString(), new Object[] {id});
        
        return list;
    }

    /**
     * <li>方法说明：根据检修节点idx查询该节点下挂的工序卡
     * <li>创建人：林欢
     * <li>创建时间：2016-6-4 下午04:40:49
     * <li>修改人：
     * <li>修改内容：
     * @throws IOException 
     * @throws JsonMappingException 
     */
    public Page<WorkSeq> findWorkSeqByNodeIDX(String searchJson, Integer start, Integer limit, Order[] orders) {
//      获取查询条件
        JSONObject ob = JSONObject.parseObject(searchJson);
        
        //获取记录单idx jxgc_repair_project
        String nodeIDX = ob.getString("nodeIDX");//
        String workSeqName = ob.getString("workSeqName");//
        StringBuilder sb = new StringBuilder(1000);
        
        sb.append(" select ");
        sb.append(" s.idx as idx, ");
        sb.append(" s.record_idx as recordIDX, ");
        sb.append(" s.work_seq_code as workSeqCode, ");
        sb.append(" s.work_seq_name as workSeqName, ");
        sb.append(" s.P_TRAIN_TYPE_IDX as pTrainTypeIDX, ");
        sb.append(" s.P_TRAIN_TYPE_SHORTNAME as pTrainTypeShortName, ");
        sb.append(" s.REPAIR_SCOPE as repairScope, ");
        sb.append(" s.SAFE_ANNOUNCEMENTS as safeAnnouncements, ");
        sb.append(" s.REMARK as remark, ");
        sb.append(" s.seq as seq ");
        sb.append(" from JXGC_Job_Process_Node_Def  a, ");
        sb.append(" JXGC_JobNode_Union_WorkSeq b, ");
        sb.append(" jxgc_work_seq s ");
        sb.append(" where s.record_status = 0  ");
        sb.append(" and a.idx = b.node_idx ");
        sb.append(" and b.record_card_idx = s.idx  ");
        
        //当检修节点维护模块使用的时候，nodeIDX不会为空
        sb.append(" and a.idx = '").append(nodeIDX).append("'");
        
        //过滤检修记录卡名称
        if (StringUtils.isNotBlank(workSeqName)) {
            sb.append(" and s.work_seq_name like '%").append(workSeqName).append("%'");
        }
        
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("from"));
//        String select = SqlMapUtil.getSql("jxgc-workseq:findWorkSeqByProject_select");
//        String from = SqlMapUtil.getSql("jxgc-workseq:findWorkSeqByProject_from");
//        String totalSql = "select count(1) " + from;
//        String sql =select+ from;
        return super.findPageList(totalSql, sb.toString(), start , limit, null, orders);
    }

    /**
     * <li>方法说明：查询当前流程下可以选择的检修工序卡
     * <li>方法名称：findWorkSeqByProject
     * <li>创建人：林欢
     * <li>创建时间：2016-6-8 下午04:40:49
     * <li>修改人：
     * <li>修改内容：
     * @param whereListJson 查询条件
     * @param start 开始页吗
     * @param limit 显示条数
     * @param orders 排序
     * @return Page<WorkSeqDTO> 检修工序卡
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     */
    public Page<WorkSeqDTO> findWorkCardInfo(String whereListJson, Integer start, Integer limit, Order[] orders) throws SecurityException, NoSuchFieldException {
        
        JSONObject ob = JSONObject.parseObject(whereListJson);
        
        //获取流程
        String processIDX = ob.getString("processIDX");//
        //获取车型idx
        String pTrainTypeIDX = ob.getString("pTrainTypeIDX");//
        //记录单编码
        String repairProjectCode = ob.getString("repairProjectCode");//
        //记录单名称
        String repairProjectName = ob.getString("repairProjectName");//
        //记录卡编码
        String workSeqCode = ob.getString("workSeqCode");//
        //记录卡名称
        String workSeqName = ob.getString("workSeqName");//
        //描述
        String repairScope = ob.getString("repairScope");//
        
        StringBuilder sb = new StringBuilder();
        sb.append(" select ws.*,rp.Repair_Project_Code,rp.Repair_Project_Name from JXGC_Work_Seq ws,JXGC_Repair_Project rp where ws.Record_IDX = rp.idx ");
        sb.append(" and ws.RECORD_STATUS = 0 ");
        sb.append(" and rp.RECORD_STATUS = 0 ");
        sb.append(" and ws.p_train_type_idx = '").append(pTrainTypeIDX).append("' ");
        sb.append(" and ws.IDX NOT IN( ");
        sb.append(" SELECT record_card_idx FROM JXGC_JobNode_Union_WorkSeq WHERE Node_IDX IN( ");
        sb.append(" SELECT a.IDX FROM JXGC_Job_Process_Node_Def a,JXGC_Job_Process_Def b WHERE a.PROCESS_IDX = b.idx  ");
        sb.append(" and b.TRAIN_TYPE_IDX ='").append(pTrainTypeIDX).append("' ");
        sb.append(" and b.RECORD_STATUS = 0  and a.RECORD_STATUS = 0 AND a.Process_IDX = '").append(processIDX).append("' ");
        sb.append(" ) ");
        sb.append(" ) ");
        
//      记录单编码
        if (StringUtils.isNotBlank(repairProjectCode)) {
            sb.append(" and rp.Repair_Project_Code like '%").append(repairProjectCode).append("%' ");
        }
        //记录单名称
        if (StringUtils.isNotBlank(repairProjectName)) {
            sb.append(" and rp.Repair_Project_Name like '%").append(repairProjectName).append("%' ");
        }
        //记录卡编码
        if (StringUtils.isNotBlank(workSeqCode)) {
            sb.append(" and ws.WORK_SEQ_Code like '%").append(workSeqCode).append("%' ");
            
        }
        //记录卡名称
        if (StringUtils.isNotBlank(workSeqName)) {
            sb.append(" and ws.WORK_SEQ_Name like '%").append(workSeqName).append("%' ");
            
        }
        //描述
        if (StringUtils.isNotBlank(repairScope)) {
            sb.append(" and ws.REPAIR_SCOPE like '%").append(repairScope).append("%' ");
        }
        sb.append(" order by ws.Update_Time desc ");
        
        
        //此处的总数别名必须是ROWCOUNT，封装方法有规定
        String totalSql = "SELECT COUNT(*) AS ROWCOUNT " + sb.substring(sb.indexOf("from"));
        return this.queryPageList(totalSql, sb.toString(), start, limit, false, WorkSeqDTO.class);
    }
}