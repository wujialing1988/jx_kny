package com.yunda.jx.wlgl.changewh.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 库位调整实体
 * <li>创建人：张迪
 * <li>创建日期：2016-5-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="WLGL_WAREHOUSE_LOCATION_CHANGE")
public class WarehouseLocationChange implements Serializable {
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /* idx主键 */
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
    private String idx;
    /* 库房主键 */
    @Column(name="WH_IDX")
    private String whIDX;
    /* 库房名称 */
    @Column(name="WH_Name")
    private String whName;
    /* 原库位 */
    @Column(name="LOCATION_NAME")
    private String locationName;
    /* 调整后库位 */
    @Column(name="LOCATION_NAME_CHANGE")
    private String locationNameChange;
    /* 调整人主键 */
    @Column(name="CHANGE_EMP_ID")
    private Long changeEmpId;
    /* 调整人名称 */
    @Column(name="CHANGE_EMP")
    private String changeEmp;
    /* 调整数量 */
    @Column(name="CHANGE_Qty")
    private Integer changeQty;
    /* 物料编码 */
    @Column(name="Mat_Code")
    private String matCode;
    /* 物料类型（技改件、消耗件） */
    @Column(name="Mat_Type")
    private String matType;
    /* 物料描述 */
    @Column(name="Mat_Desc")
    private String matDesc;
    /* 单位 */
    private String unit;
    /* 调整日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CHANGE_DATE")
    private java.util.Date changeDate;
    
    /* 原库位的id */
    @Transient
    private String locationIdx;
    /* 调整库位的id */
    @Transient
    private String changeLocationIdx;
    /* 原库位的状态 */
    @Transient
    private String status;
    /* 调整库位的状态 */
    @Transient
    private String changeStatus;
   
    
    public String getChangeEmp() {
        return changeEmp;
    }
    
    public void setChangeEmp(String changeEmp) {
        this.changeEmp = changeEmp;
    }
    
    public Long getChangeEmpId() {
        return changeEmpId;
    }
    
    public void setChangeEmpId(Long changeEmpId) {
        this.changeEmpId = changeEmpId;
    }
    
    public Integer getChangeQty() {
        return changeQty;
    }
    
    public void setChangeQty(Integer changeQty) {
        this.changeQty = changeQty;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getLocationName() {
        return locationName;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    public String getLocationNameChange() {
        return locationNameChange;
    }
    
    public void setLocationNameChange(String locationNameChange) {
        this.locationNameChange = locationNameChange;
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
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
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

    
    public java.util.Date getChangeDate() {
        return changeDate;
    }

    
    public void setChangeDate(java.util.Date changeDate) {
        this.changeDate = changeDate;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
    }

    
    public String getChangeLocationIdx() {
        return changeLocationIdx;
    }

    
    public void setChangeLocationIdx(String changeLocationIdx) {
        this.changeLocationIdx = changeLocationIdx;
    }

    
    public String getChangeStatus() {
        return changeStatus;
    }

    
    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    
    public String getLocationIdx() {
        return locationIdx;
    }

    
    public void setLocationIdx(String locationIdx) {
        this.locationIdx = locationIdx;
    }
   
    
}
