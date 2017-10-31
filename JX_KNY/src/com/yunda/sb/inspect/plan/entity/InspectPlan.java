package com.yunda.sb.inspect.plan.entity;

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
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: InspectPlan实体类，数据表：巡检计划
 * <li>创建人：何涛
 * <li>创建日期：2016年6月14
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
@Table(name = "E_INSPECT_PLAN")
public class InspectPlan implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 处理状态 - 未处理 */
	public static final String STATE_WCL = "未处理";

	/** 处理状态 - 已处理 */
	public static final String STATE_YCL = "已处理";

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 巡检线路idx主键 */
	@Column(name = "ROUTE_IDX")
	private String routeIdx;

	/** 巡检线路名称 */
	@Column(name = "ROUTE_NAME")
	private String routeName;

	/** 计划编制人 */
	@Column(name = "PARTROL_WORKER")
	private String partrolWorker;

	/** 计划编制人ID */
	@Column(name = "PARTROL_WORKER_ID")
	private Long partrolWorkerId;

	/** 巡检周期（周检，半月检，月检，季检，临时） */
	@Column(name = "PERIOD_TYPE")
	private String periodType;

	/** 计划开始日期 */
	@Column(name = "PLAN_START_DATE")
	@Temporal(TemporalType.DATE)
	private java.util.Date planStartDate;

	/** 计划结束日期 */
	@Column(name = "PLAN_END_DATE")
	@Temporal(TemporalType.DATE)
	private java.util.Date planEndDate;

	/** 实际开始日期 */
	@Column(name = "REAL_START_DATE")
	@Temporal(TemporalType.DATE)
	private java.util.Date realStartDate;

	/** 实际结束日期 */
	@Column(name = "REAL_END_DATE")
	@Temporal(TemporalType.DATE)
	private java.util.Date realEndDate;

	/** 处理状态：(未处理、已处理) */
	private String state;

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

	public String getIdx() {
		return this.idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public Integer getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getPartrolWorker() {
		return partrolWorker;
	}

	public void setPartrolWorker(String partrolWorker) {
		this.partrolWorker = partrolWorker;
	}

	public Long getPartrolWorkerId() {
		return partrolWorkerId;
	}

	public void setPartrolWorkerId(Long partrolWorkerId) {
		this.partrolWorkerId = partrolWorkerId;
	}

	public String getRouteIdx() {
		return routeIdx;
	}

	public void setRouteIdx(String routeIdx) {
		this.routeIdx = routeIdx;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public java.util.Date getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(java.util.Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	public java.util.Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(java.util.Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	public java.util.Date getRealStartDate() {
		return realStartDate;
	}

	public void setRealStartDate(java.util.Date realStartDate) {
		this.realStartDate = realStartDate;
	}

	public java.util.Date getRealEndDate() {
		return realEndDate;
	}

	public void setRealEndDate(java.util.Date realEndDate) {
		this.realEndDate = realEndDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * <li>说明：设置巡检线路idx主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月14日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param routeIdx 巡检线路idx主键
	 * @return 巡检周期计划
	 */
	public InspectPlan routeIdx(String routeIdx) {
		this.routeIdx = routeIdx;
		return this;
	}

	/**
	 * <li>说明：设置巡检线路名称
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月14日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param routeName 巡检线路名称
	 * @return 巡检周期计划
	 */
	public InspectPlan routeName(String routeName) {
		this.routeName = routeName;
		return this;
	}

	/**
	 * <li>说明：设置计划开始日期
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月14日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param routeName 计划开始日期
	 * @return 巡检周期计划
	 */
	public InspectPlan planStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
		return this;
	}

	/**
	 * <li>说明：设置计划结束日期
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月14日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param routeName 计划结束日期
	 * @return 巡检周期计划
	 */
	public InspectPlan planEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((partrolWorker == null) ? 0 : partrolWorker.hashCode());
		result = prime * result + ((partrolWorkerId == null) ? 0 : partrolWorkerId.hashCode());
		result = prime * result + ((periodType == null) ? 0 : periodType.hashCode());
		result = prime * result + ((planEndDate == null) ? 0 : planEndDate.hashCode());
		result = prime * result + ((planStartDate == null) ? 0 : planStartDate.hashCode());
		result = prime * result + ((realEndDate == null) ? 0 : realEndDate.hashCode());
		result = prime * result + ((realStartDate == null) ? 0 : realStartDate.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((routeIdx == null) ? 0 : routeIdx.hashCode());
		result = prime * result + ((routeName == null) ? 0 : routeName.hashCode());
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
		InspectPlan other = (InspectPlan) obj;
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
		if (partrolWorker == null) {
			if (other.partrolWorker != null)
				return false;
		} else if (!partrolWorker.equals(other.partrolWorker))
			return false;
		if (partrolWorkerId == null) {
			if (other.partrolWorkerId != null)
				return false;
		} else if (!partrolWorkerId.equals(other.partrolWorkerId))
			return false;
		if (periodType == null) {
			if (other.periodType != null)
				return false;
		} else if (!periodType.equals(other.periodType))
			return false;
		if (planEndDate == null) {
			if (other.planEndDate != null)
				return false;
		} else if (!planEndDate.equals(other.planEndDate))
			return false;
		if (planStartDate == null) {
			if (other.planStartDate != null)
				return false;
		} else if (!planStartDate.equals(other.planStartDate))
			return false;
		if (realEndDate == null) {
			if (other.realEndDate != null)
				return false;
		} else if (!realEndDate.equals(other.realEndDate))
			return false;
		if (realStartDate == null) {
			if (other.realStartDate != null)
				return false;
		} else if (!realStartDate.equals(other.realStartDate))
			return false;
		if (recordStatus == null) {
			if (other.recordStatus != null)
				return false;
		} else if (!recordStatus.equals(other.recordStatus))
			return false;
		if (routeIdx == null) {
			if (other.routeIdx != null)
				return false;
		} else if (!routeIdx.equals(other.routeIdx))
			return false;
		if (routeName == null) {
			if (other.routeName != null)
				return false;
		} else if (!routeName.equals(other.routeName))
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
