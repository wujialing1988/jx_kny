package com.yunda.sb.repair.process.manager;

import java.io.Serializable;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: IStateWatcher，检修范围实例状态更新观察者
 * <li>创建人：何涛
 * <li>创建日期：2017年4月14日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
public interface IStateWatcher {
	
	/**
	 * <li>说明：状态更新
	 * <li>创建人：何涛
	 * <li>创建日期：2017年4月14日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param taskListIdx 检修任务单idx主键
	 * @param repairType 检修类型
	 */
	public void updateState(Serializable taskListIdx, Integer repairType);

}
