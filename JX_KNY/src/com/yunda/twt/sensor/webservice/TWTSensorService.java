package com.yunda.twt.sensor.webservice;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.twt.sensor.entity.TWTSensor;
import com.yunda.twt.sensor.manager.TWTSensorManager;
import com.yunda.webservice.common.WsConstants;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 传感器接口实现类
 * <li>创建人：程梅
 * <li>创建日期：2015-6-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "tWTSensorService")
public class TWTSensorService implements ITWTSensorService {
    
    /** 日志工具 */
    Logger logger = Logger.getLogger(this.getClass());
    
    /** 系统出错信息 */
    private static final String MESSAGE_ERROR = "error";
    
    /** 传感器业务类 */
    @Autowired
    private TWTSensorManager tWTSensorManager ;
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
    public String getAllSensor(String siteId) {
        try {
            TWTSensor sensor = new TWTSensor();
            sensor.setSiteId(siteId);
            SearchEntity<TWTSensor> searchEntity = new SearchEntity<TWTSensor>(sensor, 0, 10000, null);
            Page page = tWTSensorManager.findPageList(searchEntity);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("TWTSensorService.getAllSensor：异常:" + e + "\n原因" + e.getMessage());
            return "error";
        }
    }
    
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
    public String getSensorByStation(String stationCodeJSON) {
        try {
            if(StringUtil.isNullOrBlank(stationCodeJSON)) return null;
            TWTSensor sensor = (TWTSensor)JSONUtil.read(stationCodeJSON, TWTSensor.class);
            SearchEntity<TWTSensor> searchEntity = new SearchEntity<TWTSensor>(sensor, 0, 10000, null);
            Page page = tWTSensorManager.findPageList(searchEntity);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return MESSAGE_ERROR;
        }
    }
    /**
     * 
     * <li>说明：批量注册传感器
     * <li>创建人：程梅
     * <li>创建日期：2015-9-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param sensorDatas 需注册的传感器实体对象数组[{"boxCode":"0011","sensorCode":"1000000011","minLimit":"100","maxLimit":"1000","checkCycle":"3","location":"aaaaaa","siteId":"A"},
     * {"boxCode":"0001","sensorCode":"1000100011","minLimit":"100","maxLimit":"1000","checkCycle":"3","location":"bbbb","siteId":"A"}]
     * @return
     */
    public String saveSensor(String sensorDatas) {
        try {
            TWTSensor[] sensorArray = JSONUtil.read(StringUtil.nvlTrim(sensorDatas, "[]"), TWTSensor[].class);
            List<TWTSensor> sensorList = new ArrayList<TWTSensor>();
            for(TWTSensor sensor : sensorArray){
                sensorList.add(sensor);
            }
            tWTSensorManager.saveOrUpdate(sensorList);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
}
