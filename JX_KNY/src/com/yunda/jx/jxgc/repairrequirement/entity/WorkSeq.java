package com.yunda.jx.jxgc.repairrequirement.entity;

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
 * <li>说明：WorkSeq实体类, 数据表：工序卡
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-19
 * <li>修改人: 程锐
 * <li>修改日期：2013-5-1
 * <li>修改内容：增加质量记录单（天津基地）相关字段
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_Work_Seq")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkSeq implements java.io.Serializable{
    
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/** idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/** 工序卡编码 (编号（天津基地）)*/
	@Column(name="Work_Seq_Code")
	private String workSeqCode;
	/** 工序卡名称 （检修工序（天津基地））*/
	@Column(name="Work_Seq_Name")
	private String workSeqName;
//	/** 额定工时 */
//	@Column(name="Rated_WorkHours")
//	private Integer ratedWorkHours;
	/** 检修范围 */
	@Column(name="Repair_Scope")
	private String repairScope;	
	/** 安全注意事项 */
	@Column(name="Safe_Announcements")
	private String safeAnnouncements;
	/** 备注 */
	private String remark;
//	/** 组成型号主键 */
//	@Column(name="BuildUp_Type_IDX")
//	private String buildUpTypeIDX;
//	/** 组成型号编码 零部件型号(天津) 对应组成中的组成型号buildUpTypeName*/
//	@Column(name="BuildUp_Type_Code")
//	private String buildUpTypeCode;
//	/** 组成型号名称 零部件名称（天津）对应组成中的组成型号名称buildUpTypeDesc*/
//	@Column(name="BuildUp_Type_Name")
//	private String buildUpTypeName;
	/** 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/** 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/** 创建人 */
	@Column(updatable=false)
	private Long creator;
	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;
	/** 修改人 */
	private Long updator;
	/** 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;
    /** 检修车型主键 */
    @Column(name="P_Train_Type_IDX")
    private String pTrainTypeIDX;
    /** 检修车型简称 */
    @Column(name="P_Train_Type_ShortName")
    private String pTrainTypeShortName;
    
    /** 检修记录单idx */
    @Column(name="record_idx")
    private String recordIDX;
    
    /* 工位主键（JXGC_WORK_STATION） */
	@Transient
	@Column(name="Work_Station_IDX")
	private String workStationIDX;
	
	/* 工位对应工序卡实体主键（WorkStationSet的idx） */
	@Transient
	private String workStationSetIDX;
    /** 顺序号*/
    private Integer seq ;
    /* 检修项目主键 */
    @Transient
    private String repairProjectIdx;
    /* 检修项目对应作业工单主键 */
    @Transient
    private String rpToWsIdx;
    
    /* 被复制的workSeqIDX */
    @Transient
    private String sourceSeqIDX;
    
	/**
	 * @return String 获取工序卡编码
	 */
	public String getWorkSeqCode(){
		return workSeqCode;
	}
	    
	public void setWorkSeqCode(String workSeqCode) {
		this.workSeqCode = workSeqCode;
	}
	/**
	 * @return String 获取工序卡名称
	 */
	public String getWorkSeqName(){
		return workSeqName;
	}
	
	public void setWorkSeqName(String workSeqName) {
		this.workSeqName = workSeqName;
	}
//	/**
//	 * @return Integer 获取额定工时（分钟）
//	 */
//	public Integer getRatedWorkHours(){
//		return ratedWorkHours;
//	}
//	/**
//	 * @param 设置额定工时（分钟）
//	 */
//	public void setRatedWorkHours(Integer ratedWorkHours) {
//		this.ratedWorkHours = ratedWorkHours;
//	}
	/**
	 * @return String 获取检修范围
	 */
	public String getRepairScope(){
		return repairScope;
	}
	
	public void setRepairScope(String repairScope) {
		this.repairScope = repairScope;
	}	
	/**
	 * @return String 获取安全注意事项
	 */
	public String getSafeAnnouncements(){
		return safeAnnouncements;
	}
	
	public void setSafeAnnouncements(String safeAnnouncements) {
		this.safeAnnouncements = safeAnnouncements;
	}
	/**
	 * @return String 获取备注
	 */
	public String getRemark(){
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
//	/**
//	 * @return String 获取组成型号主键
//	 */
//	public String getBuildUpTypeIDX(){
//		return buildUpTypeIDX;
//	}
//	/**
//	 * @param 设置组成型号主键
//	 */
//	public void setBuildUpTypeIDX(String buildUpTypeIDX) {
//		this.buildUpTypeIDX = buildUpTypeIDX;
//	}
//	/**
//	 * @return String 获取组成型号编码
//	 */
//	public String getBuildUpTypeCode(){
//		return buildUpTypeCode;
//	}
//	/**
//	 * @param 设置组成型号编码
//	 */
//	public void setBuildUpTypeCode(String buildUpTypeCode) {
//		this.buildUpTypeCode = buildUpTypeCode;
//	}
//	/**
//	 * @return String 获取组成型号名称
//	 */
//	public String getBuildUpTypeName(){
//		return buildUpTypeName;
//	}
//	/**
//	 * @param 设置组成型号名称
//	 */
//	public void setBuildUpTypeName(String buildUpTypeName) {
//		this.buildUpTypeName = buildUpTypeName;
//	}
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
	 * @return String 获取站点标识
	 */
	public String getSiteID(){
		return siteID;
	}
	
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	/**
	 * @return Long 获取创建人
	 */
	public Long getCreator(){
		return creator;
	}
	
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * @return java.util.Date 获取创建时间
	 */
	public java.util.Date getCreateTime(){
		return createTime;
	}
	
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return Long 获取修改人
	 */
	public Long getUpdator(){
		return updator;
	}
	
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	/**
	 * @return java.util.Date 获取修改时间
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
    
    public String getPTrainTypeIDX() {
        return pTrainTypeIDX;
    }
    
    public void setPTrainTypeIDX(String trainTypeIDX) {
        pTrainTypeIDX = trainTypeIDX;
    }
    
    public String getPTrainTypeShortName() {
        return pTrainTypeShortName;
    }
    
    public void setPTrainTypeShortName(String trainTypeShortName) {
        pTrainTypeShortName = trainTypeShortName;
    }
	public String getWorkStationIDX() {
		return workStationIDX;
	}
	public void setWorkStationIDX(String workStationIDX) {
		this.workStationIDX = workStationIDX;
	}
	public String getWorkStationSetIDX() {
		return workStationSetIDX;
	}
	public void setWorkStationSetIDX(String workStationSetIDX) {
		this.workStationSetIDX = workStationSetIDX;
	}
    
    public Integer getSeq() {
        return seq;
    }
    
    public void setSeq(Integer seq) {
        this.seq = seq;
    }
    
    public String getRepairProjectIdx() {
        return repairProjectIdx;
    }
    
    public void setRepairProjectIdx(String repairProjectIdx) {
        this.repairProjectIdx = repairProjectIdx;
    }
    
    public String getRpToWsIdx() {
        return rpToWsIdx;
    }
    
    public void setRpToWsIdx(String rpToWsIdx) {
        this.rpToWsIdx = rpToWsIdx;
    }
    
    public String getRecordIDX() {
        return recordIDX;
    }
    
    public void setRecordIDX(String recordIDX) {
        this.recordIDX = recordIDX;
    }
    
    public String getSourceSeqIDX() {
        return sourceSeqIDX;
    }
    
    public void setSourceSeqIDX(String sourceSeqIDX) {
        this.sourceSeqIDX = sourceSeqIDX;
    }
    
}