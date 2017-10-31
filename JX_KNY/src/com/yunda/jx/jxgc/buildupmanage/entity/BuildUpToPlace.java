package com.yunda.jx.jxgc.buildupmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 组成型号和位置关系维护视图实体类
 * <li>创建人：程锐
 * <li>创建日期：2013-1-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="V_JXGC_BUILDUPPLACE")
public class BuildUpToPlace {
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /* idx主键 */
    @Id 
    private String idx;
    /* 组成位置主键 */
    @Column(name="BuildUp_Place_Idx")
    private String buildUpPlaceIdx;
    /* 组成型号主键 */
    @Column(name="BuildUp_Type_Idx")
    private String buildUpTypeIdx;
    /* 组成位置编码全名 */
    @Column(name="BuildUpPlace_FullCode")
    private String buildUpPlaceFullCode;
    /* 组成位置名称全名 */
    @Column(name="BuildUpPlace_FullName")
    private String buildUpPlaceFullName;
    /* 上级组成位置 */
    @Column(name="Parent_Idx")
    private String parentIdx;
    /* 组成位置编码 */
    @Column(name="BuildUpPlace_Code")
    private String buildUpPlaceCode;
    /* 组成位置名称 */
    @Column(name="BuildUpPlace_Name")
    private String buildUpPlaceName;
    /* 组成位置序号 */
    @Column(name="BuildUpPlace_SEQ")
    private Integer buildUpPlaceSEQ;
    /* 组成位置简称 */
    @Column(name="BuildUpPlace_ShortName")
    private String buildUpPlaceShortName;
    /* 图号 */
    @Column(name="Chart_No")
    private String chartNo;
    /* 配件专业类型表主键 */
    @Column(name="Professional_Type_IDX")
    private String professionalTypeIDX;
    /* 专业类型名称 */
    @Column(name="Professional_Type_Name")
    private String professionalTypeName;
    /* 位置编码 */
    @Column(name="PART_ID")
    private String partID;
    /* 位置名称 */
    @Column(name="Part_Name")
    private String partName;
    /* 位置类型：10：结构位置；20：安装位置；30：虚拟位置 */
    @Column(name="Place_Type")
    private Integer placeType;
    /* 备注 */
    private String remarks;
    /* 状态，10：新增；20：启用；30：作废 */
    private Integer status;
    
