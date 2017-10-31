package com.yunda.jx.pjwz.partscancel.entity;

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
 * <li>说明：PartsCancelRegister实体类, 数据表：配件销账单
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
@Table(name="PJWZ_PARTS_CANCEL_REGISTER")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsCancelRegister implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
    @Column(name="CANCELED_EMP_ID")
    private Integer canceledEmpId;//销账人主键
    
    @Column(name="CANCELED_EMP")
    private String canceledEmp;//销账人
    
    @Column(name="CANCELED_ORG_ID")
    private Long canceledOrgId;//销账部门主键
    
    @Column(name="CANCELED_ORG")
    private String canceledOrg;//销账部门
    
    @Column(name="CANCELED_DATE")
    private java.util.Date canceledDate;//销账日期
    
    @Column(name="CANCELED_TYPE")
    private String canceledType;//销账方式
    
    @Column(name="CANCELED_REASON")
    private String canceledReason;//销账理由
    @Column(name="Parts_Account_IDX")
    private String partsAccountIdx;//配件信息主键
    
    @Column(name="PARTS_TYPE_IDX")
    private String partsTypeIdx;//配件规格型号主键
    
    @Column(name="Parts_Name")
    private String partsName;//配件名称
    
    @Column(name="Specification_Model")
    private String specificationModel;//规格型号
    
    @Column(name="Parts_No")
    private String partsNo;//配件编号
    /* 配件铭牌号 */
    @Column(name="nameplate_No")
    private String nameplateNo;
	/* 计量单位 */
	private String unit;
    /* 配件识别码 */
    @Column(name="IDENTIFICATION_CODE")
    private String identificationCode;
	/* 是否新品（新、旧） */
	@Column(name="IS_NEW_PARTS")
	private String isNewParts;
	/* 配件状态编码 */
	@Column(name="PARTS_STATUS")
	private String partsStatus;
	/* 配件状态名称 */
	@Column(name="Parts_Status_NAME")
	private String partsStatusName;
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
    /* 下车车型 */
    @Column(name="UNLOAD_TRAINTYPE")
    private String unloadTrainType;
    /* 下车车型编码 */
    @Column(name="UNLOAD_TRAINTYPE_IDX")
    private String unloadTrainTypeIdx;
    /* 下车车号 */
    @Column(name="UNLOAD_TRAINNO")
    private String unloadTrainNo;
	
    public String getUnloadTrainNo() {
        return unloadTrainNo;
    }
    
    public void setUnloadTrainNo(String unloadTrainNo) {
        this.unloadTrainNo = unloadTrainNo;
    }
    
    public String getUnloadTrainType() {
        return unloadTrainType;
    }
    
    public void setUnloadTrainType(String unloadTrainType) {
        this.unloadTrainType = unloadTrainType;
    }
    
    public String getUnloadTrainTypeIdx() {
        return unloadTrainTypeIdx;
    }
    
    public void setUnloadTrainTypeIdx(String unloadTrainTypeIdx) {
        this.unloadTrainTypeIdx = unloadTrainTypeIdx;
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
	
    public java.util.Date getCanceledDate() {
        return canceledDate;
    }
    
    public void setCanceledDate(java.util.Date canceledDate) {
        this.canceledDate = canceledDate;
    }
    
    public String getCanceledEmp() {
        return canceledEmp;
    }
    
    public void setCanceledEmp(String canceledEmp) {
        this.canceledEmp = canceledEmp;
    }
    
    public Integer getCanceledEmpId() {
        return canceledEmpId;
    }
    
    public void setCanceledEmpId(Integer canceledEmpId) {
        this.canceledEmpId = canceledEmpId;
    }
    
    public String getCanceledOrg() {
        return canceledOrg;
    }
    
    public void setCanceledOrg(String canceledOrg) {
        this.canceledOrg = canceledOrg;
    }
    
    public Long getCanceledOrgId() {
        return canceledOrgId;
    }
    
    public void setCanceledOrgId(Long canceledOrgId) {
        this.canceledOrgId = canceledOrgId;
    }
    
    public String getCanceledReason() {
        return canceledReason;
    }
    
    public void setCanceledReason(String canceledReason) {
        this.canceledReason = canceledReason;
    }
    
    public String getCanceledType() {
        return canceledType;
    }
    
    public void setCanceledType(String canceledType) {
        this.canceledType = canceledType;
    }
    
    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    
    public String getIdentificationCode() {
        return identificationCode;
    }
    
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
    
    public String getIsNewParts() {
        return isNewParts;
    }
    
    public void setIsNewParts(String isNewParts) {
        this.isNewParts = isNewParts;
    }
    
    public String getPartsAccountIdx() {
        return partsAccountIdx;
    }
    
    public void setPartsAccountIdx(String partsAccountIdx) {
        this.partsAccountIdx = partsAccountIdx;
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
    
    public String getPartsStatus() {
        return partsStatus;
    }
    
    public void setPartsStatus(String partsStatus) {
        this.partsStatus = partsStatus;
    }
    
    public String getPartsStatusName() {
        return partsStatusName;
    }
    
    public void setPartsStatusName(String partsStatusName) {
        this.partsStatusName = partsStatusName;
    }
    
    public String getPartsTypeIdx() {
        return partsTypeIdx;
    }
    
    public void setPartsTypeIdx(String partsTypeIdx) {
        this.partsTypeIdx = partsTypeIdx;
    }
    
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
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