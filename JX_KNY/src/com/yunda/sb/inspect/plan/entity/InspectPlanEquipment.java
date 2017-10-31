package com.yunda.sb.inspect.plan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：InspectPlanEquipment实体类，数据表：巡检设备
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
@Table(name = "E_INSPECT_PLAN_EQUIPMENT")
public class InspectPlanEquipment implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 巡检结果 - 未巡检 */
	public static final String CHECK_RESULT_WXJ = "未巡检";

	/** 巡检结果 - 已巡检 */
	public static final String CHECK_RESULT_YXJ = "已巡检";

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 巡检周期计划idx主键 */
	@Column(name = "PLAN_IDX")
	private String planIdx;

	/** 设备主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

	/** 序号（保留） */
	@Column(name = "SEQ_NO")
	private Integer seqNo;

	/** 实际开工时间 */
	@Column(name = "REAL_BEGIN_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date realBeginTime;

	/** 实际完工时间 */
	@Column(name = "REAL_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date realEndTime;

	/** 使用人（用于记录使用人确认） */
	@Column(name = "USE_WORKER")
	private String useWorker;

	/** 使用人Id（用于记录使用人确认） */
	@Column(name = "USE_WORKER_ID")
	private Long useWorkerId;

	/** 巡检结果（已巡检、未巡检） */
	@Column(name = "CHECK_RESULT")
	private String checkResult;

	/** 巡检情况描述 */
	@Column(name = "CHECK_RESULT_DESC")
	private String checkResultDesc;

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

	/* 设备名称 */
	@Transient
	private String equipmentName;

	/* 设备编码 */
	@Transient
	private String equipmentCode;

	/* 型号 */
	@Transient
	private String model;

	/* 规格 */
	@Transient
	private String specification;

	/* 设置地点(来源码表) */
	@Transient
	private String usePlace;

	/**
	 * <li>说明：默认构造方法
	 * <li>创建人：兰佳妮
	 * <li>创建日期：2016年10月11日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：	 
	 */
	public InspectPlanEquipment() {

	}

	/**
	 * <li>说明：用于hql查询使用人确认列表的构造方法
	 * <li>创建人：兰佳妮
	 * <li>创建日期：2016年10月11日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：	 
	 * @param idx idx主键
	 * @param equipmentName 设备名称
	 * @param equipmentCode 设备编码
	 * @param model 型号
	 * @param specification 规格
	 * @param usePlace 设置地点
	 */
	public InspectPlanEquipment(String idx, String equipmentName, String equipmentCode, String model, String specification, String usePlace) {
		super();
		this.idx = idx;
		this.equipmentName = equipmentName;
		this.equipmentCode = equipmentCode;
		this.model = model;
		this.specification = specification;
		this.usePlace = usePlace;
	}

	public String getIdx() {
		return this.idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getPlanIdx() {
		return planIdx;
	}

	public void setPlanIdx(String planIdx) {
		this.planIdx = planIdx;
	}

	public String getUseWorker() {
		return useWorker;
	}

	public void setUseWorker(String useWorker) {
		this.useWorker = useWorker;
	}

	public Long getUseWorkerId() {
		return useWorkerId;
	}

	public void setUseWorkerId(Long useWorkerId) {
		this.useWorkerId = useWorkerId;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getCheckResultDesc() {
		return checkResultDesc;
	}

	public void setCheckResultDesc(String checkResultDesc) {
		this.checkResultDesc = checkResultDesc;
	}

	public Integer getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
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

	public String getEquipmentIdx() {
		return equipmentIdx;
	}

	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getUsePlace() {
		return usePlace;
	}

	public void setUsePlace(String usePlace) {
		this.usePlace = usePlace;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checkResult == null) ? 0 : checkResult.hashCode());
		result = prime * result + ((checkResultDesc == null) ? 0 : checkResultDesc.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((equipmentCode == null) ? 0 : equipmentCode.hashCode());
		result = prime * result + ((equipmentIdx == null) ? 0 : equipmentIdx.hashCode());
		result = prime * result + ((equipmentName == null) ? 0 : equipmentName.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((planIdx == null) ? 0 : planIdx.hashCode());
		result = prime * result + ((realBeginTime == null) ? 0 : realBeginTime.hashCode());
		result = prime * result + ((realEndTime == null) ? 0 : realEndTime.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((seqNo == null) ? 0 : seqNo.hashCode());
		result = prime * result + ((specification == null) ? 0 : specification.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		result = prime * result + ((updator == null) ? 0 : updator.hashCode());
		result = prime * result + ((usePlace == null) ? 0 : usePlace.hashCode());
		result = prime * result + ((useWorker == null) ? 0 : useWorker.hashCode());
		result = prime * result + ((useWorkerId == null) ? 0 : useWorkerId.hashCode());
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
		InspectPlanEquipment other = (InspectPlanEquipment) obj;
		if (checkResult == null) {
			if (other.checkResult != null)
				return false;
		} else if (!checkResult.equals(other.checkResult))
			return false;
		if (checkResultDesc == null) {
			if (other.checkResultDesc != null)
				return false;
		} else if (!checkResultDesc.equals(other.checkResultDesc))
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
		if (equipmentCode == null) {
			if (other.equipmentCode != null)
				return false;
		} else if (!equipmentCode.equals(other.equipmentCode))
			return false;
		if (equipmentIdx == null) {
			if (other.equipmentIdx != null)
				return false;
		} else if (!equipmentIdx.equals(other.equipmentIdx))
			return false;
		if (equipmentName == null) {
			if (other.equipmentName != null)
				return false;
		} else if (!equipmentName.equals(other.equipmentName))
			return false;
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (planIdx == null) {
			if (other.planIdx != null)
				return false;
		} else if (!planIdx.equals(other.planIdx))
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
		if (seqNo == null) {
			if (other.seqNo != null)
				return false;
		} else if (!seqNo.equals(other.seqNo))
			return false;
		if (specification == null) {
			if (other.specification != null)
				return false;
		} else if (!specification.equals(other.specification))
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
		if (usePlace == null) {
			if (other.usePlace != null)
				return false;
		} else if (!usePlace.equals(other.usePlace))
			return false;
		if (useWorker == null) {
			if (other.useWorker != null)
				return false;
		} else if (!useWorker.equals(other.useWorker))
			return false;
		if (useWorkerId == null) {
			if (other.useWorkerId != null)
				return false;
		} else if (!useWorkerId.equals(other.useWorkerId))
			return false;
		return true;
	}

}
