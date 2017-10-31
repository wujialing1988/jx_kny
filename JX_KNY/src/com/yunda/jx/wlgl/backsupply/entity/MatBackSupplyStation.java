package com.yunda.jx.wlgl.backsupply.entity;

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
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatBackSupplyStation实体类, 数据表：质量反馈单
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-08
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="WLGL_Mat_Back_Supply_Station")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class MatBackSupplyStation implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/** 单据状态 - 暂存 */
	public static final String STATUS_ZC = "temporary";
	/** 单据状态 - 登账 */
	public static final String STATUS_DZ = "entryAccount";
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 库房主键 */
	@Column(name="WH_IDX")
	private String whIdx;
	/* 库房名称 */
	@Column(name="WH_Name")
	private String whName;
	/* 反馈人主键 */
	@Column(name="FeedBack_Emp_ID")
	private Long feedBackEmpID;
	/* 反馈人名称 */
	@Column(name="FeedBack_Emp")
	private String feedBackEmp;
	/* 物料编码 */
	@Column(name="Mat_Code")
	private String matCode;
	/* 物料描述 */
	@Column(name="Mat_Desc")
	private String matDesc;
	/* 单位 */
	private String unit;
	/* 数量 */
	private Integer qty;
	/* 反馈日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="FeedBack_Date")
	private java.util.Date feedBackDate;
	/* 存在原因 */
	@Column(name="FeedBack_Reason")
	private String feedBackReason;
	/* 单据状态，temporary：暂存；登帐：entryAccount */
	private String status;
	/* 制单人 */
	@Column(name="Make_Bill_Emp")
	private String makeBillEmp;
	/* 制单日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Make_Bill_Date")
	private java.util.Date makeBillDate;
	/* 登帐人 */
	@Column(name="Regist_Emp")
	private String registEmp;
	/* 登帐日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Regist_Date")
	private java.util.Date registDate;
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

	/** 查询字段 - 维护开始日期 */
	@Transient
	private Date startDate;
	
	/** 查询字段 - 维护结束日期 */
	@Transient
	private Date endDate;
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return String 获取库房主键
	 */
	public String getWhIdx(){
		return whIdx;
	}
	/**
	 * @param 设置库房主键
	 */
	public void setWhIdx(String whIdx) {
		this.whIdx = whIdx;
	}
	/**
	 * @return String 获取库房名称
	 */
	public String getWhName(){
		return whName;
	}
	/**
	 * @param 设置库房名称
	 */
	public void setWhName(String whName) {
		this.whName = whName;
	}
	/**
	 * @return Long 获取反馈人主键
	 */
	public Long getFeedBackEmpID(){
		return feedBackEmpID;
	}
	/**
	 * @param 设置反馈人主键
	 */
	public void setFeedBackEmpID(Long feedBackEmpID) {
		this.feedBackEmpID = feedBackEmpID;
	}
	/**
	 * @return String 获取反馈人名称
	 */
	public String getFeedBackEmp(){
		return feedBackEmp;
	}
	/**
	 * @param 设置反馈人名称
	 */
	public void setFeedBackEmp(String feedBackEmp) {
		this.feedBackEmp = feedBackEmp;
	}
	/**
	 * @return String 获取物料编码
	 */
	public String getMatCode(){
		return matCode;
	}
	/**
	 * @param 设置物料编码
	 */
	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}
	/**
	 * @return String 获取物料描述
	 */
	public String getMatDesc(){
		return matDesc;
	}
	/**
	 * @param 设置物料描述
	 */
	public void setMatDesc(String matDesc) {
		this.matDesc = matDesc;
	}
	/**
	 * @return String 获取单位
	 */
	public String getUnit(){
		return unit;
	}
	/**
	 * @param 设置单位
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return Integer 获取数量
	 */
	public Integer getQty(){
		return qty;
	}
	/**
	 * @param 设置数量
	 */
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	/**
	 * @return java.util.Date 获取反馈日期
	 */
	public java.util.Date getFeedBackDate(){
		return feedBackDate;
	}
	/**
	 * @param 设置反馈日期
	 */
	public void setFeedBackDate(java.util.Date feedBackDate) {
		this.feedBackDate = feedBackDate;
	}
	/**
	 * @return String 获取存在原因
	 */
	public String getFeedBackReason(){
		return feedBackReason;
	}
	/**
	 * @param 设置存在原因
	 */
	public void setFeedBackReason(String feedBackReason) {
		this.feedBackReason = feedBackReason;
	}
	/**
	 * @return String 获取单据状态
	 */
	public String getStatus(){
		return status;
	}
	/**
	 * @param 设置单据状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return String 获取制单人
	 */
	public String getMakeBillEmp(){
		return makeBillEmp;
	}
	/**
	 * @param 设置制单人
	 */
	public void setMakeBillEmp(String makeBillEmp) {
		this.makeBillEmp = makeBillEmp;
	}
	/**
	 * @return java.util.Date 获取制单日期
	 */
	public java.util.Date getMakeBillDate(){
		return makeBillDate;
	}
	/**
	 * @param 设置制单日期
	 */
	public void setMakeBillDate(java.util.Date makeBillDate) {
		this.makeBillDate = makeBillDate;
	}
	/**
	 * @return String 获取登帐人
	 */
	public String getRegistEmp(){
		return registEmp;
	}
	/**
	 * @param 设置登帐人
	 */
	public void setRegistEmp(String registEmp) {
		this.registEmp = registEmp;
	}
	/**
	 * @return java.util.Date 获取登帐日期
	 */
	public java.util.Date getRegistDate(){
		return registDate;
	}
	/**
	 * @param 设置登帐日期
	 */
	public void setRegistDate(java.util.Date registDate) {
		this.registDate = registDate;
	}
	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus(){
		return recordStatus;
	}
	/**
	 * @param 设置记录的状态
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
}