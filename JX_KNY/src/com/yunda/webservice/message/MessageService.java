package com.yunda.webservice.message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.httpclient.util.URIUtil;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.flow.snaker.util.SnakeHttpClientUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.util.HttpClientUtils;
import com.yunda.webservice.message.entity.MessageEntityHis;
import com.yunda.webservice.message.manager.MessageEntityHisManager;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 消息发送服务实现
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-7
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("messageService")
public class MessageService implements IMessageService {
    
    /**
     * 调用URL
     */
    private static String BASE_URL = "" ;
    
    static{
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream("webservice.properties");
            p.load(is);
            is.close();
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        BASE_URL = p.getProperty("dingding_url");
    }
    
    /** 消息历史记录 */
    @Resource
    private MessageEntityHisManager messageEntityHisManager ;

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
     * @throws Exception 
     */
    public String sendTpMsg(String title, String content, String receiver,String msgType) throws Exception {
        System.err.println(new Date()+"消息标题："+title+",消息内容："+content+",接受者："+receiver);
        // 调用中间服务
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("content", URIUtil.encodeQuery(content));
        params.put("receiver", receiver);
        String returnMsg = HttpClientUtils.instence.getCloseableHttpClientInstence(BASE_URL, params);
            // SnakeHttpClientUtil.doPost(BASE_URL, params);
        System.err.println(returnMsg);
        // 保存发送记录
        MessageEntityHis his = new MessageEntityHis();
        his.setKeyWord(title);
        his.setContent(content);
        his.setReceiver(receiver);
        his.setMsgType(msgType);
        his.setReturnMsg(returnMsg);
        if(StringUtil.isNullOrBlank(returnMsg) || !"true".equals(returnMsg)){
            his.setIsSuccess(false);
        }
        messageEntityHisManager.saveOrUpdate(his);
        return "sccess";
    }
    
}
