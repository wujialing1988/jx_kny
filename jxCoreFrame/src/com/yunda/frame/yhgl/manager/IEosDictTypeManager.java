package com.yunda.frame.yhgl.manager;

import java.util.List;

import org.hibernate.criterion.Order;

import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.yhgl.entity.EosDictType;

/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 业务字典分类数据查询功能接口
 * <br/><li>创建人：谭诚
 * <br/><li>创建日期：2013-8-21
 * <br/><li>修改人: 
 * <br/><li>修改日期：
 * <br/><li>修改内容：
 * <br/><li>版权: Copyright (c) 2008 运达科技公司
 * <br/><code><pre>
 * &nbsp; 
 * &nbsp; 示例代码：
 * &nbsp; 
 * &nbsp; //依赖注入：业务字典分类组件实例对象
 * &nbsp; @Resource
 * &nbsp; private IEosDictTypeManager eosDictTypeManager;
 * &nbsp; 
 * &nbsp; //调用接口方法，根据业务功能需要调用接口获取职务数据
 * &nbsp; eosDictTypeManager.findByField("dicttypename","XXX");
 * &nbsp; 
 * </pre></code> 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IEosDictTypeManager {
	/** 数据字典分类查询sql文件名称  */
	final String XMLNAME_DICT = "jcgl-dict:";
	
	/**
	 * <br/><li>说明：业务字典分类的QBE查询,支持分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 业务字典分类分页对象
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; EosDictType eosDictType = new EosDictType();   
	 * &nbsp; eosDictType.setDicttypename("XXX");       
	 * &nbsp; 
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("dicttypeid"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp;
	 * &nbsp; //构建查询对象
	 * &nbsp; SearchEntity <EosDictType> entity = new SearchEntity<EosDictType>(); 
	 * &nbsp; entity.setEntity(eosDictType);       
	 * &nbsp; entity.setStart(start);       
	 * &nbsp; entity.setLimit(limit);       
	 * &nbsp; entity.setOrders(orders);     
	 * &nbsp;  
	 * &nbsp; //调用查询
	 * &nbsp; Page <EosDictType> page = eosDictTypeManager.findByEntity(entity, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public Page <EosDictType> findByEntity(SearchEntity <EosDictType> searchEntity, Boolean isExact);
	
	/**
	 * <br/><li>说明：业务字典分类的QBE查询,非分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dictType 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 业务字典分类对象集合
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; EosDictType eosDictType = new EosDictType();
	 * &nbsp; eosDictType.setDicttypename("XXX");  
	 * &nbsp;
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("dicttypeid"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp; 
	 * &nbsp; //调用查询
	 * &nbsp; List <EosDictType> list = eosDictTypeManager.findByEntity(eosDictType, orders, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public List <EosDictType> findByEntity(EosDictType dictType, Order [] orders, Boolean isExact);
	
	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配, 支持的数据类型有NUMBER,VARCHAR2,CHAR,INTEGER</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-8-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
     * @param field 字段名
     * @param value 字段值
     * @return 业务字典分类实体列表
     */
	public List<EosDictType> findByField(String field, String value);
	
	/**
	 * <br/><li>说明： 根据业务字典分类Id查询唯一对应的业务字典分类.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dicttypeid 业务字典分类id
	 * @return 业务字典分类实体
	 */
	public EosDictType getModelById(String dicttypeid);
	
	/**
	 * <br/><li>说明： 根据业务字典分类的名称精确查找与其对应的唯一业务字典分类
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dicttypename 业务字典分类名称
	 * @return 业务字典分类实体
	 */
	public EosDictType findByName(String dicttypename);
	
	/**
	 * <br/><li>说明：根据多个业务字典分类的Id,查询与对应的业务字典分类
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个dicttypeid, 参数格式如: JCJX_LXSZ,JXGC_WORKSEQ_WORKCLASS,JCZL_PJSHSZ,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-27
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dicttypeid 业务字典分类id字符串
	 * @return 业务字典分类实体列表
	 */
	public List <EosDictType> findByIds(String dicttypeid);
	
	/**
	 * <br/><li>说明：获取业务字典的根层节点
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-12-11
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @return 业务字典分类实体列表
	 */
	public List <EosDictType> findRoots();
	
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
	public List <EosDictType> findChildsByIds(String dicttypeid);
}
