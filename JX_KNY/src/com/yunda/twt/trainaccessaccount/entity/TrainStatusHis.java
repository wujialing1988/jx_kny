package com.yunda.twt.trainaccessaccount.entity;

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
 * <li>说明：TrainStatusHis实体类, 数据表：机车状态历史信息
 * <li>创建人：程锐
 * <li>创建日期：2015-01-26
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "TWT_Train_Status_His")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainStatusHis implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 机车出入段台账主键 */
    @Column(name = "Train_Access_Account_IDX")
    private String trainAccessAccountIDX;
    
    /* 机车状态 */
    private String status;
    
    /* 状态超时时长 */
    @Column(name = "Status_OverTime")
    private Integer statusOverTime;
    
    /* 状态开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Start_Time")
    private java.util.Date startTime;
    
    /* 状态结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "End_Time")
    private java.util.Date endTime;
    
    /**
     * @return String 获取机车出入段台账主键
     */
    public String getTrainAccessAccountIDX() {
        return trainAccessAccountIDX;
    }
    
    /**
     * @param trainAccessAccountIDX 设置机车出入段台账主键
     */
    public void setTrainAccessAccountIDX(String trainAccessAccountIDX) {
        this.trainAccessAccountIDX = trainAccessAccountIDX;
    }
    
    /**
     * @return String 获取机车状态
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * @param status 设置机车状态
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * @return Integer 获取状态超时时长
     */
    public Integer getStatusOverTime() {
        return statusOverTime;
    }
    
    /**
     * @param statusOverTime 设置状态超时时长
     */
    public void setStatusOverTime(Integer statusOverTime) {
        this.statusOverTime = statusOverTime;
    }
    
    /**
     * @return java.util.Date 获取状态开始时间
     */
    public java.util.Date getStartTime() {
        return startTime;
    }
    
    /**
     * @param startTime 设置状态开始时间
     */
    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }
    
    /**
     * @return java.util.Date 获取状态结束时间
     */
    public java.util.Date getEndTime() {
        return endTime;
    }
    
    /**
     * @param endTime 设置状态结束时间
     */
    public void setEndTime(java.util.Date endTime) {
        this.endTime = endTime;
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
