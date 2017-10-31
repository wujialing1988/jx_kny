package com.yunda.passenger.operationsafetyrecord.entity;

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
 * <li>说明: 运行安全信息记录
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
@Table(name = "K_OPERATION_SAFETY_RECORD")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class OperationSafetyRecord implements java.io.Serializable {
    
    /* 使用默认序列版本ID */
    private static final long serialVersionUID = 1L;
    
    /* idx主键 */
    @GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
    @Id
    @GeneratedValue(generator = "uuid_id_generator")
    private String idx;
    
    /* 列表需求计划idx */ 
    @Column(name = "TRAIN_DEMAND_IDX")
    private String trainDemandIDX;   
    
    /* 车次 */ 
    @Column(name = "STRIPS")
    private String strips;   
    
    /* 运行开始日期 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RUNNING_DATE")
    private Date runningDate;
 	
    /* 反馈人ID（多人人员之间用";"号分隔开） */
    @Column(name = "REPORT_EMP_ID")
    private String reportEmpID;
    
    /* 反馈人名称（多人人员之间用";"号分隔开） */
    @Column(name = "REPORT_EMP_NAME")
    private String reportEmpName;
    
    /* 填报人ID */
    @Column(name = "EMP_ID")
    private Long empID;
    
    /* 填报人名称 */
    @Column(name = "EMP_NAME")
    private String empName;
    
    /* 填报内容 */ 
    private String content;            
    
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getReportEmpID() {
		return reportEmpID;
	}

	public void setReportEmpID(String reportEmpID) {
		this.reportEmpID = reportEmpID;
	}

	public String getReportEmpName() {
		return reportEmpName;
	}

	public void setReportEmpName(String reportEmpName) {
		this.reportEmpName = reportEmpName;
	}

	public Date getRunningDate() {
		return runningDate;
	}

	public void setRunningDate(Date runningDate) {
		this.runningDate = runningDate;
	}

	public String getStrips() {
		return strips;
	}

	public void setStrips(String strips) {
		this.strips = strips;
	}

	public String getTrainDemandIDX() {
		return trainDemandIDX;
	}

	public void setTrainDemandIDX(String trainDemandIDX) {
		this.trainDemandIDX = trainDemandIDX;
	}

}

