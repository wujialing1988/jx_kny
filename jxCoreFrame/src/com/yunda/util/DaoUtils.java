package com.yunda.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.Transformers;
import org.hibernate.type.NullableType;
import org.hibernate.type.Type;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.yunda.common.BusinessException;
import com.yunda.common.DaoException;
import com.yunda.common.SortObject;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.EntityUtil;

/**
 * <li>类型名称：
 * <li>说明：框架中的数据库Hibernate访问操作工具
 * <li>可供所有的业务服务对象都调用此对象完成数据库的操作。
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-01-14
 * <li>修改人：刘晓斌2013-3-2修改该类的大部分方法（删除几个无法修改的方法），避免数据连接泄漏
 * <li>修改日期：2013-3-2
 */
public class DaoUtils extends HibernateDaoSupport {
	public static final Integer ASC = 0;

	public static final Integer DESC = 1;

	/**
	 * <li>方法名：getAll
	 * <li>
	 * @param entityClass
	 *            实体类型
	 *            <li>
	 * @return 返回指定实体类型的全部实体对象。
	 *         <li>返回类型：List
	 *         <li>说明：获取指定实体类型的全部实体对象。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public List getAll(Class entityClass) throws BusinessException{
		return getHibernateTemplate().loadAll(entityClass);
	}

	/**
	 * <li>方法名：get
	 * <li>
	 * @param id
	 *            实体身份标示对象
	 *            <li>
	 * @param entityClass
	 *            实体类型
	 *            <li>
	 * @return 实体对象
	 *         <li>返回类型：Object
	 *         <li>说明：根据ID和实体类型从数据库中获取实体对象。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public Object get(Serializable id, Class entityClass) throws BusinessException{
		return getHibernateTemplate().get(entityClass, id);
	}

	/**
	 * <li>方法名：initialize
	 * <li>
	 * @param object
	 *            <li>返回类型：void
	 *            <li>说明：
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-15
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public void initialize(Object object) throws BusinessException{
		getHibernateTemplate().initialize(object);
	}

	/**
	 * <li>方法名：load
	 * <li>
	 * @param id
	 *            身份标示对象。
	 *            <li>
	 * @param entityClass
	 *            实体类型。
	 *            <li>
	 * @return 实体对象。
	 *         <li>返回类型：Object
	 *         <li>说明：根据ID和实体类型获取实体对象；如果缓存中有这个实体对象就不到数据库中去查找。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public Object load(Serializable id, Class entityClass) throws BusinessException{
		return this.getHibernateTemplate().load(entityClass, id);
	}


	/**
	 * <li>方法名：saveOrUpdate
	 * <li>
	 * @param entity
	 *            <li>
	 * @return
	 *            <li>返回类型：Object
	 *            <li>说明：保存实体对象
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-3-11
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public Object saveOrUpdate(Object entity) throws BusinessException{
		getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}

	/**
	 * <li>方法名：update
	 * <li>
	 * @param entity
	 *            实体对象
	 *            <li>
	 * @return 实体对象
	 *         <li>返回类型：Object
	 *         <li>说明：更新数据库中的实体对象。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public Object update(Object entity) throws BusinessException{
		getHibernateTemplate().update(entity);
		return entity;
	}
	
	/**
	 * <li>方法名：checkUpdateDate
	 * <li>@param entityClass 当前实体
	 * <li>@param identifierPropertyName 主键字段名
	 * <li>@param identifierPropertyValue 主键值
	 * <li>@param ModificationTimePropertyName 修改时间字段名
	 * <li>@return 
	 * <li>返回类型：boolean
	 * <li>说明：验证当前数据时候被修改过，如果修改过则返回false，未被修改过则返回true
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-24
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public boolean checkUpdateDate(Class entityClass,
			String identifierPropertyName,String identifierPropertyValue,
			String ModificationTimePropertyName)throws BusinessException{
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT t FROM ");
		hql.append(entityClass.getName() + " t ");
		hql.append(" WHERE ");
		hql.append(" t." + identifierPropertyName + " = '" +identifierPropertyValue + "' ");
		hql.append(" AND ");
		hql.append(" to_char(t." + ModificationTimePropertyName + ",'yyyy-MM-dd HH:mi:ss') >= '" + YDStringUtil.getNow("yyyy-MM-dd HH:mm:ss") + "'");
		return getCount(hql.toString()) == 0;
	}

	/**
	 * <li>方法名：insert
	 * <li>
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 *         <li>返回类型：Object
	 *         <li>说明：向数据库中插入一个新的实体对象。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public Object insert(Object entity) throws BusinessException{
		getHibernateTemplate().save(entity);
		return entity;
	}

	/**
	 * <li>方法名：removeById
	 * <li>
	 * @param id
	 *            实体身份标示对象。
	 *            <li>
	 * @param entityClass
	 *            实体类型。
	 *            <li>返回类型：void
	 *            <li>说明：根据ID移除对象；如果删除实体对象之前有一些特殊的操作，请在子类中覆盖
	 *            <li>beforeremoveById方法。
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public void removeById(Serializable id, Class entityClass) throws BusinessException{
		removeById(entityClass, id);
	}

	/**
	 * <li>方法名：removeByIds
	 * <li>@param ids 需要删除的ID集合
	 * <li>@param entityClass 实体
	 * <li>@param identifierPropertyName 主键字段名
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-4-15
	 * <li>修改人： 袁健
	 * <li>修改日期：2012-6-15
	 * <li>说明：修改参数常量为ifDel 现在数据库中逻辑删除字段为ifDel
	 */
	public void removeByIds(Serializable[] ids, Class entityClass,
			String identifierPropertyName) throws BusinessException{
		removeByIds( ids, entityClass,
				 identifierPropertyName , "ifDel") ;
	}
	
