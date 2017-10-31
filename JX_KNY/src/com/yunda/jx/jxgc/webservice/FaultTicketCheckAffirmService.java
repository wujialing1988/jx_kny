package com.yunda.jx.jxgc.webservice;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.Application;
import com.yunda.frame.common.Constants;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckAffirm;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketStatisticsNewVo;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketStatisticsVo;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketCheckAffirmManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketCheckRecordManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车提票检查确认实现类
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-12-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("faultTicketCheckAffirmWS")
public class FaultTicketCheckAffirmService implements IFaultTicketCheckAffirmService {
    
    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 提票检查确认业务 **/
    @Resource
    private FaultTicketCheckAffirmManager faultTicketCheckAffirmManager ;
    
    /** 提票业务类 */
    @Resource
    private FaultTicketManager faultTicketManager;
    
    /** 提票验收 */
    @Resource
    private FaultTicketCheckRecordManager faultTicketCheckRecordManager;
    
    
    
    
    /**
     * <li>说明：保存机车提票确认信息(实现)
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryCheckStatisticsList(String jsonObject) throws IOException {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        OperateReturnMessage msg = new OperateReturnMessage();
        List<FaultTicketStatisticsVo> list = null ;
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            // 机车检修作业计划主键
            String workPlanIDX = jo.getString("workPlanIDX");
            // 类型 1：确认 2：验收
            String type = jo.getString("type");
            list = faultTicketCheckAffirmManager.queryCheckStatisticsList(workPlanIDX,type);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        if (null == list || list.size() <= 0){
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        return JSONTools.toJSONList(list.size(), list);
    }
    
    
    /**
     * <li>说明：保存机车提票确认信息(实现)新
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryCheckStatisticsListNew(String jsonObject) throws IOException {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        OperateReturnMessage msg = new OperateReturnMessage();
        List<FaultTicketStatisticsNewVo> list = null ;
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);       
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
                return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
            }
            // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            // 机车检修作业计划主键
            String workPlanIDX = jo.getString("workPlanIDX");
            list = faultTicketCheckAffirmManager.queryCheckStatisticsListNew(workPlanIDX);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        if (null == list || list.size() <= 0){
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        return JSONTools.toJSONList(list.size(), list);
    }
    
    
    
    
    
    /**
     * <li>说明：确认
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人员ID
     * @param jsonObject 参数
     * @return
     * @throws IOException
     */
    public String saveAffirm(String jsonObject) throws IOException {
        try {
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
            // 机车作业计划主键
            String workPlanIDX = jo.getString("workPlanIDX");
            // 提票类型
            String faultTicketType = jo.getString("faultTicketType");
            faultTicketCheckAffirmManager.saveAffirm(workPlanIDX,faultTicketType);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：确认（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人员ID
     * @param jsonObject 参数
     * @return
     * @throws IOException
     */
    public String saveAffirmNew(String jsonObject) throws IOException {
        try {
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
            // 机车作业计划主键
            String workPlanIDX = jo.getString("workPlanIDX");
            // 提票类型
            String faultTicketType = jo.getString("faultTicketType");
            // 确认原因
            String affirmReason = jo.getString("affirmReason");
            // 确认角色
            String affirmRoleName = jo.getString("affirmRoleName");
            faultTicketCheckAffirmManager.saveAffirmNew(workPlanIDX,faultTicketType,affirmReason,affirmRoleName);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：验收
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人员ID
     * @param jsonObject 参数
     * @return
     * @throws IOException
     */
    public String saveCheck(String jsonObject) throws IOException {
        try {
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
            // 机车作业计划主键
            String workPlanIDX = jo.getString("workPlanIDX");
            // 提票类型
            String faultTicketType = jo.getString("faultTicketType");
            faultTicketCheckAffirmManager.saveCheck(workPlanIDX,faultTicketType);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：验收
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人员ID
     * @param jsonObject 参数
     * @return
     * @throws IOException
     */
    public String saveCheckNew(String jsonObject) throws IOException {
        try {
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
            // 机车作业计划主键
            String workPlanIDX = jo.getString("workPlanIDX");
            faultTicketCheckRecordManager.saveCheckNew(workPlanIDX);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
    
    
    
    /**
     * <li>说明：返修（实现）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人ID
     * @param jsonObject 提票信息
     * @return
     * @throws IOException
     */
    public String backCheckAffirm(String jsonData) throws IOException {
        try {
            if(StringUtil.isNullOrBlank(jsonData)) {
                throw new NullPointerException(MSG_ERROR_ARGS_NULL);
            }
            FaultTicket[] entityList = JSONUtil.read(jsonData, FaultTicket[].class);
            faultTicketCheckAffirmManager.backCheckAffirm(entityList);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
    /**
     * <li>说明：查询提票信息确认列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     */
    public String queryFaultTicketAffirm(String jsonObject) throws IOException {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        JSONObject jo = JSONObject.parseObject(jsonObject);       
        // 机车作业计划
        String workPlanIDX = jo.getString("workPlanIDX");
        // 提票类型
        String faultTicketType = jo.getString("faultTicketType");
        
        // 客货类型 10 货车 20 客车
        String vehicleType = jo.getString("vehicleType");
        
        FaultTicket bean = new FaultTicket();
        bean.setWorkPlanIDX(workPlanIDX);
        bean.setType(faultTicketType);
        bean.setVehicleType(vehicleType);
        List<FaultTicket> list = faultTicketManager.queryFaultTicketList(bean);
        // 设置确认人、验收人
        for (FaultTicket ticket : list) {
            String affirmUser = "" ;
            String checkUser = "" ;
            List<FaultTicketCheckAffirm> affirms = faultTicketCheckAffirmManager.getFaultTicketCheckAffirmListByFault(ticket.getIdx(),null);
            for (FaultTicketCheckAffirm affirm : affirms) {
                if(affirm.getStatusAffirm() != null && affirm.getStatusAffirm() == FaultTicket.AFFIRM_STATUS_DONE){
                    affirmUser += affirm.getAffirmEmp()+"，"+DateUtil.yyyy_MM_dd_HH_mm.format(affirm.getAffirmTime())+ "；\n" ;
                }
                if(affirm.getStatusAffirm() != null && affirm.getStatusAffirm() == FaultTicket.AFFIRM_STATUS_CHECK){
                    checkUser += affirm.getAffirmEmp()+"，"+DateUtil.yyyy_MM_dd_HH_mm.format(affirm.getAffirmTime())+ "；\n" ;
                }
            }
            if(!StringUtil.isNullOrBlank(affirmUser)){
                affirmUser = affirmUser.substring(0, affirmUser.length()-2);
            }
            if(!StringUtil.isNullOrBlank(checkUser)){
                checkUser = checkUser.substring(0, checkUser.length()-2);
            }
            ticket.setAffirmUser(affirmUser);
            ticket.setCheckUser(checkUser);
        }
        try {
            return JSONTools.toJSONList(list.size(), list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
    
    /**
     * <li>说明：查询提票信息验收列表（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     */
    public String queryFaultTicketCheck(String jsonObject) throws IOException {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        JSONObject jo = JSONObject.parseObject(jsonObject);       
        // 机车作业计划
        String workPlanIDX = jo.getString("workPlanIDX");
        List<FaultTicket> list = faultTicketCheckRecordManager.queryFaultTicketCheck(workPlanIDX);
        try {
            return JSONTools.toJSONList(list.size(), list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
    
    /**
     * <li>说明：查询确认信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryFaultTicketCheckAffirm(String jsonObject) throws IOException {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        JSONObject jo = JSONObject.parseObject(jsonObject);       
        // 票活ID
        String faultTicketIDX = jo.getString("faultTicketIDX");
        List<FaultTicketCheckAffirm> list = faultTicketCheckAffirmManager.getFaultTicketCheckAffirmListByFault(faultTicketIDX,FaultTicket.AFFIRM_STATUS_DONE);
        try {
            return JSONTools.toJSONList(list.size(), list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
    
    protected AcOperatorManager getAcOperatorManager() {
        return (AcOperatorManager) Application.getSpringApplicationContext().getBean("acOperatorManager");
    }
}
