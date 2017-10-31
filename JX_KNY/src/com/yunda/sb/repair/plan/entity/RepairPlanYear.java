package com.yunda.sb.repair.plan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.yunda.sb.repair.process.entity.RepairTaskList;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: RepairPlanYear，数据表：设备检修年计划（new）
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
@Table(name = "E_SBJX_REPAIR_PLAN_YEAR")
public class RepairPlanYear implements Serializable {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 修程  - 未设置【-1】 */
	public static int REPAIR_CLASS_NOSET = -1;

	/** 修程  - 小修【1】 */
	public static int REPAIR_CLASS_SMALL = 1;

	/** 修程  - 中修【2】 */
	public static int REPAIR_CLASS_MEDIUM = 2;

	/** 修程  - 项修【3】 */
	public static int REPAIR_CLASS_SUBJECT = 3;

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 设备主键 */
	@Column(name = "EQUIPMENT_IDX")
	private String equipmentIdx;

	/** 计划年度 */
	@Column(name = "plan_year")
	private Integer planYear;

	/** 1月修程，1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30 */
	@Column(name = "month_1", columnDefinition = "INT default -1")
	private Short month1;

	/** 2月修程，1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30 */
	@Column(name = "month_2", columnDefinition = "INT default -1")
	private Short month2;

	/** 3月修程，1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30 */
	@Column(name = "month_3", columnDefinition = "INT default -1")
	private Short month3;

	/** 4月修程，1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30 */
	@Column(name = "month_4", columnDefinition = "INT default -1")
	private Short month4;

	/** 5月修程，1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30 */
	@Column(name = "month_5", columnDefinition = "INT default -1")
	private Short month5;

	/** 6月修程，1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30 */
	@Column(name = "month_6", columnDefinition = "INT default -1")
	private Short month6;

	/** 7月修程，1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30 */
	@Column(name = "month_7", columnDefinition = "INT default -1")
	private Short month7;

	/** 8月修程，1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30 */
	@Column(name = "month_8", columnDefinition = "INT default -1")
	private Short month8;

	/** 9月修程，1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30 */
	@Column(name = "month_9", columnDefinition = "INT default -1")
	private Short month9;

	/** 10月修程，1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30 */
	@Column(name = "month_10", columnDefinition = "INT default -1")
	private Short month10;

	/** 11月修程，1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30 */
	@Column(name = "month_11", columnDefinition = "INT default -1")
	private Short month11;

