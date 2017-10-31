package com.yunda.zb.rdp.zbbill.entity;

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
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglRdpTempRepair实体类, 数据表：转临修
 * <li>创建人：程锐
 * <li>创建日期：2015-01-28
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_RDP_TempRepair")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpTempRepair implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 整备单ID */
    @Column(name = "RDP_IDX")
    private String rdpIDX;
    
    /* 转临修人编码 */
    @Column(name = "Handle_Person_ID")
    private Long handlePersonID;
    
    /* 转临修人名称 */
    @Column(name = "Handle_Person_Name")
    private String handlePersonName;
    
    /* 转临修时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Handle_Time")
    private java.util.Date handleTime;
    
    /* 转临修原因 */
    @Column(name = "Handle_Reason")
    private String handleReason;
    
    /* 临修处理班组代码 */
    @Column(name = "Handle_OrgID")
    private Long handleOrgID;
    
    /* 临修处理班组名称 */
    @Column(name = "Handle_OrgName")
    private String handleOrgName;
    
    /* 临修处理班组序列 */
    @Column(name = "Handle_OrgSeq")
    private String handleOrgSeq;
    
    /* 记录状态，1：删除；0：未删除； */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 最新更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /**
     * @return String 获取整备单ID
     */
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    /**
     * @param rdpIDX 设置整备单ID
     */
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
    /**
     * @return Long 获取转临修人编码
     */
    public Long getHandlePersonID() {
        return handlePersonID;
    }
    
    /**
     * @param handlePersonID 设置转临修人编码
     */
    public void setHandlePersonID(Long handlePersonID) {
        this.handlePersonID = handlePersonID;
    }
    
    /**
     * @return String 获取转临修人名称
     */
    public String getHandlePersonName() {
        return handlePersonName;
    }
    
    /**
     * @param handlePersonName 设置转临修人名称
     */
    public void setHandlePersonName(String handlePersonName) {
        this.handlePersonName = handlePersonName;
    }
    
    /**
     * @return java.util.Date 获取转临修时间
     */
    public java.util.Date getHandleTime() {
        return handleTime;
    }
    
    /**
     * @param handleTime 设置转临修时间
     */
    public void setHandleTime(java.util.Date handleTime) {
        this.handleTime = handleTime;
    }
    
    /**
     * @return String 获取转临修原因
     */
    public String getHandleReason() {
        return handleReason;
    }
    
    /**
     * @param handleReason 设置转临修原因
     */
    public void setHandleReason(String handleReason) {
        this.handleReason = handleReason;
    }
    
    /**
     * @return Long 获取临修处理班组代码
     */
    public Long getHandleOrgID() {
        return handleOrgID;
    }
    
    /**
     * @param handleOrgID 设置临修处理班组代码
     */
    public void setHandleOrgID(Long handleOrgID) {
        this.handleOrgID = handleOrgID;
    }
    
    /**
     * @return String 获取临修处理班组名称
     */
    public String getHandleOrgName() {
        return handleOrgName;
    }
    
    /**
     * @param handleOrgName 设置临修处理班组名称
     */
    public void setHandleOrgName(String handleOrgName) {
        this.handleOrgName = handleOrgName;
    }
    
    /**
     * @return String 获取临修处理班组序列
     */
    public String getHandleOrgSeq() {
        return handleOrgSeq;
    }
    
    /**
     * @param handleOrgSeq 设置临修处理班组序列
     */
    public void setHandleOrgSeq(String handleOrgSeq) {
        this.handleOrgSeq = handleOrgSeq;
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
     * @return java.util.Date 获取最新更新时间
     */
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * @param updateTime 设置最新更新时间
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
