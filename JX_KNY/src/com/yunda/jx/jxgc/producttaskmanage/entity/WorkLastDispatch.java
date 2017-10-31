package com.yunda.jx.jxgc.producttaskmanage.entity;

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
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 存储最后一次派工记录 实体类
 * <li>创建人：张凡
 * <li>创建日期：2013-12-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="JXGC_WORK_LAST_DISPATCH")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WorkLastDispatch implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 安装位置全名 */
	@Column(name="fixplace_fullname")
	private String fixPlaceFullName;
	/* 施修班组名称 */
	@Column(name="WORK_REPAIR_TEAM_NAME")
	private String workRepairTeamName;
	/* 施修班组 */
	@Column(name="WORK_REPAIR_TEAM")
	private long workRepairTeam;
	/* 工序卡主键 */
    @Column(name="WORK_SEQ_CARD_IDX")
	private String workSeqCardIdx;
    /* 施修人员主键 */
	@Column(name="WORKERS")
	private String workers;
	/* 施修人员名称 */
	@Column(name="WORKERS_NAME")
	private String workersName;
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
    public String getFixPlaceFullName() {
        return fixPlaceFullName;
    }
    
    public void setFixPlaceFullName(String fixPlaceFullName) {
        this.fixPlaceFullName = fixPlaceFullName;
    }
    
    public String getWorkRepairTeamName() {
        return workRepairTeamName;
    }
    
    public void setWorkRepairTeamName(String workRepairTeamName) {
        this.workRepairTeamName = workRepairTeamName;
    }
    
    public long getWorkRepairTeam() {
        return workRepairTeam;
    }
    
    public void setWorkRepairTeam(long workRepairTeam) {
        this.workRepairTeam = workRepairTeam;
    }
    
    public String getWorkSeqCardIdx() {
        return workSeqCardIdx;
    }
    
    public void setWorkSeqCardIdx(String workSeqCardIdx) {
        this.workSeqCardIdx = workSeqCardIdx;
    }
    
    public String getWorkers() {
        return workers;
    }
    
    public void setWorkers(String workers) {
        this.workers = workers;
    }
    
    public String getWorkersName() {
        return workersName;
    }
    
    public void setWorkersName(String workersName) {
        this.workersName = workersName;
    }
}