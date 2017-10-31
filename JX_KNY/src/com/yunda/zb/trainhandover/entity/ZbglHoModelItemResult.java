package com.yunda.zb.trainhandover.entity;

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
 * <li>说明：ZbglHoModelItemResult实体类, 数据表：机车交接模板—交接项情况
 * <li>创建人：程梅
 * <li>创建日期：2015-02-07
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_HO_Model_Item_Result")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglHoModelItemResult implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 机车交接项ID */
	@Column(name="HandOverItem_IDX")
	private String handOverItemIDX;
	/* 机车交接情况 */
	private String handOverResultDesc;
	/* 记录状态，1：删除；0：未删除； */
	@Column(name="Record_Status")
	private Integer recordStatus;

	/**
	 * @return String 获取机车交接项ID
	 */
	public String getHandOverItemIDX(){
		return handOverItemIDX;
	}
	/**
	 * @param handOverItemIDX 设置机车交接项ID
	 */
	public void setHandOverItemIDX(String handOverItemIDX) {
		this.handOverItemIDX = handOverItemIDX;
	}
	/**
	 * @return String 获取机车交接情况
	 */
	public String getHandOverResultDesc(){
		return handOverResultDesc;
	}
	/**
	 * @param handOverResultDesc 设置机车交接情况
	 */
	public void setHandOverResultDesc(String handOverResultDesc) {
		this.handOverResultDesc = handOverResultDesc;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param recordStatus 设置记录状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
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