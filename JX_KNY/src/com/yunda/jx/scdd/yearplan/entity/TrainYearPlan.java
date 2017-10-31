package com.yunda.jx.scdd.yearplan.entity;

import java.io.Serializable;

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
 * <li>说明：TrainYearPlan实体类, 数据表：机车年度检修计划
 * <li>创建人：郝凤
 * <li>创建日期：2016-10-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息事业部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="SCDD_TRAIN_YEAR_PLAN")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TrainYearPlan implements Serializable{
    /*使用默认序列号版本*/
	private static final long serialVersionUID = 1L;
	/*主键*/
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/*车型主键*/
	@Column(name="TRAIN_TYPE_IDX")
	private String trainTypeIdx;
	/*车型英文简称*/
	@Column(name="TRAIN_TYPE_SHORTNAME")
	private String trainTypeShortName;
	/*修程编码*/
	@Column(name="REPAIR_CLASS_IDX")
	private String repairClassIdx;
	/*修程名称*/
	@Column(name="REPAIR_CLASS_NAME")
	private String repairClassName;
	/*数据状态：1为删除状态，0为未删除状态*/
	@Column(name="RECORD_STATUS")
	private int recordStatus;
	/*站点标识*/
	@Column(name="SITEID")
	private String siteId;
	/* 创建人 */
	@Column(updatable=false)
	private Long creator;
	/* 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME",updatable=false)
	private java.util.Date createTime;
	/*年份*/
	@Column(name="DATE_YEAR")
	private String dateYear;
	/*第一季度计划台数*/
	@Column(name="FIRST_QUARTER")
	private Long firstQuarter;
	/*第二季度计划台数*/
	@Column(name="SECOND_QUARTER")
	private Long secondQuarter;
	/*第三季度计划台数*/
	@Column(name="THIRD_QUARTER")
	private Long thirdQuarter;
	/*第四季度计划台数*/
	@Column(name="FOURTH_QUARTER")
	private Long fourthQuarter;
	/*年计划总台数*/
	@Column(name="PLAN_COUNT")
	private Long planCount;
    
    /*初始值*/
    @Column(name="INITIAL_VALUE")
    private Long initialValue;
    
	public Long getPlanCount() {
		return planCount;
	}
	public void setPlanCount(Long planCount) {
		this.planCount = planCount;
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
	    this.creator=creator;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public int getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(int recordStatus) {
		this.recordStatus = recordStatus;
	}
	public String getRepairClassIdx() {
		return repairClassIdx;
	}
	public void setRepairClassIdx(String repairClassIdx) {
		this.repairClassIdx = repairClassIdx;
	}
	public String getRepairClassName() {
		return repairClassName;
	}
	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getTrainTypeIdx() {
		return trainTypeIdx;
	}
	public void setTrainTypeIdx(String trainTypeIdx) {
		this.trainTypeIdx = trainTypeIdx;
	}
	public String getTrainTypeShortName() {
		return trainTypeShortName;
	}
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	public String getDateYear() {
		return dateYear;
	}
	public void setDateYear(String dateYear) {
		this.dateYear = dateYear;
	}
    
    public Long getInitialValue() {
        return initialValue;
    }
    
    public void setInitialValue(Long initialValue) {
        this.initialValue = initialValue;
    }
    
    public Long getFirstQuarter() {
        return firstQuarter;
    }
    
    public void setFirstQuarter(Long firstQuarter) {
        this.firstQuarter = firstQuarter;
    }
    
    public Long getFourthQuarter() {
        return fourthQuarter;
    }
    
    public void setFourthQuarter(Long fourthQuarter) {
        this.fourthQuarter = fourthQuarter;
    }
    
    public Long getSecondQuarter() {
        return secondQuarter;
    }
    
    public void setSecondQuarter(Long secondQuarter) {
        this.secondQuarter = secondQuarter;
    }
    
    public Long getThirdQuarter() {
        return thirdQuarter;
    }
    
    public void setThirdQuarter(Long thirdQuarter) {
        this.thirdQuarter = thirdQuarter;
    }
	
	
	
}
