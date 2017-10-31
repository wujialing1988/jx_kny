package com.yunda.jx.jxgc.producttaskmanage.entity;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 工序延误记录实体类
 * <li>创建人：张凡
 * <li>创建日期：2013-5-6 下午03:32:05
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "JXGC_Node_Case_Delay")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class NodeCaseDelay {
    
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    @Column(name = "node_case_Idx")
    private String nodeCaseIdx;
    
    @Column(name = "node_idx")
    private String nodeIDX;
    
    @Column(name = "Tec_Process_Case_IDX")
    private String tecProcessCaseIDX;
    
    @Column(name = "rdp_idx")
    private String rdpIDX;
    
    @Column(name = "delay_type")
    private String delayType;
    
    @Column(name = "delay_time")
    private Integer delayTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "plan_begin_time")
    private Date planBeginTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "plan_end_time")
    private Date planEndTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "real_begin_time")
    private Date realBeginTime;
    
    @Column(name = "delay_reason")
    private String delayReason;
    
    @Column(name = "siteID")
    private String siteID;
    
    @Column(name = "record_status")
    private Integer recordStatus;
    
    @Column(updatable = false)
    private Long creator;
    
    private Long updator;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private Date createTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;
    
    @Transient
    private String nodeName;
    
    public String getNodeName() {
        return nodeName;
    }
    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public String getNodeCaseIdx() {
        return nodeCaseIdx;
    }
    
    public void setNodeCaseIdx(String nodeCaseIdx) {
        this.nodeCaseIdx = nodeCaseIdx;
    }
    
    public String getDelayType() {
        return delayType;
    }
    
    public void setDelayType(String delayType) {
        this.delayType = delayType;
    }
    
    public Integer getDelayTime() {
        return delayTime;
    }
    
    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }
    
    public Date getPlanBeginTime() {
        return planBeginTime;
    }
    
    public void setPlanBeginTime(Date planBeginTime) {
        this.planBeginTime = planBeginTime;
    }
    
    public Date getPlanEndTime() {
        return planEndTime;
    }
    
    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }
    
    public String getDelayReason() {
        return delayReason;
    }
    
    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }
    
    public String getSiteID() {
        return siteID;
    }
    
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public Long getCreator() {
        return creator;
    }
    
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    public Long getUpdator() {
        return updator;
    }
    
    public void setUpdator(Long updator) {
        this.updator = updator;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Date getRealBeginTime() {
        return realBeginTime;
    }
    
    public void setRealBeginTime(Date realBeginTime) {
        this.realBeginTime = realBeginTime;
    }
    
    public String getNodeIDX() {
        return nodeIDX;
    }
    
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }
    
    public String getTecProcessCaseIDX() {
        return tecProcessCaseIDX;
    }
    
    public void setTecProcessCaseIDX(String tecProcessCaseIDX) {
        this.tecProcessCaseIDX = tecProcessCaseIDX;
    }
    
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
}
