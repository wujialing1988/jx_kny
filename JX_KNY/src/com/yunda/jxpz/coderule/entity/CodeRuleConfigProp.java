package com.yunda.jxpz.coderule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：CodeRuleConfigProp实体类, 数据表：业务编码规则配置属性
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-10-09
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity @Table(name="JXPZ_Code_Rule_Config_Prop") @Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class CodeRuleConfigProp implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 属性名称 */
	@Column(name="property_Name")
	private String propertyName;
	/* 属性值 */
	@Column(name="property_Value")
	private String propertyValue;
	/* 属性类型 */
	@Column(name="property_Type")
	private Integer propertyType;
	/* 排序 */
	@Column(name="Order_No")
	private Integer orderNo;
	/* 规则配置idx */
	@Column(name="rule_IDX")
	private String ruleIDX;

	/**
	 * @return String 获取属性名称
	 */
	public String getPropertyName(){
		return propertyName;
	}
	/**
	 * @param 设置属性名称
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	/**
	 * @return String 获取属性值
	 */
	public String getPropertyValue(){
		return propertyValue;
	}
	/**
	 * @param 设置属性值
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	/**
	 * @return Integer 获取属性类型
	 */
	public Integer getPropertyType(){
		return propertyType;
	}
	/**
	 * @param 设置属性类型
	 */
	public void setPropertyType(Integer propertyType) {
		this.propertyType = propertyType;
	}
	/**
	 * @return Integer 获取排序号
	 */
	public Integer getOrderNo(){
		return orderNo;
	}
	/**
	 * @param 设置排序号
	 */
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * @return String 获取规则配置idx
	 */
	public String getRuleIDX(){
		return ruleIDX;
	}
	/**
	 * @param 设置规则配置idx
	 */
	public void setRuleIDX(String ruleIDX) {
		this.ruleIDX = ruleIDX;
	}
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	/**
	 * @param 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
}