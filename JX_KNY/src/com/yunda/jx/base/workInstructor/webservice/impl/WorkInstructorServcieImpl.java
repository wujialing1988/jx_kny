package com.yunda.jx.base.workInstructor.webservice.impl;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.jx.base.workInstructor.entity.WorkInstructorBean;
import com.yunda.jx.base.workInstructor.manager.WorkInstructorManager;
import com.yunda.jx.base.workInstructor.webservice.IWorkInstructorServcie;
import com.yunda.webservice.common.WsConstants;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 作业指导书接口实现
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("workInstructorServcieWS")
public class WorkInstructorServcieImpl implements IWorkInstructorServcie {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass());
    
    /**
     * 作业指导书业务类
     */
    @Resource
    private WorkInstructorManager workInstructorManager ;
    
    /**
     * <li>说明：查询作业指导书
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-5-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     * @throws IOException
     */
    public String findWorkInstructors() throws IOException {
        try {
            WorkInstructorBean obj = workInstructorManager.findWorkInstructors();
            return JSONObject.toJSONString(obj);
        }catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
    
}
