package com.yunda.sb.repair.process.entity;

import java.util.Date;

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
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: RepairWorkOrder，数据表：设备检修作业工单
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
@Table(name = "E_REPAIR_WORK_ORDER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairWorkOrder implements java.io.Serializable {
	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 工单状态 - 未处理【1】 */
	public static final int ORDER_STATUS_WCL = 1;

	/** 工单状态 - 已处理【3】 */
	public static final int ORDER_STATUS_YCL = 3;

	/** 作业工单默认实修记录 */
	public static final String DEFAULT_REPAIR_RECORD = "合格";

	/* 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/* 范围实例主键 */
	@Column(name = "scope_case_idx")
	private String scopeCaseIdx;

	/* 作业项定义主键 */
	@Column(name = "define_idx")
	private String defineIdx;

	/* 序号 */
	@Column(name = "sort_no")
	private Integer sortNo;

	/* 作业内容 */
	@Column(name = "work_content")
	private String workContent;

	/* 工艺标准 */
	@Column(name = "process_standard")
	private String processStandard;

	/* 施修人 */
	@Column(name = "worker_id")
	private Long workerId;

	/* 施修人名称 */
	@Column(name = "worker_name")
	private String workerName;

	/* 其他作业人员名称 */
	@Column(name = "other_worker_name")
	private String otherWorkerName;

	/* 处理时间 */
	@Column(name = "process_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date processTime;

	/* 实修记录 */
	@Column(name = "repair_record")
	private String repairRecord;

	/* 备注 */
	private String remark;

	/* 工单状态，默认为未处理（代码：1） */
	@Column(name = "order_status")
	private Integer orderStatus;

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
	 * <li>说明：设置范围实例主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param scopeCaseIdx 范围实例主键
	 * @return 设备检修作业工单
	 */
	public RepairWorkOrder scopeCaseIdx(String scopeCaseIdx) {
		this.scopeCaseIdx = scopeCaseIdx;
		return this;
	}

	/**
	 * <li>说明：设置作业项定义主键
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param scopeCaseIdx 作业项定义主键
	 * @return 设备检修作业工单
	 */
	public RepairWorkOrder defineIdx(String defineIdx) {
		this.defineIdx = defineIdx;
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
	 * @return 设备检修作业工单
	 */
	public RepairWorkOrder seqNo(Integer sortNo) {
		this.sortNo = sortNo;
		return this;
	}

	/**
	 * <li>说明：设置作业内容
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param workContent 作业内容
	 * @return 设备检修作业工单
	 */
	public RepairWorkOrder workContent(String workContent) {
		this.workContent = workContent;
		return this;
	}

	/**
	 * <li>说明：设置工艺标准
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月7日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param processStandard 工艺标准
	 * @return 设备检修作业工单
	 */
	public RepairWorkOrder processStandard(String processStandard) {
		this.processStandard = processStandard;
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
	 * @return 设备检修作业工单
	 */
	public RepairWorkOrder remark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
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

	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public Date getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}

	public String getRepairRecord() {
		return repairRecord;
	}

	public void setRepairRecord(String repairRecord) {
		this.repairRecord = repairRecord;
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

	public String getScopeCaseIdx() {
		return scopeCaseIdx;
	}

	public void setScopeCaseIdx(String scopeCaseIdx) {
		this.scopeCaseIdx = scopeCaseIdx;
	}

	public String getDefineIdx() {
		return defineIdx;
	}

	public void setDefineIdx(String defineIdx) {
		this.defineIdx = defineIdx;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOtherWorkerName() {
		return otherWorkerName;
	}

	public void setOtherWorkerName(String otherWorkerName) {
		this.otherWorkerName = otherWorkerName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((defineIdx == null) ? 0 : defineIdx.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((orderStatus == null) ? 0 : orderStatus.hashCode());
		result = prime * result + ((otherWorkerName == null) ? 0 : otherWorkerName.hashCode());
		result = prime * result + ((processStandard == null) ? 0 : processStandard.hashCode());
		result = prime * result + ((processTime == null) ? 0 : processTime.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((repairRecord == null) ? 0 : repairRecord.hashCode());
		result = prime * result + ((scopeCaseIdx == null) ? 0 : scopeCaseIdx.hashCode());
		result = prime * result + ((sortNo == null) ? 0 : sortNo.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		result = prime * result + ((updator == null) ? 0 : updator.hashCode());
		result = prime * result + ((workContent == null) ? 0 : workContent.hashCode());
		result = prime * result + ((workerId == null) ? 0 : workerId.hashCode());
		result = prime * result + ((workerName == null) ? 0 : workerName.hashCode());
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
		RepairWorkOrder other = (RepairWorkOrder) obj;
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
		if (defineIdx == null) {
			if (other.defineIdx != null)
				return false;
		} else if (!defineIdx.equals(other.defineIdx))
			return false;
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (orderStatus == null) {
			if (other.orderStatus != null)
				return false;
		} else if (!orderStatus.equals(other.orderStatus))
			return false;
		if (otherWorkerName == null) {
			if (other.otherWorkerName != null)
				return false;
		} else if (!otherWorkerName.equals(other.otherWorkerName))
			return false;
		if (processStandard == null) {
			if (other.processStandard != null)
				return false;
		} else if (!processStandard.equals(other.processStandard))
			return false;
		if (processTime == null) {
			if (other.processTime != null)
				return false;
		} else if (!processTime.equals(other.processTime))
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
		if (repairRecord == null) {
			if (other.repairRecord != null)
				return false;
		} else if (!repairRecord.equals(other.repairRecord))
			return false;
		if (scopeCaseIdx == null) {
			if (other.scopeCaseIdx != null)
				return false;
		} else if (!scopeCaseIdx.equals(other.scopeCaseIdx))
			return false;
		if (sortNo == null) {
			if (other.sortNo != null)
				return false;
		} else if (!sortNo.equals(other.sortNo))
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
		if (workerId == null) {
			if (other.workerId != null)
				return false;
		} else if (!workerId.equals(other.workerId))
			return false;
		if (workerName == null) {
			if (other.workerName != null)
				return false;
		} else if (!workerName.equals(other.workerName))
			return false;
		return true;
	}

}
