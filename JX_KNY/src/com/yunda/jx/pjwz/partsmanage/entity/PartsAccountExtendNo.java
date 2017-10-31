package com.yunda.jx.pjwz.partsmanage.entity;

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
 * <li>说明：PartsAccountExtendNo实体类, 数据表：配件信息扩展编号
 * <li>创建人：程锐
 * <li>创建日期：2014-08-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJWZ_Parts_Account_ExtendNo")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsAccountExtendNo implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 配件信息主键 */
	@Column(name="PARTS_ACCOUNT_IDX")
	private String partsAccountIDX;
	/* 扩展编号1 */
	private String extendNo1;
	/* 扩展编号2 */
	private String extendNo2;
	/* 扩展编号3 */
	private String extendNo3;
	/* 扩展编号4 */
	private String extendNo4;
	/* 扩展编号5 */
	private String extendNo5;
	/* 扩展编号6 */
	private String extendNo6;
	/* 扩展编号7 */
	private String extendNo7;
	/* 扩展编号8 */
	private String extendNo8;
	/* 扩展编号9 */
	private String extendNo9;
	/* 扩展编号10 */
	private String extendNo10;
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
	 * @return String 获取配件信息主键
	 */
	public String getPartsAccountIDX(){
		return partsAccountIDX;
	}
	/**
	 * @param 设置配件信息主键
	 */
	public void setPartsAccountIDX(String partsAccountIDX) {
		this.partsAccountIDX = partsAccountIDX;
	}
	/**
	 * @return String 获取扩展编号1
	 */
	public String getExtendNo1(){
		return extendNo1;
	}
	/**
	 * @param 设置扩展编号1
	 */
	public void setExtendNo1(String extendNo1) {
		this.extendNo1 = extendNo1;
	}
	/**
	 * @return String 获取扩展编号2
	 */
	public String getExtendNo2(){
		return extendNo2;
	}
	/**
	 * @param 设置扩展编号2
	 */
	public void setExtendNo2(String extendNo2) {
		this.extendNo2 = extendNo2;
	}
	/**
	 * @return String 获取扩展编号3
	 */
	public String getExtendNo3(){
		return extendNo3;
	}
	/**
	 * @param 设置扩展编号3
	 */
	public void setExtendNo3(String extendNo3) {
		this.extendNo3 = extendNo3;
	}
	/**
	 * @return String 获取扩展编号4
	 */
	public String getExtendNo4(){
		return extendNo4;
	}
	/**
	 * @param 设置扩展编号4
	 */
	public void setExtendNo4(String extendNo4) {
		this.extendNo4 = extendNo4;
	}
	/**
	 * @return String 获取扩展编号5
	 */
	public String getExtendNo5(){
		return extendNo5;
	}
	/**
	 * @param 设置扩展编号5
	 */
	public void setExtendNo5(String extendNo5) {
		this.extendNo5 = extendNo5;
	}
	/**
	 * @return String 获取扩展编号6
	 */
	public String getExtendNo6(){
		return extendNo6;
	}
	/**
	 * @param 设置扩展编号6
	 */
	public void setExtendNo6(String extendNo6) {
		this.extendNo6 = extendNo6;
	}
	/**
	 * @return String 获取扩展编号7
	 */
	public String getExtendNo7(){
		return extendNo7;
	}
	/**
	 * @param 设置扩展编号7
	 */
	public void setExtendNo7(String extendNo7) {
		this.extendNo7 = extendNo7;
	}
	/**
	 * @return String 获取扩展编号8
	 */
	public String getExtendNo8(){
		return extendNo8;
	}
	/**
	 * @param 设置扩展编号8
	 */
	public void setExtendNo8(String extendNo8) {
		this.extendNo8 = extendNo8;
	}
	/**
	 * @return String 获取扩展编号9
	 */
	public String getExtendNo9(){
		return extendNo9;
	}
	/**
	 * @param 设置扩展编号9
	 */
	public void setExtendNo9(String extendNo9) {
		this.extendNo9 = extendNo9;
	}
	/**
	 * @return String 获取扩展编号10
	 */
	public String getExtendNo10(){
		return extendNo10;
	}
	/**
	 * @param 设置扩展编号10
	 */
	public void setExtendNo10(String extendNo10) {
		this.extendNo10 = extendNo10;
	}
	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param 设置记录的状态
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
}