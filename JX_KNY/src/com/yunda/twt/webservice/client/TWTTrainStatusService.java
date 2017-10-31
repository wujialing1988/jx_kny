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
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountManager;
import com.yunda.twt.trainaccessaccount.manager.TrainAccessAccountQueryManager;
import com.yunda.twt.trainaccessaccount.webservice.TrainAccessAccountParams;
import com.yunda.twt.webservice.util.ITWTUtil;
import com.yunda.zb.common.ZbConstants;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 此接口用于机车状态信息更改，及机车状态超时预警通知
 * <li>创建人：程锐
 * <li>创建日期：2015-2-6
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
@Service(value = "twtTrainStatusService")
public class TWTTrainStatusService implements ITWTTrainStatusService {
    
    /** 日志工具 */
    private Logger logger = Logger.getLogger(getClass().getName());
    
    /** 台位图通用业务类 */
    @Autowired
    private ITWTUtil twtUtil;
    
    /** 机车出入段台账查询业务类 */
    @Autowired
    private TrainAccessAccountQueryManager trainAccessAccountQueryManager;
    
    /** 机车出入段台账业务类 */
    @Autowired
    private TrainAccessAccountManager trainAccessAccountManager;
    
    /** 台位图客户端服务名 */
    private static final String TWT_SERVICE_NAME = "TWTTrainStatusService";
    
    /**
     * <li>说明：当机车状态变更时，web端调用此方法通知台位图服务端机车状态变更
     * <li>创建人：程锐
     * <li>创建日期：2015-3-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 机车出入段台账对象
     * @param status 机车状态
     */
    public void updateTrainStatus(final TrainAccessAccount account, final String status) {
        new Thread() {            
            public void run() {
                try {
                    if (twtUtil == null) {
                        twtUtil = (ITWTUtil) Application.getSpringApplicationContext().getBean(ZbConstants.TWTUTIL_SERVICENAME);
                    }
                    if (account == null)
                        throw new BusinessException("未找到对应入段机车");
                    Client client = twtUtil.getDefaultClient(TWT_SERVICE_NAME, account.getSiteID());
                    if(client == null){
                    	 throw new BusinessException("连接webservice服务器失败，服务名："+TWT_SERVICE_NAME);
                    }
                    if (client != null) {
                        client.invoke("SetTrainStatus", new Object[] { buildTrainInfo(account), status });
                        if (!status.equals(account.getTrainStatus())) {
                            account.setTrainStatus(status);
                            trainAccessAccountManager.saveOrUpdate(account);
                        }                        
                    }
                } catch (Exception e) {
                    ExceptionUtil.process(e, logger);
                }
            }
        }.start();
    }
    
    /**
     * <li>说明：台位图服务端手工修改机车状态并调用服务通知web服务子系统后，web端调用此方法通知台位图服务端机车状态变更
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 机车信息JSON字符串
     * @param status 机车状态
     */
    public void setTrainStatus(final String trainInfo, final String status) {
        new Thread() {            
            public void run() {
                try {
                    if (twtUtil == null) {
                        twtUtil = (ITWTUtil) Application.getSpringApplicationContext().getBean(ZbConstants.TWTUTIL_SERVICENAME);
                    }
                    TrainAccessAccount account = trainAccessAccountQueryManager.getAccountByTrainInfo(trainInfo);
                    if (account == null)
                        throw new BusinessException("未找到对应入段机车");
                    Client client = twtUtil.getDefaultClient(TWT_SERVICE_NAME, account.getSiteID());
                    if(client == null){
                   	  throw new BusinessException("连接webservice服务器失败，服务名："+TWT_SERVICE_NAME);
                    }
                    if (client != null) {
                        client.invoke("SetTrainStatus", new Object[] { trainInfo, status });
                        if (!status.equals(account.getTrainStatus())) {
                            account.setTrainStatus(status);
                            trainAccessAccountManager.saveOrUpdate(account);
                        }                        
                    }
                } catch (Exception e) {
                    ExceptionUtil.process(e, logger);
                }
            }
        }.start();
    }
    
