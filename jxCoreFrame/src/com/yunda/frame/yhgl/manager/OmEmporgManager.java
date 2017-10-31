/**
 * <li>文件名：OmEmporgManager.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-17
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.frame.yhgl.manager;

import java.util.List;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmEmporg;


/**
 * <li>类型名称：员工对应机构服务类
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-17
 * <li>修改人：
 * <li>修改日期：
 */
public class OmEmporgManager extends JXBaseManager<OmEmporg, OmEmporg> {

	/**
	 * <li>方法名：findEmpOrgsByEmp
	 * <li>@param omEmployee 员工信息
	 * <li>@return 员工对应的机构
	 * <li>@throws BusinessException
	 * <li>返回类型：List<OmEmporg>
	 * <li>说明：通过员工信息查询员工对应的机构
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-2-17
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmporg> findEmpOrgsByEmp(OmEmployee omEmployee)
			throws BusinessException {
		List<OmEmporg> empOrgs = daoUtils
				.find("SELECT oeorg FROM OmEmporg oeorg "
						+ " WHERE oeorg.id.empid = "
						+ omEmployee.getEmpid());
		return empOrgs;
	}

}
