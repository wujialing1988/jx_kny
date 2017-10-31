package com.yunda.frame.report.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：FileObject实体类, 数据表：报表文件对象
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
@Table(name="R_File_Object")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class FileObject implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/** 当前标识 - 最新版本为：T */
	public static final String CONST_STR_CURRENT_FLAG_T = ReportConstants.CONST_STR_T;
	/** 当前标识 - 非最新版本为：F */
	public static final String CONST_STR_CURRENT_FLAG_F = ReportConstants.CONST_STR_F;
	
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 报表打印模板主键 */
	@Column(name="Printer_Module_IDX")
	private String printerModuleIDX;
	
	/* 文件对象 */
	@Lob
	@Basic(fetch=FetchType.EAGER)
	@Column(name="File_Object",columnDefinition="MEDIUMBLOB NOT NULL")
	@JsonIgnore
	private java.sql.Blob fileObject;
	/* 文件大小 */
	@Column(name="File_Length")
	private Long fileLength;
	/* 文件上传路径 */
	@Column(name="File_Upload_Path")
	private String fileUploadPath;
	/* 版本号 */
	private String version;
	/* 文件名称 */
	@Column(name="File_Name")
	private String fileName;
	/* 文件描述 */
	@Column(name="File_Desc")
	private String fileDesc;
	/* 当前标识，最新版本为T，非最新为F */
	@Column(name="Current_Flag")
	private String currentFlag;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;
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
	 * @return Integer 获取文件大小
	 */
	public Long getFileLength(){
		return fileLength;
	}
	/**
	 * @param fileLength 设置文件大小
	 */
	public void setFileLength(Long fileLength) {
		this.fileLength = fileLength;
	}
	/**
	 * @return String 获取文件上传路径
	 */
	public String getFileUploadPath(){
		return fileUploadPath;
	}
	/**
	 * @param fileUploadPath 设置文件上传路径
	 */
	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}
	/**
	 * @return String 获取版本号
	 */
	public String getVersion(){
		return version;
	}
	/**
	 * @param version 设置版本号
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * @return 获取当前标识
	 */
	public String getCurrentFlag() {
		return currentFlag;
	}
	/**
	 * @param currentFlag 设置当前标识
	 */
	public void setCurrentFlag(String currentFlag) {
		this.currentFlag = currentFlag;
	}
	/**
	 * @return 获取文件名称
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName 设置文件名称
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return 获取文件描述
	 */
	public String getFileDesc() {
		return fileDesc;
	}
	/**
	 * @param fileDesc 设置文件描述
	 */
	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}
	/**
	 * @return String 获取报表打印模板主键
	 */
	public String getPrinterModuleIDX(){
		return printerModuleIDX;
	}
	/**
	 * @param printerModuleIDX 设置报表打印模板主键
	 */
	public void setPrinterModuleIDX(String printerModuleIDX) {
		this.printerModuleIDX = printerModuleIDX;
	}
	/**
	 * @return java.sql.Blob 获取文件对象
	 */
	public java.sql.Blob getFileObject(){
		return fileObject;
	}
	/**
	 * @param fileObject 设置文件对象
	 */
	public void setFileObject(java.sql.Blob fileObject) {
		this.fileObject = fileObject;
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
}