package com.yunda.frame.yhgl.manager;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Order;

import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.yhgl.entity.AcMenu;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 系统菜单的查询接口
 * <li>创建人：谭诚
 * <li>创建日期：2013-8-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 * <br/><code><pre>
 * &nbsp; 
 * &nbsp; 示例代码：
 * &nbsp; 
 * &nbsp; //依赖注入：菜单组件实例对象
 * &nbsp; @Resource
 * &nbsp; private IAcMenuManager acMenuManager;
 * &nbsp; 
 * &nbsp; //调用接口方法，根据业务功能需要调用接口获取菜单数据
 * &nbsp; List <AcMenu> list = acMenuManager.findRoot();
 * &nbsp; 
 * </pre></code> 
 */
public interface IAcMenuManager {

	/** 菜单查询sql文件名称  */
	final String XMLNAME_MENU = "jcgl-menu:";
	
	/**
	 * <br/><li>说明：菜单QBE查询,非分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menu 含查询条件的实体
	 * @param orders 排序参数
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 对象集合
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; AcMenu menu = new AcMenu();
	 * &nbsp; menu.setOrgcode("ABC");
	 * &nbsp;
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("menuid"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp; 
	 * &nbsp; //调用查询
	 * &nbsp; List <AcMenu> list = acMenuManager.findByEntity(menu, orders, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public List<AcMenu> findByEntity(AcMenu menu, Order[] orders, Boolean isExact);
	
	/**
	 * <br/><li>说明：QBE查询,支持分页方式
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param searchEntity 查询实体
	 * @param isExact 是否精确匹配, true:是; false:否
	 * @return 分页对象
	 * <br/><code><pre>
	 * &nbsp; 
	 * &nbsp; 调用示例:
     * &nbsp; 
     * &nbsp; //构建查询条件
	 * &nbsp; AcMenu menu = new AcMenu();   
	 * &nbsp; menu.setMenuname("ABC");       
	 * &nbsp; 
	 * &nbsp; //指定排序字段
	 * &nbsp; Order _order = Order.asc("menuid"); 
	 * &nbsp; Order [] orders = new Order[]{_order};
	 * &nbsp;
	 * &nbsp; //构建查询对象
	 * &nbsp; SearchEntity <AcMenu> entity = new SearchEntity<AcMenu>(); 
	 * &nbsp; entity.setEntity(menu);       
	 * &nbsp; entity.setStart(start);       
	 * &nbsp; entity.setLimit(limit);       
	 * &nbsp; entity.setOrders(orders);     
	 * &nbsp;  
	 * &nbsp; //调用查询
	 * &nbsp; Page <AcMenu> page = acMenuManager.findByEntity(entity, true);
	 * &nbsp; 
	 * </pre></code> 
	 */
	public Page<AcMenu> findByEntity(SearchEntity<AcMenu> searchEntity, Boolean isExact);
	
	/**
	 * <br/><li>说明： 查询根层模块菜单
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期:
	 * <br/><li>修改内容：
	 * @return 菜单实体列表
	 */
	public List<AcMenu> findRoot();
	
	/**
	 * <br/><li>说明： 根据参数查询中层或者末层菜单
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期:
	 * <br/><li>修改内容：
	 * @param isLeaf 是否叶子菜单, y是n否
	 * @return 菜单实体列表
	 */
	public List<AcMenu> findBranchOrLeaf(String isLeaf);
	
	/**
	 * <br/><li>说明： 递归查询入参菜单id下属所有子菜单
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期:
	 * <br/><li>修改内容：
	 * @param menuid 菜单Id
	 * @param hasSelf 是否包含参数menuid本身
	 * @return 菜单实体列表
	 */
	public List<AcMenu> findAllChilds(String menuid, boolean hasSelf);
	
	/**
	 * <br/><li>说明： 分页递归查询入参菜单id下属所有子菜单
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期:
	 * <br/><li>修改内容：
	 * @param menuid 菜单Id
	 * @param hasSelf 是否包含参数menuid本身
	 * @return 菜单实体分页列表
	 */
	public Page<AcMenu> findAllChilds(String menuid, boolean hasSelf, Integer start, Integer limit);
	
	/**
	 * <br/><li>说明： 根据菜单Id查询唯一对应的菜单项.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuid 菜单id
	 * @return 菜单实体
	 */
	public AcMenu getModelById(String menuid);
	
	/**
	 * <br/><li>说明：根据多个菜单Id,查询与对应的菜单项
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个menuid, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuIds 菜单id字符串
	 * @return 菜单实体列表
	 */
	public List <AcMenu> findByIds(String menuIds);
	
	/**
	 * <br/><li>说明：根据多个菜单Id,查询与对应的菜单项
	 * <br/><li><font color=red>注*：入参是以","(英文逗号)分隔的多个menuid, 参数格式如: 1101,1105,1108,...</font>
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuIds 菜单id字符串
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 菜单实体列表
	 */
	public Page<AcMenu> findByIds(String menuIds,Integer start,Integer limit);
	
	/**
	 * <br/><li>说明：自定义单字段查询
	 * <br/><li><font color=red>注*：字段名和字段值均自由定义, 但只支持精确匹配, 不提供Like方式的模糊匹配</font>
	 * <br/><li>创建人：谭诚
     * <br/><li>创建日期：2013-8-20
     * <br/><li>修改人：
	 * <br/><li>修改日期：
     * <br/><li>修改内容：
     * @param field 字段名
     * @param value 字段值
     * @return 菜单实体列表
     */
	public List<AcMenu> findByField(String field, String value);
	
	/**
	 * <br/><li>说明： 根据菜单的名称精确查找与其对应的唯一菜单项
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuname 菜单名称
	 * @return 菜单实体
	 */
	public AcMenu findByName(String menuname);
	
	/**
	 * <br/><li>说明： 根据菜单名称进行模糊查询(like '%...%'方式),并提供分页支持
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuname 菜单名称
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 带分页的菜单实体列表
	 */
	public Page<AcMenu> findByName(String menuname,Integer start,Integer limit);
	
	/**
	 * <br/><li>说明： 根据menuseq查询唯一对应的菜单. menuseq的格式示例: ".0.7.146"
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuseq 菜单Id序列
	 * @return 菜单实体
	 */
	public AcMenu findBySeq(String menuseq);
	
	/**
	 * <br/><li>说明： 查找入参menuid的下一级菜单信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuid 菜单Id
	 * @return 菜单实体列表
	 */
	public List<AcMenu> findChildsById(String menuid);
	
	/**
	 * <br/><li>说明： 以分页方式查找入参menuid的下一级菜单信息
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param menuid 菜单Id
	 * @param start 开始行
	 * @param limit 每页最大记录数
	 * @return 带分页的菜单实体列表
	 */
	public Page<AcMenu> findChildsById(String menuid,Integer start,Integer limit);
	
	/**
	 * <br/><li>说明：根据菜单Id数组,查询匹配的菜单信息列表
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2013-8-20
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param ids 菜单Id数组
	 * @return 菜单实体列表
	 */
	public List <AcMenu> findByIdArys(Serializable... ids);
}
