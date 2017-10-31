package com.yunda.jx.webservice.stationTerminal.base.entity;

import java.io.Serializable;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于查询已/未搭载的提票信息实体包装
 * <li>说明: 用于findUnTakeFaultNotice接口方法
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
public class TakeFaultNoticeBean implements Serializable {


    private String idx; //主键
	
    private String faultNoticeCode; //提票单号
	
	private String faultName; //故障现象
	
	private String fixPlaceFullName; //故障位置
	
	private String faultNoticeTime; //提票时间

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

	public String getFaultNoticeTime() {
		return faultNoticeTime;
	}

	public void setFaultNoticeTime(String faultNoticeTime) {
		this.faultNoticeTime = faultNoticeTime;
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

     
	
}
