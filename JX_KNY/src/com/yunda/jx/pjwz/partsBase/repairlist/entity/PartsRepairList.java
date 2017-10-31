package com.yunda.jx.pjwz.partsBase.repairlist.entity;

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
 * <li>说明：配件自修列表
 * <li>创建人： 张凡
 * <li>创建日期：2015年11月4日
 * <li>成都运达科技股份有限公司
 */
@Entity
@Table(name = "PJWZ_Parts_Repair_List")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsRepairList implements java.io.Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /** 是否合格验收 - 否 */
    public static final String IS_HGYS_NO = "否";    
    /** 是否合格验收 - 是 */
    public static final String IS_HGYS_YES = "是";
    
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 配件型号主键 */
    @Column(name = "Parts_Type_IDX")
    private String partsTypeIDX;
    
    /* 生产厂家主键 */
    @Column(name = "Repair_OrgID")
    private String repairOrgID;
    
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
    
    /* 是否合格验收 */
    @Column(name = "isHgys")
    private String isHgys;
    
    /* 瞬时字段 */
    /* 机构名称 */
    @Transient
    private String orgname;
    
    /* 机构系列 */
    @Transient
    private String orgseq;
    
    /* 配件名称 */
    @Transient
    private String partsName;
    
    /* 规格型号 */
    @Transient
    private String specificationModel;
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：何涛
     * <li>创建日期：2016-1-29
     * <li>修改人：
     * <li>修改日期：
     */
    public PartsRepairList() {
        super();
    }
    
    /**
     * <li>说明：用于hibernate查询的构造方法
     * <li>创建人：何涛
     * <li>创建日期：2016-1-29
     * <li>修改人：
     * <li>修改日期：
     * @param idx
     * @param partsTypeIDX
     * @param repairOrgID
     * @param isHgys
     * @param orgname
     * @param orgseq
     * @param partsName
     * @param specificationModel
     */
    public PartsRepairList(String idx, String partsTypeIDX, String repairOrgID, String isHgys, String orgname, String orgseq, String partsName,
        String specificationModel) {
        super();
        this.idx = idx;
        this.partsTypeIDX = partsTypeIDX;
        this.repairOrgID = repairOrgID;
        this.isHgys = isHgys;
        this.orgname = orgname;
        this.orgseq = orgseq;
        this.partsName = partsName;
        this.specificationModel = specificationModel;
    }
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：何涛
     * <li>创建日期：2016-1-30
     * <li>修改人： 
     * <li>修改日期：
     * @param idx
     * @param partsTypeIDX
     * @param repairOrgID
     * @param isHgys
     * @param partsName
     * @param specificationModel
     */
    public PartsRepairList(String idx, String partsTypeIDX, String repairOrgID, String isHgys, String partsName, String specificationModel) {
        super();
        this.idx = idx;
        this.partsTypeIDX = partsTypeIDX;
        this.repairOrgID = repairOrgID;
        this.isHgys = isHgys;
        this.partsName = partsName;
        this.specificationModel = specificationModel;
    }

    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getPartsTypeIDX() {
        return partsTypeIDX;
    }
    
    public void setPartsTypeIDX(String partsTypeIDX) {
        this.partsTypeIDX = partsTypeIDX;
    }
    
    public String getRepairOrgID() {
        return repairOrgID;
    }
    
    public void setRepairOrgID(String repairOrgID) {
        this.repairOrgID = repairOrgID;
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
    
    public Long getCreator() {
        return creator;
    }
    
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    public Long getUpdator() {
        return updator;
    }
    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getOrgname() {
        return orgname;
    }
    
    public void setOrgname(String orgname) {
        this.orgname = orgname;
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
    
    public String getOrgseq() {
        return orgseq;
    }
    
    public void setOrgseq(String orgseq) {
        this.orgseq = orgseq;
    }
    
    public String getIsHgys() {
        return isHgys;
    }
    
    public void setIsHgys(String isHgys) {
        this.isHgys = isHgys;
    }
    
}
