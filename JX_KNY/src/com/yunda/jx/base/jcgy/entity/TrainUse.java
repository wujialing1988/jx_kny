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
 * <li>说明：TrainUse实体类, 数据表：Jcgy_train_use
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-31
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="J_JCGY_TRAIN_USE")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TrainUse implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/* 机车用途代码 */
	@Column(name="T_USE_ID")
	@GenericGenerator(strategy="assigned", name = "paymentableGenerator")	
    @Id @GeneratedValue(generator="paymentableGenerator")
	private String useID;
	/* 机车用途名称 */
	@Column(name="T_USE_NAME")
	private String useName;

	/**
	 * @return String 获取机车用途代码
	 */
	public String getUseID(){
		return useID;
	}
	/**
	 * @param 设置机车用途代码
	 */
	public void setUseID(String useID) {
		this.useID = useID;
	}
	/**
	 * @return String 获取机车用途名称
	 */
	public String getUseName(){
		return useName;
	}
	/**
	 * @param 设置机车用途名称
	 */
	public void setUseName(String useName) {
		this.useName = useName;
	}
}