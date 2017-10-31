package com.yunda.jx.jxgc.workplanthedynamic.action;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.workplanthedynamic.entity.TrainWorkPlanDynamic;
import com.yunda.jx.jxgc.workplanthedynamic.manager.TrainWorkPlanDynamicManager;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车检修当日动态
 * <li>创建人：张迪
 * <li>创建日期：2017-3-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings("serial")
public class TrainWorkPlanDynamicAction extends JXBaseAction<TrainWorkPlanDynamic, TrainWorkPlanDynamic, TrainWorkPlanDynamicManager>{
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2017-3-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void insertTheDynamic() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String planGenerateDateStr = getRequest().getParameter("planGenerateDate");
        String errMsg = "";
        try {
            errMsg = this.manager.insertTheDynamic(planGenerateDateStr);
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
     * <li>说明：提交动态
     * <li>创建人：张迪
     * <li>创建日期：2017-3-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveTheDynamic() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
         String errMsg = "";
        try {
            this.manager.saveTheDynamic();
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map, errMsg);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
