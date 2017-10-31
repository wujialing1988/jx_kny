package com.yunda.jx.wlgl.backwh.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatBackWHDetail实体类, 数据表：退库明细
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-8 上午10:15:55
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "WLGL_Mat_Back_WH_Detail")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatBackWHDetail implements java.io.Serializable {

	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id @GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 退库单主键 */
	@Column(name = "Mat_Back_WH_IDX")
	private String matBackWhIdx;

	/** 物料编码 */
	@Column(name = "Mat_Code")
	private String matCode;

	/** 物料描述 */
	@Column(name = "Mat_Desc")
	private String matDesc;

	/** 单位 */
	private String unit;

	/** 数量 */
	private Integer qty;

	/** 出库明细主键 */
	@Column(name = "Mat_Out_WH_Detail_IDX")
	private String matOutWhDatailIdx;

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
	 * @return java.util.Date 获取创建时间
	 */
	public java.util.Date getCreateTime() {
		return createTime;
	}

	/**
	 * @return Long 获取创建人
	 */
	public Long getCreator() {
		return creator;
	}

	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}

	/**
	 * @return 获取退库单主键
	 */
	public String getMatBackWhIdx() {
		return matBackWhIdx;
	}

	/**
	 * @return String 获取物料编码
	 */
	public String getMatCode() {
		return matCode;
	}

	/**
	 * @return String 获取物料描述
	 */
	public String getMatDesc() {
		return matDesc;
	}

	/**
	 * @return 获取出库明细主键
	 */
	public String getMatOutWhDatailIdx() {
		return matOutWhDatailIdx;
	}

	/**
	 * @return Integer 获取数量
	 */
	public Integer getQty() {
		return qty;
	}

	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus() {
		return recordStatus;
	}

	/**
	 * @return String 获取站点标识
	 */
	public String getSiteID() {
		return siteID;
	}

	/**
	 * @return String 获取单位
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @return java.util.Date 获取修改时间
	 */
	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @return Long 获取修改人
	 */
	public Long getUpdator() {
		return updator;
	}

	/**
	 * @param 设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @param 设置创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}

	/**
	 * @param 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}

	/**
	 * @param matBackWhIdx 设置退库单主键
	 */
	public void setMatBackWhIdx(String matBackWhIdx) {
		this.matBackWhIdx = matBackWhIdx;
	}

	/**
	 * @param 设置物料编码
	 */
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	/**
	 * @param 设置物料描述
	 */
	public void setMatDesc(String matDesc) {
		this.matDesc = matDesc;
	}

	/**
	 * @param matOutWhDatailIdx 设置出库明细主键
	 */
	public void setMatOutWhDatailIdx(String matOutWhDatailIdx) {
		this.matOutWhDatailIdx = matOutWhDatailIdx;
	}

	/**
	 * @param 设置数量
	 */
	public void setQty(Integer qty) {
		this.qty = qty;
	}

	/**
	 * @param 设置记录的状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	/**
	 * @param 设置站点标识
	 */
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	/**
	 * @param 设置单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @param 设置修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @param 设置修改人
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
}