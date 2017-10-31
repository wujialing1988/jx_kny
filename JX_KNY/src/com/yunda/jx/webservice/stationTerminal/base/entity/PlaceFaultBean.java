package com.yunda.jx.webservice.stationTerminal.base.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于通过组成位置主键获取故障现象数据实体包装
 * <li>说明: 用于getPlaceFault接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class PlaceFaultBean implements java.io.Serializable{
	
	private String faultId ; //故障ID
	private String faultName ; //故障名称
	
	public String getFaultId() {
		return faultId;
	}
	public void setFaultId(String faultId) {
		this.faultId = faultId;
	}
	public String getFaultName() {
		return faultName;
	}
	public void setFaultName(String faultName) {
		this.faultName = faultName;
	}
}
