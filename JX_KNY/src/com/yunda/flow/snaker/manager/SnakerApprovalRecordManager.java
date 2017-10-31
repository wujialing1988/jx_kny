package com.yunda.flow.snaker.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.flow.snaker.entity.SnakerApprovalRecord;
import com.yunda.flow.snaker.entity.WorkItem;
import com.yunda.flow.snaker.entity.WsCommonResponse;
import com.yunda.flow.snaker.util.SnakeHttpClientUtil;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 流程审批记录业务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-9-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service("snakerApprovalRecordManager")
public class SnakerApprovalRecordManager extends JXBaseManager<SnakerApprovalRecord, SnakerApprovalRecord> {
    
    private static final Logger log = Logger.getLogger(SnakerApprovalRecordManager.class);
    
    // snaker流程部署地址
    private static String SNAKER_URL = "snaker_url" ;
    
    // 流程调用基础路径
    public static String BASE_URL = "" ;
    
    // 创建流程
    public static String CREATE_METHOD = "" ;
    
    // 审批流程
    public static String APPROVAL_METHOD = "" ;
    
    // 拒绝流
    public static String REFUSE_METHOD = "" ;
    
    // 驳回流程
    public static String REJECT_METHOD = "" ;
    
    // 获取流程任务列表
    public static String GET_PROCESS_WORK_METHOD = "" ;
    
    // 获取流程图参数
    public static String GET_PROCESS_DIAGRAM_METHOD = "" ;
    
