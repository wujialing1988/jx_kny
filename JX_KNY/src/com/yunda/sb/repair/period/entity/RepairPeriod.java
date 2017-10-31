package com.yunda.sb.repair.period.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.yunda.sb.base.IEntity;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明： 设备检修周期
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
@Table(name = "E_SBJX_REPAIR_PERIOD")
public class RepairPeriod implements Serializable, IEntity {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备类别名称 */
	@Column(name = "CLASS_NAME")
	private String className;

	/** 设备类别编码 */
	@Column(name = "CLASS_CODE")
	private String classCode;

	/** 大修(年) */
	private Integer dx;

	/** 中修间隔(小修次) */
	private Integer zx;

	/** 小修周期(月) */
	private Integer xx;

	/** A类大修(年) */
	private Integer adx;

	/** A类中修间隔(小修次) */
	private Integer azx;

	/** A类小修周期(月) */
	private Integer axx;

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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Integer getDx() {
		return dx;
	}

	public void setDx(Integer dx) {
		this.dx = dx;
	}

	public Integer getZx() {
		return zx;
	}

	public void setZx(Integer zx) {
		this.zx = zx;
	}

	public Integer getXx() {
		return xx;
	}

	public void setXx(Integer xx) {
		this.xx = xx;
	}

	public Integer getAdx() {
		return adx;
	}

	public void setAdx(Integer adx) {
		this.adx = adx;
	}

	public Integer getAzx() {
		return azx;
	}

	public void setAzx(Integer azx) {
		this.azx = azx;
	}

	public Integer getAxx() {
		return axx;
	}

	public void setAxx(Integer axx) {
		this.axx = axx;
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
		result = prime * result + ((adx == null) ? 0 : adx.hashCode());
		result = prime * result + ((axx == null) ? 0 : axx.hashCode());
		result = prime * result + ((azx == null) ? 0 : azx.hashCode());
		result = prime * result + ((classCode == null) ? 0 : classCode.hashCode());
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((dx == null) ? 0 : dx.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
		result = prime * result + ((updator == null) ? 0 : updator.hashCode());
		result = prime * result + ((xx == null) ? 0 : xx.hashCode());
		result = prime * result + ((zx == null) ? 0 : zx.hashCode());
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
		RepairPeriod other = (RepairPeriod) obj;
		if (adx == null) {
			if (other.adx != null)
				return false;
		} else if (!adx.equals(other.adx))
			return false;
		if (axx == null) {
			if (other.axx != null)
				return false;
		} else if (!axx.equals(other.axx))
			return false;
		if (azx == null) {
			if (other.azx != null)
				return false;
		} else if (!azx.equals(other.azx))
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
		if (dx == null) {
			if (other.dx != null)
				return false;
		} else if (!dx.equals(other.dx))
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
		if (xx == null) {
			if (other.xx != null)
				return false;
		} else if (!xx.equals(other.xx))
			return false;
		if (zx == null) {
			if (other.zx != null)
				return false;
		} else if (!zx.equals(other.zx))
			return false;
		return true;
	}

}
