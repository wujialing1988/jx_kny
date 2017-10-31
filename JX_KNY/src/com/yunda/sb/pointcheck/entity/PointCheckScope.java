package com.yunda.sb.pointcheck.entity;

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
 * <li>说明: PointCheckScope实体类，数据表：设备点检范围
 * <li>创建人：黄杨
 * <li>创建日期：2017年5月15日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
@Table(name = "E_SBJX_POINT_CHECK_SCOPE")
public class PointCheckScope implements IOrder, java.io.Serializable, Comparable<PointCheckScope> {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备类型编码 */
	@Column(name = "class_code")
	private String classCode;

	/** 设备类型名称 */
	@Column(name = "class_name")
	private String className;

	/** 设备类别名称首拼 */
	@Column(name = "class_name_py")
	private String classNamePY;

	/** 点检内容 */
	@Column(name = "check_content")
	private String checkContent;

	/** 检查项目首拼（用于根据首字母进行快速检索） */
	@Column(name = "check_content_py")
	private String checkContentPY;

	/** 顺序号 */
	@Column(name = "seq_no")
	private Integer seqNo;

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

	public PointCheckScope() {
	}

	/**
	 * <li>说明：构造方法
	 * <li>创建人：兰佳妮
	 * <li>创建日期：2016年9月26日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：	 
	 * @param classCode 设备类别编码
	 * @param seqNo 顺序号
	 * @param checkContent 点检内容
	 */
	public PointCheckScope(String classCode, Integer seqNo, String checkContent) {
		this.classCode = classCode;
		this.seqNo = seqNo;
		this.checkContent = checkContent;
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

	public String getCheckContent() {
		return checkContent;
	}

	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}

	public String getCheckContentPY() {
		return checkContentPY;
	}

	public void setCheckContentPY(String checkContentPY) {
		this.checkContentPY = checkContentPY;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PointCheckScope o) {
		return this.seqNo.compareTo(o.getSeqNo());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checkContent == null) ? 0 : checkContent.hashCode());
		result = prime * result + ((checkContentPY == null) ? 0 : checkContentPY.hashCode());
		result = prime * result + ((classCode == null) ? 0 : classCode.hashCode());
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((classNamePY == null) ? 0 : classNamePY.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
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
		PointCheckScope other = (PointCheckScope) obj;
		if (checkContent == null) {
			if (other.checkContent != null)
				return false;
		} else if (!checkContent.equals(other.checkContent))
			return false;
		if (checkContentPY == null) {
			if (other.checkContentPY != null)
				return false;
		} else if (!checkContentPY.equals(other.checkContentPY))
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
