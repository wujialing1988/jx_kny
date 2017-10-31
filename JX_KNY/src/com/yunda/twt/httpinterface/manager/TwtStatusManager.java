package com.yunda.twt.httpinterface.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.frame.common.JXBaseManager;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
import com.yunda.twt.webservice.client.ITWTTrainStatusService;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 台位图-机车状态业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-12-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 3.2.3
 */
@Service(value = "twtStatusManager")
public class TwtStatusManager extends JXBaseManager<Object, Object> {
    
    /** 机车出入段台账查询业务类 */
    @Autowired
    private TrainAccessAccountQueryManager trainAccessAccountQueryManager;
    
    /** 机车状态管理客户端业务类 */
    @Autowired
    private ITWTTrainStatusService tWTTrainStatusService;
    
    /**
     * <li>说明：在没有股道自动化时，如果手工将机车拖放到台位图上时，台位图通过此服务获取机车当前状态
     * <li>创建人：程锐
     * <li>创建日期：2015-12-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 机车信息JSON字符串
     * @return 机车状态
     * @throws Exception
     */
    public String getTrainStatus(String trainInfo) throws Exception {
        String trainStatus = "";
        TrainAccessAccount account = trainAccessAccountQueryManager.getAccountByTrainInfo(trainInfo);
        if (account != null)
            trainStatus = account.getTrainStatus();
        return trainStatus;
    }
    
    /**
     * <li>说明：更改机车状态
     * <li>创建人：程锐
     * <li>创建日期：2015-12-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 机车信息JSON字符串
     * @param status 机车状态
     * @throws Exception
     */
    public void updateTrainStatus(String trainInfo, String status) throws Exception {
        tWTTrainStatusService.setTrainStatus(trainInfo, status);
    }
}
