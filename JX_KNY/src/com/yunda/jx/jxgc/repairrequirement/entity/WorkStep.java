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
 * <li>说明：WorkStep实体类, 数据表：工步
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_Work_Step")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkStep implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/** idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/** 工序卡主键 */
	@Column(name="Work_Seq_IDX")
	private String workSeqIDX;
	/** 工步编码 */
	@Column(name="Work_Step_Code")
	private String workStepCode;
	/** 工步名称 （检测/检修项目（天津基地）） */
	@Column(name="Work_Step_Name")
	private String workStepName;
	/** 检修内容 */
	@Column(name="Repair_Content")
	private String repairContent;
	/** 检修标准 （技术要求或标准规定(天津基地)）*/
	@Column(name="Repair_Standard")
	private String repairStandard;
	/** 备注 */
	private String remark;
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
    /** 作业顺序 */
	@Column(name="WORK_STEP_SEQ")
    private Integer workStepSeq;
    
    /* 被复制的workStepIDX */
    @Transient
    private String sourceStepIDX;
    
	/**
	 * @return String 获取工序卡主键
	 */
	public String getWorkSeqIDX(){
		return workSeqIDX;
	}
	
	public void setWorkSeqIDX(String workSeqIDX) {
		this.workSeqIDX = workSeqIDX;
	}
	/**
	 * @return String 获取工步编码
	 */
	public String getWorkStepCode(){
		return workStepCode;
	}
	
	public void setWorkStepCode(String workStepCode) {
		this.workStepCode = workStepCode;
	}
	/**
	 * @return String 获取工步名称
	 */
	public String getWorkStepName(){
		return workStepName;
	}
	
	public void setWorkStepName(String workStepName) {
		this.workStepName = workStepName;
	}
	/**
	 * @return String 获取检修内容
	 */
	public String getRepairContent(){
		return repairContent;
	}
	
	public void setRepairContent(String repairContent) {
		this.repairContent = repairContent;
	}
	/**
	 * @return String 获取检修标准
	 */
	public String getRepairStandard(){
		return repairStandard;
	}
	
	public void setRepairStandard(String repairStandard) {
		this.repairStandard = repairStandard;
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
    
    public Integer getWorkStepSeq() {
        return workStepSeq;
    }
    
    public void setWorkStepSeq(Integer workStepSeq) {
        this.workStepSeq = workStepSeq;
    }
    
    public String getSourceStepIDX() {
        return sourceStepIDX;
    }
    
    public void setSourceStepIDX(String sourceStepIDX) {
        this.sourceStepIDX = sourceStepIDX;
    }
    
}