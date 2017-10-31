package com.yunda.zb.tp.entity;

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
 * <li>说明：VZbglAndFaultTp实体类, 数据表：整备提票和检修提票整合视图
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-10-13
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "V_ZB_ZBGL_FAULT_JT6")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TZbglAndFaultTp implements java.io.Serializable {
    
    /**
     * 序列号
     */
    private static final long serialVersionUID = 1L;

    /** 提票来源：整备 */
    public static final int model_type_zb = 1;
    
    /** 提票来源：修程修 */
    public static final int model_type_fault = 2;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 车型编码 */
    @Column(name = "TRAIN_TYPE_IDX")
    private String trainTypeIDX;
    
    /* 车型拼音码 */
    @Column(name = "TRAIN_TYPE_SHORTNAME")
    private String trainTypeShortName;
    
    /* 车号 */
    @Column(name = "TRAIN_NO")
    private String trainNo;
    
    /* 故障发生日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FAULT_OCCUR_DATE")
    private java.util.Date faultOccurDate;
    
    /* 系统分类编码 */
    @Column(name = "FAULT_FIX_FULLCODE")
    private String faultFixFullCode;
    
    /* 故障部件全名 */
    @Column(name = "FAULT_FIX_FULLNAME")
    private String faultFixFullName;
    
    /* 故障ID */
    @Column(name = "FAULT_ID")
    private String faultID;
    
    /* 故障现象 */
    @Column(name = "FAULT_NAME")
    private String faultName;
    
    /* 故障描述 */
    @Column(name = "FAULT_DESC")
    private String faultDesc;
    
    /* 故障原因 */
    @Column(name = "FAULT_REASON")
    private String faultReason;
    
    /* 专业类型ID */
    @Column(name = "PROFESSIONAL_TYPE_IDX")
    private String professionalTypeIdx;
    
    /* 专业类型名称 */
    @Column(name = "PROFESSIONAL_TYPE_NAME")
    private String professionalTypeName;
    
    /* 提票单号 */
    @Column(name = "FAULT_NOTICE_CODE")
    private String faultNoticeCode;
    
    /* 提票人编码 */
    @Column(name = "NOTICE_PERSON_ID")
    private Long noticePersonId;
    
    /* 提票人名称 */
    @Column(name = "NOTICE_PERSON_NAME")
    private String noticePersonName;
    
    /* 提票时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "NOTICE_TIME")
    private java.util.Date noticeTime;
    
    /* 施修方法 */
    @Column(name = "METHOD_DESC")
    private String methodDesc;
    
    /* 处理结果，1：修复；2：观察运用；3：转JT28；4：转临修；5：返本段修；6：扣车等件； */
    @Column(name = "REPAIR_RESULT")
    private Integer repairResult;
    
    /* 处理描述 */
    @Column(name = "REPAIR_DESC")
    private String repairDesc;
    
    /* 销票人名称 */
    @Column(name = "HANDLE_PERSON_NAME")
    private String handlePersonName;
    
    /* 销票时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HANDLE_TIME")
    private java.util.Date handleTime;
    
    /* 检修类型: 10：碎修；20临修； */
    @Column(name = "REPAIR_CLASS")
    private String repairClass;
    
    /* 最新更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private java.util.Date updateTime;
    
    /* 提票类型 */
    @Column(name = "TICKET_TYPE")
    private String ticketType;
    
    /* 模块类型 1 整备 2检修 */
    @Column(name = "MODEL_TYPE")
    private Integer modelType ;
    
    //综合统计计数
    @Transient
    private String zbthtjCount;
    
    /*提票开始时间*/
    @Transient
    private String startDate;
    
    /*提票结束时间*/
    @Transient 
    private String overDate;
    
    public String getFaultDesc() {
        return faultDesc;
    }

    
    public void setFaultDesc(String faultDesc) {
        this.faultDesc = faultDesc;
    }

    
    public String getFaultFixFullCode() {
        return faultFixFullCode;
    }

    
    public void setFaultFixFullCode(String faultFixFullCode) {
        this.faultFixFullCode = faultFixFullCode;
    }

    
    public String getFaultFixFullName() {
        return faultFixFullName;
    }

    
    public void setFaultFixFullName(String faultFixFullName) {
        this.faultFixFullName = faultFixFullName;
    }

    
    public String getFaultID() {
        return faultID;
    }

    
    public void setFaultID(String faultID) {
        this.faultID = faultID;
    }

    
    public String getFaultName() {
        return faultName;
    }

    
    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }

    
    public String getFaultNoticeCode() {
        return faultNoticeCode;
    }

    
    public void setFaultNoticeCode(String faultNoticeCode) {
        this.faultNoticeCode = faultNoticeCode;
    }

    
    public java.util.Date getFaultOccurDate() {
        return faultOccurDate;
    }

    
    public void setFaultOccurDate(java.util.Date faultOccurDate) {
        this.faultOccurDate = faultOccurDate;
    }

    
    public String getFaultReason() {
        return faultReason;
    }

    
    public void setFaultReason(String faultReason) {
        this.faultReason = faultReason;
    }

    
    public String getHandlePersonName() {
        return handlePersonName;
    }

    
    public void setHandlePersonName(String handlePersonName) {
        this.handlePersonName = handlePersonName;
    }

    
    public java.util.Date getHandleTime() {
        return handleTime;
    }

    
    public void setHandleTime(java.util.Date handleTime) {
        this.handleTime = handleTime;
    }

    
    public String getIdx() {
        return idx;
    }

    
    public void setIdx(String idx) {
        this.idx = idx;
    }

    
    public String getMethodDesc() {
        return methodDesc;
    }

    
    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    
    public Long getNoticePersonId() {
        return noticePersonId;
    }

    
    public void setNoticePersonId(Long noticePersonId) {
        this.noticePersonId = noticePersonId;
    }

    
    public String getNoticePersonName() {
        return noticePersonName;
    }

    
    public void setNoticePersonName(String noticePersonName) {
        this.noticePersonName = noticePersonName;
    }

    
    public java.util.Date getNoticeTime() {
        return noticeTime;
    }

    
    public void setNoticeTime(java.util.Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    
    public String getProfessionalTypeIdx() {
        return professionalTypeIdx;
    }

    
    public void setProfessionalTypeIdx(String professionalTypeIdx) {
        this.professionalTypeIdx = professionalTypeIdx;
    }

    
    public String getProfessionalTypeName() {
        return professionalTypeName;
    }

    
    public void setProfessionalTypeName(String professionalTypeName) {
        this.professionalTypeName = professionalTypeName;
    }

    
    public String getRepairClass() {
        return repairClass;
    }

    
    public void setRepairClass(String repairClass) {
        this.repairClass = repairClass;
    }

    
    public String getRepairDesc() {
        return repairDesc;
    }

    
    public void setRepairDesc(String repairDesc) {
        this.repairDesc = repairDesc;
    }

    
    public Integer getRepairResult() {
        return repairResult;
    }

    
    public void setRepairResult(Integer repairResult) {
        this.repairResult = repairResult;
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

    
    public String getZbthtjCount() {
        return zbthtjCount;
    }

    
    public void setZbthtjCount(String zbthtjCount) {
        this.zbthtjCount = zbthtjCount;
    }
    
    public String getOverDate() {
        return overDate;
    }


    
    public void setOverDate(String overDate) {
        this.overDate = overDate;
    }


    
    public String getStartDate() {
        return startDate;
    }


    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }


    
    public Integer getModelType() {
        return modelType;
    }


    
    public void setModelType(Integer modelType) {
        this.modelType = modelType;
    }
    
    
    
}
