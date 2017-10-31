package com.yunda.twt.webservice.client;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.xfire.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.Application;
import com.yunda.common.BusinessException;
import com.yunda.frame.util.DateUtil;
import com.yunda.frame.util.ExceptionUtil;
import com.yunda.frame.util.JSONUtil;
import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;
import com.yunda.twt.trainaccessaccount.webservice.TrainAccessAccountParams;
import com.yunda.twt.webservice.util.ITWTUtil;
import com.yunda.zb.common.ZbConstants;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 台位图移动机车业务类
 * <li>创建人：程锐
 * <li>创建日期：2015-5-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
@Service(value = "moveTrainService")
public class MoveTrainService implements IMoveTrainService {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 台位图通用业务类 */
    @Autowired
    private ITWTUtil twtUtil;
    
    /** 台位图客户端服务名 */
    private static final String TWT_SERVICE_NAME = "MoveTrainService";
    
    /**
     * <li>说明：移动机车
     * <li>创建人：程锐
     * <li>创建日期：2015-5-15
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 机车出入段台账对象
     * @param positionGuid 台位idx
     */
    public void moveTrainStation(final TrainAccessAccount account, final String positionGuid) {
        new Thread() {
            
            public void run() {
                try {
                    if (twtUtil == null) {
                        twtUtil = (ITWTUtil) Application.getSpringApplicationContext().getBean(ZbConstants.TWTUTIL_SERVICENAME);
                    }
                    if (account == null)
                        throw new BusinessException("未找到对应入段机车");
                    Client client = twtUtil.getDefaultClient(TWT_SERVICE_NAME, account.getSiteID());
//                    String trainInfo = account.getTrainAliasName();
//                    if (StringUtil.isNullOrBlank(account.getTrainAliasName()))
//                        trainInfo = account.getTrainTypeShortName().concat(account.getTrainNo());
                    if (client != null) {
                        client.invoke("MoveTrain", new Object[] { positionGuid, buildTrainInfo(account) });
                    }
                } catch (Exception e) {
                    ExceptionUtil.process(e, logger);
                }
            }
        }.start();
    }
    
    /**
     * <li>说明：构造机车信息JSON字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 机车出入段实体对象
     * @return 机车信息JSON字符串
     * @throws IOException
     */
    private String buildTrainInfo(TrainAccessAccount account) throws IOException {
        TrainAccessAccountParams params = new TrainAccessAccountParams();
        params.setEquipClass(account.getEquipClass());
        params.setSiteID(account.getSiteID());
        params.setEquipNo(account.getEquipNo());
        params.setEquipName(account.getEquipName());
        params.setOnEquipTime(account.getOnEquipTime() != null ? DateUtil.yyyy_MM_dd_HH_mm_ss.format(account.getOnEquipTime()) : "");
        params.setTrainAliasName(account.getTrainAliasName());
        params.setTrainGUID(account.getTrainGUID());
        params.setEquipGUID(account.getEquipGUID());
        params.setEquipOrder(account.getEquipOrder());
        params.setTrainTypeShortName(account.getTrainTypeShortName());
        params.setTrainNo(account.getTrainNo());
        return JSONUtil.write(params);
    }
}
