package com.yunda.webservice.device.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.jx.jxgc.dispatchmanage.entity.WorkStation;
import com.yunda.jx.jxgc.producttaskmanage.entity.DetectResult;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkTask;
import com.yunda.jx.jxgc.producttaskmanage.entity.Worker;
import com.yunda.jx.jxgc.producttaskmanage.manager.DetectResultManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkTaskManager;
import com.yunda.jx.jxgc.workplanmanage.manager.NodeRunner;
import com.yunda.webservice.device.entity.DetectData;
import com.yunda.webservice.device.entity.DetectDatas;
import com.yunda.webservice.device.entity.Parameter;
import com.yunda.webservice.device.entity.Parameters;
import com.yunda.webservice.device.entity.QualityCheckItem;
import com.yunda.webservice.device.entity.WorkOrder;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkOrder业务类,设备接口-工单维护
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-01-06
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="workOrderManager")
public class WorkOrderManager extends JXBaseManager<WorkOrder, WorkOrder>{
	/**作业工单业务类（作业卡）*/
	@Resource
	private WorkCardManager workCardManager ;
	/**作业任务业务类（作业项）*/
	@Resource
	private WorkTaskManager workTaskManager ;
	/**检测结果业务类（检测项）*/
	@Resource
	private DetectResultManager detectResultManager ;
	/**操作员业务类*/
	@Resource
    private AcOperatorManager acOperatorManager;
	
	/**********下载方法处理逻辑开始*****************************************************/
	
	/**
	 * <li>说明：返回工单请求结果
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param nameplateNo：配件编号（铭牌号）
	 * @param detectorCode：机务设备编码
	 * @return String 返回工单XML字符串
	 * @throws BusinessException
	 */
	public String responseWorkOrder(String nameplateNo , String detectorCode) throws BusinessException{
		//定义XML文档根节点
		DetectDatas dds = null;
		List<DetectData> detectDataList =  new ArrayList<DetectData>(); 
		//第一步通过配件编号（铭牌号）查询兑现单
        //V3.2.1代码重构
//		PartsEnforcePlanRdp rdp =  this.findPartsRdp(nameplateNo);
        
		//第二步通过机务设备编码查询流水线工位信息
		List<WorkStation> workStationList = findWorkStation(detectorCode);
		String workStationIds = "" ;
		for(WorkStation ws : workStationList){
			if("".equals(workStationIds)){
				workStationIds = "'"+ws.getIdx()+"'";
			}else{
				workStationIds = workStationIds + ",'"+ws.getIdx()+"'";
			}
		}
		//第三步通过兑现单和流水线工位信息查询派工到当前机务设备上的作业工单信息
//		if(!StringUtil.isNullOrBlank(rdp.getIdx()) && !"".equals(workStationIds)){
//			List<WorkCard> workCardList = this.findWorkCardList(rdp.getIdx(), workStationIds);
//			//第四步通过作业工单查询作业任务下关联的检测项信息
//			for(WorkCard card : workCardList){
//				List<WorkTask> workTaskList = findWorkTaskList(card.getIdx());
//				String workTaskIds = "" ;
//				for(WorkTask task : workTaskList){
//					if("".equals(workTaskIds)){
//						workTaskIds = "'"+task.getIdx()+"'";
//					}else{
//						workTaskIds = workTaskIds + ",'"+task.getIdx()+"'";
//					}
//				}
//				List<DetectResult> detectResultList = null;
//				List<QualityControlResult> qualityList = null ;
//				if(!"".equals(workTaskIds)){
//					//第五步通过作业任务信息查询检测项信息
//					detectResultList = this.findDetectResultList(workTaskIds); //一个工单下的所有检测项
//					//第六步通过作业工单主键和作业工单对应的作业任务主键查询质量控制信息（取并集）
//					qualityList = this.findQualityInfoList(workTaskIds+",'"+card.getIdx()+"'");
//				}
//				//第七步，整合以上作业工单、作业任务、检测项信息、质量控制信息，打包后解析成XML字符串格式返回
//				DetectData dd = this.buildUpXMLBean(nameplateNo, detectorCode, card, detectResultList, qualityList);
//				if(dd !=  null){
//					detectDataList.add(dd);
//				}
//			}
//			//循环完成当前工位上的所有工单信息后，添加到映射XML文档对象节点中
//			if(detectDataList != null && detectDataList.size() > 0){
//				dds = new DetectDatas(); //初始化XML文档根节点
//				dds.setDetectDataList(detectDataList);
//			}
//		}
		if(dds != null){
			return dds.toResponseDataXML();
		}else{
			return "机务设备编号为：《"+detectorCode+"》上没有对应的作业对象《"+nameplateNo+"》的工单。";
		}
	}
	/**
	 * <li>说明：返回心跳包请求结果
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param detectorCode：机务设备编码
	 * @return String 解析成功后的XML字符串
	 * @throws BusinessException
	 */
	public String responseHeartBeat(String detectorCode) throws BusinessException{
		DetectDatas dds = new DetectDatas();
		List<DetectData> detectDataList = new ArrayList<DetectData>(); 
		DetectData dd = new DetectData();
		dd = new DetectData(); //初始化XML返回对象
		dd.setDataType(DetectData.RESPONSE_HEARTBEAT); //心跳包数据类型
		dd.setEdition(DetectData.EDITION); //版本号
		dd.setDetectorCode(detectorCode); //机务设备编码
		dd.setState(DetectData.DATA_ACCEPT+"");
		detectDataList.add(dd);
		dds.setDetectDataList(detectDataList);
		return dds.toResponseDataXML();
	}
	/**
	 * <li>说明：返回一卡通操作结果
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param detectorCode：机务设备编码
	 * @param materielCode：作业对象编码（此处为一卡通）
	 * @return String XML字符串
	 * @throws 抛出异常列表
	 */
	public String responseOneCardPass(String materielCode, String detectorCode) throws BusinessException{
		DetectDatas dds = new DetectDatas();
		List<DetectData> detectDataList = new ArrayList<DetectData>(); 
		DetectData dd = new DetectData();
		dd = new DetectData(); //初始化XML返回对象
		dd.setDataType(DetectData.RESPONSE_ONE_CARD_PASS); //一卡通权限认证返回类型
		dd.setEdition(DetectData.EDITION); //版本号
		dd.setDetectorCode(detectorCode); //机务设备编码
		List<OmEmployee> empList = getOmEmployeeByWorkerCode(materielCode);
		if(empList != null && empList.size() > 0){
			dd.setState(""+DetectData.DATA_ACCEPT); //0表示一卡通认证成功
		}else{
			dd.setState(""+DetectData.DATA_FAIL);//1表示一卡通认证失败
		}
		detectDataList.add(dd);
		dds.setDetectDataList(detectDataList);
		return dds.toResponseDataXML();
	}
	
