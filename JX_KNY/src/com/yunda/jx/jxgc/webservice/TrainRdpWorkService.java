package com.yunda.jx.jxgc.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXSystemProperties;
import com.yunda.frame.common.Page;
import com.yunda.frame.common.SearchEntity;
import com.yunda.frame.common.hibernate.Condition;
import com.yunda.frame.common.hibernate.QueryCriteria;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.OmEmployee;
import com.yunda.frame.yhgl.manager.OmEmployeeManager;
import com.yunda.jx.jxgc.producttaskmanage.entity.NodeCaseDelay;
import com.yunda.jx.jxgc.producttaskmanage.entity.WorkCard;
import com.yunda.jx.jxgc.producttaskmanage.manager.NodeCaseDelayManager;
import com.yunda.jx.jxgc.producttaskmanage.manager.WorkCardManager;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicket;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckRecord;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketStatisticsNewVo;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketCheckAffirmManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketCheckRecordManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketManager;
import com.yunda.jx.jxgc.webservice.entity.TrainWorkPlanNodeBean;
import com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode;
import com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeManager;
import com.yunda.jx.jxgc.workplanmanage.manager.JobProcessNodeQueryManager;
import com.yunda.jx.jxgc.workplanmanage.manager.TrainWorkPlanQueryManager;
import com.yunda.jx.pjjx.util.JSONTools;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.jx.webservice.stationTerminal.base.entity.WorkTaskBean;
import com.yunda.webservice.common.entity.OperateReturnMessage;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 用于扩展机车检修任务处理需对接的方法的实现
 * <li>创建人：张迪
 * <li>创建日期：2016-6-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainRdpWorkWS")
public class TrainRdpWorkService implements ITrainRdpWorkService {
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    /** 车检修作业计划查询业务类 */
    @Resource
    private TrainWorkPlanQueryManager trainWorkPlanQueryManager ;
    
    /** 机车检修作业计划节点查询业务类 */
    @Resource
    private JobProcessNodeQueryManager jobProcessNodeQueryManager ;
    
    /** 机车检修作业计划节点业务类 */
    @Resource
    private JobProcessNodeManager jobProcessNodeManager;
    /** 机车检修记录卡业务类 */
    @Resource
    private WorkCardManager workCardManager;
    /** 工序延误业务类 */
    @Resource
    private NodeCaseDelayManager nodeCaseDelayManager;
    /** 提票业务类 */
    @Resource
    private FaultTicketManager faultTicketManager;
    /** 人员业务类 */
    @Resource
    private OmEmployeeManager omEmployeeManager;
    /** 提票确认验收服务 */
    @Resource
    private FaultTicketCheckAffirmManager faultTicketCheckAffirmManager;
    
