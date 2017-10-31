package com.yunda.jx.jxgc.workplanmanage.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeQueryManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：JobProcessNode查询控制器, 机车检修计划流程节点查询控制器
 * <li>创建人：程锐
 * <li>创建日期：2015-04-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 3.2
 */
@SuppressWarnings(value = "serial")
public class JobProcessNodeQueryAction extends JXBaseAction<JobProcessNode, JobProcessNode, JobProcessNodeQueryManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：选择流程节点树查询
     * <li>创建人：王利成
     * <li>创建日期：2015-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getTree() throws Exception {
        List<HashMap<String, Object>> children = null;
        try {
            String parentIDX = getRequest().getParameter("parentIDX"); // 上级作业节点主键
            String workPlanIDX = getRequest().getParameter("workPlanIDX"); // 作业流程主键
            String nodeIDX = getRequest().getParameter("nodeIDX"); // 作业流程主键
            children = manager.getTree(parentIDX, workPlanIDX, nodeIDX);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), children);
        }
    }
    
    /**
     * <li>说明：获取只有父节点的树
     * <li>创建人：程锐
     * <li>创建日期：2015-5-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getParentTree() throws Exception {
        List<HashMap<String, Object>> children = null;
        try {
            String parentIDX = getRequest().getParameter("parentIDX"); // 上级作业节点主键
            String workPlanIDX = getRequest().getParameter("workPlanIDX"); // 作业流程主键
            String nodeIDX = getRequest().getParameter("nodeIDX"); // 选择的节点主键
            String parentNodeIDX = getRequest().getParameter("parentNodeIDX"); // 选择的节点的父节点主键
            children = manager.getParentTree(parentIDX, workPlanIDX, nodeIDX, parentNodeIDX);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), children);
        }
    }
    
    /**
     * <li>说明：此节点是否关联工单
     * <li>创建人：程锐
     * <li>创建日期：2015-5-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void hasWorkCardOrActivity() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            boolean hasWorkCardOrActivity = this.manager.hasWorkCardOrActivity(getRequest().getParameter("nodeIDX"));
            map.put("hasWorkCardOrActivity", hasWorkCardOrActivity);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：作业工单查询流程节点树
     * <li>创建人：程锐
     * <li>创建日期：2015-5-13
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void findNodeTree() throws JsonMappingException, IOException{
        String parentIDX = StringUtil.nvl(getRequest().getParameter("parentIDX"));
        String rdpIdx = StringUtil.nvl(getRequest().getParameter("queryParam"));
        List<HashMap> children = null;        
        children = manager.findNodeTreeByWorkQuery(parentIDX, rdpIdx);        
        JSONUtil.write(getResponse(), children);
    }
    
    /**
     * <li>说明：选择流程节点树查询
     * <li>创建人：林欢
     * <li>创建日期：2016-3-17
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getChildTree() throws Exception {
        List<HashMap<String, Object>> children = null;
        try {
            String parentIDX = getRequest().getParameter("parentIDX"); // 上级作业节点主键
            children = manager.getChildTree(parentIDX);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), children);
        }
    }
    
    /**
     * <li>说明：获取下级节点列表
     * <li>创建人：林欢
     * <li>创建日期：2016-3-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 下级节点列表
     */
    public void getChildNodeList() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        List<JobProcessNode> children = null;
        try {
            String nodeIDX = getRequest().getParameter("nodeIDX"); // 上级作业节点主键
            children = manager.getChildNodeList(nodeIDX);
            map = new Page<JobProcessNode>(children.size(), children).extjsStore();
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
    /**
     * <li>说明：获取本节点下所有子级节点列表
     * <li>创建人：张迪
     * <li>创建日期：2017-2-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 下级节点列表
     */
    public void getAllChildNodeExceptThisList() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        List<JobProcessNode> children = null;
        try {
            String nodeIDX = getRequest().getParameter("nodeIDX"); // 上级作业节点主键
            children = manager.getAllChildNodeExceptThisList(nodeIDX);
            map = new Page<JobProcessNode>(children.size(), children).extjsStore();
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally {
            JSONUtil.write(getResponse(), map);
        }
    }
}
