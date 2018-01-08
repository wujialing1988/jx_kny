package com.yunda.freight.zb.detain.webservice.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.base.context.SystemContext;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.freight.zb.detain.entity.DetainGztp;
import com.yunda.freight.zb.detain.entity.DetainTrain;
import com.yunda.freight.zb.detain.manager.DetainGztpManager;
import com.yunda.freight.zb.detain.manager.DetainTrainManager;
import com.yunda.freight.zb.detain.webservice.IDetainTrainService;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 扣车管理服务类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-4-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("detainTrainServiceWS")
public class DetainTrainServiceImpl implements IDetainTrainService {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    /**
     * 扣车服务类
     */
    @Resource
    private DetainTrainManager detainTrainManager ;
    
    /**
     * 扣车故障提票服务类
     */
    @Resource
    private DetainGztpManager detainGztpManager ; 
    
    /**
     * <li>说明：申请扣车
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                idx:"8a8284f24ab80704014ab891375a0002", // 如果是拒绝后再申请，则需要传
                trainTypeIdx:"8a8284f24ab80704014ab891375a0002",
                trainNo:"0001",
                detainReason:"扣车原因",
                detainTypeCode:"扣车类型编码",
                detainTypeName:"扣车类型名称"
            },
            operatorId: "7"
       }
     * @return
     * @throws IOException
     */
    public String applyDetainTrain(String jsonObject) throws IOException {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            
            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            DetainTrain entity = JSONUtil.read(entityJson, DetainTrain.class);
            if(StringUtil.isNullOrBlank(entity.getTrainTypeIdx())|| StringUtil.isNullOrBlank(entity.getTrainNo())){
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("请填写车型车号！"));
            }
            DetainTrain detail = detainTrainManager.applyDetainTrain(entity);
            String detainGztps = jo.getString("detainGztps");
            DetainGztp[] gztpArray = JSONUtil.read(StringUtil.nvlTrim(detainGztps, "[]"), DetainGztp[].class);
            if(gztpArray != null && gztpArray.length > 0){
                detainGztpManager.saveDetainGztps(gztpArray, detail.getIdx());
            }
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            if(e instanceof com.yunda.common.BusinessException){
                String msg = e.getMessage();
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(msg));
            }
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：撤销申请
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            idx:"8a8284f24ab80704014ab891375a0002",
            operatorId: "7"
       }
     * @return
     * @throws IOException
     */
    public String deleteDetainTrain(String jsonObject) throws IOException {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            
            // 申请ID
            String idx = jo.getString("idx");
            if(StringUtil.isNullOrBlank(idx)){
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("请选择一条数据！"));
            }
            detainTrainManager.deleteDetainTrain(idx);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            if(e instanceof com.yunda.common.BusinessException){
                String msg = e.getMessage();
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(msg));
            }
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：查询扣车
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                trainTypeIdx:"8a8284f24ab80704014ab891375a0002",
                trainNo:"0001"
            },
            operatorId: "7",
            start:1,
            limit:50, 
            orders:[{
                sort: "updateTime",
                dir: "ASC"
            }]            
       }
     * @return
     * @throws IOException
     */
    public String findDetainTrain(String jsonObject) throws IOException {
        try {

            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            
            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            DetainTrain entity = JSONUtil.read(entityJson, DetainTrain.class);
            
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
            
            // 申请人
            OmEmployee employee = SystemContext.getOmEmployee();
            if(employee != null){
                con = new Condition("proposerIdx", Condition.EQ, employee.getEmpid());
                con.setStringLike(false);
                whereList.add(con);
            }
            
            // 车型
            if(!StringUtil.isNullOrBlank(entity.getTrainTypeIdx())){ 
                con = new Condition("trainTypeIdx", Condition.EQ, entity.getTrainTypeIdx());
                con.setStringLike(false);
                whereList.add(con);
            }
            
            // 车号
            if(!StringUtil.isNullOrBlank(entity.getTrainNo())){ 
                con = new Condition("trainNo", Condition.EQ, entity.getTrainNo());
                con.setStringLike(true);
                whereList.add(con);
            }
            
            // 客货类型
            if(!StringUtil.isNullOrBlank(entity.getVehicleType())){ 
                con = new Condition("vehicleType", Condition.EQ, entity.getVehicleType());
                con.setStringLike(false);
                whereList.add(con);
            }            
            QueryCriteria<DetainTrain> query = new QueryCriteria<DetainTrain>(DetainTrain.class, whereList, orderList, start, limit);
            Page<DetainTrain> page = this.detainTrainManager.findPageList(query);
            List<DetainTrain> list = page.getList();
            if (null == list || list.size() <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            return JSONTools.toJSONList(page.getTotal(), list);
        }catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    
    /**
     * <li>说明：根据扣车主键查询其下的故障情况
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                detainIdx:"8a8284f24ab80704014ab891375a0002"
            },
            operatorId: "7"          
       }
     * @return
     * @throws IOException
     */
    public String findDetainGztp(String jsonObject) throws IOException {
        try {

            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            // 获取查询条件实体对象
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);

            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            DetainGztp entity = JSONUtil.read(entityJson, DetainGztp.class);
            List<DetainGztp> list = detainGztpManager.findList(entity);
            if (null == list || list.size() <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            return JSONTools.toJSONList(list.size(), list);
        }catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
}
