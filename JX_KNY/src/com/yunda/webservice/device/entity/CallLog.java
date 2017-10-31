package com.yunda.webservice.device.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.hibernate.annotations.GenericGenerator;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：CallLog实体类, 数据表：设备接口-调用日志记录
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-01-06
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Entity
@Table(name="SBJK_Call_Log")
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class CallLog implements java.io.Serializable{
	/* 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;
	/* idx主键 */
	@GenericGenerator(strategy="uuid", name = "uuid_id_generator")
	@Id	@GeneratedValue(generator="uuid_id_generator")
	private String idx;
	/* 调用方法名称 */
	@Column(name="method_Name")
	private String methodName;
	/* 请求参数内容 */
	@Column(name="request_Content")
	private String requestContent;
	/* 返回结果信息 */
	@Column(name="response_Result")
	private String responseResult;
	/* 调用接口时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="call_Time")
	private java.util.Date callTime;
	/* 调用方信息 */
	@Column(name="caller_Info")
	private String callerInfo;
	/* 备注 */
	private String remark;

	/**
	 * @return String 获取调用方法名称
	 */
	public String getMethodName(){
		return methodName;
	}
	/**
	 * @param 设置调用方法名称
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @return String 获取请求参数内容
	 */
	public String getRequestContent(){
		return requestContent;
	}
	/**
	 * @param 设置请求参数内容
	 */
	public void setRequestContent(String requestContent) {
		this.requestContent = requestContent;
	}
	/**
	 * @return String 获取返回结果信息
	 */
	public String getResponseResult(){
		return responseResult;
	}
	/**
	 * @param 设置返回结果信息
	 */
	public void setResponseResult(String responseResult) {
		this.responseResult = responseResult;
	}
	/**
	 * @return java.util.Date 获取调用接口时间
	 */
	public java.util.Date getCallTime(){
		return callTime;
	}
	/**
	 * @param 设置调用接口时间
	 */
	public void setCallTime(java.util.Date callTime) {
		this.callTime = callTime;
	}
	/**
	 * @return String 获取调用方信息
	 */
	public String getCallerInfo(){
		return callerInfo;
	}
	/**
	 * @param 设置调用方信息
	 */
	public void setCallerInfo(String callerInfo) {
		this.callerInfo = callerInfo;
	}
	/**
	 * @return String 获取备注
	 */
	public String getRemark(){
		return remark;
	}
	/**
	 * @param 设置备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return String idx主键
	 */
	public String getIdx() {
		return idx;
	}
	/**
	 * @param 设置idx主键
	 */
	public void setIdx(String idx) {
		this.idx = idx;
	}
}