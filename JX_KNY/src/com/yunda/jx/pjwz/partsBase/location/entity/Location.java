package com.yunda.jx.pjwz.partsBase.location.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Location实体类, 存放位置
 * <li>创建人：程梅
 * <li>创建日期：2015-09-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
public class Location{
    /* 字典项ID */
    @Id
	private String dictItemId;
	/* 字典编码 */
	private String dictTypeId;
	/* 短语描述 */
	private String dictItemDesc;

	/**
	 * @return String 获取字典项ID
	 */
	public String getDictItemId(){
		return dictItemId;
	}
	/**
	 * @param dictItemId 设置字典项ID
	 */
	public void setDictItemId(String dictItemId) {
		this.dictItemId = dictItemId;
	}
	/**
	 * @return String 获取字典编码
	 */
	public String getDictTypeId(){
		return dictTypeId;
	}
	/**
	 * @param dictTypeId 设置字典编码
	 */
	public void setDictTypeId(String dictTypeId) {
		this.dictTypeId = dictTypeId;
	}
	/**
	 * @return String 获取短语描述
	 */
	public String getDictItemDesc(){
		return dictItemDesc;
	}
	/**
	 * @param dictItemDesc 设置短语描述
	 */
	public void setDictItemDesc(String dictItemDesc) {
		this.dictItemDesc = dictItemDesc;
	}
}