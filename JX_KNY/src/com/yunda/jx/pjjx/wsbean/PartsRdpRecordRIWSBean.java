package com.yunda.jx.pjjx.wsbean;

import java.io.Serializable;

/**
 * <li>说明：检修/检测项列表WS实体
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-19
 * <li>成都运达科技股份有限公司
 */
public class PartsRdpRecordRIWSBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String idx;
	/* 检修检测项名称 */
	private String repairItemName;
	/* 技术要求 */
	private String repairStandard;
	/* 检测结果 */
	private String repairResult;
	/* 状态 */
	private String status;
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getRepairItemName() {
		return repairItemName;
	}
	public void setRepairItemName(String repairItemName) {
		this.repairItemName = repairItemName;
	}
	public String getRepairStandard() {
		return repairStandard;
	}
	public void setRepairStandard(String repairStandard) {
		this.repairStandard = repairStandard;
	}
	public String getRepairResult() {
		return repairResult;
	}
	public void setRepairResult(String repairResult) {
		this.repairResult = repairResult;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
