package com.yunda.webservice.device.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.DateTimeUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.equipcardinst.entity.PartsRdpEquipCard;
import com.yunda.jx.pjjx.partsrdp.equipcardinst.entity.PartsRdpEquipDI;
import com.yunda.jx.pjjx.partsrdp.equipcardinst.manager.PartsRdpEquipCardManager;
import com.yunda.jx.pjjx.partsrdp.equipcardinst.manager.PartsRdpEquipDIManager;
import com.yunda.jx.pjwz.partsmanage.entity.PartsAccount;
import com.yunda.jx.pjwz.partsmanage.manager.PartsAccountManager;
import com.yunda.jxpz.equipinfo.entity.DeviceInfo;
import com.yunda.webservice.device.entity.DetectData;
import com.yunda.webservice.device.entity.DetectDatas;
import com.yunda.webservice.device.entity.Parameter;
import com.yunda.webservice.device.entity.Parameters;
import com.yunda.webservice.device.entity.WorkOrder;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsOrder业务类,配件检修设备工单维护
 * <li>创建人：王治龙
 * <li>创建日期：2015-2-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="partsOrderManager")
public class PartsOrderManager extends JXBaseManager<WorkOrder, WorkOrder>{
	
	/**操作员业务类*/
	@Resource
    private AcOperatorManager acOperatorManager;
	/**机务设备工单业务类*/
	@Resource
	private PartsRdpEquipCardManager partsRdpEquipCardManager;
	/**机务设备工单检测项业务类*/
	@Resource
	private PartsRdpEquipDIManager partsRdpEquipDIManager;
	
	/**配件台账信息业务类*/
	@Resource
	private PartsAccountManager partsAccountManager;
	
		
	/**********下载方法处理逻辑开始*****************************************************/
	
