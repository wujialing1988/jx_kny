package com.yunda.jx.webservice.stationTerminal.wsbean;

import java.io.Serializable;

/**
 * <li>说明： WS返回工位实体映射Bean
 * <li>创建人： 张凡
 * <li>创建日期：2015年11月6日
 * <li>成都运达科技股份有限公司
 */
@SuppressWarnings(value="serial")
public class WorkStationWSBean implements Serializable{
	private String idx;
	/* 工位编码 */
	private String workStationCode;
	/* 工位名称 */
	private String workStationName;
	/* 流水线主键 */
	private String repairLineIdx;
	/* 流水线名称 */
	private String repairLineName;
	/* 备注 */
	private String remarks;
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getWorkStationCode() {
		return workStationCode;
	}
	public void setWorkStationCode(String workStationCode) {
		this.workStationCode = workStationCode;
	}
	public String getWorkStationName() {
		return workStationName;
	}
	public void setWorkStationName(String workStationName) {
		this.workStationName = workStationName;
	}
	public String getRepairLineIdx() {
		return repairLineIdx;
	}
	public void setRepairLineIdx(String repairLineIdx) {
		this.repairLineIdx = repairLineIdx;
	}
	public String getRepairLineName() {
		return repairLineName;
	}
	public void setRepairLineName(String repairLineName) {
		this.repairLineName = repairLineName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
