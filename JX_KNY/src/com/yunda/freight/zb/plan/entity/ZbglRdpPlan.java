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
 * <li>说明: 货车列检计划
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_PLAN")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpPlan implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 状态-未启动 */
    public static final String STATUS_UNRELEASED = "UNRELEASED";
    
    /* 状态-已启动 */
    public static final String STATUS_HANDLING = "ONGOING";
    
    /* 状态-中断 */
    public static final String STATUS_INTERRUPT = "INTERRUPT";
    
    /* 状态-延期 */
    public static final String STATUS_DELAY = "DELAY";
    
    /* 状态-完成 */
    public static final String STATUS_HANDLED = "COMPLETE";
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 列车车次 */
    @Column(name = "RAILWAY_TIME")
    private String railwayTime;
    
    /* 编组任务ID */
    @Column(name = "TRAIN_DEMAND_IDX")
    private String trainDemandIdx ;
    
    /* 计划开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Plan_Start_Time")
    private java.util.Date planStartTime;
    
    /* 计划结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "plan_End_Time")
    private java.util.Date planEndTime;
    
    /* 实际开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "real_Start_Time")
    private java.util.Date realStartTime;
    
    /* 实际结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "real_End_Time")
    private java.util.Date realEndTime;
    
    /* 站点ID */
    @Column(name = "SITE_ID")
    private String siteID;
    
    /* 站点名称 */
    @Column(name = "SITE_NAME")
    private String siteName;
    
    /* 停靠股道编号 */
    @Column(name = "TRACK_NO")
    private String trackNo;
    
    /* 停靠股道名称 */
    @Column(name = "TRACK_NAME")
    private String trackName;
    
    /* 接入方向编码 选列检所 */
    @Column(name = "COME_DIRECTION_NO")
    private String comeDirectionNo;
    
    /* 接入方向名称 */
    @Column(name = "COME_DIRECTION_NAME")
    private String comeDirectionName;
    
    /* 发出方向编码 选列检所 */
    @Column(name = "TO_DIRECTION_NO")
    private String toDirectionNo;
    
    /* 发出方向名称 */
    @Column(name = "TO_DIRECTION_NAME")
    private String toDirectionName;
    
    /* 技检时间（分钟） */
    @Column(name = "CHECK_TIME")
    private Integer checkTime;
    
    /* 白夜班编码 */
    @Column(name = "DAYNIGHT_TYPENO")
    private String dayNightTypeNo;
    
    /* 白夜班名称 */
    @Column(name = "DAYNIGHT_TYPENAME")
    private String dayNightTypeName;
    
    /* 班次编码 */
    @Column(name = "CLASS_NO")
    private String classNo;
    
    /* 班次名称 */
    @Column(name = "CLASS_NAME")
    private String className;
    
    /* 列检状态 */
    @Column(name = "RDP_PLNA_STATUS")
    private String rdpPlanStatus;
    
    /* 作业班组ID */
    @Column(name = "WORK_TEAM_ID")
    private String workTeamID;
    
    /* 作业班组序号 */
    @Column(name = "WORK_TEAM_SEQ")
    private String workTeamSeq;
    
    /* 作业班组名称 */
    @Column(name = "WORK_TEAM_NAME")
    private String workTeamName;
    
    /* 计划列检车辆数量 */
    @Column(name = "RDP_NUM")
    private Integer rdpNum;
    
    /* 作业方式 编码 */
    @Column(name = "WORK_TYPE_CODE")
    private String workTypeCode;
    
    /* 作业方式 人工检查、动态检查、人机分工检查 */
    @Column(name = "WORK_TYPE")
    private String workType;
    
    /* 作业性质 编码 */
    @Column(name = "WORK_NATURE_CODE")
    private String workNatureCode;
    
    /* 作业性质 到达作业、始发作业、中转作业、通过作业 */
    @Column(name = "WORK_NATURE")
    private String workNature;
    
    /* 备注 */
    @Column(name = "REMARK")
    private String remark;
    
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
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;
    
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
    
    public java.util.Date getPlanEndTime() {
        return planEndTime;
    }
    
    public void setPlanEndTime(java.util.Date planEndTime) {
        this.planEndTime = planEndTime;
    }
    
    public java.util.Date getPlanStartTime() {
        return planStartTime;
    }
    
    public void setPlanStartTime(java.util.Date planStartTime) {
        this.planStartTime = planStartTime;
    }
    
    public String getRailwayTime() {
        return railwayTime;
    }
    
    public void setRailwayTime(String railwayTime) {
        this.railwayTime = railwayTime;
    }
    
    public Integer getRdpNum() {
        return rdpNum;
    }
    
    public void setRdpNum(Integer rdpNum) {
        this.rdpNum = rdpNum;
    }
    
    public String getRdpPlanStatus() {
        return rdpPlanStatus;
    }
    
    public void setRdpPlanStatus(String rdpPlanStatus) {
        this.rdpPlanStatus = rdpPlanStatus;
    }
    
    public java.util.Date getRealEndTime() {
        return realEndTime;
    }
    
    public void setRealEndTime(java.util.Date realEndTime) {
        this.realEndTime = realEndTime;
    }
    
    public java.util.Date getRealStartTime() {
        return realStartTime;
    }
    
    public void setRealStartTime(java.util.Date realStartTime) {
        this.realStartTime = realStartTime;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    public String getSiteName() {
        return siteName;
    }
    
    public void setSiteName(String siteName) {
        this.siteName = siteName;
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
    
    public String getWorkNature() {
        return workNature;
    }
    
    public void setWorkNature(String workNature) {
        this.workNature = workNature;
    }
    
    public String getWorkTeamID() {
        return workTeamID;
    }
    
    public void setWorkTeamID(String workTeamID) {
        this.workTeamID = workTeamID;
    }
    
    public String getWorkTeamName() {
        return workTeamName;
    }
    
    public void setWorkTeamName(String workTeamName) {
        this.workTeamName = workTeamName;
    }
    
    public String getWorkType() {
        return workType;
    }
    
    public void setWorkType(String workType) {
        this.workType = workType;
    }
    
    public String getTrackNo() {
        return trackNo;
    }
    
    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }
    
    public String getWorkNatureCode() {
        return workNatureCode;
    }
    
    public void setWorkNatureCode(String workNatureCode) {
        this.workNatureCode = workNatureCode;
    }
    
    public String getWorkTypeCode() {
        return workTypeCode;
    }
    
    public void setWorkTypeCode(String workTypeCode) {
        this.workTypeCode = workTypeCode;
    }
    
    public String getWorkTeamSeq() {
        return workTeamSeq;
    }
    
    public void setWorkTeamSeq(String workTeamSeq) {
        this.workTeamSeq = workTeamSeq;
    }
    
    public Integer getCheckTime() {
        return checkTime;
    }
    
    public void setCheckTime(Integer checkTime) {
        this.checkTime = checkTime;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getClassNo() {
        return classNo;
    }
    
    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }
    
    public String getComeDirectionName() {
        return comeDirectionName;
    }
    
    public void setComeDirectionName(String comeDirectionName) {
        this.comeDirectionName = comeDirectionName;
    }
    
    public String getComeDirectionNo() {
        return comeDirectionNo;
    }
    
    public void setComeDirectionNo(String comeDirectionNo) {
        this.comeDirectionNo = comeDirectionNo;
    }
    
    public String getDayNightTypeName() {
        return dayNightTypeName;
    }
    
    public void setDayNightTypeName(String dayNightTypeName) {
        this.dayNightTypeName = dayNightTypeName;
    }
    
    public String getDayNightTypeNo() {
        return dayNightTypeNo;
    }
    
    public void setDayNightTypeNo(String dayNightTypeNo) {
        this.dayNightTypeNo = dayNightTypeNo;
    }
    
    public String getToDirectionName() {
        return toDirectionName;
    }
    
    public void setToDirectionName(String toDirectionName) {
        this.toDirectionName = toDirectionName;
    }
    
    public String getToDirectionNo() {
        return toDirectionNo;
    }
    
    public void setToDirectionNo(String toDirectionNo) {
        this.toDirectionNo = toDirectionNo;
    }
    
    public String getTrackName() {
        return trackName;
    }
    
    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getTrainDemandIdx() {
        return trainDemandIdx;
    }
    
    public void setTrainDemandIdx(String trainDemandIdx) {
        this.trainDemandIdx = trainDemandIdx;
    }
    
}
