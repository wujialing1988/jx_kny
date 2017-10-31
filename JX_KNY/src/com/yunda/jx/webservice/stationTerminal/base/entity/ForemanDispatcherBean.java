package com.yunda.jx.webservice.stationTerminal.base.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于查询作业工单工长派工数据实体包装
 * <li>说明: 用于foremanDispatcher接口方法
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
public class ForemanDispatcherBean implements java.io.Serializable{
	
	private String idx ; //作业工单IDX
	private String trainTypeTrainNo ; //车型车号
	private String workCardName ; //作业工单名称
	private String workStationBelongTeamName ; //工位所属班组
	private String workStationName ; //工位名称	
	private String workScope ; //作业内容
	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getTrainTypeTrainNo() {
		return trainTypeTrainNo;
	}
	public void setTrainTypeTrainNo(String trainTypeTrainNo) {
		this.trainTypeTrainNo = trainTypeTrainNo;
	}
	public String getWorkCardName() {
		return workCardName;
	}
	public void setWorkCardName(String workCardName) {
		this.workCardName = workCardName;
	}
	public String getWorkScope() {
		return workScope;
	}
	public void setWorkScope(String workScope) {
		this.workScope = workScope;
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
	
	
}
