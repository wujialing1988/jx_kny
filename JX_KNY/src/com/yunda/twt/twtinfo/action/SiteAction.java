package com.yunda.twt.twtinfo.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;

import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.twt.asynchronouslogin.entity.TWTAsynchronmousLongin;
import com.yunda.twt.twtinfo.entity.Site;
import com.yunda.twt.twtinfo.manager.SiteManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明：Site控制器, 站场
 * <li>创建人：程锐
 * <li>创建日期：2015-02-06
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class SiteAction extends JXBaseAction<Site, Site, SiteManager> {
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：获取台位图url地址配置
     * <li>创建人：程锐
     * <li>创建日期：2015-12-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void getStationMapURL() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Site> list = this.manager.getDaoUtils().getHibernateTemplate().loadAll(Site.class);
            if (list == null || list.size() < 1)
                JSONUtil.write(this.getResponse(), new ArrayList());
            else
                JSONUtil.write(this.getResponse(), list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：台位图服务调用该接口获取web页面显示URL
     * <li>创建人：程锐
     * <li>创建日期：2015-12-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws JsonMappingException
     * @throws IOException
     */
    public void getDisplayURL() throws JsonMappingException, IOException {
        try {            
            String queryJSONStr = getRequest().getParameter("queryJSONStr");//查询参数
            String queryType = getRequest().getParameter("queryType");//查询分类 TWTClient:表示机车信息 queryJSONStr参数格式为：{"trainInfo":"N50001"} TWTStation：表示台位 queryJSONStr参数格式为：{"code":"1201","name":"组装台位","mapcode":"HEBJD"} TWTVideo:表示视频监控 queryJSONStr参数格式为：{"siteID":"A", "videoCode":"1", "videoName":"1号摄像头"}
            String funcname = getRequest().getParameter("funcname");//应用功能名称
            String userId = getRequest().getParameter("userId");//登陆名
            TWTAsynchronmousLongin twtAsynchronmousLongin = new TWTAsynchronmousLongin();
            twtAsynchronmousLongin.setQueryType(queryType);
            twtAsynchronmousLongin.setFuncname(funcname);
            twtAsynchronmousLongin.setQueryStr(queryJSONStr);
            twtAsynchronmousLongin.setUserId(userId);            
            String paramsStr = JSONUtil.write(twtAsynchronmousLongin);            
            String url = JXConfig.getInstance().getAppURL().concat("jsp/twt/asynchronouslogin/AsynchronousLogin.jsp");
            StringBuilder params = new StringBuilder();
            //对参数进行URL加密
            params.append(url.contains("?") ? "&" : "?").append("queryParams=").append(URLEncoder.encode(paramsStr, "UTF-8"));
            String strURL = url.concat(params.toString());
            JSONUtil.write(this.getResponse(), strURL);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            JSONUtil.write(this.getResponse(), CommonUtil.buildFalseJSONMsg(e.getMessage(), logger));
        }
    }
}
