package com.yunda.sb.inspect.scope.entity;

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
 * <li>标题: 机车设备管理信息系统
 * <li>说明：设备巡检项目（标准）
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
@Table(name = "E_INSPECT_SCOPE")
public class InspectScope implements java.io.Serializable, IOrder, Comparable<InspectScope> {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备类别编码 */
	@Column(name = "CLASS_CODE")
	private String classCode;

	/** 设备类别名称 */
	@Column(name = "CLASS_NAME")
	private String className;

	/** 设备类别名称首拼 */
	@Column(name = "class_name_py")
	private String classNamePY;

	/** 检修类型（1：机械、2：电气、3：其它） */
	@Column(name = "REPAIR_TYPE")
	private Integer repairType;

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
	 * <li>说明：默认构造方法
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月22日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：	 
	 */
	public InspectScope() {
	}

	/**
	 * <li>说明：构造方法
	 * <li>创建人：何涛
	 * <li>创建日期：2016年9月22日
	 * <li>修改人： 何涛
	 * <li>修改内容：删除方法参数className
	 * <li>修改日期：	2017年2月23日
	 * @param classCode 设备类别编码
	 * @param seqNo 顺序号
	 * @param checkItem 检查项目
	 * @param checkStandard 检查标准
	 * @param repairType 检修类型（1：机械、2：电气、3：其它）
	 */
	public InspectScope(String classCode, Integer seqNo, String checkItem, String checkStandard, Integer repairType) {
		this.classCode = classCode;
		this.seqNo = seqNo;
		this.checkItem = checkItem;
		this.checkStandard = checkStandard;
		this.repairType = repairType;
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

	public String getClassNamePY() {
		return classNamePY;
	}

	public void setClassNamePY(String classNamePY) {
		this.classNamePY = classNamePY;
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

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(InspectScope o) {
		return this.seqNo.compareTo(o.getSeqNo());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checkItem == null) ? 0 : checkItem.hashCode());
		result = prime * result + ((checkItemPY == null) ? 0 : checkItemPY.hashCode());
		result = prime * result + ((checkStandard == null) ? 0 : checkStandard.hashCode());
		result = prime * result + ((classCode == null) ? 0 : classCode.hashCode());
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((classNamePY == null) ? 0 : classNamePY.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
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
		InspectScope other = (InspectScope) obj;
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
		if (checkStandard == null) {
			if (other.checkStandard != null)
				return false;
		} else if (!checkStandard.equals(other.checkStandard))
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
		if (classNamePY == null) {
			if (other.classNamePY != null)
				return false;
		} else if (!classNamePY.equals(other.classNamePY))
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
