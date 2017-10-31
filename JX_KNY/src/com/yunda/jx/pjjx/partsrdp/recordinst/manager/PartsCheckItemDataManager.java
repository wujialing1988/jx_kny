package com.yunda.jx.pjjx.partsrdp.recordinst.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.DateUtil;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsCheckItemData;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordDI;
import com.yunda.jx.pjjx.util.HttpClientUtils;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: PartsCheckItemData业务类,可视化数据采集结果
 * <li>创建人：林欢
 * <li>创建日期：2016-6-12
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 3.2
 */

@Service(value="partsCheckItemDataManager")
public class PartsCheckItemDataManager extends JXBaseManager<PartsCheckItemData, PartsCheckItemData>{
    
    /** PartsRdpRecordDI业务类,配件检修检测数据项 */
    @Resource
    private PartsRdpRecordDIManager partsRdpRecordDIManager;
    
    /** PartsRdp业务类,配件检修作业 */
    @Resource
    private PartsRdpManager partsRdpManager;
    /**
     * <li>说明：同步可视化数据
     * <li>创建人：林欢
     * <li>创建日期：2016-06-16
     * <li>修改人：林欢 
     * <li>修改日期：2016-07-25
     * <li>修改内容：通过配件检修计划idx查询配件识别码，计划实际开始时间，时间选择当前时间
     * @param rdpRecordCardRdpIDX 配件检修计划idx
     * @param map 返回map
     * @param url http与可视化系统对接地址
     * @return Map<String, Object> 返回map
     * @throws Exception 
     * @throws IOException
     */
    public Map<String, Object> doSyn(String rdpRecordCardRdpIDX,Map<String, Object> map,String url) throws Exception {
        
        //通过检修计划idx查询检修对象
        PartsRdp partsRdp = partsRdpManager.getModelById(rdpRecordCardRdpIDX);
        
//      其他参数map
        Map<String,String> paramMap = new HashMap<String, String>();
        //postMap
        Map<String,Object> postMap = new HashMap<String, Object>();
        
        String nowTime = DateUtil.yyyy_MM_dd_HH_mm_ss_SSS.format(new Date());
        postMap.put("endTime",nowTime);
        postMap.put("partID",partsRdp.getIdentificationCode());
        
        //开始时间由于可视化需要，时间-8小时
        BigDecimal startTimeBigDecimal = new BigDecimal(partsRdp.getRealStartTime().getTime()).subtract(new BigDecimal(8*60*60*1000));
        String startTime = DateUtil.yyyy_MM_dd_HH_mm_ss_SSS.format(new Date(startTimeBigDecimal.longValue()));
        
        postMap.put("startTime",startTime);
        
//      通过配件检修计划idx 查询检修计划下所有检修项编码
        List<String> checkIDs = partsRdpRecordDIManager.findCheckIDByRdpRecordCardIDX(rdpRecordCardRdpIDX);
        postMap.put("checkID", checkIDs);
        
        paramMap.put("rdpRecordCardRdpIDX", rdpRecordCardRdpIDX);
        
        //读取配置文件，查看是否启用可视化系统同步数据，如果启用，那么同步
        Properties p =HttpClientUtils.instence.getProperties();
        if (p != null && StringUtils.isNotBlank(p.getProperty(HttpClientUtils.KSHFLAG)) && "true".equals(p.getProperty(HttpClientUtils.KSHFLAG))) {
//          同步可视化数据
            return this.synPartsCheckItemDataAndSavePartsRdpRecordDI(url, postMap, paramMap, map);
        }else {
            return map;
        }
    }
    
