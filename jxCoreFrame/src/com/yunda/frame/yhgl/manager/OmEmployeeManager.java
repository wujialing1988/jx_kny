/**
 * <li>文件名：OmEmployee.java
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

import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;

/**
 * <li>类型名称：员工服务类
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-16
 * <li>修改人：
 * <li>修改日期：
 */
public class OmEmployeeManager extends JXBaseManager<OmEmployee, OmEmployee> implements IOmEmployeeManager{
	
	/**
	 * <li>方法名：findOmEmployee
	 * <li>@param Operatorid 操作员ID
	 * <li>@return 
	 * <li>@throws BusinessException
	 * <li>返回类型：AcOperator
	 * <li>说明：
	 * <li>创建人：曾锤鑫
	 * <li>创建日期：2011-2-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public OmEmployee findOmEmployee(Long Operatorid)
			throws BusinessException {
		OmEmployee omEmployee = null;
		String hql = "FROM OmEmployee WHERE operatorid = " + Operatorid;
		Object obj = daoUtils.findSingle(hql);
		if (obj != null) {
			omEmployee = (OmEmployee) obj;
		}
		return omEmployee;
	}
	
	/**
	 * 
	 * <li>方法名：getEmp
	 * <li>@param ProName
	 * <li>@param val
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：OmEmployee
	 * <li>说明：根据条件取出人选信息
	 * <li>创建人：罗鑫
	 * <li>创建日期：2011-12-21
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public OmEmployee getEmp(String ProName,String val) throws BusinessException{
		return (OmEmployee)daoUtils.findSingle(" from OmEmployee t where "+ ProName +" = '"+val+"'");
	}


	/**
	 * 
	 * <li>方法名：findEmp
	 * <li>@param orgid  单位idx
	 * <li>@return 
	 * <li>返回类型：List<OmEmployee>
	 * <li>说明：查询出本单位下所有的人员
	 * <li>创建人：罗鑫
	 * <li>创建日期：2011-9-19
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmployee> findEmp(String orgid){
		try{
			return (List<OmEmployee>)daoUtils.find("from OmEmployee where orgid='"+ orgid +"'");
		}catch(Exception ex){
			return null;
		}
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

	/**
	 * <li>说明：根据组织机构获取人员列表, 前台使用JSON输出
	 * <li>创建人：袁健
	 * <li>创建日期：2013-3-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgId 组织机构Id
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmployee> getEmpsByOrgId(String orgId) {
		//String sql = "select a.operatorid, a.operatorname from om_employee o, ac_operator a where o.userid = a.userid and o.operatorid = a.operatorid and o.orgid = '';";
//		String hql = "from OmEmployee o where o.orgid = '" + orgId + "'";
//		List<OmEmployee> list = daoUtils.find(hql);
//		return list;
		return this.findByOrgId(Long.valueOf(orgId));
	}

	/*
	 *     业务数据统一接口方法
	 */
	
	/**
	 * <br/><li>说明：根据人员code查询其信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-18
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为查询缓存方式
	 * @param empCode 人员code
	 * @return 人员实体
	 */
	
	@SuppressWarnings("unchecked")
	public OmEmployee findByCode(String empCode) {
		if(empCode == null) throw new RuntimeException("参数异常: empCode为空");
		String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findEmpByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "empcode").replaceFirst("[?]", String.valueOf(empCode));
		List <OmEmployee> list = null;
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
	 * <br/><li>说明：根据人员Id查询其信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-18
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param empId 人员Id
	 * @return 人员实体
	 */
	public OmEmployee getModelById(Long empId) {
		return (OmEmployee)daoUtils.getHibernateTemplate().get(OmEmployee.class, empId);
	}

