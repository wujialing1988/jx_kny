package com.yunda.jx.webservice.stationTerminal.base.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于获取承修车型实体包装
 * <li>说明: 用于getUndertakeTrainType,findTrainTypeByGZ,findTrainTypeByDD接口方法
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
public class UndertakeTrainTypeBean implements java.io.Serializable{
	
	private String typeID ;  //车型id
	private String shortName ; //车型名称
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getTypeID() {
		return typeID;
	}
	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}
}
