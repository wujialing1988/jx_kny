package com.yunda.jx.pjjx.webservice;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdp;
import com.yunda.jx.pjjx.partsrdp.entity.PartsRdpBean;
import com.yunda.jx.pjjx.partsrdp.manager.PartsRdpQueryManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecord;
import com.yunda.jx.pjjx.partsrdp.recordinst.entity.PartsRdpRecordBean;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordCardManager;
import com.yunda.jx.pjjx.partsrdp.recordinst.manager.PartsRdpRecordManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.webservice.common.entity.OperateReturnMessage;

@Service(value = "partsRdpQueryWS")
public class PartsRdpQueryService implements IPartsRdpQueryService {
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    /** 配件检修作业查询 */
    @Resource
    private PartsRdpQueryManager partsRdpQueryManager;
    @Resource
    private  PartsRdpRecordManager partsRdpRecordManager;
    @Resource
    private  PartsRdpRecordCardManager partsRdpRecordCardManager;
    /**
     * <li>说明：通过工位，下车车型车号，配件名称查询配件检修情况
     * <li>创建人：张迪
     * <li>创建日期：2016-7-29
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 查询参数json对象
     * @return 配件检修记录
     * @throws IOException
     */
    public String queryPartsListByWorkStation(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(IService.MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue(Constants.LIMIT);
        start = limit * (start - 1);
        
        String entityJson = jo.getString("entityJson");
        PartsRdp rdp = JSONUtil.read(StringUtil.nvl(entityJson, Constants.EMPTY_JSON_OBJECT), PartsRdp.class);
        
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        Page<PartsRdpBean> page = partsRdpQueryManager.queryPageList(new SearchEntity<PartsRdp>(rdp, start, limit, orders));
        try {
            return JSONTools.toJSONList(page);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
       
    /**
     * <li>说明：查询检修记录单
     * <li>创建人：张迪
     * <li>创建日期：2016-8-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 
     * {
     *   rdpRecordIDX 检修作业兑现单idx
     * }
     * @return 检修记录单集合
     * @throws IOException 
     * @throws JsonMappingException  
     */
    public String  queryRecordPageList(String jsonObject) throws JsonMappingException, IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(IService.MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue(Constants.LIMIT);
        start = limit * (start - 1);
        
        String entityJson = jo.getString("entityJson");
        PartsRdpRecord entity = JSONUtil.read(StringUtil.nvl(entityJson, Constants.EMPTY_JSON_OBJECT), PartsRdpRecord.class);
        
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        SearchEntity<PartsRdpRecord> searchEntity = new SearchEntity<PartsRdpRecord>(entity, start, limit, orders);
        try {
            Page<PartsRdpRecordBean>  recordList = partsRdpRecordManager.queryRecordPageList(searchEntity);
            return JSONTools.toJSONList(recordList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2016-8-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 
     * {
     *   rdpRecordIDX 检修记录单idx
     * }  
     * @return 檢修记录卡详情列表
     * @throws IOException
     */
    public String queryCardList(String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(IService.MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);        
        String  rdpRecordIDX = jo.getString("rdpRecordIDX");
        String partsRdpRecordCardList  = partsRdpRecordCardManager.integrateQueryCardList(rdpRecordIDX);
        return partsRdpRecordCardList;
    }
}
