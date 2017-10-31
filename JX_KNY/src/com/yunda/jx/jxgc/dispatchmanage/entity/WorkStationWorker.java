package com.yunda.jx.jxgc.dispatchmanage.entity;

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
 * <li>说明：WorkStationWorker实体类, 数据表：工位作业人员
 * <li>创建人：程锐
 * <li>创建日期：2012-12-07
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_WORK_STATION_WORKER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WorkStationWorker implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 新增状态 */
    public static final int NEW_STATUS = 10;
    
    /* 启用状态 */
    public static final int USE_STATUS = 20;
    
    /* 作废状态 */
    public static final int NULLIFY_STATUS = 30;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 工位主键 */
    @Column(name = "WORK_STATION_IDX")
    private String workStationIDX;
    
    /* 工位名称 */
    @Column(name = "WORK_STATION_NAME")
    private String workStationName;
    
    /* 工作人员ID */
    @Column(name = "Worker_ID")
    private Long workerID;
    
    /* 工作人员名称 */
    @Column(name = "Worker_Name")
    private String workerName;
    
    /* 工作人员编码 */
    @Column(name = "Worker_Code")
    private String workerCode;
    
    /* 状态，10：新增；20：启用；30：作废 */
    private Integer status;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME", updatable = false)
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private java.util.Date updateTime;
    
    /**
     * @return 获取工位主键
     */
    public String getWorkStationIDX() {
        return workStationIDX;
    }
    
    /**
     * @param workStationIDX 设置工位主键
     */
    public void setWorkStationIDX(String workStationIDX) {
        this.workStationIDX = workStationIDX;
    }
    
    /**
     * @return 获取工位名称
     */
    public String getWorkStationName() {
        return workStationName;
    }
    
    /**
     * @param workStationName 设置工位名称
     */
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }
    
    /**
     * @return Long 获取工作人员ID
     */
    public Long getWorkerID() {
        return workerID;
    }
    
    /**
     * @param workerID 设置工作人员ID
     */
    public void setWorkerID(Long workerID) {
        this.workerID = workerID;
    }
    
    /**
     * @return String 获取工作人员名称
     */
    public String getWorkerName() {
        return workerName;
    }
    
    /**
     * @param workerName 设置工作人员名称
     */
    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }
    
    /**
     * @return String 获取工作人员编码
     */
    public String getWorkerCode() {
        return workerCode;
    }
    
    /**
     * @param workerCode 设置工作人员编码
     */
    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }
    
    /**
     * @return Integer 获取状态
     */
    public Integer getStatus() {
        return status;
    }
    
    /**
     * @param status 设置状态
     */
    public void setStatus(Integer status) {
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
     * @return String 获取站点标识
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @param siteID 设置站点标识
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
     * @return java.util.Date 获取修改时间
     */
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * @param updateTime 设置修改时间
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
