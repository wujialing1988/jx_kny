package com.yunda.jx.jxgc.tpmanage.entity;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车提票检查确认
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-12-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_FAULT_TICKET_AFFIRM")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class FaultTicketCheckAffirm implements java.io.Serializable {

    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @Id 
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 机车检修作业计划主键 */
    @Column(name = "WORK_PLAN_IDX")
    private String workPlanIDX;
    
    /* 机车提票主键 */
    @Column(name = "FAULT_TICKET_IDX")
    private String faultTicketIDX;
    
    /* 机车提票类型 */
    @Column(name = "FAULT_TICKET_TYPE")
    private String faultTicketType;
    
    /* 确认人ID */
    @Column(name = "AFFIRM_EMP_ID")
    private Long affirmEmpId;
    
    /* 确认人名称 */
    @Column(name = "AFFIRM_EMP")
    private String affirmEmp;

    /* 确认时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AFFIRM_TIME")
    private java.util.Date affirmTime;
    
    /* 确认原因 */
    @Column(name = "AFFIRM_REASON")
    private String affirmReason;
    
    /* 确认角色 */
    @Column(name = "AFFIRM_ROLENAME")
    private String affirmRoleName;
    
    
    /* 确认状态 1：确认，2：验收*/
    @Column(name = "STATUS_AFFIRM")
    private Integer statusAffirm;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
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

    
    public String getAffirmEmp() {
        return affirmEmp;
    }

    
    public void setAffirmEmp(String affirmEmp) {
        this.affirmEmp = affirmEmp;
    }

    
    public Long getAffirmEmpId() {
        return affirmEmpId;
    }

    
    public void setAffirmEmpId(Long affirmEmpId) {
        this.affirmEmpId = affirmEmpId;
    }

    
    public java.util.Date getAffirmTime() {
        return affirmTime;
    }

    
    public void setAffirmTime(java.util.Date affirmTime) {
        this.affirmTime = affirmTime;
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

    
    public String getFaultTicketIDX() {
        return faultTicketIDX;
    }

    
    public void setFaultTicketIDX(String faultTicketIDX) {
        this.faultTicketIDX = faultTicketIDX;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public Integer getRecordStatus() {
        return recordStatus;
    }

    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
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

    
    public String getWorkPlanIDX() {
        return workPlanIDX;
    }

    
    public void setWorkPlanIDX(String workPlanIDX) {
        this.workPlanIDX = workPlanIDX;
    }

    public Integer getStatusAffirm() {
        return statusAffirm;
    }

    public void setStatusAffirm(Integer statusAffirm) {
        this.statusAffirm = statusAffirm;
    }

    
    public String getFaultTicketType() {
        return faultTicketType;
    }

    public void setFaultTicketType(String faultTicketType) {
        this.faultTicketType = faultTicketType;
    }


    public String getAffirmReason() {
        return affirmReason;
    }

    
    public void setAffirmReason(String affirmReason) {
        this.affirmReason = affirmReason;
    }


    
    public String getAffirmRoleName() {
        return affirmRoleName;
    }


    
    public void setAffirmRoleName(String affirmRoleName) {
        this.affirmRoleName = affirmRoleName;
    }
    
    
    
}