	/** 12月修程，1:小修、2：中修、3：项修；以0结尾则表示该月计划已下发，如：10、20、30 */
	@Column(name = "month_12", columnDefinition = "INT default -1")
	private Short month12;

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
	 * <li>说明：初始化每个月的修程为“未设置”，用于添加设备的年计划时
	 * <li>创建人：何涛
	 * <li>创建日期：2016年7月24日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void init() {
		this.month1 = (short) REPAIR_CLASS_NOSET;
		this.month2 = (short) REPAIR_CLASS_NOSET;
		this.month3 = (short) REPAIR_CLASS_NOSET;
		this.month4 = (short) REPAIR_CLASS_NOSET;
		this.month5 = (short) REPAIR_CLASS_NOSET;
		this.month6 = (short) REPAIR_CLASS_NOSET;
		this.month7 = (short) REPAIR_CLASS_NOSET;
		this.month8 = (short) REPAIR_CLASS_NOSET;
		this.month9 = (short) REPAIR_CLASS_NOSET;
		this.month10 = (short) REPAIR_CLASS_NOSET;
		this.month11 = (short) REPAIR_CLASS_NOSET;
		this.month12 = (short) REPAIR_CLASS_NOSET;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getEquipmentIdx() {
		return equipmentIdx;
	}

	public void setEquipmentIdx(String equipmentIdx) {
		this.equipmentIdx = equipmentIdx;
	}

	public Integer getPlanYear() {
		return planYear;
	}

	public void setPlanYear(Integer planYear) {
		this.planYear = planYear;
	}

	public Short getMonth1() {
		return month1;
	}

	public void setMonth1(Short month1) {
		this.month1 = month1;
	}

	public Short getMonth2() {
		return month2;
	}

	public void setMonth2(Short month2) {
		this.month2 = month2;
	}

	public Short getMonth3() {
		return month3;
	}

	public void setMonth3(Short month3) {
		this.month3 = month3;
	}

	public Short getMonth4() {
		return month4;
	}

	public void setMonth4(Short month4) {
		this.month4 = month4;
	}

	public Short getMonth5() {
		return month5;
	}

	public void setMonth5(Short month5) {
		this.month5 = month5;
	}

	public Short getMonth6() {
		return month6;
	}

	public void setMonth6(Short month6) {
		this.month6 = month6;
	}

	public Short getMonth7() {
		return month7;
	}

	public void setMonth7(Short month7) {
		this.month7 = month7;
	}

	public Short getMonth8() {
		return month8;
	}

	public void setMonth8(Short month8) {
		this.month8 = month8;
	}

	public Short getMonth9() {
		return month9;
	}

	public void setMonth9(Short month9) {
		this.month9 = month9;
	}

	public Short getMonth10() {
		return month10;
	}

	public void setMonth10(Short month10) {
		this.month10 = month10;
	}

	public Short getMonth11() {
		return month11;
	}

	public void setMonth11(Short month11) {
		this.month11 = month11;
	}

	public Short getMonth12() {
		return month12;
	}

	public void setMonth12(Short month12) {
		this.month12 = month12;
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

	/**
	 * <li>说明：获取修程的中文名称：小、中、项
	 * <li>创建人：何涛
	 * <li>创建日期：2016年8月26日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param repairClass 修程，1:小修、2：中修、3：项修；
	 * @return 修程的中文名称：小、中、项
	 */
	public static String getRepairClassName(Short repairClass) {
		if (RepairPlanYear.REPAIR_CLASS_SMALL == repairClass) {
			return RepairTaskList.REPAIR_CLASS_NAME_SMALL;
		}
		if (RepairPlanYear.REPAIR_CLASS_MEDIUM == repairClass) {
			return RepairTaskList.REPAIR_CLASS_NAME_MEDIUM;
		}
		if (RepairPlanYear.REPAIR_CLASS_SUBJECT == repairClass) {
			return RepairTaskList.REPAIR_CLASS_NAME_SUBJECT;
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((equipmentIdx == null) ? 0 : equipmentIdx.hashCode());
		result = prime * result + ((idx == null) ? 0 : idx.hashCode());
		result = prime * result + ((month1 == null) ? 0 : month1.hashCode());
		result = prime * result + ((month10 == null) ? 0 : month10.hashCode());
		result = prime * result + ((month11 == null) ? 0 : month11.hashCode());
		result = prime * result + ((month12 == null) ? 0 : month12.hashCode());
		result = prime * result + ((month2 == null) ? 0 : month2.hashCode());
		result = prime * result + ((month3 == null) ? 0 : month3.hashCode());
		result = prime * result + ((month4 == null) ? 0 : month4.hashCode());
		result = prime * result + ((month5 == null) ? 0 : month5.hashCode());
		result = prime * result + ((month6 == null) ? 0 : month6.hashCode());
		result = prime * result + ((month7 == null) ? 0 : month7.hashCode());
		result = prime * result + ((month8 == null) ? 0 : month8.hashCode());
		result = prime * result + ((month9 == null) ? 0 : month9.hashCode());
		result = prime * result + ((planYear == null) ? 0 : planYear.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
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
		RepairPlanYear other = (RepairPlanYear) obj;
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
		if (month1 == null) {
			if (other.month1 != null)
				return false;
		} else if (!month1.equals(other.month1))
			return false;
		if (month10 == null) {
			if (other.month10 != null)
				return false;
		} else if (!month10.equals(other.month10))
			return false;
		if (month11 == null) {
			if (other.month11 != null)
				return false;
		} else if (!month11.equals(other.month11))
			return false;
		if (month12 == null) {
			if (other.month12 != null)
				return false;
		} else if (!month12.equals(other.month12))
			return false;
		if (month2 == null) {
			if (other.month2 != null)
				return false;
		} else if (!month2.equals(other.month2))
			return false;
		if (month3 == null) {
			if (other.month3 != null)
				return false;
		} else if (!month3.equals(other.month3))
			return false;
		if (month4 == null) {
			if (other.month4 != null)
				return false;
		} else if (!month4.equals(other.month4))
			return false;
		if (month5 == null) {
			if (other.month5 != null)
				return false;
		} else if (!month5.equals(other.month5))
			return false;
		if (month6 == null) {
			if (other.month6 != null)
				return false;
		} else if (!month6.equals(other.month6))
			return false;
		if (month7 == null) {
			if (other.month7 != null)
				return false;
		} else if (!month7.equals(other.month7))
			return false;
		if (month8 == null) {
			if (other.month8 != null)
				return false;
		} else if (!month8.equals(other.month8))
			return false;
		if (month9 == null) {
			if (other.month9 != null)
				return false;
		} else if (!month9.equals(other.month9))
			return false;
		if (planYear == null) {
			if (other.planYear != null)
				return false;
		} else if (!planYear.equals(other.planYear))
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
		return true;
	}

}
