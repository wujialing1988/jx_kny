package com.yunda.twt.twtinfo.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.twt.twtinfo.entity.SiteVideo;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：SiteVideo业务类,视频监控点
 * <li>创建人：程锐
 * <li>创建日期：2015-02-06
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "siteVideoManager")
public class SiteVideoManager extends JXBaseManager<SiteVideo, SiteVideo> {
    
    /**
     * <li>说明：批量保存
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param list 视频监控点map对象列表
     * @throws Exception  
     */
    public void saveOrUpdateList(List<Map> list) throws Exception {
        List<SiteVideo> videoList = new ArrayList<SiteVideo>();
        if (list == null || list.size() < 1)
            return;
        for (int i = 0; i < list.size(); i++) {
            SiteVideo video = new SiteVideo();
            Map listMap = list.get(i);
            if (listMap.get("idx") == null)
                continue;
            video.setIdx(listMap.get("idx") != null ? listMap.get("idx").toString() : "");
            video.setSiteID(listMap.get("siteID") != null ? listMap.get("siteID").toString() : "");
            video.setVideoCode(listMap.get("videoCode") != null ? listMap.get("videoCode").toString() : "");
            video.setVideoName(listMap.get("videoName") != null ? listMap.get("videoName").toString() : "");
            videoList.add(video);
        }
        getDaoUtils().getHibernateTemplate().saveOrUpdateAll(videoList);
    }
}
