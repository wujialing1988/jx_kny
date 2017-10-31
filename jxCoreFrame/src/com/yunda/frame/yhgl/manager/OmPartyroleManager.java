/**
 * <li>文件名：OmPartyroleManager.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-16
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.yunda.base.BaseManager;
import com.yunda.common.BusinessException;
import com.yunda.frame.yhgl.entity.OmEmpgroup;
import com.yunda.frame.yhgl.entity.OmEmporg;
import com.yunda.frame.yhgl.entity.OmPartyrole;
import com.yunda.frame.yhgl.entity.OmPosition;

/**
 * <li>类型名称：组织对象角色服务类
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-16
 * <li>修改人：
 * <li>修改日期：
 */
public class OmPartyroleManager extends BaseManager<OmPartyrole, OmPartyrole> {

	/**
	 * <li>方法名：findOmPartyRolesByEmpPositions
	 * 
	 * @param empPositions
	 *            员工对应的岗位
	 * @param partyType
	 *            机构类型
	 *            <li>
	 * @return 职务与角色关系
	 *         <li>
	 * @throws BusinessException
	 *             <li>返回类型：List<OmPartyrole>
	 *             <li>说明：通过员工对应的岗位查询职务与角色关系
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-2-17
	 *             <li>修改人：李茂生
	 *             <li>修改日期：2011-5-18
	 */
	@SuppressWarnings("unchecked")
	public List<OmPartyrole> findOmPartyRolesByEmpPositions(
			List<OmPosition> empPositions, String partyType)
			throws BusinessException {
		List<OmPartyrole> partyRoles = null;

		StringBuffer strDutyId = new StringBuffer();
		if ("duty".equals(partyType)) {
			for (OmPosition empPosition : empPositions) {
					strDutyId.append(",'");
					//strDutyId.append(empPosition.getOmPosition().getDutyid()); //
					if(empPosition.getDutyid()!=null){
						strDutyId.append(empPosition.getDutyid()); //limaosheng xiugai
					}
					strDutyId.append("'");
				
			}
		} else if ("position".equals(partyType)) {
			for (OmPosition empPosition : empPositions) {
				strDutyId.append(",'");
				//strDutyId.append(empPosition.getOmPosition().getPositionid());//
				if(empPosition.getPositionid()!=null){
					strDutyId.append(empPosition.getPositionid()); //limaosheng xiugai
				}
				strDutyId.append("'");
			}
		}
		
		if(!StringUtils.isBlank(strDutyId.toString())){
			partyRoles = daoUtils.find("FROM OmPartyrole opr "
					+ " WHERE opr.id.partyid IN ("
					+ strDutyId.toString().replaceFirst(",", "")
					+ ") AND opr.id.partytype = '" + partyType + "' ");
		}
		return partyRoles;
	}

	/**
	 * <li>方法名：findOmPartyRolesByOmEmporgs
	 * <li>
	 * 
	 * @param empOrgs
	 *            员工对应的机构
	 *            <li>
	 * @return 员工所在机构角色
	 *         <li>
	 * @throws BusinessException
	 *             <li>返回类型：List<OmPartyrole>
	 *             <li>说明：通过员工对应的机构查询所在机构角色
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-2-17
	 *             <li>修改人：李茂生
	 *             <li>修改日期：2011-5-18
	 */
	@SuppressWarnings("unchecked")
	public List<OmPartyrole> findOmPartyRolesByOmEmporgs(List<OmEmporg> empOrgs)
			throws BusinessException {
		List<OmPartyrole> partyRoles = null;

		StringBuffer strOrgId = new StringBuffer();
		for (OmEmporg empOrg : empOrgs) {
			strOrgId.append(",'");
			strOrgId.append(empOrg.getId().getOrgid());
			strOrgId.append("'");
		}

		if(!StringUtils.isBlank(strOrgId.toString())){
			partyRoles = daoUtils.find(" FROM OmPartyrole opr "
					+ " WHERE opr.id.partyid IN ("
					+ strOrgId.toString().replaceFirst(",", "")
					+ ") AND opr.id.partytype = 'organization' ");
		}
		return partyRoles;
	}

	/**
	 * <li>方法名：findOmPartyRolesByEmpGroups
	 * <li>@param empGroups 员工对应的工作组
	 * <li>@return 组织与角色关系
	 * <li>@throws BusinessException
	 * <li>返回类型：List<OmPartyrole>
	 * <li>说明：通过员工对应的工作组查询组织与角色关系
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-2-17
	 * <li>修改人： 李茂生
	 * <li>修改日期：2011-5-18
	 */
	@SuppressWarnings("unchecked")
	public List<OmPartyrole> findOmPartyRolesByEmpGroups(List<OmEmpgroup> empGroups)
			throws BusinessException {
		List<OmPartyrole> partyRoles = null;

		StringBuffer strGroupId = new StringBuffer();
		for(OmEmpgroup empGroup : empGroups) {
			strGroupId.append(",'");
			strGroupId.append(empGroup.getId().getGroupid());
			strGroupId.append("'");
		}
		
		if(!StringUtils.isBlank(strGroupId.toString())){
			partyRoles = daoUtils.find("FROM OmPartyrole opr "
					+ " WHERE opr.id.partyid IN ("
					+ strGroupId.toString().replaceFirst(",", "")
					+ ") AND opr.id.partytype = 'group' ");
		}
		return partyRoles;
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
	protected void checkUpdate(OmPartyrole entity) throws BusinessException {
		// TODO Auto-generated method stub

	}
}
