package com.yunda.webservice.device.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
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
@XStreamAlias("Parameter")
//@XStreamConverter(AttributeValueConveter.class)
public class Parameter{
	/** 参数代码 */
	@XStreamAlias("Code")
	private String code;
	/** 参数名称 */
//	@XStreamAsAttribute
	@XStreamAlias("Name")
	private String name;
	/** 计量单位 */
	@XStreamAlias("Unit")
	private String unit;
	/** 描述 */
	@XStreamAlias("Describe")
	private String describe;
	/** 参数值 */
	@XStreamAlias("Value")
	private String value;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
}