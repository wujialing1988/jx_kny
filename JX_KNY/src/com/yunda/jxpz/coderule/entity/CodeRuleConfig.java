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
 * <li>说明：CodeRuleConfig实体类, 数据表：业务编码规则配置
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-10-09
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity @Table(name="JXPZ_Code_Rule_Config") @Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class CodeRuleConfig implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 规则名称 */
	@Column(name="Rule_Name")
	private String ruleName;
	/* 功能点(表名+字段名) */
	@Column(name="Rule_Function")
	private String ruleFunction;

	/**
	 * @return String 获取规则名称
	 */
	public String getRuleName(){
		return ruleName;
	}
	/**
	 * @param 设置规则名称
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	/**
	 * @return String 获取功能点
	 */
	public String getRuleFunction(){
		return ruleFunction;
	}
	/**
	 * @param 设置功能点
	 */
	public void setRuleFunction(String ruleFunction) {
		this.ruleFunction = ruleFunction;
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