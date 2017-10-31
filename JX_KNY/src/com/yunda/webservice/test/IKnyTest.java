package com.yunda.webservice.test;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 功能测试接口
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-4-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IKnyTest {
    
    /**
     * <li>说明：查询车辆列检详情
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-4-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpIdx
     * @throws Exception
     */
    public void findZbglRdpWidisByRdpIdx(String rdpIdx) throws Exception ;
    
}
