package com.yunda.twt.sensor.webservice;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 此接口用于传感器绑定
 * <li>创建人：程梅
 * <li>创建日期：2015-6-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface ITWTSensorService {
    
    /***
     * 
     * <li>说明：获取所有的传感器信息
     * <li>创建人：程梅
     * <li>创建日期：2015-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param siteId 站点
     * @return 返回传感器列表JSON字符串
     */
    @SuppressWarnings("unchecked")
    public String getAllSensor(String siteId);
    
    /**
     * 
     * <li>说明：根据台位编号查询传感器列表
     * <li>创建人：程梅
     * <li>创建日期：2015-6-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param stationCodeJSON 台位图编号JSON字符串
     * @return 返回传感器列表JSON字符串
     */
    @SuppressWarnings("unchecked")
    public String getSensorByStation(String stationCodeJSON);
    /**
     * 
     * <li>说明：批量注册传感器
     * <li>创建人：程梅
     * <li>创建日期：2015-9-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param sensorDatas
     * @return
     */
    public String saveSensor(String sensorDatas);
}
