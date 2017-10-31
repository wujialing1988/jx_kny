package com.yunda.zb.zbfw.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.order.AbstractOrderAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.zb.zbfw.entity.ZbglJobProcessNodeDef;
import com.yunda.zb.zbfw.manager.ZbglJobProcessNodeDefManager;
/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：ZbglJobProcessNodeDef控制器, 整备作业流程节点
 * <li>创建人：程梅
 * <li>创建日期：2016年4月7日
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class ZbglJobProcessNodeDefAction extends AbstractOrderAction<ZbglJobProcessNodeDef, ZbglJobProcessNodeDef, ZbglJobProcessNodeDefManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * <li>说明：作业节点树
     * <li>创建人：程梅
     * <li>创建日期：2015-4-8
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
            String zbfwIDX = getRequest().getParameter("zbfwIDX");             // 范围主键
            children = manager.tree(parentIDX, zbfwIDX);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally{
            JSONUtil.write(getResponse(), children);
        }
    }
    /**
     * <li>说明：通过拖拽的方式调整检修作业流程节点顺序
     * <li>创建人：程梅
     * <li>创建日期：2015-4-8
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