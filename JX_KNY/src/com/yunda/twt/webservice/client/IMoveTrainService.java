package com.yunda.twt.webservice.client;

import com.yunda.twt.trainaccessaccount.entity.TrainAccessAccount;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 台位图移动机车接口
 * <li>创建人：程锐
 * <li>创建日期：2015-5-15
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
public interface IMoveTrainService {
    
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
    public void moveTrainStation(final TrainAccessAccount account, final String positionGuid);
}
