package com.yunda.zb.rdp.zbbill.entity;

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
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车整备作业节点实体类
 * <li>创建人：程锐
 * <li>创建日期：2016-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2
 */
@Entity
@Table(name = "ZB_ZBGL_Job_Process_Node")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class ZbglRdpNode implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /** 状态 - 未处理 */
    public static final String STATUS_UNSTART = "NOTSTARTED";
    
    /** 状态 - 处理中 */
    public static final String STATUS_GOING = "RUNNING";
    
    /** 状态 - 已处理 */
    public static final String STATUS_COMPLETE = "COMPLETED";
    
    /** 叶子节点 */
    public static final int NODE_LEAF = 1;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 整备单主键 */
    @Column(name = "RDP_IDX")
    private String rdpIDX;
    
    /* 节点定义主键 */
    @Column(name = "Node_Def_IDX")
    private String nodeDefIDX;
    
    /* 整备范围主键 */
    @Column(name = "zbfw_IDX")
    private String zbfwIDX;
    
    /* 节点名称 */
    @Column(name = "Node_Name")
    private String nodeName;
    
    /* 节点描述 */
    @Column(name = "Node_Desc")
    private String nodeDesc;
    
    /* 节点序号 */
    @Column(name = "Seq_No")
    private Integer seqNo;
    
    /* 实际开工时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Real_Begin_Time")
    private java.util.Date realBeginTime;
    
    /* 实际完工时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Real_End_Time")
    private java.util.Date realEndTime;
    
    /* 状态：未处理，处理中，已处理 */
    private String status;
    
    /* 父级节点 */
    @Column(name = "Parent_IDX")
    private String parentIDX;
    
    /* 是否叶子节点,0:否；1：是 */
    @Column(name = "Is_Leaf")
    private Integer isLeaf;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 工位id */
    @Column(name = "WORK_STATION_IDX")
    private String workStationIDX;
    
    /* 工位名称 */
    @Column(name = "Work_Station_Name")
    private String workStationName;
    
    
    /* 时间轴上的未处理开始显示时间 */
    @Transient
    private java.util.Date showBeginTime;
    /* 时间轴上的未处理线束显示时间 */
    @Transient
    private java.util.Date showEndTime;
    @Transient
    private int flag;
    public String getIdx() {
        return idx;
    }
    
    public void setIdx(String idx) {
        this.idx = idx;
    }
    
    public Integer getIsLeaf() {
        return isLeaf;
    }
    
    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }
    
    public String getNodeDefIDX() {
        return nodeDefIDX;
    }
    
    public void setNodeDefIDX(String nodeDefIDX) {
        this.nodeDefIDX = nodeDefIDX;
    }
    
    public String getNodeDesc() {
        return nodeDesc;
    }
    
    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }
    
    public String getNodeName() {
        return nodeName;
    }
    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
    public String getParentIDX() {
        return parentIDX;
    }
    
    public void setParentIDX(String parentIDX) {
        this.parentIDX = parentIDX;
    }
    
    public String getRdpIDX() {
        return rdpIDX;
    }
    
    public void setRdpIDX(String rdpIDX) {
        this.rdpIDX = rdpIDX;
    }
    
    public java.util.Date getRealBeginTime() {
        return realBeginTime;
    }
    
    public void setRealBeginTime(java.util.Date realBeginTime) {
        this.realBeginTime = realBeginTime;
    }
    
    public java.util.Date getRealEndTime() {
        return realEndTime;
    }
    
    public void setRealEndTime(java.util.Date realEndTime) {
        this.realEndTime = realEndTime;
    }
    
    public Integer getRecordStatus() {
        return recordStatus;
    }
    
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public Integer getSeqNo() {
        return seqNo;
    }
    
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getZbfwIDX() {
        return zbfwIDX;
    }
    
    public void setZbfwIDX(String zbfwIDX) {
        this.zbfwIDX = zbfwIDX;
    }

    
    public java.util.Date getShowBeginTime() {
        return showBeginTime;
    }

    
    public void setShowBeginTime(java.util.Date showBeginTime) {
        this.showBeginTime = showBeginTime;
    }

    
    public java.util.Date getShowEndTime() {
        return showEndTime;
    }

    
    public void setShowEndTime(java.util.Date showEndTime) {
        this.showEndTime = showEndTime;
    }

    
    public int getFlag() {
        return flag;
    }

    
    public void setFlag(int flag) {
        this.flag = flag;
    }

    
    public String getWorkStationIDX() {
        return workStationIDX;
    }

    
    public void setWorkStationIDX(String workStationIDX) {
        this.workStationIDX = workStationIDX;
    }

    
    public String getWorkStationName() {
        return workStationName;
    }

    
    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }
    
}
