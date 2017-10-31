package com.yunda.frame.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 子系统id和名称信息实体
 * <li>创建人：谭诚
 * <li>创建日期：2014-1-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SubSysInfo {
	@XmlAttribute(name="appid")
	private String appid;
	@XmlAttribute(name="appname")
	private String appname;
	
	public SubSysInfo(){}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}
	
	
}
