package com.yunda.freight.zb.plan.entity;

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
 * <li>说明: 中断记录表
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_PLAN_INTERRUPT")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpPlanInterrupt implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    
    /**
     * <li>说明：默认构造器
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-27
     * <li>修改人： 
     * <li>修改日期：
     */
    public ZbglRdpPlanInterrupt() {
        // TODO Auto-generated constructor stub
    }
    
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 列检计划 */
    @Column(name = "RDP_PLAN_IDX")
    private String rdpPlanIdx;
    
    /* 中断开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INTERRUPT_START_TIME")
    private java.util.Date interruptStartTime;
    
    /* 中断结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INTERRUPT_END_TIME")
    private java.util.Date interruptEndTime;
    
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

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public java.util.Date getInterruptEndTime() {
        return interruptEndTime;
    }

    
    public void setInterruptEndTime(java.util.Date interruptEndTime) {
        this.interruptEndTime = interruptEndTime;
    }

    
    public java.util.Date getInterruptStartTime() {
        return interruptStartTime;
    }

    
    public void setInterruptStartTime(java.util.Date interruptStartTime) {
        this.interruptStartTime = interruptStartTime;
    }

    
    public String getRdpPlanIdx() {
        return rdpPlanIdx;
    }

    
    public void setRdpPlanIdx(String rdpPlanIdx) {
        this.rdpPlanIdx = rdpPlanIdx;
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

    
    public Integer getRecordStatus() {
        return recordStatus;
    }


    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
}