	/**
	 * <li>说明：返回工单请求结果
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param partsNo 配件编号（作业对象编码）
	 * @param detectorCode 机务设备编码
	 * @return String 返回工单XML字符串
	 * @throws BusinessException
	 */
	public String responseWorkOrder(String partsNo , String detectorCode) throws BusinessException{
		//定义XML文档根节点
		DetectDatas dds = null;
		List<DetectData> detectDataList = new ArrayList<DetectData>(); 
		//第一步通过机务设备编码查询机务设备分类信息
		DeviceInfo deviceInfo =  this.getDeviceInfo(detectorCode);
		//第二步通过配件编号和机务设备分类编码查询当前未处理的机务设备工单
		if(deviceInfo != null && !StringUtil.isNullOrBlank(deviceInfo.getDeviceTypeCode())){
			List<PartsRdpEquipCard> equipCardList = this.findPartsRdpEquipCard(partsNo, deviceInfo.getDeviceTypeCode());
			for(PartsRdpEquipCard card : equipCardList){
				List<PartsRdpEquipDI> equipDIList = findPartsRdpEquipDI(card.getIdx());  //查询数据检测项集合
				//整合以上数据，打包后解析成XML字符串格式返回
				DetectData dd = this.buildUpXMLBean( partsNo, detectorCode, card, equipDIList );
				if(dd !=  null){
					detectDataList.add(dd);
				}
			}
//			循环完成当前工位上的所有工单信息后，添加到映射XML文档对象节点中
			if(detectDataList != null && detectDataList.size() > 0){
				dds = new DetectDatas(); //初始化XML文档根节点
				dds.setDetectDataList(detectDataList);
			}
		}
		if(dds != null){
			return dds.toResponseDataXML();
		}else{
			return "编号：《"+detectorCode+"》的机务设备，没有作业对象《"+partsNo+"》的工单。";
		}
	}
	/**
	 * <li>说明：返回心跳包请求结果
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param detectorCode 机务设备编码
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
	 * <li>创建日期：2015-2-11
	 * @param detectorCode 机务设备编码
	 * @param materielCode 作业对象编码（此处为一卡通）
	 * @return String XML字符串
	 * @throws BusinessException
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
	 * <li>说明：通过配件编号、设备分类编码查询配件机务设备工单(状态为：未处理)
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param partsNo 配件编号（作业对象编码）
	 * @param deviceTypeCode 设备分类编码
	 * @return List<PartsRdpEquipCard> 配件机务设备工单集合
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private List<PartsRdpEquipCard> findPartsRdpEquipCard(String partsNo , String deviceTypeCode) throws BusinessException{
		String hql = "From PartsRdpEquipCard where recordStatus=0 and partsNo = ? and deviceTypeCode = ?" +
				" and status = '" + PartsRdpEquipCard.STATUS_WCL + "'" ;
		List<PartsRdpEquipCard> rdpList = this.daoUtils.find(hql, new Object[]{partsNo , deviceTypeCode});
		return rdpList;
	}
	/**
	 * <li>说明：通过配件机务设备工单主键查询机务设备检测数据项集合
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param rdpEquipCardIDX 设备工单实例主键
	 * @return List<PartsRdpEquipDI>
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	private List<PartsRdpEquipDI> findPartsRdpEquipDI(String rdpEquipCardIDX) throws BusinessException{
		StringBuffer sb = new StringBuffer();
		sb.append("From PartsRdpEquipDI where recordStatus=0 and rdpEquipCardIDX = '").append(rdpEquipCardIDX).append("'");
		return this.daoUtils.find(sb.toString());
	}
	
	/**
	 * <li>说明：通过传入参数构造XML对象
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param partsNo 配件编码
	 * @param detectorCode 机务设备编码
	 * @param card 机务设备设备工单对象
	 * @param equipDIList 机务设备设备检修项集合
	 * @return 返回值说明
	 * @throws BusinessException
	 */
	private DetectData buildUpXMLBean(String partsNo , String detectorCode, PartsRdpEquipCard card , 
			List<PartsRdpEquipDI> equipDIList )throws BusinessException{
		DetectData dd = null; //定义XML返回对象
		if(card != null){
			//设置工单信息
			dd = new DetectData(); //初始化XML返回对象
			dd.setDataType(DetectData.RESPONSE_WORKORDER); //设备工单请求返回
			dd.setEdition(DetectData.EDITION); //版本号
			dd.setMaterielCode(partsNo); //作业对象编码
			dd.setDetectorCode(detectorCode); //机务设备编码
			dd.setWorkOrderCode(card.getIdx()); //工单编码（即工单主键）
			dd.setWorkOrderDescribe(card.getEquipCardDesc()); //工单描述
			if(card.getWorkStartTime() != null)
				dd.setPlanBeginDate(DateTimeUtil.getDateTime(card.getWorkStartTime(), null));
			if(card.getWorkEndTime() != null)
				dd.setPlanEndDate(DateTimeUtil.getDateTime(card.getWorkEndTime(), null));
			if(equipDIList != null && equipDIList.size() > 0){
				//开始设置检测项信息
				Parameters params = new Parameters(); //初始化检测项根节点
				List<Parameter> paramList = new ArrayList<Parameter>();
				for(PartsRdpEquipDI di : equipDIList){
					Parameter param = new Parameter(); //初始化Parameter节点
					param.setCode(di.getDataItemNo()); //检测项编码
					param.setName(di.getDataItemName());//检测名称（内容）
					param.setUnit(di.getUnit()); //检测结果类型
					param.setDescribe(di.getDataItemDesc()); //检测描述
//					param.setValue() ;  返回工单不存在值
					paramList.add(param);
				}
				if(paramList != null && paramList.size() > 0){ //存在检测结果时添加节点
					params.setParameters(paramList); //添加节点
					dd.setParameters(params); //添加节点
				}
			}
		}
		
		return dd;
	}
	
	/**************上传方法处理逻辑开始*************************************************/
	
