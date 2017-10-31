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
 * <li>说明：ZbglHoCaseItem实体类, 数据表：机车交接单项
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
@Table(name="ZB_ZBGL_HO_Case_Item")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglHoCaseItem implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 交接单主键 */
	@Column(name="HandOverRdp_IDX")
	private String handOverRdpIDX;
	/* 交接项ID */
	@Column(name="HandOverItemModel_IDX")
	private String handOverItemModelIDX;
	/* 交接项名称 */
	private String handOverItemName;
	/* 交接状态 */
	private String handOverItemStatus;
	/* 交接情况描述 */
	private String handOverResultDesc;
	/* 父交接项名称 */
	private String parentItemName;
	/* 父级ID */
	@Column(name="Parent_IDX")
	private String parentIDX;
	/* 记录状态，1：删除；0：未删除； */
	@Column(name="Record_Status")
	private Integer recordStatus;

	/**
	 * @return String 获取交接单主键
	 */
	public String getHandOverRdpIDX(){
		return handOverRdpIDX;
	}
	/**
	 * @param handOverRdpIDX 设置交接单主键
	 */
	public void setHandOverRdpIDX(String handOverRdpIDX) {
		this.handOverRdpIDX = handOverRdpIDX;
	}
	/**
	 * @return String 获取交接项ID
	 */
	public String getHandOverItemModelIDX(){
		return handOverItemModelIDX;
	}
	/**
	 * @param handOverItemModelIDX 设置交接项ID
	 */
	public void setHandOverItemModelIDX(String handOverItemModelIDX) {
		this.handOverItemModelIDX = handOverItemModelIDX;
	}
	/**
	 * @return String 获取交接项名称
	 */
	public String getHandOverItemName(){
		return handOverItemName;
	}
	/**
	 * @param handOverItemName 设置交接项名称
	 */
	public void setHandOverItemName(String handOverItemName) {
		this.handOverItemName = handOverItemName;
	}
	/**
	 * @return String 获取交接状态
	 */
	public String getHandOverItemStatus(){
		return handOverItemStatus;
	}
	/**
	 * @param handOverItemStatus 设置交接状态
	 */
	public void setHandOverItemStatus(String handOverItemStatus) {
		this.handOverItemStatus = handOverItemStatus;
	}
	/**
	 * @return String 获取交接情况描述
	 */
	public String getHandOverResultDesc(){
		return handOverResultDesc;
	}
	/**
	 * @param handOverResultDesc 设置交接情况描述
	 */
	public void setHandOverResultDesc(String handOverResultDesc) {
		this.handOverResultDesc = handOverResultDesc;
	}
	/**
	 * @return String 获取父交接项名称
	 */
	public String getParentItemName(){
		return parentItemName;
	}
	/**
	 * @param parentItemName 设置父交接项名称
	 */
	public void setParentItemName(String parentItemName) {
		this.parentItemName = parentItemName;
	}
	/**
	 * @return String 获取父交接项ID
	 */
	public String getParentIDX(){
		return parentIDX;
	}
	/**
	 * @param parentIDX 设置父交接项ID
	 */
	public void setParentIDX(String parentIDX) {
		this.parentIDX = parentIDX;
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