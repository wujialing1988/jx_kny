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
 * <li>说明: 列检作业车辆计划
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_PLAN_RECORD")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpPlanRecord implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 状态-未处理 */
    public static final String STATUS_INITIALIZATION = "INITIALIZATION";
    
    /* 状态-处理中 */
    public static final String STATUS_HANDLING = "ONGOING";
    
    /* 状态-完成 */
    public static final String STATUS_HANDLED = "COMPLETE";
    
    
    /**
     * <li>说明：默认构造器
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-27
     * <li>修改人： 
     * <li>修改日期：
     */
    public ZbglRdpPlanRecord() {
        // TODO Auto-generated constructor stub
    }
    
    /**
     * <li>说明：生成计划调用
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-27
     * <li>修改人： 
     * <li>修改日期：
     * @param seqNum
     */
    public ZbglRdpPlanRecord(Integer seqNum) {
        this.setSeqNum(seqNum);
    }
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 列检计划 */
    @Column(name = "RDP_PLAN_IDX")
    private String rdpPlanIdx;
    
    /* 车辆编号 */
    @Column(name = "SEQ_NUM")
    private Integer seqNum ;
    
    /* 车辆车型ID */
    @Column(name = "TRAIN_TYPE_IDX")
    private String trainTypeIdx ;
    
    /* 车辆车型编码 */
    @Column(name = "TRAIN_TYPE_CODE")
    private String trainTypeCode ;
    
    /* 车辆车型名称 */
    @Column(name = "TRAIN_TYPE_NAME")
    private String trainTypeName ;
    
    /* 车辆车号 */
    @Column(name = "TRAIN_NO")
    private String trainNo ;
    
    /* 作业人员ID集合 */
    @Column(name = "WORK_PERSON_IDX")
    private String workPersonIdx ;
    
    /* 作业人员姓名集合 */
    @Column(name = "WORK_PERSON_NAME")
    private String workPersonName ;
    
    /* 启动作业人员ID 确认人*/
    @Column(name = "START_PERSON_IDX")
    private String startPersonIdx ;
    
    /* 启动作业人员姓名 确认人*/
    @Column(name = "START_PERSON_NAME")
    private String startPersonName ;
    
    /* 完成作业人员ID */
    @Column(name = "COMPLETE_PERSON_IDX")
    private String completePersonIdx ;
    
    /* 完成作业人员姓名 */
    @Column(name = "COMPLETE_PERSON_NAME")
    private String completePersonName ;
    
    /* 车辆列检开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Rdp_Start_Time")
    private java.util.Date rdpStartTime;
    
    /* 车辆列检结束时间 确认时间*/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Rdp_End_Time")
    private java.util.Date rdpEndTime;
    
    /* 作业实例ID */
    @Column(name = "rdpIdx")
    private String rdpIdx ;
    
    /* 车辆检修状态 未处理、处理中、已完成*/
    @Column(name = "RDP_RECORD_STATUS")
    private String rdpRecordStatus;
    
    /* 车型种类编码 */
    @Column(name = "T_VEHICLE_KIND_CODE")
    private String vehicleKindCode;
    
    /* 车型种类名称 篷车、罐车、煤车等*/
    @Column(name = "T_VEHICLE_KIND_NAME")
    private String vehicleKindName;
    
    
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

    
    public String getRdpIdx() {
        return rdpIdx;
    }

    
    public void setRdpIdx(String rdpIdx) {
        this.rdpIdx = rdpIdx;
    }

    
    public String getRdpPlanIdx() {
        return rdpPlanIdx;
    }

    
    public void setRdpPlanIdx(String rdpPlanIdx) {
        this.rdpPlanIdx = rdpPlanIdx;
    }

    
    public Integer getRecordStatus() {
        return recordStatus;
    }

    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    
    public Integer getSeqNum() {
        return seqNum;
    }

    
    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    
    public String getTrainNo() {
        return trainNo;
    }

    
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    
    public String getTrainTypeCode() {
        return trainTypeCode;
    }

    
    public void setTrainTypeCode(String trainTypeCode) {
        this.trainTypeCode = trainTypeCode;
    }

    
    public String getTrainTypeIdx() {
        return trainTypeIdx;
    }

    
    public void setTrainTypeIdx(String trainTypeIdx) {
        this.trainTypeIdx = trainTypeIdx;
    }

    
    public String getTrainTypeName() {
        return trainTypeName;
    }

    
    public void setTrainTypeName(String trainTypeName) {
        this.trainTypeName = trainTypeName;
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

    
    public String getWorkPersonIdx() {
        return workPersonIdx;
    }

    
    public void setWorkPersonIdx(String workPersonIdx) {
        this.workPersonIdx = workPersonIdx;
    }

    
    public String getWorkPersonName() {
        return workPersonName;
    }

    
    public void setWorkPersonName(String workPersonName) {
        this.workPersonName = workPersonName;
    }

    
    public String getVehicleKindCode() {
        return vehicleKindCode;
    }

    
    public void setVehicleKindCode(String vehicleKindCode) {
        this.vehicleKindCode = vehicleKindCode;
    }

    
    public String getVehicleKindName() {
        return vehicleKindName;
    }

    
    public void setVehicleKindName(String vehicleKindName) {
        this.vehicleKindName = vehicleKindName;
    }

    
    public String getRdpRecordStatus() {
        return rdpRecordStatus;
    }

    
    public void setRdpRecordStatus(String rdpRecordStatus) {
        this.rdpRecordStatus = rdpRecordStatus;
    }

    
    public String getStartPersonIdx() {
        return startPersonIdx;
    }

    
    public void setStartPersonIdx(String startPersonIdx) {
        this.startPersonIdx = startPersonIdx;
    }

    
    public String getStartPersonName() {
        return startPersonName;
    }

    
    public void setStartPersonName(String startPersonName) {
        this.startPersonName = startPersonName;
    }

    
    public String getCompletePersonIdx() {
        return completePersonIdx;
    }

    
    public void setCompletePersonIdx(String completePersonIdx) {
        this.completePersonIdx = completePersonIdx;
    }

    
    public String getCompletePersonName() {
        return completePersonName;
    }

    
    public void setCompletePersonName(String completePersonName) {
        this.completePersonName = completePersonName;
    }

    
    public java.util.Date getRdpEndTime() {
        return rdpEndTime;
    }

    
    public void setRdpEndTime(java.util.Date rdpEndTime) {
        this.rdpEndTime = rdpEndTime;
    }

    
    public java.util.Date getRdpStartTime() {
        return rdpStartTime;
    }

    
    public void setRdpStartTime(java.util.Date rdpStartTime) {
        this.rdpStartTime = rdpStartTime;
    }
    
    
    
}
