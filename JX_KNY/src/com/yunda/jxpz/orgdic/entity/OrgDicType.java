package com.yunda.jxpz.orgdic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：OrgDicType实体类, 数据表：常用部门字典
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
@Table(name="JXPZ_ORG_DIC_TYPE")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class OrgDicType implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 字典编码 */
    @Id
	@Column(name="DICT_TYPE_ID")
	private String dictTypeId;
	/* 字典描述 */
	@Column(name="DICT_TYPE_DESC")
	private String dictTypeDesc;

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
	 * @return String 获取字典描述
	 */
	public String getDictTypeDesc(){
		return dictTypeDesc;
	}
	/**
	 * @param dictTypeDesc 设置字典描述
	 */
	public void setDictTypeDesc(String dictTypeDesc) {
		this.dictTypeDesc = dictTypeDesc;
	}
}