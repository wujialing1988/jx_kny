package com.yunda.sb.repair.process.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairTaskListTeam，数据表：检修任务单处理班组
 * <li>创建人：黄杨
 * <li>创建日期：2016年7月7日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
@Table(name = "E_REPAIR_TASK_LIST_TEAM")
public class RepairTaskListTeam implements Serializable {

	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 工长确认 - 未确认【0】 */
	public static final Short IS_CONFIRMED_NO = 0;

	/** 工长确认 - 已确认【1】 */
	public static final Short IS_CONFIRMED_YES = 1;

	/** 工长确认 - 待确认【2】 */
	public static final Short IS_CONFIRMED_TODO = 2;

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 检修任务单idx主键 */
	@Column(name = "task_list_idx")
	private String taskListIdx;

	/** 检修类型（1：机械、2：电气、3：其它） */
	@Column(name = "repair_type")
	private Integer repairType;

	/** 班组id，多个班组以英文逗号“,”进行分隔 */
	@Column(name = "org_id")
	private String orgId;

	/** 班组名称，多个班组以英文逗号“,”进行分隔 */
	@Column(name = "org_name")
	private String orgName;

	/** 开工日期，是施修人初次扫码施修时间 */
	@Column(name = "real_begin_time")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date realBeginTime;

	/** 竣工日期，是包修工长签认时间 */
	@Column(name = "real_end_time")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date realEndTime;

	/** 工长确认，0：未确认、1：已确认 */
	@Column(name = "is_confirmed")
	private Short isConfirmed;

	/** 数据状态 */
	@Column(name = "RECORD_STATUS")
	private Integer recordStatus;

	/** 创建人 */
	@Column(name = "CREATOR", updatable = false)
	private Long creator;

	/** 创建时间 */
	@Column(name = "CREATE_TIME", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date createTime;

	/** 修改人 */
	@Column(name = "UPDATOR")
	private Long updator;

	/** 修改时间 */
	@Column(name = "UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date updateTime;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getTaskListIdx() {
		return taskListIdx;
	}

	public void setTaskListIdx(String taskListIdx) {
		this.taskListIdx = taskListIdx;
	}

	public Integer getRepairType() {
		return repairType;
	}

	public void setRepairType(Integer repairType) {
		this.repairType = repairType;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public java.util.Date getRealBeginTime() {
		return realBeginTime;
	}

	public void setRealBeginTime(java.util.Date realBeginTime) {
		this.realBeginTime = realBeginTime;
	}

	public java.util.Date getRealEndTime() {
		return realEndTime;
	}

	public void setRealEndTime(java.util.Date realEndTime) {
		this.realEndTime = realEndTime;
	}

	public Short getIsConfirmed() {
		return isConfirmed;
	}

	public void setIsConfirmed(Short isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((isConfirmed == null) ? 0 : isConfirmed.hashCode());
		result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
		result = prime * result + ((orgName == null) ? 0 : orgName.hashCode());
		result = prime * result + ((realBeginTime == null) ? 0 : realBeginTime.hashCode());
		result = prime * result + ((realEndTime == null) ? 0 : realEndTime.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((repairType == null) ? 0 : repairType.hashCode());
		result = prime * result + ((taskListIdx == null) ? 0 : taskListIdx.hashCode());
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
		RepairTaskListTeam other = (RepairTaskListTeam) obj;
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
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (isConfirmed == null) {
			if (other.isConfirmed != null)
				return false;
		} else if (!isConfirmed.equals(other.isConfirmed))
			return false;
		if (orgId == null) {
			if (other.orgId != null)
				return false;
		} else if (!orgId.equals(other.orgId))
			return false;
		if (orgName == null) {
			if (other.orgName != null)
				return false;
		} else if (!orgName.equals(other.orgName))
			return false;
		if (realBeginTime == null) {
			if (other.realBeginTime != null)
				return false;
		} else if (!realBeginTime.equals(other.realBeginTime))
			return false;
		if (realEndTime == null) {
			if (other.realEndTime != null)
				return false;
		} else if (!realEndTime.equals(other.realEndTime))
			return false;
		if (recordStatus == null) {
			if (other.recordStatus != null)
				return false;
		} else if (!recordStatus.equals(other.recordStatus))
			return false;
		if (repairType == null) {
			if (other.repairType != null)
				return false;
		} else if (!repairType.equals(other.repairType))
			return false;
		if (taskListIdx == null) {
			if (other.taskListIdx != null)
				return false;
		} else if (!taskListIdx.equals(other.taskListIdx))
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
