package com.yunda.zb.zbfw.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: ZbglJobProcessNodeDef实体 数据表：整备作业流程节点
 * <li>创建人：程梅
 * <li>创建日期：2016年4月7日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_Job_Process_Node_Def")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglJobProcessNodeDef implements java.io.Serializable{

    /** 整备作业流程节点编码规则 */
    public static final String CODE_RULE_JOB_PROCESS_NODE_CODE = "ZBGL_JOB_PROCESS_NODE_DEF_CODE";
    

    /** 是否是叶子节点 - 是（子节点） */
    public static final int CONST_INT_IS_LEAF_YES = 1;
    /** 是否是叶子节点 - 否（父节点） */
    public static final int CONST_INT_IS_LEAF_NO = 0;
    
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
    /* 整备范围主键 */
    @Column(name="ZBFW_IDX")
    private String zbfwIDX;
	/* 上级主键 */
	@Column(name="Parent_IDX")
	private String parentIDX;
	/* 节点名称 */
	@Column(name="Node_Name")
	private String nodeName;
	/* 顺序号 */
	@Column(name="Seq_No")
	private Integer seqNo;
	/* 节点描述 */
	@Column(name="Node_Desc")
	private String nodeDesc;
	/* 是否叶子节点 */
	@Column(name="Is_Leaf")
	private Integer isLeaf;
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name="Record_Status")
    private Integer recordStatus;
    
    /* 工位id */
    @Column(name = "WORK_STATION_IDX")
    private String workStationIDX;
    
    /* 工位名称 */
    @Column(name = "Work_Station_Name")
    private String workStationName;
    
    /* 前置节点（序号）,以逗号进行分割的数字序列 */
    @Transient
    private String preNodeSeqNo;
    
	
    public String getPreNodeSeqNo() {
        return preNodeSeqNo;
    }
    
    public void setPreNodeSeqNo(String preNodeSeqNo) {
        this.preNodeSeqNo = preNodeSeqNo;
    }
    /**
	 * @return String 获取上级主键
	 */
	public String getParentIDX(){
		return parentIDX;
	}
	/**
	 * @param parentIDX 设置上级主键
	 */
	public void setParentIDX(String parentIDX) {
		this.parentIDX = parentIDX;
	}
	
	/**
	 * @return String 获取节点名称
	 */
	public String getNodeName(){
		return nodeName;
	}
	/**
	 * @param nodeName 设置节点名称
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	/**
	 * @return Integer 获取顺序号
	 */
	public Integer getSeqNo(){
		return seqNo;
	}
	/**
	 * @param seqNo 设置顺序号
   	 */
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	/**
	 * @return String 获取节点描述
	 */
	public String getNodeDesc(){
		return nodeDesc;
	}
	/**
	 * @param nodeDesc 设置节点描述
	 */
	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}
	/**
	 * @return Integer 获取是否叶子节点
	 */
	public Integer getIsLeaf(){
		return isLeaf;
	}
	/**
	 * @param isLeaf 设置是否叶子节点
	 */
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}
    /**
     * @return Integer 获取记录的状态
     */
    public Integer getRecordStatus(){
        return recordStatus;
    }
    /**
     * @param recordStatus 设置记录的状态
     */
    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getZbfwIDX() {
        return zbfwIDX;
    }
    
    public void setZbfwIDX(String zbfwIDX) {
        this.zbfwIDX = zbfwIDX;
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