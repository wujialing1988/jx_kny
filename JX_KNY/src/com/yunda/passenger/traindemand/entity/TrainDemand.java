package com.yunda.passenger.traindemand.entity;


import java.util.Date;

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
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 列表需求维护
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "K_TRAIN_DEMAND")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class TrainDemand implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 编组idx */ 
    @Column(name = "MARSHALLING_IDX")
    private String marshallingIdx;       
    /* 编组code */ 
    @Column(name = "MARSHALLING_CODE")
    private String marshallingCode;       
    /* 编组名称*/ 
    @Column(name = "MARSHALLING_NAME")
    private String marshallingName;    
    
    /* 编组名称*/ 
    @Column(name = "MARSHALLING_TRAIN_COUNT_STR")
    private String marshallingTrainCountStr;       
    
    /* 交路idx */ 
    @Column(name = "ROUTING_IDX")
    private String routingIdx;       
    
    /* 交路编号 */ 
    @Column(name = "ROUTING_CODE")
    private String routingCode; 
    /* 出发地 */ 
    @Column(name = "STARTING_STATION")
    private String startingStation;   
    /* 前往地 */ 
    @Column(name = "LEAVE_OFF_STATION")
    private String leaveOffStation;   
    /* 车次 */ 
    @Column(name = "STRIPS")
    private String strips;   
    /* 返程车次 */ 
    @Column(name = "BACK_STRIPS")
    private String backStrips;   
    /* 历时（分钟） */ 
    @Column(name = "DURATION")
    private Double duration;   
    /* 到达时间 */ 
    @Column(name = "ARRIVAL_TIME")
    private String arrivalTime;   
    /* 出发时间 */ 
    @Column(name = "DEPARTURE_TIME")
    private String departureTime;   
    
    /* 返程出发时间 */ 
    @Column(name = "DEPARTURE_BACK_TIME")
    private String departureBackTime;   
    
    /* 返程到达时间 */ 
    @Column(name = "ARRIVAL_BACK_TIME")
    private String arrivalBackTime;   
    
    /* 公里数 */ 
    @Column(name = "KILOMETERS")
    private Integer kilometers;      
    /* 车辆数量 */ 
    @Column(name = "TRAIN_COUNT")
    private Integer trainCount;   
    /* 运行开始日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RUNNING_DATE")
    private Date runningDate;
    	
    /* 列检人ID（多人人员之间用","号分隔开） */
    @Column(name = "TRAIN_INSPECTOR_ID")
    private String trainInspectorID;
    
    /* 列检人名称（多人人员之间用","号分隔开） */
    @Column(name = "TRAIN_INSPECTOR_NAME")
    private String trainInspectorName;
    
    /* 备注 */ 
    private String remark;    
	/* 登记人 */
	@Column(name="EMPID")
	private Long empID;
	/* 登记人名称 */
	@Column(name="EMPNAME")
	private String empName;
	
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
    private java.util.Date createTime;
    
    /* 修改人 */
    private Long updator;
    
    /* 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Update_Time")
    private java.util.Date updateTime;
    
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
    
    public java.util.Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }
    
    public Long getCreator() {
        return creator;
    }
    
    public void setCreator(Long creator) {
        this.creator = creator;
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

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public String getLeaveOffStation() {
		return leaveOffStation;
	}

	public void setLeaveOffStation(String leaveOffStation) {
		this.leaveOffStation = leaveOffStation;
	}

	public String getStartingStation() {
		return startingStation;
	}

	public void setStartingStation(String startingStation) {
		this.startingStation = startingStation;
	}

	public String getStrips() {
		return strips;
	}

	public void setStrips(String strips) {
		this.strips = strips;
	}

	public Integer getKilometers() {
		return kilometers;
	}

	public void setKilometers(Integer kilometers) {
		this.kilometers = kilometers;
	}

	public String getRoutingCode() {
		return routingCode;
	}

	public void setRoutingCode(String routingCode) {
		this.routingCode = routingCode;
	}

	public Long getEmpID() {
		return empID;
	}

	public void setEmpID(Long empID) {
		this.empID = empID;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getMarshallingCode() {
		return marshallingCode;
	}

	public void setMarshallingCode(String marshallingCode) {
		this.marshallingCode = marshallingCode;
	}

	public String getMarshallingIdx() {
		return marshallingIdx;
	}

	public void setMarshallingIdx(String marshallingIdx) {
		this.marshallingIdx = marshallingIdx;
	}

	public String getMarshallingName() {
		return marshallingName;
	}

	public void setMarshallingName(String marshallingName) {
		this.marshallingName = marshallingName;
	}

	public String getRoutingIdx() {
		return routingIdx;
	}

	public void setRoutingIdx(String routingIdx) {
		this.routingIdx = routingIdx;
	}

	public Date getRunningDate() {
		return runningDate;
	}

	public void setRunningDate(Date runningDate) {
		this.runningDate = runningDate;
	}

	public Integer getTrainCount() {
		return trainCount;
	}

	public void setTrainCount(Integer trainCount) {
		this.trainCount = trainCount;
	}

	public String getTrainInspectorID() {
		return trainInspectorID;
	}

	public void setTrainInspectorID(String trainInspectorID) {
		this.trainInspectorID = trainInspectorID;
	}

	public String getTrainInspectorName() {
		return trainInspectorName;
	}

	public void setTrainInspectorName(String trainInspectorName) {
		this.trainInspectorName = trainInspectorName;
	}

	public String getBackStrips() {
		return backStrips;
	}

	public void setBackStrips(String backStrips) {
		this.backStrips = backStrips;
	}

	public String getMarshallingTrainCountStr() {
		return marshallingTrainCountStr;
	}

	public void setMarshallingTrainCountStr(String marshallingTrainCountStr) {
		this.marshallingTrainCountStr = marshallingTrainCountStr;
	}

	public String getArrivalBackTime() {
		return arrivalBackTime;
	}

	public void setArrivalBackTime(String arrivalBackTime) {
		this.arrivalBackTime = arrivalBackTime;
	}

	public String getDepartureBackTime() {
		return departureBackTime;
	}

	public void setDepartureBackTime(String departureBackTime) {
		this.departureBackTime = departureBackTime;
	}
    
    
}

