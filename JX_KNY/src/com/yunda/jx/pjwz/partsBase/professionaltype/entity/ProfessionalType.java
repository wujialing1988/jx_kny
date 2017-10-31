package com.yunda.jx.pjwz.partsBase.professionaltype.entity;

import java.util.Date;

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
 * <li>标题: 机车检修管理信息系统</li>
 * <li>说明: ProfessionalType实体类,数据表：专业类型</li>
 * <li>版权: Copyright (c) 2008 运达科技公司</li>
 * <li>创建日期：
 * <li>修改人: </li>
 * <li>修改日期：</li>
 * <li>修改内容：</li>
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Entity
@Table(name="PJWZ_Professional_Type")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ProfessionalType implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	/** 状态为 新增*/
    public static final int status_add = 0;
    /** 状态为 启用*/
    public static final int status_start = 1;
    /** 状态为 作废*/
    public static final int status_invalid = 2;
	/**
	 * idx主键 
	 * 
	 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/**
	 *  类型编码
	 */
	@Column(name="Professional_Type_ID")
	private String professionalTypeID;
	/**
	 * 类型名称
	 */
	@Column(name="Professional_Type_Name")
	private String professionalTypeName;
	/**
	 *  上级专业类型主键
	 */
	@Column(name="Parent_IDX")
	private String parentIDX;
	/**
	 * 专业类型序列
	 */
	@Column(name="PROSEQ")
	private String proSeq;
	/**
	 * 业务状态，0：新增；1：启用；2：作废
	 */
	private Integer status;
	/**
	 * 表示此条记录的状态：0为表示未删除；1表示删除
	 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/**
	 * 站点标识，为了同步数据而使用
	 */
	@Column(updatable = false)
	private String siteID;
	/**
	 *  创建人
	 */
	@Column(updatable = false)
	private Long creator;
	/**
	 *  创建时间 
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time" , updatable = false)
	private Date createTime;
	/**
	 *修改人
	 */
	private Long updator;
	@Column(name="IS_LAST_LEVEL")
	private String isLeaf;
	/**
	 * 
	 * 修改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private Date updateTime;
	
	public ProfessionalType(String idx,String professionalTypeID,String professionalTypeName,String parentIDX,int status,int recordStatus,
	String siteID,Long creator,Date createTime,Long updattor,Date updateTime){	
		this.idx=idx;
		this.professionalTypeID=professionalTypeID;
		this.professionalTypeName=professionalTypeName;
		this.parentIDX=parentIDX;
		this.status=status;
		this.recordStatus=recordStatus;
		this.siteID=siteID;
		this.creator=creator;
		this.createTime=createTime;
		this.updator=updattor;
		this.updateTime=updateTime;
	}
	
	

	public ProfessionalType() {
		this.status=0;
		this.recordStatus=0;
	}

	/**
	 * @return String 获取类型编码
	 */
	public String getProfessionalTypeID(){
		return professionalTypeID;
	}
	/**
	 * @param 设置类型编码
	 */
	public void setProfessionalTypeID(String professionalTypeID) {
		this.professionalTypeID = professionalTypeID;
	}	
	/**
	 * @return String 获取类型名称
	 */
	public String getProfessionalTypeName(){
		return professionalTypeName;
	}
	/**
	 * @param 设置类型名称
	 */
	public void setProfessionalTypeName(String professionalTypeName) {
		this.professionalTypeName = professionalTypeName;
	}	
	/**
	 * @return String 获取上级专业类型主键
	 */
	public String getParentIDX(){
		return parentIDX;
	}
	/**
	 * @param 设置上级专业类型主键
	 */
	public void setParentIDX(String parentIDX) {
		this.parentIDX = parentIDX;
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



	public String getIsLeaf() {
		return isLeaf;
	}



	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}



	public String getProSeq() {
		return proSeq;
	}



	public void setProSeq(String proSeq) {
		this.proSeq = proSeq;
	}
}