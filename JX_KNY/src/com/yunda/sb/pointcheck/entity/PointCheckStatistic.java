package com.yunda.sb.pointcheck.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：PointCheck统计查询封装实体
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
public class PointCheckStatistic implements Serializable {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@Id
	private String idx;

	/** 设备名称 */
	@Column(name = "equipment_name")
	private String equipmentName;

	/** 设备编码 */
	@Column(name = "equipment_code")
	private String equipmentCode;

	/** 点检次数 */
	@Column(name = "check_count")
	private Short checkCount;

	/** 漏检次数 */
	@Column(name = "omit_count")
	private Short omitCount;

	/** 查询条件 - 年 */
	@Transient
	private Short year;

	/** 查询条件 - 月 */
	@Transient
	private Short month;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public Short getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(Short checkCount) {
		this.checkCount = checkCount;
	}

	public Short getYear() {
		return year;
	}

	public void setYear(Short year) {
		this.year = year;
	}

	public Short getMonth() {
		return month;
	}

	public void setMonth(Short month) {
		this.month = month;
	}

	public Short getOmitCount() {
		return omitCount;
	}

	public void setOmitCount(Short omitCount) {
		this.omitCount = omitCount;
	}

}
