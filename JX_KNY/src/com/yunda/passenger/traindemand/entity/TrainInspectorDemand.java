package com.yunda.passenger.traindemand.entity;

import java.util.Date;

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
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 列车需求乘车列检员日志记录表
 * <li>创建人：张迪
 * <li>创建日期：2017-4-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_TRAIN_INSPECTOR_DEMAND")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainInspectorDemand implements java.io.Serializable {
	   
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 列车需求IDX */ 
    @Column(name = "TRAIN_DEMAND_IDX")
    private java.lang.String trainDemandIdx;    
    /* 车次 */ 
    @Column(name = "STRIPS")
    private String strips;   
    /* 返程车次 */ 
    @Column(name = "BACK_STRIPS")
    private String backStrips;   
    /* 运行开始日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RUNNING_DATE")
    private Date runningDate;
    
    /* 返回日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BACK_DATE")
    private Date backDate;
    
    /* 历时（分钟） */ 
    @Column(name = "DURATION")
    private Double duration;   
    
    @Column(name = "EMPID")
    private Long empid; // 人员编号
    
    @Column(name = "EMPNAME")
    private String empname; // 人员名称

    /* 站点标识，为了同步数据而使用 */
	@Column(updatable=false)
	private String siteID;
    
    /* 表示此条记录的状态：0为表示未删除；1表示删除 */
    @Column(name = "Record_Status")
    private Integer recordStatus;
    
    /* 创建人 */
    @Column(updatable = false)
    private Long creator;
    
    /* 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Time", updatable = false)
    private Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private Date updateTime;
    
    @Transient
    private String month;
    @Transient
    private Double workHours;
    
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public Long getEmpid() {
		return empid;
	}

	public void setEmpid(Long empid) {
		this.empid = empid;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdator() {
		return updator;
	}

	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public Date getBackDate() {
		return backDate;
	}

	public void setBackDate(Date backDate) {
		this.backDate = backDate;
	}

	public java.lang.String getTrainDemandIdx() {
		return trainDemandIdx;
	}

	public void setTrainDemandIdx(java.lang.String trainDemandIdx) {
		this.trainDemandIdx = trainDemandIdx;
	}

	public void setRunningDate(Date runningDate) {
		this.runningDate = runningDate;
	}

	public Date getRunningDate() {
		return runningDate;
	}

	public String getBackStrips() {
		return backStrips;
	}

	public void setBackStrips(String backStrips) {
		this.backStrips = backStrips;
	}

	public String getStrips() {
		return strips;
	}

	public void setStrips(String strips) {
		this.strips = strips;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Double getWorkHours() {
		return workHours;
	}

	public void setWorkHours(Double workHours) {
		this.workHours = workHours;
	}
}
