package com.yunda.twt.twtinfo.manager;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.twt.twtinfo.entity.Site;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：Site业务类,站场
 * <li>创建人：程锐
 * <li>创建日期：2015-02-06
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "siteManager")
public class SiteManager extends JXBaseManager<Site, Site> {
    
    /**
     * <li>说明：更新站场信息
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param site 站场实体
     */
    public void updateSite(Site site) {
        this.daoUtils.executUpdateOrDelete("delete from Site where siteID = '" + site.getSiteID() + "'");
        this.daoUtils.getHibernateTemplate().saveOrUpdate(site);
    }
}
