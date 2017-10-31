package com.yunda.jx.wlgl;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：IBillStatus单据状态接口
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-8 下午02:34:10
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IBillStatus {
	
	/** 单据状态 - 暂存 */
	public static final String CONST_STR_STATUS_ZC = "temporary";
	/** 单据状态 - 登账 */
	public static final String CONST_STR_STATUS_DZ = "entryAccount";
	/** 单据状态 - 所有 */
	public static final String CONST_STR_STATUS_SY = "all";

}
