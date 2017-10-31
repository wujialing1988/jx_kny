package com.yunda.jx.pjjx.wsbean;

import java.io.Serializable;

/**
 * <li>说明：检修记录WS实体
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-19
 * <li>成都运达科技股份有限公司
 */
public class PartsRdpRecordCardWSBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idx;
	/* 回退标识 */
	private Integer isBack;
	/* 质量检验 */
	private String qcContent;
	/* 记录卡描述 */
	private String recordCardDesc;
	/* 记录卡编号 */
	private String recordCardNo;
	/* 检修情况描述 */
	private String remarks;
	/* 顺序号 */
	private Integer seqNo;
	/* 状态 */
	private String status;
	/* 作业人名称 */
	private String workEmpName;
	/* 作业结束时间 */
	private java.util.Date workEndTime;
	/* 作业开始时间 */
	private java.util.Date workStartTime;
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public Integer getIsBack() {
		return isBack;
	}
	public void setIsBack(Integer isBack) {
		this.isBack = isBack;
	}
	public String getQcContent() {
		return qcContent;
	}
	public void setQcContent(String qcContent) {
		this.qcContent = qcContent;
	}
	public String getRecordCardDesc() {
		return recordCardDesc;
	}
	public void setRecordCardDesc(String recordCardDesc) {
		this.recordCardDesc = recordCardDesc;
	}
	public String getRecordCardNo() {
		return recordCardNo;
	}
	public void setRecordCardNo(String recordCardNo) {
		this.recordCardNo = recordCardNo;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWorkEmpName() {
		return workEmpName;
	}
	public void setWorkEmpName(String workEmpName) {
		this.workEmpName = workEmpName;
	}
	public java.util.Date getWorkEndTime() {
		return workEndTime;
	}
	public void setWorkEndTime(java.util.Date workEndTime) {
		this.workEndTime = workEndTime;
	}
	public java.util.Date getWorkStartTime() {
		return workStartTime;
	}
	public void setWorkStartTime(java.util.Date workStartTime) {
		this.workStartTime = workStartTime;
	}
}
