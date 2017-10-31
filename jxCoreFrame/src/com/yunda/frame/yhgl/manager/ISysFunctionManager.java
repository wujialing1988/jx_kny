/**
 * 
 */
package com.yunda.frame.yhgl.manager;

import java.util.List;

import com.yunda.frame.yhgl.entity.AcFunction;

/**
 * <br/><li>标题: 机车检修管理信息系统
 * <br/><li>说明: 系统应用功能查询接口
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
 * &nbsp; //依赖注入：应用功能实例对象
 * &nbsp; @Resource
 * &nbsp; private ISysFunctionManager sysFunctionManager;
 * &nbsp; 
 * &nbsp; //调用接口方法，根据业务功能需要调用接口获取应用功能数据
 * &nbsp; sysFunctionManager.findRoot();
 * &nbsp; 
 * </pre></code> 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface ISysFunctionManager {
	
	/** 功能查询sql文件名称  */
	final String XMLNAME_FUNCTION = "jcgl-function:";
	
	/**
	 * <br/><li>说明： 根据角色ID集合，以及系統應用的Code，獲取該系統應用下與入參角色ID對應的應用功能
	 * <br/><li>创建人：谭诚
	 * <br/><li>创建日期：2014-6-3
	 * <br/><li>修改人： 
	 * <br/><li>修改日期：
	 * <br/><li>修改内容：
	 * @param roleIdList 角色ID集合
	 * @param appCode 應用系統編號
	 * @return 角色ID集合
	 */
	public List<AcFunction> findByOperatorIdAndAppCode(List <Object> roleIdList, String appCode);
    
    /** 
     * <li>说明：获取指定系统应用下对应功能名称的应用功能
     * <li>创建人：程锐
     * <li>创建日期：2015-2-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param funcname 应用功能名称
     * @param appcode 应用code
     * @return 应用功能实体
     */
    public AcFunction findByFuncnameAndAppcode(String funcname, String appcode);
}
