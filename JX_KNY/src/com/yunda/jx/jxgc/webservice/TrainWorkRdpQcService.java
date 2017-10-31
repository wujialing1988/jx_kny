package com.yunda.jx.jxgc.webservice;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCardBean;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultManager;
import com.yunda.jx.jxgc.producttaskmanage.qualitychek.manager.QCResultQueryManager;
import com.yunda.jx.jxgc.webservice.entity.TrainWorkPlanBean;
import com.yunda.jx.jxgc.workplanmanage.entity.WorkPlanRepairActivityDTO;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;



/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: （工位终端）机车质量检查项
 * <li>创建人：张迪
 * <li>创建日期：2016-8-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainWorkRdpQcWS")
public class TrainWorkRdpQcService implements ITrainWorkRdpQcService{
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    @Resource
    private QCResultQueryManager qCResultQueryManager;
    
    @Resource
    private WorkCardManager workCardManager ;
    
    @Resource
    private QCResultManager qCResultManager;

    /**
     * <li>说明：获取在修机车质量检查
     * <li>创建人：张迪
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json字符串
     * @return 在修机车质量检查列表
     */
    public String getTrainRdpQCList (String jsonObject){
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        JSONObject jo = JSONObject.parseObject(jsonObject);       
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        //如果有查询参数可以封装查询参数
        TrainWorkPlanBean searchBean = new TrainWorkPlanBean();
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
        
        // 模糊查询
        String trainTypeAndNo = jo.getString("trainTypeAndNo");
        if(!StringUtil.isNullOrBlank(trainTypeAndNo)){
            searchBean.setTrainTypeShortName(trainTypeAndNo);
        }
        
        // 客货类型
        String vehicleType = jo.getString("vehicleType");
        if(!StringUtil.isNullOrBlank(vehicleType)){
            searchBean.setVehicleType(vehicleType);
        }
        try {      
            Page<TrainWorkPlanBean> page = qCResultQueryManager.getRdpQCList(operatorId,new SearchEntity<TrainWorkPlanBean>(searchBean, start, limit, orders));  
            return JSONUtil.write(page);
        } catch (Exception e) {
            ExceptionUtil.process(e,logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    /**
     * <li>说明：获取检修记录单列表
     * <li>创建人：张迪
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json字符串
     * @return 检修记录单
     * @throws NoSuchFieldException
     * @throws IOException
     */
    public String getTrainRecordList(String jsonObject) throws NoSuchFieldException, IOException{
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        JSONObject jo = JSONObject.parseObject(jsonObject);       
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }  
//        Boolean isAll = jo.getBoolean("isAll");
        try {
            Page<WorkPlanRepairActivityDTO> page = qCResultQueryManager.getRecordQCList(jsonObject);          
            return JSONUtil.write(page);
        } catch (SecurityException e) {
            ExceptionUtil.process(e,logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    /**
     * <li>说明：查询检修记录卡列表
     * <li>创建人：张迪
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject  json字符串
     * @return 检修记录卡列表
     * @throws Exception
     */
    public String  getTrainCardList (String jsonObject) throws Exception{
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        JSONObject jo = JSONObject.parseObject(jsonObject);       
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }  
        
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        //获取数据
        String rdpRecordIDX = jo.getString("rdpRecordIDX");//检修记录单主键idx
        try {
            List<WorkCardBean> workCardBean = workCardManager.findWorkCardInfoByWorkPlanRepairActivityIDX(rdpRecordIDX);   
            return JSONUtil.write(workCardBean);
        } catch (BusinessException e) {
            ExceptionUtil.process(e,logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：返修工位终端
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public String updateToBack(String jsonObject) throws Exception{
        JSONObject jo = JSONObject.parseObject(jsonObject);
        
        // 当前作业处理人员ID
        Long operatorId = jo.getLong(Constants.OPERATOR_ID);
        if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
        // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        
        if(StringUtil.isNullOrBlank(jo.getString("rdpWorkCardIDXs"))){
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance("记录卡主键不能为空！"));
        }
        
        // 获取检修记录工单主键数组
        String[] rdpWorkCardIDXs = JSONUtil.read(jo.getString("rdpWorkCardIDXs"), String[].class);
        String qcItemNo = jo.getString("qcItemNo");
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            // 返修
            this.qCResultManager.updateToBack(rdpWorkCardIDXs, qcItemNo);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
        
    }
    
}