package com.yunda.baseapp.workcalendar.entity;

import javax.persistence.*;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkCalendarDetail实体类, 数据表：工作日历明细表
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-06-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JCGY_WORK_CALENDAR_DETAIL")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkCalendarDetail implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 工作日历主键表Idx */
	private String infoIdx;
	/* 日期 */
	private String calDate;
	/* 是否工作日 0 非默认工作时间 1 非工作日 2 默认工作时间 */
	private String calDateType;
	/* 开工时间一 */
	private String period1Begin;
	/* 完工时间一 */
	private String period1End;
	/* 开工时间二 */
	private String period2Begin;
	/* 完工时间二 */
	private String period2End;
	/* 开工时间三 */
	private String period3Begin;
	/* 完工时间三 */
	private String period3End;
	/* 开工时间四 */
	private String period4Begin;
	/* 完工时间四 */
	private String period4End;
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
	 * @return String 获取工作日历主键表Idx
	 */
	public String getInfoIdx(){
		return infoIdx;
	}
	/**
	 * @param 设置工作日历主键表Idx
	 */
	public void setInfoIdx(String infoIdx) {
		this.infoIdx = infoIdx;
	}
	/**
	 * @return String 获取日期
	 */
	public String getCalDate(){
		return calDate;
	}
	/**
	 * @param 设置日期
	 */
	public void setCalDate(String calDate) {
		this.calDate = calDate;
	}
	/**
	 * @return String 获取是否工作日
	 */
	public String getCalDateType(){
		return calDateType;
	}
	/**
	 * @param 设置是否工作日
	 */
	public void setCalDateType(String calDateType) {
		this.calDateType = calDateType;
	}
	/**
	 * @return String 获取开工时间一
	 */
	public String getPeriod1Begin(){
		return period1Begin;
	}
	/**
	 * @param 设置开工时间一
	 */
	public void setPeriod1Begin(String period1Begin) {
		this.period1Begin = period1Begin;
	}
	/**
	 * @return String 获取完工时间一
	 */
	public String getPeriod1End(){
		return period1End;
	}
	/**
	 * @param 设置完工时间一
	 */
	public void setPeriod1End(String period1End) {
		this.period1End = period1End;
	}
	/**
	 * @return String 获取开工时间二
	 */
	public String getPeriod2Begin(){
		return period2Begin;
	}
	/**
	 * @param 设置开工时间二
	 */
	public void setPeriod2Begin(String period2Begin) {
		this.period2Begin = period2Begin;
	}
	/**
	 * @return String 获取完工时间二
	 */
	public String getPeriod2End(){
		return period2End;
	}
	/**
	 * @param 设置完工时间二
	 */
	public void setPeriod2End(String period2End) {
		this.period2End = period2End;
	}
	/**
	 * @return String 获取开工时间三
	 */
	public String getPeriod3Begin(){
		return period3Begin;
	}
	/**
	 * @param 设置开工时间三
	 */
	public void setPeriod3Begin(String period3Begin) {
		this.period3Begin = period3Begin;
	}
	/**
	 * @return String 获取完工时间三
	 */
	public String getPeriod3End(){
		return period3End;
	}
	/**
	 * @param 设置完工时间三
	 */
	public void setPeriod3End(String period3End) {
		this.period3End = period3End;
	}
	/**
	 * @return String 获取开工时间四
	 */
	public String getPeriod4Begin(){
		return period4Begin;
	}
	/**
	 * @param 设置开工时间四
	 */
	public void setPeriod4Begin(String period4Begin) {
		this.period4Begin = period4Begin;
	}
	/**
	 * @return String 获取完工时间四
	 */
	public String getPeriod4End(){
		return period4End;
	}
	/**
	 * @param 设置完工时间四
	 */
	public void setPeriod4End(String period4End) {
		this.period4End = period4End;
	}
	/**
	 * @return String 获取备注
	 */
	public String getRemark(){
		return remark;
	}
	/**
	 * @param 设置备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return Integer 获取RECORD_STATUS
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param 设置RECORD_STATUS
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * @return String 获取站点
	 */
	public String getSiteId(){
		return siteId;
	}
	/**
	 * @param 设置站点
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return Long 获取创建者
	 */
	public Long getCreator(){
		return creator;
	}
	/**
	 * @param 设置创建者
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
	 * @param 设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return Long 获取修改者
	 */
	public Long getUpdator(){
		return updator;
	}
	/**
	 * @param 设置修改者
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
	 * @param 设置修改时间
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
	 * @param 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
}