	/**
	 * <li>说明：接收机务设备上传工单处理结果请求，并处理请求。
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param dd 接收的XML解析对象
	 * @throws Exception 
	 */
	public void requestWorkOrder(DetectData dd) throws Exception{
		//获取作业人员信息、操作员信息，并设置到session中。
		setSeesionValue(dd.getWorkerCode());
//		获取操作人员ID
		String empIds = getOmEmployeeIds(dd.getWorkerCode());
		String empNames = dd.getWorkers();
		List<OmEmployee> empList = getOmEmployeeByWorkerCode(dd.getWorkerCode());
		if(empList != null && empList.size() > 0){
			for(OmEmployee emp : empList){
				if("".equals(empNames)){
					empNames = emp.getEmpname();
				}else{
					empNames += "," + emp.getEmpname();
				}
			}
		}
		//获取设备工单信息
		PartsRdpEquipCard objCard = null;
		if(dd.getWorkOrderCode() != null)
		    objCard = partsRdpEquipCardManager.getModelById(dd.getWorkOrderCode()); 
		//通过设备工单获取检测项目信息
		if(objCard != null){
			if(PartsRdpEquipCard.STATUS_WCL.equals(objCard.getStatus())){
				handleDetectResult(objCard , dd.getParameters() , dd) ; //处理检测项
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(objCard.getWorkStartTime() == null)
					objCard.setWorkStartTime(sdf.parse(dd.getBeginDate()));
				if(objCard.getWorkEndTime() == null)
					objCard.setWorkEndTime(sdf.parse(dd.getEndDate()));
				objCard.setWorkEmpID(empIds); //作业人
				objCard.setWorkEmpName(empNames); //作业人名称
				objCard.setWorkResult(dd.getResult()); //作业结果
				objCard.setRemarks(dd.getRemark());
				objCard.setStatus(PartsRdpEquipCard.STATUS_YCL); //已处理
				this.partsRdpEquipCardManager.saveOrUpdate(objCard);    //更新设备工单
			}
		}else{ //上传的设备工单在当前系统中不存在的情况下，新增数据
			insertCardAndEquipDI(dd, empIds, empNames); 
		}
	}
	/**
	 * <li>说明：新增设备工单和工单的设备检测项
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param dd DetectData对象
	 * @param empIds 作业人员ID
	 * @param empNames 作业人员名称
	 * @return PartsRdpEquipCard
	 * @throws BusinessException
	 * @throws ParseException 
	 * @throws NoSuchFieldException 
	 */
	private PartsRdpEquipCard insertCardAndEquipDI(DetectData dd ,String empIds ,String empNames) throws BusinessException, ParseException, NoSuchFieldException{
		PartsRdpEquipCard card = new PartsRdpEquipCard();
		setRdpInfo(dd.getMaterielCode(), card);
		card.setEquipCardNo(dd.getWorkOrderCode()); //工单编号
		card.setEquipCardDesc(dd.getWorkOrderDescribe());//描述
		PartsAccount part =  getPartInfo(dd.getMaterielCode()) ;
		if( part != null ){
			card.setPartsName(part.getPartsName());
			card.setSpecificationModel(part.getSpecificationModel());
			card.setPartsNo(part.getPartsNo());
		}else{
			card.setPartsName("不知名的配件");
			card.setSpecificationModel("不知名的规格型号");
			card.setPartsNo(dd.getMaterielCode());
		}
		DeviceInfo device = getDeviceInfo(dd.getDetectorCode());
		if(device != null){
			card.setDeviceTypeCode(device.getDeviceTypeCode()) ;//设备分类编码
			card.setDeviceTypeName(device.getDeviceTypeName()); //分类名称
			card.setDeviceInfoCode(device.getDeviceInfoCode()); //设备编码
		}else{
			card.setDeviceTypeCode("不知名的设备分类编码") ;//设备分类编码
			card.setDeviceTypeName("不知名的设备分类名称"); //分类名称
			card.setDeviceInfoCode(dd.getDetectorCode()); //设备编码
		}
		card.setWorkEmpID(empIds);
		card.setWorkEmpName(empNames) ;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(dd.getBeginDate() != null)
			card.setWorkStartTime(sdf.parse(dd.getBeginDate()));
		if(dd.getEndDate() != null)
			card.setWorkEndTime(sdf.parse(dd.getEndDate()));
		card.setDataGenTime(new java.util.Date()); //数据生成时间
		card.setWorkResult(dd.getResult()); //作业结果
		card.setRemarks(dd.getRemark());
		card.setStatus(PartsRdpEquipCard.STATUS_YCL); //已处理
		partsRdpEquipCardManager.saveOrUpdate(card);    //保存设备工单
		if(!StringUtil.isNullOrBlank(card.getIdx())){
		    Parameters parameters = dd.getParameters();
		    if(parameters != null){
    			List<Parameter> paramterList  = parameters.getParameters();
    			if(paramterList != null){
        			for(Parameter param : paramterList){
        				this.insertEquipDI(card, param); //新增检测结果
        			}
    			}
		    }
		}
		return card ;
	}
	/**
	 * <li>方法说明： 针对没有对应工单上传的数据，根据配件编号找兑现单设置关联
	 * <li>方法名：setRdpInfo
	 * @param partsNo 配件编号
	 * @param card 设备工单
	 * <li>创建人： 张凡
	 * <li>创建日期：2015年11月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
    private void setRdpInfo(String partsNo, PartsRdpEquipCard card) {
        String hql = "from PartsRdp where partsNo = ? and recordStatus = 0 and status = ?";
		PartsRdp rdp = (PartsRdp) daoUtils.findSingle(hql, partsNo, PartsRdp.STATUS_JXZ);
		if(rdp != null)
		    card.setRdpIDX(rdp.getIdx());
    }
	
	/**
	 * <li>说明：根据解析的XML中的Parameters(检测项结果信息)，生成检测结果
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param card PartsRdpEquipCard对象
	 * @param paramters Parameters对象 
	 * @param dd DetectData对象 
	 * @throws BusinessException
	 * @throws NoSuchFieldException 
	 */
	private void handleDetectResult(PartsRdpEquipCard card , Parameters paramters, DetectData dd) throws BusinessException, NoSuchFieldException{
		List<Parameter> paramterList  = paramters.getParameters();
		if(paramterList != null && paramterList.size() > 0){ //上传对象存在值的情况
			List<PartsRdpEquipDI> equipDIList = this.findPartsRdpEquipDI(card.getIdx());  //通过作业工单IDX查询检测项 
			if(equipDIList != null && equipDIList.size() > 0){
				for(Parameter param : paramterList){
					boolean flag = false ; //设置一个初始标识，默认为false，当系统中定义的检测项与XML中上传的检测项相同时为true
					for(PartsRdpEquipDI equipDI : equipDIList){
						if(param.getCode().equals(equipDI.getDataItemNo())){ //如果检测项编码存在，则修改其检测项值
							equipDI.setItemValue(param.getValue()) ; //设置检测项结果
							partsRdpEquipDIManager.saveOrUpdate(equipDI);
							flag = true ; //一一对应情况下
						}
					}
					if(!flag){ //当上传的检测项和系统中的检测项没有对应时需要新增一条记录
						this.insertEquipDI(card, param);
					}
				}
			}else{ //有工单，但是没有检测项时，需要手动新增检测项
				for(Parameter param : paramterList){
					this.insertEquipDI(card, param); //新增检测结果
				}
			}
		}
	}
	/**
	 * <li>说明：插入一条单独的检测结果。
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param card PartsRdpEquipCard对象
	 * @param param Parameter XML对象
	 * @return PartsRdpEquipDI
	 * @throws NoSuchFieldException 
	 * @throws BusinessException
	 */
	private PartsRdpEquipDI insertEquipDI(PartsRdpEquipCard card, Parameter param) throws BusinessException, NoSuchFieldException{
		PartsRdpEquipDI diEntity = new PartsRdpEquipDI();
		diEntity.setRdpEquipCardIDX(card.getIdx()) ;
		diEntity.setDataItemName(param.getName());
		diEntity.setDataItemNo(param.getCode());
		diEntity.setDataItemDesc(param.getDescribe());
		diEntity.setUnit(param.getUnit());
		diEntity.setItemValue(param.getValue());
		partsRdpEquipDIManager.saveOrUpdate(diEntity);
		return diEntity;
	}
	
