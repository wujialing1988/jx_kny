package com.yunda.jx.jxgc.processdef.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.order.AbstractOrderAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.processdef.entity.JobProcessNodeDef;
import com.yunda.jx.jxgc.processdef.manager.JobProcessNodeDefManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: JobProcessNodeDef控制器
 * <li>创建人：何涛
 * <li>创建日期：2015-4-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class JobProcessNodeDefAction extends AbstractOrderAction<JobProcessNodeDef, JobProcessNodeDef, JobProcessNodeDefManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：作业节点树
     * <li>创建人：何涛
     * <li>创建日期：2015-04-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @throws Exception 抛出异常列表
     */
    @SuppressWarnings("unchecked")
    public void tree() throws Exception{
        List<HashMap<String, Object>> children = null ;
        try {
            String parentIDX = getRequest().getParameter("parentIDX");              // 上级作业节点主键
            String processIDX = getRequest().getParameter("processIDX");             // 作业流程主键
            children = manager.tree(parentIDX, processIDX);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally{
            JSONUtil.write(getResponse(), children);
        }
    }
    
    /**
     * <li>说明：分页查询，关联查询节点前置节点
     * <li>创建人：何涛
     * <li>创建日期：2014-11-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @throws Exception
     */ 
    @SuppressWarnings("unchecked")
    public void findPageList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter(Constants.ENTITY_JSON), Constants.EMPTY_JSON_OBJECT );
            entity = (JobProcessNodeDef)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<JobProcessNodeDef> searchEntity = new SearchEntity<JobProcessNodeDef>(entity, getStart(), getLimit(), getOrders());
            map = this.manager.acquirePageList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    /**
     * <li>说明：通过拖拽的方式调整检修作业流程节点顺序
     * <li>创建人：何涛
     * <li>创建日期：2015-9-7
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * 
     * @throws Exception
     */ 
    @SuppressWarnings("unchecked")
    public void moveNode() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        HttpServletRequest req = getRequest();
        try {
            // 被拖拽的节点（idx主键）
            String node = req.getParameter("node");
            // 旧父节点（idx主键）
            String oldParent = req.getParameter("oldParent");
            // 新父节点（idx主键）
            String newParent = req.getParameter("newParent");
            // 新父节点（idx主键）
            String index = req.getParameter("index");
            this.manager.moveNode(node, oldParent, newParent, index);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
	
}