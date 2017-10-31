package com.yunda.sb.inspect.route.entity;

import java.util.Calendar;

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
 * <li>说明：InspectlRoute实体类，数据表：设备巡检范围
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "E_INSPECT_ROUTE")
public class InspectRoute implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 巡检周期：周检（1） */
	public static final int PERIOD_TYPE_ZJ = 1;

	/** 巡检周期：周检（1） */
	public static final String PERIOD_TYPE_ZJ_CH = "周检";

	/** 巡检周期：半月检（2） */
	public static final int PERIOD_TYPE_BYJ = 2;

	/** 巡检周期：半月检（2） */
	public static final String PERIOD_TYPE_BYJ_CH = "半月检";

	/** 巡检周期：月检（3） */
	public static final int PERIOD_TYPE_YJ = 3;

	/** 巡检周期：月检（3） */
	public static final String PERIOD_TYPE_YJ_CH = "月检";

	/** 巡检周期：季检（4） */
	public static final int PERIOD_TYPE_JJ = 4;

	/** 巡检周期：季检（4） */
	public static final String PERIOD_TYPE_JJ_CH = "季检";

	/** 巡检周期：临时 */
	public static final String PERIOD_TYPE_TEMP = "临时";

	/** 状态：未启用（0） */
	public static final int STATE_WQY = 0;

	/** 状态：未启用（0） */
	public static final String STATE_WQY_CH = "未启用";

	/** 状态：启用（1） */
	public static final int STATE_QY = 1;

	/** 状态：启用（1） */
	public static final String STATE_QY_CH = "启用";

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 线路名称 */
	@Column(name = "ROUTE_NAME")
	private String routeName;

	/** 计划编制人 */
	@Column(name = "PARTROL_WORKER")
	private String partrolWorker;

	/** 计划编制人ID */
	@Column(name = "PARTROL_WORKER_ID")
	private Long partrolWorkerId;

	/** 巡检周期（1：周检；2：半月检；3：月检） */
	@Column(name = "PERIOD_TYPE")
	private Integer periodType;

	/** 计划发布日期 */
	@Column(name = "PLAN_PUBLISH_DATE")
	@Temporal(TemporalType.DATE)
	private java.util.Date planPublishDate;

	/** 有效期至 */
	@Column(name = "EXPIRY_DATE")
	@Temporal(TemporalType.DATE)
	private java.util.Date expiryDate;

	/** 状态（1启用、0未启用） */
	private int state = 0;

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

	public Long getCreator() {
		return this.creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdator() {
		return this.updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Integer getPeriodType() {
		return periodType;
	}

	public void setPeriodType(Integer periodType) {
		this.periodType = periodType;
	}

	public java.util.Date getPlanPublishDate() {
		return planPublishDate;
	}

	public void setPlanPublishDate(java.util.Date planPublishDate) {
		this.planPublishDate = planPublishDate;
	}

	public java.util.Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(java.util.Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * <li>说明：获取巡检周期中文名称
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月14日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 巡检周期中文名称
	 */
	public String getPeriodTypeCH() {
		if (PERIOD_TYPE_ZJ == this.periodType) {
			return PERIOD_TYPE_ZJ_CH;
		}
		if (PERIOD_TYPE_BYJ == this.periodType) {
			return PERIOD_TYPE_BYJ_CH;
		}
		if (PERIOD_TYPE_YJ == this.periodType) {
			return PERIOD_TYPE_YJ_CH;
		}
		if (PERIOD_TYPE_JJ == this.periodType) {
			return PERIOD_TYPE_JJ_CH;
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(30);
		sb.append(this.routeName).append("【").append(this.getPeriodTypeCH()).append("-").append(this.partrolWorker).append("】");
		// 未启用的用灰色字体显示
		if (null != expiryDate && Calendar.getInstance().getTime().after(expiryDate)) {
			return "<span style=\"color:red;\">" + sb.toString() + "</span>";
		}
		if (STATE_WQY == state) {
			return "<span style=\"color:gray;\">" + sb.toString() + "</span>";
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((partrolWorker == null) ? 0 : partrolWorker.hashCode());
		result = prime * result + ((partrolWorkerId == null) ? 0 : partrolWorkerId.hashCode());
		result = prime * result + ((periodType == null) ? 0 : periodType.hashCode());
		result = prime * result + ((planPublishDate == null) ? 0 : planPublishDate.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((routeName == null) ? 0 : routeName.hashCode());
		result = prime * result + state;
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
		InspectRoute other = (InspectRoute) obj;
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
		if (expiryDate == null) {
			if (other.expiryDate != null)
				return false;
		} else if (!expiryDate.equals(other.expiryDate))
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
		if (planPublishDate == null) {
			if (other.planPublishDate != null)
				return false;
		} else if (!planPublishDate.equals(other.planPublishDate))
			return false;
		if (recordStatus == null) {
			if (other.recordStatus != null)
				return false;
		} else if (!recordStatus.equals(other.recordStatus))
			return false;
		if (routeName == null) {
			if (other.routeName != null)
				return false;
		} else if (!routeName.equals(other.routeName))
			return false;
		if (state != other.state)
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
