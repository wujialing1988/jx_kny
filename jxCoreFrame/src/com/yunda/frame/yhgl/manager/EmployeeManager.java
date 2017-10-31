package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.HqlUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.util.sqlmap.SqlMapUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmPosition;
import com.yunda.frame.yhgl.entity.UserData;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：谭诚
 * <li>创建日期：2013-11-11
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="employeeManager")
public class EmployeeManager extends JXBaseManager<OmEmployee,OmEmployee>{
	
	/** 人员查询接口 */
	@Resource(name="omEmployeeManager")
	private IOmEmployeeManager omEmployeeManager;
	
	/** 人员-机构业务类 */
	@Resource(name="empOrgManager")
	private EmpOrgManager empOrgManager;
	
	/** 人员-岗位业务类 */
	@Resource(name="empPositionManager")
	private EmpPositionManager empPositionManager;
	
	/** 人员-工作组业务类 */
	@Resource(name="empgroupManager")
	private EmpgroupManager empgroupManager;
	
	/** 操作员业务类 */
	@Resource(name="operatorManager")
	private OperatorManager operatorManager;
	
	/**
	 * <li>说明：更新前验证
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-1-2
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws BusinessException
	 */
	public String[] validateUpdate(OmEmployee t){
		if(!checkEmpCode(t.getEmpcode(), String.valueOf(t.getEmpid()))){
			return null;
		}
		return new String[]{"人员代码【 " + t.getEmpcode() + " 】已存在! 请重新设置。"};
	}
	
	/**
	 * <li>说明：查询是否存在人员编号重复现象
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-13
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param empCode 人员编号
	 * @return true/false 重复/不重复
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkEmpCode(String empCode, String empid){
		if(StringUtil.isNullOrBlank(empCode)) return true;
		if(StringUtil.isNullOrBlank(empid)) {
			//调用人员查询接口，根据人员编号查询是否存在编号相同的人员， 如已存在，则返回true
			if(omEmployeeManager.findByCode(empCode)==null){
				return false;
			}
		} else {
			//已经存在人员id，则表示是更新操作，需要检查除当前人员外，是否还有与之相同code的人员
			String hql = "from OmEmployee where empcode = '" + empCode + "' and empid <> " + empid;
			List <OmEmployee> list = this.daoUtils.getHibernateTemplate().find(hql);
			if(list == null || list.size()<1)
				return false;
		}
		return true;
	}
	
	/**
	 * <li>说明：机构人员树机构节点对应的下级人员列表查询
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param whereList 查询参数
	 * @param orderList 排序参数
	 * @param start 起始条数
	 * @param limit 每页条数
	 * @return 人员信息分页列表
	 * @throws 抛出异常列表
	 */
	public Page findEmployeeByOrg(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		Long orgid = 0L;
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				if(!StringUtil.isNullOrBlank(condition.getPropName())&&"orgid".equals(condition.getPropName())){
					orgid = Long.valueOf(String.valueOf(condition.getPropValue()));
				} 
				//如果参数中带有岗位条件，表示需查询岗位下直属人员
//				else if(!StringUtil.isNullOrBlank(condition.getPropName())&&"positionid".equals(condition.getPropName())){
//					searchParam.append(" and emp.empid in (select oep.empid from Om_Empposition oep where oep.positionid = ")
//					.append(String.valueOf(condition.getPropValue())).append(") ");
//				} 
				else {
					searchParam.append(" and ");
					searchParam.append(condition.getPropName()).append(" ");
					String sign = StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))
					? Condition.getCompare(1)+" '?' " : Condition.getCompare(8)+" '%?%' ";
					sign = sign.replace("?", String.valueOf(condition.getPropValue()));
					searchParam.append(sign);
				}
			}
		}
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by ").append(order.toString());
			}
		}
		//调用接口获取数据
