package com.yunda.frame.yhgl.entity;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 数据字典包装类
 * <li>创建人：程锐
 * <li>创建日期：2014-10-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class EosDictEntryBean implements java.io.Serializable{
	private String dictid ; 
	private String dictname ; 
	
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
