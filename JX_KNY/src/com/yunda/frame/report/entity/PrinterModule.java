package com.yunda.frame.report.entity;

import java.io.File;

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
 * <li>说明：PrinterModule实体类, 数据表：报表打印模板
 * <li>创建人：何涛
 * <li>创建日期：2015-01-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="R_Printer_Module")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PrinterModule implements java.io.Serializable{
	
	/** 是否可编辑 - 是 */
	public static final String CONST_STR_EDITABLE_T = ReportConstants.CONST_STR_T;
	/** 是否可编辑 - 否 */
	public static final String CONST_STR_EDITABLE_F = ReportConstants.CONST_STR_F;
	
	/** 报表文件部署的根路径（相对路径） */
	// Modified by hetao on 2016-03-08 10:30 使用平台相关的文件路径分隔符，兼容非Windows系统
	public static final String CONST_STR_REPORT_DEPLOY_PATH =
        File.separator + "ydReport" + File.separator + "WEB-INF" + File.separator + "reportlets";
    
	/** 报表文件部署目录 */
	public static final String CONST_STR_REPORT_DEPLOY_FOLDER = "reportlets";
	
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 一旦创建则不允许修改，或者：作统一修改 */
	private String identifier;
	/* 上级报表标主键 */
	@Column(name="Parent_IDX")
	private String parentIDX;
	/* 报表部署名称 */
	@Column(name="Deploy_Name")
	private String deployName;
	/* 报表部署目录 */
	@Column(name="Deploy_Catalog")
	private String deployCatalog;
	/* 是否可编辑；F：不可编辑，T：可编辑 */
	private String editable;
	/* 显示名称 */
	@Column(name="Display_Name")
	private String displayName;
	/* 报表描述 */
	@Column(name="Module_Desc")
	private String moduleDesc;
	/* 最近部署时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Latest_Deploy_Time")
	private java.util.Date latestDeployTime;
	/* 最近更新时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Latest_Update_Time")
	private java.util.Date latestUpdateTime;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/**
	 * @return 获取表示此条记录的状态
	 */
	public Integer getRecordStatus() {
		return recordStatus;
	}
	/**
	 * @param recordStatus 设置表示此条记录的状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	/**
	 * @return String 获取报表标识码
	 */
	public String getIdentifier(){
		return identifier;
	}
	/**
	 * @param identifier 设置报表标识码
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * @return 获取上级报表标主键
	 */
	public String getParentIDX() {
		return parentIDX;
	}
	/**
	 * @param parentIDX 设置上级报表标主键
	 */
	public void setParentIDX(String parentIDX) {
		this.parentIDX = parentIDX;
	}
	/**
	 * @return String 获取报表部署名称
	 */
	public String getDeployName(){
		return deployName;
	}
	/**
	 * @param deployName 设置报表部署名称
	 */
	public void setDeployName(String deployName) {
		this.deployName = deployName;
	}
	/**
	 * @return String 获取报表部署目录
	 */
	public String getDeployCatalog(){
		return deployCatalog;
	}
	/**
	 * @param deployCatalog 设置报表部署目录
	 */
	public void setDeployCatalog(String deployCatalog) {
		this.deployCatalog = deployCatalog;
	}
	/**
	 * @return String 获取是否可编辑
	 */
	public String getEditable(){
		return editable;
	}
	/**
	 * @param editable 设置是否可编辑
	 */
	public void setEditable(String editable) {
		this.editable = editable;
	}
	/**
	 * @return String 获取显示名称
	 */
	public String getDisplayName(){
		return displayName;
	}
	/**
	 * @param displayName 设置显示名称
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return 获取报表描述
	 */
	public String getModuleDesc() {
		return moduleDesc;
	}
	/**
	 * @param moduleDesc 设置报表描述
	 */
	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}
	/**
	 * @return java.util.Date 获取最近部署时间
	 */
	public java.util.Date getLatestDeployTime(){
		return latestDeployTime;
	}
	/**
	 * @param latestDeployTime 设置最近部署时间
	 */
	public void setLatestDeployTime(java.util.Date latestDeployTime) {
		this.latestDeployTime = latestDeployTime;
	}
	/**
	 * @return java.util.Date 获取最近更新时间
	 */
	public java.util.Date getLatestUpdateTime(){
		return latestUpdateTime;
	}
	/**
	 * @param latestUpdateTime 设置最近更新时间
	 */
	public void setLatestUpdateTime(java.util.Date latestUpdateTime) {
		this.latestUpdateTime = latestUpdateTime;
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