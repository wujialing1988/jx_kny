package com.yunda.jx.jxgc.producttaskmanage.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.ProcessTask;
import com.yunda.jx.util.MixedUtils;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：ProcessTask业务类,作业任务
 * <li>创建人：程锐
 * <li>创建日期：2012-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="processTaskManager")
public class ProcessTaskManager extends JXBaseManager<ProcessTask, ProcessTask>{
	
    private static final String LIKE_STR = "like";
    private Logger logger = Logger.getLogger(ProcessTaskManager.class.getName());
    
   
  
    
    /**
     * <li>方法说明：新增流程工单 
     * <li>方法名称：insertSystemTask
     * <li>@param processInstID
     * <li>@param rdpIdx
     * <li>@throws Exception
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2014-4-2 下午02:13:49
     * <li>修改人：
     * <li>修改内容：
     */
    public void insertSystemTask(Long processInstID, String rdpIdx) throws Exception{
        
        for(ProcessTask task : findRunningJobs(rdpIdx, ProcessTask.TYPE_SYS)){
            ProcessTask t = new ProcessTask();
            BeanUtils.copyProperties(t, task);
            t.setProcessInstID(processInstID);
            t.setIdx(null);
            saveOrUpdate(t);
            return;
        }
        
    }
    
	
	/**
	 * <li>方法名称：findHandledTaskList
	 * <li>方法说明：已处理任务查询 
	 * <li>@param searchJson searchJson
	 * <li>@param start start
	 * <li>@param limit limit
	 * <li>@param orders orders
	 * <li>@param userid userid
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>return: Page<ProcessTask>
	 * <li>创建人：张凡
	 * <li>创建时间：2013-2-28 下午01:57:09
	 * <li>修改人：
	 * <li>修改内容：
	 */
    public Page<ProcessTask> findHandledTaskList(String searchJson,int start,int limit, Order[] orders, String userid) throws BusinessException{
        String querySql = "select distinct wf.workitemid as \"idx\"," +
                		"tk.train_type as \"trainType\"," +
                		"tk.train_no as \"trainNo\"," +
                		"tk.repair_class_name as \"repairClassName\"," +
                		"tk.repair_time_name as \"repairTimeName\"," +
                		"wf.workitemname as \"workItemName\"," +
                		"tk.task_depict as \"taskDepict\"," +
                		"tk.token as \"token\"," +
                		"tk.specification_model as \"specificationModel\"," +
                		"tk.nameplate_no as \"nameplateNo\"," +
                		"tk.parts_no as \"partsNo\"," +
                		"tk.parts_name as \"partsName\"" +
                		",tk.update_time  as \"updateTime\"";
            
//        String fromSql = " from wfwiparticipant wf, jxgc_process_task tk " +
//                            " where 1=1 and tk.processinstid = wf.rootprocinstid and wf.currentstate=12" +
//                            " and wf.participant = '" + userid + "'";
        //程锐修改2014-05-20因工作项完成后wfwiparticipant记录自动转入历史表，故上面sql无法查到记录
        String fromSql = " from wfworkitem wf, jxgc_process_task tk " +
        				 " where 1=1 and (tk.processinstid = wf.rootprocinstid or tk.processinstid = wf.processinstid) and wf.currentstate = 12" +
        				 " and wf.participant = '" + userid + "'";
        StringBuffer sqlOrder =  new StringBuffer(" order by tk.update_Time DESC");
        String totalSql = "select count(1) from (" + querySql +  fromSql + ")";
        String sql =querySql+ fromSql + sqlOrder;
        return super.findPageList(totalSql, sql, start , limit, searchJson, orders);
    } 
    /**
     * TODO 未发现有流程调用
     * <li>说明：更新流程任务状态为已完成
     * <li>创建人：程梅
     * <li>创建日期：2013-3-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param taskid 参数名：参数说明
     * @throws 抛出异常列表
     */
    public void updateState(String taskid){
        try {
            ProcessTask task = getModelById(taskid);
            task = EntityUtil.setSysinfo(task);
            task.setCurrentState(ProcessTask.STATE_COMPLETE);//业务状态为已完成
            this.saveOrUpdate(task);
        } catch (NoSuchFieldException e) {
            ExceptionUtil.process(e, logger);
        }
    }

    
    