    /**
     * <li>说明：web端中机车状态超时预警时长修改后需要通知台位图服务端机车超时预警时长
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param overTime 超时时长（秒）
     * @param trainTypeShortName 车型
     * @param trainNo 车号
     */
    public void setTrainStatusOverTime(final String overTime, final String trainTypeShortName, final String trainNo) {
        new Thread() {            
            public void run() {
                try {
                    Client client = getClient(trainTypeShortName, trainNo);
                    if (client != null) {
                        client.invoke("SetTrainStatusOverTime", new Object[] { overTime });
                    }
                } catch (Exception e) {
                    ExceptionUtil.process(e, logger);
                }
            }
        }.start();
    }
    
    /**
     * <li>说明：当机车超时需要预警时，web端调用台位图服务端，通知台位图服务机车超时预警
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 机车出入段实体对象
     * @param workType 台位图上显示预警信息,例如：“车内”
     */
    public void giveAnAlarm(final TrainAccessAccount account, final String workType) {
        new Thread() {            
            public void run() {
                try {
                    Client client = getClient(account);
                    if (client != null) {
                        client.invoke("GiveAnAlarm", new Object[] { buildTrainInfo(account), workType });
                    }
                } catch (Exception e) {
                    ExceptionUtil.process(e, logger);
                }
            }
        }.start();
    }
    
    /**
     * <li>说明：当与机车有关的任务发生时，通过此方法在台位图上进行提醒，提醒无过期限制并伴随声音提示。
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 机车出入段实体对象
     * @param workType 台位图上显示预警信息,例如：“车内”
     * @param operatorIds 逗号隔开的登陆人ID ，例如“5507851，5502505”
     */
    public void startMissionWarning(final TrainAccessAccount account, final String workType, final String operatorIds) {
        new Thread() {            
            public void run() {
                try {
                    Client client = getClient(account);
                    if (client != null) {
                        client.invoke("startMissionWarning", new Object[] { buildTrainInfo(account), workType, operatorIds });
                    }
                } catch (Exception e) {
                    ExceptionUtil.process(e, logger);
                }
            }
        }.start();
    }
    
    /**
     * <li>说明：当与机车有关的任务发生时，台位图上进行提醒，提醒无过期限制并伴随声音提示，调用此方法停止提醒。
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 机车出入段实体对象
     */
    public void stopMissionWarning(final TrainAccessAccount account) {
        new Thread() {            
            public void run() {
                try {
                    Client client = getClient(account);
                    if (client != null) {
                        client.invoke("stopMissionWarning", new Object[] { buildTrainInfo(account) });
                    }
                } catch (Exception e) {
                    ExceptionUtil.process(e, logger);
                }
            }
        }.start();
    }
    
    /**
     * <li>说明：根据机车信息JSON字符串获取台位图客户端对象
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 机车出入段实体对象
     * @return 台位图客户端对象
     * @throws Exception
     */
    private Client getClient(TrainAccessAccount account) throws Exception {
        if (twtUtil == null) {
            twtUtil = (ITWTUtil) Application.getSpringApplicationContext().getBean(ZbConstants.TWTUTIL_SERVICENAME);
        }
        return twtUtil.getDefaultClient(TWT_SERVICE_NAME, account.getSiteID());
    }
    
    /**
     * <li>说明：根据车型车号获取台位图客户端对象
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 车型名称
     * @param trainNo 车号
     * @return 台位图客户端对象
     * @throws Exception
     */
    private Client getClient(String trainTypeShortName, String trainNo) throws Exception {
        if (twtUtil == null) {
            twtUtil = (ITWTUtil) Application.getSpringApplicationContext().getBean(ZbConstants.TWTUTIL_SERVICENAME);
        }
        return twtUtil.getClient(TWT_SERVICE_NAME, trainTypeShortName, trainNo);
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
