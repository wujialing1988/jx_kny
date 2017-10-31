package com.yunda.jx.jxgc.producttaskmanage.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.eos.workflow.data.WFWorkItem;
import com.yunda.frame.common.IbaseCombo;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.DisposeOpinion;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：DisposeOpinion业务类,处理意见
 * <li>创建人：程锐
 * <li>创建日期：2012-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 * 
 */ 
@Service(value="disposeOpinionManager")
public class DisposeOpinionManager extends JXBaseManager<DisposeOpinion, DisposeOpinion> implements IbaseCombo{
	/** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
	/** 流程任务业务类 */
    @Resource
	ProcessTaskManager processTaskManager;
	
	/** 人员选择业务类 */
    @Resource
	OmEmployeeSelectManager omEmployeeSelectManager;
    
    private static final String NONE = "无";
    
    /**
     * 
     * <li>说明：新增签名数据并完成工作项
     * <li>业务逻辑：1 保存签名实体数据
     * <li>		   2 根据bps流程更新工艺节点
     * <li>		   3 对不同的签名类型作特殊处理：提票开工签名、提票质量检查、提票车间调度审核/提票技术科技术鉴定。。。
     * <li>		   4 如签名实体有出段（厂）时间，则更新机车兑现单的出段时间
     * <li>		   5 完成bps工作项	
     * <li>创建人：张凡
     * <li>创建日期：2013-3-13 上午10:27:16
     * <li>修改人： 程锐
     * <li>修改日期：2014-12-25
     * <li>修改内容：代码重构
     * @param entity 签名实体对象
     * @param ac 操作者对象
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
    public void saveAndFinishWorkItem(DisposeOpinion entity, AcOperator ac) throws Exception{
		try{		    
		    saveOrUpdate(entity);
		    BPSLogin(ac.getUserid(),ac.getOperatorname());
		    updateNodeByProcess(entity);
		    //CRIF  2015-06-29  汪东良 紧急    删除V3.1版本故障提票相关的代码后报错，此处需要重构
//		    if (DisposeOpinion.SIGN_TYPE_FAULT_WORKING.equals(entity.getSignType())) {
//				updateFaultWorking(entity, ac);
//			}
//			if (DisposeOpinion.SIGN_TYPE_FAULT_QC.equals(entity.getSignType())) {
//				updateFaultQC(entity, ac);
//			}
		    if(DisposeOpinion.SIGN_TYPE_FAULT_VERTIFY.equals(entity.getSignType()) || DisposeOpinion.SIGN_TYPE_FAULT_TECVERTIFY.equals(entity.getSignType())){
		    }
		    if(entity.getOutTime() != null){
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        String sql = "update JXGC_Train_Work_Plan t set t.End_Time = " +
		        			 "to_date('" + sdf.format(entity.getOutTime()) + "','yyyy-MM-dd HH24:mi:ss') " +
		        			 "where t.idx = '" + entity.getRdpIdx() + "'";
				daoUtils.executeSql(sql);
		    }
		    
		}catch(Exception ex){
			ExceptionUtil.process(ex, logger);			
			throw new RuntimeException(ex);
		}
	}
	/**
	 * TODO V3.2.1代码重构
	 * <li>说明：根据bps流程更新工艺节点
	 * <li>业务逻辑：1 获取bps流程相关数据中的工艺流程id，为null则返回
	 * <li>        2 根据签名实体中的bps活动id获取bps流程相关信息，为null则返回
	 * <li>        3 根据2中bps流程的活动实例id和流程定义id获取工艺节点实体，为null则返回
	 * <li>        4 更新3获取的工艺节点的流程实例id、活动实例id、状态、实际完成时间、实际工期
	 * <li>创建人：程锐
	 * <li>创建日期：2014-12-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 签名实体对象
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void updateNodeByProcess(DisposeOpinion entity) throws Exception{
//	    Object tecProcessCaseIdx = getBPSService().getRelativeDataManager().getRelativeData(entity.getProcessInstID(), "p/tecProcessCaseIdx");
//	    if (tecProcessCaseIdx == null) return;
//	    String sql = "select d.processdefid,a.activitydefid from wfactivityinst a,wfprocessinst p ,wfprocessdefine d "
//				+ "where p.processinstid = a.processinstid and  d.processdefname = p.processdefname and d.currentstate=3"
//				+ " and d.currentflag=1  and activityinstid=" + entity.getActivityInstID();
//		List<Object[]> list = daoUtils.executeSqlQuery(sql);
//		if (list == null || list.size() < 1) return;
//		Object[] o = list.iterator().next();
//		
//		String hql = "from TecProcessNodeCase where tecProcessCaseIDX = '" + tecProcessCaseIdx + 
//					 "' and activityID = '" + o[1] + "' and processDefID = '" + o[0] + "'";
//		List<TecProcessNodeCase> nodeCaseList = daoUtils.find(hql);
//		if (nodeCaseList == null || nodeCaseList.size() < 1) return;
//		for (TecProcessNodeCase nodeCase : nodeCaseList) {
//			nodeCase.setProcessInstID(entity.getProcessInstID());
//			nodeCase.setActivityInstID(entity.getActivityInstID());
//			nodeCase.setStatus(TecProcessNodeCase.STATUS_COMPLETE);// 更新工艺流程节点实例状态，回填流程任务
//			nodeCase.setRealEndTime(new Date());
//			TecNodeCaseCtrlManager tecNodeCaseCtrlManager = (TecNodeCaseCtrlManager)Application.getSpringApplicationContext().getBean("tecNodeCaseCtrlManager");
//        	tecNodeCaseCtrlManager.updateRealWorkminutes(nodeCase);// 更新流程实例节点的实际工期
//		}
	}
    
    
//	/**
//	 * 
//	 * <li>说明：提票开工签名操作
//	 * <li>创建人：程锐
//	 * <li>创建日期：2014-12-25
//	 * <li>修改人： 
//	 * <li>修改日期：
//	 * <li>修改内容：
//	 * @param entity 签名实体对象
//	 * @param ac 操作者对象
//	 * @throws Exception
//     * 
//	 */
//  CRIF  2015-06-29  汪东良 紧急    删除V3.1版本故障提票相关的代码后报错，此处需要重构
//	private void updateFaultWorking(DisposeOpinion entity, AcOperator ac) throws Exception{
//        String faultIdx = getBPSService().getRelativeDataManager().getRelativeData(entity.getProcessInstID(), "p/faultIdx").toString();
//		OmEmployee emp = omEmployeeSelectManager.getByOperatorid(ac.getOperatorid());
//		faultRepairWorkerManager.updateWorking(faultIdx, emp.getEmpid());
//	}
//	/**
//	 * <li>说明：提票质量检查签名操作
//	 * <li>创建人：程锐
//	 * <li>创建日期：2014-12-25
//	 * <li>修改人：
//	 * <li>修改日期：
//	 * <li>修改内容：
//	 * @param entity 签名实体对象
//	 * @param ac 操作者对象
//	 * @throws Exception
//	 */
//    CRIF  2015-06-29  汪东良 紧急    删除V3.1版本故障提票相关的代码后报错，此处需要重构
//	private void updateFaultQC(DisposeOpinion entity, AcOperator ac) throws Exception{
//        String faultIdx = getBPSService().getRelativeDataManager().getRelativeData(entity.getProcessInstID(), "p/faultIdx").toString();		
//		faultQualityCheckResultManager.saveEntity(entity.getBizType(), faultIdx, entity.getDisposeOpinion(), ac.getOperatorid());// 新增提票质量检查记录
//	}
	
	
	/**
	 * <li>说明：根据流程活动实例ID获取已签名的签名人信息
	 * <li>创建人：程梅
	 * <li>创建日期：2013-4-7
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param activityInstID 检修活动ID
	 * @return 签名信息列表
	 */
	@SuppressWarnings("unchecked")
	public List<DisposeOpinion> getSignInfoByActivityInstId(Long activityInstID) {
		String sql = "select t.workitemname, ome.empname, omorg.orgname,to_char(t.endtime,'yyyy-MM-dd HH24:mi:ss') from wfworkitem t, om_organization omorg, om_employee ome " +
				"where t.participant = ome.userid " +
				"and ome.orgid = omorg.orgid " +
				"and t.currentstate in ('12') " +
				"and t.activityinstid = '"+ activityInstID +"'";
		List<Object[]> list = daoUtils.executeSqlQuery(sql);
		List<DisposeOpinion> unSigns = new ArrayList<DisposeOpinion>();
		DisposeOpinion opinion = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Object[] objs : list) {
			opinion = new DisposeOpinion();
			opinion.setIdx("0");
			opinion.setWorkItemName(objs[0]==null?"":objs[0].toString());
			opinion.setDisposePerson(objs[1]==null?"":objs[1].toString());
			opinion.setOrgName(objs[2]==null?"":objs[2].toString());
			try {
				opinion.setDisposeTime(objs[3]==null?new Date():sf.parse(objs[3].toString()));
			} catch (ParseException e) {
				ExceptionUtil.process(e,logger);
			}
			unSigns.add(opinion);
		}
		return unSigns;
	}
	
	
	/**
	 * <li>方法说明：查询审核处理意见 
	 * <li>方法名称：getVerifyJudgment
	 * <li>@param relationIdx
	 * <li>@return
	 * <li>return: List<DisposeOpinion>
	 * <li>创建人：张凡
	 * <li>创建时间：2013-8-5 下午01:47:10
	 * <li>修改人：
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
    public List<DisposeOpinion> getVerifyJudgment(String relationIdx){
	    
	    String sql = "select work_item_name, dispose_person, to_char(dispose_time,'yyyy-MM-dd HH24:mi'), dispose_opinion, sign_type, Dispose_Idea" +
	    		" from jxgc_dispose_opinion where business_IDX = '" + relationIdx + "' and record_Status = 0 order by dispose_Time";
	    List<Object[]> objList = daoUtils.executeSqlQuery(sql);
	    List<DisposeOpinion> list = new ArrayList<DisposeOpinion>(objList.size());
	    DisposeOpinion sign = null;
	    for(Object[] o : objList){
	        sign = new DisposeOpinion();
	        sign.setWorkItemName(StringUtil.nvl(o[0]));
	        sign.setDisposePerson(StringUtil.nvl(o[1]));
	        sign.setDisposeTimeStr(StringUtil.nvl(o[2]));
	        sign.setDisposeOpinion(StringUtil.nvl(o[3]));
	        sign.setSignType(StringUtil.nvl(o[4]));
	        sign.setDisposeIdea(StringUtil.nvl(o[5]));
	        list.add(sign);
	    }
	    return list;
	}
    
    /**
     * <li>说明：根据流程实例ID获取处理意见分页列表
     * <li>创建人：程锐
     * <li>创建日期：2014-9-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processInstID 流程实例ID
     * @return 处理意见分页列表
     */
    @SuppressWarnings("unchecked")
	public Page getInfoPageList(String processInstID) {
    	List<DisposeOpinion> opinionList = new ArrayList<DisposeOpinion>();
		List list = getInfoList(processInstID);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				DisposeOpinion disposeOpinion = new DisposeOpinion();
				disposeOpinion.setWorkItemName(objs[1] != null ? StringUtil.nvlTrim(objs[1].toString(), "") : "");
				disposeOpinion.setDisposePerson(objs[2] != null ? StringUtil.nvlTrim(objs[2].toString(), "") : "");
				disposeOpinion.setDisposeTimeStr(objs[3] != null ? StringUtil.nvlTrim(objs[3].toString(), "") : "");
				disposeOpinion.setDisposeIdea(objs[4] != null ? StringUtil.nvlTrim(objs[4].toString(), NONE) : NONE);
				disposeOpinion.setDisposeOpinion(objs[5] != null ? StringUtil.nvlTrim(objs[5].toString(), NONE) : NONE);
				opinionList.add(disposeOpinion);
			}
		}
		Page page = new Page(opinionList.size(), opinionList);
		return page;
    }
    
    /**
     * <li>说明：据流程实例ID获取处理意见列表
     * <li>创建人：程锐
     * <li>创建日期：2014-9-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processInstID 流程实例ID
     * @return 处理意见列表
     */
    private List getInfoList(String processInstID) {
    	List list = new ArrayList();
    	if (!StringUtil.isNullOrBlank(processInstID)) {
    		String sql = "select PROCESSINSTID, WORK_ITEM_NAME,DISPOSE_PERSON,to_char(DISPOSE_TIME,'yyyy-MM-dd HH24:mi')," +
    				    "DISPOSE_IDEA,DISPOSE_OPINION\n" +
						" from JXGC_Dispose_Opinion where processinstid in\n" + 
						"(select processinstid from wfprocessinst\n" + 
						"start with processinstid = " + processInstID + "\n" + 
						"connect by prior processinstid = parentprocid " +
						" union\n" + 
						"select processinstid from wf_h_processinst\n" + 
						"start with processinstid = " + processInstID + "\n" + 
						"connect by prior processinstid = parentprocid) " +
						"order by DISPOSE_TIME desc, WORKITEMID desc";
			list = daoUtils.executeSqlQuery(sql);
		}
    	return list;
    }
    
}