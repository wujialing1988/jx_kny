package com.yunda.twt.webservice.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.frame.util.ExceptionUtil;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
import com.yunda.twt.webservice.client.ITWTTrainStatusService;
import com.yunda.webservice.common.WsConstants;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车状态管理服务端，此接口管理机车状态信息
 * <li>创建人：程锐
 * <li>创建日期：2015-2-6
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "trainStatusManageService")
public class TrainStatusManageService implements ITrainStatusManageService {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 机车出入段台账查询业务类 */
    @Autowired
    private TrainAccessAccountQueryManager trainAccessAccountQueryManager;
    
    /** 机车状态管理客户端业务类 */
    @Autowired
    private ITWTTrainStatusService tWTTrainStatusService;
    
    /**
     * <li>说明：在没有股道自动化时，如果手工将机车拖放到台位图上时，台位图通过此服务获取机车当前状态
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 机车信息JSON字符串
     * @return 机车状态中文名
     * CRIF  2015-05-24  汪东良 高    为什么会有此方法，台位图不是每隔几秒钟就会从web服务端读取一次机车列表信息吗？
     */
    public String getTrainStatus(String trainInfo) {
        String trainStatus = "";
        try {
            TrainAccessAccount account = trainAccessAccountQueryManager.getAccountByTrainInfo(trainInfo);
            if (account != null)
                trainStatus = account.getTrainStatus();
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
        } 
        return trainStatus;
    }
    
    /**
     * <li>说明：台位图服务端手工修改机车状态后，需要调用此服务通知web服务子系统，web服务子系统调用设置接车状态通知台位图服务端，由台位图服务端通知各个台位图客户端
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 机车信息JSON字符串
     * @param status 机车状态
     * @return 操作是否成功的字符串
     */
    public String updateTrainStatus(String trainInfo, String status) {
        try {
            tWTTrainStatusService.setTrainStatus(trainInfo, status);
            return WsConstants.OPERATE_SUCCESS;
        } catch (Exception e) {
            ExceptionUtil.process(e, logger);
            return WsConstants.OPERATE_FALSE;
        }
    }
}
