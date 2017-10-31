package com.yunda.jx.pjjx.partsrdp.entity;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsRdp实体类, 数据表：配件检修作业
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "PJJX_Parts_Rdp")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class PartsRdp implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 状态-未启动 */
    public static final String STATUS_WQD = "01";
    /* 状态-检修中 */
    public static final String STATUS_JXZ = "02";
    /* 状态-待验收 */
    public static final String STATUS_DYS = "03";
    /* 状态-已终止 */
    public static final String STATUS_YZZ = "10";
    /* 状态-无法修复 */
    public static final String STATUS_WFXF = "0402";
    /* 状态-检修合格 */
    public static final String STATUS_JXHG = "0401";
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 配件型号主键 */
    @Column(name = "Parts_Type_IDX")
    private String partsTypeIDX;
    
    /* 配件信息主键 */
    @Column(name = "PARTS_ACCOUNT_IDX")
    private String partsAccountIDX;
    
    /* 规格型号 */
    @Column(name = "SPECIFICATION_MODEL")
    private String specificationModel;
    
    /* 配件名称 */
    @Column(name = "PARTS_NAME")
    private String partsName;
    
    /* 物料编码 */
    @Column(name = "MAT_CODE")
    private String matCode;
    
    /* 配件编号 */
    @Column(name = "PARTS_NO")
    private String partsNo;
    
    /* 配件铭牌号 */
    @Column(name="nameplate_No")
    private String nameplateNo;
    
    /* 配件识别码 */
    @Column(name = "IDENTIFICATION_CODE")
    private String identificationCode;
    
    /* 扩展编号 */
    private String extendNo;
    
    /* 下车车型 */
    @Column(name = "UNLOAD_TRAINTYPE")
    private String unloadTrainType;
    
    /* 下车车型主键 */
    @Column(name = "UNLOAD_TRAINTYPE_IDX")
    private String unloadTrainTypeIdx;
    
    /* 下车车号 */
    @Column(name = "UNLOAD_TRAINNO")
    private String unloadTrainNo;
    
    /* 下车修程编码 */
    @Column(name = "UNLOAD_REPAIR_CLASS_IDX")
    private String unloadRepairClassIdx;
    
    /* 下车修程 */
    @Column(name = "UNLOAD_REPAIR_CLASS")
    private String unloadRepairClass;
    
    /* 下车修次编码 */
    @Column(name = "UNLOAD_REPAIR_TIME_IDX")
    private String unloadRepairTimeIdx;
    
    /* 下车修次 */
    @Column(name = "UNLOAD_REPAIR_TIME")
    private String unloadRepairTime;
    
    /* 计划开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Plan_StartTime")
    private java.util.Date planStartTime;
    
    /* 计划结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Plan_EndTime")
    private java.util.Date planEndTime;
    
    /* 承修班组 */
    @Column(name = "Repair_OrgID")
    private Long repairOrgID;
    
    /* 承修班组名称 */
    @Column(name = "Repair_OrgName")
    private String repairOrgName;
    
    /* 承修班组序列 */
    @Column(name = "Repair_OrgSeq")
    private String repairOrgSeq;
    
    /* 检修负责人 */
    @Column(name = "Duty_EmpID")
    private Long dutyEmpID;
    
    /* 检修负责人名称 */
    @Column(name = "Duty_EmpName")
    private String dutyEmpName;
    
    /* 实际开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Real_StartTime")
    private java.util.Date realStartTime;
    
    /* 实际结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Real_EndTime")
    private java.util.Date realEndTime;
    
    /* 检修需求主键 */
    @Column(name = "WP_IDX")
    private String wpIDX;
    
    /* 检修需求编号 */
    @Column(name = "WP_No")
    private String wpNo;
    
    /* 检修需求描述 */
    @Column(name = "WP_Desc")
    private String wpDesc;
    
    /* 额定工期 */
    @Column(name = "Rated_Period")
    private Double ratedPeriod;
    
    /* 编制人 */
    @Column(name = "Rdp_EmpID")
    private Long rdpEmpID;
    
    /* 编制人名称 */
    @Column(name = "Rdp_EmpName")
    private String rdpEmpName;
    
    /* 编制部门 */
    @Column(name = "Rdp_OrgID")
    private Long rdpOrgID;
    
    /* 编制部门名称 */
    @Column(name = "Rdp_OrgName")
    private String rdpOrgName;
    
    /* 编制时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Rdp_Time")
    private java.util.Date rdpTime;
    
    /* 额定工时 */
    @Column(name = "Rated_WorkTime")
    private Double ratedWorkTime;
    
    /* 状态 */
    private String status;
    
    /* 检修结果描述 */
    @Column(name = "Repair_Result_Desc")
    private String repairResultDesc;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
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
    
    /* 日历主键 */
    @Column(name = "CALENDAR_IDX")
    private String calendarIdx;
    
    /* 日历名称 */
    @Column(name = "CALENDAR_NAME")
    private String calendarName;
    
    /* 验收人 */
    @Column(name = "Acceptance_EmpID")
    private Long acceptanceEmpID;
    
    /* 验收人名称 */
    @Column(name = "Acceptance_EmpName")
    private String acceptanceEmpName;
    
    /* 施修人员 */
    @Transient
    private String workNameStr;
    
    /* 计划开工时间字符串 */
    @Transient
    private String planStartTimeStr;
    
    /* 计划完工时间字符串 */
    @Transient
    private String planEndTimeStr;
    
    /* 登记情况 */
    @Transient
    private String num;
    /* 工位idx */
    @Transient
    private String workStationIDX;
    
    public String getNum() {
        return num;
    }
    
    public void setNum(String num) {
        this.num = num;
    }
    
    public String getPlanEndTimeStr() {
        return planEndTimeStr;
    }
    
    public void setPlanEndTimeStr(String planEndTimeStr) {
        this.planEndTimeStr = planEndTimeStr;
    }
    
    public String getPlanStartTimeStr() {
        return planStartTimeStr;
    }
    
    public void setPlanStartTimeStr(String planStartTimeStr) {
        this.planStartTimeStr = planStartTimeStr;
    }
    
    /**
     * @return String 获取配件型号主键
     */
    public String getPartsTypeIDX() {
        return partsTypeIDX;
    }
    
    /**
     * @param partsTypeIDX 设置配件型号主键
     */
    public void setPartsTypeIDX(String partsTypeIDX) {
        this.partsTypeIDX = partsTypeIDX;
    }
    
    /**
     * @return String 获取规格型号
     */
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    /**
     * @param specificationModel 设置规格型号
     */
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }
    
    /**
     * @return String 获取配件名称
     */
    public String getPartsName() {
        return partsName;
    }
    
    /**
     * @param partsName 设置配件名称
     */
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }
    
    /**
     * @return String 获取物料编码
     */
    public String getMatCode() {
        return matCode;
    }
    
    /**
     * @param matCode 设置物料编码
     */
    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }
    
    /**
     * @return String 获取配件编号
     */
    public String getPartsNo() {
        return partsNo;
    }
    
    /**
     * @param partsNo 设置配件编号
     */
    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }
    
    /**
     * @return String 获取扩展编号
     */
    public String getExtendNo() {
        return extendNo;
    }
    
    /**
     * @param extendNo 设置扩展编号
     */
    public void setExtendNo(String extendNo) {
        this.extendNo = extendNo;
    }
    
    /**
     * @return String 获取下车车型
     */
    public String getUnloadTrainType() {
        return unloadTrainType;
    }
    
    /**
     * @param unloadTrainType 设置下车车型
     */
    public void setUnloadTrainType(String unloadTrainType) {
        this.unloadTrainType = unloadTrainType;
    }
    
    /**
     * @return String 获取下车车型主键
     */
    public String getUnloadTrainTypeIdx() {
        return unloadTrainTypeIdx;
    }
    
    /**
     * @param unloadTrainTypeIdx 设置下车车型主键
     */
    public void setUnloadTrainTypeIdx(String unloadTrainTypeIdx) {
        this.unloadTrainTypeIdx = unloadTrainTypeIdx;
    }
    
    /**
     * @return String 获取下车车号
     */
    public String getUnloadTrainNo() {
        return unloadTrainNo;
    }
    
    /**
     * @param unloadTrainNo 设置下车车号
     */
    public void setUnloadTrainNo(String unloadTrainNo) {
        this.unloadTrainNo = unloadTrainNo;
    }
    
    /**
     * @return String 获取下车修程编码
     */
    public String getUnloadRepairClassIdx() {
        return unloadRepairClassIdx;
    }
    
    /**
     * @param unloadRepairClassIdx 设置下车修程编码
     */
    public void setUnloadRepairClassIdx(String unloadRepairClassIdx) {
        this.unloadRepairClassIdx = unloadRepairClassIdx;
    }
    
    /**
     * @return String 获取下车修程
     */
    public String getUnloadRepairClass() {
        return unloadRepairClass;
    }
    
    /**
     * @param unloadRepairClass 设置下车修程
     */
    public void setUnloadRepairClass(String unloadRepairClass) {
        this.unloadRepairClass = unloadRepairClass;
    }
    
    /**
     * @return String 获取下车修次编码
     */
    public String getUnloadRepairTimeIdx() {
        return unloadRepairTimeIdx;
    }
    
    /**
     * @param unloadRepairTimeIdx 设置下车修次编码
     */
    public void setUnloadRepairTimeIdx(String unloadRepairTimeIdx) {
        this.unloadRepairTimeIdx = unloadRepairTimeIdx;
    }
    
    /**
     * @return String 获取下车修次
     */
    public String getUnloadRepairTime() {
        return unloadRepairTime;
    }
    
    /**
     * @param unloadRepairTime 设置下车修次
     */
    public void setUnloadRepairTime(String unloadRepairTime) {
        this.unloadRepairTime = unloadRepairTime;
    }
    
    /**
     * @return java.util.Date 获取计划开始时间
     */
    public java.util.Date getPlanStartTime() {
        return planStartTime;
    }
    
    /**
     * @param planStartTime 设置计划开始时间
     */
    public void setPlanStartTime(java.util.Date planStartTime) {
        this.planStartTime = planStartTime;
    }
    
    /**
     * @return java.util.Date 获取计划结束时间
     */
    public java.util.Date getPlanEndTime() {
        return planEndTime;
    }
    
    /**
     * @param planEndTime 设置计划结束时间
     */
    public void setPlanEndTime(java.util.Date planEndTime) {
        this.planEndTime = planEndTime;
    }
    
    /**
     * @return Long 获取承修班组
     */
    public Long getRepairOrgID() {
        return repairOrgID;
    }
    
    /**
     * @param repairOrgID 设置承修班组
     */
    public void setRepairOrgID(Long repairOrgID) {
        this.repairOrgID = repairOrgID;
    }
    
    /**
     * @return String 获取承修班组名称
     */
    public String getRepairOrgName() {
        return repairOrgName;
    }
    
    /**
     * @param repairOrgName 设置承修班组名称
     */
    public void setRepairOrgName(String repairOrgName) {
        this.repairOrgName = repairOrgName;
    }
    
    /**
     * @return String 获取承修班组序列
     */
    public String getRepairOrgSeq() {
        return repairOrgSeq;
    }
    
    /**
     * @param repairOrgSeq 设置承修班组序列
     */
    public void setRepairOrgSeq(String repairOrgSeq) {
        this.repairOrgSeq = repairOrgSeq;
    }
    
    /**
     * @return Long 获取检修负责人
     */
    public Long getDutyEmpID() {
        return dutyEmpID;
    }
    
    /**
     * @param dutyEmpID 设置检修负责人
     */
    public void setDutyEmpID(Long dutyEmpID) {
        this.dutyEmpID = dutyEmpID;
    }
    
    /**
     * @return String 获取检修负责人名称
     */
    public String getDutyEmpName() {
        return dutyEmpName;
    }
    
    /**
     * @param dutyEmpName 设置检修负责人名称
     */
    public void setDutyEmpName(String dutyEmpName) {
        this.dutyEmpName = dutyEmpName;
    }
    
    /**
     * @return java.util.Date 获取实际开始时间
     */
    public java.util.Date getRealStartTime() {
        return realStartTime;
    }
    
    /**
     * @param realStartTime 设置实际开始时间
     */
    public void setRealStartTime(java.util.Date realStartTime) {
        this.realStartTime = realStartTime;
    }
    
    /**
     * @return java.util.Date 获取实际结束时间
     */
    public java.util.Date getRealEndTime() {
        return realEndTime;
    }
    
    /**
     * @param realEndTime 设置实际结束时间
     */
    public void setRealEndTime(java.util.Date realEndTime) {
        this.realEndTime = realEndTime;
    }
    
    /**
     * @return String 获取检修流程主键
     */
    public String getWpIDX() {
        return wpIDX;
    }
    
    /**
     * @param wpIDX 设置检修流程主键
     */
    public void setWpIDX(String wpIDX) {
        this.wpIDX = wpIDX;
    }
    
    /**
     * @return String 获取检修流程编号
     */
    public String getWpNo() {
        return wpNo;
    }
    
    /**
     * @param wpNo 设置检修流程编号
     */
    public void setWpNo(String wpNo) {
        this.wpNo = wpNo;
    }
    
    /**
     * @return String 获取检修流程描述
     */
    public String getWpDesc() {
        return wpDesc;
    }
    
    /**
     * @param wpDesc 设置检修流程描述
     */
    public void setWpDesc(String wpDesc) {
        this.wpDesc = wpDesc;
    }
    
    /**
     * @return Double 获取额定工期
     */
    public Double getRatedPeriod() {
        return ratedPeriod;
    }
    
    /**
     * @param ratedPeriod 设置额定工期
     */
    public void setRatedPeriod(Double ratedPeriod) {
        this.ratedPeriod = ratedPeriod;
    }
    
    /**
     * @return Long 获取编制人
     */
    public Long getRdpEmpID() {
        return rdpEmpID;
    }
    
    /**
     * @param rdpEmpID 设置编制人
     */
    public void setRdpEmpID(Long rdpEmpID) {
        this.rdpEmpID = rdpEmpID;
    }
    
    /**
     * @return String 获取编制人名称
     */
    public String getRdpEmpName() {
        return rdpEmpName;
    }
    
    /**
     * @param rdpEmpName 设置编制人名称
     */
    public void setRdpEmpName(String rdpEmpName) {
        this.rdpEmpName = rdpEmpName;
    }
    
    /**
     * @return Long 获取编制部门
     */
    public Long getRdpOrgID() {
        return rdpOrgID;
    }
    
    /**
     * @param rdpOrgID 设置编制部门
     */
    public void setRdpOrgID(Long rdpOrgID) {
        this.rdpOrgID = rdpOrgID;
    }
    
    /**
     * @return String 获取编制部门名称
     */
    public String getRdpOrgName() {
        return rdpOrgName;
    }
    
    /**
     * @param rdpOrgName 设置编制部门名称
     */
    public void setRdpOrgName(String rdpOrgName) {
        this.rdpOrgName = rdpOrgName;
    }
    
    /**
     * @return java.util.Date 获取编制时间
     */
    public java.util.Date getRdpTime() {
        return rdpTime;
    }
    
    /**
     * @param rdpTime 设置编制时间
     */
    public void setRdpTime(java.util.Date rdpTime) {
        this.rdpTime = rdpTime;
    }
    
    /**
     * @return Double 获取额定工时
     */
    public Double getRatedWorkTime() {
        return ratedWorkTime;
    }
    
    /**
     * @param ratedWorkTime 设置额定工时
     */
    public void setRatedWorkTime(Double ratedWorkTime) {
        this.ratedWorkTime = ratedWorkTime;
    }
    
    /**
     * @return String 获取状态
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * @param status 设置状态
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * @return String 获取检修结果描述
     */
    public String getRepairResultDesc() {
        return repairResultDesc;
    }
    
    /**
     * @param repairResultDesc 设置检修结果描述
     */
    public void setRepairResultDesc(String repairResultDesc) {
        this.repairResultDesc = repairResultDesc;
    }
    
    /**
     * @return Integer 获取记录的状态
     */
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    /**
     * @param recordStatus 设置记录的状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
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
    
    /**
     * @param idx 设置idx主键
     */
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    /**
     * @return 获取配件信息主键
     */
    public String getPartsAccountIDX() {
        return partsAccountIDX;
    }
    
    /**
     * @param partsAccountIDX 设置配件信息主键
     */
    public void setPartsAccountIDX(String partsAccountIDX) {
        this.partsAccountIDX = partsAccountIDX;
    }
    
    /**
     * @return 获取施修人员
     */
    public String getWorkNameStr() {
        return workNameStr;
    }
    
    /**
     * @param workNameStr 设置施修人员
     */
    public void setWorkNameStr(String workNameStr) {
        this.workNameStr = workNameStr;
    }
    
    /**
     * @return 获取配件识别码
     */
    public String getIdentificationCode() {
        return identificationCode;
    }
    
    /**
     * @param identificationCode 设置配件识别码
     */
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
    
    /**
     * @return 获取日历主键
     */
    public String getCalendarIdx() {
        return calendarIdx;
    }
    
    /**
     * @param calendarIdx 设置日历主键
     */
    public void setCalendarIdx(String calendarIdx) {
        this.calendarIdx = calendarIdx;
    }
    
    /**
     * @return 获取日历名称
     */
    public String getCalendarName() {
        return calendarName;
    }
    
    /**
     * @param calendarName 设置日历名称
     */
    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    /**
     * @return 获取验收人
     */
    public Long getAcceptanceEmpID() {
        return acceptanceEmpID;
    }
    
    /**
     * @param acceptanceEmpID 设置验收人
     */
    public void setAcceptanceEmpID(Long acceptanceEmpID) {
        this.acceptanceEmpID = acceptanceEmpID;
    }
    
    /**
     * @return 获取验收人名称
     */
    public String getAcceptanceEmpName() {
        return acceptanceEmpName;
    }
    
    /**
     * @param acceptanceEmpName 设置验收人名称
     */
    public void setAcceptanceEmpName(String acceptanceEmpName) {
        this.acceptanceEmpName = acceptanceEmpName;
    }

    
    public String getNameplateNo() {
        return nameplateNo;
    }

    
    public void setNameplateNo(String nameplateNo) {
        this.nameplateNo = nameplateNo;
    }

    
    public String getWorkStationIDX() {
        return workStationIDX;
    }

    
    public void setWorkStationIDX(String workStationIDX) {
        this.workStationIDX = workStationIDX;
    }
    
}
