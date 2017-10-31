package com.yunda.passenger.traindemand.entity;

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
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 列车需求编组车辆日志记录表
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_MARSHALLING_TRAIN_LOG")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class MarshallingTrainDemand implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 列车需求IDX */ 
    @Column(name = "TRAIN_DEMAND_IDX")
    private java.lang.String trainDemandIdx;       
    
    /*编组车辆定义的idx */ 
    @Column(name = "MARSHALLING_TRAIN_IDX")
    private java.lang.String marshallingTrainIdx;   
    
    /* 编组编号 */ 
    @Column(name = "MARSHALLING_CODE")
    private java.lang.String marshallingCode;       
    
    /* 车种编号 */ 
    @Column(name = "T_VEHICLE_KIND_CODE")
    private java.lang.String vehicleKindCode;   
    
    /* 车种名称 */ 
    @Column(name = "T_VEHICLE_KIND_NAME")
    private java.lang.String vehicleKindName;   
    /* 车型主键 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;

	/* 车型英文简称 */
	@Column(name="Train_Type_ShortName")
	private String trainTypeShortName;   
	
    /* 车号 */ 
    @Column(name = "TRAIN_NO")
    private String trainNo;            
    /* 顺序号 */
    @Column(name = "SEQ_NO")
    private Integer seqNo;
    
    /* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
    
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
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
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

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public java.lang.String getVehicleKindCode() {
		return vehicleKindCode;
	}

	public void setVehicleKindCode(String vehicleKindCode) {
		this.vehicleKindCode = vehicleKindCode;
	}

	public String getVehicleKindName() {
		return vehicleKindName;
	}

	public void setVehicleKindName(String vehicleKindName) {
		this.vehicleKindName = vehicleKindName;
	}

	public String getMarshallingCode() {
		return marshallingCode;
	}

	public void setMarshallingCode(String marshallingCode) {
		this.marshallingCode = marshallingCode;
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
	public java.lang.String getTrainDemandIdx() {
		return trainDemandIdx;
	}

	public void setTrainDemandIdx(java.lang.String trainDemandIdx) {
		this.trainDemandIdx = trainDemandIdx;
	}

	public java.lang.String getMarshallingTrainIdx() {
		return marshallingTrainIdx;
	}

	public void setMarshallingTrainIdx(java.lang.String marshallingTrainIdx) {
		this.marshallingTrainIdx = marshallingTrainIdx;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
    
    
}