	/**
	 * <li>方法名：removeByIds
	 * <li>@param ids 需要删除的ID集合
	 * <li>@param entityClass 实体
	 * <li>@param identifierPropertyName 主键字段名
	 * <li>@param delPropertyName 逻辑删除字段名
	 * <li>@throws Exception
	 * <li>返回类型：void
	 * <li>说明：
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-4-15
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public void removeByIds(Serializable[] ids, Class entityClass,
			String identifierPropertyName ,String delPropertyName) throws BusinessException{
		beforeremoveByIds(ids);

		StringBuilder deleteHql = new StringBuilder();

		List<String> propertyNames = Arrays.asList(this.getSessionFactory()
				.getClassMetadata(entityClass).getPropertyNames());

		if (propertyNames.contains(delPropertyName) 
				|| propertyNames.contains(delPropertyName.toLowerCase())
				|| propertyNames.contains(delPropertyName.toUpperCase())) {
			deleteHql.append(" update ");
			deleteHql.append(entityClass.getName());
			deleteHql.append(" set ");
			deleteHql.append(delPropertyName + "  = '1' ");
		} else {
			deleteHql.append(" delete from ");
			deleteHql.append(entityClass.getName());
		}
		deleteHql.append(" where " + identifierPropertyName + " in(");
		for (int i = 0; i < ids.length; i++) {
			deleteHql.append("'" + ids[i] + "'");
			if (i != ids.length - 1)
				deleteHql.append(",");
		}
		deleteHql.append(")");
		logger.info("delete hql:" + deleteHql.toString());

		this.getHibernateTemplate().bulkUpdate(deleteHql.toString());
	}

	/**
	 * <li>方法名：removeObject
	 * <li>
	 * @param ids
	 *            <li>
	 * @param entityClass
	 *            <li>返回类型：void
	 *            <li>说明：按对象删除，删除级联情况视hibernate的配置情况
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public void removeObject(Serializable[] ids, Class entityClass) throws BusinessException{
		List<Object> list = new ArrayList<Object>();
		for (Serializable id : ids) {
			if (StringUtils.isNumeric(String.valueOf(id))) {
				list.add(get(Long.valueOf(String.valueOf(id)), entityClass));
			} else {
				list.add(get(id, entityClass));
			}
		}
		this.getHibernateTemplate().deleteAll(list);
	}

	/**
	 * <li>方法名：beforeremoveByIds
	 * <li>
	 * @param ids
	 *            身份标示对象数组。
	 *            <li>返回类型：void
	 *            <li>说明：根据指定的身份标示对象数组进行实体对象的批量删除；如果删除前有特殊的事情要处理，请
	 *            <li>在子类中覆盖本方法。
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	protected void beforeremoveByIds(Serializable[] ids) throws BusinessException{

	}

	/**
	 * <li>方法名：remove
	 * <li>
	 * @param o
	 *            实体对象
	 *            <li>返回类型：void
	 *            <li>说明：删除指定的实体对象。
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public void remove(Object o) throws BusinessException{
		getHibernateTemplate().delete(o);
	}

	/**
	 * <li>方法名：removeById
	 * <li>
	 * @param entityClass
	 *            实体类型
	 *            <li>
	 * @param id
	 *            身份标示对象。
	 *            <li>返回类型：void
	 *            <li>说明：根据ID和实体类型删除对象.
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public void removeById(Class entityClass, Serializable id) throws BusinessException{
		remove(get(id, entityClass));
	}

	/**
	 * <li>方法名：flush
	 * <li>返回类型：void
	 * <li>说明：提交hibernate session 缓存中未提交的语句。
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人：
	 * <li>修改日期：
	 */
	public void flush() throws BusinessException{
		getHibernateTemplate().flush();
	}

	/**
	 * <li>方法名：clear
	 * <li>返回类型：void
	 * <li>说明：清除hibernate session 缓存中未提交的语句。
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人：
	 * <li>修改日期：
	 */
	public void clear() throws BusinessException{
		getHibernateTemplate().clear();
	}

