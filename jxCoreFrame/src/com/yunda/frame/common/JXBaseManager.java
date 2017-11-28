package com.yunda.frame.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.eos.workflow.api.BPSServiceClientFactory;
import com.eos.workflow.api.IBPSServiceClient;
import com.yunda.base.BaseManager;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.flow.util.BPSServiceClientUtil;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.TransformerEntity;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.util.BeanUtils;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：该类继承BaseManager，封装检修系统业务层相关的功能如逻辑删除等。
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-07
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public abstract class JXBaseManager<T, S> extends BaseManager<T, S>{
	/** 日志工具 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * <li>说明：删除实体对象前的验证业务，该方法暂不推荐使用，建议使用validateDelete
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @deprecated 该方法暂不推荐使用，建议使用validateDelete
	 * @throws BusinessException
	 */	
	@Override
	protected void checkDelete(Serializable id) throws BusinessException {}
	
	/**
	 * <li>说明：新增修改保存前的实体对象前的验证业务，该方法暂不推荐使用，建议使用validateUpdate
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @deprecated 该方法暂不推荐使用，建议使用validateUpdate
	 * @throws BusinessException
	 */		
	@Override
	protected void checkUpdate(T t) throws BusinessException {}	
	/**
	 * <li>说明：更新前验证
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	public String[] validateUpdate(T t){return null;}
	
	/**
	 * <li>说明：删除前验证
	 * <li>创建人：王治龙
	 * <li>创建日期：2012-8-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param ids 主键idx数组
	 * @return 返回删除操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	public String[] validateDelete(Serializable... ids){return null;}
	
	/**
	 * <li>说明：读取JXConfig.xml配置文件, 根据参数确定是否需要启用基础管理模块的查询缓存功能
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return Boolean true:启用;false:禁用
	 */
	public Boolean isJcglQueryCacheEnabled(){
		Boolean flag = false;
		flag = Boolean.valueOf(JXConfig.getInstance().getUseJcglQueryCache());
		return flag;
	}
	/**
	 * <li>说明：新增或更新一组实体对象，该方法只适用于检修系统v2.0的数据模型实体类，统一设置业务无关的五个字段（创建人、
	 * 创建时间、修改人、修改时间、站点标识）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-07
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param List<T> 实体类对象集合 
	 * @return void
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void saveOrUpdate(List<T> entityList) throws BusinessException, NoSuchFieldException {
		for (T t : entityList) {
			t = EntityUtil.setSysinfo(t);
			//设置逻辑删除字段状态为未删除
			t = EntityUtil.setNoDelete(t);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
	}
	/**
	 * <li>说明：新增或更新一组实体对象，该方法只适用于检修系统v2.0的数据模型实体类，统一设置业务无关的五个字段（创建人、
	 * 创建时间、修改人、修改时间、站点标识）
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-07
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param T t 实体对象
	 * @return void 
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void saveOrUpdate(T t) throws BusinessException, NoSuchFieldException {
		// 验证是否是一条新增（isNew: true）的数据
		ClassMetadata cm = daoUtils.getSessionFactory().getClassMetadata(entityClass);
		Object id = BeanUtils.forceGetProperty(t, cm.getIdentifierPropertyName());
		boolean isNew = null == id;
		if (!isNew && id instanceof String) {
			isNew = 0 >= ((String) id).trim().length();
		}
		t = EntityUtil.setSysinfo(t);
		//设置逻辑删除字段状态为未删除
		t = EntityUtil.setNoDelete(t);
		this.daoUtils.getHibernateTemplate().saveOrUpdate(t);
		// 更新之后业务处理
		this.updateAfter(t, isNew);
	}	
	
	/**
	 * <li>说明：更新之后业务处理(2017年5月3日添加自设备系统by黄杨)
	 * <li>创建人：张凡
	 * <li>创建日期：2015年6月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @paramt 实体
	 * @param isNew 是否为新增，true：新增（插入）、false：更新
	 */
	protected void updateAfter(T t, boolean isNew) {
	}
	
	/**
	 * <li>说明：逻辑删除记录，根据制定的实体身份标示数组进行批量删除实体
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-07
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 实体类主键idx数组
	 * @return void
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void logicDelete(Serializable... ids) throws BusinessException, NoSuchFieldException {
		List<T> entityList = new ArrayList<T>();
		for (Serializable id : ids) {
			T t = getModelById(id);
			t = EntityUtil.setSysinfo(t);
//			设置逻辑删除字段状态为已删除
			t = EntityUtil.setDeleted(t);
			entityList.add(t);
		}
		this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
	}
	/**
	 * <li>说明：逻辑删除记录，根据制定的实体身份标示数组进行批量删除实体
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-07
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param id 实体类主键idx
	 * @return void
	 * @throws BusinessException, NoSuchFieldException
	 */	
	public void logicDelete(Serializable id) throws BusinessException, NoSuchFieldException {
		T t = getModelById(id);
		t = EntityUtil.setSysinfo(t);
//		设置逻辑删除字段状态为已删除		
		t = EntityUtil.setDeleted(t);
		this.daoUtils.getHibernateTemplate().saveOrUpdate(t);
	}
	/**
	 * <li>说明：判定该业务类是否使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return true使用查询缓存，false不使用
	 */
	protected boolean enableCache(){
		return false;
	}
	
	/**
	 * <li>说明：分页查询，返回实体类的分页列表对象，基于单表实体类分页查询
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-8-30
	 * <li>修改人： 谭诚
	 * <li>修改日期： 2013-8-13
	 * <li>修改内容： 将方法体的实现移除, 交由acquirePageList()实现功能
	 * @param totalHql 查询总记录数的hql语句
	 * @param hql 查询语句
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @param isQueryCacheEnabled 是否启用查询缓存
	 * @return Page<T> 分页查询列表
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPageList(String totalHql, String hql, int start, int limit) throws BusinessException{
		return acquirePageList(totalHql, hql, start, limit, false);
    }

	/**
	 * <li>方法名称：updateSql
	 * <li>方法说明：执行SQL更新 
	 * <li>@param sql
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>return: int
	 * <li>创建人：张凡
	 * <li>创建时间：2013-2-6 上午11:01:22
	 * <li>修改人：
	 * <li>修改内容：
	 */
    public int updateSql(final String sql) throws BusinessException{
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Integer)template.execute(new HibernateCallback(){
            public Integer doInHibernate(Session s) {
                return s.createSQLQuery(sql).executeUpdate();
            }
        });
    }
    
    /**
     * <li>方法名称：getSQLQuery
     * <li>方法说明：执行SQL查询 
     * <li>@param sql
     * <li>@return
     * <li>@throws BusinessException
     * <li>return: List
     * <li>创建人：张凡
     * <li>创建时间：2013-2-6 上午11:02:29
     * <li>修改人：
     * <li>修改内容：
     */
    public List getSQLQuery(final String sql) throws BusinessException{
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (List)template.execute(new HibernateCallback(){
            public List doInHibernate(Session s) {
                return s.createSQLQuery(sql).setCacheable(false).list();
            }
        });
    }    
	/**
	 * <li>方法名称：findPageList
	 * <li>方法说明：分页查询，返回实体类的分页对表对象，基于SQL查询
	 * <li>创建人：张凡
	 * <li>创建时间：2012-10-26 下午02:10:29
	 * <li>修改人：谭诚
	 * <li>修改时间：2013-08-13
	 * <li>修改内容：暂时注释掉业务实现, 通过调用acquirePageList()来实现查询逻辑 
	 * <li>修改人：何涛
	 * <li>修改时间：2016-04-14
	 * <li>修改内容：由于该方法过于定制化，因此增加@Deprecated注解，以后不再建议使用该方法进行数据库查询
	 * @param <E>
	 * @param total_sql 查询总条数sql
	 * @param query_sql 查询数据sql
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @param searchJson 查询的json字符串
	 * @param orders 排序对象
	 * @throws BusinessException
	 * return: Page<E> 分页查询列表
	 */
	@SuppressWarnings("unchecked")
    @Deprecated
	public<E> Page<E> findPageList(final String total_sql,final String query_sql, int start, int limit, final String searchJson, final Order[] orders) throws BusinessException{
		final int beginIdx = start < 0 ? 0 : start;
		final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;	
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
		return (Page<E>)template.execute(new HibernateCallback(){
			public Page<E> doInHibernate(Session s) {
				StringBuffer sbWhere = new StringBuffer();				
				AbstractEntityPersister meta = (AbstractEntityPersister)getDaoUtils().getSessionFactory().getClassMetadata(entityClass);
				List<String> param = new ArrayList<String>();
				String field = null;    //字段名
				String operator = null; //操作符
				if(searchJson != null){
				    String[] jsonArray = searchJson.substring(1,searchJson.length()-1).split(",");
					//去掉searchJson两边的大括号再进行分割。
					//如果有查询条件
					if(StringUtils.isNotBlank(jsonArray[0])){
						String[] keyValue;//用于保存查询字段名与值						
						String fieldValue = null; //保存字段值
						//循环遍历拼接where语句
						for(int i = 0; i < jsonArray.length; i++){
							keyValue = jsonArray[i].split(":", 2);

							fieldValue = TransformerEntity.getValue(keyValue[1]);
							if(fieldValue.trim().equals("")){
							    continue;
							}
							
							if(sbWhere.length() != 0){//第一次不拼接And
							    sbWhere.append("and ");
							}
							//直接传递SQL字段(SF = SQL Field)值查询
							//支持格式：“{#[别名]#}$[SF]${$[LIKE]$}查询值” 大括号中的标记表示不是必须的，使用时没有大括号
							if(fieldValue.indexOf("$[SF]$") != -1){
							    if(fieldValue.indexOf("#[") < fieldValue.indexOf("]#")){
							        sbWhere.append(fieldValue.substring(fieldValue.indexOf("#[") + 2, fieldValue.indexOf("]#")));
							        sbWhere.append(".");
							        fieldValue = (fieldValue.charAt(0)=='\"'?"\"":"") + fieldValue.substring(fieldValue.indexOf("]#") + 2);
							    }
							    sbWhere.append(keyValue[0].substring(1,keyValue[0].lastIndexOf("\"")));
							    if(fieldValue.indexOf("$[LIKE]$") != -1){
							        sbWhere.append(" like ?");
							        param.add(fieldValue.substring(14));
							    }else{
							        sbWhere.append(" = ? ");
							        param.add(fieldValue.substring(6));
							    }
							    continue;
							}
							
							String[] propertys = meta.getPropertyNames();
							boolean isProperty = false;
							String entityField = keyValue[0].substring(1,keyValue[0].length()-1).trim();
							for(int j = 0; j < propertys.length; j ++){
							    if(propertys[j].equals(entityField)){
		                            isProperty = true;
		                            break;
		                        }
							}
							//为持久化字段
							if(isProperty){
							    /*
							     * *******取别名操作 
							     */
							    String alias = null;
								//要给查询字段取别名在值前面加#[你的别名]#
							    if(fieldValue.indexOf("#[") < fieldValue.indexOf("]#")){
							        alias =fieldValue.substring(fieldValue.indexOf("#[") + 2, fieldValue.indexOf("]#")); 
							        //取别名
							        sbWhere.append(" " + alias + ".");
							        //如果第一个字符为"，则加上它，后面好处理
							        fieldValue = (fieldValue.charAt(0)=='\"'?"\"":"") + fieldValue.substring(fieldValue.indexOf("]#") + 2);
							    }else if(query_sql.indexOf(" t ") != -1){
							        //主表取别名为t，给查询字段加别名
							        sbWhere.append(" t.");
							        alias = "t";
							    }
							    
							    field = meta.getPropertyColumnNames(entityField)[0]; //获取字段名,getPropertyColumnNames返回数组，取第0个元素获取数据库表字段名称。
							    
    							//日期格式特殊处理
    							if(meta.getPropertyType(entityField).getName().equals("timestamp")){
    							    
    							    TransformerEntity.appendDate(sbWhere, field, param, fieldValue, alias);
    							    
    							    field = null;
    							}else{
    							    
    							    //字段类型为字符串
    							    if(meta.getPropertyType(entityField).getName().equals("string")){
    							        if(fieldValue.indexOf("$[E]$") == 0){
    							            operator = "=";
    							            fieldValue = fieldValue.substring(5);
    						            }else{
    						                operator = "like";
    						            }
    							    }else{   //字段类型为整型之类的
    							        operator = "=";
    							    }    							    
    							}
							}else{//该字段为瞬时字段，用排序截取字段方法找到该字段
							    field = TransformerEntity.getOrderField(query_sql, entityField);
							    
							    if(fieldValue.indexOf("$[E]$") == 0){
                                    operator = "=";
                                    fieldValue = fieldValue.substring(5);
                                }else{
                                    operator = "like";
                                }
							}
							
							if(field != null){
							    
							    String checkRet = TransformerEntity.checkValueKeyWord(fieldValue); 
							    if(!"".equals(checkRet)){
							        sbWhere.append(field + checkRet); //关键字条件查询
							    }else{
							        /*
							         * 处理SQL拼接
							         */
							        TransformerEntity.appendSql(sbWhere, field, param, fieldValue, operator);
							    }
							}
						}
					}
				}
				String tableField = null;//表字段
				String entityField = null;//实体字体
				String sort = null;//排序顺序
				//处理排序字段获取
				if(orders != null) {
				    String order = orders[0].toString();
					entityField = order.substring(0,order.indexOf(" ")).trim();	
					String[] allPropertys = meta.getPropertyNames();
					boolean isProperty=false; //判断排序字段是否是持久字段
					for(int i = 0; i < allPropertys.length; i++){
					    if(allPropertys[i].equals(entityField)){
					        isProperty = true;
					        break;
					    }
					}
					if(isProperty){
					    //瞬时字段传进去会抛异常，因此需要判断
					    tableField = meta.getPropertyColumnNames(entityField)[0];
					}else{
					    tableField = entityField;
					}
					sort = order.substring(order.indexOf(" ")+1);
					
				}
				//将SQL与where条件进行拼装处理
				String q_sql = TransformerEntity.processSQLSplice(query_sql, sbWhere.toString(),entityField, tableField, sort);
				String t_sql = TransformerEntity.processSQLSplice(total_sql, sbWhere.toString());	
				Query query = s.createSQLQuery(t_sql);
				for(int i = 0; i < param.size(); i++){
				    String str = param.get(i);				    
				    query.setString(i, str);
				}
				query.setCacheable(false);
				//获取总记录数
				List totalList = query.list();
				int total = 0;
				if(totalList != null && totalList.size() > 0)	total = Integer.parseInt( totalList.get(0).toString() );
//				该语句导致执行两次查询总记录数，修改如上
//				int total = query.list().iterator().hasNext() ?Integer.parseInt(query.list().iterator().next().toString()) : 0;
				int begin = beginIdx > total ? total : beginIdx;
				//拼接分页查询SQL
				String pageSQL = "SELECT * FROM (SELECT a.*,rownum rowno FROM (" + q_sql + ") a WHERE rownum <= " + (beginIdx + pageSize)  + ") WHERE rowno >= " + (begin + 1);
				query = s.createSQLQuery(pageSQL).setResultTransformer(new TransformerEntity(entityClass,meta));
				query.setCacheable(false);
				for(int i = 0; i < param.size(); i++){
				    String str = param.get(i);				    
                    query.setString(i, str);
                }
				//进行对象封装查询
				List<E> e_list = query.list();		
				return new Page<E>(total, e_list);
			}
		});
//		return acquirePageList(total_sql, query_sql, start, limit, searchJson, orders, null, false);
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
	public Page<T> findPageList(final SearchEntity<T> searchEntity) throws BusinessException{
		final boolean enableCache = enableCache();
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
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
	 * <li>说明：基于单表实体类不分页查询,使用QBE不分页查询，返回实体类列表对象，
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 查询实体对象
	 * @return List<T> 实体类列表对象
	 * @throws BusinessException
	 */
	public List<T> findList(final T entity) throws BusinessException{
		return findList(entity, null);
	}
	/**
	 * <li>说明：不分页查询，返回实体类列表对象，基于单表实体类不分页查询
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-08-07
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 查询实体对象
	 * @param orders 排序规则数组
	 * @return List<T> 实体类列表对象
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<T> findList(final T entity, final Order[] orders) throws BusinessException{
		return findList(entity, orders, MatchMode.ANYWHERE);
    }
	/**
	 * <li>说明：不分页查询，返回实体类列表对象，基于单表实体类不分页查询
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-11-20
	 * <li>修改人：
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param entity 查询实体对象
	 * @param orders 排序规则数组
	 * @param likeMatchMode 字符串匹配模式
	 * @return List<T> 实体类列表对象
	 * @throws BusinessException
	 */	
	@SuppressWarnings("unchecked")
	public List<T> findList(final T entity, final Order[] orders, final MatchMode likeMatchMode) throws BusinessException{
		HibernateTemplate template = daoUtils.getHibernateTemplate();
		return (List<T>)template.execute(new HibernateCallback(){
			public List<T> doInHibernate(Session s){
				Criteria criteria = null;
				try {
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
						.excludeProperty(EntityUtil.UPDATE_TIME);
					//如果字符串匹配模式对象不为空，则设置
					if(likeMatchMode != null)	exp.enableLike().enableLike(likeMatchMode);
					//分页列表
					criteria = s.createCriteria(entity.getClass()).add(exp);
					if(orders != null){
						for (Order order : orders) {
							criteria.addOrder(order);
						}						
					}
					//如果该实体类存在修改时间字段，则追加按修改时间倒序
					if(EntityUtil.contains(entity.getClass(), EntityUtil.UPDATE_TIME)){
						criteria.addOrder(Order.desc(EntityUtil.UPDATE_TIME));
					}
					criteria.setCacheable(enableCache());
					return criteria.list();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if(criteria != null)	criteria.setCacheable(false);
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
	public Page<T> findPageList(final QueryCriteria<T> query) throws BusinessException{
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
		return (Page<T>)template.execute(new HibernateCallback(){
			public Page<T> doInHibernate(Session s){
				try {
					return query.getPage(s, enableCache());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
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
	public List<T> findList(final QueryCriteria<T> query) throws BusinessException{
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
		return (List<T>)template.execute(new HibernateCallback(){
			public List<T> doInHibernate(Session s){
				try {
					return query.getList(s, enableCache());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
    }
	
    /**
     * <li>说明：删除一组实体对象，该方法只适用于检修系统v2.0的数据模型实体类，统一设置业务无关的五个字段（创建人、
     * 创建时间、修改人、修改时间、站点标识）
     * <li>创建人：王治龙
     * <li>创建日期：2012-12-25
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param List<T> 实体类对象集合 
     * @return void
     * @throws BusinessException, NoSuchFieldException
     */ 
    public void logicDelete(List<T> entityList) throws BusinessException, NoSuchFieldException {
    	if(entityList == null || entityList.size() < 1)	return;
        for (T t : entityList) {
            t = EntityUtil.setSysinfo(t);
//          设置逻辑删除字段状态为已删除      
            t = EntityUtil.setDeleted(t);
        }
        this.daoUtils.getHibernateTemplate().saveOrUpdateAll(entityList);
    }
    
    /**
     * 
     * <li>说明：执行带out参数的存储过程
     * <li>创建人：张凡
     * <li>创建日期：2013-2-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 参数名：参数说明
     * <li>procName:存储过程名称
     * <li>types 装类似java.sql.Types.VARCHAR的int数组
     * <li>objects Object[]存储过程in参数
     * @return Object[] out参数
     * @throws 抛出异常列表
     */
    public Object[] executeProc(String procName, int[] types, Object...objects) throws DataAccessResourceFailureException, HibernateException, IllegalStateException, SQLException{
        StringBuffer procSql = new StringBuffer("{call " + procName +"( ");
        for(int i = 0; i < objects.length + types.length; i++){
            procSql.append("?,");
        }
        procSql.deleteCharAt(procSql.length()-1);
        procSql.append(")}");
        SessionFactory factory = daoUtils.getSessionFactory();
        ConnectionProvider cp = ((SessionFactoryImplementor)factory).getConnectionProvider();
        Connection conn = cp.getConnection();
        CallableStatement cs = conn.prepareCall(procSql.toString());
        
        int ix = objects.length;
        for(int i = 1; i <= objects.length; i++){            
            cs.setObject(i, objects[i-1]);
        }
        for(int i = 0; i < types.length; i++){
            cs.registerOutParameter(ix + i+1, types[i]);
        }        
        cs.execute();
        Object[] out = new Object[2];
        for(int i = 0; i < types.length; i++){
            out[i] = cs.getObject(ix + i +1);
        }        
        cs.close();
        conn.close();
        return out;
    }
    
    /**
     * <li>方法名称：BPSLogin
     * <li>方法说明：BPS设置前当登陆人 
     * <li>@param userId
     * <li>@param userName
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-3-20 上午11:44:36
     * <li>修改人：
     * <li>修改内容：
     */
    protected void BPSLogin(String userId, String userName){
        BPSServiceClientFactory.getLoginManager().setCurrentUser(userId, userName);
    }
    /**
     * <li>方法名称：BPSLogin
     * <li>方法说明：BPS设置当前登陆人 
     * <li>
     * <li>return: void
     * <li>创建人：张凡
     * <li>创建时间：2013-3-20 上午11:44:48
     * <li>修改人：
     * <li>修改内容：
     */
    protected void BPSLogin(){
        AcOperator ac = SystemContext.getAcOperator();
        BPSServiceClientFactory.getLoginManager().setCurrentUser(ac.getUserid(), ac.getOperatorname());
    }
    
    /**
     * <li>方法名称：getBPSService
     * <li>方法说明：获取IBPSServiceClient ,使用前请先调用BPSLogin
     * <li>@return
     * <li>return: IBPSServiceClient
     * <li>创建人：张凡
     * <li>创建时间：2013-3-20 上午11:46:42
     * <li>修改人：
     * <li>修改内容：
     */
    protected IBPSServiceClient  getBPSService(){
         return BPSServiceClientUtil.getServiceClient();
    }
    
    /**
     * 不推荐使用，建议实现接口IbaseCombo
     * <li>方法说明：公共查询Base_combo方法，子类重写该方法返回查询数据
     * <li>方法名称：getBaseComboData
     * <li>@param HttpServletRequest
     * <li>@param start
     * <li>@param limt
     * <li>@return
     * <li>return: Map<String,Object>
     * <li>创建人：张凡
     * <li>创建时间：2013-7-12 下午07:37:56
     * <li>修改人：
     * <li>修改内容：
     */
    @Deprecated
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limt) throws Exception{
        return null;
    }
	/**
	 * <li>说明：分页查询，从缓存中查找并返回实体类的分页列表对象，基于单表实体类分页查询，使用该方法应确认hibernate及实体映射对象都开启二级缓存，否则可能产生查询n+1性能问题。
	 * 该方法与public<T> Page<T> findPageList(String totalHql, String hql, int start, int limit)功能相同，区别只在于使用查询缓存
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-8-30
	 * <li>修改人： 谭诚
	 * <li>修改日期： 2013-8-13
	 * <li>修改内容： 将方法体的实现移除, 交由acquirePageList()实现功能
	 * @param totalHql 查询总记录数的hql语句
	 * @param hql 查询语句
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @param isQueryCacheEnabled 是否启用查询缓存
	 * @return Page<T> 分页查询列表
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Page<T> cachePageList(String totalHql, String hql, int start, int limit) throws BusinessException{
		return acquirePageList(totalHql, hql, start, limit, true);
    }
	
	/**
	 * <li>说明：数据的分页查询, 由本类的findPageList()或cachePageList()内部调用, 由布尔值参数isQueryCacheEnabled决定,为true时启用查询缓存,为false时禁用查询缓存
	 * <li>创建人：谭诚
	 * <li>创建日期：2012-8-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param totalHql 查询总记录数的hql语句
	 * @param hql 查询语句
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @param isQueryCacheEnabled 是否启用查询缓存
	 * @return Page<T> 分页查询列表
	 * @throws BusinessException
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private Page<T> acquirePageList(String totalHql, String hql, int start, int limit, Boolean isQueryCacheEnabled) throws BusinessException{
		final int beginIdx = start < 0 ? 0 : start;
		final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;		
		final String total_hql = totalHql;
		final String fHql = hql;
		final Boolean useCached = isQueryCacheEnabled;
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
		return (Page<T>)template.execute(new HibernateCallback(){
			public Page<T> doInHibernate(Session s) {
				Query q = null;
				try {
					q = s.createQuery(total_hql);
					q.setCacheable(useCached); //缓存开关
					int total = ((Long)q.uniqueResult()).intValue();
					q.setCacheable(false);
					int begin = beginIdx > total ? total : beginIdx;
					q = s.createQuery(fHql).setFirstResult(begin).setMaxResults(pageSize);
					q.setCacheable(useCached); //缓存开关
					return new Page<T>(total, q.list());
				} catch (HibernateException e) {
					throw e;
				} finally{
					if(q != null)	q.setCacheable(false);
				}
			}
		});
    }
	
    /**
     * <li>修改人：何涛
     * <li>修改日期：2016-2-4
     * <li>修改内容：不推荐使用该方法，因为该方法初衷初衷是想通过传人class对象，使用sql查询自定义的封装实体，当由于实现时方法返回的是manage管理的泛型T，所以并不能实现设计初衷
     * <li>若要使用该方法，推荐使用queryPageList(totalSql, sql, start, limit, isQueryCacheEnabled)方法
     * @see queryPageList
     * @param totalSql 查询记录总数的sql
     * @param sql 查询sql
     * @param start 查询开始索引
     * @param limit 查询结束索引
     * @param isQueryCacheEnabled 是否使用查询缓存，若为null，则默认不使用查询缓存
     * @param t 自定义封装实体class的对象
     * @return 自定义封装实体的分页列表
     * @throws BusinessException
     */
	@SuppressWarnings("unchecked")
    @Deprecated
    public Page<T> acquirePageList(String totalSql, String sql, int start, int limit, Boolean isQueryCacheEnabled, Class t) throws BusinessException{
        return queryPageList(totalSql, sql, start, limit, isQueryCacheEnabled, entityClass);
	}
    
    /**
     * <li>说明：使用SQL进行分页查询
     * <li>创建人：何涛
     * <li>创建日期：2016-2-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param totalSql 查询记录总数的sql
     * @param sql 查询sql
     * @param start 查询开始索引
     * @param limit 查询结束索引
     * @param isQueryCacheEnabled 是否使用查询缓存，若为null，则默认不使用查询缓存
     * @return 自定义封装实体的分页列表
     * @throws BusinessException
     */
    public Page<T> queryPageList(String totalSql, String sql, int start, int limit, Boolean isQueryCacheEnabled) throws BusinessException{
        return this.queryPageList(totalSql, sql, start, limit, isQueryCacheEnabled, entityClass);
    }
	
	/**
     * <li>说明：使用SQL进行分页查询，返回自定义查询封装实体集合
     * <li>创建人：何涛
     * <li>创建日期：2016-2-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param <E> 方法泛型
     * @param totalSql 查询记录总数的sql
     * @param sql 查询sql
     * @param start 查询开始索引
     * @param limit 查询结束索引
     * @param isQueryCacheEnabled 是否使用查询缓存，若为null，则默认不使用查询缓存
     * @param t 自定义封装实体class的对象
     * @return 自定义封装实体的分页列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public <E> Page<E> queryPageList(String totalSql, String sql, int start, int limit, Boolean isQueryCacheEnabled, Class<E> t) throws BusinessException{
        final int beginIdx = start < 0 ? 0 : start;
        final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;
        final String total_sql = totalSql;
        final String fSql = sql;
        final Boolean useCached = isQueryCacheEnabled;
        final Class<E> clazz = t;
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<E>)template.execute(new HibernateCallback(){
            public Page<E> doInHibernate(Session s) {
                SQLQuery query = null;
                try {
                    query = s.createSQLQuery(total_sql);
                    query.addScalar("rowcount", Hibernate.INTEGER);
                    query.setCacheable(useCached); //缓存开关
                    int total = ((Integer)query.uniqueResult()).intValue();
                    query.setCacheable(false);
                    int begin = beginIdx > total ? total : beginIdx;
                    query = (SQLQuery)s.createSQLQuery(fSql).addEntity(clazz).setFirstResult(begin).setMaxResults(pageSize);
                    query.setCacheable(useCached); //缓存开关
                    return new Page<E>(total, query.list());
                } catch (HibernateException e) {
                    throw e;
                } finally{
                    if(query != null)   query.setCacheable(false);
                }
            }
        });
    }
    
    /**
     * <li>说明：分页直接返回sql对应的map对象，key为查询字段别名
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param <E>
     * @param totalSql 查询记录总数的sql
     * @param sql 查询sql
     * @param start 查询开始索引
     * @param limit 查询结束索引
     * @param isQueryCacheEnabled 是否使用查询缓存，若为null，则默认不使用查询缓存
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public <E> Page<E> queryPageMap(String totalSql, String sql, int start, int limit, Boolean isQueryCacheEnabled) throws BusinessException{
        final int beginIdx = start < 0 ? 0 : start;
        final int pageSize = limit < 0 ? Page.PAGE_SIZE : limit;
        final String total_sql = totalSql;
        final String fSql = sql;
        final Boolean useCached = isQueryCacheEnabled;
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (Page<E>)template.execute(new HibernateCallback(){
            public Page<E> doInHibernate(Session s) {
                SQLQuery query = null;
                try {
                    query = s.createSQLQuery(total_sql);
                    query.addScalar("rowcount", Hibernate.INTEGER);
                    query.setCacheable(useCached); //缓存开关
                    int total = ((Integer)query.uniqueResult()).intValue();
                    query.setCacheable(false);
                    int begin = beginIdx > total ? total : beginIdx;
                    query = (SQLQuery)s.createSQLQuery(fSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(begin).setMaxResults(pageSize);
                    query.setCacheable(useCached); //缓存开关
                    return new Page<E>(total, query.list());
                } catch (HibernateException e) {
                    throw e;
                } finally{
                    if(query != null)   query.setCacheable(false);
                }
            }
        });
    }
    
    /**
     * <li>说明：通过sql返回map类型的List
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param sql 查询sql
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryListMap(String sql) throws BusinessException{
        final String fSql = sql;
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (List<Map<String, Object>>)template.execute(new HibernateCallback(){
            public List<Map<String, Object>> doInHibernate(Session s) {
                SQLQuery query = null;
                try {
                    query = (SQLQuery)s.createSQLQuery(fSql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                    query.setCacheable(false);
                    return query.list();
                } catch (HibernateException e) {
                    throw e;
                } finally{
                    if(query != null)   query.setCacheable(false);
                }
            }
        });
    }


    /**
     * 不推荐使用该方法，若要使用该方法，推荐使用queryPageList(totalSql, sql, start, limit, isQueryCacheEnabled)方法
     * @see queryPageList
	 * <li>该方法与public<T> Page<T> findPageList(final String total_sql,final String query_sql, int start, int limit, final String searchJson, final Order[] orders)功能相同，区别只在于使用查询缓存
	 * <li>方法说明：分页查询，返回实体类的分页对表对象，基于SQL查询
	 * @param <T>
	 * @param total_sql 查询总条数sql
	 * @param query_sql 查询数据sql
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @param searchJson 查询的json字符串
	 * @param orders 排序对象
	 * @param colType sql查询语句各字段的Hibernate数据类型
	 * @return
	 * @throws BusinessException
	 * return: Page<T> 分页查询列表
	 * <li>创建人：张凡
	 * <li>创建时间：2012-10-26 下午02:10:29
	 * <li>修改人：
	 * <li>修改内容：
	 */
	@SuppressWarnings("unchecked")
    @Deprecated
	public Page<T> cachePageList(final String total_sql,final String query_sql, int start, int limit,Class t) throws BusinessException{
		return acquirePageList(total_sql, query_sql, start, limit, true, t);
	}
	
	/**
	 * 
	 * <li>说明：QBE查询,非分页方式
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param t 查询实体
	 * @param columnName 不需要参与匹配的字段
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 实体对象集合
	 * 调用示例:
	 * OmEmployee emp = new OmEmployee();
	 * emp.setempcode("4");
	 * Order order = Order.asc("empid");
	 * String [] columnName = new String[]{"empid"}; 
	 * Order [] or = new Order[1];
	 * or[0] = order;
	 * list = omEmployeeManager.findByEntity(emp, columnName, or, true);
	 */
	@SuppressWarnings("unchecked")
	public List <T> findByEntity(final T t, final String [] columnName, final Order [] orders, final Boolean isExact){
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
		return (List<T>)template.execute(new HibernateCallback(){
			public List<OmEmployee> doInHibernate(Session s){
				Criteria criteria = null;
				try {
					T entity = t;
					//过滤掉empid的查询条件
					Example exp = Example.create(entity);
					if(columnName!=null&&columnName.length>0){
						for(String col : columnName){
							exp.excludeProperty(col); //不需要参与匹配的字段
						}
					}
					if(!isExact){ //如果不是精确匹配
						exp.enableLike().enableLike(MatchMode.ANYWHERE);
					}
					//分页列表
					criteria = s.createCriteria(entity.getClass()).add(exp);
					//设置排序规则
					if(orders != null){
						for (Order order : orders) {
							criteria.addOrder(order);
						}					
					}
					criteria.setCacheable(isJcglQueryCacheEnabled());  //是否利用查询缓存
					return criteria.list();
				} catch (HibernateException e) {
					throw e;
				} finally {
					if(criteria != null)	criteria.setCacheable(false);
				}
			}
		});
	}
	
	/**
	 * 
	 * <li>说明：QBE查询,支持分页方式
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-17
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 查询实体
	 * @param columnName 不需要参与匹配的字段
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 组织机构分页对象
	 * 调用示例:
	 *  SearchEntity <OmOrganization> entity = new SearchEntity<OmOrganization>();
	 *  entity.setEntity(org);
	 *	entity.setStart(start);
	 *	entity.setLimit(limit);
	 *  String [] excludeColumn = new String[]{"orgid"};
	 *	page = omOrganizationManager.findByEntity(entity,excludeColumn,true);
	 */
	@SuppressWarnings("unchecked")
	public Page <T> findByEntity(final SearchEntity <T> searchEntity, final String [] columnName, final Boolean isExact){
		HibernateTemplate template = this.daoUtils.getHibernateTemplate();
		return (Page<T>)template.execute(new HibernateCallback(){
			public Page<T> doInHibernate(Session s){
				Criteria criteria = null;
				try {
					T entity = searchEntity.getEntity();
					//过滤掉orgid的查询条件
					Example exp = Example.create(entity);
					if(columnName!=null&&columnName.length>0){
						for(String col : columnName){
							exp.excludeProperty(col); //不需要参与匹配的字段
						}
					}
					if(!isExact){ //如果不是精确匹配
						exp.enableLike().enableLike(MatchMode.ANYWHERE);
					}
					//查询总记录数
					criteria = s.createCriteria(entity.getClass());
					criteria.setCacheable(isJcglQueryCacheEnabled());//是否利用查询缓存
					int total = ((Integer)criteria.add(exp)
						.setProjection(Projections.rowCount())
						.uniqueResult())
						.intValue();
					criteria.setCacheable(false);
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
					criteria.setCacheable(isJcglQueryCacheEnabled());//是否利用查询缓存
					return new Page<T>(total, criteria.list());
				} catch (HibernateException e) {
					throw e;
				} finally {
					if(criteria != null)	criteria.setCacheable(false);
				}
			}
		});
	}
	/**
	 * 不推荐使用，建议实现接口IbaseComboTree
	 * <li>说明：公共查询Base_comboTree方法，子类重写该方法返回查询数据
	 * <li>创建人：程锐
	 * <li>创建日期：2014-1-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param req 
	 * @return List<HashMap> 前台树所需数据列表
	 * @throws Exception
	 */
	@Deprecated
	public List<HashMap> getBaseComboTree(HttpServletRequest req) throws Exception{
        return null;
    }
    
    /**
     * <li>说明：使用SQL进行查询，返回自定义查询封装实体集合
     * <li>创建人：林欢
     * <li>创建日期：2016-7-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param <E> 方法泛型
     * @param totalSql 查询记录总数的sql
     * @param sql 查询sql
     * @param start 查询开始索引
     * @param limit 查询结束索引
     * @param isQueryCacheEnabled 是否使用查询缓存，若为null，则默认不使用查询缓存
     * @param t 自定义封装实体class的对象
     * @return 自定义封装实体的分页列表
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    public <E> List<E> queryList(String sql,Boolean isQueryCacheEnabled, Class<E> t) throws BusinessException{
        final String fSql = sql;
        final Boolean useCached = isQueryCacheEnabled;
        final Class<E> clazz = t;
        HibernateTemplate template = this.daoUtils.getHibernateTemplate();
        return (List<E>)template.execute(new HibernateCallback(){
            public List<E> doInHibernate(Session s) {
                SQLQuery query = null;
                try {
                    query.setCacheable(false);
                    query = (SQLQuery)s.createSQLQuery(fSql).addEntity(clazz);
                    query.setCacheable(useCached); //缓存开关
                    return query.list();
                } catch (HibernateException e) {
                    throw e;
                } finally{
                    if(query != null)   query.setCacheable(false);
                }
            }
        });
    }
    
    /**
	 * <li>说明：使用sql进行多表联合查询，查询结果集封装到自定义实体对象时的Orders排序处理
	 * <li>创建人：何涛
	 * <li>创建日期：2017年2月16日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param searchEntity 查询添加封装实体对象，注意：该对象的entity属性不能为null
	 * @param sb 正在拼接的自定义查询sql语句
	 */
	protected void processOrdersInSql(SearchEntity<?> searchEntity, StringBuilder sb) {
		if (null == searchEntity || null == searchEntity.getEntity()) {
			return;
		}
		// hibernate排序实体对象
		Order[] orders = searchEntity.getOrders();
		if (null == orders || 0 >= orders.length) {
			return;
		}

		Field field0, field1, field = null;
		boolean ascending = false;
		String propertyName = null;
		try {
			field0 = Order.class.getDeclaredField("ascending");
			field0.setAccessible(true);
			field1 = Order.class.getDeclaredField("propertyName");
			field1.setAccessible(true);
			for (int i = 0; i < orders.length; i++) {
				ascending = field0.getBoolean(orders[i]);
				propertyName = field1.get(orders[i]).toString();
				field = searchEntity.getEntity().getClass().getDeclaredField(propertyName);
				// Modified by hetao on 2017-03-03 不处理声明了IgnoreOrder注解的字段排序
				IgnoreOrder ignoreOrder = field.getAnnotation(IgnoreOrder.class);
				if (null != ignoreOrder) {
					continue;
				}
				if (-1 == sb.indexOf(" ORDER BY ")) {
					sb.append(" ORDER BY ");
				}
				Column annotation = field.getAnnotation(Column.class);
				if (null != annotation) {
					String name = annotation.name();
					if (null != name) {
						sb.append(name);
					}
				} else {
					sb.append(propertyName);
				}
				sb.append(ascending ? " ASC" : " DESC");
				if (i < orders.length - 1) {
					sb.append(",");
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
}