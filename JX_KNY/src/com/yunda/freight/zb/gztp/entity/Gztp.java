package com.yunda.freight.zb.gztp.entity;

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
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检_故障提票实体
 * <li>创建人：何东
 * <li>创建日期：2017-04-12 10:48:28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_GZTP")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class Gztp implements java.io.Serializable {

	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** 票活状态：初始化 */
    public static final String STATUS_INIT = "INITIALIZE";
    public static final String STATUS_INIT_CH = "未处理";
    
    /** 票活状态：已处理 */
    public static final String STATUS_OVER = "COMPLETE";
    public static final String STATUS_OVER_CH = "已处理";
    
    /** 票活状态：已检验 */
    public static final String STATUS_CHECKED = "CHECKED";
    public static final String STATUS_CHECKED_CH = "已检验";
    
    /** 处理类型：登记 */
    public static final int HANDLE_TYPE_REG = 0;
    public static final String HANDLE_TYPE_REG_CH = "登记";
    
    /** 处理类型：上报 */
    public static final int HANDLE_TYPE_REP = 1;
    public static final String HANDLE_TYPE_REP_CH = "上报";
	
	/* idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/* 列检计划主键 */
	@Column(name = "RDP_PLAN_IDX")
	private java.lang.String rdpPlanIdx;

	/* 列车车次 */
	@Column(name = "RAILWAY_TIME")
	private java.lang.String railwayTime;

	/* 车型主键 */
	@Column(name = "VEHICLE_TYPE_IDX")
	private java.lang.String vehicleTypeIdx;

	/* 车型代码 */
	@Column(name = "VEHICLE_TYPE_CODE")
	private java.lang.String vehicleTypeCode;

	/* 车辆计划主键 */
	@Column(name = "RDP_RECORD_PLAN_IDX")
	private java.lang.String rdpRecordPlanIdx;

	/* 车号 */
	@Column(name = "TRAIN_NO")
	private java.lang.String trainNo;

	/* 列检车辆数量 */
	@Column(name = "RDP_NUM")
	private Integer rdpNum;

	/* 故障类型字典KEY */
	@Column(name = "FAULT_TYPE_KEY")
	private java.lang.String faultTypeKey;

	/* 故障类型字典值 */
	@Column(name = "FAULT_TYPE_VALUE")
	private java.lang.String faultTypeValue;

	/* 作业范围主键 */
	@Column(name = "SCOPE_WORK_IDX")
	private java.lang.String scopeWorkIdx;

	/* 作业范围全称 */
	@Column(name = "SCOPE_WORK_FULLNAME")
	private java.lang.String scopeWorkFullname;

	/* 故障部件分类编码 */
	@Column(name = "VEHICLE_COMPONENT_FLBM")
	private java.lang.String vehicleComponentFlbm;

	/* 故障部件全称 */
	@Column(name = "VEHICLE_COMPONENT_FULLNAME")
	private java.lang.String vehicleComponentFullname;

	/* 故障描述 */
	@Column(name = "FAULT_DESC")
	private java.lang.String faultDesc;

	/* 提票单号 */
	@Column(name = "FAULT_NOTICE_CODE")
	private java.lang.String faultNoticeCode;

	/* 提票人ID */
	@Column(name = "NOTICE_PERSON_ID")
	private java.lang.Long noticePersonId;

	/* 提票人名称 */
	@Column(name = "NOTICE_PERSON_NAME")
	private java.lang.String noticePersonName;
	
	/* 提票时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NOTICE_TIME")
	private java.util.Date noticeTime;

	/* 提票站场 */
	@Column(name = "SITE_ID")
	private java.lang.String siteId;

	/* 提票站场名称 */
	@Column(name = "SITE_NAME")
	private java.lang.String siteName;

	/* 销票人ID */
	@Column(name = "HANDLE_PERSON_ID")
	private java.lang.Long handlePersonId;

	/* 销票人名称 */
	@Column(name = "HANDLE_PERSON_NAME")
	private java.lang.String handlePersonName;
	
	/* 销票时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HANDLE_TIME")
	private java.util.Date handleTime;

	/* 处理地点 */
	@Column(name = "HANDLE_SITE")
	private java.lang.String handleSite;

	/* 处理方式值 */
	@Column(name = "HANDLE_WAY")
	private java.lang.String handleWay;
    
    /* 处理方式 */
    @Column(name = "HANDLE_WAY_VALUE")
    private java.lang.String handleWayValue;
	
	/* 处理类型：0表示登记，1表示上报 */
	@Column(name = "HANDLE_TYPE")
	private Integer handleType;

	/* 整备任务单ID */
	@Column(name = "RDP_IDX")
	private java.lang.String rdpIdx;

	/* 票活状态 */
	@Column(name = "FAULT_NOTICE_STATUS")
	private java.lang.String faultNoticeStatus;

	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name = "RECORD_STATUS")
	private Integer recordStatus;

	/* 创建人 */
	@Column(updatable = false)
	private Long creator;

	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME", updatable = false)
	private java.util.Date createTime;

	/* 修改人 */
	private Long updator;

	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	private java.util.Date updateTime;
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;

	/* 质量检验任务状态 */
	@Transient
	private String status;
	
	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public java.lang.String getRdpPlanIdx() {
		return this.rdpPlanIdx;
	}

	public void setRdpPlanIdx(java.lang.String value) {
		this.rdpPlanIdx = value;
	}

	public java.lang.String getRailwayTime() {
		return this.railwayTime;
	}

	public void setRailwayTime(java.lang.String value) {
		this.railwayTime = value;
	}

	public java.lang.String getVehicleTypeIdx() {
		return this.vehicleTypeIdx;
	}

	public void setVehicleTypeIdx(java.lang.String value) {
		this.vehicleTypeIdx = value;
	}

	public java.lang.String getVehicleTypeCode() {
		return this.vehicleTypeCode;
	}

	public void setVehicleTypeCode(java.lang.String value) {
		this.vehicleTypeCode = value;
	}

	public java.lang.String getRdpRecordPlanIdx() {
		return this.rdpRecordPlanIdx;
	}

	public void setRdpRecordPlanIdx(java.lang.String value) {
		this.rdpRecordPlanIdx = value;
	}

	public java.lang.String getTrainNo() {
		return this.trainNo;
	}

	public void setTrainNo(java.lang.String value) {
		this.trainNo = value;
	}

	public Integer getRdpNum() {
		return this.rdpNum;
	}

	public void setRdpNum(Integer value) {
		this.rdpNum = value;
	}

	public java.lang.String getFaultTypeKey() {
		return this.faultTypeKey;
	}

	public void setFaultTypeKey(java.lang.String value) {
		this.faultTypeKey = value;
	}

	public java.lang.String getFaultTypeValue() {
		return this.faultTypeValue;
	}

	public void setFaultTypeValue(java.lang.String value) {
		this.faultTypeValue = value;
	}

	public java.lang.String getScopeWorkIdx() {
		return this.scopeWorkIdx;
	}

	public void setScopeWorkIdx(java.lang.String value) {
		this.scopeWorkIdx = value;
	}

	public java.lang.String getScopeWorkFullname() {
		return this.scopeWorkFullname;
	}

	public void setScopeWorkFullname(java.lang.String value) {
		this.scopeWorkFullname = value;
	}

	public java.lang.String getVehicleComponentFlbm() {
		return this.vehicleComponentFlbm;
	}

	public void setVehicleComponentFlbm(java.lang.String value) {
		this.vehicleComponentFlbm = value;
	}

	public java.lang.String getVehicleComponentFullname() {
		return this.vehicleComponentFullname;
	}

	public void setVehicleComponentFullname(java.lang.String value) {
		this.vehicleComponentFullname = value;
	}

	public java.lang.String getFaultDesc() {
		return this.faultDesc;
	}

	public void setFaultDesc(java.lang.String value) {
		this.faultDesc = value;
	}

	public java.lang.String getFaultNoticeCode() {
		return this.faultNoticeCode;
	}

	public void setFaultNoticeCode(java.lang.String value) {
		this.faultNoticeCode = value;
	}

	public java.lang.Long getNoticePersonId() {
		return this.noticePersonId;
	}

	public void setNoticePersonId(java.lang.Long value) {
		this.noticePersonId = value;
	}

	public java.lang.String getNoticePersonName() {
		return this.noticePersonName;
	}

	public void setNoticePersonName(java.lang.String value) {
		this.noticePersonName = value;
	}

	public java.util.Date getNoticeTime() {
		return this.noticeTime;
	}

	public void setNoticeTime(java.util.Date value) {
		this.noticeTime = value;
	}

	public java.lang.String getSiteId() {
		return this.siteId;
	}

	public void setSiteId(java.lang.String value) {
		this.siteId = value;
	}

	public java.lang.String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(java.lang.String value) {
		this.siteName = value;
	}

	public java.lang.Long getHandlePersonId() {
		return this.handlePersonId;
	}

	public void setHandlePersonId(java.lang.Long value) {
		this.handlePersonId = value;
	}

	public java.lang.String getHandlePersonName() {
		return this.handlePersonName;
	}

	public void setHandlePersonName(java.lang.String value) {
		this.handlePersonName = value;
	}

	public java.util.Date getHandleTime() {
		return this.handleTime;
	}

	public void setHandleTime(java.util.Date value) {
		this.handleTime = value;
	}

	public java.lang.String getHandleSite() {
		return this.handleSite;
	}

	public void setHandleSite(java.lang.String value) {
		this.handleSite = value;
	}

	public java.lang.String getHandleWay() {
		return this.handleWay;
	}

	public void setHandleWay(java.lang.String value) {
		this.handleWay = value;
	}

	public java.lang.String getRdpIdx() {
		return this.rdpIdx;
	}

	public void setRdpIdx(java.lang.String value) {
		this.rdpIdx = value;
	}

	public java.lang.String getFaultNoticeStatus() {
		return this.faultNoticeStatus;
	}

	public void setFaultNoticeStatus(java.lang.String value) {
		this.faultNoticeStatus = value;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
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

	public Integer getHandleType() {
		return handleType;
	}

	public void setHandleType(Integer handleType) {
		this.handleType = handleType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
    public String getVehicleType() {
        return vehicleType;
    }

    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    
    public java.lang.String getHandleWayValue() {
        return handleWayValue;
    }

    
    public void setHandleWayValue(java.lang.String handleWayValue) {
        this.handleWayValue = handleWayValue;
    }
    
    

}
