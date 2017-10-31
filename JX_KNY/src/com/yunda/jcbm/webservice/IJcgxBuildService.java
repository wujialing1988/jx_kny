package com.yunda.jcbm.webservice;

/**
 * <li>标题：机车检修管理信息系统
 * <li>说明：机车组成
 * <li>创建人： 何东
 * <li>创建日期： 2016-5-19 上午11:10:16
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 */
public interface IJcgxBuildService {

	/**
	 * <li>方法名：根据车型及机车构型主键获取下级树节点列表
	 * <li>@param jsonObject json对象参数
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：
	 * <li>创建人：何东
	 * <li>创建日期：2016-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public String getJcgxBuildTree(String jsonObject) throws Exception;
	
	/**
	 * <li>方法名：通过分类编码获取故障现象数据
	 * <li>@param jsonObject json对象参数
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：
	 * <li>创建人：何东
	 * <li>创建日期：2016-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public String getFlbmFault(String jsonObject) throws Exception;
	
	/**
	 * <li>方法名：保存分类编码的故障现象
	 * <li>@param jsonObject json对象参数
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：
	 * <li>创建人：何东
	 * <li>创建日期：2016-5-16
	 * <li>修改人： 
	 * <li>修改日期：
	 */
	public String saveFlbmFault(String jsonObject) throws Exception;
    /**
     * <li>说明：根据车型及机车构型,机车组成获取下级树节点列表
     * <li>创建人：张迪
     * <li>创建日期：2016-9-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象参数
     * @return 分类名称树节点列表
     * @throws Exception
     */
     public String getJczcmcBuildTree(String jsonObject) throws Exception;
     /**
      * <li>说明：获取上级部件分类简称
      * <li>创建人：张迪
      * <li>创建日期：2016-9-9
      * <li>修改人： 
      * <li>修改日期：
      * <li>修改内容：
      * @param jsonObject json对象参数
      * @return 上级部件分类简称
      * @throws Exception
      */
     public String getSjbjList(String jsonObject) throws Exception;
     
     /**
      * <li>说明：获取所选部件位置分类简称
      * <li>创建人：张迪
      * <li>创建日期：2016-9-10
      * <li>修改人： 
      * <li>修改日期：
      * <li>修改内容：
      * @param jsonObject json对象参数
      * @return 部件分类简称
      * @throws Exception
      */public String getBjwzList(String jsonObject) throws Exception;
}
