package com.yunda.zb.pjdj.unloadparts.entity;

import java.io.Serializable;

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
 * <li>标题: 机车整备管理信息系统
 * <li>说明：PartsUnloadRegister实体类, 数据表：配件下车登记单
 * <li>创建人：黄杨
 * <li>创建日期：2016-09-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_PARTS_UNLOAD_REGISTER")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbPartsUnloadRegister implements Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    
    /** 是否范围内下车- 是 */
    public static final String IS_IN_RANGE_YES = "是";
    /** 是否范围内下车 - 否 */
    public static final String IS_IN_RANGE_NO = "否";
    public static final int ACCEPT_DEPT_TYPE_ORG =  1 ;//责任部门类型：机构 
    public static final int ACCEPT_DEPT_TYPE_WH =  2 ;//责任部门类型：库房
    
    /** 解备状态 01整备 02解备 */
    public static final String JB_STATUS_YES = "01" ;
    public static final String JB_STATUS_NO = "02" ;
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
    
    /* 解备状态 */
    @Column(name="JB_STATUS")
    private String jbStatus ;

    /* 配件状态编码 */
    @Transient
    private String partsStatus;
    /* 配件状态名称 */
    @Transient
    private String partsStatusName;
    
	public String getConfigDetail() {
		return configDetail;
	}
	public void setConfigDetail(String configDetail) {
		this.configDetail = configDetail;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public java.util.Date getFactoryDate() {
		return factoryDate;
	}
	public void setFactoryDate(java.util.Date factoryDate) {
		this.factoryDate = factoryDate;
	}
	public String getIdentificationCode() {
		return identificationCode;
	}
	public void setIdentificationCode(String identificationCode) {
		this.identificationCode = identificationCode;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getMadeFactoryName() {
		return madeFactoryName;
	}
	public void setMadeFactoryName(String madeFactoryName) {
		this.madeFactoryName = madeFactoryName;
	}
	public String getNameplateNo() {
		return nameplateNo;
	}
	public void setNameplateNo(String nameplateNo) {
		this.nameplateNo = nameplateNo;
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
	public String getPartsStatusName() {
		return partsStatusName;
	}
	public void setPartsStatusName(String partsStatusName) {
		this.partsStatusName = partsStatusName;
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
	public String getTakeOverDept() {
		return takeOverDept;
	}
	public void setTakeOverDept(String takeOverDept) {
		this.takeOverDept = takeOverDept;
	}
	public String getTakeOverDeptId() {
		return takeOverDeptId;
	}
	public void setTakeOverDeptId(String takeOverDeptId) {
		this.takeOverDeptId = takeOverDeptId;
	}
	public String getTakeOverDeptOrgseq() {
		return takeOverDeptOrgseq;
	}
	public void setTakeOverDeptOrgseq(String takeOverDeptOrgseq) {
		this.takeOverDeptOrgseq = takeOverDeptOrgseq;
	}
	public Integer getTakeOverDeptType() {
		return takeOverDeptType;
	}
	public void setTakeOverDeptType(Integer takeOverDeptType) {
		this.takeOverDeptType = takeOverDeptType;
	}
	public java.util.Date getUnloadDate() {
		return unloadDate;
	}
	public void setUnloadDate(java.util.Date unloadDate) {
		this.unloadDate = unloadDate;
	}
	public String getUnloadPlace() {
		return unloadPlace;
	}
	public void setUnloadPlace(String unloadPlace) {
		this.unloadPlace = unloadPlace;
	}
	public String getUnloadPlaceCode() {
		return unloadPlaceCode;
	}
	public void setUnloadPlaceCode(String unloadPlaceCode) {
		this.unloadPlaceCode = unloadPlaceCode;
	}
	public String getUnloadReason() {
		return unloadReason;
	}
	public void setUnloadReason(String unloadReason) {
		this.unloadReason = unloadReason;
	}
	public String getUnloadTrainNo() {
		return unloadTrainNo;
	}
	public void setUnloadTrainNo(String unloadTrainNo) {
		this.unloadTrainNo = unloadTrainNo;
	}
	public String getUnloadTrainTypeIdx() {
		return unloadTrainTypeIdx;
	}
	public void setUnloadTrainTypeIdx(String unloadTrainTypeIdx) {
		this.unloadTrainTypeIdx = unloadTrainTypeIdx;
	}
	public Long getUpdator() {
		return updator;
	}
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getExtendNoJson() {
		return extendNoJson;
	}
	public void setExtendNoJson(String extendNoJson) {
		this.extendNoJson = extendNoJson;
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
	public String getIsInRange() {
		return isInRange;
	}
	public void setIsInRange(String isInRange) {
		this.isInRange = isInRange;
	}
	public String getMadeFactoryIdx() {
		return madeFactoryIdx;
	}
	public void setMadeFactoryIdx(String madeFactoryIdx) {
		this.madeFactoryIdx = madeFactoryIdx;
	}
	public String getPartsStatus() {
		return partsStatus;
	}
	public void setPartsStatus(String partsStatus) {
		this.partsStatus = partsStatus;
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
	public Integer getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	public Integer getRuningKM() {
		return runingKM;
	}
	public void setRuningKM(Integer runingKM) {
		this.runingKM = runingKM;
	}
	public String getSiteID() {
		return siteID;
	}
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTakeOverEmp() {
		return takeOverEmp;
	}
	public void setTakeOverEmp(String takeOverEmp) {
		this.takeOverEmp = takeOverEmp;
	}
	public Long getTakeOverEmpId() {
		return takeOverEmpId;
	}
	public void setTakeOverEmpId(Long takeOverEmpId) {
		this.takeOverEmpId = takeOverEmpId;
	}
	public java.util.Date getTakeOverTime() {
		return takeOverTime;
	}
	public void setTakeOverTime(java.util.Date takeOverTime) {
		this.takeOverTime = takeOverTime;
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
	public String getUnloadTrainType() {
		return unloadTrainType;
	}
	public void setUnloadTrainType(String unloadTrainType) {
		this.unloadTrainType = unloadTrainType;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getWorkCardIDX() {
		return workCardIDX;
	}
	public void setWorkCardIDX(String workCardIDX) {
		this.workCardIDX = workCardIDX;
	}
    
    public String getJbStatus() {
        return jbStatus;
    }
    public void setJbStatus(String jbStatus) {
        this.jbStatus = jbStatus;
    }
}
