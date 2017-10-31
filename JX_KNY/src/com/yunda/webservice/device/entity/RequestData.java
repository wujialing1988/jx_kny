package com.yunda.webservice.device.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 用于机车检修管理系统为机务设备系统（或其他外部系统）提供根据查询条件查询未处理完的配件生产计划的接口服务。接收请求参数
 * <li>创建人：王治龙
 * <li>创建日期：2014-6-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XStreamAlias("requestData")
public class RequestData {
	/**规格型号*/
	@XStreamAlias("specificationModelCode")
	private String specificationModelCode ;

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
		x.processAnnotations(new Class[]{RequestData.class});
		/*AttributeValueConveter converter = new AttributeValueConveter(x.getMapper());
		x.registerConverter(converter);*/
		return x;
	}
	
	/**
	 * <li>说明：根据xml字符串解析并返回RequestData对象
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String xml：xml字符串
	 * @return DetectDatas
	 * @throws 
	 */
	public static RequestData fromXML(String xml){
		RequestData data = (RequestData)getXStream().fromXML(xml); 
		return data;
	}
	/**测试请求信息是否能够解析*/
	public static void main(String[] args) throws URISyntaxException, FileNotFoundException {
		XStream x = getXStream();
		String path = "com/yunda/webservice/device/entity/UploadPartType.xml";
		URI uri = Thread.currentThread().getContextClassLoader().getResource(path).toURI();
		File f1 = new File(uri);
		RequestData data = (RequestData)x.fromXML(new FileInputStream(f1));
		System.out.println(x.toXML(data));
	}
	
	public String getSpecificationModelCode() {
		return specificationModelCode;
	}

	public void setSpecificationModelCode(String specificationModelCode) {
		this.specificationModelCode = specificationModelCode;
	}
	
	
}
