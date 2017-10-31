package com.yunda.twt.twtinfo.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.StringUtil;
import com.yunda.twt.twtinfo.entity.SiteTrack;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：SiteTrack业务类,股道
 * <li>创建人：程锐
 * <li>创建日期：2015-02-06
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "siteTrackManager")
public class SiteTrackManager extends JXBaseManager<SiteTrack, SiteTrack> {
    
    /**
     * <li>说明：批量保存
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 股道map对象列表
     * @throws Exception  
     */
    public void saveOrUpdateList(List<Map> list) throws Exception {
        List<SiteTrack> stackList = new ArrayList<SiteTrack>();
        if (list == null || list.size() < 1)
            return;
        for (int i = 0; i < list.size(); i++) {
            SiteTrack track = new SiteTrack();
            Map listMap = list.get(i);
            if (listMap.get("idx") == null)
                continue;
            track.setIdx(listMap.get("idx").toString());
            track.setSiteID(listMap.get("siteID") != null ? listMap.get("siteID").toString() : "");
            track.setTrackCode(listMap.get("trackCode") != null ? listMap.get("trackCode").toString() : "");
            track.setTrackName(listMap.get("trackName") != null ? listMap.get("trackName").toString() : "");
            stackList.add(track);
        }
        getDaoUtils().getHibernateTemplate().saveOrUpdateAll(stackList);
    }
    
    /**
     * <li>说明：根据站场标示和股道名称获取股道对象
     * <li>创建人：程锐
     * <li>创建日期：2015-11-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param siteID 站场标示
     * @param trackName 股道名称
     * @return 股道对象
     */
    @SuppressWarnings("unchecked")
    public SiteTrack getTrackBySiteIDAndName(String siteID, String trackName) {
        if (StringUtil.isNullOrBlank(siteID))
            return null;
        if (StringUtil.isNullOrBlank(trackName))
            return null;
        Map paramMap = new HashMap<String, String>();
        paramMap.put("siteID", siteID);
        paramMap.put("trackName", trackName);
        return getTrack(paramMap);
    }     
    
    /**
     * <li>说明：获取股道对象
     * <li>创建人：程锐
     * <li>创建日期：2015-3-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param paramMap 查询参数map key：字段名 value:字段值
     * @return 股道对象
     */
    private SiteTrack getTrack(Map paramMap) {
        StringBuilder hql = new StringBuilder();
        hql.append("from SiteTrack where 1 = 1 ");
        if(paramMap.get("siteID") != null){
            hql.append(" and siteID = '"+ paramMap.get("siteID").toString() + "'");
        }
        if(paramMap.get("trackName") != null){
            hql.append(" and trackName = '"+ paramMap.get("trackName").toString() + "'");
        }
        //.append(CommonUtil.buildParamsHql(paramMap));
        return (SiteTrack) daoUtils.findSingle(hql.toString());
    }
}
