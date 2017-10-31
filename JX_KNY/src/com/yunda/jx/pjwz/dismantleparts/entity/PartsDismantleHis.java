package com.yunda.jx.pjwz.dismantleparts.entity;

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
 * <li>说明：PartsDismantle实体类, 配件拆卸历史查询视图
 * <li>创建人：何涛
 * <li>创建日期：2016-01-20
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJWZ_PARTS_DISMANTLE_HIS")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsDismantleHis implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 上级配件信息主键 */
    @Column(name = "PARENT_PARTS_ACCOUNT_IDX")
    private String parentPartsAccountIDX;
    
    /* 配件识别码 */
    @Column(name = "IDENTIFICATION_CODE")
    private String identificationCode;
    
    @Column(name = "PARTS_NO")
    private String partsNo;
    /* 配件铭牌号 */
    @Column(name="nameplate_No")
    private String nameplateNo;
    /* 配件名称 */
    @Column(name = "PARTS_NAME")
    private String partsName;
    
    /* 规格型号 */
    @Column(name = "SPECIFICATION_MODEL")
    private String specificationModel;
    
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
    
    /* 位置 */
    @Column(name = "UNLOAD_Place")
    private String unloadPlace;
    
    /* 拆卸原因 */
    @Column(name = "UNLOAD_Reason")
    private String unloadReason;
    
    /* 拆卸日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UNLOAD_DATE")
    private java.util.Date unloadDate;
    
    /* 配件状态编码 */
    @Column(name = "Parts_Status")
    private String partsStatus;
    
    /* 配件状态名称 */
    @Column(name = "Parts_Status_Name")
    private String partsStatusName;
    
    /* 检修需求主键 */
    @Column(name = "WP_IDX")
    private String wpIDX;
    
    /* 检修需求编号 */
    @Column(name = "WP_No")
    private String wpNo;
    
    /* 检修需求描述 */
    @Column(name = "WP_Desc")
    private String wpDesc;
    
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
    
    public String getParentPartsAccountIDX() {
        return parentPartsAccountIDX;
    }
    
    public void setParentPartsAccountIDX(String parentPartsAccountIDX) {
        this.parentPartsAccountIDX = parentPartsAccountIDX;
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
    
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
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
    
    public String getWpDesc() {
        return wpDesc;
    }
    
    public void setWpDesc(String wpDesc) {
        this.wpDesc = wpDesc;
    }
    
    public String getWpIDX() {
        return wpIDX;
    }
    
    public void setWpIDX(String wpIDX) {
        this.wpIDX = wpIDX;
    }
    
    public String getWpNo() {
        return wpNo;
    }
    
    public void setWpNo(String wpNo) {
        this.wpNo = wpNo;
    }

    
    public String getNameplateNo() {
        return nameplateNo;
    }

    
    public void setNameplateNo(String nameplateNo) {
        this.nameplateNo = nameplateNo;
    }
    
}
