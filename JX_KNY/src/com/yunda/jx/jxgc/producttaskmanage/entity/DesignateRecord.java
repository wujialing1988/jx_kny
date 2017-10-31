package com.yunda.jx.jxgc.producttaskmanage.entity;

import java.io.Serializable;

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

import com.yunda.frame.common.JXConfig;

/**
 * <li>标题：DesignateRecord
 * <li>说明：派工人员记录
 * <li>创建人：张凡
 * <li>创建时间：2013-7-17
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 *
 */
@Entity
@Table(name="jxgc_designate_record")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class DesignateRecord implements Serializable{
        
    private static final long serialVersionUID = 1L;
    /**
     * 派工类型 - 调度派工
     * <p>Value: Dispatcher
     */
    public static final String TYPE_DISPATCHER = "Dispatcher";
    /**
     * 派工类型 - 工长派工
     *<p>Value: Headman
     */
    public static final String TYPE_HEADMAN = "Headman";
    
    /**
     * 提票派工
     */
    public static final String FAULT_DISPATCHER = "fault_dispatcher";	//提票派工
    /**
     * 工单派工
     */
	public static final String WORK_CARD_DISPATCHER = "work_card_dispatcher";	//工单派工
	/**
	 * 短信功能总开关
	 */
	public static final boolean SMS_ENABLE = JXConfig.getInstance().isSmsEnable();
	/**
	 * 提票派工短信功能开关
	 */
	public static final boolean SMS_FAULT_ENABLE = JXConfig.getInstance().isSmsFaultEnable();
	/**
	 * 工单派工短信功能开关
	 */
	public static final boolean SMS_WORKCARD_ENABLE = JXConfig.getInstance().isSmsWorkCardEnable();
	/**
	 * 工长派工短信功能开关
	 */
	public static final boolean SMS_HEADMAN_ENABLE = JXConfig.getInstance().isSmsHeadManEnable();
    
    /* idx主键 */
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
    private String idx;
    /* 关联主键 */
    @Column(name="Relation_IDX")
    private String relationIdx;
    /* 派工类型 */
    @Column(name="Designate_Type")
    private String designateType;
    /* 派工人员ID */
    @Column(name="Worker_ID")
    private Long workerID;
    /* 派工人员名称 */
    @Column(name="Worker_Name")
    private String workerName;
    /* 派工人员编码 */
    @Column(name="Worker_Code")
    private String workerCode;
    /* 派工时间 */
    @Column(name="Designate_Time")
    private java.util.Date designateTime;
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name="RECORD_STATUS")
    private Integer recordStatus;
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable=false)
    private String siteID;
    /* 创建人 */
    @Column(updatable=false)
    private Long creator;
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATE_TIME",updatable=false)
    private java.util.Date createTime;
    /* 修改人 */
    private Long updator;
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UPDATE_TIME")
    private java.util.Date updateTime;
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getRelationIdx() {
        return relationIdx;
    }
    
    public void setRelationIdx(String relationIdx) {
        this.relationIdx = relationIdx;
    }
    
    public String getDesignateType() {
        return designateType;
    }
    
    public void setDesignateType(String designateType) {
        this.designateType = designateType;
    }
    
    public Long getWorkerID() {
        return workerID;
    }
    
    public void setWorkerID(Long workerID) {
        this.workerID = workerID;
    }
    
    public String getWorkerName() {
        return workerName;
    }
    
    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }
    
    public String getWorkerCode() {
        return workerCode;
    }
    
    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }
    
    public java.util.Date getDesignateTime() {
        return designateTime;
    }
    
    public void setDesignateTime(java.util.Date designateTime) {
        this.designateTime = designateTime;
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
    
    public Long getCreator() {
        return creator;
    }
    
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    public Long getUpdator() {
        return updator;
    }
    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }
}
