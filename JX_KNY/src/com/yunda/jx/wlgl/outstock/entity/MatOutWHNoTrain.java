package com.yunda.jx.wlgl.outstock.entity;

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
 * <li>说明：MatOutWHNoTrain实体类, 数据表：机车外用料单
 * <li>创建人： 何涛
 * <li>创建日期： 2014-9-26 上午10:48:06
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "WLGL_Mat_Out_WH_NoTrain")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class MatOutWHNoTrain implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@GenericGenerator(strategy = "uuid", name = "uuid_id_generator")
	@Id @GeneratedValue(generator = "uuid_id_generator")
	private String idx;

	/** 单据编号 */
	@Column(name = "Bill_No")
	private String billNo;

	/** 单据摘要 */
	@Column(name = "Bill_Summary")
	private String billSummary;

	/** 库房主键 */
	@Column(name = "WH_IDX")
	private String whIdx;

	/** 库房名称 */
	@Column(name = "WH_Name")
	private String whName;

	/** 单据状态，temporary：暂存；登帐：entryAccount */
	private String status;

	/** 出库人主键 */
	@Column(name = "EX_WH_Emp_ID")
	private Integer exWhEmpId;

	/** 出库人名称 */
	@Column(name = "EX_WH_Emp")
	private String exWhEmp;

	/** 领用人主键 */
	@Column(name = "Get_Emp_ID")
	private Integer getEmpId;

	/** 领用人名称 */
	@Column(name = "Get_Emp")
	private String getEmp;

	/** 领用机构id */
	@Column(name = "Get_Org_ID")
	private Integer getOrgId;

	/** 领用机构序列 */
	@Column(name = "Get_Org_Seq")
	private String getOrgSeq;

	/** 领用机构名称 */
	@Column(name = "Get_Org")
	private String getOrg;

	/** 用途 */
	private String purpose;

	/** 领用日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Get_Date")
	private java.util.Date getDate;

	/** 制单人 */
	@Column(name = "Make_Bill_Emp")
	private String makeBillEmp;

	/** 制单日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Make_Bill_Date")
	private java.util.Date makeBillDate;

	/** 登帐人 */
	@Column(name = "Regist_Emp")
	private String registEmp;

	/** 登帐日期 */
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

	/** 查询字段 - 维护开始日期 */
	@Transient
	private Date startDate;

	/** 查询字段 - 维护结束日期 */
	@Transient
	private Date endDate;

	/**
	 * @return String 获取单据编号
	 */
	public String getBillNo() {
		return billNo;
	}

	/**
	 * @return String 获取单据摘要
	 */
	public String getBillSummary() {
		return billSummary;
	}

	/**
	 * @return java.util.Date 获取创建时间
	 */
	public java.util.Date getCreateTime() {
		return createTime;
	}

	/**
	 * @return Long 获取创建人
	 */
	public Long getCreator() {
		return creator;
	}

	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return 获取出库人名称
	 */
	public String getExWhEmp() {
		return exWhEmp;
	}

	/**
	 * @return 获取出库人id
	 */
	public Integer getExWhEmpId() {
		return exWhEmpId;
	}

	/**
	 * @return 获取领用日期
	 */
	public java.util.Date getGetDate() {
		return getDate;
	}

	/**
	 * @return 获取领用人名称
	 */
	public String getGetEmp() {
		return getEmp;
	}

	/**
	 * @return 获取领用人id
	 */
	public Integer getGetEmpId() {
		return getEmpId;
	}

	/**
	 * @return 获取领用机构名称
	 */
	public String getGetOrg() {
		return getOrg;
	}

	/**
	 * @return 获取领用机构id
	 */
	public Integer getGetOrgId() {
		return getOrgId;
	}

	/**
	 * @return 获取领用机构序列
	 */
	public String getGetOrgSeq() {
		return getOrgSeq;
	}

	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}

	/**
	 * @return java.util.Date 获取制单日期
	 */
	public java.util.Date getMakeBillDate() {
		return makeBillDate;
	}

	/**
	 * @return String 获取制单人
	 */
	public String getMakeBillEmp() {
		return makeBillEmp;
	}

	/**
	 * @return 获取用途
	 */
	public String getPurpose() {
		return purpose;
	}

	/**
	 * @return Integer 获取记录的状态
	 */
	public Integer getRecordStatus() {
		return recordStatus;
	}

	/**
	 * @return java.util.Date 获取登帐日期
	 */
	public java.util.Date getRegistDate() {
		return registDate;
	}

	/**
	 * @return String 获取登帐人
	 */
	public String getRegistEmp() {
		return registEmp;
	}

	/**
	 * @return String 获取站点标识
	 */
	public String getSiteID() {
		return siteID;
	}

	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return String 获取单据状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return java.util.Date 获取修改时间
	 */
	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @return Long 获取修改人
	 */
	public Long getUpdator() {
		return updator;
	}

	/**
	 * @return String 获取库房主键
	 */
	public String getWhIdx() {
		return whIdx;
	}

	/**
	 * @return String 获取库房名称
	 */
	public String getWhName() {
		return whName;
	}

	/**
	 * @param 设置单据编号
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * @param 设置单据摘要
	 */
	public void setBillSummary(String billSummary) {
		this.billSummary = billSummary;
	}

	/**
	 * @param 设置创建时间
	 */
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @param 设置创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @param exWhEmp 设置出库人名称
	 */
	public void setExWhEmp(String exWhEmp) {
		this.exWhEmp = exWhEmp;
	}

	/**
	 * @param exWhEmpId 设置出库人名称id
	 */
	public void setExWhEmpId(Integer exWhEmpId) {
		this.exWhEmpId = exWhEmpId;
	}

	/**
	 * @param getDate 设置领用日期
	 */
	public void setGetDate(java.util.Date getDate) {
		this.getDate = getDate;
	}

	/**
	 * @param getEmp 设置领用人名称
	 */
	public void setGetEmp(String getEmp) {
		this.getEmp = getEmp;
	}

	/**
	 * @param getEmpId 设置领用人id
	 */
	public void setGetEmpId(Integer getEmpId) {
		this.getEmpId = getEmpId;
	}

	/**
	 * @param getOrg 设置领用机构名称
	 */
	public void setGetOrg(String getOrg) {
		this.getOrg = getOrg;
	}

	/**
	 * @param getOrgId 设置领用机构id
	 */
	public void setGetOrgId(Integer getOrgId) {
		this.getOrgId = getOrgId;
	}

	/**
	 * @param getOrgSeq 设置领用机构序列
	 */
	public void setGetOrgSeq(String getOrgSeq) {
		this.getOrgSeq = getOrgSeq;
	}

	/**
	 * @param 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}

	/**
	 * @param 设置制单日期
	 */
	public void setMakeBillDate(java.util.Date makeBillDate) {
		this.makeBillDate = makeBillDate;
	}

	/**
	 * @param 设置制单人
	 */
	public void setMakeBillEmp(String makeBillEmp) {
		this.makeBillEmp = makeBillEmp;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	/**
	 * @param 设置记录的状态
	 */
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	/**
	 * @param 设置登帐日期
	 */
	public void setRegistDate(java.util.Date registDate) {
		this.registDate = registDate;
	}

	/**
	 * @param 设置登帐人
	 */
	public void setRegistEmp(String registEmp) {
		this.registEmp = registEmp;
	}

	/**
	 * @param 设置站点标识
	 */
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @param 设置单据状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param 设置修改时间
	 */
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @param 设置修改人
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}

	/**
	 * @param 设置库房主键
	 */
	public void setWhIdx(String whIdx) {
		this.whIdx = whIdx;
	}

	/**
	 * @param 设置库房名称
	 */
	public void setWhName(String whName) {
		this.whName = whName;
	}

}