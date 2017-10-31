package com.yunda.zb.trainhandover.entity;

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
 * <li>说明：ZbglHoCase实体类, 数据表：机车交接单
 * <li>创建人：程梅
 * <li>创建日期：2015-02-07
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_HO_Case")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglHoCase implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 整备任务单主键 */
	@Column(name="RDP_IDX")
	private String rdpIdx;
	/* 车型主键 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	/* 车型简称 */
	@Column(name="Train_Type_ShortName")
	private String trainTypeShortName;
	/* 车号 */
	@Column(name="Train_No")
	private String trainNo;
	/* 交车人 */
	@Column(name="From_PersonId")
	private Long fromPersonId;
	/* 交车人名称 */
	@Column(name="From_PersonName")
	private String fromPersonName;
	/* 接车人 */
	@Column(name="To_PersonId")
	private Long toPersonId;
	/* 接车人名称 */
	@Column(name="To_PersonName")
	private String toPersonName;
	/* 交接时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="HandOver_Time")
	private java.util.Date handOverTime;
	/* 入段车次 */
	@Column(name="HandOver_Train_Order")
	private String handOverTrainOrder;
	/* 备注 */
	private String remarks;
	/* 记录状态，1：删除；0：未删除； */
	@Column(name="Record_Status")
	private Integer recordStatus;
    
    /* 到达时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ARRIVED_TIME")
    private java.util.Date arrivedTime;
    
    /* 配属段 */
    @Transient
    private String dName;
    /* 入段时间 */
    @Transient
    private Date inTime;
    /* 站场 */
    @Transient
    private String siteName;
    /* 当前操作人ID */
    @Transient
    private String operatorid;
    /* 入段去向 */
    @Transient
    private String trainToGo;
    /**
     * 
     * <li>说明：构造方法
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人： 
     * <li>修改日期：
     */
    public ZbglHoCase(){
        super();
    }
    /**
     * 
     * <li>说明：查询交接单列表
     * <li>创建人：程梅
     * <li>创建日期：2015-2-10
     * <li>修改人： 
     * <li>修改日期：
     * @param idx 主键
     * @param trainTypeIDX 车型id
     * @param trainTypeShortName 车型简称
     * @param trainNo 车号
     * @param fromPersonName 交车人
     * @param toPersonName 接车人
     * @param handOverTime 交接时间
     * @param handOverTrainOrder 入段车次
     * @param remarks 备注
     * @param dName 配属段
     * @param inTime 入段时间
     * @param siteName 站场
     */
    public ZbglHoCase(String idx, String trainTypeIDX, String trainTypeShortName, String trainNo, String fromPersonName, String toPersonName,
        Date handOverTime, String handOverTrainOrder, String remarks ,String dName, Date inTime, String siteName,String trainToGo,Date arrivedTime){
        this.idx = idx;
        this.trainTypeIDX = trainTypeIDX;
        this.trainTypeShortName = trainTypeShortName;
        this.trainNo = trainNo;
        this.fromPersonName = fromPersonName;
        this.toPersonName = toPersonName;
        this.handOverTime = handOverTime ;
        this.handOverTrainOrder = handOverTrainOrder;
        this.remarks = remarks;
        this.dName = dName;
        this.inTime = inTime;
        this.siteName = siteName;
        this.trainToGo = trainToGo;
        this.arrivedTime = arrivedTime ;
    }
    
    public String getTrainToGo() {
        return trainToGo;
    }
    
    public void setTrainToGo(String trainToGo) {
        this.trainToGo = trainToGo;
    }
    /**
	 * @return String 获取整备任务单主键
	 */
	public String getRdpIdx(){
		return rdpIdx;
	}
	/**
	 * @param rdpIdx 设置整备任务单主键
	 */
	public void setRdpIdx(String rdpIdx) {
		this.rdpIdx = rdpIdx;
	}
	/**
	 * @return String 获取车型主键
	 */
	public String getTrainTypeIDX(){
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
	public String getTrainTypeShortName(){
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
	public String getTrainNo(){
		return trainNo;
	}
	/**
	 * @param trainNo 设置车号
	 */
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	/**
	 * @return Long 获取交车人
	 */
	public Long getFromPersonId(){
		return fromPersonId;
	}
	/**
	 * @param fromPersonId 设置交车人
	 */
	public void setFromPersonId(Long fromPersonId) {
		this.fromPersonId = fromPersonId;
	}
	/**
	 * @return String 获取交车人名称
	 */
	public String getFromPersonName(){
		return fromPersonName;
	}
	/**
	 * @param fromPersonName 设置交车人名称
	 */
	public void setFromPersonName(String fromPersonName) {
		this.fromPersonName = fromPersonName;
	}
	/**
	 * @return Long 获取接车人
	 */
	public Long getToPersonId(){
		return toPersonId;
	}
	/**
	 * @param toPersonId 设置接车人
	 */
	public void setToPersonId(Long toPersonId) {
		this.toPersonId = toPersonId;
	}
	/**
	 * @return String 获取接车人名称
	 */
	public String getToPersonName(){
		return toPersonName;
	}
	/**
	 * @param toPersonName 设置接车人名称
	 */
	public void setToPersonName(String toPersonName) {
		this.toPersonName = toPersonName;
	}
	/**
	 * @return java.util.Date 获取交接时间
	 */
	public java.util.Date getHandOverTime(){
		return handOverTime;
	}
	/**
	 * @param handOverTime 设置交接时间
	 */
	public void setHandOverTime(java.util.Date handOverTime) {
		this.handOverTime = handOverTime;
	}
	/**
	 * @return String 获取入段车次
	 */
	public String getHandOverTrainOrder(){
		return handOverTrainOrder;
	}
	/**
	 * @param handOverTrainOrder 设置入段车次
	 */
	public void setHandOverTrainOrder(String handOverTrainOrder) {
		this.handOverTrainOrder = handOverTrainOrder;
	}
	/**
	 * @return String 获取备注
	 */
	public String getRemarks(){
		return remarks;
	}
	/**
	 * @param remarks 设置备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param recordStatus 设置记录状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
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
    
    public String getDName() {
        return dName;
    }
    
    public void setDName(String name) {
        dName = name;
    }
    
    public Date getInTime() {
        return inTime;
    }
    
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }
    
    public String getSiteName() {
        return siteName;
    }
    
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    public String getOperatorid() {
        return operatorid;
    }
    
    public void setOperatorid(String operatorid) {
        this.operatorid = operatorid;
    }
    
    public java.util.Date getArrivedTime() {
        return arrivedTime;
    }
    
    public void setArrivedTime(java.util.Date arrivedTime) {
        this.arrivedTime = arrivedTime;
    }
    
}