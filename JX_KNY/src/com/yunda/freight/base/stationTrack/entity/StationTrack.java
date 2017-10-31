package com.yunda.freight.base.stationTrack.entity;

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
 * <li>说明: 股道维护实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-04-10 11:36:09
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_STATION_TRACK")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class StationTrack implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
            
    /* 列检所 */ 
    @Column(name = "INSPECTION_IDX")
    private java.lang.String inspectionIdx;            
            
    /* 股道名称 */ 
    @Column(name = "TRACK_NAME")
    private java.lang.String trackName;            
            
    /* 股道编码 */ 
    @Column(name = "TRACK_CODE")
    private java.lang.String trackCode;            
            
    /* 排序 */ 
    @Column(name = "SEQ_NO")
    private Integer seqNo;            
    
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
    
    public java.lang.String getInspectionIdx() {
        return this.inspectionIdx;
    }
    
    public void setInspectionIdx(java.lang.String value) {
        this.inspectionIdx = value;
    }
    public java.lang.String getTrackName() {
        return this.trackName;
    }
    
    public void setTrackName(java.lang.String value) {
        this.trackName = value;
    }
    public java.lang.String getTrackCode() {
        return this.trackCode;
    }
    
    public void setTrackCode(java.lang.String value) {
        this.trackCode = value;
    }
    public Integer getSeqNo() {
        return this.seqNo;
    }
    
    public void setSeqNo(Integer value) {
        this.seqNo = value;
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

