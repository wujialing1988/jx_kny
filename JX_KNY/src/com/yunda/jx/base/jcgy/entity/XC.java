package com.yunda.jx.base.jcgy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：修程实体类, 数据表：Jcgy_xc
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
@Table(name="J_JCGY_XC")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class XC implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 修程编码 */
    @Id
	@Column(name="XC_ID")
	private String xcID;
	/* 修程名称 */
	@Column(name="XC_NAME")
	private String xcName;
	/* 修程类型 */
	@Column(name="xc_type")
	private String xcType;
    
    /* 客货类型 10 货车 20 客车 */
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;
    
	/**
	 * @return String 获取修程编码
	 */
	public String getXcID(){
		return xcID;
	}
	/**
	 * @param xcID 设置修程编码
	 */
	public void setXcID(String xcID) {
		this.xcID = xcID;
	}
	/**
	 * @return String 获取修程名称
	 */
	public String getXcName(){
		return xcName;
	}
	/**
	 * @param xcName 设置修程名称
	 */
	public void setXcName(String xcName) {
		this.xcName = xcName;
	}
	public String getXcType() {
		return xcType;
	}
	public void setXcType(String xcType) {
		this.xcType = xcType;
	}
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    
}
