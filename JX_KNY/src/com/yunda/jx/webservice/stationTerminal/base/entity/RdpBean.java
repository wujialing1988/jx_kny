package com.yunda.jx.webservice.stationTerminal.base.entity;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: WebService包装类：用于根据工单或者流程任务查询兑现单实体包装
 * <li>说明: 用于getRdpByWorkQuery,getRdpByProcess接口方法
 * <li>创建人：王治龙
 * <li>创建日期：2013-12-30
 * <li>修改人: 王斌
 * <li>修改日期：2014-1-9
 * <li>修改内容：增加字段trainTypeIDX、trainNo、buildUpTypeIDX
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class RdpBean implements java.io.Serializable{
	
	/**
     * 
     */
    private String idx ; // 兑现单主键
	private String rdpText ; //兑现单信息
	private String trainTypeIDX;//车型主键
	private String trainNo;//车号
	private String buildUpTypeIDX;//组成主键
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getRdpText() {
		return rdpText;
	}
	public void setRdpText(String rdpText) {
		this.rdpText = rdpText;
	}
	public String getBuildUpTypeIDX() {
		return buildUpTypeIDX;
	}
	public void setBuildUpTypeIDX(String buildUpTypeIDX) {
		this.buildUpTypeIDX = buildUpTypeIDX;
	}
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
