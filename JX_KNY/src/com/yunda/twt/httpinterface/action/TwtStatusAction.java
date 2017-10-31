package com.yunda.twt.httpinterface.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.twt.httpinterface.manager.TwtStatusManager;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 台位图-机车状态控制器类
 * <li>创建人：程锐
 * <li>创建日期：2015-12-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
public class TwtStatusAction extends JXBaseAction<Object, Object, TwtStatusManager>{
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    protected String trainInfo;
    
    protected String status;
    
    /**
     * <li>说明：在没有股道自动化时，如果手工将机车拖放到台位图上时，台位图通过此服务获取机车当前状态
     * <li>创建人：程锐
     * <li>创建日期：2015-12-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void getTrainStatus() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String trainStatus = this.manager.getTrainStatus(trainInfo);
            JSONUtil.write(this.getResponse(), trainStatus);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        }
    }
    
    /**
     * <li>说明：台位图更改机车状态
     * <li>创建人：程锐
     * <li>创建日期：2015-12-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void updateTrainStatus() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.manager.updateTrainStatus(trainInfo, status);
            map.put(Constants.SUCCESS, true);
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
            JSONUtil.write(this.getResponse(), map);
        }
        JSONUtil.write(this.getResponse(), map);
    }
    
}
