package com.yunda.jxpz.systemconfig.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统配置项
 * <li>创建人：程梅
 * <li>创建日期：2013-7-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXPZ_SYSTEM_CONFIG")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class SystemConfig implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 主键   键 */
	@Id
	@Column(name="KEY")
	private String key;
	/* 编码*/
	private String id;
	/* 名称 */
	@Column(name="CONFIGNAME")
	private String configName;
	/* 键值 */
	@Column(name="KEYVALUE")
	private String keyValue;
	/* 父级键 */
	@Column(name="PARENTKEY")
	private String parentKey;
	/* 配置描述 */
	@Column(name="CONFIGDESC")
	private String configDesc;
	/* 配置说明 */
	@Column(name="CONFIGDECLARE")
	private String configDeclare;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getConfigDeclare() {
		return configDeclare;
	}
	public void setConfigDeclare(String configDeclare) {
		this.configDeclare = configDeclare;
	}
	public String getConfigDesc() {
		return configDesc;
	}
	public void setConfigDesc(String configDesc) {
		this.configDesc = configDesc;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	public String getParentKey() {
		return parentKey;
	}
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	public SystemConfig(){}
	
	/**
	 * Hibernate 查询构造函数
	 * @param key
	 * @param value
	 */
	public SystemConfig(String key, String value){
	    this.key = key;
	    this.keyValue = value;
	}
}