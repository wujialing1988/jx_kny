package com.yunda.sb.equipmentprimaryinfo.action;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.JSONUtil;
import com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo;
import com.yunda.sb.equipmentprimaryinfo.manager.EquipmentPrimaryInfoManager;

/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明： 设备主要信息控制器
 * <li>创建人：黄杨
 * <li>创建日期：2017-4-26
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class EquipmentPrimaryInfoAction extends JXBaseAction<EquipmentPrimaryInfo, EquipmentPrimaryInfo, EquipmentPrimaryInfoManager> {

	/** 日志工具 */
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * <li>说明： 获取同类型下最大的设备编号
	 * <li>创建人： 黄杨
	 * <li>创建日期：2017-5-2
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	public void maxEquipmentCode() throws JsonMappingException, IOException {
		String classCode = getRequest().getParameter("classCode");
		String equipmentCode = this.manager.findMaxEquipmentCode(classCode);
		JSONUtil.write(getResponse(), equipmentCode);
	}

}
