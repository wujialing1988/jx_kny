/**
 * <li>文件名：IbatisDaoUtils.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-5-5
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.util.Assert;

/**
 * <li>类型名称：
 * <li>说明：框架中的数据库Ibatis访问操作工具
 * <li>可供所有的业务服务对象都调用此对象完成数据库的操作。
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-5-5
 * <li>修改人： 
 * <li>修改日期：
 */
public class IbatisDaoUtils extends SqlMapClientDaoSupport{

	public static final String POSTFIX_INSERT = "insert";

	public static final String POSTFIX_UPDATE = "update";

	public static final String POSTFIX_DELETE = "delete";

	public static final String POSTFIX_DELETE_PRIAMARYKEY = "deleteByPrimaryKey";

	public static final String POSTFIX_SELECT = "select";

	public static final String POSTFIX_GETALL = "getAll";

	public static final String POSTFIX_SELECTMAP = "selectByMap";

	public static final String POSTFIX_SELECTSQL = "selectBySql";

	public static final String POSTFIX_COUNT = "count";

	public static final String POSTFIX_QUERY = "query";

	/**
	 * <li>方法名：getStatementId
	 * <li>@param entityClass 实体类型
	 * <li>@param suffix 标识
	 * <li>@return SQL Map文件中对应的statement标识
	 * <li>返回类型：String
	 * <li>说明：获取SQL Map文件中对应的statement标识
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public String getStatementId(Class entityClass, String suffix) {
		/*String className = entityClass.getName();
		String shortName = className.replace(entityClass.getPackage().getName()
				+ ".", "");
		return shortName + suffix;*/
		
