package com.yunda.freight.jx.classwarning.entity;

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
 * <li>说明: 修程预警实体
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-05-04 15:14:11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_REPAIR_CLASS_WARNING")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class RepairClassWarning implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
            
    /* 车型ID */ 
    @Column(name = "TRAIN_TYPE_IDX")
    private java.lang.String trainTypeIdx;            
            
    /* 车型 */ 
    @Column(name = "TRAIN_TYPE")
    private java.lang.String trainType;            
            
    /* 车号 */ 
    @Column(name = "TRAIN_NO")
    private java.lang.String trainNo;            
            
    /* 修程 */ 
    @Column(name = "REPAIR_CLASS")
    private java.lang.String repairClass;            
            
    /* 修程名称 */ 
    @Column(name = "REPAIR_CLASS_NAME")
    private java.lang.String repairClassName;            
            
    /* 修次 */ 
    @Column(name = "REPAIR_ORDER")
    private java.lang.String repairOrder;            
            
    /* 修次名称 */ 
    @Column(name = "REPAIR_ORDER_NAME")
    private java.lang.String repairOrderName;            
    /* 最近预警时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_WARNING_DATE")
    private java.util.Date lastWarningDate;
            
    /* 是否终止(0为表示未终止；1表示终止) */ 
    @Column(name = "IS_END")
    private java.lang.Boolean isEnd;            
            
    /* 终止方式 10 人为终止 20 自动终止 */ 
    @Column(name = "END_TYPE")
    private java.lang.String endType;            
            
    /* 客货类型 10 货车 20 客车 */ 
    @Column(name = "T_VEHICLE_TYPE")
    private java.lang.String vehicleType;            
    /* 预警生成时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME")
    private java.util.Date startTime;
    /* 预警终止时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
    private java.util.Date endTime;
            
    /* 临近或超限值（最大限减去当前累计公里或天数） */ 
    @Column(name = "LIMIT_VALUE")
    private Long limitValue;            
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public java.lang.String getTrainTypeIdx() {
        return this.trainTypeIdx;
    }
    
    public void setTrainTypeIdx(java.lang.String value) {
        this.trainTypeIdx = value;
    }
    public java.lang.String getTrainType() {
        return this.trainType;
    }
    
    public void setTrainType(java.lang.String value) {
        this.trainType = value;
    }
    public java.lang.String getTrainNo() {
        return this.trainNo;
    }
    
    public void setTrainNo(java.lang.String value) {
        this.trainNo = value;
    }
    public java.lang.String getRepairClass() {
        return this.repairClass;
    }
    
    public void setRepairClass(java.lang.String value) {
        this.repairClass = value;
    }
    public java.lang.String getRepairClassName() {
        return this.repairClassName;
    }
    
    public void setRepairClassName(java.lang.String value) {
        this.repairClassName = value;
    }
    public java.lang.String getRepairOrder() {
        return this.repairOrder;
    }
    
    public void setRepairOrder(java.lang.String value) {
        this.repairOrder = value;
    }
    public java.lang.String getRepairOrderName() {
        return this.repairOrderName;
    }
    
    public void setRepairOrderName(java.lang.String value) {
        this.repairOrderName = value;
    }
    public java.util.Date getLastWarningDate() {
        return this.lastWarningDate;
    }
    
    public void setLastWarningDate(java.util.Date value) {
        this.lastWarningDate = value;
    }
    public java.lang.Boolean getIsEnd() {
        return this.isEnd;
    }
    
    public void setIsEnd(java.lang.Boolean value) {
        this.isEnd = value;
    }
    public java.lang.String getEndType() {
        return this.endType;
    }
    
    public void setEndType(java.lang.String value) {
        this.endType = value;
    }
    public java.lang.String getVehicleType() {
        return this.vehicleType;
    }
    
    public void setVehicleType(java.lang.String value) {
        this.vehicleType = value;
    }
    public java.util.Date getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(java.util.Date value) {
        this.startTime = value;
    }
    public java.util.Date getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(java.util.Date value) {
        this.endTime = value;
    }
    public Long getLimitValue() {
        return this.limitValue;
    }
    
    public void setLimitValue(Long value) {
        this.limitValue = value;
    }
    
}

