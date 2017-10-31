package com.yunda.jx.jxgc.producttaskmanage.gzpg.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.manager.DispatcherProcManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkerManager;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeQueryManager;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanQueryManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.FindRdpByForemanDispatcherBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.NewForemanDispatcherBean;
import com.yunda.util.BeanUtils;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 工长派工处理业务类
 * <li>创建人：程锐
 * <li>创建日期：2014-10-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="gzpgProcManager")
public class GzpgProcManager extends JXBaseManager<WorkCard, WorkCard> {
	
	
	protected OmEmployeeManager getOmEmployeeManager() {
		return (OmEmployeeManager) Application.getSpringApplicationContext().getBean("omEmployeeManager");
	}
	
	protected WorkCardManager getWorkCardManager() {
		return (WorkCardManager) Application.getSpringApplicationContext().getBean("workCardManager");
	}
	
	protected WorkerManager getWorkerManager(){
        return (WorkerManager)Application.getSpringApplicationContext().getBean("workerManager");
    }
	
	protected AcOperatorManager getAcOperatorManager(){
        return (AcOperatorManager)Application.getSpringApplicationContext().getBean("acOperatorManager");
    }
	
	protected DispatcherProcManager getDispatcherProcManager() {
		return (DispatcherProcManager) Application.getSpringApplicationContext().getBean("dispatcherProcManager");
	}
    
    protected TrainWorkPlanQueryManager getTrainWorkPlanQueryManager() {
        return (TrainWorkPlanQueryManager) Application.getSpringApplicationContext().getBean("trainWorkPlanQueryManager");
    }
    
    protected JobProcessNodeQueryManager getJobProcessNodeQueryManager() {
        return (JobProcessNodeQueryManager) Application.getSpringApplicationContext().getBean("jobProcessNodeQueryManager");
    }
    
    private static final String WORKERNAME = "workerName";
    
	/**
	 * <li>说明：查询工长派工列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchJson	 
		rdpIDX					String		否	生产任务单IDX
		nodeCaseIDX				String		否	工艺节点idx
		repairActivityIDX		String		否	检修活动IDX
		workCardName			String		否	作业工单名称
	 * @param start 开始行
     * @param limit 查询每页结果数量
	 * @param mode 查询标识，已派工/未派工 (0为未派工，1为已派工)
	 * @param operatorid 操作员ID
	 * @return 派工列表
	 *  标识							数据类型	说明
		idx							String	作业工单IDX
		trainNo						String	机车(配件)号
		lastTimeWorker				String	上次作业人员
		workers						String	作业人员
		nodeCaseName				String	节点名称
		repairActivityName			String	活动名称
		workCardName				String	作业工单名称
		workStationName				String	班组
		workStationBelongTeamName	String	工位
		status						String	状态
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Page queryGzpgList(String searchJson,
							   int start,
							   int limit, 
							   String mode,
							   Long operatorid) throws Exception {

		Page pageList = new Page();
		Map searchJsonMap = new HashMap<String, Object>();
		if (!StringUtil.isNullOrBlank(searchJson)) {
			searchJsonMap = JSONUtil.read(searchJson, Map.class);
		}
		
		searchJsonMap.put("haveDefaultPerson", "1".equals(mode) ? "1" : "$[OR]$0]|[$IS NULL$");
		Long teamId = getOmEmployeeManager().findByOperator(operatorid).getOrgid();
		searchJsonMap.put("workStationBelongTeam", String.valueOf(teamId));
		if (searchJsonMap.containsKey(WORKERNAME) && searchJsonMap.get(WORKERNAME) != null && !StringUtil.isNullOrBlank(searchJsonMap.get(WORKERNAME).toString())) {				
			searchJsonMap.put("workers", "#[w]#$[SF]$$[LIKE]$%" + searchJsonMap.get(WORKERNAME) + "%");
			searchJsonMap.remove(WORKERNAME);
		}
		searchJson = JSONUtil.write(searchJsonMap);
		/**返回结果包装*/
		Page<WorkCard> page = getWorkCardManager().findWorkCard(searchJson, start, limit, null, mode, null);
		List<NewForemanDispatcherBean> list = new ArrayList<NewForemanDispatcherBean>();
		list = BeanUtils.copyListToList(NewForemanDispatcherBean.class, page.getList());
//		List<NewForemanDispatcherBean> newlist = new ArrayList<NewForemanDispatcherBean>();
//		for (NewForemanDispatcherBean bean : list) {
//			List<Worker> workerList = getWorkerManager().getHandledWorkerListByWorkCard(bean.getIdx());
//			String handledWorkers = "";
//			for (Worker worker : workerList) {
//				handledWorkers += worker.getWorkerName() + ",";
//			}
//			bean.setHandledWorkers(StringUtil.isNullOrBlank(handledWorkers) ? handledWorkers : handledWorkers.substring(0, handledWorkers.length() - 1));
//			newlist.add(bean);
//		}
		pageList = new Page(page.getTotal(), list);
		
