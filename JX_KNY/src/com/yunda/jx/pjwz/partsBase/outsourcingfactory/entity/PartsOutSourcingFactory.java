package com.yunda.jx.pjwz.partsBase.outsourcingfactory.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 配件委外厂家
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name = "PJWZ_PARTS_OUTSOURCING_FACTORY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsOutSourcingFactory implements Serializable {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/* idx主键 委外厂家编码 */
	@Id
	private String id;

	/* 委外厂家名称 */
	@Column(name = "FACTORY_NAME")
	private String factoryName;

	/* 委外厂家简称 */
	@Column(name = "FACTORY_SHORTNAME")
	private String factoryShortname;

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

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getFactoryShortname() {
		return factoryShortname;
	}

	public void setFactoryShortname(String factoryShortname) {
		this.factoryShortname = factoryShortname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
