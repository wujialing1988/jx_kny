package com.yunda.webservice.device;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.webservice.device.entity.CallLog;
import com.yunda.webservice.device.entity.DetectData;
import com.yunda.webservice.device.entity.DetectDatas;
import com.yunda.webservice.device.entity.RequestData;
import com.yunda.webservice.device.manager.CallLogManager;
import com.yunda.webservice.device.manager.PartRdpPlanManager;
import com.yunda.webservice.device.manager.PartsOrderManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-12-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="deviceInterfaceService")
public class DeviceInterfaceService implements IDeviceInterface {
	
    Logger logger = Logger.getLogger(this.getClass());
    @Resource
	private CallLogManager callLogManager;
	
	/*、
	 * 武汉使用
	@Resource
	private PartRepairPlanManager partRepairPlanManager;
    @Resource
    private WorkOrderManager workOrderManager ;*/
	/** 机车检修管理系统为机务设备系统（或其他外部系统）提供根据查询条件查询未处理完的配件生产计划的接口服务 管理器 */
	@Resource
	private PartRdpPlanManager partRdpPlanManager;
	/**设备工单操作业务类*/
	@Resource
	private PartsOrderManager partsOrderManager;
	
	
	/* *
	 * <li>说明：根据文件路径获取XML字符串
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-1-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param path xml路径
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	/*private String getXML(String path){
		StringBuilder sb = new StringBuilder();
		try {
			URI uri = Thread.currentThread().getContextClassLoader().getResource(path).toURI();
			File f1 = new File(uri);
			java.io.InputStreamReader r = new java.io.InputStreamReader(new java.io.FileInputStream(f1), "UTF-8");
			int ch = 0;
			while(true){
				ch = r.read();
				if(ch == -1)	break;
				sb.append((char)ch);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();		
	}	*/
	/* *
	 * <li>说明：返回一卡通请求结果数据
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-1-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws String
	 */
	/*protected String responseOneCardPass(){
		return getXML("com/yunda/webservice/device/entity/ResponseOneCardPass.xml");
	}
	protected String responseHeartBeat(){
		return getXML("com/yunda/webservice/device/entity/ResponseHeartBeat.xml");
	}
	protected String responseWorkOrder(){
		return getXML("com/yunda/webservice/device/entity/ResponseWorkOrder.xml");
	}*/
	
	/**
	 * <li>说明：获取业务处理类调用结果，此处为获取配件检修兑现单
	 * <li>创建人：王治龙
	 * <li>创建日期：2015-2-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param specificationModelCode 配件规格型号
	 * @return String XML格式字符串
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException 
	 */
	protected String responsePartPlanList(String specificationModelCode) throws InvocationTargetException, IllegalAccessException{
		return partRdpPlanManager.getPartRepairPlan(specificationModelCode);
	}	
	
