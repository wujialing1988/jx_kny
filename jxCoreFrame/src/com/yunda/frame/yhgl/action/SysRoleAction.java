/**
 * 
 */
package com.yunda.frame.yhgl.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.yhgl.entity.AcRole;
import com.yunda.frame.yhgl.manager.SysRoleManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统角色管理-控制类
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 2.0
 */
@SuppressWarnings(value="serial")
public class SysRoleAction extends JXBaseAction <AcRole, AcRole, SysRoleManager>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：保存操作员-角色关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void saveOperatorToRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String roleid = getRequest().getParameter("roleid"); //角色ID
			this.manager.saveOperatorToRole(roleid,ids); //调用业务类方法， 保存操作员与角色关联信息
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：删除操作员-角色关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void deleteOperatorToRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String roleid = getRequest().getParameter("roleid"); //角色ID
			this.manager.deleteOperatorToRole(roleid,ids); //调用业务类方法， 删除操作员与角色关联信息
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：保存机构-角色关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void saveOrganizationToRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String roleid = getRequest().getParameter("roleid"); //角色ID
			this.manager.saveOrganizationToRole(roleid,ids); //调用业务类方法， 保存机构与角色关联信息
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：删除机构-角色关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void deleteOrganizationToRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String roleid = getRequest().getParameter("roleid"); //角色ID
			this.manager.deleteOrganizationToRole(roleid,ids); //调用业务类方法， 删除机构与角色关联信息
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：保存工作组-角色关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void saveGroupToRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String roleid = getRequest().getParameter("roleid"); //角色ID
			this.manager.saveGroupToRole(roleid,ids); //调用业务类方法， 保存工作组与角色关联信息
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：删除工作组-角色关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void deleteGroupToRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String roleid = getRequest().getParameter("roleid"); //角色ID
			this.manager.deleteGroupToRole(roleid,ids); //调用业务类方法， 删除工作组与角色关联信息
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：保存岗位-角色关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void savePositionToRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String roleid = getRequest().getParameter("roleid"); //角色ID
			this.manager.savePositionToRole(roleid,ids); //调用业务类方法， 保存岗位与角色关联信息
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：删除岗位-角色关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void deletePositionToRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String roleid = getRequest().getParameter("roleid"); //角色ID
			this.manager.deletePositionToRole(roleid,ids); //调用业务类方法， 删除岗位与角色关联信息
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：保存职务-角色关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void saveDutyToRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String roleid = getRequest().getParameter("roleid"); //角色ID
			this.manager.saveDutyToRole(roleid,ids); //调用业务类方法， 保存职务与角色关联信息
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
	
	/**
	 * <li>说明：删除职务-角色关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	public void deleteDutyToRole() throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			String roleid = getRequest().getParameter("roleid"); //角色ID
			this.manager.deleteDutyToRole(roleid,ids); //调用业务类方法， 删除职务与角色关联信息
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			ExceptionUtil.process(e, logger, map);
		} finally {
			JSONUtil.write(this.getResponse(), map);
		}		
	}
}