	/**
     * TODO V3.2.1代码重构
	 * <li>说明：通过铭牌号查询配件兑现单(状态为：新兑现、处理中)
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
//	@SuppressWarnings("unchecked")
//	private PartsEnforcePlanRdp findPartsRdp(String nameplateNo) throws BusinessException{
//		String hql = "From PartsEnforcePlanRdp where recordStatus=0 and nameplateNo = ? " +
//				" and billStatus in ('"+PartsEnforcePlanRdp.STATUS_NEW+"','"+PartsEnforcePlanRdp.STATUS_HANDLING+"')" ;
//		PartsEnforcePlanRdp rdp = (PartsEnforcePlanRdp)this.daoUtils.findSingle(hql, new Object[]{nameplateNo});
//		return rdp;
//	}
	/**
	 * <li>说明：通过机务设备主键查询所有机务设备所对应的工位。
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param detectorCode：机务设备编码
	 * @return List<WorkStation>
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private List<WorkStation> findWorkStation(String detectorCode)throws BusinessException{
//		EquipInfo info = this.getEquipInfo(detectorCode) ;
//		String hql = "From WorkStation where recordStatus=0 and equipIDX = '"+info.getIdx()+"'";
//		List<WorkStation> workStationList = this.daoUtils.find(hql);
//		return workStationList;
        return null;
	}
	/**
	 * <li>说明：通过兑现单主键和工位主键，查询请求的作业对象所在工位有哪些初始化、开放，处理中的兑现单
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param rdpIdx：兑现单主键
	 * @param workStationIds：工位主键（可能是'aaaaa','bbbbb'类型）
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	private List<WorkCard> findWorkCardList(String rdpIdx ,String workStationIds) throws BusinessException{
		StringBuffer hql = new StringBuffer();
		hql.append("From WorkCard where recordStatus=0 ");
		hql.append(" and rdpIDX = '").append(rdpIdx).append("'");
		hql.append(" and workStationIDX in (").append(workStationIds).append(")");
		hql.append(" and status in ('")/*.append(WorkCard.STATUS_NEW).append("','")*/.append(WorkCard.STATUS_OPEN).append("','");
		hql.append(WorkCard.STATUS_HANDLING).append("')");
		return this.daoUtils.find(hql.toString());
	} 
	/**
	 * <li>说明：通过作业工单主键查询作业任务集合
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param workCardIdx：作业工单主键
	 * @return List<WorkTask>
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private List<WorkTask> findWorkTaskList(String workCardIdx) throws BusinessException{
		StringBuffer hql = new StringBuffer();
		hql.append("From WorkTask where recordStatus = 0 and workCardIDX = '").append(workCardIdx).append("'");
		hql.append(" and status in ('").append(WorkTask.STATUS_INIT).append("','");
		hql.append(WorkTask.STATUS_WAITINGFORGET).append("','").append(WorkTask.STATUS_WAITINGFORHANDLE).append("')");
		return this.daoUtils.find(hql.toString());
	}
	/**
     * TODO V3.2.1代码重构
	 * <li>说明：通过关联主键（作业工单主键和作业任务主键集合）查询质量信息的并集
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param relationIds：作业工单主键和作业任务主键集合
	 * @return List<QualityControlResult>
	 * @throws BusinessException
	 */
