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
 * <li>说明：EquipFault实体类, 数据表：故障现象编码
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
@Table(name="J_JCGY_EQUIP_FAULT")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class EquipFault implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 故障编号 */
	@Column(name="FAULT_ID")
	@GenericGenerator(strategy="assigned", name = "paymentableGenerator")  
    @Id @GeneratedValue(generator="paymentableGenerator")
	private String FaultID;
	/* 故障名称 */
	@Column(name="FAULT_NAME")
	private String FaultName;
	/* 故障类别 */
	@Column(name="FAULT_TYPE_ID")
	private String FaultTypeID;

	/**
	 * EquipFault空构造
	 */
	public EquipFault(){}
	
	/**
	 * hiberante查询构造
	 * @param faultId
	 * @param faultName
	 */
	public EquipFault(String faultId, String faultName){
	    this.FaultID = faultId;
	    this.FaultName = faultName;
	}
	/**
	 * @return String 获取故障编号
	 */
	public String getFaultID(){
		return FaultID;
	}
	/**
	 * @param 设置故障编号
	 */
	public void setFaultID(String FaultID) {
		this.FaultID = FaultID;
	}
	/**
	 * @return String 获取故障名称
	 */
	public String getFaultName(){
		return FaultName;
	}
	/**
	 * @param 设置故障名称
	 */
	public void setFaultName(String FaultName) {
		this.FaultName = FaultName;
	}
	/**
	 * @return String 获取故障类别
	 */
	public String getFaultTypeID(){
		return FaultTypeID;
	}
	/**
	 * @param 设置故障类别
	 */
	public void setFaultTypeID(String FaultTypeID) {
		this.FaultTypeID = FaultTypeID;
	}
}