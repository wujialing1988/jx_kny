package com.yunda.jx.scdd.repairplan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: RepairWarningHC实体类, 数据表：客车修程预警
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-12-13
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_REPAIR_WARNING_KC")
public class RepairWarningKC implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    /* 主键 */
    @Column(name = "IDX")
    private String idx;
    
    /* 车型主键 */
    @Column(name = "TRAIN_TYPE_IDX")
    private String trainTypeIdx;
    
    /* 车型 */
    @Column(name = "TRAIN_TYPE")
    private String trainType;
    
    /* 车号 */
    @Column(name = "TRAIN_NO")
    private String trainNo;
    
    /* 上次A1时间 */
    @Column(name = "BEFORE_A1_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date beforeA1Date;
    
    /* 上次A2时间 */
    @Column(name = "BEFORE_A2_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date beforeA2Date;
    
    /* 上次A3时间 */
    @Column(name = "BEFORE_A3_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date beforeA3Date;
    
    /* 上次A4时间 */
    @Column(name = "BEFORE_A4_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date beforeA4Date;
    
    /* 上次A5时间 */
    @Column(name = "BEFORE_A5_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date beforeA5Date;
    
    /* 下次A1时间 */
    @Column(name = "NEXT_A1_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date nextA1Date;
    
    /* 下次A2时间 */
    @Column(name = "NEXT_A2_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date nextA2Date;
    
    /* 下次A3时间 */
    @Column(name = "NEXT_A3_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date nextA3Date;
    
    /* 下次A4时间 */
    @Column(name = "NEXT_A4_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date nextA4Date;
    
    /* 下次A5时间 */
    @Column(name = "NEXT_A5_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date nextA5Date;
    
    /* A1累计走行公里 */
    @Column(name = "A1_KM")
    private Float a1Km;
    
    /* A2累计走行公里 */
    @Column(name = "A2_KM")
    private Float a2Km;
    
    /* A3累计走行公里 */
    @Column(name = "A3_KM")
    private Float a3Km;
    
    /* A4累计走行公里 */
    @Column(name = "A4_KM")
    private Float a4Km;
    
    /* A5累计走行公里 */
    @Column(name = "A5_KM")
    private Float a5Km;
    
    /* 总行驶公里 不清零 */
    @Column(name = "TOTAL_KM")
    private Float totalKm;
    
    /* 下一修程 */
    @Column(name = "REPAIR_CLASS")
    private String repairClass;
    
    /* 下一修程名称 */
    @Column(name = "REPAIR_CLASS_NAME")
    private String repairClassName;
    
    /* 修程预警类型 10 走行判断 20 时间判断 */
    @Column(name = "REPAIR_WARNING_TYPE")
    private String repairWarningType;
    
    /* 备注 */
    @Column(name = "REMARK")
    private String remark;
    
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime; 

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

	public String getTrainType() {
		return trainType;
	}

	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}


	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public java.util.Date getBeforeA1Date() {
		return beforeA1Date;
	}

	public void setBeforeA1Date(java.util.Date beforeA1Date) {
		this.beforeA1Date = beforeA1Date;
	}

	public java.util.Date getBeforeA2Date() {
		return beforeA2Date;
	}

	public void setBeforeA2Date(java.util.Date beforeA2Date) {
		this.beforeA2Date = beforeA2Date;
	}

	public java.util.Date getBeforeA3Date() {
		return beforeA3Date;
	}

	public void setBeforeA3Date(java.util.Date beforeA3Date) {
		this.beforeA3Date = beforeA3Date;
	}

	public java.util.Date getBeforeA4Date() {
		return beforeA4Date;
	}

	public void setBeforeA4Date(java.util.Date beforeA4Date) {
		this.beforeA4Date = beforeA4Date;
	}

	public java.util.Date getBeforeA5Date() {
		return beforeA5Date;
	}

	public void setBeforeA5Date(java.util.Date beforeA5Date) {
		this.beforeA5Date = beforeA5Date;
	}

	public Float getA1Km() {
		return a1Km;
	}

	public void setA1Km(Float a1Km) {
		this.a1Km = a1Km;
	}

	public Float getA2Km() {
		return a2Km;
	}

	public void setA2Km(Float a2Km) {
		this.a2Km = a2Km;
	}

	public Float getA3Km() {
		return a3Km;
	}

	public void setA3Km(Float a3Km) {
		this.a3Km = a3Km;
	}

	public Float getA4Km() {
		return a4Km;
	}

	public void setA4Km(Float a4Km) {
		this.a4Km = a4Km;
	}

	public Float getA5Km() {
		return a5Km;
	}

	public void setA5Km(Float a5Km) {
		this.a5Km = a5Km;
	}

	public Float getTotalKm() {
		return totalKm;
	}

	public void setTotalKm(Float totalKm) {
		this.totalKm = totalKm;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public java.util.Date getNextA1Date() {
		return nextA1Date;
	}

	public void setNextA1Date(java.util.Date nextA1Date) {
		this.nextA1Date = nextA1Date;
	}

	public java.util.Date getNextA2Date() {
		return nextA2Date;
	}

	public void setNextA2Date(java.util.Date nextA2Date) {
		this.nextA2Date = nextA2Date;
	}

	public java.util.Date getNextA3Date() {
		return nextA3Date;
	}

	public void setNextA3Date(java.util.Date nextA3Date) {
		this.nextA3Date = nextA3Date;
	}

	public java.util.Date getNextA4Date() {
		return nextA4Date;
	}

	public void setNextA4Date(java.util.Date nextA4Date) {
		this.nextA4Date = nextA4Date;
	}

	public java.util.Date getNextA5Date() {
		return nextA5Date;
	}

	public void setNextA5Date(java.util.Date nextA5Date) {
		this.nextA5Date = nextA5Date;
	}

	public String getRepairWarningType() {
		return repairWarningType;
	}

	public void setRepairWarningType(String repairWarningType) {
		this.repairWarningType = repairWarningType;
	}
	
	
}
