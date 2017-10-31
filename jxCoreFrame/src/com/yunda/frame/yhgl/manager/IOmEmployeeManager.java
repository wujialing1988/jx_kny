package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Order;

import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.yhgl.entity.OmEmployee;

/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 人员相关数据查询功能接口
 * <br/><li>创建人：谭诚
 * <br/><li>创建日期：2013-7-18
 * <br/><li>修改人: 
 * <br/><li>修改日期：
 * <br/><li>修改内容：
 * <br/><li>版权: Copyright (c) 2008 运达科技公司
 * <br/><code><pre>
 * &nbsp; 
 * &nbsp; 示例代码：
 * &nbsp; 
 * &nbsp; //依赖注入：人员组件实例对象
 * &nbsp; @Resource
 * &nbsp; private IOmEmployeeManager omEmployeeManager;
 * &nbsp; 
 * &nbsp; //调用接口方法，根据业务功能需要调用接口获取人员数据
 * &nbsp; omEmployeeManager.getModelById("141");
 * &nbsp; 
 * </pre></code> 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IOmEmployeeManager {
	/** 人员查询sql文件名称  */
	final String XMLNAME_EMP = "jcgl-emp:";
	
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
	public OmEmployee getModelById(Long empId);
	
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
	public OmEmployee findByName(String empName);
	
	/**
	 * <br/><li>说明：根据人员编号查询其信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-18
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param empCode 人员code
	 * @return 人员实体
	 */
	public OmEmployee findByCode(String empCode);
	
	
	/**
     * <br/><li>说明： 自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配</font>
     * <br/><li>创建人：张凡
     * <br/><li>创建日期：2013-5-20
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
     * @param field 字段名
     * @param value 字段值
     * @return 人员实体列表
     */
	public List<OmEmployee> findByField(String field, String value);
	
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
	public List <OmEmployee> findByIds(String empIds);
	
	/**
	 * <br/><li>说明：根据人员Id数组,查询匹配的人员列表
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-29
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param ids 人员Id数组
	 * @return 人员实体列表
	 */
	public List <OmEmployee> findByIdArys(Serializable... ids);
	
	/**
	 * <br/><li>说明：查询与该用户相同组织下的所有用户, degree表示范围 [tream,hq,plant,oversea,branch]
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-18
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param empId 用户Id
	 * @param degree 部门级别
	 * @return 人员实体列表
	 */
	public List<OmEmployee> findBySameOrg(Long empId, String degree);
	
	/**
	 * <br/><li>说明：根据操作员ID,查询与该操作员相同组织下的所有用户, 同时还需要模糊匹配参数指定的人员姓名
	 * <br/><li><font color=red>注*：当不需要匹配人员姓名时,该参数传入null</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-18
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param operatorId 操作员Id
	 * @param empName 人员姓名
	 * @return 人员实体列表
	 */
	public List <OmEmployee> findByOperator(Long operatorId, String empName);
	
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
	public List<OmEmployee> findByOrgId(Long orgId);
	
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
	public List<OmEmployee> findByOrgIds(Long... orgIds);	
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
	public List<OmEmployee> findNoPositionByOrgId(Long orgId);
	
	/**
	 * <li>说明：查询隶属于某个工作组但并不属于该组织下任何岗位的人员信息（工作组树采用）
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupId 工作组id
	 * @return 人员实体列表
	 */
	public List<OmEmployee> findNoPositionByGroupId(Long groupId);
	
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
	public Page findNoPositionByOrgId(Long orgId, String searchParam, Integer start,Integer limit);
	
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
	public Page findByOrgId(Long orgId, String searchParam, Integer start,Integer limit);
	
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
	public Page findNoPositionByPosId(Long positionid, String searchParam, Integer start, Integer limit);
	
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
	public List<OmEmployee> findByPosition(Long positionid);
	
	/**
	 * <br/><li>说明：查询某个组织机构下直属的用户信息(不包含子机构的用户),并分页显示结果
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
	public Page findByOrgId(Long orgId,Integer start,Integer limit);
	
	/**
	 * <br/><li>说明：查询某个组织机构下直属的用户信息,以及归属本组织机构或班组的子机构的人员,并分页显示结果
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
	public Page findChildsByOrgId(Long orgId,Integer start,Integer limit);
	
	/**
	 * <br/><li>说明：根据操作员Id,获取人员信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param operatorId 操作员Id
	 * @return 人员实体
	 */
	public OmEmployee findByOperator(Long operatorId);
	
	/**
	 * <br/><li>说明：人员的QBE查询,支持分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-29
	 * <br/><li>修改人：谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 人员分页对象
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; OmEmployee omEmployee = new OmEmployee();   
	 * &nbsp; omEmployee.setEmpcode("XXX");       
	 * &nbsp; 
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("realname"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp;
	 * &nbsp; //构建查询对象
	 * &nbsp; SearchEntity <OmEmployee> entity = new SearchEntity<OmEmployee>(); 
	 * &nbsp; entity.setEntity(omEmployee);       
	 * &nbsp; entity.setStart(start);       
	 * &nbsp; entity.setLimit(limit);       
	 * &nbsp; entity.setOrders(orders);     
	 * &nbsp;  
	 * &nbsp; //调用查询
	 * &nbsp; Page <OmEmployee> page = omEmployeeManager.findByEntity(entity, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public Page <OmEmployee> findByEntity(SearchEntity <OmEmployee> searchEntity, Boolean isExact);
	
	/**
	 * <br/><li>说明：人员的QBE查询,非分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-29
	 * <br/><li>修改人：谭诚 
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param emp 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 人员对象集合
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; OmEmployee emp = new OmEmployee();
	 * &nbsp; emp.setEmpcode("XXX");  
	 * &nbsp;
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("realname"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp; 
	 * &nbsp; //调用查询
	 * &nbsp; List <OmEmployee> list = omEmployeeManager.findByEntity(emp, orders, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public List <OmEmployee> findByEntity(OmEmployee emp, Order [] orders, Boolean isExact);
	
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
	public Page<OmEmployee> findEmpAndOrgPath(String maxOrgDegree, String sqlParam,Integer start,Integer limit);
}
