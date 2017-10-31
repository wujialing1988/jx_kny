/**
 * <li>文件名：OmOrganizationManager.java
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-16
 * <li>修改人： 
 * <li>修改日期：
 */
package com.yunda.frame.yhgl.manager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.type.Type;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;
import com.yunda.frame.yhgl.entity.OmPosition;

/**
 * <li>类型名称：机构服务类
 * <li>说明：
 * <li>创建人： 曾锤鑫
 * <li>创建日期：2011-2-16
 * <li>修改人：
 * <li>修改日期：
 */
public class OmOrganizationManager extends
		JXBaseManager<OmOrganization, OmOrganization> implements IOmOrganizationManager {

	private OmEmployeeManager omEmployeeManager;
	
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
	 * <li>方法名：findOmOrganizationList
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：List<OmOrganization>
	 * <li>说明：根据父Id来查找子机构
	 * <li>创建人：李茂生
	 * <li>创建日期：2011-4-2
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<OmOrganization> findOmOrganizationList(long parentorgid) throws BusinessException {
		String hql="from OmOrganization m where 1=1 ";
		if(parentorgid!=-1){
			hql+="and m.parentorgid="+parentorgid+"";
		}else{
			hql+="and m.parentorgid is null";
		}
		return daoUtils.find(hql.toString());
	}
	
	/**
	 * 
	 * <li>方法名：findLoginOmOrgList
	 * <li>@param orgid
	 * <li>@param 需要找出的级别，OM_ORGANIZATION的orglevel为单位级别
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：List<OmOrganization>
	 * <li>说明：根据传入的级别返回当前操作员所属段、局、部信息，-1表示返回所有
	 * <li>创建人：田华
	 * <li>创建日期：2011-7-4
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public List<OmOrganization> findLoginOmOrgList(long orgid,String orglevel) throws BusinessException {
		List<OmOrganization> orglist = null;
		String sql="SELECT m.ORGID,m.ORGCODE,m.ORGNAME,m.ORGLEVEL,m.PARENTORGID,M.ORGDEGREE FROM OM_ORGANIZATION m START WITH ORGID='"+orgid+"' CONNECT BY PRIOR m.PARENTORGID=m.ORGID";
		try{
			List list = daoUtils.executeSqlQuery(sql);
			if(list!=null&&list.size()>0){
				orglist = new ArrayList<OmOrganization>();
				OmOrganization org = null;
				Object[] obj = null;
				for(int i = 0;i<list.size();i++){
					obj = (Object[]) list.get(i);
					if("-1".equals(orglevel)){
						org = new OmOrganization();
						org.setOrgid(Long.valueOf(obj[0]+""));
						org.setOrgcode(obj[1]+"");
						org.setOrgname(obj[2]+"");
						org.setOrglevel(obj[3]!=null?Long.valueOf(obj[3]+""):null);
						org.setOrgdegree(obj[5]+"");
						org.setParentorgid(Long.valueOf(obj[4]+""));
						orglist.add(org);
					}else if(!StringUtils.isBlank(orglevel)&&obj[5]!=null&&orglevel.equals(obj[5])){
						org = new OmOrganization();
						obj = (Object[]) list.get(i);
						org.setOrgid(Long.valueOf(obj[0]+""));
						org.setOrgcode(obj[1]+"");
						org.setOrgname(obj[2]+"");
						org.setOrglevel(obj[3]!=null?Long.valueOf(obj[3]+""):null);
						org.setOrgdegree(obj[5]+"");
						org.setParentorgid(Long.valueOf(obj[4]+""));
						orglist.add(org);
					}
				}
			}
		}catch(Exception e){
			return orglist;
		}
		return orglist;
	}
	
	/**
	 * <li>方法名：findOmPostionByorgid
	 * <li>@param orgid
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：List<OmPosition>
	 * <li>说明：查找机构下的岗位
	 * <li>创建人：李茂生
	 * <li>创建日期：2011-4-18
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<OmPosition>	 findOmPostionByorgid(Long orgid) throws BusinessException{
		StringBuffer hql = new StringBuffer();
		hql.append("from OmPosition m  where m.orgid  ="+orgid+""); 
		return daoUtils.find(hql.toString());
	}
	
	/**
	 * <li>方法名：findOmPostionByorgid
	 * <li>@param orgid
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：List<OmPosition>
	 * <li>说明：根据orgcode查找数据
	 * <li>创建人：罗鑫
	 * <li>创建日期：2011-4-18
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public OmOrganization findOrgForCode(String orgcode) throws BusinessException{
		//String hql = "from OmOrganization where orgcode = '"+orgcode+"'";
		//return (OmOrganization) daoUtils.findSingle(hql);
		return this.findByCode(orgcode);
	}
	
	/**
	 * <li>方法名：findOmEmployeebyPositionid
	 * <li>@param positionid
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：List<OmEmployee>
	 * <li>说明：
	 * <li>创建人：李茂生
	 * <li>创建日期：2011-4-2
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<OmEmployee> findOmEmployeeByOrgid(Long orgid)throws BusinessException{
		StringBuffer hql = new StringBuffer();
		hql.append(" from OmEmployee e  where e.orgid = '"+orgid+"' ");
		hql.append(" and e.empid not in(");
		hql.append(" select m.id.empid from OmEmpposition m,OmPosition p" +
				   " where m.id.positionid = p.positionid " +
				   " and p.orgid = '"+ orgid +"')");		
		return daoUtils.find(hql.toString());
	}
	/***
	 * 
	 * <li>方法名：findOmPositionnList
	 * <li>@param manaposi
	 * <li>@param type  
	 * <li>@return
	 * <li>@throws BusinessException
	 * <li>返回类型：List<OmPosition>
	 * <li>说明：查找某个岗位下的子岗位
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
	 * <li>方法名：getParentById
	 * <li>@param parentId
	 * <li>@return
	 * <li>返回类型：OmOrganization
	 * <li>说明：根据父ID递归查询【单位ID】
	 * <li>创建人：王开强
	 * <li>创建日期：2011-6-7
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public OmOrganization getOrgById(long parentId){
		OmOrganization  omo = null;
		String hql = "from OmOrganization where parentorgid = " + parentId;
		omo = (OmOrganization)daoUtils.find(hql).get(0);
		if(omo == null || omo.getOrglevel() == 3)return omo;
		else return getOrgById(omo.getParentorgid());
	}
	
	/**
	 * <li>方法名：isDepartment
	 * <li>@param omoId
	 * <li>@return
	 * <li>返回类型：boolean
	 * <li>说明：判断是否选择部门（部门Level为5）
	 * <li>创建人：王开强
	 * <li>创建日期：2011-6-8
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public boolean isDepartment(long omoId){
		OmOrganization  omo = null;
		long orglevel;
		omo = (OmOrganization)getDaoUtils().get(omoId, OmOrganization.class);
		/**
		System.out.println("omoId = " + omoId);
		System.out.println("omo is null??" + (omo == null));
		System.out.println("omo.getOrglevel() ==??" + omo.getOrglevel());
		*/
		orglevel = omo.getOrglevel() == null ? 0l : omo.getOrglevel();
		return orglevel == 5;
	}
	
	/**
	 * 
	 * <li>方法名：getEmpAllOrg
	 * <li>@param orgid
	 * <li>@return
	 * <li>返回类型：List<Object[]>
	 * <li>说明：
	 * <li>创建人：罗鑫
	 * <li>创建日期：2012-2-14
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getEmpAllOrg(String orgid){
		String sql = "select a.orgid,a.orgname,a.orgdegree,a.ORGLEVEL from OM_ORGANIZATION a start with  a.orgid='"+orgid+"' connect by prior a.parentorgid = a.orgid";
		return (List<Object[]>)daoUtils.executeSqlQuery(sql);
	}
	

	public OmEmployeeManager getOmEmployeeManager() {
		return omEmployeeManager;
	}

	public void setOmEmployeeManager(OmEmployeeManager omEmployeeManager) {
		this.omEmployeeManager = omEmployeeManager;
	}

	/*
	 *     业务数据统一接口方法
	 */
	/**
	 * <br/><li>说明： 查询组织机构树的根节点.
	 * <br/><li><font color=red>注*：将取得组织机构表中所有parentorgid为空并且机构状态为"running",可能会有多个</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @return 组织机构实体列表
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public List <OmOrganization> findRoot(){
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findOrgRoot"));
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.find(true, hql); //利用查询缓存
		} else {
			return daoUtils.getHibernateTemplate().find(hql); //不使用查询缓存
		}
	}
	
	/**
	 * <br/><li>说明： 根据组织机构Id查询唯一对应的组织机构.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构id
	 * @return 组织机构实体
	 */
	public OmOrganization getModelById(Long orgId){
		return (OmOrganization)daoUtils.getHibernateTemplate().get(OmOrganization.class, orgId); //利用查询缓存
	}
	
	/**
	 * <br/><li>说明：根据多个组织机构Id,查询与对应的组织机构
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个orgId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgIds 组织机构id字符串
	 * @return 组织机构实体列表
	 */
	@SuppressWarnings("unchecked")
	public List <OmOrganization> findByIds(String orgIds){
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByIds")).replace("?", orgIds);  
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.find(true, hql); //利用查询缓存
		} else {
			return daoUtils.getHibernateTemplate().find(hql);  //不利用查询缓存
		}
	}
	
	/**
	 * <br/><li>说明：根据多个组织机构Id,查询与对应的组织机构
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个orgId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgIds 组织机构id字符串
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 组织机构实体列表
	 */
	public Page findByIds(String orgIds,Integer start,Integer limit){
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByIds")).replace("?", orgIds);
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit);  //利用查询缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不利用查询缓存
		}
	}
	
	/**
	 * <br/><li>说明： 根据组织机构的编号精确查找与其对应的唯一组织机构
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgCode 组织机构编号
	 * @return 组织机构实体
	 */
	@SuppressWarnings("unchecked")
	public OmOrganization findByCode(String orgCode){
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByField"));
		hql = hql.replaceFirst("[?]", "orgcode").replaceFirst("[?]", orgCode);
		List<OmOrganization> list = null;
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
	 * <br/><li>说明： 根据组织机构编号进行模糊查询(like '%...%'方式),并提供分页支持
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgCode 组织机构编号
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 带分页的组织机构实体列表
	 */
	public Page findByCode(String orgCode,Integer start,Integer limit){
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByCode")).replace("?", "'%"+orgCode+"%'");
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit);  //使用缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}	
	}
	
	/**
	 * <br/><li>说明： 根据orgseq查询唯一对应的组织机构. orgseq的格式示例: ".0.7.146"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgseq 组织机构Id序列
	 * @return 组织机构实体
	 */
	@SuppressWarnings("unchecked")
	public OmOrganization findBySeq(String orgseq){
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByField"));
		hql = hql.replaceFirst("[?]", "orgseq").replaceFirst("[?]", orgseq);
		List<OmOrganization> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql);  //不使用缓存
		}
		if(list != null && list.size() > 0)	return list.get(0);
		else return null;
	}
	
	/**
	 * <br/><li>说明： 获得用户可管理的组织机构列表,并提供分页支持
	 * <br/><li><font color=red>注*：预留接口,未实现</font>
	 * <br/><li>创建人：
	 * <br/><li>创建日期：
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param userId 用户Id
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 组织机构分页列表
	 */
	public Page findUserManager(Long userId,Integer start,Integer limit){
		return null;
	}
	
	/**
	 * <br/><li>说明： 查找入参Orgid的下一级组织机构信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构Id
	 * @return 组织机构实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmOrganization> findChildsById(Long orgId) {
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByField"));
		hql = hql.replaceFirst("[?]", "parentorgid").replaceFirst("[?]", String.valueOf(orgId));
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.find(true, hql);
		} else {
			return daoUtils.getHibernateTemplate().find(hql);
		}
	}
	
	/**
	 * <br/><li>说明： 根据组织机构ID,查找其下一级组织机构信息,无running约束
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-10-31
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构Id
	 * @return 组织机构实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmOrganization> findAllChildsById(Long orgId){
		String hql = "from OmOrganization where parentorgid = "+orgId;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.find(true, hql);
		} else {
			return daoUtils.getHibernateTemplate().find(hql);
		}
	}
	
	/**
	 * <br/><li>说明： 以分页方式查找入参Orgid的下一级组织机构信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构Id
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 带分页的组织机构实体列表
	 */
	@SuppressWarnings("unchecked")
	public Page findChildsById(Long orgId,Integer start,Integer limit){
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByField"));
		hql = hql.replaceFirst("[?]", "parentorgid").replaceFirst("[?]", String.valueOf(orgId));
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit);   //使用缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}
	}
	
	/**
	 * <br/><li>说明：获得人员Id入参直属的组织机构
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param empId 人员id
	 * @return 组织机构实体
	 */
	@SuppressWarnings("unchecked")
	public OmOrganization findByEmpId(Long empId){
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByEmpId"));
		Object[] param = new Object[]{empId};
		List <OmOrganization> list = null;
		//是否利用查询缓存
//		if(isJcglQueryCacheEnabled()){
//			list = daoUtils.find(true, hql, param);  //使用缓存
//		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
//		}
		if (list != null && list.size() > 0)	return (OmOrganization) list.get(0);
		else return null;
	}
	
	/**
	 * <br/><li>说明：获得操作员Id入参直属的组织机构
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param operatorid 操作员id
	 * @return 组织机构实体
	 */
	@SuppressWarnings("unchecked")
	public OmOrganization findByOperator(Long operatorid){
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByOperator"));
		Object[] param = new Object[]{operatorid};
		List<OmOrganization> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param); //不使用缓存
		}
		if (list != null && list.size() > 0)	return (OmOrganization) list.get(0);
		else return null;
	}
	
	/**
	 * <br/><li>说明：查询入参机构Id下的某个指定级别的组织机构
	 * <br/><li>参数示例1: orgId=0,degree=oversea,表示查询铁道部下所有机务段;
	 * <br/><li>参数示例2: orgId=141,degree=tream,表示查询天津基地下所有班组.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构Id
	 * @param degree 组织机构级别
	 * @return 组织机构实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmOrganization> findByDegree(Long orgId, String degree){
		OmOrganization org = getModelById(orgId);
		if (org == null) return null;
		String orgseq = org.getOrgseq();
		if (orgseq == null || "".equals(orgseq)	|| orgseq.equalsIgnoreCase("null")) return null;
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByDegree"));
		Object[] param = new Object[] { orgseq.concat("%"), orgseq, degree };
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.find(true, hql, param);  //使用缓存
		} else {
			return daoUtils.getHibernateTemplate().find(hql,param); //不使用缓存
		}
	}
	
	/**
	 * <br/><li>说明：查询入参机构Id下的某个指定级别的组织机构,并提供分页支持
	 * <br/><li>参数示例1: orgId=0,degree=oversea,表示查询铁道部下所有机务段;
	 * <br/><li>参数示例2: orgId=141,degree=tream,表示查询天津基地下所有班组.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构Id
	 * @param degree 组织机构级别
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @return 组织机构实体列表
	 */
	public Page findByDegree(Long orgId, String degree, Integer start,Integer limit){
		OmOrganization org = getModelById(orgId);
		if (org == null)	return null;
		String orgseq = org.getOrgseq();
		if (orgseq == null || "".equals(orgseq)	|| orgseq.equalsIgnoreCase("null")) 	return null;
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByDegree"));
		hql = hql.replaceFirst("[?]", "'".concat(orgseq).concat("%").concat("'"));
		hql = hql.replaceFirst("[?]", "'".concat(orgseq).concat("'"));
		hql = hql.replaceFirst("[?]", "'".concat(degree).concat("'"));
		String totalHql = "select count(*) ".concat(hql);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalHql, hql, start, limit); //使用缓存
		} else {
			return super.findPageList(totalHql, hql, start, limit); //不使用缓存
		}
	}
	/**
	 * <br/><li>说明： 查询某级机构下的子机构,可通过treeLevel参数指定查询相对于orgId作为根节点的第几层
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构Id
	 * @param treeLevel 从当前orgid开始计算, 向下查询多少层
	 * @param hasSelf 是否包含参数OrgId本身
	 * @return 组织机构实体列表
	 */
	public List<OmOrganization> findTreeByDynamicLevel(Long orgId, Integer treeLevel, boolean hasSelf){
		return findTreeForTargetLevel(orgId,treeLevel,null,hasSelf);
	}
	
	/**
	 * <br/><li>说明： 查询某级机构下的子机构. 可通过orgLevel参数指定查询到哪一层级
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构Id
	 * @param orgLevel 与orglevel字段值对应
	 * @param hasSelf 是否包含参数OrgId本身
	 * @return 组织机构实体列表
	 */
	public List<OmOrganization> findTreeByStaticLevel(Long orgId, Integer orgLevel, boolean hasSelf){
		return findTreeForTargetLevel(orgId,null,orgLevel,hasSelf);
	}
	
	/**
	 * <br/><li>说明：查询某级机构下的子机构,可指定查询到第几层
	 * <br/><li><font color=red>注*：downwardLevel参数和targetLevel参数只使用1个, 另一个传入null</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构Id
	 * @param treeLevel 从当前orgid开始计算, 向下查询多少层
	 * @param orgLevel 从根节点开始计算,共查询多少层
	 * @param hasSelf 是否包含参数OrgId本身
	 * @return 组织机构实体列表
	 */
	@SuppressWarnings("unchecked")
	private List<OmOrganization> findTreeForTargetLevel(Long orgId, Integer treeLevel, Integer orgLevel, boolean hasSelf){
		String sql = null;
		//如果orgId为空,则从根开始查;否则,从指定位置开始查
		sql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findTreeByLevel")).replace("#orgId", String.valueOf(orgId)==null?"0":String.valueOf(orgId));
		if(treeLevel!=null){  //从当前orgid位置开始,向下查"downwardLevel"层 <动态Level>
			sql = sql.replace("#level", " and level <=".concat(String.valueOf(treeLevel)));
		} else if(orgLevel!=null){ //从根节点位置开始,查询到第几级 <静态Level>
			sql = sql.replace("#orglevel", (" and orglevel <=".concat(String.valueOf(orgLevel))));
		} 
		sql = sql.replace("#orglevel",""); //消除查询条件标识
		sql = sql.replace("#level",""); //消除查询条件标识
		if(hasSelf){ //如果包含当前orgid,则消除条件标识
			sql = sql.replace("#currentOrg", "");
		} else {
			sql = sql.replace("#currentOrg", " and \"orgid\" <> ".concat(String.valueOf(orgId)));
		}
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.cacheSqlQueryEntity(sql, OmOrganization.class); //使用缓存
		} else {
			return daoUtils.executeSqlQueryEntity(sql, OmOrganization.class); //不使用缓存
		}
	}
	
	/**
	 * <br/><li>说明： 递归查询入参机构id下属所有子机构
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构Id
	 * @param hasSelf 是否包含参数OrgId本身
	 * @return 组织机构实体列表
	 */
	@SuppressWarnings("unchecked")
	public List<OmOrganization> findAllChilds(Long orgId, boolean hasSelf){
		String sql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findAllChilds"));
		String orgIdStr = String.valueOf(orgId)==null?"0":String.valueOf(orgId);
		sql = sql.replaceFirst("[?]", hasSelf ? "" : ("and \"orgid\"<>".concat(orgIdStr)) ); //处理是否包含当前节点本身
		sql = sql.replaceFirst("[?]", orgIdStr);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.cacheSqlQueryEntity(sql, OmOrganization.class); //使用缓存
		} else {
			return daoUtils.executeSqlQueryEntity(sql, OmOrganization.class); //不使用缓存 
		}
	}

	/**
	 * <br/><li>说明： 递归查询入参机构id下属所有子机构,并提供分页支持
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构Id
	 * @param hasSelf 是否包含参数OrgId本身
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @return 组织机构实体列表
	 */
	public Page findAllChilds(Long orgId, boolean hasSelf, Integer start,Integer limit){
		String sql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findAllChilds"));
		String orgIdStr = String.valueOf(orgId)==null?"0":String.valueOf(orgId);
		sql = sql.replaceFirst("[?]", hasSelf ? "" : ("and \"orgid\"<>".concat(orgIdStr)) ); //处理是否包含当前节点本身
		sql = sql.replaceFirst("[?]", orgIdStr);
		String totalSql = sql.replace("select *", "select count(*) as \"rowcount\"");
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return cachePageList(totalSql,sql, start, limit,OmOrganization.class); //使用缓存
		} else {
			return super.findPageList(totalSql, sql, start, limit, null, null);  //不使用缓存
		}
	}
	
	/**
	 * <br/><li>说明：根据入参OrgId,查询其之上各级组织机构序列,如指定了degree参数,则返回该参数指定的级别的单位
	 * <br/><li>参数示例1: orgId=242,degree="branch", 表示查询"机电一班"属于哪个铁路局
	 * <br/><li>参数示例2: orgId=242,degree="oversea", 表示查询"机电一班"属于哪个机务段或检修公司
	 * <br/><li>参数示例3: orgId=242,degree="branch,oversea",表示查询"机电一班"属于哪个局下的哪个段
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构id
	 * @param degree 目标机构的级别
	 * @return 组织机构实体列表
	 */                         
	@SuppressWarnings("unchecked")
	public List<OmOrganization> findUpperByDegree(Long orgId, String degree){
		String sql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findUpperByDegree"));
		String orgIdStr = String.valueOf(orgId)==null?"0":String.valueOf(orgId);
		if(!StringUtil.isNullOrBlank(degree)){
			if(degree.indexOf(",")!=-1){
				String _t = degree.replace(",", "','");
				sql = sql.replaceFirst("[?]", "and orgdegree in ('".concat(_t).concat("') "));  //指定了以","分割的多个组织机构或者班组
			} else {
				sql = sql.replaceFirst("[?]", "and orgdegree = '".concat(degree).concat("' ")); //指定了查询到哪级组织机构或班组
			}
		} else {
			sql = sql.replaceFirst("[?]", "and rownum <2 "); //未指定单位级别,则查询当前组织机构或班组
		} 
		sql = sql.replaceFirst("[?]", orgIdStr);
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.cacheSqlQueryEntity(sql, OmOrganization.class);
		} else {
			return daoUtils.executeSqlQueryEntity(sql, OmOrganization.class);
		}
	}
	
	/**
	 * <br/><li>说明： 构建组织机构树,为组织机构控件所调用
	 * <br/><li>参数示例1: orgId=361,ultimateOrgDegree="plant",  表示orgname显示为"机电一班/制造部"
	 * <br/><li>参数示例2: orgId=361,ultimateOrgDegree="oversea",表示orgname显示为"机电一班/制造部/天津电力机车有限公司"
	 * <br/><li>参数示例3: orgId=361,ultimateOrgDegree="branch", 表示orgname显示为"机电一班/制造部/天津电力机车有限公司/北京铁路局"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构id
	 * @param ultimateOrgDegree 组织机构名称Path的最终显示层级
	 * @return 组织机构Map列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map> findOrgTree(Long orgId, String ultimateOrgDegree){
		List <OmOrganization> list = this.findChildsById(orgId); //查询下级单位
		List <Map> children = null;
		
		if( list != null ){
			children = new ArrayList<Map>();
			Map nodeMap = null;
			
			for ( OmOrganization org : list ) {
				nodeMap = new HashMap();

				Boolean IsLeaf = "y".equals(org.getIsleaf()) || "".equals(org.getIsleaf()) ? true : false;
	            nodeMap.put("id", org.getOrgid());
	            nodeMap.put("text", org.getOrgname());
	            nodeMap.put("orgcode", org.getOrgcode());
	            nodeMap.put("orgname", getFullName(org.getOrgid(),ultimateOrgDegree));
	            nodeMap.put("isleaf", org.getIsleaf());
	            nodeMap.put("orgtype", org.getOrgtype());
	            nodeMap.put("leaf", IsLeaf);
	            nodeMap.put("orgseq", org.getOrgseq());
	            nodeMap.put("orgaddr", org.getOrgaddr());
	            nodeMap.put("sortno", org.getSortno());
                nodeMap.put("orgdegree", org.getOrgdegree());
	            children.add(nodeMap);
			}
		}
		return children;
	}
	
	/**
	 * <br/><li>说明：根据自定义HQL,构建组织机构树,为组织机构控件所调用
	 * <br/><li>参数示例1: orgId=361,ultimateOrgDegree="plant",  表示orgname显示为"机电一班/制造部"
	 * <br/><li>参数示例2: orgId=361,ultimateOrgDegree="oversea",表示orgname显示为"机电一班/制造部/天津电力机车有限公司"
	 * <br/><li>参数示例3: orgId=361,ultimateOrgDegree="branch", 表示orgname显示为"机电一班/制造部/天津电力机车有限公司/北京铁路局"
	 * <br/><li>参数示例4: orgId=141,queryHql="orgcode like 'JSB%'",ultimateOrgDegree="oversea",表示查询机构编号为JSB开头的北京铁路局的直属下级单位,orgname显示为"机电一班/制造部/天津电力机车有限公司"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param orgId 组织机构id
	 * @param queryHql 查询Hql字符串
	 * @param ultimateOrgDegree 组织机构名称Path的最终显示层级
	 * @return 组织机构Map列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map> findOrgTree(Long orgId, String queryHql, String ultimateOrgDegree, String isChecked){
		List <OmOrganization> list = null;
		String hql = "from OmOrganization where parentorgid=? and status = 'running' ";
		//如果自定义了查询语句
		if (!StringUtil.isNullOrBlank(queryHql)) {
			hql = queryHql.concat(" and parentorgid = "+orgId);
			//是否利用查询缓存
			if(isJcglQueryCacheEnabled()){
				list = daoUtils.find(true, hql); //使用缓存查询
			} else {
				list = daoUtils.getHibernateTemplate().find(hql); //不使用缓存查询
			}
		} else {
			list = this.findChildsById(orgId); //查询下级单位
		}
		List <Map> children = null;
		
		if( list != null ){
			children = new ArrayList<Map>();
			Map nodeMap = null;
			Boolean IsLeaf = false;
			
	        for (OmOrganization org : list) {
	        	IsLeaf = true;
	        	List <OmOrganization> childList = this.findChildsById(org.getOrgid()); //查询下级单位
	            if(childList != null && childList.size() > 0) {
	            	IsLeaf = false;
	            }
	            nodeMap = new HashMap();
	            nodeMap.put("id", org.getOrgid());
	            nodeMap.put("text", org.getOrgname());
	            nodeMap.put("orgcode", org.getOrgcode());
	            nodeMap.put("orgname", getFullName(true, ultimateOrgDegree, org.getOrgid(), org.getOrgname()));
	            nodeMap.put("isleaf", IsLeaf);
	            nodeMap.put("orgtype", org.getOrgtype());
	            nodeMap.put("leaf", IsLeaf);
	            nodeMap.put("orgseq", org.getOrgseq());
	            nodeMap.put("orgaddr", org.getOrgaddr());
	            nodeMap.put("sortno", org.getSortno());
	            if(!StringUtil.isNullOrBlank(isChecked)){
	            	nodeMap.put("checked", false);
	            }
	            children.add(nodeMap);
	        }
		}
        return children;
	}
	
	/**
	 * <br/><li>说明：根据自定义HQL,构建组织机构树,为组织机构控件所调用
	 * <br/><li><font color=red>注*：通常用于关联数据字典,例如查询承修部门</font>
	 * <br/><li>查询语句结构: "from OmOrganization where status = 'running' " + queryHql
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
     * @param queryHql 查询Hql字符串
     * @param showGrandChilds 是否需要按照属性结构显示更下层,用于控制树控件的展开按钮.
     * @return 组织机构Map列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map> findOrgTree(String queryHql,boolean showGrandChilds,String isChecked){
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findRunningOrg"));
		if (!StringUtil.isNullOrBlank(queryHql)) {
			hql = hql.concat(queryHql);
	    }
		List <OmOrganization> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql);  //使用缓存查询
		} else {
			list = daoUtils.getHibernateTemplate().find(hql); //不使用缓存查询
		}
        List<Map> children = null;
        
        if( list != null ){
        	children = new ArrayList<Map>();
	        Boolean IsLeaf = false;
	        Map nodeMap = null;
	        
	        for (OmOrganization org : list) {
	        	 nodeMap = new HashMap();
	        	//是否按照树型结构继续向下检索
	        	if(showGrandChilds){
	        		//继续向下层检索
	        		IsLeaf = "y".equals(org.getIsleaf()) || "".equals(org.getIsleaf()) ? true : false;
	        		nodeMap.put("isleaf", org.getIsleaf()); 
	                nodeMap.put("leaf", IsLeaf);
	        	} else {
	        		//只获取当前层次,默认当前层次为叶子节点
	        		nodeMap.put("isleaf", "y");
	        		nodeMap.put("leaf", true);
	        	}
	            nodeMap.put("id", org.getOrgid());
	            nodeMap.put("text", org.getOrgname());
	            nodeMap.put("orgcode", org.getOrgcode());
	            nodeMap.put("orgname", org.getOrgname());
	            nodeMap.put("orgtype", org.getOrgtype());
	            nodeMap.put("orgseq", org.getOrgseq());
	            nodeMap.put("orgaddr", org.getOrgaddr());
	            nodeMap.put("sortno", org.getSortno());
	            if(!StringUtil.isNullOrBlank(isChecked)){
	            	nodeMap.put("checked", false);
	            }
	            children.add(nodeMap);
	        }
        }
        return children;
	}
	
	/**
	 * <br/><li>说明： 组织机构树控件业务方法
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
     * @param orgId 组织机构Id
     * @param orgdegree 获取显示级别，如queryHql为[degree]plant|fullName，则取plant值
     * @param fullNameDegree 组织机构路径的显示层级
     * @return 查询列表所需List列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map> findSomeLevelOrgTree(Long orgId,String orgdegree,String fullNameDegree,String isChecked){
		boolean fullName = false;
        if(orgdegree.indexOf("|fullName") != -1){
            orgdegree = orgdegree.substring(orgdegree.indexOf("]")+1,orgdegree.indexOf("|"));//获取显示级别，如queryHql为[degree]plant|fullName，则取plant值
            fullName = true;
        }else{
            orgdegree = orgdegree.substring(orgdegree.indexOf("]")+1);//获取显示级别，如queryHql为[degree]plant，则取plant值
        }
        //queryHql配置是：类似[degree]或[degree]|fullName的
        String sql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findSomeLevelOrgTree1"));
        sql = sql.replaceFirst("[?]", String.valueOf(orgId));
        //queryHql配置是：类似[degree]plant或[degree]plant|fullName
        if(!StringUtil.isNullOrBlank(orgdegree) && (orgdegree.equals(Constants.DEPARTMENT_LEVEL) 
        		|| orgdegree.equals(Constants.TEAM_LEVEL) || orgdegree.equals(Constants.SEGMENT_LEVEL))){
            sql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findSomeLevelOrgTree2"));
            sql = sql.replaceFirst("[?]", orgdegree).replaceFirst("[?]", String.valueOf(orgId));
        }
        List<Object[]> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
	        Map <String,Type>colType = new LinkedHashMap<String,Type>();
	        colType.put("orgid", Hibernate.LONG);
	        colType.put("orgname", Hibernate.STRING);
	        colType.put("orgdegree", Hibernate.STRING);
	        colType.put("orgseq", Hibernate.STRING);
	        colType.put("equalsDegreeCount", Hibernate.INTEGER);
	        colType.put("orgcode", Hibernate.STRING);
	        list = daoUtils.cacheExecuteSqlQuery(sql,colType);
		} else {
			list = daoUtils.executeSqlQuery(sql); 
		}
        List<Map> children = new ArrayList<Map>();
        
        for (Object[] orgx : list) {
            Map nodeMap = new HashMap();
            nodeMap.put("id", orgx[0]);
            nodeMap.put("orgid", orgx[0]);
            nodeMap.put("text", orgx[1]);
            nodeMap.put("orgdegree", orgx[2]);
            nodeMap.put("orgseq", orgx[3]);
            nodeMap.put("orgcode", orgx[5]);
            boolean IsLeaf = false;
            
            //queryHql配置是：[degree]plant或[degree]plant|fullName
            if(!StringUtil.isNullOrBlank(orgdegree) && (orgdegree.equals("plant") || orgdegree.equals("tream") || orgdegree.equals("oversea"))){
                //如已对应显示级别，则看是否下级有对应显示级别
                if(orgdegree.equals(orgx[2])){
                    if(orgx[4] != null && Integer.valueOf(orgx[4].toString()) < 2){
                        IsLeaf = true;
                    }
                    nodeMap.put("leaf", IsLeaf);
                    nodeMap.put("orgname", getFullName(fullName, fullNameDegree, Long.valueOf(orgx[0].toString()), orgx[1].toString()));
                }
                //如无对应显示级别，则看是否下级有对应显示级别
                else if(orgx[4] != null ){
                	if(Integer.valueOf(orgx[4].toString()) == 0)
                		IsLeaf = true;
                    nodeMap.put("disabled", true);
                    nodeMap.put("leaf", IsLeaf);
                    nodeMap.put("orgname", getFullName(fullName, fullNameDegree, Long.valueOf(orgx[0].toString()), orgx[1].toString()));
                }            
            }
            //queryHql配置是：[degree]或[degree]|fullName的
            else{ 
                //判断是否叶子节点
                if(orgx[4] == null || Integer.valueOf(orgx[4].toString()) < 1){                
                    IsLeaf = true;
                }
                nodeMap.put("leaf", IsLeaf);
                nodeMap.put("orgname", getFullName(fullName, fullNameDegree, Long.valueOf(orgx[0].toString()), orgx[1].toString()));
            }
            if(!StringUtil.isNullOrBlank(isChecked)){
            	nodeMap.put("checked", false);
            }
            children.add(nodeMap);
        }        
        return children;
	}
	
	/**
     * <br/><li>说明：根据登录人所属部门id获取与承修部门数据字典匹配的机构实体对象
     * <br/><li>创建人：程锐
     * <br/><li>创建日期：2013-5-17
     * <br/><li>修改人： 
     * <br/><li>修改日期：
     * <br/><li>修改内容：
     * @param orgId 登录人所属部门id
     * @return 组织机构实体对象
     */
    @SuppressWarnings("unchecked")
	public OmOrganization getUnderTakeByEmp(String orgId){
    	String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("getUnderTakeByEmp"));
    	Object[] param = null;
    	if(!StringUtil.isNullOrBlank(orgId)){
    		hql = hql.concat("and o.orgid = ?");
    		param = new Object[] { Long.valueOf(orgId) };
    	}
		List<OmOrganization> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param); //使用缓存查询
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param); //不使用缓存查询
		}
		if (list != null && list.size() > 0)	return (OmOrganization) list.get(0);
		else return null;
    }
    
    /**
     * <br/><li>说明：根据orgid获取单位全名，格式如:重庆西/重庆机务段/成都铁路局，最大单位到铁道部下一级
     * <br/><li>创建人：程锐
     * <br/><li>创建日期：2012-10-10
     * <br/><li>修改人：
     * <br/><li>修改日期：
     * <br/><li>修改内容：
     * @param orgId 单位orgid
     * @param orgdegree 显示组织全名Path时最大到哪一级
     * @return 单位全名 格式如:重庆西/重庆机务段/成都铁路局，最大单位到铁道部下一级
     */
    @SuppressWarnings("unchecked")
	public String getFullName(Long orgId,String orgdegree){
    	StringBuffer retStr = new StringBuffer();
        if (orgId == 0) {
            return Constants.ORG_ROOT_NAME;
        }
        /*
         * 查询下级当前机构的上级各单位,并以倒序
         */
        String sql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findListForOrgNameSeqByOrgId"));
        sql = sql.replaceFirst("[?]", String.valueOf(orgId));
        
        List <OmOrganization> orgList = null;
    	//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			orgList = daoUtils.cacheSqlQueryEntity(sql, OmOrganization.class);
		} else {
			orgList = daoUtils.executeSqlQueryEntity(sql, OmOrganization.class);
		}
        for( OmOrganization org : orgList ){
        	retStr.append(org.getOrgname()).append("/");
        	if(orgdegree.equals(org.getOrgdegree())){
        		break;
       	 	}
        }
        return retStr.deleteCharAt(retStr.length()-1).toString();
    }
    
    /**
     * <li>说明：获取单位全名
     * <li>创建人：程锐
     * <li>创建日期：2013-6-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param fullName 是否获取全名
     * @param fullNameDegree 获取全名到哪一级
     * @param orgId 单位ID
     * @param orgName 单位名称
     * @return 单位全名
     */
    public String getFullName(boolean fullName, String fullNameDegree, Long orgId, String orgName){
        if(fullName){
            if(!StringUtil.isNullOrBlank(fullNameDegree)){ 
                return getFullName(orgId, fullNameDegree);
            }else{
                return getFullName(orgId, Constants.BUREAU_LEVEL);
            }
        }
        return orgName;
    }
    
	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-7-25
     * <br/><li>修改人：
	 * <br/><li>修改日期：
     * <br/><li>修改内容：
     * @param field 字段名
     * @param value 字段值
     * @return 组织机构实体列表
     */
	@SuppressWarnings("unchecked")
	public List<OmOrganization> findByField(String field, String value){
		if(field == null || value == null) throw new RuntimeException("参数异常: field或value为空");
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByField"));
		if(StringUtil.isNullOrBlank(hql))	return null;  //SQL语句读取异常
		hql = hql.replaceFirst("[?]", field).replaceFirst("[?]", String.valueOf(value));
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			return daoUtils.find(true, hql);
		} else {
			return daoUtils.getHibernateTemplate().find(hql); //不使用查询缓存
		}
	}
	
	/**
	 * <li>说明：根据组织机构Id数组,查询匹配的机构信息列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-7-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 组织机构Id数组
	 * @return 组织机构实体列表
	 */
	public List <OmOrganization> findByIdArys(Serializable... ids){
		List<OmOrganization> list = new ArrayList<OmOrganization>();
		for (Serializable orgId : ids) {
			list.add(this.getModelById(Long.parseLong(orgId.toString())));
		}
		return list;
	}
	
	/**
	 * 
	 * <li>说明：组织机构的QBE查询,支持分页方式
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-7-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity 查询实体
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
	public Page <OmOrganization> findByEntity(final SearchEntity <OmOrganization> searchEntity, final Boolean isExact){
		String [] excludeColumn = new String[]{"orgid"};
		return super.findByEntity(searchEntity, excludeColumn, isExact);
	}
	
	/**
	 * 
	 * <li>说明：组织机构的QBE查询,非分页方式
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-7-29
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param org 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 组织机构对象集合
	 * 调用示例:
	 *  OmOrganization org = new OmOrganization();
	 *  String [] excludeColumn = new String[]{"orgid"};
	 *	org.setOrgcode("4");
	 *	Order order = Order.asc("orgid");
	 *	Order [] or = new Order[1];
	 *	or[0] = order;
	 *  list = omOrganizationManager.findByEntity(org, excludeColumn, or, true);
	 */
	@SuppressWarnings("unchecked")
	public List <OmOrganization> findByEntity(OmOrganization org, Order [] orders, Boolean isExact){
		String [] excludeColumn = new String[]{"orgid"};
		return super.findByEntity(org, excludeColumn, orders, isExact);
	}

	/**
	 * <br/><li>说明：根据岗位ID获取其所属的组织机构
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param  positionid 岗位id
	 * @return 组织机构实体集合
	 */
	@SuppressWarnings("unchecked")
	public List<OmOrganization> findByPositionid(Long positionid) {
		if(positionid==null) throw new RuntimeException("参数异常: positionid为空");
		String hql = SqlMapUtil.getSql(XMLNAME_ORG.concat("findByPositionid"));
		if(StringUtil.isNullOrBlank(hql)) throw new RuntimeException("SQL语句读取异常");
		Object[] param = new Object[]{positionid};
		List <OmOrganization> list = null;
		//是否利用查询缓存
		if(isJcglQueryCacheEnabled()){
			list = daoUtils.find(true, hql, param);  //使用缓存
		} else {
			list = daoUtils.getHibernateTemplate().find(hql,param);  //不使用缓存
		}
		return list;
	}
}
