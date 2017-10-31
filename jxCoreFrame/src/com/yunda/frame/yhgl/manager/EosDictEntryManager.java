/**
 * <li>文件名：EosDictEntryManager.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-4-7
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Order;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.EosDictEntry;

/**
 * <li>类型名称：
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-4-7
 * <li>修改人：
 * <li>修改日期：
 */
public class EosDictEntryManager extends
		JXBaseManager<EosDictEntry, EosDictEntry> implements IEosDictEntryManager{

	@Override
	protected void checkDelete(Serializable id) throws BusinessException {
		// TODO Auto-generated method stub
	}
	@Override
	protected void checkUpdate(EosDictEntry entity) throws BusinessException {
		/**
		if(entity.getId() == null || "".equals(entity.getId())){		//	添加时验证类型代码是否存在
			if(daoUtils.isExist("from EosDictEntry where id.dictid = '"+entity.getId().getDictid() +"'")){
				throw new BusinessException("类型项代码【"+ entity.getId().getDictid() +"】已存在！");
			}
		}
		if(daoUtils.isNotUnique(entity, "dictname")){
			throw new BusinessException("类型项名称【"+ entity.getDictname() +"】已存在！");
		}
		*/
	}
	
	/**
	 * <li>方法名：deleteByEosdy
	 * <li>@param dicttypeid
	 * <li>@param dictid
	 * <li>@throws BusinessException
	 * <li>返回类型：void
	 * <li>说明：根据复合ID批量删除对象
	 * <li>创建人：王开强
	 * <li>创建日期：2011-4-11
	 * <li>修改人： 
	 * <li>修改日期： 
	 * <li>说明: 
	 */
	@SuppressWarnings("unchecked")
	public EosDictEntry getEntity(String dictid,String dicttypeid){
		EosDictEntry eosdy = null;
		String hql = "from EosDictEntry where id.dicttypeid = '"+ dicttypeid +"' and id.dictid = '"+ dictid +"'";
		List<EosDictEntry> eosdyList = daoUtils.find(hql);
		if(eosdyList != null && eosdyList.size() == 1)eosdy = eosdyList.get(0);
		return eosdy;
	}
	
	/**
	 * <li>说明：接口实现,根据业务字典项ID,及业务字典分类ID,查询业务字典项实体
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param dictid 业务字典项ID
	 * @param dicttypeid 业务字典分类ID
	 * @return 业务字典项实体
	 */
	@SuppressWarnings("unchecked")
	public EosDictEntry findCacheEntry(String dictid,String dicttypeid){
		if(StringUtil.isNullOrBlank(dictid)||StringUtil.isNullOrBlank(dicttypeid)) throw new RuntimeException("参数异常: 入参dictid或dicttypeid为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DICT.concat("getEntity"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		EosDictEntry eosdy = null;
		Object[] param = new Object[]{dictid,dicttypeid};
		List<EosDictEntry> eosdyList = null;//daoUtils.find(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			eosdyList = daoUtils.find(true, hql, param);  //使用缓存
		} else {
			eosdyList = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		if(eosdyList != null && eosdyList.size() == 1)eosdy = eosdyList.get(0);
		return eosdy;
	}
	
	/**
	 * <li>说明：接口实现,根据业务字典分类,查询业务字典项实体(同"findByDictTypeId()")
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param dicttypeid 业务字典分类ID
	 * @return 业务字典项实体
	 */
	@SuppressWarnings("unchecked")
	public List <EosDictEntry> findCacheEntry(String dicttypeid){
		if(StringUtil.isNullOrBlank(dicttypeid)) throw new RuntimeException("参数异常: 入参dicttypeid为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DICT.concat("findByField2"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "id.dicttypeid").replaceFirst("[?]", dicttypeid);
		List<EosDictEntry> list =  null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql); //使用缓存查询
		} else {
			list = daoUtils.getHibernateTemplate().find(hql); //不使用缓存查询
		}
		return list;
	}
	
	/**
	 * <li>方法名：findByTypeId
	 * <li>@param dicttypeid
	 * <li>@return
	 * <li>返回类型：List<EosDictEntry>
	 * <li>说明：根据hql语句查询相应的字典数据
	 * <li>创建人：王开强
	 * <li>创建日期：2011-4-19
	 * <li>修改人： 谭诚
	 * <li>修改日期： 2013-08-22
	 * <li>修改内容: 添加查询缓存支持
	 */
	@SuppressWarnings("unchecked")
	public List<EosDictEntry> findToList(String hql){
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("参数异常: 入参hql为空");
		List <EosDictEntry> list = daoUtils.getHibernateTemplate().find(hql); 
		return list;
	}
	
	/**
	 * 
	 * <li>方法名：getdept
	 * <li>@param xcid
	 * <li>@param traintype
	 * <li>@return
	 * <li>@throws Exception
	 * <li>返回类型：EosDictEntry
	 * <li>说明：根据车型和修程获得默认承修部门
	 * <li>创建人：罗鑫
	 * <li>创建日期：2012-2-15
	 * <li>修改人： 谭诚
	 * <li>修改日期： 2013-08-21
	 * <li>修改内容: 添加缓存支持
	 */
	@SuppressWarnings("unchecked")
	public EosDictEntry getdept(String xcid,String traintype) throws Exception{
		if(StringUtil.isNullOrBlank(xcid)||StringUtil.isNullOrBlank(traintype)) throw new RuntimeException("参数异常: 入参xcid或traintype为空");
		String hql = SqlMapUtil.getSql(XMLNAME_DICT.concat("getdept"));
		Object[] param = new Object[]{traintype,xcid};
		List <EosDictEntry> list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * <li>方法名：getDefaultMethod
	 * <li>@return
	 * <li>返回类型：List<EosDictEntry>
	 * <li>说明：查询落修的默认设置
	 * <li>创建人：罗鑫
	 * <li>创建日期：2012-2-23
	 * <li>修改人： 谭诚
	 * <li>修改日期： 2013-08-22
	 * <li>修改内容: 添加查询缓存支持
	 */
	@SuppressWarnings("unchecked")
	public EosDictEntry getDefaultMethod(){
		List <EosDictEntry> list = findCacheEntry("JCJX_LXSZ");
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * <li>说明：业务字典项的QBE查询,支持分页方式
	 * <br/><li><font color=red>注*：QBE查询条件对主键字段无效,而Eos_Dict_Entry表采用双主键,即dicttypeid和dictid列不能作为查询条件</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 分页对象
	 * 调用示例:
	 *  SearchEntity <EosDictEntry> entity = new SearchEntity<EosDictEntry>();
	 *  String [] excludeColumn = new String[]{"id.dicttypeid","id.dictid"};
	 *  EosDictEntry entry = new EosDictEntry();
	 *  entry.setDictName("开工签名");
	 *  entity.setEntity(entry);
	 *	entity.setStart(start);
	 *	entity.setLimit(limit);
	 *	page = omEmployeeManager.findByEntity(entity,excludeColumn,true);
	 */
	public Page<EosDictEntry> findByEntity(SearchEntity<EosDictEntry> searchEntity, Boolean isExact) {
		String [] excludeColumn = null;//new String[]{"empid"};
		return super.findByEntity(searchEntity, excludeColumn, isExact);
	}
	
	/**
	 * 
	 * <li>说明：业务字典项的QBE查询,非分页方式
	 * <br/><li><font color=red>注*：QBE查询条件对主键字段无效,而Eos_Dict_Entry表采用双主键,即dicttypeid和dictid列不能作为查询条件</font>
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param entry 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 对象集合
	 * 调用示例:
	 *  EosDictEntry entry = new EosDictEntry();
	 *  String [] excludeColumn = new String[]{"id.dicttypeid"};
	 *	entry.setDictName("助理工程师");
	 *	Order order = Order.asc("sortno");
	 *	Order [] or = new Order[1];
	 *	or[0] = order;
	 *  list = eosDictEntryManager.findByEntity(emp, or, false);
	 */
	public List<EosDictEntry> findByEntity(EosDictEntry entry, Order[] orders, Boolean isExact) {
		String [] excludeColumn = null;//new String[]{"empid"};
		return super.findByEntity(entry, excludeColumn, orders, isExact);
	}
	
	/**
	 * <br/><li>说明：获取业务字典的根层节点
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-12-13
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @return 业务字典分类实体列表
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public List <EosDictEntry> findRoots(String dicttypeid){
		if(StringUtil.isNullOrBlank(dicttypeid)) throw new RuntimeException("参数异常: 入参dicttypeid为空");
		String hql = "from EosDictEntry where id.dicttypeid = ? and parentid is null order by seqno ";
		List <EosDictEntry> list = null;
		Object[] param = new Object[] {dicttypeid};
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param); //使用缓存查询
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param); //不使用缓存查询
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
	public List <EosDictEntry> findChildsByIds(String dicttypeid, String dictid){
		if(StringUtil.isNullOrBlank(dicttypeid)) throw new RuntimeException("参数异常: 入参dicttypeid为空");
		if(StringUtil.isNullOrBlank(dictid)) throw new RuntimeException("参数异常: 入参dictid为空");
		String hql = "from EosDictEntry where id.dicttypeid = ? and parentid = ? order by seqno ";
		List <EosDictEntry> list = null;
		Object[] param = new Object[] {dicttypeid,dictid};
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param); //使用缓存查询
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param); //不使用缓存查询
		}
		return list;
	}
}