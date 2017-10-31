package com.yunda.jx.base.workInstructor.webservice;

import java.io.IOException;

import com.yunda.jx.pjjx.webservice.IService;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 作业指导书接口定义
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IWorkInstructorServcie extends IService {
    
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
    public String findWorkInstructors() throws IOException;
    
}
