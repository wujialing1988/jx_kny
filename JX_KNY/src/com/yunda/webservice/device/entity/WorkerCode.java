package com.yunda.webservice.device.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 检修系统与终端设备接口，上传、下传数据交互以XML文本格式传输，
 * 			该类描述XML中的WorkerCode元素
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
//@XStreamConverter(AttributeValueConveter.class)
@XStreamAlias("WorkerCode")
public class WorkerCode{
	/** 人员工号值 */
	private String value;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}