package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.List;

import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.yhgl.entity.OmDuty;

import org.hibernate.criterion.Order;

/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 职务管理数据查询功能接口
 * <br/><li>创建人：谭诚
 * <br/><li>创建日期：2013-8-14
 * <br/><li>修改人: 
 * <br/><li>修改日期：
 * <br/><li>修改内容：
 * <br/><li>版权: Copyright (c) 2008 运达科技公司
 * <br/><code><pre>
 * &nbsp; 
 * &nbsp; 示例代码：
 * &nbsp; 
 * &nbsp; //依赖注入：职务组件实例对象
 * &nbsp; @Resource
 * &nbsp; private IOmDutyManager omDutyManager;
 * &nbsp; 
 * &nbsp; //调用接口方法，根据业务功能需要调用接口获取职务数据
 * &nbsp; omDutyManager.findById("141");
 * &nbsp; 
 * </pre></code> 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IOmDutyManager {
	/** 职务查询sql文件名称  */
	final String XMLNAME_DUTY = "jcgl-duty:";
	
	/* 单实体查询 */
	/**
	 * <br/><li>说明： 根据职务Id查询唯一对应的职务信息.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyId 职务id
	 * @return 职务实体
	 */
	public OmDuty getModelById(Long dutyId);
	
	/**
	 * <br/><li>说明： 根据职务的编号精确查找与其对应的唯一职务
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyCode 职务编号
	 * @return 职务实体
	 */
	public OmDuty findByCode(String dutyCode);
	
	/**
	 * <br/><li>说明： 根据dutyseq精确查询唯一对应的职务. dutyseq的格式示例: ".0.7.146"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyseq 职务Id序列
	 * @return 职务实体
	 */
	public OmDuty findBySeq(String dutyseq);
	
	/**
	 * <br/><li>说明：根据人员Id查询其所属的职务
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param empId 人员id
	 * @return 职务实体
	 */
	public OmDuty findByEmpId(Long empId);
	
	/**
	 * <br/><li>说明：获得操作员Id入参直属的职务
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param operatorid 操作员id
	 * @return 职务实体
	 */
	public OmDuty findByOperator(Long operatorid);
	
	/* 实体列表查询 */
	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配, 支持的数据类型有NUMBER,VARCHAR2,CHAR,INTEGER</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
     * @param field 字段名
     * @param value 字段值
     * @return 职务实体列表
     */
	public List<OmDuty> findByField(String field, String value);
	
	/**
	 * <br/><li>说明：根据多个职务Id,查询与对应的职务
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个dutyId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyIds 职务id字符串
	 * @return 职务实体列表
	 */
	public List<OmDuty> findByIds(String dutyIds);
	
	/**
	 * <br/><li>说明： 根据职务ID,查找该职务的下一级职务信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyId 职务Id
	 * @return 职务实体列表
	 */
	public List<OmDuty> findChildsById(Long dutyId);
	
	/**
	 * <br/><li>说明： 根据职务ID,查询该职务的下属所有子职务
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyId 职务Id
	 * @param hasSelf 是否包含当前入参dutyId本身
	 * @return 职务实体列表
	 */
	public List<OmDuty> findAllChilds(Long dutyId, boolean hasSelf);
	
	/**
	 * <br/><li>说明：根据职务ID,查询其上级职务
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyId 职务id
	 * @return 职务实体列表
	 */
	public List<OmDuty> findUpperByDegree(Long dutyId);
	
	/**
	 * <br/><li>说明：根据职务Id数组,查询匹配的职务信息列表
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param ids 职务Id数组
	 * @return 职务实体列表
	 */
	public List<OmDuty> findByIdArys(Serializable... ids);
	
	/**
	 * <br/><li>说明：职务的QBE查询,非分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param duty 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 职务对象集合
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; OmDuty omDuty = new OmDuty();
	 * &nbsp; omDuty.setDutyid("XXX");  
	 * &nbsp;
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("dutycode"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp; 
	 * &nbsp; //调用查询
	 * &nbsp; List <OmDuty> list = omDutyManager.findByEntity(omDuty, orders, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public List <OmDuty> findByEntity(OmDuty duty, Order [] orders, Boolean isExact);
	
	/* 分页查询 */
	
	/**
	 * <br/><li>说明：根据多个职务Id,查询与对应的职务
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个dutyId, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-7-22
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyIds 职务id字符串
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 职务实体列表
	 */
	public Page findByIds(String dutyIds,Integer start,Integer limit);
	
	/**
	 * <br/><li>说明： 根据职务编号进行模糊查询(like '%...%'方式),并提供分页支持
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 谭诚
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyCode 职务编号
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 带分页的职务实体列表
	 */
	public Page findByCode(String dutyCode,Integer start,Integer limit);
	
	/**
	 * <br/><li>说明： 根据职务ID,查询该职务的下一级职务信息,并提供分页支持
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyId 职务Id
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 带分页的职务实体列表
	 */
	public Page findChildsById(Long dutyId,Integer start,Integer limit);
	
	/**
	 * <br/><li>说明： 根据职务Id,查询该职务下属所有子职务,并提供分页支持
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dutyId 职务Id
	 * @param hasSelf 是否包含参数dutyId本身
	 * @param start 开始行
	 * @param limit 每页记录数
	 * @return 职务实体列表
	 */
	public Page findAllChilds(Long dutyId, boolean hasSelf, Integer start,Integer limit);
	
	/**
	 * <br/><li>说明：职务的QBE查询,支持分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-14
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
	 * &nbsp; OmDuty omDuty = new OmDuty();   
	 * &nbsp; omDuty.setDutycode("XXX");       
	 * &nbsp; 
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("dutycode"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp;
	 * &nbsp; //构建查询对象
	 * &nbsp; SearchEntity <OmDuty> entity = new SearchEntity<OmDuty>(); 
	 * &nbsp; entity.setEntity(omDuty);       
	 * &nbsp; entity.setStart(start);       
	 * &nbsp; entity.setLimit(limit);       
	 * &nbsp; entity.setOrders(orders);     
	 * &nbsp;  
	 * &nbsp; //调用查询
	 * &nbsp; Page <OmDuty> page = omDutyManager.findByEntity(entity, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public Page <OmDuty> findByEntity(SearchEntity <OmDuty> searchEntity, Boolean isExact);
}
