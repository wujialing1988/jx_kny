package com.yunda.freight.base.vehicle.webservice.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.base.vehicle.entity.TrainVehicleType;
import com.yunda.freight.base.vehicle.manager.TrainVehicleTypeManager;
import com.yunda.freight.base.vehicle.webservice.ITrainVehicleTypeService;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 车辆车型接口实现
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-4-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("trainVehicleTypeServiceWS")
public class TrainVehicleTypeServiceImpl implements ITrainVehicleTypeService {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    /** 车辆车型服务 */
    @Resource
    private TrainVehicleTypeManager trainVehicleTypeManager;
    
    /**
     * <li>说明：查询车辆车型列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                typeCode:"",
                typeName:"",
                vehicleKindCode:""
            },
            start:1,
            limit:50, 
            orders:[{
                sort: "idx",
                dir: "ASC"
            }]
       }
     * @return
     * @throws IOException
     */
    public String findTrainVehicleTypeList(String jsonObject) throws IOException {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            TrainVehicleType entity = JSONUtil.read(entityJson, TrainVehicleType.class);
            
            // 查询工艺开始索引
            int start = jo.getIntValue(Constants.START);
            
            // 查询工艺条数
            int limit = jo.getIntValue(Constants.LIMIT);
            start = limit * (start - 1);
            
            // 排序字段
            JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
            List<Order> orderList = new ArrayList<Order>();
            if (null != jArray) {
                Order[] orders = JSONTools.getOrders(jArray);
                for (Order order : orders) {
                    orderList.add(order);
                }
            }
            
            // 查询条件
            List<Condition> whereList = new ArrayList<Condition>();
            Condition con = null;
            // 查询条件 - 货车客车类型
            if(!StringUtil.isNullOrBlank(entity.getVehicleType())){ 
                con = new Condition("vehicleType", Condition.EQ, entity.getVehicleType());
                con.setStringLike(false);
                whereList.add(con);
            }else{
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("车辆类型不能为空!"));
            }
            
            // 查询条件 - 车型代码
            if(!StringUtil.isNullOrBlank(entity.getTypeCode())){ 
                con = new Condition("typeCode", Condition.EQ, entity.getTypeCode());
                whereList.add(con);
            }
            
            // 查询条件 - 车型名称
            if(!StringUtil.isNullOrBlank(entity.getTypeName())){ 
                con = new Condition("typeName", Condition.EQ, entity.getTypeName());
                whereList.add(con);
            }
            
            // 查询条件 - 车型种类编码
            if(!StringUtil.isNullOrBlank(entity.getVehicleKindCode())){ 
                con = new Condition("vehicleKindCode", Condition.EQ, entity.getVehicleKindCode());
                con.setStringLike(false);
                whereList.add(con);
            }
            
            QueryCriteria<TrainVehicleType> query = new QueryCriteria<TrainVehicleType>(TrainVehicleType.class, whereList, orderList, start, limit);
            Page<TrainVehicleType> page = this.trainVehicleTypeManager.findPageList(query);
            List<TrainVehicleType> list = page.getList();
            if (null == list || list.size() <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            return JSONTools.toJSONList(page.getTotal(), list);
        }catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
}
