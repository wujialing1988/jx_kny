package com.yunda.twt.httpinterface.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.frame.util.StringUtil;
import com.yunda.frame.yhgl.entity.AcOperator;
import com.yunda.twt.httpinterface.manager.TwtLoginManager;
import com.yunda.webservice.employee.entity.AcFunctionBean;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 台位图登陆控制器类
 * <li>创建人：程锐
 * <li>创建日期：2015-12-8
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
public class TwtLoginAction extends JXBaseAction<Object, Object, TwtLoginManager> {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    protected String userid;
    
    protected String appCode;
    
    protected String operatorId;

    protected String jsonObject;
    
    public String getJsonObject() {
        return jsonObject;
    }
    
    public void setJsonObject(String jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getAppCode() {
        return appCode;
    }
    
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
    
    public String getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    
    public String getUserid() {
        return userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * <li>说明：台位图登陆
     * <li>创建人：程锐
     * <li>创建日期：2015-12-8
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void loginByUserid() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            AcOperator operator = this.manager.loginByUserid(userid);
            if (operator == null) {
                JSONUtil.write(this.getResponse(), "null");
            } else {
                JSONUtil.write(this.getResponse(), operator);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：根据应用功能编码获取此应用功能下的所有权限列表
     * <li>创建人：程锐
     * <li>创建日期：2015-12-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getRoleFuncByAppCode() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<AcFunctionBean> beanList = this.manager.getRoleFuncByAppCode(appCode, operatorId);
            JSONUtil.write(this.getResponse(), beanList);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：通过右键台位图，判断当前车是否属于检修 整备 通用
     * <li>创建人：林欢
     * <li>创建日期：2016-4-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getTrainStateByMessage() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            
            if (StringUtil.isNullOrBlank(jsonObject)) {
                map.put("errMsg", "右键台位图，传递数据为空!");
                JSONUtil.write(this.getResponse(), map);
            }else{
                JSONObject ob = JSONObject.parseObject(jsonObject);
//              获取数据
                String trainTypeShortName = ob.getString("trainTypeShortName");
                map = this.manager.getTrainStateByMessage(trainTypeShortName);
                JSONUtil.write(this.getResponse(), map);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        }
    }
}
