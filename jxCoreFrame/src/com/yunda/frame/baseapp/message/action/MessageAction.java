package com.yunda.frame.baseapp.message.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.baseapp.message.entity.Message;
import com.yunda.frame.baseapp.message.manager.MessageManager;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：Message控制器, 系统消息记录表
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-06-04
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class MessageAction extends JXBaseAction<Message, Message, MessageManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：请求发送在线即时消息
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void sendOnlineMsg() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			Message message = (Message)JSONUtil.read(getRequest(), entity.getClass());
			OmEmployee receivEmp = new OmEmployee();
			receivEmp.setEmpid(Long.valueOf(message.getReceiver()));
			receivEmp.setEmpname(message.getReceiverName());
			this.manager.send(receivEmp, message.getContent());
//			返回记录保存成功的实体对象
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}	
	/**
	 * <li>说明：请求获取当前操作员尚未接收的消息记录
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return viod
	 * @throws Exception
	 */
	public void getNoReceivedMsg() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			if(!JXConfig.getInstance().isOnlineMessageEnable()){
				map.put("success", false);
				return;
			}
			List<Message> list = manager.getNoReceivedMsg();
			map = Page.extjsStore(list);
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	/**
	 * <li>说明：通知服务端已接收消息，设置消息接收时间
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-6-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("all")
	public void received() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			this.manager.received(ids);
			map.put("success", true);			
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
}