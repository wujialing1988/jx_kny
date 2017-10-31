package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmDuty;
/**
 * 
 * <li>类型名称：
 * <li>说明：
 * <li>创建人： 田华
 * <li>创建日期：2011-4-1
 * <li>修改人： 
 * <li>修改日期：
 */
public class OmDutyManager  extends JXBaseManager<OmDuty, OmDuty> implements IOmDutyManager{

	@Override
	protected void checkDelete(Serializable id) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	/*
	 *     业务数据统一接口方法
	 */
	
	/**
	 * <br/><li>说明： 递归查询入参职务dutyId下属所有子职务
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyId 职务Id
	 * @param hasSelf 是否包含参数dutyId本身
	 * @return 职务实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmDuty> findAllChilds(Long dutyId, boolean hasSelf) {
		if(dutyId == null) throw new RuntimeException("参数异常: 入参dutyId为空");
		String sql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findAllChilds"));
		if(StringUtil.isNullOrBlank(sql)) throw new RuntimeException("SQL语句读取异常");
		if(hasSelf) sql = sql.replaceFirst("[?]", "-1").replaceFirst("[?]", String.valueOf(dutyId)); //不包含参数dutyId节点
		else sql = sql.replaceAll("[?]", String.valueOf(dutyId));  //包含参数dutyId节点
		List <OmDuty> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.cacheSqlQueryEntity(sql, OmDuty.class);  //使用缓存
		} else {
			list = daoUtils.executeSqlQueryEntity(sql,OmDuty.class);  //不使用缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明： 递归查询入参职务dutyId下属所有子职务,并提供分页支持
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyId 职务Id
	 * @param hasSelf 是否包含参数dutyId本身
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @return 职务实体列表
	 */
	public Page findAllChilds(Long dutyId, boolean hasSelf, Integer start, Integer limit) {
		if(dutyId == null) throw new RuntimeException("参数异常: 入参dutyId为空");
		String sql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findAllChilds"));
		if(StringUtil.isNullOrBlank(sql)) throw new RuntimeException("SQL语句读取异常");
		if(hasSelf) sql = sql.replaceFirst("[?]", "-1").replaceFirst("[?]", String.valueOf(dutyId)); //不包含参数dutyId节点
		else sql = sql.replaceAll("[?]", String.valueOf(dutyId));  //包含参数dutyId节点
		String totalSql = "select count(*) as \"rowcount\" from (".concat(sql).concat(")");
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalSql, sql, start, limit, OmDuty.class); //使用缓存
		} else {
			return super.findPageList(totalSql, sql, start, limit, null, null);  //不使用缓存
		}
	}

	/**
	 * <br/><li>说明： 根据职务的编号精确查找与其对应的唯一职务
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyCode 职务编号
	 * @return 职务实体
	 */
	@SuppressWarnings("unchecked")
	public OmDuty findByCode(String dutyCode) {
		if(StringUtil.isNullOrBlank(dutyCode)) throw new RuntimeException("参数异常: 入参dutyCode为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "dutycode").replaceFirst("[?]", dutyCode);
		List<OmDuty> list = null;
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
	 * <br/><li>说明： 根据职务编号进行模糊查询(like '%...%'方式),并提供分页支持
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyCode 职务编号
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 带分页的职务实体列表
	 */
	public Page findByCode(String dutyCode, Integer start, Integer limit) {
		if(StringUtil.isNullOrBlank(dutyCode)) throw new RuntimeException("参数异常: 入参dutyCode为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "dutycode").replaceFirst("[?]", dutyCode);
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit);  //利用查询缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}
	}

	/**
	 * <br/><li>说明：获得人员Id入参直属的职务
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param empId 人员id
	 * @return 职务实体
	 */
	@SuppressWarnings("unchecked")
	public OmDuty findByEmpId(Long empId) {
		if(empId == null) throw new RuntimeException("参数异常: 入参empId为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findByEmpId"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		Object[] param = new Object[]{empId};
		List <OmDuty> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		if (list != null && list.size() > 0)	return (OmDuty) list.get(0);
		else return null;
	}

	/**
	 * 
	 * <br/><li>说明：职务的QBE查询,非分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param duty 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 职务对象集合
	 * <br/>调用示例:
	 * <br/>OmDuty org = new OmDuty();
	 * <br/>org.setdutycode("4");
	 * <br/>Order order = Order.asc("dutyId");
	 * <br/>Order [] or = new Order[1];
	 * <br/>or[0] = order;
	 * <br/>list = omDutyManager.findByEntity(duty, or);
	 */
	public List<OmDuty> findByEntity(OmDuty duty, Order[] orders, Boolean isExact) {
		String [] columnName = new String[]{"dutyid","isleaf","subcount","remark"};
		return super.findByEntity(duty,columnName,orders,isExact);
	}

	/**
	 * 
	 * <br/><li>说明：职务的QBE查询,支持分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 职务分页对象
	 * <br/>调用示例:
	 * <br/>SearchEntity <OmDuty> entity = new SearchEntity<OmDuty>();
	 * <br/>entity.setEntity(org);
	 * <br/>entity.setStart(start);
	 * <br/>entity.setLimit(limit);
	 * <br/>page = omDutyManager.findByEntity(entity);
	 */
	public Page<OmDuty> findByEntity(SearchEntity<OmDuty> searchEntity, Boolean isExact) {
		String [] columnName = new String[]{"dutyid","isleaf","subcount","remark"};
		return super.findByEntity(searchEntity, columnName, isExact);
	}

	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配, 支持的数据类型有NUMBER,VARCHAR2,CHAR,INTEGER</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
     * @param field 字段名
     * @param value 字段值
     * @return 职务实体列表
     */
	@SuppressWarnings("unchecked")
	public List<OmDuty> findByField(String field, String value) {
		if(field == null || value == null) throw new RuntimeException("参数异常: 入参field或者value为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", field).replaceFirst("[?]", String.valueOf(value));
		List <OmDuty> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql);  //利用查询缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明：根据职务Id数组,查询匹配的职务信息列表
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param ids 职务Id数组
	 * @return 职务实体列表
	 */
	public List<OmDuty> findByIdArys(Serializable... ids) {
		if(ids == null) throw new RuntimeException("参数异常: 入参ids为空");
		List<OmDuty> list = new ArrayList<OmDuty>();
		for (Serializable dutyId : ids) {
			list.add(this.getModelById(Long.parseLong(dutyId.toString())));
		}
		return list;
	}

	/**
	 * <br/><li>说明：根据多个职务Id,查询与对应的职务
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个dutyId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyIds 职务id字符串
	 * @return 职务实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmDuty> findByIds(String dutyIds) {
		if(dutyIds == null) throw new RuntimeException("参数异常: 入参dutyIds为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findByIds"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replace("?", dutyIds);
		List <OmDuty> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql);  //利用查询缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明：根据多个职务Id,查询与对应的职务
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个dutyId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyIds 职务id字符串
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 职务实体列表
	 */
	public Page findByIds(String dutyIds, Integer start, Integer limit) {
		if(dutyIds == null) throw new RuntimeException("参数异常: 入参dutyIds为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findByIds"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replace("?", dutyIds);
		@SuppressWarnings("unused")
		Page <OmDuty> page = null;
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit);  //利用查询缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}
	}

	/**
	 * <br/><li>说明：获得操作员Id入参直属的职务
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param operatorid 操作员id
	 * @return 职务实体
	 */
	@SuppressWarnings("unchecked")
	public OmDuty findByOperator(Long operatorid) {
		if(operatorid == null) throw new RuntimeException("参数异常: 入参operatorid为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findByOperator"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		Object[] param = new Object[]{operatorid};
		List <OmDuty> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		if (list != null && list.size() > 0)	return (OmDuty) list.get(0);
		else return null;
	}

	/**
	 * <br/><li>说明： 根据dutyseq查询唯一对应的职务. dutyseq的格式示例: ".0.7.146"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyseq 职务Id序列
	 * @return 职务实体
	 */
	@SuppressWarnings("unchecked")
	public OmDuty findBySeq(String dutyseq) {
		if(StringUtil.isNullOrBlank(dutyseq)) throw new RuntimeException("参数异常: 入参dutyseq为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "dutyseq").replaceFirst("[?]", dutyseq);
		List <OmDuty> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不使用缓存
		}
		if (list != null && list.size() > 0)	return (OmDuty) list.get(0);
		else return null;
	}

	/**
	 * <br/><li>说明： 查找入参dutyId的下一级职务信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyId 职务Id
	 * @return 职务实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmDuty> findChildsById(Long dutyId) {
		if(dutyId == null) throw new RuntimeException("参数异常: 入参dutyId为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "parentduty").replaceFirst("[?]", String.valueOf(dutyId));
		List <OmDuty> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不使用缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明： 以分页方式查找入参dutyId的下一级职务信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyId 职务Id
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 带分页的职务实体列表
	 */
	public Page findChildsById(Long dutyId, Integer start, Integer limit) {
		if(dutyId == null) throw new RuntimeException("参数异常: 入参dutyId为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "parentduty").replaceFirst("[?]", String.valueOf(dutyId));
		@SuppressWarnings("unused")
		Page <OmDuty> page = null;
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit);  //利用查询缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}
	}

	/**
	 * <br/><li>说明：根据入参dutyId,查询其上级职务
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyId 职务id
	 * @return 职务实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmDuty> findUpperByDegree(Long dutyId) {
		if(dutyId == null) throw new RuntimeException("参数异常: 入参dutyId为空");
		String sql = SqlMapUtil.getSql(XMLNAME_DUTY.concat("findUpperByDegree"));
		if(StringUtil.isNullOrBlank(sql)) throw new RuntimeException("SQL语句读取异常");
		sql = sql.replaceAll("[?]", String.valueOf(dutyId));
		List <OmDuty> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.cacheSqlQueryEntity(sql, OmDuty.class);  //使用缓存
		} else {
			list = daoUtils.executeSqlQueryEntity(sql,OmDuty.class);  //不使用缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明： 根据职务Id查询唯一对应的职务信息.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-19
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyId 职务id
	 * @return 职务实体
	 */
	public OmDuty getModelById(Long dutyId) {
		return (OmDuty)daoUtils.getHibernateTemplate().get(OmDuty.class, dutyId);
	}
}
