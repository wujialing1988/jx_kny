package com.yunda.sb.inspect.route.entity;

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
 * <li>说明：InspectlRouteDetails实体类，数据表：设备巡检线路明细
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
@Table(name = "E_INSPECT_ROUTE_DETAILS")
public class InspectRouteDetails implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 巡检线路idx主键 */
	@Column(name = "ROUTE_IDX")
	private String routeIdx;

	/** 机械巡检人 */
	@Column(name = "MAC_INSPECT_EMPID")
	private String macInspectEmpid;

	/** 机械巡检人名称 */
	@Column(name = "MAC_INSPECT_EMP")
	private String macInspectEmp;

	/** 电气巡检人 */
	@Column(name = "ELC_INSPECT_EMPID")
	private String elcInspectEmpid;

	/** 电气巡检人名称 */
	@Column(name = "ELC_INSPECT_EMP")
	private String elcInspectEmp;

	/** 序号 */
	@Column(name = "SEQ_NO")
	private Integer seqNo;

	/** 设备主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

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
		return this.idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public Integer getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getCreator() {
		return this.creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdator() {
		return this.updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getMacInspectEmpid() {
		return macInspectEmpid;
	}

	public void setMacInspectEmpid(String macInspectEmpid) {
		this.macInspectEmpid = macInspectEmpid;
	}

	public String getMacInspectEmp() {
		return macInspectEmp;
	}

	public void setMacInspectEmp(String macInspectEmp) {
		this.macInspectEmp = macInspectEmp;
	}

	public String getElcInspectEmpid() {
		return elcInspectEmpid;
	}

	public void setElcInspectEmpid(String elcInspectEmpid) {
		this.elcInspectEmpid = elcInspectEmpid;
	}

	public String getElcInspectEmp() {
		return elcInspectEmp;
	}

	public void setElcInspectEmp(String elcInspectEmp) {
		this.elcInspectEmp = elcInspectEmp;
	}

	public String getRouteIdx() {
		return routeIdx;
	}

	public void setRouteIdx(String routeIdx) {
		this.routeIdx = routeIdx;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getEquipmentIdx() {
		return equipmentIdx;
	}

	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((elcInspectEmp == null) ? 0 : elcInspectEmp.hashCode());
		result = prime * result + ((elcInspectEmpid == null) ? 0 : elcInspectEmpid.hashCode());
		result = prime * result + ((equipmentIdx == null) ? 0 : equipmentIdx.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((macInspectEmp == null) ? 0 : macInspectEmp.hashCode());
		result = prime * result + ((macInspectEmpid == null) ? 0 : macInspectEmpid.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((routeIdx == null) ? 0 : routeIdx.hashCode());
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
		InspectRouteDetails other = (InspectRouteDetails) obj;
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
		if (elcInspectEmp == null) {
			if (other.elcInspectEmp != null)
				return false;
		} else if (!elcInspectEmp.equals(other.elcInspectEmp))
			return false;
		if (elcInspectEmpid == null) {
			if (other.elcInspectEmpid != null)
				return false;
		} else if (!elcInspectEmpid.equals(other.elcInspectEmpid))
			return false;
		if (equipmentIdx == null) {
			if (other.equipmentIdx != null)
				return false;
		} else if (!equipmentIdx.equals(other.equipmentIdx))
			return false;
		if (idx == null) {
			if (other.idx != null)
				return false;
		} else if (!idx.equals(other.idx))
			return false;
		if (macInspectEmp == null) {
			if (other.macInspectEmp != null)
				return false;
		} else if (!macInspectEmp.equals(other.macInspectEmp))
			return false;
		if (macInspectEmpid == null) {
			if (other.macInspectEmpid != null)
				return false;
		} else if (!macInspectEmpid.equals(other.macInspectEmpid))
			return false;
		if (recordStatus == null) {
			if (other.recordStatus != null)
				return false;
		} else if (!recordStatus.equals(other.recordStatus))
			return false;
		if (routeIdx == null) {
			if (other.routeIdx != null)
				return false;
		} else if (!routeIdx.equals(other.routeIdx))
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
