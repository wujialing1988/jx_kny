package com.yunda.twt.trainaccessaccount.entity;

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
 * <li>标题: 机车整备管理信息系统
 * <li>说明：TrainPositionHIS实体类, 数据表：机车位置历史信息
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
@Table(name = "TWT_Train_Position_HIS")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainPositionHIS implements java.io.Serializable {
    
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
    
    /* 设备GUID */
    private String equipGUID;
    
    /* 设备编号 */
    @Column(name = "Equip_No")
    private String equipNo;
    
    /* 设备类型 */
    @Column(name = "Equip_Class")
    private String equipClass;
    
    /* 设备名称 */
    @Column(name = "Equip_Name")
    private String equipName;
    
    /* 上设备时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Start_Time")
    private java.util.Date startTime;
    
    /* 下设备时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "End_Time")
    private java.util.Date endTime;
    
    /* 设备顺序 */
    @Transient
    private String equipOrder;
    
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
     * @return String 获取设备GUID
     */
    public String getEquipGUID() {
        return equipGUID;
    }
    
    /**
     * @param equipGUID 设置设备GUID
     */
    public void setEquipGUID(String equipGUID) {
        this.equipGUID = equipGUID;
    }
    
    /**
     * @return String 获取设备编号
     */
    public String getEquipNo() {
        return equipNo;
    }
    
    /**
     * @param equipNo 设置设备编号
     */
    public void setEquipNo(String equipNo) {
        this.equipNo = equipNo;
    }
    
    /**
     * @return String 获取设备类型
     */
    public String getEquipClass() {
        return equipClass;
    }
    
    /**
     * @param equipClass 设置设备类型
     */
    public void setEquipClass(String equipClass) {
        this.equipClass = equipClass;
    }
    
    /**
     * @return String 获取设备名称
     */
    public String getEquipName() {
        return equipName;
    }
    
    /**
     * @param equipName 设置设备名称
     */
    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }
    
    /**
     * @return java.util.Date 获取上设备时间
     */
    public java.util.Date getStartTime() {
        return startTime;
    }
    
    /**
     * @param startTime 设置上设备时间
     */
    public void setStartTime(java.util.Date startTime) {
        this.startTime = startTime;
    }
    
    /**
     * @return java.util.Date 获取下设备时间
     */
    public java.util.Date getEndTime() {
        return endTime;
    }
    
    /**
     * @param endTime 设置下设备时间
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

    
    public String getEquipOrder() {
        return equipOrder;
    }
    
    public void setEquipOrder(String equipOrder) {
        this.equipOrder = equipOrder;
    }
    
    
}
