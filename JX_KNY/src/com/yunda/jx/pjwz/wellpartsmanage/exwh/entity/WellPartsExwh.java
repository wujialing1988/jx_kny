package com.yunda.jx.pjwz.wellpartsmanage.exwh.entity;

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
 * <li>说明：WellPartsExwh实体类, 数据表：良好配件出库单
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
@Table(name="PJWZ_WELL_Parts_EXWH")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WellPartsExwh implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /* 出库去向--上车*/
    public static final String TO_GO_SC = "上车" ;
    /* 出库去向--校验*/
    public static final String TO_GO_JY = "校验" ;
    /* 出库去向--检修*/
    public static final String TO_GO_JX = "检修" ;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 接收库房主键 */
	@Column(name="WH_IDX")
	private String whIdx;
	/* 接收库房 */
	@Column(name="WH_NAME")
	private String whName;
	/* 库位名称 */
	@Column(name="LOCATION_NAME")
	private String locationName;
	/* 交件人主键 */
	@Column(name="HAND_OVER_EMP_ID")
	private Long handOverEmpId;
	/* 交件人 */
	@Column(name="HAND_OVER_EMP")
	private String handOverEmp;
	/* 接收部门主键 */
	@Column(name="ACCEPT_ORG_ID")
	private Long acceptOrgID;
	/* 接收部门 */
	@Column(name="ACCEPT_ORG")
	private String acceptOrg;
	/* 领件日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="WH_TIME")
	private java.util.Date whTime;
	/* 接收人主键 */
	@Column(name="ACCEPT_EMP_ID")
	private Long acceptEmpId;
	/* 接收人 */
	@Column(name="ACCEPT_EMP")
	private String acceptEmp;
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
	/* 是否配送（是、否） */
	@Column(name="IS_DELIVER")
	private String isDeliver;
	/* 配送地址 */
	@Column(name="DELIVER_LOCATION")
	private String deliverLocation;
	/* 配送时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELIVER_TIME")
	private java.util.Date deliverTime;
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

    /* 出库去向 */
    @Column(name="to_go")
    private String toGo;
    
    public String getAcceptEmp() {
        return acceptEmp;
    }
    
    public void setAcceptEmp(String acceptEmp) {
        this.acceptEmp = acceptEmp;
    }
    
    public Long getAcceptEmpId() {
        return acceptEmpId;
    }
    
    public void setAcceptEmpId(Long acceptEmpId) {
        this.acceptEmpId = acceptEmpId;
    }
    
    public String getAcceptOrg() {
        return acceptOrg;
    }
    
    public void setAcceptOrg(String acceptOrg) {
        this.acceptOrg = acceptOrg;
    }
    
    public Long getAcceptOrgID() {
        return acceptOrgID;
    }
    
    public void setAcceptOrgID(Long acceptOrgID) {
        this.acceptOrgID = acceptOrgID;
    }
    
    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    
    public String getDeliverLocation() {
        return deliverLocation;
    }
    
    public void setDeliverLocation(String deliverLocation) {
        this.deliverLocation = deliverLocation;
    }
    
    public java.util.Date getDeliverTime() {
        return deliverTime;
    }
    
    public void setDeliverTime(java.util.Date deliverTime) {
        this.deliverTime = deliverTime;
    }
    
    public String getHandOverEmp() {
        return handOverEmp;
    }
    
    public void setHandOverEmp(String handOverEmp) {
        this.handOverEmp = handOverEmp;
    }
    
    public Long getHandOverEmpId() {
        return handOverEmpId;
    }
    
    public void setHandOverEmpId(Long handOverEmpId) {
        this.handOverEmpId = handOverEmpId;
    }
    
    public String getIdentificationCode() {
        return identificationCode;
    }
    
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
    
    public String getIsDeliver() {
        return isDeliver;
    }
    
    public void setIsDeliver(String isDeliver) {
        this.isDeliver = isDeliver;
    }
    
    public String getLocationName() {
        return locationName;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
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
    
    public String getWhIdx() {
        return whIdx;
    }
    
    public void setWhIdx(String whIdx) {
        this.whIdx = whIdx;
    }
    
    public String getWhName() {
        return whName;
    }
    
    public void setWhName(String whName) {
        this.whName = whName;
    }
    
    public java.util.Date getWhTime() {
        return whTime;
    }
    
    public void setWhTime(java.util.Date whTime) {
        this.whTime = whTime;
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

    
    public String getToGo() {
        return toGo;
    }

    
    public void setToGo(String toGo) {
        this.toGo = toGo;
    }
}