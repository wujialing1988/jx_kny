package com.yunda.jx.jczl.attachmanage.entity;

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
 * <li>说明: 车辆状态流转实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-05-18 15:23:13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_TRAIN_STATUS_CHANGE")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainStatusChange implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 启动列检 */
    public static final String START_LIEJIAN = "START_LIEJIAN" ;
    
    /* 终止列检 */
    public static final String DEL_LIEJIAN = "DEL_LIEJIAN" ;
    
    /* 完成列检 */
    public static final String COM_LIEJIAN = "COM_LIEJIAN" ;
    
    /* 扣车登记 */
    public static final String START_DETAIN = "START_DETAIN" ;
    
    /* 删除扣车登记 */
    public static final String DEL_DETAIN = "DEL_DETAIN" ;
    
    /* 启动检修 */
    public static final String START_JX = "START_JX" ;
    
    /* 终止检修 */
    public static final String DEL_JX = "DEL_JX" ;
    
    /* 完成检修 */
    public static final String COM_JX = "COM_JX" ;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
            
    /* 车辆主键 */ 
    @Column(name = "TRAIN_IDX")
    private java.lang.String trainIdx;            
            
    /* 车型ID */ 
    @Column(name = "TRAIN_TYPE_IDX")
    private java.lang.String trainTypeIdx;            
            
    /* 车型 */ 
    @Column(name = "TRAIN_TYPE_SHORTNAME")
    private java.lang.String trainTypeShortname;            
            
    /* 车号 */ 
    @Column(name = "TRAIN_NO")
    private java.lang.String trainNo;            
            
    /* 客货类型 10 货车 20 客车 */ 
    @Column(name = "T_VEHICLE_TYPE")
    private java.lang.String vehicleType;            
            
    /* 状态 */ 
    @Column(name = "TRAIN_STATE")
    private Integer trainState;            
    /* 记录时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECORD_TIME")
    private java.util.Date recordTime;
            
    /* 业务ID */ 
    @Column(name = "BUSINESS_IDX")
    private java.lang.String businessIdx;            
            
    /* 业务环节 */ 
    @Column(name = "BUSINESS_NAME")
    private java.lang.String businessName;            
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public java.lang.String getTrainIdx() {
        return this.trainIdx;
    }
    
    public void setTrainIdx(java.lang.String value) {
        this.trainIdx = value;
    }
    public java.lang.String getTrainTypeIdx() {
        return this.trainTypeIdx;
    }
    
    public void setTrainTypeIdx(java.lang.String value) {
        this.trainTypeIdx = value;
    }
    public java.lang.String getTrainTypeShortname() {
        return this.trainTypeShortname;
    }
    
    public void setTrainTypeShortname(java.lang.String value) {
        this.trainTypeShortname = value;
    }
    public java.lang.String getTrainNo() {
        return this.trainNo;
    }
    
    public void setTrainNo(java.lang.String value) {
        this.trainNo = value;
    }
    public java.lang.String getVehicleType() {
        return this.vehicleType;
    }
    
    public void setVehicleType(java.lang.String value) {
        this.vehicleType = value;
    }
    public Integer getTrainState() {
        return this.trainState;
    }
    
    public void setTrainState(Integer value) {
        this.trainState = value;
    }
    public java.util.Date getRecordTime() {
        return this.recordTime;
    }
    
    public void setRecordTime(java.util.Date value) {
        this.recordTime = value;
    }
    public java.lang.String getBusinessIdx() {
        return this.businessIdx;
    }
    
    public void setBusinessIdx(java.lang.String value) {
        this.businessIdx = value;
    }
    public java.lang.String getBusinessName() {
        return this.businessName;
    }
    
    public void setBusinessName(java.lang.String value) {
        this.businessName = value;
    }
    
}

