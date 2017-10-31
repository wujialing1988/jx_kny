package com.yunda.zb.tecorder.entity;

import java.util.Date;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglTecOrder实体类, 数据表：技术指令及措施
 * <li>创建人：王利成
 * <li>创建日期：2015-02-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_Tec_Order")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglTecOrder implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    
    /** 销号方式 - 单次销号 */
    public static final int COMPLETE_SINGAL = 0;
    public static final String COMPLETE_SINGAL_CH = "单次销号";

    /** 销号方式 - 多次销号 */
    public static final int COMPLETE_MANY = 1;
    public static final String COMPLETE_MANY_CH = "多次销号";

    /** 指令单状态 - 新增 */
    public static final String STATUS_NEW = "Add";
    public static final String STATUS_NEW_CH = "未发布";
   
    /** 指令单状态 - 发布 */
    public static final String STATUS_PUBLISH = "Release";
    public static final String STATUS_PUBLISH_CH = "已发布";

    /** 指令单状态 - 销号 */
    public static final String STATUS_CANCEL = "Complete";
    
    /** 指令单任务单名称 */
    public static final String ZBGL_TECORDER_WINAME = "技术指令及措施";
    
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 车型主键 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	/* 车型简称 */
	@Column(name="Train_Type_ShortName")
	private String trainTypeShortName;
	/* 车号 */
	@Column(name="Train_No")
	private String trainNo;
	/* 措施来源ID */
	@Column(name="Rel_IDX")
	private String relIDX;
	/* 措施类型，配置成数据字典 */
	@Column(name="Order_Class")
	private String orderClass;
	/* 指令内容 */
	@Column(name="Order_Content")
	private String orderContent;
	/* 指令处理次数 */
	@Column(name="Order_Handle_Times")
	private Integer orderHandleTimes;
	/* 销号方式，0：单次销号；1：多次销号 */
	@Column(name="Complete_Method")
	private Integer completeMethod;
	/* 发布人ID */
	@Column(name="Release_PersonID")
	private Long releasePersonID;
	/* 发布人名称 */
	@Column(name="Release_PersonName")
	private String releasePersonName;
    /* 发布时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Release_Time")
    private java.util.Date releaseTime;
	/* 销号时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Complete_Time")
	private java.util.Date completeTime;
	/* 销号人ID */
	@Column(name="Complete_PersonID")
	private Long completePersonID;
	/* 销号人名称 */
	@Column(name="Complete_PersonName")
	private String completePersonName;
	/* 措施状态，Add：新增；Release：发布；Complete：销号 */
	@Column(name="Order_Status")
	private String orderStatus;
	/* 站场 */
	@Column(updatable=false)
	private String siteID;
	/* 站场名称 */
	private String siteName;
	/* 记录状态，1：删除；0：未删除； */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 最新更新时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;
    
    
    /* 入段时间 */
    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    private Date inTime;
    
    /* 任务状态 */
    @Transient
    private String rdpStatus;
    
    /* 处理人员 */
    @Transient
    private String handlePersonName;
    
    /* 处理时间 */
    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    private Date handleTime;

    /**
     * <li>说明：默认构造方法
     * <li>创建人：王利成
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     */
    public ZbglTecOrder(){  
    }
    
    /**
     * <li>说明：机车技术指令处理详细信息查询
     * <li>创建人：王利成
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * @param inTime 入段时间
     * @param rdpStatus 任务状态
     * @param handlePersonName 处理人员
     * @param handleTime 处理时间
     */
    public ZbglTecOrder(Date inTime,String rdpStatus,String handlePersonName, Date handleTime) {
        super();
        this.inTime = inTime;
        this.rdpStatus = rdpStatus;
        this.handlePersonName = handlePersonName;
        this.handleTime = handleTime;
    }
    
    public String getHandlePersonName() {
        return handlePersonName;
    }
    
    public void setHandlePersonName(String handlePersonName) {
        this.handlePersonName = handlePersonName;
    }
    
    public Date getHandleTime() {
        return handleTime;
    }
    
    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }
    
    public Date getInTime() {
        return inTime;
    }
    
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }
    
    public String getRdpStatus() {
        return rdpStatus;
    }
    
    public void setRdpStatus(String rdpStatus) {
        this.rdpStatus = rdpStatus;
    }
    /**
	 * @return String 获取车型主键
	 */
	public String getTrainTypeIDX(){
		return trainTypeIDX;
	}

	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}
	/**
	 * @return String 获取车型简称
	 */
	public String getTrainTypeShortName(){
		return trainTypeShortName;
	}

	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	/**
	 * @return String 获取车号
	 */
	public String getTrainNo(){
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	/**
	 * @return String 获取措施来源ID
	 */
	public String getRelIDX(){
		return relIDX;
	}

	public void setRelIDX(String relIDX) {
		this.relIDX = relIDX;
	}
	/**
	 * @return String 获取措施类型
	 */
	public String getOrderClass(){
		return orderClass;
	}

	public void setOrderClass(String orderClass) {
		this.orderClass = orderClass;
	}
	/**
	 * @return String 获取指令内容
	 */
	public String getOrderContent(){
		return orderContent;
	}

	public void setOrderContent(String orderContent) {
		this.orderContent = orderContent;
	}
	/**
	 * @return Integer 获取指令处理次数
	 */
	public Integer getOrderHandleTimes(){
		return orderHandleTimes;
	}

	public void setOrderHandleTimes(Integer orderHandleTimes) {
		this.orderHandleTimes = orderHandleTimes;
	}
	/**
	 * @return Integer 获取销号方式
	 */
	public Integer getCompleteMethod(){
		return completeMethod;
	}

	public void setCompleteMethod(Integer completeMethod) {
		this.completeMethod = completeMethod;
	}
	/**
	 * @return Long 获取发布人ID
	 */
	public Long getReleasePersonID(){
		return releasePersonID;
	}

	public void setReleasePersonID(Long releasePersonID) {
		this.releasePersonID = releasePersonID;
	}
	/**
	 * @return String 获取发布人名称
	 */
	public String getReleasePersonName(){
		return releasePersonName;
	}

	public void setReleasePersonName(String releasePersonName) {
		this.releasePersonName = releasePersonName;
	}
	/**
	 * @return java.util.Date 获取销号时间
	 */
	public java.util.Date getCompleteTime(){
		return completeTime;
	}

	public void setCompleteTime(java.util.Date completeTime) {
		this.completeTime = completeTime;
	}
	/**
	 * @return Long 获取销号人ID
	 */
	public Long getCompletePersonID(){
		return completePersonID;
	}

	public void setCompletePersonID(Long completePersonID) {
		this.completePersonID = completePersonID;
	}
	/**
	 * @return String 获取销号人名称
	 */
	public String getCompletePersonName(){
		return completePersonName;
	}

	public void setCompletePersonName(String completePersonName) {
		this.completePersonName = completePersonName;
	}
	/**
	 * @return String 获取措施状态
	 */
	public String getOrderStatus(){
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	/**
	 * @return String 获取站场
	 */
	public String getSiteID(){
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	/**
	 * @return String 获取站场名称
	 */
	public String getSiteName(){
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * @return java.util.Date 获取最新更新时间
	 */
	public java.util.Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}
    
    public java.util.Date getReleaseTime() {
        return releaseTime;
    }
    
    public void setReleaseTime(java.util.Date releaseTime) {
        this.releaseTime = releaseTime;
    }
    
}