    // 查询流程图中任务节点提示信息
    public static String GET_PROCESS_NODETIP_METHOD = "" ;
    
    
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
        BASE_URL = p.getProperty(SNAKER_URL);
        CREATE_METHOD = BASE_URL+"/ws/snaker/flow/startUp" ;
        APPROVAL_METHOD = BASE_URL+"/ws/snaker/task/execute"; 
        REFUSE_METHOD = BASE_URL+"/ws/snaker/flow/terminate" ;
        REJECT_METHOD = BASE_URL+"/ws/snaker/task/reject";
        GET_PROCESS_WORK_METHOD = BASE_URL+"/ws/snaker/task/activeTasks" ;
        GET_PROCESS_DIAGRAM_METHOD = BASE_URL+"/ws/snaker/process/flowDiagram" ;
        GET_PROCESS_NODETIP_METHOD = BASE_URL+"/ws/snaker/process/nodeTip" ;
    }
    
    /**
     * <li>说明：保存审批意见
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param businessID 业务ID
     * @param processInstID 流程实例ID
     * @param taskID 任务ID
     * @param taskName 任务名称
     * @param opinionType 意见类型
     * @param opinions 意见内容
     */
    public void createApprovalRecord(String businessID,String processInstID,String taskID,String taskName,String opinionType,String opinions){
        // 当前用户
        AcOperator operator = SystemContext.getAcOperator();
        SnakerApprovalRecord record = new SnakerApprovalRecord();
        record.setApprovalDate(new Date());
        record.setApprovalUserID(operator.getOperatorid()+"");
        record.setApprovalUserName(operator.getOperatorname());
        record.setBusinessID(businessID);
        record.setProcessInstID(processInstID);
        record.setTaskID(taskID);
        record.setTaskName(taskName);
        record.setOpinionType(opinionType);
        record.setOpinions(opinions);
        this.save(record);
    }
    
    // snaker流程
    
    /**
     * <li>说明：发起流程（提交流程）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processDefName 流程模板定义名称
     * @param processInstName 流程实例名称
     * @return processInstId 流程实例ID
     * @throws BusinessException 
     */
    public String startProcess(String processDefName, String processInstName , String operator) throws BusinessException{
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("processName", processDefName);
            params.put("processInstName", processInstName);
            params.put("operator", operator);
            String result = SnakeHttpClientUtil.doGet(CREATE_METHOD, params);
            if(StringUtil.isNullOrBlank(result)){
                throw new BusinessException("流程提交失败，数据返回异常！");
            }
            WsCommonResponse response = JSONUtil.read(result, WsCommonResponse.class);
            if(StringUtil.isNullOrBlank(response.getOrderId())){
                throw new BusinessException("流程提交失败，数据返回异常！");
            }
            return response.getOrderId();
        } catch (Exception e) {
            throw new BusinessException("流程提交失败！");
        }
    }
    
    /**
     * <li>说明：流程审批(同意)
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processInstID 流程实例ID
     * @param workId 任务ID
     * @return Boolean true 审批完成 
     * @throws BusinessException
     */
    public Boolean approvalProcessSnaker(String processInstID,String workId) throws BusinessException{
        try {
            Map<String, String> params = new HashMap<String, String>();
            AcOperator operator = SystemContext.getAcOperator();
            params.put("processInstID", processInstID);
            params.put("taskId", workId);
            params.put("operator", operator.getUserid());
            String result = SnakeHttpClientUtil.doGet(APPROVAL_METHOD, params);
            WsCommonResponse response = JSONUtil.read(result, WsCommonResponse.class);
            if(response.isComplete()){
                return true ;
            }
            return false ;
        } catch (Exception e) {
            throw new BusinessException("流程审批失败！");
        }
    }
    
    /**
     * <li>说明：流程审批(不同意)
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processInstID 实例ID
     * @param workId 任务ID
     * @return
     * @throws BusinessException
     */
    public void refuseProcessSnaker(String processInstID,String workId) throws BusinessException{
        try {
            Map<String, String> params = new HashMap<String, String>();
            AcOperator operator = SystemContext.getAcOperator();
            params.put("orderId", processInstID);
            params.put("taskId", workId);
            params.put("operator", operator.getUserid());
            String result = SnakeHttpClientUtil.doGet(REFUSE_METHOD, params);
            if(StringUtil.isNullOrBlank(result)){
                throw new BusinessException("流程提交失败，数据返回异常！");
            }
            WsCommonResponse response = JSONUtil.read(result, WsCommonResponse.class);
            if(!response.isSuccess()){
                throw new BusinessException("流程提交失败，数据返回异常！");
            }
        } catch (Exception e) {
            throw new BusinessException("流程审批失败！");
        }
    }
    
    /**
     * <li>说明：流程驳回（驳回）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processInstID 流程实例ID
     * @param workId 任务ID
     * @return 
     * @throws BusinessException
     */
    public String rejectProcessSnaker(String processInstID,String workId) throws BusinessException{
        try {
            Map<String, String> params = new HashMap<String, String>();
            AcOperator operator = SystemContext.getAcOperator();
            params.put("processInstID", processInstID);
            params.put("taskId", workId);
            params.put("operator", operator.getUserid());
            String result = SnakeHttpClientUtil.doGet(REJECT_METHOD, params);
            if(StringUtil.isNullOrBlank(result)){
                throw new BusinessException("流程提交失败，数据返回异常！");
            }
            WsCommonResponse response = JSONUtil.read(result, WsCommonResponse.class);
            if(StringUtil.isNullOrBlank(response.getOrderId())){
                throw new BusinessException("流程提交失败，数据返回异常！");
            }
            return response.getOrderId();
        } catch (Exception e) {
            throw new BusinessException("流程审批失败！");
        }
    }
    
    /**
     * <li>说明：通过人员获取流程任务列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param userID
     * @return 流程任务
     * @throws BusinessException
     */
    public com.yunda.flow.snaker.entity.Page<WorkItem> getProcessWorkByUser(String userID,Integer startPage,Integer limit) throws BusinessException{
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", userID);
            params.put("rolename", "");
            params.put("startIdx", startPage == null ? "0" : startPage.toString());
            params.put("pageSize", limit == null ? "9999" : limit.toString());
            String result = SnakeHttpClientUtil.doGet(GET_PROCESS_WORK_METHOD, params);
            if(StringUtil.isNullOrBlank(result)){
                throw new BusinessException("流程提交失败，数据返回异常！");
            }
            WsCommonResponse<WorkItem> response = JSON.parseObject(result,new TypeReference<WsCommonResponse<WorkItem>>(){});
            if(response.getPage() != null){
                return response.getPage();
            }
            return null ;
        } catch (Exception e) {
            throw new BusinessException("获取流程任务失败！");
        }
    }
    
    
    /**
     * <li>说明：获取待办数量
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param userID 用户名
     * @return
     * @throws BusinessException
     */
    public Integer getProcessWorkCountByUser(String userID) throws BusinessException{
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", userID);
            params.put("startIdx", "0");
            params.put("pageSize", "9999");
            String result = SnakeHttpClientUtil.doGet(GET_PROCESS_WORK_METHOD, params);
            if(StringUtil.isNullOrBlank(result)){
                return 0 ;
            }
            WsCommonResponse<WorkItem> response = JSON.parseObject(result,new TypeReference<WsCommonResponse<WorkItem>>(){});
            if(response.getPage() != null){
                return Integer.parseInt(response.getPage().getTotalCount()+"");
            }
            return 0 ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0 ;
    }
    
    
    /**
     * <li>说明：查询流程图
     * <li>创建人：何东
     * <li>创建日期：2016-9-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param processId 流程ID
     * @param orderId 流程实例ID
     * @return 流程任务
     * @throws BusinessException
     */
    public Map<String, String> flowDiagram(String processId, String orderId) throws BusinessException{
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("processId", processId);
            params.put("orderId", orderId);
            String result = SnakeHttpClientUtil.doGet(GET_PROCESS_DIAGRAM_METHOD, params);
            if(StringUtil.isNullOrBlank(result)){
                throw new BusinessException("获取流程图参数失败，数据返回异常！");
            }
            Map<String, String> response = JSON.parseObject(result,new TypeReference<HashMap<String, String>>(){});
            if(response != null){
                return response;
            }
            return null ;
        } catch (Exception e) {
            throw new BusinessException("获取流程图参数失败！");
        }
    }
    
    /**
     * <li>说明：查询流程图中任务节点提示信息
     * <li>创建人：何东
     * <li>创建日期：2016-9-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param orderId 流程实例ID
     * @param taskName 流程实例ID
     * @return 流程任务
     * @throws BusinessException
     */
    public Map<String, String> nodeTip(String orderId, String taskName) throws BusinessException{
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("taskName", taskName);
            params.put("orderId", orderId);
            String result = SnakeHttpClientUtil.doGet(GET_PROCESS_NODETIP_METHOD, params);
            if(StringUtil.isNullOrBlank(result)){
                throw new BusinessException("获取流程图中任务节点提示信息失败，数据返回异常！");
            }
            Map<String, String> response = JSON.parseObject(result,new TypeReference<HashMap<String, String>>(){});
            if(response != null){
                return response;
            }
            return null ;
        } catch (Exception e) {
            throw new BusinessException("获取流程图中任务节点提示信息失败！");
        }
    }
}
