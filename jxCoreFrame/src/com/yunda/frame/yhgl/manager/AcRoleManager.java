/**
 * <li>文件名：AcRoleManager.java
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
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcOperatorrole;
import com.yunda.frame.yhgl.entity.AcRole;
import com.yunda.frame.yhgl.entity.OmPartyrole;

/**
 * <li>类型名称：系统角色服务类
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-16
 * <li>修改人：
 * <li>修改日期：
 */
public class AcRoleManager extends BaseManager<AcRole, AcRole> implements IAcRoleManager{

	/**
	 * <li>方法名：findAllRoles
	 * <li>@param acOperatorroles 操作员自身角色
	 * <li>@param OmPartyRoles 机构组织对应的角色关系
	 * <br>（包括操作员自身赋予的角色，所在机构赋予的角色、拥有的职务赋予的角色、所在岗位赋予的角色、所在工作组赋予的角色）
	 * <li>@return
	 * <li>返回类型：List<AcRole>
	 * <li>说明：
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-2-17
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<AcRole> findAllRoles(List<AcOperatorrole> acOperatorroles,
			List<OmPartyrole>... OmPartyRoles) {
		List<AcRole> acRoles = null;
		
		StringBuffer roleIds = new StringBuffer();
		//添加操作员自身角色
		if(acOperatorroles != null){
			for(AcOperatorrole acOperatorrole : acOperatorroles){
				roleIds.append(",'");
				roleIds.append(acOperatorrole.getId().getRoleid());
				roleIds.append("'");
			}
		}
		
		//所在机构赋予的角色、拥有的职务赋予的角色、所在岗位赋予的角色、所在工作组赋予的角色
		if (OmPartyRoles != null) {
			for (List<OmPartyrole> partyRoles : OmPartyRoles) {
				if (partyRoles != null) {
					for (OmPartyrole partyRole : partyRoles) {
						roleIds.append(",'");
						roleIds.append(partyRole.getId().getRoleid());
						roleIds.append("'");
					}
				}
			}
			
			if(!StringUtils.isBlank(roleIds.toString())) {
				acRoles = daoUtils.find("FROM AcRole WHERE roleid IN ("
						+ roleIds.toString().replaceFirst(",", "") + ")");
			}
		}
		return acRoles;
	}

	/**
	 * <li>方法名：checkDelete
	 * <li>@param entity
	 * <li>@return
	 * <li>返回类型：
	 * <li>说明：删除验证
	 * <li>创建人：田华
	 * <li>创建日期：2011-3-11
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	protected void checkDelete(Serializable id) throws BusinessException {
	}

	/**
	 * <li>方法名：checkUnique
	 * <li>@param entity
	 * <li>@return
	 * <li>返回类型：
	 * <li>说明：保存验证
	 * <li>创建人：田华
	 * <li>创建日期：2011-3-11
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	protected void checkUpdate(AcRole entity) throws BusinessException {
		if(daoUtils.isNotUnique(entity, "rolename")){
			throw new BusinessException("角色名称【"+ entity.getRolename() +"】已存在！");
		}
	}
	
	/**
	 * <br/><li>说明： 根据当前登录的操作员ID，获取该操作员对应的系统角色ID，包括操作员直接对应的角色以及通过组织机构、岗位、工作组和职务间接对应的角色.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2014-6-3
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @return 角色ID集合
	 */
	@SuppressWarnings("unchecked")
	public List <Object> findRoleIdByOperatorId(Long operatorid){
		String _sql01 = SqlMapUtil.getSql(XMLNAME_ROLE.concat("findRoleIdByOperatorId1")).replace("?", String.valueOf(operatorid)); //根据登录人ID， 获取其对应的角色ID
		String _sql02 = SqlMapUtil.getSql(XMLNAME_ROLE.concat("findRoleIdByOperatorId2")).replace("?", String.valueOf(operatorid)); //根据登录人ID， 获取其所属机构对应的角色ID
		String _sql03 = SqlMapUtil.getSql(XMLNAME_ROLE.concat("findRoleIdByOperatorId3")).replace("?", String.valueOf(operatorid)); //根据登录人ID， 获取其所属工作组对应的角色ID
		String _sql04 = SqlMapUtil.getSql(XMLNAME_ROLE.concat("findRoleIdByOperatorId4")).replace("?", String.valueOf(operatorid)); //根据登录人ID， 获取其所属岗位对应的角色ID
		String _sql05 = SqlMapUtil.getSql(XMLNAME_ROLE.concat("findRoleIdByOperatorId5")).replace("?", String.valueOf(operatorid)); //根据登录人ID， 获取其所属职务对应的角色ID
		//将人员-机构-岗位-职务-工作组对应的角色全部汇总并排除重复
		StringBuffer _union = new StringBuffer();
		_union.append("SELECT DISTINCT roleid FROM (")
		.append(_sql01).append(" UNION ALL ").append(_sql02).append(" UNION ALL ").append(_sql03).append(" UNION ALL ").append(_sql04).append(" UNION ALL ").append(_sql05)
		.append(")");
		List <Object> list = this.daoUtils.executeSqlQuery(_union.toString());
		return list;
	}
    
    /**
     * <li>说明：根据当前登录的操作员ID，获取该操作员对应的系统角色名称，包括操作员直接对应的角色以及通过组织机构、岗位、工作组和职务间接对应的角色.
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 人员ID
     * @return 角色名称集合
     */
    public List <Object> findRoleNameByOperatorId(Long operatorid){
        String _sql01 = SqlMapUtil.getSql(XMLNAME_ROLE.concat("findRoleNameByOperatorId1")).replace("?", String.valueOf(operatorid)); //根据登录人ID， 获取其对应的角色名称
        String _sql02 = SqlMapUtil.getSql(XMLNAME_ROLE.concat("findRoleNameByOperatorId2")).replace("?", String.valueOf(operatorid)); //根据登录人ID， 获取其所属机构对应的角色名称
        String _sql03 = SqlMapUtil.getSql(XMLNAME_ROLE.concat("findRoleNameByOperatorId3")).replace("?", String.valueOf(operatorid)); //根据登录人ID， 获取其所属工作组对应的角色名称
        String _sql04 = SqlMapUtil.getSql(XMLNAME_ROLE.concat("findRoleNameByOperatorId4")).replace("?", String.valueOf(operatorid)); //根据登录人ID， 获取其所属岗位对应的角色名称
        String _sql05 = SqlMapUtil.getSql(XMLNAME_ROLE.concat("findRoleNameByOperatorId5")).replace("?", String.valueOf(operatorid)); //根据登录人ID， 获取其所属职务对应的角色名称
        //将人员-机构-岗位-职务-工作组对应的角色全部汇总并排除重复
        StringBuffer _union = new StringBuffer();
        _union.append("SELECT DISTINCT rolename FROM (")
        .append(_sql01).append(" UNION ALL ").append(_sql02).append(" UNION ALL ").append(_sql03).append(" UNION ALL ").append(_sql04).append(" UNION ALL ").append(_sql05)
        .append(")");
        List <Object> list = this.daoUtils.executeSqlQuery(_union.toString());
        return list;
    }
}
