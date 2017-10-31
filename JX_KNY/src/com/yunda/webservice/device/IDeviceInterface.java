package com.yunda.webservice.device;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 定义机务设备接口
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-12-25
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IDeviceInterface {
	/**
	 * <li>说明：机务设备请求检修系统下传数据服务接口（推荐采用WebService方式实现）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-25
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String XMLData: 请求参数，XML格式的字符串
	 * @return String: 返回处理结果的XML格式的字符串
	 * @throws 
	 */
	public String getData(String XMLData);
	/**
	 * <li>说明：机务设备向检修系统上传数据服务接口（推荐采用WebService方式实现）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-12-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String XMLData: 请求参数，XML格式的字符串
	 * @return int: 返回调用结果
	 * <li>	0接口方法调用成功，数据操作接受。
	 * <li>	1接口方法调用成功，数据操作失败。
	 * <li>	2接口方法调用成功，无效或未知的设备。
	 * <li>	3接口方法调用成功，无效的XML数据类型。
	 * <li>	9接口方法调用失败，未知错误。	
	 * @throws
	 */
	public int sendData(String XMLData);
	/**
	 * <li>说明：机车检修管理系统为机务设备系统（或其他外部系统）提供根据查询条件查询未处理完的配件生产计划的接口服务。
	 * <li>创建人：王治龙
	 * <li>创建日期：2014-6-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param XMLData： 请求参数，XML格式的字符串
	 * @return String :返回处理结果的XML格式的字符串
	 * @throws
	 */
	public String getPartRepairPlan(String XMLData) ;
}