    /** 提票验收 */
    @Resource
    private FaultTicketCheckRecordManager faultTicketCheckRecordManager;
    
    
    /**
     * <li>说明：修竣提交节点
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 提示信息
     * @throws IOException 
     */
    public String finishNode(String jsonObject) throws IOException {
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            // 配件检修作业节点主键
            String idx = jo.getString("idx"); 
            Long operatorId = jo.getLong(Constants.OPERATOR_ID);
            if (null == operatorId) {
               return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
           }
            JobProcessNode jobProcessNode = jobProcessNodeManager.getModelById(idx);
            String validateMsg = jobProcessNodeManager.updateFinishNodeCase(jobProcessNode);
            if (null != validateMsg && !"".equals(validateMsg)) {
                msg.setFaildFlag(validateMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
    
    /**
     * <li>说明：修竣提交节点前的验证
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     *  {
        idx: "8a8284f25146a0d8015146a7670d0003"
     }
     * @return 提示信息
     *       1验证通过：return "{'flag':'true','message':'操作成功！'}";
        2验证发现检修记录单未完成，工位终端收到此信息应做提示处理：
        return "{'flag':'true','message':'机车检修记录单未全部处理 '}";
        3验证时报错：
                    return "{'flag':'false','message':错误信息}"
;
     * @throws IOException 
     */
    public String validateFinishedStatus(String jsonObject) throws IOException {
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            // 配件检修作业节点主键
            String idx = jo.getString("idx");            
            String validateMsg = jobProcessNodeManager.validateFinishedStatus(idx);
            if (null != validateMsg) {
                msg.setFaildFlag(validateMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONUtil.write(msg);
    }
 
    /**
     * <li>说明：查询当前工位的机车检修节点任务列表 
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 当前工位的机车检修节点任务列表
     */
    public String queryNodeListByWorkStation(String jsonObject) {
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
        // 查询记录开始索引
        int start = jo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = jo.getIntValue("limit");
        start = limit * (start - 1);
        // 排序字段
        JSONArray jArray = jo.getJSONArray(Constants.ORDERS);
        Order[] orders = JSONTools.getOrders(jArray);
        JobProcessNode bean = new JobProcessNode();
        bean.setWorkStationIDX(jo.getString("workStationIDX"));    
        bean.setStatus(jo.getString("status"));
        bean.setNodeName(jo.getString("nodeName"));
        bean.setWorkPlanIDX(jo.getString("workPlanIDX"));
        
        // 客货类型
        String vehicleType = jo.getString("vehicleType");
        
        Page<TrainWorkPlanNodeBean> page = jobProcessNodeQueryManager.queryNodeListByWorkStation(new SearchEntity<JobProcessNode>(bean, start, limit, orders),vehicleType);
        try {
            return JSONTools.toJSONList(page);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
    /**
     * <li>说明：查询当前工位的检修作业节点任务数量
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 当前工位的检修作业节点任务数量
     */
    public String queryNodeCountByWorkStation(String jsonObject) {
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
        JobProcessNode bean = new JobProcessNode();
        bean.setWorkStationIDX(jo.getString("workStationIDX"));
        bean.setStatus(jo.getString("status"));
        bean.setNodeName(jo.getString("nodeName"));
        bean.setWorkPlanIDX(jo.getString("workPlanIDX"));
        
        // 客货类型
        String vehicleType = jo.getString("vehicleType");
        
        int count = jobProcessNodeQueryManager.queryNodeCountByWorkStation(new SearchEntity<JobProcessNode>(bean, 1, 1, null),vehicleType);
        return String.valueOf(count);
    }
    
    /**
     * <li>说明：获取范围活数量
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @param workPlanIDX 检修任务单idx
     * @param status 状态1：未处理 状态2：已处理
     * @return
     */
    public String queryNodeCountByWorkStationNew(String jsonObject,String workPlanIDX,String status) {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            return 0 + "" ;
        }
         JSONObject jo = JSONObject.parseObject(jsonObject);
         Long operatorId = jo.getLong(Constants.OPERATOR_ID);
         if (null == operatorId) {
             return 0 + "" ;
        }
                // 设置系统用户信息
        SystemContextUtil.setSystemInfoByOperatorId(operatorId);
        JobProcessNode bean = new JobProcessNode();
        bean.setStatus(status);
        bean.setWorkPlanIDX(workPlanIDX);
        
        // 客货类型
        String vehicleType = jo.getString("vehicleType");
        
        int count = jobProcessNodeQueryManager.queryNodeCountByWorkStation(new SearchEntity<JobProcessNode>(bean, 1, 1, null),vehicleType);
        return String.valueOf(count);
    }
    
   

    /**
     * <li>说明：查询当前在修的机车列表
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 当前在修的机车列表
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public String queryZXJCList(String jsonObject) throws IOException {
        
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 客货类型
        String vehicleType = jo.getString("vehicleType");
        
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("workPlanStatus", Condition.IN_STR.concat(Constants.SINGLE_QUOTE_MARK.concat(TrainWorkPlan.STATUS_HANDLING).concat(Constants.SINGLE_QUOTE_MARK)));
        paramMap.put("vehicleType", Condition.EQ_STR.concat(Constants.SINGLE_QUOTE_MARK.concat(vehicleType).concat(Constants.SINGLE_QUOTE_MARK)));
        List<TrainWorkPlan> workPlanList = trainWorkPlanQueryManager.getTrainWorkPlanList(paramMap);
        if (null == workPlanList || workPlanList.size() <= 0) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        
        List<Map<String, Object>> entityList = new ArrayList<Map<String,Object>>(workPlanList.size());
        Map<String, Object> typeJson = null;
        for (TrainWorkPlan workPlan : workPlanList) {
            typeJson = new HashMap<String, Object>();
            
            typeJson.put("idx", workPlan.getIdx());                                 // 机车检修作业计划主键
//            typeJson.put("typeID", workPlan.getTrainTypeIDX());                                 // 车型代码
            typeJson.put("trainTypeShortName", workPlan.getTrainTypeShortName());   // 车型名称
            typeJson.put("trainNo", workPlan.getTrainNo());                        // 车号
            typeJson.put("repairClassRepairTime", workPlan.getRepairClassName()+ workPlan.getRepairtimeName());            // 修程修次
            
            entityList.add(typeJson);
        }
        return JSONTools.toJSONList(workPlanList.size(), entityList);
    }
    
    
    /**
     * <li>说明：查询当前在修的机车列表(提票确认)
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 当前在修的机车列表
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public String queryZXJCListAffirm() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("workPlanStatus", Condition.IN_STR.concat(Constants.SINGLE_QUOTE_MARK.concat(TrainWorkPlan.STATUS_HANDLING).concat(Constants.SINGLE_QUOTE_MARK)));
        List<TrainWorkPlan> workPlanList = trainWorkPlanQueryManager.getTrainWorkPlanList(paramMap);
        if (null == workPlanList || workPlanList.size() <= 0) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        List<Map<String, Object>> entityList = new ArrayList<Map<String,Object>>(workPlanList.size());
        Map<String, Object> typeJson = null;
        for (TrainWorkPlan workPlan : workPlanList) {
            // 获取提票验收记录，如果已经验收，则不显示了
            FaultTicketCheckRecord record = faultTicketCheckRecordManager.getFaultTicketCheckRecord(workPlan.getIdx());
            if(record == null){
                typeJson = new HashMap<String, Object>();
                typeJson.put("idx", workPlan.getIdx());                                 // 机车检修作业计划主键
                typeJson.put("trainTypeShortName", workPlan.getTrainTypeShortName());   // 车型名称
                typeJson.put("trainNo", workPlan.getTrainNo());                        // 车号
                typeJson.put("repairClassRepairTime", workPlan.getRepairClassName()+ workPlan.getRepairtimeName());            // 修程修次
                // 查询确认数量提示
                List<FaultTicketStatisticsNewVo> affirms = faultTicketCheckAffirmManager.queryCheckStatisticsListNew(workPlan.getIdx());
                Integer doneAffirm  = 0; // 已确认
                Integer doneCheck = 0 ; // 已验收
                Integer doneCount = 0 ; // 已处理
                Integer undoCount = 0 ; // 未处理
                if(affirms != null){
                    for (FaultTicketStatisticsNewVo vo : affirms) {
                        doneAffirm += vo.getDoneAffirm();
                        doneCheck += vo.getDoneCheck();
                        doneCount += vo.getDoneCount();
                        undoCount += vo.getUndoCount();
                    }
                }
                typeJson.put("doneAffirm", doneAffirm); 
                typeJson.put("doneCheck", doneCheck); 
                typeJson.put("doneCount", doneCount); 
                typeJson.put("undoCount", undoCount); 
                entityList.add(typeJson);
            }
        }
        return JSONTools.toJSONList(workPlanList.size(), entityList);
    }
    
    /**
     * <li>说明：查询当前在修的机车列表(提票验收)
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 当前在修的机车列表
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public String queryZXJCListCheck() throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("workPlanStatus", Condition.IN_STR.concat(Constants.SINGLE_QUOTE_MARK.concat(TrainWorkPlan.STATUS_HANDLING).concat(Constants.SINGLE_QUOTE_MARK)));
        List<TrainWorkPlan> workPlanList = trainWorkPlanQueryManager.getTrainWorkPlanList(paramMap);
        if (null == workPlanList || workPlanList.size() <= 0) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        List<Map<String, Object>> entityList = new ArrayList<Map<String,Object>>(workPlanList.size());
        Map<String, Object> typeJson = null;
        for (TrainWorkPlan workPlan : workPlanList) {
            // 获取提票验收记录，如果已经验收，则不显示了
            FaultTicketCheckRecord record = faultTicketCheckRecordManager.getFaultTicketCheckRecord(workPlan.getIdx());
            if(record == null){
                typeJson = new HashMap<String, Object>();
                typeJson.put("idx", workPlan.getIdx());                                 // 机车检修作业计划主键
                typeJson.put("trainTypeShortName", workPlan.getTrainTypeShortName());   // 车型名称
                typeJson.put("trainNo", workPlan.getTrainNo());                        // 车号
                typeJson.put("repairClassRepairTime", workPlan.getRepairClassName()+ workPlan.getRepairtimeName());            // 修程修次
                typeJson.put("isCanCheck", faultTicketCheckRecordManager.isCanCheck(workPlan.getIdx())); // 是否可验收
                entityList.add(typeJson);
            }
        }
        return JSONTools.toJSONList(entityList.size(), entityList);
    }
    
    
    
    /**
     * <li>说明：查询当前在修的机车列表 带范围活提票活数量
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryZXJCListJCJX(String jsonObject) throws IOException {
        
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 客货类型
        String vehicleType = jo.getString("vehicleType");
        
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("workPlanStatus", Condition.IN_STR.concat(Constants.SINGLE_QUOTE_MARK.concat(TrainWorkPlan.STATUS_HANDLING).concat(Constants.SINGLE_QUOTE_MARK)));
        if(!StringUtil.isNullOrBlank(vehicleType)){
            paramMap.put("vehicleType", vehicleType);
        }
        List<TrainWorkPlan> workPlanList = trainWorkPlanQueryManager.getTrainWorkPlanList(paramMap);
        if (null == workPlanList || workPlanList.size() <= 0) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        
        List<Map<String, Object>> entityList = new ArrayList<Map<String,Object>>(workPlanList.size());
        Map<String, Object> typeJson = null;
        for (TrainWorkPlan workPlan : workPlanList) {
            typeJson = new HashMap<String, Object>();
            typeJson.put("idx", workPlan.getIdx());                                 // 机车检修作业计划主键
            typeJson.put("trainTypeShortName", workPlan.getTrainTypeShortName());   // 车型名称
            typeJson.put("trainNo", workPlan.getTrainNo());                        // 车号
            typeJson.put("repairClassRepairTime", workPlan.getRepairClassName()+ workPlan.getRepairtimeName());            // 修程修次
            typeJson.put("fwhUndoCount", this.queryNodeCountByWorkStationNew(jsonObject, workPlan.getIdx(),"1"));            // 范围活
            typeJson.put("tphUndoCount", this.queryFaultTicketCountNew(jsonObject, workPlan.getIdx(),1));            // 提票活
            entityList.add(typeJson);
        }
        return JSONTools.toJSONList(workPlanList.size(), entityList);
    }
    
    /**
     * <li>说明：提票活处理
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 
     * @return
     * @throws IOException
     */
    public String queryZXJCListTph(String jsonObject) throws IOException {
        
        JSONObject jo = JSONObject.parseObject(jsonObject);
        // 客货类型
        String vehicleType = jo.getString("vehicleType");
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("workPlanStatus", Condition.IN_STR.concat(Constants.SINGLE_QUOTE_MARK.concat(TrainWorkPlan.STATUS_HANDLING).concat(Constants.SINGLE_QUOTE_MARK)));
        if(!StringUtil.isNullOrBlank(vehicleType)){
            paramMap.put("vehicleType", vehicleType);
        }
        List<TrainWorkPlan> workPlanList = trainWorkPlanQueryManager.getTrainWorkPlanList(paramMap);
        if (null == workPlanList || workPlanList.size() <= 0) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        List<Map<String, Object>> entityList = new ArrayList<Map<String,Object>>(workPlanList.size());
        Map<String, Object> typeJson = null;
        for (TrainWorkPlan workPlan : workPlanList) {
            typeJson = new HashMap<String, Object>();
            typeJson.put("idx", workPlan.getIdx());                                 // 机车检修作业计划主键
            typeJson.put("trainTypeShortName", workPlan.getTrainTypeShortName());   // 车型名称
            typeJson.put("trainNo", workPlan.getTrainNo());                        // 车号
            typeJson.put("repairClassRepairTime", workPlan.getRepairClassName()+ workPlan.getRepairtimeName());            // 修程修次
            typeJson.put("tphUndoCount", this.queryFaultTicketCountNew(jsonObject, workPlan.getIdx(),1));            // 提票活 未处理
            typeJson.put("tphDoneCount", this.queryFaultTicketCountNew(jsonObject, workPlan.getIdx(),2));            // 提票活 已处理
            entityList.add(typeJson);
        }
        return JSONTools.toJSONList(workPlanList.size(), entityList);
    }
    
    /**
     * <li>说明：启动节点
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 提示信息
     * @throws Exception 
     */
    public String startNode(String jsonObject) throws Exception {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
         JSONObject jo = JSONObject.parseObject(jsonObject);
         Long operatorId = jo.getLong(Constants.OPERATOR_ID);
         if (null == operatorId) {
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_ERROR_ARGS_NULL_OPERATOR_ID));
        }
         OperateReturnMessage msg = new OperateReturnMessage();
         try {
             // 设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorId);
            String idx = jo.getString("idx");         
            String validateMsg = jobProcessNodeManager.startNode(idx); 
            if (null != validateMsg) {
                msg.setSucessFlag(validateMsg);
            } 
         } catch (Exception e) {
             ExceptionUtil.process(e, logger);
             msg.setFaildFlag(e.getMessage());
         }
         return JSONObject.toJSONString(msg); 
    }
   
    /**
     * <li>说明：机车检修记录单分页查询
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEnityJson json对象 
     * @return 结果集合JSON字符串
     * @throws IOException 
     */
    public String queryWorkCardList(String searchEnityJson) throws IOException{
        if(StringUtil.isNullOrBlank(searchEnityJson)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        JSONObject searchjo = JSONObject.parseObject(searchEnityJson);
        // 查询记录开始索引
        int start = searchjo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = searchjo.getIntValue("limit");
        start = limit * (start-1);
        // 排序字段
        JSONArray jArray = searchjo.getJSONArray(Constants.ORDERS);
        List<Order> orderList = new ArrayList<Order>();
        if (null != jArray) {
        Order[] orders = JSONTools.getOrders(jArray);
            for (Order order : orders) {
                orderList.add(order);
            }
        }
        // 获取查询条件实体对象
        String entityJson = searchjo.getString(Constants.ENTITY_JSON);
        WorkCard entity = JSONUtil.read(entityJson, WorkCard.class);
        //  查询条件
        List<Condition> whereList = new ArrayList<Condition>();
        Condition con = null;
        if(!StringUtil.isNullOrBlank(entity.getStatus())){
            // 未处理的检修记录卡    
            if ("1".equals(entity.getStatus())) {
                String[] statusArray = {WorkCard.STATUS_HANDLING, WorkCard.STATUS_NEW, WorkCard.STATUS_OPEN};
                con = new Condition("status", Condition.IN, statusArray);
            // 已处理的检修记录卡
            } else if ("2".equals(entity.getStatus())) {
                String[] statusArray = {WorkCard.STATUS_FINISHED, WorkCard.STATUS_HANDLED};
                con = new Condition("status", Condition.IN, statusArray);
            }     
            whereList.add(con);
        }
        if(!StringUtil.isNullOrBlank(entity.getRdpIDX())){
            con = new Condition("rdpIDX", Condition.EQ, entity.getRdpIDX());
            con.setStringLike(false);
            whereList.add(con);
        }
        if (!StringUtil.isNullOrBlank(entity.getNodeCaseIDX())) {
            con = new Condition("nodeCaseIDX", Condition.EQ, entity.getNodeCaseIDX());
            con.setStringLike(false);
            whereList.add(con);
        } 
        QueryCriteria<WorkCard> query = new QueryCriteria<WorkCard>(WorkCard.class, whereList, orderList, start, limit);
        Page<WorkCard> page = this.workCardManager.findPageList(query);
        
        List<WorkCard> list = page.getList();
        if (null == list || list.size() <= 0){
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        for(WorkCard t : list){
            t.setStatus(WorkCard.getStatusMeaning(t.getStatus()));
        }
        return JSONTools.toJSONList(page.getTotal(), list);
    } 
    
    
    /**
     * <li>说明：机车检修记录单分页查询（全部）
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-1-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEnityJson
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public String queryWorkCardListAll(String searchEnityJson) throws IOException{
        if(StringUtil.isNullOrBlank(searchEnityJson)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        JSONObject searchjo = JSONObject.parseObject(searchEnityJson);
        // 查询记录开始索引
        int start = searchjo.getIntValue(Constants.START);
        // 查询记录条数
        int limit = searchjo.getIntValue("limit");
        start = limit * (start-1);
        // 排序字段
        JSONArray jArray = searchjo.getJSONArray(Constants.ORDERS);
        List<Order> orderList = new ArrayList<Order>();
        if (null != jArray) {
        Order[] orders = JSONTools.getOrders(jArray);
            for (Order order : orders) {
                orderList.add(order);
            }
        }
        // 获取查询条件实体对象
        String entityJson = searchjo.getString(Constants.ENTITY_JSON);
        WorkCard entity = JSONUtil.read(entityJson, WorkCard.class);
        //  查询条件
        List<Condition> whereList = new ArrayList<Condition>();
        Condition con = null;
        if(!StringUtil.isNullOrBlank(entity.getStatus())){
            // 未处理的检修记录卡    
            if ("1".equals(entity.getStatus())) {
                String[] statusArray = {WorkCard.STATUS_HANDLING, WorkCard.STATUS_NEW, WorkCard.STATUS_OPEN};
                con = new Condition("status", Condition.IN, statusArray);
            // 已处理的检修记录卡
            } else if ("2".equals(entity.getStatus())) {
                String[] statusArray = {WorkCard.STATUS_FINISHED, WorkCard.STATUS_HANDLED};
                con = new Condition("status", Condition.IN, statusArray);
            }     
            whereList.add(con);
        }
        if(!StringUtil.isNullOrBlank(entity.getRdpIDX())){
            con = new Condition("rdpIDX", Condition.EQ, entity.getRdpIDX());
            con.setStringLike(false);
            whereList.add(con);
        }
        if (!StringUtil.isNullOrBlank(entity.getNodeCaseIDX())) {
            con = new Condition("nodeCaseIDX", Condition.EQ, entity.getNodeCaseIDX());
            con.setStringLike(false);
            whereList.add(con);
        } 
        QueryCriteria<WorkCard> query = new QueryCriteria<WorkCard>(WorkCard.class, whereList, orderList, start, limit);
        Page<WorkCard> page = this.workCardManager.findPageList(query);
        
        List<WorkCard> list = page.getList();
        if (null == list || list.size() <= 0){
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        for(WorkCard t : list){
            t.setStatus(WorkCard.getStatusMeaning(t.getStatus()));
            Page page2 = workCardManager.getWorkTaskAndDetectResultList(null, t.getIdx(), null, 1, 9999);
            List<WorkTaskBean> workTaskBeanlist = page2.getList();
            t.setWorkTaskBeanlist(workTaskBeanlist);
        }
        return JSONTools.toJSONList(page.getTotal(), list);
    } 
    

    /**
     * <li>说明：机车检修记录单数量查询
     * <li>创建人：张迪
     * <li>创建日期：2016-6-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityJson json对象 
     * @return 机车检修记录单数量
     * @throws IOException
     */
    public String queryWorkCardCount(String entityJson) throws IOException{
        JSONObject searchjo = JSONObject.parseObject(entityJson);
        // 获取查询条件实体对象
//        String entityJson = searchjo.getString(Constants.ENTITY_JSON);
        WorkCard entity = JSONUtil.read(searchjo.toString(), WorkCard.class);
        //  查询条件
        List<Condition> whereList = new ArrayList<Condition>();     
        Condition con = null;
        
        if(!StringUtil.isNullOrBlank(entity.getRdpIDX())){
            con = new Condition("rdpIDX", Condition.EQ, entity.getRdpIDX());
            con.setStringLike(false);
            whereList.add(con);
        }
        if (!StringUtil.isNullOrBlank(entity.getNodeCaseIDX())) {
            con = new Condition("nodeCaseIDX", Condition.EQ, entity.getNodeCaseIDX());
            con.setStringLike(false);
            whereList.add(con);
        } 

        // 未处理的检修记录卡数量
        String[] statusArray1 = {WorkCard.STATUS_HANDLING, WorkCard.STATUS_NEW, WorkCard.STATUS_OPEN};
        con = new Condition("status", Condition.IN, statusArray1);
        whereList.add(con);
        QueryCriteria<WorkCard> query = new QueryCriteria<WorkCard>(WorkCard.class, whereList, null, 1, 1);        
       int count1 = this.workCardManager.findPageList(query).getTotal();

       // 已处理的检修记录卡数量
        String[] statusArray2 = {WorkCard.STATUS_FINISHED, WorkCard.STATUS_HANDLED}; 
        whereList.remove(con);
        con = new Condition("status", Condition.IN, statusArray2);    
        whereList.add(con);
        query = new QueryCriteria<WorkCard>(WorkCard.class, whereList, null, 1, 1);  
        int count2 = this.workCardManager.findPageList(query).getTotal();
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count1", String.valueOf(count1));
        map.put("count2", String.valueOf(count2));
        Object cou = JSONObject.toJSON(map);
        return cou.toString();            
   }
    
    /**
     * <li>说明：保存工序延误信息
     * <li>创建人：张迪
     * <li>创建日期：2016-6-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象 
     * @return 返回提示信息
     */
    public String saveWorkSeqDelay(String jsonObject){
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
        NodeCaseDelay entity = new NodeCaseDelay();
        JobProcessNode node = jobProcessNodeQueryManager.getModelById(jo.getString("nodeCaseIdx"));
       
        if(node == null){
            throw new BusinessException("机车检修作业节点为空"); 
        }
        if(null == jo.getString("idx")||"".equals(jo.getString("idx"))){
            entity.setCreateTime(new Date());
            entity.setCreator((operatorId));
        }else{
            entity.setIdx(jo.getString("idx"));    
        }  
        entity.setUpdateTime(new Date());
        entity.setUpdator(operatorId);
        entity.setDelayReason(jo.getString("delayReason"));
//        entity.setDelayTime(delayTime);
        entity.setDelayType(jo.getString("delayType"));
        entity.setNodeCaseIdx(jo.getString("nodeCaseIdx"));
        entity.setNodeIDX(node.getNodeIDX());
        entity.setPlanBeginTime(node.getPlanBeginTime());
        entity.setPlanEndTime(node.getPlanEndTime());
        entity.setRdpIDX(node.getWorkPlanIDX());
        entity.setRealBeginTime(node.getRealBeginTime());
        entity.setRecordStatus(0);
        entity.setSiteID(JXSystemProperties.SYN_SITEID);
        OperateReturnMessage msg = new OperateReturnMessage();
        try {
            nodeCaseDelayManager.save(entity);
            nodeCaseDelayManager.sendDelayNotify(jo.getString("nodeCaseIdx")); //发送消息延误
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONObject.toJSONString(msg);   
    }
    
    /**
     * <li>说明：根据在修机车，查询当前班组提票活
     * <li>创建人：张迪
     * <li>创建日期：2016-8-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 封装实体
     * @return 提票活列表
     */
    public String queryFaultTicket(String jsonObject) {
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
        FaultTicket bean = new FaultTicket();
        bean.setStatus(jo.getInteger(("status")));
        bean.setWorkPlanIDX(jo.getString("workPlanIDX"));
        bean.setType(jo.getString("type"));  
        if(jo.getBoolean("isTeam")){
            OmEmployee omEmployee = omEmployeeManager.findOmEmployee(operatorId);
            bean.setRepairTeam(omEmployee.getOrgid().toString());
        }
       // 客货类型
       String vehicleType = jo.getString("vehicleType");
       bean.setVehicleType(vehicleType);
       List<FaultTicket> list = faultTicketManager.queryFaultTicketList(bean);
        try {
            return JSONTools.toJSONList(list);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(e.getMessage()));
        }
    }
    
    /**
     * <li>说明：获取提票活数量
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @param workPlanIDX 检修任务单
     * @param status 状态1：未处理 2：已处理
     * @return
     */
    public String queryFaultTicketCountNew(String jsonObject,String workPlanIDX,Integer status) {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            return 0 + "" ;
        }
         JSONObject jo = JSONObject.parseObject(jsonObject);
         Long operatorId = jo.getLong(Constants.OPERATOR_ID);
         if (null == operatorId) {
             return 0 + "" ;
        }
         FaultTicket bean = new FaultTicket();
         bean.setStatus(status);
         bean.setWorkPlanIDX(workPlanIDX);
         
         // 客货类型
         String vehicleType = jo.getString("vehicleType");
         bean.setVehicleType(vehicleType);
         List<FaultTicket> list = faultTicketManager.queryFaultTicketList(bean);
        int count = list == null ? 0 : list.size();
        return String.valueOf(count);
    }
    
  
}