//		return omEmployeeManager.findNoPositionByOrgId(orgid,searchParam.toString(),start,limit); //不包含机构下已分配岗位的人员
		return omEmployeeManager.findByOrgId(orgid,searchParam.toString(),start,limit); //包含机构下已分配岗位的人员
	}
	
	/**
	 * <li>说明：机构人员树机构节点对应的下级人员列表查询
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param whereList 查询参数
	 * @param orderList 排序参数
	 * @param start 起始条数
	 * @param limit 每页条数
	 * @return 人员信息分页列表
	 * @throws 抛出异常列表
	 */
	public Page findEmployeeByPos(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		Long positionid = 0L;
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				if(!StringUtil.isNullOrBlank(condition.getPropName())&&"positionid".equals(condition.getPropName())){
					positionid = Long.valueOf(String.valueOf(condition.getPropValue()));
				} else {
					searchParam.append(" and ");
					searchParam.append(condition.getPropName()).append(" ");
					String sign = StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))
					? Condition.getCompare(1)+" '?' " : Condition.getCompare(8)+" '%?%' ";
					sign = sign.replace("?", String.valueOf(condition.getPropValue()));
					searchParam.append(sign);
				}
			}
		}
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by ").append(order.toString());
			}
		}
		//调用接口获取数据
		return omEmployeeManager.findNoPositionByPosId(positionid,searchParam.toString(),start,limit);
	}
	
	/**
	 * <li>说明：物理删除机构人员树对应的下级人员信息，同时删除该人员与机构的关联关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 人员id数组
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void deleteByIds(Serializable... ids) throws BusinessException {
		try {
			
			for (Serializable id : ids) {
				if(StringUtil.isNullOrBlank(String.valueOf(id))) continue;
				Long empid = Long.valueOf(String.valueOf(id)); //获得人员id
				OmEmployee employee = omEmployeeManager.getModelById(empid);//根据人员ID获取要删除的人员实体
				if(employee == null) continue;
				empPositionManager.deleteByEmployeeId(employee.getEmpid()); //【删除人员-岗位关联关系】
				operatorManager.deleteByIds(employee.getOperatorid()); 		//【删除操作员信息】
				empOrgManager.deleteByEmployeeId(employee.getEmpid());		//【删除人员-机构关联关系】
				this.daoUtils.getHibernateTemplate().delete(employee); 		//【删除人员信息】
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
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
	public void saveOrUpdate(OmEmployee emp) throws BusinessException, NoSuchFieldException {
		Date now = new Date();
		if(emp.getEmpid() == null) emp.setCreatetime(now); //后台设置创建时间
		emp.setLastmodytime(now); //后台设置最后更新时间
		daoUtils.saveOrUpdate(emp);
		empOrgManager.addEmpOrgCorrelation(emp.getOrgid(), emp.getEmpid(), "y"); //添加机构-人员关联
		//如当前岗位不为空，则说明人员是在岗位之下添加的
		if(emp.getPosition()!=null){
			empPositionManager.addEmpPositionCorrelation(emp.getPosition(), emp.getEmpid(), "y");//添加岗位-人员关联
		}
		
		
//		/*
//		 *  查询该人员归属的机构，如该机构当前状态为叶子节点（isleaf为空或者y）则更新为非叶子节点，使机构树能够展开看到该用户 
//		 */
//		OrganizationManager orgManager = (OrganizationManager)Application.getSpringApplicationContext().getBean("organizationManager");
//		OmOrganization org = orgManager.getModelById(emp.getOrgid());
//		if(StringUtil.isNullOrBlank(org.getIsleaf())||"y".equals(org.getIsleaf())){
//			org.setIsleaf("n");
//			daoUtils.saveOrUpdate(org);
//		}
	}
	
	
	
	/**
	 * <li>说明：根据机构id，获取其直属人员id
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgids 机构id序列
	 * @return 人员id序列
	 * @throws 抛出异常列表
	 */
	public String getChildEmpIdsByOrg(String orgids) {
		StringBuffer childEmpidAry = null;
		if(!StringUtil.isNullOrBlank(orgids)){
			childEmpidAry = new StringBuffer();
			//根据机构id序列，查询其直属人员的id
			String sql = "SELECT EMPID FROM OM_EMPORG WHERE ORGID IN("+orgids+")";
			List _t = daoUtils.executeSqlQuery(sql);
			if(_t!=null&&_t.size()>0){
				for(Object obj : _t){
					childEmpidAry.append(obj.toString()).append(",");
				}
				return childEmpidAry.toString().substring(0, childEmpidAry.toString().lastIndexOf(","));
			}
		}
		return null;
	}
	
	/**
	 * 
	 * <li>说明：人员列表选择控件
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-14
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param searchEntity
	 * @param codeOrName 人员编号或姓名
	 * @param empstatus  人员状态
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Page findPageList(SearchEntity<OmEmployee> searchEntity, String codeOrName, String empstatus) throws BusinessException {
		StringBuffer findAllEmp = new StringBuffer();
		findAllEmp.append("from OmEmployee ");
		if(!StringUtil.isNullOrBlank(empstatus)){
			findAllEmp.append(" where empstatus in ("+empstatus+") ");
		}
		if(!StringUtil.isNullOrBlank(codeOrName)){
			if(!StringUtil.isNullOrBlank(empstatus)) {
				findAllEmp.append(" and ");
			} else {
				findAllEmp.append(" where ");
			}
			findAllEmp.append(" (empcode like '%"+codeOrName+"%' or empname like '%"+codeOrName+"%')");
		}
		Order[] orders = searchEntity.getOrders();
		findAllEmp.append(HqlUtil.getOrderHql(orders));
    	String total = "select count(*) ".concat(findAllEmp.toString());
		return super.findPageList(total, findAllEmp.toString(), searchEntity.getStart(), searchEntity.getLimit());
	}
	
	/*******************************************/
	/*              工作组相关查询                  */
	/*******************************************/
	
	/**
	 * <li>说明：当用户点击工作组TreeNode时，调用该方法获取所选工作组的直属人员列表信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	public Page findEmpListByGroupNode(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer(); //查询条件字符串
		String sql = "SELECT emp.empid as \"empid\", emp.userid as \"userid\", emp.empname as \"empname\", emp.empstatus as \"empstatus\"," +
				" org.orgname as \"orgname\", gop.groupname as \"groupname\" FROM om_employee emp " +
				" LEFT JOIN om_emporg eo on emp.empid = eo.empid " +
				" LEFT JOIN om_organization org on eo.orgid = org.orgid " +
				" LEFT JOIN om_empgroup eg on emp.empid = eg.empid " +
				" LEFT JOIN om_group gop on eg.groupid = gop.groupid WHERE 1=1 ";
		String totalsql = "select count(*) as \"rowcount\" from (?)";
		
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				searchParam.append(" and ");
				searchParam.append(condition.getPropName()).append(" ");
				String sign = "";
				//如果匹配参数为空或者为"1" , 则转为 "=" 条件 
				if(StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))){
					sign = Condition.getCompare(1) + " '?' " ;  //转为 "=" 条件 
				} else if ("8".equals(String.valueOf(condition.getCompare()))){
					sign = Condition.getCompare(8) + " '%?%' "; //转为 "like" 条件 
				} else if ("21".equals(String.valueOf(condition.getCompare()))){
					sign = Condition.getCompare(21);            //转为 is null 条件
				}
				sign = sign.replace("?", String.valueOf(condition.getPropValue()));
				searchParam.append(sign);
			}
		}
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by ").append(order.toString());
			}
		}
		return super.findPageList(totalsql.replace("?", sql.concat(searchParam.toString())), sql.concat(searchParam.toString()), start, limit, null,null);
	}
	
	/**
	 * <li>说明：当用户点击岗位TreeNode时，调用该方法获取所选岗位的直属人员列表信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	public Page findEmpListByPosiNode(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer(); //查询条件字符串
		String sql = "SELECT emp.empid as \"empid\", emp.userid as \"userid\", emp.empname as \"empname\", emp.empstatus as \"empstatus\"," +
				" org.orgname as \"orgname\", pos.posiname as \"groupname\" FROM om_employee emp " +
				" LEFT JOIN om_emporg eo on emp.empid = eo.empid " +
				" LEFT JOIN om_organization org on eo.orgid = org.orgid " +
				" LEFT JOIN om_empposition ep on emp.empid = ep.empid " +
				" LEFT JOIN om_position pos on ep.positionid = pos.positionid WHERE 1=1 ";
		String totalsql = "select count(*) as \"rowcount\" from (?)";
		
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				searchParam.append(" and ");
				searchParam.append(condition.getPropName()).append(" ");
				String sign = "";
				//如果匹配参数为空或者为"1" , 则转为 "=" 条件 
				if(StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))){
					sign = Condition.getCompare(1) + " '?' " ;  //转为 "=" 条件 
				} else if ("8".equals(String.valueOf(condition.getCompare()))){
					sign = Condition.getCompare(8) + " '%?%' "; //转为 "like" 条件 
				} else if ("21".equals(String.valueOf(condition.getCompare()))){
					sign = Condition.getCompare(21);            //转为 is null 条件
				}
				sign = sign.replace("?", String.valueOf(condition.getPropValue()));
				searchParam.append(sign);
			}
		}
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by ").append(order.toString());
			}
		}
		return super.findPageList(totalsql.replace("?", sql.concat(searchParam.toString())), sql.concat(searchParam.toString()), start, limit, null,null);
	}
	
	/**
	 * <li>说明：工作组管理中新增人员功能，读取未指派工作组的人员列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Page findAddEmpList (List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer(); //查询条件字符串
		String hql = "";
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				//当前查询工作组下属人员
				if(!StringUtil.isNullOrBlank(condition.getPropName())&&"groupid".equals(condition.getPropName())){
					hql = "from OmEmployee where empid not in (select id.empid from OmEmpgroup where id.groupid = ?)";
					hql = hql.replace("?", String.valueOf(condition.getPropValue()));
				} 
				//当前查询岗位下属人员
				else if(!StringUtil.isNullOrBlank(condition.getPropName())&&"positionid".equals(condition.getPropName())){
					hql = "from OmEmployee where empid not in (select id.empid from OmEmpposition where id.positionid = ?)";
					hql += " and empid in (select id.empid from OmEmporg where id.orgid = (select orgid from OmPosition where positionid = ?))";
					hql = hql.replace("?", String.valueOf(condition.getPropValue()));
				} 
				//其他查询条件
				else {
					searchParam.append(" and ");
					searchParam.append(condition.getPropName()).append(" ");
					String sign = "";
					//如果匹配参数为空或者为"1" , 则转为 "=" 条件 
					if(StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))){
						sign = Condition.getCompare(1) + " '?' " ;  //转为 "=" 条件 
					} else if ("8".equals(String.valueOf(condition.getCompare()))){
						sign = Condition.getCompare(8) + " '%?%' "; //转为 "like" 条件 
					} else if ("21".equals(String.valueOf(condition.getCompare()))){
						sign = Condition.getCompare(21);            //转为 is null 条件
					}
					sign = sign.replace("?", String.valueOf(condition.getPropValue()));
					searchParam.append(sign);
				}
			}
		}
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by ").append(order.toString());
			}
		}
		String totalHql = "select count(*) ".concat(hql).concat(searchParam.toString());
		return super.findPageList(totalHql, hql.concat(searchParam.toString()), start, limit);
	}
	
	/**
	 * <li>说明：工作组管理中新增人员功能，读取未指派工作组的人员列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public Page findAddEmpList2 (List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer(); //查询条件字符串
		String hql = "";
		IOmPositionManager omPositionManager = (OmPositionManager)Application.getSpringApplicationContext().getBean("omPositionManager"); 
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				//当前查询工作组下属人员
				if(!StringUtil.isNullOrBlank(condition.getPropName())&&"groupid".equals(condition.getPropName())){
					hql = "from OmEmployee where empid not in (select id.empid from OmEmpgroup where id.groupid = ?)";
					hql = hql.replace("?", String.valueOf(condition.getPropValue()));
				} 
				//当前查询岗位下属人员
				else if(!StringUtil.isNullOrBlank(condition.getPropName())&&"positionid".equals(condition.getPropName())){
					//不能选择已经在本岗位中选过的
					hql = "from OmEmployee where empid not in (select id.empid from OmEmpposition where (ismain = 'n' or ismain is null) and id.positionid = ?)";
					//当前岗位是否是某个岗位的子岗位
					OmPosition op = omPositionManager.getModelById(Long.valueOf(String.valueOf(condition.getPropValue()))); //获取当前岗位实体
					if(op.getManaposi() != null){
						//当前岗位是子岗位, 人员选择范围只能是上级岗位内的人员 select empid from om_empposition where positionid = (select manaposi from om_position where positionid = 50145)
						hql += " and empid in (select id.empid from OmEmpposition where id.positionid = (select manaposi from OmPosition where positionid = ?))";
					} 
					else {
						//当前岗位直属于某个工作组, 人员选择范围只能是该工作组内的人员
						hql += " and empid in (select id.empid from OmEmpgroup where id.groupid = (select id.groupid from OmGroupposi where id.positionid = ?))";
					}
					//hql += " and empid in (select id.empid from OmEmporg where id.orgid = (select orgid from OmPosition where positionid = ?))";
					hql = hql.replace("?", String.valueOf(condition.getPropValue()));
				} 
				//其他查询条件
				else {
					searchParam.append(" and ");
					searchParam.append(condition.getPropName()).append(" ");
					String sign = "";
					//如果匹配参数为空或者为"1" , 则转为 "=" 条件 
					if(StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))){
						sign = Condition.getCompare(1) + " '?' " ;  //转为 "=" 条件 
					} else if ("8".equals(String.valueOf(condition.getCompare()))){
						sign = Condition.getCompare(8) + " '%?%' "; //转为 "like" 条件 
					} else if ("21".equals(String.valueOf(condition.getCompare()))){
						sign = Condition.getCompare(21);            //转为 is null 条件
					}
					sign = sign.replace("?", String.valueOf(condition.getPropValue()));
					searchParam.append(sign);
				}
			}
		}
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by ").append(order.toString());
			}
		}
		String totalHql = "select count(*) ".concat(hql).concat(searchParam.toString());
		return super.findPageList(totalHql, hql.concat(searchParam.toString()), start, limit);
	}
	
	/**
	 * <li>说明：保存人员-工作组关联关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 工作组id
	 * @return ids 人员id序列
	 * @throws 抛出异常列表
	 */
	public void addEmpGroup(Long groupid, Serializable... ids) throws BusinessException, NoSuchFieldException {
		for (Serializable id : ids) {
			Long empid = Long.valueOf(String.valueOf(id));
			empgroupManager.addOmEmpgroup(groupid,empid);
		}
	}
	
	/**
	 * <li>说明：保存人员-岗位关联关系 (工作组管理)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param positionid 岗位id
	 * @return ids 人员id序列
	 * @throws 抛出异常列表
	 */
	public void addEmpPosition(Long positionid, Serializable... ids) throws BusinessException, NoSuchFieldException {
		for (Serializable id : ids) {
			Long empid = Long.valueOf(String.valueOf(id));
			empPositionManager.addOmEmpPosition(positionid,empid, false);
		}
	}
	
	/**
	 * <li>说明：保存人员-岗位关联关系 (机构人员管理)
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param positionid 岗位id
	 * @param isMain 是否主岗位
	 * @return ids 人员id序列
	 * @throws 抛出异常列表
	 */
	public void addEmpPosition(Long positionid, Boolean isMain, Serializable... ids) throws BusinessException, NoSuchFieldException {
		for (Serializable id : ids) {
			Long empid = Long.valueOf(String.valueOf(id));
			empPositionManager.addOmEmpPosition(positionid,empid, isMain);
		}
	}
	
	/**
	 * <li>说明：移除人员-工作组关联关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-21
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 工作组id
	 * @return ids 人员id序列
	 * @throws 抛出异常列表
	 */
	public void delEmpGroup(Long groupid, Serializable... ids) throws BusinessException, NoSuchFieldException {
		for (Serializable id : ids) {
			Long empid = Long.valueOf(String.valueOf(id));
			empgroupManager.delOmEmpgroup(groupid,empid);
		}
	}
	
	/**
	 * <li>说明：移除人员-岗位关联关系
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 工作组id
	 * @return ids 人员id序列
	 * @throws 抛出异常列表
	 */
	public void delEmpPosition(Long positionid, Serializable... ids) throws BusinessException, NoSuchFieldException {
		for (Serializable id : ids) {
			Long empid = Long.valueOf(String.valueOf(id));
			empPositionManager.delOmEmpPosition(positionid,empid);
		}
	}
	
	/*******************************************/
	/*                 职务相关查询                */
	/*******************************************/
	
	/**
	 * <li>说明：当用户点击职务TreeNode时，调用该方法获取所选职务的直属人员列表信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 */
	public Page findEmpListByDuty(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer(); //查询条件字符串
		String sql = "SELECT emp.empid as \"empid\", emp.userid as \"userid\", emp.empname as \"empname\", emp.empstatus as \"empstatus\"," +
				" org.orgname as \"orgname\" FROM om_employee emp " +
				" LEFT JOIN om_emporg eo on emp.empid = eo.empid " +
				" LEFT JOIN om_organization org on eo.orgid = org.orgid " +
//				" LEFT JOIN om_empgroup eg on emp.empid = eg.empid " +
//				" LEFT JOIN om_group gop on eg.groupid = gop.groupid " +
				" WHERE emp.empid IN (" + 
				" SELECT POSI_.EMPID FROM OM_EMPPOSITION POSI_ WHERE POSI_.POSITIONID IN " +
				"(SELECT POSI.POSITIONID FROM OM_POSITION POSI WHERE POSI.DUTYID = ?))";
		String totalsql = "select count(*) as \"rowcount\" from (?)";
		
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				//当前查询职务下属人员
				if(!StringUtil.isNullOrBlank(condition.getPropName())&&"dutyid".equals(condition.getPropName())){
					sql = sql.replace("?", String.valueOf(condition.getPropValue()));
				}
				//其他查询条件
				else {
					searchParam.append(" and ");
					searchParam.append(condition.getPropName()).append(" ");
					String sign = "";
					//如果匹配参数为空或者为"1" , 则转为 "=" 条件 
					if(StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))){
						sign = Condition.getCompare(1) + " '?' " ;  //转为 "=" 条件 
					} else if ("8".equals(String.valueOf(condition.getCompare()))){
						sign = Condition.getCompare(8) + " '%?%' "; //转为 "like" 条件 
					} else if ("21".equals(String.valueOf(condition.getCompare()))){
						sign = Condition.getCompare(21);            //转为 is null 条件
					}
					sign = sign.replace("?", String.valueOf(condition.getPropValue()));
					searchParam.append(sign);
				}
			}
		}
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by ").append(order.toString());
			}
		}
		return super.findPageList(totalsql.replace("?", sql.concat(searchParam.toString())), sql.concat(searchParam.toString()), start, limit, null,null);
	}
	
	/*******************************************/
	/*              人员控制查询                    */
	/*******************************************/
	
	/**
	 * <li>说明：机构人员树机构节点对应的下级人员列表查询
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param whereList 查询参数
	 * @param orderList 排序参数
	 * @param start 起始条数
	 * @param limit 每页条数
	 * @return 人员信息分页列表
	 * @throws 抛出异常列表
	 */
	public Page findAllEmployees(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		String sql = SqlMapUtil.getSql("jcgl-emp:findEmployee360");
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				searchParam.append(" and EMP.");
				searchParam.append(condition.getPropName()).append(" ");
				String sign = "";
				//如果匹配参数为空或者为"1" , 则转为 "=" 条件 
				if(StringUtil.isNullOrBlank(String.valueOf(condition.getCompare()))||"1".equals(String.valueOf(condition.getCompare()))){
					sign = Condition.getCompare(1) + " '?' " ;  //转为 "=" 条件 
				} else if ("8".equals(String.valueOf(condition.getCompare()))){
					sign = Condition.getCompare(8) + " '%?%' "; //转为 "like" 条件 
				} else if ("21".equals(String.valueOf(condition.getCompare()))){
					sign = Condition.getCompare(21);            //转为 is null 条件
				}
				sign = sign.replace("?", String.valueOf(condition.getPropValue()));
				searchParam.append(sign);
			}
		}
		//拼接排序参数
		if(orderList != null && orderList.size()>0) {
			for(Order order : orderList){
				searchParam.append(" order by EMP.").append(order.toString());
			}
		}
		sql += searchParam.toString();
		String totalsql = "select count(*) as \"rowcount\" from ("+sql+")";
		return super.findPageList(totalsql, sql, start, limit,null,null);
	}
	
	/**
	 * <li>说明：更新人员-机构关联
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-28
	 * <li>修改人： 汪东良
	 * <li>修改日期：2015-08-15
	 * <li>修改内容：添加更新人员信息的ORGID属性
	 * @param orgid 组织机构ID
	 * @param empid 人员ID
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void updateEmpOrg(Long orgid, Long empid) throws BusinessException {
		empOrgManager.addEmpOrgCorrelation(orgid, empid, "y");
         //2015-08-15 汪东良，添加更新人员信息上的ORGID
        OmEmployee omEmployee = omEmployeeManager.getModelById(empid);
        omEmployee.setOrgid(orgid);
        this.save(omEmployee);
	}
	
	/**
	 * <li>说明：更新人员-工作组关联
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 工作组ID
	 * @param empid 人员ID
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void updateEmpGroup(Long groupid, Long empid, Long oldgroupid) throws BusinessException {
		empgroupManager.updateOmEmpgroupByWidget(groupid,empid,oldgroupid);
		//调用方法，删除岗位于人员的关联关系
		empPositionManager.delOmEmpPositionByGroupId(oldgroupid, empid);
	}
	
	/**
	 * <li>说明：更新人员-岗位关联
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param positionid 岗位ID
	 * @param empid 人员ID
	 * @param oldpositionid 原岗位ID
	 * @param ismain 是否主岗位
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void updateEmpPosi(Long positionid, Long empid, Long oldpositionid,String isMain) throws BusinessException {
		empPositionManager.updateOmEmpPositionByWidget(positionid, empid, oldpositionid, isMain);
	}
	
	/**
	 * <li>说明：更新人员-岗位关联
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-28
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param positionid 岗位ID
	 * @param empid 人员ID
	 * @throws 抛出异常列表
	 */
	@SuppressWarnings("unchecked")
	public void updateEmpPosi(Long positionid, Long empid, String isMain) throws BusinessException {
		empPositionManager.addEmpPositionCorrelation(positionid, empid, isMain);
	}
	/**
	 * 
	 * <li>说明：根据组织机构获取人员列表
	 * <li>创建人：程锐
	 * <li>创建日期：2014-10-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgId 组织机构Id
	 * @return 分页对象
	 */	
	@SuppressWarnings("unchecked")
	public Page queryEmpListByTeam(String orgId) {
        orgId = StringUtil.nvl(orgId, SystemContext.getOmOrganization().getOrgid().toString());
		List<OmEmployee> list = omEmployeeManager.findByOrgId(Long.valueOf(orgId));
		return new Page(list.size(), list);
	}
	
	
	/**
	* <li>说明： 根据用户所在部门ID查询该部门下的所有用户
	* <li>创建人： 黄杨
	* <li>创建日期：2017-5-26
	* <li>修改人：
	* <li>修改内容：
	* <li>修改日期：
	*@param orgId 组织ID
	*@param empName 员工姓名（或编码）
	*@return
	*/
	@SuppressWarnings("unchecked")
	public List<UserData> getEmpListByOrgid(String orgId, String empName) {
		StringBuilder sb = new StringBuilder("SELECT A.EMPID, A.USERID, A.OPERATORID, C.OPERATORNAME, A.EMPNAME, A.ORGID, B.ORGNAME, B.PARENTORGID, B.ORGSEQ, B.ORGLEVEL, B.ORGDEGREE  FROM OM_EMPLOYEE A, OM_ORGANIZATION B, AC_OPERATOR C WHERE A.ORGID = B.ORGID AND A.OPERATORID = C.OPERATORID");
		// 查询条件 - 班组id
		sb.append(" AND B.ORGID = '").append(orgId).append("'");
		// 查询条件 - 人员名称或代码
		if (!StringUtil.isNullOrBlank(empName)) {
			sb.append(" AND (A.EMPNAME LIKE '%").append(empName).append("%'");
			sb.append(" OR ");
			sb.append(" A.USERID LIKE '%").append(empName).append("%'");
			sb.append(" )");
		}
		sb.append(" ORDER BY A.EMPNAME");
		return this.daoUtils.executeSqlQueryEntity(sb.toString(), UserData.class);
	}
}