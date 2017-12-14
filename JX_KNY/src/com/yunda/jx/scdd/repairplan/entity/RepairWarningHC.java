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
 * <li>说明: RepairWarningHC实体类, 数据表：货车修程预警
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-12-13
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_REPAIR_WARNING_HC")
public class RepairWarningHC implements java.io.Serializable {
    
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
    
    /* 上次辅修时间 */
    @Column(name = "BEFORE_FX_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date beforeFxDate;
    
    /* 上次段修时间 */
    @Column(name = "BEFORE_DX_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date beforeDxDate;
    
    /* 上次厂修时间 */
    @Column(name = "BEFORE_CX_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date beforeCxDate;
    
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

	public java.util.Date getBeforeFxDate() {
		return beforeFxDate;
	}

	public void setBeforeFxDate(java.util.Date beforeFxDate) {
		this.beforeFxDate = beforeFxDate;
	}

	public java.util.Date getBeforeDxDate() {
		return beforeDxDate;
	}

	public void setBeforeDxDate(java.util.Date beforeDxDate) {
		this.beforeDxDate = beforeDxDate;
	}

	public java.util.Date getBeforeCxDate() {
		return beforeCxDate;
	}

	public void setBeforeCxDate(java.util.Date beforeCxDate) {
		this.beforeCxDate = beforeCxDate;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
   
}
