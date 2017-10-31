package com.yunda.webservice.device.entity;

import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 检修系统与终端设备接口，上传、下传数据交互以XML文本格式传输，
 * 			该类描述XML中的<DetectDatas>元素，同时也可以描述<RequestDatas>、<ResponseDatas>元素。
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XStreamAlias("DetectData")
public class DetectData{
	/** 业务常量：上传数据服务接口方法返回值，表示接口方法调用成功，数据操作接受。 */
	public static final int DATA_ACCEPT = 0;
	/** 业务常量：上传数据服务接口方法返回值，表示接口方法调用成功，数据操作失败。 */
	public static final int DATA_FAIL = 1;
	/** 业务常量：上传数据服务接口方法返回值，表示接口方法调用成功，无效或未知的设备。 */
	public static final int INVALID_UNKNOWN_DEVICE = 2;	
	/** 业务常量：上传数据服务接口方法返回值，表示接口方法调用成功，无效的XML数据类型。 */
	public static final int INVALID_XML = 3;
	/** 业务常量：上传数据服务接口方法返回值，表示接口方法调用失败，未知错误。 */
	public static final int UNKNOWN_ERROR = 9;
	
	/** 业务常量：上传数据类型，表示机务设备上传的数据。 */
	public static final String UPLOAD_DEVICE = "01";
	/** 业务常量：上传数据类型，表示机务设备自检状态。 */
	public static final String UPLOAD_SELF_CHECK = "02";
	/** 业务常量：上传数据类型，表示机务设备上传质量检查数据。 */
	public static final String UPLOAD_QUALITY_CHECK = "03";
	/** 业务常量：请求数据类型，表示工单请求。 */
	public static final String REQUEST_WORKORDER = "01";
	/** 业务常量：请求数据类型，表示心跳包请求。 */
	public static final String REQUEST_HEARTBEAT = "02";
	/** 业务常量：请求数据类型，表示“一卡通”认证请求。 */
	public static final String REQUEST_ONE_CARD_PASS = "03";
	/** 业务常量：请求数据类型，表示工单请求返回。 */
	public static final String RESPONSE_WORKORDER = "01";
	/** 业务常量：请求数据类型，表示心跳包请求返回。 */
	public static final String RESPONSE_HEARTBEAT = "02";
	/** 业务常量：请求数据类型，表示“一卡通”权限认证返回。 */
	public static final String RESPONSE_ONE_CARD_PASS = "03";	
	/**业务常量：表示接口版本号*/
	public static final String EDITION = "1.0" ;
	
	/**
	 * 表示机务设备上传、下传XML数据类型：
	 * 上传：    01表示机务设备上传的数据
				02表示设备自检状态
				03表示机务设备上传质量检查数据
	   请求：    01表示工单请求
				02表示心跳包请求
          		03表示“一卡通”认证请求
	   请求返回：	01表示工单请求返回
				02表示心跳包请求返回
          		03表示“一卡通”权限认证返回
	 */
	@XStreamAlias("DataType")
	private String dataType;
	/** 表示接口版本信息（号）  */
	@XStreamAlias("Edition")
	private String edition;
	/** 作业对象，检测/测试/试验对象编号编码（可理解为互换配件编码或者一卡通等设备）  */
	@XStreamAlias("MaterielCode")
	private String materielCode;
	/** 机务设备编码  */
	@XStreamAlias("DetectorCode")
	private String detectorCode;
	/** 人员工号  */
	@XStreamAlias("WorkerCodes")
	private List<WorkerCode> workerCodes;	
	/** 人员工号  */
	@XStreamAlias("WorkerCode")
	private String workerCode;
	/* 人员 */
	@XStreamAlias("Workers")
    private String workers;
	/** 工单编码  */
	@XStreamAlias("WorkOrderCode")
	private String workOrderCode;
	/** 预留字段，原样返回即可  */
	@XStreamAlias("Code")
	private String code;
	/** 工单描述  */
	@XStreamAlias("WorkOrderDescribe")
	private String workOrderDescribe;
	/** 数据生成时间  */
	@XStreamAlias("DetectorDate")
	private String detectorDate;
	/** 计划开始时间 */
	@XStreamAlias("PlanBeginDate")
	private String planBeginDate;
	/** 计划结束时间 */
	@XStreamAlias("PlanEndDate")
	private String planEndDate;
	
    public String getPlanBeginDate() {
        return planBeginDate;
    }
    
    public void setPlanBeginDate(String planBeginDate) {
        this.planBeginDate = planBeginDate;
    }
    
    public String getPlanEndDate() {
        return planEndDate;
    }
    
    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }
    /** 作业开始时间  */
	@XStreamAlias("RealBeginTime")
	private String beginDate;
	/** 作业结束时间  */
	@XStreamAlias("RealEndTime")
	private String endDate;
	/** 结论10000001表示正常，参考参数字典  */
	@XStreamAlias("Result")
	private String result;
	/** 备注  */
	@XStreamAlias("Remark")
	private String remark;
	/** 设备自检状态编码，参考设备状态编码字典  */
	@XStreamAlias("State")	
	private String state;
	/** 参数代码参考参数字典 */
	@XStreamAlias("Parameters")
	private Parameters parameters;
	/** 质量检查项 */
	@XStreamAlias("QualityCheckList")
	private QualityCheckList qualityCheckList;

	
	/**
	 * <li>说明：获取基本的初始化XML解析对象XStream
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return XStream
	 * @throws XStream
	 */
	public static XStream getXStream(){
		XStream x = new XStream(new com.thoughtworks.xstream.io.xml.DomDriver("UTF-8"));
		x.processAnnotations(new Class[]{DetectData.class, Parameters.class, Parameter.class, 
				WorkerCode.class,QualityCheckList.class,QualityCheckItem.class});
		/*AttributeValueConveter converter = new AttributeValueConveter(x.getMapper());
		converter.addConvertClass(WorkerCode.class);
		x.registerConverter(converter);*/
		return x;
	}
	
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDetectorCode() {
		return detectorCode;
	}
	public void setDetectorCode(String detectorCode) {
		this.detectorCode = detectorCode;
	}
	public String getDetectorDate() {
		return detectorDate;
	}
	public void setDetectorDate(String detectorDate) {
		this.detectorDate = detectorDate;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMaterielCode() {
		return materielCode;
	}
	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getWorkerCode() {
		return workerCode;
	}
	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}
	public String getWorkOrderCode() {
		return workOrderCode;
	}
	public void setWorkOrderCode(String workOrderCode) {
		this.workOrderCode = workOrderCode;
	}
	public String getWorkOrderDescribe() {
		return workOrderDescribe;
	}
	public void setWorkOrderDescribe(String workOrderDescribe) {
		this.workOrderDescribe = workOrderDescribe;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public List<WorkerCode> getWorkerCodes() {
		return workerCodes;
	}

	public void setWorkerCodes(List<WorkerCode> workerCodes) {
		this.workerCodes = workerCodes;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	public QualityCheckList getQualityCheckList() {
		return qualityCheckList;
	}
	public void setQualityCheckList(QualityCheckList qualityCheckList) {
		this.qualityCheckList = qualityCheckList;
	}

    
    public String getWorkers() {
        return workers;
    }

    
    public void setWorkers(String workers) {
        this.workers = workers;
    }
}