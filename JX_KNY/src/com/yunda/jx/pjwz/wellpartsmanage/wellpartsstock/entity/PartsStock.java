package com.yunda.jx.pjwz.wellpartsmanage.wellpartsstock.entity;

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
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 良好配件库存信息
 * <li>创建人：程梅
 * <li>创建日期：2014-5-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJWZ_PARTS_STOCK")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsStock implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 配件信息主键*/
	@Column(name="PARTS_ACCOUNT_IDX")
	private String partsAccountIDX;
	/* 库房主键 */
	@Column(name="WH_IDX")
	private String whIdx;
	/* 库房名称*/
	@Column(name="WH_NAME")
	private String whName;
	/* 库位主键 */
	@Column(name="WH_LOCATION_IDX")
	private String whLocationIDX;
	/* 库位名称*/
	@Column(name="WH_LOCATION_NAME")
	private String whLocationName;
	/* 交件人主键 */
	@Column(name="HAND_OVER_EMP_ID")
	private Long handOverEmpId;
	/* 交件人名称 */
	@Column(name="HAND_OVER_EMP")
	private String handOverEmp;
	/* 交件部门主键*/
	@Column(name="HAND_OVER_ORG_ID")
	private Long handOverOrgId;
	/* 交件部门名称 */
	@Column(name="HAND_OVER_ORG")
	private String handOverOrg;
	/* 入库日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="WH_DATE")
	private java.util.Date whDate;
	/* 入库接收人主键 */
	@Column(name="TAKE_OVER_EMP_ID")
	private Long takeOverEmpId;
	/* 入库接收人名称 */
	@Column(name="TAKE_OVER_EMP")
	private String takeOverEmp;
	/* 出库领件人主键 */
	@Column(name="GET_EMP_id")
	private Long getEmpId;
	/* 出库领件人名称 */
	@Column(name="GET_EMP")
	private String getEmp;
	/* 领件部门主键*/
	@Column(name="GET_ORG_ID")
	private Long getOrgId;
	/* 领件部门名称 */
	@Column(name="GET_ORG")
	private String getOrg;
	/* 出库日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EX_WH_DATE")
	private java.util.Date exWhDate;
	/* 出库人主键 */
	@Column(name="EX_WH_EMP_id")
	private Long exWhEmpId;
	/* 出库人名称 */
	@Column(name="EX_WH_EMP")
	private String exWhEmp;
	/* 记录状态；0未删除；1删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 站点标识，为后期数据同步用 */
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
	public String getHandOverEmp() {
		return handOverEmp;
	}
	public void setHandOverEmp(String handOverEmp) {
		this.handOverEmp = handOverEmp;
	}
	public Long getHandOverEmpId() {
		return handOverEmpId;
	}
	public void setHandOverEmpId(Long handOverEmpId) {
		this.handOverEmpId = handOverEmpId;
	}
	public String getHandOverOrg() {
		return handOverOrg;
	}
	public void setHandOverOrg(String handOverOrg) {
		this.handOverOrg = handOverOrg;
	}
	public Long getHandOverOrgId() {
		return handOverOrgId;
	}
	public void setHandOverOrgId(Long handOverOrgId) {
		this.handOverOrgId = handOverOrgId;
	}
	public String getTakeOverEmp() {
		return takeOverEmp;
	}
	public void setTakeOverEmp(String takeOverEmp) {
		this.takeOverEmp = takeOverEmp;
	}
	public Long getTakeOverEmpId() {
		return takeOverEmpId;
	}
	public void setTakeOverEmpId(Long takeOverEmpId) {
		this.takeOverEmpId = takeOverEmpId;
	}
	public String getWhIdx() {
		return whIdx;
	}
	public void setWhIdx(String whIdx) {
		this.whIdx = whIdx;
	}
	public String getWhName() {
		return whName;
	}
	public void setWhName(String whName) {
		this.whName = whName;
	}
	public java.util.Date getExWhDate() {
		return exWhDate;
	}
	public void setExWhDate(java.util.Date exWhDate) {
		this.exWhDate = exWhDate;
	}
	public String getExWhEmp() {
		return exWhEmp;
	}
	public void setExWhEmp(String exWhEmp) {
		this.exWhEmp = exWhEmp;
	}
	public Long getExWhEmpId() {
		return exWhEmpId;
	}
	public void setExWhEmpId(Long exWhEmpId) {
		this.exWhEmpId = exWhEmpId;
	}
	public String getGetEmp() {
		return getEmp;
	}
	public void setGetEmp(String getEmp) {
		this.getEmp = getEmp;
	}
	public Long getGetEmpId() {
		return getEmpId;
	}
	public void setGetEmpId(Long getEmpId) {
		this.getEmpId = getEmpId;
	}
	public String getGetOrg() {
		return getOrg;
	}
	public void setGetOrg(String getOrg) {
		this.getOrg = getOrg;
	}
	public Long getGetOrgId() {
		return getOrgId;
	}
	public void setGetOrgId(Long getOrgId) {
		this.getOrgId = getOrgId;
	}
	public String getPartsAccountIDX() {
		return partsAccountIDX;
	}
	public void setPartsAccountIDX(String partsAccountIDX) {
		this.partsAccountIDX = partsAccountIDX;
	}
	public java.util.Date getWhDate() {
		return whDate;
	}
	public void setWhDate(java.util.Date whDate) {
		this.whDate = whDate;
	}
	public String getWhLocationIDX() {
		return whLocationIDX;
	}
	public void setWhLocationIDX(String whLocationIDX) {
		this.whLocationIDX = whLocationIDX;
	}
	public String getWhLocationName() {
		return whLocationName;
	}
	public void setWhLocationName(String whLocationName) {
		this.whLocationName = whLocationName;
	}
}