package com.yunda.jx.jxgc.repairrequirement.entity;

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
 * <li>说明: 作业项对应的处理结果 实体类
 * <li>创建人：程锐
 * <li>创建日期：2013-5-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_Work_Step_Result")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkStepResult {
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /** 是否默认的处理结果 是 */
    public static final Integer ISDEFAULT_YES = 1;
    /** 是否默认的处理结果 否 */
    public static final Integer ISDEFAULT_NO = 0;
    /** idx主键 */
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
    private String idx;
    /** 工步主键 */
    @Column(name="Work_Step_IDX")
    private String workStepIDX;
    /** 处理结果编码 */
    @Column(name="Result_Code")
    private String resultCode;
    /** 处理结果名称 */
    @Column(name="Result_Name")
    private String resultName;
    /** 是否默认的处理结果，1：是；0：否 */
    @Column(name="Is_Default")
    private Integer isDefault;
    /** 业务状态：10新增；20启用；30作废 */
    private Integer status;
    /** 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name="Record_Status")
    private Integer recordStatus;
    /** 站点标识，为了同步数据而使用 */
    @Column(updatable=false)
    private String siteID;
    /** 创建人 */
    @Column(updatable=false)
    private Long creator;
    /** 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Create_Time",updatable=false)
    private java.util.Date createTime;
    /** 修改人 */
    private Long updator;
    /** 修改时间 */
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
    
    public Integer getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getResultCode() {
        return resultCode;
    }
    
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
    
    public String getResultName() {
        return resultName;
    }
    
    public void setResultName(String resultName) {
        this.resultName = resultName;
    }
    
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
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
    
    public String getWorkStepIDX() {
        return workStepIDX;
    }
    
    public void setWorkStepIDX(String workStepIDX) {
        this.workStepIDX = workStepIDX;
    }
    
    public WorkStepResult(String resultName){
        this.resultName = resultName;
    }
    public WorkStepResult(){}
}
