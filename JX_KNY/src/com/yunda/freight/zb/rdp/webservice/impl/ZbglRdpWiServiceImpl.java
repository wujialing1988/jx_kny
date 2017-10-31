package com.yunda.freight.zb.rdp.webservice.impl;

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
import com.yunda.freight.zb.plan.manager.ZbglRdpPlanRecordManager;
import com.yunda.freight.zb.rdp.webservice.IZbglRdpWiService;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWi;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWidi;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWiManager;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWidiManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 列检任务处理实现类
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-4-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("zbglRdpWiServiceWS")
public class ZbglRdpWiServiceImpl implements IZbglRdpWiService {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    /** 任务记录卡服务 **/
    @Resource
    private ZbglRdpWiManager zbglRdpWiManager ;
    
    /** 任务数据项服务 **/
    @Resource
    private ZbglRdpWidiManager zbglRdpWidiManager ;
    
    /** 车辆计划服务 **/
    @Resource
    private ZbglRdpPlanRecordManager zbglRdpPlanRecordManager ;
    
    
    /**
     * <li>说明：查询任务及其下的数据项
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson: {
                rdpIDX:"8a8284c35b276ca1015b2789390c0010",
                nodeIDX:"8a8284c35b276ca1015b278939280011",
                wiName:""
            },
            start:1,
            limit:50, 
            orders:[{
                sort: "seqNo",
                dir: "ASC"
            }]
       }
     * @return 
     * @throws IOException
     */
    public String findZbglRdpWiDiList(String jsonObject) throws IOException {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            ZbglRdpWi entity = JSONUtil.read(entityJson, ZbglRdpWi.class);
            
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
            // 查询条件 - 整备任务单
            if(!StringUtil.isNullOrBlank(entity.getRdpIDX())){ 
                con = new Condition("rdpIDX", Condition.EQ, entity.getRdpIDX());
                con.setStringLike(false);
                whereList.add(con);
            }
            // 查询条件 - 节点
            if(!StringUtil.isNullOrBlank(entity.getNodeIDX())){ 
                con = new Condition("nodeIDX", Condition.EQ, entity.getNodeIDX());
                con.setStringLike(false);
                whereList.add(con);
            }
            QueryCriteria<ZbglRdpWi> query = new QueryCriteria<ZbglRdpWi>(ZbglRdpWi.class, whereList, orderList, start, limit);
            Page<ZbglRdpWi> page = this.zbglRdpWiManager.findPageList(query);
            List<ZbglRdpWi> list = page.getList();
            if (null == list || list.size() <= 0) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
            }
            // 查询数据项
            for (ZbglRdpWi wi : list) {
                List<ZbglRdpWidi> zbglRdpWidis = zbglRdpWidiManager.getModels(wi.getIdx());
                wi.setZbglRdpWidis(zbglRdpWidis);
            }
            return JSONTools.toJSONList(page.getTotal(), list);
        }catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    /**
     * <li>说明：保存数据项
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject {
            entityJson : [{
                idx:"D89F538E6A9E4C7689670165A3500E77",
                rdpWiIDX:"A1323DBA169B47309A132B9289DF6C9E",
                diResult:""
            }],
            isHg:"是",
            operatorId: "800109"
       }
     * @return 
     * @throws IOException
     */
    public String saveZbglRdpDiDatas(String jsonObject) throws IOException {
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            String isHg = jo.getString("isHg");
            // 获取查询条件实体对象
            String entityJson = jo.getString(Constants.ENTITY_JSON);
            ZbglRdpWidi[] widiArray = JSONUtil.read(StringUtil.nvlTrim(entityJson, "[]"), ZbglRdpWidi[].class);
            if(widiArray != null && widiArray.length > 0){
                String rdpWiIDX = widiArray[0].getRdpWiIDX();
                zbglRdpWiManager.updateForHandleRdp(operatorId, rdpWiIDX, isHg, widiArray,"");
                // 完成车辆计划
                zbglRdpPlanRecordManager.completeRecordByRdpWiIDX(rdpWiIDX);
            }else{
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("没有需要提交的数据！"));
            }
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
}
