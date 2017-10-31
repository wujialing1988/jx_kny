package com.yunda.jxpz.temporaryemp.entity;

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
 * <li>说明：TemporaryEmp实体类, 数据表：临时人员
 * <li>创建人：程梅
 * <li>创建日期：2015-04-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXPZ_TEMPORARY_EMP")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TemporaryEmp implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 人员id */
	private Long empid;
	/* 人员代码 */
	private String empcode;
	/* 人员名称 */
	private String empname;
	/* 性别 */
	private String gender;
	/* 人员状态 */
	private String empstatus;
	/* 原所属车间 */
	@Column(name="old_plant")
	private String oldPlant;
	/* 原所属班组 */
	@Column(name="old_tream")
	private String oldTream;
	/* 临时班组id */
	@Column(name="temporary_tream_id")
	private String temporaryTreamId;
    /* 临时班组序列 */
    @Column(name="temporary_tream_seq")
    private String temporaryTreamSeq;
	/* 临时班组名称 */
	@Column(name="temporary_tream_name")
	private String temporaryTreamName;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="RECORD_STATUS")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
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

	/**
	 * @return Long 获取人员id
	 */
	public Long getEmpid(){
		return empid;
	}
	/**
	 * @param empid 设置人员id
	 */
	public void setEmpid(Long empid) {
		this.empid = empid;
	}
	/**
	 * @return String 获取人员代码
	 */
	public String getEmpcode(){
		return empcode;
	}
	/**
	 * @param empcode 设置人员代码
	 */
	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}
	/**
	 * @return String 获取人员名称
	 */
	public String getEmpname(){
		return empname;
	}
	/**
	 * @param empname 设置人员名称
	 */
	public void setEmpname(String empname) {
		this.empname = empname;
	}
	/**
	 * @return String 获取性别
	 */
	public String getGender(){
		return gender;
	}
	/**
	 * @param gender 设置性别
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return String 获取人员状态
	 */
	public String getEmpstatus(){
		return empstatus;
	}
	/**
	 * @param empstatus 设置人员状态
	 */
	public void setEmpstatus(String empstatus) {
		this.empstatus = empstatus;
	}
	/**
	 * @return String 获取原所属车间
	 */
	public String getOldPlant(){
		return oldPlant;
	}
	/**
	 * @param oldPlant 设置原所属车间
	 */
	public void setOldPlant(String oldPlant) {
		this.oldPlant = oldPlant;
	}
	/**
	 * @return String 获取原所属班组
	 */
	public String getOldTream(){
		return oldTream;
	}
	/**
	 * @param oldTream 设置原所属班组
	 */
	public void setOldTream(String oldTream) {
		this.oldTream = oldTream;
	}
	/**
	 * @return String 获取临时班组id
	 */
	public String getTemporaryTreamId(){
		return temporaryTreamId;
	}
	/**
	 * @param temporaryTreamId 设置临时班组id
	 */
	public void setTemporaryTreamId(String temporaryTreamId) {
		this.temporaryTreamId = temporaryTreamId;
	}
	/**
	 * @return String 获取临时班组名称
	 */
	public String getTemporaryTreamName(){
		return temporaryTreamName;
	}
	/**
	 * @param temporaryTreamName 设置临时班组名称
	 */
	public void setTemporaryTreamName(String temporaryTreamName) {
		this.temporaryTreamName = temporaryTreamName;
	}
	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param recordStatus 设置记录的状态
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
	 * @param siteID 设置站点标识
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
	 * @param creator 设置创建人
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
	 * @param createTime 设置创建时间
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
	 * @param updator 设置修改人
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
	 * @param updateTime 设置修改时间
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
	 * @param idx 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
    
    public String getTemporaryTreamSeq() {
        return temporaryTreamSeq;
    }
    
    public void setTemporaryTreamSeq(String temporaryTreamSeq) {
        this.temporaryTreamSeq = temporaryTreamSeq;
    }
}