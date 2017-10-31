package com.yunda.frame.util.sqlmap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: sql语句标签对象
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-4-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XmlRootElement(name="sql")
@XmlAccessorType(XmlAccessType.FIELD)
public class SqlTag {
	
	@XmlAttribute(name="id")
	private String id;
	@XmlValue
	private String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