	/**
	 * <li>说明：机务设备请求检修系统下传数据服务接口（默认WebService方式实现）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param xmlData 请求参数，XML格式的字符串
	 * @return String: 返回处理结果的XML格式的字符串
	 * @throws 
	 */
	public String getData(String xmlData){
		//记录接口调用日志
		CallLog log = new CallLog();
		log.setRequestContent(xmlData);
		log.setCallTime(new Date());
		log.setMethodName("GetData");
		log.setRemark("下传接口:WebService方式");
		String resp = null;
		try {
		    logger.info("GetData接收的请求XML：\n" + xmlData);			
			/**业务实现方法开始*/
		    DetectDatas dds = DetectDatas.fromXML(xmlData);
		    DetectData dd = dds.getDetectDataList().get(0);
            
            // 请求数据类型，表示工单请求。
			if(DetectData.REQUEST_WORKORDER.equals(dd.getDataType())){
//				resp = responseWorkOrder();
				//返回根据作业对象和作业机务设备查询所得的工单信息
			    resp = this.partsOrderManager.responseWorkOrder(dd.getMaterielCode(), dd.getDetectorCode()); 
                
			// 请求数据类型，表示心跳包请求。
			} else if(DetectData.REQUEST_HEARTBEAT.equals(dd.getDataType())){
//				resp = responseHeartBeat();
				resp = this.partsOrderManager.responseHeartBeat(dd.getDetectorCode());
                
            // 请求数据类型，表示“一卡通”认证请求。
			} else if(DetectData.REQUEST_ONE_CARD_PASS.equals(dd.getDataType())){
//				resp = responseOneCardPass();
				resp = this.partsOrderManager.responseOneCardPass(dd.getMaterielCode(), dd.getDetectorCode());
			} else {
                // 表示接口方法调用成功，无效的XML数据类型。
				resp = "" + DetectData.INVALID_XML;
			}
			
			logger.info("GetData处理后返回XML：\n" + resp);
			//logger.info("****************下传数据End  ************************");
		} catch (Exception e){
			e.printStackTrace();
//          保存接口调用日志
            log.setResponseResult(resp);
            try {
                callLogManager.save(log);
            } catch (BusinessException be) {
                e.printStackTrace();
            }
			return "" + DetectData.INVALID_XML;
		} 
		return resp;
	}
	/**
	 * <li>说明：机务设备向检修系统上传数据服务接口（默认WebService方式实现）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-27
     * <li>修改人：何涛
     * <li>修改日期：2016-03-10
     * <li>修改内容：增加接口说明
	 * @param xmlData 请求参数，XML格式的字符串
     * 以下数据为2016-03-10厂商接口调试测试数据，需要注意以下几点：
         * 1、需增加作业人员<Workders>字段，
         * 2、时间字段格式保持统一，yyyy-MM-dd HH:mm:ss
         * TODO 机务设备接口数据传输处理修改
         * 3、由于现在无需getData接口来请求作业工单号这个操作，因此上传数据的<WorkOrderCode>为空
     * <DetectDatas>
            <DetectData>
                <DataType>01</DataType>
                <Edition>1.0</Edition>
                <MaterielCode>0110</MaterielCode>
                <DetectorCode>00011000000001</DetectorCode>
                <WorkOrderCode></WorkOrderCode>
                <WorkOrderDescribe>工单名称：拆电机吊杆/n作业工位：解体工位/n作业班组：解体组/转向架车间/n</WorkOrderDescribe>
                <RealBeginTime>2014-04-24 16:14:00</RealBeginTime>
                <RealEndTime>2014-04-25 08:44:00</RealEndTime>
                <DetectorDate>2016/3/10 10:25:53</DetectorDate>
                <Workers>作业人员姓名</Workers>
                <WorkerCode>1</WorkerCode>
                <Result>合格</Result>
                <Remark>完全正常</Remark>
                <Parameters>
                    <Parameter>
                        <Code>10001</Code><Name>UpTime</Name>
                        <Unit>s</Unit>
                        <Describe>升弓时间</Describe>
                        <Value>4.54</Value>
                    </Parameter>
                </Parameters>
            </DetectData>
        </DetectDatas>
	 * @return int: 返回调用结果
	 * <li>	0接口方法调用成功，数据操作接受。
	 * <li>	1接口方法调用成功，数据操作失败。
	 * <li>	2接口方法调用成功，无效或未知的设备。
	 * <li>	3接口方法调用成功，无效的XML数据类型。
	 * <li>	9接口方法调用失败，未知错误。	
	 * @throws
	 */
	public int sendData(String xmlData){
        
//	    StringBuffer sb = new StringBuffer();
        
//        sb.append(" <DetectDatas> ");
//        sb.append(" <DetectData>  ");
//        sb.append(" <DataType>01</DataType> ");
//        sb.append(" <Edition>1.0</Edition> ");
//        sb.append(" <MaterielCode>10000000000001</MaterielCode> ");
//        sb.append(" <DetectorCode>00011000000001 </DetectorCode> ");
//        sb.append(" <WorkerCode>00022000000001,00022000000002</WorkerCode>  ");
//        sb.append(" <Workers>作业人员姓名</Workers> ");
//        sb.append(" <WorkOrderCode>11100312</WorkOrderCode> ");
//        sb.append(" <WorkOrderDescribe>工单描述内容</WorkOrderDescribe> ");
//        sb.append(" <DetectorDate>2016/3/10 10:25:53</DetectorDate> ");
//        sb.append(" <RealBeginTime>2014-04-24 16:14:00</RealBeginTime>");
//        sb.append(" <RealEndTime>2014-04-25 08:44:00</RealEndTime>");
//        sb.append(" <Result>合格/不合格</Result>");
//        sb.append(" <Remark>完全正常</Remark>");
//        sb.append(" <Parameters> ");
//        sb.append(" <Parameter> ");
//        sb.append(" <Code>100000001</Code> ");
//        sb.append(" <Name>高度</Name> ");
//        sb.append(" <Unit>cm</Unit> ");
//        sb.append(" <Describe >高度</Describe > ");
//        sb.append(" <Value>20</Value> ");
//        sb.append(" </Parameter> ");
//        sb.append(" <Parameter> ");
//        sb.append(" <Code>100000002’ </Code> ");
//        sb.append(" <Name>厚度</Name> ");
//        sb.append(" <Unit>mm</Unit> ");
//        sb.append(" <Describe>厚度</Describe> ");
//        sb.append(" <Value>40</Value> ");
//        sb.append(" </Parameter> ");
//        sb.append(" </Parameters> ");
//        sb.append(" </DetectData> ");
//        sb.append(" </DetectDatas> ");
//        
//        xmlData = sb.toString();
        
		//记录接口调用日志
		CallLog log = new CallLog();
		log.setRequestContent(xmlData);
		log.setCallTime(new Date());
		log.setMethodName("GetData");
		log.setRemark("上传接口:WebService方式");
		
		AcOperator ac = new AcOperator();
		ac.setOperatorid(0L);
		ac.setOperatorname("未知");
		SystemContext.setAcOperator(ac);
		
		int resp = DetectData.DATA_FAIL;
		try {
		    logger.info("SendData接收的请求XML：\n" + xmlData);
			DetectDatas dds = null;
			try {
				dds = DetectDatas.fromXML(xmlData);
				resp = DetectData.DATA_ACCEPT ; //方法接受成功
				for (DetectData dd : dds.getDetectDataList()) {
					if(DetectData.UPLOAD_DEVICE.equals(dd.getDataType())){ //上传工单
						this.partsOrderManager.requestWorkOrder(dd); //上传工单操作
					}
				}
			} catch (RuntimeException e) {
				resp = DetectData.INVALID_XML;
				e.printStackTrace();
			}
			logger.info("SendData处理后返回XML：\n" + resp);
		} catch (Exception e) {
			resp = DetectData.DATA_FAIL;
			e.printStackTrace();
		} finally{
			//保存接口调用日志
			log.setResponseResult(resp + "");
			try {
				callLogManager.save(log);
			} catch (BusinessException e) {
				e.printStackTrace();
			}			
		}
		return resp;
	}
	
