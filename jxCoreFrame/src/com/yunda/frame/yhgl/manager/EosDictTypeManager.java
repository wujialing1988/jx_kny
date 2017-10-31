/**
 * <li>文件名：EosDictTypeManager.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-4-7
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.frame.yhgl.manager;

import java.util.List;

import org.hibernate.criterion.Order;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.EosDictType;

/**
 * <li>类型名称：
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-4-7
 * <li>修改人：
 * <li>修改日期：
 */
public class EosDictTypeManager extends JXBaseManager<EosDictType, EosDictType> implements IEosDictTypeManager{
	/*
	 * 统一业务接口
	 */
	
	/**
	 * 
	 * <br/><li>说明：业务字典分类的QBE查询,支持分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 业务字典分类分页对象
	 * <br/>调用示例:
	 *  <br/>SearchEntity <EosDictType> entity = new SearchEntity<EosDictType>();
	 *  <br/>EosDictType dictType = new EosDictType();
	 *  <br/>dict.setDicttypeid("JXGC_WORKSEQ_WORKCLASS");
	 *  <br/>entity.setEntity(dictType);
	 *	<br/>entity.setStart(start);
	 *	<br/>entity.setLimit(limit);
	 *	<br/>page = eosDictTypeManager.findByEntity(entity,true);
	 */
	public Page <EosDictType> findByEntity(SearchEntity <EosDictType> searchEntity, Boolean isExact){
		String [] columnName = new String[]{"rank"};
		return super.findByEntity(searchEntity, columnName, isExact);
	}
	
	/**
	 * 
	 * <br/><li>说明：业务字典分类的QBE查询,非分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dictType 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 业务字典分类对象集合
	 * <br/>调用示例:
	 *  <br/>EosDictType dictType = new EosDictType();
	 *	<br/>dictType.setDicttypename("4");
	 *	<br/>Order order = Order.asc("dicttypeid");
	 *	<br/>Order [] or = new Order[1];
	 *	<br/>or[0] = order;
	 *  <br/>list = omOrganizationManager.findByEntity(dictType, or,true);
	 */
	public List <EosDictType> findByEntity(EosDictType dictType, Order [] orders, Boolean isExact){
		String [] columnName = new String[]{"rank"};
		return super.findByEntity(dictType,columnName,orders,isExact);
	}
	
	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配, 支持的数据类型有NUMBER,VARCHAR2,CHAR,INTEGER</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-8-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
     * @param field 字段名
     * @param value 字段值
     * @return 业务字典分类实体列表
     */
	@SuppressWarnings("unchecked")
	public List<EosDictType> findByField(String field, String value){
		if(field == null || value == null) throw new RuntimeException("参数异常: 入参field或者value为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DICT.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", field).replaceFirst("[?]", String.valueOf(value));
		List <EosDictType> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql);  //利用查询缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
		return list;
	}
	
	/**
	 * <br/><li>说明： 根据业务字典分类Id查询唯一对应的业务字典分类.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dicttypeid 业务字典分类id
	 * @return 业务字典分类实体
	 */
	public EosDictType getModelById(String dicttypeid){
		return (EosDictType)daoUtils.getHibernateTemplate().get(EosDictType.class, dicttypeid);
	}
	
	/**
	 * <br/><li>说明： 根据业务字典分类的名称精确查找与其对应的唯一业务字典分类
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dicttypename 业务字典分类名称
	 * @return 业务字典分类实体
	 */
	@SuppressWarnings("unchecked")
	public EosDictType findByName(String dicttypename){
		if(StringUtil.isNullOrBlank(dicttypename)) throw new RuntimeException("参数异常: 入参dicttypename为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DICT.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "dicttypename").replaceFirst("[?]", dicttypename);
		List<EosDictType> list = null;
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
	 * <br/><li>说明：根据多个业务字典分类的Id,查询与对应的业务字典分类
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个dicttypeid, 参数格式如: JCJX_LXSZ,JXGC_WORKSEQ_WORKCLASS,JCZL_PJSHSZ,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-27
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dicttypeid 业务字典分类id字符串
	 * @return 业务字典分类实体列表
	 */
	@SuppressWarnings("unchecked")
	public List <EosDictType> findByIds(String dicttypeid){
		if(StringUtil.isNullOrBlank(dicttypeid)) throw new RuntimeException("参数异常: 入参dicttypeid为空");
		String [] _t = dicttypeid.split(",");
		StringBuffer buff = new StringBuffer();
		if(_t!=null&&_t.length>0){
			for(String _s : _t){
				buff.append("'").append(_s).append("',");
			}
		}
		String _dicttypeid = buff.toString().substring(0,buff.toString().length()-1);
		String hql = SqlMapUtil.getSql(XMLNAME_DICT.concat("findByIds")).replace("?", _dicttypeid);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.find(true, hql); //利用查询缓存
		} else {
			return daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
	}
	
	/**
	 * <br/><li>说明：获取业务字典的根层节点
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-12-11
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @return 业务字典分类实体列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public List <EosDictType> findRoots(){
		String hql = "from EosDictType where parentid = '0' or parentid is null";
		List<EosDictType> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql); //使用缓存查询
		} else {
			list = daoUtils.getHibernateTemplate().find(hql); //不使用缓存查询
		}
		return list;
	}
	
	/**
	 * <br/><li>说明：根据指定数据字典类型ID，查询其下级（仅下一级）的数据字典类型
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-12-11
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dicttypeid 业务字典分类id字符串
	 * @return 业务字典分类实体列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public List <EosDictType> findChildsByIds(String dicttypeid){
		if(StringUtil.isNullOrBlank(dicttypeid)) throw new RuntimeException("参数异常: 入参dicttypeid为空");
		String hql = "from EosDictType where parentid = ?";
		Object[] param = new Object[] {dicttypeid};
		List<EosDictType> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param); //使用缓存查询
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param); //不使用缓存查询
		}
		return list;
	}
}