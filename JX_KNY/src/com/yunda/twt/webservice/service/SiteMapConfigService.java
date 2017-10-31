package com.yunda.twt.webservice.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yunda.common.BusinessException;
import com.yunda.frame.common.JXConfig;
import com.yunda.frame.util.CommonUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jxpz.utils.SystemConfigUtil;
import com.yunda.twt.asynchronouslogin.entity.TWTAsynchronmousLongin;
import com.yunda.twt.twtinfo.entity.Site;
import com.yunda.twt.twtinfo.entity.TrainStatusColors;
import com.yunda.twt.twtinfo.manager.SiteManager;
import com.yunda.twt.twtinfo.manager.SiteStationManager;
import com.yunda.twt.twtinfo.manager.SiteTrackManager;
import com.yunda.twt.twtinfo.manager.SiteVideoManager;
import com.yunda.twt.twtinfo.manager.SiteWHManager;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 此接口用于进行台位图配置相关服务，包括台位图访问地址配置，台位图元素信息配置服务、台位图颜色状态配置服务
 * <li>创建人：程锐
 * <li>创建日期：2015-2-6
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "siteMapConfigService")
public class SiteMapConfigService implements ISiteMapConfigService {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 站场业务类 */
    @Resource
    private SiteManager siteManager;
    
    /** 站场业务类 */
    @Resource
    private SiteTrackManager siteTrackManager;
    
    /** 站场业务类 */
    @Resource
    private SiteStationManager siteStationManager;
    
    /** 站场业务类 */
    @Resource
    private SiteWHManager siteWHManager;
    
    /** 站场业务类 */
    @Resource
    private SiteVideoManager siteVideoManager;
    
    /**
     * <li>说明：当台位图服务初始化时，可调用该接口将台位元素信息写入web端的表中
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param elementList 台位图元素列表
     * @return 操作成功与失败的json字符串
     * @throws IOException 
     */
    @SuppressWarnings("unchecked")
    public String saveTWTElements(String elementList) throws IOException {
        Map<String, List<Map>> map = new HashMap<String, List<Map>>();
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            map = JSONUtil.read(elementList, Map.class);
            if (map.isEmpty())
                throw new BusinessException("台位图元素为空");
            if (map.containsKey("siteTrack")) {                
                List<Map> list = map.get("siteTrack");
                siteTrackManager.saveOrUpdateList(list);
            }
            if (map.containsKey("siteStation")) {
                List<Map> list = map.get("siteStation");
                siteStationManager.saveOrUpdateList(list);
            }
            if (map.containsKey("siteWH")) {                
                List<Map> list = map.get("siteWH");                
                siteWHManager.saveOrUpdateList(list);
            }
            if (map.containsKey("siteVideo")) {
                List<Map> list = map.get("siteVideo");                
                siteVideoManager.saveOrUpdateList(list);
            }
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
            return JSONUtil.write(msg);
        }
    }
    
    /**
     * <li>说明：保存或更新台位图相关配置信息及Server服务url地址
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param stationMapURL 台位图服务相关url配置的json字符串
     * @return 操作成功与失败的json字符串
     */
    public String saveStationMapURL(String stationMapURL) {
        try {
            Site site = JSONUtil.read(stationMapURL, Site.class);
            siteManager.updateSite(site);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
        return WsConstants.OPERATE_SUCCESS;
    }
    
    /**
     * <li>说明：获取当前系统要展示的台位图信息及Server服务url地址
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 台位图服务地址列表
     */
    @SuppressWarnings("unchecked")
    public String getStationMapURL() {
        List<Site> list = siteManager.getDaoUtils().getHibernateTemplate().loadAll(Site.class);
        String json = "[]";
        try {
            json = JSONUtil.write(list);
        } catch (IOException e) {
            ExceptionUtil.process(e, logger);
        }
        return json;
    }
    
    /**
     * <li>说明：台位图服务调用该接口获取所有颜色配置及状态信息
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return web端配置台位图颜色配置列表
     */
    @SuppressWarnings("unchecked")
    public String getStatusColor() {
        List<TrainStatusColors> list = siteManager.getDaoUtils().getHibernateTemplate().loadAll(TrainStatusColors.class);
        String json = "[]";
        try {
            json = JSONUtil.write(list);
        } catch (IOException e) {
            ExceptionUtil.process(e, logger);
        }
        return json;
    }
    
    /**
     * <li>说明：台位图服务调用该接口获取web页面显示URL
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：汪东良
     * <li>修改日期：2015-05-25
     * <li>修改内容：增加查询参数类型，并对代码进行重构以适应点击台位弹出页面的功能
     * @param queryJSONStr 查询参数
     * @param queryType 查询分类 
     *      <br>TWTClient:表示机车信息参数格式为：{"trainInfo":"N50001"}
     *      <br>TWTStation：表示台位queryJSONStr参数格式为：{"code":"1201","name":"组装台位","mapcode":"HEBJD"}
     *      <br>TWTVideo:表示台位queryJSONStr参数格式为：{"siteID":"A", "videoCode":"1", "videoName":"1号摄像头"}
     * @param funcname 功能名称
     * @param userId 登录名
     * @return web端机车相关信息显示URL字符串,包含车型车号参数.url字符串格式样式：http://www.baidu.com,如果查询不到机车则返回失败的字符串
     */
    public String getDisplayURL(String queryJSONStr, String queryType, String funcname, String userId) {
        try {            
            //将参数值设置到TWTAsynchronmousLongin对象中并输出成JSON格式；
            TWTAsynchronmousLongin twtAsynchronmousLongin=new TWTAsynchronmousLongin();
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
            return strURL;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return CommonUtil.buildFalseJSONMsg(e.getMessage(), logger);
        }
    }
 
    
    /**
     * <li>说明：获取系统配置项：超时预警时长
     * <li>创建人：程锐
     * <li>创建日期：2015-3-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 系统配置项：超时预警时长
     */
    public String getTrainStatusOverTime() {
        String statusOverTime = getSystemConfig("ck.jczb.trainStatusOverTime");
        if(statusOverTime==null||"".equals(statusOverTime)){
            statusOverTime="60";//默认为60秒
        }
        return statusOverTime;
    }
    
    /**
     * <li>说明：获取系统配置项值
     * <li>创建人：程锐
     * <li>创建日期：2014-5-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param configItemkey 系统配置项键值 
     * @return 系统配置项值
     */ 
    public String getSystemConfig(String configItemkey) {
        return SystemConfigUtil.getValue(configItemkey);
    }
}
