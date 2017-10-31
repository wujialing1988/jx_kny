package com.yunda.jx.scdd.enforceplan.manager;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.flow.snaker.entity.WorkItem;
import com.yunda.flow.snaker.manager.SnakerApprovalRecordManager;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlan;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetail;
import com.yunda.jxpz.utils.SystemConfigUtil;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainEnforcePlan业务类,机车施修计划
 * <li>创建人：程锐
 * <li>创建日期：2012-12-06
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="trainEnforcePlanManager")
public class TrainEnforcePlanManager extends JXBaseManager<TrainEnforcePlan, TrainEnforcePlan>{
    /** 机车施修计划明细业务类 */
	@Resource
    private TrainEnforcePlanDetailManager trainEnforcePlanDetailManager;
    
    /** snaker流程服务 **/
    @Resource
    private SnakerApprovalRecordManager snakerApprovalRecordManager ;
    
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务
	 * <li>创建人：程锐
	 * <li>创建日期：2012-12-06
	 * <li>修改人： 程锐 
	 * <li>修改日期：2012-12-13
	 * <li>修改内容：验证计划名称唯一
	 * @param entity 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */		
	@Override
	public String[] validateUpdate(TrainEnforcePlan entity) throws BusinessException {
        String[] errs = null;
        String planName = entity.getPlanName();
        List<TrainEnforcePlan> list = getPlanListByName(planName, entity.getIdx());
        if (list != null && list.size() > 0) {
            errs = new String[1];
            errs[0] = "计划名称已存在！";
            return errs;
        }
        return null;
	}
    /**
     * 
     * <li>说明：根据计划名称和计划主键获取计划列表
     * <li>创建人：程锐
     * <li>创建日期：2012-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param planName 计划名称
     * @param idx 计划主键
     * @return List<TrainEnforcePlan> 计划列表
     */
    @SuppressWarnings("unchecked")
    public List<TrainEnforcePlan> getPlanListByName(String planName, String idx){
        String hql = "from TrainEnforcePlan where recordStatus = 0 and planName = '" + planName + "'";
        if (!StringUtil.isNullOrBlank(idx)) {
            hql += " and idx != '" + idx + "'";
        }
        return daoUtils.find(hql);
    }
    
