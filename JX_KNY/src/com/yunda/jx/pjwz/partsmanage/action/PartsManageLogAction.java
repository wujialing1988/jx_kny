package com.yunda.jx.pjwz.partsmanage.action; 

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.jx.pjwz.partsmanage.entity.PartsManageLog;
import com.yunda.jx.pjwz.partsmanage.manager.PartsManageLogManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：PartsManageLog控制器, 配件管理日志
 * <li>创建人：程梅
 * <li>创建日期：2015-10-16
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class PartsManageLogAction extends JXBaseAction<PartsManageLog, PartsManageLog, PartsManageLogManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
}