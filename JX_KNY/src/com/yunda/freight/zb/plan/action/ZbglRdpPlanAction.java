package com.yunda.freight.zb.plan.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlan;
import com.yunda.freight.zb.plan.manager.ZbglRdpPlanManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检计划action
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-22
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public class ZbglRdpPlanAction extends JXBaseAction<ZbglRdpPlan, ZbglRdpPlan, ZbglRdpPlanManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    

    /**
     * <li>说明：保存列表计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveZbglRdpPlan() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            ZbglRdpPlan plan = (ZbglRdpPlan)JSONUtil.read(getRequest(), entity.getClass());
            String[] errMsg = this.manager.validateUpdate(plan);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.saveZbglRdpPlan(plan);
//              返回记录保存成功的实体对象
                map.put("entity", plan);  
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
     * <li>说明：启动计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void startPlan() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errMsg = "";
        try {
            errMsg = this.manager.startPlan(id);
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
     * <li>说明：中断计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void interruptPlan() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errMsg = "";
        try {
            errMsg = this.manager.interruptPlan(id);
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
     * <li>说明：恢复计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void regainPlan() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errMsg = "";
        try {
            errMsg = this.manager.regainPlan(id);
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
     * <li>说明：完成计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void cmpPlan() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String errMsg = "";
        try {
            errMsg = this.manager.cmpPlan(id);
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
     * <li>说明：终止计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void stopPlan() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String[] errMsg = this.manager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
                this.manager.stopPlan(ids);
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
     * <li>说明：web端客车列检按专业按车辆查询
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void findKCLJLIst() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            String planIdx = getRequest().getParameter("rdpPlanIdx");
            map = this.manager.findKCLJLIst(planIdx).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }    
    
}
