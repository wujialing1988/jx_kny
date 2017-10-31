package com.yunda.frame.util.sqlmap;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: sql文件映射对象
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-4-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XmlRootElement(name="SqlMap")
@XmlAccessorType(XmlAccessType.FIELD)
public final class SqlMap {
	
	/** SQL映射语句对象列表 */
	@XmlElement(name="sql")
	private List<SqlTag> sql;
	
	public List<SqlTag> getSql() {
		return sql;
	}

	public void setSql(List<SqlTag> sql) {
		this.sql = sql;
	}

}