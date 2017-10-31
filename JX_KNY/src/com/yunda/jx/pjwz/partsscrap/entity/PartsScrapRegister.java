package com.yunda.jx.pjwz.partsscrap.entity;

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
 * <li>说明：PartsScrapRegister实体类, 数据表：配件报废登记
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
@Table(name="PJWZ_PARTS_SCRAP_REGISTER")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsScrapRegister implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
    @Column(name="SCRAP_EMP_ID")
    private Integer scrapEmpId;//报废人主键
    
    @Column(name="SCRAP_EMP")
    private String scrapEmp;//报废人
    
    @Column(name="SCRAP_ORG_ID")
    private String scrapOrgId;//报废部门主键
    
    @Column(name="SCRAP_ORG")
    private String scrapOrg;//报废部门
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="SCRAP_DATE")
    private java.util.Date scrapDate;//报废时间
    
    @Column(name="SCARP_REASON")
    private String scrapReason;//报废理由
    
    @Column(name="Parts_Account_IDX")
    private String partsAccountIdx;//配件信息主键
    
    @Column(name="PARTS_TYPE_IDX")
    private String partsTypeIDX;//配件规格型号主键
    
    @Column(name="Parts_Name")
    private String partsName;//配件名称
    
    @Column(name="Specification_Model")
    private String specificationModel;//规格型号
    
    @Column(name="Parts_No")
    private String partsNo;//配件编号
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
	 * @return String 获取配件名称
	 */
	public String getPartsName(){
		return partsName;
	}
	/**
	 * @param partsName 设置配件名称
	 */
	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}
	/**
	 * @return String 获取规格型号
	 */
	public String getSpecificationModel(){
		return specificationModel;
	}
	/**
	 * @param specificationModel 设置规格型号
	 */
	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel;
	}
	/**
	 * @return String 获取配件编号
	 */
	public String getPartsNo(){
		return partsNo;
	}
	/**
	 * @param partsNo 设置配件编号
	 */
	public void setPartsNo(String partsNo) {
		this.partsNo = partsNo;
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
    
    public String getPartsAccountIdx() {
        return partsAccountIdx;
    }
    
    public void setPartsAccountIdx(String partsAccountIdx) {
        this.partsAccountIdx = partsAccountIdx;
    }
    
    public String getPartsTypeIDX() {
        return partsTypeIDX;
    }
    
    public void setPartsTypeIDX(String partsTypeIDX) {
        this.partsTypeIDX = partsTypeIDX;
    }
    
    public java.util.Date getScrapDate() {
        return scrapDate;
    }
    
    public void setScrapDate(java.util.Date scrapDate) {
        this.scrapDate = scrapDate;
    }
    
    public String getScrapEmp() {
        return scrapEmp;
    }
    
    public void setScrapEmp(String scrapEmp) {
        this.scrapEmp = scrapEmp;
    }
    
    public Integer getScrapEmpId() {
        return scrapEmpId;
    }
    
    public void setScrapEmpId(Integer scrapEmpId) {
        this.scrapEmpId = scrapEmpId;
    }
    
    public String getScrapOrg() {
        return scrapOrg;
    }
    
    public void setScrapOrg(String scrapOrg) {
        this.scrapOrg = scrapOrg;
    }
    
    public String getScrapOrgId() {
        return scrapOrgId;
    }
    
    public void setScrapOrgId(String scrapOrgId) {
        this.scrapOrgId = scrapOrgId;
    }
    
    public String getScrapReason() {
        return scrapReason;
    }
    
    public void setScrapReason(String scrapReason) {
        this.scrapReason = scrapReason;
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