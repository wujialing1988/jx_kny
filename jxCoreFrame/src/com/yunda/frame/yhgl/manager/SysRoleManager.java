package com.yunda.frame.yhgl.manager;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.AcOperatorrole;
import com.yunda.frame.yhgl.entity.AcOperatorroleId;
import com.yunda.frame.yhgl.entity.AcRole;
import com.yunda.frame.yhgl.entity.OmPartyrole;
import com.yunda.frame.yhgl.entity.OmPartyroleId;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统角色管理-业务类
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 2.0
 */
@Service(value="sysRoleManager")
public class SysRoleManager extends JXBaseManager <AcRole, AcRole>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
		
	/**
	 * <li>说明：删除角色
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return void
	 * @throws Exception
	 */	
	public void deleteByIds(Serializable... ids) throws BusinessException {
		String sql = null;
		try {
			for (Serializable id : ids) {
				sql = "DELETE Ac_Operatorrole WHERE roleid = '" + id + "'"; //删除角色-操作员关联
				this.daoUtils.executeSql(sql);
				sql = "DELETE Om_Partyrole WHERE roleid = '" + id + "'"; //删除角色-（机构、工作组、岗位、职务）关联
				this.daoUtils.executeSql(sql);
				sql = "DELETE AC_Rolefunc WHERE roleid = '" + id + "'"; //删除角色-应用功能关联
				this.daoUtils.executeSql(sql);
				sql = "DELETE Ac_Role WHERE roleid = '" + id + "'"; //删除角色
				this.daoUtils.executeSql(sql);
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	
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
	public void saveOperatorToRole(String roleid, Serializable... ids) throws BusinessException {
		try {
			for (Serializable id : ids) {
				AcOperatorroleId acOperatorroleId = new AcOperatorroleId();
				acOperatorroleId.setOperatorid(Long.valueOf(String.valueOf(id)));
				acOperatorroleId.setRoleid(roleid);
				
				AcOperatorrole acOperatorrole = new AcOperatorrole();
				acOperatorrole.setId(acOperatorroleId);
				this.daoUtils.getHibernateTemplate().save(acOperatorrole); //执行【保存】
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
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
	public void deleteOperatorToRole(String roleid, Serializable... ids) throws BusinessException {
		String hql = null;
		AcOperatorrole acOperatorrole = null;
		try {
			for (Serializable id : ids) {
				hql = "from AcOperatorrole where id.roleid = '" + roleid + "' and id.operatorid = " + id;
				acOperatorrole = (AcOperatorrole) this.daoUtils.findSingle(hql);
				this.daoUtils.getHibernateTemplate().delete(acOperatorrole);
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
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
	public void saveOrganizationToRole(String roleid, Serializable... ids) throws BusinessException {
		OmPartyroleId oprid = null;
		OmPartyrole pr = null;
		try {
			for (Serializable id : ids) {
				oprid = new OmPartyroleId();
				oprid.setRoleid(roleid);
				oprid.setPartytype("organization");
				oprid.setPartyid(Long.valueOf(String.valueOf(id)));
				
				pr = new OmPartyrole();
				pr.setId(oprid);
				this.daoUtils.getHibernateTemplate().save(pr); //执行【保存】
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
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
	public void deleteOrganizationToRole(String roleid, Serializable... ids) throws BusinessException {
		String hql = null;
		OmPartyrole pr = null;
		try {
			for (Serializable id : ids) {
				hql = "from OmPartyrole where id.roleid = '" + roleid + "' and id.partyid = " + id + " and partytype = 'organization'";
				pr = (OmPartyrole) this.daoUtils.findSingle(hql);
				this.daoUtils.getHibernateTemplate().delete(pr);
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
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
	public void saveGroupToRole(String roleid, Serializable... ids) throws BusinessException {
		OmPartyroleId oprid = null;
		OmPartyrole pr = null;
		try {
			for (Serializable id : ids) {
				oprid = new OmPartyroleId();
				oprid.setRoleid(roleid);
				oprid.setPartytype("workgroup");
				oprid.setPartyid(Long.valueOf(String.valueOf(id)));
				
				pr = new OmPartyrole();
				pr.setId(oprid);
				this.daoUtils.getHibernateTemplate().save(pr); //执行【保存】
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
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
	public void deleteGroupToRole(String roleid, Serializable... ids) throws BusinessException {
		String hql = null;
		OmPartyrole pr = null;
		try {
			for (Serializable id : ids) {
				hql = "from OmPartyrole where id.roleid = '" + roleid + "' and id.partyid = " + id + " and partytype = 'workgroup'";
				pr = (OmPartyrole) this.daoUtils.findSingle(hql);
				this.daoUtils.getHibernateTemplate().delete(pr);
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
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
	public void savePositionToRole(String roleid, Serializable... ids) throws BusinessException {
		OmPartyroleId oprid = null;
		OmPartyrole pr = null;
		try {
			for (Serializable id : ids) {
				oprid = new OmPartyroleId();
				oprid.setRoleid(roleid);
				oprid.setPartytype("position");
				oprid.setPartyid(Long.valueOf(String.valueOf(id)));
				
				pr = new OmPartyrole();
				pr.setId(oprid);
				this.daoUtils.getHibernateTemplate().save(pr); //执行【保存】
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
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
	public void deletePositionToRole(String roleid, Serializable... ids) throws BusinessException {
		String hql = null;
		OmPartyrole pr = null;
		try {
			for (Serializable id : ids) {
				hql = "from OmPartyrole where id.roleid = '" + roleid + "' and id.partyid = " + id + " and partytype = 'position'";
				pr = (OmPartyrole) this.daoUtils.findSingle(hql);
				this.daoUtils.getHibernateTemplate().delete(pr);
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
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
	public void saveDutyToRole(String roleid, Serializable... ids) throws BusinessException {
		OmPartyroleId oprid = null;
		OmPartyrole pr = null;
		try {
			for (Serializable id : ids) {
				oprid = new OmPartyroleId();
				oprid.setRoleid(roleid);
				oprid.setPartytype("duty");
				oprid.setPartyid(Long.valueOf(String.valueOf(id)));
				
				pr = new OmPartyrole();
				pr.setId(oprid);
				this.daoUtils.getHibernateTemplate().save(pr); //执行【保存】
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
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
	public void deleteDutyToRole(String roleid, Serializable... ids) throws BusinessException {
		String hql = null;
		OmPartyrole pr = null;
		try {
			for (Serializable id : ids) {
				hql = "from OmPartyrole where id.roleid = '" + roleid + "' and id.partyid = " + id + " and partytype = 'duty'";
				pr = (OmPartyrole) this.daoUtils.findSingle(hql);
				this.daoUtils.getHibernateTemplate().delete(pr);
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
}
