package com.yunda.jx.wlgl.stockmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatStockQuery实体类, 视图：物料库存查询
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-9 下午04:31:30
 * <li>修改人: 张迪
 * <li>修改日期： 2016-5-20
 * <li>修改内容： 添加物料类型字段
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "V_WLGL_MAT_STOCK_QUERY")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class MatStockQuery implements java.io.Serializable {

	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/** 状态 - 所有 */
	public static final short CONST_INT_STATUS_ALL = 0;
	/** 状态 - 低于最小保有量 */
	public static final short CONST_INT_STATUS_MIN = 1;
	/** 状态 - 正常 */
	public static final short CONST_INT_STATUS_NOR = 2;
	/** 状态 - 高于最大保有量 */
	public static final short CONST_INT_STATUS_MAX = 3;

	/** idx主键 */
	@Id
	private String idx;

	/** 库房主键 */
	@Column(name = "WH_IDX")
	private String whIdx;

	/** 库房名称 */
	@Column(name = "WH_Name")
	private String whName;

	/** 物料编码 */
	@Column(name = "Mat_Code")
	private String matCode;

	/** 物料描述 */
	@Column(name = "Mat_Desc")
	private String matDesc;
    
	/** 物料类型 */
	@Column(name = "Mat_Type")
	private String matType;

	/** 单位 */
	private String unit;

	/** 数量 */
	private Integer qty;

	/** 最小保有量 */
	@Column(name = "Min_Qty")
	private Integer minQty;

	/** 最大保有量 */
	@Column(name = "Max_Qty")
	private Integer maxQty;
	
	/** 查询条件 - 状态 */
	@Transient
	private Short status;

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getMatCode() {
		return matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	public String getMatDesc() {
		return matDesc;
	}

	public void setMatDesc(String matDesc) {
		this.matDesc = matDesc;
	}

	public Integer getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(Integer maxQty) {
		this.maxQty = maxQty;
	}

	public Integer getMinQty() {
		return minQty;
	}

	public void setMinQty(Integer minQty) {
		this.minQty = minQty;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getWhIdx() {
		return whIdx;
	}

	public void setWhIdx(String whIdx) {
		this.whIdx = whIdx;
	}

	public String getWhName() {
		return whName;
	}

	public void setWhName(String whName) {
		this.whName = whName;
	}

    
    public String getMatType() {
        return matType;
    }

    
    public void setMatType(String matType) {
        this.matType = matType;
    }

}