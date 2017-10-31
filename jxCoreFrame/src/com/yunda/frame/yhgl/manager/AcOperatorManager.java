/**
 * <li>文件名：AcOperatorManager.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-16
 * <li>修改人： 
 * <li>修改日期：
 */
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
import com.yunda.frame.yhgl.entity.AcOperator;

/**
 * <li>类型名称：系统操作员服务类
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-16
 * <li>修改人：
 * <li>修改日期：
 */
public class AcOperatorManager extends JXBaseManager<AcOperator, AcOperator> implements IAcOperatorManager {

	/**
	 * <li>方法名：findLoginAcOperator
	 * <li>
	 * 
	 * @param operatorid
	 *            <li>
	 * @return
	 *            <li>返回类型：AcOperator
	 *            <li>说明：通过operatorid获取操作员信息
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-2-16
	 *            <li>修改人：
	 *            <li>修改日期：
	 * @throws BusinessException
	 */
	public AcOperator findLoginAcOprator(Long operatorid)
			throws BusinessException {
		try{
//			AcOperator acOperator = this.findSingle("FROM AcOperator  WHERE operatorid = '"  + operatorid  + "'");
			AcOperator acOperator = this.getModelById(operatorid);
			return acOperator;
		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * <li>方法名：findLoginAcOprator
	 * <li>@param userid
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：AcOperator
	 * <li>说明： 通过userid获取操作员信息
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2012-6-29
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public AcOperator findLoginAcOprator(String userid) throws BusinessException {
		try{
//			AcOperator acOperator = this.findSingle("FROM AcOperator  WHERE userid = '" + userid + "'");
			AcOperator acOperator = this.findByUserId(userid);
			return acOperator;
		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * <li>方法名：findLoginAcOperator
	 * <li>
	 * 
	 * @param userid
	 * @param pwd
	 *            <li>
	 * @return
	 *            <li>返回类型：AcOperator
	 *            <li>说明：通过userid和pwd获取操作员信息
	 *            <li>创建人：曾锤鑫
	 *            <li>创建日期：2011-2-16
	 *            <li>修改人：
	 *            <li>修改日期：
	 * @throws BusinessException
	 */
	public AcOperator findLoginAcOprator(String userid, String pwd)
			throws BusinessException {
		try{
		AcOperator ac = new AcOperator();
		ac.setUserid(userid);
		ac.setPassword(pwd);
		List<AcOperator> list = super.findList(ac, null, null);
		if(list == null || list.size() < 1)	return null;
		return list.get(0);
		}catch(Exception ex){
			return null;
		}
	}
	
	@Override
	protected void checkDelete(Serializable id) throws BusinessException {
	}

	/*
	 *     业务数据统一接口方法
	 */
	
	/**
	 * <br/><li>说明： 通过人员Id查询操作员信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-23
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param userId 人员Id
	 * @return 操作员实体
	 */
	@SuppressWarnings("unchecked")
	public AcOperator findByUserId(String userId) {
		if(userId == null) throw new RuntimeException("参数异常: 入参userId为空");
		String hql = SqlMapUtil.getSql(XMLNAME_OPE.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "userid").replaceFirst("[?]", userId);
		List <AcOperator> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql); //不使用缓存
		}
		if(list!=null&&list.size()>0) return (AcOperator)list.get(0);
		else return null;
	}
	
	/**
	 * <br/><li>说明： 通过操作员Id查询操作员信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-23
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param operatorId 操作员Id
	 * @return 操作员实体
	 */
	public AcOperator getModelById(Long operatorId){
		if(operatorId == null) throw new RuntimeException("参数异常: 入参operatorId为空");
		return (AcOperator)daoUtils.getHibernateTemplate().get(AcOperator.class, operatorId);
	}
	
	/**
	 * <br/><li>说明： 根据人员卡号查询操作员信息列表
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-25
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param cardNo 卡号
	 * @return 操作员实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<AcOperator> findByCardNo(String cardNo){
		if(cardNo == null) throw new RuntimeException("参数异常: 入参cardNo为空");
		String hql = SqlMapUtil.getSql(XMLNAME_OPE.concat("findByCardNo"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", cardNo);
		List <AcOperator> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql); //不使用缓存
		}
		return list;
	}
	
	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-7-25
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
     * @param field 字段名
     * @param value 字段值
     * @return 操作员实体列表
     */
	@SuppressWarnings("unchecked")
	public List<AcOperator> findByField(String field, String value){
		if(field == null || value == null) throw new RuntimeException("参数异常: 入参field或value为空");
		String hql = SqlMapUtil.getSql(XMLNAME_OPE.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", field).replaceFirst("[?]", String.valueOf(value));
		List <AcOperator> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql); //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql); //不使用缓存
		}
		return list;
	}
	
	/**
	 * <br/><li>说明：根据多个操作员Id,查询与对应的操作员
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个operatorid, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param opeIds 操作员id字符串
	 * @return 操作员实体列表
	 */
	@SuppressWarnings("unchecked")
	public List <AcOperator> findByIds(String opeIds){
		if(opeIds == null) throw new RuntimeException("参数异常: 入参opeIds为空");
		String hql = SqlMapUtil.getSql(XMLNAME_OPE.concat("findByIds"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replace("?", opeIds);
		List <AcOperator> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql); //不使用缓存
		}
		return list;
	}
	
	/**
	 * <li>说明：根据操作员Id数组,查询匹配的操作员列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-7-29
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param ids 操作员Id数组
	 * @return 操作员实体列表
	 */
	public List <AcOperator> findByIdArys(Serializable... ids){
		if(ids == null) throw new RuntimeException("参数异常: 入参ids为空");
		List<AcOperator> list = new ArrayList<AcOperator>();
		for (Serializable opeId : ids) {
			list.add(this.getModelById(Long.parseLong(opeId.toString())));
		}
		return list;
	}
	
	/**
	 * <li>说明：操作员的QBE查询,支持分页方式
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-7-29
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 操作员分页对象
	 * 调用示例:
	 *  SearchEntity <AcOperator> entity = new SearchEntity<AcOperator>();
	 *  String [] excludeColumn = new String[]{"operatorid","password"};
	 *  entity.setEntity(org);
	 *	entity.setStart(start);
	 *	entity.setLimit(limit);
	 *	page = acOperatorManager.findByEntity(entity,excludeColumn,true);
	 */
	@SuppressWarnings("unchecked")
	public Page <AcOperator> findByEntity(SearchEntity <AcOperator> searchEntity, Boolean isExact){
		String [] excludeColumn = new String[]{"operatorid","password"};
		return super.findByEntity(searchEntity, excludeColumn, isExact);
	}
	
	/**
	 * <li>说明：操作员的QBE查询,非分页方式
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-7-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ope 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 人员对象集合
	 * 调用示例:
	 *  AcOperator ope = new AcOperator();
	 *  String [] excludeColumn = new String[]{"operatorid","password"};
	 *	ope.setoperatorid("4");
	 *	Order order = Order.asc("operatorid");
	 *	Order [] or = new Order[1];
	 *	or[0] = order;
	 *  list = acOperatorManager.findByEntity(ope,excludeColumn, or,true);
	 */
	@SuppressWarnings("unchecked")
	public List <AcOperator> findByEntity(AcOperator ope, Order [] orders, Boolean isExact){
		String [] excludeColumn = new String[]{"operatorid","password"};
		return super.findByEntity(ope, excludeColumn, orders, isExact);
	}
	
	/**
	 * <li>说明：根据功能名称查询拥有该功能名称菜单权限的operatorid结果集
	 * <li>创建人：林欢
	 * <li>创建日期：2016-9-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param functionName 功能菜单名称
	 * @return List<String> 人员操作表idx
	 */
	@SuppressWarnings("unchecked")
	public List<String> findOperatorIDsByFunctionName(String functionName){
		StringBuffer sb = new StringBuffer();
		
		sb.append(" select distinct t.operatorid || '' from (select d.operatorid ");
		sb.append(" from ac_function a left join ac_rolefunc b on a.funccode = b.funccode ");
		sb.append(" left join ac_role c on c.roleid = b.roleid ");
		sb.append(" left join ac_operatorrole d on d.roleid = c.roleid ");
		sb.append(" where a.funcname in ('").append(functionName.replaceAll(",", "','")).append("')");
		sb.append(" union all ");
		sb.append(" select f.operatorid ");
		sb.append(" from ac_function a left join ac_rolefunc b on a.funccode = b.funccode ");
		sb.append(" left join ac_role c on c.roleid = b.roleid ");
		sb.append(" left join Om_Partyrole d on d.roleid = c.roleid and d.partytype = 'organization' ");
		sb.append(" left join om_emporg e on d.partyid = e.orgid ");
		sb.append(" left join om_employee f on f.empid = e.empid ");
		sb.append(" where a.funcname in ('").append(functionName.replaceAll(",", "','")).append("')");
		sb.append(" union all ");
		sb.append(" select g.operatorid ");
		sb.append(" from ac_function a left join ac_rolefunc b on a.funccode = b.funccode ");
		sb.append(" left join ac_role c on c.roleid = b.roleid ");
		sb.append(" left join om_Partyrole d on d.roleid = c.roleid and d.partytype = 'position' ");
		sb.append(" left join om_position e on e.positionid = d.partyid ");
		sb.append(" left join OM_EMPPOSITION f on f.positionid = e.positionid ");
		sb.append(" left join om_employee g on g.empid = f.empid ");
		sb.append(" where a.funcname in ('").append(functionName.replaceAll(",", "','")).append("')");
		sb.append(" union all ");
		sb.append(" select h.operatorid ");
		sb.append(" from ac_function a left join ac_rolefunc b on a.funccode = b.funccode ");
		sb.append(" left join ac_role c on c.roleid = b.roleid ");
		sb.append(" left join om_Partyrole d on d.roleid = c.roleid and d.partytype = 'duty' ");
		sb.append(" left join om_duty e on e.dutyid = d.partyid ");
		sb.append(" left join om_position f on f.dutyid = e.dutyid ");
		sb.append(" left join om_empposition g on g.positionid = f.positionid ");
		sb.append(" left join om_employee h on h.empid = g.empid ");
		sb.append(" where a.funcname in ('").append(functionName.replaceAll(",", "','")).append("')) t");
		
		return this.daoUtils.executeSqlQuery(sb.toString());
	}
}