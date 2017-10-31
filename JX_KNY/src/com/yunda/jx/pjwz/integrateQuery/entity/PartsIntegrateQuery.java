package com.yunda.jx.pjwz.integrateQuery.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;



/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 配件综合查询视图
 * <li>创建人：张迪
 * <li>创建日期：2016-5-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "V_PJWZ_PARTS_INTEGRATE_QUERY")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsIntegrateQuery implements java.io.Serializable {

	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;


	/** idx主键 */
	@Id
	private String idx;

	/** 配件名称 */
	@Column(name = "parts_name")
	private String partsName;

	/** 规格型号 */
	@Column(name = "specification_model")
	private String specificationModel;
	/** 物料编码 */
	@Column(name = "MAT_CODE")
	private String matCode;
	/** 单位 */
    @Column(name = "unit")
	private String unit;

	/** 定量 */
    @Column(name = "LIMIT_QTY")
	private Integer limitQty;
	
	/** 保有量 */
    @Column(name = "byl")
	private Integer byQty;
	
	/** 良好数量 */
    @Column(name = "lh")
	private Integer lhQty;
	
	/** 待修量 */
    @Column(name = "dx")
	private Integer dxQty;
	
	/** 自修量 */
    @Column(name = "zx")
	private Integer zxQty;
	
	/** 委外修 */
    @Column(name = "wwx")
	private Integer wwxQty;
	/** 待校验 */
    @Column(name = "djy")
	private Integer djyQty;
	/** 待报废 */
    @Column(name = "dbf")
	private Integer dbfQty;
    
	/** 良好率  */
    
	private BigDecimal lhl;
    
	/** 可修台数 */
    @Column(name = "standard_qty")
	private Integer standardQty;
 
    /* 车型主键 */
    @Transient
    private String unloadTrainTypeIdx;
    /**
     * <li>说明：构造方法
     * <li>创建人：张迪
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     */
    public PartsIntegrateQuery(){  
    }
    /**
     * <li>说明：带参构造方法
     * <li>创建人：张迪
     * <li>创建日期：2016-6-7
     * <li>修改人： 
     * <li>修改日期：
     * @param idx id
     * @param partsName 配名称
     * @param specificationModel 规格型号
     * @param matCode 物料编码
     * @param unit 单位
     * @param limitQty 定量
     * @param byQty 保有量
     * @param lhQty 良好数量
     * @param dxQty 待修数量
     * @param zxQty 自修数量
     * @param wwxQty 委外修数量
     * @param djyQty 待校验数量
     * @param dbfQty 待报废数量
     * @param lhl 良好率
     * @param standardQty 可修车参数
     */
    public PartsIntegrateQuery(String idx, String partsName, String specificationModel, String matCode, 
        String unit, Integer limitQty, Integer byQty, Integer lhQty, Integer dxQty, Integer zxQty, Integer wwxQty,
        Integer djyQty, Integer dbfQty, BigDecimal lhl, Integer standardQty){
        this.idx = idx;
        this.partsName = partsName;
        this.specificationModel = specificationModel;
        this.matCode = matCode;
        this.unit = unit;
        this.limitQty = limitQty;
        this.byQty = byQty;
        this.lhQty = lhQty;
        this.dxQty = dxQty;
        this.zxQty = zxQty;
        this.wwxQty = wwxQty;
        this.djyQty = djyQty;
        this.dbfQty = dbfQty;
        this.lhl = lhl;
        this.standardQty = standardQty;
    }
    
    public Integer getByQty() {
        return byQty;
    }

    
    public void setByQty(Integer byQty) {
        this.byQty = byQty;
    }

    
    public Integer getDbfQty() {
        return dbfQty;
    }

    
    public void setDbfQty(Integer dbfQty) {
        this.dbfQty = dbfQty;
    }

    
    public Integer getDxQty() {
        return dxQty;
    }

    
    public void setDxQty(Integer dxQty) {
        this.dxQty = dxQty;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public BigDecimal getLhl() { 
        
        return lhl;
    }

    
    public void setLhl(BigDecimal lhl) {
        this.lhl = lhl;
    }

    
    public Integer getLhQty() {
        return lhQty;
    }

    
    public void setLhQty(Integer lhQty) {
        this.lhQty = lhQty;
    }

    
    public Integer getLimitQty() {
        return limitQty;
    }

    
    public void setLimitQty(Integer limitQty) {
        this.limitQty = limitQty;
    }

    
    public String getPartsName() {
        return partsName;
    }

    
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }

    
    public String getSpecificationModel() {
        return specificationModel;
    }

    
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }

    
    public String getUnit() {
        return unit;
    }

    
    public void setUnit(String unit) {
        this.unit = unit;
    }

    
    public Integer getWwxQty() {
        return wwxQty;
    }

    
    public void setWwxQty(Integer wwxQty) {
        this.wwxQty = wwxQty;
    }

    
    public Integer getZxQty() {
        return zxQty;
    }

    
    public void setZxQty(Integer zxQty) {
        this.zxQty = zxQty;
    }


    
    public Integer getDjyQty() {
        return djyQty;
    }


    
    public void setDjyQty(Integer djyQty) {
        this.djyQty = djyQty;
    }


    
    public Integer getStandardQty() {
        return standardQty;
    }


    
    public void setStandardQty(Integer standardQty) {
        this.standardQty = standardQty;
    }


    
    public String getMatCode() {
        return matCode;
    }


    
    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }


    
    public String getUnloadTrainTypeIdx() {
        return unloadTrainTypeIdx;
    }


    
    public void setUnloadTrainTypeIdx(String unloadTrainTypeIdx) {
        this.unloadTrainTypeIdx = unloadTrainTypeIdx;
    }





	
}