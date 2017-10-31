/**
 * 
 */
package com.yunda.frame.yhgl.manager;

import java.util.List;

/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 角色查询功能接口
 * <br/><li>创建人：谭诚
 * <br/><li>创建日期：2014-6-3
 * <br/><li>修改人: 
 * <br/><li>修改日期：
 * <br/><li>修改内容：
 * <br/><li>版权: Copyright (c) 2008 运达科技公司
 * <br/><code><pre>
 * &nbsp; 
 * &nbsp; 示例代码：
 * &nbsp; 
 * &nbsp; //依赖注入：角色实例对象
 * &nbsp; @Resource
 * &nbsp; private IAcRoleManager acRoleManager;
 * &nbsp; 
 * &nbsp; //调用接口方法，根据业务功能需要调用接口获取角色数据
 * &nbsp; acRoleManager.findRoot();
 * &nbsp; 
 * </pre></code> 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IAcRoleManager {
	
	/** 角色查询sql文件名称  */
	final String XMLNAME_ROLE = "jcgl-role:";
	
	/**
	 * <br/><li>说明： 根据当前登录的操作员ID，获取该操作员对应的系统角色ID，包括操作员直接对应的角色以及通过组织机构、岗位、工作组和职务间接对应的角色.
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2014-6-3
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @return 角色ID集合
	 */
	public List <Object> findRoleIdByOperatorId(Long operatorid);
    
    /**
     * <li>说明：根据当前登录的操作员ID，获取该操作员对应的系统角色名称，包括操作员直接对应的角色以及通过组织机构、岗位、工作组和职务间接对应的角色.
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 人员ID
     * @return 角色名称集合
     */
    public List <Object> findRoleNameByOperatorId(Long operatorid);
    

}
