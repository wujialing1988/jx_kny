package com.yunda.jx.webservice.stationTerminal.base.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于获取故障处理方法实体包装
 * <li>说明: 用于findFaultMethod接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class FaultMethodBean implements java.io.Serializable {
	
	private String methodID ; //处理方法ID
	private String methodName ; //处理方法名称
	
	public String getMethodID() {
		return methodID;
	}
	public void setMethodID(String methodID) {
		this.methodID = methodID;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
