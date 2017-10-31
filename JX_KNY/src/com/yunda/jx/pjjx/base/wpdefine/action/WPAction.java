package com.yunda.jx.pjjx.base.wpdefine.action; 

import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.base.wpdefine.entity.WP;
import com.yunda.jx.pjjx.base.wpdefine.manager.WPManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：WP控制器, 检修作业流程
 * <li>创建人：何涛
 * <li>创建日期：2014-11-19
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */  
@SuppressWarnings(value="serial")
public class WPAction extends JXBaseAction<WP, WP, WPManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>方法说明：根据配件查询检修需求单
	 * <li>方法名：findByPartsType
	 * @throws Exception
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-22
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public void findByPartsType() throws Exception{
		String partsTypeIDX = getRequest().getParameter("queryName");
		Map<String, Object> map = manager.getListForPartsRdp(partsTypeIDX, getStart(), getLimit(), getOrders()).extjsStore();
		JSONUtil.write(getResponse(), map);
	}
	
}