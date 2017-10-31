package com.yunda.webservice.device.entity;

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
 * <li>说明：WorkOrder实体类, 数据表：设备接口-工单维护
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-01-06
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="SBJK_Work_Order")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkOrder implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 计划名称 */
	private String code;
	/* 计划开始日期 */
	private String name;
	/* 作业对象编码 */
	private String materielCode;
	/* 返回结果 */
	private String result;
	/* 计划结束日期 */
	private String state;
	/* 工单描述 */
	private String workOrderDescribe;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="Record_Status")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Time",updatable=false)
	private java.util.Date createTime;
	/* 修改人 */
	private Long updator;
	/* 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Update_Time")
	private java.util.Date updateTime;

	/**
	 * @return String 获取工单编码
	 */
	public String getCode(){
		return code;
	}
	/**
	 * @param 设置工单编码
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return String 获取工单名称
	 */
	public String getName(){
		return name;
	}
	/**
	 * @param 设置工单名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return String 获取作业对象编码
	 */
	public String getMaterielCode(){
		return materielCode;
	}
	/**
	 * @param 设置作业对象编码
	 */
	public void setMaterielCode(String materielCode) {
		this.materielCode = materielCode;
	}
	/**
	 * @return String 获取返回结果
	 */
	public String getResult(){
		return result;
	}
	/**
	 * @param 设置返回结果
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return String 获取设备自检状态
	 */
	public String getState(){
		return state;
	}
	/**
	 * @param 设置设备自检状态
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return String 获取工单描述
	 */
	public String getWorkOrderDescribe(){
		return workOrderDescribe;
	}
	/**
	 * @param 设置工单描述
	 */
	public void setWorkOrderDescribe(String workOrderDescribe) {
		this.workOrderDescribe = workOrderDescribe;
	}
	/**
	 * @return Integer 获取记录状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param 设置记录状态
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
	 * @param 设置站点标识
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
	 * @param 设置创建人
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
	 * @return Long 获取修改人
	 */
	public Long getUpdator(){
		return updator;
	}
	/**
	 * @param 设置修改人
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