    /* 车型主键 */
    @Column(name="TRAIN_TYPE_IDX")
    private String trainTypeIDX;
    /* 配件规格型号主键 */
    @Column(name = "Parts_Type_IDX")
    private String partsTypeIDX;
    /**
     * @return String 获取组成位置编码
     */
    public String getBuildUpPlaceCode(){
        return buildUpPlaceCode;
    }
    /**
     * @param 设置组成位置编码
     */
    public void setBuildUpPlaceCode(String buildUpPlaceCode) {
        this.buildUpPlaceCode = buildUpPlaceCode;
    }
    /**
     * @return String 获取组成位置名称
     */
    public String getBuildUpPlaceName(){
        return buildUpPlaceName;
    }
    /**
     * @param 设置组成位置名称
     */
    public void setBuildUpPlaceName(String buildUpPlaceName) {
        this.buildUpPlaceName = buildUpPlaceName;
    }
    /**
     * @return Integer 获取组成位置序号
     */
    public Integer getBuildUpPlaceSEQ(){
        return buildUpPlaceSEQ;
    }
    /**
     * @param 设置组成位置序号
     */
    public void setBuildUpPlaceSEQ(Integer buildUpPlaceSEQ) {
        this.buildUpPlaceSEQ = buildUpPlaceSEQ;
    }
    /**
     * @return String 获取组成位置简称
     */
    public String getBuildUpPlaceShortName(){
        return buildUpPlaceShortName;
    }
    /**
     * @param 设置组成位置简称
     */
    public void setBuildUpPlaceShortName(String buildUpPlaceShortName) {
        this.buildUpPlaceShortName = buildUpPlaceShortName;
    }
    /**
     * @return String 获取图号
     */
    public String getChartNo(){
        return chartNo;
    }
    /**
     * @param 设置图号
     */
    public void setChartNo(String chartNo) {
        this.chartNo = chartNo;
    }
    /**
     * @return String 获取配件专业类型表主键
     */
    public String getProfessionalTypeIDX(){
        return professionalTypeIDX;
    }
    /**
     * @param 设置配件专业类型表主键
     */
    public void setProfessionalTypeIDX(String professionalTypeIDX) {
        this.professionalTypeIDX = professionalTypeIDX;
    }
    /**
     * @return String 获取专业类型名称
     */
    public String getProfessionalTypeName(){
        return professionalTypeName;
    }
    /**
     * @param 设置专业类型名称
     */
    public void setProfessionalTypeName(String professionalTypeName) {
        this.professionalTypeName = professionalTypeName;
    }
    /**
     * @return 
     * @return true 获取位置编码
     */
    public String getPartID(){
        return partID;
    }
    /**
     * @param 设置位置编码
     */
    public void setPartID(String partID) {
        this.partID = partID;
    }
    /**
     * @return String 获取位置名称
     */
    public String getPartName(){
        return partName;
    }
    /**
     * @param 设置位置名称
     */
    public void setPartName(String partName) {
        this.partName = partName;
    }
    /**
     * @return Integer 获取位置类型
     */
    public Integer getPlaceType(){
        return placeType;
    }
    /**
     * @param 设置位置类型
     */
    public void setPlaceType(Integer placeType) {
        this.placeType = placeType;
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
     * @return Integer 获取状态
     */
    public Integer getStatus(){
        return status;
    }
    /**
     * @param 设置状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * @return String 获取组成位置主键
     */
    public String getBuildUpPlaceIdx(){
        return buildUpPlaceIdx;
    }
    /**
     * @param 设置组成位置主键
     */
    public void setBuildUpPlaceIdx(String buildUpPlaceIdx) {
        this.buildUpPlaceIdx = buildUpPlaceIdx;
    }
    /**
     * @return String 获取组成型号主键
     */
    public String getBuildUpTypeIdx(){
        return buildUpTypeIdx;
    }
    /**
     * @param 设置组成型号主键
     */
    public void setBuildUpTypeIdx(String buildUpTypeIdx) {
        this.buildUpTypeIdx = buildUpTypeIdx;
    }
    /**
     * @return String 获取组成位置编码全名
     */
    public String getBuildUpPlaceFullCode(){
        return buildUpPlaceFullCode;
    }
    /**
     * @param 设置组成位置编码全名
     */
    public void setBuildUpPlaceFullCode(String buildUpPlaceFullCode) {
        this.buildUpPlaceFullCode = buildUpPlaceFullCode;
    }
    /**
     * @return String 获取组成位置名称全名
     */
    public String getBuildUpPlaceFullName(){
        return buildUpPlaceFullName;
    }
    /**
     * @param 设置组成位置名称全名
     */
    public void setBuildUpPlaceFullName(String buildUpPlaceFullName) {
        this.buildUpPlaceFullName = buildUpPlaceFullName;
    }
    /**
     * @return String 获取上级组成位置
     */
    public String getParentIdx(){
        return parentIdx;
    }
    /**
     * @param 设置上级组成位置
     */
    public void setParentIdx(String parentIdx) {
        this.parentIdx = parentIdx;
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
    /**
     * @return String 获取车型主键
     */
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }
    /**
     * @param 设置车型主键
     */
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }
    
    /**
     * @return String 获取配件规格型号主键
     */
    public String getPartsTypeIDX() {
        return partsTypeIDX;
    }
    
    /**
     * @param 设置配件规格型号主键
     */
    public void setPartsTypeIDX(String partsTypeIDX) {
        this.partsTypeIDX = partsTypeIDX;
    }
}
