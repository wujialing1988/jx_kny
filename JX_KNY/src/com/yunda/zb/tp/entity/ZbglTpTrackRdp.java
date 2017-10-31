package com.yunda.zb.tp.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpTrackRdp实体类, 数据表：JT6提票跟踪
 * <li>创建人：林欢
 * <li>创建日期：2016-8-5
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "zb_zbgl_jt6_track_rdp")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglTpTrackRdp implements java.io.Serializable {

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 结束跟踪 */
	public static final Integer TRACKOVER = 1;

	/** 正在跟踪 */
	public static final Integer TRACKING = 0;
	
//	/** 开始跟踪操作 */
//	public static final Integer STARTDOING = 0;
//
//	/** 完成本次跟踪操作 */
//	public static final Integer ENDDOING = 1;

	/* idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/* 车型编码 */
	@Column(name = "Train_Type_IDX")
	private String trainTypeIDX;

	/* 车型拼音码 */
	@Column(name = "Train_Type_ShortName")
	private String trainTypeShortName;

	/* 车号 */
	@Column(name = "Train_No")
	private String trainNo;
	
	/* 本次跟踪操作状态 */
	@Column(name = "Single_Status")
	private Integer singleStatus;

	/* jt6提票主键idx */
	@Column(name = "jt6_idx")
	private String jt6IDX;

	/* 跟踪原因 */
	@Column(name = "track_reason")
	private String trackReason;

	/* 跟踪人员 */
	@Column(name = "track_person_idx")
	private String trackPersonIDX;

	/* 跟踪人员姓名 */
	@Column(name = "track_person_name")
	private String trackPersonName;

	/* 跟踪状态(1==结束跟踪 0==正在跟踪) */
	@Column(name = "status")
	private Integer status;

	/* 已记录次数 */
	@Column(name = "record_count")
	private Integer recordCount;

	/* 提票单号 */
	@Column(name = "Fault_Notice_Code")
	private String faultNoticeCode;

	/* 跟踪时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "track_date")
	private Date trackDate;
	
	/* 跟踪单对应的整备单 */
	@Column(name = "rdp_idx")
	private String rdpIDX;


	@Transient
	private String startTime;// 查询开始时间

	@Transient
	private String EndTime;// 查询结束时间
	
	@Transient
	private String dId;// 

	@Transient
	private String dName;// 
	
	@Transient
	private String greater;// 大于

	@Transient
	private String less;// 小于

	public String getGreater() {
		return greater;
	}

	public void setGreater(String greater) {
		this.greater = greater;
	}

	public String getLess() {
		return less;
	}

	public void setLess(String less) {
		this.less = less;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getFaultNoticeCode() {
		return faultNoticeCode;
	}

	public void setFaultNoticeCode(String faultNoticeCode) {
		this.faultNoticeCode = faultNoticeCode;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getJt6IDX() {
		return jt6IDX;
	}

	public void setJt6IDX(String jt6IDX) {
		this.jt6IDX = jt6IDX;
	}

	public Integer getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public java.util.Date getTrackDate() {
		return trackDate;
	}

	public void setTrackDate(java.util.Date trackDate) {
		this.trackDate = trackDate;
	}

	public String getTrackPersonIDX() {
		return trackPersonIDX;
	}

	public void setTrackPersonIDX(String trackPersonIDX) {
		this.trackPersonIDX = trackPersonIDX;
	}

	public String getTrackPersonName() {
		return trackPersonName;
	}

	public void setTrackPersonName(String trackPersonName) {
		this.trackPersonName = trackPersonName;
	}

	public String getTrackReason() {
		return trackReason;
	}

	public void setTrackReason(String trackReason) {
		this.trackReason = trackReason;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getTrainTypeIDX() {
		return trainTypeIDX;
	}

	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}

	public String getTrainTypeShortName() {
		return trainTypeShortName;
	}

	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}

	public String getRdpIDX() {
		return rdpIDX;
	}

	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
	}

	public Integer getSingleStatus() {
		return singleStatus;
	}

	public void setSingleStatus(Integer singleStatus) {
		this.singleStatus = singleStatus;
	}

	public String getDId() {
		return dId;
	}

	public void setDId(String id) {
		dId = id;
	}

	public String getDName() {
		return dName;
	}

	public void setDName(String name) {
		dName = name;
	}





}
