package com.yunda.webservice.device.entity;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 检修系统与终端设备接口，上传、下传数据交互以XML文本格式传输，
 * 			该类描述XML中的Parameters元素，表示参数代码 （参数代码参考参数字典）
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XStreamAlias("Parameters")
//@XStreamConverter(AttributeValueConveter.class)
public class Parameters{
	/** 参数名称 */
	@XStreamAlias("State")
	private String state;
	/** 参数代码参考参数字典  */
	@XStreamImplicit(itemFieldName="Parameter")
	private List<Parameter> parameters;
	
	public List<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}