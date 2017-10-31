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
 * <li>说明：BuildUpPlace实体类, 数据表：组成位置
 * <li>创建人：程锐
 * <li>创建日期：2013-01-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_BUILDUP_PLACE")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class BuildUpPlace implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /** 结构位置类型 */
    public static final int TYPE_STRUCTURE = 10;
    /** 配件位置类型 */
    public static final int TYPE_FIX = 20;
    /** 虚拟位置类型 */
    public static final int TYPE_VIRTUAL = 30;

	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
    /* 车型主键 */
    @Column(name="TRAIN_TYPE_IDX")
    private String trainTypeIDX;
    /* 配件规格型号主键 */
    @Column(name = "Parts_Type_IDX")
    private String partsTypeIDX;
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