package com.yunda.jx.wlgl.movewh.entity;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 封装移库入库单信息
 * <li>创建人：张迪
 * <li>创建日期：2016-5-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MatMoveInConfirm implements java.io.Serializable{
	/* 使用默认序列版本ID */
	/**  类型：long  */
	private static final long serialVersionUID = 1L;	
    /* idx主键 */
    private String idx;  
    /* 出库单id */
    private String outWhIdx;  
    /* 接收库房主键 */
    private String getWhIDX;
    /* 接收库房名称 */
    private String getWhName;
    /* 接收库房主键 */
    private String getLocationIDX;
    /* 接收库库位名称 */
    private String getLocationName;   
    /* 出库库房主键 */
    private String whIDX;   
    /* 出库库房名称 */
    private String whName;
    
    /* 物料类型（技改件、消耗件） */
    private String matType;
    
    /* 物料描述 */
    private String matDesc;
    
    /* 物料编码 */
    private String matCode;
    
    /* 单位 */
    private String unit;
    
    /* 移库数量 */
    private Integer qty;
    /* 出库库位状态 */
    private String status ;   
    /* 出库库位 */
    private String locationName;  
    
    /* 出库人主键 */
    private Long exWhEmpID;
    /* 出库人名称 */
    private String exWhEmp;
    /* 出库日期 */
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date whDate;   
	/* 移库状态（待确认移入、退回原库待确认、确认入库、退回原库入库） */
	private String moveStatus;
    public MatMoveInConfirm(){ super();}
   

    public String getGetWhIDX() {
        return getWhIDX;
    }

    
    public void setGetWhIDX(String getWhIDX) {
        this.getWhIDX = getWhIDX;
    }

    
    public String getGetWhName() {
        return getWhName;
    }

    
    public void setGetWhName(String getWhName) {
        this.getWhName = getWhName;
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

    
    public String getMatType() {
        return matType;
    }

    
    public void setMatType(String matType) {
        this.matType = matType;
    }

    
    public String getMoveStatus() {
        return moveStatus;
    }

    
    public void setMoveStatus(String moveStatus) {
        this.moveStatus = moveStatus;
    }

    
    public String getOutWhIdx() {
        return outWhIdx;
    }

    
    public void setOutWhIdx(String outWhIdx) {
        this.outWhIdx = outWhIdx;
    }

    
    public Integer getQty() {
        return qty;
    }

    
    public void setQty(Integer qty) {
        this.qty = qty;
    }
    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
    }

    
    public String getUnit() {
        return unit;
    }

    
    public void setUnit(String unit) {
        this.unit = unit;
    }


    
    public String getExWhEmp() {
        return exWhEmp;
    }


    
    public void setExWhEmp(String exWhEmp) {
        this.exWhEmp = exWhEmp;
    }


    
    public Long getExWhEmpID() {
        return exWhEmpID;
    }


    
    public void setExWhEmpID(Long exWhEmpID) {
        this.exWhEmpID = exWhEmpID;
    }


    
    public String getGetLocationIDX() {
        return getLocationIDX;
    }


    
    public void setGetLocationIDX(String getLocationIDX) {
        this.getLocationIDX = getLocationIDX;
    }


    
    public String getGetLocationName() {
        return getLocationName;
    }


    
    public void setGetLocationName(String getLocationName) {
        this.getLocationName = getLocationName;
    }


    
    public String getLocationName() {
        return locationName;
    }


    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }


    
    public java.util.Date getWhDate() {
        return whDate;
    }


    
    public void setWhDate(java.util.Date whDate) {
        this.whDate = whDate;
    }


    
    public String getWhIDX() {
        return whIDX;
    }


    
    public void setWhIDX(String whIDX) {
        this.whIDX = whIDX;
    }


    
    public String getWhName() {
        return whName;
    }


    
    public void setWhName(String whName) {
        this.whName = whName;
    }
    
   
}