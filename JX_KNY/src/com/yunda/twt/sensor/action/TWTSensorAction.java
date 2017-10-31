package com.yunda.twt.sensor.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.order.AbstractOrderAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.twt.sensor.entity.TWTSensor;
import com.yunda.twt.sensor.manager.TWTSensorManager;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：TWTSensor控制器, 传感器注册
 * <li>创建人：程梅
 * <li>创建日期：2015-05-13
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@SuppressWarnings(value = "serial")
public class TWTSensorAction extends AbstractOrderAction<TWTSensor, TWTSensor, TWTSensorManager> {
    
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：传感器设置门限
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void twtSensorUpdate() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String data = StringUtil.nvlTrim(getRequest().getParameter("data"), Constants.EMPTY_JSON_OBJECT);
            TWTSensor sensor = (TWTSensor) JSONUtil.read(data, TWTSensor.class);
            this.manager.twtSensorUpdate(sensor, ids);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：设置传感器的检查周期
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-1
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void twtaddcycle() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String data = StringUtil.nvlTrim(getRequest().getParameter("data"), Constants.EMPTY_JSON_OBJECT);
            TWTSensor sensor = (TWTSensor) JSONUtil.read(data, TWTSensor.class);
            this.manager.twtaddcycle(sensor, ids);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：修改传感器的集线盒号
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void twtupdatebox() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String data = StringUtil.nvlTrim(getRequest().getParameter("data"), Constants.EMPTY_JSON_OBJECT);
            TWTSensor sensor = (TWTSensor) JSONUtil.read(data, TWTSensor.class);
            String[] errMsg = this.manager.twtupdatebox(sensor, ids);
            if (errMsg == null || errMsg.length < 1) {
                map.put(Constants.ENTITY, sensor);
                map.put(Constants.SUCCESS, true);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：解除传感器的绑定
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void twtRelieveBin() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.twtRelieveBin(ids);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：通过输入集线盒编号以及传感器编号，保存绑定信息
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void bindSensorSave() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String deskCode = getRequest().getParameter("deskCode"); // 台位编码
            String deskName = getRequest().getParameter("deskName"); // 台位名称
            String mapcode = getRequest().getParameter("mapcode"); // 战场
            String boxCodeId = getRequest().getParameter("boxCodeId"); // 集线盒号
            String sensorCodeId = getRequest().getParameter("sensorCodeId"); // 传感器编号
            String[] errMsg = this.manager.bindSensorSave(deskCode, deskName, mapcode, boxCodeId, sensorCodeId);
            if (errMsg == null || errMsg.length < 1) {
                map.put(Constants.SUCCESS, true);
            } else {
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：保存选择的传感器信息
     * <li>创建人：姚凯
     * <li>创建日期：2015-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveStationBySelect() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String deskCode = getRequest().getParameter("deskCode"); // 台位编码
            String deskName = getRequest().getParameter("deskName"); // 台位名称
            String mapcode = getRequest().getParameter("mapcode"); // 站场
            TWTSensor[] stationList = (TWTSensor[]) JSONUtil.read(getRequest(), TWTSensor[].class); // 选择传感器信息
            this.manager.saveStation(deskCode, deskName, mapcode, stationList);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：查询台位信息列表
     * <li>创建人：程梅
     * <li>创建日期：2015-6-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getSiteStationList() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            HttpServletRequest req = getRequest();
            String searchJson = StringUtil.nvlTrim(req.getParameter("entityJson"), Constants.EMPTY_JSON_OBJECT);
            String unBindSensor = req.getParameter("unBindSensor"); // 是否只显示未绑定传感器台位
            entity = (TWTSensor) JSONUtil.read(searchJson, entitySearch.getClass());
            map = this.manager.getSiteStationList(unBindSensor, entity, searchJson, getStart(), getLimit(), getOrders()).extjsStore();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
}
