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
 * <li>说明：RcClassToParts实体类, 数据表：修程配件分类对应车型规格型号
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
@Table(name="PJWZ_RcClass_To_Parts_TYPE")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class RcClassToParts implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 修程编码 */
	@Column(name="Repair_Class_IDX")
	private String repairClassIDX;
	/* 修程名称 */
	@Column(name="Repair_Class_Name")
	private String repairClassName;
	/* 车型对应规格型号 */
	@Column(name="TrainType_To_Parts_IDX")
	private String trainTypeToPartsIDX;
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
	@Transient
	private Integer standardQty;
	@Transient
	private String trainTypeIDX;//车型主键
	@Transient
	private String trainTypeShortName;//车型名称
	@Transient
	private String partsTypeIDX;//配件型号表主键
	
	public RcClassToParts(){
		
	}
	
	public RcClassToParts(String idx,String trainTypeIDX,String trainTypeShortName, String partsTypeIDX,String repairClassIDX, String repairClassName, String trainTypeToPartsIDX, String matCode, String partsName, String specificationModel, String specificationModelCode, String unit, Integer standardQty) {
		this.idx = idx;
		this.repairClassIDX = repairClassIDX;
		this.repairClassName = repairClassName;
		this.trainTypeIDX=trainTypeIDX;
		this.trainTypeShortName=trainTypeShortName;
		this.partsTypeIDX=partsTypeIDX;
		this.trainTypeToPartsIDX = trainTypeToPartsIDX;
		this.matCode = matCode;
		this.partsName = partsName;
		this.specificationModel = specificationModel;
		this.specificationModelCode = specificationModelCode;
		this.unit = unit;
		this.standardQty = standardQty;
	}
	/**
	 * @return String 获取修程主键
	 */
	public String getRepairClassIDX(){
		return repairClassIDX;
	}
	/**
	 * @param 设置修程主键
	 */
	public void setRepairClassIDX(String repairClassIDX) {
		this.repairClassIDX = repairClassIDX;
	}
	/**
	 * @return String 获取修程名称
	 */
	public String getRepairClassName(){
		return repairClassName;
	}
	/**
	 * @param 设置修程名称
	 */
	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}
	/**
	 * @return String 获取车型对应规格型号
	 */
	public String getTrainTypeToPartsIDX(){
		return trainTypeToPartsIDX;
	}
	/**
	 * @param 设置车型对应规格型号
	 */
	public void setTrainTypeToPartsIDX(String trainTypeToPartsIDX) {
		this.trainTypeToPartsIDX = trainTypeToPartsIDX;
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
	public Integer getStandardQty() {
		return standardQty;
	}
	public void setStandardQty(Integer standardQty) {
		this.standardQty = standardQty;
	}

	public String getPartsTypeIDX() {
		return partsTypeIDX;
	}

	public void setPartsTypeIDX(String partsTypeIDX) {
		this.partsTypeIDX = partsTypeIDX;
	}

	public String getTrainTypeIDX() {
		return trainTypeIDX;
	}

	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}

	public String getTrainTypeShortName() {
		return trainTypeShortName;
	}

	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	
}