package com.yunda.jx.pjjx.partsrdp.recordinst.entity;

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
 * <li>说明：PartsRdpRecord实体类, 数据表：配件检修记录单实例
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
@Table(name="PJJX_Parts_Rdp_Record")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpRecord implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 作业主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
	/* 记录单主键 */
	@Column(name="Record_IDX")
	private String recordIDX;
	/* 记录单编号 */
	@Column(name="Record_No")
	private String recordNo;
	/* 记录单名称 */
	@Column(name="Record_Name")
	private String recordName;
	/* 记录单描述 */
	@Column(name="Record_Desc")
	private String recordDesc;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 报表主键 */
	@Column(name="ReportTmpl_Manage_IDX")
	private String reportTmplManageIDX;
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
	 * @return String 获取记录单主键
	 */
	public String getRecordIDX(){
		return recordIDX;
	}
	/**
	 * @param recordIDX 设置记录单主键
	 */
	public void setRecordIDX(String recordIDX) {
		this.recordIDX = recordIDX;
	}
	/**
	 * @return String 获取记录单编号
	 */
	public String getRecordNo(){
		return recordNo;
	}
	/**
	 * @param recordNo 设置记录单编号
	 */
	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}
	/**
	 * @return String 获取记录单名称
	 */
	public String getRecordName(){
		return recordName;
	}
	/**
	 * @param recordName 设置记录单名称
	 */
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}
	/**
	 * @return String 获取记录单描述
	 */
	public String getRecordDesc(){
		return recordDesc;
	}
	/**
	 * @param recordDesc 设置记录单描述
	 */
	public void setRecordDesc(String recordDesc) {
		this.recordDesc = recordDesc;
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
	 * @return String 获取报表主键
	 */
	public String getReportTmplManageIDX(){
		return reportTmplManageIDX;
	}
	/**
	 * @param reportTmplManageIDX 设置报表主键
	 */
	public void setReportTmplManageIDX(String reportTmplManageIDX) {
		this.reportTmplManageIDX = reportTmplManageIDX;
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