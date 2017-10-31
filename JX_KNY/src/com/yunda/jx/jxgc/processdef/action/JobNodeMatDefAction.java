package com.yunda.jx.jxgc.processdef.action; 

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yunda.frame.common.Constants;
import com.yunda.frame.common.JXBaseAction;
import com.yunda.frame.common.JXBaseManager;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.jx.jxgc.processdef.entity.JobNodeMatDef;
import com.yunda.jx.jxgc.processdef.manager.JobNodeMatDefManager;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：张迪
 * <li>创建日期：2016-9-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@SuppressWarnings(value="serial")
public class JobNodeMatDefAction extends JXBaseAction<JobNodeMatDef, JobNodeMatDef, JobNodeMatDefManager>{
    /** 日志工具 */
    @SuppressWarnings("unused")
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /**
     * <li>说明：方法实现功能说明
     * <li>创建人：张迪
     * <li>创建日期：2016-9-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void saveNodeMats() throws Exception{
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            JobNodeMatDef[] list = (JobNodeMatDef[])JSONUtil.read(getRequest(), JobNodeMatDef[].class);
            String[] errMsg = this.manager.saveNodeMats(list);
            if (errMsg == null || errMsg.length < 1) {
                map.put(Constants.SUCCESS, true);
            }else{
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }       
    }

    /**
     * <li>说明：更新物料信息
     * <li>创建人：陈志刚
     * <li>创建日期：2016-9-9
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @throws Exception
     */
    public void Update() throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        try {
            JobNodeMatDef[] jnmd = (JobNodeMatDef[])JSONUtil.read(getRequest(), JobNodeMatDef[].class);
            String[] errMsg = this.manager.Update(jnmd);
            if (errMsg == null || errMsg.length < 1) {
                map.put(Constants.SUCCESS, true);
            }else{
                map.put(Constants.SUCCESS, false);
                map.put(Constants.ERRMSG, errMsg);
            }   
        } catch (Exception e) {
            ExceptionUtil.process(e, logger, map);
        } finally {
            JSONUtil.write(this.getResponse(), map);
        }   
    }
    
    
    
}