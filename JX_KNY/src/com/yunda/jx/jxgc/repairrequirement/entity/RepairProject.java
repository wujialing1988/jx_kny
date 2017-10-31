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
 * <li>说明：RepairProject实体类, 数据表：检修项目
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
@Table(name="JXGC_Repair_Project")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class RepairProject implements java.io.Serializable{
	/**  类型：long  */
    private static final long serialVersionUID = 1L;
    /* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 检修项目编码 */
	@Column(name="Repair_Project_Code")
	private String repairProjectCode;
	/* 检修项目名称 */
	@Column(name="Repair_Project_Name")
	private String repairProjectName;
	/* 备注 */
	private String remark;
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

	/* 检修车型id */
	@Column(name="P_TRAIN_TYPE_IDX")
	private String pTrainTypeIdx;
	/* 检修车型简称 */
	@Column(name="P_TRAIN_TYPE_SHORTNAME")
	private String pTrainTypeShortname;
    
	/* 额定工时（分钟） */
	@Column(name="Rated_WorkHours")
	private String ratedWorkHours;
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType;   
    
    public String getRatedWorkHours() {
        return ratedWorkHours;
    }
    
    public void setRatedWorkHours(String ratedWorkHours) {
        this.ratedWorkHours = ratedWorkHours;
    }
    /**
	 * @return String 获取检修项目编码
	 */
	public String getRepairProjectCode(){
		return repairProjectCode;
	}
	/**
	 * @param 设置检修项目编码
	 */
	public void setRepairProjectCode(String repairProjectCode) {
		this.repairProjectCode = repairProjectCode;
	}
	/**
	 * @return String 获取检修项目名称
	 */
	public String getRepairProjectName(){
		return repairProjectName;
	}
	/**
	 * @param 设置检修项目名称
	 */
	public void setRepairProjectName(String repairProjectName) {
		this.repairProjectName = repairProjectName;
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
	public String getPTrainTypeIdx() {
		return pTrainTypeIdx;
	}
	public void setPTrainTypeIdx(String trainTypeIdx) {
		pTrainTypeIdx = trainTypeIdx;
	}
	public String getPTrainTypeShortname() {
		return pTrainTypeShortname;
	}
	public void setPTrainTypeShortname(String trainTypeShortname) {
		pTrainTypeShortname = trainTypeShortname;
	}

    
    public String getVehicleType() {
        return vehicleType;
    }

    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    
}