//	@SuppressWarnings("unchecked")
//	private List<QualityControlResult> findQualityInfoList(String relationIds) throws BusinessException{
//		StringBuffer sql = new StringBuffer();
//		sql.append("select t.check_item_code as \"checkItemCode\",t.check_item_name as \"checkItemName\" ");
//		sql.append("from JXGC_Quality_Control_Result  t ");
//		sql.append("where t.record_status=0 and t.relation_idx in (").append(relationIds).append(")");
//		sql.append("group by t.check_item_code,t.check_item_name");
//		List<QualityControlResult> qualityList =  this.daoUtils.executeSqlQueryEntity(sql.toString(), QualityControlResult.class);
//		return qualityList;
//	}
	/**
     * TODO V3.2.1代码重构
	 * <li>说明：方法实现功能说明
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
//	private DetectData buildUpXMLBean(String nameplateNo , String detectorCode,WorkCard card , 
//			List<DetectResult> detectResultList , List<QualityControlResult> qualityList)throws BusinessException{
//		DetectData dd = null; //定义XML返回对象
//		if(card != null){
//			//设置工单信息
//			dd = new DetectData(); //初始化XML返回对象
//			dd.setDataType(DetectData.RESPONSE_WORKORDER); //作业工单请求返回
//			dd.setEdition(DetectData.EDITION); //版本号
//			dd.setMaterielCode(nameplateNo); //作业对象编码
//			dd.setDetectorCode(detectorCode); //机务设备编码
//			dd.setWorkOrderCode(card.getIdx()); //工单编码（即工单主键）
//			StringBuffer describe = new StringBuffer();
//			describe.append("工单名称：").append(card.getWorkCardName()).append("/n");
//			describe.append("作业工位：").append(card.getWorkStationName()).append("/n");
//			describe.append("作业班组：").append(card.getWorkStationBelongTeamName()).append("/n");
//			if(card.getRatedWorkHours() != null)
//			    describe.append("额定工时（单位：分钟）：").append(card.getRatedWorkHours()).append("/n");
//			if(card.getDutyPersonName() != null){
//				describe.append("工位负责人名称：").append(card.getDutyPersonName()).append("/n");
//			}
//			dd.setWorkOrderDescribe(describe.toString()); //工单描述
//			//dd.setBeginDate(DateTimeUtil.getDateTime(card.getPlanBeginTime(),null)); //计划开工时间
//			//dd.setEndDate(DateTimeUtil.getDateTime(card.getPlanEndTime(),null)); //计划完工时间
//			dd.setPlanBeginDate(DateTimeUtil.getDateTime(card.getPlanBeginTime(), null));
//			dd.setPlanEndDate(DateTimeUtil.getDateTime(card.getPlanEndTime(), null));
//			if(detectResultList != null && detectResultList.size() > 0){
//				//开始设置检测项信息
//				Parameters params = new Parameters(); //初始化检测项根节点
//				List<Parameter> paramList = new ArrayList<Parameter>();
//				for(DetectResult dr : detectResultList){
//					Parameter param = new Parameter(); //初始化Parameter节点
//					param.setCode(dr.getDetectItemCode()); //检测项编码
//					param.setName(dr.getDetectItemContent());//检测名称（内容）
//					param.setUnit(dr.getDetectResulttype()); //检测结果类型
//					param.setDescribe(dr.getDetectItemStandard()); //检测描述
////					param.setValue() ;  返回工单不存在值
//					paramList.add(param);
//				}
//				if(paramList != null && paramList.size() > 0){ //存在检测结果时添加节点
//					params.setParameters(paramList); //添加节点
//					dd.setParameters(params); //添加节点
//				}
//			}
//			if(qualityList != null && qualityList.size() > 0){
//				//开始设置质量检查信息
//				QualityCheckList qualityCheckList = new QualityCheckList(); //初始化质量检查根节点
//				List<QualityCheckItem> qualityCheckItemList = new ArrayList<QualityCheckItem>();
//				for(QualityControlResult qc : qualityList){
//					QualityCheckItem item = new QualityCheckItem(); //初始化QualityCheckItem节点
//					item.setCheckItemName(qc.getCheckItemName());
//					item.setCheckItemCode(qc.getCheckItemCode());
//					qualityCheckItemList.add(item);
//				}
//				if(qualityCheckItemList != null && qualityCheckItemList.size() > 0){//存在质量检查时添加节点
//					qualityCheckList.setQualityCheckList(qualityCheckItemList); 
//					dd.setQualityCheckList(qualityCheckList); 
//				}
//			}
//		}
//		
//		return dd;
//	}
	
	/**************上传方法处理逻辑开始*************************************************/
	
	/**
	 * <li>说明：接收机务设备上传工单处理结果请求，并处理请求。
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param DetectData : dd 接收的XML解析对象
	 * @return void
	 * @throws Exception 
	 */
	public void requestWorkOrder(DetectData dd) throws Exception{
		//获取作业人员信息、操作员信息，并设置到session中。
		setSeesionValue(dd.getWorkerCode());
		//获取工单信息
		WorkCard card = workCardManager.getModelById(dd.getWorkOrderCode()); 
		//通过作业工单获取作业任务和检测项目信息
		if(card != null){
			if(WorkCard.STATUS_HANDLING.equals(card.getStatus()) || WorkCard.STATUS_OPEN.equals(card.getStatus())){
				handleDetectResult(card , dd.getParameters() , dd) ; //处理检测项
				//获取操作人员ID
				//String empIds = getOmEmployeeIds(dd.getWorkerCode());
				//调用完成工单处理操作(目前仅支持单个人操作作业工单)
				//TODO 完工重构
				/*QCResultVO[] result = null;
				this.workCardManager.updateCompleteWorkCard(card.getIdx(), 
															dd.getBeginDate(), 
															dd.getEndDate(),
															dd.getResult(), 
															SystemContext.getOmEmployee().getEmpid(), 
															null, 
															true, 
															result); //需要分号标记*/
				List<OmEmployee> empList = getOmEmployeeByWorkerCode(dd.getWorkerCode());
				 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for(OmEmployee emp : empList){
                    String sql = "select worker_id from jxgc_worker " + 
                                 " where work_card_idx = '" + card.getIdx() + "' and worker_id = " + emp.getEmpid();
                    List list = daoUtils.executeSqlQuery(sql);
				    if(list == null || list.size() < 1){  //没有人员则新增施工人员
				        Worker worker = new Worker();
				        worker.setWorkCardIDX(card.getIdx());
				        worker.setWorkerID(emp.getEmpid());
				        worker.setWorkerName(emp.getEmpname());
				        worker.setWorkerCode(emp.getEmpcode());
				        worker.setWorkerTreamIDX(emp.getOrgid());
				        daoUtils.saveOrUpdate(worker);
				    }
				}
				if(card.getRealBeginTime() == null)
				    card.setRealBeginTime(sdf.parse(dd.getBeginDate()));
				if(card.getRealEndTime() == null)
				    card.setRealEndTime(sdf.parse(dd.getEndDate()));
//				card.setSystemEndTime(new java.util.Date());
				card.setJudgment(dd.getResult());
				card.setStatus(WorkCard.STATUS_HANDLED);
				this.workCardManager.saveOrUpdate(card);    //更新作业工单
				if(StringUtil.nvl(card.getNodeCaseIDX()).equals("") == false)
				    NodeRunner.runner(card.getNodeCaseIDX());//完成工序
			}
		}
	}
	/**
     * TODO V3.2.1代码重构
	 * <li>说明：接收机务设备上传质量检查数据处理结果请求，并处理请求。
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param DetectData : dd 接收的XML解析对象
	 * @return void
	 * @throws Exception 
	 */
	public void requestQualityCheck(DetectData dd) throws Exception{
		//获取作业人员信息、操作员信息，并设置到session中。
		setSeesionValue(dd.getWorkerCode());
		//获取工单信息
		WorkCard card = workCardManager.getModelById(dd.getWorkOrderCode()); 
		//查询当前操作员下所有质量检查项。
		if(card != null){
			if(WorkCard.STATUS_HANDLING.equals(card.getStatus()) || WorkCard.STATUS_OPEN.equals(card.getStatus())){
				// 根据作业卡获取该作业卡下属所有的质量控制信息
//				Map<String, List<QualityControlResult>> map = this.getQualityControlByWorkCard(card);
				// 获取上传的所有质量检查项目
				List<QualityCheckItem> qualityCheckList = dd.getQualityCheckList().getQualityCheckList();
				// 声明一个【检验信息】实体
//				QualityControlCheckInfo info = null;
//				// 声明一个【检验信息】实体集合
//				List<QualityControlCheckInfo> entityList = new ArrayList<QualityControlCheckInfo>();
//				for (QualityCheckItem item : qualityCheckList) {
//					if (null == item) {
//						continue;
//					}
//					List<QualityControlResult> resultList = map.remove(item.getCheckItemCode());
//					if (null == resultList) {
//						continue;
//					}
//					for (QualityControlResult result : resultList) {
//						info = new QualityControlCheckInfo();
//						
//						info.setControlIdx(result.getIdx());												// 质量控制主键
//						OmEmployee omEmployee = null;														// 检查人员信息
//						if (!StringUtil.isNullOrBlank(item.getCheckWorkerCode())) {
//							List<OmEmployee> omEmployeeList = getOmEmployeeByWorkerCode(item.getCheckWorkerCode());		
//							if (null != omEmployeeList && omEmployeeList.size() > 0) {
//								omEmployee = omEmployeeList.get(0);
//							}
//						}
//						if (null != omEmployee) {
//							info.setCheckPersonIdx(String.valueOf(omEmployee.getEmpid()));					// 检验人id
//							info.setCheckPersonName(omEmployee.getEmpname());								// 检验人名称
//						}
//						if (!StringUtil.isNullOrBlank(item.getCheckTime())) {
//							info.setCheckTime(DateUtil.yyyy_MM_dd_HH_mm_ss.parse(item.getCheckTime()));		// 检验时间
//						}
//						info.setRemarks(item.getResult() + "[" + item.getRemark() + "]");					// 备注
//						entityList.add(info);
//					}
//				}
//				qualityControlCheckInfoManager.saveOrUpdate(entityList);
			}
		}
	}
	/**
     * TODO V3.2.1代码重构
	 * <li>说明：根据作业卡获取该作业卡下属所有的质量控制信息
	 * <li>创建人：何涛
	 * <li>创建日期：2014-09-09
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param workCard 作业卡实体
	 * @return Map<String, List<QualityControl>> k - 检验项编码； v - 质量控制实体集合；
	 */
