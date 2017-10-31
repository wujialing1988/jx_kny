package com.yunda.jx.wlgl.partsBase.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明： UsedMatSelect实体 仅用于【机车检修用料】批量添加物料时的查询数据源实体
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-31 上午11:06:10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="V_WLGL_USED_MAT_SELECT")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class UsedMatSelect implements Serializable {

	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@Id
	private String idx;

	/** 清单主键 */
	@Column(name = "Used_Mat_IDX")
	private String usedMatIdx;

	/** 物料编码 */
	@Column(name = "Mat_Code")
	private String matCode;

	/** 库房主键 */
	@Column(name = "WH_IDX")
	private String whIdx;

	/** 物料描述 */
	@Column(name = "Mat_Desc")
	private String matDesc;

	/** 单位 */
	private String unit;
	
	private Float price;

	/** 数量 */
	private Integer qty;

	public String getIdx() {
		return idx;
	}

	public String getMatCode() {
		return matCode;
	}

	public String getMatDesc() {
		return matDesc;
	}

	public Float getPrice() {
		return price;
	}

	public Integer getQty() {
		return qty;
	}

	public String getUnit() {
		return unit;
	}

	public String getUsedMatIdx() {
		return usedMatIdx;
	}

	public String getWhIdx() {
		return whIdx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	public void setMatDesc(String matDesc) {
		this.matDesc = matDesc;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setUsedMatIdx(String usedMatIdx) {
		this.usedMatIdx = usedMatIdx;
	}

	public void setWhIdx(String whIdx) {
		this.whIdx = whIdx;
	}

}
