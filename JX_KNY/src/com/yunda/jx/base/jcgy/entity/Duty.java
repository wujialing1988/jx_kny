package com.yunda.jx.base.jcgy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * TODO 未见使用，J_JCGY_DUTY表也被删除，待确定无用后删除
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Duty实体类, 数据表：责任划分
 * <li>创建人：王治龙
 * <li>创建日期：2012-11-02
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="J_JCGY_DUTY")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class Duty implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 责任编码（主键） */
    @Id
	@Column(name="DUTY_ID")
	private String dutyID;
	/* 责任名称 */
	@Column(name="DUTY_NAME")
	private String dutyName;

	/**
	 * @return String 获取责任编码（主键）
	 */
	public String getDutyID(){
		return dutyID;
	}
	/**
	 * @param 设置责任编码（主键）
	 */
	public void setDutyID(String dutyID) {
		this.dutyID = dutyID;
	}
	/**
	 * @return String 获取责任名称
	 */
	public String getDutyName(){
		return dutyName;
	}
	/**
	 * @param 设置责任名称
	 */
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
}