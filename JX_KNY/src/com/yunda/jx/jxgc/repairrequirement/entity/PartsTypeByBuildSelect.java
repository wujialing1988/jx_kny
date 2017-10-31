package com.yunda.jx.jxgc.repairrequirement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 配件组成型号选择控件实体类 V_JXGC_PARTSTYPE_SELECT视图
 * <li>创建人：程锐
 * <li>创建日期：2013-5-3
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="V_JXGC_PARTSTYPE_SELECT")
public class PartsTypeByBuildSelect {
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;   
    /* idx主键 */
    @Id
    private String idx;
    /* 专业类型表主键*/
    @Column(name="Professional_Type_IDX")
    private String professionalTypeIdx;
    
    /* 专业类型名称*/
    @Column(name="Professional_Type_Name")
    private String professionalTypeName;
    /* 配件分类表主键 */
    @Column(name="Parts_Class_IDX")
    private String partsClassIdx;
    /* 配件分类名称 */
    @Column(name="Class_Name")
    private String className;
    /* 物料编码 */
    @Column(name="Mat_Code")
    private String matCode;
    /* 配件名称 */
    @Column(name="Parts_NAME")
    private String partsName;
    /* 规格型号*/
    @Column(name="Specification_Model")
    private String specificationModel;
    /* 规格型号编码*/
    @Column(name="Specification_Model_Code")
    private String specificationModelCode;
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name="Record_Status")
    private Integer recordStatus;
    
    /* 组成型号主键 */
    @Column(name = "buildup_type_idx")
    private String buildUpTypeIDX;
    /* 组成型号编码 */
    @Column(name = "BuildUp_Type_Code")
    private String buildUpTypeCode;
    
    /* 组成型号名称 */
    @Column(name = "BuildUp_Type_Name")
    private String buildUpTypeName;
    
    /* 组成型号描述 */
    @Column(name = "BuildUp_Type_Desc")
    private String buildUpTypeDesc;
    /* 图号*/
    @Column(name="CHART_NO")
    private String chartNo;
    /**配件规格型号主键*/
    private String partsTypeIDX;
    /** 是否虚拟配件； 根据组成型号是否有对应配件规格型号确定是否是虚拟配件。 无对应配件规格型号：是；有：否 */
    private String isVirtual;
    
    public String getBuildUpTypeCode() {
        return buildUpTypeCode;
    }
    
    public void setBuildUpTypeCode(String buildUpTypeCode) {
        this.buildUpTypeCode = buildUpTypeCode;
    }
    
    public String getBuildUpTypeDesc() {
        return buildUpTypeDesc;
    }
    
    public void setBuildUpTypeDesc(String buildUpTypeDesc) {
        this.buildUpTypeDesc = buildUpTypeDesc;
    }
    
    public String getBuildUpTypeIDX() {
        return buildUpTypeIDX;
    }
    
    public void setBuildUpTypeIDX(String buildUpTypeIDX) {
        this.buildUpTypeIDX = buildUpTypeIDX;
    }
    
    public String getBuildUpTypeName() {
        return buildUpTypeName;
    }
    
    public void setBuildUpTypeName(String buildUpTypeName) {
        this.buildUpTypeName = buildUpTypeName;
    }
    
    public String getChartNo() {
        return chartNo;
    }
    
    public void setChartNo(String chartNo) {
        this.chartNo = chartNo;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getMatCode() {
        return matCode;
    }
    
    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }
    
    public String getPartsClassIdx() {
        return partsClassIdx;
    }
    
    public void setPartsClassIdx(String partsClassIdx) {
        this.partsClassIdx = partsClassIdx;
    }
    
    public String getPartsName() {
        return partsName;
    }
    
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }
    
    public String getProfessionalTypeIdx() {
        return professionalTypeIdx;
    }
    
    public void setProfessionalTypeIdx(String professionalTypeIdx) {
        this.professionalTypeIdx = professionalTypeIdx;
    }
    
    public String getProfessionalTypeName() {
        return professionalTypeName;
    }
    
    public void setProfessionalTypeName(String professionalTypeName) {
        this.professionalTypeName = professionalTypeName;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }
    
    public String getSpecificationModelCode() {
        return specificationModelCode;
    }
    
    public void setSpecificationModelCode(String specificationModelCode) {
        this.specificationModelCode = specificationModelCode;
    }

    
    public String getPartsTypeIDX() {
        return partsTypeIDX;
    }

    
    public void setPartsTypeIDX(String partsTypeIDX) {
        this.partsTypeIDX = partsTypeIDX;
    }

    
    public String getIsVirtual() {
        return isVirtual;
    }

    
    public void setIsVirtual(String isVirtual) {
        this.isVirtual = isVirtual;
    }
    
    
    
}
