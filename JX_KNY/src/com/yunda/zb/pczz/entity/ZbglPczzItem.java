package com.yunda.zb.pczz.entity;

import javax.persistence.*;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczzItem实体类, 数据表：普查整治计划项
 * <li>创建人：王利成
 * <li>创建日期：2015-03-04
 * <li>修改人: 林欢
 * <li>修改日期：2016-08-18
 * <li>修改内容：普查整治功能重做
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_PCZZ_Item")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglPczzItem implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 普查整治计划主键 */
	@Column(name="ZBGL_PCZZ_IDX")
	private String zbglPczzIDX;
	/* 项目名称 */
	@Column(name="Item_Name")
	private String itemName;
	/* 项目内容 */
	@Column(name="Item_Content")
	private String itemContent;
//	/* 车型简称列表 */
//	@Column(name="TrainType_ShortName_List")
//	private String trainTypeShortNameList;
//	/* 车型代码列表 */
//	@Column(name="TrainType_List")
//	private String trainTypeList;
	/* 记录状态 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 更新时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;

	/**
	 * @return String 获取普查整治单主键
	 */
	public String getZbglPczzIDX(){
		return zbglPczzIDX;
	}
	public void setZbglPczzIDX(String zbglPczzIDX) {
		this.zbglPczzIDX = zbglPczzIDX;
	}
	/**
	 * @return String 获取项目名称
	 */
	public String getItemName(){
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * @return String 获取项目内容
	 */
	public String getItemContent(){
		return itemContent;
	}
	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
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
	 * @return java.util.Date 获取更新时间
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
}