    /**
     * <li>说明：根据工作项ID查询任务基本信息
     * <li>创建人：张凡
     * <li>创建日期：2013-3-10
     * <li>修改人： 袁健
     * <li>修改日期：修改"where w.rootprocinstid = t.processinstid and r.idx=t.rdp_Idx and workitemid=" 
     *                      + workitemid;查询条件中rootprocinstid为processinstid
     * <li>修改内容：
     * @param workitemid 工作项ID
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public ProcessTask getProcessBasicInfo(String workitemid){
    	String sql="select workitemname,w.processinstid,t.train_type,t.train_no,t.repair_class_name," +
                "(case t.repair_time_name||'a' when 'a' then '无' else t.repair_time_name end)," +
                "w.processinstname,r.rc_idx,r.type_idx " +
    			"from wfworkitem w,jxgc_process_task t,v_rdp r " +
    			"where w.processinstid = t.processinstid and r.idx=t.rdp_Idx and workitemid=" + workitemid;
    	List<Object[]> list = daoUtils.executeSqlQuery(sql);
    	ProcessTask process=new ProcessTask();
    	for(Object[] o : list){
    		process.setWorkItemName(StringUtil.nvl(o[0]));
    		process.setProcessInstID(Long.valueOf(StringUtil.nvl(o[1])));
    		process.setTrainType(MixedUtils.spliceStr(o[2], o[3], null));
    		process.setTrainNo(StringUtil.nvl(o[3]));
    		process.setRepairClassName(MixedUtils.spliceStr(o[4], o[5], null));
    		process.setRepairtimeName(StringUtil.nvl(o[5]));
    		process.setProcessInstName(StringUtil.nvl(o[6]));
    		process.setXcIdx(StringUtil.nvl(o[7]));
    		process.setTrainTypeIdx(StringUtil.nvl(o[8]));
    	}
    	return process;
    }

    
    /**
     * <li>方法说明：根据工作项ID查询流程实例信息 
     * <li>方法名称：getEntityByWorkItemId
     * <li>@param workitemid
     * <li>@return
     * <li>return: ProcessTask
     * <li>创建人：张凡
     * <li>创建时间：2013-8-5 上午09:50:18
     * <li>修改人：袁健
     * <li>修改内容："where w.rootprocinstid = t.processinstid and workitemid=" + workitemid;查询条件中rootprocinstid为processinstid
     */
    @SuppressWarnings("unchecked")
    public ProcessTask getEntityByWorkItemId(String workitemid){
        String sql="select workitemname,w.processinstid,t.train_type,t.train_no,t.repair_class_name," +
                "(case t.repair_time_name||'a' when 'a' then '无' else t.repair_time_name end)," +
                "w.processinstname " +
                "from wfworkitem w,jxgc_process_task t " +
                "where w.processinstid = t.processinstid and workitemid=" + workitemid;
        List<Object[]> list = daoUtils.executeSqlQuery(sql);
        ProcessTask process=new ProcessTask();
        for(Object[] o : list){
            process.setWorkItemName(o[0].toString());
            process.setProcessInstID(MixedUtils.string2Long(o[1]));
            process.setTrainType(MixedUtils.spliceStr(o[2], o[3], null));
            process.setTrainNo(StringUtil.nvl(o[3]));
            process.setRepairClassName(MixedUtils.spliceStr(o[4], o[5], null));
            process.setRepairtimeName(StringUtil.nvl(o[5]));
            process.setProcessInstName(StringUtil.nvl(o[6]));
        }
        return process;
    }
    
	/**
	 * <li>说明：当流程完成后,更新流程任务单processTask中的状态
	 * <li>创建人：袁健
	 * <li>创建日期：2013-3-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param processInstId 流程实例ID
	 * @throws 抛出异常列表
	 */
	public void updateTaskStatusWhenProcessFinished(String processInstId) throws Exception{
		String sql = "update JXGC_Process_Task " +
				     "set current_State = '" + ProcessTask.STATE_COMPLETE + "' " +
				     "where processInstID = '"+ processInstId +"'";
        daoUtils.executeSql(sql);
	}
	
