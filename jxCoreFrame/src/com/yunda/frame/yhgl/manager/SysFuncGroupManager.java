package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.primeton.workflow.commons.utility.StringUtil;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.yhgl.entity.AcFuncgroup;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统应用功能管理-应用功能组业务类
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="sysFuncGroupManager")
public class SysFuncGroupManager extends JXBaseManager <AcFuncgroup, AcFuncgroup> {
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	/** 应用功能 */
	private SysFunctionManager sysFunctionManager;
	
	/**
	 * <li>说明：获取系统应用功能组同步树数据
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
	public List <Map> getFuncGroupByApp(String appid, String funcgroupid) throws BusinessException{
		//根据入参构建查询条件
		if(appid == null) return null;
		String hql = "from AcFuncgroup where appid = " + appid;
		if(funcgroupid == null) hql += " and parentgroup is null order by funcgroupname";
		else 					hql += " and parentgroup = " + funcgroupid + "  order by funcgroupname";
		
		//获取功能组
		List <AcFuncgroup> grouplist = this.daoUtils.getHibernateTemplate().find(hql);
		
		//构建treeNode
		List <Map> nodeAry = null;
		if(grouplist!=null&&grouplist.size()>0){
			nodeAry = new ArrayList();
			Map node = null;
			for(AcFuncgroup group : grouplist){
				node = new LinkedHashMap();
				node.put("id", "b_".concat(String.valueOf(group.getFuncgroupid()))); //ID
				node.put("text", group.getFuncgroupname()); 
				hql = "from AcFuncgroup where appid = " + appid + " and parentgroup = " + group.getFuncgroupid();
				List <AcFuncgroup> nextLevel = this.daoUtils.getHibernateTemplate().find(hql);
				if(nextLevel == null || nextLevel.size()<1) node.put("leaf", true);
				else node.put("leaf", false);
				node.put("funcgroupid", group.getFuncgroupid());
				node.put("appid", group.getAppid());
				node.put("funcgroupname", group.getFuncgroupname());
				node.put("parentgroup", group.getParentgroup());
				node.put("grouplevel", group.getGrouplevel());
				node.put("funcgroupseq", group.getFuncgroupseq());
				node.put("isleaf", group.getIsleaf());
				node.put("subcount", String.valueOf(group.getSubcount()));
				List <Map> children = getFuncGroupByApp(appid,String.valueOf(group.getFuncgroupid()));
				node.put("children", children);
				nodeAry.add(node);
			}
			return nodeAry;
		} else {
			return null;
		}
	}
	
	/**
	 * <li>说明：根据应用功能ID，查询其下是否存在应用功能分组
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-1-2
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param appid 应用功能ID
	 * @return true/false true：有下级分组， false： 无下级分组
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public Boolean hasChildFuncgroup(Long appid){
		Boolean flag = false;
		if(appid == null) return flag;
		String hql = "from AcFuncgroup where appid = " + appid + " and parentgroup is null";
		List <AcFuncgroup> grouplist = this.daoUtils.getHibernateTemplate().find(hql);
		if(grouplist != null && grouplist.size()>0) flag = true;
		return flag;
	}
	
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
	public List <Map> getTreeNodeByApp(Long appid, Long funcgroupid) throws BusinessException{
		//根据入参构建查询条件
		if(appid == null) return null;
		String hql = "from AcFuncgroup t where t.appid = " + appid;
		if(funcgroupid == null) hql += " and t.parentgroup is null order by t.funcgroupname";
		else 					hql += " and t.parentgroup = " + funcgroupid + "  order by t.funcgroupname";
		
		//获取功能组
		List <AcFuncgroup> grouplist = this.daoUtils.getHibernateTemplate().find(hql);
		
		//构建treeNode
		List <Map> nodeAry = null;
		if(grouplist!=null&&grouplist.size()>0){
			nodeAry = new ArrayList();
			Map node = null;
			String sql = null;
			for(AcFuncgroup group : grouplist){
				/** 查询当前分组及其所有下级分组是否已经创建了功能，如果没有功能，这个分组及其下属各子分组则不必再显示在树中 */
				sql = "select count(*) from ac_function where funcgroupid in (select funcgroupid from ( select funcgroupid, parentgroup from ac_funcgroup where appid = " + 
						appid + ") start with funcgroupid = " + group.getFuncgroupid() + " connect by prior funcgroupid = parentgroup ) ";
				List <Object> _t = this.daoUtils.executeSqlQuery(sql);
				if(_t == null || _t.get(0)==null || String.valueOf(_t.get(0)).equals("0")) {
					continue; //如果当前功能组的所有下级工作组都不存在功能， 则该工作组不参与树的构建
				}				
				/** 组装节点 */
				node = new LinkedHashMap();
				node.put("id", "b_".concat(String.valueOf(group.getFuncgroupid()))); //ID
				node.put("text", group.getFuncgroupname()); 
				hql = "from AcFuncgroup where appid = " + appid + " and parentgroup = " + group.getFuncgroupid();
				List <AcFuncgroup> nextLevel = this.daoUtils.getHibernateTemplate().find(hql);
				if(nextLevel == null || nextLevel.size()<1) {
					List <Map> nextLevel2 = sysFunctionManager.getTreeNodeByApp(appid, group.getFuncgroupid());
					if(nextLevel2 == null || nextLevel2.size()<1){
						node.put("leaf", true);
					} else {
						node.put("leaf", false);
					}
				}
				else {node.put("leaf", false);}
				node.put("funcgroupid", group.getFuncgroupid());
				node.put("appid", group.getAppid());
				node.put("funcgroupname", group.getFuncgroupname());
				node.put("parentgroup", group.getParentgroup());
				node.put("grouplevel", group.getGrouplevel());
				node.put("funcgroupseq", group.getFuncgroupseq());
				node.put("isleaf", group.getIsleaf());
				node.put("subcount", String.valueOf(group.getSubcount()));
				node.put("realid", group.getFuncgroupid()); //真实ID
				node.put("nodetype", "group");//节点类型
				node.put("checked", false);
				node.put("parent", group.getParentgroup()==null?appid:group.getParentgroup());
				node.put("children", buildChilds(appid,group.getFuncgroupid()));
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
	public List<Map> buildChilds (Long appid, Long funcgroupid) throws BusinessException {
		List <Map> childsGroup = getTreeNodeByApp(appid, funcgroupid); //功能组
		List <Map> childsFunc  = sysFunctionManager.getTreeNodeByApp(appid, funcgroupid); //功能
		if(childsGroup == null){
			return childsFunc; //查询第一层应用功能分组
		} 
		else if (childsFunc == null) {
			return childsGroup;
		}
		else {
			if(childsFunc == null || childsFunc.size() <1) return childsGroup; //如果功能为空，则直接返回功能组
			if((childsGroup == null || childsGroup.size() <1)) return childsFunc; //功能组为空， 则直接返回功能
			for(Map map : childsFunc){
				childsGroup.add(map); //合并集合
			}
			return childsGroup;
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
	public String[] validateUpdate(AcFuncgroup group, String isAdd){
		if(group==null || StringUtil.isNullOrBlank(isAdd)){
			return new String[]{"保存功能组信息失败！"};
		}
		if(Boolean.valueOf(isAdd)){
			String hql = "from AcFuncgroup where funcgroupid = " + group.getFuncgroupid();// + " and appid = " + group.getAppid();
			AcFuncgroup tgroup = (AcFuncgroup)this.daoUtils.findSingle(hql);
			if(tgroup == null) return null; //通过验证，没有功能组ID重复的现象
			return new String []{"系统中已存在["+group.getFuncgroupid()+"]功能组编号！"}; //返回验证失败信息
		}
		return null;
	}
	
	/**
	 * <li>说明：保存/更新功能组，并更改上级工作组的属性
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param group 功能组实体
	 * @param isAdd 新增操作 true是 false否
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public void saveOrUpdate(AcFuncgroup group, String isAdd) throws BusinessException, NoSuchFieldException {
		if(!StringUtil.isNullOrBlank(isAdd)&&"true".equals(isAdd)){
			this.daoUtils.getHibernateTemplate().save(group); //新增功能组
		} else {
			String hql = "from AcFuncgroup where funcgroupid = " + group.getFuncgroupid();
			AcFuncgroup tgroup = (AcFuncgroup)this.daoUtils.findSingle(hql);
			if(tgroup == null) return;
			if(!tgroup.getFuncgroupname().equals(group.getFuncgroupname())){
				tgroup.setFuncgroupname(group.getFuncgroupname());
			}
			this.daoUtils.getHibernateTemplate().update(tgroup);  //编辑功能组
			return; //编辑操作不需要更新上级功能组
		}
		/** 新增组后，更新该组的上级功能组信息 */
		if(group.getParentgroup() == null) return; //没有上级功能组存在
		AcFuncgroup parentGroup = (AcFuncgroup)this.daoUtils.findSingle("from AcFuncgroup where funcgroupid = " + group.getParentgroup() + " and appid = " + group.getAppid()); //获取当前更新实体的上级功能组实体
		if(parentGroup == null) return; //没有查询到上级功能组【异常情况】
		parentGroup.setIsleaf("n"); //上级功能组不再是叶子
		List <AcFuncgroup> childGroup = this.daoUtils.find("from AcFuncgroup where parentgroup = " + group.getParentgroup() + " and appid = " + group.getAppid()); //获取当前更新实体的同级功能组实体列表
		parentGroup.setSubcount(Long.valueOf(String.valueOf(childGroup.size())));
		this.daoUtils.getHibernateTemplate().update(parentGroup);//更新上级功能组实体
	}	
	
	/**
	 * <li>说明：根据应用主键，删除其下辖的功能组
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param group 功能组实体
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public void deleteGroupByApp(Long appid) throws BusinessException {
		List <AcFuncgroup> list = this.daoUtils.getHibernateTemplate().find("from AcFuncgroup where appid = " + appid + " and parentgroup is null");
		if(list == null || list.size()<1) return;
		String [] groupids = new String [list.size()];
		for(int i=0; i<list.size(); i++){
			groupids[i] = String.valueOf(list.get(i).getFuncgroupid());
		}
		deleteByIds(groupids);
	}
	
	/**
	 * <li>说明：删除功能组，并更改上级工作组的属性,以及删除该组下的功能
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
		Long parentgroup = null;
		try {
			for (Serializable id : ids) {
				//删除当前id对应的功能组
				AcFuncgroup group = (AcFuncgroup)daoUtils.getHibernateTemplate().get(AcFuncgroup.class, Long.valueOf(String.valueOf(id)));  //根据ID获取功能组实体对象
				if(group == null) continue;
				parentgroup = group.getParentgroup(); //提取上级功能组id
				iterationDeleteChildGroup(group); //执行删除操作
				//更新上级工作组状态
				if(parentgroup == null) continue; //不存在上级功能组
				AcFuncgroup parentGroup = (AcFuncgroup)daoUtils.getHibernateTemplate().get(AcFuncgroup.class, parentgroup);  //根据ID获取上层功能组实体对象
				List <AcFuncgroup> childGroup = this.daoUtils.find("from AcFuncgroup where parentgroup = " + parentgroup); //获取当前被删除的功能组实体的同级功能组实体列表
				if(childGroup!=null && childGroup.size()>0){ //如果存在下级功能组
					parentGroup.setIsleaf("n");
					parentGroup.setSubcount(Long.valueOf(String.valueOf(childGroup.size())));
				} else { 
					parentGroup.setIsleaf("y");
					parentGroup.setSubcount(0L);
				}
				this.daoUtils.getHibernateTemplate().update(parentGroup);
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * <li>说明：递归方法，根据入参的功能组，迭代删除其下属所有子功能组
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param group 功能组实体
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private void iterationDeleteChildGroup(AcFuncgroup group) throws BusinessException{
		String hql = "from AcFuncgroup where parentgroup = " + group.getFuncgroupid();
		List <AcFuncgroup> childGroup = this.daoUtils.find(hql); //获取入参功能组的下级子功能组
		if(childGroup==null||childGroup.size()<1) {
			sysFunctionManager.deleteFuncByGroup(group.getFuncgroupid()); //删除入参功能组的下辖功能
			this.daoUtils.getHibernateTemplate().delete(group); //【执行删除功能组】
			return; //如果未找到子项，跳出递归
		}
		for(AcFuncgroup _o : childGroup){
			iterationDeleteChildGroup(_o); //递归调用，继续向下层查找
		}
		sysFunctionManager.deleteFuncByGroup(group.getFuncgroupid()); //删除入参功能组的下辖功能
		this.daoUtils.getHibernateTemplate().delete(group); //【执行删除功能组】
	}

	public SysFunctionManager getSysFunctionManager() {
		return sysFunctionManager;
	}

	public void setSysFunctionManager(SysFunctionManager sysFunctionManager) {
		this.sysFunctionManager = sysFunctionManager;
	}
	
	
}
