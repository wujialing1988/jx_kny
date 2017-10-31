package com.yunda.frame.baseapp.message;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.common.BusinessException;
import com.yunda.frame.baseapp.message.entity.MessageReceiver;
import com.yunda.frame.baseapp.message.entity.MessageSender;
import com.yunda.frame.baseapp.message.manager.MessageReceiverManager;
import com.yunda.frame.baseapp.message.manager.MessageSenderManager;
import com.yunda.frame.baseapp.todojob.entity.TodoJob;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.LogonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.frame.yhgl.manager.TodoJobFunction;
import com.yunda.jxpz.systemconfig.entity.SystemConfig;
import com.yunda.jxpz.systemconfig.manager.SystemConfigManager;
import com.yunda.util.PurviewUtil;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 消息推送webservice服务
 * <li>创建人：何涛
 * <li>创建日期：2015-3-4
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value="messagePropellingWS")
public class MessagePropellingService implements IMessagePropellingService {

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    /** 系统操作员服务类 */
    @Resource
    private AcOperatorManager acOperatorManager;
    
    /** 员工服务类 */
    @Resource
    private OmEmployeeManager omEmployeeManager;
    
    /** 机构服务类 */
    @Resource
    private OmOrganizationManager omOrganizationManager;
    
    /** SystemConfig业务类 系统配置项 */
    @Resource
    private SystemConfigManager systemConfigManager;
    
    /** 消息推送有效时间配置键值 */
    private final String messagValidTimeKey = "ck.frame.message.validTime";
    
    /** 服务器端口配置键值 */
//    private final String serverPortKey = "ck.frame.server.port";
    
    
    /** MessageSender业务类,消息记录发送者表 */
    @Resource
    private MessageSenderManager messageSenderManager;
    
    /** MessageReceiver业务类,消息接收者表 */
    @Resource
    private MessageReceiverManager messageReceiverManager;
    
