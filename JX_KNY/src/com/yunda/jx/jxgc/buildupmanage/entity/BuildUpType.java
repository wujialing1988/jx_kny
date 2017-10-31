
package com.yunda.jx.jxgc.buildupmanage.entity;

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
 * <li>说明：BuildUpType实体类, 数据表：组成型号
 * <li>创建人：程锐
 * <li>创建日期：2012-10-24
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_BUILDUP_TYPE")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class BuildUpType implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 新增状态 */
    public static final int NEW_STATUS = 10;
    
    /** 启用状态 */
    public static final int USE_STATUS = 20;
    
    /** 作废状态 */
    public static final int NULLIFY_STATUS = 30;
    
    /** 是否为标准组成， 1：是 */
    public static final int ISDEFAULT_YES = 1;
    
    /** 是否为标准组成， 0：否 */
    public static final int ISDEFAULT_NO = 0;
    
    /** 机车组成型号 */
    public static final int TYPE_TRAIN = 20;
    
    /** 配件组成型号 */
    public static final int TYPE_PARTS = 10;
    
    /** 虚拟组成型号 */
    public static final int TYPE_VIRTUAL = 30;
    
    /** 图标路径 */
    public static final String IMAGE_PATH = "/jsp/jx/images/builduptree/";
    
    /** 车组成根节点位置ID */
    public static final String PARENT_IDX = "ROOT_0";
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 组成型号编码 */
    @Column(name = "BuildUp_Type_Code")
    private String buildUpTypeCode;
    
    /* 组成型号 默认时对应配件规格型号、对应车型简称*/
    @Column(name = "BuildUp_Type_Name")
    private String buildUpTypeName;
    
    /* 组成型号名称 默认时对应配件名称、对应车型名称*/
    @Column(name = "BuildUp_Type_Desc")
    private String buildUpTypeDesc;
    
    /* 组成类型，10：表示配件组成型号；20：表示机车组成型号; 30:表示虚拟组成型号 */
    private Integer type;
    
    /* 车型主键 */
    @Column(name = "Train_Type_IDX")
    private String trainTypeIDX;
    
    /* 车型英文简称 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;
    
    /* 配件规格型号主键 */
    @Column(name = "Parts_Type_IDX")
    private String partsTypeIDX;
    
    /* 配件名称 */
    @Column(name = "Parts_Name")
    private String partsName;
    
    /* 规格型号 */
    @Column(name = "Specification_Model")
    private String specificationModel;
    
    /* 配件专业类型表主键 */
    @Column(name="Professional_Type_IDX")
    private String professionalTypeIDX;
    
    /* 专业类型名称 */
    @Column(name="Professional_Type_Name")
    private String professionalTypeName;
    
    /* 是否为标准组成 (1:是 0：否) */
    @Column(name = "Is_Default")
    private Integer isDefault;
    
    /* 状态，10：新增；20：启用；30：作废 */
    private Integer status;
    
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
    
    /**
     * @return String 获取组成型号编码
     */
    public String getBuildUpTypeCode() {
        return buildUpTypeCode;
    }
    
    /**
     * @param 设置组成型号编码
     */
    public void setBuildUpTypeCode(String buildUpTypeCode) {
        this.buildUpTypeCode = buildUpTypeCode;
    }
    
    /**
     * @return String 获取组成型号
     */
    public String getBuildUpTypeName() {
        return buildUpTypeName;
    }
    
    /**
     * @param 设置组成型号
     */
    public void setBuildUpTypeName(String buildUpTypeName) {
        this.buildUpTypeName = buildUpTypeName;
    }
    
    /**
     * @return String 获取组成型号名称
     */
    public String getBuildUpTypeDesc() {
        return buildUpTypeDesc;
    }
    
    /**
     * @param 设置组成型号名称
     */
    public void setBuildUpTypeDesc(String buildUpTypeDesc) {
        this.buildUpTypeDesc = buildUpTypeDesc;
    }
    
    /**
     * @return Integer 获取组成类型
     */
    public Integer getType() {
        return type;
    }
    
    /**
     * @param 设置组成类型
     */
    public void setType(Integer type) {
        this.type = type;
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
     * @return String 获取车型简称
     */
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    /**
     * @param 设置车型简称
     */
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
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
    
    /**
     * @return String 获取配件名称
     */
    public String getPartsName() {
        return partsName;
    }
    
    /**
     * @param 设置配件名称
     */
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }
    
    /**
     * @return String 获取配件规格型号
     */
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    /**
     * @param 设置配件规格型号
     */
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
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
     * @return Integer 获取状态
     */
    public Integer getStatus() {
        return status;
    }
    
    /**
     * @param 设置状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    /**
     * @return Integer 获取记录状态
     */
    public Integer getRecordStatus() {
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
    public String getSiteID() {
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
    public Long getCreator() {
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
    public java.util.Date getCreateTime() {
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
    public Long getUpdator() {
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
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * @param 设置修改时间
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
     * @param 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    /**
     * @return Integer 是否为标准组成
     */
    public Integer getIsDefault() {
        return isDefault;
    }
    
    /**
     * @param 设置是否为标准组成
     */
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
    
    public BuildUpType(){}
    
    public BuildUpType(String idx, String buildUpTypeCode, String buildUpTypeName, String partsName, String specificationModel){
        this.idx = idx;
        this.buildUpTypeCode = buildUpTypeCode;
        this.buildUpTypeName = buildUpTypeName;
        this.partsName = partsName;
        this.specificationModel = specificationModel;
    }
    
}
