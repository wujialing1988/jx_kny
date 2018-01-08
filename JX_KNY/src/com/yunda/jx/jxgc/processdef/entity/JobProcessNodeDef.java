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
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobProcessNodeDef实体 数据表：检修作业流程节点
 * <li>创建人：何涛
 * <li>创建日期：2015-4-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_Job_Process_Node_Def")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class JobProcessNodeDef implements java.io.Serializable{

    /** 机车检修作业流程节点编码规则 */
    public static final String CODE_RULE_JOB_PROCESS_NODE_CODE = "JXGC_JOB_PROCESS_NODE_DEF_CODE";
    

    /** 是否是叶子节点 - 是（子节点） */
    public static final int CONST_INT_IS_LEAF_YES = 1;
    /** 是否是叶子节点 - 否（父节点） */
    public static final int CONST_INT_IS_LEAF_NO = 0;
    
    /** 前置节点类型 - FS */
    public static final String CONST_STR_SEQ_CLASS_FS = "FS";
    public static final String CONST_STR_SEQ_CLASS_FS_NAME = "完成-开始";
    /** 前置节点类型 - SS */
    public static final String CONST_STR_SEQ_CLASS_SS = "SS";
    public static final String CONST_STR_SEQ_CLASS_SS_NAME = "开始-开始";
    /** 前置节点类型 - FF */
    public static final String CONST_STR_SEQ_CLASS_FF = "FF";
    public static final String CONST_STR_SEQ_CLASS_FF_NAME = "完成-完成";
    /** 前置节点类型 - SF */
    public static final String CONST_STR_SEQ_CLASS_SF = "SF";
    public static final String CONST_STR_SEQ_CLASS_SF_NAME = "开始-完成";
    
    /** 计划模式 - 自动 */
    public static final String PLANMODE_AUTO = "AUTO";
    /** 计划模式 - 手动 */
    public static final String PLANMODE_MUNAUL = "MUNAUL";
    /** 计划模式 - 定时 */
    public static final String  PLANMODE_TIMER = "TIMER";
    
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 流程主键 */
	@Column(name="Process_IDX")
	private String processIDX;
	/* 上级主键 */
	@Column(name="Parent_IDX")
	private String parentIDX;
	/* 节点编码 */
	@Column(name="Node_Code")
	private String nodeCode;
	/* 节点名称 */
	@Column(name="Node_Name")
	private String nodeName;
	/* 额定工期（分钟） */
	@Column(name="Rated_WorkMinutes")
	private Double ratedWorkMinutes;
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
    /* 站点标识 */
    @Column(updatable=false)
    private String siteID;
    /* 创建者 */
    @Column(updatable=false)
    private Long creator;
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Create_Time",updatable=false)
    private java.util.Date createTime;
    /* 更新者*/
    private Long updator;
    /* 更新时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Update_Time")
    private java.util.Date updateTime;
    /* 前置节点（序号）,以逗号进行分割的数字序列 */
    @Transient
    private String preNodeSeqNo;
    /* 日历 */
    @Column(name = "Work_Calendar_IDX")
    private String workCalendarIDX;
    
    /* 计划模式 AUTO:自动，MANUAL：手动 */
    @Column(name = "Plan_Mode")
    private String planMode;
    
    /* 计划开始时间 */
    @Column(name="START_TIME")
    private String startTime;
    /* 计划结束时间 */
    @Column(name="END_TIME")
    private String endTime;
    /* 设定延误时间 */
    @Column(name="RELAY_TIME")
    private String relayTime;
    
    /* 开始天数 */
    @Column(name="START_DAY")
    private Integer startDay;
    /* 结束天数 */
    @Column(name="END_DAY")
    private Integer endDay;
    
    /* 流程显示样式 */
    @Column(name="SHOW_FLAG")    
    private String showFlag; 
    
	/* 是否属于临修节点 */
	@Column(name="IS_LXNODE")
	private Integer isLxNode;
    
    
    public String getShowFlag() {
        return showFlag;
    }



    
    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }



    public String getRelayTime() {
        return relayTime;
    }


    
    public void setRelayTime(String relayTime) {
        this.relayTime = relayTime;
    }


    public String getPlanMode() {
        return planMode;
    }

    
    public void setPlanMode(String planMode) {
        this.planMode = planMode;
    }

    
    public String getWorkCalendarIDX() {
        return workCalendarIDX;
    }

    
    public void setWorkCalendarIDX(String workCalendarIDX) {
        this.workCalendarIDX = workCalendarIDX;
    }

    /**
     * @return String 获取前置节点（序号） 
     */
    public String getPreNodeSeqNo() {
        return preNodeSeqNo;
    }
    
    /**
     * @param preNodeSeqNo 设置前置节点（序号） 
     */
    public void setPreNodeSeqNo(String preNodeSeqNo) {
        this.preNodeSeqNo = preNodeSeqNo;
    }
    /**
	 * @return String 获取流程主键
	 */
	public String getProcessIDX(){
		return processIDX;
	}
	/**
	 * @param processIDX 设置流程主键
	 */
	public void setProcessIDX(String processIDX) {
		this.processIDX = processIDX;
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
	 * @return String 获取节点编码
	 */
	public String getNodeCode(){
		return nodeCode;
	}
	/**
	 * @param nodeCode 设置节点编码
	 */
	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
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
	 * @return Double 获取额定工期（分钟）
	 */
	public Double getRatedWorkMinutes(){
		return ratedWorkMinutes;
	}
	/**
	 * @param ratedWorkMinutes 设置额定工期（分钟）
	 */
	public void setRatedWorkMinutes(Double ratedWorkMinutes) {
		this.ratedWorkMinutes = ratedWorkMinutes;
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
    /**
     * @return String 获取站点标识
     */
    public String getSiteID(){
        return siteID;
    }
    /**
     * @param siteID 设置站点标识
     */
    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }
    /**
     * @return Long 获取创建者
     */
    public Long getCreator(){
        return creator;
    }
    /**
     * @param creator 设置创建者
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    /**
     * @return java.util.Date 获取创建时间
     */
    public java.util.Date getCreateTime(){
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
    public Long getUpdator(){
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
    public java.util.Date getUpdateTime(){
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


    
    public Integer getEndDay() {
        return endDay;
    }


    
    public void setEndDay(Integer endDay) {
        this.endDay = endDay;
    }
    
    
    public String getEndTime() {
        return endTime;
    }


    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    
    public String getStartTime() {
        return startTime;
    }


    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public Integer getStartDay() {
        return startDay;
    }


    
    public void setStartDay(Integer startDay) {
        this.startDay = startDay;
    }

	public Integer getIsLxNode() {
		return isLxNode;
	}

	public void setIsLxNode(Integer isLxNode) {
		this.isLxNode = isLxNode;
	}
    
    
}