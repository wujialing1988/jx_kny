package com.yunda.webservice.device.entity;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 检修系统与终端设备接口，上传、下传数据交互以XML文本格式传输，
 * 			该类描述XML中的QualityCheckList元素，表示参数代码 （参数代码参考参数字典）
 * <li>创建人：王治龙
 * <li>创建日期：2012-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@XStreamAlias("QualityCheckList")
//@XStreamConverter(AttributeValueConveter.class)
public class QualityCheckList{
	/** 参数名称 */
	@XStreamAlias("State")
	private String state;
	/** 质量检查项目  */
	@XStreamImplicit(itemFieldName="QualityCheckItem")
	private List<QualityCheckItem> qualityCheckList;
	
	
	public List<QualityCheckItem> getQualityCheckList() {
		return qualityCheckList;
	}
	public void setQualityCheckList(List<QualityCheckItem> qualityCheckList) {
		this.qualityCheckList = qualityCheckList;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}