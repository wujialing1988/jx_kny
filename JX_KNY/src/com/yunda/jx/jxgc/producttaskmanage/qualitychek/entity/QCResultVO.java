package com.yunda.jx.jxgc.producttaskmanage.qualitychek.entity;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：QCResultVO 质量检验结果值对象，用于记录质量检验结果信息；
 * <li>创建人：汪东良
 * <li>创建日期：2014-11-22
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */

public class QCResultVO implements java.io.Serializable {
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* 作业卡主键 */
	private String workCardIDX;

	/* 检验项编码 */
	private String checkItemCode;

	/* 检验人员ID */
	private long qcEmpID;

	/* 检验人员名称 */
	private String qcEmpName;

	/* 检验时间 */
	private java.util.Date qcTime;

	/* 备注 */
	private String remarks;

	public String getCheckItemCode() {
		return checkItemCode;
	}

	public void setCheckItemCode(String checkItemCode) {
		this.checkItemCode = checkItemCode;
	}

	public long getQcEmpID() {
		return qcEmpID;
	}

	public void setQcEmpID(long qcEmpID) {
		this.qcEmpID = qcEmpID;
	}

	public String getQcEmpName() {
		return qcEmpName;
	}

	public void setQcEmpName(String qcEmpName) {
		this.qcEmpName = qcEmpName;
	}

	public java.util.Date getQcTime() {
		return qcTime;
	}

	public void setQcTime(java.util.Date qcTime) {
		this.qcTime = qcTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getWorkCardIDX() {
		return workCardIDX;
	}

	public void setWorkCardIDX(String workCardIDX) {
		this.workCardIDX = workCardIDX;
	}

}