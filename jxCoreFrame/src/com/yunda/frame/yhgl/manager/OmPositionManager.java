/**
 * <li>文件名：OmPositionManager.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-16
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
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmPosition;

/**
 * <li>类型名称：人员岗位服务类
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-16
 * <li>修改人：
 * <li>修改日期：
 */
public class OmPositionManager extends JXBaseManager<OmPosition, OmPosition> implements IOmPositionManager{



	/**
	 * <li>方法名：findOmPositionByEmp
	 * <li>@param omEmployee
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：List<OmPosition>
	 * <li>说明：通过员工信息查询员工对应的岗位
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-2-17
	 * <li>修改人： 李茂生
	 * <li>修改日期：2011-5-18
	 */
	@SuppressWarnings("unchecked")
	public List<OmPosition> findOmPositionByEmp(OmEmployee omEmployee)
			throws BusinessException {
		// 员工对应的岗位
		@SuppressWarnings("unused")
		List<OmPosition> empPositions = daoUtils
				.find("SELECT op FROM OmPosition op,OmEmpposition oemp "
						+ " WHERE op.positionid = oemp.id.positionid "
						+ " AND oemp.id.empid = "
						+ omEmployee.getEmpid());
		return empPositions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yunda.base.BaseManager#checkDelete(java.io.Serializable)
	 */
	@Override
	protected void checkDelete(Serializable id) throws BusinessException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yunda.base.BaseManager#checkUnique(java.lang.Object)
	 */
	@Override
	protected void checkUpdate(OmPosition entity) throws BusinessException {
		String propertyName = "posicode";
		if(daoUtils.isNotUnique(entity, propertyName)){
			throw new BusinessException("岗位代码【"+ entity.getPosicode() +"】已存在！");
		}
		 //判断时间
        if(entity.getEnddate()!=null
        		&&entity.getStartdate()!=null
        		&&((entity.getStartdate().getTime()-entity.getEnddate().getTime())>=0)){
    		throw new BusinessException("失效日期不能小于有效日期");
        }
	}

	/***
	 * 
	 * <li>方法名：findOmPositionnList
	 * <li>@param manaposi
	 * <li>@param type  
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：List<OmPosition>
	 * <li>说明：查找某个岗位下的字岗位
	 * <li>创建人：李茂生
	 * <li>创建日期：2011-4-18
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<OmPosition> findOmPositionnList(Long manaposi) throws BusinessException {
		String hql="from OmPosition m where 1=1 ";
			if(manaposi!=-1){
				hql+="and m.manaposi="+manaposi+"";
			}else{
				hql+="and m.manaposi is null";
			}
		return daoUtils.find(hql.toString());
	}
	/**
    * <li>方法名：findOmEmployeeByOmPostionIds
    * <li>@param postionid
    * <li>@return
    * <li>@throws BusinessException
    * <li>返回类型：List<OmEmployee>
    * <li>说明：查找岗位下的人员
    * <li>创建人：李茂生
    * <li>创建日期：2011-4-7
    * <li>修改人： 
    * <li>修改日期：
    */
	@SuppressWarnings("unchecked")
	public List<OmEmployee> findOmEmployeeByOmPostionId(Long postionid)throws BusinessException{
		String hql="select new OmEmployee(t.empid,t.empname,p.positionid) " +
			" from OmEmployee t,OmEmpposition m,OmPosition p " +
			" where t.empid=m.id.empid " +
			" and m.id.positionid=p.positionid" +
			" and m.id.positionid =("+postionid+") ";
		return daoUtils.find(hql.toString());
	}

	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配, 支持的数据类型有NUMBER,VARCHAR2,CHAR,INTEGER</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
     * @param field 字段名
     * @param value 字段值
     * @return 岗位实体列表
     */
	@SuppressWarnings("unchecked")
	public List<OmPosition> findByField(String field, String value) {
		if(field == null || value == null) throw new RuntimeException("参数异常: 入参field或者value为空");
		String hql = SqlMapUtil.getSql(XMLNAME_POS.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", field).replaceFirst("[?]", String.valueOf(value));
		List <OmPosition> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //利用查询缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
		return list;
	}
	
	/**
	 * <br/><li>说明： 根据Id查询唯一对应的岗位.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param positionid 岗位id
	 * @return 岗位实体
	 */
	public OmPosition getModelById(Long positionid){
		return (OmPosition)daoUtils.getHibernateTemplate().get(OmPosition.class, positionid);
	}
	
	/**
	 * <br/><li>说明： 根据编号精确查找与其对应的唯一岗位
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容:
	 * @param posicode 岗位编号
	 * @return 岗位实体
	 */
	@SuppressWarnings("unchecked")
	public OmPosition findByCode(String posicode){
		if(posicode == null) throw new RuntimeException("参数异常: posicode为空");
		String hql = SqlMapUtil.getSql(XMLNAME_POS.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "posicode").replaceFirst("[?]", posicode);
		List <OmPosition> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //利用查询缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
		if(list!=null&&list.size()>0) return list.get(0);
		else return null;
	}
	
	/**
	 * <br/><li>说明： 根据岗位序列查询唯一对应的岗位. positionseq的格式示例: ".0.7.146"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param positionseq 岗位Id序列
	 * @return 岗位实体
	 */
	@SuppressWarnings("unchecked")
	public OmPosition findBySeq(String positionseq){
		if(StringUtil.isNullOrBlank(positionseq)) throw new RuntimeException("参数异常: positionseq为空");
		String hql = SqlMapUtil.getSql(XMLNAME_POS.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "positionseq").replaceFirst("[?]", positionseq);
		List<OmPosition> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不使用缓存
		}
		if(list != null && list.size() > 0)	return list.get(0);
		else return null;
	}
	
	/**
	 * <br/><li>说明：根据操作员ID获取其所属的岗位
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param operatorid 操作员id
	 * @return 岗位实体
	 */
	@SuppressWarnings("unchecked")
	public OmPosition findByOperatorid(Long operatorid){
		if(operatorid==null) throw new RuntimeException("参数异常: operatorid为空");
		String hql = SqlMapUtil.getSql(XMLNAME_POS.concat("findByOperatorid"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		Object[] param = new Object[]{operatorid};
		List<OmPosition> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		if(list !=null && list.size()>0) return list.get(0);
		else return null;
	}
	
	/**
	 * <br/><li>说明：根据人员ID获取其所属岗位
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-11-17
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param empid 人员id
	 * @return 岗位实体
	 */
	@SuppressWarnings("unchecked")
	public OmPosition findByEmpid(Long empid, String isMain){
		if(empid==null) throw new RuntimeException("参数异常: empid为空");
		String hql = "";
		if(!StringUtil.isNullOrBlank(isMain)&&"y".equals(isMain)){
			hql = SqlMapUtil.getSql(XMLNAME_POS.concat("findByEmpidMain"));
		} else {
			hql = SqlMapUtil.getSql(XMLNAME_POS.concat("findByEmpid"));
		}
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		Object[] param = new Object[]{empid};
		List<OmPosition> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString(),param);  //利用查询缓存//cacheTmplFind(hql,param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		if(list !=null && list.size()>0) return list.get(0);
		else return null;
	}
	
	/**
	 * <br/><li>说明：根据人员ID获取其所属岗位
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2014-1-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param empid 人员id
	 * @return 岗位实体
	 */
	@SuppressWarnings("unchecked")
	public OmPosition findByEmpid(Long empid){
		if(empid==null) throw new RuntimeException("参数异常: empid为空");
		String hql = "select a from OmPosition a, OmEmpposition b where a.positionid = b.id.positionid  and b.id.empid = ?";
		Object[] param = new Object[]{empid};
		List<OmPosition> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString(),param);  //利用查询缓存//cacheTmplFind(hql,param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		if(list !=null && list.size()>0) return list.get(0);
		else return null;
	}

	/**
	 * 
	 * <li>说明：岗位的QBE查询,支持分页方式
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
	 *  SearchEntity <OmPosition> entity = new SearchEntity<OmPosition>(); 
	 *  OmPosition omPosition = new OmPosition();
	 *  omPosition.setPosiname("工人");
	 *  entity.setEntity(omPosition);
	 *	entity.setStart(start);
	 *	entity.setLimit(limit);
	 *	Page <OmPosition> page = omPositionManager.findByEntity(entity,true);
	 */
	public Page<OmPosition> findByEntity(SearchEntity<OmPosition> searchEntity, Boolean isExact) {
		String [] excludeColumn = null;
		return super.findByEntity(searchEntity, excludeColumn, isExact);
	}

	/**
	 * 
	 * <li>说明：岗位的QBE查询,非分页方式
	 * <br/><li><font color=red>注*：QBE查询条件对主键字段无效,即positionid列不能作为查询条件</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param entry 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 对象集合
	 * 调用示例:
	 *  OmPosition omPosition = new OmPosition();
	 *  omPosition.setPosiname("工人");
	 *	Order order = Order.asc("sortno");
	 *	Order [] or = new Order[1];
	 *	or[0] = order;
	 *  list = eosDictEntryManager.findByEntity(emp, or, false);
	 */
	public List<OmPosition> findByEntity(OmPosition omPosition, Order[] orders, Boolean isExact) {
		String [] excludeColumn = null;
		return super.findByEntity(omPosition, excludeColumn, orders, isExact);
	}
	
	/**
	 * 
	 * <li>说明：查询组织机构下所属岗位
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgid 组织机构id
	 * @return 岗位列表
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List <OmPosition> findPertainToOrg(Long orgid){
		if(orgid==null) throw new RuntimeException("参数异常: orgid为空");
		String hql = SqlMapUtil.getSql(XMLNAME_POS.concat("findPertainToOrg"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		Object[] param = new Object[]{orgid};
		List<OmPosition> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		return list;
	}
	
	/**
	 * <li>说明：查询工作组下属岗位
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 工作组id
	 * @return 岗位列表
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List <OmPosition> findPertainToWorkGroup(Long groupid){
		if(groupid==null) throw new RuntimeException("参数异常: groupid为空");
		String hql = SqlMapUtil.getSql(XMLNAME_POS.concat("findPertainToWorkGroup"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		Object[] param = new Object[]{groupid};
		List<OmPosition> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		return list;
	}
	
	/**
	 * <li>说明：查询工作组下属岗位(含子工作组下的岗位及岗位下的子岗位)
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-04-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 工作组id
	 * @return 岗位列表
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List <OmPosition> findAllPertainToWorkGroup(Long groupid){
		if(groupid==null) throw new RuntimeException("参数异常: groupid为空");
		String sql = SqlMapUtil.getSql(XMLNAME_POS.concat("findAllPertainToWorkGroup"));
		if(StringUtil.isNullOrBlank(sql)) throw new RuntimeException("SQL语句读取异常");
		sql = sql.replaceFirst("[?]", String.valueOf(groupid));
		//List<OmPosition> list = null;
		return daoUtils.executeSqlQueryEntity(sql, OmPosition.class);
	}
	
	/**
	 * <li>说明：根据岗位ID查询其下级岗位
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param positionid 岗位ID
	 * @return 岗位列表
	 * @throws 抛出异常列表
	 */
	public List <OmPosition> findChildPosition(Long positionid){
		if(positionid==null) throw new RuntimeException("参数异常: positionid为空");
		List <OmPosition> list = this.findByField("manaposi", String.valueOf(positionid));
		return list;
	}
}