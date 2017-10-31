package com.yunda.zb.tp.entity;

import java.util.Date;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTpException实体类, 数据表：提票例外放行
 * <li>创建人：程锐
 * <li>创建日期：2015-03-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_JT6_Exception")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglTpException implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 整备任务单ID */
    @Column(name = "RDP_IDX")
    private String rdpIDX;
    
    /* JT6的ID */
    @Column(name = "JT6_IDX")
    private String tpIDX;
    
    /* 放行原因 */
    @Column(name = "Exception_Reason")
    private String exceptionReason;
    
    /* 处理人编码 */
    @Column(name = "Handle_Person_Id")
    private Long handlePersonId;
    
    /* 处理人名称 */
    @Column(name = "Handle_Person_Name")
    private String handlePersonName;
    
    /* 处理时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Handle_Time")
    private java.util.Date handleTime;
    
    /* 记录状态，1：删除；0：未删除； */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 最新更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /* 车型拼音码 */
    @Transient
    private String trainTypeShortName;
    
    /* 车号 */
    @Transient
    private String trainNo;
    
    /* 提票人名称 */
    @Transient
    private String noticePersonName;
    
    /* 提票时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Transient
    private java.util.Date noticeTime;
    
    /* 故障部件名称 */
    @Transient
    private String faultFixFullName;
    
    /* 故障现象 */
    @Transient
    private String faultName;
    
    /* 故障描述 */
    @Transient
    private String faultDesc;
    
    /* 故障发生日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Transient
    private java.util.Date faultOccurDate;
    
    /* 提票单号 */
    @Transient
    private String faultNoticeCode;
    
    /**
     * @return String 获取整备单ID
     */
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    /**
     * @param rdpIDX 设置整备单ID
     */
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
    /**
     * @return String 获取JT6的ID
     */
    public String getTpIDX() {
        return tpIDX;
    }
    
    /**
     * @param tpIDX 设置JT6的ID
     */
    public void setTpIDX(String tpIDX) {
        this.tpIDX = tpIDX;
    }
    
    /**
     * @return String 获取放行原因
     */
    public String getExceptionReason() {
        return exceptionReason;
    }
    
    /**
     * @param exceptionReason 设置放行原因
     */
    public void setExceptionReason(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }
    
    /**
     * @return Long 获取处理人编码
     */
    public Long getHandlePersonId() {
        return handlePersonId;
    }
    
    /**
     * @param handlePersonId 设置处理人编码
     */
    public void setHandlePersonId(Long handlePersonId) {
        this.handlePersonId = handlePersonId;
    }
    
    /**
     * @return String 获取处理人名称
     */
    public String getHandlePersonName() {
        return handlePersonName;
    }
    
    /**
     * @param handlePersonName 设置处理人名称
     */
    public void setHandlePersonName(String handlePersonName) {
        this.handlePersonName = handlePersonName;
    }
    
    /**
     * @return java.util.Date 获取处理时间
     */
    public java.util.Date getHandleTime() {
        return handleTime;
    }
    
    /**
     * @param handleTime 设置处理时间
     */
    public void setHandleTime(java.util.Date handleTime) {
        this.handleTime = handleTime;
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
     * @return java.util.Date 获取最新更新时间
     */
    public java.util.Date getUpdateTime() {
        return updateTime;
    }
    
    /**
     * @param updateTime 设置最新更新时间
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
    
    /**
     * @param idx 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getFaultDesc() {
        return faultDesc;
    }
    
    public void setFaultDesc(String faultDesc) {
        this.faultDesc = faultDesc;
    }
    
    public String getFaultFixFullName() {
        return faultFixFullName;
    }
    
    public void setFaultFixFullName(String faultFixFullName) {
        this.faultFixFullName = faultFixFullName;
    }
    
    public String getFaultName() {
        return faultName;
    }
    
    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }
    
    public java.util.Date getFaultOccurDate() {
        return faultOccurDate;
    }
    
    public void setFaultOccurDate(java.util.Date faultOccurDate) {
        this.faultOccurDate = faultOccurDate;
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
    
    public String getTrainNo() {
        return trainNo;
    }
    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }
    
    public String getFaultNoticeCode() {
        return faultNoticeCode;
    }
    
    public void setFaultNoticeCode(String faultNoticeCode) {
        this.faultNoticeCode = faultNoticeCode;
    }
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     */
    public ZbglTpException() {
        
    }
    
    /**
     * <li>说明：机车整备交验-提票例外放行列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-12
     * <li>修改人：
     * <li>修改日期：
     * @param idx 提票例外放行IDX
     * @param rdpIDX 机车整备单IDX
     * @param tpIDX 提票单IDX
     * @param exceptionReason 例外放行原因
     * @param handlePersonName 放行人
     * @param handleTime 放行时间
     * @param trainTypeShortName 车型
     * @param trainNo 车号
     * @param noticePersonName 提票人
     * @param noticeTime 提票时间
     * @param faultFixFullName 故障部件
     * @param faultName 故障现象
     * @param faultDesc 故障描述
     * @param faultOccurDate 故障发生日期
     * @param faultNoticeCode 提票单号
     */
    public ZbglTpException(String idx, String rdpIDX, String tpIDX, String exceptionReason, String handlePersonName, Date handleTime,
        String trainTypeShortName, String trainNo, String noticePersonName, Date noticeTime, String faultFixFullName, String faultName,
        String faultDesc, Date faultOccurDate, String faultNoticeCode) {
        super();
        this.idx = idx;
        this.rdpIDX = rdpIDX;
        this.tpIDX = tpIDX;
        this.exceptionReason = exceptionReason;
        this.handlePersonName = handlePersonName;
        this.handleTime = handleTime;
        this.trainTypeShortName = trainTypeShortName;
        this.trainNo = trainNo;
        this.noticePersonName = noticePersonName;
        this.noticeTime = noticeTime;
        this.faultFixFullName = faultFixFullName;
        this.faultName = faultName;
        this.faultDesc = faultDesc;
        this.faultOccurDate = faultOccurDate;
        this.faultNoticeCode = faultNoticeCode;
    }
    
}
