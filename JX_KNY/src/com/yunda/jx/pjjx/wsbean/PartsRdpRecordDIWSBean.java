package com.yunda.jx.pjjx.wsbean;

import java.io.Serializable;

/**
 * <li>说明：检测项结果列表WS实体
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-19
 * <li>成都运达科技股份有限公司
 */
public class PartsRdpRecordDIWSBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idx;
	/* 检测项编号 */
	private String dataItemNo;
	/* 检测项名称 */
	private String dataItemName;
	/* 是否必填 */
	private Integer isBlank;
	/* 顺序号 */
	private Integer seqNo;
	/* 检测结果 */
	private String dataItemResult;
	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getDataItemNo() {
		return dataItemNo;
	}
	public void setDataItemNo(String dataItemNo) {
		this.dataItemNo = dataItemNo;
	}
	public String getDataItemName() {
		return dataItemName;
	}
	public void setDataItemName(String dataItemName) {
		this.dataItemName = dataItemName;
	}
	public Integer getIsBlank() {
		return isBlank;
	}
	public void setIsBlank(Integer isBlank) {
		this.isBlank = isBlank;
	}
	public Integer getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	public String getDataItemResult() {
		return dataItemResult;
	}
	public void setDataItemResult(String dataItemResult) {
		this.dataItemResult = dataItemResult;
	}
}
