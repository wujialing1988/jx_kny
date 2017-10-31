package com.yunda.sb.pointcheck.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：EquipmentShutdownBean，设备停机总时长统计查询实体
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
public class EquipmentShutdownBean implements Serializable {

	/** 使用默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 设备idx主键 */
	@Id
	@Column(name = "equipment_idx")
	private String equipmentIdx;

	/** 设备编码 */
	@Column(name = "equipment_code")
	private String equipmentCode;

	/** 设备名称 */
	@Column(name = "equipment_name")
	private String equipmentName;

	/** 设备类别编码 */
	@Column(name = "class_code")
	private String classCode;

	/** 设备类别名称 */
	@Column(name = "class_name")
	private String className;

	/** 停机总时长 */
	@Column(name = "total_count")
	private Float totalCount;

	/** 停机总时长_临修 */
	@Column(name = "total_count_lx")
	private Float totalCountLX;

	/** 停机总时长_小修 */
	@Column(name = "total_count_xx")
	private Float totalCountXX;

	/** 停机总时长_中修 */
	@Column(name = "total_count_zx")
	private Float totalCountZX;

	/** 停机总时长_项修 */
	@Column(name = "total_count_xiangx")
	private Float totalCountXiangX;

	/** 停机总时长_缺勤 */
	@Column(name = "total_count_qq")
	private Float totalCountQQ;

	/** 停机总时长_停工待料 */
	@Column(name = "total_count_tgdl")
	private Float totalCountTGDL;

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

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
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

	public Float getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Float totalCount) {
		this.totalCount = totalCount;
	}

	public Float getTotalCountLX() {
		return totalCountLX;
	}

	public void setTotalCountLX(Float totalCountLX) {
		this.totalCountLX = totalCountLX;
	}

	public Float getTotalCountXX() {
		return totalCountXX;
	}

	public void setTotalCountXX(Float totalCountXX) {
		this.totalCountXX = totalCountXX;
	}

	public Float getTotalCountZX() {
		return totalCountZX;
	}

	public void setTotalCountZX(Float totalCountZX) {
		this.totalCountZX = totalCountZX;
	}

	public Float getTotalCountXiangX() {
		return totalCountXiangX;
	}

	public void setTotalCountXiangX(Float totalCountXiangX) {
		this.totalCountXiangX = totalCountXiangX;
	}

	public Float getTotalCountQQ() {
		return totalCountQQ;
	}

	public void setTotalCountQQ(Float totalCountQQ) {
		this.totalCountQQ = totalCountQQ;
	}

	public Float getTotalCountTGDL() {
		return totalCountTGDL;
	}

	public void setTotalCountTGDL(Float totalCountTGDL) {
		this.totalCountTGDL = totalCountTGDL;
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
