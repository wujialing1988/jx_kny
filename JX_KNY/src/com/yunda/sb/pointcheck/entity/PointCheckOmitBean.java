package com.yunda.sb.pointcheck.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：PointCheckOmitBean 点检漏检统计查询实体
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
public class PointCheckOmitBean implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	@Id
	/** 设备idx主键 */
	@Column(name = "IDX")
	private String equipmentIdx;

	/** 设备名称 */
	@Column(name = "EQUIPMENT_NAME")
	private String equipmentName;

	/** 设备编码 */
	@Column(name = "EQUIPMENT_CODE")
	private String equipmentCode;

	/** 规格 */
	@Column(name = "SPECIFICATION")
	private String specification;

	/** 型号 */
	@Column(name = "MODEL")
	private String model;

	/** 故障次数 */
	@Column(name = "OMITCOUNT")
	private Integer omitCount;

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

	public Integer getOmitCount() {
		return omitCount;
	}

	public void setOmitCount(Integer omitCount) {
		this.omitCount = omitCount;
	}

}
