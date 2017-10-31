package com.yunda.jx.webservice.stationTerminal.base.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于工长派工实体包装
 * <li>说明: 用于newForemanDispatcher接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class NewForemanDispatcherBean implements java.io.Serializable {
	
	private String idx ; //作业工单IDX
	private String trainNo ; //机车(配件)号
//	private String fixPlaceFullName ; //位置
	private String lastTimeWorker ; //上次作业人员
	private String workers ; //作业人员
	private String nodeCaseName ; //节点名称
	private String repairActivityName ; //活动名称
	private String workCardName ; //作业工单名称
	private String workStationName ; //班组	
	private String workStationBelongTeamName ; //工位
	private String status;//状态
	private String handledWorkers;//已处理状态的上次作业人员
	
	public String getHandledWorkers() {
		return handledWorkers;
	}
	public void setHandledWorkers(String handledWorkers) {
		this.handledWorkers = handledWorkers;
	}
	public String getWorkStationBelongTeamName() {
		return workStationBelongTeamName;
	}
	public void setWorkStationBelongTeamName(String workStationBelongTeamName) {
		this.workStationBelongTeamName = workStationBelongTeamName;
	}
	public String getWorkStationName() {
		return workStationName;
	}
	public void setWorkStationName(String workStationName) {
		this.workStationName = workStationName;
	}
//	public String getFixPlaceFullName() {
//		return fixPlaceFullName;
//	}
//	public void setFixPlaceFullName(String fixPlaceFullName) {
//		this.fixPlaceFullName = fixPlaceFullName;
//	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getLastTimeWorker() {
		return lastTimeWorker;
	}
	public void setLastTimeWorker(String lastTimeWorker) {
		this.lastTimeWorker = lastTimeWorker;
	}
	public String getNodeCaseName() {
		return nodeCaseName;
	}
	public void setNodeCaseName(String nodeCaseName) {
		this.nodeCaseName = nodeCaseName;
	}
	public String getRepairActivityName() {
		return repairActivityName;
	}
	public void setRepairActivityName(String repairActivityName) {
		this.repairActivityName = repairActivityName;
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public String getWorkCardName() {
		return workCardName;
	}
	public void setWorkCardName(String workCardName) {
		this.workCardName = workCardName;
	}
	public String getWorkers() {
		return workers;
	}
	public void setWorkers(String workers) {
		this.workers = workers;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
