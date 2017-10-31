package com.yunda.jx.jxgc.dispatchmanage.webservice;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 工位绑定接口
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-7-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IZbglWorkStationBindingService {
	
	/**
	 * <li>说明：根据操作员ID获取人员绑定的工位信息
	 * <li>创建人：伍佳灵
	 * <li>创建日期：2016-7-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operatIDX
	 * @return
	 */
	public String getBindingWorkStationByOperatIDX(String operatIDX);
}
