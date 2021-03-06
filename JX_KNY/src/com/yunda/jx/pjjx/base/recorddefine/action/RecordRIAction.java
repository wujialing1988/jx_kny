package com.yunda.jx.pjjx.base.recorddefine.action; 

import org.apache.log4j.Logger;

import com.yunda.frame.order.AbstractOrderAction;
import com.yunda.jx.pjjx.base.recorddefine.entity.RecordRI;
import com.yunda.jx.pjjx.base.recorddefine.manager.RecordRIManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：RecordRI控制器, 检修检测项
 * <li>创建人：何涛
 * <li>创建日期：2014-11-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class RecordRIAction extends AbstractOrderAction<RecordRI, RecordRI, RecordRIManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
}