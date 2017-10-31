package com.yunda.jx.pjjx.partsrdp.wpinst.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 配件检修节点流程图实体类
 * <li>创建人：张迪
 * <li>创建日期：2017-3-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
public class PartsRdpNodeFlowSheetBean implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/** 检修作业流程节点状态 - 未处理 */
	public static final String CONST_STR_STATUS_WCL = "01";
	/** 检修作业流程节点状态 - 处理中 */
	public static final String CONST_STR_STATUS_CLZ = "02";
	/** 检修作业流程节点状态 - 已处理 */
	public static final String CONST_STR_STATUS_YCL = "03";
    /** 检修作业流程节点状态 - 返修 */
	public static final String CONST_STR_STATUS_FX = "04";	
	/** 检修作业流程节点状态 - 已终止*/
	public static final String CONST_STR_STATUS_YZZ = "10";	
	
	/* idx主键 */
	@Id	
	private String idx;
	/* 作业主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
	/* 流程节点主键 */
	@Column(name="WP_Node_IDX")
	private String wpNodeIDX;
	/* 上级作业节点 */
	@Column(name="Parent_WP_Node_IDX")
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
	@Column(name="Is_Leaf")
	private Integer isLeaf;
	/* 计划开始时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Plan_StartTime")
	private java.util.Date planStartTime;
	/* 计划结束时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Plan_EndTime")
	private java.util.Date planEndTime;
	/* 实际开始时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Real_StartTime")
	private java.util.Date realStartTime;
	/* 实际结束时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Real_EndTime")
	private java.util.Date realEndTime;
	/* 状态 */
	private String status;
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
    
    /* 层级（从1开始） */
    @Column(name = "nodeLevel")
    private Integer nodeLevel;
    /* 日历 */
    @Column(name="CALENDAR_IDX")    
    private String calendarIdx; 
    /* 处理班组ID */
    private Long handleOrgID;
    /* 处理班组名称 */
    private String handleOrgName; 
    /* 流程显示样式 */
    @Column(name="SHOW_FLAG")    
    private String showFlag; 
    
    /* 前置流程节点主键 */
    @Column(name="PRE_NODE_IDX")
    private String preNodeIDX;
    
    /* 配件型号主键 */
    @Column(name = "Parts_Type_IDX")
    private String partsTypeIDX;
    
    /* 配件信息主键 */
    @Column(name = "PARTS_ACCOUNT_IDX")
    private String partsAccountIDX;
    
    /* 规格型号 */
    @Column(name = "SPECIFICATION_MODEL")
    private String specificationModel;
    
    /* 配件名称 */
    @Column(name = "PARTS_NAME")
    private String partsName;
    
    /* 配件识别码 */
    @Column(name = "IDENTIFICATION_CODE")
    private String identificationCode;
    /* 配件编号 */
    @Column(name = "PARTS_NO")
    private String partsNo;
    
	/**
	 * @return String 获取作业主键
	 */
	public String getRdpIDX(){
		return rdpIDX;
	}
	/**
	 * @param rdpIDX 设置作业主键
	 */
	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
	}
	/**
	 * @return String 获取流程节点主键
	 */
	public String getWpNodeIDX(){
		return wpNodeIDX;
	}
	/**
	 * @param wpNodeIDX 设置流程节点主键
	 */
	public void setWpNodeIDX(String wpNodeIDX) {
		this.wpNodeIDX = wpNodeIDX;
	}
	/**
	 * @return String 获取上级作业节点
	 */
	public String getParentWPNodeIDX(){
		return parentWPNodeIDX;
	}
	/**
	 * @param parentWPNodeIDX 设置上级作业节点
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
	 * @param wpNodeName 设置节点名称
	 */
	public void setWpNodeName(String wpNodeName) {
		this.wpNodeName = wpNodeName;
	}
	/**
	 * @return String 获取节点描述
	 */
	public String getWpNodeDesc(){
		return wpNodeDesc;
	}
	/**
	 * @param wpNodeDesc 设置节点描述
	 */
	public void setWpNodeDesc(String wpNodeDesc) {
		this.wpNodeDesc = wpNodeDesc;
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
	 * @return java.util.Date 获取计划开始时间
	 */
	public java.util.Date getPlanStartTime(){
		return planStartTime;
	}
	/**
	 * @param planStartTime 设置计划开始时间
	 */
	public void setPlanStartTime(java.util.Date planStartTime) {
		this.planStartTime = planStartTime;
	}
	/**
	 * @return java.util.Date 获取计划结束时间
	 */
	public java.util.Date getPlanEndTime(){
		return planEndTime;
	}
	/**
	 * @param planEndTime 设置计划结束时间
	 */
	public void setPlanEndTime(java.util.Date planEndTime) {
		this.planEndTime = planEndTime;
	}
	/**
	 * @return java.util.Date 获取实际开始时间
	 */
	public java.util.Date getRealStartTime(){
		return realStartTime;
	}
	/**
	 * @param realStartTime 设置实际开始时间
	 */
	public void setRealStartTime(java.util.Date realStartTime) {
		this.realStartTime = realStartTime;
	}
	/**
	 * @return java.util.Date 获取实际结束时间
	 */
	public java.util.Date getRealEndTime(){
		return realEndTime;
	}
	/**
	 * @param realEndTime 设置实际结束时间
	 */
	public void setRealEndTime(java.util.Date realEndTime) {
		this.realEndTime = realEndTime;
	}
	/**
	 * @return String 获取状态
	 */
	public String getStatus(){
		return status;
	}
	/**
	 * @param status 设置状态
	 */
	public void setStatus(String status) {
		this.status = status;
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
    
    public Integer getNodeLevel() {
        return nodeLevel;
    }
    
    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
    }
    
    public String getCalendarIdx() {
        return calendarIdx;
    }
    
    public void setCalendarIdx(String calendarIdx) {
        this.calendarIdx = calendarIdx;
    }
	public Long getHandleOrgID() {
		return handleOrgID;
	}
	public void setHandleOrgID(Long handleOrgID) {
		this.handleOrgID = handleOrgID;
	}
	public String getHandleOrgName() {
		return handleOrgName;
	}
	public void setHandleOrgName(String handleOrgName) {
		this.handleOrgName = handleOrgName;
	}
    
    public String getPreNodeIDX() {
        return preNodeIDX;
    }
    
    public void setPreNodeIDX(String preNodeIDX) {
        this.preNodeIDX = preNodeIDX;
    }
    
    public String getShowFlag() {
        return showFlag;
    }
    
    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }
    
    public String getIdentificationCode() {
        return identificationCode;
    }
    
    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }
    
    public String getPartsAccountIDX() {
        return partsAccountIDX;
    }
    
    public void setPartsAccountIDX(String partsAccountIDX) {
        this.partsAccountIDX = partsAccountIDX;
    }
    
    public String getPartsName() {
        return partsName;
    }
    
    public void setPartsName(String partsName) {
        this.partsName = partsName;
    }
    
    public String getPartsNo() {
        return partsNo;
    }
    
    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }
    
    public String getPartsTypeIDX() {
        return partsTypeIDX;
    }
    
    public void setPartsTypeIDX(String partsTypeIDX) {
        this.partsTypeIDX = partsTypeIDX;
    }
    
    public String getSpecificationModel() {
        return specificationModel;
    }
    
    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }

}