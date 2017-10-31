package com.yunda.sb.pointcheck.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：EquipmentShutdown，数据表：设备停机登记
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
@Table(name = "E_SBJX_EQUIPMENT_SHUTDOWN")
public class EquipmentShutdown implements Serializable {

	/** 使用默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 停机类型 - 临修 */
	public static final String SHUTDOWN_TYPE_LX = "临修";

	/** 停机类型 - 小修 */
	public static final String SHUTDOWN_TYPE_XX = "小修";

	/** 停机类型 - 中修 */
	public static final String SHUTDOWN_TYPE_ZX = "中修";

	/** 停机类型 - 项修 */
	public static final String SHUTDOWN_TYPE_XIX = "项修";

	/** 停机类型 - 缺勤 */
	public static final String SHUTDOWN_TYPE_QQ = "缺勤";

	/** 停机类型 - 停工待料 */
	public static final String SHUTDOWN_TYPE_TGDL = "停工待料";

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备idx主键 */
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

	/** 停机类型：临修、小修、中修、项修、缺勤、停工待料 */
	private String type;

	/** 停机开始时间 */
	@Column(name = "start_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	/** 停机结束时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time")
	private Date endTime;

	/* 数据状态 */
	@Column(name = "RECORD_STATUS")
	private Integer recordStatus;

	/* 创建人 */
	@Column(name = "CREATOR", updatable = false)
	private Long creator;

	/* 创建时间 */
	@Column(name = "CREATE_TIME", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	/* 修改人 */
	@Column(name = "UPDATOR")
	private Long updator;

	/* 修改时间 */
	@Column(name = "UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	/**
	 * <li>说明：设置设备idx主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentIdx 设备idx主键
	 * @return 设备停机登记实体对象
	 */
	public EquipmentShutdown equipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
		return this;
	}

	/**
	 * <li>说明：设置设备编码
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentName 设备编码
	 * @return 设备停机登记实体对象
	 */
	public EquipmentShutdown equipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
		return this;
	}

	/**
	 * <li>说明：设置设备名称
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param equipmentName 设备名称
	 * @return 设备停机登记实体对象
	 */
	public EquipmentShutdown equipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
		return this;
	}

	/**
	 * <li>说明：设置设备类别编码
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备类别编码
	 * @return 设备停机登记实体对象
	 */
	public EquipmentShutdown classCode(String classCode) {
		this.classCode = classCode;
		return this;
	}

	/**
	 * <li>说明：设置设备类别名称
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param className 设备类别编码
	 * @return 设备停机登记实体对象
	 */
	public EquipmentShutdown className(String className) {
		this.className = className;
		return this;
	}

	/**
	 * <li>说明：设置停机类型
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param type 停机类型：临修、小修、中修、项修、缺勤、停工待料
	 * @return 设备停机登记实体对象
	 */
	public EquipmentShutdown type(String type) {
		this.type = type;
		return this;
	}

	/**
	 * <li>说明：设置停机开始时间
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param startTime 停机开始时间
	 * @return 设备停机登记实体对象
	 */
	public EquipmentShutdown startTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}

	/**
	 * <li>说明：设置停机结束时间
	 * <li>创建人：何涛
	 * <li>创建日期：2016年11月3日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param startTime 停机结束时间
	 * @return 设备停机登记实体对象
	 */
	public EquipmentShutdown endTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdator() {
		return updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classCode == null) ? 0 : classCode.hashCode());
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((equipmentCode == null) ? 0 : equipmentCode.hashCode());
		result = prime * result + ((equipmentIdx == null) ? 0 : equipmentIdx.hashCode());
		result = prime * result + ((equipmentName == null) ? 0 : equipmentName.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		result = prime * result + ((updator == null) ? 0 : updator.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EquipmentShutdown other = (EquipmentShutdown) obj;
		if (classCode == null) {
			if (other.classCode != null)
				return false;
		} else if (!classCode.equals(other.classCode))
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (equipmentCode == null) {
			if (other.equipmentCode != null)
				return false;
		} else if (!equipmentCode.equals(other.equipmentCode))
			return false;
		if (equipmentIdx == null) {
			if (other.equipmentIdx != null)
				return false;
		} else if (!equipmentIdx.equals(other.equipmentIdx))
			return false;
		if (equipmentName == null) {
			if (other.equipmentName != null)
				return false;
		} else if (!equipmentName.equals(other.equipmentName))
			return false;
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (recordStatus == null) {
			if (other.recordStatus != null)
				return false;
		} else if (!recordStatus.equals(other.recordStatus))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (updateTime == null) {
			if (other.updateTime != null)
				return false;
		} else if (!updateTime.equals(other.updateTime))
			return false;
		if (updator == null) {
			if (other.updator != null)
				return false;
		} else if (!updator.equals(other.updator))
			return false;
		return true;
	}

	public static void main(String[] args) {
		EquipmentShutdown es = new EquipmentShutdown();
		es.setType("临修");
	}

}
