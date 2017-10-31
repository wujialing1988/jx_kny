package com.yunda.sb.pointcheck.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：PointCheck，数据表：设备点检任务单
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
@Table(name = "E_SBJX_POINT_CHECK")
public class PointCheck implements Serializable {

	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 处理状态 - 未处理 */
	public static final String STATE_WCL = "未处理";

	/** 处理状态 - 已处理 */
	public static final String STATE_YCL = "已处理";

	/** 设备状态 - 启动 */
	public static final String EQUIPMENT_STATE_QD = "启动";

	/** 设备状态 - 停止 */
	public static final String EQUIPMENT_STATE_TZ = "停止";

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备idx主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

	/** 点检人 */
	@Column(name = "CHECK_EMP_ID")
	private String checkEmpId;

	/** 点检人名称 */
	@Column(name = "CHECK_EMP")
	private String checkEmp;

	/** 点检日期 */
	@Column(name = "CHECK_DATE")
	@Temporal(TemporalType.DATE)
	private Date checkDate;

	/** 点检时间，该字段用于计算“设备运转时间”的临时字段 */
	@Column(name = "CHECK_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkTime;

	/** 设备运转时间，单位：小时，运转时间 = 点检结束时间 - 点检开始时间 */
	@Column(name = "RUNNING_TIME")
	private Float runningTime;

	/** 计算设备运转时间 */
	@Column(name = "cal_running_time")
	private Float calRunningTime;

	/** 处理状态 */
	@Column(name = "state")
	private String state;

	/** 设备运行状态状态 */
	@Column(name = "EQUIPMENT_STATE")
	private String equipmentState;

	/* 数据状态 */
	@Column(name = "RECORD_STATUS")
	private Integer recordStatus;

	/* 创建人 */
	@Column(name = "CREATOR", updatable = false)
	private Long creator;

	/* 创建时间 */
	@Column(name = "CREATE_TIME", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date createTime;

	/* 修改人 */
	@Column(name = "UPDATOR")
	private Long updator;

	/* 修改时间 */
	@Column(name = "UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date updateTime;

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

	public String getCheckEmpId() {
		return checkEmpId;
	}

	public void setCheckEmpId(String checkEmpId) {
		this.checkEmpId = checkEmpId;
	}

	public String getCheckEmp() {
		return checkEmp;
	}

	public void setCheckEmp(String checkEmp) {
		this.checkEmp = checkEmp;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public Float getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(Float runningTime) {
		this.runningTime = runningTime;
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

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdator() {
		return updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public Float getCalRunningTime() {
		return calRunningTime;
	}

	public void setCalRunningTime(Float calRunningTime) {
		this.calRunningTime = calRunningTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/** 设备点检内容 */
	@Transient
	private List<PointCheckContent> checkContentList;

	/** 设备信息 */
	@Transient
	private EquipmentPrimaryInfo equipmentInfo;

	public String getEquipmentState() {
		return equipmentState;
	}

	public void setEquipmentState(String equipmentState) {
		this.equipmentState = equipmentState;
	}

	public List<PointCheckContent> getCheckContentList() {
		return checkContentList;
	}

	public void setCheckContentList(List<PointCheckContent> checkContentList) {
		this.checkContentList = checkContentList;
	}

	public EquipmentPrimaryInfo getEquipmentInfo() {
		return equipmentInfo;
	}

	public void setEquipmentInfo(EquipmentPrimaryInfo equipmentInfo) {
		this.equipmentInfo = equipmentInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((calRunningTime == null) ? 0 : calRunningTime.hashCode());
		result = prime * result + ((checkContentList == null) ? 0 : checkContentList.hashCode());
		result = prime * result + ((checkDate == null) ? 0 : checkDate.hashCode());
		result = prime * result + ((checkEmp == null) ? 0 : checkEmp.hashCode());
		result = prime * result + ((checkEmpId == null) ? 0 : checkEmpId.hashCode());
		result = prime * result + ((checkTime == null) ? 0 : checkTime.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((equipmentIdx == null) ? 0 : equipmentIdx.hashCode());
		result = prime * result + ((equipmentInfo == null) ? 0 : equipmentInfo.hashCode());
		result = prime * result + ((equipmentState == null) ? 0 : equipmentState.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((runningTime == null) ? 0 : runningTime.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		PointCheck other = (PointCheck) obj;
		if (calRunningTime == null) {
			if (other.calRunningTime != null)
				return false;
		} else if (!calRunningTime.equals(other.calRunningTime))
			return false;
		if (checkContentList == null) {
			if (other.checkContentList != null)
				return false;
		} else if (!checkContentList.equals(other.checkContentList))
			return false;
		if (checkDate == null) {
			if (other.checkDate != null)
				return false;
		} else if (!checkDate.equals(other.checkDate))
			return false;
		if (checkEmp == null) {
			if (other.checkEmp != null)
				return false;
		} else if (!checkEmp.equals(other.checkEmp))
			return false;
		if (checkEmpId == null) {
			if (other.checkEmpId != null)
				return false;
		} else if (!checkEmpId.equals(other.checkEmpId))
			return false;
		if (checkTime == null) {
			if (other.checkTime != null)
				return false;
		} else if (!checkTime.equals(other.checkTime))
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
		if (equipmentIdx == null) {
			if (other.equipmentIdx != null)
				return false;
		} else if (!equipmentIdx.equals(other.equipmentIdx))
			return false;
		if (equipmentInfo == null) {
			if (other.equipmentInfo != null)
				return false;
		} else if (!equipmentInfo.equals(other.equipmentInfo))
			return false;
		if (equipmentState == null) {
			if (other.equipmentState != null)
				return false;
		} else if (!equipmentState.equals(other.equipmentState))
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
		if (runningTime == null) {
			if (other.runningTime != null)
				return false;
		} else if (!runningTime.equals(other.runningTime))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
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

}
