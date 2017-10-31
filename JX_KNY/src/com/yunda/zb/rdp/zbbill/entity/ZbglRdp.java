package com.yunda.zb.rdp.zbbill.entity;

import java.util.List;

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
 * <li>说明：ZbglRdp实体类, 数据表：机车整备单
 * <li>创建人：程锐
 * <li>创建日期：2015-01-21
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "ZB_ZBGL_RDP")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdp implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* 状态-整备中 */
    public static final String STATUS_HANDLING = "ONGOING";
    
    /* 状态-整备完成 */
    public static final String STATUS_HANDLED = "COMPLETE";
    
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 车型主键 */
    @Column(name = "Train_Type_IDX")
    private String trainTypeIDX;
    
    /* 车型简称 */
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
    
    /* 整备站场 */
    @Column(updatable = false)
    private String siteID;
    
    /* 整备站场名称 */
    private String siteName;
    
    /* 整备开始时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Rdp_Start_Time")
    private java.util.Date rdpStartTime;
    
    /* 整备结束时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Rdp_End_Time")
    private java.util.Date rdpEndTime;
    
    /* 整备后去向 */
    @Column(name = "To_Go")
    private String toGo;
    
    /* 机车出入段台账主键 */
    @Column(name = "Train_Access_Account_IDX")
    private String trainAccessAccountIDX;
    
    /* 整备单状态 */
    @Column(name = "Rdp_Status")
    private String rdpStatus;
    
    /* 记录状态，1：删除；0：未删除； */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 最新更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /* 检修类型: 10：碎修；20临修； */
    @Column(name = "Repair_Class")
    private String repairClass;
    
    /* 交验交车人 */
    @Column(name = "From_PersonId")
    private Long fromPersonId;
    
    /* 交验交车人名称 */
    @Column(name = "From_PersonName")
    private String fromPersonName;
    
    /* 交验接车人 */
    @Column(name = "To_PersonId")
    private Long toPersonId;
    
    /* 交验接车人名称 */
    @Column(name = "To_PersonName")
    private String toPersonName;
    
    /* 出段车次 */
    @Column(name = "Out_Order")
    private String outOrder;
    
    /* 交验情况描述 */
    private String remarks;
    
    /* 整备范围主键 */
    @Column(name = "zbfw_IDX")
    private String zbfwIDX;
    
    /* 机车整备单下属的作业节点（第一层） */
    @Transient
    private List<ZbglRdpNode> zbglRdpNodes;
    
    /* 新增字段 是否做范围活 1=做 0=不做 **/
    @Column(name = "is_Do_ZbglRdpWi")
    private Integer isDoZbglRdpWi;
    
    
    public Integer getIsDoZbglRdpWi() {
        return isDoZbglRdpWi;
    }
    
    public void setIsDoZbglRdpWi(Integer isDoZbglRdpWi) {
        this.isDoZbglRdpWi = isDoZbglRdpWi;
    }

    /**
     * @return String 获取车型主键
     */
    public String getTrainTypeIDX() {
        return trainTypeIDX;
    }
    
    /**
     * @param trainTypeIDX 设置车型主键
     */
    public void setTrainTypeIDX(String trainTypeIDX) {
        this.trainTypeIDX = trainTypeIDX;
    }
    
    /**
     * @return String 获取车型简称
     */
    public String getTrainTypeShortName() {
        return trainTypeShortName;
    }
    
    /**
     * @param trainTypeShortName 设置车型简称
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
     * @return String 获取整备站场
     */
    public String getSiteID() {
        return siteID;
    }
    
    /**
     * @param siteID 设置整备站场
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    /**
     * @return String 获取整备站场名称
     */
    public String getSiteName() {
        return siteName;
    }
    
    /**
     * @param siteName 设置整备站场名称
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    /**
     * @return java.util.Date 获取整备开始时间
     */
    public java.util.Date getRdpStartTime() {
        return rdpStartTime;
    }
    
    /**
     * @param rdpStartTime 设置整备开始时间
     */
    public void setRdpStartTime(java.util.Date rdpStartTime) {
        this.rdpStartTime = rdpStartTime;
    }
    
    /**
     * @return java.util.Date 获取整备结束时间
     */
    public java.util.Date getRdpEndTime() {
        return rdpEndTime;
    }
    
    /**
     * @param rdpEndTime 设置整备结束时间
     */
    public void setRdpEndTime(java.util.Date rdpEndTime) {
        this.rdpEndTime = rdpEndTime;
    }
    
    /**
     * @return String 获取整备后去向
     */
    public String getToGo() {
        return toGo;
    }
    
    /**
     * @param toGo 设置整备后去向
     */
    public void setToGo(String toGo) {
        this.toGo = toGo;
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
     * @return String 获取整备单状态
     */
    public String getRdpStatus() {
        return rdpStatus;
    }
    
    /**
     * @param rdpStatus 设置整备单状态
     */
    public void setRdpStatus(String rdpStatus) {
        this.rdpStatus = rdpStatus;
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
     * @return Long 获取交验交车人
     */
    public Long getFromPersonId() {
        return fromPersonId;
    }
    
    /**
     * @param fromPersonId 设置交验交车人
     */
    public void setFromPersonId(Long fromPersonId) {
        this.fromPersonId = fromPersonId;
    }
    
    /**
     * @return String 获取交验交车人名称
     */
    public String getFromPersonName() {
        return fromPersonName;
    }
    
    /**
     * @param fromPersonName 设置交验交车人名称
     */
    public void setFromPersonName(String fromPersonName) {
        this.fromPersonName = fromPersonName;
    }
    
    /**
     * @return Long 获取交验接车人
     */
    public Long getToPersonId() {
        return toPersonId;
    }
    
    /**
     * @param toPersonId 设置交验接车人
     */
    public void setToPersonId(Long toPersonId) {
        this.toPersonId = toPersonId;
    }
    
    /**
     * @return String 获取交验接车人名称
     */
    public String getToPersonName() {
        return toPersonName;
    }
    
    /**
     * @param toPersonName 设置交验接车人名称
     */
    public void setToPersonName(String toPersonName) {
        this.toPersonName = toPersonName;
    }
    
    /**
     * @return String 获取出段车次
     */
    public String getOutOrder() {
        return outOrder;
    }
    
    /**
     * @param outOrder 设置出段车次
     */
    public void setOutOrder(String outOrder) {
        this.outOrder = outOrder;
    }
    
    /**
     * @return String 获取交验情况描述
     */
    public String getRemarks() {
        return remarks;
    }
    
    /**
     * @param remarks 设置交验情况描述
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
    
    public String getZbfwIDX() {
        return zbfwIDX;
    }
    
    public void setZbfwIDX(String zbfwIDX) {
        this.zbfwIDX = zbfwIDX;
    }
    
    public List<ZbglRdpNode> getZbglRdpNodes() {
        return zbglRdpNodes;
    }
    
    public void setZbglRdpNodes(List<ZbglRdpNode> zbglRdpNodes) {
        this.zbglRdpNodes = zbglRdpNodes;
    }
    
}
