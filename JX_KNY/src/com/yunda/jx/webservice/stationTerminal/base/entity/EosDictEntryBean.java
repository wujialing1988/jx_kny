package com.yunda.jx.webservice.stationTerminal.base.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于获取提票类型实体包装
 * <li>说明: 用于getFaultTypes、getDict接口方法
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
public class EosDictEntryBean implements java.io.Serializable{
	
	private String dictid ; //提票类型名称
	private String dictname ; //提票类型名称
	
	public String getDictid() {
		return dictid;
	}
	public void setDictid(String dictid) {
		this.dictid = dictid;
	}
	public String getDictname() {
		return dictname;
	}
	public void setDictname(String dictname) {
		this.dictname = dictname;
	}
}
