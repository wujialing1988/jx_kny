package com.yunda.webservice.message;

import com.yunda.common.BusinessException;
import com.yunda.jx.pjjx.webservice.IService;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 消息发送服务
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-7
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IMessageService extends IService {
    
    /**
     * <li>说明：发送提票消息
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param title 消息标题
     * @param content 消息内容
     * @param receiver 接受者（传以英文逗号分隔的手机号码） 
     * @param msgType 消息类型
     * @return
     */
    public String sendTpMsg(String title,String content,String receiver,String msgType) throws Exception; 
    
    
}
