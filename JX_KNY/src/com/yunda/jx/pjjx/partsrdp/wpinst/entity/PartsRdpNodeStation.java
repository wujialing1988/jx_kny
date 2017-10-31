package com.yunda.jx.pjjx.partsrdp.wpinst.entity;

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
 * <li>说明：
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-22
 * <li>成都运达科技股份有限公司
 */
@Entity
@Table(name="PJJX_PARTS_RDP_NODE_STATION")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpNodeStation implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	/* 主键 */
	@Column(name="IDX")
	private String idx;
	/* 流程节点主键 */
	@Column(name="RDP_NODE_IDX")
	private String rdpNodeIdx;
	/* 工位主键 */
	@Column(name="WORK_STATION_IDX")
	private String workStationIdx;
	/* 工位编码 */
	@Column(name="WORK_STATION_CODE")
	private String workStationCode;
	/* 工位名称 */
	@Column(name="WORK_STATION_NAME")
	private String workStationName;
	/* 流水线主键 */
	@Column(name="REPAIR_LINE_IDX")
	private String repairLineIdx;
	/* 流水线名称 */
	@Column(name="REPAIR_LINE_NAME")
	private String repairLineName;
	/* 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name="RECORD_STATUS")
	private Integer recordStatus;
	/* 站点标识，为了同步数据而使用 */
	@Column(name="SITEID")
	private String siteid;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Column(name="CREATE_TIME", updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date createTime;
	/* 修改人 */
	@Column(name="UPDATOR")
	private Long updator;
	/* 修改时间 */
	@Column(name="UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date updateTime;
	/*
	 * getter setter
	 */
	/**
	 * @return 获取主键
	 */
	public String getIdx(){
		return this.idx;
	}
	/**
	 * @param idx 设置主键
	 */
	public void setIdx(String idx){
		this.idx = idx;
	}
	/**
	 * @return 获取流程节点主键
	 */
	public String getRdpNodeIdx(){
		return this.rdpNodeIdx;
	}
	/**
	 * @param rdpNodeIdx 设置流程节点主键
	 */
	public void setRdpNodeIdx(String rdpNodeIdx){
		this.rdpNodeIdx = rdpNodeIdx;
	}
	/**
	 * @return 获取工位主键
	 */
	public String getWorkStationIdx(){
		return this.workStationIdx;
	}
	/**
	 * @param workStationIdx 设置工位主键
	 */
	public void setWorkStationIdx(String workStationIdx){
		this.workStationIdx = workStationIdx;
	}
	/**
	 * @return 获取工位编码
	 */
	public String getWorkStationCode(){
		return this.workStationCode;
	}
	/**
	 * @param workStationCode 设置工位编码
	 */
	public void setWorkStationCode(String workStationCode){
		this.workStationCode = workStationCode;
	}
	/**
	 * @return 获取工位名称
	 */
	public String getWorkStationName(){
		return this.workStationName;
	}
	/**
	 * @param workStationName 设置工位名称
	 */
	public void setWorkStationName(String workStationName){
		this.workStationName = workStationName;
	}
	/**
	 * @return 获取流水线主键
	 */
	public String getRepairLineIdx(){
		return this.repairLineIdx;
	}
	/**
	 * @param repairLineIdx 设置流水线主键
	 */
	public void setRepairLineIdx(String repairLineIdx){
		this.repairLineIdx = repairLineIdx;
	}
	/**
	 * @return 获取流水线名称
	 */
	public String getRepairLineName(){
		return this.repairLineName;
	}
	/**
	 * @param repairLineName 设置流水线名称
	 */
	public void setRepairLineName(String repairLineName){
		this.repairLineName = repairLineName;
	}
	/**
	 * @return 获取表示此条记录的状态：0为表示未删除；1表示删除
	 */
	public Integer getRecordStatus(){
		return this.recordStatus;
	}
	/**
	 * @param recordStatus 设置表示此条记录的状态：0为表示未删除；1表示删除
	 */
	public void setRecordStatus(Integer recordStatus){
		this.recordStatus = recordStatus;
	}
	/**
	 * @return 获取站点标识，为了同步数据而使用
	 */
	public String getSiteid(){
		return this.siteid;
	}
	/**
	 * @param siteid 设置站点标识，为了同步数据而使用
	 */
	public void setSiteid(String siteid){
		this.siteid = siteid;
	}
	/**
	 * @return 获取创建人
	 */
	public Long getCreator(){
		return this.creator;
	}
	/**
	 * @param creator 设置创建人
	 */
	public void setCreator(Long creator){
		this.creator = creator;
	}
	/**
	 * @return 获取创建时间
	 */
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	/**
	 * @param createTime 设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 * @return 获取修改人
	 */
	public Long getUpdator(){
		return this.updator;
	}
	/**
	 * @param updator 设置修改人
	 */
	public void setUpdator(Long updator){
		this.updator = updator;
	}
	/**
	 * @return 获取修改时间
	 */
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 * @param updateTime 设置修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
}
