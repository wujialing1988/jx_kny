package com.yunda.frame.yhgl.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcRolefunc;
import com.yunda.frame.yhgl.manager.SysRoleFuncManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 角色-应用关联action
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
public class SysRoleFuncAction extends JXBaseAction <AcRolefunc, AcRolefunc, SysRoleFuncManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * <li>说明：保存用户勾选的角色和应用功能关系数据
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void saveRoleAndFunc() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String roleid = getRequest().getParameter("roleid");
			AcRolefunc [] rf = (AcRolefunc [])JSONUtil.read(getRequest(), AcRolefunc[].class); //获取json数组转换成对象数组
			this.manager.saveRoleAndFunc(roleid,rf);
			//返回记录保存成功的实体对象
			map.put("success", true);
		} catch (Exception e) {
			ExceptionUtil.process(e, logger, map);
			map.put("errMsg", "更新角色权限功能失败！");
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
}
