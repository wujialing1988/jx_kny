package com.yunda.jx.jxgc.workplanmanage.entity;

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
 * <li>说明：JobProcessNodeRel实体类, 数据表：机车检修作业计划-前置节点
 * <li>创建人：程锐
 * <li>创建日期：2015-04-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */
@Entity
@Table(name = "JXGC_Job_Process_Node_Rel")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class JobProcessNodeRel implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 节点主键 */
    @Column(name = "Node_IDX")
    private String nodeIDX;
    
    /* 前置节点主键 */
    @Column(name = "Pre_Node_IDX")
    private String preNodeIDX;
    
    /* 类型 */
    @Column(name = "Seq_Class")
    private String seqClass;
    
    /* 延隔时间 */
    @Column(name = "Delay_Time")
    private Double delayTime;
    
    /* 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
    /* 前置节点名称 */
    @Transient
    private String preNodeName;
    
    /* 类型名称 */
    @Transient
    private String seqClassName;
    
    /**
     * @return String 获取节点主键
     */
    public String getNodeIDX() {
        return nodeIDX;
    }
    
    /**
     * @param nodeIDX 设置节点主键
     */
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }
    
    /**
     * @return String 获取前置节点主键
     */
    public String getPreNodeIDX() {
        return preNodeIDX;
    }
    
    /**
     * @param preNodeIDX 设置前置节点主键
     */
    public void setPreNodeIDX(String preNodeIDX) {
        this.preNodeIDX = preNodeIDX;
    }
    
    /**
     * @return String 获取类型
     */
    public String getSeqClass() {
        return seqClass;
    }
    
    /**
     * @param seqClass 设置类型
     */
    public void setSeqClass(String seqClass) {
        this.seqClass = seqClass;
    }
    
    /**
     * @return Double 获取延隔时间
     */
    public Double getDelayTime() {
        return delayTime;
    }
    
    /**
     * @param delayTime 设置延隔时间
     */
    public void setDelayTime(Double delayTime) {
        this.delayTime = delayTime;
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
    
    public String getPreNodeName() {
        return preNodeName;
    }
    
    public void setPreNodeName(String preNodeName) {
        this.preNodeName = preNodeName;
    }
    
    public String getSeqClassName() {
        return seqClassName;
    }
    
    public void setSeqClassName(String seqClassName) {
        this.seqClassName = seqClassName;
    }
    
    /**
     * <li>说明：默认构造方法
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     */
    public JobProcessNodeRel() {
        
    }
    
    /**
     * 
     * <li>说明：流程节点前置关系列表所用实体
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人： 
     * <li>修改日期：
     * @param idx 流程节点前置关系IDX
     * @param nodeIDX 流程节点IDX
     * @param preNodeIDX 前置节点IDX
     * @param seqClass 类型
     * @param delayTime 延搁时间
     * @param preNodeName 前置节点名称
     * @param seqClassName 类型名称
     */
    public JobProcessNodeRel(String idx, 
                             String nodeIDX, 
                             String preNodeIDX, 
                             String seqClass, 
                             Double delayTime, 
                             String preNodeName, 
                             String seqClassName) {
        super();
        this.idx = idx;
        this.nodeIDX = nodeIDX;
        this.preNodeIDX = preNodeIDX;
        this.seqClass = seqClass;
        this.delayTime = delayTime;
        this.preNodeName = preNodeName;
        this.seqClassName = seqClassName;
    }
    
}
