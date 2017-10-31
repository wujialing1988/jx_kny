package com.yunda.jx.pjjx.wsbean;

import java.io.Serializable;

/**
 * <li>说明： 工序
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-19
 * <li>成都运达科技股份有限公司
 */
public class PartsRdpTecWSBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idx;
	/* 工序编号 */
	private String wsNo;
	/* 工序名称 */
	private String wsName;
	/* 工序描述 */
	private String wsDesc;
	/* 状态 */
	private String status;
	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getWsNo() {
		return wsNo;
	}
	public void setWsNo(String wsNo) {
		this.wsNo = wsNo;
	}
	public String getWsName() {
		return wsName;
	}
	public void setWsName(String wsName) {
		this.wsName = wsName;
	}
	public String getWsDesc() {
		return wsDesc;
	}
	public void setWsDesc(String wsDesc) {
		this.wsDesc = wsDesc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
