package com.yunda.jxpz.phrasedic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PhraseDicItem实体类, 数据表：常用短语字典项
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
@Table(name="JXPZ_PHRASE_DIC_ITEM")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PhraseDicItem implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /* 字典项ID */
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
	@Column(name="DICT_ITEM_ID")
	private String dictItemId;
	/* 字典编码 */
	@Column(name="DICT_TYPE_ID")
	private String dictTypeId;
	/* 短语描述 */
	@Column(name="DICT_ITEM_DESC")
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