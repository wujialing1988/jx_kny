package com.yunda.passenger.marshalling.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 编组车辆信息视图
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "V_K_MARSHALLING_TRAIN_COUNT")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class MarshallingTrainCountView implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    @Id
    private String idx;
    /* 编组编号 */ 
    @Column(name = "MARSHALLING_CODE")
    private java.lang.String marshallingCode;       
    
    /* 车种编号 */ 
    @Column(name = "T_VEHICLE_KIND_CODE")
    private java.lang.String vehicleKindCode;   
    
    /* 车种名称 */ 
    @Column(name = "T_VEHICLE_KIND_NAME_COUNT")
    private java.lang.String vehicleKindNameCount;   
    

	public java.lang.String getVehicleKindCode() {
		return vehicleKindCode;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public void setVehicleKindCode(String vehicleKindCode) {
		this.vehicleKindCode = vehicleKindCode;
	}


	public String getMarshallingCode() {
		return marshallingCode;
	}

	public void setMarshallingCode(String marshallingCode) {
		this.marshallingCode = marshallingCode;
	}

	public java.lang.String getVehicleKindNameCount() {
		return vehicleKindNameCount;
	}

	public void setVehicleKindNameCount(java.lang.String vehicleKindNameCount) {
		this.vehicleKindNameCount = vehicleKindNameCount;
	}

}

