package com.yunda.sb.repair.scope.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.yunda.sb.base.IOrder;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairScopeDetails，数据表：设备检修范围明细-作业内容
 * <li>创建人：何涛
 * <li>创建日期：2016年7月7日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
@Table(name = "E_REPAIR_SCOPE_DETAILS")
public class RepairScopeDetails implements java.io.Serializable, IOrder {
	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备检修范围主键 */
	@Column(name = "scope_idx")
	private String scopeIdx;

	/** 排序号 */
	@Column(name = "seq_no")
	private Integer seqNo;

	/** 作业内容 */
	@Column(name = "work_content")
	private String workContent;

	/** 工艺标准 */
	@Column(name = "process_standard")
	private String processStandard;

	/** 备注 */
	private String remark;

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

	public String getScopeIdx() {
		return scopeIdx;
	}

	public void setScopeIdx(String scopeIdx) {
		this.scopeIdx = scopeIdx;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getWorkContent() {
		return workContent;
	}

	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}

	public String getProcessStandard() {
		return processStandard;
	}

	public void setProcessStandard(String processStandard) {
		this.processStandard = processStandard;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
		result = prime * result + ((processStandard == null) ? 0 : processStandard.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((scopeIdx == null) ? 0 : scopeIdx.hashCode());
		result = prime * result + ((seqNo == null) ? 0 : seqNo.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		result = prime * result + ((updator == null) ? 0 : updator.hashCode());
		result = prime * result + ((workContent == null) ? 0 : workContent.hashCode());
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
		RepairScopeDetails other = (RepairScopeDetails) obj;
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
		if (processStandard == null) {
			if (other.processStandard != null)
				return false;
		} else if (!processStandard.equals(other.processStandard))
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
		if (scopeIdx == null) {
			if (other.scopeIdx != null)
				return false;
		} else if (!scopeIdx.equals(other.scopeIdx))
			return false;
		if (seqNo == null) {
			if (other.seqNo != null)
				return false;
		} else if (!seqNo.equals(other.seqNo))
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
		if (workContent == null) {
			if (other.workContent != null)
				return false;
		} else if (!workContent.equals(other.workContent))
			return false;
		return true;
	}

}
