package com.yunda.twt.webservice.service;

import java.io.IOException;


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
public interface ISiteMapConfigService {
    
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
    public String saveTWTElements(String elementList) throws IOException;
    
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
    public String saveStationMapURL(String stationMapURL);
    
    /**
     * <li>说明：获取当前系统要展示的台位图信息及Server服务url地址
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 台位图服务地址列表
     */
    public String getStationMapURL();
    
    /**
     * <li>说明：台位图服务调用该接口获取所有颜色配置及状态信息
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return web端配置台位图颜色配置列表
     */
    public String getStatusColor();
    
    /**
     * <li>说明：台位图服务调用该接口获取web页面显示URL
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：汪东良
     * <li>修改日期：2015-05-25
     * <li>修改内容：增加查询参数类型，并对代码进行重构以适应点击台位弹出页面的功能
     * @param queryJSONStr 查询参数
     * @param queryType 查询分类 
     *<br>TWTClient:表示机车信息参数格式为：{"trainInfo":"N50001"}
     *<br>TWTStation：表示台位queryJSONStr参数格式为：{"code":"1201","name":"组装台位","mapcode":"HEBJD"}
     * @param funcname 功能名称
     * @param userId 登录名
     * @return web端机车相关信息显示URL字符串,包含车型车号参数.url字符串格式样式：http://www.baidu.com,如果查询不到机车则返回失败的字符串
     */
    public String getDisplayURL(String queryJSONStr, String queryType, String funcname, String userId);
    
    /**
     * <li>说明：获取系统配置项：超时预警时长
     * <li>创建人：程锐
     * <li>创建日期：2015-3-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 系统配置项：超时预警时长
     */
    public String getTrainStatusOverTime();
    
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
    public String getSystemConfig(String configItemkey);
}
