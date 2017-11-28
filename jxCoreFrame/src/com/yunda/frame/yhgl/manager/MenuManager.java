package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcFuncgroup;
import com.yunda.frame.yhgl.entity.AcMenu;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.AcRole;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 菜单业务处理类
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-12-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="menuManager")
public class MenuManager extends JXBaseManager<AcMenu, AcMenu> implements IAcMenuManager{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());	
	
	@Resource(name="acMenuManager")
	private IAcMenuManager acMenuManager;

	/**
	 * <li>说明：判定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-12-18
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return true使用查询缓存，false不使用
	 */
	@Override
	protected boolean enableCache(){
		return true;
	}
	
	/**
	 * 
	 * <li>方法名：findAcMenuByRoles
	 * <li>@param acOperator
	 * <li>@param acRoles
	 * <li>@param isTop
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：List<AcMenu>
	 * <li>说明：获取登录人员对应权限的菜单
	 * <li>创建人：田华
	 * <li>创建日期：2011-6-28
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<AcMenu> findAcMenuByRoles(AcOperator acOperator, List<AcRole> acRoles,boolean isTop) throws BusinessException {
		List list = null;
		StringBuffer sbRoleIds = new StringBuffer();
		if(acRoles != null){
			for(AcRole acRole : acRoles){
				sbRoleIds.append(",'");
				sbRoleIds.append(acRole.getRoleid());
				sbRoleIds.append("'");
			}
		}
		StringBuffer hql = new StringBuffer();
		
		hql.append(" SELECT DISTINCT k.menuid,k.menuname,k.parentsid,k.subcount,k.isleaf,k.menuaction,k.menulevel,k.displayorder,k.funccode from (SELECT m.* FROM Ac_Menu m START WITH ");
		hql.append(" ( m.funccode IN ( "); 
		if(acOperator.getOperatorid() != null){
			hql.append(" SELECT DISTINCT f.funccode FROM Ac_Function f,Ac_Operfunc a ");
			hql.append(" WHERE f.funccode = a.funccode AND a.authtype='y' AND a.operatorid = " + acOperator.getOperatorid());            
		}
		hql.append(")");
		if (!"".equals(sbRoleIds) && sbRoleIds.length() > 0) {
			hql.append(" OR ");
			hql.append(" m.funccode IN ( "); 
			hql.append(" SELECT DISTINCT f.funccode FROM Ac_Function f ,Ac_Rolefunc rf ");
			hql.append(" WHERE rf.funccode = f.funccode AND f.ismenu = 'y' AND rf.roleid IN ("+sbRoleIds.toString().replaceFirst(",", "")+")");
			hql.append(")");
		}
		hql.append(")");
		if(acOperator.getOperatorid() != null){
			hql.append(" AND "); 
			hql.append(" m.funccode NOT IN(SELECT b.funccode FROM Ac_Operfunc b WHERE b.operatorid= " + acOperator.getOperatorid() + " AND b.authtype='n')");
		}
		hql.append(" CONNECT BY PRIOR m.parentsid=m.menuid ) k");
		if(isTop){
			hql.append(" where k.parentsid is null ");
		}else{
			hql.append(" where k.parentsid is not null ");
		}
		hql.append(" and k.menucode like 'YD%' ");
		hql.append(" order by k.menulevel ,k.displayorder ,k.isleaf ");
		
		list = daoUtils.executeSqlQuery(hql.toString());
		List<AcMenu> menulist = null;
		if(list!=null&&list.size()>0){
			menulist = new ArrayList<AcMenu>();
			AcMenu acMenu = null;
			Object[] obj = null;
			for(int i = 0;i<list.size();i++){
				acMenu = new AcMenu();
				obj = (Object[]) list.get(i);
				acMenu.setMenuid(obj[0]+"");
				acMenu.setMenuname(obj[1]+"");
				acMenu.setParentsid(obj[2]+"");
				acMenu.setSubcount((obj[3]==null)?0:Long.parseLong(obj[3]+""));
				acMenu.setIsleaf(obj[4]+"");
				acMenu.setMenuaction(obj[5]+"");
				acMenu.setFunccode(obj[8]+"");
				menulist.add(acMenu);
			}
		}
		return menulist;
	}
//	/**
//	 * <li>方法名：createMenuTreeXml
//	 * <li>@param menuList 
//	 * <li>@return 
//	 * <li>返回类型：Map<String,String>
//	 * <li>说明：
//	 * <li>创建人：曾锤鑫
//	 * <li>创建日期：2011-2-18
//	 * <li>修改人： 
//	 * <li>修改日期：
//	 */
//	public Map<String, String> createMenuTreeXml(HttpServletRequest req,AcOperator acOperator, List<AcMenu> menuList)  throws BusinessException {
//		Map<String, String> treeXMLMap = new HashMap<String, String>();
//		List<TreeNode> nodeList = new ArrayList<TreeNode>();
//
//		TreeNode node;
//		
//		String funccode = null;
//		//String menuParam = null;
//		StringBuffer menuUrl = null;
//		for (AcMenu menu : menuList) {// 将目录菜单及链接菜单转换为树节点
//			node = new TreeNode(menu.getMenuid(), menu.getMenuname());
//			node.setCasCode(StringUtils.isBlank(menu.getParentsid()) ? "-" : menu.getParentsid());// 设置级联编码便于建立父子关系
//			if (menu.getSubcount()!=null&&!StringUtils.isBlank(menu.getIsleaf())&&menu.getSubcount() == 0 && "y".equals(menu.getIsleaf().toLowerCase())) {// 不包含有子菜单的情况
//				node.setLoadChild(true);// 加载子节点
//				node.setOpen(false);
//				node.setImgLeaf(TreeNode.IMGLEAF);
//				
//				funccode = menu.getFunccode();
//				menuUrl = new StringBuffer(StringUtils.isBlank(menu.getMenuaction()) ? "" : menu.getMenuaction().replaceAll("&", "|"));  //使用“&”替换“|”，否则dhtmlxtree解析出错
//				//menuParam = menu.getParameter();
//
//				//if(!StringUtils.isBlank(menuParam)){
//				//	menuUrl.append((menuUrl.toString().indexOf("?") > -1) ? ("&" + menuParam) : ("?" + menuParam));
//				//}
//				
//				//附带menu的funccode
//				menuUrl.append(((menuUrl.toString().indexOf("?") > -1) ? "|" : "?") + "funccode=" + funccode);
//				node.setUrl(menuUrl.toString());
//			}else{
//				node.setHasChild(true);
//				node.setLoadChild(true);
//				node.setOpen(false);
//			}
//			nodeList.add(node);
//		}
//		
//		if("default".equals(acOperator.getMenutype())  //default布局
//				|| StringUtils.isBlank(acOperator.getMenutype()) ){
//			treeXMLMap.put("sysMenuTree", TreeHelper.getTree("sysMenuTree", nodeList));
//		}else if("outlook".equals(acOperator.getMenutype())
//				|| "tabs".equals(acOperator.getMenutype())){  //outlook布局、tabs布局（数据格式相同，各个子模块为独立菜单树）
//			Collection<TreeNode> treeNodes = TreeHelper.setUpTreeNode(nodeList);
//			
//			//if("tabs".equals(acOperator.getMenutype())){
//				Object[] menuObj = treeNodes.toArray(); //如果布局设置为“outlook”或“tabs”，则需要设置第一次装载的菜单信息
//				if(menuObj != null && menuObj.length > 0){
//					req.setAttribute("firstMenuTreeNode", (TreeNode)menuObj[0]);
//				}
//			//}
//			
//			for(TreeNode tnode : treeNodes){
//				/*treeXMLMap.put(
//						"sysMenuTree_" + tnode.getId(), 
//						TreeHelper.getTreeXML("0", tnode.getChildNodes()));*/
//
//				treeXMLMap.put(
//						"sysMenuTree_" + tnode.getId(), 
//						TreeHelper.getTreeXML("sysMenuTree_" + tnode.getId(), tnode.getChildNodes()));
//			}
//		}
//		return treeXMLMap;
//	}
	
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
	protected void checkUpdate(AcMenu entity) throws BusinessException {
		String hqlName = "from AcMenu m where m.menuname='" + entity.getMenuname() + "' ";
		String hqllabel = "from AcMenu m where m.menulabel='" + entity.getMenulabel() + "' ";
		if(entity.getMenuid()!=null){
			String hql= " and m.menuid <> '" +entity.getMenuid()+ "' ";
			hqlName+=hql;
			hqllabel+=hql;
		}
		if(entity.getParentsid()!=null){
			String hql= " and m.parentsid = '" +entity.getParentsid()+ "' ";
			hqlName+=hql;
			hqllabel+=hql;
		}else{
			String hql= " and m.parentsid is null ";
			hqlName+=hql;
			hqllabel+=hql;
		}
		if(daoUtils.isExist(hqlName)){
			throw new BusinessException("菜单名称【"+ entity.getMenuname() +"】已存在！");
		}else if(daoUtils.isExist(hqllabel)){
			throw new BusinessException("菜单显示名称【"+ entity.getMenulabel() +"】已存在！");
		}
	}
	/**
	 * 
	 * <li>方法名：checkInsert
	 * <li>@param entity
	 * <li>@throws BusinessException
	 * <li>返回类型：void
	 * <li>说明：
	 * <li>创建人：田华
	 * <li>创建日期：2011-10-9
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	protected void checkInsert(AcMenu entity) throws BusinessException {
		String hqlName = "from AcMenu m where m.menuname='" + entity.getMenuname() + "' ";
		String hqllabel = "from AcMenu m where m.menulabel='" + entity.getMenulabel() + "' ";
		String hqlid = "from AcMenu m where m.menuid='" +entity.getMenuid()+ "' ";
		if(entity.getParentsid()!=null){
			String hql= " and m.parentsid = '" +entity.getParentsid()+ "' ";
			hqlName+=hql;
			hqllabel+=hql;
		}else{
			String hql= " and m.parentsid is null ";
			hqlName+=hql;
			hqllabel+=hql;
		}
		if(daoUtils.isExist(hqlid)){
			throw new BusinessException("菜单编号【"+ entity.getMenuid() +"】已存在！");
		}else if(daoUtils.isExist(hqlName)){
			throw new BusinessException("菜单名称【"+ entity.getMenuname() +"】已存在！");
		}else if(daoUtils.isExist(hqllabel)){
			throw new BusinessException("菜单显示名称【"+ entity.getMenulabel() +"】已存在！");
		}
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
	 * 
	 * <li>方法名：update
	 * <li>@param entity
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：AcMenu
	 * <li>说明：
	 * <li>创建人：田华
	 * <li>创建日期：2011-4-15
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public AcMenu update(AcMenu entity)throws BusinessException {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM AcMenu m ");
		if(!StringUtils.isBlank(entity.getMenuid()))
		{
			hql.append(" WHERE m.parentsid= '"+entity.getMenuid()+"'");
			List<AcMenu> acMenus = daoUtils.find(hql.toString());
			if(acMenus!=null&&acMenus.size()>0){
				entity.setSubcount((long)acMenus.size());
			}else{
				entity.setSubcount((long)0);
			}
		}else{
			entity.setSubcount((long)0);
		}
		
//		this.checkUpdate(entity);
		return (AcMenu) daoUtils.saveOrUpdate(entity);
	}
	
	/**
	 * 
	 * <li>方法名：insert
	 * <li>@param entity
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：AcMenu
	 * <li>说明：
	 * <li>创建人：田华
	 * <li>创建日期：2011-4-15
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public AcMenu insert(AcMenu entity)throws BusinessException {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM AcMenu m ");
		String sql = "FROM AcMenu m WHERE m.menuid= '"+entity.getParentsid()+"'";
		AcMenu acMenu = (AcMenu) daoUtils.findSingle(sql);
		if(!StringUtils.isBlank(entity.getMenuid()))
		{
			hql.append(" WHERE m.parentsid= '"+entity.getMenuid()+"'");
			List<AcMenu> acMenus = daoUtils.find(hql.toString());
			if(acMenus!=null&&acMenus.size()>0){
				entity.setSubcount((long)acMenus.size());
			}else{
				entity.setSubcount((long)0);
			}
		}else{
			entity.setSubcount((long)0);
		}
		
//		this.checkInsert(entity);
		if(acMenu!=null){
			if(acMenu.getMenuseq()!=null){
				entity.setMenuseq(acMenu.getMenuseq()+acMenu.getMenuid());
			}
		}
		entity = (AcMenu) daoUtils.insert(entity);
		return entity;
	}
	/**
	 * <li>方法名：getMenuCount
	 * <li>@param menuid
	 * <li>@return count
	 * <li>返回类型：int
	 * <li>说明：统计下级菜单数量
	 * <li>创建人：张凡
	 * <li>创建日期：2012-07-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public int getMenuCount(String menuid){
		String sql="select count(1) from Ac_menu where parentsid='"+menuid+"'";
		return Integer.valueOf(daoUtils.executeSqlQuery(sql).get(0).toString());
	}
	
	/**
	 * <li>方法名称：getCascadeIdx
	 * <li>方法说明：查询级连IDX
	 * <li>@param idxs
	 * <li>@return
	 * <li>@throws NullPointerException
	 * <li>返回类型：String
	 * <li>创建人：张凡
	 * <li>创建时间：2012-8-1
	 */
	public String getCascadeIdx(String idxs) throws NullPointerException{
		String sql="select menuid from ac_menu start with menuid in ("+idxs+") connect by prior menuid=parentsid";
		List list = daoUtils.executeSqlQuery(sql);
		StringBuffer sb=new StringBuffer();
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				sb.append(list.get(i)+",");
			}
			return sb.toString().substring(0, sb.toString().length() - 1);
		}
		return null;
	}
	
	/**
	 * <li>说明： 递归删除入参id对应的菜单及其下级菜单
	 * <li>@param id 菜单id
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public void excuteDeleteMenu(String id) throws BusinessException {
		try {
			//查找待删除的菜单的下级菜单信息
			List <AcMenu> childMenuList = acMenuManager.findChildsById(id);
			if(childMenuList!=null && childMenuList.size()>0){
				for(AcMenu child : childMenuList){
					excuteDeleteMenu(child.getMenuid());
				}
			}
			this.daoUtils.getHibernateTemplate().delete(acMenuManager.getModelById(id));
			return;
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * <li>说明： 批量删除菜单
	 * <li>@param ids 菜单ids集合
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-01-20
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public void deleteByIds(Serializable... ids) throws BusinessException {
		for(Serializable id : ids){
			AcMenu menu = acMenuManager.getModelById(String.valueOf(id)); //获取菜单实体
			if(!StringUtil.isNullOrBlank(menu.getParentsid())){
				menu = acMenuManager.getModelById(menu.getParentsid()); //获取上级菜单
				Long subcount = menu.getSubcount()-1<0?0:menu.getSubcount()-1; //子菜单总数
				menu.setSubcount(subcount);
				menu.setIsleaf(subcount>0?"n":"y"); //是否叶子
				this.daoUtils.getHibernateTemplate().update(menu); //更新父菜单
			}
			excuteDeleteMenu(String.valueOf(id)); //递归删除子集
		}
	}
	
	
	
//	/**
//	 * <li>方法说明: 重写父类中的deleteByIds删除方法, 在菜单管理中,删除子项菜单后, 检查是否还存在同级别的菜单, 
//	 *              如果没有了, 则将父节点的isleaf(是否叶子节点设为"y")
//	 * <li>创建人：谭诚
//	 * <li>创建时间：2013-7-11
//	 */
//	@SuppressWarnings("unchecked")
//	public void deleteByIds(Serializable... ids) throws BusinessException {
//		try {
//			for (Serializable id : ids) {
//				checkDelete(id);
//			}
//			//1. 根据当前传递的参数,在数据库中查找要删除的菜单的父项菜单id
//			List<AcMenu> menulist = null;
//			StringBuffer hql = new StringBuffer();
//			StringBuffer temp = new StringBuffer();
//			hql.append("from AcMenu where menuid in");
//			temp.append(" (");
//			for (int i = 0; i < ids.length; i++) {
//				temp.append("'" + ids[i] + "'");
//				if (i != ids.length - 1)
//					temp.append(",");
//			}
//			temp.append(")");
//			hql.append(temp.toString());
//			menulist = daoUtils.find(hql.toString());
//			AcMenu acMenu = null;
//			if(menulist!=null&&menulist.size()>0){
//				for(int i=0;i<menulist.size();i++){
//					acMenu = menulist.get(i);
//					if(acMenu.getParentsid()!=null){
//						break;
//					}
//				}
//			}
//			//2. 执行删除操作
//			daoUtils.removeByIds(ids, entityClass, getModelIdName(entityClass));// 按ID删除
//			
//			//3. 更新父菜单的属性:根据先前记录的父项菜单Id, 在数据库中查询其是否仍存在子菜单, 如果没有, 则将父项菜单的isleaf字段设置为y
//			//   特别注意:因为这个函数是一个事物, 执行到这里时需要排除掉本次准备删除的这部分数据
//			hql = new StringBuffer();
//			hql.append("select count(*) from AcMenu where parentsid = '").append(acMenu.getParentsid()).append("' and menuid not in ").append(temp.toString());
//			List childAry = daoUtils.find(hql.toString());
//			if(childAry!=null&&childAry.size()>0&&Integer.parseInt(String.valueOf(childAry.get(0)))==0){
//				hql = new StringBuffer();
//				hql.append("update AcMenu set isleaf = 'y' where menuid = '").append(acMenu.getParentsid()).append("'");
//				daoUtils.executUpdateOrDelete(hql.toString());
//			}
//			// daoUtils.removeObject(ids, entityClass);// 按对象删除
//		} catch (Exception ex) {
//			throw new BusinessException(ex);
//		}
//	}
	/**
	 * <li>说明：根据功能编号, 从功能表中查询其所属功能组, 再从功能组中查询出应用ID
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-7-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param funccode 功能编号
	 * @return AC_FUNCGROUP.APPID
	 */
	@SuppressWarnings("unchecked")
	public Long findApplicationId(String funccode){
		StringBuffer buff = new StringBuffer();
		buff.append("from AcFuncgroup where funcgroupid = (");
		buff.append("select funcgroupid from AcFunction where funccode = '");
		buff.append(funccode);
		buff.append("') ");
		List <AcFuncgroup> list = daoUtils.getHibernateTemplate().find(buff.toString());
		if(list!=null&&list.size()>0){
			AcFuncgroup ag = list.get(0);
			return ag.getAppid();
		}
		return null;
	}
	
	//统一查询接口
	/**
	 * <br/><li>说明：QBE查询,非分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menu 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 对象集合
	 * <br/>调用示例:
	 *  <br/>AcMenu menu = new AcMenu();
	 *  String [] excludeColumn = new String[]{"groupid","createtime","lastupdate","updator","isleaf","subcount"};
	 *	<br/>menu.setOrgcode("4");
	 *	<br/>Order order = Order.asc("menuid");
	 *	<br/>Order [] or = new Order[1];
	 *	<br/>or[0] = order;
	 *  <br/>list = acMenuManager.findByEntity(menu,excludeColumn, or,true);
	 */
	@SuppressWarnings("unchecked")
	public List<AcMenu> findByEntity(AcMenu menu, Order[] orders, final Boolean isExact){
		String [] excludeColumn = new String[]{"menuid","isleaf","subcount"};//过滤查询字段
		return super.findByEntity(menu, excludeColumn, orders, isExact);
	}
	
	/**
	 * <br/><li>说明：QBE查询,支持分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 分页对象
	 * <br/>调用示例:
	 *  <br/>SearchEntity <AcMenu> entity = new SearchEntity<AcMenu>();
	 *  String [] excludeColumn = new String[]{"groupid","createtime","lastupdate","updator","isleaf","subcount"};
	 *  <br/>entity.setEntity(menu);
	 *	<br/>entity.setStart(start);
	 *	<br/>entity.setLimit(limit);
	 *	<br/>page = acMenuManager.findByEntity(entity,excludeColumn,true);
	 */
	@SuppressWarnings("unchecked")
	public Page<AcMenu> findByEntity(SearchEntity<AcMenu> searchEntity, Boolean isExact){
		String [] excludeColumn = new String[]{"menuid","isleaf","subcount"};//过滤查询字段
		return super.findByEntity(searchEntity,excludeColumn,isExact);
	}
	
	/**
	 * <br/><li>说明： 查询顶层模块菜单
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期:
	 * <br/><li>修改内容：
	 * @return 菜单实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<AcMenu> findRoot(){
		String hql = SqlMapUtil.getSql(XMLNAME_MENU.concat("findRoot"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.find(true, hql); //利用查询缓存
		} else {
			return daoUtils.getHibernateTemplate().find(hql); //不使用查询缓存
		}
	}

	/**
	 * <br/><li>说明： 递归查询入参菜单id下属所有子菜单
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期:
	 * <br/><li>修改内容：
	 * @param menuid 菜单Id
	 * @param hasSelf 是否包含参数menuid本身
	 * @return 菜单实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<AcMenu> findAllChilds(String menuid, boolean hasSelf) {
		if(StringUtil.isNullOrBlank(menuid)) throw new RuntimeException("参数异常: 入参menuid为空");
		String sql = SqlMapUtil.getSql(XMLNAME_MENU.concat("findAllChilds"));
		if(StringUtil.isNullOrBlank(sql)) throw new RuntimeException("SQL语句读取异常");
		if(hasSelf) sql = sql.replaceFirst("[?]", ""); //包含menuid
		else sql = sql.replaceFirst("[?]", menuid);    //不包含menuid
		sql = sql.replaceFirst("[?]", menuid);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.cacheSqlQueryEntity(sql, AcMenu.class); //使用缓存
		} else {
			return daoUtils.executeSqlQueryEntity(sql, AcMenu.class); //不使用缓存
		}
	}

	/**
	 * <br/><li>说明： 分页递归查询入参菜单id下属所有子菜单
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期:
	 * <br/><li>修改内容：
	 * @param menuid 菜单Id
	 * @param hasSelf 是否包含参数menuid本身
	 * @return 菜单实体分页列表
	 */
	public Page<AcMenu> findAllChilds(String menuid, boolean hasSelf, Integer start, Integer limit) {
		if(StringUtil.isNullOrBlank(menuid)) throw new RuntimeException("参数异常: 入参menuid为空");
		String sql = SqlMapUtil.getSql(XMLNAME_MENU.concat("findAllChilds"));
		if(StringUtil.isNullOrBlank(sql)) throw new RuntimeException("SQL语句读取异常");
		if(hasSelf) sql = sql.replaceFirst("[?]", ""); //包含menuid
		else sql = sql.replaceFirst("[?]", menuid);    //不包含menuid
		sql = sql.replaceFirst("[?]", menuid);
		String totalSql = "select count(*) as \"rowcount\" from (".concat(sql).concat(")"); //总条数
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalSql, sql, start, limit, AcMenu.class); //使用缓存
		} else {
			return super.findPageList(totalSql, sql, start, limit, null, null);  //不使用缓存
		}
	}

	/**
	 * <br/><li>说明： 根据参数查询中层或者末层菜单
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期:
	 * <br/><li>修改内容：
	 * @param isLeaf 是否叶子菜单, y是n否
	 * @return 菜单实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<AcMenu> findBranchOrLeaf(String isLeaf) {
		if(StringUtil.isNullOrBlank(isLeaf)) throw new RuntimeException("参数异常: 入参isLeaf为空");
		if(!"Y".equals(isLeaf.toUpperCase())&&!"N".equals(isLeaf.toUpperCase())) throw new RuntimeException("参数异常: 入参isLeaf应为\"Y(y)\"或者\"N(n)\"");
		String hql = SqlMapUtil.getSql(XMLNAME_MENU.concat("findBranchOrLeaf"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		Object[] param = new Object[]{isLeaf};
		List <AcMenu> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		return list;
	}
	
	/**
	 * <br/><li>说明： 根据菜单Id查询唯一对应的菜单项.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuid 菜单id
	 * @return 菜单实体
	 */
	public AcMenu getModelById(String menuid){
		return (AcMenu)daoUtils.getHibernateTemplate().get(AcMenu.class, menuid); //利用查询缓存
	}
	
	/**
	 * <br/><li>说明：根据多个菜单Id,查询与对应的菜单项
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个menuid, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuIds 菜单id字符串
	 * @return 菜单实体列表
	 */
	@SuppressWarnings("unchecked")
	public List <AcMenu> findByIds(String menuIds){
		if(StringUtil.isNullOrBlank(menuIds)) throw new RuntimeException("参数异常: 入参menuIds为空");
		String _t = "'".concat(menuIds.replaceAll(",", "','")).concat("'");
		String hql = SqlMapUtil.getSql(XMLNAME_MENU.concat("findByIds")).replace("?", _t);  
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.find(true, hql); //利用查询缓存
		} else {
			return daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
	}
	
	/**
	 * <br/><li>说明：根据多个菜单Id,查询与对应的菜单项
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个menuid, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuIds 菜单id字符串
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 菜单实体列表
	 */
	public Page<AcMenu> findByIds(String menuIds,Integer start,Integer limit){
		if(StringUtil.isNullOrBlank(menuIds)) throw new RuntimeException("参数异常: 入参menuIds为空");
		String _t = "'".concat(menuIds.replaceAll(",", "','")).concat("'");
		String hql = SqlMapUtil.getSql(XMLNAME_MENU.concat("findByIds")).replace("?", _t);
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit);  //利用查询缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不利用查询缓存
		}
	}
	
	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-8-20
     * <br/><li>修改人：
	 * <br/><li>修改日期：
     * <br/><li>修改内容：
     * @param field 字段名
     * @param value 字段值
     * @return 菜单实体列表
     */
	@SuppressWarnings("unchecked")
	public List<AcMenu> findByField(String field, String value){
		if(field == null || value == null) throw new RuntimeException("参数异常: field或value为空");
		String hql = SqlMapUtil.getSql(XMLNAME_MENU.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql))	return null;  //SQL语句读取异常
		hql = hql.replaceFirst("[?]", field).replaceFirst("[?]", String.valueOf(value));
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.find(true, hql);
		} else {
			return daoUtils.getHibernateTemplate().find(hql); //不使用查询缓存
		}
	}
	
	/**
	 * <br/><li>说明： 根据菜单的名称精确查找与其对应的唯一菜单项
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuname 菜单名称
	 * @return 菜单实体
	 */
	@SuppressWarnings("unchecked")
	public AcMenu findByName(String menuname){
		String hql = SqlMapUtil.getSql(XMLNAME_MENU.concat("findByField"));
		hql = hql.replaceFirst("[?]", "menuname").replaceFirst("[?]", menuname);
		List<AcMenu> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql); //不使用缓存
		}
		if(list != null && list.size() > 0)	return list.get(0);
		else return null;
	}
	
	/**
	 * <br/><li>说明： 根据菜单名称进行模糊查询(like '%...%'方式),并提供分页支持
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuname 菜单名称
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 带分页的菜单实体列表
	 */
	public Page<AcMenu> findByName(String menuname,Integer start,Integer limit){
		String hql = SqlMapUtil.getSql(XMLNAME_MENU.concat("findByName")).replace("?", "'%"+menuname+"%'");
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit);  //使用缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}	
	}
	
	/**
	 * <br/><li>说明： 根据menuseq查询唯一对应的菜单. menuseq的格式示例: ".0.7.146"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuseq 菜单Id序列
	 * @return 菜单实体
	 */
	@SuppressWarnings("unchecked")
	public AcMenu findBySeq(String menuseq){
		String hql = SqlMapUtil.getSql(XMLNAME_MENU.concat("findByField"));
		hql = hql.replaceFirst("[?]", "menuseq").replaceFirst("[?]", menuseq);
		List<AcMenu> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不使用缓存
		}
		if(list != null && list.size() > 0)	return list.get(0);
		else return null;
	}
	
	/**
	 * <br/><li>说明： 查找入参menuid的下一级菜单信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuid 菜单Id
	 * @return 菜单实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<AcMenu> findChildsById(String menuid) {
		String hql = SqlMapUtil.getSql(XMLNAME_MENU.concat("findByField"));
		hql = hql.replaceFirst("[?]", "parentsid").replaceFirst("[?]", menuid);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.find(true, hql);
		} else {
			return daoUtils.getHibernateTemplate().find(hql);
		}
	}
	
	/**
	 * <br/><li>说明： 以分页方式查找入参menuid的下一级菜单信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuid 菜单Id
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 带分页的菜单实体列表
	 */
	@SuppressWarnings("unchecked")
	public Page<AcMenu> findChildsById(String menuid,Integer start,Integer limit){
		String hql = SqlMapUtil.getSql(XMLNAME_MENU.concat("findByField"));
		hql = hql.replaceFirst("[?]", "parentsid").replaceFirst("[?]", menuid);
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit);   //使用缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}
	}
	
	/**
	 * <li>说明：根据菜单Id数组,查询匹配的菜单信息列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-20
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 菜单Id数组
	 * @return 菜单实体列表
	 */
	public List <AcMenu> findByIdArys(Serializable... ids){
		List<AcMenu> list = new ArrayList<AcMenu>();
		for (Serializable menuid : ids) {
			list.add(this.getModelById(menuid.toString()));
		}
		return list;
	}
	/**
	 * <li>说明：得到菜单树
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-12-19
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param parentsid：父节点主键
	 * @return List<HashMap> 返回树的集合
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> getTree(String parentsid) throws BusinessException{
		List<AcMenu> menuList = null;
		if(parentsid == null){
			menuList = findRoot();
		} else {
			menuList = findChildsById(parentsid);
		}
		List<HashMap> children = new ArrayList<HashMap>();
		for (AcMenu menu : menuList) {
			HashMap nodeMap = new HashMap();
			nodeMap.put("id", menu.getMenuid());
			nodeMap.put("text", menu.getMenuname());
			nodeMap.put("leaf", ("y".equals(menu.getIsleaf()) ? true : false) );
			children.add(nodeMap);
		}
		return children;
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
	public String[] validateUpdate(AcMenu t, String isAdd){
		String hql = null;
		AcMenu _t = null;
		if(t==null || StringUtil.isNullOrBlank(isAdd)){
			return new String[]{"保存菜单信息失败！"};
		}
		//如果当前是新增操作，检查主键是否重复
		if(Boolean.valueOf(isAdd)){
			hql = "from AcMenu where menuid = '" + t.getMenuid() + "'";
			_t = (AcMenu)this.daoUtils.findSingle(hql);
			if(_t != null) {
				return new String []{"系统中已存在["+t.getMenuid()+"]菜单编号！"}; //返回验证失败信息
			}
		} 
//		//检查菜单名称是否重复
//		hql = "from AcMenu where menuname = '" + t.getMenuname() + "'";
//		if(!Boolean.valueOf(isAdd)) { //如果是编辑操作，则需排除该数据本身
//			hql = hql.concat(" and menuid <> '" + t.getMenuid() + "'");
//		}
//		_t = (AcMenu)this.daoUtils.findSingle(hql);
//		if(_t != null) {
//			return new String []{"系统中已存在["+t.getMenuname()+"]菜单名称！"}; //返回验证失败信息
//		}
//		//检查菜单显示名称是否重复
//		hql = "from AcMenu where menulabel = '" + t.getMenulabel() + "'";
//		if(!Boolean.valueOf(isAdd)) { //如果是编辑操作，则需排除该数据本身
//			hql = hql.concat(" and menuid <> '" + t.getMenuid() + "'");
//		}
//		_t = (AcMenu)this.daoUtils.findSingle(hql);
//		if(_t != null) {
//			return new String []{"系统中已存在["+t.getMenulabel()+"]菜单显示名称！"}; //返回验证失败信息
//		}		
		//检查菜单代码是否重复
		String menucode = t.getMenucode();
		if(!StringUtil.isNullOrBlank(t.getMenucode()) && !t.getMenucode().startsWith("YD")){
			menucode = "YD".concat(menucode);
		}
		hql = "from AcMenu where menucode = '" + menucode + "'";
		if(!Boolean.valueOf(isAdd)) { //如果是编辑操作，则需排除该数据本身
			hql = hql.concat(" and menuid <> '" + t.getMenuid() + "'");
		}
		_t = (AcMenu)this.daoUtils.findSingle(hql);
		if(_t != null) {
			return new String []{"系统中已存在["+t.getMenucode()+"]菜单代码！"}; //返回验证失败信息
		}
		return null;
	}
	
	/**
	 * <li>说明：得到菜单树
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-12-19
	 * <li>修改人： 谭诚
	 * <li>修改日期：2014-01-07
	 * <li>修改内容：
	 * @param parentsid：父节点主键
	 * @return List<HashMap> 返回树的集合
	 * @throws BusinessException
	 */
	public void saveOrUpdate(AcMenu menu, String isAdd) throws BusinessException, NoSuchFieldException {
		//如果菜单代码不为空且不是以YD开头,则自动加上YD
		String menuCode = menu.getMenucode();
		if(!StringUtil.isNullOrBlank(menuCode) && !menuCode.startsWith("YD")){
			menu.setMenucode("YD".concat(menuCode)); 
		}
//		新增时永远是叶子节点
		menu.setMenuid(StringUtil.nvlTrim(menu.getMenuid(), null));
		if(!StringUtil.isNullOrBlank(isAdd)&&"true".equals(isAdd)){		//新增
			menu.setIsleaf("y");
			this.insert(menu);
			//	新增时回填部份数据
			if(!StringUtils.isBlank(menu.getParentsid())){
				AcMenu parent = this.getModelById(menu.getParentsid());
				menu.setMenuseq(parent.getMenuseq() + menu.getMenuid() + ".");//设置菜单Seq
				parent.setIsleaf("n");//设置父节点为非叶子节点
				menu.setMenulevel((parent.getMenulevel() == null ? 0 : parent.getMenulevel()) + 1);//设置菜单等级
				int count = this.getMenuCount(parent.getMenuid());
				parent.setSubcount(Long.valueOf(count));//设置父节点子节点数量
				this.update(parent);
				menu.setAppid(this.findApplicationId(menu.getFunccode())); //查询功能所属的应用ID
				this.update(menu);
			}
		} else {							//更新
			menu.setAppid(this.findApplicationId(menu.getFunccode())); //查询功能所属的应用ID
			this.update(menu);				
		}
	}	
	
	/**
	 * <li>说明：得到菜单树
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-27
	 * <li>修改人：何涛
	 * <li>修改日期：2016-0411
	 * <li>修改内容：增加对应用功能类型的查询
	 * @param operatorid 当前登录系统的操作员id
	 * @param menuid 当前菜单的id
	 * @return 需要显示的系统功能菜单项
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<Map> authorityMenuTree(Long operatorid, String menuid) throws BusinessException{
		String sql = buildSysMenuTreeSQL(String.valueOf(operatorid),menuid); //调用方法生成查询语句
		List <Object[]> list = this.daoUtils.executeSqlQuery(sql);
		if(list == null || list.size()<1) return null;
		List<Map> children = new ArrayList<Map>();
		Map map = null;
		Boolean flag = false;
		for(Object [] obj : list){
			map = new LinkedHashMap();
			map.put("id", obj[0]); 			//menuid 菜单id
			map.put("text", obj[1]); 		//menulabel 菜单显示名称
			flag = StringUtil.isNullOrBlank(String.valueOf(obj[2]))||"n".equals(String.valueOf(obj[2]))?false:true;
			map.put("leaf", flag); 			//isleaf 是否有下级菜单
			map.put("funcaction", obj[3]); 	//funcaction 应用url
			map.put("funcType", obj[4]); 	//funcType 应用类型：前台功能、后台服务、报表功能...
			children.add(map);
		}	
		return children;
	}
    
    /**
     * <li>说明：获取所有节点集合
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid
     * @param menuid
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<Map> authorityMenuTreeAll(Long operatorid, String menuid) throws BusinessException{
        String sql = buildSysMenuTreeSQL(String.valueOf(operatorid),menuid); //调用方法生成查询语句
        List <Object[]> list = this.daoUtils.executeSqlQuery(sql);
        if(list == null || list.size()<1) return null;
        List<Map> children = new ArrayList<Map>();
        Map map = null;
        Boolean flag = false;
        for(Object [] obj : list){
            map = new LinkedHashMap();
            map.put("id", obj[0]);          //menuid 菜单id
            flag = StringUtil.isNullOrBlank(String.valueOf(obj[2]))||"n".equals(String.valueOf(obj[2]))?false:true;
			if(flag){
				map.put("text", "<span style=\"color:#3A5A82;\" title=\"" + obj[1] + "\">" + obj[1] + "</span>"); // menulabel 菜单显示名称
				map.put("iconCls", "groupCheckedIcon");			
			}else{
				String iconCls = getIconClsByText(obj[1]+"");
				map.put("text", "<span style=\"font-size:13px;display:inline-block;height:20px;color:rgb(21,66,139);\" title=\"" + obj[1] + "\">" + obj[1] + "</span>"); // menulabel 菜单显示名称
				map.put("iconCls", iconCls);				
			}
            map.put("leaf", flag);          //isleaf 是否有下级菜单
            map.put("funcaction", obj[3]);  //funcaction 应用url
            map.put("funcType", obj[4]);    //funcType 应用类型：前台功能、后台服务、报表功能...
            if(!flag){
                map.put("children", authorityMenuTreeAll(operatorid, obj[0]+""));  
            }
            children.add(map);
        }   
        return children;
    }
    
    /**
	 * <li>说明：根据不同的菜单获取菜单图标样式
	 * <li>创建人：何涛
	 * <li>创建日期：2017年5月25日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param text 菜单名称
	 * @return 菜单图表样式
	 */
	private String getIconClsByText(String text) {
		String iconClass = "icon-collapse-all" ;
		if("基础配置".equals(text)){
			iconClass = "cogIcon" ;
		}else if("计划管理".equals(text)){
			iconClass = "flagIcon" ;
		}else if("系统查询".equals(text)){
			iconClass = "previewIcon" ;
		}else if("生产调度".equals(text)){
			iconClass = "scddIcon" ;
		}else if("过程管理".equals(text)){
			iconClass = "gcglIcon" ;
		}else if("质量控制".equals(text)){
			iconClass = "zlkzIcon" ;
		}else if("配件管理".equals(text)){
			iconClass = "pjglIcon" ;
		}else if("技术管理".equals(text)){
			iconClass = "jsglIcon" ;
		}else if("物资管理".equals(text)){
			iconClass = "wlglIcon" ;
		}else if("统计分析".equals(text)){
			iconClass = "tjfxIcon" ;
		}else if("系统帮助".equals(text)){
			iconClass = "helpIcon" ;
		}
		return iconClass;
	}
	
	/**
	 * <li>说明：构建系统功能树的查询语句
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-27
	 * <li>修改人：何涛
	 * <li>修改日期：2016-0411
	 * <li>修改内容：增加对应用功能类型的查询
	 * @param operatorid 当前登录系统的操作员id
	 * @param menuid 当前菜单的id
	 * @return 构建完成的查询语句字符串
	 * @throws BusinessException
	 */
	private String buildSysMenuTreeSQL (String operatorid, String menuid) throws BusinessException {
		
		/* 根据登录人ID， 获取其对应的角色ID */ 
		StringBuffer _a = new StringBuffer(); 
		_a.append("SELECT roleid FROM ac_role WHERE roleid IN (")
		.append("SELECT roleid FROM ac_operatorrole WHERE operatorid = ")
		.append(operatorid)
		.append(")");
		
		/* 根据登录人ID， 获取其所属机构对应的角色ID*/
		StringBuffer _b = new StringBuffer(); 
		_b.append("SELECT roleid FROM ac_role WHERE roleid IN (")
		.append("SELECT roleid FROM Om_Partyrole WHERE partytype = 'organization' AND partyid IN (")
		.append("SELECT orgid FROM om_emporg WHERE empid = (")
		.append("select empid FROM om_employee WHERE operatorid = ")
		.append(operatorid)
		.append(")))");
		
		/* 根据登录人ID， 获取其所属工作组对应的角色ID*/
		StringBuffer _c = new StringBuffer();
		_c.append("SELECT roleid FROM ac_role WHERE roleid in (")
		.append("SELECT roleid FROM om_Partyrole WHERE partytype = 'workgroup' AND partyid in (")
		.append("SELECT groupid FROM om_group WHERE groupid in (")
		.append("SELECT groupid FROM om_empgroup WHERE empid = (SELECT empid FROM om_employee WHERE operatorid = ")
		.append(operatorid)
		.append("))))");
		
		/* 根据登录人ID， 获取其所属岗位对应的角色ID*/
		StringBuffer _d = new StringBuffer();
		_d.append("SELECT roleid FROM ac_role WHERE roleid in (")
		.append("SELECT roleid FROM om_Partyrole WHERE partytype = 'position' AND partyid in (")
		.append("SELECT positionid FROM om_position WHERE positionid in (")
		.append("SELECT positionid FROM OM_EMPPOSITION WHERE empid = (SELECT empid FROM om_employee WHERE operatorid = ")
		.append(operatorid)
		.append("))))");
		
		/* 根据登录人ID， 获取其所属职务对应的角色ID*/
		StringBuffer _e = new StringBuffer();
		_e.append("SELECT roleid from ac_role WHERE roleid in (")
		.append("SELECT roleid FROM om_Partyrole WHERE partytype = 'duty' AND partyid in (")
		.append("SELECT dutyid FROM om_duty WHERE dutyid in (")
		.append("SELECT dutyid FROM om_position WHERE positionid in (")
		.append("SELECT positionid FROM om_empposition where empid = (")
		.append("SELECT empid FROM om_employee WHERE operatorid = ")
		.append(operatorid)
		.append(")))))");
		
		/** 将人员-机构-岗位-职务-工作组对应的角色全部汇总并排除重复 **/
		StringBuffer _union = new StringBuffer();
		_union.append("SELECT DISTINCT roleid FROM (")
		.append(_a).append(" UNION ALL ").append(_b).append(" UNION ALL ").append(_c).append(" UNION ALL ").append(_d).append(" UNION ALL ").append(_e)
		.append(")");
		
		/* 根据登录人所具备的全部直接或间接角色，查询其拥有权限的功能 */
		StringBuffer _funtion = new StringBuffer();
		_funtion.append("SELECT funccode FROM ac_rolefunc WHERE roleid IN (").append(_union).append(")");
		
		/* 查询这些功能对应并且以YD开头的菜单编号的菜单项*/
		StringBuffer _menuItem = new StringBuffer();
		_menuItem.append("SELECT menuid FROM ac_menu WHERE menucode LIKE 'YD%' AND funccode IN (").append(_funtion).append(")");
		
		/* 利用ORACLE函数，从这些菜单向上递归查询，获取其各层菜单项 */
		StringBuffer _menuAll = new StringBuffer();
		_menuAll.append("SELECT DISTINCT menuid,  menulabel, isleaf, displayorder, funccode, parentsid FROM ac_menu START WITH menuid IN (")
		.append(_menuItem)
		.append(") CONNECT BY PRIOR parentsid = menuid");
		
		/* 将菜单项与系统功能表联合查询， 获取菜单对应的功能 */
		StringBuffer _menuFunc = new StringBuffer();
		_menuFunc.append("SELECT a.menuid, a.menulabel, a.isleaf, a.displayorder, a.funccode, b.funcaction, a.parentsid, b.functype FROM (")
		.append(_menuAll)
		.append(") a LEFT JOIN ac_function b  ON a.funccode = b.funccode");
		
		/* 根据入参条件过滤，并对结果进行排序*/
		StringBuffer _filterParam = new StringBuffer();
		_filterParam.append("SELECT menuid, menulabel, isleaf, funcaction, functype FROM (")
		.append(_menuFunc)
		.append(") WHERE parentsid ");
		if(StringUtil.isNullOrBlank(menuid)) {
			_filterParam.append(" IS NULL");
		} else {
			_filterParam.append(" = '").append(menuid).append("'");
		}
		_filterParam.append(" ORDER BY displayorder");
		return _filterParam.toString();
	}

}
