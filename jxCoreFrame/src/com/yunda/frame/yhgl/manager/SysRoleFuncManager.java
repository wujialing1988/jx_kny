package com.yunda.frame.yhgl.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.AcRolefunc;
import com.yunda.frame.yhgl.entity.AcRolefuncId;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 角色管理-角色与功能关系业务类
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="sysRoleFuncManager")
public class SysRoleFuncManager extends JXBaseManager <AcRolefunc, AcRolefunc>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * <li>说明：获取系统功能
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public List <AcRolefunc> findRoleFunc(String roleid) throws BusinessException{
		String hql = "from AcRolefunc where id.roleid = '" + roleid + "'";
		List <AcRolefunc> list = this.daoUtils.getHibernateTemplate().find(hql);
		return list;
	}
	
	public List findFuncGroupByChildGroup(List <AcRolefunc> RoleFuncList) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct funcgroupid from ac_funcgroup start with funcgroupid in (");
		for(AcRolefunc rf : RoleFuncList){
			sql.append(rf.getFuncgroupid()).append(",");
		}
		sql.delete(sql.length()-1, sql.length()).append(")");
		sql.append(" connect by prior parentgroup = funcgroupid");
		List list = this.daoUtils.executeSqlQuery(sql.toString());
		return list;
	}
	
	/**
	 * <li>说明：传入appid， 在角色-应用关系列表中查找，找到后返回true
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param appid 应用id
	 * @param RoleFuncList 角色-应用关系
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Boolean isCrtApp(Long appid, List <AcRolefunc> RoleFuncList) throws BusinessException {
		Boolean flag = false;
		for(AcRolefunc rf : RoleFuncList){
			if(String.valueOf(rf.getAppid()).equals(String.valueOf(appid))){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * <li>说明：传入groupid， 在角色-应用关系列表中查找，找到后返回true
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 应用功能组id
	 * @param RoleFuncList 角色-应用关系
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Boolean isCrtGroup(Long groupid, List list) throws BusinessException {
		Boolean flag = false;
		for(Object obj : list){
			if(obj!=null&&String.valueOf(obj).equals(String.valueOf(groupid))) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * <li>说明：传入funccode， 在角色-应用关系列表中查找，找到后返回true
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param funccode 应用code
	 * @param RoleFuncList 角色-应用关系
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Boolean isCrtFunc(String funccode, List <AcRolefunc> RoleFuncList) throws BusinessException {
		Boolean flag = false;
		for(AcRolefunc rf : RoleFuncList){
			if(rf.getId().getFunccode().equals(funccode)){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * <li>说明：删除角色原有权限之后，再保存
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param roleid 角色ID
	 * @param RoleFuncList 角色-应用关系
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public void saveRoleAndFunc(String roleid, AcRolefunc [] acRolefuncAry) throws BusinessException {
		String sql = "delete Ac_Rolefunc where roleid = '" + roleid + "'";
		this.daoUtils.executeSql(sql);
		List<AcRolefunc> listfun = new ArrayList<AcRolefunc>();
		AcRolefunc acRolefunc = null;
		AcRolefuncId acRolefuncId = null;
		for(AcRolefunc _acRolefunc : acRolefuncAry){
			acRolefuncId = new AcRolefuncId();
			acRolefuncId.setRoleid(roleid);
			acRolefuncId.setFunccode(_acRolefunc.getFunccode());
			
			acRolefunc = new AcRolefunc();
			acRolefunc.setId(acRolefuncId);
			acRolefunc.setAppid(_acRolefunc.getAppid());
			acRolefunc.setFuncgroupid(_acRolefunc.getFuncgroupid());
			listfun.add(acRolefunc);
		}
		this.daoUtils.bluckInsert(listfun);
//		String saveSQL = null;
//		for(AcRolefunc acRolefunc : acRolefuncAry){
//			saveSQL = "insert into Ac_Rolefunc (roleid, funccode, funcgroupid, appid) values ('"+roleid+"', '"+acRolefunc.getFunccode()+"',"+acRolefunc.getFuncgroupid()+","+acRolefunc.getAppid()+")";
//			this.daoUtils.executeSql(saveSQL);
//		}
	}
}
