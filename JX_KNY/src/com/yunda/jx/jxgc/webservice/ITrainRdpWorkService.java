package com.yunda.jx.jxgc.webservice;

import java.io.IOException;

import com.yunda.jx.pjjx.webservice.IService;


/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 用于扩展机车检修任务处理需对接的方法
 * <li>创建人：张迪
 * <li>创建日期：2016-6-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface ITrainRdpWorkService extends IService {
    
    /**
     * <li>说明：查询当前在修的机车列表
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 当前在修的机车列表
     * @throws IOException 
     */
    public String queryZXJCList(String jsonObject) throws IOException;
    
    
    /**
     * <li>说明：查询当前在修的机车列表(提票确认)
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     * @throws IOException
     */
    public String queryZXJCListAffirm() throws IOException;
    
    /**
     * <li>说明：查询当前在修的机车列表(提票验收)
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return
     * @throws IOException
     */
    public String queryZXJCListCheck() throws IOException;
    
    
    /**
     * <li>说明：查询当前在修的机车列表 带范围活提票活数量
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-11-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryZXJCListJCJX(String jsonObject) throws IOException; 
    
    /**
     * <li>说明：查询当前在修的机车列表 带提票活【已处理】及【未处理】数量
     * <li>创建人：伍佳灵
     * <li>创建日期：2016-12-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public String queryZXJCListTph(String jsonObject) throws IOException;
    
    /**
     * <li>说明：查询当前工位的机车检修节点任务列表 
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 当前工位的机车检修节点任务列表
     */
    public String queryNodeListByWorkStation(String jsonObject);
    /**
     * <li>说明：查询当前工位的检修作业节点任务数量
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 当前工位的检修作业节点任务数量
     */
    public String queryNodeCountByWorkStation(String jsonObject);
 
    /**
     * <li>说明：启动机车检修任务节点
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 提示信息
     * @throws Exception 
     */
    public String startNode (String jsonObject) throws Exception;
    /**
     * <li>说明：修竣提交节点前的验证
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 提示信息
     * @throws IOException 
     */
    public String validateFinishedStatus (String jsonObject) throws IOException;
    /**
     * <li>说明：修竣提交节点
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象
     * @return 提示信息
     * @throws IOException 
     */
    public String finishNode(String jsonObject) throws IOException;
    /**
     * <li>说明：机车检修记录单分页查询
     * <li>创建人：张迪
     * <li>创建日期：2016-6-15
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEnityJson json对象 
     * @return 结果集合JSON字符串
     * @throws IOException  
     */
    public String queryWorkCardList (String searchEnityJson) throws IOException;
    
    /**
     * <li>说明：机车检修记录单分页查询(全部)
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-1-13
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchEnityJson
     * @return
     * @throws IOException
     */
    public String queryWorkCardListAll (String searchEnityJson) throws IOException;

    /**
     * <li>说明：机车检修记录单数量查询
     * <li>创建人：张迪
     * <li>创建日期：2016-6-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param entityJson json对象 
     * @return 机车检修记录单数量
     * @throws IOException
     */
    public String queryWorkCardCount (String entityJson) throws IOException;
    /**
     * <li>说明：保存工序延误信息
     * <li>创建人：张迪
     * <li>创建日期：2016-6-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject json对象 
     * @return 返回提示信息
     */
    public String saveWorkSeqDelay(String jsonObject);
    /**
     * <li>说明：根据在修机车，当前班组查询提票活
     * <li>创建人：张迪
     * <li>创建日期：2016-8-4
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 封装实体
     * @return 提票活列表
     */
    public String queryFaultTicket(String jsonObject);
   
}
