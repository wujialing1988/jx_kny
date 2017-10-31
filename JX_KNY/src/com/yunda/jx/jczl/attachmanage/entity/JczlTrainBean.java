package com.yunda.jx.jczl.attachmanage.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 机车信息
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
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JczlTrainBean implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/* idx主键 */
	@Id	
	private String idx;
	/* 车型主键 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	/* 车号 */
	@Column(name="Train_No")
	private String trainNo;
	/* 车型英文简称 */
	@Column(name="Train_Type_ShortName")
	private String trainTypeShortName;
	/* 资产状态(10使用中，20报废) */
	@Column(name="Asset_State")
	private Integer assetState;
	/* 机车状态(10检修，20运用，30备用) */
	@Column(name="Train_State")
	private Integer trainState;
	/* 来源于机车公用基础码表：机车用途编码（J_JCGY_TRAIN_USER） */
	@Column(name="Train_Use")
	private String trainUse;
	/* 配属单位ID */
	@Column(name="Hold_ORGID")
	private Long holdOrgId;
	/* 支配单位ID */
	@Column(name="Used_ORGID")
	private Long usedOrgId;
	/* 原配属单位ID */
	@Column(name="Old_Hold_ORGID")
	private Long oldHoldOrgId;
	/* 制造厂家主键，来源于表J_GYJC_FACTORY中的F_ID */
	@Column(name="Make_Factory_IDX")
	private String makeFactoryIDX;
	/* 制造厂家名，来源于表J_GYJC_FACTORY中的F_NAME */
	@Column(name="Make_Factory_Name")
	private String makeFactoryName;
	/* 出厂日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Leave_Date")
	private java.util.Date leaveDate;
	/* 组成型号主键 */
	@Column(name="BuildUp_Type_IDX")
	private String buildUpTypeIDX;
	/* 组成型号编码 */
	@Column(name="BuildUp_Type_Code")
	private String buildUpTypeCode;
	/* 组成型号名称 */
	@Column(name="BuildUp_Type_Name")
	private String buildUpTypeName;
	/* 备注 */
	private String remarks;
	/* 登记人 */
	@Column(name="Register_Person")
	private Long registerPerson;
	/* 登记人名称 */
	@Column(name="Register_Person_Name")
	private String registerPersonName;
	/* 登记时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Register_Time")
	private java.util.Date registerTime;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;
	/* 是否有履历，用于演示的临时字段；1：是；0：否 */
	@Column(name="Is_Have_Resume")
	private Integer isHaveResume;
    /** 配属局ID */
    @Column(name="B_ID")
    public String bId;
    /** 配属段ID */
    @Column(name="D_ID")
    public String dId;
    /** 支配段ID */
    @Column(name="USED_ID")
    private String useDId;
    
    /** 配属日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ATTACHMENT_TIME")
    private Date attachmentTime;
    
    /** 改配日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="RE_ATTACHMENT_TIME")
    private Date reAttachmentTime;
    
    /** 改配单位 */
    @Column(name="RE_ATTACHMENT_DEPORT_ID")
    private String reAttachmentDeportId;
    
    /** 命令号 */
    @Column(name="ORDER_NUMBER")
    private String orderNumber;
    
    /** 状态 */
    @Column(name="STATUS")
    private String status;
    
    /** 报废日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="SCRAP_TIME")
    private Date scrapTime;
    
    /** 报废原因 */
    @Column(name="SCRAP_REASON")
    private String scrapReason;
    
   // 肯尼亚新加字段

    /* 车型种类编码 */
    @Column(name = "T_VEHICLE_KIND_CODE")
    private String vehicleKindCode;
    
    /* 车型种类名称 篷车、罐车、煤车等*/
    @Column(name = "T_VEHICLE_KIND_NAME")
    private String vehicleKindName;
    
  	
	/**
	 * @return String 获取车型主键
	 */
	public String getTrainTypeIDX(){
		return trainTypeIDX;
	}
	/**
	 * @param 设置车型主键
	 */
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}
	/**
	 * @return String 获取车号
	 */
	public String getTrainNo(){
		return trainNo;
	}
	/**
	 * @param 设置车号
	 */
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	/**
	 * @return String 获取车型简称
	 */
	public String getTrainTypeShortName(){
		return trainTypeShortName;
	}
	/**
	 * @param 设置车型简称
	 */
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	/**
	 * @return Integer 获取资产状态
	 */
	public Integer getAssetState(){
		return assetState;
	}
	/**
	 * @param 设置资产状态
	 */
	public void setAssetState(Integer assetState) {
		this.assetState = assetState;
	}
	/**
	 * @return Integer 获取机车状态
	 */
	public Integer getTrainState(){
		return trainState;
	}
	/**
	 * @param 设置机车状态
	 */
	public void setTrainState(Integer trainState) {
		this.trainState = trainState;
	}
	/**
	 * @return String 获取使用别
	 */
	public String getTrainUse(){
		return trainUse;
	}
	/**
	 * @param 设置使用别
	 */
	public void setTrainUse(String trainUse) {
		this.trainUse = trainUse;
	}
	/**
	 * @return Long 获取配属单位ID
	 */
	public Long getHoldOrgId(){
		return holdOrgId;
	}
	/**
	 * @param 设置配属单位ID
	 */
	public void setHoldOrgId(Long holdOrgId) {
		this.holdOrgId = holdOrgId;
	}
	/**
	 * @return Long 获取支配单位ID
	 */
	public Long getUsedOrgId(){
		return usedOrgId;
	}
	/**
	 * @param 设置支配单位ID
	 */
	public void setUsedOrgId(Long usedOrgId) {
		this.usedOrgId = usedOrgId;
	}
	/**
	 * @return Long 获取原配属单位ID
	 */
	public Long getOldHoldOrgId(){
		return oldHoldOrgId;
	}
	/**
	 * @param 设置原配属单位ID
	 */
	public void setOldHoldOrgId(Long oldHoldOrgId) {
		this.oldHoldOrgId = oldHoldOrgId;
	}
	/**
	 * @return String 获取制造厂家主键
	 */
	public String getMakeFactoryIDX(){
		return makeFactoryIDX;
	}
	/**
	 * @param 设置制造厂家主键
	 */
	public void setMakeFactoryIDX(String makeFactoryIDX) {
		this.makeFactoryIDX = makeFactoryIDX;
	}
	/**
	 * @return String 获取制造厂家名
	 */
	public String getMakeFactoryName(){
		return makeFactoryName;
	}
	/**
	 * @param 设置制造厂家名
	 */
	public void setMakeFactoryName(String makeFactoryName) {
		this.makeFactoryName = makeFactoryName;
	}
	/**
	 * @return java.util.Date 获取出厂日期
	 */
	public java.util.Date getLeaveDate(){
		return leaveDate;
	}
	/**
	 * @param 设置出厂日期
	 */
	public void setLeaveDate(java.util.Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	/**
	 * @return String 获取组成型号编码
	 */
	public String getBuildUpTypeCode(){
		return buildUpTypeCode;
	}
	/**
	 * @param 设置组成型号编码
	 */
	public void setBuildUpTypeCode(String buildUpTypeCode) {
		this.buildUpTypeCode = buildUpTypeCode;
	}
	/**
	 * @return String 获取组成型号名称
	 */
	public String getBuildUpTypeName(){
		return buildUpTypeName;
	}
	/**
	 * @param 设置组成型号名称
	 */
	public void setBuildUpTypeName(String buildUpTypeName) {
		this.buildUpTypeName = buildUpTypeName;
	}
	/**
	 * @return String 获取备注
	 */
	public String getRemarks(){
		return remarks;
	}
	/**
	 * @param 设置备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return Long 获取登记人
	 */
	public Long getRegisterPerson(){
		return registerPerson;
	}
	/**
	 * @param 设置登记人
	 */
	public void setRegisterPerson(Long registerPerson) {
		this.registerPerson = registerPerson;
	}
	/**
	 * @return String 获取登记人名称
	 */
	public String getRegisterPersonName(){
		return registerPersonName;
	}
	/**
	 * @param 设置登记人名称
	 */
	public void setRegisterPersonName(String registerPersonName) {
		this.registerPersonName = registerPersonName;
	}
	/**
	 * @return java.util.Date 获取登记时间
	 */
	public java.util.Date getRegisterTime(){
		return registerTime;
	}
	/**
	 * @param 设置登记时间
	 */
	public void setRegisterTime(java.util.Date registerTime) {
		this.registerTime = registerTime;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param 设置记录状态
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
	 * @param 设置站点标识
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
	 * @param 设置创建人
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
	 * @param 设置创建时间
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
	 * @param 设置修改人
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
	 * @param 设置修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return Integer 获取是否有履历
	 */
	public Integer getIsHaveResume(){
		return isHaveResume;
	}
	/**
	 * @param 设置是否有履历
	 */
	public void setIsHaveResume(Integer isHaveResume) {
		this.isHaveResume = isHaveResume;
	}
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	/**
	 * @param 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
	
	
	
	
    public String getBId() {
        return bId;
    }

    
    public void setBId(String id) {
        bId = id;
    }

    
    public String getDId() {
        return dId;
    }

    
    public void setDId(String id) {
        dId = id;
    }

    public String getBuildUpTypeIDX() {
		return buildUpTypeIDX;
	}
	public void setBuildUpTypeIDX(String buildUpTypeIDX) {
		this.buildUpTypeIDX = buildUpTypeIDX;
	}

  
	/**
	 * <li>说明：支配段
	 * <li>返回值： the useDId
	 */
	public String getUseDId() {
		return useDId;
	}

	/**
	 * <li>说明：支配段
	 * <li>参数： useDId
	 */
	public void setUseDId(String useDId) {
		this.useDId = useDId;
	}

    
    /**
     * <li>说明：配属日期
     * <li>返回值： the attachmentTime
     */
    public Date getAttachmentTime() {
        return attachmentTime;
    }

    
    /**
     * <li>说明：配属日期
     * <li>参数： attachmentTime
     */
    public void setAttachmentTime(Date attachmentTime) {
        this.attachmentTime = attachmentTime;
    }

    /**
     * <li>说明：命令号
     * <li>返回值： the orderNumber
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    
    /**
     * <li>说明：命令号
     * <li>参数： orderNumber
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    
    /**
     * <li>说明：改配单位
     * <li>返回值： the reAttachmentDeportId
     */
    public String getReAttachmentDeportId() {
        return reAttachmentDeportId;
    }

    
    /**
     * <li>说明：改配单位
     * <li>参数： reAttachmentDeportId
     */
    public void setReAttachmentDeportId(String reAttachmentDeportId) {
        this.reAttachmentDeportId = reAttachmentDeportId;
    }

    /**
     * <li>说明：改配日期
     * <li>返回值： the reAttachmentTime
     */
    public Date getReAttachmentTime() {
        return reAttachmentTime;
    }

    
    /**
     * <li>说明：改配日期
     * <li>参数： reAttachmentTime
     */
    public void setReAttachmentTime(Date reAttachmentTime) {
        this.reAttachmentTime = reAttachmentTime;
    }

    /**
     * <li>说明：报废原因
     * <li>返回值： the scrapReason
     */
    public String getScrapReason() {
        return scrapReason;
    }

    
    /**
     * <li>说明：报废原因
     * <li>参数： scrapReason
     */
    public void setScrapReason(String scrapReason) {
        this.scrapReason = scrapReason;
    }

    /**
     * <li>说明：报废日期
     * <li>返回值： the scrapTime
     */
    public Date getScrapTime() {
        return scrapTime;
    }

    
    /**
     * <li>说明：报废日期
     * <li>参数： scrapTime
     */
    public void setScrapTime(Date scrapTime) {
        this.scrapTime = scrapTime;
    }

    /**
     * <li>说明：状态
     * <li>返回值： the status
     */
    public String getStatus() {
        return status;
    }

    
    /**
     * <li>说明：状态
     * <li>参数： status
     */
    public void setStatus(String status) {
        this.status = status;
    }

  	public String getVehicleKindName() {
		return vehicleKindName;
	}

	public void setVehicleKindName(String vehicleKindName) {
		this.vehicleKindName = vehicleKindName;
	}

	public String getVehicleKindCode() {
		return vehicleKindCode;
	}

	public void setVehicleKindCode(String vehicleKindCode) {
		this.vehicleKindCode = vehicleKindCode;
	}
   	
	 
}