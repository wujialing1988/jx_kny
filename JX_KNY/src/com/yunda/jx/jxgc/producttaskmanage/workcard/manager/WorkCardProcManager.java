package com.yunda.jx.jxgc.producttaskmanage.workcard.manager;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.jx.component.manager.OmEmployeeSelectManager;
import com.yunda.jx.jxgc.buildupmanage.manager.BuildUpTypeQueryManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.DetectResult;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardHandle;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask;
import com.yunda.jx.jxgc.producttaskmanage.manager.DetectResultManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkTaskManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkerManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResult;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity.QCResultVO;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultManager;
import com.yunda.jx.webservice.stationTerminal.base.entity.DataItemBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.RdpBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.WaitHandleBean;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkCardBean;
import com.yunda.util.BeanUtils;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 作业工单处理业务类
 * <li>创建人：程锐
 * <li>创建日期：2014-10-8
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "workCardProcManager")
public class WorkCardProcManager extends JXBaseManager<WorkCard, WorkCard> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    
	protected OmEmployeeManager getOmEmployeeManager() {
		return (OmEmployeeManager) Application.getSpringApplicationContext().getBean("omEmployeeManager");
	}

	protected WorkCardManager getWorkCardManager() {
		return (WorkCardManager) Application.getSpringApplicationContext().getBean("workCardManager");
	}
	
	protected OmEmployeeSelectManager getOmEmployeeSelectManager() {
		return (OmEmployeeSelectManager) Application.getSpringApplicationContext().getBean("omEmployeeSelectManager");
	}
	
	protected WorkTaskManager getWorkTaskManager() {
		return (WorkTaskManager) Application.getSpringApplicationContext().getBean("workTaskManager");
	}
	
	protected DetectResultManager getDetectResultManager(){
        return (DetectResultManager)Application.getSpringApplicationContext().getBean("detectResultManager");
    }
	
	protected AcOperatorManager getAcOperatorManager(){
        return (AcOperatorManager)Application.getSpringApplicationContext().getBean("acOperatorManager");
    }
	
	protected WorkerManager getWorkerManager(){
        return (WorkerManager)Application.getSpringApplicationContext().getBean("workerManager");
    }
	
	protected BuildUpTypeQueryManager getBuildUpTypeQueryManager(){
        return (BuildUpTypeQueryManager)Application.getSpringApplicationContext().getBean("buildUpTypeQueryManager");
    }
	
	protected QCResultManager getQCResultManager(){
        return (QCResultManager)Application.getSpringApplicationContext().getBean("qCResultManager");
    }
	
	/**
	 * 
	 * <li>说明：全部完工(最多执行100条)
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param result 质量检验结果值对象数组
     *     [{
     *     		checkItemCode 质检项编码
     *     		qcEmpID 质检人员id
     *     }]
     * @param otherWorkerIDS 其他处理人员的id字符串（,分隔的id字符串） 
	 * @return true 成功 false 失败
	 * @throws Exception
	 */
	public boolean completeAllWorkCard(Long operatorid, QCResultVO[] result, String otherWorkerIDS) throws Exception {
		AcOperator ac = getAcOperatorManager().findLoginAcOprator(operatorid);
        SystemContext.setAcOperator(ac);
        OmEmployee emp = getOmEmployeeSelectManager().findEmpByOperator(operatorid);
        getWorkCardManager().updateCompleteAllWorkCard( emp.getEmpid(), emp.getOrgid(), result, otherWorkerIDS);
        return true;
	}
	
	/**
	 * 
	 * <li>说明：完工
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param idxs 作业卡主键集合作业卡之间用英文逗号分隔例如：“1,2,3”
	 * @param remarks 备注（异常记录）
	 * @param result 质量检验结果值对象数组
     *     [{
     *     		checkItemCode 质检项编码
     *     		qcEmpID 质检人员id
     *     }] 
     * @param otherWorkerIDS 其他处理人员的id字符串（,分隔的id字符串）
	 * @return true 成功 false 失败
	 * @throws Exception
	 */
	public boolean completeWorkCard(Long operatorid, 
								    String idxs, 
								    String remarks,
								    QCResultVO[] result, 
                                    String otherWorkerIDS) throws Exception {
		OmEmployee emp = getOmEmployeeManager().findOmEmployee(operatorid);
		AcOperator ac = getAcOperatorManager().getModelById(operatorid);
		WorkCardManager m = getWorkCardManager();
		idxs = m.filterWorkCard(idxs, emp.getEmpid().toString()); // 过滤不能完工的作业工单
		if (StringUtil.isNullOrBlank(idxs)) {
			return false;
		}
		SystemContext.setAcOperator(ac);
        WorkCardHandle workCardEntity = new WorkCardHandle();
        workCardEntity.setIdx(idxs);
        workCardEntity.setRealBeginTime(new Date());
        workCardEntity.setRealEndTime(new Date());
        workCardEntity.setWorkerID(otherWorkerIDS);
		m.updateCompleteWorkCard(workCardEntity, emp.getEmpid(), result);

		return true;
	}
	
	/**
	 * 
	 * <li>说明：查询检测项列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param workTaskIdx 作业任务IDX
     * @param start 开始行
     * @param limit 每页记录数
	 * @return 检测项分页列表
	 *  标识					数据类型	说明
		detectItemContent	String	检测内容
		isNotBlank			String	是否必填（0是,1否）
		detectResulttype	String	数据类型
		detectResult		String	检测结果
		idx					String	检测项idx
		detectItemStandard	String	检测项标准

	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Page queryDetectResultList(String workTaskIdx, int start, int limit) throws Exception {
		if (workTaskIdx == null) {
			workTaskIdx = "-1";
		}
		DetectResult entity = new DetectResult();
		entity.setWorkTaskIDX(workTaskIdx);
		entity.setRecordStatus(0);
		SearchEntity<DetectResult> searchEntity = new SearchEntity<DetectResult>(entity, start, limit, null);
		Page<DetectResult> page = getDetectResultManager().findPageList(searchEntity);
		List<DataItemBean> dataItemList = new ArrayList<DataItemBean>();
		dataItemList = BeanUtils.copyListToList(DataItemBean.class, page.getList());
		Page<DataItemBean> pageList = new Page<DataItemBean>(page.getTotal(), dataItemList);
		return pageList;
	}
	/**
	 * TODO V3.2.1代码重构
	 * <li>说明：查询生产任务单列表【待处理】
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
     * @param start 开始行
     * @param limit 每页记录数
	 * @return 生产任务单分页列表
	 *  标识				数据类型	说明
		idx				String	兑现单主键
		rdpText			String	兑现单内容
		trainTypeIDX	String	车型主键
		trainNo			String	车号
		buildUpTypeIDX	String	组成主键

	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Page queryHandlerRdpList(Long operatorid, int start, int limit) throws Exception {
		OmEmployee emp = getOmEmployeeSelectManager().findEmpByOperator(operatorid);
		SystemContext.setOmEmployee(emp);
//		Map<String, Object> map = getTrainEnforcePlanRdpManager().getComboDataByWorkCardQuery(start, limit, 1);
        Map<String, Object> map = new HashMap<String, Object>();
		// 封装返回值
		List<RdpBean> beanList = new ArrayList<RdpBean>();
		beanList = BeanUtils.copyListToList(RdpBean.class, (List) map.get("root"));
		Page pageList = new Page((Integer) (map.get("totalProperty")), beanList);
		return pageList;
	}
	/**
	 * TODO V3.2.1代码重构
	 * <li>说明：查询检修活动列表【待处理】
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param rdpIdx 生产任务单IDX
     * @param start 开始行
     * @param limit 每页记录数
	 * @return 检修活动列表
	 *  标识				数据类型	说明
		idx				String  检修活动IDX
		activityName	String	检修活动名称

	 */
	public Map<String, Object> queryHandlerRepairActivityList(Long operatorid, String rdpIdx, int start, int limit) {
		OmEmployee emp =  getOmEmployeeSelectManager().findEmpByOperator(operatorid);
        SystemContext.setOmEmployee(emp);
//        return getRepairActivityManager().getComboDataByWorkCardQuery(rdpIdx, start, limit, 1);
        return new HashMap<String, Object>();
	}
	/**
	 * TODO V3.2.1代码重构
	 * <li>说明：查询工艺节点列表【待处理】
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param rdpIdx 生产任务单IDX
	 * @param parentIdx 父节点IDX
	 * @return 工艺节点列表
	 * 标识			数据类型	说明
		id			String	工艺节点idx
		text		String	工艺节点名称
		leaf		boolean	是否子节点 true 是 false 否
		parentIdx	String	父节点idx

	 */
	public List<HashMap> queryHandlerTecNodeList(Long operatorid, String rdpIdx, String parentIdx) {
		OmEmployee emp = getOmEmployeeSelectManager().findEmpByOperator(operatorid);
		SystemContext.setOmEmployee(emp);
//		List<HashMap> list = getTecProcessNodeCaseManager().findTecNodeCaseTreeByWorkQuery(parentIdx, rdpIdx, 1);
		return new ArrayList<HashMap>();
	}
	/**
	 * 
	 * <li>说明：查询待处理作业工单列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchString 查询字符串 格式如：,"rdpIDX":"8a828464490303dc01490834ad110280"
	 *  标识					数据类型	是否必填	说明
		rdpIDX				String		否	生产任务单IDX
		nodeCaseIDX			String		否	工艺节点IDX
		repairActivityIDX	String		否	检修活动IDX
		fixPlaceFullName	String		否	位置，机车组成树名称
		workCardName		String		否	作业工单名称
	 * @param operatorid 操作员ID
     * @param start 开始行
     * @param limit 每页记录数
	 * @return 待处理作业工单列表
	 *  标识						数据类型	说明
		idx						String	作业工单idx
		workCardName			String	作业卡名称
		trainSortName			String	车型（配件型号）
		trainNo					String	车号（配件号）
		fixPlaceFullName		String	位置
		repairClassRepairTime	String	修程修次
		nodeCaseName			String	节点名称
		repairActivityName		String	活动名称
		batch					String	是否批量完工标识
		planBeginTimeStr		String	计划开始时间
		planEndTimeStr			String	计划完成时间	

	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Page queryHandlerWorkCardList(String searchString, 
										 Long operatorid, 
										 int start, 
										 int limit) throws Exception {
		OmEmployee emp = getOmEmployeeManager().findOmEmployee(operatorid);
		String json = "{\"workStationBelongTeam\":\"" + emp.getOrgid() + "\"" + searchString + "}";

		Page<WorkCard> page = getWorkCardManager().findWorkCardByWork(json, start, limit, null, emp.getEmpid());
		List<WaitHandleBean> list = new ArrayList<WaitHandleBean>();
		list = BeanUtils.copyListToList(WaitHandleBean.class, page.getList());
		Page pageList = new Page(page.getTotal(), list);
		return pageList;
	}
	/**
	 * TODO V3.2.1代码重构
	 * <li>说明：查询生产任务单列表【待领取】
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
     * @param start 开始行
     * @param limit 每页记录数
	 * @return 生产任务单列表【待领取】
	 *  标识				数据类型	说明
		idx				String	兑现单主键
		rdpText			String	兑现单内容
		trainTypeIDX	String	车型主键
		trainNo			String	车号
		buildUpTypeIDX	String	组成主键 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Page queryReceiveRdpList(Long operatorid, int start, int limit) throws Exception {
		OmEmployee emp = getOmEmployeeSelectManager().findEmpByOperator(operatorid);
		SystemContext.setOmEmployee(emp);
//		Map<String, Object> map = getTrainEnforcePlanRdpManager().getComboDataByWorkCardQuery(start, limit, 0);
		Map<String, Object> map = new HashMap<String, Object>();
		// 封装返回值
		List<RdpBean> beanList = new ArrayList<RdpBean>();
		beanList = BeanUtils.copyListToList(RdpBean.class, (List) map.get("root"));
		Page pageList = new Page((Integer) (map.get("totalProperty")), beanList);
		return pageList;
	}
	/**
	 * TODO V3.2.1代码重构
	 * <li>说明：查询检修活动列表【待领取】
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param rdpIdx 生产任务单IDX
     * @param start 开始行
     * @param limit 每页记录数
	 * @return 检修活动列表【待领取】
	 *  标识				数据类型	说明
		idx				String  检修活动idx
		activityName	String	检修活动名称
	 */
	public Map<String, Object> queryReceiveRepairActivityList(Long operatorid, String rdpIdx, int start, int limit) {
		OmEmployee emp =  getOmEmployeeSelectManager().findEmpByOperator(operatorid);
        SystemContext.setOmEmployee(emp);
//        return getRepairActivityManager().getComboDataByWorkCardQuery(rdpIdx, start, limit, 0);
        return new HashMap<String, Object>();
	}
	/**
	 * TODO V3.2.1代码重构
	 * <li>说明：查询工艺节点列表【待领取】
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param rdpIdx 生产任务单IDX
	 * @param parentIdx 父节点IDX
	 * @return 工艺节点列表【待领取】
	 * 标识			数据类型	说明
		id			String	工艺节点idx
		text		String	工艺节点名称
		leaf		boolean	是否子节点 true 是 false 否
		parentIdx	String	父节点idx
	 */
	public List<HashMap> queryReceiveTecNodeList(Long operatorid, String rdpIdx, String parentIdx) {
		OmEmployee emp =  getOmEmployeeSelectManager().findEmpByOperator(operatorid);
        SystemContext.setOmEmployee(emp);
//	    List<HashMap> list = getTecProcessNodeCaseManager().findTecNodeCaseTreeByWorkQuery(parentIdx, rdpIdx, 0);
	    return new ArrayList<HashMap>();
	}
	/**
	 * V3.2.1删除
	 * <li>说明：查询待领取作业工单列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchString 查询字符串 格式如：,"rdpIDX":"8a828464490303dc01490834ad110280"
	 *  标识					数据类型	是否必填	说明
		rdpIDX				String		否	生产任务单IDX
		nodeCaseIDX			String		否	工艺节点IDX
		repairActivityIDX	String		否	检修活动IDX
		fixPlaceFullName	String		否	位置，机车组成树名称
		workCardName		String		否	作业工单名称

	 * @param operatorid 操作员ID
     * @param start 开始行
     * @param limit 每页记录数
	 * @return 待领取作业工单列表
	 *  标识						数据类型	说明
		idx						String	作业工单idx
		workCardName			String	作业卡名称
		trainSortName			String	车型（配件型号）
		trainNo					String	车号（配件号）
		fixPlaceFullName		String	位置
		repairClassRepairTime	String	修程修次
		nodeCaseName			String	节点名称
		repairActivityName		String	活动名称

	 * @throws Exception
	 */
