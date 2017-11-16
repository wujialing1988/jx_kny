package com.yunda.freight.zb.plan.webservice.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
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
import com.yunda.frame.util.EntityUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlan;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanRecord;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanRecordBean;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanRecordListBean;
import com.yunda.freight.zb.plan.entity.ZbglRdpPlanWiBean;
import com.yunda.freight.zb.plan.manager.ZbglRdpPlanManager;
import com.yunda.freight.zb.plan.manager.ZbglRdpPlanRecordManager;
import com.yunda.freight.zb.plan.webservice.IZbglRdpPlanService;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.jxpz.utils.SystemConfigUtil;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检计划接口实现
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-3-31
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("zbglRdpPlanServiceWS")
public class ZbglRdpPlanServiceImpl implements IZbglRdpPlanService {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    /** 列检计划业务 */
    @Resource
    private ZbglRdpPlanManager zbglRdpPlanManager ;
    
    /** 列检车辆计划业务 */
    @Resource
    private ZbglRdpPlanRecordManager zbglRdpPlanRecordManager ;
    
    
    /**
     * <li>说明：查询列检计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                rdpPlanStatus:"ONGOING",
                vehicleType:"10"
            },
            operatorId: "800109",
            filterByOperatorId: "false",
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
    public String findZbglRdpPlanList(String jsonObject) throws IOException {
        
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
            ZbglRdpPlan entity = JSONUtil.read(entityJson, ZbglRdpPlan.class);
            
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
            
            // 是否查询指派人员的计划车辆
            String flag = SystemConfigUtil.getValue("knySys.freightSys.ZbglRdpPlanDispatch");
            // 判断是否需要根据操作员ID进行数据过滤
            String filterByOperatorId = jo.getString("filterByOperatorId");
            // 当前端明确指定需要进行过滤时，直接将flag设置为true
            if (StringUtils.isNotBlank(filterByOperatorId) && "true".equals(filterByOperatorId)) {
            	flag = "true";
            	
            // 当前端明确指定不需要进行过滤时，直接将flag设置为false
            } else if (StringUtils.isNotBlank(filterByOperatorId) && !"true".equals(filterByOperatorId)) {
            	flag = "false";
            }
            // 其他情况根据系统配置项决定
            // 过滤指派人员
            OmEmployee employee = SystemContext.getOmEmployee();
            if ("true".equals(flag)) {
                if(employee != null){
                    String sqlStr = " IDX in ( select distinct p.idx from ZB_ZBGL_PLAN p,ZB_ZBGL_PLAN_RECORD r,ZB_ZBGL_PLAN_WORKER t" +
                            " where p.idx = r.rdp_plan_idx and r.idx = t.rdp_record_idx and t.work_person_idx = '"+employee.getEmpid()+"' ) " ;
                    con = new Condition();
                    con.setCompare(Condition.SQL);
                    con.setSql(sqlStr);
                    whereList.add(con);
                }
            } 
            
            // 查询条件 - 状态
            if(!StringUtil.isNullOrBlank(entity.getRdpPlanStatus())){ 
                con = new Condition("rdpPlanStatus", Condition.EQ, entity.getRdpPlanStatus());
                con.setStringLike(false);
                whereList.add(con);
            }
            
            // 客货类型 10 货车，20 客车
            if(!StringUtil.isNullOrBlank(entity.getVehicleType())){ 
                con = new Condition("vehicleType", Condition.EQ, entity.getVehicleType());
                con.setStringLike(false);
                whereList.add(con);
            }
            
            // 添加通过站点过滤
            String siteID = EntityUtil.findSysSiteId(null);
            if(!StringUtil.isNullOrBlank(siteID)){
                con = new Condition("siteID", Condition.EQ, siteID);
                con.setStringLike(false);
                whereList.add(con);
            }
            
            QueryCriteria<ZbglRdpPlan> query = new QueryCriteria<ZbglRdpPlan>(ZbglRdpPlan.class, whereList, orderList, start, limit);
            Page<ZbglRdpPlan> page = this.zbglRdpPlanManager.findPageList(query);
            List<ZbglRdpPlan> list = page.getList();
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
     * <li>说明：查询列检车辆计划
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-3-31
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                rdpPlanIdx:"8a8284c35b233e87015b23416fb40002",
                rdpRecordStatus:""
            },
            operatorId: "800109",
            filterByOperatorId: "false",
            start:1,
            limit:50, 
            orders:[{
                sort: "seqNum",
                dir: "ASC"
            }]
       }
     * @return
     * @throws IOException
     */
    public String findZbglRdpRecordList(String jsonObject) throws IOException {
        
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
            ZbglRdpPlanRecord entity = JSONUtil.read(entityJson, ZbglRdpPlanRecord.class);
            
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
            
            // 是否查询指派人员的计划车辆
            String flag = SystemConfigUtil.getValue("knySys.freightSys.ZbglRdpPlanDispatch");
            // 判断是否需要根据操作员ID进行数据过滤
            String filterByOperatorId = jo.getString("filterByOperatorId");
            // 当前端明确指定需要进行过滤时，直接将flag设置为true
            if (StringUtils.isNotBlank(filterByOperatorId) && "true".equals(filterByOperatorId)) {
            	flag = "true";
            	
            // 当前端明确指定不需要进行过滤时，直接将flag设置为false
            } else if (StringUtils.isNotBlank(filterByOperatorId) && !"true".equals(filterByOperatorId)) {
            	flag = "false";
            }
            // 其他情况根据系统配置项决定
            if ("true".equals(flag)) {
                // 过滤指派人员
                OmEmployee employee = SystemContext.getOmEmployee();
                if(employee != null){
                    String sqlStr = " IDX in ( select distinct t.rdp_record_idx from ZB_ZBGL_PLAN_WORKER t where t.work_person_idx = '"+employee.getEmpid()+"' ) " ;
                    con = new Condition();
                    con.setCompare(Condition.SQL);
                    con.setSql(sqlStr);
                    whereList.add(con);
                }
            } 
            
            // 查询条件 - 计划ID (不能为空)
            if(!StringUtil.isNullOrBlank(entity.getRdpPlanIdx())){ 
                con = new Condition("rdpPlanIdx", Condition.EQ, entity.getRdpPlanIdx());
                con.setStringLike(false);
                whereList.add(con);
            }
            
            // 查询条件 - 状态
            if(!StringUtil.isNullOrBlank(entity.getRdpRecordStatus())){ 
                con = new Condition("rdpRecordStatus", Condition.EQ, entity.getRdpRecordStatus());
                con.setStringLike(false);
                whereList.add(con);
            }
            QueryCriteria<ZbglRdpPlanRecord> query = new QueryCriteria<ZbglRdpPlanRecord>(ZbglRdpPlanRecord.class, whereList, orderList, start, limit);
            Page<ZbglRdpPlanRecord> page = this.zbglRdpPlanRecordManager.findPageList(query);
            List<ZbglRdpPlanRecord> list = page.getList();
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
     * <li>说明：批量确认货车列检
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-04-01
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            ids:["8a8284c35ced2f47015ced3bd0e30003","8a8284c35ced2f47015ced3bd0e30004"],
            operatorId: "800109"
       }
     * @return
     * @throws IOException
     */
    public String bacthCompleteRecordHC(String jsonObject) throws IOException {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            
            // 获取查询条件实体对象
            String ids = jo.getString(Constants.IDX);
            String[] idArray = JSONUtil.read(ids, String[].class);
            // test 
            zbglRdpPlanRecordManager.bacthCompleteRecordHC(idArray);
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
     * <li>说明：填写车号
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                idx:"8a8284f24ab80704014ab891375a0004",
                trainNo:"0055"
            },
            operatorId: "800109"
       }
     * @return
     * @throws IOException
     */
    public String writeTrainNo(String jsonObject) throws IOException {
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
            ZbglRdpPlanRecord entity = JSONUtil.read(entityJson, ZbglRdpPlanRecord.class);
            ZbglRdpPlanRecord result = zbglRdpPlanRecordManager.writeTrainNo(entity);
            return JSONTools.toJSON(result, null);
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
     * <li>说明：客车库列检专业查询（按专业）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String findRdpWis(String jsonObject) throws IOException {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            String planIdx = jo.getString("planIdx");
            if(StringUtil.isNullOrBlank(planIdx)){
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("接口参数[计划ID]不能为空！"));
            }
            Page<ZbglRdpPlanWiBean> page = this.zbglRdpPlanRecordManager.findRdpWis(planIdx);
            List<ZbglRdpPlanWiBean> list = page.getList();
            if (null == list || list.size() <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            return JSONTools.toJSONList(page.getTotal(), list);
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
     * <li>说明：按专业查询车辆列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            planIdx:"8a8284c35cf1f528015cf261c39400f8",
            wiIdx:"8a8284c35cf16978015cf1b67ca50026",
            operatorId: "1039"
       }
     * @return
     * @throws IOException
     */
    public String findRecordByWi(String jsonObject) throws IOException {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            String planIdx = jo.getString("planIdx");
            if(StringUtil.isNullOrBlank(planIdx)){
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("接口参数[计划ID]不能为空！"));
            }
            String wiIdx = jo.getString("wiIdx");
            if(StringUtil.isNullOrBlank(wiIdx)){
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("接口参数[专业ID]不能为空！"));
            }
            Page<ZbglRdpPlanRecordBean> page = this.zbglRdpPlanRecordManager.findRecordByWi(planIdx,wiIdx);
            List<ZbglRdpPlanRecordBean> list = page.getList();
            if (null == list || list.size() <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            return JSONTools.toJSONList(page.getTotal(), list);
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
     * <li>说明：专业下批量保存车辆数据
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            recordIdxs:["8a8284c35cf1f528015cf261c39d00f9","8a8284c35cf1f528015cf261c39f00fa"],
            wiIdx:"8a8284c35cf16978015cf1b67ca50026",
            operatorId: "1039"
       }
     * @return
     * @throws IOException
     */
    public String saveZbglRdpWiDatas(String jsonObject) throws IOException {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            // 获取参数
            String recordIdxs = jo.getString("recordIdxs");
            if(StringUtil.isNullOrBlank(recordIdxs)){
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("接口参数[车辆ID数组]不能为空！"));
            }
            String wiIdx = jo.getString("wiIdx");
            if(StringUtil.isNullOrBlank(wiIdx)){
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("接口参数[专业ID]不能为空！"));
            }
            String[] idArray = JSONUtil.read(recordIdxs, String[].class);
            zbglRdpPlanRecordManager.saveZbglRdpWiDatas(idArray,wiIdx,operatorId);
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
     * <li>说明：同一车辆下批量保存专业数据（按车辆）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-7-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            recordIdx:"8a8284c35cf16978015cf1b67ca50026",
            wiIdxs:["8a8284c35cf1f528015cf261c39d00f9","8a8284c35cf1f528015cf261c39f00fa"],
            operatorId: "1039"
       }
     * @return
     * @throws IOException
     */    
    public String saveZbglRecordDatas(String jsonObject) throws IOException {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            // 获取参数
            String recordIdx = jo.getString("recordIdx");
            if(StringUtil.isNullOrBlank(recordIdx)){
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("接口参数[车辆ID]不能为空！"));
            }
            String wiIdxs = jo.getString("wiIdxs");
            if(StringUtil.isNullOrBlank(wiIdxs)){
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("接口参数[专业ID数组]不能为空！"));
            }
            String[] idArray = JSONUtil.read(wiIdxs, String[].class);
            zbglRdpPlanRecordManager.saveZbglRecordDatas(idArray,recordIdx,operatorId);
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
     * <li>说明：客车库列检车辆业查询（按车辆）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject{
            planIdx:"8a8284c35cf1f528015cf261c39400f8",
            operatorId: "1039"
       }
     * @return
     * @throws IOException
     */    
    public String findRecordsForKC(String jsonObject) throws IOException {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            String planIdx = jo.getString("planIdx");
            if(StringUtil.isNullOrBlank(planIdx)){
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("接口参数[计划ID]不能为空！"));
            }
            Page<ZbglRdpPlanRecordListBean> page = this.zbglRdpPlanRecordManager.findRecordsForKC(planIdx);
            List<ZbglRdpPlanRecordListBean> list = page.getList();
            if (null == list || list.size() <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            return JSONTools.toJSONList(page.getTotal(), list);
        } catch (Exception e) {
            if(e instanceof com.yunda.common.BusinessException){
                String msg = e.getMessage();
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(msg));
            }
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    
}
