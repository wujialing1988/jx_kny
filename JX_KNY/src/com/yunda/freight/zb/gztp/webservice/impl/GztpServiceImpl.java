package com.yunda.freight.zb.gztp.webservice.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.freight.zb.gztp.entity.Gztp;
import com.yunda.freight.zb.gztp.manager.GztpManager;
import com.yunda.freight.zb.gztp.webservice.IGztpService;
import com.yunda.freight.zb.plan.manager.ZbglRdpPlanRecordManager;
import com.yunda.jcbm.jcgx.manager.JcgxBuildManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.jx.pjwz.partsBase.entity.MatTypeList;
import com.yunda.jx.pjwz.partsBase.entity.MatTypeUse;
import com.yunda.jx.pjwz.partsBase.manager.MatTypeListManager;
import com.yunda.jx.pjwz.partsBase.manager.MatTypeUseManager;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 故障登记webservice接口
 * <li>创建人：何东
 * <li>创建日期：2017-4-17
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("gztpServiceWS")
public class GztpServiceImpl implements IGztpService {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    /** 列检计划业务 */
    @Resource
    private GztpManager gztpManager ;
    
    /** 机车构型业务处理对象 */
    @Resource
    private JcgxBuildManager jcgxBuildManager ;
    
    /** 车辆列检计划业务类 */
    @Resource
    private ZbglRdpPlanRecordManager zbglRdpPlanRecordManager ;
    
    /** 物料清单服务 */
    @Resource
    private MatTypeListManager matTypeListManager ;
    
    /** 物料消耗服务 */
    @Resource
    private MatTypeUseManager matTypeUseManager ;    

    /**
     * <li>说明：查询故障登记列表数据
     * <li>创建人：何东
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》2.5.2.1 查询故障登记集合
     * @return
     * @throws IOException
     */
    @Override
    public String findGztpList(String jsonObject) {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            if (null == jo) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            
            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            Gztp entity = JSONUtil.read(entityJson, Gztp.class);
            if (null == entity) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            
            // 分页页号
            int start = jo.getIntValue(Constants.START);
            
            // 分页大小
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
            } else { // 添加默认排序
                orderList.add(Order.desc("faultNoticeStatus"));
            }
            
            // 查询条件
            List<Condition> whereList = new ArrayList<Condition>();
            Condition con = null;
            // 站点ID
            String siteId = EntityUtil.findSysSiteId(null);
            if(!StringUtil.isNullOrBlank(siteId)){
                con = new Condition("siteId", Condition.EQ, siteId);
                con.setStringLike(false);
                whereList.add(con);
            }
            
            // 列检车辆计划ID
            if(!StringUtil.isNullOrBlank(entity.getRdpRecordPlanIdx())){
                con = new Condition("rdpRecordPlanIdx", Condition.EQ, entity.getRdpRecordPlanIdx());
                con.setStringLike(false);
                whereList.add(con);
            }
            
            // 处理类型：0表示登记，1表示上报
            if(entity.getHandleType() != null){
                con = new Condition("handleType", Condition.EQ, entity.getHandleType());
                con.setStringLike(false);
                whereList.add(con);
            }
            
            // 客货类型 10货车 20客车
            if(entity.getVehicleType() != null){
                con = new Condition("vehicleType", Condition.EQ, entity.getVehicleType());
                con.setStringLike(false);
                whereList.add(con);
            }            
            
            // 根据关键字进行查询
            String queryKey = entity.getRailwayTime();
            if(!StringUtil.isNullOrBlank(queryKey)){
                String sqlStr = " (RAILWAY_TIME LIKE '%" + queryKey + "%'"
                    + " OR VEHICLE_TYPE_CODE LIKE '%" + queryKey + "%'"
                    + " OR TRAIN_NO LIKE '%" + queryKey + "%'"
                    + " OR NOTICE_PERSON_NAME LIKE '%" + queryKey + "%') ";
                con = new Condition();
                con.setCompare(Condition.SQL);
                con.setSql(sqlStr);
                whereList.add(con);
            }
            
