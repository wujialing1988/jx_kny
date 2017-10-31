package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.List;

import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.yhgl.entity.AcOperator;

import org.hibernate.criterion.Order;

/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 操作员查询接口
 * <br/><li>创建人：谭诚
 * <br/><li>创建日期：2013-7-23
 * <br/><li>修改人: 
 * <br/><li>修改日期：
 * <br/><li>修改内容：
 * <br/><li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 * <br/><code><pre>
 * &nbsp; 
 * &nbsp; 示例代码：
 * &nbsp; 
 * &nbsp; //依赖注入：菜单组件实例对象
 * &nbsp; @Resource
 * &nbsp; private IAcOperatorManager acOperatorManager;
 * &nbsp; 
 * &nbsp; //调用接口方法，根据业务功能需要调用接口获取菜单数据
 * &nbsp; List <AcOperator> list = acOperatorManager.findByIds("092");
 * &nbsp; 
 * </pre></code> 
 */
public interface IAcOperatorManager {
	/** 操作员查询sql文件名称  */
	final String XMLNAME_OPE = "jcgl-ope:";
	
	/**
	 * <br/><li>说明： 通过人员Id查询操作员信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param userId 人员Id
	 * @return 操作员实体
	 */
	public AcOperator findByUserId(String userId);
	
	/**
	 * <br/><li>说明： 通过操作员Id查询操作员信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param operatorId 操作员Id
	 * @return 操作员实体
	 */
	public AcOperator getModelById(Long operatorId);
	
	/**
	 * <br/><li>说明：根据多个操作员Id,查询与对应的操作员
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个operatorid, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param opeIds 操作员id字符串
	 * @return 操作员实体列表
	 */
	public List <AcOperator> findByIds(String opeIds);
	
	/**
	 * <br/><li>说明：根据操作员Id数组,查询匹配的操作员列表
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-29
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param ids 操作员Id数组
	 * @return 操作员实体列表
	 */
	public List <AcOperator> findByIdArys(Serializable... ids);
	
	/**
	 * <br/><li>说明： 根据人员卡号查询操作员信息列表
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-25
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param cardNo 卡号
	 * @return 操作员实体列表
	 */
	public List<AcOperator> findByCardNo(String cardNo);
	
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
     * @return 操作员实体列表
     */
	public List<AcOperator> findByField(String field, String value);
	
	/**
	 * <br/><li>说明：操作员的QBE查询,支持分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-29
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 操作员分页对象
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; AcOperator acOperator = new AcOperator();   
	 * &nbsp; acOperator.setEmail("XXXXX@hotmail.com");       
	 * &nbsp; 
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("enddate"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp;
	 * &nbsp; //构建查询对象
	 * &nbsp; SearchEntity <AcOperator> entity = new SearchEntity<AcOperator>(); 
	 * &nbsp; entity.setEntity(acOperator);       
	 * &nbsp; entity.setStart(start);       
	 * &nbsp; entity.setLimit(limit);       
	 * &nbsp; entity.setOrders(orders);     
	 * &nbsp;  
	 * &nbsp; //调用查询
	 * &nbsp; Page <AcOperator> page = acOperatorManager.findByEntity(entity, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public Page <AcOperator> findByEntity(SearchEntity <AcOperator> searchEntity, Boolean isExact);
	
	/**
	 * <br/><li>说明：操作员的QBE查询,非分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-29
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param ope 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 人员对象集合
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; AcOperator acOperator = new AcOperator();
	 * &nbsp; acOperator.setEmail("XXXXX@hotmail.com");  
	 * &nbsp;
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("enddate"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp; 
	 * &nbsp; //调用查询
	 * &nbsp; List <AcOperator> list = acOperatorManager.findByEntity(acOperator, orders, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public List <AcOperator> findByEntity(AcOperator ope, Order [] orders, Boolean isExact);
}
