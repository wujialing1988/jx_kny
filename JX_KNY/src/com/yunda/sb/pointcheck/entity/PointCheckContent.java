package com.yunda.sb.pointcheck.entity;

import java.io.Serializable;

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
 * <li>说明：PointCheckContent，数据表：设备点检内容
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "E_SBJX_POINT_CHECK_CONTENT")
public class PointCheckContent implements Serializable {

	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 点检技术状态标志 - 良好 */
	public static final String STATE_FLAG_LH = "良好";

	/** 点检技术状态标志 - 不良 */
	public static final String STATE_FLAG_BL = "不良";

	/** 点检技术状态标志 - 待修 */
	public static final String STATE_FLAG_DX = "待修";

	/** 点检技术状态标志 - 修理*/
	public static final String STATE_FLAG_XL = "修理";

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备点检任务单idx主键 */
	@Column(name = "POINT_CHECK_IDX")
	private String pointCheckIdx;

	/** 点检内容 */
	@Column(name = "CHECK_CONTENT")
	private String checkContent;

	/** 技术状态标志：良好、不良、待修、修理 */
	@Column(name = "TECHNOLOGY_STATE_FLAG")
	private String technologyStateFlag;

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

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getTechnologyStateFlag() {
		return technologyStateFlag;
	}

	public void setTechnologyStateFlag(String technologyStateFlag) {
		this.technologyStateFlag = technologyStateFlag;
	}

	public String getPointCheckIdx() {
		return pointCheckIdx;
	}

	public void setPointCheckIdx(String bitCheckIdx) {
		this.pointCheckIdx = bitCheckIdx;
	}

	public String getCheckContent() {
		return checkContent;
	}

	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
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
		result = prime * result + ((checkContent == null) ? 0 : checkContent.hashCode());
		result = prime * result + ((pointCheckIdx == null) ? 0 : pointCheckIdx.hashCode());
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
		PointCheckContent other = (PointCheckContent) obj;
		if (checkContent == null) {
			if (other.checkContent != null)
				return false;
		} else if (!checkContent.equals(other.checkContent))
			return false;
		if (pointCheckIdx == null) {
			if (other.pointCheckIdx != null)
				return false;
		} else if (!pointCheckIdx.equals(other.pointCheckIdx))
			return false;
		return true;
	}

}
