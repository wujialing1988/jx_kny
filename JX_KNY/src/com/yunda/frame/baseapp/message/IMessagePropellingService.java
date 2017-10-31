package com.yunda.frame.baseapp.message;

import java.io.IOException;

import com.yunda.jx.pjjx.webservice.IService;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 消息推送webservice服务接口
 * <li>创建人：何涛
 * <li>创建日期：2015-3-4
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface IMessagePropellingService extends IService {
    
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
    public String login(String jsonObject) throws IOException;
    
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
    public String requestMessage () throws IOException;
    
    /**
     * <li>说明：确认接收
     * <li>创建人：何涛
     * <li>创建日期：2015-3-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            idx:”8a8284fc49a84a050149a8784e390111”
        }
     * @return 
     * <li>"{'flag':'true','message':'操作成功！'}";
     * <li>"{'flag':'false','message':'操作失败！'}"
     * @throws IOException
     */
    public String confirmReceived (String jsonObject) throws IOException;
    
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
    public String getTodoJobList(String operatorIDS) throws IOException;
    
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
//    public String getZBTodoJobList(String operatorIDS) throws IOException;
    
}
