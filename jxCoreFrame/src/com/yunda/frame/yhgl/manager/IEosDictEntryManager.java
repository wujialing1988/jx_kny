package com.yunda.frame.yhgl.manager;

import java.util.List;

import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.yhgl.entity.EosDictEntry;


import org.hibernate.criterion.Order;

/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 业务字典项数据查询功能接口
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
 * &nbsp; //依赖注入：业务字典项组件实例对象
 * &nbsp; @Resource
 * &nbsp; private IEosDictEntryManager eosDictEntryManager;
 * &nbsp; 
 * &nbsp; //调用接口方法，根据业务功能需要调用接口获取业务字典项数据
 * &nbsp; eosDictEntryManager.findCacheEntry("141");
 * &nbsp; 
 * </pre></code> 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IEosDictEntryManager {

	/** 数据字典分类查询sql文件名称  */
	final String XMLNAME_DICT = "jcgl-dict:";
	
	/**
	 * <li>说明：根据业务字典分类,查询业务字典项实体
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-21
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dicttypeid 业务字典分类ID
	 * @return 业务字典项实体
	 */
	public List <EosDictEntry> findCacheEntry(String dicttypeid);
	
	/**
	 * <li>说明：根据业务字典项ID及业务字典分类ID,查询业务字典项实体
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-22
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param dictid 业务字典项ID
	 * @param dicttypeid 业务字典分类ID
	 * @return 业务字典项实体
	 */
	public EosDictEntry findCacheEntry(String dictid,String dicttypeid);
	
	/**
	 * <li>说明：业务字典项的QBE查询,支持分页方式
	 * <br/><li><font color=red>注*：QBE查询条件对主键字段无效,而Eos_Dict_Entry表采用双主键,即dicttypeid和dictid列不能作为查询条件</font>
	 * <br/><li>创建日: 谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 业务字典项分页对象
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; EosDictEntry eosDictEntry = new EosDictEntry();   
	 * &nbsp; eosDictEntry.setDictname("XXX");       
	 * &nbsp; 
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("sortno"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp;
	 * &nbsp; //构建查询对象
	 * &nbsp; SearchEntity <EosDictEntry> entity = new SearchEntity<EosDictEntry>(); 
	 * &nbsp; entity.setEntity(eosDictEntry);       
	 * &nbsp; entity.setStart(start);       
	 * &nbsp; entity.setLimit(limit);       
	 * &nbsp; entity.setOrders(orders);     
	 * &nbsp;  
	 * &nbsp; //调用查询
	 * &nbsp; Page <EosDictEntry> page = eosDictEntryManager.findByEntity(entity, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public Page <EosDictEntry> findByEntity(SearchEntity <EosDictEntry> searchEntity, Boolean isExact);
	
	/**
	 * <br/><li>说明：业务字典项的QBE查询,非分页方式
	 * <br/><li><font color=red>注*：QBE查询条件对主键字段无效,而Eos_Dict_Entry表采用双主键,即dicttypeid和dictid列不能作为查询条件</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-23
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param entry 查询实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 业务字典项对象集合
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; EosDictEntry eosDictEntry = new EosDictEntry();
	 * &nbsp; eosDictEntry.setDictname("XXX");  
	 * &nbsp;
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("sortno"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp; 
	 * &nbsp; //调用查询
	 * &nbsp; List <EosDictEntry> list = eosDictEntryManager.findByEntity(eosDictEntry, orders, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public List <EosDictEntry> findByEntity(EosDictEntry entry, Order [] orders, Boolean isExact);
	
	/**
	 * 
	 * <li>说明：根据车型和修程获得默认承修部门
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param  xcid 车型ID
	 * @param  traintype 车型
	 * @return 业务字典项实体
	 */
	public EosDictEntry getdept(String xcid,String traintype) throws Exception;
	
	/**
	 * 
	 * <li>说明： 查询落修的默认设置
	 * <li>创建人：谭诚
	 * <li>创建日期：2013-8-22
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @return 业务字典项实体
	 */
	public EosDictEntry getDefaultMethod();
	
	/**
	 * <br/><li>说明：获取业务字典的根层节点
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-12-13
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @return 业务字典分类实体列表
	 */
	public List <EosDictEntry> findRoots(String dicttypeid);
	
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
	public List <EosDictEntry> findChildsByIds(String dicttypeid, String dictid);
    
    /**
     * <li>方法名：findByTypeId
     * <li>@param dicttypeid
     * <li>@return
     * <li>返回类型：List<EosDictEntry>
     * <li>说明：根据hql语句查询相应的字典数据
     * <li>创建人：王开强
     * <li>创建日期：2011-4-19
     * <li>修改人： 谭诚
     * <li>修改日期： 2013-08-22
     * <li>修改内容: 添加查询缓存支持
     */
    @SuppressWarnings("unchecked")
    public List<EosDictEntry> findToList(String hql);
}
