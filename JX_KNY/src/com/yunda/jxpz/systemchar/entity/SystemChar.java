package com.yunda.jxpz.systemchar.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：SystemChar实体类, 数据表：特殊字符
 * <li>创建人：程锐
 * <li>创建日期：2013-07-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXPZ_System_Char")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class SystemChar implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;	
	/* 编码 */    
    @Id 
	private String id;
	/* 字符 */
	@Column(name="Special_Char")
	private String specialChar;
	/* 特殊字符描述 */
	@Column(name="Special_Char_Desc")
	private String specialCharDesc;

	/**
	 * @return String 获取编码
	 */
	public String getId(){
		return id;
	}
	/**
	 * @param 设置编码
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return String 获取特殊字符
	 */
	public String getSpecialChar(){
		return specialChar;
	}
	/**
	 * @param 设置特殊字符
	 */
	public void setSpecialChar(String specialChar) {
		this.specialChar = specialChar;
	}
	/**
	 * @return String 获取特殊字符描述
	 */
	public String getSpecialCharDesc(){
		return specialCharDesc;
	}
	/**
	 * @param 设置特殊字符描述
	 */
	public void setSpecialCharDesc(String specialCharDesc) {
		this.specialCharDesc = specialCharDesc;
	}
}