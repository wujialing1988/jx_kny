package com.yunda.zb.tp.entity;

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
 * <li>说明：ZbglTp实体类, 数据表：JT6提票
 * <li>创建人：程锐
 * <li>创建日期：2015-01-21
 * <li>修改人: 张迪
 * <li>修改日期：2016-4-15
 * <li>修改内容： 添加故障原因字段
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_JT6")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglTp implements java.io.Serializable {
    
    /** 业务状态：初始化 */
    public static final String STATUS_INIT = "INITIALIZE";
    
    public static final String STATUS_INIT_CH = "初始化";
    
    /** 业务状态：待接活 */
    public static final String STATUS_DRAFT = "TODO";
    
    public static final String STATUS_DRAFT_CH = "待接活";
    
    /** 业务状态：待销活 */
    public static final String STATUS_OPEN = "ONGOING";
    
    public static final String STATUS_OPEN_CH = "待销活";
    
    /** 业务状态：已处理 */
    public static final String STATUS_OVER = "COMPLETE";
    
    public static final String STATUS_OVER_CH = "已处理";
    
    /** 业务状态：已验收 */
    public static final String STATUS_CHECK = "CHECKED";
    
    public static final String STATUS_CHECK_CH = "已检验";
    
    /** 提票来源：整备 */
    public static final String NOTICESOURCE_ZB = "2";
    
    /** 提票处理结果：转临修 */
    public static final int REPAIRRESULT_ZLX = 4;
    
    /** 1==跟踪 */
    public static final int ISTRACKED_YES = 1;
    /** 0==未跟踪 */
    public static final int ISTRACKED_NO = 0;
    
    /* 状态-校验后提票 是 */
    public static final String IS_JY_STATUS_YES = "01";
    
    /* 状态-校验后提票 否 */
    public static final String IS_JY_STATUS_NO = "02";
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 车型编码 */
    @Column(name = "Train_Type_IDX")
    private String trainTypeIDX;
    
    /* 车型拼音码 */
    @Column(name = "Train_Type_ShortName")
    private String trainTypeShortName;
    
    /* 车号 */
    @Column(name = "Train_No")
    private String trainNo;
    
    /* 配属段编码 */
    @Column(name = "D_ID")
    private String dID;
    
    /* 配属段名称 */
    @Column(name = "D_Name")
    private String dName;
    
    /* 故障发生日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Fault_Occur_Date")
    private java.util.Date faultOccurDate;
    
    /* 系统分类编码 */
    @Column(name = "Fault_Fix_FullCode")
    private String faultFixFullCode;
    
    /* 故障部件全名 */
    @Column(name = "Fault_Fix_FullName")
    private String faultFixFullName;
    
    /* 故障ID */
    @Column(name = "Fault_ID")
    private String faultID;
    
    /* 故障现象 */
    @Column(name = "Fault_Name")
    private String faultName;
    
    /* 故障描述 */
    @Column(name = "Fault_Desc")
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
    
    /* 专业类型序列 */
    @Column(name = "PROFESSIONAL_TYPE_SEQ")
    private String professionalTypeSeq;
    
    /* 提票单号 */
    @Column(name = "Fault_Notice_Code")
    private String faultNoticeCode;
    
    /* 提票来源,1：运用；2：整备；3：质检；4：技术； */
    @Column(name = "Notice_Source")
    private String noticeSource;
    
    /* 提票人编码 */
    @Column(name = "Notice_Person_Id")
    private Long noticePersonId;
    
    /* 提票人名称 */
    @Column(name = "Notice_Person_Name")
    private String noticePersonName;
    
    /* 提票时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Notice_Time")
    private java.util.Date noticeTime;
    
    /* 提票站场 */
    @Column(updatable = false)
    private String siteID;
    
    /* 提票站场名称 */
    private String siteName;
    
    /* 处理班组 */
    @Column(name = "Rev_OrgID")
    private Long revOrgID;
    
    /* 处理班组名称 */
    @Column(name = "Rev_OrgName")
    private String revOrgName;
    
    /* 处理班组序列 */
    @Column(name = "Rev_OrgSeq")
    private String revOrgSeq;
    
    /* 接票人编码 */
    @Column(name = "Rev_Person_Id")
    private Long revPersonId;
    
    /* 接票人名称 */
    @Column(name = "Rev_Person_Name")
    private String revPersonName;
    
    /* 接票时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Rev_Time")
    private java.util.Date revTime;
    
    /* 施修方法 */
    @Column(name = "Method_Desc")
    private String methodDesc;
    
    /* 处理结果，1：修复；2：观察运用；3：转JT28；4：转临修；5：返本段修；6：扣车等件； */
    @Column(name = "Repair_Result")
    private Integer repairResult;
    
    /* 处理描述 */
    @Column(name = "REPAIR_DESC")
    private String repairDesc;
    
    /* 销票人编码 */
    @Column(name = "Handle_Person_Id")
    private Long handlePersonId;
    
    /* 销票人名称 */
    @Column(name = "Handle_Person_Name")
    private String handlePersonName;
    
    /* 销票时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Handle_Time")
    private java.util.Date handleTime;
    
    /* 销票站场 */
    @Column(name = "Handle_SiteID")
    private String handleSiteID;
    
    /* 销票站场名称 */
    @Column(name = "Handle_SiteName")
    private String handleSiteName;
    
    /* 验收人编码 */
    @Column(name = "Acc_Person_Id")
    private Long accPersonId;
    
    /* 验收人名称 */
    @Column(name = "Acc_Person_Name")
    private String accPersonName;
    
    /* 验收时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Acc_Time")
    private java.util.Date accTime;
    
    /* 整备任务单ID */
    @Column(name = "RDP_IDX")
    private String rdpIDX;
    
    /* 票活状态 */
    @Column(name = "Fault_Notice_Status")
    private String faultNoticeStatus;
    
    /* 检修类型: 10：碎修；20临修； */
    @Column(name = "Repair_Class")
    private String repairClass;
    
    /* 记录状态，1：删除；0：未删除； */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 最新更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /* 处理人ID（多人人员之间用","号分隔开） */
    @Column(name = "Repair_Emp_ID")
    private String repairEmpID;
    
    /* 处理人名称（多人人员之间用","号分隔开） */
    @Column(name = "Repair_Emp")
    private String repairEmp;
    
    /* 发现人 */
    @Column(name = "DISCOVERER_ID")
    private Long discoverID;
    
    /* 发现人名称 */
    @Column(name = "DISCOVERER")
    private String discover;
    
    /* 是否被跟踪过（1==跟踪 0==未跟踪） */
    @Column(name = "isTracked")
    private Integer isTracked;
    
    /* 机车号 */
    @Transient
    private String trainTypeAndNo;
    
    /* 机车检测预警IDX */
    @Transient
    private String warningIDX;
    
    /* 故障发生日期 */
    @Transient
    private String faultOccurDateV;
    
    
    //整备综合统计计数
    @Transient
    private String zbthtjCount;
    
    /*提票开始时间*/
    @Transient
    private String startDate;
    
    /*提票结束时间*/
    @Transient 
    private String overDate;
    
    /* 录音文件idx */
    @Transient
    private String audioAttIdx;
    /* 是否是遗留活*/
    @Transient
    private Boolean isTpException;
    
    //返修次数
    @Column(name = "Repair_Times")
    private Integer repairTimes;
    
    // 校验提票
    @Column(name = "JY_STATUS")
    private String jyStatus ;
    
    /**
     * <li>说明：提票结束时间
     * <li>返回值： the overDate
     */
    public String getOverDate() {
        return overDate;
    }
    
    /**
     * <li>说明：提票结束时间
     * <li>参数： overDate
     */
    public void setOverDate(String overDate) {
        this.overDate = overDate;
    }
    
    /**
     * <li>说明：开始时间
     * <li>返回值： the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    
    /**
     * <li>说明：开始时间
     * <li>参数： startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * <li>说明：整备综合统计计数
     * <li>返回值： the zbthtjCount
     */
    public String getZbthtjCount() {
        return zbthtjCount;
    }
    
    /**
     * <li>说明：整备综合统计计数
     * <li>参数： zbthtjCount
     */
    public void setZbthtjCount(String zbthtjCount) {
        this.zbthtjCount = zbthtjCount;
    }

    /**
     * @return String 获取车型编码
     */
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }
    
    /**
     * @param trainTypeIDX 设置车型编码
     */
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }
    
    /**
     * @return String 获取车型拼音码
     */
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    /**
     * @param trainTypeShortName 设置车型拼音码
     */
    public void setTrainTypeShortName(String trainTypeShortName) {
        this.trainTypeShortName = trainTypeShortName;
    }
    
    /**
     * @return String 获取车号
     */
    public String getTrainNo() {
        return trainNo;
    }
    
    /**
     * @param trainNo 设置车号
     */
    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
    
    /**
     * @return String 获取配属段编码
     */
    public String getDID() {
        return dID;
    }
    
    /**
     * @param dID 设置配属段编码
     */
    public void setDID(String dID) {
        this.dID = dID;
    }
    
    /**
     * @return String 获取配属段名称
     */
    public String getDName() {
        return dName;
    }
    
    /**
     * @param dName 设置配属段名称
     */
    public void setDName(String dName) {
        this.dName = dName;
    }
    
    /**
     * @return java.util.Date 获取故障发生日期
     */
    public java.util.Date getFaultOccurDate() {
        return faultOccurDate;
    }
    
    /**
     * @param faultOccurDate 设置故障发生日期
     */
    public void setFaultOccurDate(java.util.Date faultOccurDate) {
        this.faultOccurDate = faultOccurDate;
    }
    
    /**
     * @return String 获取故障部件编码
     */
    public String getFaultFixFullCode() {
        return faultFixFullCode;
    }
    
    /**
     * @param faultFixFullCode 设置故障部件编码
     */
    public void setFaultFixFullCode(String faultFixFullCode) {
        this.faultFixFullCode = faultFixFullCode;
    }
    
    /**
     * @return String 获取故障部件名称
     */
    public String getFaultFixFullName() {
        return faultFixFullName;
    }
    
    /**
     * @param faultFixFullName 设置故障部件名称
     */
    public void setFaultFixFullName(String faultFixFullName) {
        this.faultFixFullName = faultFixFullName;
    }
    
    /**
     * @return String 获取故障ID
     */
    public String getFaultID() {
        return faultID;
    }
    
    /**
     * @param faultID 设置故障ID
     */
    public void setFaultID(String faultID) {
        this.faultID = faultID;
    }
    
    /**
     * @return String 获取故障现象
     */
    public String getFaultName() {
        return faultName;
    }
    
    /**
     * @param faultName 设置故障现象
     */
    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }
    
    /**
     * @return String 获取故障描述
     */
    public String getFaultDesc() {
        return faultDesc;
    }
    
    /**
     * @param faultDesc 设置故障描述
     */
    public void setFaultDesc(String faultDesc) {
        this.faultDesc = faultDesc;
    }
    
    /**
     * @return String 获取专业类型ID
     */
    public String getProfessionalTypeIdx() {
        return professionalTypeIdx;
    }
    
    /**
     * @param professionalTypeIdx 设置专业类型ID
     */
    public void setProfessionalTypeIdx(String professionalTypeIdx) {
        this.professionalTypeIdx = professionalTypeIdx;
    }
    
    /**
     * @return String 获取专业类型名称
     */
    public String getProfessionalTypeName() {
        return professionalTypeName;
    }
    
    /**
     * @param professionalTypeName 设置专业类型名称
     */
    public void setProfessionalTypeName(String professionalTypeName) {
        this.professionalTypeName = professionalTypeName;
    }
    
    /**
     * @return String 获取专业类型序列
     */
    public String getProfessionalTypeSeq() {
        return professionalTypeSeq;
    }
    
    /**
     * @param professionalTypeSeq 设置专业类型序列
     */
    public void setProfessionalTypeSeq(String professionalTypeSeq) {
        this.professionalTypeSeq = professionalTypeSeq;
    }
    
    /**
     * @return String 获取提票单号
     */
    public String getFaultNoticeCode() {
        return faultNoticeCode;
    }
    
    /**
     * @param faultNoticeCode 设置提票单号
     */
    public void setFaultNoticeCode(String faultNoticeCode) {
        this.faultNoticeCode = faultNoticeCode;
    }
    
    /**
     * @return String 获取提票来源
     */
    public String getNoticeSource() {
        return noticeSource;
    }
    
    /**
     * @param noticeSource 设置提票来源
     */
    public void setNoticeSource(String noticeSource) {
        this.noticeSource = noticeSource;
    }
    
    /**
     * @return Long 获取提票人编码
     */
    public Long getNoticePersonId() {
        return noticePersonId;
    }
    
    /**
     * @param noticePersonId 设置提票人编码
     */
    public void setNoticePersonId(Long noticePersonId) {
        this.noticePersonId = noticePersonId;
    }
    
    /**
     * @return String 获取提票人名称
     */
    public String getNoticePersonName() {
        return noticePersonName;
    }
    
    /**
     * @param noticePersonName 设置提票人名称
     */
    public void setNoticePersonName(String noticePersonName) {
        this.noticePersonName = noticePersonName;
    }
    
    /**
     * @return java.util.Date 获取提票时间
     */
    public java.util.Date getNoticeTime() {
        return noticeTime;
    }
    
    /**
     * @param noticeTime 设置提票时间
     */
    public void setNoticeTime(java.util.Date noticeTime) {
        this.noticeTime = noticeTime;
    }
    
    /**
     * @return String 获取提票站场
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @param siteID 设置提票站场
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @return String 获取提票站场名称
     */
    public String getSiteName() {
        return siteName;
    }
    
    /**
     * @param siteName 设置提票站场名称
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    /**
     * @return Long 获取处理班组编码
     */
    public Long getRevOrgID() {
        return revOrgID;
    }
    
    /**
     * @param revOrgID 设置处理班组编码
     */
    public void setRevOrgID(Long revOrgID) {
        this.revOrgID = revOrgID;
    }
    
    /**
     * @return String 获取处理班组名称
     */
    public String getRevOrgName() {
        return revOrgName;
    }
    
    /**
     * @param revOrgName 设置处理班组名称
     */
    public void setRevOrgName(String revOrgName) {
        this.revOrgName = revOrgName;
    }
    
    /**
     * @return String 获取处理班组序列
     */
    public String getRevOrgSeq() {
        return revOrgSeq;
    }
    
    /**
     * @param revOrgSeq 设置处理班组序列
     */
    public void setRevOrgSeq(String revOrgSeq) {
        this.revOrgSeq = revOrgSeq;
    }
    
    /**
     * @return Long 获取接票人编码
     */
    public Long getRevPersonId() {
        return revPersonId;
    }
    
    /**
     * @param revPersonId 设置接票人编码
     */
    public void setRevPersonId(Long revPersonId) {
        this.revPersonId = revPersonId;
    }
    
    /**
     * @return String 获取接票人名称
     */
    public String getRevPersonName() {
        return revPersonName;
    }
    
    /**
     * @param revPersonName 设置接票人名称
     */
    public void setRevPersonName(String revPersonName) {
        this.revPersonName = revPersonName;
    }
    
    /**
     * @return java.util.Date 获取接票时间
     */
    public java.util.Date getRevTime() {
        return revTime;
    }
    
    /**
     * @param revTime 设置接票时间
     */
    public void setRevTime(java.util.Date revTime) {
        this.revTime = revTime;
    }
    
    /**
     * @return String 获取施修方法
     */
    public String getMethodDesc() {
        return methodDesc;
    }
    
    /**
     * @param methodDesc 设置施修方法
     */
    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }
    
    /**
     * @return Integer 获取处理结果
     */
    public Integer getRepairResult() {
        return repairResult;
    }
    
    /**
     * @param repairResult 设置处理结果
     */
    public void setRepairResult(Integer repairResult) {
        this.repairResult = repairResult;
    }
    
    /**
     * @return Long 获取销票人编码
     */
    public Long getHandlePersonId() {
        return handlePersonId;
    }
    
    /**
     * @param handlePersonId 设置销票人编码
     */
    public void setHandlePersonId(Long handlePersonId) {
        this.handlePersonId = handlePersonId;
    }
    
    /**
     * @return String 获取销票人名称
     */
    public String getHandlePersonName() {
        return handlePersonName;
    }
    
    /**
     * @param handlePersonName 设置销票人名称
     */
    public void setHandlePersonName(String handlePersonName) {
        this.handlePersonName = handlePersonName;
    }
    
    /**
     * @return java.util.Date 获取销票时间
     */
    public java.util.Date getHandleTime() {
        return handleTime;
    }
    
    /**
     * @param handleTime 设置销票时间
     */
    public void setHandleTime(java.util.Date handleTime) {
        this.handleTime = handleTime;
    }
    
    /**
     * @return String 获取销票站场
     */
    public String getHandleSiteID() {
        return handleSiteID;
    }
    
    /**
     * @param handleSiteID 设置销票站场
     */
    public void setHandleSiteID(String handleSiteID) {
        this.handleSiteID = handleSiteID;
    }
    
    /**
     * @return String 获取销票站场名称
     */
    public String getHandleSiteName() {
        return handleSiteName;
    }
    
    /**
     * @param handleSiteName 设置销票站场名称
     */
    public void setHandleSiteName(String handleSiteName) {
        this.handleSiteName = handleSiteName;
    }
    
    /**
     * @return Long 获取验收人编码
     */
    public Long getAccPersonId() {
        return accPersonId;
    }
    
    /**
     * @param accPersonId 设置验收人编码
     */
    public void setAccPersonId(Long accPersonId) {
        this.accPersonId = accPersonId;
    }
    
    /**
     * @return String 获取验收人名称
     */
    public String getAccPersonName() {
        return accPersonName;
    }
    
    /**
     * @param accPersonName 设置验收人名称
     */
    public void setAccPersonName(String accPersonName) {
        this.accPersonName = accPersonName;
    }
    
    /**
     * @return java.util.Date 获取验收时间
     */
    public java.util.Date getAccTime() {
        return accTime;
    }
    
    /**
     * @param accTime 设置验收时间
     */
    public void setAccTime(java.util.Date accTime) {
        this.accTime = accTime;
    }
    
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
     * @return String 获取票活状态
     */
    public String getFaultNoticeStatus() {
        return faultNoticeStatus;
    }
    
    /**
     * @param faultNoticeStatus 设置票活状态
     */
    public void setFaultNoticeStatus(String faultNoticeStatus) {
        this.faultNoticeStatus = faultNoticeStatus;
    }
    
    /**
     * @return String 获取检修类型
     */
    public String getRepairClass() {
        return repairClass;
    }
    
    /**
     * @param repairClass 设置检修类型
     */
    public void setRepairClass(String repairClass) {
        this.repairClass = repairClass;
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
    
    public String getTrainTypeAndNo() {
        return trainTypeAndNo;
    }
    
    public void setTrainTypeAndNo(String trainTypeAndNo) {
        this.trainTypeAndNo = trainTypeAndNo;
    }
    
    public String getRepairDesc() {
        return repairDesc;
    }
    
    public void setRepairDesc(String repairDesc) {
        this.repairDesc = repairDesc;
    }
    
    public String getWarningIDX() {
        return warningIDX;
    }
    
    public void setWarningIDX(String warningIDX) {
        this.warningIDX = warningIDX;
    }
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     */
    public ZbglTp() {
        
    }
    
    /**
     * <li>说明：提票活处理列表实体构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-1-27
     * <li>修改人：
     * <li>修改日期：
     * @param idx 主键
     * @param faultFixFullName 故障部件
     * @param faultName 故障现象
     * @param faultDesc 故障描述
     * @param faultNoticeCode 提票单号
     * @param noticePersonName 提票人
     * @param noticeTime 提票时间
     * @param trainTypeAndNo 机车号
     * @param professionalTypeIdx 专业类型IDX
     * @param professionalTypeName 专业类型名称
     * @param professionalTypeSeq 专业类型seq
     * @param discoverID 发现人id
     * @param discover 发现人名称
     * @param faultID 故障现象id
     * @param faultFixFullCode 故障位置Code
     * @param methodDesc 施修方法
     * @param repairResult 处理结果
     * @param repairEmp 人员    多人人员之间用","号分隔开
     * @param repairEmpID人员id   多人人员之间用","号分隔开
     * @param faultReason 故障原因
     * @param repairDesc 处理描述
     */
    public ZbglTp(String idx, String faultFixFullName, String faultName, String faultDesc, String faultNoticeCode, String noticePersonName,
        Date noticeTime, String trainTypeAndNo, String professionalTypeIdx, String professionalTypeName, String professionalTypeSeq,Long discoverID,String discover,String faultID,String faultFixFullCode,Integer repairTimes
        ,String methodDesc, Integer repairResult, String repairEmp, String repairEmpID, String faultReason, String repairDesc) {
        this.idx = idx;
        this.faultFixFullName = faultFixFullName;
        this.faultName = faultName;
        this.faultDesc = faultDesc;
        this.faultNoticeCode = faultNoticeCode;
        this.noticePersonName = noticePersonName;
        this.noticeTime = noticeTime;
        this.trainTypeAndNo = trainTypeAndNo;
        this.professionalTypeIdx = professionalTypeIdx;
        this.professionalTypeName = professionalTypeName;
        this.professionalTypeSeq = professionalTypeSeq;
        this.discoverID = discoverID;
        this.discover = discover;
        this.faultID = faultID;
        this.faultFixFullCode = faultFixFullCode;
        this.repairTimes = repairTimes;
        this.methodDesc = methodDesc;
        this.repairResult = repairResult;
        this.repairEmp = repairEmp;
        this.repairEmpID = repairEmpID;
        this.faultReason = faultReason;
        this.repairDesc = repairDesc;
    }
    
    public String getFaultOccurDateV() {
        return faultOccurDateV;
    }
    
    public void setFaultOccurDateV(String faultOccurDateV) {
        this.faultOccurDateV = faultOccurDateV;
    }
    
    public String getRepairEmp() {
        return repairEmp;
    }
    
    public void setRepairEmp(String repairEmp) {
        this.repairEmp = repairEmp;
    }
    
    public String getRepairEmpID() {
        return repairEmpID;
    }
    
    public void setRepairEmpID(String repairEmpID) {
        this.repairEmpID = repairEmpID;
    }

    public String getAudioAttIdx() {
        return audioAttIdx;
    }

    public void setAudioAttIdx(String audioAttIdx) {
        this.audioAttIdx = audioAttIdx;
    }

    
    public String getFaultReason() {
        return faultReason;
    }

    
    public void setFaultReason(String faultReason) {
        this.faultReason = faultReason;
    }

    
    public String getDiscover() {
        return discover;
    }

    
    public void setDiscover(String discover) {
        this.discover = discover;
    }

    
    public Long getDiscoverID() {
        return discoverID;
    }

    
    public void setDiscoverID(Long discoverID) {
        this.discoverID = discoverID;
    }
	public Integer getIsTracked() {
		return isTracked;
	}

	public void setIsTracked(Integer isTracked) {
		this.isTracked = isTracked;
	}
    
    public Integer getRepairTimes() {
        return repairTimes;
    }

    
    public void setRepairTimes(Integer repairTimes) {
        this.repairTimes = repairTimes;
    }
    
    public String getJyStatus() {
        return jyStatus;
    }
    
    public void setJyStatus(String jyStatus) {
        this.jyStatus = jyStatus;
    }

    
    public Boolean getIsTpException() {
        return isTpException;
    }

    
    public void setIsTpException(Boolean isTpException) {
        this.isTpException = isTpException;
    }
}
