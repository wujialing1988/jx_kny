package com.yunda.jx.webservice.stationTerminal.base.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于工长派工查询页面中的生产任务单信息实体包装
 * <li>说明: 用于findRdpByForemanDispatcher接口方法
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
public class FindRdpByForemanDispatcherBean implements java.io.Serializable{
	
	private String idx ; //主键

	private String groupRdpInfo ; //工长派工查询-生产任务单: 机车兑现单和配件兑现单区分标识
	
	private String trainTypeIDX ; // 车型主键
	
	private String trainNo ; //车号
	
//	private String buildUpTypeIDX ; //组成型号主键

//	public String getBuildUpTypeIDX() {
//		return buildUpTypeIDX;
//	}
//
//	public void setBuildUpTypeIDX(String buildUpTypeIDX) {
//		this.buildUpTypeIDX = buildUpTypeIDX;
//	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getTrainTypeIDX() {
		return trainTypeIDX;
	}

	public void setTrainTypeIDX(String trainTypeIDX) {
		this.trainTypeIDX = trainTypeIDX;
	}

	public String getGroupRdpInfo() {
		return groupRdpInfo;
	}

	public void setGroupRdpInfo(String groupRdpInfo) {
		this.groupRdpInfo = groupRdpInfo;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}
	
}
