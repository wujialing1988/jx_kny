package com.yunda.jx.base.jcgy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainType实体类, 表：J_JCGY_TRAIN_TYPE
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="J_JCGY_TRAIN_TYPE")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TrainType implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 车型代码 */
	@Id	
	@Column(name="T_TYPE_ID")
	private String typeID;
	/* 车型名称 */
	@Column(name="T_TYPE_NAME")
	private String typeName;
	/* 简称 */
	private String shortName;
	/* 拼音码 */
	private String spell;
	/* 别名 */
	private String alias;
	/* 小辅修公里 */
	private Long kmlightRepair;
	/* 功率系数 */
	private Double powerQuotiety;
	/* 车号 */
	@Transient
	private String trainNo;
	/* 动力类型 */
	@Column(name="power_type")
	private String powerType;
	/* 修程类型 */
	@Column(name="repair_type")
	private String repairType;
	/**
	 * @return String 获取车型代码
	 */
	public String getTypeID(){
		return typeID;
	}
	/**
	 * @param typeID 设置车型代码
	 */
	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}
	/**
	 * @return String 获取车型名称
	 */
	public String getTypeName(){
		return typeName;
	}
	/**
	 * @param typeName 设置车型名称
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * @return String 获取简称
	 */
	public String getShortName(){
		return shortName;
	}
	/**
	 * @param shortName 设置简称
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	/**
	 * @return String 获取拼音码
	 */
	public String getSpell(){
		return spell;
	}
	/**
	 * @param spell 设置拼音码
	 */
	public void setSpell(String spell) {
		this.spell = spell;
	}
	/**
	 * @return String 获取别名
	 */
	public String getAlias(){
		return alias;
	}
	/**
	 * @param alias 设置别名
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	/**
	 * @return Long 获取小辅修公里
	 */
	public Long getKmlightRepair(){
		return kmlightRepair;
	}
	/**
	 * @param kmlightRepair 设置小辅修公里
	 */
	public void setKmlightRepair(Long kmlightRepair) {
		this.kmlightRepair = kmlightRepair;
	}
	/**
	 * @return Double 获取功率系数
	 */
	public Double getPowerQuotiety(){
		return powerQuotiety;
	}
	/**
	 * @param powerQuotiety 设置功率系数
	 */
	public void setPowerQuotiety(Double powerQuotiety) {
		this.powerQuotiety = powerQuotiety;
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public String getPowerType() {
		return powerType;
	}
	public void setPowerType(String powerType) {
		this.powerType = powerType;
	}
	public String getRepairType() {
		return repairType;
	}
	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}
	
}
