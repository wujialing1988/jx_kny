package com.yunda.zb.trainclean.entity;

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
 * <li>说明：ZbglCleaning实体类, 数据表：机车保洁记录
 * <li>创建人：程梅
 * <li>创建日期：2015-02-13
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_Cleaning")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglCleaning implements java.io.Serializable{
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
	/* 保洁人员ID */
	@Column(name="Duty_PersonId")
	private Long dutyPersonId;
	/* 保洁人名称 */
	@Column(name="Duty_PersonName")
	private String dutyPersonName;
	/* 保洁等级名称 */
	@Column(name="Cleaning_Level")
	private String cleaningLevel;
	/* 保洁机车等级名称 */
	@Column(name="Train_Level")
	private String trainLevel;
	/* 交接时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Cleaning_Time")
	private java.util.Date cleaningTime;
	/* 备注 */
	private String remarks;

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
	
    
    public String getTrainToGo() {
        return trainToGo;
    }

    
    public void setTrainToGo(String trainToGo) {
        this.trainToGo = trainToGo;
    }

    public String getOperatorid() {
        return operatorid;
    }
    
    public void setOperatorid(String operatorid) {
        this.operatorid = operatorid;
    }
    /**
     * 
     * <li>说明：构造方法
     * <li>创建人：程梅
     * <li>创建日期：2015-2-13
     * <li>修改人： 
     * <li>修改日期：
     */
    public ZbglCleaning(){
        super();
    }
    /**
     * 
     * <li>说明：查询保洁记录列表
     * <li>创建人：程梅
     * <li>创建日期：2015-2-13
     * <li>修改人： 
     * <li>修改日期：
     * @param idx 主键
     * @param trainTypeIDX 车型id
     * @param trainTypeShortName 车型简称
     * @param trainNo 车号
     * @param dutyPersonName 保洁人员
     * @param cleaningLevel 保洁等级
     * @param trainLevel 机车等级
     * @param cleaningTime 保洁时间
     * @param remarks 备注
     * @param dName 配属段
     * @param inTime 入段时间
     * @param siteName 站场
     * @param trainToGo 入段去向
     */
    public ZbglCleaning(String idx, String trainTypeIDX, String trainTypeShortName, String trainNo, String dutyPersonName, String cleaningLevel, 
        String trainLevel, java.util.Date cleaningTime, String remarks ,String dName, Date inTime, String siteName, String trainToGo){
        this.idx = idx;
        this.trainTypeIDX = trainTypeIDX;
        this.trainTypeShortName = trainTypeShortName;
        this.trainNo = trainNo;
        this.dutyPersonName = dutyPersonName;
        this.cleaningLevel = cleaningLevel;
        this.trainLevel = trainLevel ;
        this.cleaningTime = cleaningTime;
        this.remarks = remarks;
        this.dName = dName;
        this.inTime = inTime;
        this.siteName = siteName;
        this.trainToGo = trainToGo;
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
	 * @return Long 获取检查人员ID
	 */
	public Long getDutyPersonId(){
		return dutyPersonId;
	}
	/**
	 * @param dutyPersonId 设置检查人员ID
	 */
	public void setDutyPersonId(Long dutyPersonId) {
		this.dutyPersonId = dutyPersonId;
	}
	/**
	 * @return String 获取检查人名称
	 */
	public String getDutyPersonName(){
		return dutyPersonName;
	}
	/**
	 * @param dutyPersonName 设置检查人名称
	 */
	public void setDutyPersonName(String dutyPersonName) {
		this.dutyPersonName = dutyPersonName;
	}
	/**
	 * @return String 获取保洁等级
	 */
	public String getCleaningLevel(){
		return cleaningLevel;
	}
	/**
	 * @param cleaningLevel 设置保洁等级
	 */
	public void setCleaningLevel(String cleaningLevel) {
		this.cleaningLevel = cleaningLevel;
	}
	/**
	 * @return String 获取机车等级
	 */
	public String getTrainLevel(){
		return trainLevel;
	}
	/**
	 * @param trainLevel 设置机车等级
	 */
	public void setTrainLevel(String trainLevel) {
		this.trainLevel = trainLevel;
	}
	/**
	 * @return java.util.Date 获取保洁时间
	 */
	public java.util.Date getCleaningTime(){
		return cleaningTime;
	}
	/**
	 * @param cleaningTime 设置保洁时间
	 */
	public void setCleaningTime(java.util.Date cleaningTime) {
		this.cleaningTime = cleaningTime;
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
}