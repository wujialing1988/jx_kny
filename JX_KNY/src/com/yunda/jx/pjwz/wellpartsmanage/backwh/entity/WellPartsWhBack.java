package com.yunda.jx.pjwz.wellpartsmanage.backwh.entity;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WellPartsWhBack实体类, 数据表：良好配件退库单
 * <li>创建人：程梅
 * <li>创建日期：2015-10-28
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJWZ_WELL_PARTS_WH_BACK")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WellPartsWhBack implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
    /* 库房主键 */
    @Column(name="WAREHOUSE_IDX")
    private String warehouseIdx;
    /* 库房名称 */
    @Column(name="WAREHOUSE_NAME")
    private String whName;
    /* 库管员主键 */
    @Column(name="WAREHOUSE_EMP_ID")
    private Long warehouseEmpId;
    /* 库管员 */
    @Column(name="WAREHOUSE_EMP")
    private String warehouseEmp;
    /* 交件人主键 */
    @Column(name="HAND_OVER_EMP_ID")
    private Long handoverEmpId;
    /* 交件人 */
    @Column(name="HAND_OVER_EMP")
    private String handoverEmp;
    /* 交件部门主键 */
    @Column(name="HAND_OVER_ORG_ID")
    private Long handoverOrgID;
    /* 交接部门 */
    @Column(name="HAND_OVER_ORG")
    private String handoverOrg;
    /* 退库日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="BACK_TIME")
    private java.util.Date backTime;
	/* 配件规格型号主键 */
	@Column(name="PARTS_TYPE_IDX")
	private String partsTypeIDX;
    /* 配件名称 */
    @Column(name="PARTS_NAME")
    private String partsName;
    /* 规格型号 */
    @Column(name="SPECIFICATION_MODEL")
    private String specificationModel;
    /* 配件信息表主键 */
    @Column(name="Parts_Account_IDX")
    private String partsAccountIDX;
    /* 配件编号 */
    @Column(name="Parts_No")
    private String partsNo;
    /* 配件铭牌号 */
    @Column(name="nameplate_No")
    private String nameplateNo;
	/* 配件识别码 */
	@Column(name="IDENTIFICATION_CODE")
	private String identificationCode;
	/* 单据状态 */
	private String status;
	/* 创建人名称 */
	@Column(name="CREATOR_NAME")
	private String creatorName;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="RECORD_STATUS")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private java.util.Date updateTime;
    /* 存放位置 */
    @Column(name="LOCATION_NAME")
    private String locationName;
	
    public String getLocationName() {
        return locationName;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    /**
	 * @return String 获取单据状态
	 */
	public String getStatus(){
		return status;
	}
	/**
	 * @param status 设置单据状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
    public java.util.Date getBackTime() {
        return backTime;
    }
    
    public void setBackTime(java.util.Date backTime) {
        this.backTime = backTime;
    }
    
    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    
    public String getHandoverEmp() {
        return handoverEmp;
    }
    
    public void setHandoverEmp(String handoverEmp) {
        this.handoverEmp = handoverEmp;
    }
    
    public Long getHandoverEmpId() {
        return handoverEmpId;
    }
    
    public void setHandoverEmpId(Long handoverEmpId) {
        this.handoverEmpId = handoverEmpId;
    }
    
    public String getHandoverOrg() {
        return handoverOrg;
    }
    
    public void setHandoverOrg(String handoverOrg) {
        this.handoverOrg = handoverOrg;
    }
    
    public Long getHandoverOrgID() {
        return handoverOrgID;
    }
    
    public void setHandoverOrgID(Long handoverOrgID) {
        this.handoverOrgID = handoverOrgID;
    }
    
    public String getIdentificationCode() {
        return identificationCode;
    }
    
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
    
    public String getPartsAccountIDX() {
        return partsAccountIDX;
    }
    
    public void setPartsAccountIDX(String partsAccountIDX) {
        this.partsAccountIDX = partsAccountIDX;
    }
    
    public String getPartsName() {
        return partsName;
    }
    
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }
    
    public String getPartsNo() {
        return partsNo;
    }
    
    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }
    
    public String getPartsTypeIDX() {
        return partsTypeIDX;
    }
    
    public void setPartsTypeIDX(String partsTypeIDX) {
        this.partsTypeIDX = partsTypeIDX;
    }
    
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }
    
    public String getWarehouseEmp() {
        return warehouseEmp;
    }
    
    public void setWarehouseEmp(String warehouseEmp) {
        this.warehouseEmp = warehouseEmp;
    }
    
    public Long getWarehouseEmpId() {
        return warehouseEmpId;
    }
    
    public void setWarehouseEmpId(Long warehouseEmpId) {
        this.warehouseEmpId = warehouseEmpId;
    }
    
    public String getWarehouseIdx() {
        return warehouseIdx;
    }
    
    public void setWarehouseIdx(String warehouseIdx) {
        this.warehouseIdx = warehouseIdx;
    }
    
    public String getWhName() {
        return whName;
    }
    
    public void setWhName(String whName) {
        this.whName = whName;
    }
    /**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param recordStatus 设置记录的状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * @return String 获取站点标识
	 */
	public String getSiteID(){
		return siteID;
	}
	/**
	 * @param siteID 设置站点标识
	 */
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	/**
	 * @return Long 获取创建人
	 */
	public Long getCreator(){
		return creator;
	}
	/**
	 * @param creator 设置创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * @return java.util.Date 获取创建时间
	 */
	public java.util.Date getCreateTime(){
		return createTime;
	}
	/**
	 * @param createTime 设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return Long 获取修改人
	 */
	public Long getUpdator(){
		return updator;
	}
	/**
	 * @param updator 设置修改人
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	/**
	 * @return java.util.Date 获取修改时间
	 */
	public java.util.Date getUpdateTime(){
		return updateTime;
	}
	/**
	 * @param updateTime 设置修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	/**
	 * @param idx 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}

    
    public String getNameplateNo() {
        return nameplateNo;
    }

    
    public void setNameplateNo(String nameplateNo) {
        this.nameplateNo = nameplateNo;
    }
}