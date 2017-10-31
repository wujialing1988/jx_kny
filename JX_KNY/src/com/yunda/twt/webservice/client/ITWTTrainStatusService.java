package com.yunda.twt.webservice.client;

import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车状态管理客户端，此接口用于机车状态信息更改，及机车状态超时预警通知
 * <li>创建人：程锐
 * <li>创建日期：2015-2-6
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface ITWTTrainStatusService {
    
    /**
     * <li>说明：当机车状态变更时，web端调用此方法通知台位图服务端机车状态变更
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 机车信息JSON字符串
     * @param status 机车状态
     */
    public void setTrainStatus(String trainInfo, String status);
    
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
    public void setTrainStatusOverTime(String overTime, String trainTypeShortName, String trainNo);
    
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
    public void giveAnAlarm(TrainAccessAccount account, String workType);
    
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
    public void startMissionWarning(TrainAccessAccount account, String workType, String operatorIds);
    
    /**
     * <li>说明：当与机车有关的任务发生时，台位图上进行提醒，提醒无过期限制并伴随声音提示，调用此方法停止提醒。
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param account 机车出入段实体对象
     */
    public void stopMissionWarning(TrainAccessAccount account);
    
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
    public void updateTrainStatus(TrainAccessAccount account, String status);
}
