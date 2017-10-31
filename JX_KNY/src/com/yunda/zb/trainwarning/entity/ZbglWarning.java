package com.yunda.zb.trainwarning.entity;

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
 * <li>说明：ZbglWarning实体类, 数据表：机车检测预警
 * <li>创建人：程锐
 * <li>创建日期：2015-03-04
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_Warning")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglWarning implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 状态-未处理 */
    public static final String STATUS_TODO = "TODO";
    
    public static final String STATUS_TODO_CH = "未处理";
    
    /* 状态-下发 */
    public static final String STATUS_RELEASE = "RELEASE";
    
    public static final String STATUS_RELEASE_CH = "下发";
    
    /* 状态-已下发 */
    public static final String STATUS_RELEASED = "RELEASED";
    
    public static final String STATUS_RELEASED_CH = "已下发班组";
    
    /* 状态-已转提票 */
    public static final String STATUS_NOTICE = "NOTICE";
    
    public static final String STATUS_NOTICE_CH = "已转提票";
    
    /* 状态-取消预警 */
    public static final String STATUS_CANCEL = "CANCEL";
    
    public static final String STATUS_CANCEL_CH = "取消预警";
    
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
    
    /* 机车出入段台账主键 */
    @Column(name = "Train_Access_Account_IDX")
    private String trainAccessAccountIDX;
    
    /* 报活来源 */
    @Column(name = "Warning_Source")
    private String warningSource;
    
    /* 报活时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Warning_Time")
    private java.util.Date warningTime;
    
    /* 报活位置 */
    @Column(name = "Fix_Place")
    private String fixPlace;
    
    /* 活票描述 */
    @Column(name = "Warning_Desc")
    private String warningDesc;
    
    /* 活票状态，TODO：未处理；RELEASE：下发；RELEASED：已下发；NOTICE：已转提票；CANCEL：取消预警 */
    @Column(name = "Warning_Status")
    private String warningStatus;
    
    /* 处理人编码 */
    @Column(name = "Handle_Person_ID")
    private Long handlePersonID;
    
    /* 处理时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Handle_Time")
    private java.util.Date handleTime;
    
    /* 处理人名称 */
    @Column(name = "Handle_Person_Name")
    private String handlePersonName;
    
    /* 检查站场 */
    private String siteID;
    
    /* 检查站场名称 */
    private String siteName;
    
    /* 处理ID */
    @Column(name = "Rel_IDX")
    private String relIDX;
    
    /* 记录状态，1：删除；0：未删除； */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 最新更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /* 机车整备单IDX */
    @Transient
    private String rdpIDX;
    
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
     * @return String 获取机车出入段台账主键
     */
    public String getTrainAccessAccountIDX() {
        return trainAccessAccountIDX;
    }
    
    /**
     * @param trainAccessAccountIDX 设置机车出入段台账主键
     */
    public void setTrainAccessAccountIDX(String trainAccessAccountIDX) {
        this.trainAccessAccountIDX = trainAccessAccountIDX;
    }
    
    /**
     * @return String 获取预警来源
     */
    public String getWarningSource() {
        return warningSource;
    }
    
    /**
     * @param warningSource 设置预警来源
     */
    public void setWarningSource(String warningSource) {
        this.warningSource = warningSource;
    }
    
    /**
     * @return java.util.Date 获取预警时间
     */
    public java.util.Date getWarningTime() {
        return warningTime;
    }
    
    /**
     * @param warningTime 设置预警时间
     */
    public void setWarningTime(java.util.Date warningTime) {
        this.warningTime = warningTime;
    }
    
    /**
     * @return String 获取预警位置
     */
    public String getFixPlace() {
        return fixPlace;
    }
    
    /**
     * @param fixPlace 设置预警位置
     */
    public void setFixPlace(String fixPlace) {
        this.fixPlace = fixPlace;
    }
    
    /**
     * @return String 获取预警描述
     */
    public String getWarningDesc() {
        return warningDesc;
    }
    
    /**
     * @param warningDesc 设置预警描述
     */
    public void setWarningDesc(String warningDesc) {
        this.warningDesc = warningDesc;
    }
    
    /**
     * @return String 获取预警状态
     */
    public String getWarningStatus() {
        return warningStatus;
    }
    
    /**
     * @param warningStatus 设置预警状态
     */
    public void setWarningStatus(String warningStatus) {
        this.warningStatus = warningStatus;
    }
    
    /**
     * @return Long 获取处理人编码
     */
    public Long getHandlePersonID() {
        return handlePersonID;
    }
    
    /**
     * @param handlePersonID 设置处理人编码
     */
    public void setHandlePersonID(Long handlePersonID) {
        this.handlePersonID = handlePersonID;
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
     * @return String 获取检查站场
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @param siteID 设置检查站场
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @return String 获取检查站场名称
     */
    public String getSiteName() {
        return siteName;
    }
    
    /**
     * @param siteName 设置检查站场名称
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    /**
     * @return String 获取处理ID
     */
    public String getRelIDX() {
        return relIDX;
    }
    
    /**
     * @param relIDX 设置处理ID
     */
    public void setRelIDX(String relIDX) {
        this.relIDX = relIDX;
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
    
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
}
