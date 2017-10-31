package com.yunda.webservice.systemconfig;

import org.springframework.stereotype.Service;

import com.yunda.jxpz.utils.SystemConfigUtil;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统配置项服务
 * <li>创建人：程锐
 * <li>创建日期：2014-8-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="systemConfigService")
public class SystemConfigService implements ISystemConfigService {
    
	/**
	 * <li>说明：获取系统配置项值
	 * <li>创建人：程锐
	 * <li>创建日期：2014-8-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param configItemkey 系统配置项键值
	 * @return 系统配置项值
	 */
	public String getSystemConfig(String configItemkey) {
		return SystemConfigUtil.getValue(configItemkey);
	}

}
