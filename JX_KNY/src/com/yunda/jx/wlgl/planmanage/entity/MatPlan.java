package com.yunda.jx.wlgl.planmanage.entity;

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
 * <li>说明：MatPlan实体类, 数据表：用料计划单
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-10
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "WLGL_Mat_Plan")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class MatPlan implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id @GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 单据编号 */
	@Column(name = "BILL_NO")
	private String billNo;

	/** 计划名称 */
	@Column(name = "Plan_Name")
	private String planName;

	/** 申请人主键 */
	@Column(name = "Apply_EMP_ID")
	private Long applyEmpId;

	/** 申请人名称 */
	@Column(name = "Apply_EMP")
	private String applyEmp;

	/** 申请日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Apply_DATE")
	private java.util.Date applyDate;

	/** 使用班组 */
	@Column(name = "Use_Team")
	private String useTeam;

	/** 单据状态，temporary：暂存；ToAudit：待审核；Audited：已审核 */
	private String status;

	/** 制单人 */
	@Column(name = "Make_Bill_Emp")
	private String makeBillEmp;

	/** 制单日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Make_Bill_Date")
	private java.util.Date makeBillDate;

	/** 提交人 */
	@Column(name = "Regist_Emp")
	private String registEmp;

	/** 提交人日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Regist_Date")
	private java.util.Date registDate;

	/** 表示此条记录的状态：0为表示未删除；1表示删除 */
	@Column(name = "Record_Status")
	private Integer recordStatus;

	/** 站点标识，为了同步数据而使用 */
	@Column(updatable = false)
	private String siteID;

	/** 创建人 */
	@Column(updatable = false)
	private Long creator;

	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Create_Time", updatable = false)
	private java.util.Date createTime;

	/** 修改人 */
	private Long updator;

	/** 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Update_Time")
	private java.util.Date updateTime;

	/**
	 * @return String 获取单据编号
	 */
	public String getBillNo() {
		return billNo;
	}

	/**
	 * @param 设置单据编号
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * @return String 获取计划名称
	 */
	public String getPlanName() {
		return planName;
	}

	/**
	 * @param 设置计划名称
	 */
	public void setPlanName(String planName) {
		this.planName = planName;
	}

	/**
	 * @return Long 获取申请人主键
	 */
	public Long getApplyEmpId() {
		return applyEmpId;
	}

	/**
	 * @param 设置申请人主键
	 */
	public void setApplyEmpId(Long applyEmpId) {
		this.applyEmpId = applyEmpId;
	}

	/**
	 * @return String 获取申请人名称
	 */
	public String getApplyEmp() {
		return applyEmp;
	}

	/**
	 * @param 设置申请人名称
	 */
	public void setApplyEmp(String applyEmp) {
		this.applyEmp = applyEmp;
	}

	/**
	 * @return java.util.Date 获取申请日期
	 */
	public java.util.Date getApplyDate() {
		return applyDate;
	}

	/**
	 * @param 设置申请日期
	 */
	public void setApplyDate(java.util.Date applyDate) {
		this.applyDate = applyDate;
	}

	/**
	 * @return String 获取使用班组
	 */
	public String getUseTeam() {
		return useTeam;
	}

	/**
	 * @param 设置使用班组
	 */
	public void setUseTeam(String useTeam) {
		this.useTeam = useTeam;
	}

	/**
	 * @return String 获取单据状态
	 */
	public String getStatus() {
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
	public String getMakeBillEmp() {
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
	public java.util.Date getMakeBillDate() {
		return makeBillDate;
	}

	/**
	 * @param 设置制单日期
	 */
	public void setMakeBillDate(java.util.Date makeBillDate) {
		this.makeBillDate = makeBillDate;
	}

	/**
	 * @return String 获取提交人
	 */
	public String getRegistEmp() {
		return registEmp;
	}

	/**
	 * @param 设置提交人
	 */
	public void setRegistEmp(String registEmp) {
		this.registEmp = registEmp;
	}

	/**
	 * @return java.util.Date 获取提交日期
	 */
	public java.util.Date getRegistDate() {
		return registDate;
	}

	/**
	 * @param 设置提交日期
	 */
	public void setRegistDate(java.util.Date registDate) {
		this.registDate = registDate;
	}

	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus() {
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
	public String getSiteID() {
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
	public Long getCreator() {
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
	public java.util.Date getCreateTime() {
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
	public Long getUpdator() {
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
	public java.util.Date getUpdateTime() {
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

	/** 查询字段 - 开始日期 */
	@Transient
	private Date startDate;

	/** 查询字段 - 结束日期 */
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
	
}