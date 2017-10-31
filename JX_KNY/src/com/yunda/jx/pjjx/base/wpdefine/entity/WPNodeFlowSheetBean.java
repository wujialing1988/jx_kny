package com.yunda.jx.pjjx.base.wpdefine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: WPNode流程实体类
 * <li>创建人：张迪
 * <li>创建日期：2017-3-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class WPNodeFlowSheetBean implements java.io.Serializable{
	
	/** 是否是叶子节点 - 是 */
	public static final int CONST_INT_IS_LEAF_YES = 1;
	/** 是否是叶子节点 - 否 */
	public static final int CONST_INT_IS_LEAF_NO = 0;
	
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@Id	
	private String idx;
	/* 作业流程主键 */
	@Column(name="WP_IDX")
	private String wpIDX;
	/* 上级作业节点主键 */
	@Column(name="PARENT_WP_NODE_IDX")
	private String parentWPNodeIDX;
	/* 节点名称 */
	@Column(name="WP_Node_Name")
	private String wpNodeName;
	/* 节点描述 */
	@Column(name="WP_Node_Desc")
	private String wpNodeDesc;
	/* 工期 */
	@Column(name="Rated_Period")
	private Double ratedPeriod;
	/* 顺序号 */
	@Column(name="Seq_No")
	private Integer seqNo;
	/* 是否叶子节点,0:否；1：是 */
	@Column(name="IS_LEAF")
	private Integer isLeaf;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;
    
       /* 计划开始时间 */
    @Column(name="START_TIME")
    private String startTime;
    /* 计划结束时间 */
    @Column(name="END_TIME")
    private String endTime;
    
    /* 开始天数 */
    @Column(name="START_DAY")
    private Integer startDay;
    /* 结束天数 */
    @Column(name="END_DAY")
    private Integer endDay;
    
    /* 流程显示样式 */
    @Column(name="Show_Flag")
    private String showFlag;
    /* 前置节点主键 */
    @Column(name="Pre_WP_Node_IDX")
    private String preWPNodeIDX;
    /* 后置节点主键 */
    @Column(name="NEXT_WP_Node_IDX")
    private String nextWPNodeIDX;
      
    /* 状态 */
    @Transient
    private String status;
    /* 各状态节点数量 */
    @Transient
    private String countStr;
    
    public String getPreWPNodeIDX() {
        return preWPNodeIDX;
    }
    
    public void setPreWPNodeIDX(String preWPNodeIDX) {
        this.preWPNodeIDX = preWPNodeIDX;
    }
   	/**
	 * @return String 获取作业流程idx主键
	 */
	public String getWpIDX(){
		return wpIDX;
	}
	/**
	 * @param wPIDX 设置作业流程idx主键
	 */
	public void setWpIDX(String wPIDX) {
		this.wpIDX = wPIDX;
	}
	/**
	 * @return 获取上级作业节点主键
	 */
	public String getParentWPNodeIDX() {
		return parentWPNodeIDX;
	}
	/**
	 * @param parentWPNodeIDX 设置上级作业节点主键
	 */
	public void setParentWPNodeIDX(String parentWPNodeIDX) {
		this.parentWPNodeIDX = parentWPNodeIDX;
	}
	/**
	 * @return String 获取节点名称
	 */
	public String getWpNodeName(){
		return wpNodeName;
	}
	/**
	 * @param wPNodeName 设置节点名称
	 */
	public void setWpNodeName(String wPNodeName) {
		this.wpNodeName = wPNodeName;
	}
	/**
	 * @return String 获取节点描述
	 */
	public String getWpNodeDesc(){
		return wpNodeDesc;
	}
	/**
	 * @param wPNodeDesc 设置节点描述
	 */
	public void setWpNodeDesc(String wPNodeDesc) {
		this.wpNodeDesc = wPNodeDesc;
	}
	/**
	 * @return Double 获取工期
	 */
	public Double getRatedPeriod(){
		return ratedPeriod;
	}
	/**
	 * @param ratedPeriod 设置工期
	 */
	public void setRatedPeriod(Double ratedPeriod) {
		this.ratedPeriod = ratedPeriod;
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
	 * @return 获取是否是叶子节点
	 */
	public Integer getIsLeaf() {
		return isLeaf;
	}
	/**
	 * @param isLeaf 设置是否是叶子节点
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
	 * @return Long 获取创建人
	 */
	public Long getCreator(){
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
	 * @return String 主键
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
    
    public Integer getStartDay() {
        return startDay;
    }
    
    public void setStartDay(Integer startDay) {
        this.startDay = startDay;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getShowFlag() {
        return showFlag;
    }
    
    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

    
    public String getNextWPNodeIDX() {
        return nextWPNodeIDX;
    }

    
    public void setNextWPNodeIDX(String nextWPNodeIDX) {
        this.nextWPNodeIDX = nextWPNodeIDX;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
    }

    
    public String getCountStr() {
        return countStr;
    }

    
    public void setCountStr(String countStr) {
        this.countStr = countStr;
    }
}