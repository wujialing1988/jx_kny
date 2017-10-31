package com.yunda.jx.jxgc.workplanmanage.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.workplanmanage.entity.WorkPlanRepairActivity;
import com.yunda.jx.jxgc.workplanmanage.manager.WorkPlanRepairActivityManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkPlanRepairActivity控制器, 机车检修作业计划-检修活动
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
public class WorkPlanRepairActivityAction extends JXBaseAction<WorkPlanRepairActivity, WorkPlanRepairActivity, WorkPlanRepairActivityManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
  
    /**
     * <li>说明：根据检修项目生成检修活动及关联的作业工单
     * <li>创建人：程锐
     * <li>创建日期：2015-4-28
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveOrUpdateBatch() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            WorkPlanRepairActivity[] repairActivity = (WorkPlanRepairActivity[]) JSONUtil.read(getRequest(), WorkPlanRepairActivity[].class);
            this.manager.saveOrUpdateBatch(repairActivity);
            map.put("success", true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据检修作业计划主键idx查询机车检修作业计划-检修活动
     * <li>创建人：林欢
     * <li>创建日期：2016-6-6
     * <li>修改人：张迪
     * <li>修改日期：2016-6-30
     * <li>修改内容：
     * @throws Exception
     */
    public void findWorkPlanRepairActivityByWorkPlanIDX() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );  
            JSONObject ob = JSONObject.parseObject(searchJson);           
            //获取是否转历史的作业状态
            String workPlanStatus = ob.getString("workPlanStatus");     
            
            map =  this.manager.findWorkPlanRepairActivityByWorkPlanIDX(searchJson, getStart(), getLimit(), getOrders(),workPlanStatus).extjsStore();
            
        } catch (Exception e) {
          ExceptionUtil.process(e, logger, map);
        } finally {
          JSONUtil.write(this.getResponse(), map);
        }          
    }

    /**
     * <li>说明：根据检修作业节点主键idx查询检修记录单
     * <li>创建人：张迪
     * <li>创建日期：2016-10-28
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findWorkPlanRecordByNodeIDX() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim( req.getParameter("entityJson"), "{}" );  
            JSONObject ob = JSONObject.parseObject(searchJson);                        
            map =  this.manager.findWorkPlanRecordByNodeIDX(ob , getStart(), getLimit(), getOrders()).extjsStore();
            
        } catch (Exception e) {
          ExceptionUtil.process(e, logger, map);
        } finally {
          JSONUtil.write(this.getResponse(), map);
        }          
    }
}
