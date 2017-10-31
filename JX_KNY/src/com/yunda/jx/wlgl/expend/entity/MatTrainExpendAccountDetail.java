package com.yunda.jx.wlgl.expend.entity;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatOutWHNoTrainDetail实体类
 * <li>创建人： 何涛
 * <li>创建日期： 2014-9-26 上午10:59:36
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class MatTrainExpendAccountDetail {
	
	/** 物料编码 */
	private String matCode;

	/** 物料描述 */
	private String matDesc;

	/** 单价 */
	private Float price;

	/** 数量 */
	private Integer qty;

	/** 单位 */
	private String unit;
	
	/**
	 * @return String 获取物料编码
	 */
	public String getMatCode() {
		return matCode;
	}

	/**
	 * @return String 获取物料描述
	 */
	public String getMatDesc() {
		return matDesc;
	}

	/**
	 * @return 获取单价
	 */
	public Float getPrice() {
		return price;
	}

	/**
	 * @return Integer 获取数量
	 */
	public Integer getQty() {
		return qty;
	}

	/**
	 * @return String 获取单位
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param 设置物料编码
	 */
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	/**
	 * @param 设置物料描述
	 */
	public void setMatDesc(String matDesc) {
		this.matDesc = matDesc;
	}

	/**
	 * @param price 设置
	 */
	public void setPrice(Float price) {
		this.price = price;
	}

	/**
	 * @param 设置数量
	 */
	public void setQty(Integer qty) {
		this.qty = qty;
	}


	/**
	 * @param 设置单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
}