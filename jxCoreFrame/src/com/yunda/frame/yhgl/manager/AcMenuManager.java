/**
 * <li>文件名：AcMenuManager.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-17
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.AcMenu;

/**
 * <li>类型名称：系统菜单服务类
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-17
 * <li>修改人：
 * <li>修改日期：
 */
public class AcMenuManager extends JXBaseManager<AcMenu, AcMenu> implements IAcMenuManager{

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
}