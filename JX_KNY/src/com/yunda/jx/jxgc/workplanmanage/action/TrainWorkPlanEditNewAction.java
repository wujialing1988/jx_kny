package com.yunda.jx.jxgc.workplanmanage.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanEditNewManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: TrainWorkPlan控制器, 机车检修作业计划编制（新）
 * <li>创建人：张迪
 * <li>创建日期：2017-1-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class TrainWorkPlanEditNewAction extends JXBaseAction<TrainWorkPlan, TrainWorkPlan, TrainWorkPlanEditNewManager> {
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    /**
     * <li>说明：生成机车检修作业计划
     * <li>创建人：张迪
     * <li>创建日期：2015-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void generateWorkPlan() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            TrainWorkPlan workPlan = (TrainWorkPlan) JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(workPlan);
            if (errMsg == null || errMsg.length < 1) {
                workPlan = this.manager.generateWorkPlan(workPlan);
                map.put("entity", workPlan);
                map.put("success", true);
            } else {
                map.put("success", false);
                map.put("errMsg", errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
   
    /**
     * <li>说明：机车检修作业计划编制查询检修作业节点，按节点顺序，依次显示检修作业流程节点
     * <li>创建人：张迪
     * <li>创建日期：2016-7-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void queryWorkPlanList() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            TrainWorkPlan objEntity = (TrainWorkPlan)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<TrainWorkPlan> searchEntity = new SearchEntity<TrainWorkPlan>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.queryWorkPlanList(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * <li>说明：机车检修作业计划编制查询检修作业流程(最新修改)
     * <li>创建人：张迪
     * <li>创建日期：2016-7-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void queryTrainWorkPlan() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            TrainWorkPlan objEntity = (TrainWorkPlan)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<TrainWorkPlan> searchEntity = new SearchEntity<TrainWorkPlan>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.queryTrainWorkPlan(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    /**
     * <li>说明：流程图 获取第一级节点及下级节点列表
     * <li>创建人：张迪
     * <li>创建日期：2016-3-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param nodeIDX 节点IDX
     * @return 下级节点列表
     */
    public void getFirstNodeListByRdpIDX() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );
            TrainWorkPlan objEntity = (TrainWorkPlan)JSONUtil.read(searchJson, entitySearch.getClass());
            SearchEntity<TrainWorkPlan> searchEntity = new SearchEntity<TrainWorkPlan>(objEntity, getStart(), getLimit(), getOrders());
            map = this.manager.getFirstNodeListByRdpIDX(searchEntity).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }                 
    }
}