	/**
	 * <li>说明：通过机务设备编码查询机务设备信息
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param detectorCode 机务设备编码
	 * @return DeviceInfo
	 * @throws BusinessException
	 */
	private DeviceInfo getDeviceInfo(String detectorCode) throws BusinessException{
		String hql = "From DeviceInfo where deviceInfoCode = ?" ;
		return (DeviceInfo)this.daoUtils.findSingle(hql, new Object[]{detectorCode});
	}
	/**
	 * <li>说明：通过配件编号查询配件信息
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param partsNo 配件编号
	 * @return PartsAccount
	 * @throws BusinessException
	 */
	private PartsAccount getPartInfo(String partsNo) throws BusinessException{
		List<PartsAccount> partsList = partsAccountManager.getPartsAccountByPartsNo(partsNo, PartsAccount.PARTS_STATUS_ZC) ;
		if(partsList != null && partsList.size() > 0){
			return partsList.get(0); 
		}
		return null ;
	}
	/**
	 * <li>说明：通过人员工号查询用户信息
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param workerCode 人员工号
	 * @return List<OmEmployee>
	 * @throws NoSuchFieldException 
	 */
	@SuppressWarnings("unchecked")
	private List<OmEmployee> getOmEmployeeByWorkerCode(String workerCode) throws BusinessException{
		if(!StringUtil.isNullOrBlank(workerCode)){
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
		return null ;
	}
	/**
	 * 
	 * <li>说明：设置session中用户信息和操作员信息值
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-11
	 * @param workerCode 人员工号(可能是多个，它们以逗号分隔)
	 * @throws NoSuchFieldException 
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
	 * <li>创建日期：2015-2-11
	 * @param workerCode 人员工号(可能是多个，它们以逗号分隔)
	 * @return workerCode 人员工号(可能是多个，它们以逗号分隔)
	 * @throws BusinessException
	 */
	private String getOmEmployeeIds(String workerCode) throws BusinessException{
		String empIds = "" ;
		List<OmEmployee> empList = getOmEmployeeByWorkerCode(workerCode);
		if(empList != null && empList.size() > 0){
			for(OmEmployee emp : empList){
				if("".equals(empIds)){
					empIds = "" + emp.getEmpid() ;
				}else{
					empIds = empIds + "," + emp.getEmpid() ;
				}
			}
		}
		if("".equals(empIds)){
			empIds = workerCode ;
		}
		return empIds;
	}
}