package com.yunda.jx.webservice.stationTerminal.base.entity;

import java.io.Serializable;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于查询已工长派工或未工长派工的信息列表实体包装
 * <li>说明: 用于findForemanDispatchList接口方法
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
public class ForemanDispatchBean implements Serializable{
  

    private String idx; //主键
	
	private String trainTypeShortName; //车型
	
	private String trainNo; //车号
	
	private String faultType; //提票类型
	
	private String faultNoticeCode; //提票单号
	
	private String fixPlaceFullName; //位置
	
	private String count; //作业人员
	
	private String faultName; //故障现象
	
	private String faultDesc; //故障描述

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getFaultDesc() {
		return faultDesc;
	}

	public void setFaultDesc(String faultDesc) {
		this.faultDesc = faultDesc;
	}

	public String getFaultName() {
		return faultName;
	}

	public void setFaultName(String faultName) {
		this.faultName = faultName;
	}

	public String getFaultNoticeCode() {
		return faultNoticeCode;
	}

	public void setFaultNoticeCode(String faultNoticeCode) {
		this.faultNoticeCode = faultNoticeCode;
	}

	public String getFaultType() {
		return faultType;
	}

	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}

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

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getTrainTypeShortName() {
		return trainTypeShortName;
	}

	public void setTrainTypeShortName(String trainTypeShortName) {
		this.trainTypeShortName = trainTypeShortName;
	}
	
	
	
   
}
