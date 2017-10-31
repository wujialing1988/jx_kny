/**
 * 
 */
package com.yunda.frame.util.systemsite;


/**
 * <li>标题: 系统站点siteid获取接口
 * <li>说明：ISystemSiteManager业务接口，接口定义在JxCoreFrame中，实现类SystemSiteManager在jx中，
 *          通过接口的实现类按照整备系统的siteid生成规则获取siteid
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部整备系统项目组
 * @version 2.0
 */
public interface ISystemSiteManager {
	
	/**
	 * 说明：根据当前系统登录用户，生成该用户所属机构的SiteID
	 * @return siteId
	 */
	public String findSysSiteIdByLoginUser();
	/**
	 * 说明：根据当前系统登录用户，生成该用户所属机构的site名称
	 * @return siteId
	 */
	public String findSysSiteNameByLoginUser(String siteID);
}
