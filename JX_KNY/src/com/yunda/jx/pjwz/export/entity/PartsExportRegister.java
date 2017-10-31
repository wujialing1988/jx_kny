package com.yunda.jx.pjwz.export.entity;

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
 * <li>说明：PartsExportRegister实体类, 数据表：配件调出登记
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
@Table(name="PJWZ_PARTS_EXPORT_REGISTER")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsExportRegister implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
    /* 配件信息主键 */
    @Column(name="PARTS_ACCOUNT_IDX")
    private String partsAccountIDX;
    /* 配件规格型号主键 */
    @Column(name="PARTS_TYPE_IDX")
    private String partsTypeIDX;
    /* 规格型号 */
    @Column(name="SPECIFICATION_MODEL")
    private String specificationModel;
    /* 配件名称 */
    @Column(name="PARTS_NAME")
    private String partsName;
    /* 配件编号 */
    @Column(name="PARTS_NO")
    private String partsNo;
    /* 配件铭牌号 */
    @Column(name="nameplate_No")
    private String nameplateNo;
    /* 配件识别码 */
    @Column(name="IDENTIFICATION_CODE")
    private String identificationCode;
    /* 下车车型 */
    @Column(name="UNLOAD_TRAINTYPE")
    private String unloadTrainType;
    /* 下车车型编码 */
    @Column(name="UNLOAD_TRAINTYPE_IDX")
    private String unloadTrainTypeIdx;
    /* 下车车号 */
    @Column(name="UNLOAD_TRAINNO")
    private String unloadTrainNo;
    /* 下车修程编码 */
    @Column(name="UNLOAD_Repair_Class_IDX")
    private String unloadRepairClassIdx;
    /* 下车修程 */
    @Column(name="UNLOAD_Repair_Class")
    private String unloadRepairClass;
    /* 下车修次编码*/
    @Column(name="UNLOAD_Repair_time_IDX")
    private String unloadRepairTimeIdx;
    /* 下车修次*/
    @Column(name="UNLOAD_Repair_time")
    private String unloadRepairTime;
	/* 调出日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EXPORT_DATE")
	private java.util.Date exportDate;
	/* 调拨命令 */
	@Column(name="EXPORT_ORDER")
	private String exportOrder;
	/* 接收单位编码 */
	@Column(name="ACCEPT_DEP_CODE")
	private String acceptDepCode;
	/* 接收单位 */
	@Column(name="ACCEPT_DEP")
	private String acceptDep;
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
    /* 是否新品 */
    @Column(name="IS_NEW_PARTS")
    private String isNewParts;

	
    public String getIsNewParts() {
        return isNewParts;
    }
    
    public void setIsNewParts(String isNewParts) {
        this.isNewParts = isNewParts;
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
	
    public String getAcceptDep() {
        return acceptDep;
    }
    
    public void setAcceptDep(String acceptDep) {
        this.acceptDep = acceptDep;
    }
    
    public String getAcceptDepCode() {
        return acceptDepCode;
    }
    
    public void setAcceptDepCode(String acceptDepCode) {
        this.acceptDepCode = acceptDepCode;
    }
    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    
    public java.util.Date getExportDate() {
        return exportDate;
    }
    
    public void setExportDate(java.util.Date exportDate) {
        this.exportDate = exportDate;
    }
    
    public String getExportOrder() {
        return exportOrder;
    }
    
    public void setExportOrder(String exportOrder) {
        this.exportOrder = exportOrder;
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
    
    public String getUnloadRepairClass() {
        return unloadRepairClass;
    }
    
    public void setUnloadRepairClass(String unloadRepairClass) {
        this.unloadRepairClass = unloadRepairClass;
    }
    
    public String getUnloadRepairClassIdx() {
        return unloadRepairClassIdx;
    }
    
    public void setUnloadRepairClassIdx(String unloadRepairClassIdx) {
        this.unloadRepairClassIdx = unloadRepairClassIdx;
    }
    
    public String getUnloadRepairTime() {
        return unloadRepairTime;
    }
    
    public void setUnloadRepairTime(String unloadRepairTime) {
        this.unloadRepairTime = unloadRepairTime;
    }
    
    public String getUnloadRepairTimeIdx() {
        return unloadRepairTimeIdx;
    }
    
    public void setUnloadRepairTimeIdx(String unloadRepairTimeIdx) {
        this.unloadRepairTimeIdx = unloadRepairTimeIdx;
    }
    
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