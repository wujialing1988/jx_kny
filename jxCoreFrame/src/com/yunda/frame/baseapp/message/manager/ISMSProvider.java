package com.yunda.frame.baseapp.message.manager;

import com.yunda.frame.yhgl.entity.OmEmployee;

/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 发送手机短信业务类
 * <br/><li>创建人：刘晓斌
 * <br/><li>创建日期：2013-8-16
 * <br/><li>修改人: 
 * <br/><li>修改日期：
 * <br/><li>修改内容：
 * <br/><li>版权: Copyright (c) 2008 运达科技公司
 * <br/><code><pre>
 * &nbsp; 
 * &nbsp; 示例代码：
 * &nbsp; 
 * &nbsp; //依赖注入：消息功能组件实例对象，不需要提供getter/setter方法
 * &nbsp; @Resource
 * &nbsp; private ISMSProvider smsManager;
 * &nbsp; 
 * &nbsp; //发送短信
 * &nbsp; smsManager.send("短信内容", employeeList); 
 * &nbsp; 
 * </pre></code> 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface ISMSProvider {

	/**
	 * <br/><li>说明：向数组中的所有人员发送短信
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-8-16
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容： 
	 * @param content 短信内容，最大长度62，注意：若包含非法字符（&<>%~/|^$#@等）则可能发送失败
	 * @param employees 接收人员数组
	 */
	public void send(String content, OmEmployee... employees);
	
	/**
	 * <br/><li>说明：向数组中的所有手机号码发送短信
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-8-16
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容： 
	 * @param content 短信内容，最大长度62，注意：若包含非法字符（&<>%~/|^$#@等）则可能发送失败
	 * @param phoneNumbers 手机号码数组
	 */
	public void send(String content, String... phoneNumbers);
}
