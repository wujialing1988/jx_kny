package com.yunda.jx.jxgc.workplanmanage.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeRel;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：JobProcessNode控制器, 机车检修计划流程节点
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
public class JobProcessNodeAction extends JXBaseAction<JobProcessNode, JobProcessNode, JobProcessNodeManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：新增编辑流程节点及节点前置关系
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveOrUpdateNodeAndRel() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JobProcessNode node = (JobProcessNode) JSONUtil.read(getRequest(), entity.getClass());
            JobProcessNodeRel[] rels = JSONUtil.read(getRequest().getParameter("rels"), JobProcessNodeRel[].class);
            this.manager.editNodeAndRel(node, rels);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据节点主键获取节点实体对象
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getEntityByIDX() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JobProcessNode node = this.manager.getModelById(getRequest().getParameter("nodeIDX"));
            map.put("entity", node);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：删除流程节点
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void deleteNode() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errMsg = "";
        try {
            errMsg = this.manager.delNode(id);
            if (errMsg == null)
                map.put(Constants.SUCCESS, true);
            else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errMsg);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：启动流程节点任务
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void startNode() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errMsg = "";
        try {
            errMsg = this.manager.startNode(id);
            if (errMsg == null)
                map.put(Constants.SUCCESS, true);
            else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errMsg);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：更新节点的层级关系
     * <li>创建人：程锐
     * <li>创建日期：2015-5-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void changeNode() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errMsg = "";
        try {
            String changeNodeId = getRequest().getParameter("changeNodeId");
            errMsg = this.manager.changeNodeParent(id, changeNodeId);
            if (errMsg == null)
                map.put(Constants.SUCCESS, true);
            else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errMsg);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：流水线排程-对单个节点派工
     * <li>创建人：程锐
     * <li>创建日期：2015-5-21
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws IOException
     */
    public void dispatchNode() throws IOException{
        
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            WorkCard workCard = JSONUtil.read(getRequest(), WorkCard.class);
            String[] errMsg = this.manager.dispatchNode(workCard);
            if (errMsg == null || errMsg.length < 1) {
                map.put(Constants.SUCCESS, true);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：更改节点的实际时间
     * <li>创建人：程锐
     * <li>创建日期：2015-8-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateRealTime() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JobProcessNode node = (JobProcessNode) JSONUtil.read(getRequest(), entity.getClass());
            this.manager.updateRealTime(node);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
    /**
     * <li>说明：作业节点树
     * <li>创建人：张迪
     * <li>创建日期：2016-10-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void findFirstNodeTree() throws Exception{
        List<HashMap<String, Object>> children = null ;
        try {
//            String parentIDX = getRequest().getParameter("parentIDX");              // 上级作业节点主键
            String workPlanIDX = getRequest().getParameter("workPlanIDX");             // 作业流程主键
            children = manager.findFirstNodeTree(null, workPlanIDX);
        } catch (RuntimeException e) {
            ExceptionUtil.process(e, logger);
        } finally{
            JSONUtil.write(getResponse(), children);
        }
    }
}