            QueryCriteria<Gztp> query = new QueryCriteria<Gztp>(Gztp.class, whereList, orderList, start, limit);
            Page<Gztp> page = gztpManager.findPageList(query);
            List<Gztp> list = page.getList();
            if (null == list || list.size() <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            
            return JSONTools.toJSONList(page.getTotal(), list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    /**
     * <li>说明：保存/修改故障登记
     * <li>创建人：何东
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            // 故障登记实体
            entityJson: {
            },
            operatorId:"操作人ID",
            isDatailTrain:"是否扣车 true 是 false 否",
            // 物料消耗明细数组
            matUses:[{
            }]
       } 
     * 
     * @return
     */
    @Override
    public String saveOrUpdate(String jsonObject) {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            if (null == jo) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            
            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            Gztp entity = JSONUtil.read(entityJson, Gztp.class);
            if (null == entity) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            // 是否扣车
            String[] errMsg = gztpManager.validateUpdate(entity);
            if (errMsg == null || errMsg.length < 1) {
                Gztp result = gztpManager.saveGztp(entity);
                String matUses = jo.getString("matUses");
                MatTypeUse[] matUseArray = JSONUtil.read(StringUtil.nvlTrim(matUses, "[]"), MatTypeUse[].class);
                if(matUseArray != null && matUseArray.length > 0){
                    matTypeUseManager.saveMatUsesForGZTP(matUseArray,result.getIdx()); // 保存物料信息
                }
                // 返回记录保存成功的消息
                return WsConstants.OPERATE_SUCCESS;
            } else {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(errMsg[0]));
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    /**
     * <li>说明：删除故障登记
     * <li>创建人：何东
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》2.5.2.3 删除故障登记
     * @return
     */
    @SuppressWarnings("all")
    @Override
    public String delete(String jsonObject) {
        try {
            String[] ids = JSONObject.parseObject(jsonObject, new TypeReference<String[]>(){});
            if (null == ids || ids.length <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            
            String[] errMsg = gztpManager.validateDelete(ids);
            if (errMsg == null || errMsg.length < 1) {
            	gztpManager.deleteByIds(ids);
                // 返回记录保存成功的消息
                return WsConstants.OPERATE_SUCCESS;
            } else {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(errMsg[0]));
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    /**
     * <li>说明：获取车辆构型下拉树数据
     * <li>创建人：何东
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》2.5.2.4 获取车辆构型下拉树数据
     * @return
     */
    @Override
    public String getJcgxBuildComboTree(String jsonObject) {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            if (null == jo) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            
            String parentIDX = jo.getString("parentIDX");
            String shortName = jo.getString("shortName");
            
            return JSONTools.toJSONList(jcgxBuildManager.getJcgxBuildTree(parentIDX, shortName));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    /**
     * <li>说明：获取车辆范围活下拉树数据
     * <li>创建人：何东
     * <li>创建日期：2017-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》2.5.2.5 获取车辆范围活下拉树数据
     * @return
     */
    @Override
    public String getScopeWorkComboTree(String jsonObject) {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            if (null == jo) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            String planIdx = jo.getString("planIdx");
            return JSONTools.toJSONList(zbglRdpPlanRecordManager.getScopeWorkTree(planIdx));
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    /**
     * <li>说明：获取物料清单列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            matCode:"编码",
            matDesc:"描述",
            start:1,
            limit:50, 
            orders:[{
                sort: "seqNum",
                dir: "ASC"
            }]
       }
     * @return
     */
    public String findMatTypeList(String jsonObject) {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            if (null == jo) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            // 查询开始索引
            int start = jo.getIntValue(Constants.START);
            // 查询条数
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
            // 模糊查询条件
            String matCode = jo.getString("matCode");
            if(!StringUtil.isNullOrBlank(matCode)){ 
                con = new Condition("matCode", Condition.EQ, matCode);
                con.setStringLike(true);
                whereList.add(con);
            }            
            String matDesc = jo.getString("matDesc");
            if(!StringUtil.isNullOrBlank(matDesc)){ 
                con = new Condition("matDesc", Condition.EQ, matDesc);
                con.setStringLike(true);
                whereList.add(con);
            }               
            QueryCriteria<MatTypeList> query = new QueryCriteria<MatTypeList>(MatTypeList.class, whereList, orderList, start, limit);
            Page<MatTypeList> page = this.matTypeListManager.findPageList(query);
            List<MatTypeList> list = page.getList();
            if (null == list || list.size() <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            return JSONTools.toJSONList(page.getTotal(), list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    /**
     * <li>说明：获取物料消耗列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            gztpIdx:"8a8284c35b233e87015b23416fb40002",
            start:1,
            limit:50, 
            orders:[{
                sort: "seqNum",
                dir: "ASC"
            }]
       }
     * @return 
     */
    public String findMatTypeUse(String jsonObject) {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            if (null == jo) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
            }
            // 查询开始索引
            int start = jo.getIntValue(Constants.START);
            // 查询条数
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
            String gztpIdx = jo.getString("gztpIdx");// 故障登记ID
            if(StringUtil.isNullOrBlank(gztpIdx)){
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("接口参数[故障登记ID]不能为空！"));
            }
            con = new Condition("gztpIdx", Condition.EQ, gztpIdx);
            con.setStringLike(false);
            whereList.add(con);                    
            QueryCriteria<MatTypeUse> query = new QueryCriteria<MatTypeUse>(MatTypeUse.class, whereList, orderList, start, limit);
            Page<MatTypeUse> page = this.matTypeUseManager.findPageList(query);
            List<MatTypeUse> list = page.getList();
            if (null == list || list.size() <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            return JSONTools.toJSONList(page.getTotal(), list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
}
