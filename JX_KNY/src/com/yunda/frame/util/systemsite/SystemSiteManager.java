/**
 * 
 */
package com.yunda.frame.util.systemsite;

import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

import com.yunda.jxpz.workplace.manager.WorkPlaceToOrgManager;

/**
 * <li>标题: 系统站点siteid获取类
 * <li>说明：通过接口的实现类按照整备系统的siteid生成规则获取siteid. 实现了ISystemSiteManager业务接口，接口定义在JxCoreFrame中
 * <li>创建人：谭诚
 * <li>创建日期：2013-12-9
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部整备系统项目组
 * @version 2.0
 */
@Service(value="systemSiteManager")
public class SystemSiteManager implements ISystemSiteManager {

	/**
	 * 说明：根据当前系统登录用户，生成该用户所属机构的SiteID
	 * @return siteId
	 */
	public String findSysSiteIdByLoginUser(){
	    WorkPlaceToOrgManager manager = (WorkPlaceToOrgManager)ContextLoader.getCurrentWebApplicationContext().getBean("workPlaceToOrgManager");
        return manager.getWorkPlaceCodeByCurrentUser();
	}
	/**
	 * 说明：根据当前系统登录用户，生成该用户所属机构的site
	 * @return siteId
	 */
	public String findSysSiteNameByLoginUser(String siteID){
	    WorkPlaceToOrgManager manager = (WorkPlaceToOrgManager)ContextLoader.getCurrentWebApplicationContext().getBean("workPlaceToOrgManager");
        return manager.getWorkPlaceNameByCurrentUser(siteID);
	}
}
