package com.yunda.jx.jxgc.workhis.workcard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：视图JXGC_DETECT_RESULT_HIS实体类, 数据表：检测结果历史
 * <li>创建人：程梅
 * <li>创建日期：2015年8月17日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_DETECT_RESULT_HIS")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class DetectResultHis implements java.io.Serializable{
	/**  类型：long  */
    private static final long serialVersionUID = 1L;
    /* idx主键 */
	@Id
	private String idx;
	/* 作业任务主键 */
	@Column(name="Work_Task_IDX")
	private String workTaskIDX;
	/* 检测项编码 */
	@Column(name="Detect_Item_Code")
	private String detectItemCode;
	/* 检测结果数据类型:数据字典获取数据（数据字段均采用英文） */
	@Column(name="Detect_Result_type")
	private String detectResulttype;
	/* 检测项标准 */
	@Column(name="Detect_Item_Standard")
	private String detectItemStandard;
	/* 检测内容 */
	@Column(name="Detect_Item_Content")
	private String detectItemContent;
	/* 检测结果 */
	@Column(name="Detect_Result")
	private String detectResult;
//	/* 字典项编码 */
//	@Column(name="Dict_Item_Code")
//	private String dictItemCode;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="RECORD_STATUS")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private java.util.Date updateTime;
    
    /* 是否必填 0必填1非必填 */
    private Integer isNotBlank;
    
    /* 最小范围 */
    private Double minResult;
    /* 最大范围 */
    private Double maxResult;

    @Column(name="sort_seq")
    private Integer sortSeq;
	/**
	 * @return String 获取作业任务主键
	 */
	public String getWorkTaskIDX(){
		return workTaskIDX;
	}
	/**
	 * @param workTaskIDX 设置作业任务主键
	 */
	public void setWorkTaskIDX(String workTaskIDX) {
		this.workTaskIDX = workTaskIDX;
	}
	/**
	 * @return String 获取检测项编码
	 */
	public String getDetectItemCode(){
		return detectItemCode;
	}
	/**
	 * @param detectItemCode 设置检测项编码
	 */
	public void setDetectItemCode(String detectItemCode) {
		this.detectItemCode = detectItemCode;
	}
	/**
	 * @return String 获取检测结果数据类型
	 */
	public String getDetectResulttype(){
		return detectResulttype;
	}
	/**
	 * @param detectResulttype 设置检测结果数据类型
	 */
	public void setDetectResulttype(String detectResulttype) {
		this.detectResulttype = detectResulttype;
	}
	/**
	 * @return String 获取检测项标准
	 */
	public String getDetectItemStandard(){
		return detectItemStandard;
	}
	/**
	 * @param detectItemStandard 设置检测项标准
	 */
	public void setDetectItemStandard(String detectItemStandard) {
		this.detectItemStandard = detectItemStandard;
	}
	/**
	 * @return String 获取检测内容
	 */
	public String getDetectItemContent(){
		return detectItemContent;
	}
	/**
	 * @param detectItemContent 设置检测内容
	 */
	public void setDetectItemContent(String detectItemContent) {
		this.detectItemContent = detectItemContent;
	}
	/**
	 * @return String 获取检测结果
	 */
	public String getDetectResult(){
		return detectResult;
	}
	/**
	 * @param detectResult 设置检测结果
	 */
	public void setDetectResult(String detectResult) {
		this.detectResult = detectResult;
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
    
    public Integer getIsNotBlank() {
        return isNotBlank;
    }
    
    public void setIsNotBlank(Integer isNotBlank) {
        this.isNotBlank = isNotBlank;
    }
    
    public Integer getSortSeq() {
        return sortSeq;
    }
    
    public void setSortSeq(Integer sortSeq) {
        this.sortSeq = sortSeq;
    }
    
    public Double getMaxResult() {
        return maxResult;
    }
    
    public void setMaxResult(Double maxResult) {
        this.maxResult = maxResult;
    }
    
    public Double getMinResult() {
        return minResult;
    }
    
    public void setMinResult(Double minResult) {
        this.minResult = minResult;
    }
    
}