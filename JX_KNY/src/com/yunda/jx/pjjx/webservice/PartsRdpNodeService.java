package com.yunda.jx.pjjx.webservice;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
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
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNode;
import com.yunda.jx.pjjx.partsrdp.wpinst.entity.PartsRdpNodeBean;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsNodeReManager;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeManager;
import com.yunda.jx.pjjx.partsrdp.wpinst.manager.PartsRdpNodeQueryManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.webservice.common.entity.OperateReturnMessage;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 用于扩展配件检修节点任务需对接的方法
 * <li>http://localhost:8080/CoreFrame/ydservices/PartsRdpNodeService?wsdl
 * <li>创建人：程锐
 * <li>创建日期：2015-9-28
 * <li>修改人: 何涛
 * <li>修改日期：2016-03-31
 * <li>修改内容：单元测试，优化代码
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@Service(value="partsRdpNodeWS")
public class PartsRdpNodeService implements IPartsRdpNodeService {

    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    /** 配件检修作业节点查询业务类 */
    @Resource
    private PartsRdpNodeQueryManager partsRdpNodeQueryManager;
    
    /** PartsRdpNode业务类,配件检修作业节点 */
    @Resource
    private PartsRdpNodeManager partsRdpNodeManager;
    
    /** 返修节点业务类 */
    @Resource
    private PartsNodeReManager partsNodeReManager;
    
