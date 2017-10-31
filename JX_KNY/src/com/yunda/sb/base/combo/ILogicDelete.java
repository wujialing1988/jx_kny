package com.yunda.sb.base.combo;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: ILogicDelete，数据库实体逻辑删除对象接口
 * <li>创建人：何涛
 * <li>创建日期：2016年10月9日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
public interface ILogicDelete {

	/**
	 * <li>说明：获取数据状态，0：正常，1：逻辑删除
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @return 数据状态，0：正常，1：逻辑删除
	 */
	public Integer getRecordStatus();

	/**
	 * <li>说明：设置数据状态，0：正常，1：逻辑删除
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月9日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param recordStatus 数据状态，0：正常，1：逻辑删除
	 */
	public void setRecordStatus(Integer recordStatus);
	
}
