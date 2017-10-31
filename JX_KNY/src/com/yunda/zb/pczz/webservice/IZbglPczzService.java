package com.yunda.zb.pczz.webservice;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 普查整治webservice接口
 * <li>创建人：王利成
 * <li>创建日期：2015-3-8
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface IZbglPczzService {
    
    /**
     * <li>说明：查询普查整治任务单
     * <li>创建人：林欢
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonData 查询实体
     * @return String
     */
    public String findZbglPczzWiPageList(String jsonData);
    
    /**
     * <li>说明：通過普查整治任務單idx查询普查整治任务项
     * <li>创建人：林欢
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonData 查询实体
     * @return String
     */
    public String findZbglPczzWiItemPageList(String jsonData);
    
    /**
     * <li>说明：普查整治任务项完工/普查整治任务项检查完毕
     * <li>创建人：林欢
     * <li>创建日期：2016-8-19
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonData 查询实体
     * @return String
     */
    public String finishOrCheckedZbglPczzWiItem(String jsonData);
}
