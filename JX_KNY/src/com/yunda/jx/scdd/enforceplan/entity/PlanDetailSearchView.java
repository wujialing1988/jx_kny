package com.yunda.jx.scdd.enforceplan.entity;

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
 * <li>说明：PlanDetailSearchView对应视图：v_scdd_plan_detail_search
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
@Table(name="v_scdd_plan_detail_search")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PlanDetailSearchView implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/* 生产计划开始日期(生产计划主表的计划开始日期Plan_Start_Date) */
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date startPlanDate;
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
//	/* 实际进车日期（作业进度表的入厂/段修日期） */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="enter_deport_date")
//	private java.util.Date enterDeportDate;
//	/* 实际交车日期（作业进度表的落成日期） */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="Complete_Date")
//	private java.util.Date completeDate;
//	/* 实际出厂日期（作业进度表的出厂/段修日期） */
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="out_deport_date")	
//	private java.util.Date outDeportDate;
//	/* 停时（落成日期Complete_Date-入厂/段修日期Enter_Deport_Date），单位天  */
//	private Integer stopTime;
//	/* 在厂（出厂/段修日期Out_Deport_Date-入厂/段修日期Enter_Deport_Date)，单位天  */
//	private Integer inFactory;
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
	
	public java.util.Date getStartPlanDate() {
		return startPlanDate;
	}
	public void setStartPlanDate(java.util.Date startPlanDate) {
		this.startPlanDate = startPlanDate;
	}
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    
	
}