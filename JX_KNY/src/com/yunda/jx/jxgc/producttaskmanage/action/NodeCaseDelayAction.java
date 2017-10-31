package com.yunda.jx.jxgc.producttaskmanage.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.NodeCaseDelay;
import com.yunda.jx.jxgc.producttaskmanage.entity.NodeCaseDelayQuery;
import com.yunda.jx.jxgc.producttaskmanage.manager.NodeCaseDelayManager;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 工序延误记录控制器类
 * <li>创建人：张凡
 * <li>创建日期：2013-5-6 下午03:32:05
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class NodeCaseDelayAction extends JXBaseAction<NodeCaseDelay, NodeCaseDelay, NodeCaseDelayManager> {
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    private static final long serialVersionUID = 1L;
    
    /**
     * <li>方法名称：getDelayInfo
     * <li>方法说明：根据工艺节点实例IDX找到实体 (ajax) 
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-5-6 下午03:33:47
     * <li>修改人：
     * <li>修改内容：
     */
    public void getDelayInfo() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String nodeCaseIdx = req.getParameter("noceCaseIdx");
            entity = this.manager.getEntityByNodeCaseIdx(nodeCaseIdx);
            map.put("entity", entity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>方法名称：saveDelay
     * <li>方法说明：新增或保存延迟信息 
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-5-6 下午04:10:23
     * <li>修改人：
     * <li>修改内容：
     */
    public void saveDelay() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("data"), "{}");
            entity = (NodeCaseDelay) JSONUtil.read(searchJson, entitySearch.getClass());
            this.manager.saveOrUpdate(entity);
            this.manager.sendDelayNotify(entity.getNodeCaseIdx());//发送延误通知
            map.put("entity", entity);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            map.put("errMsg", e.getMessage());
            e.getStackTrace();
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>方法名称：findWorkSeqPutOff
     * <li>方法说明：查询工序延误 
     * <li>@throws JsonMappingException
     * <li>@throws IOException
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-5-6 下午01:41:50
     * <li>修改人：
     * <li>修改内容：
     */
    public void findWorkSeqPutOff() throws JsonMappingException, IOException{
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String isSearch = req.getParameter("isSearch");//是否是查询
            String isHis = req.getParameter("isHis");//是否是查询历史记录            
            map = this.manager.findWorkSeqPutOff(req.getParameter("orgid"), 
            									 getStart(), 
            									 getLimit(), 
            									 getOrders(), 
            									 req.getParameter("entityJson"), 
            									 isSearch, 
            									 isHis).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    
    /**
     * <li>说明：获取某一节点，及其下级所有子节点的工序延误信息
     * <li>创建人：何涛
     * <li>创建日期：2015-9-1
     * <li>修改人：何涛
     * <li>修改日期：2016-04-18
     * <li>修改内容：修改不规范的异常处理
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findChildren() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            // 机车检修计划流程节点主键
            String nodeCaseIdx = req.getParameter("nodeCaseIdx");
            List<com.yunda.jx.jxgc.producttaskmanage.manager.NodeCaseDelayManager.NodeCaseDelayBean> list = this.manager.findChildren(nodeCaseIdx);
            map.put("id", EntityUtil.IDX);
            map.put("root", list);
            map.put("totalProperty", list.size());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    /**
     * <li>说明：获取节点及其下级所有子节点的工序延误信息（延误类型改为多选）
     * <li>创建人：张迪
     * <li>创建日期：2016-7-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void findChildrenNew() throws JsonMappingException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        HttpServletRequest req = getRequest();
        try {
            // 机车检修计划流程节点主键
            String nodeCaseIdx = req.getParameter("nodeCaseIdx");
            String workPlanIDX = req.getParameter("workPlanIDX");
            List<NodeCaseDelayQuery> list = this.manager.findChildrenNew(nodeCaseIdx,workPlanIDX);
            map.put("id", EntityUtil.IDX);
            map.put("root", list);
            map.put("totalProperty", list.size());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
}
