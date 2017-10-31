package com.yunda.frame.common;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 多子系统简称与名称配置实体
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
public class SubSystem {
	@XmlElement(name="subSysInfo")
	private List<SubSysInfo> subSysInfo;
	
	public SubSystem(){}

	public List<SubSysInfo> getSubSysInfo() {
		return subSysInfo;
	}

	public void setSubSysInfo(List<SubSysInfo> subSysInfo) {
		this.subSysInfo = subSysInfo;
	}
	
	
}
