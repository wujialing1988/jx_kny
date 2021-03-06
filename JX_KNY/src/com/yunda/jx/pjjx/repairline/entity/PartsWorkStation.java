package com.yunda.jx.pjjx.repairline.entity;

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
 * <li>说明：PartsWorkStation实体类, 数据表：配件检修工位
 * <li>创建人：程梅
 * <li>创建日期：2015-10-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJJX_PARTS_WORK_STATION")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsWorkStation implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 工位编码 */
	@Column(name="Work_Station_Code")
	private String workStationCode;
	/* 工位名称 */
	@Column(name="Work_Station_Name")
	private String workStationName;
	/* 流水线主键 */
	@Column(name="REPAIR_LINE_IDX")
	private String repairLineIdx;
	/* 流水线名称 */
	@Column(name="Repair_Line_Name")
	private String repairLineName;
	/* 备注 */
	private String remarks;
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

	/**
	 * @return String 获取工位编码
	 */
	public String getWorkStationCode(){
		return workStationCode;
	}
	/**
	 * @param workStationCode 设置工位编码
	 */
	public void setWorkStationCode(String workStationCode) {
		this.workStationCode = workStationCode;
	}
	/**
	 * @return String 获取工位名称
	 */
	public String getWorkStationName(){
		return workStationName;
	}
	/**
	 * @param workStationName 设置工位名称
	 */
	public void setWorkStationName(String workStationName) {
		this.workStationName = workStationName;
	}
	/**
	 * @return String 获取流水线主键
	 */
	public String getRepairLineIdx(){
		return repairLineIdx;
	}
	/**
	 * @param repairLineIdx 设置流水线主键
	 */
	public void setRepairLineIdx(String repairLineIdx) {
		this.repairLineIdx = repairLineIdx;
	}
	/**
	 * @return String 获取流水线名称
	 */
	public String getRepairLineName(){
		return repairLineName;
	}
	/**
	 * @param repairLineName 设置流水线名称
	 */
	public void setRepairLineName(String repairLineName) {
		this.repairLineName = repairLineName;
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
}