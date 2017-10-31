package com.yunda.webservice.device.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 检修系统与终端设备接口，上传、下传数据交互以XML文本格式传输，
 * 			该类描述XML中的<DetectDatas>元素，作为根元素，包含整个传输数据对象，
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XStreamAlias("DetectDatas")
public class DetectDatas{
	
	@XStreamImplicit
	@XStreamAlias("DetectData")
	private List<DetectData> detectDataList;
	
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
		x.processAnnotations(new Class[]{DetectDatas.class, DetectData.class, Parameters.class, Parameter.class, 
				WorkerCode.class,QualityCheckList.class,QualityCheckItem.class});
		//AttributeValueConveter converter = new AttributeValueConveter(x.getMapper());
//		converter.addConvertClass(Parameter.class); //此处用于解析不严格的XML如：包含属性小写"value" 
		//converter.addConvertClass(WorkerCode.class);
		//x.registerConverter(converter);
		return x;
	}
	/**
	 * <li>说明：根据xml字符串解析并返回DetectDatas对象
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String xml：xml字符串
	 * @return DetectDatas
	 * @throws 
	 */
	public static DetectDatas fromXML(String xml){
		DetectDatas detectDatas = (DetectDatas)getXStream().fromXML(xml); 
		return detectDatas;
	}
	/** 调试方法  
	 * @throws URISyntaxException 
	 * @throws FileNotFoundException */
	public static void main(String[] args) throws URISyntaxException, FileNotFoundException{
		XStream x = DetectDatas.getXStream();
		String path = "com/yunda/webservice/device/entity/UploadWorkOrder.xml";
		URI uri = Thread.currentThread().getContextClassLoader().getResource(path).toURI();
		File f1 = new File(uri);
		DetectDatas dds = (DetectDatas)x.fromXML(new FileInputStream(f1));
		System.out.println(x.toXML(dds));
	}	
	/**
	 * <li>说明：获取上传数据接口的XML字符串内容
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public String toDetectDataXML(){
		return getXStream().toXML(this);
	}
	/**
	 * <li>说明：获取请求参数XML字符串内容
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public String toRequestDataXML(){
		XStream x = getXStream();
		x.alias("RequestData", DetectData.class);
		return x.toXML(this);
	}
	/**
	 * <li>说明：获取调用下传数据接口返回XML字符串内容
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public String toResponseDataXML(){
		XStream x = getXStream();
		x.alias("DetectDatas", DetectDatas.class);
		return x.toXML(this);
	}

	public List<DetectData> getDetectDataList() {
		return detectDataList;
	}

	public void setDetectDataList(List<DetectData> detectDataList) {
		this.detectDataList = detectDataList;
	}

}