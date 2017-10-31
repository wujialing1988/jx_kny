package com.yunda.jx.pjwz.partsBase.warehouse.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WarehouseLocation实体类, 数据表：库位
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJWZ_Warehouse_Location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WarehouseLocation implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /** 状态 - 未满 */
    public static final String STATUS_NO = "未满";
    
    /** 状态 - 已满 */
    public static final String STATUS_YES = "已满";
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 库位编码 */
    @Column(name = "Location_ID")
    private String locationID;
    
    /* 库位名称 */
    @Column(name = "Location_Name")
    private String locationName;
    
    /* 库位地址 */
    @Column(name = "Location_Address")
    private String locationAddress;
    
    /* 库房主键 */
    @Column(name = "WareHouse_IDX")
    private String wareHouseIDX;
    
    /* 库房编码 */
    @Column(name = "Warehouse_ID")
    private String warehouseID;
    
    /* 库房名称 */
    @Column(name = "Warehouse_Name")
    private String warehouseName;
    
    /* 状态，未满、已满 */
    private String status;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 站点标示，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /**
     * @return String 获取库位编码
     */
    public String getLocationID() {
        return locationID;
    }
    
    /**
     * @param locationID 设置库位编码
     */
    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }
    
    /**
     * @return String 获取库位名称
     */
    public String getLocationName() {
        return locationName;
    }
    
    /**
     * @param locationName 设置库位名称
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    /**
     * @return String 获取库位地址
     */
    public String getLocationAddress() {
        return locationAddress;
    }
    
    /**
     * @param locationAddress 设置库位地址
     */
    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }
    
    /**
     * @return String 获取库房主键
     */
    public String getWareHouseIDX() {
        return wareHouseIDX;
    }
    
    /**
     * @param wareHouseIDX 设置库房主键
     */
    public void setWareHouseIDX(String wareHouseIDX) {
        this.wareHouseIDX = wareHouseIDX;
    }
    
    /**
     * @return String 获取库房编码
     */
    public String getWarehouseID() {
        return warehouseID;
    }
    
    /**
     * @param warehouseID 设置库房编码
     */
    public void setWarehouseID(String warehouseID) {
        this.warehouseID = warehouseID;
    }
    
    /**
     * @return String 获取库房名称
     */
    public String getWarehouseName() {
        return warehouseName;
    }
    
    /**
     * @param warehouseName 设置库房名称
     */
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    
    /**
     * @return String 获取状态
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * @param status 设置状态
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * @return Integer 获取记录状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    /**
     * @param recordStatus 设置记录状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    /**
     * @return String 获取站点标示
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @param siteID 设置站点标示
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @return Long 获取创建人
     */
    public Long getCreator() {
        return creator;
    }
    
    /**
     * @param creator 设置创建人
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    /**
     * @return java.util.Date 获取创建时间
     */
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    /**
     * @param createTime 设置创建时间
     */
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    /**
     * @return Long 获取修改人
     */
    public Long getUpdator() {
        return updator;
    }
    
    /**
     * @param updator 设置修改人
     */
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    
    /**
     * @return java.util.Date 获取改时间
     */
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * @param updateTime 设置改时间
     */
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
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
