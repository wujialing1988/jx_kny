package com.yunda.frame.yhgl.manager;

import java.util.List;

import org.hibernate.criterion.Order;


import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.yhgl.entity.OmPosition;

/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 岗位数据查询功能接口
 * <br/><li>创建人：谭诚
 * <br/><li>创建日期：2013-8-23
 * <br/><li>修改人: 
 * <br/><li>修改日期：
 * <br/><li>修改内容：
 * <br/><li>版权: Copyright (c) 2008 运达科技公司
 * <br/><code><pre>
 * &nbsp; 
 * &nbsp; 示例代码：
 * &nbsp; 
 * &nbsp; //依赖注入：岗位组件实例对象
 * &nbsp; @Resource
 * &nbsp; private IOmPositionManager omPositionManager;
 * &nbsp; 
 * &nbsp; //调用接口方法，根据业务功能需要调用接口获取岗位数据
 * &nbsp; omPositionManager.findByField("posicode","XXXX");
 * &nbsp; 
 * </pre></code> 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IOmPositionManager {
	/** 岗位查询sql文件名称  */
	final String XMLNAME_POS = "jcgl-position:";
	
	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配, 支持的数据类型有NUMBER,VARCHAR2,CHAR,INTEGER</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
     * @param field 字段名
     * @param value 字段值
     * @return 岗位实体列表
     */
	public List<OmPosition> findByField(String field, String value);
	
	/**
	 * <br/><li>说明： 根据岗位Id查询唯一对应的岗位.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param positionid 岗位id
	 * @return 岗位实体
	 */
	public OmPosition getModelById(Long positionid);
	
	/**
	 * <br/><li>说明： 根据岗位编号精确查找与其对应的唯一岗位
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容:
	 * @param posicode 岗位编号
	 * @return 岗位实体
	 */
	public OmPosition findByCode(String posicode);
	
	/**
	 * <br/><li>说明： 根据岗位序列查询唯一对应的岗位. positionseq的格式示例: ".0.7.146"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param positionseq 岗位Id序列
	 * @return 岗位实体
	 */
	public OmPosition findBySeq(String positionseq);
	
	/**
	 * <br/><li>说明：根据操作员ID获取其所属的岗位
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param operatorid 操作员id
	 * @return 岗位实体
	 */
	public OmPosition findByOperatorid(Long operatorid);
	
	/**
	 * <br/><li>说明：根据人员ID获取其所属岗位
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-11-17
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param empid 人员id
	 * @param isMain 是否主岗位
	 * @return 岗位实体
	 */
	public OmPosition findByEmpid(Long empid, String isMain);
	
	/**
	 * <br/><li>说明：根据人员ID获取其所属岗位
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2014-1-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param empid 人员id
	 * @return 岗位实体
	 */
	public OmPosition findByEmpid(Long empid);
	
	/**
	 * <br/><li>说明：岗位的QBE查询,支持分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 岗位分页对象
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; OmPosition omPosition = new OmPosition();   
	 * &nbsp; omPosition.setPosicode("XXX");       
	 * &nbsp; 
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("posiname"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp;
	 * &nbsp; //构建查询对象
	 * &nbsp; SearchEntity <OmPosition> entity = new SearchEntity<OmPosition>(); 
	 * &nbsp; entity.setEntity(omPosition);       
	 * &nbsp; entity.setStart(start);       
	 * &nbsp; entity.setLimit(limit);       
	 * &nbsp; entity.setOrders(orders);     
	 * &nbsp;  
	 * &nbsp; //调用查询
	 * &nbsp; Page <OmPosition> page = omPositionManager.findByEntity(entity, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public Page <OmPosition> findByEntity(SearchEntity <OmPosition> searchEntity, Boolean isExact);
	
	/**
	 * <br/><li>说明：岗位的QBE查询,非分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param omPosition 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 岗位对象集合
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; OmPosition omPosition = new OmPosition();
	 * &nbsp; omPosition.setPosicode("XXX");  
	 * &nbsp;
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("posiname"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp; 
	 * &nbsp; //调用查询
	 * &nbsp; List <OmPosition> list = omPositionManager.findByEntity(omPosition, orders, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public List <OmPosition> findByEntity(OmPosition omPosition, Order [] orders, Boolean isExact);
	
	/**
	 * 
	 * <li>说明：查询组织机构下所属岗位
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-10-31
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param orgid 组织机构id
	 * @return 岗位列表
	 * @throws 抛出异常列表
	 */
	public List <OmPosition> findPertainToOrg(Long orgid);
	
	/**
	 * <li>说明：查询工作组下属岗位
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 工作组id
	 * @return 岗位列表
	 * @throws 抛出异常列表
	 */
	public List <OmPosition> findPertainToWorkGroup(Long groupid);
	
	/**
	 * <li>说明：查询工作组下属岗位(含子工作组下的岗位及岗位下的子岗位)
	 * <li>创建人：谭诚
	 * <li>创建日期：2014-04-10
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param groupid 工作组id
	 * @return 岗位列表
	 * @throws 抛出异常列表
	 */
	public List <OmPosition> findAllPertainToWorkGroup(Long groupid);
	
	/**
	 * <li>说明：根据岗位ID查询其下级岗位
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-11-5
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param positionid 岗位ID
	 * @return 岗位列表
	 * @throws 抛出异常列表
	 */
	public List <OmPosition> findChildPosition(Long positionid);
}
