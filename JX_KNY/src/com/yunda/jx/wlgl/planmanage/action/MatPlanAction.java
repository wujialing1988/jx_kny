package com.yunda.jx.wlgl.planmanage.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.wlgl.planmanage.entity.MatPlan;
import com.yunda.jx.wlgl.planmanage.entity.MatPlanDetail;
import com.yunda.jx.wlgl.planmanage.manager.MatPlanManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：MatPlan控制器, 用料计划单
 * <li>创建人：刘晓斌
 * <li>创建日期：2014-10-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class MatPlanAction extends JXBaseAction<MatPlan, MatPlan, MatPlanManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：保存用料计划单以及明细
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception
	 */
	public void savePlanAndDetail() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String sMatPlan = StringUtil.nvlTrim( getRequest().getParameter("matPlan"), "{}" );
			// 用料计划单
			MatPlan oMatPlan = (MatPlan)JSONUtil.read(sMatPlan, MatPlan.class);
			// 用料计划单明细
			MatPlanDetail[] detailList = (MatPlanDetail[])JSONUtil.read(getRequest(), MatPlanDetail[].class);
			String[] errMsg = this.manager.savePlanAndDetail(oMatPlan, detailList);
			if (errMsg == null || errMsg.length < 1) {
				map.put("success", "true");
			}else{
				map.put("success", "false");
				map.put("errMsg", errMsg);
			}
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
}