//	@SuppressWarnings("unchecked")
//	public Page queryReceiveWorkCardList(String searchString, 
//										 Long operatorid, 
//										 int start, 
//										 int limit) throws Exception {
//		OmEmployee emp = getOmEmployeeManager().findOmEmployee(operatorid);
//		String json = "{\"status\":\"#[k]#$[IN]$" + Worker.STATUS_WAITINGFORHANDLE + "]|[" + Worker.STATUS_WAITINGFORGET + "\",\"status\":\"$[OR]$"
//				+ WorkCard.STATUS_OPEN + "]|[" + WorkCard.STATUS_HANDLING + "\",\"workStationBelongTeam\":\"" + emp.getOrgid() + "\"" + searchString
//				+ "}";		
//		Page<WorkCard> page = getWorkCardManager().findWorkCardByWork(json, start, limit, null, emp.getEmpid());
//		List<WaitReceiveBean> workCardList = new ArrayList<WaitReceiveBean>();
//		workCardList = BeanUtils.copyListToList(WaitReceiveBean.class, page.getList());
//		Page packList = new Page<WaitReceiveBean>(page.getTotal(), workCardList);
//		return packList;
//	}
	/**
	 * 
	 * <li>说明：查询作业工单信息
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param idx 作业工单IDX
	 * @return 作业工单信息
	 *  标识					数据类型	说明
		nodeCaseName		String	工艺节点名称
		partsName			String	零部件名称
		nameplateNo			String	零部件编号
		safeAnnouncements	String	安全注意事项
		ratedWorkHours		String	工时
		workSeqClass		String	检修类型
		partsName			String	零部件名称
		fixPlaceFullName	String	位置
		remarks				String	描述
		fixPlaceIDX			String	零部件IDX
		planTrainTimeStr	String	计划交车时间
		transinTimeStr		String	计划开始时间
		specificationModel	String	规格型号
		workCardName		String	作业工单名称
		realBeginTimeStr	String	实际开始时间
		realEndTimeStr		String	实际结束时间
 
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public WorkCardBean queryWorkCardInfo(String idx) throws IllegalAccessException, InvocationTargetException {
		WorkCardManager m = getWorkCardManager();
		WorkCard workCard = m.getModelById(idx);
		String[] val = m.getInfo(idx);
		workCard.setTransinTimeStr(val[0]);
		workCard.setPlanTrainTimeStr(val[1]);
		workCard.setRepairActivityTypeName("检修活动");
		workCard.setRepairActivityName(val[3]);
		workCard.setNodeCaseName(val[4]);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		if (workCard.getRealBeginTime() != null) {
			workCard.setRealBeginTimeStr(sdf.format(workCard.getRealBeginTime()));
		}
		if (workCard.getRealEndTime() != null) {
			workCard.setRealEndTimeStr(sdf.format(workCard.getRealEndTime()));
		}
		WorkCardBean workCardBean = new WorkCardBean();
		BeanUtils.copyProperties(workCardBean, workCard);
		workCardBean.setNodeCaseName(val[4]);
		return workCardBean;
	}
	/**
	 * 
	 * <li>说明：查询未处理作业任务列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
     * <li>修改人：何涛
     * <li>修改日期：2016-04-14
     * <li>修改内容：优化代码，删除无用的operatorId参数
	 * @param workCardIdx 作业工单IDX
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @return 未处理作业任务列表
	 	标识					数据类型	说明
		workTaskName		String	检测项名称
		repairStandard		String	技术要求或标准规定
		idx					String	作业任务IDX
		workStepIDX			String	检测结果idx
		repairResult		String	结果
		remarks				String	备注
		mutualCheckPerson	String	互检员
		checkPerson			String	检查员
		spotCheckPerson		String	抽检


	 * @throws Exception
	 */
	public Page queryHandlerWorkTaskList(String workCardIdx,  int start,  int limit) {
	    // 查询未处理
        String[] statusArray = new String[]{ WorkTask.STATUS_WAITINGFORGET, WorkTask.STATUS_INIT };
        /** 返回结果包装：将查询对象page中的集合数据用指定的WorkTaskBean对象进行封装 */
        return getWorkTaskManager().pageQuery2(workCardIdx, statusArray, start, limit, null);
	}
	/**
	 * 
	 * <li>说明：查询已处理作业任务列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
     * <li>修改人：何涛
     * <li>修改日期：2016-04-14
     * <li>修改内容：优化代码，删除无用的operatorId参数
	 * @param workCardIdx 作业工单IDX
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @return 已处理作业任务列表
	 	标识					数据类型	说明
		workTaskName		String	检测项名称
		repairStandard		String	技术要求或标准规定
		idx					String	作业任务IDX
		workStepIDX			String	检测结果idx
		repairResult		String	结果
		remarks				String	备注
		mutualCheckPerson	String	互检员
		checkPerson			String	检查员
		spotCheckPerson		String	抽检
	 * @throws Exception
	 */
	public Page queryHandledWorkTaskList(String workCardIdx, int start, int limit) {
        // 查询已处理
        String[] statusArray = new String[]{ WorkTask.STATUS_HANDLED };
        /** 返回结果包装：将查询对象page中的集合数据用指定的WorkTaskBean对象进行封装 */
        return getWorkTaskManager().pageQuery2(workCardIdx, statusArray, start, limit, null);
	}
    
	/**
	 * <li>说明：保存检测项
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param idx 数据项主键
	 * @param value 数据项录入值
	 * @throws Exception
	 */
	public void saveDetectResult(Long operatorid, String idx, String value) throws Exception {
		getDetectResultManager().updateDetectResult(idx, value, operatorid);
	}
	/**
	 * 
	 * <li>说明：保存检测项并完成作业任务
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param data 操作数据数组(字符串数组，字符串格式为“idx值,结果值”，例如：“aa,22”。第一个值是数据项id，第二个值是数据项结果值)
	 * @param taskIdx 作业任务主键
	 * @param result 检修结果字段值
	 * @param remarks 检修备注字段值
	 * @throws Exception
	 */
	public void saveDetectResultAndWorkTask(Long operatorid,
											String[] data, 
											String taskIdx, 
											String result, 
											String remarks) throws Exception {
		String[] item = null;
        AcOperator ac = getAcOperatorManager().getModelById(operatorid);
        SystemContext.setAcOperator(ac);
        for(int i = 0; i < data.length; i++){
            item = data[i].split("\\,",2);
            getDetectResultManager().updateDetectResult(item[0], item[1], operatorid);
        }
        WorkTaskManager m = getWorkTaskManager();
        WorkTask t = m.getModelById(taskIdx);
        t.setRepairResult(result);
        t.setRemarks(remarks);
        t.setUpdator(operatorid);
        t.setStatus(WorkTask.STATUS_HANDLED);
        t.setUpdateTime(new Date());
        m.update(t);
	}
	/**
	 * V3.2.1删除
	 * <li>说明：领取全部作业工单
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param searchJson
	 *   名称				类型		说明
		“rdpIDX”			String	兑现单主键
		“repairActivityIDX”	String	检修活动主键
		“workCardName”		String	作业工单名称(全模糊匹配)
		“fixPlaceFullName”	String	安装位置全名(半模糊匹配)
		“nodeCaseIDX”		String	工艺节点实例主键

	 * @throws Exception
	 */
