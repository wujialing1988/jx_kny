package com.yunda.webservice.device.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 检修系统与终端设备接口，上传、下传数据交互以XML文本格式传输，
 * 			该类描述XML中的QualityCheckItem元素，表示参数代码 （参数代码参考参数字典）
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XStreamAlias("QualityCheckItem")
//@XStreamConverter(AttributeValueConveter.class)
public class QualityCheckItem{
	/** 检查项名称 */
	@XStreamAlias("CheckItemName")
	private String checkItemName;
	/** 检查项代码 */
//	@XStreamAsAttribute
	@XStreamAlias("CheckItemCode")
	private String checkItemCode;
	/** 作业工单代码 */
	@XStreamAlias("CheckWorkerCode")
	private String checkWorkerCode;
	/** 检查时间 */
	@XStreamAlias("CheckTime")
	private String checkTime;
	/** 检查结果*/
	@XStreamAlias("Result")
	private String result;
	/** 备注*/
	@XStreamAlias("Remark")
	private String remark;
	
	public String getCheckItemCode() {
		return checkItemCode;
	}
	public void setCheckItemCode(String checkItemCode) {
		this.checkItemCode = checkItemCode;
	}
	public String getCheckItemName() {
		return checkItemName;
	}
	public void setCheckItemName(String checkItemName) {
		this.checkItemName = checkItemName;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getCheckWorkerCode() {
		return checkWorkerCode;
	}
	public void setCheckWorkerCode(String checkWorkerCode) {
		this.checkWorkerCode = checkWorkerCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}