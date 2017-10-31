package com.yunda.jx.webservice.stationTerminal.base.entity;

import java.io.Serializable;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：获取系统特殊字符列表实体包装
 * <li>说明: 用于getSystemCharList接口方法
 * <li>创建人：王斌
 * <li>创建日期：2013-12-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SystemCharBean implements Serializable{

	/**
     * 
     */

    private String id	;//	编码
	
	private String specialChar	;//	字符
	
	private String specialCharDesc	;// 特殊字符描述

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpecialChar() {
		return specialChar;
	}

	public void setSpecialChar(String specialChar) {
		this.specialChar = specialChar;
	}

	public String getSpecialCharDesc() {
		return specialCharDesc;
	}

	public void setSpecialCharDesc(String specialCharDesc) {
		this.specialCharDesc = specialCharDesc;
	}
    
}
