package com.yunda.sb.equipmentprimaryinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：EquipmentUnionRFID查询实体
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
public class EquipmentUnionRFIDBean implements Serializable {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 设备idx主键 */
	@Id
	private String idx;

	/** 设备类别编码 */
	@Column(name = "class_code")
	private String classCode;

	/** 设备类别名称*/
	@Column(name = "class_name")
	private String className;

	/** 设备名称 */
	@Column(name = "equipment_name")
	private String equipmentName;

	/** 设备编码 */
	@Column(name = "equipment_code")
	private String equipmentCode;

	/** 型号 */
	private String model;

	/** 规格 */
	private String specification;

	/** 设置地点 */
	@Column(name = "use_place")
	private String usePlace;

	/** 设备编码（标识已关联RFID） */
	@Column(name = "rfid_code")
	private String rfidCode;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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

	public String getUsePlace() {
		return usePlace;
	}

	public void setUsePlace(String usePlace) {
		this.usePlace = usePlace;
	}

	public String getRfidCode() {
		return rfidCode;
	}

	public void setRfidCode(String rfidCode) {
		this.rfidCode = rfidCode;
	}

}
