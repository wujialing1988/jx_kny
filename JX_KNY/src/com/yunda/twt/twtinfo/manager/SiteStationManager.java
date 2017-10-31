package com.yunda.twt.twtinfo.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.EntityUtil;
import com.yunda.twt.twtinfo.entity.SiteStation;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：SiteStation业务类,台位
 * <li>创建人：程锐
 * <li>创建日期：2015-02-06
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "siteStationManager")
public class SiteStationManager extends JXBaseManager<SiteStation, SiteStation> {
    
    /**
     * <li>说明：批量保存
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 台位map对象列表
     * @throws Exception  
     */
    public void saveOrUpdateList(List<Map> list) throws Exception {
        List<SiteStation> stationList = new ArrayList<SiteStation>();
        if (list == null || list.size() < 1)
            return;
        for (int i = 0; i < list.size(); i++) {
            SiteStation station = new SiteStation();
            Map listMap = list.get(i);
            if (listMap.get("idx") == null)
                continue;
            station.setIdx(listMap.get("idx") != null ? listMap.get("idx").toString() : "");
            if (listMap.get("siteID") == null)
                continue;
            station.setSiteID(listMap.get("siteID") != null ? listMap.get("siteID").toString() : "");
            station.setStationCode(listMap.get("stationCode") != null ? listMap.get("stationCode").toString() : "");
            station.setStationName(listMap.get("stationName") != null ? listMap.get("stationName").toString() : "");
            station.setWhName(listMap.get("whName") != null ? listMap.get("whName").toString() : "");
            station.setTrackName(listMap.get("trackName") != null ? listMap.get("trackName").toString() : "");
            stationList.add(station);
        }
        getDaoUtils().getHibernateTemplate().saveOrUpdateAll(stationList);
    }
    
    /**
     * <li>说明：根据台位图编码和siteID获取台位对象
     * <li>创建人：程锐
     * <li>创建日期：2015-5-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param stationCode 台位图编码
     * @param siteID siteID
     * @return 台位对象
     */
    @SuppressWarnings("unchecked")
    public SiteStation getStationByCodeAndSite(String stationCode, String siteID) {
        SiteStation siteStation = new SiteStation();
        siteStation.setStationCode(stationCode);
        siteStation.setSiteID(EntityUtil.findSysSiteId(null));
        List<SiteStation> list = daoUtils.getHibernateTemplate().findByExample(siteStation);
        if (list == null || list.size() < 1)
            return null;
        return list.get(0); 
    }
}