	/**
	 * <li>方法名：removeSelect
	 * <li>
	 * @param hql
	 *            hql语句内容。
	 *            <li>
	 * @return 返回剔出掉from 之前的所有内容的hql语句。
	 *         <li>返回类型：String
	 *         <li>说明：去除hql的select 子句，未考虑union的情况,用于pagedQuery.
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	private static String removeSelect(String hql) throws BusinessException{
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql
				+ " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * <li>方法名：removeOrders
	 * <li>
	 * @param hql
	 *            hql语句内容。
	 *            <li>
	 * @return 返回剔出掉排序内容的hql语句。
	 *         <li>返回类型：String
	 *         <li>说明：去除hql的orderby 子句，用于pagedQuery.
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	private static String removeOrders(String hql)throws BusinessException {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * <li>方法名：getIdType
	 * <li>
	 * @param entityClass
	 *            实体类型。
	 *            <li>
	 * @return 返回实体身份标示对象的类型。
	 *         <li>返回类型：Class
	 *         <li>说明：返回指定实体类型的身份标示对象的类型。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public Class getIdType(Class entityClass) throws BusinessException{
		Class idClass = null;
		String idName = getSessionFactory().getClassMetadata(entityClass)
				.getIdentifierPropertyName();
		try {
			Field field = entityClass.getDeclaredField(idName);
			idClass = field.getType();
		} catch (SecurityException e) {

		} catch (NoSuchFieldException e) {

		}
		return idClass;
	}

	/**
	 * <li>方法名：getId
	 * <li>
	 * @param o
	 *            实体对象。
	 *            <li>
	 * @return 返回实体对象的身份标示对象。
	 *         <li>返回类型：Serializable
	 *         <li>说明：获取指定实体对象的身份标示对象。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public Serializable getId(Object o) throws BusinessException{
		Serializable id = null;
		String idName = getSessionFactory().getClassMetadata(o.getClass())
				.getIdentifierPropertyName();
		try {
			Field field = o.getClass().getDeclaredField(idName);
			field.setAccessible(true);
			id = (Serializable) field.get(o);
			field.setAccessible(false);
		} catch (SecurityException e) {

		} catch (NoSuchFieldException e) {

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * <li>方法名：getIdName
	 * <li>
	 * @param o
	 *            实体对象。
	 *            <li>
	 * @return 返回实体id属性名称。
	 *         <li>返回类型：String
	 *         <li>说明：获取指定实体对象的id对象名。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-24
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public String getIdName(Object o)throws BusinessException {
		return getSessionFactory().getClassMetadata(o.getClass())
				.getIdentifierPropertyName();
	}

	/**
	 * <li>方法名：find
	 * <li>
	 * @param queryString
	 *            要执行的hql语句。
	 *            <li>
	 * @return 返回查询结果集合。
	 *         <li>返回类型：List
	 *         <li>说明：执行不带参数的hql语句，并返回查询结果集合；查询结果不做后台分页。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public List find(String queryString) throws BusinessException{
		return find(queryString,null);
	}

	/**
	 * <li>方法名：find
	 * <li>
	 * @param cls
	 *            要查找的类
	 *            <li>
	 * @param ids
	 *            要查找的对象ID动态数据
	 *            <li>
	 * @return
	 *            <li>
	 * @throws BusinessException
	 *             <li>返回类型：Collection
	 *             <li>说明：
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-17
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<Object> find(Class cls, String... ids) throws BusinessException {
		StringBuffer sbIds = new StringBuffer();
		if (ids != null) {
			for (String id : ids) {
				sbIds.append(",'");
				sbIds.append(id);
				sbIds.append("'");
			}
			if (!"".equals(sbIds) && sbIds.length() > 0) {
				return this.find("FROM " + cls.getName() + " obj "
						+ " WHERE obj.id in("
						+ sbIds.toString().replaceFirst(",", "") + ")");
			}
		}
		return null;
	}

	/**
	 * <li>方法名：find
	 * <li>
	 * @param queryString
	 *            要执行的hql语句。
	 *            <li>
	 * @param params
	 *            hql语句中需要的参数集合。
	 *            <li>
	 * @return 返回查询结果集合。
	 *         <li>返回类型：List
	 *         <li>说明：执行带参数的hql语句，并返回查询结果集合；查询结果不做后台分页。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public List find(String queryString, Object... params) throws BusinessException{
		return this.getHibernateTemplate().find(queryString, params);
	}

	/**
	 * <li>方法名：findSingle
	 * <li>
	 * @param queryString
	 *            要执行的hql语句。
	 *            <li>
	 * @return 返回一个实体对象。
	 *         <li>返回类型：Object
	 *         <li>说明：执行不带参数的hql语句，只返回查询结果中的一个实体对象。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public Object findSingle(String queryString) throws BusinessException{
		return findSingle(queryString,null);
	}

	/**
	 * <li>方法名：findSingle
	 * <li>
	 * @param queryString
	 *            要执行的hql语句。
	 *            <li>
	 * @param params
	 *            <li>
	 * @return
	 *            <li>返回类型：Object
	 *            <li>说明：
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public Object findSingle(String queryString, Object... params) throws BusinessException{
		List list = findPage(queryString, params, 0, 1);
		if (list != null && list.size() > 0)
			return list.get(0);

		return null;
	}

	/**
	 * <li>方法名：findPage
	 * <li>
	 * @param queryString
	 *            要执行的hql语句。
	 *            <li>
	 * @param pageNo
	 *            要查询的分页记录的页码。
	 *            <li>
	 * @param pageSize
	 *            每页显示记录的条数。
	 *            <li>
	 * @return 返回查询结果集合。
	 *         <li>返回类型：List
	 *         <li>说明：执行hql语句，查询指定分页的数据。后台分页。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public List findPage(final String queryString, final int rowStart,
			final int rowEnd)throws BusinessException{
		return findPage(queryString, null, rowStart, rowEnd);
	}

	/**
	 * <li>方法名：buildHql
	 * <li>
	 * @param entity
	 *            持久化模板对象
	 *            <li>
	 * @param alias
	 *            对象别名，与附加HQL语句中应一致
	 *            <li>
	 * @param addHql
	 *            附加HQL查询条件语句，必须有where关键字
	 *            <li>
	 * @return hql语句
	 *         <li>返回类型：String
	 *         <li>说明：通过查询模板对象的持久化字符及数字字段做为条件与附加HQL语句一起组成HQL
	 *         <li>注意：字符默认按模糊条件查询，数字默认进行精确查询
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-20
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public String buildHql(final Object example, final String alias,
			final String addHql)throws BusinessException {

		StringBuffer sbHql = new StringBuffer();
		if (StringUtils.isBlank(alias)) {
			sbHql.append("from " + example.getClass().getSimpleName());
		}else{
			sbHql.append("select " + alias + " from ");// 返回别名
			sbHql.append(example.getClass().getSimpleName());
			sbHql.append(" " + alias);// 定义HQL中的别名
		}
		
		if(StringUtils.isNotBlank(addHql)){
			int indexWhere = addHql.toLowerCase().indexOf("where ");
	
			if (indexWhere < 0) {
				throw new DaoException("附加HQL【" + addHql + "】必须包含'where '关键字！");
			}
	
			String beforeWhere = addHql.substring(0, indexWhere).trim();
			String afterWhere = addHql.substring(indexWhere + 6).trim();
			if (!"".equals(beforeWhere)) {// 需要其它表
				sbHql.append(",");
				sbHql.append(beforeWhere);
			}
			sbHql.append(" where 1=1");// 添加条件
			sbHql.append(getSearchCondHql(example, alias));// 模板条件
			if (!"".equals(afterWhere)) {
				sbHql.append(" and " + afterWhere);// 附加条件
			}
		}
		return sbHql.toString();
	}

	/**
	 * <li>方法名：getSearchCondHql
	 * <li>
	 * @param example
	 *            持久化模板对象
	 *            <li>
	 * @param alias
	 *            对象别名
	 *            <li>
	 * @return
	 *            <li>返回类型：String
	 *            <li>说明：获取持久化对象查询条件HQL，排除了非持久化字段及非空或空字符情况，
	 *            <li> 持久化字段仅针对字符及数字，字符默认按模糊条件查询，数字默认进行精确查询
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-20
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public String getSearchCondHql(final Object example, final String alias)throws BusinessException {
		StringBuffer sbHql = new StringBuffer();
		BeanWrapper bw = new BeanWrapperImpl(example);
		ClassMetadata metaData = getSessionFactory().getClassMetadata(
				example.getClass());
		String[] proNames = metaData.getPropertyNames();
		for (String name : proNames) {
			Object value = bw.getPropertyValue(name);
			Object v = metaData
					.getPropertyValue(example, name, EntityMode.POJO);
			System.out.println(value + "        " + v);
			if (value != null && !"".endsWith(value.toString())) {
				sbHql.append(" and ");
				sbHql.append(alias + ".");
				sbHql.append(name);
				if (value instanceof String || value instanceof Character) {// 是字符串的采用模糊查询
					sbHql.append(" like '%" + value + "%'");
				} else if (value instanceof Integer || value instanceof Byte
						|| value instanceof Short || value instanceof Long
						|| value instanceof Float || value instanceof Double
						|| value instanceof Number) {
					sbHql.append(" =" + value + "");
				}
			}
		}
		return sbHql.toString();
	}

	/**
	 * <li>方法名：findPage
	 * <li>
	 * @param queryString
	 *            要执行的hql语句。
	 *            <li>
	 * @param params
	 *            hql语句中的所有参数的集合。
	 *            <li>
	 * @param pageNo
	 *            要查询的分页记录的页码。
	 *            <li>
	 * @param pageSize
	 *            每页显示记录的条数。
	 *            <li>
	 * @return 返回查询结果集合。
	 *         <li>返回类型：List
	 *         <li>说明：执行带参数的hql语句，查询指定分页的数据。后台分页。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public List findPage(final String queryString, final Object[] params,
			final int rowStart, final int rowEnd) throws BusinessException{
		return this.getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery(queryString);
				if (params != null) {
					for (int i = 0; i < params.length; ++i) {
						q.setParameter(i, params[i]);
					}
				}
				q.setFirstResult(rowStart);
				q.setMaxResults(rowEnd - rowStart);
				List list = q.list();
				return list;
			}
		});
	}

	/**
	 * <li>方法名：getCount
	 * <li>
	 * @param queryString
	 *            要执行的hql语句。
	 *            <li>
	 * @return 返回某个hql语句查询结果记录的条数。
	 *         <li>返回类型：int
	 *         <li>说明：获取某个指定的不带参数的hql语句查询结果的总记录数量；可用于分页查询。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public int getCount(final String queryString) throws BusinessException{
		Assert.hasText(queryString);
		int totalCount = 0;
		// Count查询
		String countQueryString = " select count (*) "
				+ removeSelect(removeOrders(queryString));
		List countlist = getHibernateTemplate().find(countQueryString);
		if(countlist != null && countlist.size() > 0){
			totalCount = ((Long) countlist.get(0)).intValue();
		}
		return totalCount;
	}

	/**
	 * <li>方法名：getCount
	 * <li>
	 * @param queryString
	 *            要执行的hql语句。
	 *            <li>
	 * @param params
	 *            hql语句的参数集合。
	 *            <li>
	 * @return 返回某个hql语句查询结果记录的条数。
	 *         <li>返回类型：int
	 *         <li>说明：获取某个指定的带参数的hql语句查询结果的总记录数量；用于分页查询。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public int getCount(final String queryString, final Object... params) {

		final String countQueryString = " select count (*) "
				+ removeSelect(removeOrders(queryString));

		Object c = (Object) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(countQueryString);
						if (params != null) {
							for (int i = 0; i < params.length; ++i) {
								q.setParameter(i, params[i]);
							}
						}
						return q.iterate().next();
					}
				});
		if (c instanceof Integer)
			return ((Integer) c).intValue();
		else if (c instanceof Long)
			return ((Long) c).intValue();
		else
			return (int) Double.parseDouble(c.toString());
	}

	/**
	 * <li>方法名：executUpdateOrDelete
	 * <li>
	 * @param queryString
	 *            要执行的hql语句。
	 *            <li>
	 * @return 返回hql语句执行完毕影响的记录数量。
	 *         <li>返回类型：int
	 *         <li>说明：执行修改或删除sql,hql语句
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public int executUpdateOrDelete(String queryString) {
		return this.execute(queryString, null);
	}

	/**
	 * <li>方法名：execute
	 * <li>
	 * @param hql
	 *            <li>
	 * @param values
	 *            <li>
	 * @return
	 *            <li>返回类型：int
	 *            <li>说明：
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-18
	 *            <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *            <li>修改日期：
	 */
	public int execute(final String hql, final Object... values) {
		
        HibernateTemplate template = getHibernateTemplate();
        Integer returnInt =  (Integer)template.execute(new HibernateCallback(){
            public Integer doInHibernate(Session s) {
            	Assert.hasText(hql);
        		Query query = s.createQuery(hql);
        		if (values != null && values.length != 0){
        			for (int i = 0; i < values.length; i++) {
            			query.setParameter(i, values[i]);
            		}
        		}
        		return query.executeUpdate();
            }
        });
        if(returnInt != null){
        	return returnInt.intValue();
        } else{
        	return 0;
        }
	}
	
