package com.yunda.sb.equipmentprimaryinfo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：EquipmentUnionRFID，数据表：设备RFID关联
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
@Table(name = "E_EQUIPMENT_UNION_RFID")
public class EquipmentUnionRFID implements Serializable {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 设备编码（标识已关联RFID） */
	@Id
	@Column(name = "rfid_code")
	private String rfidCode;

	public String getRfidCode() {
		return rfidCode;
	}

	public void setRfidCode(String rfidCode) {
		this.rfidCode = rfidCode;
	}
}