		return pageList;
    
	}	
	/**
	 * <li>说明：查询生产任务单列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid operatorid 操作员ID
	 * @param start 开始行
     * @param limit 查询每页结果数量
	 * @param dispatcherStr 派工查询过滤条件
	 * @return 生产任务单列表
	 * 标识				数据类型	说明
		idx				String	主键
		groupRdpInfo	String	生产任务单名称
		buildUpTypeIDX	String	组成型号主键
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Page queryRdpList(Long operatorid, int start, int limit, String dispatcherStr) throws Exception {
		Long teamId = getOmEmployeeManager().findByOperator(operatorid).getOrgid();
		String sql = SqlMapUtil.getSql("jxgc-workcardquery:findByTrainAndParts").replace("?", String.valueOf(teamId)).replace("###", dispatcherStr);
		String totalSql = "select count(*) as \"rowcount\" from (" + sql + ")";
		Page<TrainWorkPlan> page = getTrainWorkPlanQueryManager().findPageList(totalSql, sql, start, limit, null, null);
		
		List<FindRdpByForemanDispatcherBean> list = new ArrayList<FindRdpByForemanDispatcherBean>();
		list = BeanUtils.copyListToList(FindRdpByForemanDispatcherBean.class, page.getList());
		return new Page(list.size(), list);
	}
	
	/**
	 * 
	 * <li>说明：查询检修活动列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param rdpIdx 兑现单主键
	 * @param isDispatcher 已派工/未派工 'y'/'n'
	 * @param operatorid 操作员ID
	 * @param start 起始页
	 * @param limit 每页限制条数
	 * @return 检修活动列表
	 *  标识				数据类型	说明
		idx				String	检修活动idx
		activityName	String	检修活动名称

	 * @throws Exception
	 */
    //V3.2.1代码删除