    /**
     * 
     * <li>说明：分页查询，返回实体类的分页列表对象
     * <li>创建人：程锐
     * <li>创建日期：2012-12-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEntity 机车施修计划对象包装类
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return Page 分页查询列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public Map findPageList(Map queryParams, int start, int limit, Order [] order)  throws BusinessException {

        StringBuilder hql = new StringBuilder();
        hql.append("from TrainEnforcePlan where recordStatus = 0");
        //计划名称 planName
        if(!StringUtil.isNullOrBlank(String.valueOf(queryParams.get("planName")).trim())){
            hql.append(" and planName like '%").append(String.valueOf(queryParams.get("planName")).trim()).append("%'");
        }
        //编制人  planPersonName
        if(!StringUtil.isNullOrBlank(String.valueOf(queryParams.get("planPersonName")).trim())){
            hql.append(" and planPersonName like '%").append(String.valueOf(queryParams.get("planPersonName")).trim()).append("%'");
        }
        if(queryParams != null && queryParams.get("planStartDate")!=null && String.valueOf(queryParams.get("planStartDate")) != null){
	        //计划开始日期起 planStartDate
	        if (!StringUtil.isNullOrBlank(String.valueOf(queryParams.get("planStartDate")).trim())) {
	            hql.append(" and to_char(planStartDate,'YYYY-MM-DD') >= '").append(String.valueOf(queryParams.get("planStartDate")).trim()).append("'");
	        }
        }
        if(queryParams != null && queryParams.get("toPlanStartDate")!=null && String.valueOf(queryParams.get("toPlanStartDate")) != null){
	        //计划开始日期止 toPlanStartDate
	        if (!StringUtil.isNullOrBlank(String.valueOf(queryParams.get("toPlanStartDate")).trim())) {
	            hql.append(" and to_char(planStartDate,'YYYY-MM-DD') <= '").append(String.valueOf(queryParams.get("toPlanStartDate")).trim()).append("'");
	        }
        }
        if(queryParams != null && queryParams.get("planEndDate")!=null && String.valueOf(queryParams.get("planEndDate")) != null){
	        //计划结束日期起
	        if (!StringUtil.isNullOrBlank(String.valueOf(queryParams.get("planEndDate")).trim())) {
	            hql.append(" and to_char(planEndDate,'YYYY-MM-DD') >= '").append(String.valueOf(queryParams.get("planEndDate")).trim()).append("'");
	        }
        }
        if(queryParams != null && queryParams.get("toplanEndDate")!=null && String.valueOf(queryParams.get("toplanEndDate")) != null){
	        //计划结束日期止
	        if (!StringUtil.isNullOrBlank(String.valueOf(queryParams.get("toplanEndDate")).trim())) {
	            hql.append(" and to_char(planEndDate,'YYYY-MM-DD') <= '").append(String.valueOf(queryParams.get("toplanEndDate")).trim()).append("'");
	        }
        }
        
        
        // 客货类型
        if(!StringUtil.isNullOrBlank(String.valueOf(queryParams.get("vehicleType")).trim())){
            hql.append(" and vehicleType = '").append(String.valueOf(queryParams.get("vehicleType")).trim()).append("'");
        }
        
        
        // 状态 未提交 已提交
        if(!StringUtil.isNullOrBlank(String.valueOf(queryParams.get("status")))){
            ArrayList<Integer> statusInt = (ArrayList<Integer>)queryParams.get("status");
                if(statusInt != null && statusInt.size() > 0){
                    String ins = "" ;
                    for (Integer i : statusInt) {
                        ins += i + ",";
                    }
                    if(!StringUtil.isNullOrBlank(ins) && ins.length() > 0){
                        ins = ins.substring(0, ins.length()-1);
                        hql.append(" and  planStatus in ("+ins+") ");
                    }
                    
                }
        }
        
        
        Order[] orders = order;
        if(orders != null && orders.length > 0){            
        	hql.append(HqlUtil.getOrderHql(orders));
        }else{
        	hql.append(" order by planStartDate desc");
        }
        int beginPos = hql.toString().toLowerCase().indexOf("from");
        StringBuffer totalHql = new StringBuffer(" select count(*)");
        totalHql.append(hql.toString().substring(beginPos));
        Page page = findPageList(totalHql.toString(), hql.toString(), start, limit);
        return page.extjsStore();
    }    
    
    /**
     * <li>说明：逻辑删除机车施修计划并级联删除计划明细记录，
     * <li>创建人：程锐
     * <li>创建日期：2013-01-09
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param ids 实体类主键idx数组
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */ 
    public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
        List<TrainEnforcePlan> entityList = new ArrayList<TrainEnforcePlan>();
        for (Serializable id : ids) {
            TrainEnforcePlan trainEnforcePlan = getModelById(id);
            trainEnforcePlan = EntityUtil.setSysinfo(trainEnforcePlan);
//          设置逻辑删除字段状态为已删除
            trainEnforcePlan = EntityUtil.setDeleted(trainEnforcePlan);
            entityList.add(trainEnforcePlan);
            List<TrainEnforcePlanDetail> detailList = trainEnforcePlanDetailManager.getDetailByPlan(id);
            if(detailList != null && detailList.size() > 0){
                for(TrainEnforcePlanDetail detail : detailList){
                    trainEnforcePlanDetailManager.logicDelete(detail.getIdx());
                }
            }
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }

    
    /**
     * <li>方法名称：getDateTreeData
     * <li>方法说明：生产计划日期段树 
     * <li>@param year
     * <li>@param monthx
     * <li>@param id
     * <li>@return
     * <li>@throws BusinessException
     * <li>@throws ParseException
     * <li>return: List<HashMap>
     * <li>创建人：张凡
     * <li>创建时间：2013-4-18 上午10:07:12
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public List<HashMap> getDateTreeData(String year, String monthx, String id) throws BusinessException, ParseException{
        
        List<HashMap> children = new ArrayList<HashMap>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if("Y_".equals(id.substring(0,2))){//为第一级，查出下级所有
            String y = id.substring(2);
            String[] month = {"一","二","三","四","五","六","七","八","九","十","十一","十二"};
            for(int i=0;i<12;i++){
                HashMap nodeMap = new HashMap();
                nodeMap.put("id", "M_" +y+"_" + i);
                nodeMap.put("text", month[i] + "月");
                nodeMap.put("leaf", false);
                nodeMap.put("start",y+"-" + (i+1<10 ? "0" + (i+1) :i+1) + "-01");
                Calendar clr = Calendar.getInstance();
                Date d = sdf.parse(y+"-"+(i+1<10 ? "0" + (i+1) :i+1)+"-01");
                clr.setTime(d);
                nodeMap.put("end", y+"-" + (i+1<10 ? "0" + (i+1) :i+1) + "-" + clr.getActualMaximum(Calendar.DAY_OF_MONTH));
                children.add(nodeMap);
            }
        }else{
            for(int i=1;i<=4;i++){
                String[] week = {"一","二","三","四"};
                String y = id.substring(2,6);
                Integer m = Integer.parseInt(id.substring(7));
                m++;
                HashMap nodeMap = new HashMap();
                nodeMap.put("id", "W_" + i);
                nodeMap.put("text", "第" + week[i-1] + "周");
                nodeMap.put("leaf", true);
                nodeMap.put("start",y+"-" + (m < 10 ? "0" + m : m) +"-"+ (((i-1) * 7) + 1));
                if(i == 4){
                    Calendar clr = Calendar.getInstance();
                    Date d = sdf.parse(y+"-"+(m < 10 ? "0" + m : m)+"-01");
                    clr.setTime(d);
                    nodeMap.put("end", y+"-" + (m < 10 ? "0" + m : m) + "-" + clr.getActualMaximum(Calendar.DAY_OF_MONTH));
                }else{
                    nodeMap.put("end", y+"-" + (m < 10 ? "0" + m : m) + "-" + (i * 7));
                }
                children.add(nodeMap);
            }
        }
        return children;
    }
    /**
     * 
     * <li>说明：根据计划主键查询对应的计划实体列表
     * <li>创建人：谭诚
     * <li>创建日期：2013-5-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：计划主键数组
     * @return 返回值说明 计划信息实体列表
     * @throws 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public List<TrainEnforcePlan> getTrainEnforcePlanList(String idx){
    	if(idx==null||"".equals(idx)) { return null;} //如果参数为空,则直接返回
    	String hql = SqlMapUtil.getSql("scdd:queryPlanInfo");
		Object[] param = new Object[]{idx};//计划主表主键Idx
		List <TrainEnforcePlan> list = daoUtils.getHibernateTemplate().find(hql,param);
		if(list == null||list.size()<1){
			return null;
		}
		return list;
    }
    
    /**
	 * <li>说明：删除实体对象前的验证业务
	 * <li>创建人：袁健
	 * <li>创建日期：2013-08-09
	 * <li>修改人：
	 * <li>修改日期： 
	 * <li>修改内容：
	 * @param ids 实体对象的idx主键数组
	 * @return 返回删除操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */	
	@SuppressWarnings("unchecked")
    @Override
	public String[] validateDelete(Serializable... ids) {
		List list = null;
		String[] message = null;
		try{
			for(int i = 0; i<ids.length; i++) {
				//逻辑删除操作前,需根据待删除信息Id,判断其是否已经处于兑现或者检修完成状态,这两种状态不允许修改计划内容
				String hql = SqlMapUtil.getSql("scdd:TrainEnforcePlanManager.validateDelete");
				Object[] param = new Object[]{ids[i].toString()};      //主单IDX
				list = daoUtils.getHibernateTemplate().find(hql,param);

				if(list!=null&&list.size()>0){ 
					//存在检修计划明细,不能删除
					message = new String[]{"计划主单下存在计划明细数据,不能执行删除操作!"};
                    return message ;
				}
                // 已提交流程数据不能执行删除操作
                TrainEnforcePlan enforcePlan = this.getModelById(ids[i]);
                if(enforcePlan == null){
                    message = new String[]{"数据不存在,不能执行删除操作!"};
                    return message ;
                }else if(enforcePlan.getPlanStatus() != TrainEnforcePlan.STATUS_COMPLETEPLAN 
                    && enforcePlan.getPlanStatus() != TrainEnforcePlan.STATUS_REFUSED){
                    // message = new String[]{"已提交流程数据,不能执行删除操作!"};
                    return message ;
                }
                
			}
		} catch (Exception e) {
			message = new String[]{"删除验证出错!"};
		}
		return message;
	}
    
    
    /**
     * <li>说明：提交流程
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 计划ID
     */
    public void sendProcess(String idx) {
        try {
            String processDefName = SystemConfigUtil.getValue(TrainEnforcePlan.SNAKER_FLOW_DEF_TRAINENFORCE);
            if(StringUtil.isNullOrBlank(processDefName)){
                throw new BusinessException("月计划流程名称未定义！");
            }
            TrainEnforcePlan enforcePlan = this.getModelById(idx);
            if(enforcePlan == null){
                throw new BusinessException("数据不存在！");
            }
            if(enforcePlan.getPlanStatus() != TrainEnforcePlan.STATUS_COMPLETEPLAN && enforcePlan.getPlanStatus() != TrainEnforcePlan.STATUS_REFUSED){
                throw new BusinessException("只有初始化和流程拒绝的数据才能提交流程！");
            }
            AcOperator operator = SystemContext.getAcOperator();
            String processInstId = snakerApprovalRecordManager.startProcess(processDefName,enforcePlan.getPlanName(),operator.getUserid());
            if(StringUtil.isNullOrBlank(processInstId)){
                throw new BusinessException("提交流程返回错误，请重新提交！");
            }
            enforcePlan.setProcessInstId(processInstId);
            enforcePlan.setPlanStatus(TrainEnforcePlan.STATUS_AUDITING);// 数据状态修改为【审核中】
            this.saveOrUpdate(enforcePlan);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
    
    /**
     * <li>说明：流程通过
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx
     * @param processInstID
     * @param workId
     */
    public void approvalProcess(String idx ,String processInstID,String workId){
        try {
            TrainEnforcePlan enforcePlan = this.getModelById(idx);
            if(enforcePlan == null){
                throw new BusinessException("数据不存在！");
            }
            if(enforcePlan.getPlanStatus() != TrainEnforcePlan.STATUS_AUDITING){
                throw new BusinessException("只有审核中的数据才能进行审批！");
            }
            boolean flag = snakerApprovalRecordManager.approvalProcessSnaker(processInstID, workId);
            if(flag){
                enforcePlan.setPlanStatus(TrainEnforcePlan.STATUS_AUDITED);// 数据状态修改为【审核通过】
                this.saveOrUpdate(enforcePlan);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
    
    
    /**
     * <li>说明：审批不通过
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 业务ID
     * @param processInstID 流程实例ID
     * @param workId 任务ID
     */
    public void refuseProcess(String idx ,String processInstID,String workId){
        try {
            TrainEnforcePlan enforcePlan = this.getModelById(idx);
            if(enforcePlan == null){
                throw new BusinessException("数据不存在！");
            }
            if(enforcePlan.getPlanStatus() != TrainEnforcePlan.STATUS_AUDITING){
                throw new BusinessException("只有审核中的数据才能进行审批！");
            }
            snakerApprovalRecordManager.refuseProcessSnaker(processInstID, workId);
            enforcePlan.setPlanStatus(TrainEnforcePlan.STATUS_REFUSED);// 数据状态修改为【审核拒绝】
            this.saveOrUpdate(enforcePlan);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
    
    /**
     * <li>说明：审批驳回（驳回到上一级）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 业务ID
     * @param processInstID 流程实例ID
     * @param workId 任务ID
     */
    public void rejectProcess(String idx ,String processInstID,String workId){
        try {
            TrainEnforcePlan enforcePlan = this.getModelById(idx);
            if(enforcePlan == null){
                throw new BusinessException("数据不存在！");
            }
            if(enforcePlan.getPlanStatus() != TrainEnforcePlan.STATUS_AUDITING){
                throw new BusinessException("只有审核中的数据才能进行审批！");
            }
            snakerApprovalRecordManager.rejectProcessSnaker(processInstID, workId);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
    
    /**
     * <li>说明：获取计划流程待办
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return Page<TrainEnforcePlan> 月计划
     */
    public Page<TrainEnforcePlan> getProcessWork(Map queryMap ,Integer startPage,Integer limit,Order [] order){
        AcOperator operator = SystemContext.getAcOperator();
        // 查询当前登录人角色
        com.yunda.flow.snaker.entity.Page<WorkItem> results = snakerApprovalRecordManager.getProcessWorkByUser(operator.getUserid(), startPage, limit);
        if(results == null){
            return null ;
        }
        Page<TrainEnforcePlan> pages = new Page<TrainEnforcePlan>();
        List<TrainEnforcePlan> list = new ArrayList<TrainEnforcePlan>();
        // 组装月计划待办事宜
        for (Object obj : results.getResult()) {
            JSONObject item = (JSONObject)obj;
            TrainEnforcePlan enforcePlan = this.getTrainEnforcePlanByInstID(item.getString("orderId"));
            if(enforcePlan != null){
                enforcePlan.setWorkId(item.getString("taskId"));
                enforcePlan.setWorkName(item.getString("taskName"));
                enforcePlan.setProcessId(item.getString("processId"));
                list.add(enforcePlan);
            }
        }
        pages.setList(list);
        pages.setTotal(Integer.parseInt(results.getTotalCount()+""));
        return pages ;
    }
    
    /**
     * <li>说明：通过流程实例ID获取任务
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processInstID 流程实例ID
     * @return TrainEnforcePlan 月计划任务实体
     */
    private TrainEnforcePlan getTrainEnforcePlanByInstID(String processInstID){
        String hql = " From TrainEnforcePlan where recordStatus = 0 and processInstId = '"+processInstID+"'" ;
        return (TrainEnforcePlan)this.daoUtils.findSingle(hql);
    }
    
    
}