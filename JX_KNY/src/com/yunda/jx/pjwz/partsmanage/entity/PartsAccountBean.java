package com.yunda.jx.pjwz.partsmanage.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: PartsAccount配件周转信息查询实体
 * <li>创建人：何涛
 * <li>创建日期：2016-3-11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJWZ_PARTS_ACCOUNT")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsAccountBean implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    private String idx;
    
    /* 配件型号表主键 */
    @Column(name = "Parts_TYPE_IDX")
    private String partsTypeIDX;
    
    /* 配件名称 */
    @Column(name = "Parts_Name")
    private String partsName;
    
    /* 规格型号 */
    @Column(name = "Specification_Model")
    private String specificationModel;
    
    /* 计量单位 */
    @Column(name = "unit")
    private String unit;
    
    /* 配件编号 */
    @Column(name = "Parts_NO")
    private String partsNo;
    
    /* 生产厂主键 */
    @Column(name = "MADE_FACTORY_IDX")
    private String madeFactoryIdx;
    
    /* 生产厂名 */
    @Column(name = "MADE_FACTORY_NAME")
    private String madeFactoryName;
    
    /* 出厂日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FACTORY_DATE")
    private java.util.Date factoryDate;
    
    /* 存放地点 */
    @Column(name = "LOCATION")
    private String location;
    
    /* 详细配置 */
    @Column(name = "CONFIG_DETAIL")
    private String configDetail;
    
    /* 责任部门id */
    @Column(name = "MANAGE_DEPT_ID")
    private String manageDeptId;
    
    /* 责任部门 */
    @Column(name = "MANAGE_DEPT")
    private String manageDept;
    
    /* 责任部门类型 */
    @Column(name = "MANAGE_DEPT_Type")
    private Integer manageDeptType;
    
    /* 配件状态编码 */
    @Column(name = "Parts_Status")
    private String partsStatus;
    
    /* 配件状态名称 */
    @Column(name = "Parts_Status_Name")
    private String partsStatusName;
    
    /* 是否新品 */
    @Column(name = "IS_NEW_PARTS")
    private String isNewParts;
    
    /* 配件旧编号 */
    @Column(name = "OLD_PARTS_NO")
    private String oldPartsNo;
    
    /* 下车车型 */
    @Column(name = "UNLOAD_TRAINTYPE")
    private String unloadTrainType;
    
    /* 下车车型编码 */
    @Column(name = "UNLOAD_TRAINTYPE_IDX")
    private String unloadTrainTypeIdx;
    
    /* 下车车号 */
    @Column(name = "UNLOAD_TRAINNO")
    private String unloadTrainNo;
    
    /* 下车修程编码 */
    @Column(name = "UNLOAD_Repair_Class_IDX")
    private String unloadRepairClassIdx;
    
    /* 下车修程 */
    @Column(name = "UNLOAD_Repair_Class")
    private String unloadRepairClass;
    
    /* 下车修次编码 */
    @Column(name = "UNLOAD_Repair_time_IDX")
    private String unloadRepairTimeIdx;
    
    /* 下车修次 */
    @Column(name = "UNLOAD_Repair_time")
    private String unloadRepairTime;
    
    /* 下车位置 */
    @Column(name = "UNLOAD_Place")
    private String unloadPlace;
    
    /* 下车原因 */
    @Column(name = "UNLOAD_Reason")
    private String unloadReason;
    
    /* 下车日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UNLOAD_DATE")
    private java.util.Date unloadDate;
    
    /* 上车车型 */
    @Column(name = "ABOARD_TRAINTYPE")
    private String aboardTrainType;
    
    /* 上车车型编码 */
    @Column(name = "ABOARD_TRAINTYPE_IDX")
    private String aboardTrainTypeIdx;
    
    /* 上车车号 */
    @Column(name = "ABOARD_TRAINNO")
    private String aboardTrainNo;
    
    /* 上车修程编码 */
    @Column(name = "ABOARD_Repair_Class_IDX")
    private String aboardRepairClassIdx;
    
    /* 上车修程 */
    @Column(name = "ABOARD_Repair_Class")
    private String aboardRepairClass;
    
    /* 上车修次编码 */
    @Column(name = "ABOARD_Repair_time_IDX")
    private String aboardRepairTimeIdx;
    
    /* 上车修次 */
    @Column(name = "ABOARD_Repair_time")
    private String aboardRepairTime;
    
    /* 上车位置 */
    @Column(name = "ABOARD_Place")
    private String aboardPlace;
    
    /* 上车日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ABOARD_DATE")
    private java.util.Date aboardDate;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /* 配件状态更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PARTS_STATUS_UPDATE_DATE")
    private java.util.Date partsStatusUpdateDate;
    
    /* 责任部门序列 */
    @Column(name = "MANAGE_DEPT_ORGSEQ")
    private String manageDeptOrgseq;
    
    /* 扩展编号json */
    @Column(name = "extendNoJson")
    private String extendNoJson;
    
    /* 配件识别码 */
    @Column(name = "IDENTIFICATION_CODE")
    private String identificationCode;
    
    /* 配件来源 （新、调入、下车） */
    private String source;
    
    /* 下车检修任务单主键 */
    @Column(name = "UNLOAD_RDP_IDX")
    private String unloadRdpIDX;
    
    /* 下车检修任务单类型 */
    @Column(name = "UNLOAD_RDP_TYPE")
    private String unloadRdpType;
    
    /* 上车检修任务单主键 */
    @Column(name = "ABOARD_RDP_IDX")
    private String aboardRdpIDX;
    
    /* 上车检修任务单类型 */
    @Column(name = "ABOARD_RDP_TYPE")
    private String aboadRdpType;
    
    /** 联合查询字段：物料编码 */
    @Column(name = "MAT_CODE")
    private String matCode;
    
    /** 联合查询字段：领件日期 */
    @Column(name = "GET_DATE")
    private Date getDate;
    
    public String getMatCode() {
        return matCode;
    }
    
    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }
    
    public Date getGetDate() {
        return getDate;
    }
    
    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }
    
    public String getPartsTypeIDX() {
        return partsTypeIDX;
    }
    
    public void setPartsTypeIDX(String partsTypeIDX) {
        this.partsTypeIDX = partsTypeIDX;
    }
    
    public String getPartsName() {
        return partsName;
    }
    
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }
    
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
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
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getIsNewParts() {
        return isNewParts;
    }
    
    public void setIsNewParts(String isNewParts) {
        this.isNewParts = isNewParts;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
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
    
    public String getManageDept() {
        return manageDept;
    }
    
    public void setManageDept(String manageDept) {
        this.manageDept = manageDept;
    }
    
    public java.util.Date getFactoryDate() {
        return factoryDate;
    }
    
    public void setFactoryDate(java.util.Date factoryDate) {
        this.factoryDate = factoryDate;
    }
    
    public Integer getManageDeptType() {
        return manageDeptType;
    }
    
    public void setManageDeptType(Integer manageDeptType) {
        this.manageDeptType = manageDeptType;
    }
    
    public String getOldPartsNo() {
        return oldPartsNo;
    }
    
    public void setOldPartsNo(String oldPartsNo) {
        this.oldPartsNo = oldPartsNo;
    }
    
    public String getPartsNo() {
        return partsNo;
    }
    
    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }
    
    public String getPartsStatus() {
        return partsStatus;
    }
    
    public void setPartsStatus(String partsStatus) {
        this.partsStatus = partsStatus;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
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
    
    public String getUnloadReason() {
        return unloadReason;
    }
    
    public void setUnloadReason(String unloadReason) {
        this.unloadReason = unloadReason;
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
    
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Long getUpdator() {
        return updator;
    }
    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    
    public String getPartsStatusName() {
        return partsStatusName;
    }
    
    public void setPartsStatusName(String partsStatusName) {
        this.partsStatusName = partsStatusName;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getManageDeptId() {
        return manageDeptId;
    }
    
    public void setManageDeptId(String manageDeptId) {
        this.manageDeptId = manageDeptId;
    }
    
    public java.util.Date getPartsStatusUpdateDate() {
        return partsStatusUpdateDate;
    }
    
    public void setPartsStatusUpdateDate(java.util.Date partsStatusUpdateDate) {
        this.partsStatusUpdateDate = partsStatusUpdateDate;
    }
    
    public String getManageDeptOrgseq() {
        return manageDeptOrgseq;
    }
    
    public void setManageDeptOrgseq(String manageDeptOrgseq) {
        this.manageDeptOrgseq = manageDeptOrgseq;
    }
    
    public String getExtendNoJson() {
        return extendNoJson;
    }
    
    public void setExtendNoJson(String extendNoJson) {
        this.extendNoJson = extendNoJson;
    }
    
    public String getIdentificationCode() {
        return identificationCode;
    }
    
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getAboadRdpType() {
        return aboadRdpType;
    }
    
    public void setAboadRdpType(String aboadRdpType) {
        this.aboadRdpType = aboadRdpType;
    }
    
    public String getAboardRdpIDX() {
        return aboardRdpIDX;
    }
    
    public void setAboardRdpIDX(String aboardRdpIDX) {
        this.aboardRdpIDX = aboardRdpIDX;
    }
    
    public String getUnloadRdpIDX() {
        return unloadRdpIDX;
    }
    
    public void setUnloadRdpIDX(String unloadRdpIDX) {
        this.unloadRdpIDX = unloadRdpIDX;
    }
    
    public String getUnloadRdpType() {
        return unloadRdpType;
    }
    
    public void setUnloadRdpType(String unloadRdpType) {
        this.unloadRdpType = unloadRdpType;
    }
}
