package com.yunda.sb.fault.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：FaultOrder统计查询实体
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
public class FaultOrderBean implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	@Id
	/** 设备idx主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

	/** 设备名称 */
	@Column(name = "EQUIPMENT_NAME")
	private String equipmentName;

	/** 设备编码 */
	@Column(name = "EQUIPMENT_CODE")
	private String equipmentCode;

	/** 规格 */
	private String specification;

	/** 型号 */
	private String model;

	/** 制造工厂 */
	@Column(name = "make_factory")
	private String makeFactory;

	/** 故障次数 */
	@Column(name = "fault_count")
	private Integer faultCount;

	@Transient
	/** 统计开始日期 */
	private Date startDate;

	@Transient
	/** 统计结束日期 */
	private Date endDate;

	public String getEquipmentIdx() {
		return equipmentIdx;
	}

	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getMakeFactory() {
		return makeFactory;
	}

	public void setMakeFactory(String makeFactory) {
		this.makeFactory = makeFactory;
	}

	public Integer getFaultCount() {
		return faultCount;
	}

	public void setFaultCount(Integer faultCount) {
		this.faultCount = faultCount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