    /**
     * <li>说明：查询当前人员可以处理的检修作业节点任务
     * <li>创建人：程锐
     * <li>创建日期：2015-9-28
     * <li>修改人：何涛
     * <li>修改日期：2016-03-07
     * <li>修改内容：重构，优化接口异常处理（测试通过）
     * <li>修改人：何涛
     * <li>修改日期：2016-03-21
     * <li>修改内容：增加查询2个查询参数
     *              1、名称规格型号（unloadTrainType）
     *              2、名称规格型号（specificationModel）
     * @param jsonObject json对象
     * {
         operatorId: "800109",
         start:0,
         limit:50, 
         orders:[{
                sort: "idx",
                dir: "ASC"
            }],
         workStationIDX: "800109",
         status: 1,
         partsNo: "800109",
         identificationCode: "800109",
         unloadTrainType: "HXD10002",
         specificationModel: "转向架"
       }                
     * @return 当前人员可以处理的检修作业节点任务
     */
    @Override
    public String queryNodeListByUser (String jsonObject){
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue("limit");
        start = limit * (start - 1);
        
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        PartsRdpNodeBean bean = new PartsRdpNodeBean();
        bean.setWorkStationIDX(jo.getString("workStationIDX"));
        bean.setStatus(jo.getString("status"));
        bean.setIdentificationCode(jo.getString("identificationCode"));
        bean.setPartsNo(jo.getString("partsNo"));
        bean.setUnloadTrainType(jo.getString("unloadTrainType"));
        bean.setSpecificationModel(jo.getString("specificationModel"));
        Page<PartsRdpNodeBean> page = partsRdpNodeQueryManager.queryNodeListByUser(new SearchEntity<PartsRdpNodeBean>(bean, start, limit, orders));
        try {
            return JSONTools.toJSONList(page);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
    
    /**
     * <li>说明：查询当前人员可以处理的检修作业节点任务的数量
     * <li>创建人：程锐
     * <li>创建日期：2015-10-14
     * <li>修改人：何涛
     * <li>修改日期：2016-03-07
     * <li>修改内容：重构，优化接口异常处理
     * @param jsonObject 
     * {
         operatorId: "800109",
         start:0,
         limit:50, 
         orders:[{
                    sort: "idx",
                    dir: "ASC"
                }],
         workStationIDX: "800109",
         status: 1,
         partsNo: "800109",
         identificationCode: "800109"
       }                
     * @return 当前人员可以处理的检修作业节点任务的数量
     */
    @Deprecated
    public String queryNodeCountByUser (String jsonObject){
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        PartsRdpNodeBean bean = new PartsRdpNodeBean();
        bean.setWorkStationIDX(jo.getString("workStationIDX"));
        bean.setStatus(jo.getString("status"));
        bean.setIdentificationCode(jo.getString("identificationCode"));
        bean.setPartsNo(jo.getString("partsNo"));
        int count = partsRdpNodeQueryManager.queryNodeCountByUser(new SearchEntity<PartsRdpNodeBean>(bean, 1, 1, null));
        return String.valueOf(count);
    }
    
    /**
     * <li>说明：根据作业流程IDX查询其关联的节点任务列表
     * <li>创建人：程锐
     * <li>创建日期：2015-9-28
     * <li>修改人：何涛
     * <li>修改日期：2016-03-07
     * <li>修改内容：重构，优化接口异常处理（测试通过）
     * @param jsonObject JSON对象
       {
         rdpIDX: "8a8284f25146a0d8015146a7670d0003"
       }
     * @return 所属的配件检修流程的节点任务列表
     */
    @Override
    public String queryNodeListByRdp (String jsonObject){
        JSONObject jo = JSONObject.parseObject(jsonObject);
        String rdpIDX = jo.getString("rdpIDX");
        List<PartsRdpNode> list = partsRdpNodeQueryManager.queryNodeListByRdp(rdpIDX);
        try {
            return JSONTools.toJSONList(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
    
    /**
     * <li>说明：修竣提交节点
     * <li>创建人：程锐
     * <li>创建日期：2015-9-28
     * <li>修改人：何涛
     * <li>修改日期：2016-03-07
     * <li>修改内容：重构，优化接口异常处理（测试通过）
     * @param jsonObject JSON对象
     {
         operatorId: "800109",
         idx: "8a8284f25146a0d8015146a7670d0003"
     }
     * @return 操作成功与否
     */
    @Override
    public String finishPartsNode (String jsonObject) throws IOException {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 配件检修作业节点主键
        String idx = jo.getString("idx");
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            partsRdpNodeManager.updateFinishedStatus(idx);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
    
    /**
     * <li>说明：修竣提交节点前的验证
     * <li>创建人：程锐
     * <li>创建日期：2015-9-28
     * <li>修改人：何涛
     * <li>修改日期：2016-03-07
     * <li>修改内容：重构，优化接口异常处理（测试通过）
     * @param jsonObject JSON对象
     {
        idx: "8a8284f25146a0d8015146a7670d0003"
     }
     * @return 1验证通过：return "{'flag':'true','message':'操作成功！'}"; 
			   2验证发现检修记录单或作业工单未完成，工位终端收到此信息应做提示处理：
						return "{'flag':'true','message':'作业工单未全部处理 '}";
			   3验证时报错：
						return "{'flag':'false','message':错误信息}";
     */
    @Override
    public String validateFinishedStatus (String jsonObject) throws IOException {
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            // 配件检修作业节点主键
            String idx = jo.getString("idx");            
            String validateMsg = partsRdpNodeManager.validateFinishedStatus(idx);
            if (null != validateMsg) {
                msg.setSucessFlag(validateMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
    
    /**
     * <li>说明：配件检修任务单-启动节点
     * <li>创建人：程锐
     * <li>创建日期：2015-9-30
     * <li>修改人：何涛
     * <li>修改日期：2016-03-07
     * <li>修改内容：重构，优化接口异常处理（测试通过）
     * @param jsonObject
     {
         operatorId: "800109",
         idx: "C22FA642BF2641CB94B48A6FD5EBE734"
     }
     * @return 操作成功与否
     */
    @Override
    public String startPartsNode (String jsonObject) {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
        }
        JSONObject jo = JSONObject.parseObject(jsonObject);
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 设置系统用户信息
            String idx = jo.getString("idx");
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            partsRdpNodeManager.startUp(idx);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONObject.toJSONString(msg); 
    }

    /**
     * <li>说明：返修
     * <li>创建人：张凡
     * <li>创建日期：2015-10-30
     * <li>修改人：何涛
     * <li>修改日期：2016-03-07
     * <li>修改内容：重构，优化接口异常处理（测试通过）
     * @param jsonObject 
     { 
         "operatorId": "800109",
         "rdpNodeIdx": "c22fa642bf2641cb94b48a6fd5ebe734",
         "cause": "回退原因" 
     }
     * @return 操作成功与否
     */
    @Override
    public String backRepair(String jsonObject){
        JSONObject jo = JSONObject.parseObject(jsonObject);
//        String rdpNodeIdx = jo.getString("rdpNodeIdx");
//        if (StringUtil.isNullOrBlank(rdpNodeIdx)) {
//            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("配件检修作业节点主键不能为空！"));
//        }
        String rdpIdx = jo.getString("rdpIdx");
        if (StringUtil.isNullOrBlank(rdpIdx)) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("配件检修作业主键不能为空！"));
        }
        String cause = jo.getString("cause");
        if (StringUtil.isNullOrBlank(cause)) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("返修原因不能为空！"));
        }
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            partsRdpNodeManager.updateBack(rdpIdx, cause);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
		return JSONObject.toJSONString(msg);
    }
    
    /**
     * <li>说明：获取树节点(无处调用，考虑删除)
     * <li>创建人：张凡
     * <li>创建日期：2015-10-15
     * <li>修改人：何涛
     * <li>修改日期：2016-03-07
     * <li>修改内容：重构，优化接口异常处理（测试通过）
     * @param jsonObject 
     { 
         "rdpIdx": "8a8284f25146a0d8015146a7670d0003", 
         "parentNodeIdx": "ROOT_0" 
     }
     * @return 配件检修作业流程节点树集合
     */
	@Override
	public String treeNodes(String jsonObject) {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
        }
        JSONObject o = JSONObject.parseObject(jsonObject);
        String rdpIdx = o.getString("rdpIdx");
        if(StringUtil.isNullOrBlank(rdpIdx)) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("配件检修作业主键不能为空！"));
        }
        String parentNodeIdx = o.getString("parentNodeIdx");
        PartsRdpNode entity = new PartsRdpNode();
        entity.setRdpIDX(rdpIdx);
        entity.setParentWPNodeIDX(parentNodeIdx);
        List<PartsRdpNode> list = partsRdpNodeManager.findList(entity);
        try {
            return JSONTools.toJSONList(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
	}
    
    /**
     * <li>说明：获取配件作业计划的所有子节点列表
     * <li>创建人：程锐
     * <li>创建日期：2015-11-23
     * <li>修改人：何涛
     * <li>修改日期：2016-03-07
     * <li>修改内容：重构，优化接口异常处理（测试通过）
     * @param jsonObject JSON对象
       {
         rdpIDX: "8a8284f25146a0d8015146a7670d0003"
       }
     * @return 配件作业计划的所有子节点列表
     */
    @Override
    public String getLeafNodeListByRdp (String jsonObject){
        if(StringUtil.isNullOrBlank(jsonObject)) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL));
        }

        JSONObject jo = JSONObject.parseObject(jsonObject);
        String rdpIDX = jo.getString("rdpIDX");
        List<PartsRdpNode> list = partsRdpNodeQueryManager.getLeafNodeListByRdp(rdpIDX);
        try {
            return JSONTools.toJSONList(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }    

    /**
     * <li>说明：获取返修原因
     * <li>创建人：程锐
     * <li>创建日期：2016-2-3
     * <li>修改人：何涛
     * <li>修改日期：2016-03-07
     * <li>修改内容：重构，优化接口异常处理（测试通过）
     * @param jsonObject 
     {
        "rdpNodeIDX": "c22fa642bf2641cb94b48a6fd5ebe734"
     }
     * @return 返修原因
     */
    @Override
    public String getBackRepairCause(String jsonObject) {
        JSONObject jo = JSONObject.parseObject(jsonObject);
        String rdpNodeIDX = jo.getString("rdpNodeIDX");
        return partsNodeReManager.getCausesByNode(rdpNodeIDX);
    }

}