		return  suffix;
	}
	
	/**
	 * <li>方法名：get
	 * <li>@param id 数据ID
	 * <li>@param entityClass 实体类型
	 * <li>@return 
	 * <li>返回类型：Object
	 * <li>说明：根据ID获取对象
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public Object get(Serializable id, Class entityClass) {
		return getSqlMapClientTemplate().queryForObject(
				getStatementId(entityClass, IbatisDaoUtils.POSTFIX_SELECT),
				id);
	}
	
	/**
	 * <li>方法名：queryForObject
	 * <li>@param entityClass 实体类型
	 * <li>@param statementId SQL Map文件对应标识
	 * <li>@param parameters 参数
	 * <li>@return
	 * <li>返回类型：Object
	 * <li>说明：获取对象
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public Object get(Class entityClass, String statementId,
			Object parameters) {
		return getSqlMapClientTemplate().queryForObject(
				getStatementId(entityClass, statementId), parameters);
	}

	/**
	 * <li>方法名：insert
	 * <li>@param entity 实体对象
	 * <li>@return 
	 * <li>返回类型：Object
	 * <li>说明：向数据库中插入一个新的实体对象
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public Object insert(Object entity) {
		return getSqlMapClientTemplate().insert(
				getStatementId(entity.getClass(), IbatisDaoUtils.POSTFIX_INSERT),
				entity);
	}

	/**
	 * <li>方法名：update
	 * <li>@param entity 实体对象
	 * <li>@return 
	 * <li>返回类型：Object
	 * <li>说明：更新数据库中的实体对象
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public Object update(Object entity) {
		return getSqlMapClientTemplate().update(
				getStatementId(entity.getClass(), IbatisDaoUtils.POSTFIX_UPDATE),
				entity);
	}

	/**
	 * <li>方法名：update
	 * <li>@param entityClass 实体类型
	 * <li>@param statementId SQL Map文件对应标识
	 * <li>@param parameters 参数
	 * <li>@return
	 * <li>返回类型：Object
	 * <li>说明：通过SQL Map配置的SQL更新数据库中的实体对象
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public Object update(Class entityClass, String statementId, Object parameters) {
		return getSqlMapClientTemplate().update(
				getStatementId(entityClass, statementId), parameters);
	}

	/**
	 * <li>方法名：removeById
	 * <li>@param id  实体身份标示
	 * <li>@param entityClass 实体类型
	 * <li>返回类型：void
	 * <li>说明：根据ID移除对象
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public void removeById(Serializable id, Class entityClass) {
		getSqlMapClientTemplate().delete(
				getStatementId(entityClass,
						IbatisDaoUtils.POSTFIX_DELETE_PRIAMARYKEY), id);
	}
	
	/**
	 * <li>方法名：find 
	 * <li>@param entityClass 实体类型
	 * <li>@param statementId SQL Map文件对应标识
	 * <li>@param parameters 参数
	 * <li>@return
	 * <li>返回类型：List<Object>
	 * <li>说明：获取对象列表
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public  List<Object> find(Class entityClass, String statementId,
			Object parameters) {
		return getSqlMapClientTemplate().queryForList(
				getStatementId(entityClass, statementId), parameters);
	}

	/**
	 * <li>方法名：findPage
	 * <li>@param entityClass 实体类型
	 * <li>@param parameterObject 参数对象
	 * <li>@param start 起始条数
	 * <li>@param limit 每页条数
	 * <li>@return 分页对象
	 * <li>返回类型：Page
	 * <li>说明：执行分页语句，查询指定分页的数据。后台分页。
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public Page findPage(Class entityClass, Map parameterObject, int start,
			int limit) {
		Assert.isTrue(start >= 0, "pageNo should start from 0");
		// 计算总数 
		Integer totalCount = (Integer) getSqlMapClientTemplate()
				.queryForObject(
						getStatementId(entityClass,
								IbatisDaoUtils.POSTFIX_COUNT),
						parameterObject);

		// 如果没有数据则返回Empty Page
		Assert.notNull(totalCount, "totalCount Error");

		if (totalCount.intValue() == 0) {
			return new Page();
		}

		List list;
		int totalPageCount = 0;
		int startIndex = 0;

		// 如果pageSize小于0,则返回所有数捄1177,等同于getAll
		if (limit > 0) {

			// 计算页数
			totalPageCount = (totalCount / limit);
			totalPageCount += ((totalCount % limit) > 0) ? 1 : 0;

			// 计算skip数量
			if (totalCount > start) {
				startIndex = start;
			} else {
				startIndex = (totalPageCount - 1) * limit;
			}

			if (parameterObject == null)
				parameterObject = new HashMap();

			parameterObject.put("startIndex", startIndex);
			parameterObject.put("endIndex", limit);

			list = getSqlMapClientTemplate()
					.queryForList(
							getStatementId(entityClass,
									IbatisDaoUtils.POSTFIX_QUERY),
							parameterObject);

		} else {
			list = getSqlMapClientTemplate()
					.queryForList(
							getStatementId(entityClass,
									IbatisDaoUtils.POSTFIX_QUERY),
							parameterObject);
		}
		return new Page(startIndex, totalCount, limit, list);
	}
	
	/**
	 * <li>方法名：findPage
	 * <li>@param entityClass 实体类型
	 * <li>@param parameterObject 参数对象
	 * <li>@param start 起始条数
	 * <li>@param limit 每页条数
	 * <li>@param countSqlId 查询记录数Sql标识
	 * <li>@param pageQuerySqlId 分页查询Sql标识
	 * <li>@return  分页对象
	 * <li>返回类型：Page
	 * <li>说明：
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public Page findPage(Class entityClass, Map parameterObject, int start,
			int limit, String countSqlId, String pageQuerySqlId) {

		Assert.isTrue(start >= 0, "pageNo should start from 0");

		// 计算总数 
		Integer totalCount = (Integer) getSqlMapClientTemplate()
				.queryForObject(
						getStatementId(entityClass,
								countSqlId),
						parameterObject);

		// 如果没有数据则返回Empty Page
		Assert.notNull(totalCount, "totalCount Error");

		if (totalCount.intValue() == 0) {
			return new Page();
		}

		List list;
		int totalPageCount = 0;
		int startIndex = 0;

		// 如果pageSize小于0,则返回所有数捄1177,等同于getAll
		if (limit > 0) {

			// 计算页数
			totalPageCount = (totalCount / limit);
			totalPageCount += ((totalCount % limit) > 0) ? 1 : 0;

			// 计算skip数量
			if (totalCount >= start) {
				startIndex = start;
			} else {
				startIndex = (totalPageCount - 1) * limit;
			}

			if (parameterObject == null)
				parameterObject = new HashMap();

			parameterObject.put("startIndex", startIndex);
			parameterObject.put("endIndex",  limit);

			list = getSqlMapClientTemplate()
					.queryForList(
							getStatementId(entityClass,
									pageQuerySqlId),
							parameterObject);

		} else {
			list = getSqlMapClientTemplate()
					.queryForList(
							getStatementId(entityClass,
									pageQuerySqlId),
							parameterObject);
		}
		return new Page(startIndex, totalCount, limit, list);
	}
}