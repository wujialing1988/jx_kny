package com.yunda.jx.pjjx.base.tecdefine.action; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.order.AbstractOrderAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.base.tecdefine.entity.TecCardWS;
import com.yunda.jx.pjjx.base.tecdefine.manager.TecCardWSManager;
/**
 * <li>标题: 机车配件检修管理信息系统
 * <li>说明: TecCardWS控制器, 配件检修工序
 * <li>创建人： 何涛
 * <li>创建日期： 2014-10-22 上午11:22:08
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class TecCardWSAction extends AbstractOrderAction<TecCardWS, TecCardWS, TecCardWSManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：查询配件检修工序
	 * <li>创建人：何涛
	 * <li>创建日期：2014-10-23
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @throws Exception 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
    public void tree() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		List<HashMap<String, Object>> children = null ;
	    try {
			String parentIDX = getRequest().getParameter("parentIDX");  	// 上级工序idx主键
			String tecCardIDX = getRequest().getParameter("tecCardIDX");  	// 检修工艺卡idx主键
		    children = manager.tree(parentIDX, tecCardIDX);
		} catch (RuntimeException e) {
			ExceptionUtil.process(e, logger, map);
		} finally{
			JSONUtil.write(getResponse(), children);
		}
    }
	
}