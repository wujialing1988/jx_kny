package com.yunda.zb.pjdj.fixparts.entity;

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
 * <li>说明：ZbPartsFixRegister实体类, 数据表：上车配件登记单
 * <li>创建人：黄杨
 * <li>创建日期：2016-9-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_PARTS_FIX_REGISTER")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbPartsFixRegister implements java.io.Serializable{
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
	/* 检修任务单类型（机车、配件） */
	@Column(name="RDP_TYPE")
	private String rdpType;
	/* 上车人主键 */
	@Column(name="FIX_EMP_ID")
	private Long fixEmpId;
	/* 上车人 */
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
    /* 是否范围内下车（是，否） */
    @Column(name="IS_IN_RANGE")
    private String isInRange;
    /* 上车车型 */
    @Column(name="ABOARD_TRAINTYPE")
    private String aboardTrainType;
    /* 上车车型编码 */
    @Column(name="ABOARD_TRAINTYPE_IDX")
    private String aboardTrainTypeIdx;
    /* 上车车号 */
    @Column(name="ABOARD_TRAINNO")
    private String aboardTrainNo;
    /* 上车修程编码 */
    @Column(name="ABOARD_Repair_Class_IDX")
    private String aboardRepairClassIdx;
    /* 上车修程 */
    @Column(name="ABOARD_Repair_Class")
    private String aboardRepairClass;
    /* 上车修次编码*/
    @Column(name="ABOARD_Repair_time_IDX")
    private String aboardRepairTimeIdx;
    /* 上车修次*/
    @Column(name="ABOARD_Repair_time")
    private String aboardRepairTime;
    /* 上车位置编码*/
    @Column(name="ABOARD_Place_code")
    private String aboardPlaceCode;
    /* 上车位置*/
    @Column(name="ABOARD_Place")
    private String aboardPlace;
    /* 上车日期 */
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
    
    /* 作业工单主键 */
    @Column(name="work_card_idx")
    private String workCardIDX;
    
    /* 解备状态 */
    @Column(name="JB_STATUS")
    private String jbStatus ;

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
	 * @return String 获取检修任务单类型
	 */
	public String getRdpType(){
		return rdpType;
	}
	/**
	 * @param rdpType 设置检修任务单类型
	 */
	public void setRdpType(String rdpType) {
		this.rdpType = rdpType;
	}
	/**
	 * @return Long 获取上车人主键
	 */
	public Long getFixEmpId(){
		return fixEmpId;
	}
	/**
	 * @param fixEmpId 设置上车人主键
	 */
	public void setFixEmpId(Long fixEmpId) {
		this.fixEmpId = fixEmpId;
	}
	/**
	 * @return String 获取上车人
	 */
	public String getFixEmp(){
		return fixEmp;
	}
	/**
	 * @param fixEmp 设置上车人
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
    
    public String getAboardRepairClass() {
        return aboardRepairClass;
    }
    
    public void setAboardRepairClass(String aboardRepairClass) {
        this.aboardRepairClass = aboardRepairClass;
    }
    
    public String getAboardRepairClassIdx() {
        return aboardRepairClassIdx;
    }
    
    public void setAboardRepairClassIdx(String aboardRepairClassIdx) {
        this.aboardRepairClassIdx = aboardRepairClassIdx;
    }
    
    public String getAboardRepairTime() {
        return aboardRepairTime;
    }
    
    public void setAboardRepairTime(String aboardRepairTime) {
        this.aboardRepairTime = aboardRepairTime;
    }
    
    public String getAboardRepairTimeIdx() {
        return aboardRepairTimeIdx;
    }
    
    public void setAboardRepairTimeIdx(String aboardRepairTimeIdx) {
        this.aboardRepairTimeIdx = aboardRepairTimeIdx;
    }
    
    public String getAboardTrainNo() {
        return aboardTrainNo;
    }
    
    public void setAboardTrainNo(String aboardTrainNo) {
        this.aboardTrainNo = aboardTrainNo;
    }
    
    public String getAboardTrainType() {
        return aboardTrainType;
    }
    
    public void setAboardTrainType(String aboardTrainType) {
        this.aboardTrainType = aboardTrainType;
    }
    
    public String getAboardTrainTypeIdx() {
        return aboardTrainTypeIdx;
    }
    
    public void setAboardTrainTypeIdx(String aboardTrainTypeIdx) {
        this.aboardTrainTypeIdx = aboardTrainTypeIdx;
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
	public String getWorkCardIDX() {
		return workCardIDX;
	}
	public void setWorkCardIDX(String workCardIDX) {
		this.workCardIDX = workCardIDX;
	}
    
    public String getNameplateNo() {
        return nameplateNo;
    }
    
    public void setNameplateNo(String nameplateNo) {
        this.nameplateNo = nameplateNo;
    }
    
    public String getAboardPlaceCode() {
        return aboardPlaceCode;
    }
    
    public void setAboardPlaceCode(String aboardPlaceCode) {
        this.aboardPlaceCode = aboardPlaceCode;
    }
    
    public String getJbStatus() {
        return jbStatus;
    }
    
    public void setJbStatus(String jbStatus) {
        this.jbStatus = jbStatus;
    }
}