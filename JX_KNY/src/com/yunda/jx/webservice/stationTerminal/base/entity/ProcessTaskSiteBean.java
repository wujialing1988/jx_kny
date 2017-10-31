package com.yunda.jx.webservice.stationTerminal.base.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于获得流程任务实体包装类
 * <li>说明: 用于getProcessTaskListSite接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProcessTaskSiteBean implements java.io.Serializable{
	
	private String rdpIdx ; // 兑现单主键
	private String workItemName ; // 工作项名称
	private String trainType ; // 车型
	private String trainNo ; // 车号
	private String repairClassName ; // 修程
	private String repairtimeName ; // 修次
	
	public String getRdpIdx() {
		return rdpIdx;
	}
	public void setRdpIdx(String rdpIdx) {
		this.rdpIdx = rdpIdx;
	}
	public String getRepairClassName() {
		return repairClassName;
	}
	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}
	public String getRepairtimeName() {
		return repairtimeName;
	}
	public void setRepairtimeName(String repairtimeName) {
		this.repairtimeName = repairtimeName;
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public String getTrainType() {
		return trainType;
	}
	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}
	public String getWorkItemName() {
		return workItemName;
	}
	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}
}
