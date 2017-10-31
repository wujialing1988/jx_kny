package com.yunda.jx.pjjx.wsbean;

import java.io.Serializable;

/**
 * <li>说明：回修提票WS实体
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-19
 * <li>成都运达科技股份有限公司
 */
public class PartsRdpNoticeWSBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idx;
	/* 提票单编号 */
	private String noticeNo;
	/* 问题描述 */
	private String noticeDesc;
	/* 提报人名称 */
	private String noticeEmpName;
	/* 提报时间 */
	private java.util.Date noticeTime;
	/* 处理方案描述 */
	private String solution;
	/* 作业人名称 */
	private String workEmpName;
	/* 作业开始时间 */
	private java.util.Date workStartTime;
	/* 作业结束时间 */
	private java.util.Date workEndTime;
	/* 领活人名称 */
	private String handleEmpName;
	/* 状态 */
	private String status;
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getNoticeNo() {
		return noticeNo;
	}
	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}
	public String getNoticeDesc() {
		return noticeDesc;
	}
	public void setNoticeDesc(String noticeDesc) {
		this.noticeDesc = noticeDesc;
	}
	public String getNoticeEmpName() {
		return noticeEmpName;
	}
	public void setNoticeEmpName(String noticeEmpName) {
		this.noticeEmpName = noticeEmpName;
	}
	public java.util.Date getNoticeTime() {
		return noticeTime;
	}
	public void setNoticeTime(java.util.Date noticeTime) {
		this.noticeTime = noticeTime;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
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
	public String getHandleEmpName() {
		return handleEmpName;
	}
	public void setHandleEmpName(String handleEmpName) {
		this.handleEmpName = handleEmpName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