//	@SuppressWarnings("unchecked")
//	public Page queryRepairActivityList(String rdpIdx,
//										 String isDispatcher,
//										 Long operatorid,  
//										 int start, 
//										 int limit) throws Exception {
//		Long teamId = getOmEmployeeManager().findByOperator(operatorid).getOrgid(); //根据当前操作员ID，调用查询接口，获取其所属班组
//        
//		Map<String, Object> map = getRepairActivityManager().getRepairActivate(rdpIdx, String.valueOf(teamId), isDispatcher, start, limit);
//		/**封装返回值*/
//		List<RepairActiveBean> list = new ArrayList<RepairActiveBean>();
//		list = BeanUtils.copyListToList(RepairActiveBean.class, (List)map.get("root"));
//		return new Page((Integer)(map.get("totalProperty")),list); //构造page对象，并返回分页前的总数量
//	}

	/**
	 * <li>说明：查询工艺节点列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param parentIdx    上级节点IDX
	 * @param rdpIdx 	   兑现单主键
	 * @param operatorid   操作员ID
	 * @param isDispatcher 已派工/未派工 'y'/'n'
	 * @return 工艺节点列表
	 *  标识				数据类型	说明
		id				String	工艺节点id
		text			String	工艺节点名称
		nodeCaseCode	String	工艺节点实例编码
 
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> queryTecNodeList(String parentIdx, 
								  String rdpIdx, 
								  Long operatorid, 
								  String isDispatcher) {
		Long teamId = getOmEmployeeManager().findByOperator(operatorid).getOrgid(); // 根据当前操作员ID，调用查询接口，获取其所属班组
		String pid = (StringUtil.isNullOrBlank(parentIdx) || parentIdx.startsWith("xnode")) ? null : parentIdx;
        return getJobProcessNodeQueryManager().getNodeTree(rdpIdx,String.valueOf(teamId),pid,isDispatcher);
	}
			
	/**
	 * 
	 * <li>说明：查询处理状态列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 处理状态列表
	 */
	@SuppressWarnings("unchecked")
	public Page queryStatusList() throws Exception{
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("status", WorkCard.STATUS_NEW);
		map.put("statusMeaning", "初始化");
		list.add(map);
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("status", WorkCard.STATUS_OPEN);
		map1.put("statusMeaning", "已开放");
		list.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("status", WorkCard.STATUS_HANDLING);
		map2.put("statusMeaning", "处理中");
		list.add(map2);
		return new Page(list.size(), list);
	}

	/**
	 * 
	 * <li>说明：全部工长派工
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchJson 查询条件
	 *	标识						数据类型	 是否必填	说明
		nodeCaseIDX				String		否	工艺节点IDX
		repairActivityIDX		String		否	检修活动IDX
		rdpIDX					String		否	生产任务单IDX
		fixPlaceFullName		String		否	位置全名(取消使用)
		workCardName			String		否	作业工单名称
	 * @param operatorid 操作员ID
	 * @param empids 工人ID集合，多个主键以英文,号分隔
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void saveAllDispatch(String searchJson, Long operatorid, String empids) throws JsonParseException, JsonMappingException, IOException {
		AcOperator ac = getAcOperatorManager().getModelById(operatorid);            
        SystemContext.setAcOperator(ac);
        
        OmEmployee emp = getOmEmployeeManager().findByOperator(operatorid);
        SystemContext.setOmEmployee(emp);
        Map searchJsonMap = new HashMap<String, Object>();
		if (!StringUtil.isNullOrBlank(searchJson)) {
			searchJsonMap = JSONUtil.read(searchJson, Map.class);
		}
		searchJsonMap.put("haveDefaultPerson", "0");
		searchJsonMap.put("workStationBelongTeam", String.valueOf(emp.getOrgid()));
		
		searchJson = JSONUtil.write(searchJsonMap);
		getDispatcherProcManager().foremanAllDispatcher(searchJson, empids, operatorid);
	}

	/**
	 * 
	 * <li>说明：默认上次工长派工
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param workCardIds 作业卡主键，多个主键以英文,号分隔
	 * @return page分页对象
	 * @throws Exception
	 */
	public Page saveDefaultLastTimeDispatch(Long operatorid, String workCardIds) throws Exception{
		AcOperator ac = getAcOperatorManager().getModelById(operatorid);            
        SystemContext.setAcOperator(ac);
        
        OmEmployee emp = getOmEmployeeManager().findByOperator(operatorid);
        SystemContext.setOmEmployee(emp);
        Page page = new Page();
        String errMsg = getWorkCardManager().getDispatcher().updateDefaultLastTimeWorker(workCardIds);
        if (errMsg != null ) {
        	page.setSuccess(false);
			page.setErrMsg(errMsg);
		}
        return page;
	}

	/**
	 * 
	 * <li>说明：工长派工、批量工长派工
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param workCardIds 作业卡主键，多个主键以英文,号分隔
	 * @param empids 工人ID集合，多个主键以英文,号分隔
	 * @return page分页对象
	 * @throws Exception
	 */
	public Page saveDispatch(Long operatorid, String workCardIds, String empids) throws Exception{
		AcOperator ac = getAcOperatorManager().getModelById(operatorid);            
        SystemContext.setAcOperator(ac);
        
        OmEmployee emp = getOmEmployeeManager().findByOperator(operatorid);
        SystemContext.setOmEmployee(emp);
        Page page = new Page();
        getWorkCardManager().getDispatcher().updateForemanDispater(workCardIds, empids, operatorid);
        return page;
	}
	
}