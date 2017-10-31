package com.yunda.jx.webservice.stationTerminal.base.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于根据车型获取车号列表实体包装
 * <li>说明: 用于getTrainNoByTrainType,findTrainNoByDD,findTrainNoByGZ接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-25
 * <li>修改人: 王斌
 * <li>修改日期：2014-1-2
 * <li>修改内容：增加字段trainTypeIDX
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class TrainNoBean implements java.io.Serializable{
	
	private String trainNo ; // 车号
    
	private String trainTypeIDX;//车型主键
	
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

	
	
}
