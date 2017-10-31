/**
 * <li>文件名：OmEmpgroupManager.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-17
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.List;

import com.yunda.base.BaseManager;
import com.yunda.common.BusinessException;
import com.yunda.frame.yhgl.entity.OmEmpgroup;
import com.yunda.frame.yhgl.entity.OmEmployee;

/**
 * <li>类型名称：员工对应的工作组服务类
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-17
 * <li>修改人：
 * <li>修改日期：
 */
public class OmEmpgroupManager extends BaseManager<OmEmpgroup, OmEmpgroup> {

	/**
	 * <li>方法名：findEmpGroupsByEmp
	 * <li>@param omEmployee 员工信息
	 * <li>@return 员工对应的工作组
	 * <li>@throws BusinessException
	 * <li>返回类型：List<OmEmpgroup>
	 * <li>说明：	通过员工信息查询员工对应的工作组
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-2-17
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmpgroup> findEmpGroupsByEmp(OmEmployee omEmployee)
			throws BusinessException {
		List<OmEmpgroup> empGroups = daoUtils
				.find("SELECT oeg FROM OmEmpgroup oeg "
						+ " WHERE oeg.id.empid = " + omEmployee.getEmpid());
		return empGroups;
	}

	@Override
	protected void checkDelete(Serializable id) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void checkUpdate(OmEmpgroup entity) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	
}