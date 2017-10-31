package com.yunda.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.primeton.workflow.api.WFServiceException;
import com.yunda.common.BusinessException;
import com.yunda.util.DaoUtils;
import com.yunda.util.GenericsUtils;
import com.yunda.util.IbatisDaoUtils;

/**
 * 
 * <li>类型名称：
 * <li>说明：普通服务类的基类，该基类提供了增删改查业务方法的基本实现；本类主要调用daoUtils对象进行数据库操作
 * <li>要继承基类必须提供T，S两个范型参数，T指明manger操作的实体类，S指明了manager查询时使用的SearchBean类型
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-01-14
 * <li>修改人：
 * <li>修改日期：
 */
public abstract class BaseManager<T, S> extends FlowManager {

	protected DaoUtils daoUtils; // Hibernate数据库操作工具
	
	protected IbatisDaoUtils ibatisDaoUtils;//Ibatis数据库操作工具

	protected Class<T> entityClass; // DAO所管理的Entity类型.

	/**
	 * <li>方法名：getIbatisDaoUtils
	 * <li>@return Ibatis数据库操作工具
	 * <li>返回类型：IbatisDaoUtils
	 * <li>说明：返回Ibatis数据库操作工具
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public IbatisDaoUtils getIbatisDaoUtils() {
		return ibatisDaoUtils;
	}

	/**
	 * <li>方法名：setIbatisDaoUtils
	 * <li>@param ibatisDaoUtils Ibatis数据库操作工具
	 * <li>返回类型：void
	 * <li>说明：spring容器通过依赖注入机制调用此方法将数据库操作工具注入进来；整个应用只有有个数据库操作工具实例。
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-5-5
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public void setIbatisDaoUtils(IbatisDaoUtils ibatisDaoUtils) {
		this.ibatisDaoUtils = ibatisDaoUtils;
	}

	/**
	 * <li>方法名：getDaoUtils
	 * @return 返回Hibernate数据库操作工具
	 *         <li>返回类型：DaoUtils
	 *         <li>说明：获取Hibernate数据库操作工具
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	public DaoUtils getDaoUtils() {
		return daoUtils;
	}

	/**
	 * <li>方法名：setDaoUtils
	 * @param daoUtils   Hibernate数据库操作工具
	 *            <li>返回类型：void
	 *            <li>说明：spring容器通过依赖注入机制调用此方法将数据库操作工具注入进来；整个应用只有有个数据库操作工具实例。
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */

	public void setDaoUtils(DaoUtils daoUtils) {
		this.daoUtils = daoUtils;
	}

	/**
	 * 
	 * <li>说明：根据子类定义时传入的实体类参数设置entityClass
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-01-14
	 * <li>修改人：
	 * <li>修改日期：
	 * @throws WFServiceException 
	 */
	@SuppressWarnings("unchecked")
	public BaseManager(){
		entityClass = GenericsUtils.getSuperClassGenricType(getClass());
	}

