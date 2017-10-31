package com.yunda.jx.pjjx.partsrdp.tecinst.entity;

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
 * <li>说明：PartsRdpTec实体类, 数据表：配件检修工艺
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJJX_Parts_Rdp_Tec")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpTec implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 作业主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
	/* 工艺主键 */
	@Column(name="Tec_IDX")
	private String tecIDX;
	/* 工艺编号 */
	@Column(name="Tec_No")
	private String tecNo;
	/* 工艺名称 */
	@Column(name="Tec_Name")
	private String tecName;
	/* 工艺描述 */
	@Column(name="Tec_Desc")
	private String tecDesc;
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
	 * @return String 获取作业主键
	 */
	public String getRdpIDX(){
		return rdpIDX;
	}
	/**
	 * @param rdpIDX 设置作业主键
	 */
	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
	}
	/**
	 * @return String 获取工艺主键
	 */
	public String getTecIDX(){
		return tecIDX;
	}
	/**
	 * @param tecIDX 设置工艺主键
	 */
	public void setTecIDX(String tecIDX) {
		this.tecIDX = tecIDX;
	}
	/**
	 * @return String 获取工艺编号
	 */
	public String getTecNo(){
		return tecNo;
	}
	/**
	 * @param tecNo 设置工艺编号
	 */
	public void setTecNo(String tecNo) {
		this.tecNo = tecNo;
	}
	/**
	 * @return String 获取工艺名称
	 */
	public String getTecName(){
		return tecName;
	}
	/**
	 * @param tecName 设置工艺名称
	 */
	public void setTecName(String tecName) {
		this.tecName = tecName;
	}
	/**
	 * @return String 获取工艺描述
	 */
	public String getTecDesc(){
		return tecDesc;
	}
	/**
	 * @param tecDesc 设置工艺描述
	 */
	public void setTecDesc(String tecDesc) {
		this.tecDesc = tecDesc;
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
}