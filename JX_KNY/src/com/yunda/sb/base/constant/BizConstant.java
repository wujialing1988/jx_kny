package com.yunda.sb.base.constant;

import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：BizConstant，系统业务常量定义接口
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
public interface BizConstant {

	/** 审批通过 */
	int VERIFY_RESULT_YES = 1;

	/**  审批未通过 */
	int VERIFY_RESULT_NO = 2;

	/** 审批中 */
	int VERIFY_RESULT_WAIT = 3;

	/** 计划状态 - 完成 */
	int PLAN_STATUS_COMPLATE = 3;

	/** 计划状态 - 进行中 */
	int PLAN_STATUS_GOING = 2;

	/** 计划状态 － 计划中 */
	int PLAN_STATUS_PLAN = 1;

	/** 固资起值（满足此值才在主设备中显示） */
	float FIXED_ASSET_VALUE = 5000;

	/**
	 * 主设备动态过滤
	 * Modified by hetao on 2017-01-20 增加数组成员常量EquipmentPrimaryInfo.DYNAMIC_OUT
	 * 因为2.0系统设备主要信息管理页面查询动态包含【新购、调出、调入】
	 */
	int[] PRIMARY_DYNAMIC_FILTER = { EquipmentPrimaryInfo.DYNAMIC_IN, EquipmentPrimaryInfo.DYNAMIC_OUT, EquipmentPrimaryInfo.DYNAMIC_NEW_BUY };

}