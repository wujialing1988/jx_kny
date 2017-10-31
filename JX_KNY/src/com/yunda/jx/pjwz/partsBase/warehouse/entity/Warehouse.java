package com.yunda.jx.pjwz.partsBase.warehouse.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Warehouse实体类, 数据表：库房
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJWZ_Warehouse")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class Warehouse implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/**业务状态，0：新增；*/
	public static final int STATUS_ADD = 0 ;
	/**业务状态，1：启用；*/
	public static final int STATUS_USE = 1 ;
	/**业务状态，2：作废*/
	public static final int STATUS_INVALID = 2 ;
	
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 库房编码 */
	@Column(name="WareHouse_ID")
	private String wareHouseID;
	/* 库房名称 */
	@Column(name="WareHouse_Name")
	private String wareHouseName;
	/* 库房地点 */
	@Column(name="WareHouse_Address")
	private String wareHouseAddress;
	/* 所属部门的组织主键 */
	private Long orgID;
	/* 所属部门的组织代码 */
	private String orgCode;
	/* 所属部门的组织名称 */
	private String orgName;
	/* 业务状态，0：新增；1：启用；2：作废 */
	private Integer status;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 站点标示，为了同步数据而使用 */
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
	 * @return String 获取库房编码
	 */
	public String getWareHouseID(){
		return wareHouseID;
	}
	/**
	 * @param 设置库房编码
	 */
	public void setWareHouseID(String wareHouseID) {
		this.wareHouseID = wareHouseID;
	}
	/**
	 * @return String 获取库房名称
	 */
	public String getWareHouseName(){
		return wareHouseName;
	}
	/**
	 * @param 设置库房名称
	 */
	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}
	/**
	 * @return String 获取库房地点
	 */
	public String getWareHouseAddress(){
		return wareHouseAddress;
	}
	/**
	 * @param 设置库房地点
	 */
	public void setWareHouseAddress(String wareHouseAddress) {
		this.wareHouseAddress = wareHouseAddress;
	}
	/**
	 * @return Long 获取所属部门的组织主键
	 */
	public Long getOrgID(){
		return orgID;
	}
	/**
	 * @param 设置所属部门的组织主键
	 */
	public void setOrgID(Long orgID) {
		this.orgID = orgID;
	}
	/**
	 * @return String 获取所属部门的组织代码
	 */
	public String getOrgCode(){
		return orgCode;
	}
	/**
	 * @param 设置所属部门的组织代码
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	/**
	 * @return String 获取所属部门的组织名称
	 */
	public String getOrgName(){
		return orgName;
	}
	/**
	 * @param 设置所属部门的组织名称
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
}