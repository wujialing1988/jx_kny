package com.yunda.jxpz.equipinfo.entity;

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
 * <li>说明：DeviceType实体类, 数据表：设备分类
 * <li>创建人：刘晓斌
 * <li>创建日期：2015-01-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXPZ_Device_Type")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class DeviceType implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 设备分类编码 */
	@Column(name="Device_Type_Code")
	private String deviceTypeCode;
	/* 设备分类名称 */
	@Column(name="Device_Type_Name")
	private String deviceTypeName;
	/* 设备分类描述 */
	@Column(name="Device_Type_Desc")
	private String deviceTypeDesc;

	/**
	 * @return String 获取设备分类编码
	 */
	public String getDeviceTypeCode(){
		return deviceTypeCode;
	}
	/**
	 * @param 设置设备分类编码
	 */
	public void setDeviceTypeCode(String deviceTypeCode) {
		this.deviceTypeCode = deviceTypeCode;
	}
	/**
	 * @return String 获取设备分类名称
	 */
	public String getDeviceTypeName(){
		return deviceTypeName;
	}
	/**
	 * @param 设置设备分类名称
	 */
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	/**
	 * @return String 获取设备分类描述
	 */
	public String getDeviceTypeDesc(){
		return deviceTypeDesc;
	}
	/**
	 * @param 设置设备分类描述
	 */
	public void setDeviceTypeDesc(String deviceTypeDesc) {
		this.deviceTypeDesc = deviceTypeDesc;
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