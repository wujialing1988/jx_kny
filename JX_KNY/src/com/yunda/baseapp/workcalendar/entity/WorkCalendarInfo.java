package com.yunda.baseapp.workcalendar.entity;

import javax.persistence.*;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkCalendarInfo实体类, 数据表：工作日历默认工作时间表
 * <li>创建人：谭诚
 * <li>创建日期：2013-06-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JCGY_WORK_CALENDAR_INFO")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkCalendarInfo implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
    
    /** 设为当前默认日历 - 否 */
	public static final String CONST_STR_IS_DEFAULT_NO = "0";
	/** 设为当前默认日历 - 是 */
    public static final String CONST_STR_IS_DEFAULT_YES = "1";
    
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 日历名称 */
	private String calendarName;
	/* 默认开工时间一 */
	private String period1Begin;
	/* 默认完工时间一 */
	private String period1End;
	/* 默认开工时间二 */
	private String period2Begin;
	/* 默认完工时间二 */
	private String period2End;
	/* 默认开工时间三 */
	private String period3Begin;
	/* 默认完工时间三 */
	private String period3End;
	/* 默认开工时间四 */
	private String period4Begin;
	/* 默认完工时间四 */
	private String period4End;
	/* 设为当前默认日历 */
	private String isDefault;
	/* 备注 */
	private String remark;
	/* RECORD_STATUS */
	@Column(name="RECORD_STATUS")
	private Integer recordStatus;
	/* 站点 */
	private String siteId;
	/* 创建者 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME",updatable=false)
	private java.util.Date createTime;
	/* 修改者 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private java.util.Date updateTime;
	/**
	 * <li>说明：
	 * <li>返回值： the calendarName
	 */
	public String getCalendarName() {
		return calendarName;
	}
	/**
	 * <li>说明：
	 * <li>参数： calendarName
	 */
	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the createTime
	 */
	public java.util.Date getCreateTime() {
		return createTime;
	}
	/**
	 * <li>说明：
	 * <li>参数： createTime
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the creator
	 */
	public Long getCreator() {
		return creator;
	}
	/**
	 * <li>说明：
	 * <li>参数： creator
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the idx
	 */
	public String getIdx() {
		return idx;
	}
	/**
	 * <li>说明：
	 * <li>参数： idx
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the isDefault
	 */
	public String getIsDefault() {
		return isDefault;
	}
	/**
	 * <li>说明：
	 * <li>参数： isDefault
	 */
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the period1Begin
	 */
	public String getPeriod1Begin() {
		return period1Begin;
	}
	/**
	 * <li>说明：
	 * <li>参数： period1Begin
	 */
	public void setPeriod1Begin(String period1Begin) {
		this.period1Begin = period1Begin;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the period1End
	 */
	public String getPeriod1End() {
		return period1End;
	}
	/**
	 * <li>说明：
	 * <li>参数： period1End
	 */
	public void setPeriod1End(String period1End) {
		this.period1End = period1End;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the period2Begin
	 */
	public String getPeriod2Begin() {
		return period2Begin;
	}
	/**
	 * <li>说明：
	 * <li>参数： period2Begin
	 */
	public void setPeriod2Begin(String period2Begin) {
		this.period2Begin = period2Begin;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the period2End
	 */
	public String getPeriod2End() {
		return period2End;
	}
	/**
	 * <li>说明：
	 * <li>参数： period2End
	 */
	public void setPeriod2End(String period2End) {
		this.period2End = period2End;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the period3Begin
	 */
	public String getPeriod3Begin() {
		return period3Begin;
	}
	/**
	 * <li>说明：
	 * <li>参数： period3Begin
	 */
	public void setPeriod3Begin(String period3Begin) {
		this.period3Begin = period3Begin;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the period3End
	 */
	public String getPeriod3End() {
		return period3End;
	}
	/**
	 * <li>说明：
	 * <li>参数： period3End
	 */
	public void setPeriod3End(String period3End) {
		this.period3End = period3End;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the period4Begin
	 */
	public String getPeriod4Begin() {
		return period4Begin;
	}
	/**
	 * <li>说明：
	 * <li>参数： period4Begin
	 */
	public void setPeriod4Begin(String period4Begin) {
		this.period4Begin = period4Begin;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the period4End
	 */
	public String getPeriod4End() {
		return period4End;
	}
	/**
	 * <li>说明：
	 * <li>参数： period4End
	 */
	public void setPeriod4End(String period4End) {
		this.period4End = period4End;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the recordStatus
	 */
	public Integer getRecordStatus() {
		return recordStatus;
	}
	/**
	 * <li>说明：
	 * <li>参数： recordStatus
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * <li>说明：
	 * <li>参数： remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the siteId
	 */
	public String getSiteId() {
		return siteId;
	}
	/**
	 * <li>说明：
	 * <li>参数： siteId
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the updateTime
	 */
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * <li>说明：
	 * <li>参数： updateTime
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the updator
	 */
	public Long getUpdator() {
		return updator;
	}
	/**
	 * <li>说明：
	 * <li>参数： updator
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
    
}