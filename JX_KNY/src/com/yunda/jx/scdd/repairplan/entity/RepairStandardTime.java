package com.yunda.jx.scdd.repairplan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * <li>说明：客车检修标准（时间维度）
 * <li>创建人： 伍佳灵
 * <li>创建日期：2017年12月21日
 * <li>成都运达科技股份有限公司
 */
@Entity
@Table(name="JCJX_REPAIR_STANDARD_TIME")
public class RepairStandardTime implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	/* 主键 */
	@Column(name="IDX")
	private String idx;
	/* 车型 */
	@Column(name="TRAIN_TYPE_IDX")
	private String trainTypeIdx;
	/* 修程 */
	@Column(name="REPAIR_CLASS")
	private String repairClass;
	/* 修程名称 */
	@Column(name="REPAIR_CLASS_NAME")
	private String repairClassName;
	
	/* 对比修程 */
	@Column(name="REPAIR_CLASS_COMPARE")
	private String repairClassCompare;
	
	/* 对比修程名称 */
	@Column(name="REPAIR_CLASS_COMPARE_NAME")
	private String repairClassCompareName;
	
	/* 对比天数 */
	@Column(name="COMPARE_DAY")
	private Integer compareDay;
	
	/* 站点ID */
	@Column(name="SITEID")
	private String siteid;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getTrainTypeIdx() {
		return trainTypeIdx;
	}

	public void setTrainTypeIdx(String trainTypeIdx) {
		this.trainTypeIdx = trainTypeIdx;
	}

	public String getRepairClass() {
		return repairClass;
	}

	public void setRepairClass(String repairClass) {
		this.repairClass = repairClass;
	}

	public String getRepairClassName() {
		return repairClassName;
	}

	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}

	public String getRepairClassCompare() {
		return repairClassCompare;
	}

	public void setRepairClassCompare(String repairClassCompare) {
		this.repairClassCompare = repairClassCompare;
	}

	public String getRepairClassCompareName() {
		return repairClassCompareName;
	}

	public void setRepairClassCompareName(String repairClassCompareName) {
		this.repairClassCompareName = repairClassCompareName;
	}

	public Integer getCompareDay() {
		return compareDay;
	}

	public void setCompareDay(Integer compareDay) {
		this.compareDay = compareDay;
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}
	
}
