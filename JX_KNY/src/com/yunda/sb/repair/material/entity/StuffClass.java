package com.yunda.sb.repair.material.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.yunda.baseapp.ChineseCharToEn;
import com.yunda.frame.util.StringUtil;
import com.yunda.sb.base.IEntity;
import com.yunda.sb.base.combo.IPyFilter;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: StuffClass，数据表：物料类型
 * <li>创建人：何涛
 * <li>创建日期：2016年12月9日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Entity
@Table(name = "E_WLGL_STUFF_CLASS")
public class StuffClass implements java.io.Serializable, IEntity, IPyFilter {

	/** 默认序列号 */
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id
	@GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 材料名称规格 */
	@Column(name = "stuff_name")
	private String stuffName;

	/** 材料名称规格首拼 */
	@Column(name = "stuff_name_py")
	private String stuffNamePY;

	/** 计量单位 */
	@Column(name = "stuff_unit")
	private String stuffUnit;

	/** 单价	*/
	@Column(name = "stuff_unit_price")
	private Float stuffUnitPrice;

	/* 数据状态 */
	@Column(name = "RECORD_STATUS")
	private Integer recordStatus;

	/* 创建人 */
	@Column(name = "CREATOR", updatable = false)
	private Long creator;

	/* 创建时间 */
	@Column(name = "CREATE_TIME", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date createTime;

	/* 修改人 */
	@Column(name = "UPDATOR")
	private Long updator;

	/* 修改时间 */
	@Column(name = "UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date updateTime;

	/**
	 * <li>说明：默认构造方法
	 * <li>创建人：何涛
	 * <li>创建日期：2016年12月9日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：	 
	 */
	public StuffClass() {
		super();
	}

	/**
	 * <li>说明：构造方法
	 * <li>创建人：何涛
	 * <li>创建日期：2016年12月9日
	 * <li>修改人： 
	 * <li>修改内容：
	 * <li>修改日期：	 
	 * @param stuffName 材料名称规格
	 * @param stuffNamePY 材料名称规格首拼
	 * @param stuffUnit 计量单位
	 * @param stuffUnitPrice 单价
	 */
	public StuffClass(String stuffName, String stuffNamePY, String stuffUnit, Float stuffUnitPrice) {
		super();
		this.stuffName = stuffName;
		if (StringUtil.isNullOrBlank(stuffNamePY)) {
			stuffNamePY = ChineseCharToEn.getInstance().getAllFirstLetter(stuffName);
		}
		this.stuffNamePY = stuffNamePY;
		this.stuffUnit = stuffUnit;
		this.stuffUnitPrice = stuffUnitPrice;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getStuffName() {
		return stuffName;
	}

	public void setStuffName(String stuffName) {
		this.stuffName = stuffName;
	}

	public String getStuffNamePY() {
		return stuffNamePY;
	}

	public void setStuffNamePY(String stuffNamePY) {
		this.stuffNamePY = stuffNamePY;
	}

	public String getStuffUnit() {
		return stuffUnit;
	}

	public void setStuffUnit(String stuffUnit) {
		this.stuffUnit = stuffUnit;
	}

	public Float getStuffUnitPrice() {
		return stuffUnitPrice;
	}

	public void setStuffUnitPrice(Float stuffUnitPrice) {
		this.stuffUnitPrice = stuffUnitPrice;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdator() {
		return updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String getFieldName4PY(String xfield) {
		if ("stuffName".equals(xfield)) {
			return "stuffNamePY";
		}
		return null;
	}

}
