/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-7-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */

package com.yunda.frame.baseapp.message.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunda.frame.baseapp.message.entity.Message;
import com.yunda.frame.baseapp.message.entity.MessageReceiver;
import com.yunda.frame.baseapp.message.entity.MessageSender;
import com.yunda.frame.common.JXBaseManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 在线消息发送服务提供者
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-7-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="onlineMessageProvider")
public class OnlineMessageProvider extends JXBaseManager<Message, Message>{
	/** 消息记录发送者业务类 */
	private MessageSenderManager messageSenderManager;
	/** 消息记录接收者业务类 */
	private MessageReceiverManager messageReceiverManager;

	/**
	 * <li>说明：向集合对象的所有人员发送消息
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容： 
	 * @param MessageSender sender 消息发送者
	 * @param Collection<MessageReceiver> receivers 消息接收者集合
	 * @param String content 消息内容
	 * @return void
	 */
	@Transactional
	public void send(MessageSender sender, Collection<MessageReceiver> receivers){
		messageSenderManager.save(sender);
		for (MessageReceiver r : receivers){
			r.setMsgSendIDX(sender.getIdx());
		}
		List<MessageReceiver> list = new ArrayList<MessageReceiver>();
		list.addAll(receivers);
		messageReceiverManager.saveOrUpdate(list);
	}
	
	public MessageReceiverManager getMessageReceiverManager() {
		return messageReceiverManager;
	}
	public void setMessageReceiverManager(
			MessageReceiverManager messageReceiverManager) {
		this.messageReceiverManager = messageReceiverManager;
	}
	public MessageSenderManager getMessageSenderManager() {
		return messageSenderManager;
	}
	public void setMessageSenderManager(MessageSenderManager messageSenderManager) {
		this.messageSenderManager = messageSenderManager;
	}
}