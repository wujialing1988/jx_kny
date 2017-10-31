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

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明： RepairScopeCase，数据表：检修范围实例
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-4
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "E_REPAIR_SCOPE_CASE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairScopeCase implements java.io.Serializable {

	/** 默认序列号 */
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

	/** 范围定义主键 */
	@Column(name = "scope_define_idx")
	private String scopeDefineIdx;

	/** 检修任务单主键 */
	@Column(name = "task_list_idx")
	private String taskListIdx;

	/** 序号 */
	@Column(name = "sort_no")
	private Integer sortNo;

	/** 检修类型，1：机型、2：电气、3：其它 */
	@Column(name = "repair_type")
	private Integer repairType;

	/** 检修范围名称 */
	@Column(name = "repair_item_name")
	private String repairItemName;

	/** 备注 */
	private String remark;

	/** 处理状态：(未处理、已处理)，默认为未处理 */
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

	/**
	 * <li>说明：设置范围定义主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param scopeDefineIdx 范围定义主键
	 * @return 检修范围实例
	 */
	public RepairScopeCase scopeDefineIdx(String scopeDefineIdx) {
		this.scopeDefineIdx = scopeDefineIdx;
		return this;
	}

	/**
	 * <li>说明：设置检修任务单主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param taskListIdx 检修任务单主键
	 * @return 检修范围实例
	 */
	public RepairScopeCase taskListIdx(String taskListIdx) {
		this.taskListIdx = taskListIdx;
		return this;
	}

	/**
	 * <li>说明：设置序号
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param sortNo 序号
	 * @return 检修范围实例
	 */
	public RepairScopeCase seqNo(Integer sortNo) {
		this.sortNo = sortNo;
		return this;
	}

	/**
	 * <li>说明：设置检修类型，1：机型、2：电气、3：其它
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param repairType 检修类型，1：机型、2：电气、3：其它
	 * @return 检修范围实例
	 */
	public RepairScopeCase repairType(Integer repairType) {
		this.repairType = repairType;
		return this;
	}

	/**
	 * <li>说明：设置检修范围名称
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param repairItemName 检修范围名称
	 * @return 检修范围实例
	 */
	public RepairScopeCase repairScopeName(String repairItemName) {
		this.repairItemName = repairItemName;
		return this;
	}

	/**
	 * <li>说明：设置备注
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param remark 备注
	 * @return 检修范围实例
	 */
	public RepairScopeCase remark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getScopeDefineIdx() {
		return scopeDefineIdx;
	}

	public void setScopeDefineIdx(String scopeDefineIdx) {
		this.scopeDefineIdx = scopeDefineIdx;
	}

	public String getTaskListIdx() {
		return taskListIdx;
	}

	public void setTaskListIdx(String taskListIdx) {
		this.taskListIdx = taskListIdx;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getRepairType() {
		return repairType;
	}

	public void setRepairType(Integer repairType) {
		this.repairType = repairType;
	}

	public String getRepairItemName() {
		return repairItemName;
	}

	public void setRepairItemName(String repairItemName) {
		this.repairItemName = repairItemName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((repairItemName == null) ? 0 : repairItemName.hashCode());
		result = prime * result + ((repairType == null) ? 0 : repairType.hashCode());
		result = prime * result + ((scopeDefineIdx == null) ? 0 : scopeDefineIdx.hashCode());
		result = prime * result + ((sortNo == null) ? 0 : sortNo.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		RepairScopeCase other = (RepairScopeCase) obj;
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
		if (recordStatus == null) {
			if (other.recordStatus != null)
				return false;
		} else if (!recordStatus.equals(other.recordStatus))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (repairItemName == null) {
			if (other.repairItemName != null)
				return false;
		} else if (!repairItemName.equals(other.repairItemName))
			return false;
		if (repairType == null) {
			if (other.repairType != null)
				return false;
		} else if (!repairType.equals(other.repairType))
			return false;
		if (scopeDefineIdx == null) {
			if (other.scopeDefineIdx != null)
				return false;
		} else if (!scopeDefineIdx.equals(other.scopeDefineIdx))
			return false;
		if (sortNo == null) {
			if (other.sortNo != null)
				return false;
		} else if (!sortNo.equals(other.sortNo))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
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
