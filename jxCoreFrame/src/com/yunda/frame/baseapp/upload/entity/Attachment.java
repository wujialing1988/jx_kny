package com.yunda.frame.baseapp.upload.entity;

import javax.persistence.*;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Attachment 附件信息 实体类, 数据表：附件管理
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-10-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXPZ_Attachment_Manage")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class Attachment implements java.io.Serializable {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/* idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/* 附件保存名称 */
	@Column(name = "Attachment_Save_Name")
	private String attachmentSaveName;

	/* 附件真实名称 */
	@Column(name = "Attachment_Real_Name")
	private String attachmentRealName;

	/* 附件大小 */
	@Column(name = "Attachment_Size")
	private Long attachmentSize;

	/* 附件所属的关键值 */
	@Column(name = "Attachment_Key_IDX")
	private String attachmentKeyIDX;

	/* 附件所属的业务表单，记录表名 */
	@Column(name = "Attachment_Key_Name")
	private String attachmentKeyName;

	/* 文件类型，例如txt，docx等 */
	@Column(name = "File_Type")
	private String fileType;

	/* 附件类型 */
	@Column(name = "Attachment_Class")
	private String attachmentClass;

	/* 上传人 */
	@Column(name = "Upload_Person")
	private Long uploadPerson;

	/* 上传人名称 */
	@Column(name = "Upload_Person_Name")
	private String uploadPersonName;

	/* 上传时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Upload_Time")
	private java.util.Date uploadTime;

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

	/** 黄杨2017/5/5 新增 用于pda附件保存 */
	/* 存放真实路径 */
	@Column(name = "REAL_PATH")
	private String realPath;

	/**
	 * @return String 获取附件保存名称
	 */
	public String getAttachmentSaveName() {
		return attachmentSaveName;
	}

	/**
	 * @param 设置附件保存名称
	 */
	public void setAttachmentSaveName(String attachmentSaveName) {
		this.attachmentSaveName = attachmentSaveName;
	}

	/**
	 * @return String 获取附件真实名称
	 */
	public String getAttachmentRealName() {
		return attachmentRealName;
	}

	/**
	 * @param 设置附件真实名称
	 */
	public void setAttachmentRealName(String attachmentRealName) {
		this.attachmentRealName = attachmentRealName;
	}

	/**
	 * @return Long 获取附件大小
	 */
	public Long getAttachmentSize() {
		return attachmentSize;
	}

	/**
	 * @param 设置附件大小
	 */
	public void setAttachmentSize(Long attachmentSize) {
		this.attachmentSize = attachmentSize;
	}

	/**
	 * @return String 获取附件所属的关键值
	 */
	public String getAttachmentKeyIDX() {
		return attachmentKeyIDX;
	}

	/**
	 * @param 设置附件所属的关键值
	 */
	public void setAttachmentKeyIDX(String attachmentKeyIDX) {
		this.attachmentKeyIDX = attachmentKeyIDX;
	}

	/**
	 * @return String 获取附件所属的业务表单
	 */
	public String getAttachmentKeyName() {
		return attachmentKeyName;
	}

	/**
	 * @param 设置附件所属的业务表单
	 */
	public void setAttachmentKeyName(String attachmentKeyName) {
		this.attachmentKeyName = attachmentKeyName;
	}

	/**
	 * @return String 获取文件类型
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param 设置文件类型
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return String 获取附件类型
	 */
	public String getAttachmentClass() {
		return attachmentClass;
	}

	/**
	 * @param 设置附件类型
	 */
	public void setAttachmentClass(String attachmentClass) {
		this.attachmentClass = attachmentClass;
	}

	/**
	 * @return Long 获取上传人
	 */
	public Long getUploadPerson() {
		return uploadPerson;
	}

	/**
	 * @param 设置上传人
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
	 * @param 设置上传人名称
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
	 * @param 设置上传时间
	 */
	public void setUploadTime(java.util.Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus() {
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
	public String getSiteID() {
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
	public Long getCreator() {
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
	public java.util.Date getCreateTime() {
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
	public Long getUpdator() {
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
	public java.util.Date getUpdateTime() {
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

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

}