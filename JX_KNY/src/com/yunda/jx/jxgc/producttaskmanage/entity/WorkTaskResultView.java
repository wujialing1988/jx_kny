package com.yunda.jx.jxgc.producttaskmanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkTaskResultView实体类, 数据表：作业任务结果查询视图
 * <li>创建人：程锐
 * <li>创建日期：2012-12-24
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "v_task_result")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WorkTaskResultView implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 状态-待领取 */
    public static final String STATUS_WAITINGFORGET = "WAITING_RECEIVE";
    
    /** 状态-待处理 */
    public static final String STATUS_WAITINGFORHANDLE = "TODO";
    
    /** 状态-已处理 */
    public static final String STATUS_HANDLED = "COMPLETE";
    
    /** 状态-终止 */
    public static final String STATUS_FINISHED = "TERMINATED";
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 作业卡主键 */
    @Column(name = "Work_Card_IDX")
    private String workCardIDX;
    
    /* 工步主键 */
    @Column(name = "Work_Step_IDX")
    private String workStepIDX;
    
    /* 作业任务编码 */
    @Column(name = "Work_Task_Code")
    private String workTaskCode;
    
    /* 作业任务名称 */
    @Column(name = "Work_Task_Name")
    private String workTaskName;
    
    /* 检修内容 */
    @Column(name = "Repair_Content")
    private String repairContent;
    
    /* 检修标准 */
    @Column(name = "Repair_Standard")
    private String repairStandard;
    
    /* 检修结果 */
    @Column(name = "Repair_Result")
    private String repairResult;
    
    /* 状态:10待领取、20待处理、30已处理、40终止 */
    private String status;
    
    /* 备注 */
    private String remarks;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "RECORD_STATUS")
    private Integer recordStatus;
    
    /* 检修方法 */
    @Column(name = "Repair_Method")
    private String repairMethod;
    
    /* 检修结果主键 */
    @Column(name = "Repair_Result_Idx")
    private String repairResultIdx;
    
    @Transient
    private String repairMethodIdx;// 预留字段，作业方法主键
    
    /* 作业结果代码 */
    @Column(name = "result_code")
    private String resultCode;
    
    /* 作业结果名称 */
    @Column(name = "result_name")
    private String resultName;
    
    /**
     * @return String 获取作业卡主键
     */
    public String getWorkCardIDX() {
        return workCardIDX;
    }
    
    public void setWorkCardIDX(String workCardIDX) {
        this.workCardIDX = workCardIDX;
    }
    
    /**
     * @return String 获取工步主键
     */
    public String getWorkStepIDX() {
        return workStepIDX;
    }
    
    public void setWorkStepIDX(String workStepIDX) {
        this.workStepIDX = workStepIDX;
    }
    
    /**
     * @return String 获取作业任务编码
     */
    public String getWorkTaskCode() {
        return workTaskCode;
    }
    
    public void setWorkTaskCode(String workTaskCode) {
        this.workTaskCode = workTaskCode;
    }
    
    /**
     * @return String 获取作业任务名称
     */
    public String getWorkTaskName() {
        return workTaskName;
    }
    
    public void setWorkTaskName(String workTaskName) {
        this.workTaskName = workTaskName;
    }
    
    /**
     * @return String 获取检修内容
     */
    public String getRepairContent() {
        return repairContent;
    }
    
    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
    }
    
    /**
     * @return String 获取检修标准
     */
    public String getRepairStandard() {
        return repairStandard;
    }
    
    public void setRepairStandard(String repairStandard) {
        this.repairStandard = repairStandard;
    }
    
    /**
     * @return String 获取检修结果
     */
    public String getRepairResult() {
        return repairResult;
    }
    
    public void setRepairResult(String repairResult) {
        this.repairResult = repairResult;
    }
    
    /**
     * @return String 获取状态
     */
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * @return String 获取备注
     */
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * @return Integer 获取记录状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    /**
     * @return String idx主键
     */
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getRepairMethod() {
        return repairMethod;
    }
    
    public void setRepairMethod(String repairMethod) {
        this.repairMethod = repairMethod;
    }
    
    public String getRepairResultIdx() {
        return repairResultIdx;
    }
    
    public void setRepairResultIdx(String repairResultIdx) {
        this.repairResultIdx = repairResultIdx;
    }
    
    public String getRepairMethodIdx() {
        return repairMethodIdx;
    }
    
    public void setRepairMethodIdx(String repairMethodIdx) {
        this.repairMethodIdx = repairMethodIdx;
    }
    
    public String getResultCode() {
        return resultCode;
    }
    
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
    
    public String getResultName() {
        return resultName;
    }
    
    public void setResultName(String resultName) {
        this.resultName = resultName;
    }
}
