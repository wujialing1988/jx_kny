package com.yunda.jx.pjwz.partsBase.outsourcinglist.entity;

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

@Entity
@Table(name="PJWZ_Parts_Outsourcing_List")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsOutsourcingList implements java.io.Serializable{    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
    private String idx;
    /* 配件型号主键 */
    @Column(name="Parts_Type_IDX")    
    private String partsTypeIDX;
    /* 委外厂家主键 */
    @Column(name="Made_Factory_ID")
    private String madeFactoryID;
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
    
    /* 瞬时字段 */
    /* 委修厂名称 */
    @Transient
    private String factoryName;
    /* 配件名称 */
    @Transient
    private String partsName;
    /* 规格型号 */
    @Transient
    private String specificationModel;


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
    
    public String getMadeFactoryID() {
        return madeFactoryID;
    }
    
    public void setMadeFactoryID(String madeFactoryID) {
        this.madeFactoryID = madeFactoryID;
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

  


	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
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
}
