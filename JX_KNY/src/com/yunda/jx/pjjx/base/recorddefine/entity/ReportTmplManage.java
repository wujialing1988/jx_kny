package com.yunda.jx.pjjx.base.recorddefine.entity;

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
 * <li>说明：ReportTmplManage实体类, 数据表：记录单报表模板管理
 * <li>创建人：何涛
 * <li>创建日期：2014-11-14
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJJX_ReportTmpl_Manage")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ReportTmplManage implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 记录单主键 */
	@Column(name = "Qr_IDX")
	private String qrIDX;

	/** 记录单报表模板编码 */
	@Column(name = "Qr_ReportTmpl_Code")
	private String qrReportTmplCode;

	/** 记录单报表模板报表名称 */
	@Column(name = "Qr_ReportTmpl_Name")
	private String qrReportTmplName;

	/** 记录单报表模板报表路径 */
	@Column(name = "Qr_ReportTmpl_ReportPath")
	private String qrReportTmplReportPath;

	/** 记录单报表模板保存名称 */
	@Column(name = "Qr_ReportTmpl_UploadName")
	private String qrReportTmplUploadName;

	/** 记录单报表模板真实名称 */
	@Column(name = "Qr_ReportTmpl_RealName")
	private String qrReportTmplRealName;

	/** 记录单文件上传路径 */
	@Column(name = "Qr_ReportTmpl_UploadPath")
	private String qrReportTmplUploadPath;

	/** 记录单文件大小 */
	@Column(name = "Qr_Size")
	private Long qrSize;

	/** 文件类型，例如cpt */
	@Column(name = "File_Type")
	private String fileType;

	/** 是否最新版本：Y为最新版本，N为非最新版本，同一质量记录单模板编码只有一条最新版本 */
	private String isCurrentVersion;

	/** 版本号（同一质量记录单模板编码下的版本，初始为1，每次递增1） */
	private String versionNo;

	/** 上传人 */
	@Column(name = "Upload_Person")
	private Long uploadPerson;

	/** 上传人名称 */
	@Column(name = "Upload_Person_Name")
	private String uploadPersonName;

	/** 上传时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Upload_Time")
	private java.util.Date uploadTime;

	/** 是否已发布报表 是，否 */
	private String isPublish;

	/** 报表文件对象 */
	private java.sql.Blob reportFile;

	/** 模板类型：record为记录单报表、result为兑现后的记录单报表 */
	private String type;

	/** 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name = "Record_Status")
	private Integer recordStatus;

	/** 站点标识，为了同步数据而使用 */
	@Column(updatable = false)
	private String siteID;

	/** 创建人 */
	@Column(updatable = false)
	private Long creator;

	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Create_Time", updatable = false)
	private java.util.Date createTime;

	/** 修改人 */
	private Long updator;

	/** 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Update_Time")
	private java.util.Date updateTime;

	/**
	 * @return String 获取记录单主键
	 */
	public String getQrIDX() {
		return qrIDX;
	}

	/**
	 * @param qrIDX 设置记录单主键
	 */
	public void setQrIDX(String qrIDX) {
		this.qrIDX = qrIDX;
	}

	/**
	 * @return String 获取记录单报表模板编码
	 */
	public String getQrReportTmplCode() {
		return qrReportTmplCode;
	}

	/**
	 * @param qrReportTmplCode 设置记录单报表模板编码
	 */
	public void setQrReportTmplCode(String qrReportTmplCode) {
		this.qrReportTmplCode = qrReportTmplCode;
	}

	/**
	 * @return String 获取记录单报表模板名称
	 */
	public String getQrReportTmplName() {
		return qrReportTmplName;
	}

	/**
	 * @param qrReportTmplName 设置记录单报表模板名称
	 */
	public void setQrReportTmplName(String qrReportTmplName) {
		this.qrReportTmplName = qrReportTmplName;
	}

	/**
	 * @return String 获取记录单模板文件报表路径
	 */
	public String getQrReportTmplReportPath() {
		return qrReportTmplReportPath;
	}

	/**
	 * @param qrReportTmplReportPath 设置记录单模板文件报表路径
	 */
	public void setQrReportTmplReportPath(String qrReportTmplReportPath) {
		this.qrReportTmplReportPath = qrReportTmplReportPath;
	}

	/**
	 * @return String 获取记录单模板文件保存名称
	 */
	public String getQrReportTmplUploadName() {
		return qrReportTmplUploadName;
	}

	/**
	 * @param qrReportTmplUploadName 设置记录单模板文件保存名称
	 */
	public void setQrReportTmplUploadName(String qrReportTmplUploadName) {
		this.qrReportTmplUploadName = qrReportTmplUploadName;
	}

	/**
	 * @return String 获取记录单模板文件真实名称
	 */
	public String getQrReportTmplRealName() {
		return qrReportTmplRealName;
	}

	/**
	 * @param qrReportTmplRealName 设置记录单模板文件真实名称
	 */
	public void setQrReportTmplRealName(String qrReportTmplRealName) {
		this.qrReportTmplRealName = qrReportTmplRealName;
	}

	/**
	 * @return String 获取记录单模板文件上传路径
	 */
	public String getQrReportTmplUploadPath() {
		return qrReportTmplUploadPath;
	}

	/**
	 * @param qrReportTmplUploadPath 设置记录单模板文件上传路径
	 */
	public void setQrReportTmplUploadPath(String qrReportTmplUploadPath) {
		this.qrReportTmplUploadPath = qrReportTmplUploadPath;
	}

	/**
	 * @return Long 获取记录单模板文件大小
	 */
	public Long getQrSize() {
		return qrSize;
	}

	/**
	 * @param qrSize 设置记录单模板文件大小
	 */
	public void setQrSize(Long qrSize) {
		this.qrSize = qrSize;
	}

	/**
	 * @return String 获取记录单模板文件类型
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType 设置记录单模板文件类型
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return String 获取是否最新版本
	 */
	public String getIsCurrentVersion() {
		return isCurrentVersion;
	}

	/**
	 * @param isCurrentVersion 设置是否最新版本
	 */
	public void setIsCurrentVersion(String isCurrentVersion) {
		this.isCurrentVersion = isCurrentVersion;
	}

	/**
	 * @return String 获取版本号
	 */
	public String getVersionNo() {
		return versionNo;
	}

	/**
	 * @param versionNo 设置版本号
	 */
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	/**
	 * @return Long 获取上传人
	 */
	public Long getUploadPerson() {
		return uploadPerson;
	}

	/**
	 * @param uploadPerson 设置上传人
	 */
	public void setUploadPerson(Long uploadPerson) {
		this.uploadPerson = uploadPerson;
	}

	/**
	 * @return String 获取上传人名称
	 */
	public String getUploadPersonName() {
		return uploadPersonName;
	}

	/**
	 * @param uploadPersonName 设置上传人名称
	 */
	public void setUploadPersonName(String uploadPersonName) {
		this.uploadPersonName = uploadPersonName;
	}

	/**
	 * @return java.util.Date 获取上传时间
	 */
	public java.util.Date getUploadTime() {
		return uploadTime;
	}

	/**
	 * @param uploadTime 设置上传时间
	 */
	public void setUploadTime(java.util.Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	/**
	 * @return String 获取是否已发布报表
	 */
	public String getIsPublish() {
		return isPublish;
	}

	/**
	 * @param isPublish 设置是否已发布报表
	 */
	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}

	/**
	 * @return java.sql.Blob 获取报表文件对象
	 */
	public java.sql.Blob getReportFile() {
		return reportFile;
	}

	/**
	 * @param reportFile 设置报表文件对象
	 */
	public void setReportFile(java.sql.Blob reportFile) {
		this.reportFile = reportFile;
	}

	/**
	 * @return String 获取模板类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type 设置模板类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus() {
		return recordStatus;
	}

	/**
	 * @param recordStatus 设置记录状态 
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	/**
	 * @return String 获取站点标识
	 */
	public String getSiteID() {
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
	public Long getCreator() {
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
	public java.util.Date getCreateTime() {
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
	public Long getUpdator() {
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
	public java.util.Date getUpdateTime() {
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