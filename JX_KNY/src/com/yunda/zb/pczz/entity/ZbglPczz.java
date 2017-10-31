package com.yunda.zb.pczz.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import javax.persistence.*;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglPczz实体类, 数据表：普查整治计划
 * <li>创建人：王利成
 * <li>创建日期：2015-03-04
 * <li>修改人: 林欢
 * <li>修改日期：2016-08-18
 * <li>修改内容：普查整治功能重做
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Entity
@Table(name="ZB_ZBGL_PCZZ")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class ZbglPczz implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    
    /**  任务状态 - 待发布 */
    public static final String STATUS_TORELEASE = "TORELEASE";
    public static final String STATUS_TORELEASE_CH = "待发布";
   
    /**  任务状态 - 发布 */
    public static final String STATUS_RELEASED = "RELEASED";
    public static final String STATUS_RELEASED_CH = "发布";
    
    /**  任务状态 - 归档 */
    public static final String STATUS_COMPLETE = "COMPLETE";
    public static final String STATUS_COMPLETE_CH = "归档";

    /** 业务状态：待接活 */
    public static final String STATUS_DRAFT = "TODO";
    
    public static final String STATUS_DRAFT_CH = "待接活";
    
    /** 业务状态：待销活 */
    public static final String STATUS_OPEN = "ONGOING";
    
    public static final String STATUS_OPEN_CH = "待销活";
    
    /** 作业情况:已完毕 */
    public static final Integer WORK_STATUS_END = 1;
   
    /** 作业情况:作业中 */
    public static final Integer WORK_STATUS_ONGOING = 0;
    
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 任务单号 */
	@Column(name="PCZZ_No")
	private String pczzNo;
	/* 任务要求 */
	@Column(name="PCZZ_Req")
	private String pczzReq;
	/* 发布单位编码 */
	@Column(name="Release_JGDM")
	private String releaseJGDM;
	/* 发布单位名称 */
	@Column(name="Release_JGMC")
	private String releaseJGMC;
	/* 开始日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Start_Date")
	private java.util.Date startDate;
	/* 结束日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="End_Date")
	private java.util.Date endDate;
	/* 任务状态，TORELEASE：待发布；RELEASED：发布；COMPLETE：归档； */
	private String status;
	/* 任务类型，PC：普查；ZZ：整治 */
	@Column(name="PCZZ_Class")
	private String pczzClass;
	/* 记录状态 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 更新时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;
    
    /* 普查计划名称 */
    @Column(name="pczz_name")
    private String pczzName;
    /* 作业情况 */
    @Column(name="work_status")
    private Integer workStatus;
    /* 发布人idx */
    @Column(name="Release_Person_IDX")
    private Long releasePersonIDX;
    /* 发布人名称 */
    @Column(name="Release_Person_Name")
    private String releasePersonName;
    /* 发布时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="Release_date")
    private Date releaseDate;
    
    /* 普查计划发布查询开始时间 */
    @Transient
    private Date releaseStartDate;
    
    /* 普查计划发布查询结束时间 */
    @Transient
    private Date releaseEndDate;
    
    /* 作业情况 */
    @Transient
    private String workStatusString;
    
    public String getWorkStatusString() {
        return workStatusString;
    }

    
    public void setWorkStatusString(String workStatusString) {
        this.workStatusString = workStatusString;
    }

    public Date getReleaseEndDate() {
        return releaseEndDate;
    }

    public void setReleaseEndDate(Date releaseEndDate) {
        this.releaseEndDate = releaseEndDate;
    }

    public Date getReleaseStartDate() {
        return releaseStartDate;
    }

    
    public void setReleaseStartDate(Date releaseStartDate) {
        this.releaseStartDate = releaseStartDate;
    }

    public String getPczzName() {
        return pczzName;
    }
    
    public void setPczzName(String pczzName) {
        this.pczzName = pczzName;
    }
    
    public Date getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public Long getReleasePersonIDX() {
        return releasePersonIDX;
    }
    
    public void setReleasePersonIDX(Long releasePersonIDX) {
        this.releasePersonIDX = releasePersonIDX;
    }
    
    public String getReleasePersonName() {
        return releasePersonName;
    }
    
    public void setReleasePersonName(String releasePersonName) {
        this.releasePersonName = releasePersonName;
    }
    
    public Integer getWorkStatus() {
        return workStatus;
    }
    
    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }
    public void setPczzNo(String pczzNo) {
		this.pczzNo = pczzNo;
	}
    /**
     * @return true 获取任务单号
     */
    public String getPczzNo(){
        return pczzNo;
    }
	/**
	 * @return String 获取任务要求
	 */
	public String getPczzReq(){
		return pczzReq;
	}
	public void setPczzReq(String pczzReq) {
		this.pczzReq = pczzReq;
	}
	/**
	 * @return String 获取发布单位编码
	 */
	public String getReleaseJGDM(){
		return releaseJGDM;
	}
	public void setReleaseJGDM(String releaseJGDM) {
		this.releaseJGDM = releaseJGDM;
	}
	/**
	 * @return String 获取发布单位名称
	 */
	public String getReleaseJGMC(){
		return releaseJGMC;
	}
	public void setReleaseJGMC(String releaseJGMC) {
		this.releaseJGMC = releaseJGMC;
	}
	/**
	 * @return java.util.Date 获取开始日期
	 */
	public java.util.Date getStartDate(){
		return startDate;
	}
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return java.util.Date 获取结束日期
	 */
	public java.util.Date getEndDate(){
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return String 获取任务状态
	 */
	public String getStatus(){
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return String 获取任务类型
	 */
	public String getPczzClass(){
		return pczzClass;
	}
	public void setPczzClass(String pczzClass) {
		this.pczzClass = pczzClass;
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
	 * @return java.util.Date 获取更新时间
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
}