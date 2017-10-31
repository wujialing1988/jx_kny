package com.yunda.freight.base.trainInspection.entity;

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
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检所维护实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-10 10:13:18
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_TRAIN_INSPECTION")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainInspection implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
            
    /* 列检所名称 */ 
    @Column(name = "INSPECTION_NAME")
    private java.lang.String inspectionName;            
            
    /* 等级名称 */ 
    @Column(name = "LEVEL_NAME")
    private java.lang.String levelName;            
            
    /* 列检所编码 */ 
    @Column(name = "INSPECTION_CODE")
    private java.lang.String inspectionCode;            
            
    /* 等级编码 */ 
    @Column(name = "LEVEL_CODE")
    private java.lang.String levelCode;            
            
    /* 备注 */ 
    @Column(name = "REMARK")
    private java.lang.String remark;            
    
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
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public java.lang.String getInspectionName() {
        return this.inspectionName;
    }
    
    public void setInspectionName(java.lang.String value) {
        this.inspectionName = value;
    }
    public java.lang.String getLevelName() {
        return this.levelName;
    }
    
    public void setLevelName(java.lang.String value) {
        this.levelName = value;
    }
    public java.lang.String getInspectionCode() {
        return this.inspectionCode;
    }
    
    public void setInspectionCode(java.lang.String value) {
        this.inspectionCode = value;
    }
    public java.lang.String getLevelCode() {
        return this.levelCode;
    }
    
    public void setLevelCode(java.lang.String value) {
        this.levelCode = value;
    }
    public java.lang.String getRemark() {
        return this.remark;
    }
    
    public void setRemark(java.lang.String value) {
        this.remark = value;
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
    
    
}

