package com.yunda.jx.jxgc.workplanmanage.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNodeRel;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeNewManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车检修计划流程节点（分段调度，车间调度）
 * <li>创建人：张迪
 * <li>创建日期：2017-1-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class JobProcessNodeNewAction extends JXBaseAction<JobProcessNode, JobProcessNode, JobProcessNodeNewManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：新增编辑流程第一级节点及节点前置关系
     * <li>创建人：张迪
     * <li>创建日期：2017-1-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveOrUpdateFirstNodeAndRel() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JobProcessNode node = (JobProcessNode) JSONUtil.read(getRequest(), entity.getClass());
            JobProcessNodeRel[] rels = JSONUtil.read(getRequest().getParameter("rels"), JobProcessNodeRel[].class);
            this.manager.saveOrUpdateFirstNodeAndRel(node, rels, false);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
//    /**
//     * <li>说明：方法实现功能说明
//     * <li>创建人：张迪
//     * <li>创建日期：2017-4-5
//     * <li>修改人： 
//     * <li>修改日期：
//     * <li>修改内容：
//     * @throws Exception
//     */
//    public void updateFirstNodeAndRel() throws Exception {
//        Map<String, Object> map = new HashMap<String, Object>();
//        try {
//            JobProcessNode node = (JobProcessNode) JSONUtil.read(getRequest(), entity.getClass());
//            JobProcessNodeRel[] rels = JSONUtil.read(getRequest().getParameter("rels"), JobProcessNodeRel[].class);
//            this.manager.updateFirstNode(node, rels, false);
//            map.put(Constants.SUCCESS, true);
//        } catch (Exception e) {
//            ExceptionUtil.process(e, logger, map);
//        } finally {
//            JSONUtil.write(this.getResponse(), map);
//        }
//    }
    
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
     * <li>创建人：张迪
     * <li>创建日期：2017-1-14
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
     * <li>创建人：张迪
     * <li>创建日期：2017-1-21
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
     * <li>说明：获取第一级节点及下级节点列表
     * <li>创建人：张迪
     * <li>创建日期：2016-3-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 下级节点列表
     */
    public void getFirstNodeListNew() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            JobProcessNode objEntity = (JobProcessNode)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<JobProcessNode> searchEntity = new SearchEntity<JobProcessNode>(objEntity, getStart(), getLimit(), getOrders());
            map = this. manager.getFirstNodeListNew(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }                 
    }
    /**
     * <li>说明：更新子级节点列表
     * <li>创建人：张迪
     * <li>创建日期：2017-1-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 下级节点列表
     */
    public void saveOrUpdateLeafNode() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            JobProcessNode node = (JobProcessNode) JSONUtil.read(getRequest(), entity.getClass());
            this.manager.saveOrUpdateLeafNode(node);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }           
        
    }
 
    /**
     * <li>说明：vis界面上拖动节点修改时间 更新子级节点时间
     * <li>创建人：张迪
     * <li>创建日期：2017-1-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 下级节点列表
     */
    public void updateLeafNodeTime() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            JobProcessNode node = (JobProcessNode) JSONUtil.read(getRequest(), entity.getClass());
            this.manager.updateLeafNodeTime(node);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }           
        
    }
  }
