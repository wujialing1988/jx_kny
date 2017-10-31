package com.yunda.jx.jxgc.workhis.workcard.action; 


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardBean;
import com.yunda.jx.jxgc.workhis.workcard.entity.WorkCardHis;
import com.yunda.jx.jxgc.workhis.workcard.manager.WorkCardHisManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WorkCardHis控制器, 作业工单历史
 * <li>创建人：程锐
 * <li>创建日期：2012-12-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WorkCardHisAction extends JXBaseAction<WorkCardHis, WorkCardHis, WorkCardHisManager>{
	/** 日志工具 */
	private Logger logger = Logger.getLogger(getClass().getName());	
     /**
     * <li>说明：根据状态判断是查询历史还是当前记录卡
     * <li>创建人：张迪
     * <li>创建日期：2016-9-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception 
     */
    public void findWorkCardInfoByWorkPlanRepairActivityIDX() throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        try {            
            String workPlanRepairActivityIDX = getRequest().getParameter("workPlanRepairActivityIDX");
            String workPlanStatus = getRequest().getParameter("workPlanStatus");
            List<WorkCardBean> workCardBeanHisList = this.manager.findListByWorkPlanRepairActivityIDX(workPlanRepairActivityIDX,workPlanStatus);
            map.put("workCardBeanList", workCardBeanHisList);          
        } catch (Exception e) {
            ExceptionUtil.process(e, logger,map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    } 
}