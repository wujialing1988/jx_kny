package com.yunda.frame.report.entity;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：FileCatalog实体类, 数据表：报表模板管理类型标识目录
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
@Table(name="R_File_Catalog")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class FileCatalog implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/** 报表部署根目录标识【ROOT_0】 */
	public static final String CONST_STR_PATH_ROOT = "ROOT_0";
	/** 文件件路径分隔符【.】 */
	public static final String CONST_STR_PATH_SEPARATOR = ".";
	// Modified by hetao on 2016-03-08 10:30 使用平台相关的文件路径分隔符，兼容非Windows系统
	/** 文件件路径分隔符【\】 */
	public static final String CONST_STR_FILE_SEPARATOR = File.separator;
	
	/** 是否可编辑 - 是 */
	public static final String CONST_STR_EDITABLE_T = ReportConstants.CONST_STR_T;
	/** 是否可编辑 - 否 */
	public static final String CONST_STR_EDITABLE_F = ReportConstants.CONST_STR_F;
	
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 上级主键 */
	@Column(name="Parent_IDX")
	private String parentIDX;
	/* 是否可编辑；F：不可编辑，T：可编辑 */
	private String editable;
	/* 文件夹描述 */
	@Column(name="Folder_Desc")
	private String folderDesc;
	/* 文件夹英文名称 */
	@Column(name="Folder_Name_EN")
	private String folderNameEN;
	/* 文件夹中文名称 */
	@Column(name="Folder_Name_CH")
	private String folderNameCH;
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
	 * @return String 获取文件夹描述
	 */
	public String getFolderDesc(){
		return folderDesc;
	}
	/**
	 * @param folderDesc 设置文件夹描述
	 */
	public void setFolderDesc(String folderDesc) {
		this.folderDesc = folderDesc;
	}
	/**
	 * @return String 获取文件夹英文名称
	 */
	public String getFolderNameEN(){
		return folderNameEN;
	}
	/**
	 * @param folderNameEN 设置文件夹英文名称
	 */
	public void setFolderNameEN(String folderNameEN) {
		this.folderNameEN = folderNameEN;
	}
	/**
	 * @return String 获取文件夹中文名称
	 */
	public String getFolderNameCH(){
		return folderNameCH;
	}
	/**
	 * @param folderNameCH 设置文件夹中文名称
	 */
	public void setFolderNameCH(String folderNameCH) {
		this.folderNameCH = folderNameCH;
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
	 * @return 获取上级主键
	 */
	public String getParentIDX() {
		return parentIDX;
	}
	/**
	 * @param parentIDX 设置上级主键
	 */
	public void setParentIDX(String parentIDX) {
		this.parentIDX = parentIDX;
	}

    /**
     * <li>说明：格式化报表部署目录的节点显示文本
     * <li>创建人：何涛
     * <li>创建日期：2015-2-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return String 
     */
    public String formatDisplayText() {
        StringBuilder sb = new StringBuilder(this.getFolderNameEN());
        String folderNameCH = this.getFolderNameCH();
        if (null != folderNameCH && folderNameCH.trim().length() > 0) {
            sb.append("【").append(this.getFolderNameCH()).append("】");
        }
        if (this.editable.equals(ReportConstants.CONST_STR_F)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("<span style='color:").append(ReportConstants.CONST_STR_UNEDITABLE_COLOR).append(";'>");
            sb2.append(sb);
            sb2.append("</span>");
            return sb2.toString();
        }
        return sb.toString();
    }
}