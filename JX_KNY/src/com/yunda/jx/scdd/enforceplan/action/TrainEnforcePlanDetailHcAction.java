package com.yunda.jx.scdd.enforceplan.action; 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.scdd.enforceplan.entity.TrainEnforcePlanDetailHc;
import com.yunda.jx.scdd.enforceplan.manager.TrainEnforcePlanDetailHcManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TrainEnforcePlanDetailHc控制器, 货车检修计划详情控制器
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-12-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class TrainEnforcePlanDetailHcAction extends JXBaseAction<TrainEnforcePlanDetailHc, TrainEnforcePlanDetailHc, TrainEnforcePlanDetailHcManager>{
	
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());
	
	  /**
     * <li>说明：查询货车月计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param start 开始条数
     * @param limit 限制条数
     * @param planIdx 主表id
     * @return
     */   
    public void findTrainEnforcePlanDetailHcList() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            HttpServletRequest req = getRequest();
            String planIdx = req.getParameter("planIdx");
            map = this. manager.findTrainEnforcePlanDetailHcList(getStart(),getLimit(),planIdx).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }                 
    }
    
   
}