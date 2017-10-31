package com.yunda.jx.jxgc.webservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.yunda.Application;
import com.yunda.base.context.SystemContext;
import com.yunda.common.BusinessException;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.frame.yhgl.manager.AcOperatorManager;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckSign;
import com.yunda.jx.jxgc.tpmanage.entity.FaultTicketCheckStart;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketCheckSignManager;
import com.yunda.jx.jxgc.tpmanage.manager.FaultTicketCheckStartManager;
import com.yunda.webservice.common.entity.OperateReturnMessage;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车提票检查签到实现类
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-12-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service(value = "faultTicketCheckSignWS")
public class FaultTicketCheckSignService implements IFaultTicketCheckSignService {
    
    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 提票检查签到业务 **/
    @Resource
    private FaultTicketCheckSignManager faultTicketCheckSignManager ;
    
    /** 提票检查签到开始检查记录业务 **/
    @Resource
    private FaultTicketCheckStartManager faultTicketCheckStartManager ;
    
    /**
     * <li>说明：查询检查提票签到列表(实现)
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 
     * @return
     * @throws IOException
     */
    public String queryCheckSignList(String jsonObject) throws IOException {
        if(StringUtil.isNullOrBlank(jsonObject)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        OperateReturnMessage msg = new OperateReturnMessage();
        List<FaultTicketCheckSign> list = null ;
        FaultTicketCheckStart data = null ;
        try {
            JSONObject jo = JSONObject.parseObject(jsonObject);
            // 机车检修作业计划主键
            String workPlanIDX = jo.getString("workPlanIDX");
            // 提票类型
            String faultTicketType = jo.getString("faultTicketType");
            // 查询已签到人员列表
            list = faultTicketCheckSignManager.queryCheckSignList(workPlanIDX, faultTicketType);
            // 查询开始检查记录
            data = faultTicketCheckStartManager.getFaultTicketCheckStart(workPlanIDX, faultTicketType);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        if (null == list || list.size() <= 0){
            return JSONObject.toJSONString(OperateReturnMessage.newFailsInstance(MSG_RESULT_IS_EMPTY));
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", data);
        map.put("list", list);
        Object cou = JSONObject.toJSON(map);
        return cou.toString();
    }
    
    /**
     * <li>说明：保存检查签到信息(实现)
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String saveCheckSign(String jsonData) throws IOException {
        if(StringUtil.isNullOrBlank(jsonData)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        OperateReturnMessage msg = new OperateReturnMessage();
        FaultTicketCheckSign checkSign = JSONUtil.read(jsonData, FaultTicketCheckSign.class);
        if(null == checkSign){
            throw new BusinessException("检查签到数据为空！"); 
        }
        if(null == checkSign.getCheckSignEmpId()){
            throw new BusinessException("人员ID为空！"); 
        }
        AcOperator ac = getAcOperatorManager().getModelById(checkSign.getCheckSignEmpId());
        SystemContext.setAcOperator(ac);
        try {
            faultTicketCheckSignManager.saveCheckSign(checkSign);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONObject.toJSONString(msg);
    }

    /**
     * <li>说明：保存开始检查记录（实现）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String saveCheckStart(String jsonData) throws IOException {
        if(StringUtil.isNullOrBlank(jsonData)) {
            throw new NullPointerException(MSG_ERROR_ARGS_NULL);
        }
        OperateReturnMessage msg = new OperateReturnMessage();
        FaultTicketCheckStart checkStart = JSONUtil.read(jsonData, FaultTicketCheckStart.class);
        if(null == checkStart){
            throw new BusinessException("数据为空！"); 
        }
        if(null == checkStart.getCheckSignEmpId()){
            throw new BusinessException("人员ID为空！"); 
        }
        AcOperator ac = getAcOperatorManager().getModelById(checkStart.getCheckSignEmpId());
        SystemContext.setAcOperator(ac);
        try {
            faultTicketCheckStartManager.saveCheckStart(checkStart);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            msg.setFaildFlag(e.getMessage());
        }
        return JSONObject.toJSONString(msg);
    }
    
    protected AcOperatorManager getAcOperatorManager() {
        return (AcOperatorManager) Application.getSpringApplicationContext().getBean("acOperatorManager");
    }
    
}
