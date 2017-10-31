package com.yunda.sb.repair.plan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.yunda.sb.repair.process.entity.RepairTaskList;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairPlanMonth，数据表：设备检修月计划（new）
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月5日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
@Table(name = "E_SBJX_REPAIR_PLAN_MONTH")
public class RepairPlanMonth implements java.io.Serializable {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 计划状态 - 未下发【0】 */
	public static final int PLAN_STATUS_WXF = 0;

	/** 计划状态 - 已下发【1】 */
	public static final int PLAN_STATUS_YXF = 1;

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

	/** 计划年度 */
	@Column(name = "PLAN_YEAR")
	private Integer planYear;

	/** 计划月份 */
	@Column(name = "PLAN_MONTH")
	private Integer planMonth;

	/** 修程，1:小修、2：中修、3：项修  */
	@Column(name = "repair_class")
	private Short repairClass;

	/** 计划开始时间 */
	@Column(name = "BEGIN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date beginTime;

	/** 计划结束时间 */
	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date endTime;

	/** 计划状态，0：未下发、1：已下发 */
	@Column(name = "PLAN_STATUS")
	private Integer planStatus;

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

	/**
	 * @return 获取主键
	 */
	public String getIdx() {
		return this.idx;
	}

	/**
	 * @param idx 设置主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}

	/**
	 * @return 获取设备主键
	 */
	public String getEquipmentIdx() {
		return equipmentIdx;
	}

	/**
	 * @param equipmentIdx 设置设备主键
	 */
	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
	}

	/**
	 * @return 获取计划年度
	 */
	public Integer getPlanYear() {
		return planYear;
	}

	/**
	 * @param month 设置计划年度
	 */
	public void setPlanYear(Integer planYear) {
		this.planYear = planYear;
	}

	/**
	 * @return 获取计划月份
	 */
	public Integer getPlanMonth() {
		return this.planMonth;
	}

	/**
	 * @param month 设置计划月份
	 */
	public void setPlanMonth(Integer month) {
		this.planMonth = month;
	}

	/**
	 * @return 获取修别
	 */
	public Short getRepairClass() {
		return this.repairClass;
	}

	/**
	 * @param repairClass 设置修程
	 */
	public void setRepairClass(Short repairClass) {
		this.repairClass = repairClass;
	}

	/**
	 * @return 获取计划开始时间
	 */
	public java.util.Date getBeginTime() {
		return this.beginTime;
	}

	/**
	 * @param beginTime 设置计划开始时间
	 */
	public void setBeginTime(java.util.Date beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return 获取计划结束时间
	 */
	public java.util.Date getEndTime() {
		return this.endTime;
	}

	/**
	 * @param endTime 设置计划结束时间
	 */
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return 获取计划状态
	 */
	public Integer getPlanStatus() {
		return this.planStatus;
	}

	/**
	 * @param planStatus 设置计划状态
	 */
	public void setPlanStatus(Integer planStatus) {
		this.planStatus = planStatus;
	}

	/**
	 * @return 获取数据状态
	 */
	public Integer getRecordStatus() {
		return this.recordStatus;
	}

	/**
	 * @param recordStatus 设置数据状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	/**
	 * @return 获取创建人
	 */
	public Long getCreator() {
		return this.creator;
	}

	/**
	 * @param creator 设置创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}

	/**
	 * @return 获取创建时间
	 */
	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	/**
	 * @param createTime 设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return 获取修改人
	 */
	public Long getUpdator() {
		return this.updator;
	}

	/**
	 * @param updator 设置修改人
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	/**
	 * @return 获取修改时间
	 */
	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	/**
	 * @param updateTime 设置修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * <li>说明：获取修程的中文名称：小、中、项
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月23日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 修程的中文名称：小、中、项
	 */
	public String getRepairClassName() {
		if (RepairPlanYear.REPAIR_CLASS_SMALL == repairClass.intValue()) {
			return RepairTaskList.REPAIR_CLASS_NAME_SMALL;
		}
		if (RepairPlanYear.REPAIR_CLASS_MEDIUM == repairClass.intValue()) {
			return RepairTaskList.REPAIR_CLASS_NAME_MEDIUM;
		}
		if (RepairPlanYear.REPAIR_CLASS_SUBJECT == repairClass.intValue()) {
			return RepairTaskList.REPAIR_CLASS_NAME_SUBJECT;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beginTime == null) ? 0 : beginTime.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((equipmentIdx == null) ? 0 : equipmentIdx.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((planMonth == null) ? 0 : planMonth.hashCode());
		result = prime * result + ((planStatus == null) ? 0 : planStatus.hashCode());
		result = prime * result + ((planYear == null) ? 0 : planYear.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((repairClass == null) ? 0 : repairClass.hashCode());
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
		RepairPlanMonth other = (RepairPlanMonth) obj;
		if (beginTime == null) {
			if (other.beginTime != null)
				return false;
		} else if (!beginTime.equals(other.beginTime))
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
		if (equipmentIdx == null) {
			if (other.equipmentIdx != null)
				return false;
		} else if (!equipmentIdx.equals(other.equipmentIdx))
			return false;
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (planMonth == null) {
			if (other.planMonth != null)
				return false;
		} else if (!planMonth.equals(other.planMonth))
			return false;
		if (planStatus == null) {
			if (other.planStatus != null)
				return false;
		} else if (!planStatus.equals(other.planStatus))
			return false;
		if (planYear == null) {
			if (other.planYear != null)
				return false;
		} else if (!planYear.equals(other.planYear))
			return false;
		if (recordStatus == null) {
			if (other.recordStatus != null)
				return false;
		} else if (!recordStatus.equals(other.recordStatus))
			return false;
		if (repairClass == null) {
			if (other.repairClass != null)
				return false;
		} else if (!repairClass.equals(other.repairClass))
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
