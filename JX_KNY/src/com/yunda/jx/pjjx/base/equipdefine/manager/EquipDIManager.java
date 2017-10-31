package com.yunda.jx.pjjx.base.equipdefine.manager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.jx.pjjx.base.equipdefine.entity.EquipDI;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：EquipDI业务类,机务设备检测数据项定义
 * <li>创建人：程梅
 * <li>创建日期：2015-01-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="equipDIManager")
public class EquipDIManager extends JXBaseManager<EquipDI, EquipDI>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
}