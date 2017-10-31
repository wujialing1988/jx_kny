package com.yunda.frame.yhgl.action;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcApplication;
import com.yunda.frame.yhgl.manager.SysAppManager;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统应用功能管理-应用action
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class SysAppAction extends JXBaseAction <AcApplication, AcApplication, SysAppManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * 
	 * <li>说明：获得系统应用机构树
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-30
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unused")
	public void tree() throws Exception{
		String roleid = getRequest().getParameter("roleid");//角色ID
		List<Map> childNodeList = this.manager.buildSysAppTree(); //获取系统功能菜单树
		if(childNodeList!=null&&childNodeList.size()>0){
			this.manager.updatePowerTreeView(roleid,childNodeList); //查询当前角色的权限，并在系统功能菜单树中回显
		}
		JSONUtil.write(getResponse(),childNodeList);
	}
}
