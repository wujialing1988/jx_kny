package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcApplication;
import com.yunda.frame.yhgl.entity.AcRolefunc;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统应用功能管理-应用业务类
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="sysAppManager")
public class SysAppManager extends JXBaseManager <AcApplication, AcApplication>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 应用功能 */
	private SysFunctionManager sysFunctionManager;
	
	/** 应用功能组 */
	private SysFuncGroupManager sysFuncGroupManager;
	
	/** 角色功能对应关系表*/
	private SysRoleFuncManager sysRoleFuncManager;
	
	/**
	 * <li>说明：获取系统应用功能
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
	public List <Map> getTreeNodeByApp() throws BusinessException{
		String hql = "from AcApplication t where ((select count(*) from AcFuncgroup where appid = t.appid)>0) order by appcode"; //只查询存在下级功能组的应用
		List <AcApplication> applist = this.daoUtils.getHibernateTemplate().find(hql);
		List <Map> nodeAry = null;
		
		if(applist!=null&&applist.size()>0){
			nodeAry = new ArrayList();
			Map node = null;
			String sql = null;
			for(AcApplication app : applist){
				/** 查询当前应用下的功能组是否拥有功能 */
				sql = "select count(*) from ac_function where funcgroupid in (select funcgroupid from ac_funcgroup where appid = " + app.getAppid() + ")";
				List <Object> _t = this.daoUtils.executeSqlQuery(sql);
				if(_t == null || _t.get(0)==null || String.valueOf(_t.get(0)).equals("0")) {
					continue; //如果当前应用下的所有功能组都没有下属的功能，则该应用不显示
				}				
				/** 构建树 */
				node = new LinkedHashMap();
				node.put("id", "a_".concat(String.valueOf(app.getAppid()))); //ID
				node.put("text", app.getAppname()); 
				node.put("leaf", !sysFuncGroupManager.hasChildFuncgroup(app.getAppid()));
				node.put("appid", String.valueOf(app.getAppid()));
				node.put("appcode", app.getAppcode());
				node.put("appname", app.getAppname());
				node.put("apptype", app.getApptype());
				node.put("isopen", app.getIsopen());
				node.put("realid", app.getAppid()); //真实ID
				node.put("nodetype", "app");//节点类型
				node.put("checked", false);
				node.put("parent", "");
				node.put("children", sysFuncGroupManager.getTreeNodeByApp(app.getAppid(), null));
				nodeAry.add(node);
			}
			return nodeAry;
		} else {
			return null;
		}
	}
	
	/**
	 * <li>说明：构建系统应用功能权限树
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public List<Map> buildSysAppTree () throws BusinessException {
		return getTreeNodeByApp(); //查询根层应用
	}
	
	
	
	/**
	 * <li>说明：更新权限树的显示， 将具备权限的功能、功能组、应用勾上
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void updatePowerTreeView(String roleid, List<Map> funcTree) throws BusinessException {
		List <AcRolefunc> powers = sysRoleFuncManager.findRoleFunc(roleid); //根据角色id，获取的该角色对应的应用、功能组、功能
		if(powers == null || powers.size() <1) return;
		List list = sysRoleFuncManager.findFuncGroupByChildGroup(powers); //获取当前角色下，功能组的路径
		if(list == null || list.size() <1) return;
		updateChildMap(funcTree, powers, list);
	}
	
	/**
	 * <li>说明：递归获取下层节点信息，并根据数据内容设置回显
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void updateChildMap(List<Map> funcTree, List <AcRolefunc> powers, List list) throws BusinessException {
		List <Map> childList = null;
		for(Map map : funcTree){
			if(map.get("nodetype") == null || StringUtil.isNullOrBlank(String.valueOf(map.get("nodetype")))) continue; //当前节点没有标注类型
			String nodetype = String.valueOf(map.get("nodetype"));
			if(nodetype.equals("app") && sysRoleFuncManager.isCrtApp(Long.valueOf(String.valueOf(map.get("realid"))),powers)){
				map.put("checked", true);
			} 
			else if(nodetype.equals("group") && sysRoleFuncManager.isCrtGroup(Long.valueOf(String.valueOf(map.get("realid"))),list)){
				map.put("checked", true);
			}
			else if(nodetype.equals("func") && sysRoleFuncManager.isCrtFunc(String.valueOf(map.get("realid")),powers)){
				map.put("checked", true);
			}
			childList = (List<Map>)map.get("children");
			if(childList!=null&&childList.size()>0)
				updateChildMap(childList, powers, list);
				
		}
	}
	
	/**
	 * <li>说明：删除应用，以及删除该应用下的功能组及功能
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 功能组主键集合
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public void deleteByIds(Serializable... ids) throws BusinessException {
		try {
			for (Serializable id : ids) {
				//删除当前id对应的应用
				AcApplication app = (AcApplication)daoUtils.getHibernateTemplate().get(AcApplication.class, Long.valueOf(String.valueOf(id)));  //根据ID获取应用实体对象
				sysFuncGroupManager.deleteGroupByApp(app.getAppid()); //调用方法删除应用下的功能组、功能
				this.daoUtils.getHibernateTemplate().delete(app); //【执行删除】
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * <li>说明：更新前验证
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-1-7
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	public String[] validateUpdate(AcApplication t){
		if(t==null){
			return new String[]{"保存应用信息失败！"};
		}
		String hql = "from AcApplication where appcode = '" + t.getAppcode() + "'";
		if(t.getAppid()!=null){
			//编辑验证
			hql = hql.concat(" and appid <> " + t.getAppid());
		}
		AcApplication _t = (AcApplication)this.daoUtils.findSingle(hql);
		if(_t!=null) return new String [] {"系统中已存在["+_t.getAppcode()+"]应用代码！"};
		else return null;
	}
	
	/**
	 * <li>说明：根据isAdd参数，判断是否是新增操作
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-01-07
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param T t 实体对象
	 * @param isAdd true新增 false编辑
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void saveOrUpdate(AcApplication t) throws BusinessException, NoSuchFieldException {
		if(t!=null && t.getAppid()!=null){
			//编辑操作
			this.daoUtils.getHibernateTemplate().update(t);
		}
		else {
			//新增操作
			String sql = "select nvl(max(appid),0)+1 from Ac_Application";
			List <Object> list = (List<Object>)this.daoUtils.executeSqlQuery(sql);
			if(list!=null && list.get(0)!=null && !StringUtil.isNullOrBlank(String.valueOf(list.get(0)))){
				t.setAppid(Long.valueOf(String.valueOf(list.get(0))));
			}
			this.daoUtils.getHibernateTemplate().save(t);
		}
	}	
	

	public SysFuncGroupManager getSysFuncGroupManager() {
		return sysFuncGroupManager;
	}

	public void setSysFuncGroupManager(SysFuncGroupManager sysFuncGroupManager) {
		this.sysFuncGroupManager = sysFuncGroupManager;
	}

	public SysFunctionManager getSysFunctionManager() {
		return sysFunctionManager;
	}

	public void setSysFunctionManager(SysFunctionManager sysFunctionManager) {
		this.sysFunctionManager = sysFunctionManager;
	}
	
	public SysRoleFuncManager getSysRoleFuncManager() {
		return sysRoleFuncManager;
	}

	public void setSysRoleFuncManager(SysRoleFuncManager sysRoleFuncManager) {
		this.sysRoleFuncManager = sysRoleFuncManager;
	}
}