	/**
	 * TODO 调用的方法已被注释，看是否还必要保留
	 * <li>方法名称：findFaultProcessActivityInstID
	 * <li>方法说明：根据流程任务数据查找提票施修活动实例ID 和流程实例ID
	 * <li>@param faultIdx
	 * <li>@return
	 * <li>return: Long[][]
	 * <li>创建人：张凡
	 * <li>创建时间：2013-3-19 下午01:23:31
	 * <li>修改人：
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
    public Long[][] findFaultProcessActivityInstID(String faultIdx){
	    String sql = "select activityinstid,processInstID from wfactivityinst where processinstid in " +
	    		"(select processinstid from jxgc_process_task where source_idx in (" + faultIdx + ")) and currentstate=2";
	    List<Object[]> list = daoUtils.executeSqlQuery(sql);
	    Long[][] activityInst = new Long[list.size()][2];
	    int i=0;
	    for(Object[] o : list){
	        activityInst[i++] = new Long[]{Long.valueOf(o[0].toString()), Long.valueOf(o[1].toString())};
	    }
	    return activityInst;
	}
	/**
     * 
     * <li>说明：根据sourceIDX或rdpIDX获取运行中的流程任务单列表
     * <li>创建人：程锐
     * <li>创建日期：2013-3-20
     * <li>修改人： 程锐
     * <li>修改日期：2013-11-12
     * <li>修改内容：添加“运行中”状态过滤，去除“系统”类型过滤
     * @param idx 什么主键
     * @param type 什么类型
     * @return 工作项列表
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<ProcessTask> getListBySourceOrRdp(String idx, String type){
        StringBuilder hql = new StringBuilder();
        hql.append("from ProcessTask where recordStatus = 0 and currentState = '").append(ProcessTask.STATE_RUNNING).append("'");
        if("rdp".equals(type)){
            hql.append(" and sourceIdx = '").append(idx).append("' or rdpIdx = '").append(idx).append("'");
        }else if("other".equals(type)){
            hql.append(" and sourceIdx = '").append(idx).append("'");
        }
        return daoUtils.find(hql.toString());
    }
    
    
    /**
     * 
     * <li>说明：查询流程工单处理情况【机车部分】
     * <li>创建人：程梅
     * <li>创建日期：2013-7-23
     * <li>修改人： 程梅
     * <li>修改日期：2013年12月11日
     * <li>修改内容：机车和配件的都一起查了。。。
     * <li>修改人： 张凡
     * <li>修改日期：
     * <li>修改内容：重构了整个查询方法
     * @param searchJson 查询JSON
     * @param start start
     * @param limit limit
     * @param orders orders
     * @return 返回值说明
     * @throws 抛出异常列表
     */
    public Page<ProcessTask> findPageDataListForDisposeStatusTrain(String searchJson,int start,
                        int limit, Order[] orders) throws BusinessException{
    	String select = SqlMapUtil.getSql("jxgc-gdgl3:findAllTaskOfRuning_select");
    	String from = SqlMapUtil.getSql("jxgc-gdgl3:findAllTaskOfRuning_from");
		String totalSql = "select count(1) " + from;
        return super.findPageList(totalSql, select + from, start , limit, searchJson, orders);
    }
    
    /**
     * <li>方法说明：查询整备流程工单未处理项 
     * <li>方法名称：findPageForZBDisposeStatusTrain
     * <li>@param searchJson
     * <li>@param start
     * <li>@param limit
     * <li>@param orders
     * <li>@return
     * <li>@throws BusinessException
     * <li>return: Page<ProcessTask>
     * <li>创建人：张凡
     * <li>创建时间：2014-4-9 下午01:36:16
     * <li>修改人：
     * <li>修改内容：
     */
    public Page<ProcessTask> findPageForZBDisposeStatusTrain(String searchJson,int start,
        int limit, Order[] orders) throws BusinessException{
        String select = SqlMapUtil.getSql("jxgc-gdgl3:findAllTaskOfRuning_select");
        String from = SqlMapUtil.getSql("jxgc-gdgl3:findAllTaskOfRuning_from").replace("not exists", "exists");        
        String totalSql = "select count(1) " + from;
        return super.findPageList(totalSql, select + from, start , limit, searchJson, orders);
    } 

