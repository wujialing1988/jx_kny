package com.yunda.sb.inspect.record.entity;

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
 * <li>说明: InspectRecord实体类，数据表：设备巡检记录
 * <li>创建人：何涛
 * <li>创建日期：2016年6月14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
@Table(name = "E_INSPECT_RECORD")
public class InspectRecord implements java.io.Serializable {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 巡检结果 - 合格 */
	public static final String CHECK_RESULT_HG = "合格";

	/** 巡检结果 - 不合格 */
	public static final String CHECK_RESULT_BHG = "不合格";

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 巡检设备idx主键 */
	@Column(name = "PLAN_EQUIPMENT_IDX")
	private String planEquipmentIdx;

	/** 检修类型（1：机械、2：电气、3：其它） */
	@Column(name = "REPAIR_TYPE")
	private Integer repairType;

	/** 设备类别编码 */
	@Column(name = "CLASS_CODE")
	private String classCode;

	/** 设备类别名称 */
	@Column(name = "CLASS_NAME")
	private String className;

	/** 检查项目 */
	@Column(name = "CHECK_ITEM")
	private String checkItem;

	/** 检查项目首拼（用于根据首字母进行快速检索） */
	@Column(name = "CHECK_ITEM_PY")
	private String checkItemPY;

	/** 检查标准 */
	@Column(name = "CHECK_STANDARD")
	private String checkStandard;

	/** 顺序号 */
	@Column(name = "SEQ_NO")
	private Integer seqNo;

	/** 备注 */
	private String remarks;

	/** 检查结果（合格，不合格） */
	@Column(name = "CHECK_RESULT")
	private String checkResult;

	/** 巡检人 */
	@Column(name = "INSPECT_WORKER")
	private String inspectWorker;

	/** 巡检人ID */
	@Column(name = "INSPECT_WORKER_ID")
	private Long inspectWorkerId;

