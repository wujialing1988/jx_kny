package com.yunda.jx.pjjx.wsbean;

import java.io.Serializable;

/**
 * <li>说明：物料消耗WS实体
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-19
 * <li>成都运达科技股份有限公司
 */
public class PartsRdpExpendMatWSBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idx;
	/* 物料编码 */
	private String matCode;
	/* 物料描述 */
	private String matDesc;
	/* 计量单位 */
	private String unit;
	/* 单价 */
	private Double price;
	/* 消耗数量 */
	private Double qty;
    
    /* 额定数量 */
    private Double numberRated;
    
    /* 消耗人名称 */
    private String handleEmpName;
    
    
	public String getIdx() {
		return idx;
	}
	
    public String getHandleEmpName() {
        return handleEmpName;
    }
    
    public void setHandleEmpName(String handleEmpName) {
        this.handleEmpName = handleEmpName;
    }
    
    public Double getNumberRated() {
        return numberRated;
    }
    
    public void setNumberRated(Double numberRated) {
        this.numberRated = numberRated;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
}
