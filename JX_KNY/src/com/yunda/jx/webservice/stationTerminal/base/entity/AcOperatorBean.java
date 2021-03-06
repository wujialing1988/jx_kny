package com.yunda.jx.webservice.stationTerminal.base.entity;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于登录用户信息实体包装
 * <li>说明: 用于login接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class AcOperatorBean implements java.io.Serializable {
	
	private Long operatorid;  //操作员ID 

	private String userid;  //登录用户名

	private String operatorname; //操作员名称

	public Long getOperatorid() {
		return operatorid;
	}

	public void setOperatorid(Long operatorid) {
		this.operatorid = operatorid;
	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
}
