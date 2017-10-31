package com.yunda.zb.oilconsumption.entity;

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
 * <li>标题: 机车整备管理信息系统
 * <li>说明：OilconSumption实体类, 数据表：机油消耗台账
 * <li>创建人：王利成
 * <li>创建日期：2015-01-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_Oil_Consumption")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class OilconSumption implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 车型编码 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	/* 车型拼音码 */
	@Column(name="Train_Type_ShortName")
	private String trainTypeShortName;
	/* 车号 */
	@Column(name="Train_No")
	private String trainNo;
	/* 机油种类编码 */
	@Column(name="Oil_Class_Code")
	private String jyCode;
	/* 机油种类名称 */
	@Column(name="Oil_Class_Name")
	private String jyName;
	/* 计量单位 */
	@Column(name="unit")
	private String dw;
	/* 机油数量 */
	@Column(name="Consume_Qty")
	private Double consumeQty;
	/* 领用时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Fetch_Time")
	private java.util.Date fetchTime;
	/* 消耗站场 */
	@Column(name="siteID")
	private String siteID;
	/* 站场名称 */
	@Column(name="siteName")
	private String siteName;
	/* 记录人编码 */
	@Column(name="Handle_Person_ID")
	private Long handlePersonID;
	/* 记录人名称 */
	@Column(name="Handle_Person_Name")
	private String handlePersonName;
	/* 记录人时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Handle_Time")
	private java.util.Date handleTime;
	/* 记录状态，1：删除；0：未删除； */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 最新更新时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;

	/**
	 * @return String 获取车型编码
	 */
	public String getTrainTypeIDX(){
		return trainTypeIDX;
	}
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}
	/**
	 * @return String 获取车型拼音码
	 */
	public String getTrainTypeShortName(){
		return trainTypeShortName;
	}
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	/**
	 * @return String 获取车号
	 */
	public String getTrainNo(){
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	/**
	 * @return Double 获取机油数量
	 */
	public Double getConsumeQty(){
		return consumeQty;
	}
	public void setConsumeQty(Double consumeQty) {
		this.consumeQty = consumeQty;
	}
	/**
	 * @return java.util.Date 获取领用时间
	 */
	public java.util.Date getFetchTime(){
		return fetchTime;
	}
	public void setFetchTime(java.util.Date fetchTime) {
		this.fetchTime = fetchTime;
	}
	
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	/**
	 * @return Long 获取记录人编码
	 */
	public Long getHandlePersonID(){
		return handlePersonID;
	}
	public void setHandlePersonID(Long handlePersonID) {
		this.handlePersonID = handlePersonID;
	}
	/**
	 * @return String 获取记录人名称
	 */
	public String getHandlePersonName(){
		return handlePersonName;
	}
	public void setHandlePersonName(String handlePersonName) {
		this.handlePersonName = handlePersonName;
	}
	/**
	 * @return java.util.Date 获取记录人时间
	 */
	public java.util.Date getHandleTime(){
		return handleTime;
	}
	public void setHandleTime(java.util.Date handleTime) {
		this.handleTime = handleTime;
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
	 * @return java.util.Date 获取最新更新时间
	 */
	public java.util.Date getUpdateTime(){
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
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
	public String getJyName() {
		return jyName;
	}
	public void setJyName(String jyName) {
		this.jyName = jyName;
	}
	public String getJyCode() {
		return jyCode;
	}
	public void setJyCode(String jyCode) {
		this.jyCode = jyCode;
	}
}