package com.yunda.jx.wlgl.outstock.entity;

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
 * <li>说明：MatOutWHTrainDetail实体类, 数据表：机车用料明细
 * <li>创建人： 何涛
 * <li>创建日期： 2014-9-28 下午01:45:53
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "WLGL_Mat_Out_WH_Train_Detail")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class MatOutWHTrainDetail implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id @GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 机车用料单主键 */
	@Column(name = "Mat_Out_WH_Train_IDX")
	private String matOutWHTrainIDX;
	
	/** 机车用料消耗记录主键 */
	@Column(name = "MAT_Train_Expend_Account_IDX")
	private String matTrainExpendAccountIDX;

	/** 物料编码 */
	@Column(name = "Mat_Code")
	private String matCode;

	/** 物料描述 */
	@Column(name = "Mat_Desc")
	private String matDesc;

	/** 单位 */
	private String unit;
	
	/** 单价 */
	private Float price;

	/** 数量 */
	private Integer qty;
	
	@Column(name="BACK_QTY")
	private Integer backQty;

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
	 * @return 获取退库数量
	 */
	public Integer getBackQty() {
		return backQty;
	}

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
	 * @return 获取机车用料单主键
	 */
	public String getMatOutWHTrainIDX() {
		return matOutWHTrainIDX;
	}

	/**
	 * @return 获取机车用料消耗记录主键
	 */
	public String getMatTrainExpendAccountIDX() {
		return matTrainExpendAccountIDX;
	}

	/**
	 * @return 获取单价
	 */
	public Float getPrice() {
		return price;
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
	 * @param backQty 设置退库数量
	 */
	public void setBackQty(Integer backQty) {
		this.backQty = backQty;
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
	 * @param ckJcwDIdx
	 *            设置机车用料单主键
	 */
	public void setMatOutWHTrainIDX(String matOutWHTrainIDX) {
		this.matOutWHTrainIDX = matOutWHTrainIDX;
	}

	/**
	 * @param matTrainExpendAccountIDX 设置机车用料消耗记录主键
	 */
	public void setMatTrainExpendAccountIDX(String matTrainExpendAccountIDX) {
		this.matTrainExpendAccountIDX = matTrainExpendAccountIDX;
	}

	/**
	 * @param price 设置单价
	 */
	public void setPrice(Float price) {
		this.price = price;
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