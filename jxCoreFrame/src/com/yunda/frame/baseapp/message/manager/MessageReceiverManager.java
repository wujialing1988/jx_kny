package com.yunda.frame.baseapp.message.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.frame.baseapp.message.entity.MessageReceiver;
import com.yunda.frame.baseapp.message.entity.MessageSender;
import com.yunda.frame.common.JXBaseManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MessageReceiver业务类,消息接收者表
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-07-22
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value = "messageReceiverManager")
public class MessageReceiverManager extends JXBaseManager<MessageReceiver, MessageReceiver> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** MessageSender业务类,消息记录发送者表 */
    @Resource
    private MessageSenderManager messageSenderManager;
    
    /** 消息被确认接收的备注信息 */
    public static final String  CONST_STR_MSG_REC_REMARK = "信息已接收";
    
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
    public void saveOrUpdate(List<MessageReceiver> entityList) {
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }
    
    /**
     * <li>说明：新增或更新实体对象
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-7-22
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param receiver 实体对象
     */
    @Override
    public void saveOrUpdate(MessageReceiver receiver) {
        this.daoUtils.getHibernateTemplate().saveOrUpdate(receiver);
    }
    
    /**
     * <li>说明：消息确认接收后的实体更新
     * <li>创建人：何涛
     * <li>创建日期：2015-3-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param idx 实体主键
     */
    public void confirmReceived(String idx) {
        MessageReceiver receiver = this.getModelById(idx);
        if (null == receiver) {
            throw new NullPointerException("数据异常，未找到主键[" + idx + "]的消息接收者！");
        }
        if (null != receiver.getReceiveTime() && CONST_STR_MSG_REC_REMARK.equals(receiver.getRemark())) {
            return;
        }
        this.confirmReceived(receiver);
    }
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：何涛
     * <li>创建日期：2015-03-12
     * <li>修改人：何涛
     * <li>修改日期：2015-03-25
     * <li>修改内容：增加同一个消息有多个人接收时的备注说明
     * @param receiver 实体对象
     */
    private void confirmReceived(MessageReceiver receiver) {
        // 设置接收时间
        Date receiveTime = Calendar.getInstance().getTime();
        receiver.setRemark(CONST_STR_MSG_REC_REMARK);           // 备注信息已接收
        receiver.setReceiveTime(receiveTime);                   // 设置消息接收时间
        this.saveOrUpdate(receiver);
        
        // 验证消息发送类型，如果是多个人必须一一接收，则不进行后续批量更新确认接收信息的处理
        MessageSender sender = this.messageSenderManager.getModelById(receiver.getMsgSendIDX());
        if (MessageSender.RECEIVER_TYPE_MANY_TO_ONE != sender.getReceiverType()) {
            return;
        }
        
        // 多个接收者仅需一个人接收时，同步更新其它未接收消息人员的消息接收时间
        List<MessageReceiver> list = this.getModelsByMsgSendIDX(receiver.getMsgSendIDX());
        if (null == list || 0 >= list.size()) {
            return;
        }
        
        // 针对多个接受者时，确认接收的批量更新
        List<MessageReceiver> entityList = new ArrayList<MessageReceiver>();
        
        for (MessageReceiver mr : list) {
            // 如果信息被相应的人员接收了，则不处理
            if (CONST_STR_MSG_REC_REMARK.equals(mr.getRemark())) {
                continue;
            }
            if (null != mr.getReceiveTime()) {
                String remark = mr.getRemark();
                String str1 = remark.substring(0, remark.lastIndexOf("】"));
                String str2 = remark.substring(remark.lastIndexOf("】"));
                StringBuilder sb = new StringBuilder();
                sb.append(str1).append("、").append(receiver.getReceiverName()).append(str2);
                mr.setRemark(sb.toString());
            } else {
                mr.setReceiveTime(receiveTime); // 消息接收时间
                mr.setRemark("消息已被【" + receiver.getReceiverName() + "】接收"); // 备注，说明该消息已被同组消息接收者的其他人接收
            }
            entityList.add(mr);
        }
        if (0 >= entityList.size()) {
            return;
        }
        this.saveOrUpdate(entityList);
    }
    
    /**
     * <li>说明：根据“消息记录及发送表主键”查询消息接收者实体列表
     * <li>创建人：何涛
     * <li>创建日期：2015-3-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param msgSendIDX 消息记录及发送表主键
     * @return 同一消息接收对象列表
     */
    @SuppressWarnings("unchecked")
    private List<MessageReceiver> getModelsByMsgSendIDX(String msgSendIDX) {
        String hql = "From MessageReceiver Where msgSendIDX = ?";
        return this.daoUtils.find(hql, new Object[] { msgSendIDX });
    }
    
}
