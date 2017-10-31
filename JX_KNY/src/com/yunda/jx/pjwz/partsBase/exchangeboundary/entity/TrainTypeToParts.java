package com.yunda.jx.pjwz.partsBase.exchangeboundary.entity;

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
 * <li>说明：TrainTypeToParts实体类, 数据表：车型对应规格型号
 * <li>创建人：王治龙
 * <li>创建日期：2013-05-23
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJWZ_TrainType_To_Parts_TYPE")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TrainTypeToParts implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 车型主键 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	/* 车型简称 */
	@Column(name="Train_Type_ShortName")
	private String trainTypeShortName;
	/* 配件型号表主键 */
	@Column(name="PARTS_TYPE_IDX")
	private String partsTypeIDX;
	/* 标准数量 */
	@Column(name="standard_qty")
	private Integer standardQty;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="RECORD_STATUS")
	private Integer recordStatus;
	/* 站点标示，为了同步数据而使用 */
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

	 /* 物料编码 */
	@Transient
    private String matCode;
	/* 配件名称 */
	@Transient
	private String partsName;
	/* 规格型号*/
	@Transient
	private String specificationModel;
    /* 规格型号编码*/
	@Transient
    private String specificationModelCode;
    /* 计量单位*/
	@Transient
	private String unit;
    /* 已登记数量*/
    @Transient
    private Integer checkQty ;
    /* 未登记数量*/
    @Transient
    private Integer unCheckQty;
	
	public TrainTypeToParts(){
		
	}
	
	public TrainTypeToParts(String idx, String trainTypeIDX, String trainTypeShortName, String partsTypeIDX, 
			Integer standardQty, String matCode, String partsName, String specificationModel, String specificationModelCode, String unit) {
		this.idx = idx;
		this.trainTypeIDX = trainTypeIDX;
		this.trainTypeShortName = trainTypeShortName;
		this.partsTypeIDX = partsTypeIDX;
		this.standardQty = standardQty;
		this.matCode = matCode;
		this.partsName = partsName;
		this.specificationModel = specificationModel;
		this.specificationModelCode = specificationModelCode;
		this.unit = unit;
	}
	/*
	 * 修程及分类
	 */
	public String getMatCode() {
		return matCode;
	}
	public void setMatCode(String matCode) {
		this.matCode = matCode;
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
	public String getSpecificationModelCode() {
		return specificationModelCode;
	}
	public void setSpecificationModelCode(String specificationModelCode) {
		this.specificationModelCode = specificationModelCode;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return String 获取车型主键
	 */
	public String getTrainTypeIDX(){
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
	public String getTrainTypeShortName(){
		return trainTypeShortName;
	}
	/**
	 * @param 设置车型简称
	 */
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
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
	 * @return Double 获取标准数量
	 */
	public Integer getStandardQty(){
		return standardQty;
	}
	/**
	 * @param 设置标准数量
	 */
	public void setStandardQty(Integer standardQty) {
		this.standardQty = standardQty;
	}
	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param 设置记录的状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * @return String 获取站点标示
	 */
	public String getSiteID(){
		return siteID;
	}
	/**
	 * @param 设置站点标示
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

    
    public Integer getCheckQty() {
        return checkQty;
    }

    
    public void setCheckQty(Integer checkQty) {
        this.checkQty = checkQty;
    }

    
    public Integer getUnCheckQty() {
        return unCheckQty;
    }

    
    public void setUnCheckQty(Integer unCheckQty) {
        this.unCheckQty = unCheckQty;
    }
}