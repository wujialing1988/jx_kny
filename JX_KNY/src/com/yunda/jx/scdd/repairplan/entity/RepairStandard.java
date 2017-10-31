package com.yunda.jx.scdd.repairplan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * <li>说明：检修标准
 * <li>创建人： 张凡
 * <li>创建日期：2015年11月4日
 * <li>成都运达科技股份有限公司
 */
@Entity
@Table(name="JCJX_REPAIR_STANDARD")
public class RepairStandard implements java.io.Serializable{
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
	/* 修次 */
	@Column(name="REPAIR_ORDER")
	private String repairOrder;
	/* 修次名称 */
	@Column(name="REPAIR_ORDER_NAME")
	private String repairOrderName;
	/* 最小走行公里 */
	@Column(name="MIN_RUNNING_KM")
	private Float minRunningKm;
	/* 最大走行公里 */
	@Column(name="MAX_RUNNING_KM")
	private Float maxRunningKm;
	/* 站点ID */
	@Column(name="SITEID")
	private String siteid;
	/*
	 * getter setter
	 */
	/**
	 * @return 获取主键
	 */
	public String getIdx(){
		return this.idx;
	}
	/**
	 * @param idx 设置主键
	 */
	public void setIdx(String idx){
		this.idx = idx;
	}
	/**
	 * @return 获取车型
	 */
	public String getTrainTypeIdx(){
		return this.trainTypeIdx;
	}
	/**
	 * @param trainTypeIdx 设置车型
	 */
	public void setTrainTypeIdx(String trainTypeIdx){
		this.trainTypeIdx = trainTypeIdx;
	}
	/**
	 * @return 获取修程
	 */
	public String getRepairClass(){
		return this.repairClass;
	}
	/**
	 * @param repairClass 设置修程
	 */
	public void setRepairClass(String repairClass){
		this.repairClass = repairClass;
	}
	/**
	 * @return 获取修程名称
	 */
	public String getRepairClassName(){
		return this.repairClassName;
	}
	/**
	 * @param repairClassName 设置修程名称
	 */
	public void setRepairClassName(String repairClassName){
		this.repairClassName = repairClassName;
	}
	/**
	 * @return 获取修次
	 */
	public String getRepairOrder(){
		return this.repairOrder;
	}
	/**
	 * @param repairOrder 设置修次
	 */
	public void setRepairOrder(String repairOrder){
		this.repairOrder = repairOrder;
	}
	/**
	 * @return 获取修次名称
	 */
	public String getRepairOrderName(){
		return this.repairOrderName;
	}
	/**
	 * @param repairOrderName 设置修次名称
	 */
	public void setRepairOrderName(String repairOrderName){
		this.repairOrderName = repairOrderName;
	}
	/**
	 * @return 获取最小走行公里
	 */
	public Float getMinRunningKm(){
		return this.minRunningKm;
	}
	/**
	 * @param minRunningKm 设置最小走行公里
	 */
	public void setMinRunningKm(Float minRunningKm){
		this.minRunningKm = minRunningKm;
	}
	/**
	 * @return 获取最大走行公里
	 */
	public Float getMaxRunningKm(){
		return this.maxRunningKm;
	}
	/**
	 * @param maxRunningKm 设置最大走行公里
	 */
	public void setMaxRunningKm(Float maxRunningKm){
		this.maxRunningKm = maxRunningKm;
	}
	/**
	 * @return 获取站点ID
	 */
	public String getSiteid(){
		return this.siteid;
	}
	/**
	 * @param siteid 设置站点ID
	 */
	public void setSiteid(String siteid){
		this.siteid = siteid;
	}
}
