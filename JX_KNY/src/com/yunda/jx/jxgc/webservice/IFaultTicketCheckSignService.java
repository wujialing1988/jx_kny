package com.yunda.jx.jxgc.webservice;

import java.io.IOException;

import com.yunda.jx.pjjx.webservice.IService;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车提票检查签到接口定义
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-12-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IFaultTicketCheckSignService extends IService {
    
    /**
     * <li>说明：查询检查提票签到列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 
     * @return
     * @throws IOException
     */
    public String queryCheckSignList(String jsonObject) throws IOException; 
    
    /**
     * <li>说明：保存检查签到信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String saveCheckSign(String jsonData) throws IOException ;
    
    /**
     * <li>说明：保存开始检查记录
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String saveCheckStart(String jsonData) throws IOException ;
    
}
