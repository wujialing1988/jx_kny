package com.yunda.jxpz.utils;

import org.springframework.web.context.ContextLoader;

import com.yunda.frame.util.systemsite.ISystemSiteManager;

/**
 * <li>标题：系统站点获取工具类
 * <li>说明：
 * <li>创建人：张凡
 * <li>创建时间：2014-2-20
 * <li>修改人：
 * <li>修 改时间：
 * <li>修改内容：
 * @author PEAK-CHEUNG
 *
 */
public class SysSiteUtil {
    
    /**
     * <li>方法说明：根据当前用户获取所属机构siteID 
     * <li>方法名称：findSiteByCurrentUser
     * <li>@return
     * <li>return: String
     * <li>创建人：张凡
     * <li>创建时间：2014-2-20 下午04:41:17
     * <li>修改人：
     * <li>修改内容：
     */
    public static String findSiteByCurrentUser(){
        ISystemSiteManager manager = (ISystemSiteManager)ContextLoader.getCurrentWebApplicationContext().getBean("systemSiteManager");
        return manager.findSysSiteIdByLoginUser();
    }
}
