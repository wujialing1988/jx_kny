package com.yunda.passenger.routing.entity;

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
 * <li>说明: 交路基本信息
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
@Table(name = "K_ROUTING")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class Routing implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 编号 */ 
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
    /* 历时（分钟） */ 
    @Column(name = "DURATION")
    private Double duration;   
    /* 到达时间 */ 
    @Column(name = "ARRIVAL_TIME")
    private String arrivalTime;   
    
    /* 返程出发时间 */ 
    @Column(name = "DEPARTURE_BACK_TIME")
    private String departureBackTime;   
    
    /* 返程到达时间 */ 
    @Column(name = "ARRIVAL_BACK_TIME")
    private String arrivalBackTime;   
    
    /* 出发时间 */ 
    @Column(name = "DEPARTURE_TIME")
    private String departureTime;   
            
    /* 公里数 */ 
    @Column(name = "KILOMETERS")
    private Integer kilometers;            

    /* 备注 */ 
    private String remark;    
    
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

