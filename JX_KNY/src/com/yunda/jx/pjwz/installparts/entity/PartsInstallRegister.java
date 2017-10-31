package com.yunda.jx.pjwz.installparts.entity;

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
 * <li>说明：PartsInstallRegister实体类, 数据表：配件安装登记单
 * <li>创建人：程梅
 * <li>创建日期：2016-01-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJWZ_Parts_Install_Register")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsInstallRegister implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /** 是否范围内上车- 是 */
    public static final String IS_IN_RANGE_YES = "是";
    /** 是否范围内上车 - 否 */
    public static final String IS_IN_RANGE_NO = "否";
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 检修任务单主键 */
	@Column(name="RDP_IDX")
	private String rdpIdx;
    /* 上级配件信息主键 */
    @Column(name="PARENT_PARTS_ACCOUNT_IDX")
    private String parentPartsAccountIDX;
    /* 配件检修记录单主键 */
    @Column(name="RECORD_CARD_IDX")
    private String recordCardIdx;
	/* 安装人主键 */
	@Column(name="FIX_EMP_ID")
	private Long fixEmpId;
	/* 安装人 */
	@Column(name="FIX_EMP")
	private String fixEmp;
    /** 配件信息主键 */
    @Column(name = "PARTS_ACCOUNT_IDX")
    private String partsAccountIDX;

    /** 规格型号主键 */
    @Column(name = "PARTS_TYPE_IDX")
    private String partsTypeIDX;

    /** 配件名称 */
    @Column(name = "Parts_Name")
    private String partsName;

    /** 规格型号 */
    @Column(name = "Specification_Model")
    private String specificationModel;

    /** 配件编号 */
    @Column(name = "Parts_NO")
    private String partsNo;
    /* 配件铭牌号 */
    @Column(name="nameplate_No")
    private String nameplateNo;
    /* 配件识别码 */
    @Column(name="IDENTIFICATION_CODE")
    private String identificationCode;
    /* 是否范围内安装（是，否） */
    @Column(name="IS_IN_RANGE")
    private String isInRange;
    /* 位置*/
    @Column(name="ABOARD_Place")
    private String aboardPlace;
    /* 安装日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ABOARD_DATE")
    private java.util.Date aboardDate;
	/* 单据状态:(未登帐、已登帐) */
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

	/**
	 * @return String 获取检修任务单主键
	 */
	public String getRdpIdx(){
		return rdpIdx;
	}
	/**
	 * @param rdpIdx 设置检修任务单主键
	 */
	public void setRdpIdx(String rdpIdx) {
		this.rdpIdx = rdpIdx;
	}
	/**
	 * @return Long 获取安装人主键
	 */
	public Long getFixEmpId(){
		return fixEmpId;
	}
	/**
	 * @param fixEmpId 设置安装人主键
	 */
	public void setFixEmpId(Long fixEmpId) {
		this.fixEmpId = fixEmpId;
	}
	/**
	 * @return String 获取安装人
	 */
	public String getFixEmp(){
		return fixEmp;
	}
	/**
	 * @param fixEmp 设置安装人
	 */
	public void setFixEmp(String fixEmp) {
		this.fixEmp = fixEmp;
	}
	
    public java.util.Date getAboardDate() {
        return aboardDate;
    }
    
    public void setAboardDate(java.util.Date aboardDate) {
        this.aboardDate = aboardDate;
    }
    
    public String getAboardPlace() {
        return aboardPlace;
    }
    
    public void setAboardPlace(String aboardPlace) {
        this.aboardPlace = aboardPlace;
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
    
    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
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
    
    public String getIsInRange() {
        return isInRange;
    }
    
    public void setIsInRange(String isInRange) {
        this.isInRange = isInRange;
    }
    
    public String getParentPartsAccountIDX() {
        return parentPartsAccountIDX;
    }
    
    public void setParentPartsAccountIDX(String parentPartsAccountIDX) {
        this.parentPartsAccountIDX = parentPartsAccountIDX;
    }
    
    public String getRecordCardIdx() {
        return recordCardIdx;
    }
    
    public void setRecordCardIdx(String recordCardIdx) {
        this.recordCardIdx = recordCardIdx;
    }
    
    public String getNameplateNo() {
        return nameplateNo;
    }
    
    public void setNameplateNo(String nameplateNo) {
        this.nameplateNo = nameplateNo;
    }
}