    /**
     * <li>说明：动态通过http获取可视化检测结果数据，并且同步更新到对应的检测项中
     * <li>创建人：林欢
     * <li>创建日期：2016-06-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param url http访问url
     * @param postMap http访问查询参数
     * @param paramMap 其他查询参数
     * @param map 返回map
     * @return Map<String, Object> 返回结果map {success : true} {success : false,errMsg:"操作失败"}
     * @throws Exception 
     */
    public Map<String, Object> synPartsCheckItemDataAndSavePartsRdpRecordDI(String url, Map<String,Object> postMap, Map<String, String> paramMap, Map<String, Object> map) throws Exception {

        //      通过http获取可视化检测结果数据
        String jsonObject = HttpClientUtils.instence.getCloseableHttpClientInstence(url, postMap);
        
        if (StringUtils.isNotBlank(jsonObject) && !"调用失败".equals(jsonObject)) {
            JSONObject ob = JSONObject.parseObject(jsonObject);
            //操作结果标示
            String success = ob.getString(Constants.SUCCESS);
            if (StringUtils.isNotBlank(success) && "true".equals(success)) {
//              返回的检测项数据结果
                String root = ob.getString("root");
                
                if (StringUtils.isNotBlank(root)) {
//                  检测项数据结果数组
                    
                    //      获取mapper对象
                    ObjectMapper mapper = new ObjectMapper();
                    // ==========自定义日期格式========
            //      获取实体序列化配置对象
                    DeserializationConfig cfg = mapper.getDeserializationConfig();
            //      设置自己的日期转换工具-其中MyDateFormat为我自己继承自SimpleDateFormat的类，以便处理“yyyy-MM-dd HH:mm:ss”与"yyy-MM-dd"两种日期格式
                    cfg.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:ss:mm SSS"));
            //      注入mapper中
                    mapper = mapper.setDeserializationConfig(cfg);
                    
                    PartsCheckItemData[] pcid = mapper.readValue(root, PartsCheckItemData[].class);
                    //需要和可视化沟通，对方针对一个配件下的一个检测项只返回最新的一条数据
                    if (pcid != null && pcid.length > 0) {
                        for (PartsCheckItemData data : pcid) {
                            //同步更新到对应检测项编码的检测结果中
                            
                            //如果有值为空则报错，因为该2个值为空，该操作无意义
                            if (StringUtils.isNotBlank(data.getCheckID())) {
                                paramMap.put("checkID", data.getCheckID());
                            }
                            if (StringUtils.isNotBlank(data.getCheckValue())) {
                                paramMap.put("checkValue", data.getCheckValue());
                            }
                            
//                          动态通过http获取可视化检测结果数据，并且同步更新到对应的检测项中
                            PartsRdpRecordDI partsRdpRecordDI = partsRdpRecordDIManager.getPartsRdpRecordDIDataItemResult(paramMap);
                            if (partsRdpRecordDI != null) {
                                partsRdpRecordDI.setDataItemResult(data.getCheckValue());
                                partsRdpRecordDI.setDataSource(1);
                                partsRdpRecordDIManager.saveOrUpdate(partsRdpRecordDI);
                                
//                              将可视化处获取到的数据持久化到partsCheckItemData表中保存
                                data.setPartsRdpRecordDIIDX(partsRdpRecordDI.getIdx());
                                //判断idx是否已经存在，存在做修改操作
                                Map<String, Object> map1 = new HashMap<String, Object>();
                                map1.put("partsRdpRecordDIIDX", partsRdpRecordDI.getIdx());
                                PartsCheckItemData p = this.getPartsCheckItemDataByParams(map1);
                                if (p != null) {
                                    this.saveOrUpdate(p);
                                }else {
                                    this.saveOrUpdate(data);
                                }
                            }
                        }
                        map.put(Constants.SUCCESS, true);
                    }else{
                        map.put(Constants.ERRMSG, "无可视化数据同步!");
                    }
                }
            }else {
                String errMsg = ob.getString(Constants.ERRMSG);
                map.put(Constants.ERRMSG, errMsg);
            }
        }else {
            map.put(Constants.ERRMSG, jsonObject);
        }
        
        return map;
    }

    /**
     * <li>说明：通过检测项编码查询检测项结果对象
     * <li>创建人：林欢
     * <li>创建日期：2016-6-13
     * <li>修改人： 林欢
     * <li>修改日期：2016-7-25
     * <li>修改内容：判断非空条件，添加新的条件字段
     * @param map 封装检测查询条件
     * @return PartsCheckItemData 检测项对象
     */
    public PartsCheckItemData getPartsCheckItemDataByParams(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("FROM PartsCheckItemData where 1=1 ");
        
        //配件数据项idx
        if (map.get("partsRdpRecordDIIDX") != null) {
            sb.append(" and partsRdpRecordDIIDX = '").append(map.get("partsRdpRecordDIIDX")).append("'");
        }
        
        //checkID条件
        if (map.get("checkID") != null) {
            sb.append(" and checkID = '").append(map.get("checkID")).append("'");
        }
        
        //根据时间排序，默认赋值最近的一个
        sb.append(" order by checkTime desc ");
        
        return this.findSingle(sb.toString());
    }
    
}
