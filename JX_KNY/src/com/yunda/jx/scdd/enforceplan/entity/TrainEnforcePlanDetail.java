package com.yunda.jx.scdd.enforceplan.entity;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainEnforcePlanDetail实体类, 数据表：机车生产计划明细
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-05-01
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="SCDD_Train_Enforce_Plan_Detail")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class TrainEnforcePlanDetail implements java.io.Serializable{
    /** 计划状态-编制(初始状态) */
    public static final int STATUS_PLAN = 10;
    /** 计划状态-已兑现 */
    public static final int STATUS_REDEMPTION = 20;
    /** 计划状态-检修完成 */
    public static final int STATUS_COMPLETE = 30;	
    /* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 机车施修计划主键 */
	@Column(name="Train_Enforce_Plan_IDX")
	private String trainEnforcePlanIDX;
	/* 车型主键 */
	@Column(name="Train_Type_IDX")
	private String trainTypeIDX;
	/* 车型英文简称 */
	@Column(name="Train_Type_ShortName")
	private String trainTypeShortName;
	/* 车号 */
	@Column(name="Train_No")
	private String trainNo;
	/* 配属局ID */
	@Column(name="B_ID")
	private String bid;
	/* 配属局名称 */
	@Column(name="B_Name")
	private String bName;
	/* 配属局简称 */
	@Column(name="B_ShortName")
	private String bShortName;
	/* 配属段ID */
	@Column(name="D_ID")
	private String did;
	/* 配属段名称 */
	@Column(name="D_NAME")
	private String dNAME;
	/* 配属段简称 */
	@Column(name="D_ShortName")
	private String dShortName;
	
	/* 支配段ID */
	@Column(name="used_d_id")
	private String usedDId;
	
	/* 支配段名称 */
	@Column(name="used_d_name")
	private String usedDName;
	
	/* 支配段简称 */
	@Column(name="used_d_shortname")
	private String usedDShortName;
	
	/* 修程编码 */
	@Column(name="Repair_Class_IDX")
	private String repairClassIDX;
	/* 修程名称 */
	@Column(name="Repair_Class_Name")
	private String repairClassName;
	/* 修次 */
	@Column(name="Repair_time_IDX")
	private String repairtimeIDX;
	/* 修次名称 */
	@Column(name="Repair_time_Name")
	private String repairtimeName;
	/*走行公里*/
	@Column(name="RUNNING_KM")
	private Double runningKM;
	/* 工作号 */
	@Column(name="Work_Number")
	private String workNumber;
	/* 承修部门ID */
	@Column(name="Undertake_ORGID")
	private Long undertakeOrgId;
	/* 承修部门序列 */
	@Column(name="Undertake_ORGSEQ")
	private String undertakeOrgSeq;
	/* 承修部门名称 */
	@Column(name="Undertake_ORGName")
	private String undertakeOrgName;
	/* 计划进车日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Plan_Start_Date")
	private java.util.Date planStartDate;
	/* 计划交车日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Plan_End_Date")
	private java.util.Date planEndDate;
	/* 实际进车日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Real_Start_Date")
	private java.util.Date realStartDate;
	/* 实际交车日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Real_End_Date")
	private java.util.Date realEndDate;
	/* 计划状态，10：编制(初始状态)；20：已兑现；30：检修完成 */
	@Column(name="Plan_Status")
	private Integer planStatus;
	/* 备注 */
	private String remarks;
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
	/* BPS流程定义名称 */
    @Column(name="BPS_Process_DefName")
    private String bpsProcessDefName;
    /* BPS流程中文名称 */
    @Column(name="BPS_Process_ChName")
    private String bpsProcessChName;
    /* 工艺流程主键 */
    @Column(name="Process_IDX")
    private String processIDX;
    /* 工艺流程名称 */
    @Column(name="Process_Name")
    private String processName;
	/* BPS模板主键*/
    @Column(name="bps_template_idx")
    private String bpsTemplateIdx;
    /* 委托维修段ID */
	@Column(name="DELEGATE_D_ID")
	private String delegateDId;
	/* 委托维修段名称 */
	@Column(name="DELEGATE_D_NAME")
	private String delegateDName;
	/* 委托维修段简称 */
	@Column(name="DELEGATE_D_SHORTNAME")
	private String delegateDShortname;
    
    /* 添加日期 2016-10-31 */ 
    /* 上次修程编码 */
    @Column(name="Last_Repair_Class_IDX")
    private String lastRepairClassIDX;
    /* 上次修程名称 */
    @Column(name="Last_Repair_Class_Name")
    private String lastRepairClassName;
    /* 上次修次 */
    @Column(name="Last_Repair_time_IDX")
    private String lastRepairtimeIDX;
    /* 上次修次名称 */
    @Column(name="Last_Repair_time_Name")
    private String lastRepairtimeName;
    
    /* 入厂/段时间 */    
    @Transient
    private String realWarehousingTime;  
    
    /* 客货类型 10 货车 20 客车*/
    @Column(name = "T_VEHICLE_TYPE")
    private String vehicleType; 

    /**
	 * @return String 获取机车施修计划主键
	 */
	public String getTrainEnforcePlanIDX(){
		return trainEnforcePlanIDX;
	}
	/**
	 * @param 设置机车施修计划主键
	 */
	public void setTrainEnforcePlanIDX(String trainEnforcePlanIDX) {
		this.trainEnforcePlanIDX = trainEnforcePlanIDX;
	}
	/**
	 * @return String 获取车型主键
	 */
	public String getTrainTypeIDX(){
		return trainTypeIDX;
	}
	/**
	 * @param 设置车型主键
	 */
	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}
	/**
	 * @return String 获取车型简称
	 */
	public String getTrainTypeShortName(){
		return trainTypeShortName;
	}
	/**
	 * @param 设置车型简称
	 */
	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	/**
	 * @return String 获取车号
	 */
	public String getTrainNo(){
		return trainNo;
	}
	/**
	 * @param 设置车号
	 */
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	/**
	 * @return String 获取配属局ID
	 */
	public String getBid(){
		return bid;
	}
	/**
	 * @param 设置配属局ID
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}
	/**
	 * @return String 获取配属局名称
	 */
	public String getBName(){
		return bName;
	}
	/**
	 * @param 设置配属局名称
	 */
	public void setBName(String bName) {
		this.bName = bName;
	}
	/**
	 * @return String 获取配属局简称
	 */
	public String getBShortName(){
		return bShortName;
	}
	/**
	 * @param 设置配属局简称
	 */
	public void setBShortName(String bShortName) {
		this.bShortName = bShortName;
	}
	/**
	 * @return String 获取配属段ID
	 */
	public String getDid(){
		return did;
	}
	/**
	 * @param 设置配属段ID
	 */
	public void setDid(String did) {
		this.did = did;
	}
	/**
	 * @return String 获取配属段名称
	 */
	public String getDNAME(){
		return dNAME;
	}
	/**
	 * @param 设置配属段名称
	 */
	public void setDNAME(String dNAME) {
		this.dNAME = dNAME;
	}
	/**
	 * @return String 获取配属段简称
	 */
	public String getDShortName(){
		return dShortName;
	}
	/**
	 * @param 设置配属段简称
	 */
	public void setDShortName(String dShortName) {
		this.dShortName = dShortName;
	}
	/**
	 * @return String 获取修程主键
	 */
	public String getRepairClassIDX(){
		return repairClassIDX;
	}
	/**
	 * @param 设置修程主键
	 */
	public void setRepairClassIDX(String repairClassIDX) {
		this.repairClassIDX = repairClassIDX;
	}
	/**
	 * @return String 获取修程名称
	 */
	public String getRepairClassName(){
		return repairClassName;
	}
	/**
	 * @param 设置修程名称
	 */
	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}
	/**
	 * @return String 获取修次主键
	 */
	public String getRepairtimeIDX(){
		return repairtimeIDX;
	}
	/**
	 * @param 设置修次主键
	 */
	public void setRepairtimeIDX(String repairtimeIDX) {
		this.repairtimeIDX = repairtimeIDX;
	}
	/**
	 * @return String 获取修次名称
	 */
	public String getRepairtimeName(){
		return repairtimeName;
	}
	/**
	 * @param 设置修次名称
	 */
	public void setRepairtimeName(String repairtimeName) {
		this.repairtimeName = repairtimeName;
	}
	
	public Double getRunningKM() {
		return runningKM;
	}
	public void setRunningKM(Double runningKM) {
		this.runningKM = runningKM;
	}
	/**
	 * @return String 获取工作号
	 */
	public String getWorkNumber(){
		return workNumber;
	}
	/**
	 * @param 设置工作号
	 */
	public void setWorkNumber(String workNumber) {
		this.workNumber = workNumber;
	}
	/**
	 * @return Long 获取承修部门ID
	 */
	public Long getUndertakeOrgId(){
		return undertakeOrgId;
	}
	/**
	 * @param 设置承修部门ID
	 */
	public void setUndertakeOrgId(Long undertakeOrgId) {
		this.undertakeOrgId = undertakeOrgId;
	}
	/**
	 * @return String 获取承修部门序列
	 */
	public String getUndertakeOrgSeq(){
		return undertakeOrgSeq;
	}
	/**
	 * @param 设置承修部门序列
	 */
	public void setUndertakeOrgSeq(String undertakeOrgSeq) {
		this.undertakeOrgSeq = undertakeOrgSeq;
	}
	/**
	 * @return String 获取承修部门名称
	 */
	public String getUndertakeOrgName(){
		return undertakeOrgName;
	}
	/**
	 * @param 设置承修部门名称
	 */
	public void setUndertakeOrgName(String undertakeOrgName) {
		this.undertakeOrgName = undertakeOrgName;
	}
	/**
	 * @return java.util.Date 获取计划进车日期
	 */
	public java.util.Date getPlanStartDate(){
		return planStartDate;
	}
	/**
	 * @param 设置计划进车日期
	 */
	public void setPlanStartDate(java.util.Date planStartDate) {
		this.planStartDate = planStartDate;
	}
	/**
	 * @return java.util.Date 获取计划交车日期
	 */
	public java.util.Date getPlanEndDate(){
		return planEndDate;
	}
	/**
	 * @param 设置计划交车日期
	 */
	public void setPlanEndDate(java.util.Date planEndDate) {
		this.planEndDate = planEndDate;
	}
	/**
	 * @return java.util.Date 获取实际进车日期
	 */
	public java.util.Date getRealStartDate(){
		return realStartDate;
	}
	/**
	 * @param 设置实际进车日期
	 */
	public void setRealStartDate(java.util.Date realStartDate) {
		this.realStartDate = realStartDate;
	}
	/**
	 * @return java.util.Date 获取实际交车日期
	 */
	public java.util.Date getRealEndDate(){
		return realEndDate;
	}
	/**
	 * @param 设置实际交车日期
	 */
	public void setRealEndDate(java.util.Date realEndDate) {
		this.realEndDate = realEndDate;
	}
	/**
	 * @return Integer 获取计划状态
	 */
	public Integer getPlanStatus(){
		return planStatus;
	}
	/**
	 * @param 设置计划状态
	 */
	public void setPlanStatus(Integer planStatus) {
		this.planStatus = planStatus;
	}
	/**
	 * @return String 获取备注
	 */
	public String getRemarks(){
		return remarks;
	}
	/**
	 * @param 设置备注
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
	/**
	 * <li>说明：
	 * <li>返回值： the usedDId
	 */
	public String getUsedDId() {
		return usedDId;
	}
	/**
	 * <li>说明：
	 * <li>参数： usedDId
	 */
	public void setUsedDId(String usedDId) {
		this.usedDId = usedDId;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the usedDName
	 */
	public String getUsedDName() {
		return usedDName;
	}
	/**
	 * <li>说明：
	 * <li>参数： usedDName
	 */
	public void setUsedDName(String usedDName) {
		this.usedDName = usedDName;
	}
	/**
	 * <li>说明：
	 * <li>返回值： the usedDShortName
	 */
	public String getUsedDShortName() {
		return usedDShortName;
	}
	/**
	 * <li>说明：
	 * <li>参数： usedDShortName
	 */
	public void setUsedDShortName(String usedDShortName) {
		this.usedDShortName = usedDShortName;
	}
    
    public String getRealWarehousingTime() {
        return realWarehousingTime;
    }
    
    public void setRealWarehousingTime(String realWarehousingTime) {
        this.realWarehousingTime = realWarehousingTime;
    }
    
    public String getBpsProcessDefName() {
        return bpsProcessDefName;
    }
    
    public void setBpsProcessDefName(String bpsProcessDefName) {
        this.bpsProcessDefName = bpsProcessDefName;
    }
    
    public String getBpsProcessChName() {
        return bpsProcessChName;
    }
    
    public void setBpsProcessChName(String bpsProcessChName) {
        this.bpsProcessChName = bpsProcessChName;
    }
    
    public String getProcessIDX() {
        return processIDX;
    }
    
    public void setProcessIDX(String processIDX) {
        this.processIDX = processIDX;
    }
    
    public String getProcessName() {
        return processName;
    }
    
    public void setProcessName(String processName) {
        this.processName = processName;
    }
    
    public String getBpsTemplateIdx() {
        return bpsTemplateIdx;
    }
    
    public void setBpsTemplateIdx(String bpsTemplateIdx) {
        this.bpsTemplateIdx = bpsTemplateIdx;
    }
	public String getDelegateDId() {
		return delegateDId;
	}
	public void setDelegateDId(String delegateDId) {
		this.delegateDId = delegateDId;
	}
	public String getDelegateDName() {
		return delegateDName;
	}
	public void setDelegateDName(String delegateDName) {
		this.delegateDName = delegateDName;
	}
	public String getDelegateDShortname() {
		return delegateDShortname;
	}
	public void setDelegateDShortname(String delegateDShortname) {
		this.delegateDShortname = delegateDShortname;
	}
    
    public String getLastRepairClassIDX() {
        return lastRepairClassIDX;
    }
    
    public void setLastRepairClassIDX(String lastRepairClassIDX) {
        this.lastRepairClassIDX = lastRepairClassIDX;
    }
    
    public String getLastRepairClassName() {
        return lastRepairClassName;
    }
    
    public void setLastRepairClassName(String lastRepairClassName) {
        this.lastRepairClassName = lastRepairClassName;
    }
    
    public String getLastRepairtimeIDX() {
        return lastRepairtimeIDX;
    }
    
    public void setLastRepairtimeIDX(String lastRepairtimeIDX) {
        this.lastRepairtimeIDX = lastRepairtimeIDX;
    }
    
    public String getLastRepairtimeName() {
        return lastRepairtimeName;
    }
    
    public void setLastRepairtimeName(String lastRepairtimeName) {
        this.lastRepairtimeName = lastRepairtimeName;
    }
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    
    
}