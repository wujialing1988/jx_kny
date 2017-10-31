package com.yunda.jx.jxgc.buildupmanage.entity;

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
 * 
 * <li>标题: 机车检修管理信息系统 
 * <li>说明: 故障现象处理方法
 * <li>创建人：程锐
 * <li>创建日期：2013-4-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_PLACE_FAULT_METHOD")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PlaceFaultMethod {
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /** 是否默认 1是 */
    public static final int ISDEFAULT = 1;
    /** 是否默认 0否 */
    public static final int NODEFAULT = 0;
    /* idx主键 */
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
    private String idx;
    /** 故障现象主键 */
    @Column(name="PLACE_FAULT_IDX")
    private String placeFaultIDX;
    /** 处理方法编码 */
    @Column(name="METHOD_ID")
    private String methodID;
    /** 处理方法名称 */
    @Column(name="METHOD_NAME")
    private String methodName;
    /** 处理方法描述 */
    @Column(name="METHOD_DESC")
    private String methodDesc;
    /** 是否默认 1默认0非默认 */
    @Column(name="IS_DEFAULT")
    private Integer isDefault;
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name="Record_Status")
    private Integer recordStatus;
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable=false)
    private String siteID;
    /* 创建人 */
    @Column(updatable=false)
    private Long creator;
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Create_Time",updatable=false)
    private java.util.Date createTime;
    /* 修改人 */
    private Long updator;
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Update_Time")
    private java.util.Date updateTime;
    
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    public Long getCreator() {
        return creator;
    }
    
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getMethodDesc() {
        return methodDesc;
    }
    
    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }
    
    public String getMethodID() {
        return methodID;
    }
    
    public void setMethodID(String methodID) {
        this.methodID = methodID;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public String getPlaceFaultIDX() {
        return placeFaultIDX;
    }
    
    public void setPlaceFaultIDX(String placeFaultIDX) {
        this.placeFaultIDX = placeFaultIDX;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Long getUpdator() {
        return updator;
    }
    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }

    
    public Integer getIsDefault() {
        return isDefault;
    }

    
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
    
}
