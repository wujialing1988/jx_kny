package com.yunda.flow.snaker.entity;

import java.util.Date;

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
 * <li>标题: 流程审批记录表
 * <li>说明: 类的功能描述
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-9-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "SNAKER_APPROVAL_RECORD")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class SnakerApprovalRecord implements java.io.Serializable {

    /**  序列化  */
    private static final long serialVersionUID = 1L;
    
    /** 审批类型 01通过 02拒绝 03驳回 */
    public static final String OPINION_TYPE_APPROVE = "01" ;
    
    public static final String OPINION_TYPE_REFUSE = "02" ;
    
    public static final String OPINION_TYPE_REJECT = "03" ;
    
    /** 主键 */
    @GenericGenerator(strategy="uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator="uuid_id_generator")
    @Column(name="IDX")
    private String idx;
    
    /** 流程实例ID */
    @Column(name="PROCESS_INST_ID")
    private String processInstID ;
    
    /** 业务主键ID */
    @Column(name="BUSINESS_ID")
    private String businessID ;
    
    /** 任务ID */
    @Column(name="TASK_ID")
    private String taskID ;
    
    /** 任务名称 */
    @Column(name="TASK_NAME")
    private String taskName ;
    
    /** 审批意见类型 01通过 02拒绝 03驳回 */
    @Column(name="OPINION_TYPE")
    private String opinionType ;
    
    /** 审批意见 */
    @Column(name="OPINIONS")
    private String opinions ;
    
    /** 审批时间 **/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPROVAL_DATE", updatable = false)
    private Date approvalDate ;
    
    /** 审批人ID */
    @Column(name="APPROVAL_USER_ID")
    private String approvalUserID ;
    
    /** 审批人姓名 */
    @Column(name="APPROVAL_USER_NAME")
    private String approvalUserName ;

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    
    public String getApprovalUserID() {
        return approvalUserID;
    }

    
    public void setApprovalUserID(String approvalUserID) {
        this.approvalUserID = approvalUserID;
    }

    
    public String getApprovalUserName() {
        return approvalUserName;
    }

    
    public void setApprovalUserName(String approvalUserName) {
        this.approvalUserName = approvalUserName;
    }

    
    public String getBusinessID() {
        return businessID;
    }

    
    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    
    public String getOpinions() {
        return opinions;
    }

    
    public void setOpinions(String opinions) {
        this.opinions = opinions;
    }

    
    public String getOpinionType() {
        return opinionType;
    }

    
    public void setOpinionType(String opinionType) {
        this.opinionType = opinionType;
    }

    
    public String getProcessInstID() {
        return processInstID;
    }

    
    public void setProcessInstID(String processInstID) {
        this.processInstID = processInstID;
    }

    
    public String getTaskID() {
        return taskID;
    }

    
    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    
    public String getTaskName() {
        return taskName;
    }

    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
