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
 * <li>说明：TrainOrderHIS实体类, 数据表：机车位置顺序历史信息
 * <li>创建人：程锐
 * <li>创建日期：2015-02-06
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "TWT_Train_Order_HIS")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainOrderHIS implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 机车出入段台账位置历史主键 */
    @Column(name = "Train_Position_HIS_IDX")
    private String trainPositionHISIDX;
    
    /* 设备顺序 */
    private String equipOrder;
    
    /* 上设备时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Start_Time")
    private java.util.Date startTime;
    
    /* 下设备时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "End_Time")
    private java.util.Date endTime;
    
    /**
     * @return String 获取位置历史信息主键
     */
    public String getTrainPositionHISIDX() {
        return trainPositionHISIDX;
    }
    
    /**
     * @param trainPositionHISIDX 设置位置历史信息主键
     */
    public void setTrainPositionHISIDX(String trainPositionHISIDX) {
        this.trainPositionHISIDX = trainPositionHISIDX;
    }
    
    /**
     * @return String 获取设备顺序
     */
    public String getEquipOrder() {
        return equipOrder;
    }
    
    /**
     * @param equipOrder 设置设备顺序
     */
    public void setEquipOrder(String equipOrder) {
        this.equipOrder = equipOrder;
    }
    
    /**
     * @return java.util.Date 获取开始时间
     */
    public java.util.Date getStartTime() {
        return startTime;
    }
    
    /**
     * @param startTime 设置开始时间
     */
    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }
    
    /**
     * @return java.util.Date 获取结束时间
     */
    public java.util.Date getEndTime() {
        return endTime;
    }
    
    /**
     * @param endTime 设置结束时间
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
