package com.yunda.freight.zb.detain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 扣车管理实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-20 17:26:28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_DETAIN_TRAIN")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class DetainTrain implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 状态 - 待检修 */
    public static final String TRAIN_STATE_NEW = "10"; 
    /** 状态 - 检修中 */
    public static final String TRAIN_STATE_HANDLING = "20";
    /** 状态 - 检修完成 */
    public static final String TRAIN_STATE_HANDLED = "30";
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
            
    /* 车辆车型ID */ 
    @Column(name = "TRAIN_TYPE_IDX")
    private java.lang.String trainTypeIdx;            
            
    /* 车辆车型编码 */ 
    @Column(name = "TRAIN_TYPE_CODE")
    private java.lang.String trainTypeCode;            
            
    /* 车辆车型名称 */ 
    @Column(name = "TRAIN_TYPE_NAME")
    private java.lang.String trainTypeName;            
            
    /* 车辆车号 */ 
    @Column(name = "TRAIN_NO")
    private java.lang.String trainNo;            
            
    /* 扣车状态 10 待检修 20 检修中  30 完成 */ 
    @Column(name = "DETAIN_STATUS")
    private java.lang.String detainStatus;            
            
    /* 申请人ID */ 
    @Column(name = "PROPOSER_IDX")
    private java.lang.Long proposerIdx;            
            
    /* 申请人 */ 
    @Column(name = "PROPOSER_NAME")
    private java.lang.String proposerName;         
    
    /* 申请时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PROPOSER_DATE")
    private java.util.Date proposerDate;
            
    /* 审批人ID */ 
    @Column(name = "APPROVE_IDX")
    private java.lang.Long approveIdx;            
            
    /* 审批人 */ 
    @Column(name = "APPROVE_NAME")
    private java.lang.String approveName;       
    
    /* 审批时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPROVE_DATE")
    private java.util.Date approveDate;
            
    /* 扣车类型编码 */ 
    @Column(name = "DETAIN_TYPE_CODE")
    private java.lang.String detainTypeCode;            
            
    /* 扣车类型名称 */ 
    @Column(name = "DETAIN_TYPE_NAME")
    private java.lang.String detainTypeName;            
            
    /* 申请原因 */ 
    @Column(name = "DETAIN_REASON")
    private java.lang.String detainReason;            
            
    /* 审批意见 */ 
    @Column(name = "APPROVE_OPINION")
    private java.lang.String approveOpinion;            
            
    /* 命令发布者 */ 
    @Column(name = "ORDER_USER")
    private java.lang.String orderUser;     
    
    /* 命令发布时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ORDER_DATE")
    private java.util.Date orderDate;
            
    /* 命令号 */ 
    @Column(name = "ORDER_NO")
    private java.lang.String orderNo;            
            
    /* 站点ID */ 
    @Column(name = "SITE_ID")
    private java.lang.String siteID;            
            
    /* 站点名称 */ 
    @Column(name = "SITE_NAME")
    private java.lang.String siteName;            
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;
    
    /* 修程编码 */
    @Column(name = "Repair_Class_IDX")
    private String repairClassIDX;
    
    /* 修程名称 */
    @Column(name = "Repair_Class_Name")
    private String repairClassName;
    
    /* 修次 */
    @Column(name = "Repair_time_IDX")
    private String repairtimeIDX;
    
    /* 修次名称 */
    @Column(name = "Repair_time_Name")
    private String repairtimeName;
    
    /* 检修时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "jx_Time")
    private java.util.Date jxTime;
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public java.lang.String getTrainTypeIdx() {
        return this.trainTypeIdx;
    }
    
    public void setTrainTypeIdx(java.lang.String value) {
        this.trainTypeIdx = value;
    }
    public java.lang.String getTrainTypeCode() {
        return this.trainTypeCode;
    }
    
    public void setTrainTypeCode(java.lang.String value) {
        this.trainTypeCode = value;
    }
    public java.lang.String getTrainTypeName() {
        return this.trainTypeName;
    }
    
    public void setTrainTypeName(java.lang.String value) {
        this.trainTypeName = value;
    }
    public java.lang.String getTrainNo() {
        return this.trainNo;
    }
    
    public void setTrainNo(java.lang.String value) {
        this.trainNo = value;
    }
    public java.lang.String getDetainStatus() {
        return this.detainStatus;
    }
    
    public void setDetainStatus(java.lang.String value) {
        this.detainStatus = value;
    }
    public java.lang.Long getProposerIdx() {
        return this.proposerIdx;
    }
    
    public void setProposerIdx(java.lang.Long value) {
        this.proposerIdx = value;
    }
    public java.lang.String getProposerName() {
        return this.proposerName;
    }
    
    public void setProposerName(java.lang.String value) {
        this.proposerName = value;
    }
    public java.util.Date getProposerDate() {
        return this.proposerDate;
    }
    
    public void setProposerDate(java.util.Date value) {
        this.proposerDate = value;
    }
    public java.lang.Long getApproveIdx() {
        return this.approveIdx;
    }
    
    public void setApproveIdx(java.lang.Long value) {
        this.approveIdx = value;
    }
    public java.lang.String getApproveName() {
        return this.approveName;
    }
    
    public void setApproveName(java.lang.String value) {
        this.approveName = value;
    }
    public java.util.Date getApproveDate() {
        return this.approveDate;
    }
    
    public void setApproveDate(java.util.Date value) {
        this.approveDate = value;
    }
    public java.lang.String getDetainTypeCode() {
        return this.detainTypeCode;
    }
    
    public void setDetainTypeCode(java.lang.String value) {
        this.detainTypeCode = value;
    }
    public java.lang.String getDetainTypeName() {
        return this.detainTypeName;
    }
    
    public void setDetainTypeName(java.lang.String value) {
        this.detainTypeName = value;
    }
    public java.lang.String getDetainReason() {
        return this.detainReason;
    }
    
    public void setDetainReason(java.lang.String value) {
        this.detainReason = value;
    }
    public java.lang.String getApproveOpinion() {
        return this.approveOpinion;
    }
    
    public void setApproveOpinion(java.lang.String value) {
        this.approveOpinion = value;
    }
    public java.lang.String getOrderUser() {
        return this.orderUser;
    }
    
    public void setOrderUser(java.lang.String value) {
        this.orderUser = value;
    }
    public java.util.Date getOrderDate() {
        return this.orderDate;
    }
    
    public void setOrderDate(java.util.Date value) {
        this.orderDate = value;
    }
    public java.lang.String getOrderNo() {
        return this.orderNo;
    }
    
    public void setOrderNo(java.lang.String value) {
        this.orderNo = value;
    }

    
    public java.lang.String getSiteID() {
        return siteID;
    }

    public void setSiteID(java.lang.String siteID) {
        this.siteID = siteID;
    }

    public java.lang.String getSiteName() {
        return this.siteName;
    }
    
    public void setSiteName(java.lang.String value) {
        this.siteName = value;
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

    
    public String getVehicleType() {
        return vehicleType;
    }

    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

	public String getRepairClassIDX() {
		return repairClassIDX;
	}

	public void setRepairClassIDX(String repairClassIDX) {
		this.repairClassIDX = repairClassIDX;
	}

	public String getRepairClassName() {
		return repairClassName;
	}

	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}

	public String getRepairtimeIDX() {
		return repairtimeIDX;
	}

	public void setRepairtimeIDX(String repairtimeIDX) {
		this.repairtimeIDX = repairtimeIDX;
	}

	public String getRepairtimeName() {
		return repairtimeName;
	}

	public void setRepairtimeName(String repairtimeName) {
		this.repairtimeName = repairtimeName;
	}

	public java.util.Date getJxTime() {
		return jxTime;
	}

	public void setJxTime(java.util.Date jxTime) {
		this.jxTime = jxTime;
	}
    
}

