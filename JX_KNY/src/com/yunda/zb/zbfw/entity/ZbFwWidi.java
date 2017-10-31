package com.yunda.zb.zbfw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbFwWidi实体类, 数据表：整备作业项目数据项
 * <li>创建人：王利成
 * <li>创建日期：2015-01-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBFW_WI_DI")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbFwWidi implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/** 是否必填 - 是 */
	public static final String ZBFW_WIDI_IS_BLANK_YES = "是";
	/** 是否必填 - 否 */
	public static final String ZBFW_WIDI_IS_BLANK_NO ="否";
	
	/** 数据值类型 - 字符 */
	public static final String ZBFW_WIDI_DATA_TYPE_ZF = "字符";
	/** 数据值类型 - 数字 */
	public static final String ZBFW_WIDI_DATA_TYPE_SZ = "数字";
	
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 整备作业项目主键 */
	@Column(name="ZBFW_WI_IDX")
	private String zbfwwiIDX;
	/* 编号 */
	@Column(name="DI_Code")
	private String dICode;
	/* 名称 */
	@Column(name="DI_Name")
	private String dIName;
	/* 标准 */
	@Column(name="DI_Standard")
	private String dIStandard;
	/* 数据类型，char：字符；number：数字； */
	@Column(name="DI_Class")
	private String dIClass;
	/* 是,否 */
	@Column(name="Is_Blank")
	private String isBlank;
	/* 顺序号 */
	@Column(name="Seq_No")
	private Integer seqNo;
	/* 记录状态，1：删除；0：未删除； */
	@Column(name="Record_Status")
	private Integer recordStatus;

	/**
	 * @return String 获取整备作业项目主键
	 */
	public String getZbfwwiIDX(){
		return zbfwwiIDX;
	}
	public void setZbfwwiIDX(String zbfwwiIDX) {
		this.zbfwwiIDX = zbfwwiIDX;
	}
	/**
	 * @return String 获取编号
	 */
	public String getDICode(){
		return dICode;
	}
	public void setDICode(String dICode) {
		this.dICode = dICode;
	}
	/**
	 * @return String 获取名称
	 */
	public String getDIName(){
		return dIName;
	}
	public void setDIName(String dIName) {
		this.dIName = dIName;
	}
	/**
	 * @return String 获取标准
	 */
	public String getDIStandard(){
		return dIStandard;
	}
	public void setDIStandard(String dIStandard) {
		this.dIStandard = dIStandard;
	}
	/**
	 * @return String 获取数据类型
	 */
	public String getDIClass(){
		return dIClass;
	}
	public void setDIClass(String dIClass) {
		this.dIClass = dIClass;
	}
	/**
	 * @return Integer 获取是否必填
	 */
	public String getIsBlank(){
		return isBlank;
	}
	public void setIsBlank(String isBlank) {
		this.isBlank = isBlank;
	}
	/**
	 * @return Integer 获取顺序号
	 */
	public Integer getSeqNo(){
		return seqNo;
	}
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
}