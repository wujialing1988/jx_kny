package com.yunda.twt.webservice.util;

import org.codehaus.xfire.client.Client;

import com.yunda.jx.base.jcgy.entity.TrainType;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 台位图公用业务接口
 * <li>创建人：程锐
 * <li>创建日期：2015-2-5
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface ITWTUtil {
    
    /**
     * <li>说明：根据台位图服务名、车型车号获取台位图服务端
     * <li>创建人：程锐
     * <li>创建日期：2015-2-5
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param serviceName 台位图服务名
     * @param trainTypeShortName 车型简称
     * @param trainNo 车号
     * @return 台位图webservice客户端对象
     * @throws Exception
     */
    public Client getClient(String serviceName, String trainTypeShortName, String trainNo) throws Exception;
    
    /**
     * <li>说明：根据台位图服务名、机车信息获取台位图webservice客户端对象
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param serviceName 台位图服务名
     * @param trainInfo 机车信息JSON字符串
     * @return 台位图webservice客户端对象
     * @throws Exception
     */
    public Client getClient(String serviceName, String trainInfo) throws Exception;
    
    /**
     * <li>说明:根据车型车号六位简称获取机车简称和车号
     * <li>创建人：程锐
     * <li>创建日期：2014-06-03
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 6位字符，前两位是车型、后四位是车号
     * @return TrainType 机车信息
     */
    @SuppressWarnings("unchecked")
    public TrainType getTrainTypeByTrainInfo(String trainInfo);
    
    /**
     * <li>说明：根据台位图服务名、站点标示获取台位图webservice客户端对象
     * <li>创建人：程锐
     * <li>创建日期：2014-5-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param serviceName 台位图服务名
     * @param siteID 站点标示
     * @return 台位图webservice客户端对象
     */
    public Client getDefaultClient(String serviceName, String siteID);
}
