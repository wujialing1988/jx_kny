package com.yunda.sb.equipmentprimaryinfo.entity;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：设备主要信息-附属设备
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "E_AFFILIATED_EQUIPMENT")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class AffiliatedEquipment implements java.io.Serializable {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/* idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/* 主设备主键 */
	@Column(name = "equipment_idx")
	private String equipmentIdx;

	/* 设备编号 */
	@Column(name = "equipment_no")
	private String equipmentNo;

	/* 设备名称 */
	@Column(name = "equipment_name")
	private String equipmentName;

	/* 设备型号 */
	private String modal;

	/* 设备规格 */
	private String specification;

	/* 机械系数 */
	@Column(name = "mechanical_coefficient")
	private Integer mechanicalcoefficient;

	/* 电气系数 */
	@Column(name = "electric_coefficient")
	private Integer electriccoefficient;

	/* 数量 */
	private Integer count;

	/* 单位 */
	private String unit;

	/* 功率 */
	private Integer power;

	/* 单价 */
	private Float price;

	/* 生产厂家 */
	@Column(name = "make_factory")
	private String makeFactory;

	/* 备注 */
	private String remark;

	/* 数据状态 */
	@Column(name = "record_status")
	private Integer recordStatus;

	/* 创建人 */
	@Column(updatable = false)
	private Long creator;

	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", updatable = false)
	private java.util.Date createTime;

	/* 修改人 */
	private Long updator;

	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private java.util.Date updateTime;

	public String getEquipmentIdx() {
		return equipmentIdx;
	}

	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
	}

	/**
	 * @return String 获取设备编号
	 */
	public String getEquipmentNo() {
		return equipmentNo;
	}

	/**
	 * @param equipmentNo 设置设备编号
	 */
	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	/**
	 * @return String 获取设备名称
	 */
	public String getEquipmentName() {
		return equipmentName;
	}

	/**
	 * @param equipmentName 设置设备名称
	 */
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	/**
	 * @return String 获取设备型号
	 */
	public String getModal() {
		return modal;
	}

	/**
	 * @param modal 设置设备型号
	 */
	public void setModal(String modal) {
		this.modal = modal;
	}

	/**
	 * @return String 获取设备规格
	 */
	public String getSpecification() {
		return specification;
	}

	/**
	 * @param specification 设置设备规格
	 */
	public void setSpecification(String specification) {
		this.specification = specification;
	}

	/**
	 * @return Float 获取机械系数
	 */
	public Integer getMechanicalcoefficient() {
		return mechanicalcoefficient;
	}

	/**
	 * @param mechanicalcoefficient 设置机械系数
	 */
	public void setMechanicalcoefficient(Integer mechanicalcoefficient) {
		this.mechanicalcoefficient = mechanicalcoefficient;
	}

	/**
	 * @return Float 获取电气系数
	 */
	public Integer getElectriccoefficient() {
		return electriccoefficient;
	}

	/**
	 * @param electriccoefficient 设置电气系数
	 */
	public void setElectriccoefficient(Integer electriccoefficient) {
		this.electriccoefficient = electriccoefficient;
	}

	/**
	 * @return Integer 获取数量
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count 设置数量
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * @return String 获取单位
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit 设置单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return Integer 获取功率
	 */
	public Integer getPower() {
		return power;
	}

	/**
	 * @param power 设置功率
	 */
	public void setPower(Integer power) {
		this.power = power;
	}

	/**
	 * @return Integer 获取单价
	 */
	public Float getPrice() {
		return price;
	}

	/**
	 * @param price 设置单价
	 */
	public void setPrice(Float price) {
		this.price = price;
	}

	/**
	 * @return String 获取生产厂家
	 */
	public String getMakeFactory() {
		return makeFactory;
	}

	/**
	 * @param makeFactory 设置生产厂家
	 */
	public void setMakeFactory(String makeFactory) {
		this.makeFactory = makeFactory;
	}

	/**
	 * @return String 获取备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark 设置备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return Integer 获取数据状态
	 */
	public Integer getRecordStatus() {
		return recordStatus;
	}

	/**
	 * @param recordStatus 设置数据状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	/**
	 * @return Long 获取创建人
	 */
	public Long getCreator() {
		return creator;
	}

	/**
	 * @param creator 设置创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}

	/**
	 * @return java.util.Date 获取创建时间
	 */
	public java.util.Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime 设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return Long 获取修改人
	 */
	public Long getUpdator() {
		return updator;
	}

	/**
	 * @param updator 设置修改人
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	/**
	 * @return java.util.Date 获取修改时间
	 */
	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime 设置修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}

	/**
	 * @param idx 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
}
