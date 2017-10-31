package com.yunda.freight.base.classTransfer.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 班次交接实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-18 11:35:19
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_CLASS_TRANSFER")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ClassTransfer implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
            
    /* 当前值班员id */ 
    @Column(name = "EMPID")
    private java.lang.String empid;            
            
    /* 当前值班员名称 */ 
    @Column(name = "EMPNAME")
    private java.lang.String empname;            
            
    /* 交接值班员id */ 
    @Column(name = "TRANSFER_EMPID")
    private java.lang.String transferEmpid;            
            
    /* 交接值班员名称 */ 
    @Column(name = "TRANSFER_NAME")
    private java.lang.String transferName;            
            
    /* 当前班组编码 */ 
    @Column(name = "CLASS_NO")
    private java.lang.String classNo;            
            
    /* 当前班组名称 */ 
    @Column(name = "CLASS_NAME")
    private java.lang.String className;            
            
    /* 交接班组编码 */ 
    @Column(name = "TRANSFER_CLASS_NO")
    private java.lang.String transferClassNo;            
            
    /* 交接班组名称 */ 
    @Column(name = "TRANSFER_CLASS_NAME")
    private java.lang.String transferClassName;            
    /* 交接时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRANSFER_DATE")
    private java.util.Date transferDate;
    
    /* 站点ID */
    @Column(name = "SITE_ID")
    private String siteID;
    
    /* 站点名称 */
    @Column(name = "SITE_NAME")
    private String siteName;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
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
    
    /* 详情 */
    @Transient
    private List<ClassTransferDetails> details = new ArrayList<ClassTransferDetails>();
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public java.lang.String getEmpid() {
        return this.empid;
    }
    
    public void setEmpid(java.lang.String value) {
        this.empid = value;
    }
    public java.lang.String getEmpname() {
        return this.empname;
    }
    
    public void setEmpname(java.lang.String value) {
        this.empname = value;
    }
    public java.lang.String getTransferEmpid() {
        return this.transferEmpid;
    }
    
    public void setTransferEmpid(java.lang.String value) {
        this.transferEmpid = value;
    }
    public java.lang.String getTransferName() {
        return this.transferName;
    }
    
    public void setTransferName(java.lang.String value) {
        this.transferName = value;
    }
    public java.lang.String getClassNo() {
        return this.classNo;
    }
    
    public void setClassNo(java.lang.String value) {
        this.classNo = value;
    }
    public java.lang.String getClassName() {
        return this.className;
    }
    
    public void setClassName(java.lang.String value) {
        this.className = value;
    }
    public java.lang.String getTransferClassNo() {
        return this.transferClassNo;
    }
    
    public void setTransferClassNo(java.lang.String value) {
        this.transferClassNo = value;
    }
    public java.lang.String getTransferClassName() {
        return this.transferClassName;
    }
    
    public void setTransferClassName(java.lang.String value) {
        this.transferClassName = value;
    }
    public java.util.Date getTransferDate() {
        return this.transferDate;
    }
    
    public void setTransferDate(java.util.Date value) {
        this.transferDate = value;
    }
    
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    public String getSiteName() {
        return siteName;
    }
    
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
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

    
    public List<ClassTransferDetails> getDetails() {
        return details;
    }

    
    public void setDetails(List<ClassTransferDetails> details) {
        this.details = details;
    }
}

