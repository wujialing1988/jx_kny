package com.yunda.jx.jxgc.processdef.entity;

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
 * <li>说明: 机车检修用料定义
 * <li>创建人：张迪
 * <li>创建日期：2016-9-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_JOBNODE_MAT_DEF")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class JobNodeMatDef implements java.io.Serializable{
    /** 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;

    /** idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;

    /** 作业节点idx主键 */
    @Column(name = "Node_IDX")
    private String nodeIDX;
    
    /**  作业流程主键 */
    @Column(name="Process_IDX")
    private String processIDX;
    
    /**  上级作业节点主键 */
    @Column(name="Parent_Node_IDX")
    private String parentNodeIDX;
    
    /**  节点名称 */
    @Column(name="Node_Name")    
    private String nodeName;
    /** 物料编码 */
    @Column(name = "Mat_Code")
    private String matCode;

    /** 物料描述 */
    @Column(name = "Mat_Desc")
    private String matDesc;

    /** 单位 */
    private String unit;
    
    /** 数量 */
    @Column(name = "Default_Qty")
    private Long qty;
    
    /** 查询字段 - 单价 */
    @Transient
    private Double price;
    
    /** 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;

    /** 站点标识，为了同步数据而使用 */
    @Column(updatable = false)
    private String siteID;

    /** 创建人 */
    @Column(updatable = false)
    private Long creator;

    /** 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private java.util.Date createTime;

    /** 修改人 */
    private Long updator;

    /** 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public Long getCreator() {
        return creator;
    }

    public String getIdx() {
        return idx;
    }

    public String getMatCode() {
        return matCode;
    }

    public String getMatDesc() {
        return matDesc;
    }

    public Double getPrice() {
        return price;
    }

    public Long getQty() {
        return qty;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public String getSiteID() {
        return siteID;
    }

    public String getUnit() {
        return unit;
    }

    public java.util.Date getUpdateTime() {
        return updateTime;
    }

    public Long getUpdator() {
        return updator;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public void setMatDesc(String matDesc) {
        this.matDesc = matDesc;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setUpdator(Long updator) {
        this.updator = updator;
    }

    
    public String getNodeIDX() {
        return nodeIDX;
    }

    
    public void setNodeIDX(String nodeIDX) {
        this.nodeIDX = nodeIDX;
    }

    
    public String getNodeName() {
        return nodeName;
    }

    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    
    public String getParentNodeIDX() {
        return parentNodeIDX;
    }

    
    public void setParentNodeIDX(String parentNodeIDX) {
        this.parentNodeIDX = parentNodeIDX;
    }

    
    public String getProcessIDX() {
        return processIDX;
    }

    
    public void setProcessIDX(String processIDX) {
        this.processIDX = processIDX;
    }

   
}