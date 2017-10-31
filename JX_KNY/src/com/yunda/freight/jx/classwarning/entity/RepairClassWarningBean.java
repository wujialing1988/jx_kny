package com.yunda.freight.jx.classwarning.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 查询实体
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
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class RepairClassWarningBean implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id
    @Column(name = "IDX")
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
    
    /* 累计走形公里 */
    @Column(name="TOTALRM")
    private Float totalrm;
   
    /* 最小走行公里 */
    @Column(name="MIN_RUNNING_KM")
    private Float minRunningKm;
    
    /* 最大走行公里 */
    @Column(name="MAX_RUNNING_KM")
    private Float maxRunningKm;
    
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

    
    public Float getMaxRunningKm() {
        return maxRunningKm;
    }

    
    public void setMaxRunningKm(Float maxRunningKm) {
        this.maxRunningKm = maxRunningKm;
    }

    
    public Float getMinRunningKm() {
        return minRunningKm;
    }

    
    public void setMinRunningKm(Float minRunningKm) {
        this.minRunningKm = minRunningKm;
    }

    
    public Float getTotalrm() {
        return totalrm;
    }

    
    public void setTotalrm(Float totalrm) {
        this.totalrm = totalrm;
    }
    
}

