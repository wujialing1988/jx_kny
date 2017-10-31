package com.yunda.jx.pjwz.partsBase.partsquota.entity;

import java.util.Date;

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
 * <li>说明：PartsQuota实体类, 数据表：互换配件定额
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJWZ_Parts_Quota")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsQuota implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 配件型号表主键 */
	@Column(name="Parts_TYPE_IDX")
	private String partsTypeIDX;
	/* 配件名称 */
	@Column(name="Parts_Name")
	private String partsName;
	/* 规格型号 */
	@Column(name="Specification_Model")
	private String specificationModel;
	/* 计量单位 */
	private String unit;
	/* 所属单位，某段或某基地 */
	@Column(name="Owner_Unit")
	private Long ownerUnit;
	/* 所属单位名称 */
	@Column(name="Owner_Unit_Name")
	private String ownerUnitName;
	/* 定额 */
	@Column(name="LIMIT_Quantity")
	private Integer limitQuantity;
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

    public PartsQuota(){
        super();
    }
    /**
     * 
     * <li>说明：互换配件定量列表
     * <li>创建人：程梅
     * <li>创建日期：2016-8-24
     * <li>修改人： 
     * <li>修改日期：
     * @param idx
     * @param partsTypeIDX
     * @param partsName
     * @param specificationModel
     * @param unit
     * @param ownerUnit
     * @param ownerUnitName
     * @param limitQuantity
     * @param operatorname
     */
    public PartsQuota(String idx,String partsTypeIDX,String partsName,String specificationModel,String unit,Long ownerUnit,
        String ownerUnitName,Integer limitQuantity,Date updateTime,String operatorname){
        this.idx = idx;
        this.partsTypeIDX = partsTypeIDX;
        this.partsName = partsName;
        this.specificationModel = specificationModel;
        this.unit = unit;
        this.ownerUnit = ownerUnit;
        this.ownerUnitName = operatorname;
        this.limitQuantity = limitQuantity;
        this.updateTime = updateTime;
        this.updatorName = operatorname ;
    }
    /* 修改人名称*/
    @Transient
    private String updatorName ;
	/**
	 * @return String 获取配件型号表主键
	 */
	public String getPartsTypeIDX(){
		return partsTypeIDX;
	}
	/**
	 * @param 设置配件型号表主键
	 */
	public void setPartsTypeIDX(String partsTypeIDX) {
		this.partsTypeIDX = partsTypeIDX;
	}
	/**
	 * @return String 获取配件名称
	 */
	public String getPartsName(){
		return partsName;
	}
	/**
	 * @param 设置配件名称
	 */
	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}
	/**
	 * @return String 获取规格型号
	 */
	public String getSpecificationModel(){
		return specificationModel;
	}
	/**
	 * @param 设置规格型号
	 */
	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel;
	}
	/**
	 * @return Long 获取所属单位
	 */
	public Long getOwnerUnit(){
		return ownerUnit;
	}
	/**
	 * @param 设置所属单位
	 */
	public void setOwnerUnit(Long ownerUnit) {
		this.ownerUnit = ownerUnit;
	}
	/**
	 * @return String 获取所属单位名称
	 */
	public String getOwnerUnitName(){
		return ownerUnitName;
	}
	/**
	 * @param 设置所属单位名称
	 */
	public void setOwnerUnitName(String ownerUnitName) {
		this.ownerUnitName = ownerUnitName;
	}
	/**
	 * @return Integer 获取定额
	 */
	public Integer getLimitQuantity(){
		return limitQuantity;
	}
	/**
	 * @param 设置定额
	 */
	public void setLimitQuantity(Integer limitQuantity) {
		this.limitQuantity = limitQuantity;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
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
	public String getSiteID(){
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
	public Long getCreator(){
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
	public java.util.Date getCreateTime(){
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
	public Long getUpdator(){
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
	public java.util.Date getUpdateTime(){
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
    
    public String getUpdatorName() {
        return updatorName;
    }
    
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }
}