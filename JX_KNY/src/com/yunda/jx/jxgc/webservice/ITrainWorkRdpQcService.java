package com.yunda.jx.jxgc.webservice;

import java.io.IOException;

import com.yunda.jx.pjjx.webservice.IService;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 类的功能描述
 * <li>创建人：张迪
 * <li>创建日期：2016-8-18
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface ITrainWorkRdpQcService extends IService {
   
    /**
     * <li>说明：获取在修机车质量检查
     * <li>创建人：张迪
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json字符串
     * @return 在修机车质量检查列表
     */
    public String getTrainRdpQCList (String jsonObject);
    /**
     * <li>说明：获取检修记录单列表
     * <li>创建人：张迪
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json字符串
     * @return 检修记录单
     * @throws NoSuchFieldException
     * @throws IOException
     */
    public String getTrainRecordList(String jsonObject) throws NoSuchFieldException, IOException;
    /**
     * <li>说明：查询检修记录卡
     * <li>创建人：张迪
     * <li>创建日期：2016-8-18
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject  json字符串
     * @return 检修记录卡列表
     * @throws Exception
     */
    public String getTrainCardList (String jsonObject) throws Exception;
    
    /**
     * <li>说明：返修工位终端
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-9-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public String updateToBack(String jsonObject) throws Exception;
}