	/**
	 * <li>说明：机车检修管理系统为机务设备系统（或其他外部系统）提供根据查询条件查询未处理完的配件生产计划的接口服务。
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-6-16
	 * <li>修改人： 何涛
	 * <li>修改日期：2014-09-02
	 * <li>修改内容：接口实现
	 * @param xmlData  请求参数，XML格式的字符串
	 * @return String :返回处理结果的XML格式的字符串
	 */
	public String getPartRepairPlan(String xmlData) {
		// 记录接口调用日志
		CallLog log = new CallLog();
		log.setRequestContent(xmlData);
		log.setCallTime(new Date());
		log.setMethodName("getPartRepairPlan");
		log.setRemark("配件生产计划接口:WebService方式");
		
		String resp = null;
		try {
			logger.info("**************** 配件生产计划请求Begin ************************");
			logger.info("接收的请求XML：\n" + xmlData);			
			RequestData requestData = RequestData.fromXML(xmlData);
			logger.info("成功解析后回显XML：\n" + RequestData.getXStream().toXML(requestData));
			String specificationModelCode = requestData.getSpecificationModelCode();
			if (StringUtil.isNullOrBlank(specificationModelCode)) {
				return "请求的规格型号编码为空";
			}
			logger.info("接口[IDeviceInterface.getPartRepairPlan()]请求的规格型号：[ "+ specificationModelCode + " ]");
			resp = responsePartPlanList(specificationModelCode);
			logger.info("处理后返回XML：\n" + resp);
			logger.info("**************** 配件生产计划请求End ************************");
		} catch (Exception e){
			e.printStackTrace();
			return "接口调用失败！";
		} finally {
			//保存接口调用日志
			log.setResponseResult(resp);
			try {
				callLogManager.save(log);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		return resp;
	}
	
}