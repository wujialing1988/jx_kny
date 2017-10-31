package com.yunda.jx.jxgc.repairrequirement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：DetectItem实体类, 数据表：检测项
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
@Table(name="JXGC_Detect_Item")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class DetectItem implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    /* 是否必填 0必填 */
    public static final Integer ISNOTBLANK_YES = 0;
    /* 是否必填 1非必填 */
    public static final Integer ISNOTBLANK_NO = 1;
    
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 工步主键 */
	@Column(name="Work_Step_IDX")
	private String workStepIDX;
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
	/* 字典项编码 */
	@Column(name="Dict_Item_Code")
	private String dictItemCode;
	/* 业务状态：10新增；20启用；30作废 */
	private Integer status;
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
    @Column(name="sort_seq")
    private Integer sortSeq;
    /* 最小范围值 */
    private Double minResult;
    /* 最大范围值 */
    private Double maxResult;
	
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
    /**
	 * @return String 获取工步主键
	 */
	public String getWorkStepIDX(){
		return workStepIDX;
	}
	/**
	 * @param workStepIDX 设置工步主键
	 */
	public void setWorkStepIDX(String workStepIDX) {
		this.workStepIDX = workStepIDX;
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
	 * @return String 获取字典项编码
	 */
	public String getDictItemCode(){
		return dictItemCode;
	}
	/**
	 * @param dictItemCode 设置字典项编码
	 */
	public void setDictItemCode(String dictItemCode) {
		this.dictItemCode = dictItemCode;
	}
	/**
	 * @return Integer 获取业务状态
	 */
	public Integer getStatus(){
		return status;
	}
	/**
	 * @param status 设置业务状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
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
    
}