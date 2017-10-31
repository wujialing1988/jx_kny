package com.yunda.passenger.traindemand.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.passenger.traindemand.entity.TrainDemand;
import com.yunda.passenger.traindemand.manager.TrainDemandManager;

/**
 * <li>标题: 肯尼亚综合管理信息系统
 * <li>说明: 列表需求维护业务类
 * <li>创建人：张迪
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部检修系统项目组
 * @version 1.0
 */
public class TrainDemandAction extends JXBaseAction<TrainDemand, TrainDemand, TrainDemandManager> {

    /**  序列  */
    private static final long serialVersionUID = 1L;
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
	/**
	 * <li>说明：方法实现功能说明
	 * <li>创建人：张迪
	 * <li>创建日期：2017-4-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws Exception
	 */
	public void saveOrUpdateDemand() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			TrainDemand t = (TrainDemand)JSONUtil.read(getRequest(), entity.getClass());
			String[] errMsg = this.manager.validateUpdate(t);
			if (errMsg == null || errMsg.length < 1) {
				this.manager.saveOrUpdateDemand(t);
//				返回记录保存成功的实体对象
				map.put("entity", t);  
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
   
    
    
}
