package com.yunda.jx.pjwz.wellpartsmanage.wellpartsregister.entity;

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
 * <li>说明：wellPartsRegister实体类, 数据表：良好配件登记
 * <li>创建人：程梅
 * <li>创建日期：2015-10-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJWZ_WELL_PARTS_REGISTER")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WellPartsRegister implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    public static final int ACCEPT_DEPT_TYPE_ORG =  1 ;//责任部门类型：机构 
    public static final int ACCEPT_DEPT_TYPE_WH =  2 ;//责任部门类型：库房
    /** 单据类型 - 新购 */
    public static final String SOURCE_NEW = "新购";
    /** 单据类型 - 调入 */
    public static final String SOURCE_INTO= "调入";
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 责任部门ID */
	@Column(name="ACCEPT_DEPT_ID")
	private String acceptDeptId;
	/* 责任部门序列(存储组织机构的orgseq字段) */
	@Column(name="ACCEPT_DEPT_ORGSEQ")
	private String acceptDeptOrgSeq;
	/* 责任部门 */
	@Column(name="ACCEPT_DEPT")
	private String acceptDept;
	/* 责任部门类型：1：机构；2：库房 */
	@Column(name="ACCEPT_DEPT_Type")
	private Integer acceptDeptType;
	/* 接收日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ACCEPT_TIME")
	private java.util.Date acceptTime;
	/* 接收人主键 */
	@Column(name="TAKE_OVER_EMP_ID")
	private Long takeOverEmpId;
	/* 接收人 */
	@Column(name="TAKE_OVER_EMP")
	private String takeOverEmp;
	/* 配件信息主键 */
	@Column(name="PARTS_ACCOUNT_IDX")
	private String partsAccountIdx;
	/* 配件规格型号主键 */
	@Column(name="PARTS_TYPE_IDX")
	private String partsTypeIdx;
	/* 配件名称 */
	@Column(name="PARTS_NAME")
	private String partsName;
	/* 规格型号 */
	@Column(name="SPECIFICATION_MODEL")
	private String specificationModel;
	/* 配件编号 */
	@Column(name="PARTS_NO")
	private String partsNo;
    /* 配件铭牌号 */
    @Column(name="nameplate_No")
    private String nameplateNo;
	/* 配件识别码 */
	@Column(name="IDENTIFICATION_CODE")
	private String identificationCode;
    /* 生产厂主键 */
    @Column(name="MADE_FACTORY_IDX")
    private String madeFactoryIdx;
    /* 生产厂名 */
    @Column(name="MADE_FACTORY_NAME")
    private String madeFactoryName;
    /* 出厂日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FACTORY_DATE")
    private java.util.Date factoryDate;
	/* 存放位置 */
	@Column(name="LOCATION_NAME")
	private String locationName;
    /*详细配置 */
    @Column(name="CONFIG_DETAIL")
    private String configDetail;
	/* 单据类型：（新、调入） */
	private String source;
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
    /* 配件状态编码 */
    @Column(name="Parts_Status")
    private String partsStatus;
    /* 配件状态名称 */
    @Column(name="Parts_Status_Name")
    private String partsStatusName;
	
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
    /**
	 * @return String 获取接收部门主键
	 */
	public String getAcceptDeptId(){
		return acceptDeptId;
	}
	/**
	 * @param acceptDeptId 设置接收部门主键
	 */
	public void setAcceptDeptId(String acceptDeptId) {
		this.acceptDeptId = acceptDeptId;
	}
	/**
	 * @return String 获取接收部门序列
	 */
	public String getAcceptDeptOrgSeq(){
		return acceptDeptOrgSeq;
	}
	/**
	 * @param acceptDeptOrgSeq 设置接收部门序列
	 */
	public void setAcceptDeptOrgSeq(String acceptDeptOrgSeq) {
		this.acceptDeptOrgSeq = acceptDeptOrgSeq;
	}
	/**
	 * @return String 获取接收部门
	 */
	public String getAcceptDept(){
		return acceptDept;
	}
	/**
	 * @param acceptDept 设置接收部门
	 */
	public void setAcceptDept(String acceptDept) {
		this.acceptDept = acceptDept;
	}
	/**
	 * @return Integer 获取接收部门类型
	 */
	public Integer getAcceptDeptType(){
		return acceptDeptType;
	}
	/**
	 * @param acceptDeptType 设置接收部门类型
	 */
	public void setAcceptDeptType(Integer acceptDeptType) {
		this.acceptDeptType = acceptDeptType;
	}
	/**
	 * @return java.util.Date 获取接收日期
	 */
	public java.util.Date getAcceptTime(){
		return acceptTime;
	}
	/**
	 * @param acceptTime 设置接收日期
	 */
	public void setAcceptTime(java.util.Date acceptTime) {
		this.acceptTime = acceptTime;
	}
	/**
	 * @return Long 获取接收人主键
	 */
	public Long getTakeOverEmpId(){
		return takeOverEmpId;
	}
	/**
	 * @param takeOverEmpId 设置接收人主键
	 */
	public void setTakeOverEmpId(Long takeOverEmpId) {
		this.takeOverEmpId = takeOverEmpId;
	}
	/**
	 * @return String 获取接收人
	 */
	public String getTakeOverEmp(){
		return takeOverEmp;
	}
	/**
	 * @param takeOverEmp 设置接收人
	 */
	public void setTakeOverEmp(String takeOverEmp) {
		this.takeOverEmp = takeOverEmp;
	}
	/**
	 * @return String 获取配件信息主键
	 */
	public String getPartsAccountIdx(){
		return partsAccountIdx;
	}
	/**
	 * @param partsAccountIdx 设置配件信息主键
	 */
	public void setPartsAccountIdx(String partsAccountIdx) {
		this.partsAccountIdx = partsAccountIdx;
	}
	/**
	 * @return String 获取配件规格型号主键
	 */
	public String getPartsTypeIdx(){
		return partsTypeIdx;
	}
	/**
	 * @param partsTypeIdx 设置配件规格型号主键
	 */
	public void setPartsTypeIdx(String partsTypeIdx) {
		this.partsTypeIdx = partsTypeIdx;
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
	/**
	 * @return String 获取配件识别码
	 */
	public String getIdentificationCode(){
		return identificationCode;
	}
	/**
	 * @param identificationCode 设置配件识别码
	 */
	public void setIdentificationCode(String identificationCode) {
		this.identificationCode = identificationCode;
	}
	/**
	 * @return String 获取配件来源
	 */
	public String getSource(){
		return source;
	}
	/**
	 * @param source 设置配件来源
	 */
	public void setSource(String source) {
		this.source = source;
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
	
    public String getConfigDetail() {
        return configDetail;
    }
    
    public void setConfigDetail(String configDetail) {
        this.configDetail = configDetail;
    }
    
    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    
    public java.util.Date getFactoryDate() {
        return factoryDate;
    }
    
    public void setFactoryDate(java.util.Date factoryDate) {
        this.factoryDate = factoryDate;
    }
    
    public String getLocationName() {
        return locationName;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    public String getMadeFactoryIdx() {
        return madeFactoryIdx;
    }
    
    public void setMadeFactoryIdx(String madeFactoryIdx) {
        this.madeFactoryIdx = madeFactoryIdx;
    }
    
    public String getMadeFactoryName() {
        return madeFactoryName;
    }
    
    public void setMadeFactoryName(String madeFactoryName) {
        this.madeFactoryName = madeFactoryName;
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