package com.yunda.jx.webservice.stationTerminal.base.entity;

import java.io.Serializable;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于获取待领取任务实体包装
 * <li>说明: 用于waitReceive接口方法
 * <li>创建人：王斌
 * <li>创建日期：2013-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WaitReceiveBean implements Serializable{
  
	private String idx;   //作业工单idx
	
	private String workCardName; //作业卡名称
	
	private String trainSortName; //车型(配件型号)
	
	private String trainNo; //车号(配件号)
	
	private String fixPlaceFullName; //位置
	
	private String repairClassRepairTime; //修程修次
	
	private String nodeCaseName; //节点名称
	
	private String repairActivityName; //活动名称

	public String getFixPlaceFullName() {
		return fixPlaceFullName;
	}

	public void setFixPlaceFullName(String fixPlaceFullName) {
		this.fixPlaceFullName = fixPlaceFullName;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
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

	public String getRepairClassRepairTime() {
		return repairClassRepairTime;
	}

	public void setRepairClassRepairTime(String repairClassRepairTime) {
		this.repairClassRepairTime = repairClassRepairTime;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getTrainSortName() {
		return trainSortName;
	}

	public void setTrainSortName(String trainSortName) {
		this.trainSortName = trainSortName;
	}

	public String getWorkCardName() {
		return workCardName;
	}

	public void setWorkCardName(String workCardName) {
		this.workCardName = workCardName;
	}
	
	
}
