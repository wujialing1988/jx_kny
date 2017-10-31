package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;

import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.yhgl.entity.OmOrganization;

/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 组织机构数据查询功能接口
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
 * &nbsp; //依赖注入：组织机构组件实例对象
 * &nbsp; @Resource
 * &nbsp; private IOmOrganizationManager omOrganizationManager;
 * &nbsp; 
 * &nbsp; //调用接口方法，根据业务功能需要调用接口获取组织机构数据
 * &nbsp; omOrganizationManager.findRoot();
 * &nbsp; 
 * </pre></code> 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IOmOrganizationManager {
	/** 组织机构查询sql文件名称  */
	final String XMLNAME_ORG = "jcgl-org:";
	
	/**
	 * <br/><li>说明： 查询组织机构树的根节点.
	 * <br/><li><font color=red>注*：将取得组织机构表中所有parentorgid为空并且机构状态为"running",可能会有多个</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @return 组织机构实体列表
	 */
	public List <OmOrganization> findRoot();
	
	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配, 支持的数据类型有NUMBER,VARCHAR2,CHAR,INTEGER</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-7-25
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
     * @param field 字段名
     * @param value 字段值
     * @return 组织机构实体列表
     */
	public List<OmOrganization> findByField(String field, String value);
	
	/**
	 * <br/><li>说明： 根据组织机构Id查询唯一对应的组织机构.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 组织机构id
	 * @return 组织机构实体
	 */
	public OmOrganization getModelById(Long orgId);
	
	/**
	 * <br/><li>说明：根据多个组织机构Id,查询与对应的组织机构
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个orgId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgIds 组织机构id字符串
	 * @return 组织机构实体列表
	 */
	public List <OmOrganization> findByIds(String orgIds);
	
	/**
	 * <br/><li>说明：根据多个组织机构Id,查询与对应的组织机构,并分页显示结果
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个orgId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgIds 组织机构id字符串
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 组织机构实体分页列表
	 */
	public Page findByIds(String orgIds,Integer start,Integer limit);
	
	/**
	 * <br/><li>说明： 根据组织机构的编号精确查找与其对应的唯一组织机构
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgCode 组织机构编号
	 * @return 组织机构实体
	 */
	public OmOrganization findByCode(String orgCode);
	
	/**
	 * <br/><li>说明： 根据组织机构编号进行模糊查询(like '%...%'方式),并提供分页支持
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgCode 组织机构编号
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 带分页的组织机构实体列表
	 */
	public Page findByCode(String orgCode,Integer start,Integer limit);
	
	/**
	 * <br/><li>说明： 根据orgseq查询唯一对应的组织机构. orgseq的格式示例: ".0.7.146"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgseq 组织机构Id序列
	 * @return 组织机构实体
	 */
	public OmOrganization findBySeq(String orgseq);
	
	/**
	 * <br/><li>说明： 根据组织机构ID,查找其下一级组织机构信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 组织机构Id
	 * @return 组织机构实体列表
	 */
	public List<OmOrganization> findChildsById(Long orgId);
	
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
	public List<OmOrganization> findAllChildsById(Long orgId);
	
	/**
	 * <br/><li>说明： 根据组织机构ID,查找其下一级组织机构信息,并分页显示结果
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 组织机构Id
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 带分页的组织机构实体列表
	 */
	public Page findChildsById(Long orgId,Integer start,Integer limit);
	
	/**
	 * <br/><li>说明：根据人员ID,获得其所属的组织机构
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param empId 人员id
	 * @return 组织机构实体
	 */
	public OmOrganization findByEmpId(Long empId);
	
	/**
	 * <br/><li>说明：根据操作员ID,查询其所属的组织机构
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param operatorid 操作员id
	 * @return 组织机构实体
	 */
	public OmOrganization findByOperator(Long operatorid);
	
	/**
	 * <br/><li>说明：根据组织机构ID,查询该机构下的某个指定级别的组织机构
	 * <br/><li>参数示例1: orgId=0,degree=oversea,表示查询铁道部下所有机务段;
	 * <br/><li>参数示例2: orgId=141,degree=tream,表示查询天津基地下所有班组.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 组织机构Id
	 * @param degree 组织机构级别
	 * @return 组织机构实体列表
	 */
	public List<OmOrganization> findByDegree(Long orgId, String degree);
	
	/**
	 * <br/><li>说明：根据组织机构ID,查询该机构下的某个指定级别的组织机构,并分页显示结果
	 * <br/><li>参数示例1: orgId=0,degree=oversea,表示查询铁道部下所有机务段;
	 * <br/><li>参数示例2: orgId=141,degree=tream,表示查询天津基地下所有班组.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 组织机构Id
	 * @param degree 组织机构级别
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @return 组织机构实体列表
	 */
	public Page findByDegree(Long orgId, String degree, Integer start,Integer limit);
	
	/**
	 * <br/><li>说明： 根据组织机构ID,查询以该机构为基点(level=1)的子机构,treeLevel参数指定从基点开始向下查询到第几层,参数hasSelf指明结果集是否包含入参orgId这一机构
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 组织机构Id
	 * @param treeLevel 从当前orgid开始计算, 向下查询多少层
	 * @param hasSelf 是否包含参数OrgId本身
	 * @return 组织机构实体列表
	 */
	public List<OmOrganization> findTreeByDynamicLevel(Long orgId, Integer treeLevel, boolean hasSelf);
	
	/**
	 * <br/><li>说明： 根据组织机构ID,查询以组织机构树根节点为基点(level=1)的子机构,treeLevel参数指定从基点开始向下查询到第几层,参数hasSelf指明结果集是否包含入参orgId这一机构
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 组织机构Id
	 * @param orgLevel 与orglevel字段值对应
	 * @param hasSelf 是否包含参数OrgId本身
	 * @return 组织机构实体列表
	 */
	public List<OmOrganization> findTreeByStaticLevel(Long orgId, Integer orgLevel, boolean hasSelf);
	
	
	/**
	 * <br/><li>说明： 根据组织机构ID,查询该机构下属所有子机构.参数hasSelf指定结果集是否包含入参Id机构
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 组织机构Id
	 * @param hasSelf 是否包含参数OrgId本身
	 * @return 组织机构实体列表
	 */
	public List<OmOrganization> findAllChilds(Long orgId, boolean hasSelf);
	
	/**
	 * <br/><li>说明： 根据组织机构ID,查询该机构下属所有子机构,参数hasSelf指定结果集是否包含入参Id机构,并分页显示查询结果
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-21
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 组织机构Id
	 * @param hasSelf 是否包含参数OrgId本身
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @return 组织机构实体分页列表
	 */
	public Page findAllChilds(Long orgId, boolean hasSelf, Integer start,Integer limit);
	
	/**
	 * <br/><li>说明：根据组织机构Id,查询其之上各级组织机构,如指定了degree参数,则返回该参数指定的级别的单位
	 * <br/><li>参数示例1: orgId=242,degree="branch", 表示查询"机电一班"属于哪个铁路局
	 * <br/><li>参数示例2: orgId=242,degree="oversea", 表示查询"机电一班"属于哪个机务段或检修公司
	 * <br/><li>参数示例3: orgId=242,degree="branch,oversea",表示查询"机电一班"属于哪个局下的哪个段
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-23
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 组织机构id
	 * @param degree 目标机构的级别
	 * @return 组织机构实体列表
	 */
	public List<OmOrganization> findUpperByDegree(Long orgId, String degree);
	
	/**
	 * <br/><li>说明： 构建组织机构树,为组织机构控件所调用
	 * <br/><li>参数示例1: orgId=361,ultimateOrgDegree="plant",  表示orgname显示为"机电一班/制造部"
	 * <br/><li>参数示例2: orgId=361,ultimateOrgDegree="oversea",表示orgname显示为"机电一班/制造部/天津电力机车有限公司"
	 * <br/><li>参数示例3: orgId=361,ultimateOrgDegree="branch", 表示orgname显示为"机电一班/制造部/天津电力机车有限公司/北京铁路局"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 组织机构id
	 * @param ultimateOrgDegree 组织机构名称Path的最终显示层级
	 * @return 组织机构Map列表
	 */
	public List<Map> findOrgTree(Long orgId, String ultimateOrgDegree);
	
	/**
	 * <br/><li>说明：根据自定义HQL,构建组织机构树,为组织机构控件所调用
	 * <br/><li>参数示例1: orgId=361,ultimateOrgDegree="plant",  表示orgname显示为"机电一班/制造部"
	 * <br/><li>参数示例2: orgId=361,ultimateOrgDegree="oversea",表示orgname显示为"机电一班/制造部/天津电力机车有限公司"
	 * <br/><li>参数示例3: orgId=361,ultimateOrgDegree="branch", 表示orgname显示为"机电一班/制造部/天津电力机车有限公司/北京铁路局"
	 * <br/><li>参数示例4: orgId=141,queryHql="orgcode like '2131%'",ultimateOrgDegree="oversea",表示查询机构编号为JSB开头的北京铁路局的直属下级单位,orgname显示为"机电一班/制造部/天津电力机车有限公司"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param orgId 组织机构id
	 * @param queryHql 查询Hql字符串
	 * @param ultimateOrgDegree 组织机构名称Path的最终显示层级
	 * @return 组织机构Map列表
	 */
	public List<Map> findOrgTree(Long orgId, String queryHql, String ultimateOrgDegree, String isChecked);
	
	/**
	 * <br/><li>说明：根据自定义HQL,构建组织机构树,为组织机构控件所调用
	 * <br/><li><font color=red>注*：通常用于关联数据字典,例如查询承修部门</font>
	 * <br/><li>查询语句结构: "from OmOrganization where status = 'running' " + queryHql
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
     * @param queryHql 查询Hql字符串
     * @param showGrandChilds 是否需要按照属性结构显示更下层,用于控制树控件的展开按钮.
     * @return 组织机构Map列表
	 */
	public List<Map> findOrgTree(String queryHql,boolean showGrandChilds,String isChecked);
	
	/**
	 * <br/><li>说明： 组织机构树控件业务方法
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-24
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
     * @param orgId 组织机构Id
     * @param orgdegree 获取显示级别，如queryHql为[degree]plant|fullName，则取plant值
     * @param fullNameDegree 组织机构路径的显示层级
     * @return 查询列表所需List列表
	 */
	public List<Map> findSomeLevelOrgTree(Long orgId,String orgdegree,String fullNameDegree, String isChecked);
	
	/**
     * <br/><li>说明：根据登录人所属部门id获取与承修部门数据字典匹配的机构实体对象
     * <br/><li>创建人：程锐
     * <br/><li>创建日期：2013-5-17
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
     * @param orgId 登录人所属部门id
     * @return 组织机构实体对象
     */
    public OmOrganization getUnderTakeByEmp(String orgId);
    
    /**
     * <br/><li>说明：根据orgid获取单位全名，格式如:重庆西/重庆机务段/成都铁路局，最大单位到铁道部下一级
     * <br/><li>创建人：程锐
     * <br/><li>创建日期：2012-10-10
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
     * @param orgId 单位orgid
     * @param orgdegree 显示组织全名Path时最大到哪一级
     * @return 单位全名 格式如:重庆西/重庆机务段/成都铁路局，最大单位到铁道部下一级
     */
	public String getFullName(Long orgId,String orgdegree);
	
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
	public Page findUserManager(Long userId,Integer start,Integer limit);
	
	/**
	 * <br/><li>说明：根据组织机构Id数组,查询匹配的机构信息列表
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-29
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param ids 组织机构Id数组
	 * @return 组织机构实体列表
	 */
	public List <OmOrganization> findByIdArys(Serializable... ids);
	
	/**
	 * <br/><li>说明：组织机构的QBE查询,支持分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-29
	 * <br/><li>修改人：谭诚 
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 组织机构分页对象
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; OmOrganization omOrganization = new OmOrganization();   
	 * &nbsp; omOrganization.setOrgname("XXX");       
	 * &nbsp; 
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("orgcode"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp;
	 * &nbsp; //构建查询对象
	 * &nbsp; SearchEntity <OmOrganization> entity = new SearchEntity<OmOrganization>(); 
	 * &nbsp; entity.setEntity(omOrganization);       
	 * &nbsp; entity.setStart(start);       
	 * &nbsp; entity.setLimit(limit);       
	 * &nbsp; entity.setOrders(orders);     
	 * &nbsp;  
	 * &nbsp; //调用查询
	 * &nbsp; Page <OmOrganization> page = omOrganizationManager.findByEntity(entity, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public Page <OmOrganization> findByEntity(SearchEntity <OmOrganization> searchEntity, Boolean isExact);

	/**
	 * <br/><li>说明：组织机构的QBE查询,非分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-29
	 * <br/><li>修改人：谭诚 
	 * <br/><li>修改日期：2013-8-8
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param org 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 组织机构对象集合
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; OmOrganization org = new OmOrganization();
	 * &nbsp; org.setOrgcode("XXX");  
	 * &nbsp;
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("orgid"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp; 
	 * &nbsp; //调用查询
	 * &nbsp; List <OmOrganization> list = omOrganizationManager.findByEntity(org, orders, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public List <OmOrganization> findByEntity(OmOrganization org, Order [] orders, Boolean isExact);
	
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
	public List <OmOrganization> findByPositionid(Long positionid);
}