//	public void updateReceiveAllWorkCard(Long operatorid, String searchJson) throws Exception {
//		OmEmployee emp = getOmEmployeeSelectManager().findEmpByOperator(operatorid);
//		if (searchJson.indexOf(",") != -1) {
//			searchJson = searchJson.replaceFirst(",", "{").replace("'", "\"").replaceFirst("workCardName", "\"workCardName\"");
//		} else {
//			searchJson = "{";
//		}
//		searchJson += "}";
//		getWorkCardManager().updateBeginAllWorkCard(emp.getEmpid(), emp.getOrgid(), searchJson);
//	}
	/**
	 * V3.2.1删除
	 * <li>说明：领取作业工单
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作员ID
	 * @param idxs 领取的作业卡ID集合idx之间用英文逗号分隔 例如：“1,2,3”
	 * @throws Exception
	 */
//	public void updateReceiveWorkCard(Long operatorid, String idxs) throws Exception {
//		if (!StringUtil.isNullOrBlank(idxs)) {
//			OmEmployee emp = getOmEmployeeManager().findOmEmployee(operatorid);
//			idxs = idxs.replace(",", "','");
////			getWorkCardManager().updateReceiveWorkCard(idxs, emp.getEmpid());
//			getWorkCardManager().updateOngoingWorkCard(idxs, emp.getEmpid());
//		}
//	}
	/**
	 * 
	 * <li>说明：根据作业工单idx获取需要指派的质量检查项列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param workCardIDXS 多个idx用,分隔
	 * @return  质量检查项列表
	 * [{
	 * 		checkItemCode //质检项编码
	 * 		checkItemName //质检项名称
	 * }]
	 */
	public List<QCResult> getIsAssignCheckItems(String workCardIDXS) throws Exception {
		return getQCResultManager().getIsAssignCheckItems(workCardIDXS);
	}
	/**
	 * 
	 * <li>说明：获取当前操作者全部完工时需要指派的质量检查项列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作者ID
	 * @return  质量检查项列表
	 * [{
	 * 		checkItemCode //质检项编码
	 * 		checkItemName //质检项名称
	 * }]
	 */
	public List<QCResult> getIsAssignCheckItemsForAllComplete(Long operatorid) throws Exception {
		return getQCResultManager().getIsAssignCheckItems(getWorkCardIDXSForAllComplete(operatorid));
	}
	/**
	 * 
	 * <li>说明：获取当前操作者全部完工的工单idx字符串（以,号分隔）
	 * <li>创建人：程锐
	 * <li>创建日期：2014-11-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatorid 操作者id
	 * @return  当前操作者全部完工的工单idx字符串（以,号分隔）
	 */
	@SuppressWarnings("unchecked")
	public String getWorkCardIDXSForAllComplete(Long operatorid) throws Exception {
		OmEmployee emp = getOmEmployeeSelectManager().findEmpByOperator(operatorid);
		
		String sql = SqlMapUtil.getSql("jxgc-gdgl:findWaitReceiveIdx")
						        .replace("开放", WorkCard.STATUS_OPEN)
						        .replace("处理中", WorkCard.STATUS_HANDLING)
						        .replace("班组", emp.getOrgid().toString())
						        .replace("人员", emp.getEmpid().toString());
		
		String s = SqlMapUtil.getSql("jxgc-gdgl3:filterWork")
							 .replace("工单主键", sql)
							 .replace("待领取", WorkTask.STATUS_WAITINGFORGET);
        List<Object[]> list = daoUtils.executeSqlQuery(s);
        //记录可以完工的工单主键 
        StringBuffer idxs = new StringBuffer(); 
        //a未完成的检测/修项目数量; b未完成的检测/修项目有需要录入检测项数据的数量;c有默认结果的检测/修项目数量
        int a, b, c;    
        int cpleCount = 0; //可以完成的数量
        for (Object[] o : list) {
			a = Integer.parseInt(o[1].toString());
			b = Integer.parseInt(o[2].toString());
			c = Integer.parseInt(o[3].toString());
			if (a != 0) { // 当a不等于0， 即有未完成的检测/修项目
				// 当c（有默认结果）不等于a（未完成的检测/修项目数量）
				// b(需录入检测项)不等于0，即有未完成的有录入数据项的检测/修项目
				if ((c != a) || b != 0) {
					continue;
				}
			}
			idxs.append(o[0] + ",");
			cpleCount++;// 记录可以完成的数量
			if (cpleCount == 100) { // 最多执行一百条，多余的不执行
				break;
			}
		}
//        logger.info("【完成工单的数量：】" + cpleCount);
        if (idxs.length() == 0) {
			return "";// 返回结果用于页面提示
		} else {
			idxs.deleteCharAt(idxs.length() - 1);
		}
        return idxs.toString();
	}
}