	/**
	 * <li>方法名：executeSql
	 * <li>@param sql
	 * <li>返回类型：int
	 * <li>说明：执行sql语句
	 * <li>创建人：田华
	 * <li>创建日期：2011-5-4
	 * <li>修改人： 张凡
	 * <li>修改日期：2013-2-6
	 * <li>修改说明：修复无法释放连接池资源的问题，修改返回类型
	 */
	public int executeSql(final String sql) {
        return (Integer)getHibernateTemplate().execute(new HibernateCallback(){
            public Integer doInHibernate(Session s) {
                return s.createSQLQuery(sql).executeUpdate();
            }
        });
	}
	/**
	 * <li>方法名：executeSqlQuery
	 * <li>@param sql
	 * <li>@return
	 * <li>返回类型：List
	 * <li>说明：执行sql查询
	 * <li>创建人：田华
	 * <li>创建日期：2011-6-28
	 * <li>修改人： 张凡
     * <li>修改日期：2013-2-6
     * <li>修改说明：修复无法释放连接池资源的问题
	 */
	public List executeSqlQuery(final String sql){
	    return (List)getHibernateTemplate().execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
                return s.createSQLQuery(sql).list();
            }
        });
	}
	/**
	 * <li>说明：该方法与public List executeSqlQuery(final String sql)功能相同，区别只在于使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-8-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param sql sql查询语句
	 * @return 结果列表
	 */
	public List cacheExecuteSqlQuery(final String sql){
	    return acquireExecuteSqlQuery(sql, null, true);
	}	
	
	/**
	 * <li>说明：该方法与public List cacheExecuteSqlQuery(final String sql)功能相同，区别在于可指定每个字段的数据类型,防止Hibernate自动转换类型出错
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param sql sql查询语句
	 * @param colType sql查询的列字段类型
	 * @return 结果列表
	 * 
	 * colType参数示例:
	 * 1. 如果是统计类查询, 比如count(*)、sum这类单个字段的，用HashMap
	 * 	  Map <String,Type>colType = new HashMap<String,Type>();
     * 	  colType.put("countPos", Hibernate.INTEGER);
     * 
     * 2. 如果是多个字段的查询，并且需要按照一定的顺序（通常是put的顺序），则需使用LinkedHashMap
     * 	  Map <String,Type>colType = new LinkedHashMap<String,Type>();
     *    colType.put("orgid", Hibernate.LONG);
     *    colType.put("orgname", Hibernate.STRING);
     *    colType.put("orgdegree", Hibernate.STRING);
	 */
	public List cacheExecuteSqlQuery(String sql, Map<String,Type> colType){
	    return acquireExecuteSqlQuery(sql,colType,true);
	}
	
	public List acquireExecuteSqlQuery(String sql,Map<String,Type> colType,Boolean isQueryCacheEnabled){
		final String _sql = sql;
		final Map<String,Type> _colType = colType;
		final Boolean useCached = isQueryCacheEnabled;
		return (List)getHibernateTemplate().execute(new HibernateCallback(){
			public List doInHibernate(Session s) {
				SQLQuery query = null;
	            try {
					query = s.createSQLQuery(_sql);
					if(useCached && _colType != null){  //当不使用缓存时, 无需指定各字段的数据类型
						for(Map.Entry<String, Type> m : _colType.entrySet()){
							query.addScalar(m.getKey(), m.getValue());
						}
					}
					query.setCacheable(useCached);
					List list = query.list();
					return list;
				} catch (HibernateException e) {
					throw e;
				} finally {
					if(query != null)	query.setCacheable(false);
				}            	
	        }
	    });
	}
	
	/**
	 * <li>@param sql
	 * <li>@param t 实体泛型
	 * <li>@return
	 * <li>返回类型：List
	 * <li>说明：执行sql查询并自动封装实体
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-7-22
	 * <li>修改人： 
     * <li>修改日期：
     * <li>修改说明：
	 */
	public List executeSqlQueryEntity(final String sql, final Class t){
		return acquireSqlQueryEntity(sql,t,false);
	}
	
	/**
	 * <li>@param sql
	 * <li>@param t 实体泛型
	 * <li>@return
	 * <li>返回类型：List
	 * <li>说明：执行sql查询并自动封装实体,并启用查询缓存
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-13
	 * <li>修改人： 
     * <li>修改日期：
     * <li>修改说明：
	 */
	public List cacheSqlQueryEntity(final String sql, final Class t){
		return acquireSqlQueryEntity(sql,t,true);
	}
	
	/**
	 * <li>方法名：acquireSqlQueryEntity
	 * <li>@param sql
	 * <li>@param t 实体泛型
	 * <li>@param isQueryCacheEnabled 是否启用查询缓存
	 * <li>@return
	 * <li>返回类型：List
	 * <li>说明：执行sql查询并自动封装实体,根据配置自动识别是否需要进行缓存处理
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-13
	 * <li>修改人： 
     * <li>修改日期：
     * <li>修改说明：
	 */
	@SuppressWarnings("unused")
	private List acquireSqlQueryEntity(final String sql, final Class t, final Boolean isQueryCacheEnabled){
		return (List)getHibernateTemplate().execute(new HibernateCallback(){
			public List doInHibernate(Session s) {
				SQLQuery query = null;
	            try {
					query = s.createSQLQuery(sql).addEntity(t);
					query.setCacheable(isQueryCacheEnabled);
					List list = query.list();
					return list;
				} catch (HibernateException e) {
					throw e;
				} finally {
					if(query != null)	query.setCacheable(false);
				}
	        }
	    });
	}
	
	/**
	 * <li>方法名：executeSqlProc
	 * <li>@param procNme存储过程名称
	 * <li>@param param参数
	 * <li>@return
	 * <li>返回类型：List
	 * <li>说明：调用存储过程
	 * <li>创建人：田华
	 * <li>创建日期：2011-7-5
	 * <li>修改人： 张凡
     * <li>修改日期：2013-2-6
     * <li>修改说明：修复无法释放连接池资源的问题
	 */
	public List executeSqlProc(final String procNme,final String ...param){	    
	    return (List)getHibernateTemplate().execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
                if(!StringUtils.isBlank(procNme)){
                    try{
                        String sql = "";
                        int cn = 0;
                        if(param!=null){
                            cn = param.length;
                        }
                        for(int i = 0; i<cn; i++){
                            sql+=",?";
                        }
                        if(cn>0){
                            sql = sql.substring(1,sql.length());
                        }
                        sql = "{Call "+procNme+"("+sql+")}";
                        SQLQuery query = s.createSQLQuery(sql);
                        for(int i = 0; i<cn; i++){
                            query.setString(i, param[i]);
                        }
                        return query.list();
                    }catch(Exception e){
                        return null;
                    }
                }else{
                    return null;
                }
            }
        });	
	}
	/**
	 * <li>方法名：executeSqlProc
	 * <li>@param procNme存储过程名称
	 * <li>@param param参数
	 * <li>@return
	 * <li>返回类型：void
	 * <li>说明：调用无返回结果的存储过程
	 * <li>创建人：田华
	 * <li>创建日期：2011-7-5
	 * <li>修改人： 刘晓斌2013-3-2修改：避免数据连接泄漏
	 * <li>修改日期：
	 */
	public void executeProc(String procNme, final String ...param){
		if(!StringUtils.isBlank(procNme)){
			String sql = "";
			int cn = 0;
			if(param!=null){
				cn = param.length;
			}
			for(int i = 0; i<cn; i++){
				sql+=",?";
			}
			if(cn>0){
				sql = sql.substring(1,sql.length());
			}
			sql = "{Call "+procNme+"("+sql+")}";
			
			final String finalstr = sql;
			final int finalcn = cn;
			this.getHibernateTemplate().execute(new HibernateCallback(){
	            public Integer doInHibernate(Session s) {
	            	SQLQuery query = s.createSQLQuery(finalstr);
					for(int i = 0; i<finalcn; i++){
						query.setString(i, param[i]);
					}
					return query.executeUpdate();
	            }
	        });

		}
	}

	/**
	 * <li>方法名：getEntityClass
	 * <li>
	 * @param entity
	 *            实体对象。
	 *            <li>
	 * @return 返回实体对象的类型，主要用于根据search bean实现默认的查询。
	 *         <li>返回类型：Class
	 *         <li>说明：根据指定的实体对象获取实体对象所对应的实体类型。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public Class getEntityClass(Object entity) {
		if (getSessionFactory().getClassMetadata(entity.getClass()) == null) {
			return entity.getClass().getSuperclass();
		} else {
			return entity.getClass();
		}
	}

	/**
	 * <li>方法名：findByEntity
	 * <li>
	 * @param exampleEntity
	 *            查询需要使用的实体对象。
	 *            <li>
	 * @param pageNo
	 *            需要查询记录的分页页码。
	 *            <li>
	 * @param pageSize
	 *            每页显示的记录数量。
	 *            <li>
	 * @param orderString
	 *            排序字符串
	 *            <li>
	 * @return 返回查询结果集合。
	 *         <li>返回类型：List
	 *         <li>说明：根据实体属性的取值进行查询，后台分页。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *         <li>修改日期：
	 * 
	 */
	public List findByEntity(final Object exampleEntity, final int rowStart, final int rowEnd,
			final String orders) {
		return (List) this.getHibernateTemplate().execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
        		Criteria criteria = s.createCriteria(
        				getEntityClass(exampleEntity)).add(
        				Example.create(exampleEntity).enableLike(MatchMode.ANYWHERE));
        		Assert.notNull(criteria);

        		if (orders != null && orders.length() > 0) {
        			List<SortObject> hs = YDStringUtil.orderString(orders);
        			for (int i = 0; i < hs.size(); ++i) {
        				SortObject sortProperty = hs.get(i);
        				if (sortProperty.getSortType().compareTo(ASC) == 0) {
        					criteria
        							.addOrder(Order.asc(sortProperty.getSortProperty()));
        				} else if (sortProperty.getSortType().compareTo(DESC) == 0) {
        					criteria.addOrder(Order
        							.desc(sortProperty.getSortProperty()));
        				}
        			}
        		}
        		return criteria.setFirstResult(rowStart).setMaxResults(
        				rowEnd - rowStart).list();
            }
        });
	}

	/**
	 * <li>方法名：findSingleByEntity
	 * <li>
	 * @param exampleEntity
	 *            查询需要使用的实体对象。
	 *            <li>
	 * @return
	 *            <li>返回类型：Object
	 *            <li>说明：根据指定的实体进行查询，只返回一个查询结果
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *            <li>修改日期：
	 */

	public Object findSingleByEntity(final Object exampleEntity) {
		return this.getHibernateTemplate().execute(new HibernateCallback(){
            public Object doInHibernate(Session s) {
            	Criteria criteria = s.createCriteria(
        				getEntityClass(exampleEntity)).add(
        				Example.create(exampleEntity).enableLike(MatchMode.ANYWHERE));
        		Assert.notNull(criteria);

        		List list = criteria.setMaxResults(1).list();
        		if (list != null && list.size() > 0) {
        			return list.get(0);
        		}
        		return null;
            }
        });		
	}

	/**
	 * <li>方法名：findByEntity
	 * <li>
	 * @param exampleEntity
	 *            <li>
	 * @param orders
	 *            <li>
	 * @return
	 *            <li>返回类型：List
	 *            <li>说明：
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-3
	 *            <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *            <li>修改日期：
	 */
	public List findByEntity(final Object exampleEntity, final String orders) {
		
		return (List) this.getHibernateTemplate().execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
            	Criteria criteria = s.createCriteria(
        				getEntityClass(exampleEntity)).add(
        				Example.create(exampleEntity).enableLike(MatchMode.ANYWHERE));
        		Assert.notNull(criteria);

        		if (orders != null && orders.length() > 0) {
        			List<SortObject> hs = YDStringUtil.orderString(orders);
        			for (int i = 0; i < hs.size(); ++i) {
        				SortObject sortProperty = hs.get(i);
        				if (sortProperty.getSortType().compareTo(ASC) == 0) {
        					criteria
        							.addOrder(Order.asc(sortProperty.getSortProperty()));
        				} else if (sortProperty.getSortType().compareTo(DESC) == 0) {
        					criteria.addOrder(Order
        							.desc(sortProperty.getSortProperty()));
        				}
        			}
        		}
        		return criteria.list();
            }
        });		
	}

	/**
	 * <li>方法名：getCount
	 * <li>
	 * @param exampleEntity
	 *            查询需要使用的实体对象。
	 *            <li>
	 * @return 返回查询结果记录总数量。
	 *         <li>返回类型：int
	 *         <li>说明：根据指定的实体对象进行查询时，获取查询结果记录总数量。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *         <li>修改日期：
	 */
	public int getCount(final Object exampleEntity) {
		return (Integer) this.getHibernateTemplate().execute(new HibernateCallback(){
            public Integer doInHibernate(Session s) {
            	Criteria criteria = s.createCriteria(
        				getEntityClass(exampleEntity)).add(
        				Example.create(exampleEntity).enableLike(MatchMode.ANYWHERE));
        		Assert.notNull(criteria);

        		// 执行查询
        		int totalCount = (Integer) criteria.setProjection(
        				Projections.rowCount()).uniqueResult();
        		return totalCount;
            }
        });		
	}

	/**
	 * <li>方法名：isNotUniqueDD
	 * <li>
	 * @param entity
	 *            需判断是否唯一的对象
	 *            <li>
	 * @param criterions
	 *            判断规则集合
	 *            <li>
	 * @return
	 *            <li>返回类型：boolean
	 *            <li>说明：自定义规则进行唯一性判断
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *            <li>修改日期：
	 */
	public boolean isNotUniqueCriterion(final Object entity,
			final List<Criterion> criterions) {

		Integer resultint =   (Integer) this.getHibernateTemplate().execute(new HibernateCallback(){
            public Integer doInHibernate(Session s) {
            	Criteria criteria = s.createCriteria(getEntityClass(entity))
				.setProjection(Projections.rowCount());
				Integer result = null;
				for (Criterion crit : criterions) {// 加入定制条件
					criteria.add(crit);
				}
		
				String idName = getIdName(entity);
				Object id = getId(entity);
		
				if (id != null && !id.toString().equals("")) {// 新增还是修改
					criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
				}
		
				result = (Integer) criteria.uniqueResult();
		
				return result;
            }
        });
		
		return resultint.intValue()>0;		
	}

	/**
	 * <li>方法名：isNotUnique
	 * <li>
	 * @param entity
	 *            要检查的实体对象
	 *            <li>
	 * @param propertyName
	 *            要验证的属性名列表。
	 *            <li>
	 * @return 只要某个属性的值存在相同即返回false,所有属性的值都不存在相同的情况，返回true.
	 *         <li>
	 * @throws Exception
	 *             <li>返回类型：boolean
	 *             <li>说明：判断某些指定属性的值是否唯一；该方法主要供外部调用。
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-24
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public boolean isNotUnique(Object entity, List<String> propertyName) {
		List<Criterion> listCri = new ArrayList<Criterion>();

		for (String name : propertyName) {
			try {
				Object value = BeanUtils.forceGetProperty(entity, name);
				if (value instanceof String) {// 字符类型忽略大小写
					listCri.add(Restrictions.ilike(name, value));
				} else {
					listCri.add(Restrictions.eq(name, value));
				}
			} catch (NoSuchFieldException e) {
				throw new DaoException(e);
			}
		}

		return isNotUniqueCriterion(entity, listCri);
	}

	/**
	 * <li>方法名：isNotUnique
	 * <li>
	 * @param entity
	 *            要检查的实体对象
	 *            <li>
	 * @param propertyName
	 *            要验证的属性名
	 *            <li>
	 * @return 属性的值存在相同即返回false,属性的值都不存在相同的情况，返回true.
	 *         <li>
	 * @throws Exception
	 *             <li>返回类型：boolean
	 *             <li>说明：判断某个指定属性的值是否唯一；该方法主要供外部调用。
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-24
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public boolean isNotUnique(Object entity, String propertyName) {
		List<Criterion> listCri = new ArrayList<Criterion>();

		try {
			Object value = BeanUtils.getPropertyValue(entity, propertyName);
			if (value instanceof String) {
				listCri.add(Restrictions.ilike(propertyName, value));
			} else {
				listCri.add(Restrictions.eq(propertyName, value));
			}
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return isNotUniqueCriterion(entity, listCri);
	}

	/**
	 * <li>方法名：isExist
	 * <li>
	 * @param exampleEntity
	 *            需要使用的实体对象。
	 *            <li>
	 * @return 如果数据库中存在符合条件的数据，返回true；否则返回false.
	 *         <li>返回类型：boolean
	 *         <li>说明：判断数据库中是否存在满足条件的数据。如果数据库中存在符合条件的数据，返回true；否则返回false.
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-3
	 *         <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *         <li>修改日期：
	 */
	public boolean isExist(final Object exampleEntity) {
		
		Integer count = (Integer) this.getHibernateTemplate().execute(new HibernateCallback(){
            public Integer doInHibernate(Session s) {
            	Criteria criteria = s.createCriteria(
        				getEntityClass(exampleEntity)).add(
        				Example.create(exampleEntity));
        		Assert.notNull(criteria);
        		// 执行查询
        		int totalCount = (Integer) criteria.setFirstResult(0).setMaxResults(1)
        				.setProjection(Projections.rowCount()).uniqueResult();
        		return totalCount;
            }
        });
		return count > 0 ? true : false;		
	}

	/**
	 * <li>方法名：isExist
	 * <li>
	 * @param queryString
	 *            根据指定的hql语句，判断数据库中是否存在满足条件的数据。此参数为用户指定的hql语句。
	 *            <li>
	 * @return 如果存在满足条件的数据，返回true;否则返回false.
	 *         <li>返回类型：boolean
	 *         <li>说明：根据指定的hql语句，判断数据库中是否存在满足条件的数据;如果存在满足条件的数据，返回true;否则返回false.
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-3
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public boolean isExist(final String queryString) {
		Assert.hasText(queryString);
		// Count查询
		String countQueryString = removeSelect(removeOrders(queryString));
		return findSingle(countQueryString) != null ? true : false;
	}

	/**
	 * <li>方法名：getMaxProperty
	 * <li>
	 * @param entity
	 *            <li>
	 * @param propertyName
	 *            <li>
	 * @param criterions
	 *            <li>
	 * @return
	 *            <li>返回类型：Object
	 *            <li>说明：获得有casCode属性的对象同级下的某一属性的最大值
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-27
	 *            <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *            <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public Object getMaxProperty(final Object entity, final String propertyName,
			final Criterion... criterions) {
		String codeProName = "casCode";
		BeanWrapper bwEntity = new BeanWrapperImpl(entity);
		String entityCode = "";
		if (bwEntity.getPropertyValue(codeProName) != null) {
			entityCode = bwEntity.getPropertyValue(codeProName).toString();
		}
		if (entityCode.length() < 3) {// 处理树顶层id为"0"的情况
			entityCode = "";
		}
		if (!entityCode.endsWith("___")) {// 查询下级条件
			entityCode += "___";
			bwEntity.setPropertyValue(codeProName, entityCode);
		}

		final String codeProNamefinal = codeProName;
		final String entityCodefinal = entityCode;
		List relist = (List) this.getHibernateTemplate().execute(new HibernateCallback(){
            public List doInHibernate(Session s) {

        		Criteria criteria = s.createCriteria(getEntityClass(entity));
        		if (criterions != null) {
        			for (Criterion crit : criterions) {// 加入附加条件
        				criteria.add(crit);
        			}
        		}

        		criteria.add(Restrictions.like(codeProNamefinal, entityCodefinal));// 编码条件
        		criteria.addOrder(Order.desc(propertyName));// 按所需的属性排序
        		criteria.setFirstResult(0);
        		return criteria.list();
            }
        });
		if (relist.size() > 0) {
			BeanWrapper bwMaxObj = new BeanWrapperImpl(relist.get(0));
			return bwMaxObj.getPropertyValue(propertyName);// 符合条件的最大值
		}		
		return null;
	}

	/**
	 * <li>方法名：bluckInsert
	 * <li>
	 * @param v
	 *            要批量插入的实体向量。
	 *            <li>返回类型：void
	 *            <li>说明：本方法提供对业务实体的批量插入，传入的参数为实体的矢量容器。
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-26
	 *            <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *            <li>修改日期：
	 */
	public void bluckInsert(Collection v) {
		final Collection vfinal = v;
		this.getHibernateTemplate().execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
            	if(vfinal != null && vfinal.size() > 0){
        			
        			int i = 1;
        			for (Object object : vfinal) {
        				s.save(object);
        				if (i % 20 == 0) { // 20, same as the JDBC batch size
        					i = 1;
        					s.flush();
        					s.clear();
        				}
        				i++;
        			}
        			s.flush();
        			s.clear();
        		}
            	return null;
            }
        });		
	}

	/**
	 * <li>方法名：bluckUpdate
	 * <li>
	 * @param v
	 *            要批量更新的实体向量。
	 *            <li>返回类型：void
	 *            <li>说明：本方法提供对业务实体的批量更新，传入的参数为实体的矢量容器。
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-26
	 *            <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *            <li>修改日期：
	 */
	public void bluckUpdate(Collection v) {
		final Collection vfinal = v;
		this.getHibernateTemplate().execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
            	
            	if(vfinal != null && vfinal.size() > 0){ 
        			int i = 1;
        			for (Object object : vfinal) {
        				s.update(object);
        				if (i % 20 == 0) { // 20, same as the JDBC batch size
        					i = 1;
        					s.flush();
        					s.clear();
        				}
        				i++;
        			}
        			s.flush();
        			s.clear();
        			
        		}

    			return null;
            }
        });		
	}
	
	/**
	 * <li>方法名：bluckUpdate
	 * <li>
	 * @param v
	 *            要批量更新的实体向量。
	 *            <li>返回类型：void
	 *            <li>说明：本方法提供对Hql一句的批量执行
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-26
	 *            <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *            <li>修改日期：
	 */
	public void bluckUpdate(List<String> v) {
		
		final List<String> vfinal = v;
		this.getHibernateTemplate().execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
            	
            	if(vfinal != null && vfinal.size() > 0){
        			int i = 1;
        			Query query = null;	
        			for (String hql : vfinal) {				
        				query = s.createQuery(hql);
        				query.executeUpdate();
        				if (i % 20 == 0) { // 20, same as the JDBC batch size
        					i = 1;
        					s.flush();
        					s.clear();
        				}
        				i++;
        			}
        			
        			s.flush();
        			s.clear();
        		}
            	return null;
            }
        });		
	}

	/**
	 * <li>方法名：bluckRemove
	 * <li>
	 * @param v
	 *            要批量删除的实体向量。
	 *            <li>返回类型：void
	 *            <li>说明：本方法提供对业务实体的批量删除，传入的参数为实体的矢量容器。
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-26
	 *            <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *            <li>修改日期：
	 */
	public void bluckRemove(Collection v) {
		final Collection vfinal = v;
		this.getHibernateTemplate().execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
            	if(vfinal != null && vfinal.size() > 0){ 
        			int i = 1;
        			for (Object object : vfinal) {
        				s.delete(object);
        				if (i % 20 == 0) { // 20, same as the JDBC batch size
        					i = 1;
        					s.flush();
        					s.clear();
        				}
        				i++;
        			}
        			s.flush();
        			s.clear();
        		}
            	return null;
            }
        });		
	}
	
	/**
	 * <li>方法名：bluckInsertRt
	 * <li>@param v
	 * <li>@return
	 * <li>返回类型：Collection
	 * <li>说明：批量添加数据并返回值
	 * <li>创建人：田华
	 * <li>创建日期：2011-7-7
	 * <li>修改人： 刘晓斌2013-3-2修改：避免数据连接泄漏
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public Collection bluckInsertRt(Collection v) {
		
		final Collection vfinal = v;
		return (Collection) this.getHibernateTemplate().execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
            	List list = null;
        		if(vfinal != null && vfinal.size() > 0){
        			list = new ArrayList(); 
        			int i = 1;
        			for (Object object : vfinal) {
        				s.save(object);
        				if (i % 20 == 0) { // 20, same as the JDBC batch size
        					i = 1;
        					s.flush();
        					s.clear();
        				}
        				i++;
        				list.add(object);
        			}
        			s.flush();
        			s.clear();
        		}
        		return list;
            }
        });		
	}

	/**
	 * <li>方法名：findPage
	 * <li>
	 * @param queryString
	 *            要执行的hql语句。
	 *            <li>
	 * @param params
	 *            hql语句中的所有参数的集合。
	 *            <li>
	 * @param pageNo
	 *            要查询的分页记录的页码。
	 *            <li>
	 * @param pageSize
	 *            每页显示记录的条数。
	 *            <li>
	 * @return 返回查询结果集合。
	 *         <li>返回类型：List
	 *         <li>说明：执行带参数的hql语句，查询指定分页的数据。后台分页。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *         <li>修改日期：
	 */
	public List findPageSQL(final Object entity, final String[] cloumns,  final String queryString, final int rowStart,
			final int rowEnd) {
		return (List) this.getHibernateTemplate().execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
	            SQLQuery q = s.createSQLQuery(queryString);
	    		for (String c : cloumns) {
	    			try {
	    				q.addScalar(c,YDStringUtil.toConvertType(entity.getClass().getDeclaredField(c).getGenericType()));
	    			} catch (SecurityException e) {
	    				e.printStackTrace();
	    			} catch (NoSuchFieldException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    		q.setResultTransformer(Transformers.aliasToBean(entity.getClass()));
	    		q.setFirstResult(rowStart);
	    		q.setMaxResults(rowEnd - rowStart);
	    		return q.list();
    		}
        });		
	}

	/**
	 * <li>方法名：getCount
	 * @param queryString
	 *            要执行的hql语句。
	 *            <li>
	 * @return 返回某个hql语句查询结果记录的条数。
	 *         <li>返回类型：int
	 *         <li>说明：获取某个指定的不带参数的sql语句查询结果的总记录数量；用于分页查询。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：刘晓斌2013-3-2修改：避免数据连接泄漏
	 *         <li>修改日期：
	 */
	public int getCountSQL(final String queryString) {
        HibernateTemplate template = getHibernateTemplate();
        return (Integer)template.execute(new HibernateCallback(){
            public Integer doInHibernate(Session s) {
            	Assert.hasText(queryString);
        		String countQueryString = " SELECT COUNT (*)  FROM (" + queryString + ") ";
        		List countlist = s.createSQLQuery(countQueryString).list();
        		int totalCount = Integer.parseInt(countlist.get(0).toString());
        		return totalCount;
            }
        });		
	}
	
	/**
	 * <li>方法名：sql2Object
	 * <li>@param classType
	 * <li>@param mappings
	 * <li>@param sql
	 * <li>@param params
	 * <li>返回类型：List<T>
	 * <li>说明：根据sql查询语句的执行结果封装特例实体Bean对象集合(Hibernate环境)
	 * <li>创建人：袁健
	 * <li>创建日期：2012-4-9
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> sql2Object(Class<T> classType, final Map<String, NullableType> mappings, final String sql, final Object...params) {
		
		List<T> results = null;
		try {
		     HibernateTemplate template = getHibernateTemplate();
		     List<T> maps = (List<T>)template.execute(new HibernateCallback(){
		            public List<T> doInHibernate(Session s) {
		            	SQLQuery query = s.createSQLQuery(sql);
		    			
		    			if(params!= null && params.length>0){
		    				for (int i = 0; i < params.length; i++) {
		    					query.setParameter(i, params[i]);
		    				}
		    			}
		    			if (mappings != null && mappings.size() > 0) {
		    				for (Iterator<Entry<String, NullableType>> it = mappings.entrySet().iterator(); it.hasNext();) {
		    					Map.Entry<String, NullableType> entry = (Map.Entry<String, NullableType>) it.next();
		    					query.addScalar(entry.getKey(), entry.getValue());
		    				}
		    			}
		    			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		    			return query.list();
		            }
		        });
			
			results = new ArrayList<T>();
			
			T entity = null;
			Field[] fields = null;
			Map map = null;
			Map.Entry colResult = null;
			Object colName = null;
			Object colValue = null;
			String fieldName = "";
			String firstLetter = "";
			String setMethodName = "";
			Method setMethod = null;
			Field field = null;
			
			for (Iterator it = maps.iterator(); it.hasNext();) {
				entity = classType.newInstance();
				fields = classType.getDeclaredFields();
				map = (Map) it.next();
				for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
					colResult = (Map.Entry) iter.next();
					colName = colResult.getKey();
					colValue = colResult.getValue();
					for (int i = 0; i < fields.length; i++) {
						field = fields[i];
						fieldName = field.getName();
						if (fieldName.toLowerCase().equals(colName.toString().toLowerCase())) {
							firstLetter = fieldName.substring(0, 1).toUpperCase();
							setMethodName = "set" + firstLetter + fieldName.substring(1);
							setMethod = classType.getMethod(setMethodName, new Class[] { field.getType() });
							setMethod.invoke(entity, new Object[] { colValue });
							break;
						}
					}
				}
				results.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;		
	}
	
	/**
	 * <li>说明：该方法与HibernateTemplate.find(String hql, Object... param)相同，
	 * 		区别根据入参enableCache是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param enableCache 是否使用缓存
	 * @param hql hql语句
	 * @param params 替换hql语句中？的参数值数组 
	 * @return 结构列表
	 */
	public List find(boolean enableCache, final String hql, Object... params){
		final boolean useCache = enableCache;
		final String queryString = hql;
		final Object[] values = params;
        return (List)getHibernateTemplate().execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
            	Query query = s.createQuery(queryString);
            	if(values != null && values.length > 0){
            		for (int i = 0, len = values.length; i < len; i++) {
						query.setParameter(i, values[ i ]);
					}
            	}
            	return query.setCacheable(useCache).list();
            }
        });		
	}
	/**
	 * <li>说明：该方法与HibernateTemplate.find(String hql, Object param)相同，
	 * 		区别根据入参enableCache是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param enableCache 是否使用缓存
	 * @param hql hql语句
	 * @param params 替换hql语句中？的参数值数组 
	 * @return 结构列表
	 */
	public List find(boolean enableCache, String hql, Object param){
		return find(enableCache, hql, param);
	}
	/**
	 * <li>说明：该方法与HibernateTemplate.find(String hql)相同，通过入参enableCache控制是否使用缓存查询
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param enableCache：是否使用查询缓存
	 * @param hql：hql语句
	 * @return 结果列表
	 */
	public List find(boolean enableCache, String hql){
		return find(enableCache, hql, null);
	}
	/**
	 * <li>说明：根据hql查找只返回第一条记录，根据入参enableCache确定是否使用缓存查询
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param enableCache：是否使用查询缓存
	 * @param hql hibernate查询语句
	 * @return Object
	 */
	public Object findSingle(boolean enableCache, String hql){
		List list = find(enableCache, hql);
		if(list == null || list.size() < 1)	return null;
		return list.get(0);
	}	
	/**
	 * <li>说明：执行hql强制返回int型数据，提供缓存或非缓存查询， 注意hql必须返回int类型否则报错
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param enableCache 是否使用缓存
	 * @param hql 查询语句
	 * @return int型数据
	 */
	public int getInt(boolean enableCache, String hql){
		int i = 0;
		Object obj = findSingle(enableCache, hql);
		if(obj != null)		 i = ((Long)obj).intValue();
		return i;
	}	
	/**
	 * <li>说明：不分页查询，返回实体类列表对象，基于单表实体类不分页查询
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-15
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param query:包含动态查询条件参数的对象
	 * @return 实体类列表对象
	 * @throws BusinessException
	 */	
	@SuppressWarnings("unchecked")
	public <E> List<E> findByQC(final QueryCriteria<E> query) throws BusinessException{
		HibernateTemplate template = getHibernateTemplate();
		return (List<E>)template.execute(new HibernateCallback(){
			public List<E> doInHibernate(Session s){
				try {
					return query.getList(s, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
    }
	/**
	 * <li>说明：动态分页查询，返回实体类的分页列表对象
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-15
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param query 包含动态查询条件参数的对象
	 * @return 分页查询列表
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public<E> Page<E> findPageByQC(final QueryCriteria<E> query) throws BusinessException{
		HibernateTemplate template = getHibernateTemplate();
		return (Page<E>)template.execute(new HibernateCallback(){
			public Page<E> doInHibernate(Session s){
				try {
					return query.getPage(s, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
    }
	
	/**
	 * <li>说明：分页查询，返回实体类的分页列表对象，基于单表实体类分页查询
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-15
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 包装了实体类查询条件的对象
	 * @return 分页查询列表
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public <T> Page<T> findPageList(final SearchEntity<T> searchEntity) throws BusinessException{
		final boolean enableCache = false;
		HibernateTemplate template = getHibernateTemplate();
		return (Page<T>)template.execute(new HibernateCallback(){
			public Page<T> doInHibernate(Session s){
				try {
					T entity = searchEntity.getEntity();
					//如果该实体类存在逻辑删除字段，过滤逻辑删除记录
					if(EntityUtil.contains(entity.getClass(), EntityUtil.RECORD_STATUS)){
						BeanUtils.forceSetProperty(entity, EntityUtil.RECORD_STATUS, Constants.NO_DELETE);
					}
					//过滤掉idx、siteID、creator、createTime、updator、updateTime的查询条件
					Example exp = Example.create(entity)
						.excludeProperty(EntityUtil.IDX)
//						.excludeProperty(EntityUtil.SITE_ID)//由于多站点数据过滤需求，siteID视为业务字段
						.excludeProperty(EntityUtil.CREATOR)
						.excludeProperty(EntityUtil.CREATE_TIME)
						.excludeProperty(EntityUtil.UPDATOR)
						.excludeProperty(EntityUtil.UPDATE_TIME)
						.enableLike().enableLike(MatchMode.ANYWHERE);
					//查询总记录数
					Criteria criteria = s.createCriteria(entity.getClass())
						.add(exp)
						.setProjection(Projections.rowCount())
						.setCacheable(enableCache);
					int total = ((Integer)criteria.uniqueResult()).intValue();
					//分页列表
					criteria = s.createCriteria(entity.getClass()).add(exp)
						.setFirstResult(searchEntity.getStart())
						.setMaxResults(searchEntity.getLimit());
					//设置排序规则
					Order[] orders = searchEntity.getOrders();
					if(orders != null){
						for (Order order : orders) {
							criteria.addOrder(order);
						}					
					}
					//如果该实体类存在修改时间字段，则追加按修改时间倒序
					if(EntityUtil.contains(entity.getClass(), EntityUtil.UPDATE_TIME)){
						criteria.addOrder(Order.desc(EntityUtil.UPDATE_TIME));
					}
					criteria.setCacheable(enableCache);
					return new Page<T>(total, criteria.list());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
    }	
	
	/**
	* <li>说明： 提供一个简化的批量保存更新，该方法其实上循环调用Template.saveOrUpdate进行，并没有利用JDBC底层进行批量操作
	* <li>创建人： 黄杨
	* <li>创建日期：2017-5-9
	* <li>修改人：
	* <li>修改内容：
	* <li>修改日期：
	*@param entityList
	*/
	public void saveOrUpdateAll(Collection<?> entityList) {
		HibernateTemplate tmp = getHibernateTemplate();
		for (Object entity : entityList) {
			tmp.saveOrUpdate(entity);
		}
	}
	
}
