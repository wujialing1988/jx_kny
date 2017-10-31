package com.yunda.jx.jczl.attachmanage.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：JczlTrain实体类, 数据表：机车信息
 * <li>创建人：王治龙
 * <li>创建日期：2012-10-23
 * <li>修改人: 张迪
 * <li>修改日期：2017-04-23
 * <li>修改内容：客车运用
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JCZL_TRAIN")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class JczlTrain implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/** 车辆状态 - 检修 */
	public static final int TRAIN_STATE_REPAIR = 10; 
	/** 车辆状态 - 运用 */
	public static final int TRAIN_STATE_USE = 20; 
	/** 车辆状态 - 列检 */
	public static final int TRAIN_STATE_LIEJIAN = 30; 
    /** 车辆状态 - 扣车 */
    public static final int TRAIN_STATE_DETAIN = 40; 
	/** 资产状态 - 运用 */
	public static final int TRAIN_ASSET_STATE_USE = 10;
	/** 资产状态 - 报废 */
	public static final int TRAIN_ASSET_STATE_SCRAP = 20; 
    /** 资产状态 - 备用 */
    public static final int TRAIN_ASSET_STATE_SPARE = 30; 
    
    /**
     * <li>说明：获取状态名称
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param key
     * @return
     */
    public static String getTrainStatusName(Integer key){
        String result = "" ;
        switch (key) {
            case JczlTrain.TRAIN_STATE_REPAIR:
                result = "检修";
                break;
            case JczlTrain.TRAIN_STATE_USE:
                result = "运用";
                break;
            case JczlTrain.TRAIN_STATE_LIEJIAN:
                result = "列检";
                break;
            case JczlTrain.TRAIN_STATE_DETAIN:
                result = "扣车";
                break;                
            default:
                break;
        }
        return result ;
    }
	
	
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 车型主键 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	/* 车号 */
	@Column(name="Train_No")
	private String trainNo;
	/* 车型英文简称 */
	@Column(name="TRAIN_TYPE_SHORTNAME")
	private String trainTypeShortName;
	/* 资产状态(10使用中，20报废，30备用) */
	@Column(name="Asset_State")
	private Integer assetState;
	/* 机车状态(10检修，20运用，30列检，40扣车) */
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
    
    /** 客货类型 */
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;
    
    
	/* 用于外键显示   ******* 扩展字段 */
	
	/* 配属单位名称 */
    @Transient
	private String holdOrgName;
    
    /* 配属单位机构序列 */
    @Transient
    private String holdOrgSeq;
		
	/* 支配单位名称 */
    @Transient
	private String usedOrgName;
	
	/* 原配属单位名称 */
    @Transient
	private String oldHoldOrgName;
    
    /* 使用别名称 */
    @Transient
    private String trainUseName;
    /* 车型名称 */
	@Transient
	private String trainTypeName;
    /* 所属局简称 */
    @Transient
    private String bShortName;
    /* 所属段简称 */
    @Transient
    private String dShortName;
    /* 所属局名称 */
    @Transient
    private String bName;
    /* 所属段名称 */
    @Transient
    private String dName;
    
    /* 支配段段名称 */
    @Transient
    private String useDName;
    
    /*改配单位名称 */
    @Transient
    private String reAttachmentDeportName;
    /* ****** 扩展字段结束  ********* */
    /**
     * 空构造
     */
    public JczlTrain(){}
    
    /**
     * 
     * <li>说明：车号选择控件查询所需
     * <li>创建人：程梅
     * <li>创建日期：2012-11-2
     * <li>修改人： 
     * <li>修改日期：
     */
    public JczlTrain(String trainNo,Long holdOrgId,String holdOrgName,String holdOrgSeq){
        this.trainNo = trainNo;
        this.holdOrgId = holdOrgId;
        this.holdOrgName = holdOrgName;
        this.holdOrgSeq = holdOrgSeq;
    }
    /**
     * 
     * <li>说明：车号选择控件查询所需
     * <li>创建人：程梅
     * <li>创建日期：2012-11-2
     * <li>修改人： 
     * <li>修改日期：
     */
    public JczlTrain(String trainNo,Long holdOrgId,String holdOrgName,String holdOrgSeq ,
        String makeFactoryIDX,String makeFactoryName,Date leaveDate ,
        String buildUpTypeIDX,String buildUpTypeCode,String buildUpTypeName,
        String trainUse, String trainUseName){
        this.trainNo = trainNo;
        this.holdOrgId = holdOrgId;
        this.holdOrgName = holdOrgName;
        this.holdOrgSeq = holdOrgSeq;
        this.makeFactoryIDX = makeFactoryIDX;
        this.makeFactoryName = makeFactoryName;
        this.leaveDate = leaveDate ;
        this.buildUpTypeIDX = buildUpTypeIDX ;
        this.buildUpTypeCode = buildUpTypeCode ;
        this.buildUpTypeName = buildUpTypeName ;
        this.trainUse = trainUse ;
        this.trainUseName = trainUseName ;
    }
    
    /**
     * 
     * <li>说明：车号选择控件查询所需
     * <li>创建人：程锐
     * <li>创建日期：2013-04-29
     * <li>修改人： 
     * <li>修改日期：
     */
    public JczlTrain(String trainNo,Long holdOrgId,String holdOrgName,String holdOrgSeq ,
        String makeFactoryIDX,String makeFactoryName,Date leaveDate ,
        String buildUpTypeIDX,String buildUpTypeCode,String buildUpTypeName,
        String trainUse, String trainUseName, String bId, String dId, String bName, String bShortName, String dName, String dShortName){
        this.trainNo = trainNo;
        this.holdOrgId = holdOrgId;
        this.holdOrgName = holdOrgName;
        this.holdOrgSeq = holdOrgSeq;
        this.makeFactoryIDX = makeFactoryIDX;
        this.makeFactoryName = makeFactoryName;
        this.leaveDate = leaveDate ;
        this.buildUpTypeIDX = buildUpTypeIDX ;
        this.buildUpTypeCode = buildUpTypeCode ;
        this.buildUpTypeName = buildUpTypeName ;
        this.trainUse = trainUse ;
        this.trainUseName = trainUseName ;
        this.bId = bId;
        this.dId = dId;
        this.bName = bName;
        this.dName = dName;
        this.bShortName = bShortName;
        this.dShortName = dShortName;
    }
    
    /**
     * <li>说明：整备产品化V3.2车号选择控件查询所需
     * <li>创建人：程锐
     * <li>创建日期：2015-3-20
     * <li>修改人：
     * <li>修改日期：
     * @param trainNo 车号
     * @param makeFactoryIDX 生产厂家IDX
     * @param makeFactoryName 生产厂家名称
     * @param leaveDate 出厂日期
     * @param buildUpTypeIDX 组成idx
     * @param buildUpTypeCode 组成编码
     * @param buildUpTypeName 组成名称
     * @param trainUse 机车用途
     * @param trainUseName 机车用途名称
     * @param bId 所属局id
     * @param dId 所属段id
     * @param bName 所属局名称
     * @param bShortName 所属局简称
     * @param dName 所属段名称
     * @param dShortName 所属段简称
     */
    public JczlTrain(String trainNo,
                     String makeFactoryIDX,
                     String makeFactoryName,
                     Date leaveDate ,
                     String buildUpTypeIDX,
                     String buildUpTypeCode,
                     String buildUpTypeName,
                     String trainUse, 
                     String trainUseName, 
                     String bId, 
                     String dId, 
                     String bName, 
                     String bShortName, 
                     String dName, 
                     String dShortName){
        this.trainNo = trainNo;
        this.makeFactoryIDX = makeFactoryIDX;
        this.makeFactoryName = makeFactoryName;
        this.leaveDate = leaveDate ;
        this.buildUpTypeIDX = buildUpTypeIDX ;
        this.buildUpTypeCode = buildUpTypeCode ;
        this.buildUpTypeName = buildUpTypeName ;
        this.trainUse = trainUse ;
        this.trainUseName = trainUseName ;
        this.bId = bId;
        this.dId = dId;
        this.bName = bName;
        this.dName = dName;
        this.bShortName = bShortName;
        this.dShortName = dShortName;
    }
    
    /* getter setter */
    public String getTrainUseName() {
        return trainUseName;
    }
    
    public void setTrainUseName(String trainUseName) {
        this.trainUseName = trainUseName;
    }
	public String getHoldOrgName() {
		return holdOrgName;
	}
	public void setHoldOrgName(String holdOrgName) {
		this.holdOrgName = holdOrgName;
	}
	public String getUsedOrgName() {
		return usedOrgName;
	}
	public void setUsedOrgName(String usedOrgName) {
		this.usedOrgName = usedOrgName;
	}
	public String getOldHoldOrgName() {
		return oldHoldOrgName;
	}
	public void setOldHoldOrgName(String oldHoldOrgName) {
		this.oldHoldOrgName = oldHoldOrgName;
	}
	
	
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

    
    public String getHoldOrgSeq() {
        return holdOrgSeq;
    }

    
    public void setHoldOrgSeq(String holdOrgSeq) {
        this.holdOrgSeq = holdOrgSeq;
    }

    
    public String getTrainTypeName() {
        return trainTypeName;
    }
    
    
    public void setTrainTypeName(String trainTypeName) {
        this.trainTypeName = trainTypeName;
    }

    
    public String getBShortName() {
        return bShortName;
    }

    
    public void setBShortName(String shortName) {
        bShortName = shortName;
    }

    
    public String getDShortName() {
        return dShortName;
    }

    
    public void setDShortName(String shortName) {
        dShortName = shortName;
    }

    
    public String getBName() {
        return bName;
    }

    
    public void setBName(String name) {
        bName = name;
    }

    
    public String getDName() {
        return dName;
    }

    
    public void setDName(String name) {
        dName = name;
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

    
    /**
     * <li>说明：改配单位名稱
     * <li>返回值： the reAttachmentDeportName
     */
    public String getReAttachmentDeportName() {
        return reAttachmentDeportName;
    }

    
    /**
     * <li>说明：改配单位名稱
     * <li>参数： reAttachmentDeportName
     */
    public void setReAttachmentDeportName(String reAttachmentDeportName) {
        this.reAttachmentDeportName = reAttachmentDeportName;
    }

    
    /**
     * <li>说明：支配段名稱
     * <li>返回值： the useDName
     */
    public String getUseDName() {
        return useDName;
    }

    
    /**
     * <li>说明：支配段名稱
     * <li>参数： useDName
     */
    public void setUseDName(String useDName) {
        this.useDName = useDName;
    }

    
    public String getVehicleType() {
        return vehicleType;
    }

    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    
   	
	 
}