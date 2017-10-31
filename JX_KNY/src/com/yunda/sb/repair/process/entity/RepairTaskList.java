package com.yunda.sb.repair.process.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.yunda.sb.base.IEntity;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：检修任务单
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "E_REPAIR_TASK_LIST")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairTaskList implements java.io.Serializable, IEntity {

	/** 处理状态 - 未处理 */
	public static final String STATE_WCL = "未处理";

	/** 处理状态 - 处理中 */
	public static final String STATE_CLZ = "处理中";

	/** 处理状态 - 已处理 */
	public static final String STATE_YCL = "已处理";

	/** 处理状态 - 待验收（工长已确认） */
	public static final String STATE_DYS = "待验收";

	/** 处理状态 - 已验收 */
	public static final String STATE_YYS = "已验收";

	/** 修别 - 小 */
	public static final String REPAIR_CLASS_NAME_SMALL = "小";

	/** 修别 - 中 */
	public static final String REPAIR_CLASS_NAME_MEDIUM = "中";

	/** 修别 - 项 */
	public static final String REPAIR_CLASS_NAME_SUBJECT = "项";

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备检修月计划idx主键 */
	@Column(name = "plan_month_idx")
	private String planMonthIdx;

	/** 设备主键 */
	@Column(name = "equipment_idx")
	private String equipmentIdx;

	/** 修别 */
	@Column(name = "repair_class_name")
	private String repairClassName;

	/** 机械工长签名 */
	@Column(name = "gz_sign_mac")
	private String gzSignMac;

	/** 电气工长签名 */
	@Column(name = "gz_sign_elc")
	private String gzSignElc;

	/** 实际开工时间 */
	@Column(name = "real_begin_time")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date realBeginTime;

	/** 实际完工时间 */
	@Column(name = "real_end_time")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date realEndTime;

	/** 计划开工时间 */
	@Column(name = "begin_time")
	@Temporal(TemporalType.DATE)
	private java.util.Date beginTime;

	/** 计划完工时间 */
	@Column(name = "end_time")
	@Temporal(TemporalType.DATE)
	private java.util.Date endTime;

	/** 是否需要验收，0：无需验收人验收、1：需要验收人验收 */
	@Column(name = "is_need_acceptance")
	private Boolean isNeedAcceptance;

	/** 验收人ID */
	@Column(name = "acceptor_id")
	private Long acceptorId;

	/** 验收人名称 */
	@Column(name = "acceptor_name")
	private String acceptorName;

	/** 验收评语 */
	@Column(name = "acceptance_reviews")
	private String acceptanceReviews;

	/** 验收时间，即：验交日期，是包机人签认时间 */
	@Column(name = "acceptance_time")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date acceptanceTime;

	/** 使用人ID */
	@Column(name = "user_id")
	private Long userId;

	/** 使用人名称 */
	@Column(name = "user_name")
	private String userName;

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

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getPlanMonthIdx() {
		return planMonthIdx;
	}

	public void setPlanMonthIdx(String planMonthIdx) {
		this.planMonthIdx = planMonthIdx;
	}

	public String getEquipmentIdx() {
		return equipmentIdx;
	}

	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
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

	public java.util.Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(java.util.Date beginTime) {
		this.beginTime = beginTime;
	}

	public java.util.Date getEndTime() {
		return endTime;
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	public Boolean getIsNeedAcceptance() {
		return isNeedAcceptance;
	}

	public void setIsNeedAcceptance(Boolean isNeedAcceptance) {
		this.isNeedAcceptance = isNeedAcceptance;
	}

	public Long getAcceptorId() {
		return acceptorId;
	}

	public void setAcceptorId(Long acceptorId) {
		this.acceptorId = acceptorId;
	}

	public String getAcceptorName() {
		return acceptorName;
	}

	public void setAcceptorName(String acceptorName) {
		this.acceptorName = acceptorName;
	}

	public String getAcceptanceReviews() {
		return acceptanceReviews;
	}

	public void setAcceptanceReviews(String acceptanceReviews) {
		this.acceptanceReviews = acceptanceReviews;
	}

	public java.util.Date getAcceptanceTime() {
		return acceptanceTime;
	}

	public void setAcceptanceTime(java.util.Date acceptanceTime) {
		this.acceptanceTime = acceptanceTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getRepairClassName() {
		return repairClassName;
	}

	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}

	public String getGzSignMac() {
		return gzSignMac;
	}

	public void setGzSignMac(String gzSignMac) {
		this.gzSignMac = gzSignMac;
	}

	public String getGzSignElc() {
		return gzSignElc;
	}

	public void setGzSignElc(String gzSignElc) {
		this.gzSignElc = gzSignElc;
	}

	/**
	 * <li>说明：验证检修任务是否已经验收完成，包括：工长验收、验收员验收、使用人确认，该方法是用于更新检修任务状态为“已验收”前的数据验证方法，其它时候请慎用
	 * <li>创建人：何涛
	 * <li>创建日期：2017年1月23日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 是否已经验收完成，true：所有环节已验收，false：还有环节没有验收，返回false时不能更新任务单状态为“已验收”
	 */
	public boolean isAllChecked() {
		if (!RepairTaskList.STATE_DYS.equals(this.state)) {
			return false;
		}
		if (isNeedAcceptance && null == this.acceptorId) {
			return false;
		}
		if (null == this.userId) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acceptanceReviews == null) ? 0 : acceptanceReviews.hashCode());
		result = prime * result + ((acceptanceTime == null) ? 0 : acceptanceTime.hashCode());
		result = prime * result + ((acceptorId == null) ? 0 : acceptorId.hashCode());
		result = prime * result + ((acceptorName == null) ? 0 : acceptorName.hashCode());
		result = prime * result + ((beginTime == null) ? 0 : beginTime.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((equipmentIdx == null) ? 0 : equipmentIdx.hashCode());
		result = prime * result + ((gzSignElc == null) ? 0 : gzSignElc.hashCode());
		result = prime * result + ((gzSignMac == null) ? 0 : gzSignMac.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((isNeedAcceptance == null) ? 0 : isNeedAcceptance.hashCode());
		result = prime * result + ((planMonthIdx == null) ? 0 : planMonthIdx.hashCode());
		result = prime * result + ((realBeginTime == null) ? 0 : realBeginTime.hashCode());
		result = prime * result + ((realEndTime == null) ? 0 : realEndTime.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((repairClassName == null) ? 0 : repairClassName.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		result = prime * result + ((updator == null) ? 0 : updator.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		RepairTaskList other = (RepairTaskList) obj;
		if (acceptanceReviews == null) {
			if (other.acceptanceReviews != null)
				return false;
		} else if (!acceptanceReviews.equals(other.acceptanceReviews))
			return false;
		if (acceptanceTime == null) {
			if (other.acceptanceTime != null)
				return false;
		} else if (!acceptanceTime.equals(other.acceptanceTime))
			return false;
		if (acceptorId == null) {
			if (other.acceptorId != null)
				return false;
		} else if (!acceptorId.equals(other.acceptorId))
			return false;
		if (acceptorName == null) {
			if (other.acceptorName != null)
				return false;
		} else if (!acceptorName.equals(other.acceptorName))
			return false;
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
		if (gzSignElc == null) {
			if (other.gzSignElc != null)
				return false;
		} else if (!gzSignElc.equals(other.gzSignElc))
			return false;
		if (gzSignMac == null) {
			if (other.gzSignMac != null)
				return false;
		} else if (!gzSignMac.equals(other.gzSignMac))
			return false;
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (isNeedAcceptance == null) {
			if (other.isNeedAcceptance != null)
				return false;
		} else if (!isNeedAcceptance.equals(other.isNeedAcceptance))
			return false;
		if (planMonthIdx == null) {
			if (other.planMonthIdx != null)
				return false;
		} else if (!planMonthIdx.equals(other.planMonthIdx))
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
		if (repairClassName == null) {
			if (other.repairClassName != null)
				return false;
		} else if (!repairClassName.equals(other.repairClassName))
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
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
