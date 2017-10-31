package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.util.LogonUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.entity.OmOrganization;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 工作组业务处理类
 * <li>创建人：谭诚
 * <li>创建日期：2013-11-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@Service(value="operatorManager")
public class OperatorManager extends JXBaseManager<AcOperator,AcOperator>{
	/** 组织机构查询接口 */
	@Resource(name="omOrganizationManager")
	private IOmOrganizationManager omOrganizationManager;
	
	/** 人员查询接口 */
	@Resource(name="omEmployeeManager")
	private IOmEmployeeManager omEmployeeManager;
	
	/** 操作员查询接口*/
	@Resource(name="acOperatorManager")
	private IAcOperatorManager acOperatorManager;
    
    /** 应用功能业务类*/
    @Resource
    private SysFunctionManager sysFunctionManager ;

	public void saveOrUpdate(AcOperator ao,String empid){
		try {
			boolean isAdd = false;
			if(ao.getOperatorid()==null) isAdd = true;
			ao.setPassword(LogonUtil.getPassword(ao.getPassword()));
			OmEmployee emp = omEmployeeManager.getModelById(Long.valueOf(empid));
			if(emp == null) return;
			if(isAdd){
				this.daoUtils.getHibernateTemplate().save(ao); //新增操作员信息
				emp.setOperatorid(ao.getOperatorid()); //设置人员表关联关系
				emp.setUserid(ao.getUserid());
				this.daoUtils.getHibernateTemplate().saveOrUpdate(emp); //更新人员表
			} else {
				this.daoUtils.getHibernateTemplate().update(ao); //更新操作员信息
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * <li>说明：更新操作员信息，并根据操作员数据实体，更新人员表的部分字段
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operator 操作员信息实体
	 * @throws 抛出异常列表
	 */
	public void updateOperatorInfo(AcOperator operator) throws BusinessException {
		if(operator == null) return; //入参实体为空，终止后续操作
		this.daoUtils.getHibernateTemplate().update(operator); //执行更新操作
		//根据操作员ID获取人员实体
		OmEmployee emp = omEmployeeManager.findByOperator(operator.getOperatorid());
		if(emp == null) return; //未能找到对应的人员信息，终止后续操作
		emp.setUserid(operator.getUserid()); //更新人员表中记录的操作员登录帐号
		this.daoUtils.getHibernateTemplate().update(emp); //执行人员表更新操作
	}
	
	/**
	 * <li>说明：更新操作员信息，并根据操作员数据实体，更新人员表的部分字段
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param operator 操作员信息实体
	 * @param newPWD 新密码
	 * @throws Exception 
	 * @throws 抛出异常列表
	 */
	public void updateOperatorPWD(AcOperator operator,String newPWD) throws Exception {
		if(operator == null || StringUtil.isNullOrBlank(newPWD)) return; //入参实体和密码参数为空，终止后续操作
		AcOperator originally = acOperatorManager.getModelById(operator.getOperatorid());
		originally.setPassword(LogonUtil.getPassword(newPWD));
		this.daoUtils.getHibernateTemplate().saveOrUpdate(originally);
	}
	
	/**
	 * <li>说明：更新前验证用户输入的原始密码是否与数据库存储的密码相同，如果不同，则返回错误信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * @param t 实体对象
	 * @return 返回更新操作前的验证信息，如果验证成功返回null，验证失败返回错误信息
	 * @throws Exception 
	 * @throws BusinessException
	 */
	public String[] validatePWD(AcOperator t) throws Exception{
		AcOperator originally = acOperatorManager.getModelById(t.getOperatorid());
		if(t.getPassword() != null){
			String pwd = LogonUtil.getPassword(t.getPassword()); //将在表单中填写的原密码进行加密
			if(!StringUtil.isNullOrBlank(pwd)&&pwd.equals(originally.getPassword())){
				return null;
			}
		}
		return new String []{"原始密码输入错误，请重新输入！"};
	}
	
	/**
	 * <li>说明：物理删除操作员信息
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-6
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param ids 人员id数组
	 * @throws 抛出异常列表
	 */
	public void deleteByIds(Serializable... ids) throws BusinessException {
		try {
			for (Serializable id : ids) {
				if(StringUtil.isNullOrBlank(String.valueOf(id))) continue; //获取要删除的操作员ID
				Long operatorId = Long.valueOf(String.valueOf(id)); //获得操作员id
				AcOperator operator = acOperatorManager.getModelById(operatorId); //根据操作员ID获取实体
				if(operator == null) continue;
				this.daoUtils.getHibernateTemplate().delete(operator); //执行删除
				//根据操作员ID获取人员实体,清空人员表中关于该操作员信息的2个字段值
				OmEmployee emp = omEmployeeManager.findByOperator(operator.getOperatorid());
				if(emp == null) continue; //未能找到对应的人员信息，终止后续操作
				String hql = "update om_employee set operatorid = null, userid = null where empid = " + emp.getEmpid();
				this.daoUtils.executeSql(hql);
			}
		} catch (Exception ex) {
			throw new BusinessException(ex);
		}
	}
	
	/**
	 * <li>说明：返回已配置为当前角色的操作员列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-17
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
	public Page findOperatorListByRole(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		String sql = " from AcOperator operator,AcOperatorrole operrole where operator.operatorid = operrole.id.operatorid and operrole.id.roleid = '?'";
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				if(!StringUtil.isNullOrBlank(condition.getPropName())&&"roleid".equals(condition.getPropName())){
					sql = sql.replace("?", String.valueOf(condition.getPropValue()));
				} 
				else {
					//构建查询条件
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
		sql = sql.concat(searchParam.toString()); //拼装查询条件
		String totalHql = "select count(*) ".concat(sql); //构建总条数统计语句
		String hql = "select operator ".concat(sql); //构建数据查询语句
		return super.findPageList(totalHql, hql, start, limit);
	}
    
    /**
     * <li>说明：根据功能名和系统名获取对应权限手机号集合
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param funcname
     * @param appcode
     * @return
     */
    public String getAcOperatorsPhoneByfuncnameAndappcode(String funcname,String appcode){
        String phones = "" ;
        String sql = "select em from OmEmployee em, AcOperator operator,AcOperatorrole operrole where em.operatorid = operator.operatorid and  operator.operatorid = operrole.id.operatorid ";
        List<String> roles = sysFunctionManager.findRolesByNameAndAppId(funcname, appcode);
        String roleids = "" ;
        for (String roleid : roles) {
            roleids += "'" + roleid + "',";
        }
        if(StringUtil.isNullOrBlank(roleids)){
           return "" ; 
        }
        roleids = roleids.substring(0,roleids.length()-1);
        sql = sql + "and operrole.id.roleid in ("+roleids+")";
        List <OmEmployee> list = this.daoUtils.find(sql);
        List<String> plist = new ArrayList<String>();
        for (OmEmployee em : list) {
            if(!StringUtil.isNullOrBlank(em.getMobileno()) && !plist.contains(em.getMobileno())){
                plist.add(em.getMobileno());
                phones += em.getMobileno() + ",";
            }
        }
        if(!StringUtil.isNullOrBlank(phones)){
            phones = phones.substring(0,phones.length()-1);
        }
        return phones ;
    }
	
	/**
	 * <li>说明：返回尚未配置为当前角色的操作员列表
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-18
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
	public Page findOperatorListByRole2(List<Condition> whereList, List<Order> orderList, Integer start, Integer limit){
		StringBuffer searchParam = new StringBuffer();
		String sql = " from AcOperator operator where operator.operatorid not in (select id.operatorid from AcOperatorrole where id.roleid = '?')";
		//拼接查询参数
		if (whereList != null && whereList.size() > 0) {
			for (Condition condition : whereList) {
				if(!StringUtil.isNullOrBlank(condition.getPropName())&&"roleid".equals(condition.getPropName())){
					sql = sql.replace("?", String.valueOf(condition.getPropValue()));
				} 
				else {
					//构建查询条件
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
		sql = sql.concat(searchParam.toString()); //拼装查询条件
		String totalHql = "select count(*) ".concat(sql); //构建总条数统计语句
		String hql = "select operator ".concat(sql); //构建数据查询语句
		return super.findPageList(totalHql, hql, start, limit);
	}
	
	/**
	 * 
	 * <li>说明：根据当前登录用户账号和密码，查询匹配的操作员
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param userid 用户账号
	 * @param pwd  登录密码
	 * @return 当前登录操作员实体
	 */
	public AcOperator findLoginAcOprator(String userid, String pwd)
			throws BusinessException {
		try{
			AcOperator acOperator = this.findSingle("FROM AcOperator  WHERE userid = '" + userid + "' AND password = '" + pwd + "'");
			return acOperator;
		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * 
	 * <li>说明：根据当前登录人员的信息，获取其所属机构
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param emp 当前登录系统人员实体
	 * @return 当前登录人所属机构
	 */
	public OmOrganization findOrgByCrtEmployee(OmEmployee emp) throws BusinessException{
		if(emp == null) return null;
		OmOrganization org = omOrganizationManager.findByEmpId(emp.getEmpid());
		return org;
	}
	
	/**
	 * 
	 * <li>说明：根据当前登录人员的信息，获取人员信息实体
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-12-26
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param acOperator 当前登录系统操作员实体
	 * @return 当前登录人员信息实体
	 */
	public OmEmployee findEmpByCrtOperator(AcOperator acOperator) throws BusinessException{
		if(acOperator == null) return null;
		OmEmployee emp = omEmployeeManager.findByOperator(acOperator.getOperatorid()); // 查询操作员对应员工信息
		return emp;
	}
	
	
}