	/**
	 * 
	 * <li>方法名：get
	 * 
	 * @param id
	 *            实体身份标示
	 *            <li>
	 * @return 返回范型指定类型的实体
	 *         <li>返回类型：T
	 *         <li>说明：根据制定的实体身份标示从数据库获取实体。
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public T getModelById(Serializable id) throws BusinessException {
		Class idType = daoUtils.getIdType(entityClass);
		Long tempId;
		if (Long.class.equals(idType)
				|| BigDecimal.class.equals(idType)
				|| Integer.class.equals(idType)
				|| Float.class.equals(idType)) {
			tempId = Long.valueOf(String.valueOf(id));
			return (T) daoUtils.get(tempId, entityClass);
		} else {
			return (T) daoUtils.get(id, entityClass);
		}
	}

	/**
	 * 
	 * <li>方法名：getModelIdName
	 * <li>
	 * 
	 * @param entity
	 *            <li>
	 * @return
	 *            <li>返回类型：String
	 *            <li>说明：通过实体对象查找对象标示ID属性名
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-3-2
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public String getModelIdName(Object entity) {
		return daoUtils.getSessionFactory().getClassMetadata(entityClass)
				.getIdentifierPropertyName();
	}

	/**
	 * 
	 * <li>方法名：getAll
	 * 
	 * @return 返回注入实体类型的所有实体
	 *         <li>返回类型：List<T>
	 *         <li>说明：获取注入实体类型的所有实体
	 *         <li>创建人：曾锤鑫
	 *         <li>创建日期：2011-01-14
	 *         <li>修改人：
	 *         <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAll() throws BusinessException {
		return daoUtils.getAll(entityClass);
	}

	/**
	 * 
	 * <li>方法名：find
	 * 
	 * @param hql
	 *            <li>
	 * @return
	 *            <li>
	 * @throws BusinessException
	 *             <li>返回类型：Collection
	 *             <li>说明：
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-14
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	public Collection find(String hql) throws BusinessException {
		return daoUtils.find(hql);
	}

	/**
	 * 
	 * <li>方法名：findSingle
	 * <li>
	 * 
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
	@SuppressWarnings("unchecked")
	public T findSingle(String hql) throws BusinessException {
		return (T) daoUtils.findSingle(hql);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public T save(T entity) throws BusinessException {
		checkUpdate(entity);
		return (T) daoUtils.saveOrUpdate(entity);
	}

	/**
	 * <li>方法名：insert
	 * @param entity
	 *            范型指定类型的实体。
	 *            <li>
	 * @return 返回范型制定类型的实体对象。
	 *         <li>
	 * @throws BusinessException
	 *             <li>返回类型：T
	 *             <li>说明：新增实体。
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-6
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public T insert(T entity) throws BusinessException {
		checkUpdate(entity);
		return (T) daoUtils.insert(entity);
	}

	/**
	 * 
	 * <li>方法名：update
	 * @param entity
	 *            范型指定类型的实体。
	 *            <li>
	 * @return 返回范型制定类型的实体对象。
	 *         <li>
	 * @throws BusinessException
	 *             <li>返回类型：T
	 *             <li>说明：更新实体。
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-6
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public T update(T entity) throws BusinessException {
		checkUpdate(entity);
		return (T) daoUtils.update(entity);
	}

	/**
	 * <li>方法名：deleteByIds
	 * @param ids
	 *            根据制定的实体身份标示数组进行批量删除实体对象。
	 *            <li>返回类型：void
	 *            <li>说明：根据制定的实体身份标示数组进行批量删除实体；如果删除业务实体前需要做一些特殊的业务判断或事情；
	 *            <li>请在子类中覆盖checkDelete方法。
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-01-14
	 *            <li>修改人：
	 *            <li>修改日期：
	 */
	public void deleteByIds(Serializable... ids) throws BusinessException {
		try {
			for (Serializable id : ids) {
				checkDelete(id);
			}
			daoUtils.removeByIds(ids, entityClass, getModelIdName(entityClass));// 按ID删除
			// daoUtils.removeObject(ids, entityClass);// 按对象删除
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * 
	 * <li>方法名：checkUnique
	 * <li>
	 * 
	 * @param entity
	 *            范型指定类型的实体对象。
	 *            <li>
	 * @throws BusinessException
	 *             <li>返回类型：void
	 *             <li>说明：检查某个字段或某些字段是否有重复的情况；在保存，修改之前调用；强制程序员实现。
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-24
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	abstract protected void checkUpdate(T entity) throws BusinessException;

	/**
	 * 
	 * <li>方法名：checkDelete
	 * <li>
	 * 
	 * @param id
	 *            范型指定类型的实体对象的ID。
	 *            <li>
	 * @throws BusinessException
	 *             <li>返回类型：void
	 *             <li>说明：在删除时需要做的删除验证
	 *             <li>创建人：曾锤鑫
	 *             <li>创建日期：2011-01-13
	 *             <li>修改人：
	 *             <li>修改日期：
	 */
	abstract protected void checkDelete(Serializable id)
			throws BusinessException;
	
}
