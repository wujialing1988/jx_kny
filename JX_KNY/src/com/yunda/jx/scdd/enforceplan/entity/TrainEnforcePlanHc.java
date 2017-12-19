package com.yunda.jx.scdd.enforceplan.entity;

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
 * <li>说明：TrainEnforcePlanHc实体类, 数据表：货车检修月计划实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-12-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="SCDD_TRAIN_ENFORCE_PLAN_HC")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TrainEnforcePlanHc implements java.io.Serializable{
    
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	
	/* 计划年份 */
	@Column(name="PLAN_YEAR")
	private String planYear;
	
	/* 计划月份 */
	@Column(name="PLAN_MORTH")
	private String planMorth;
	
	/* 编制人 */
	@Column(name="PLAN_PERSON")
	private Long planPerson;
	
	/* 编制人名称 */
	@Column(name="PLAN_PERSON_NAME")
	private String planPersonName;
	
	/* 编制日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PLAN_TIME")
	private java.util.Date planTime;
	
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	
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
	
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getPlanYear() {
		return planYear;
	}

	public void setPlanYear(String planYear) {
		this.planYear = planYear;
	}

	public String getPlanMorth() {
		return planMorth;
	}

	public void setPlanMorth(String planMorth) {
		this.planMorth = planMorth;
	}

	public Long getPlanPerson() {
		return planPerson;
	}

	public void setPlanPerson(Long planPerson) {
		this.planPerson = planPerson;
	}

	public String getPlanPersonName() {
		return planPersonName;
	}

	public void setPlanPersonName(String planPersonName) {
		this.planPersonName = planPersonName;
	}

	public java.util.Date getPlanTime() {
		return planTime;
	}

	public void setPlanTime(java.util.Date planTime) {
		this.planTime = planTime;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
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

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	
	
}