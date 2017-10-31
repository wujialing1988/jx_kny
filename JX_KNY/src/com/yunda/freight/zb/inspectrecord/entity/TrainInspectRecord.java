package com.yunda.freight.zb.inspectrecord.entity;

import java.io.Serializable;
import java.util.Date;

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
 * <li>说明：货车客车巡检信息录入
 * <li>创建人：黄杨
 * <li>创建日期：2017-6-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "TRAIN_INSPECTINFO_RECORD")
public class TrainInspectRecord implements Serializable {

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/* idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/* 列车主键 */
	@Column(name = "TRAIN_IDX")
	private String trainIdx;

	/* 车号 */
	@Column(name = "TRAIN_NO")
	private String trainNo;

	/* 责任人Id */
	@Column(name = "PERSON_RESPONSIBLE_ID")
	private String personResponsibleId;

	/* 责任人 */
	@Column(name = "PERSON_RESPONSIBLE")
	private String personResponsible;

	/* 录入人Id */
	@Column(name = "RECORD_PERSON_ID")
	private String recordPersonId;

	/* 录入人 */
	@Column(name = "RECORD_PERSON")
	private String recordPerson;

	/* 巡检类型 */
	@Column(name = "INSPECT_TYPE")
	private String inspectType;

	/* 巡检详情描述 */
	@Column(name = "INSPECT_DETAIL")
	private String inspectDetail;

	/* 录入时间 */
	@Column(name = "RECORDTIME")
	private Date recordTime;

	/* 客货类型 10 货车 20 客车*/
	@Column(name = "T_VEHICLE_TYPE")
	private String vehicleType;
	
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
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getInspectDetail() {
		return inspectDetail;
	}

	public void setInspectDetail(String inspectDetail) {
		this.inspectDetail = inspectDetail;
	}

	public String getInspectType() {
		return inspectType;
	}

	public void setInspectType(String inspectType) {
		this.inspectType = inspectType;
	}

	public String getPersonResponsible() {
		return personResponsible;
	}

	public void setPersonResponsible(String personResponsible) {
		this.personResponsible = personResponsible;
	}

	public String getPersonResponsibleId() {
		return personResponsibleId;
	}

	public void setPersonResponsibleId(String personResponsibleId) {
		this.personResponsibleId = personResponsibleId;
	}

	public String getRecordPerson() {
		return recordPerson;
	}

	public void setRecordPerson(String recordPerson) {
		this.recordPerson = recordPerson;
	}

	public String getRecordPersonId() {
		return recordPersonId;
	}

	public void setRecordPersonId(String recordPersonId) {
		this.recordPersonId = recordPersonId;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public String getTrainIdx() {
		return trainIdx;
	}

	public void setTrainIdx(String trainIdx) {
		this.trainIdx = trainIdx;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdator() {
		return updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	
	

}
