package com.yunda.jx.pjjx.base.equipdefine.entity;

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
 * <li>说明：EquipDI实体类, 数据表：机务设备检测数据项定义
 * <li>创建人：程梅
 * <li>创建日期：2015-01-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJJX_Equip_DI")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class EquipDI implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 设备工单主键 */
	@Column(name="Equip_Card_IDX")
	private String equipCardIDX;
	/* 检测项编号 */
	@Column(name="Data_Item_No")
	private String dataItemNo;
	/* 检测项名称 */
	@Column(name="Data_Item_Name")
	private String dataItemName;
	/* 检测项描述 */
	@Column(name="Data_Item_Desc")
	private String dataItemDesc;
	/* 单位 */
	private String unit;
	/* 默认值 */
	@Column(name="Default_Value")
	private String defaultValue;
	/* 顺序号 */
	@Column(name="Seq_No")
	private Integer seqNo;
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
	 * @return String 获取设备工单主键
	 */
	public String getEquipCardIDX(){
		return equipCardIDX;
	}
	/**
	 * @param equipCardIDX 设置设备工单主键
	 */
	public void setEquipCardIDX(String equipCardIDX) {
		this.equipCardIDX = equipCardIDX;
	}
	/**
	 * @return String 获取检测项编号
	 */
	public String getDataItemNo(){
		return dataItemNo;
	}
	/**
	 * @param dataItemNo 设置检测项编号
	 */
	public void setDataItemNo(String dataItemNo) {
		this.dataItemNo = dataItemNo;
	}
	/**
	 * @return String 获取检测项名称
	 */
	public String getDataItemName(){
		return dataItemName;
	}
	/**
	 * @param dataItemName 设置检测项名称
	 */
	public void setDataItemName(String dataItemName) {
		this.dataItemName = dataItemName;
	}
	/**
	 * @return String 获取检测项描述
	 */
	public String getDataItemDesc(){
		return dataItemDesc;
	}
	/**
	 * @param dataItemDesc 设置检测项描述
	 */
	public void setDataItemDesc(String dataItemDesc) {
		this.dataItemDesc = dataItemDesc;
	}
	/**
	 * @return String 获取单位
	 */
	public String getUnit(){
		return unit;
	}
	/**
	 * @param unit 设置单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return Integer 获取默认值
	 */
	public String getDefaultValue(){
		return defaultValue;
	}
	/**
	 * @param defaultValue 设置默认值
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @return Integer 获取顺序号
	 */
	public Integer getSeqNo(){
		return seqNo;
	}
	/**
	 * @param seqNo 设置顺序号
	 */
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
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