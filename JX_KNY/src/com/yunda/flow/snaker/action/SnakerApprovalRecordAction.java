package com.yunda.flow.snaker.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.flow.snaker.entity.SnakerApprovalRecord;
import com.yunda.flow.snaker.manager.SnakerApprovalRecordManager;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 流程审批记录action
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-9-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public class SnakerApprovalRecordAction extends JXBaseAction<SnakerApprovalRecord, SnakerApprovalRecord, SnakerApprovalRecordManager> {

    /**  序列化  */
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(SnakerApprovalRecordAction.class);
    
    /**
     * <li>说明：查询流程图
     * <li>创建人：何东
     * <li>创建日期：2016-9-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public final void flowDiagram() throws JsonMappingException, IOException {
        HttpServletRequest req = getRequest();
        Map<String, String> map = null;
        try {
            // 流程ID
            String processId = req.getParameter("processId");
            // 流程实例ID
            String orderId =  req.getParameter("orderId");
            map = this.manager.flowDiagram(processId, orderId);
            if (map == null) {
            	map = new HashMap<String, String>();
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, new HashMap<String, Object>());
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询流程图中任务节点提示信息
     * <li>创建人：何东
     * <li>创建日期：2016-9-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public final void nodeTip() throws JsonMappingException, IOException {
        HttpServletRequest req = getRequest();
        Map<String, String> map = null;
        try {
            // 流程任务名称
            String taskName = req.getParameter("taskName");
            // 流程实例ID
            String orderId =  req.getParameter("orderId");
            map = this.manager.nodeTip(orderId, taskName);
            if (map == null) {
            	map = new HashMap<String, String>();
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, new HashMap<String, Object>());
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