    /**
     * <li>说明：登录
     * <li>创建人：何涛
     * <li>创建日期：2015-3-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            userId: "wangqian",
            password: "000000",
        }
     * @return {
            orgId:”106”,
            orgCode:”106”,
            orgName:”电检一组”,
            orgLevel:”3”,
            orgDegree:”tream”,
            orgSeq:” .0.1.106.”,
            webAccessAddress:”http://10.2.4.114/CoreFrame”,
            userId:”wangqian”,
            operatorId:”800109”,
            operatorName:”王谦”,
            empId:”109”
        }
     * @throws IOException
     */
    public String login(String jsonObject) throws IOException {
        OperateReturnMessage msg = new OperateReturnMessage();
        AcOperator operator = null;
        try {
            if (StringUtil.isNullOrBlank(jsonObject)) {
                throw new NullPointerException(MSG_ERROR_ARGS_NULL);
            }
            JSONObject jObject = JSONObject.parseObject(jsonObject);
            String userId = jObject.getString("userId");
            String password = jObject.getString("password");
            
            // 查询操作员对象
            operator = acOperatorManager.findLoginAcOprator(userId, LogonUtil
                    .getPassword(password));
            if (operator == null) {
                throw new NullPointerException("用户名或密码错误！");
            }
            if (PurviewUtil.isSuperUsers(operator)) {
                throw new BusinessException("超级管理员登录");
            }
            JSONObject json = new JSONObject();
            OmEmployee employee = omEmployeeManager.findByOperator(operator.getOperatorid());
            OmOrganization organization = omOrganizationManager.getModelById(employee.getOrgid());
//            orgId:”106”,
            json.put("orgId", organization.getOrgid());
//            orgCode:”106”,
            json.put("orgCode", organization.getOrgcode());
//            orgName:”电检一组”,
            json.put("orgName", organization.getOrgname());
//            orgLevel:”3”,
            json.put("orgLevel", organization.getOrglevel());
//            orgDegree:”tream”,
            json.put("orgDegree", organization.getOrgdegree());
//            orgSeq:” .0.1.106.”,
            json.put("orgSeq", organization.getOrgseq());
//            webAccessAddress:”http://10.2.4.114/CoreFrame”,
            json.put("webAccessAddress", this.getWebAccessAddress());
//            userId:”wangqian”,
            json.put("userId", userId);
//            operatorId:”800109”,
            json.put("operatorId", operator.getOperatorid());
//            operatorName:”王谦”,
            json.put("operatorName", operator.getOperatorname());
//            empId:”109”
            json.put("empId", employee.getEmpid());
            return json.toJSONString();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
    
    /**
     * <li>说明：请求消息
     * <li>创建人：何涛
     * <li>创建日期：2015-3-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return {
            idx:” 8a8284644844f63b014844f8a4860001”,
            title:” 系统消息”,
            sender:” 10091”,
            senderName:”张三”,
            sendMode:”1”,
            sendTime:”2015/03/04 10:00:00”,
            content:”你有新的消息”,
            pageTitle:””,
            url:””,
            showPageMode:”1”,
            receiverArray:[{
                idx:” 8a8284fc49a84a050149a8784e390111”,
                msgSendIDX:” 8a8284644844f63b014844f8a4860001”,
                receiver:” 10258”,
                receiverName:”李四”,
                receiveTime:null
            },{
                idx:” 8a8284fc49a84a050149a8784e390117”, 
                msgSendIDX:” 8a8284644844f63b014844f8a4860001”,
                receiver:” 10273”,
                receiverName:”王五”, 
                receiveTime:null
            }]
        }
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public String requestMessage () throws IOException{
        // 获取系统配置的消息过期时间
        int validTime = 48;
        SystemConfig sc = this.systemConfigManager.getModelById(this.messagValidTimeKey);
        if (null != sc || null != sc.getKeyValue()) {
            validTime = Integer.parseInt(sc.getKeyValue());
        }
        
        Collection<MessageReceiver> collection = this.messageReceiverManager.find("From MessageReceiver Where receiveTime Is Null");
        // 根据消息主键，对消息接收者进行分组
        Map<String, List<MessageReceiver>> map = new HashMap<String, List<MessageReceiver>>();
        for (MessageReceiver mr : collection) {
            List<MessageReceiver> list = map.get(mr.getMsgSendIDX());
            if (null == list) {
                list = new ArrayList<MessageReceiver>();
                map.put(mr.getMsgSendIDX(), list);
            } 
            list.add(mr);
        }
        // 遍历map，构造消息的JSON对象
        List<MessageSender> list = new ArrayList<MessageSender>();
        Set<Entry<String, List<MessageReceiver>>> set = map.entrySet();
        for (Iterator<Entry<String, List<MessageReceiver>>> i = set.iterator(); i.hasNext(); ) {
            Entry<String, List<MessageReceiver>> entry = i.next();
            MessageSender ms = this.messageSenderManager.getModelById(entry.getKey());
            // 验证消息是否已经过期
            if (null == ms || !ms.isValid(validTime)) {
                continue;
            }
            ms.setReceiverArray(entry.getValue());
            list.add(ms);
        }
        logger.info("共发送(" + list.size() + ")条系统消息");
        // 设置日期的输出格式为 yyyy-MM-dd HH:mm:ss
        ObjectMapper om = new ObjectMapper();
        om.getSerializationConfig().setDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);
        om.getDeserializationConfig().setDateFormat(DateUtil.yyyy_MM_dd_HH_mm_ss);
        return om.writeValueAsString(list);
    }
    
    /**
     * <li>说明：确认接收
     * <li>创建人：何涛
     * <li>创建日期：2015-3-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            idx:”8a8284f24be2e8b2014be2ea598f0002”
        }
     * @return 
     * <li>"{'flag':'true','message':'操作成功！'}";
     * <li>"{'flag':'false','message':'操作失败！'}"
     * @throws IOException
     */
    public String confirmReceived (String jsonObject) throws IOException {
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            if (StringUtil.isNullOrBlank(jsonObject)) {
                throw new NullPointerException(MSG_ERROR_ARGS_NULL);
            }
            JSONObject jObject = JSONObject.parseObject(jsonObject);
            String idx = jObject.getString("idx");
            
            this.messageReceiverManager.confirmReceived(idx);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
    
    /**
     * <li>说明：获取当前所有登录人员的待办事宜列表
     * <li>创建人：程锐
     * <li>创建日期：2015-7-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorIDS 以,号分隔的所有登录人ID字符串
     * @return 当前所有登录人员的待办事宜列表
     * {
            "800109" : [{
                        "jobNum" : "97",
                        "jobText" : "未派工(97)",
                        "jobType" : "工长派工",
                        "jobUrl" : "/jsp/jx/scdd/dispatch/foreman.jsp"
                    }, {
                        "jobNum" : "1",
                        "jobText" : "未处理(1)",
                        "jobType" : "提票处理",
                        "jobUrl" : "/jsp/jx/jxgc/tpmanage/FaultTicketHandle.jsp"
                    }, {
                        "jobNum" : "3",
                        "jobText" : "未派工(3)",
                        "jobType" : "提票调度派工",
                        "jobUrl" : "/jsp/jx/jxgc/tpmanage/FaultTicketDdpg.jsp"
                    }, {
                        "jobNum" : "3",
                        "jobText" : "未派工(3)",
                        "jobType" : "提票工长派工",
                        "jobUrl" : "/jsp/jx/jxgc/tpmanage/FaultTicketGzpg.jsp"
                    }, {
                        "jobNum" : "6",
                        "jobText" : "未处理(6)",
                        "jobType" : "提票质量检查",
                        "jobUrl" : "/jsp/jx/jxgc/tpmanage/FaultQCResult.jsp"
                    }, {
                        "jobNum" : "6",
                        "jobText" : "待处理（6）",
                        "jobType" : "作业工单",
                        "jobUrl" : "/jsp/jx/jxgc/WorkTask/WorkTask.jsp"
                    }]
        }
     * @throws IOException
     */
    public String getTodoJobList(String operatorIDS) throws IOException {
        OperateReturnMessage msg = new OperateReturnMessage();
        Map<String, List<TodoJob>> map = new HashMap<String, List<TodoJob>>();
        try {
            if (StringUtil.isNullOrBlank(operatorIDS)) {
                throw new NullPointerException(MSG_ERROR_ARGS_NULL);
            }
            String[] operatorIDArray = StringUtil.tokenizer(operatorIDS, Constants.JOINSTR);
            TodoJobFunction todoJobFunction = TodoJobFunction.getInstance();
            for (String operatorId : operatorIDArray) {
                List<TodoJob> list = todoJobFunction.getToDoListContext(operatorId);
                if (list == null || list.size() < 1) {
                    TodoJob job = new TodoJob();
                    job.setJobNum("0");
                    list = new ArrayList<TodoJob>();
                    list.add(job);
                }
                map.put(operatorId, list);
            }
            
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(map);
    }
    
    /**
     * <li>说明：获取web应用访问路径
     * <li>创建人：何涛
     * <li>创建日期：2015-3-6
     * <li>修改人：程锐
     * <li>修改日期：2015-9-21
     * <li>修改内容：修改为从jxconfig配置文件获取web应用访问路径
     * @return web应用访问路径
     * @throws UnknownHostException 
     */
    private String getWebAccessAddress() throws UnknownHostException {
//        InetAddress localHost = InetAddress.getLocalHost();
//        String hostAddress = localHost.getHostAddress();
//        String serverPort = this.systemConfigManager.getModelById(serverPortKey).getKeyValue();
//        if (null == serverPort || serverPort.trim().length() <= 0) {
//            serverPort = "8080";
//        }
//        StringBuilder sb = new StringBuilder("http://");
//        sb.append(hostAddress).append(":").append(serverPort).append("/");
//        sb.append("CoreFrame");
//        return sb.toString();
        String url = JXConfig.getInstance().getAppURL();
        return url.endsWith("/") ? url.substring(0, url.length() -1) : url;        
    }
    
//    /**
//     * <li>说明：获取当前所有登录人员的待办事宜列表(只针对ZB系统)
//     * <li>创建人：林欢
//     * <li>创建日期：2016-4-20
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @param operatorIDS 以,号分隔的所有登录人ID字符串
//     * @return 当前所有登录人员的待办事宜列表
//     * {
//            "800109" : [{
//                        "jobNum" : "97",
//                        "jobText" : "未派工(97)",
//                        "jobType" : "工长派工",
//                        "jobUrl" : "/jsp/jx/scdd/dispatch/foreman.jsp"
//                    }, {
//                        "jobNum" : "1",
//                        "jobText" : "未处理(1)",
//                        "jobType" : "提票处理",
//                        "jobUrl" : "/jsp/jx/jxgc/tpmanage/FaultTicketHandle.jsp"
//                    }, {
//                        "jobNum" : "3",
//                        "jobText" : "未派工(3)",
//                        "jobType" : "提票调度派工",
//                        "jobUrl" : "/jsp/jx/jxgc/tpmanage/FaultTicketDdpg.jsp"
//                    }, {
//                        "jobNum" : "3",
//                        "jobText" : "未派工(3)",
//                        "jobType" : "提票工长派工",
//                        "jobUrl" : "/jsp/jx/jxgc/tpmanage/FaultTicketGzpg.jsp"
//                    }, {
//                        "jobNum" : "6",
//                        "jobText" : "未处理(6)",
//                        "jobType" : "提票质量检查",
//                        "jobUrl" : "/jsp/jx/jxgc/tpmanage/FaultQCResult.jsp"
//                    }, {
//                        "jobNum" : "6",
//                        "jobText" : "待处理（6）",
//                        "jobType" : "作业工单",
//                        "jobUrl" : "/jsp/jx/jxgc/WorkTask/WorkTask.jsp"
//                    }]
//        }
//     * @throws IOException
//     */
//    public String getZBTodoJobList(String operatorIDS) throws IOException {
//        OperateReturnMessage msg = new OperateReturnMessage();
//        Map<String, List<TodoJob>> map = new HashMap<String, List<TodoJob>>();
//        try {
//            if (StringUtil.isNullOrBlank(operatorIDS)) {
//                throw new NullPointerException(MSG_ERROR_ARGS_NULL);
//            }
//            String[] operatorIDArray = StringUtil.tokenizer(operatorIDS, Constants.JOINSTR);
//            TodoJobFunction todoJobFunction = TodoJobFunction.getInstance();
//            for (String operatorId : operatorIDArray) {
//                List<TodoJob> list = todoJobFunction.getZBToDoListContext(operatorId);
//                if (list == null || list.size() < 1) {
//                    TodoJob job = new TodoJob();
//                    job.setJobNum("0");
//                    list = new ArrayList<TodoJob>();
//                    list.add(job);
//                }
//                map.put(operatorId, list);
//            }
//            
//        } catch (Exception e) {
//            ExceptionUtil.process(e, logger);
//            msg.setFaildFlag(e.getMessage());
//        }
//        return JSONUtil.write(map);
//    }
}
