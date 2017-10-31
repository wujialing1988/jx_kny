package com.yunda.jx.pjwz.unloadparts.entity;

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
 * <li>说明：PartsUnloadRegister实体类, 数据表：下车配件登记单
 * <li>创建人：程梅
 * <li>创建日期：2015-04-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJWZ_Parts_Unload_Register")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsUnloadRegister implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    
    /** 是否范围内下车- 是 */
    public static final String IS_IN_RANGE_YES = "是";
    /** 是否范围内下车 - 否 */
    public static final String IS_IN_RANGE_NO = "否";
    public static final int ACCEPT_DEPT_TYPE_ORG =  1 ;//责任部门类型：机构 
    public static final int ACCEPT_DEPT_TYPE_WH =  2 ;//责任部门类型：库房
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
    /* 检修任务单主键 */
    @Column(name="RDP_IDX")
    private String rdpIdx;
    /* 检修任务单类型【机车、配件】 */
    @Column(name="RDP_TYPE")
    private String rdpType;
	/* 交件人主键 */
	@Column(name="HAND_OVER_EMP_ID")
	private Long handOverEmpId;
	/* 交件人 */
	@Column(name="HAND_OVER_EMP")
	private String handOverEmp;
	/* 收件日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="TAKE_OVER_TIME")
	private java.util.Date takeOverTime;
	/* 接收人主键 */
	@Column(name="TAKE_OVER_EMP_ID")
	private Long takeOverEmpId;
	/* 接收人 */
	@Column(name="TAKE_OVER_EMP")
	private String takeOverEmp;
	/* 接收部门主键 */
	@Column(name="TAKE_OVER_DEPT_ID")
	private String takeOverDeptId;
	/* 接收部门 */
	@Column(name="TAKE_OVER_DEPT")
	private String takeOverDept;
	/* 接收部门序列(存储组织机构的orgseq字段) */
	@Column(name="TAKE_OVER_DEPT_ORGSEQ")
	private String takeOverDeptOrgseq;
	/* 接收部门类型：1：机构；2：库房 */
	@Column(name="TAKE_OVER_DEPT_Type")
	private Integer takeOverDeptType;
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
	/* 走行公里 */
	@Column(name="RUNING_KM")
	private Integer runingKM;
	/* 生产厂家主键 */
	@Column(name="MADE_FACTORY_IDX")
	private String madeFactoryIdx;
    /* 生产厂名 */
    @Column(name="MADE_FACTORY_NAME")
    private String madeFactoryName;
    /* 出厂日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FACTORY_DATE")
    private java.util.Date factoryDate;
    /*详细配置 */
    @Column(name="CONFIG_DETAIL")
    private String configDetail;
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
    /* 下车位置编码*/
    @Column(name="UNLOAD_Place_code")
    private String unloadPlaceCode;
    /* 下车位置*/
    @Column(name="UNLOAD_Place")
    private String unloadPlace;
    /* 下车原因*/
    @Column(name="UNLOAD_Reason")
    private String unloadReason;
    /* 下车日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UNLOAD_DATE")
    private java.util.Date unloadDate;
	/* 存放地点 */
	private String location;
	/* 是否范围内下车（是，否） */
    @Column(name="IS_IN_RANGE")
	private String isInRange;
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
    /* 扩展编号json */
    @Column(name="extendNoJson")
    private String extendNoJson;
    
    /* 作业工单主键 */
    @Column(name="work_card_idx")
    private String workCardIDX;

    /* 配件状态编码 */
    @Transient
    private String partsStatus;
    /* 配件状态名称 */
    @Transient
    private String partsStatusName;
    
    public String getExtendNoJson() {
        return extendNoJson;
    }
    
    public void setExtendNoJson(String extendNoJson) {
        this.extendNoJson = extendNoJson;
    }
	/**
	 * @return Long 获取交件人主键
	 */
	public Long getHandOverEmpId(){
		return handOverEmpId;
	}
	/**
	 * @param handOverEmpId 设置交件人主键
	 */
	public void setHandOverEmpId(Long handOverEmpId) {
		this.handOverEmpId = handOverEmpId;
	}
	/**
	 * @return String 获取交件人
	 */
	public String getHandOverEmp(){
		return handOverEmp;
	}
	/**
	 * @param handOverEmp 设置交件人
	 */
	public void setHandOverEmp(String handOverEmp) {
		this.handOverEmp = handOverEmp;
	}
	/**
	 * @return java.util.Date 获取收件日期
	 */
	public java.util.Date getTakeOverTime(){
		return takeOverTime;
	}
	/**
	 * @param takeOverTime 设置收件日期
	 */
	public void setTakeOverTime(java.util.Date takeOverTime) {
		this.takeOverTime = takeOverTime;
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
	 * @return String 获取接收部门主键
	 */
	public String getTakeOverDeptId(){
		return takeOverDeptId;
	}
	/**
	 * @param takeOverDeptId 设置接收部门主键
	 */
	public void setTakeOverDeptId(String takeOverDeptId) {
		this.takeOverDeptId = takeOverDeptId;
	}
	/**
	 * @return String 获取接收部门
	 */
	public String getTakeOverDept(){
		return takeOverDept;
	}
	/**
	 * @param takeOverDept 设置接收部门
	 */
	public void setTakeOverDept(String takeOverDept) {
		this.takeOverDept = takeOverDept;
	}
	/**
	 * @return String 获取接收部门序列
	 */
	public String getTakeOverDeptOrgseq(){
		return takeOverDeptOrgseq;
	}
	/**
	 * @param takeOverDeptOrgseq 设置接收部门序列
	 */
	public void setTakeOverDeptOrgseq(String takeOverDeptOrgseq) {
		this.takeOverDeptOrgseq = takeOverDeptOrgseq;
	}
	/**
	 * @return Integer 获取接收部门类型
	 */
	public Integer getTakeOverDeptType(){
		return takeOverDeptType;
	}
	/**
	 * @param takeOverDeptType 设置接收部门类型
	 */
	public void setTakeOverDeptType(Integer takeOverDeptType) {
		this.takeOverDeptType = takeOverDeptType;
	}
	/**
	 * @return String 获取配件信息主键
	 */
	public String getPartsAccountIDX(){
		return partsAccountIDX;
	}
	/**
	 * @param partsAccountIDX 设置配件信息主键
	 */
	public void setPartsAccountIDX(String partsAccountIDX) {
		this.partsAccountIDX = partsAccountIDX;
	}
	/**
	 * @return String 获取配件规格型号主键
	 */
	public String getPartsTypeIDX(){
		return partsTypeIDX;
	}
	/**
	 * @param partsTypeIDX 设置配件规格型号主键
	 */
	public void setPartsTypeIDX(String partsTypeIDX) {
		this.partsTypeIDX = partsTypeIDX;
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
	 * @return Integer 获取走行公里
	 */
	public Integer getRuningKM(){
		return runingKM;
	}
	/**
	 * @param runingKM 设置走行公里
	 */
	public void setRuningKM(Integer runingKM) {
		this.runingKM = runingKM;
	}
	/**
	 * @return String 获取生产厂家主键
	 */
	public String getMadeFactoryIdx(){
		return madeFactoryIdx;
	}
	/**
	 * @param madeFactoryIdx 设置生产厂家主键
	 */
	public void setMadeFactoryIdx(String madeFactoryIdx) {
		this.madeFactoryIdx = madeFactoryIdx;
	}
	
	
    public String getConfigDetail() {
        return configDetail;
    }
    
    public void setConfigDetail(String configDetail) {
        this.configDetail = configDetail;
    }
    
    public java.util.Date getFactoryDate() {
        return factoryDate;
    }
    
    public void setFactoryDate(java.util.Date factoryDate) {
        this.factoryDate = factoryDate;
    }
    
    public String getMadeFactoryName() {
        return madeFactoryName;
    }
    
    public void setMadeFactoryName(String madeFactoryName) {
        this.madeFactoryName = madeFactoryName;
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
	 * @return String 获取下车位置
	 */
	public String getUnloadPlace(){
		return unloadPlace;
	}
	/**
	 * @param unloadPlace 设置下车位置
	 */
	public void setUnloadPlace(String unloadPlace) {
		this.unloadPlace = unloadPlace;
	}
	/**
	 * @return String 获取下车原因
	 */
	public String getUnloadReason(){
		return unloadReason;
	}
	/**
	 * @param unloadReason 设置下车原因
	 */
	public void setUnloadReason(String unloadReason) {
		this.unloadReason = unloadReason;
	}
	/**
	 * @return java.util.Date 获取下车日期
	 */
	public java.util.Date getUnloadDate(){
		return unloadDate;
	}
	/**
	 * @param unloadDate 设置下车日期
	 */
	public void setUnloadDate(java.util.Date unloadDate) {
		this.unloadDate = unloadDate;
	}
	/**
	 * @return String 获取存放地点
	 */
	public String getLocation(){
		return location;
	}
	/**
	 * @param location 设置存放地点
	 */
	public void setLocation(String location) {
		this.location = location;
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
	 * @return String 获取创建人名称
	 */
	public String getCreatorName(){
		return creatorName;
	}
	/**
	 * @param creatorName 设置创建人名称
	 */
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

    
    public String getIdentificationCode() {
        return identificationCode;
    }

    
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }

    
    public String getIsInRange() {
        return isInRange;
    }

    
    public void setIsInRange(String isInRange) {
        this.isInRange = isInRange;
    }

    
    public String getRdpIdx() {
        return rdpIdx;
    }

    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }

    
    public String getRdpType() {
        return rdpType;
    }

    
    public void setRdpType(String rdpType) {
        this.rdpType = rdpType;
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

	public String getUnloadPlaceCode() {
		return unloadPlaceCode;
	}

	public void setUnloadPlaceCode(String unloadPlaceCode) {
		this.unloadPlaceCode = unloadPlaceCode;
	}
}