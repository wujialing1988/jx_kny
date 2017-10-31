package com.yunda.jx.webservice.stationTerminal.base.entity;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于获取任务列表包装
 * <li>说明: 用于getProcessTaskList接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-24
 * <li>修改人: 王斌
 * <li>修改日期：2014-1-2
 * <li>修改内容：增加字段activityInstID、rdpIdx、key、processInstName
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProcessTaskListBean implements java.io.Serializable{
	
	private String idx ; //Idx
	private String trainType ; //车型（配件型）
	private String trainNo ; //车号（配件号）
	private String repairClassName ; //修程
	private String taskDepict ; //任务描述
	private String workItemName ; //工单名称
	private String processInstID ; //流程实例ID
	private String workItemID ; //工作项ID
	private String sourceIdx ; //业务主键ID
	private String token ; //业务类型（1.作业卡、2.提票、3.技术改造等）
	private String actionType ; //数据查询类型标识
	private String repairtimeName ; //修次
	private Long activityInstID;//活动实例ID
	private String rdpIdx;//兑现单主键
	private String key; //存放主键
	private String processInstName;//流程实例名称
	private boolean parts = false; //标识是否为配件
	private String repairTeam;//承修班组
	private String workCardName;//作业工单名称
	private String fixPlaceFullName;//位置
	private String checkItemCode;//质检项id
	
	public String getRepairtimeName() {
		return repairtimeName;
	}
	public void setRepairtimeName(String repairtimeName) {
		this.repairtimeName = repairtimeName;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getProcessInstID() {
		return processInstID;
	}
	public void setProcessInstID(String processInstID) {
		this.processInstID = processInstID;
	}
	public String getRepairClassName() {
		return repairClassName;
	}
	public void setRepairClassName(String repairClassName) {
		this.repairClassName = repairClassName;
	}
	public String getSourceIdx() {
		return sourceIdx;
	}
	public void setSourceIdx(String sourceIdx) {
		this.sourceIdx = sourceIdx;
	}
	public String getTaskDepict() {
		return taskDepict;
	}
	public void setTaskDepict(String taskDepict) {
		this.taskDepict = taskDepict;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	public String getWorkItemID() {
		return workItemID;
	}
	public void setWorkItemID(String workItemID) {
		this.workItemID = workItemID;
	}
	public String getWorkItemName() {
		return workItemName;
	}
	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}
	public Long getActivityInstID() {
		return activityInstID;
	}
	public void setActivityInstID(Long activityInstID) {
		this.activityInstID = activityInstID;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getProcessInstName() {
		return processInstName;
	}
	public void setProcessInstName(String processInstName) {
		this.processInstName = processInstName;
	}
	public String getRdpIdx() {
		return rdpIdx;
	}
	public void setRdpIdx(String rdpIdx) {
		this.rdpIdx = rdpIdx;
	}
    
    public boolean isParts() {
        return parts;
    }
    
    public void setParts(boolean parts) {
        this.parts = parts;
    }
	public String getRepairTeam() {
		return repairTeam;
	}
	public void setRepairTeam(String repairTeam) {
		this.repairTeam = repairTeam;
	}
	public String getFixPlaceFullName() {
		return fixPlaceFullName;
	}
	public void setFixPlaceFullName(String fixPlaceFullName) {
		this.fixPlaceFullName = fixPlaceFullName;
	}
	public String getWorkCardName() {
		return workCardName;
	}
	public void setWorkCardName(String workCardName) {
		this.workCardName = workCardName;
	}
	public String getCheckItemCode() {
		return checkItemCode;
	}
	public void setCheckItemCode(String checkItemCode) {
		this.checkItemCode = checkItemCode;
	}
    
}