	/**
	 * <li>方法名：findRunningJobs
	 * <li>@param rdpIdx
	 * <li>@param token
	 * <li>@return
	 * <li>返回类型：List<String>
	 * <li>说明：查询运行流程任务
	 * <li>创建人：袁健
	 * <li>创建日期：2013-10-23
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
    public List<ProcessTask> findRunningJobs(String rdpIdx, String token) {
	    StringBuilder sb = new StringBuilder("from ProcessTask o where o.rdpIdx = '");
	    sb.append(rdpIdx).append("' and o.token = '");
	    sb.append(token).append("' and o.currentState = '");
	    sb.append(ProcessTask.STATE_RUNNING).append("'");
		List<ProcessTask> processTasks = (List<ProcessTask>) find(sb.toString());
		return processTasks;
	}

	
	/**
	 * <li>方法名：updateTaskToTerminate
	 * <li>@param processTasks
	 * <li>返回类型：void
	 * <li>说明：
	 * <li>创建人：袁健
	 * <li>创建日期：2013-10-23
	 * <li>修改人： 张凡
	 * <li>修改日期：2014-04-04 重构了代码将For循环更新变成1次更新
	 */
	public void updateTaskToTerminate(List<ProcessTask> processTasks){
	    if(MixedUtils.listIsEmpty(processTasks)){
	        return;
	    }
	    StringBuilder sb = new StringBuilder("update ProcessTask x set x.currentState = ? where x.idx in (");
	    List<String> param = new ArrayList<String>();
	    param.add(ProcessTask.STATE_TERMINATED);
		for (ProcessTask task : processTasks) {
		    sb.append("?,");
		    param.add(task.getIdx());
		}
		sb.deleteCharAt(sb.length() - 1).append(")");
		daoUtils.execute(sb.toString(), param.toArray());
	}
    
//	
//    /**
//     * <li>方法说明：新增转临修审核流程任务单 
//     * <li>方法名称：saveTaskForApplyLX
//     * <li>@param rdp
//     * <li>@param fault
//     * <li>@param processInstID
//     * <li>@throws BusinessException
//     * <li>@throws NoSuchFieldException
//     * <li>return: void
//     * <li>创建人：张凡
//     * <li>创建时间：2013-11-15 上午10:54:45
//     * <li>修改人：
//     * <li>修改内容：
//     */
//    public void saveTaskForApplyLX(ZbRdp rdp, FaultNotice fault, long processInstID) throws BusinessException, NoSuchFieldException{
//        
//        ProcessTask task = new ProcessTask();
//        task.setTrainType(rdp.getTrainTypeShortName());
//        task.setTrainNo(rdp.getTrainNo());
//        task.setRdpIdx(rdp.getIdx());
//        task.setRepairClassName(rdp.getRepairClassName());
//        task.setRepairtimeName(rdp.getRepairtimeName());
//        task.setProcessInstID(processInstID);
//        task.setToken(ProcessTask.TYPE_APPLY_LX);
//        task.setCurrentState(ProcessTask.STATE_RUNNING);
//        if(fault != null){
//            task.setSourceIdx(fault.getIdx());
//            task.setTaskDepict("申请转临修-位置：" + fault.getFixPlaceFullName() + "；描述：" + fault.getFaultDesc());
//        }else{
//            task.setTaskDepict(rdp.getTrainTypeShortName()+"|" + rdp.getTrainNo() + "申请转临修");
//        }
//        saveOrUpdate(task);
//    }

    /**
     * <li>方法说明：根据SOURCE IDX查询流程实例 
     * <li>方法名称：findProcessInstIDBySourceIdxs
     * <li>@param ids 格式(id1,id2)
     * <li>@return
     * <li>return: List<Long>
     * <li>创建人：张凡
     * <li>创建时间：2014-1-27 下午01:26:53
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public List<Long> findProcessInstIDBySourceIdxs(String ids){
        String hql = "select processInstID from ProcessTask where sourceIdx in ('" + ids.replace(",", "','") + "')";
        
        List<Long> processInstList = daoUtils.find(hql);
        return processInstList;
    }
    
    
    
    /**
     * <li>方法说明：根据条件查询流程工单数据 
     * <li>方法名称：findTaskOfCondition
     * <li>@param field
     * <li>@param value
     * <li>@param token
     * <li>@return
     * <li>return: List<ProcessTask>
     * <li>创建人：张凡
     * <li>创建时间：2014-3-31 下午02:21:22
     * <li>修改人：
     * <li>修改内容：
     */
    @SuppressWarnings("unchecked")
    public List<ProcessTask> findTaskOfCondition(String field, String value, String token){
        
        String hql = "from ProcessTask where " + field + " = ? and recordStatus = 0";
        Object[] param = new Object[2];
        param[0] = value;
        if(token != null){
            hql += " and token = ?";
            param[1] = token;
        }
        List<ProcessTask> list = daoUtils.find(hql, param);
        return list;
    }
 }
