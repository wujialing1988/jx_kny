package com.yunda.jx.wlgl.backwh.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatBackWHSelect实体类, 视图：出库物料选择
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-8 上午11:36:29
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name = "V_WLGL_MAT_BACK_WH_SELECT")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE)
public class MatBackWHSelect implements java.io.Serializable {

	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** idx主键 */
	@Id
	private String idx;

	/** 退库库房主键 */
	@Column(name = "WH_IDX")
	private String whIdx;
	
	/** 单据编号 */
	@Column(name = "Bill_No")
	private String billNo;

	/** 单据摘要 */
	@Column(name = "Bill_Summary")
	private String billSummary;

	/** 领用机构id */
	@Column(name = "GET_ORG_ID")
	private String getOrgId;

	/** 领用人 */
	@Column(name = "GET_EMP")
	private String getEmp;

	/** 领用日期 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GET_DATE")
	private Date getDate;

	/** 领用人id */
	@Column(name = "GET_EMP_ID")
	private String getEmpId;

	/** 用途 */
	private String purpose;

	/** 物料编码 */
	@Column(name = "Mat_Code")
	private String matCode;

	/** 物料描述 */
	@Column(name = "Mat_Desc")
	private String matDesc;

	/** 出库数量 */
	private Integer qty;

	/** 已退库数量 */
	@Column(name = "BACK_QTY")
	private Integer backQty;

	/** 计量单位 */
	private String unit;

	public Date getGetDate() {
		return getDate;
	}

	public void setGetDate(Date getDate) {
		this.getDate = getDate;
	}

	public String getGetEmp() {
		return getEmp;
	}

	public void setGetEmp(String getEmp) {
		this.getEmp = getEmp;
	}

	public String getGetEmpId() {
		return getEmpId;
	}

	public void setGetEmpId(String getEmpId) {
		this.getEmpId = getEmpId;
	}

	public String getGetOrgId() {
		return getOrgId;
	}

	public void setGetOrgId(String getOrgId) {
		this.getOrgId = getOrgId;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getMatCode() {
		return matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	public String getMatDesc() {
		return matDesc;
	}

	public void setMatDesc(String matDesc) {
		this.matDesc = matDesc;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	public Integer getBackQty() {
		return backQty;
	}

	public void setBackQty(Integer backQty) {
		this.backQty = backQty;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getWhIdx() {
		return whIdx;
	}

	public void setWhIdx(String whIdx) {
		this.whIdx = whIdx;
	}
	
	/** 查询字段 - 领用开始日期 */
	@Transient
	private Date startDate;

	/** 查询字段 - 领用结束日期 */
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

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBillSummary() {
		return billSummary;
	}

	public void setBillSummary(String billSummary) {
		this.billSummary = billSummary;
	}

}