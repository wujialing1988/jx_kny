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
 * <li>说明: 机车提票检查签到
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
@Table(name = "JXGC_FAULT_TICKET_CHECKSIGN")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class FaultTicketCheckSign implements java.io.Serializable {

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
    
    /* 车型主键 */
    @Column(name = "TRAIN_TYPE_IDX")
    private String trainTypeIDX;
    
    /* 车号 */
    @Column(name = "TRAIN_NO")
    private String trainNo;
    
    /* 车型英文简称 */
    @Column(name = "TRAIN_TYPE_SHORTNAME")
    private String trainTypeShortName;
    
    /* 提票类型（数据字典项：如JT6，JT28等） */
    @Column(name = "FAULT_TICKET_TYPE")
    private String faultTicketType;
    
    /* 检查签到人ID */
    @Column(name = "CHECKSIGN_EMP_ID")
    private Long checkSignEmpId;
    
    /* 检查签到人名称 */
    @Column(name = "CHECKSIGN_EMP")
    private String checkSignEmp;
    
    /* 检查签到人部门 */
    @Column(name = "CHECKSIGN_ORGID")
    private Long checkSignOrgID;
    
    /* 检查签到人部门名称 */
    @Column(name = "CHECKSIGN_ORGNAME")
    private String checkSignOrgName;
    
    /* 检查签到时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHECKSIGN_TIME")
    private java.util.Date checkSignTime;
    
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
    
    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getCheckSignEmp() {
        return checkSignEmp;
    }

    public void setCheckSignEmp(String checkSignEmp) {
        this.checkSignEmp = checkSignEmp;
    }
    
    public Long getCheckSignEmpId() {
        return checkSignEmpId;
    }

    
    public void setCheckSignEmpId(Long checkSignEmpId) {
        this.checkSignEmpId = checkSignEmpId;
    }

    
    public Long getCheckSignOrgID() {
        return checkSignOrgID;
    }

    
    public void setCheckSignOrgID(Long checkSignOrgID) {
        this.checkSignOrgID = checkSignOrgID;
    }

    
    public String getCheckSignOrgName() {
        return checkSignOrgName;
    }

    
    public void setCheckSignOrgName(String checkSignOrgName) {
        this.checkSignOrgName = checkSignOrgName;
    }

    
    public java.util.Date getCheckSignTime() {
        return checkSignTime;
    }

    
    public void setCheckSignTime(java.util.Date checkSignTime) {
        this.checkSignTime = checkSignTime;
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

    
    public String getFaultTicketType() {
        return faultTicketType;
    }

    
    public void setFaultTicketType(String faultTicketType) {
        this.faultTicketType = faultTicketType;
    }

    
    public Integer getRecordStatus() {
        return recordStatus;
    }

    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    
    public String getTrainNo() {
        return trainNo;
    }

    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }

    
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }

    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }

    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
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
    
}
