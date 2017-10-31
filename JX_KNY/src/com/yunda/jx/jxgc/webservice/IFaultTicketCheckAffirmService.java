package com.yunda.jx.jxgc.webservice;

import java.io.IOException;

import com.yunda.jx.pjjx.webservice.IService;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 机车提票检查确认接口定义
 * <li>创建人：伍佳灵
 * <li>创建日期：2016-12-1
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IFaultTicketCheckAffirmService extends IService {
    
    /**
     * <li>说明：确认
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String saveAffirm(String jsonObject) throws IOException ;
    
    /**
     * <li>说明：验收
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String saveCheck(String jsonObject) throws IOException ;
    
    /**
     * <li>说明：返修
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作人ID
     * @param jsonObject 提票信息
     * @return
     * @throws IOException
     */
    public String backCheckAffirm(String jsonObject) throws IOException;
    
    /**
     * <li>说明：查询提票确认统计列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryCheckStatisticsList(String jsonObject) throws IOException;
    
    /**
     * <li>说明：查询提票信息列表
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-5
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryFaultTicketAffirm(String jsonObject) throws IOException;
    
    
    /*************** 以下为新版 **************/
    
    /**
     * <li>说明：查询提票确认统计列表（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryCheckStatisticsListNew(String jsonObject) throws IOException ;
    
    /**
     * <li>说明：确认（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String saveAffirmNew(String jsonObject) throws IOException ;
    
    /**
     * <li>说明：机车提票验收查询列表（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryFaultTicketCheck(String jsonObject) throws IOException;
    
    /**
     * <li>说明：验收（新）
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-6
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String saveCheckNew(String jsonObject) throws IOException ;
    
    /**
     * <li>说明：查询确认信息
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-16
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryFaultTicketCheckAffirm(String jsonObject) throws IOException ;
    
}
