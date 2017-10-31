package com.yunda.jx.wlgl.backwh.entity;

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
 * <li>说明：MatBackWH实体类, 数据表：退库单
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-8 上午10:22:52
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "WLGL_Mat_Back_WH")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class MatBackWH implements java.io.Serializable {

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

	/** 退库人主键 */
	@Column(name = "Back_WH_Emp_ID")
	private Integer backWhEmpId;

	/** 退库人名称 */
	@Column(name = "Back_WH_Emp")
	private String backWhEmp;

	/** 退库机构id */
	@Column(name = "BACK_ORG_ID")
	private Integer backOrgId;

	/** 退库机构序列 */
	@Column(name = "BACK_ORG_SEQ")
	private String backOrgSeq;

	/** 退库机构名称 */
	@Column(name = "BACK_ORG")
	private String backOrg;

	/** 退库原因 */
	@Column(name = "Back_Reason")
	private String backReason;

	/** 退库日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Back_Date")
	private java.util.Date backDate;

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
	 * @return 获取退库日期
	 */
	public java.util.Date getBackDate() {
		return backDate;
	}

	/**
	 * @return 获取退库原因
	 */
	public String getBackReason() {
		return backReason;
	}

	/**
	 * @return 获取退库人名称
	 */
	public String getBackWhEmp() {
		return backWhEmp;
	}

	/**
	 * @return 获取退库人主键
	 */
	public Integer getBackWhEmpId() {
		return backWhEmpId;
	}

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
	 * @return 获取退库机构名称
	 */
	public String getBackOrg() {
		return backOrg;
	}

	/**
	 * @return 获取退库机构id
	 */
	public Integer getBackOrgId() {
		return backOrgId;
	}

	/**
	 * @return 获取退库机构序列
	 */
	public String getBackOrgSeq() {
		return backOrgSeq;
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
	 * @param backDate
	 *            设置退库日期
	 */
	public void setBackDate(java.util.Date backDate) {
		this.backDate = backDate;
	}

	/**
	 * @param backReason
	 *            设置退库原因
	 */
	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}

	/**
	 * @param backWhEmp
	 *            设置退库人名称
	 */
	public void setBackWhEmp(String backWhEmp) {
		this.backWhEmp = backWhEmp;
	}

	/**
	 * @param backWhEmpId
	 *            设置退库人主键
	 */
	public void setBackWhEmpId(Integer backWhEmpId) {
		this.backWhEmpId = backWhEmpId;
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
	 * @param backOrg
	 *            设置退库机构名称
	 */
	public void setBackOrg(String backOrg) {
		this.backOrg = backOrg;
	}

	/**
	 * @param backOrgId
	 *            设置退库机构id
	 */
	public void setBackOrgId(Integer backOrgId) {
		this.backOrgId = backOrgId;
	}

	/**
	 * @param backOrgSeq
	 *            设置退库机构序列
	 */
	public void setBackOrgSeq(String backOrgSeq) {
		this.backOrgSeq = backOrgSeq;
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