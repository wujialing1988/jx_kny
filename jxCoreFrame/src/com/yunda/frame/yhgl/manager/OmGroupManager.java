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
import com.yunda.frame.yhgl.entity.OmGroup;
/**
 * 
 * <li>类型名称：
 * <li>说明：
 * <li>创建人： 田华
 * <li>创建日期：2011-4-1
 * <li>修改人： 
 * <li>修改日期：
 */
public class OmGroupManager  extends JXBaseManager<OmGroup, OmGroup> implements IOmGroupManager{
	//------------统一查询接口------------

	/**
	 * <br/><li>说明： 递归查询入参工作组id下属所有子工作组
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupId 工作组Id
	 * @param hasSelf 是否包含参数groupId本身
	 * @return 工作组实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmGroup> findAllChilds(Long groupId, boolean hasSelf) {
		if(groupId == null) throw new RuntimeException("参数异常: 入参groupId为空");
		String sql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findAllChilds"));
		if(hasSelf){
			sql = sql.replaceFirst("[?]", " 1=1 "); //包含参数ID本身
		} else {
			sql = sql.replaceFirst("[?]", " groupid <>".concat(String.valueOf(groupId)).concat(" "));
		}
		sql = sql.replaceFirst("[?]", String.valueOf(groupId));
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			daoUtils.cacheSqlQueryEntity(sql, OmGroup.class);  //使用缓存
		} else {
			daoUtils.executeSqlQueryEntity(sql, OmGroup.class);  //不使用缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明： 递归查询入参工作组id下属所有子工作组,并提供分页支持
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupId 工作组Id
	 * @param hasSelf 是否包含参数groupId本身
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @return 工作组实体分页列表
	 */
	public Page<OmGroup> findAllChilds(Long groupId, boolean hasSelf, Integer start, Integer limit) {
		if(groupId == null) throw new RuntimeException("参数异常: 入参groupId为空");
		String sql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findAllChilds"));
		if(hasSelf){
			sql = sql.replaceFirst("[?]", " 1=1 "); //包含参数ID本身
		} else {
			sql = sql.replaceFirst("[?]", " groupid <>".concat(String.valueOf(groupId)).concat(" "));
		}
		sql = sql.replaceFirst("[?]", String.valueOf(groupId));
		String totalSql = "select count(*) as \"rowcount\" from (".concat(sql).concat(")");
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalSql,sql, start, limit,OmGroup.class);
		} else {
			return super.findPageList(totalSql, sql, start, limit, null, null);  //不使用缓存
		}
	}

	/**
	 * <br/><li>说明： 根据员工ID查询其所属工作组信息
	 * <br/><li>创建人：
	 * <br/><li>创建日期：
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param empId 人员Id
	 * @return 工作组实体
	 */
	@SuppressWarnings("unchecked")
	public OmGroup findByEmpId(Long empId) {
		if(empId == null) throw new RuntimeException("参数异常: 入参empId为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByEmpId"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		Object[] param = new Object[]{empId};
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql,param);  //利用查询缓存//cacheTmplFind(hql,param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		if(list !=null && list.size()>0) return list.get(0);
		else return null;
	}

	/**
	 * <br/><li>说明：的QBE查询,非分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param group 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 对象集合
	 * <br/>调用示例:
	 *  <br/>OmGroup group = new OmGroup();
	 *  String [] excludeColumn = new String[]{"groupid","createtime","lastupdate","updator","isleaf","subcount"};
	 *	<br/>group.setOrgcode("4");
	 *	<br/>Order order = Order.asc("groupid");
	 *	<br/>Order [] or = new Order[1];
	 *	<br/>or[0] = order;
	 *  <br/>list = omGroupManager.findByEntity(group,excludeColumn, or,true);
	 */
	@SuppressWarnings("unchecked")
	public List<OmGroup> findByEntity(final OmGroup group, final Order[] orders, final Boolean isExact) {
		String [] excludeColumn = new String[]{"groupid","createtime","lastupdate","updator","isleaf","subcount"};
		return super.findByEntity(group, excludeColumn, orders, isExact);
	}

	/**
	 * <br/><li>说明：工作组的QBE查询,支持分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 工作组分页对象
	 * <br/>调用示例:
	 *  <br/>SearchEntity <OmGroup> entity = new SearchEntity<OmGroup>();
	 *   String [] excludeColumn = new String[]{"groupid","createtime","lastupdate","updator","isleaf","subcount"};
	 *  <br/>entity.setEntity(org);
	 *	<br/>entity.setStart(start);
	 *	<br/>entity.setLimit(limit);
	 *	<br/>page = omGroupManager.findByEntity(entity,excludeColumn,true);
	 */
	@SuppressWarnings("unchecked")
	public Page<OmGroup> findByEntity(SearchEntity<OmGroup> searchEntity, Boolean isExact) {
		String [] excludeColumn = new String[]{"groupid","createtime","lastupdate","updator","isleaf","subcount"};
		return super.findByEntity(searchEntity,excludeColumn,isExact);
	}

	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配, 支持的数据类型有NUMBER,VARCHAR2,CHAR,INTEGER</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
     * @param field 字段名
     * @param value 字段值
     * @return 工作组实体列表
     */
	@SuppressWarnings("unchecked")
	public List<OmGroup> findByField(String field, String value) {
		if(StringUtil.isNullOrBlank(field)||StringUtil.isNullOrBlank(value)) throw new RuntimeException("参数异常: 入参field或value为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]",field).replaceFirst("[?]", value);
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString()); //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql); //不使用缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明：自定义单字段查询,并分页显示结果
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配, 支持的数据类型有NUMBER,VARCHAR2,CHAR,INTEGER</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
     * @param field 字段名
     * @param value 字段值
	 * @param start 开始行
	 * @param limit 每页最大记录数
     * @return 工作组实体分页列表
     */
	public Page<OmGroup> findByField(String field, String value, Integer start, Integer limit) {
		if(StringUtil.isNullOrBlank(field)||StringUtil.isNullOrBlank(value)) throw new RuntimeException("参数异常: 入参field或value为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]",field).replaceFirst("[?]", value);
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit); //使用缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}
	}

	/**
	 * <br/><li>说明：根据工作组Id数组,查询匹配的工作组信息列表
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param ids 工作组Id数组
	 * @return 工作组实体列表
	 */
	public List<OmGroup> findByIdArys(Serializable... ids) {
		if(ids == null) throw new RuntimeException("参数异常: 入参ids为空");
		List<OmGroup> list = new ArrayList<OmGroup>();
		for (Serializable groupid : ids) {
			list.add(this.getModelById(Long.parseLong(groupid.toString())));
		}
		return list;
	}

	/**
	 * <br/><li>说明：根据多个工作组Id,查询与对应的工作组
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个orgId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupIds 工作组id字符串
	 * @return 工作组实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmGroup> findByIds(String groupIds) {
		if(StringUtil.isNullOrBlank(groupIds)) throw new RuntimeException("参数异常: 入参groupIds为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByIds"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replace("?", groupIds);
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不使用缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明：根据多个工作组Id,查询与对应的工作组,并提供分页支持
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个orgId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupIds 工作组id字符串
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 工作组实体分页列表
	 */
	public Page<OmGroup> findByIds(String groupIds, Integer start, Integer limit) {
		if(StringUtil.isNullOrBlank(groupIds)) throw new RuntimeException("参数异常: 入参groupIds为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByIds"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replace("?", groupIds);
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit); //使用缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}
	}

	/**
	 * <br/><li>说明： 根据名称精确查询工作组信息(如出现多条匹配记录,则取第一条)
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param name 工作组名称
	 * @return 工作组实体列表
	 */
	@SuppressWarnings("unchecked")
	public OmGroup findByName(String name) {
		if(StringUtil.isNullOrBlank(name)) throw new RuntimeException("参数异常: 入参name为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]","groupname").replaceFirst("[?]", name);
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不使用缓存
		}
		return getFirst(list);
	}

	/**
	 * <br/><li>说明： 根据岗位ID查询其所属工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param positionId 岗位Id
	 * @return 工作组实体
	 */
	@SuppressWarnings("unchecked")
	public OmGroup findByPosId(Long positionId) {
		if(positionId==null) throw new RuntimeException("参数异常: 入参positionId为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByPosId"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		Object[] param = new Object[]{positionId};
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		return getFirst(list);
	}

	/**
	 * <br/><li>说明： 根据名称模糊查询工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param name 查询参数
	 * @return 工作组实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmGroup> findByResName(String name) {
		if(StringUtil.isNullOrBlank(name)) throw new RuntimeException("参数异常: 入参name为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByResField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]","groupname").replaceFirst("[?]", "%".concat(name).concat("%"));
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString()); //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不使用缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明： 根据名称分页模糊查询工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param name 工作组name字符串
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 工作组实体分页列表
	 */
	public Page<OmGroup> findByResName(String name, Integer start, Integer limit) {
		if(StringUtil.isNullOrBlank(name)) throw new RuntimeException("参数异常: 入参name为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByResField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]","groupname").replaceFirst("[?]", "%".concat(name).concat("%"));
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit); //使用缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}
	}

	/**
	 * <br/><li>说明： 根据入参查询groupseq开头与之相同的工作组. orgseq的格式示例: ".0.7.146"
	 * <br/><li><font color=red>注*：查询的方案是 groupseq like '[param]%'形式</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupSeq 工作组Id序列
	 * @return 工作组实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmGroup> findByResSeq(String groupSeq) {
		if(StringUtil.isNullOrBlank(groupSeq)) throw new RuntimeException("参数异常: 入参groupSeq为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByResField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]","groupseq").replaceFirst("[?]", groupSeq.concat("%"));
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不使用缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明： 根据groupseq查询唯一对应的工作组. orgseq的格式示例: ".0.7.146"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupSeq 工作组Id序列
	 * @return 工作组实体
	 */
	@SuppressWarnings("unchecked")
	public OmGroup findBySeq(String groupSeq) {
		if(StringUtil.isNullOrBlank(groupSeq)) throw new RuntimeException("参数异常: 入参groupSeq为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "groupseq").replaceFirst("[?]", groupSeq);
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不使用缓存
		}
		return getFirst(list);
	}

	/**
	 * <br/><li>说明： 查找入参groupid的下一级工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupId 工作组Id
	 * @return 工作组实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmGroup> findChildsById(Long groupId) {
		if(groupId==null) throw new RuntimeException("参数异常: 入参groupId为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "parentgroupid").replaceFirst("[?]", String.valueOf(groupId));
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不使用缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明： 查找入参groupid的下一级工作组信息并分页显示
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupId 工作组Id
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 工作组实体分页列表
	 */
	public Page<OmGroup> findChildsById(Long groupId, Integer start, Integer limit) {
		if(groupId==null) throw new RuntimeException("参数异常: 入参groupId为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "parentgroupid").replaceFirst("[?]", String.valueOf(groupId));
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit);   //使用缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}
	}

	/**
	 * <br/><li>说明： 根据工作组ID查询其它同级工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @return 工作组实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmGroup> findOtherById(Long groupId) {
		if(groupId==null) throw new RuntimeException("参数异常: 入参groupId为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findOtherById"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		Object[] param = new Object[]{groupId,groupId};
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		return list;
	}
	
	/**
	 * <li>说明：查询根级工作组
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 工作组实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmGroup> findRoot(){
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findRoot"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不使用缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明： 根据工作组ID查询其上级工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupId 工作组Id
	 * @return 工作组实体
	 */
	@SuppressWarnings("unchecked")
	public OmGroup findUpById(Long groupId) {
		if(groupId==null) throw new RuntimeException("参数异常: 入参groupId为空");
		String hql = SqlMapUtil.getSql(XMLNAME_GROUP.concat("findUpById"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		Object[] param = new Object[]{groupId};
		List <OmGroup> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		return this.getFirst(list);
	}

	/**
	 * <br/><li>说明： 根据ID查询工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupId 工作组Id
	 * @return 工作组实体
	 */
	public OmGroup getModelById(Long groupId) {
		return (OmGroup)daoUtils.getHibernateTemplate().get(OmGroup.class, groupId);
	}
	
	/**
	 * <li>说明：获取集合的第一个元素
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-6
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param list：工作组实体对象集合
	 * @return 工作组实体对象
	 */
	@SuppressWarnings("unused")
	private OmGroup getFirst(List<OmGroup> list){
		if(list!=null&&list.size()>0){
			return list.get(0);
		} else {
			return null;
		}
	}
}
