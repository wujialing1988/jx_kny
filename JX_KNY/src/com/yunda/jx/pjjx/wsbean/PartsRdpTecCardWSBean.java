package com.yunda.jx.pjjx.wsbean;

import java.io.Serializable;

/**
 * <li>说明： 作业工单WS实体
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-19
 * <li>成都运达科技股份有限公司
 */
public class PartsRdpTecCardWSBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idx;
	/* 工艺卡编号 */
	private String tecCardNo;
	/* 工艺卡描述 */
	private String tecCardDesc;
	/* 作业人名称 */
	private String workEmpName;
	/* 作业开始时间 */
	private java.util.Date workStartTime;
	/* 作业结束时间 */
	private java.util.Date workEndTime;
	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getTecCardNo() {
		return tecCardNo;
	}
	public void setTecCardNo(String tecCardNo) {
		this.tecCardNo = tecCardNo;
	}
	public String getTecCardDesc() {
		return tecCardDesc;
	}
	public void setTecCardDesc(String tecCardDesc) {
		this.tecCardDesc = tecCardDesc;
	}
	public String getWorkEmpName() {
		return workEmpName;
	}
	public void setWorkEmpName(String workEmpName) {
		this.workEmpName = workEmpName;
	}
	public java.util.Date getWorkStartTime() {
		return workStartTime;
	}
	public void setWorkStartTime(java.util.Date workStartTime) {
		this.workStartTime = workStartTime;
	}
	public java.util.Date getWorkEndTime() {
		return workEndTime;
	}
	public void setWorkEndTime(java.util.Date workEndTime) {
		this.workEndTime = workEndTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/* 状态 */
	private String status;
}
