package com.yunda.webservice.test.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunda.webservice.test.IKnyTest;
import com.yunda.zb.rdp.zbtaskbill.entity.ZbglRdpWidiVo;
import com.yunda.zb.rdp.zbtaskbill.manager.ZbglRdpWidiManager;


/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 测试接口
 * <li>创建人：伍佳灵
 * <li>创建日期：2017-4-6
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
@Service("knyTestWS")
public class KnyTestImpl implements IKnyTest {
    
    @Resource
    private ZbglRdpWidiManager zbglRdpWidiManager ;
    
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
    public void findZbglRdpWidisByRdpIdx(String rdpIdx) throws Exception {
        List<ZbglRdpWidiVo> list = zbglRdpWidiManager.findZbglRdpWidisByRdpIdx(rdpIdx);
        System.err.println(list.size());
    };
    
}