//	private Map<String, List<QualityControlResult>> getQualityControlByWorkCard(WorkCard workCard) {
//		// 质量控制 - 关联主键 列表
//		List<String> relationIdxList = new ArrayList<String>();
//		relationIdxList.add(workCard.getIdx());
//		// 根据作业卡（作业工单）获取下属的作业任务
//		List<WorkTask> workTaskList = workTaskManager.getListByWorkCard(workCard.getIdx());
//		if (null != workTaskList && workTaskList.size() > 0) {
//			for (WorkTask task : workTaskList) {
//				relationIdxList.add(task.getIdx());
//			}
//		}
//		// 查询该作业卡（作业工单）下所有质量控制信息 
//		List<QualityControlResult> resultList = qualityControlResultManager.getModelList(relationIdxList);
//		if (null == resultList || resultList.size() <= 0) {
//			return null;
//		}
//		Map<String, List<QualityControlResult>> map = new HashMap<String, List<QualityControlResult>>();
//		for (QualityControlResult result : resultList) {
//			String checkItemCode = result.getCheckItemCode(); 			// 检验项编码
//			List<QualityControlResult> temp = map.get(checkItemCode);
//			if (null == temp) {
//				temp = new ArrayList<QualityControlResult>();
//				temp.add(result);
//				map.put(checkItemCode, temp);
//			} else {
//				temp.add(result);
//			}
//		}
//		return map;
//	}
	/**
	 * <li>说明：根据解析的XML中的Parameters(检测项结果信息)，生成检测结果
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param WorkCard card
	 * @param Parameters paramter
	 * @return void
	 * @throws BusinessException
	 * @throws NoSuchFieldException 
	 */
	private void handleDetectResult(WorkCard card , Parameters paramters, DetectData dd) throws BusinessException, NoSuchFieldException{
		List<Parameter> paramterList  = paramters.getParameters();
		if(paramterList != null && paramterList.size() > 0){
			List<WorkTask> taskList = this.workTaskManager.getListByWorkCard(card.getIdx());  //调用程锐方法，通过作业工单IDX查询作业任务 
			if(taskList != null && taskList.size() > 0){
				String taskIds = "" ;
				for(WorkTask task : taskList){
					if("".equals(taskIds)){
						taskIds = "'"+task.getIdx()+"'" ;
					}else{
						taskIds = taskIds +"," + "'"+task.getIdx()+"'" ;
					}
				}
				List<DetectResult> detectResultList = findDetectResultList(taskIds); //作业工单对应的所有作业任务的检测项信息。
				if(detectResultList != null && detectResultList.size() > 0){
					WorkTask entity = null ; //定义作业任务类
					for(Parameter param : paramterList){
						boolean flag = false ; //设置一个初始标识，默认为false，当作业任务对应的检测项与XML中上传的检测项相同时为true
						for(DetectResult detect : detectResultList){
							if(param.getCode().equals(detect.getDetectItemCode())){ //如果检测项编码存在，则修改其检测项值
								detect.setDetectResult(param.getValue()) ; //设置检测项结果
								detectResultManager.saveOrUpdate(detect);
								flag = true ; //一一对应情况下
							}
						}
						if(!flag){ //当上传的检测项不存在系统定义中时需要新增一条记录
							if(entity == null){ 
								entity = insertWorkTask(card, dd);
							}else{
								insertDetectResult(entity, paramterList); //新增检测项
							}
						}
					}
				}else{
					//有作业任务，但是没有检测项时，需要手动新增一条作业任务并将检测项目关联到此作业任务上。
					WorkTask entity1 = insertWorkTask(card, dd); //新增作业任务
					if(entity1 != null){
						insertDetectResult(entity1, paramterList);
					}
				}
				
			}else{
				//没有作业任务，但是有检测项时，需要手动新增一条作业任务并将检测项目关联到此作业任务上。
				WorkTask entity2 = insertWorkTask(card, dd); //新增作业任务
				if(entity2 != null){
					insertDetectResult(entity2, paramterList);
				}
			}
		}
	}
	/**
	 * <li>说明：通过作业项（WorkTask）主键查询所有检测项
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param taskIds：任务主键集合
	 * @return List<DetectResult> 检测项结果集合
	 * @throws insert
	 */
	@SuppressWarnings("unchecked")
	private List<DetectResult> findDetectResultList(String taskIds) throws BusinessException{
		List<DetectResult> list = null ;
		String hql = "From DetectResult where recordStatus=0 and workTaskIDX in ("+taskIds+")";
		list = this.daoUtils.find(hql);
		return list ;
	}
	/**
	 * <li>说明：插入一条单独的作业项任务，用于关联机务设备上传而信息系统又没有定义的检测项数据。
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param card：作业卡
	 * @return WorkTask
	 * @throws NoSuchFieldException 
	 * @throws 抛出异常列表
	 */
	private WorkTask insertWorkTask(WorkCard card, DetectData dd) throws BusinessException, NoSuchFieldException{
		WorkTask task = new WorkTask();
//		task.setWorkCardIDX(card.getIdx()) ;
//		//EquipInfo info = getEquipInfo(dd.getDetectorCode()) ;
//		String taskName = "机务设备检测任务" ;
//		if(info != null){
//			taskName ="机务设备" +"《"+ info.getEquipName()+"》检测任务";
//		}
//		task.setWorkTaskName(taskName);
//		task.setStatus(WorkTask.STATUS_HANDLED) ; //新增后变成已处理
//		workTaskManager.saveOrUpdate(task);
		return task;
	}
	/**
	 * <li>说明：当机务设备上传的检测项，没有在信息系统定义时，直接新增创建一条记录，并关联到自动创建的作业任务上
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param task：作业任务
	 * @param paramterList：上传的检测项
	 * @return void
	 * @throws BusinessException
	 * @throws NoSuchFieldException 
	 */
	private void insertDetectResult(WorkTask task , List<Parameter> paramterList) throws BusinessException, NoSuchFieldException{
		List<DetectResult> entityList = new ArrayList<DetectResult>() ;
		for(Parameter param : paramterList){
			DetectResult t = new DetectResult();
			t.setWorkTaskIDX(task.getIdx()); //作业任务主键
			t.setDetectItemCode(param.getCode()); //检测项编码
//			t.setDetectResulttype("字符串"); //检测项类型
			t.setDetectItemStandard(param.getDescribe()); //检测标准
			t.setDetectItemContent(param.getName()); //检测内容
			t.setDetectResult(param.getValue());//设置检测结果
			t.setIsNotBlank(1); //非必填
			entityList.add(t);
		}
		this.detectResultManager.saveOrUpdate(entityList);
	}
	
	
	/**
	 * <li>说明：通过人员工号查询用户信息
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param workerCode：人员工号或一卡通号(可能是多个，它们以逗号分隔)
	 * @return List<OmEmployee>
	 */
	@SuppressWarnings("unchecked")
	private List<OmEmployee> getOmEmployeeByWorkerCode(String workerCode) throws BusinessException{
		String[] obj = workerCode.split(",");
		String ids = "" ;
		for (int i = 0; i < obj.length; i++) {
			if("".equals(ids)){
				ids = "'"+obj[i]+"'" ;
			}else{
				ids = ids + ",'"+obj[i]+"'" ;
			}
		}
		String hql = "from OmEmployee where cardNum in("+ids+")";
		List<OmEmployee> empList = this.daoUtils.find(hql);
		return empList;
	}
	/**
	 * 
	 * <li>说明：设置session中用户信息和操作员信息值
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param workerCode：人员工号(可能是多个，它们以逗号分隔)
	 * @return void
	 */
	private void setSeesionValue(String workerCode) throws BusinessException{
		List<OmEmployee> empList = getOmEmployeeByWorkerCode(workerCode);
		if(empList != null && empList.size()>0 ){
			AcOperator operator = acOperatorManager.findLoginAcOprator(empList.get(0).getUserid());
			if(operator != null){
				SystemContext.setAcOperator(operator);
			}
			SystemContext.setOmEmployee(empList.get(0)); //默认设置第一个人
		}
	}
	/**
	 * <li>说明：通过人员工号字符串查询用户ID，如果是多个时，以逗号分隔。
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-8-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param workerCode：人员工号(可能是多个，它们以逗号分隔)
	 * @return workerCode：人员工号(可能是多个，它们以逗号分隔)
	 * @throws 抛出异常列表
	 */
	private String getOmEmployeeIds(String workerCode) throws BusinessException{
		String empIds = "" ;
		List<OmEmployee> empList = getOmEmployeeByWorkerCode(workerCode);
		if(empList != null && empList.size() > 0){
			for(OmEmployee emp : empList){
				if("".equals(empIds)){
					empIds = "" + emp.getEmpid() ;
				}else{
					empIds = empIds + ","+emp.getEmpid() ;
				}
			}
		}
		return empIds;
	}
//	private List<>
}