package com.yunda.jx.pjjx.util;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.frame.yhgl.manager.OmOrganizationManager;
import com.yunda.util.DaoUtils;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：设置系统上下文用户信息的工具类
 * <li>创建人： 何涛
 * <li>创建日期： 2015-1-8 下午12:59:10
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class SystemContextUtil {

	/**
	 * <li>说明：设置系统的当前用户信息
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param omEmployee 人员实体
	 */
	public static void setSystemInfo(OmEmployee omEmployee) {
		SystemContext.setOmEmployee(omEmployee);
		
		// 设置操作员信息
		AcOperatorManager acOperatorManager = (AcOperatorManager)Application.getSpringApplicationContext().getBean("acOperatorManager");
		AcOperator acOperator = acOperatorManager.getModelById(omEmployee.getOperatorid());
		SystemContext.setAcOperator(acOperator);
		
		// 设置组织机构信息
		OmOrganizationManager omOrganizationManager = (OmOrganizationManager)Application.getSpringApplicationContext().getBean("omOrganizationManager");
		OmOrganization omOrganization = omOrganizationManager.getModelById(omEmployee.getOrgid());
		SystemContext.getUserSeesion().setWebAttribute(SystemContext.KEY_ORG, omOrganization);
	}
	
	/**
	 * <li>说明：根据empId设置系统的当前用户信息
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-08
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param empId 人员ID
	 */
	public static void setSystemInfoByempId(Long empId) {
		OmEmployeeManager omEmployeeManager = (OmEmployeeManager)Application.getSpringApplicationContext().getBean("omEmployeeManager");
		OmEmployee omEmployee = omEmployeeManager.getModelById(empId);
		setSystemInfo(omEmployee);
	}
	
	/**
	 * <li>说明：根据operatorId设置系统的当前用户信息
	 * <li>创建人：何涛
	 * <li>创建日期：2015-01-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * 
	 * @param operatorId 操作员ID
	 */
	public static void setSystemInfoByOperatorId(Long operatorId) {
		DaoUtils daoUtils = (DaoUtils)Application.getSpringApplicationContext().getBean("daoUtils");
		String hql = "From OmEmployee Where operatorid = ?";
		OmEmployee omEmployee = (OmEmployee) daoUtils.findSingle(hql, new Object[]{operatorId});
		setSystemInfo(omEmployee);
	}
	
}
