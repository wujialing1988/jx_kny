package com.yunda.jx.jxgc.workplanmanage.entity;
import java.util.Date;

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
 * <li>说明: 类的功能描述
 * <li>创建人：张迪
 * <li>创建日期：2017-1-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_JOB_PROCESS_NODE_APPLY")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JobProcessNodeUpdateApply implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    /** 状态 - 取消 */
    public static final Integer EDIT_STATUS_UN = 2;
    /** 状态 - 待确认 */
    public static final Integer EDIT_STATUS_WAIT = 1;
    /** 状态 -  确认*/
    public static final Integer EDIT_STATUS_ON = 0;
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;

    /* 作业流程节点主键 */
    @Column(name = "Node_IDX")
    private String nodeIDX;
 
    /* 节点名称 */
    @Column(name = "Node_Name")
    private String nodeName;
    /* 申请原因 */
    @Column(name = "REAAON")
    private String reason;
    
    /** 审核意见 */
    @Column(name = "OPINIONS")
    private String opinions;
    
    /** 审核时间 **/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPROVAL_DATE", updatable = false)
    private Date approvalDate ;
    
    /** 审核人ID */
    @Column(name="APPROVAL_EMP_ID")
    private Long  approvalEmpID ;
    
    /** 审核人姓名 */
    @Column(name="APPROVAL_EMP_NAME")
    private String approvalEmpName ;
    
    /* 计划开工时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Plan_Begin_Time")
    private java.util.Date planBeginTime;
    
    /* 计划完工时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Plan_End_Time")
    private java.util.Date planEndTime;
    
    /* 计划开工时间 --- 修改后待确认时间*/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "new_Plan_Begin_Time")
    private java.util.Date newPlanBeginTime;
    
    /* 计划完工时间 -- 修改后待确认时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "new_Plan_End_Time")
    private java.util.Date newPlanEndTime;
    
    /* 状态:0 确认、1 待确认 */
    @Column(name = "Edit_Status")
    private Integer editStatus;
    
    @Transient
    private String editStatusStr;
    
    /* 修改人 */
    private Long updator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    @Transient
    private String empName;
    
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
   
   
   
    /**
     * @return String 获取作业流程节点主键
     */
    public String getNodeIDX() {
        return nodeIDX;
    }
    
    /**
     * @param nodeIDX 设置作业流程节点主键
     */
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }
    
   
    /**
     * @return String 获取节点名称
     */
    public String getNodeName() {
        return nodeName;
    }
    
    /**
     * @param nodeName 设置节点名称
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
   
    
    /**
     * @return java.util.Date 获取计划开工时间
     */
    public java.util.Date getPlanBeginTime() {
        return planBeginTime;
    }
    
    /**
     * @param planBeginTime 设置计划开工时间
     */
    public void setPlanBeginTime(java.util.Date planBeginTime) {
        this.planBeginTime = planBeginTime;
    }
    
    /**
     * @return java.util.Date 获取计划完工时间
     */
    public java.util.Date getPlanEndTime() {
        return planEndTime;
    }
    
    /**
     * @param planEndTime 设置计划完工时间
     */
    public void setPlanEndTime(java.util.Date planEndTime) {
        this.planEndTime = planEndTime;
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

        
    public Integer getEditStatus() {
        return editStatus;
    }


    
    public void setEditStatus(Integer editStatus) {
        this.editStatus = editStatus;
    }

    
    public java.util.Date getNewPlanBeginTime() {
        return newPlanBeginTime;
    }

    
    public void setNewPlanBeginTime(java.util.Date newPlanBeginTime) {
        this.newPlanBeginTime = newPlanBeginTime;
    }

    
    public java.util.Date getNewPlanEndTime() {
        return newPlanEndTime;
    }

    
    public void setNewPlanEndTime(java.util.Date newPlanEndTime) {
        this.newPlanEndTime = newPlanEndTime;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getReason() {
        return reason;
    }

    
    public void setReason(String reason) {
        this.reason = reason;
    }

    
    public String getOpinions() {
        return opinions;
    }

    
    public void setOpinions(String opinions) {
        this.opinions = opinions;
    }

    
    public Date getApprovalDate() {
        return approvalDate;
    }

    
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    
    public Long getApprovalEmpID() {
        return approvalEmpID;
    }

    
    public void setApprovalEmpID(Long approvalEmpID) {
        this.approvalEmpID = approvalEmpID;
    }

    
    public String getApprovalEmpName() {
        return approvalEmpName;
    }

    
    public void setApprovalEmpName(String approvalEmpName) {
        this.approvalEmpName = approvalEmpName;
    }

    
    public String getEditStatusStr() {
        return editStatusStr;
    }

    
    public void setEditStatusStr(String editStatusStr) {
        this.editStatusStr = editStatusStr;
    }
    
    public String getEmpName() {
        return empName;
    }

    
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    
  
    
}
