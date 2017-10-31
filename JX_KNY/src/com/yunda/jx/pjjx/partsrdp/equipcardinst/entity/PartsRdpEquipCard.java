package com.yunda.jx.pjjx.partsrdp.equipcardinst.entity;

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
 * <li>说明：PartsRdpEquipCard实体类, 数据表：机务设备工单
 * <li>创建人：何涛
 * <li>创建日期：2014-12-05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="PJJX_Parts_Rdp_Equip_Card")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class PartsRdpEquipCard implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	
	/** 状态-未处理 */
    public static final String STATUS_WCL = "01";
    /** 状态-已处理 */
    public static final String STATUS_YCL = "02";
    
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 作业主键 */
	@Column(name="Rdp_IDX")
	private String rdpIDX;
	/* 设备工单定义主键 */
	@Column(name="Equip_Card_IDX")
	private String equipCardIDX;
	/* 工单编号 */
	@Column(name="Equip_Card_No")
	private String equipCardNo;
	/* 工单描述 */
	@Column(name="Equip_Card_Desc")
	private String equipCardDesc;
	/* 规格型号 */
	@Column(name="SPECIFICATION_MODEL")
	private String specificationModel;
	/* 配件名称 */
	@Column(name="PARTS_NAME")
	private String partsName;
	/* 配件编号 */
	@Column(name="PARTS_NO")
	private String partsNo;
	/* 设备分类编码 */
	@Column(name="Device_Type_Code")
	private String deviceTypeCode;
	/* 设备分类名称 */
	@Column(name="Device_Type_Name")
	private String deviceTypeName;
	/* 设备编码 */
	@Column(name="Device_Info_Code")
	private String deviceInfoCode;
	/* 顺序号 */
	@Column(name="Seq_No")
	private Integer seqNo;
	/* 作业人 */
	@Column(name="Work_EmpID")
	private String workEmpID;
	/* 作业人名称 */
	@Column(name="Work_EmpName")
	private String workEmpName;
	/* 作业开始时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Work_StartTime")
	private java.util.Date workStartTime;
	/* 作业结束时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Work_EndTime")
	private java.util.Date workEndTime;
	/* 数据生成时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Data_GenTime")
	private java.util.Date dataGenTime;
	/* 作业结果 */
	@Column(name="Work_Result")
	private String workResult;
	/* 备注 */
	private String remarks;
	/* 状态 */
	private String status;
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

	/**
	 * @return String 获取作业主键
	 */
	public String getRdpIDX(){
		return rdpIDX;
	}
	/**
	 * @param rdpIDX 设置作业主键
	 */
	public void setRdpIDX(String rdpIDX) {
		this.rdpIDX = rdpIDX;
	}
	/**
	 * @return String 获取设备工单定义主键
	 */
	public String getEquipCardIDX(){
		return equipCardIDX;
	}
	/**
	 * @param equipCardIDX 设置设备工单定义主键
	 */
	public void setEquipCardIDX(String equipCardIDX) {
		this.equipCardIDX = equipCardIDX;
	}
	/**
	 * @return String 获取工单编号
	 */
	public String getEquipCardNo(){
		return equipCardNo;
	}
	/**
	 * @param equipCardNo 设置工单编号
	 */
	public void setEquipCardNo(String equipCardNo) {
		this.equipCardNo = equipCardNo;
	}
	/**
	 * @return String 获取工单描述
	 */
	public String getEquipCardDesc(){
		return equipCardDesc;
	}
	/**
	 * @param equipCardDesc 设置工单描述
	 */
	public void setEquipCardDesc(String equipCardDesc) {
		this.equipCardDesc = equipCardDesc;
	}
	/**
	 * @return String 获取规格型号
	 */
	public String getSpecificationModel(){
		return specificationModel;
	}
	/**
	 * @param specificationModel 设置规格型号
	 */
	public void setSpecificationModel(String specificationModel) {
		this.specificationModel = specificationModel;
	}
	/**
	 * @return String 获取配件名称
	 */
	public String getPartsName(){
		return partsName;
	}
	/**
	 * @param partsName 设置配件名称
	 */
	public void setPartsName(String partsName) {
		this.partsName = partsName;
	}
	/**
	 * @return String 获取配件编号
	 */
	public String getPartsNo(){
		return partsNo;
	}
	/**
	 * @param partsNo 设置配件编号
	 */
	public void setPartsNo(String partsNo) {
		this.partsNo = partsNo;
	}
	/**
	 * @return String 获取设备编码
	 */
	public String getDeviceInfoCode() {
		return deviceInfoCode;
	}
	/**
	 * @param deviceInfoCode 设置设备编码
	 */
	public void setDeviceInfoCode(String deviceInfoCode) {
		this.deviceInfoCode = deviceInfoCode;
	}
	/**
	 * @return 获取设备分类编码
	 */
	public String getDeviceTypeCode() {
		return deviceTypeCode;
	}
	/**
	 * @param deviceTypeCode 设置设备分类编码
	 */
	public void setDeviceTypeCode(String deviceTypeCode) {
		this.deviceTypeCode = deviceTypeCode;
	}
	/**
	 * @return 获取设备分类名称
	 */
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	/**
	 * @param deviceTypeName 设置设备分类名称
	 */
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	/**
	 * @return Integer 获取顺序号
	 */
	public Integer getSeqNo(){
		return seqNo;
	}
	/**
	 * @param seqNo 设置顺序号
	 */
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	/**
	 * @return String 获取作业人
	 */
	public String getWorkEmpID(){
		return workEmpID;
	}
	/**
	 * @param workEmpID 设置作业人
	 */
	public void setWorkEmpID(String workEmpID) {
		this.workEmpID = workEmpID;
	}
	/**
	 * @return String 获取作业人名称
	 */
	public String getWorkEmpName(){
		return workEmpName;
	}
	/**
	 * @param workEmpName 设置作业人名称
	 */
	public void setWorkEmpName(String workEmpName) {
		this.workEmpName = workEmpName;
	}
	/**
	 * @return java.util.Date 获取作业开始时间
	 */
	public java.util.Date getWorkStartTime(){
		return workStartTime;
	}
	/**
	 * @param workStartTime 设置作业开始时间
	 */
	public void setWorkStartTime(java.util.Date workStartTime) {
		this.workStartTime = workStartTime;
	}
	/**
	 * @return java.util.Date 获取作业结束时间
	 */
	public java.util.Date getWorkEndTime(){
		return workEndTime;
	}
	/**
	 * @param workEndTime 设置作业结束时间
	 */
	public void setWorkEndTime(java.util.Date workEndTime) {
		this.workEndTime = workEndTime;
	}
	/**
	 * @return java.util.Date 获取数据生成时间
	 */
	public java.util.Date getDataGenTime(){
		return dataGenTime;
	}
	/**
	 * @param dataGenTime 设置数据生成时间
	 */
	public void setDataGenTime(java.util.Date dataGenTime) {
		this.dataGenTime = dataGenTime;
	}
	/**
	 * @return String 获取作业结果
	 */
	public String getWorkResult(){
		return workResult;
	}
	/**
	 * @param workResult 设置作业结果
	 */
	public void setWorkResult(String workResult) {
		this.workResult = workResult;
	}
	/**
	 * @return String 获取备注
	 */
	public String getRemarks(){
		return remarks;
	}
	/**
	 * @param remarks 设置备注
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return String 获取状态
	 */
	public String getStatus(){
		return status;
	}
	/**
	 * @param status 设置状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param recordStatus 设置记录的状态
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
	 * @param siteID 设置站点标识
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
	 * @param creator 设置创建人
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
	 * @param createTime 设置创建时间
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
	 * @param updator 设置修改人
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
	 * @param updateTime 设置修改时间
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
	 * @param idx 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
}