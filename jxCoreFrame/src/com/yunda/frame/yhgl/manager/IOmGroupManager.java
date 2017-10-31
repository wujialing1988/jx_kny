package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Order;

import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.yhgl.entity.OmGroup;

/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 工作组相关数据查询功能接口
 * <br/><li>创建人：谭诚
 * <br/><li>创建日期：2013-8-5
 * <br/><li>修改人: 
 * <br/><li>修改日期：
 * <br/><li>修改内容：
 * <br/><li>版权: Copyright (c) 2008 运达科技公司
 * <br/><code><pre>
 * &nbsp; 
 * &nbsp; 示例代码：
 * &nbsp; 
 * &nbsp; //依赖注入：工作组组件实例对象
 * &nbsp; @Resource
 * &nbsp; private IOmGroupManager omGroupManager;
 * &nbsp; 
 * &nbsp; //调用接口方法，根据业务功能需要调用接口获取工作组数据
 * &nbsp; omGroupManager.getModelById("141");
 * &nbsp; 
 * </pre></code> 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IOmGroupManager {
	
	/* 工作组查询Sql文件 */
	final String XMLNAME_GROUP = "jcgl-group:";
	
	//========================================实体方式============================================
	
	/**
	 * <br/><li>说明： 根据ID查询工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupId 工作组Id
	 * @return 工作组实体
	 */
	public OmGroup getModelById(Long groupId);
	
	/**
	 * <br/><li>说明： 根据工作组名称精确查询工作组信息(如出现多条匹配记录,则取第一条)
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param name 工作组名称
	 * @return 工作组实体列表
	 */
	public OmGroup findByName(String name);
	
	/**
	 * <br/><li>说明： 根据工作组ID序列,查询唯一对应的工作组. groupseq的格式示例: ".0.7.146"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupSeq 工作组Id序列
	 * @return 工作组实体
	 */
	public OmGroup findBySeq(String groupSeq);
	
	/**
	 * <br/><li>说明： 根据岗位ID查询其所属工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param positionId 岗位Id
	 * @return 工作组实体
	 */
	public OmGroup findByPosId(Long positionId);
	
	/**
	 * <br/><li>说明： 根据员工ID查询其所属工作组信息
	 * <br/><li>创建人：
	 * <br/><li>创建日期：
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param empId 人员Id
	 * @return 工作组实体
	 */
	public OmGroup findByEmpId(Long empId);
	
	/**
	 * <br/><li>说明： 根据工作组ID,查询其上级工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupId 工作组Id
	 * @return 工作组实体
	 */
	public OmGroup findUpById(Long groupId);
	
	//========================================集合方式============================================
	
	/**
	 * <br/><li>说明：根据多个工作组Id,查询与对应的工作组
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个orgId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupIds 工作组id字符串
	 * @return 工作组实体列表
	 */
	public List<OmGroup> findByIds(String groupIds);
	
	/**
	 * <br/><li>说明：根据工作组Id数组,查询匹配的工作组信息列表
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param ids 工作组Id数组
	 * @return 工作组实体列表
	 */
	public List<OmGroup> findByIdArys(Serializable... ids);
	
	/**
	 * <br/><li>说明： 根据名称模糊查询工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param name 查询参数
	 * @return 工作组实体列表
	 */
	public List<OmGroup> findByResName(String name);

	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配, 支持的数据类型有NUMBER,VARCHAR2,CHAR,INTEGER</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
     * @param field 字段名
     * @param value 字段值
     * @return 工作组实体列表
     */
	public List<OmGroup> findByField(String field, String value);
	
	/**
	 * <br/><li>说明： 根据入参查询groupseq开头与之相同的工作组. orgseq的格式示例: ".0.7.146"
	 * <br/><li><font color=red>注*：查询的方案是 groupseq like '[param]%'形式</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupSeq 工作组Id序列
	 * @return 工作组实体列表
	 */
	public List<OmGroup> findByResSeq(String groupSeq);
	
	/**
	 * <br/><li>说明： 根据工作组ID,查询其的下一级工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupId 工作组Id
	 * @return 工作组实体列表
	 */
	public List<OmGroup> findChildsById(Long groupId);
	
	/**
	 * <br/><li>说明： 根据工作组ID,查询其下属所有子工作组
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupId 工作组Id
	 * @param hasSelf 是否包含参数groupId本身
	 * @return 工作组实体列表
	 */
	public List<OmGroup> findAllChilds(Long groupId, boolean hasSelf);
	
	/**
	 * <br/><li>说明：工作组的QBE查询,非分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-29
	 * <br/><li>修改人：谭诚 
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param group 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 工作组对象集合
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; OmGroup omGroup = new OmGroup();
	 * &nbsp; omGroup.setGroupname("XXX");  
	 * &nbsp;
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("grouptype"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp; 
	 * &nbsp; //调用查询
	 * &nbsp; List <OmGroup> list = omGroupManager.findByEntity(omGroup, orders, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public List<OmGroup> findByEntity(OmGroup group, Order [] orders, Boolean isExact);
	
	/**
	 * <br/><li>说明： 根据工作组ID查询其它同级工作组信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @return 工作组实体列表
	 */
	public List<OmGroup> findOtherById(Long groupId);
	
	/**
	 * <li>说明：查询根级工作组
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 工作组实体列表
	 */
	public List<OmGroup> findRoot();
	
	
	//========================================分页方式============================================
	
	/**
	 * <br/><li>说明：根据多个工作组Id,查询与对应的工作组,并提供分页支持
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个orgId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupIds 工作组id字符串
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 工作组实体分页列表
	 */
	public Page<OmGroup> findByIds(String groupIds, Integer start, Integer limit);
	
	/**
	 * <br/><li>说明： 根据工作组名称模糊查询对应的工作组信息, 并提供分页支持
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param name 工作组名称
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 工作组实体分页列表
	 */
	public Page<OmGroup> findByResName(String name, Integer start, Integer limit);
	
	/**
	 * <br/><li>说明：自定义单字段查询,并分页显示结果
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配, 支持的数据类型有NUMBER,VARCHAR2,CHAR,INTEGER</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
     * @param field 字段名
     * @param value 字段值
	 * @param start 开始行
	 * @param limit 每页最大记录数
     * @return 工作组实体分页列表
     */
	public Page<OmGroup> findByField(String field, String value, Integer start, Integer limit);
	
	/**
	 * <br/><li>说明： 根据工作组ID,查找其下一级工作组信息,并分页显示结果
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupId 工作组Id
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 工作组实体分页列表
	 */
	public Page<OmGroup> findChildsById(Long groupId, Integer start, Integer limit);
	
	/**
	 * <br/><li>说明： 根据工作组ID,查询其下属所有子工作组,并分页显示结果
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param groupId 工作组Id
	 * @param hasSelf 是否包含参数groupId本身
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @return 工作组实体分页列表
	 */
	public Page<OmGroup> findAllChilds(Long groupId, boolean hasSelf, Integer start, Integer limit);
	
	/**
	 * <br/><li>说明：工作组的QBE查询,支持分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-5
	 * <br/><li>修改人：谭诚 
	 * <br/><li>修改日期：2013-8-9
	 * <br/><li>修改内容：修改为支持缓存查询方式
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 工作组分页对象
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; OmGroup omGroup = new OmGroup();   
	 * &nbsp; omGroup.setGroupname("XXX");       
	 * &nbsp; 
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("grouptype"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp;
	 * &nbsp; //构建查询对象
	 * &nbsp; SearchEntity <OmGroup> entity = new SearchEntity<OmGroup>(); 
	 * &nbsp; entity.setEntity(omGroup);       
	 * &nbsp; entity.setStart(start);       
	 * &nbsp; entity.setLimit(limit);       
	 * &nbsp; entity.setOrders(orders);     
	 * &nbsp;  
	 * &nbsp; //调用查询
	 * &nbsp; Page <OmGroup> page = omGroupManager.findByEntity(entity, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public Page<OmGroup> findByEntity(SearchEntity <OmGroup> searchEntity, Boolean isExact);
}
