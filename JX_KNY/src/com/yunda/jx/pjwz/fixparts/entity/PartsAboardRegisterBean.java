package com.yunda.jx.pjwz.fixparts.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 下车配件查询封装实体
 * <li>创建人：张迪
 * <li>创建日期：2016-11-3
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsAboardRegisterBean implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    @Column(name = "row_num")
    private String rownum ;
    
    /* 下车登记表主键 */
    @Column(name = "idx")
    private String idx;
    
    /* 大部件编码 */
    @Column(name = "JCPJBM")
    private String jcpjbm ;
    
    /* 大部件名称 */
    @Column(name = "JCPJMC")
    private String jcpjmc ;
    
    /* 检修任务单主键 */
    @Column(name = "RDP_IDX")
    private String rdpIdx;
    
    /* 配件信息主键 */
    @Column(name = "PARTS_ACCOUNT_IDX")
    private String partsAccountIDX;
    
    /* 配件规格型号主键 */
    @Column(name = "PARTS_TYPE_IDX")
    private String partsTypeIDX;
    
    /* 规格型号 */
    @Column(name = "SPECIFICATION_MODEL")
    private String specificationModel;
    
    /* 配件名称 */
    @Column(name = "PARTS_NAME")
    private String partsName;
    
    /* 配件编号 */
    @Column(name = "PARTS_NO")
    private String partsNo;
    
    /* 配件识别码 */
    @Column(name = "IDENTIFICATION_CODE")
    private String identificationCode;

    /* 上车位置 */
    @Column(name = "aboard_Place")
    private String aboardPlace;
    /* 上车日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="aboard_date")
    private java.util.Date aboardDate;
    
    /** 是否范围内下车（是，否） */
    @Column(name = "IS_IN_RANGE")
    private String isInRange;
    
    /** 下车登记表主键 */
    @Column(name = "unload_idx")
    private String unloadIdx; 
    
    /** 下车配件信息主键 */
    @Column(name = "unload_PARTS_ACCOUNT_IDX")
    private String unloadPartsAccountIDX;

    /** 下车规格型号主键 */
    @Column(name = "unload_parts_type_idx")
    private String unloadPartsTypeIDX;

    /** 下车配件识别码 */
    @Column(name = "unload_identification_code")
    private String unloadIdentificationCode;
    
    /** 下车规格型号 */
    @Column(name = "unload_Specification_Model")
    private String unloadSpecificationModel;

    /** 下车配件编号 */
    @Column(name = "unload_Parts_NO")
    private String unloadPartsNo;
    /** 下车位置*/
    @Column(name="unload_Place")
    private String unloadPlace;
    
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
    
    public String getIsInRange() {
        return isInRange;
    }
    
    public void setIsInRange(String isInRange) {
        this.isInRange = isInRange;
    }
    
    public String getJcpjbm() {
        return jcpjbm;
    }
    
    public void setJcpjbm(String jcpjbm) {
        this.jcpjbm = jcpjbm;
    }
    
    public String getJcpjmc() {
        return jcpjmc;
    }
    
    public void setJcpjmc(String jcpjmc) {
        this.jcpjmc = jcpjmc;
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
    
    public String getRdpIdx() {
        return rdpIdx;
    }
    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }
    
    public String getRownum() {
        return rownum;
    }
    
    public void setRownum(String rownum) {
        this.rownum = rownum;
    }
    
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }
    
    public String getUnloadIdentificationCode() {
        return unloadIdentificationCode;
    }
    
    public void setUnloadIdentificationCode(String unloadIdentificationCode) {
        this.unloadIdentificationCode = unloadIdentificationCode;
    }
    
    public String getUnloadIdx() {
        return unloadIdx;
    }
    
    public void setUnloadIdx(String unloadIdx) {
        this.unloadIdx = unloadIdx;
    }
    
    public String getUnloadPartsAccountIDX() {
        return unloadPartsAccountIDX;
    }
    
    public void setUnloadPartsAccountIDX(String unloadPartsAccountIDX) {
        this.unloadPartsAccountIDX = unloadPartsAccountIDX;
    }
    
    public String getUnloadPartsNo() {
        return unloadPartsNo;
    }
    
    public void setUnloadPartsNo(String unloadPartsNo) {
        this.unloadPartsNo = unloadPartsNo;
    }
    
    public String getUnloadPartsTypeIDX() {
        return unloadPartsTypeIDX;
    }
    
    public void setUnloadPartsTypeIDX(String unloadPartsTypeIDX) {
        this.unloadPartsTypeIDX = unloadPartsTypeIDX;
    }
    
    public String getUnloadPlace() {
        return unloadPlace;
    }
    
    public void setUnloadPlace(String unloadPlace) {
        this.unloadPlace = unloadPlace;
    }
    
    public String getUnloadSpecificationModel() {
        return unloadSpecificationModel;
    }
    
    public void setUnloadSpecificationModel(String unloadSpecificationModel) {
        this.unloadSpecificationModel = unloadSpecificationModel;
    }

   
}
