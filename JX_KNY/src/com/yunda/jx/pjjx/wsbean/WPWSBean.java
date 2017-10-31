package com.yunda.jx.pjjx.wsbean;

import java.io.Serializable;

/**
 * <li>说明： 需求Bean
 * <li>创建人： 张凡
 * <li>创建日期：2015-11-3
 * <li>成都运达科技股份有限公司
 */
@SuppressWarnings(value="serial")
public class WPWSBean implements Serializable{

	private String idx;
	/* 编号 */
	private String wPNo;
	/* 描述 */
	private String wPDesc;
	/* 额定工期 */
	private Double ratedPeriod;
	/* 额定工时 */
	private Double ratedWorkTime;
	/* 备注 */
	private String remarks;
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getWPNo() {
		return wPNo;
	}
	public void setWPNo(String no) {
		wPNo = no;
	}
	public String getWPDesc() {
		return wPDesc;
	}
	public void setWPDesc(String desc) {
		wPDesc = desc;
	}
	public Double getRatedPeriod() {
		return ratedPeriod;
	}
	public void setRatedPeriod(Double ratedPeriod) {
		this.ratedPeriod = ratedPeriod;
	}
	public Double getRatedWorkTime() {
		return ratedWorkTime;
	}
	public void setRatedWorkTime(Double ratedWorkTime) {
		this.ratedWorkTime = ratedWorkTime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