	/** 巡检时间 */
	@Column(name = "CHECK_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkTime;

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
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
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

	public Integer getRepairType() {
		return repairType;
	}

	public void setRepairType(Integer repairType) {
		this.repairType = repairType;
	}

	public String getCheckItem() {
		return checkItem;
	}

	public void setCheckItem(String checkItem) {
		this.checkItem = checkItem;
	}

	public String getCheckItemPY() {
		return checkItemPY;
	}

	public void setCheckItemPY(String checkItemPY) {
		this.checkItemPY = checkItemPY;
	}

	public String getCheckStandard() {
		return checkStandard;
	}

	public void setCheckStandard(String checkStandard) {
		this.checkStandard = checkStandard;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getPlanEquipmentIdx() {
		return planEquipmentIdx;
	}

	public void setPlanEquipmentIdx(String planEquipmentIdx) {
		this.planEquipmentIdx = planEquipmentIdx;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getInspectWorker() {
		return inspectWorker;
	}

	public void setInspectWorker(String inspectWorker) {
		this.inspectWorker = inspectWorker;
	}

	public Long getInspectWorkerId() {
		return inspectWorkerId;
	}

	public void setInspectWorkerId(Long inspectWorkerId) {
		this.inspectWorkerId = inspectWorkerId;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	/**
	 * <li>说明：设置检修类型
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param repairType 检修类型（1：机械、2：电气、3：其它）
	 * @return 设备巡检记录
	 */
	public InspectRecord repairType(Integer repairType) {
		this.repairType = repairType;
		return this;
	}

	/**
	 * <li>说明：设置周期巡检设备idx主键 
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param planEquipmentIdx 周期巡检设备idx主键 
	 * @return 设备巡检记录
	 */
	public InspectRecord planEquipmentIdx(String planEquipmentIdx) {
		this.planEquipmentIdx = planEquipmentIdx;
		return this;
	}

	/**
	 * <li>说明：设置设备类别编码
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param classCode 设备类别编码
	 * @return 设备巡检记录
	 */
	public InspectRecord classCode(String classCode) {
		this.classCode = classCode;
		return this;
	}

	/**
	 * <li>说明：设置设备类别名称
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param className 设备类别名称
	 * @return 设备巡检记录
	 */
	public InspectRecord className(String className) {
		this.className = className;
		return this;
	}

	/**
	 * <li>说明：设置检查项目
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param checkItem 检查项目
	 * @return 设备巡检记录
	 */
	public InspectRecord checkItem(String checkItem) {
		this.checkItem = checkItem;
		return this;
	}

	/**
	 * <li>说明：设置检查项目首拼（用于根据首字母进行快速检索）
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param checkItemPY 检查项目首拼
	 * @return 设备巡检记录
	 */
	public InspectRecord checkItemPY(String checkItemPY) {
		this.checkItemPY = checkItemPY;
		return this;
	}

	/**
	 * <li>说明：设置检查标准
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param checkStandard 检查标准
	 * @return 设备巡检记录
	 */
	public InspectRecord checkStandard(String checkStandard) {
		this.checkStandard = checkStandard;
		return this;
	}

	/**
	 * <li>说明：设置顺序号
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param seqNo 顺序号
	 * @return 设备巡检记录
	 */
	public InspectRecord seqNo(Integer seqNo) {
		this.seqNo = seqNo;
		return this;
	}

	/**
	 * <li>说明：设置备注
	 * <li>创建人：何涛
	 * <li>创建日期：2016年6月15日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param remarks 备注
	 * @return 设备巡检记录
	 */
	public InspectRecord remarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checkItem == null) ? 0 : checkItem.hashCode());
		result = prime * result + ((checkItemPY == null) ? 0 : checkItemPY.hashCode());
		result = prime * result + ((checkResult == null) ? 0 : checkResult.hashCode());
		result = prime * result + ((checkStandard == null) ? 0 : checkStandard.hashCode());
		result = prime * result + ((checkTime == null) ? 0 : checkTime.hashCode());
		result = prime * result + ((classCode == null) ? 0 : classCode.hashCode());
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((inspectWorker == null) ? 0 : inspectWorker.hashCode());
		result = prime * result + ((inspectWorkerId == null) ? 0 : inspectWorkerId.hashCode());
		result = prime * result + ((planEquipmentIdx == null) ? 0 : planEquipmentIdx.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
		result = prime * result + ((repairType == null) ? 0 : repairType.hashCode());
		result = prime * result + ((seqNo == null) ? 0 : seqNo.hashCode());
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
		InspectRecord other = (InspectRecord) obj;
		if (checkItem == null) {
			if (other.checkItem != null)
				return false;
		} else if (!checkItem.equals(other.checkItem))
			return false;
		if (checkItemPY == null) {
			if (other.checkItemPY != null)
				return false;
		} else if (!checkItemPY.equals(other.checkItemPY))
			return false;
		if (checkResult == null) {
			if (other.checkResult != null)
				return false;
		} else if (!checkResult.equals(other.checkResult))
			return false;
		if (checkStandard == null) {
			if (other.checkStandard != null)
				return false;
		} else if (!checkStandard.equals(other.checkStandard))
			return false;
		if (checkTime == null) {
			if (other.checkTime != null)
				return false;
		} else if (!checkTime.equals(other.checkTime))
			return false;
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
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (inspectWorker == null) {
			if (other.inspectWorker != null)
				return false;
		} else if (!inspectWorker.equals(other.inspectWorker))
			return false;
		if (inspectWorkerId == null) {
			if (other.inspectWorkerId != null)
				return false;
		} else if (!inspectWorkerId.equals(other.inspectWorkerId))
			return false;
		if (planEquipmentIdx == null) {
			if (other.planEquipmentIdx != null)
				return false;
		} else if (!planEquipmentIdx.equals(other.planEquipmentIdx))
			return false;
		if (recordStatus == null) {
			if (other.recordStatus != null)
				return false;
		} else if (!recordStatus.equals(other.recordStatus))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		if (repairType == null) {
			if (other.repairType != null)
				return false;
		} else if (!repairType.equals(other.repairType))
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
		return true;
	}

}
