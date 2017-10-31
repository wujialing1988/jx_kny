/**
 * <li>文件名：AcOperatorroleManager.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-16
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.List;

import com.yunda.base.BaseManager;
import com.yunda.common.BusinessException;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.AcOperatorrole;

/**
 * <li>类型名称：操作员角色服务类
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-16
 * <li>修改人：
 * <li>修改日期：
 */
public class AcOperatorroleManager extends
		BaseManager<AcOperatorrole, AcOperatorrole> {

	/**
	 * <li>方法名：findAcOperatorRolesByAcOperator
	 * 
	 * @param acOperator
	 *            操作员信息
	 *            <li>
	 * @return 操作员自身角色
	 *         <li>
	 * @throws BusinessException
	 *             <li>返回类型：List<AcOperatorrole>
	 *             <li>说明：通过操作员信息查询操作员自身角色
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-2-17
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<AcOperatorrole> findAcOperatorRolesByAcOperator(
			AcOperator acOperator) throws BusinessException {
		List<AcOperatorrole> acOperatorroles = daoUtils
				.find("FROM AcOperatorrole aopr WHERE aopr.id.operatorid = "
						+ acOperator.getOperatorid());
		return acOperatorroles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yunda.base.BaseManager#checkDelete(java.io.Serializable)
	 */
	@Override
	protected void checkDelete(Serializable id) throws BusinessException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yunda.base.BaseManager#checkUnique(java.lang.Object)
	 */
	@Override
	protected void checkUpdate(AcOperatorrole entity) throws BusinessException {
		// TODO Auto-generated method stub

	}
}
