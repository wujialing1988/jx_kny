package com.yunda.jx.base.jcgy.entity;

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
 * <li>说明：EquipFaultType实体类, 数据表：配件故障分类编码
 * <li>创建人：王治龙
 * <li>创建日期：2012-11-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="J_JCGY_EQUIP_FAULT_TYPE")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class EquipFaultType implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 配件故障分类代码 */
	@Column(name="FAULT_TYPE_ID")
	private String FaultTypeID;
	/* 配件故障分类名称 */
	@Column(name="FAULT_TYPE_NAME")
	private String FaultTypeName;

	/**
	 * @return String 获取配件故障分类代码
	 */
	public String getFaultTypeID(){
		return FaultTypeID;
	}
	/**
	 * @param 设置配件故障分类代码
	 */
	public void setFaultTypeID(String FaultTypeID) {
		this.FaultTypeID = FaultTypeID;
	}
	/**
	 * @return String 获取配件故障分类名称
	 */
	public String getFaultTypeName(){
		return FaultTypeName;
	}
	/**
	 * @param 设置配件故障分类名称
	 */
	public void setFaultTypeName(String FaultTypeName) {
		this.FaultTypeName = FaultTypeName;
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