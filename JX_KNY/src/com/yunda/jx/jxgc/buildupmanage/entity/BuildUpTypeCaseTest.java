package com.yunda.jx.jxgc.buildupmanage.entity;

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
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 组成实例测试表--业务实体
 * <li>创建人：程锐
 * <li>创建日期：2013-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_BUILDUP_TYPE_CASE_TEST")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class BuildUpTypeCaseTest implements java.io.Serializable {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 所在组成型号 */
	@Column(name="F_BUILDUP_TYPE_IDX")
	private String fBuildUpTypeIDX;
	/* 上级组成型号 */
	@Column(name="P_BUILDUP_TYPE_IDX")
	private String pBuildUpTypeIDX;
	/* 组成位置主键 */
	@Column(name="BUILDUPPLACE_IDX")
	private String buildUpPlaceIdx;
	/* 所安的组成型号 */
	@Column(name="BUILDUP_TYPE_IDX")
	private String buildUpTypeIdx;
	/* 组成位置编码全名 */
	@Column(name="BUILDUPPLACE_FULLCODE")
	private String buildUpPlaceFullCode;
	/* 组成位置名称全名 */
	@Column(name="BUILDUPPLACE_FULLNAME")
	private String buildUpPlaceFullName;
	/* 位置类型：Virtual：虚拟组成；True：真实组成 */
	@Column(name="BUILDUP_CLASS")
	private String buildUpClass;
	/* 是否第一阶真实配件：Y为是，N为否 */
	@Column(name="IS_FIRST_LEVEL")
	private String isFirstLevel;
	/* 是否最新版本：Y为最新版本，N为非最新版本 */
	@Column(name="ISLASTVSERSION")
	private String isLastVsersion;
	
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
	
	/* 组成型号编码 */
	@Transient
    private String buildUpTypeCode;
    
    /* 组成型号名称 */
	@Transient
    private String buildUpTypeName;
    
    /* 组成型号描述 */
	@Transient
    private String buildUpTypeDesc;
    /* 图号*/
	@Transient
    private String chartNo;
	
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
	public String getBuildUpClass() {
		return buildUpClass;
	}
	public void setBuildUpClass(String buildUpClass) {
		this.buildUpClass = buildUpClass;
	}
	public String getBuildUpPlaceFullCode() {
		return buildUpPlaceFullCode;
	}
	public void setBuildUpPlaceFullCode(String buildUpPlaceFullCode) {
		this.buildUpPlaceFullCode = buildUpPlaceFullCode;
	}
	public String getBuildUpPlaceFullName() {
		return buildUpPlaceFullName;
	}
	public void setBuildUpPlaceFullName(String buildUpPlaceFullName) {
		this.buildUpPlaceFullName = buildUpPlaceFullName;
	}
	public String getBuildUpPlaceIdx() {
		return buildUpPlaceIdx;
	}
	public void setBuildUpPlaceIdx(String buildUpPlaceIdx) {
		this.buildUpPlaceIdx = buildUpPlaceIdx;
	}
	public String getBuildUpTypeIdx() {
		return buildUpTypeIdx;
	}
	public void setBuildUpTypeIdx(String buildUpTypeIdx) {
		this.buildUpTypeIdx = buildUpTypeIdx;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public String getFBuildUpTypeIDX() {
		return fBuildUpTypeIDX;
	}
	public void setFBuildUpTypeIDX(String buildUpTypeIDX) {
		fBuildUpTypeIDX = buildUpTypeIDX;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getIsFirstLevel() {
		return isFirstLevel;
	}
	public void setIsFirstLevel(String isFirstLevel) {
		this.isFirstLevel = isFirstLevel;
	}
	public String getIsLastVsersion() {
		return isLastVsersion;
	}
	public void setIsLastVsersion(String isLastVsersion) {
		this.isLastVsersion = isLastVsersion;
	}
	public String getPBuildUpTypeIDX() {
		return pBuildUpTypeIDX;
	}
	public void setPBuildUpTypeIDX(String buildUpTypeIDX) {
		pBuildUpTypeIDX = buildUpTypeIDX;
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
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUpdator() {
		return updator;
	}
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	
	
}
