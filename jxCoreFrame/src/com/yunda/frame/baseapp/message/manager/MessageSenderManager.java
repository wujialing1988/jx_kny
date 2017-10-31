package com.yunda.frame.baseapp.message.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.frame.baseapp.message.entity.MessageReceiver;
import com.yunda.frame.baseapp.message.entity.MessageSender;
import com.yunda.frame.common.JXBaseManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MessageSender业务类,消息记录发送者表
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-07-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
@Service(value="messageSenderManager")
public class MessageSenderManager extends JXBaseManager<MessageSender, MessageSender>{
	/** 日志工具 */
//	@SuppressWarnings("unused")
//	private Logger logger = Logger.getLogger(getClass().getName());
    
    /** MessageReceiver业务类,消息接收者表 */
    @Resource
    private  MessageReceiverManager messageReceiverManager;
	
	/**
	 * <li>说明：新增或更新一组实体对象
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-22
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entityList 实体类对象集合 
	 */	
	@Override
	public void saveOrUpdate(List<MessageSender> entityList) {
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
	}
	/**
	 * <li>说明：新增或更新实体对象
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-7-22
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param sender 实体对象
	 */	
	@Override
	public void saveOrUpdate(MessageSender sender) {
		this.daoUtils.getHibernateTemplate().saveOrUpdate(sender);
	}
    
    /**
     * <li>说明：针对手动设置消息接收者时的存储
     * <li>创建人：何涛
     * <li>创建日期：2015-3-10
     * <li>修改人：何涛
     * <li>修改日期：2015-03-19
     * <li>修改内容：增加一个临时列表，用于去除重复的消息接收者实体
     * @param sender 包含消息接受者信息的消息发送实体
     */
    public void saveMessage(MessageSender sender) {
        // 针对手动设置消息接受者时的存储
        List<MessageReceiver> receiverArray = sender.getReceiverArray();
        if (null == receiverArray || receiverArray.size() <= 0) {
            return;
        }
        this.saveOrUpdate ( sender);
        
        // 记录消息接收者的id，用于去除可能重复的消息消息
        List<String> temp = new ArrayList<String>(receiverArray.size());
        // 设置消息记录及发送表主键
        for (MessageReceiver mr : receiverArray) {
            if (temp.contains(mr.getReceiver())) {
                receiverArray.remove(mr);
                continue;
            }
            temp.add(mr.getReceiver());
            mr.setMsgSendIDX(sender.getIdx());
        }
        this.messageReceiverManager.saveOrUpdate(receiverArray);
    }	
    
}