	/**
	 * <br/><li>说明：查询与该用户相同组织下的所有用户, degree表示范围 [tream,hq,plant,oversea,branch]
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-18
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param empId 用户Id
	 * @param degree 哪级部门
	 * @return 人员实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmployee> findBySameOrg(Long empId, String degree) {
		@SuppressWarnings("unused")
		OmOrganizationManager omOrganizationManager = (OmOrganizationManager)Application.getSpringApplicationContext().getBean("omOrganizationManager");
		/*
		 * 根据人员Id, 查询其所属组织机构或部门班组
		 */
		OmOrganization org = omOrganizationManager.findByEmpId(empId);
		if( org == null || org.getOrgid() == null ){
			return null;
		}
		/*
		 * 根据组织机构Orgid,查询其上级各组织机构, 以实体集合形式返回 
		 * 如指定了degree, 则查询符合degree指定级别的上级组织机构
		 */
		List <OmEmployee> empList = null;
		List <OmOrganization> list = omOrganizationManager.findUpperByDegree(org.getOrgid(), degree);
		if( list == null || list.size()<1){
			return null;
		} else if ( list.size() == 1 ) { //当degree == null 或者 只有一个目标组织机构级别时
			if(StringUtil.isNullOrBlank(degree)){
				//查询直接归属本组织机构或班组的人员
				String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findListByOrgIdForTargetOrg"));
				if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
				Object[] param = new Object[]{list.get(0).getOrgid()};
				//是否利用查询缓存
				if(isJcglQueryCacheEnabled()){
					empList = daoUtils.find(true, hql, param); //利用查询缓存
				} else {
					empList = daoUtils.getHibernateTemplate().find(hql,param);  //不利用查询缓存
				}
			} else {
				//查询直接归属本组织机构或班组的人员,以及归属本组织机构或班组的子机构的人员  
				String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findListByOrgIdForMultilevelOrg"));
				if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
				hql = hql.replace("?", list.get(0).getOrgseq());
				//是否利用查询缓存
				if(isJcglQueryCacheEnabled()){
					empList = daoUtils.find(true, hql.toString());  //利用查询缓存
				} else {
					empList = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
				}
			}
		} else if ( list.size() > 1 ) {  //当degree是以","分割的多个组织机构级别时, 返回的List.size将大于1
			//查询直接归属目标组织机构中级别较高的机构下属的机构或班组人员, 包括子机构
			//查询直接归属本组织机构或班组的人员,以及归属本组织机构或班组的子机构的人员  
			String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findListByOrgIdForMultilevelOrg"));
			if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
			hql = hql.replace("?", list.get((list.size()-1)).getOrgseq());//list集合中,是根据机构级别倒序排列, 所以级别最高的机构在list尾部
			//是否利用查询缓存
			if(isJcglQueryCacheEnabled()){
				empList = daoUtils.find(true, hql.toString());  //利用查询缓存
			} else {
				empList = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
			}
		}
		return empList;
	}
	
	/**
	 * <br/><li>说明：查询与该用户相同组织下的所有用户, degree表示范围 [tream,hq,plant,oversea,branch], 同时还需要匹配参数指定的人员姓名
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-18
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param operatorId 操作员Id
	 * @param empName 模糊匹配人员姓名
	 * @return 人员实体列表
	 */
	@SuppressWarnings("unchecked")
	public List <OmEmployee> findByOperator(Long operatorId, String empName){
		@SuppressWarnings("unused")
		OmOrganizationManager omOrganizationManager = (OmOrganizationManager)Application.getSpringApplicationContext().getBean("omOrganizationManager");
		
		OmEmployee omEmployee = findByOperator(operatorId);  //根据操作员Id获取人员实体
		if(omEmployee==null||omEmployee.getEmpid() == null) return null;
		
		OmOrganization org = omOrganizationManager.findByEmpId(omEmployee.getEmpid());//根据人员Id, 查询其所属组织机构或部门班组
		List <OmEmployee> empList = null;
		if( org == null ){
			return null;
		} else { 
			if(!StringUtil.isNullOrBlank(empName)){
				//需要与人员姓名匹配
				String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findListByOrgIdForTargetOrg2"));
				if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
				hql = hql.replaceFirst("[?]", String.valueOf(org.getOrgid())).replaceFirst("[?]", "%".concat(empName).concat("%"));
				//是否利用查询缓存
				if(isJcglQueryCacheEnabled()){
					empList = daoUtils.find(true, hql.toString());  //利用查询缓存
				} else {
					empList = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
				}
			} else {
				//不需要与人员姓名匹配
				String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findListByOrgIdForTargetOrg"));
				if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
				Object[] param = new Object[]{org.getOrgid()};
				//是否利用查询缓存
				if(isJcglQueryCacheEnabled()){
					empList = daoUtils.find(true, hql, param);   //利用查询缓存
				} else {
					empList = daoUtils.getHibernateTemplate().find(hql,param);  //不利用查询缓存
				}
			}
		}
		return empList;
	}

	/**
	 * <br/><li>说明：根据人员姓名查询其信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-18
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param empName 人员姓名
	 * @return 人员实体
	 */
	@SuppressWarnings("unchecked")
	public OmEmployee findByName(String empName) {
		if(empName == null) throw new RuntimeException("参数异常: 入参empName为空");
		String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findEmpByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", "empname").replaceFirst("[?]", String.valueOf(empName));
		List <OmEmployee> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());   //利用查询缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
		if(list!=null&&list.size()>0) return list.get(0);
		else return null;
	}
	
	/**
	 * <br/><li>说明：查询某个组织机构下直属的用户信息(不包含子机构的用户)
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-23
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 机构Id
	 * @return 人员实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmployee> findByOrgId(Long orgId){
		if(orgId == null) throw new RuntimeException("参数异常: 入参orgId为空");
		//查询直接归属本组织机构或班组的人员
		String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findListByOrgIdForTargetOrg"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replace("?", String.valueOf(orgId));
		List <OmEmployee> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //利用查询缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
		return list;
	}
	/**
	 * <br/><li>说明：查询一组机构下直属的用户信息(不包含子机构的用户)
	 * <br/><li>创建人：刘晓斌
	 * <br/><li>创建日期：2013-12-25
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgIds 机构Id
	 * @return 人员实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmployee> findByOrgIds(Long... orgIds){
		if(orgIds == null || orgIds.length < 1) throw new RuntimeException("参数异常: 入参orgIds为空");
		//查询直接归属本组织机构或班组的人员
		String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findListByOrgIdForTargetOrgs"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		StringBuilder sb = new StringBuilder();
		for (Long orgid : orgIds) {
			sb.append("'").append(orgid).append("',");
		}
		sb.deleteCharAt(sb.length() - 1);
		hql = hql.replace("?", String.valueOf(sb.toString()));
		List <OmEmployee> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //利用查询缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
		return list;
	}	
	/**
	 * <li>说明：查询隶属于某个机构但并不属于该组织下任何岗位的人员信息（机构人员树采用）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgId 机构ID
	 * @return 人员实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmployee> findNoPositionByOrgId(Long orgId){
		if(orgId == null) throw new RuntimeException("参数异常: 入参orgId为空");
		//查询直接归属本组织机构或班组的人员
		String sql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findNoPositionByOrgId"));
		if(StringUtil.isNullOrBlank(sql)) throw new RuntimeException("SQL语句读取异常");
		sql = sql.replace("?", String.valueOf(orgId));
		List <OmEmployee> list = null;
		//是否利用查询缓存
		if(false){
			list = daoUtils.cacheSqlQueryEntity(sql, OmEmployee.class);  //利用查询缓存
		} else {
			list = daoUtils.executeSqlQueryEntity(sql, OmEmployee.class);  //不利用查询缓存
		}
		return list;
	}
	
	/**
	 * <li>说明：查询隶属于某个工作组但并不属于该组织下任何岗位的人员信息（工作组树采用）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 谭诚
	 * <li>修改日期：2013-11-21
	 * <li>修改内容：该查询不适用于缓存查询
	 * @param groupId 工作组id
	 * @return 人员实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmployee> findNoPositionByGroupId(Long groupId){
		if(groupId == null) throw new RuntimeException("参数异常: 入参groupId为空");
		//查询直接归属本工作组的人员
		String sql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findNoPositionByGroupId"));
		if(StringUtil.isNullOrBlank(sql)) throw new RuntimeException("SQL语句读取异常");
		sql = sql.replace("?", String.valueOf(groupId));
		List <OmEmployee> list = null;
//		//是否利用查询缓存
//		if(isJcglQueryCacheEnabled()){
//			list = daoUtils.cacheSqlQueryEntity(sql, OmEmployee.class);  //利用查询缓存
//		} else {
			list = daoUtils.executeSqlQueryEntity(sql, OmEmployee.class);  //不利用查询缓存
//		}
		return list;
	}
	
	/**
	 * <li>说明：查询隶属于某个机构但并不属于该组织下任何岗位的人员信息（机构人员列表采用）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgId 机构ID
	 * @return 人员实体分页列表
	 */
	public Page findNoPositionByOrgId(Long orgId, String searchParam, Integer start, Integer limit){
		if(orgId == null ) throw new RuntimeException("参数异常: 入参orgId为空");
		//查询直接归属本组织机构或班组的人员
		String sql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findNoPositionByOrgId2"));
		if(StringUtil.isNullOrBlank(sql)) throw new RuntimeException("SQL语句读取异常");
		sql = sql.replace("?", String.valueOf(orgId)).concat(searchParam);
		String totalSql = "select count(*) as \"rowcount\" from (".concat(sql).concat(")");
		return super.findPageList(totalSql, sql, start, limit, null, null);  //不使用缓存
	}
	
	/**
	 * <li>说明：查询隶属于某个机构以及该机构下任何岗位的人员信息（机构人员列表采用）
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-1-2
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgId 机构ID
	 * @return 人员实体分页列表
	 */
	public Page findByOrgId(Long orgId, String searchParam, Integer start,Integer limit){
		if(orgId == null ) throw new RuntimeException("参数异常: 入参orgId为空");
		//查询直接归属本组织机构或班组的人员
		String sql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findNoPositionByOrgId3"));
		if(StringUtil.isNullOrBlank(sql)) throw new RuntimeException("SQL语句读取异常");
		sql = sql.replace("?", String.valueOf(orgId)).concat(searchParam);
		String totalSql = "select count(*) as \"rowcount\" from (".concat(sql).concat(")");
		return super.findPageList(totalSql, sql, start, limit, null, null);  //不使用缓存
	}
	
	/**
	 * <li>说明：查询隶属于某个岗位但并不属于该岗位下任何子岗位的人员信息（机构人员列表采用）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgId 机构ID
	 * @return 人员实体分页列表
	 */
	public Page findNoPositionByPosId(Long positionid, String searchParam, Integer start, Integer limit){
		if(positionid == null ) throw new RuntimeException("参数异常: 入参orgId为空");
		//查询直接归属本组织机构或班组的人员
		String sql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findNoPositionByPosId"));
		if(StringUtil.isNullOrBlank(sql)) throw new RuntimeException("SQL语句读取异常");
		sql = sql.replace("?", String.valueOf(positionid)).concat(searchParam);
		String totalSql = "select count(*) as \"rowcount\" from (".concat(sql).concat(")");
		return super.findPageList(totalSql, sql, start, limit, null, null);  //不使用缓存
	}
	
	
	
	/**
	 * <li>说明：根据岗位ID，查询其所属人员
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param positionid 岗位ID
	 * @return 人员实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmployee> findByPosition(Long positionid){
		if(positionid == null) throw new RuntimeException("参数异常: 入参positionid为空");
		//查询直接归属本组织机构或班组的人员
		String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findByPosition"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replace("?", String.valueOf(positionid));
		List <OmEmployee> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //利用查询缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
		return list;
	}

	/**
	 * <br/><li>说明：分页查询某个组织机构下直属的用户信息(不包含子机构的用户)
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-23
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 机构Id
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 人员实体分页列表
	 */
	public Page findByOrgId(Long orgId,Integer start,Integer limit){
		if(orgId == null ) throw new RuntimeException("参数异常: 入参orgId为空");
		//查询直接归属本组织机构或班组的人员
		String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findListByOrgIdForTargetOrg"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replace("?", String.valueOf(orgId));
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit);  //利用查询缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}
	}
	
	/**
	 * <br/><li>说明：分页查询某个组织机构下直属的用户信息,以及归属本组织机构或班组的子机构的人员
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-23
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 机构Id
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 人员实体分页列表
	 */
	public Page findChildsByOrgId(Long orgId,Integer start,Integer limit){
		if(orgId == null) throw new RuntimeException("参数异常: 入参orgId为空");
		/* 调用组织机构查询接口, 获得当前OrgId的对象, 从中获取OrgSeq序列 */
		OmOrganizationManager omOrganizationManager = (OmOrganizationManager)Application.getSpringApplicationContext().getBean("omOrganizationManager");
		List <OmOrganization> list = omOrganizationManager.findUpperByDegree(orgId, null);
		if( list == null || list.size()<1) return null;
		//查询直接归属本组织机构或班组的人员,以及归属本组织机构或班组的子机构的人员  
		String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findListByOrgIdForMultilevelOrg"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replace("?", list.get(0).getOrgseq());
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit);   //利用查询缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}
	}
	
	/**
	 * 
	 * <li>说明：根据操作员Id,获取人员信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-7-24
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param operatorId 操作员Id
	 * @return 人员实体
	 */
	@SuppressWarnings("unchecked")
	public OmEmployee findByOperator(Long operatorId){
		if(operatorId == null) throw new RuntimeException("参数异常: 入参operatorId为空");
		String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findEmpByOperator"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replace("?", String.valueOf(operatorId));
		List <OmEmployee> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //利用查询缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
		if(list!=null&&list.size()>0) return (OmEmployee)list.get(0);
		else return null;
	}
	
	/**
	 * <br/><li>说明：根据操作员Id,获取人员信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param field 字段名
	 * @param value 字段值
	 * @return 人员实体
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmployee> findByField(String field, String value){
		if(field == null || value == null) throw new RuntimeException("参数异常: 入参field或者value为空");
		String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findEmpByField"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replaceFirst("[?]", field).replaceFirst("[?]", String.valueOf(value));
		List <OmEmployee> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //利用查询缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
		return list;
	}
	
	/**
	 * <br/><li>说明：根据多个人员Id,查询与对应的人员
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个empId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param empIds 人员id字符串
	 * @return 人员实体列表
	 */
	@SuppressWarnings("unchecked")
	public List <OmEmployee> findByIds(String empIds){
		if(empIds == null) throw new RuntimeException("参数异常: 入参empIds为空");
		String hql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findByIds"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		hql = hql.replace("?", empIds);
		List <OmEmployee> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql.toString());  //利用查询缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
		return list;
	}
	
	/**
	 * <li>说明：根据人员Id数组,查询匹配的机构信息列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-7-29
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param ids 人员Id数组
	 * @return 人员实体列表
	 */
	public List <OmEmployee> findByIdArys(Serializable... ids){
		if(ids == null) throw new RuntimeException("参数异常: 入参ids为空");
		List<OmEmployee> list = new ArrayList<OmEmployee>();
		for (Serializable empId : ids) {
			list.add(this.getModelById(Long.parseLong(empId.toString())));
		}
		return list;
	}
	
	/**
	 * 
	 * <li>说明：人员的QBE查询,支持分页方式
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-7-29
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 人员分页对象
	 * 调用示例:
	 *  SearchEntity <OmEmployee> entity = new SearchEntity<OmEmployee>();
	 *  String [] excludeColumn = new String[]{"empid"};
	 *  entity.setEntity(org);
	 *	entity.setStart(start);
	 *	entity.setLimit(limit);
	 *	page = omEmployeeManager.findByEntity(entity,excludeColumn,true);
	 */
	@SuppressWarnings("unchecked")
	public Page <OmEmployee> findByEntity(SearchEntity <OmEmployee> searchEntity, Boolean isExact){
		String [] excludeColumn = new String[]{"empid"};
		return super.findByEntity(searchEntity, excludeColumn, isExact);
	}
	
	/**
	 * 
	 * <li>说明：人员的QBE查询,非分页方式
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-7-29
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param emp 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 人员对象集合
	 * 调用示例:
	 *  OmEmployee emp = new OmEmployee();
	 *  String [] excludeColumn = new String[]{"empid"};
	 *	emp.setempcode("4");
	 *	Order order = Order.asc("empid");
	 *	Order [] or = new Order[1];
	 *	or[0] = order;
	 *  list = omEmployeeManager.findByEntity(emp, or);
	 */
	@SuppressWarnings("unchecked")
	public List <OmEmployee> findByEntity(OmEmployee emp, Order [] orders, Boolean isExact){
		String [] excludeColumn = new String[]{"empid"};
		return super.findByEntity(emp, excludeColumn, orders, isExact);
	}
	
	/**
	 * 
	 * <li>说明：根据参数，查询人员信息及其对应组织机构的orgDegree路径
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-8
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param maxOrgDegree 该参数指定查询至最高级别的单位
	 * @param sql 对人员信息的过滤SQL 例如：empid in (104,107),或者 empName like '小凡%'等
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Page<OmEmployee> findEmpAndOrgPath(String maxOrgDegree, String sqlParam, Integer start,Integer limit){
		if(StringUtil.isNullOrBlank(maxOrgDegree)) throw new RuntimeException("参数异常: 入参maxOrgDegree为空");
		//获取无条件查询SQL 
		String sql = SqlMapUtil.getSql(XMLNAME_EMP.concat("findEmpAndOrgPath"));
		if(StringUtil.isNullOrBlank(sql)) throw new RuntimeException("SQL语句读取异常");
		//添加查询条件
		sql = sql.replaceFirst("[?]", maxOrgDegree);
		if(!StringUtil.isNullOrBlank(sql)){
			if(StringUtil.isNullOrBlank(sqlParam)){
				sql = sql.replaceFirst("[?]", "");
			} else {
				sql = sql.replaceFirst("[?]", " and ".concat(sqlParam));
			}
		}
		String totalSql = "select count(*) as \"rowcount\" from (".concat(sql).concat(")");
		return super.findPageList(totalSql, sql, start, limit, null, null);  //不使用缓存
	}
	
}

