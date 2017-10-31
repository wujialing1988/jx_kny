package com.yunda.zb.pczz.webservice;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.common.BusinessException;
import com.yunda.frame.common.Page;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.pjjx.util.SystemContextUtil;
import com.yunda.webservice.common.WsConstants;
import com.yunda.webservice.common.entity.OperateReturnMessage;
import com.yunda.zb.common.webservice.ITerminalCommonService;
import com.yunda.zb.pczz.manager.ZbglPczzWIManager;
import com.yunda.zb.pczz.manager.ZbglPczzWiItemManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 普查整治处理webservice实现类
 * <li>创建人：王利成
 * <li>创建日期：2015-3-8
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "zbglPczzService")
public class ZbglPczzService implements IZbglPczzService {
    /**
     * 日志对象
     */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    
    /**
     * ZbglPczzWI业务类,普查整治任务单
     */
    @Autowired
    private ZbglPczzWIManager zbglPczzWIManager;
    
    /**
     * ZbglPczzWI业务类,普查整治任务单
     */
    @Autowired
    private ZbglPczzWiItemManager zbglPczzWiItemManager;
    
    /**
     * 工位终端共有接口
     */
    @Autowired
    private ITerminalCommonService terminalCommonService;
    
    

    /**
     * <li>说明：查询普查整治单
     * <li>创建人：林欢
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonData 查询实体
     * @return String
     */
    public String findZbglPczzWiPageList(String jsonData) {
        try {
            
            JSONObject ob = JSONObject.parseObject(jsonData);
            
            //获取数据
            Long operatorid = Long.valueOf(ob.getString("operatorid"));//操作员idx
            Integer start = Integer.valueOf(ob.getString("start"));//起始页
            Integer limit = Integer.valueOf(ob.getString("limit"));//每页数
            String trainTypeShortName = ob.getString("trainTypeShortName");//车型
            String trainNo = ob.getString("trainNo");//车号
            if (trainTypeShortName == null) {
                trainTypeShortName = " ";
            }
            if (trainNo == null) {
                trainNo = " ";
            }
            //封装查询参数
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("trainTypeShortName", trainTypeShortName);
            paramsMap.put("trainNo", trainNo);
            start--;
            //赋值操作人员
            terminalCommonService.setAcOperatorById(operatorid);
            Page<ZbglPczzWiBean> page = zbglPczzWIManager.findZbglPczzPageList(paramsMap, operatorid, start * limit, limit);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    /**
     * <li>说明：查看单条普查整治任务项
     * <li>创建人：林欢
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonData 查询实体
     * @return String
     */
    public String findZbglPczzWiItemPageList(String jsonData) {
        try {
            
            JSONObject ob = JSONObject.parseObject(jsonData);
            
            //获取数据
            Long operatorid = Long.valueOf(ob.getString("operatorid"));//操作员idx
            Integer start = Integer.valueOf(ob.getString("start"));//起始页
            Integer limit = Integer.valueOf(ob.getString("limit"));//每页数
            String zbglPczzWiIDX = ob.getString("zbglPczzWiIDX");//普查整治任务单idx
            
            //封装查询参数
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("zbglPczzWiIDX", zbglPczzWiIDX);
            
            start--;
            //赋值操作人员
            terminalCommonService.setAcOperatorById(operatorid);
            Page<ZbglPczzWiItemBean> page = zbglPczzWiItemManager.findZbglPczzWiItemPageList(paramsMap, start * limit, limit);
            return JSONUtil.write(page.extjsStore());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }

    /**
     * <li>说明：普查整治任务项完工/普查整治任务项检查完毕
     * <li>创建人：林欢
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonData 查询实体
     * @return String
     */
    public String finishOrCheckedZbglPczzWiItem(String jsonData) {
        OperateReturnMessage message = new OperateReturnMessage();
        try {
            
            JSONObject ob = JSONObject.parseObject(jsonData);
            
            //获取数据
            Long operatorid = Long.valueOf(ob.getString("operatorid"));//操作员idx
            String zbglPczzWiItemIDX = ob.getString("zbglPczzWiItemIDX");//普查整治任务项
            String operatStatus = ob.getString("operatStatus");//操作动作（finished == 普查整治任务项完工 checked == 普查整治任务项检查完毕）
            String itemResualt = ob.getString("itemResualt");//普查整治任务项完成结果
            
//          设置系统用户信息
            SystemContextUtil.setSystemInfoByOperatorId(operatorid);
            
//          封装查询参数
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("operatorid", operatorid);
            paramsMap.put("zbglPczzWiItemIDX", zbglPczzWiItemIDX);
            paramsMap.put("itemResualt", itemResualt);
            paramsMap.put("operatStatus", operatStatus);
            
            zbglPczzWiItemManager.finishOrCheckedZbglPczzWiItem(paramsMap);
            
        } catch (BusinessException e) {
            ExceptionUtil.process(e, logger);
            message.setFaildFlag(e.getMessage());
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            message.setFaildFlag(e.getMessage());
        } finally {
            return JSONObject.toJSONString(message);
        }
    }
}
