package com.yunda.sb.repair.process.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.yunda.sb.base.IEntity;
import com.yunda.sb.base.combo.IPyFilter;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairWorkOrderStuff，数据表：设备检修用料
 * <li>创建人：黄杨
 * <li>创建日期：2016年12月9日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
@Table(name = "E_REPAIR_WORK_ORDER_STUFF")
public class RepairWorkOrderStuff implements java.io.Serializable, IEntity, IPyFilter {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备检修工单idx主键 */
	@Column(name = "repair_work_order_idx")
	private String repairWorkOrderIdx;

	/** 材料名称规格 */
	@Column(name = "stuff_name")
	private String stuffName;

	/** 材料名称规格首拼 */
	@Column(name = "stuff_name_py")
	private String stuffNamePY;

	/** 数量 */
	@Column(name = "stuff_number")
	private Float stuffNumber;

	/** 计量单位 */
	@Column(name = "stuff_unit")
	private String stuffUnit;

	/** 单价	*/
	@Column(name = "stuff_unit_price")
	private Float stuffUnitPrice;

	/** 金额	*/
	@Column(name = "stuff_total_money")
	private Float stuffTotalMoney;

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

	public String getRepairWorkOrderIdx() {
		return repairWorkOrderIdx;
	}

	public void setRepairWorkOrderIdx(String repairWorkOrderIdx) {
		this.repairWorkOrderIdx = repairWorkOrderIdx;
	}

	public String getStuffName() {
		return stuffName;
	}

	public void setStuffName(String stuffName) {
		this.stuffName = stuffName;
	}

	public String getStuffNamePY() {
		return stuffNamePY;
	}

	public void setStuffNamePY(String stuffNamePY) {
		this.stuffNamePY = stuffNamePY;
	}

	public Float getStuffNumber() {
		return stuffNumber;
	}

	public void setStuffNumber(Float stuffNumber) {
		this.stuffNumber = stuffNumber;
	}

	public String getStuffUnit() {
		return stuffUnit;
	}

	public void setStuffUnit(String stuffUnit) {
		this.stuffUnit = stuffUnit;
	}

	public Float getStuffUnitPrice() {
		return stuffUnitPrice;
	}

	public void setStuffUnitPrice(Float stuffUnitPrice) {
		this.stuffUnitPrice = stuffUnitPrice;
	}

	public Float getStuffTotalMoney() {
		return stuffTotalMoney;
	}

	public void setStuffTotalMoney(Float stuffTotalMoney) {
		this.stuffTotalMoney = stuffTotalMoney;
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
	public String getFieldName4PY(String xfield) {
		if ("stuffName".equals(xfield)) {
			return "stuffNamePY";
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((repairWorkOrderIdx == null) ? 0 : repairWorkOrderIdx.hashCode());
		result = prime * result + ((stuffName == null) ? 0 : stuffName.hashCode());
		result = prime * result + ((stuffNamePY == null) ? 0 : stuffNamePY.hashCode());
		result = prime * result + ((stuffNumber == null) ? 0 : stuffNumber.hashCode());
		result = prime * result + ((stuffTotalMoney == null) ? 0 : stuffTotalMoney.hashCode());
		result = prime * result + ((stuffUnit == null) ? 0 : stuffUnit.hashCode());
		result = prime * result + ((stuffUnitPrice == null) ? 0 : stuffUnitPrice.hashCode());
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
		RepairWorkOrderStuff other = (RepairWorkOrderStuff) obj;
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
		if (repairWorkOrderIdx == null) {
			if (other.repairWorkOrderIdx != null)
				return false;
		} else if (!repairWorkOrderIdx.equals(other.repairWorkOrderIdx))
			return false;
		if (stuffName == null) {
			if (other.stuffName != null)
				return false;
		} else if (!stuffName.equals(other.stuffName))
			return false;
		if (stuffNamePY == null) {
			if (other.stuffNamePY != null)
				return false;
		} else if (!stuffNamePY.equals(other.stuffNamePY))
			return false;
		if (stuffNumber == null) {
			if (other.stuffNumber != null)
				return false;
		} else if (!stuffNumber.equals(other.stuffNumber))
			return false;
		if (stuffTotalMoney == null) {
			if (other.stuffTotalMoney != null)
				return false;
		} else if (!stuffTotalMoney.equals(other.stuffTotalMoney))
			return false;
		if (stuffUnit == null) {
			if (other.stuffUnit != null)
				return false;
		} else if (!stuffUnit.equals(other.stuffUnit))
			return false;
		if (stuffUnitPrice == null) {
			if (other.stuffUnitPrice != null)
				return false;
		} else if (!stuffUnitPrice.equals(other.stuffUnitPrice))
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
