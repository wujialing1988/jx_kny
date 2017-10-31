package com.yunda.twt.trainaccessaccount.webservice;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 此接口用于机车出入段信息，包含该业务场景所需接口
 * <li>创建人：程锐
 * <li>创建日期：2015-1-19
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface ITrainAccessAccountService {
    
    /**
     * <li>说明：获取承修车型
     * <li>创建人：程锐
     * <li>创建日期：2015-1-26
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 承修车型列表JSON字符串
     */
    @SuppressWarnings("unchecked")
    public String getUndertakeTrainType();
    
    /**
     * <li>说明：机车出入段（手工入段）
     * <li>创建人：程锐
     * <li>创建日期：2015-1-19
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeIdx 车型ID
     * @param trainno 车号
     * @param siteID 站场ID
     * @param uid 操作者ID
     * @param toGo 入段去向
     * @return 操作成功或操作错误信息
     */
    @SuppressWarnings("unchecked")
    public String intoWareHouseForPDA(String trainTypeIdx, String trainno, String siteID, String uid, String toGo);
    
    /**
     * <li>说明：自动入段机车（台位图）
     * <li>创建人：程锐
     * <li>创建日期：2015-2-9
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainList 入段的机车信息列表
     * @return 操作成功或失败信息
     */
    public String intoAccessByTWT(String trainList);
    
    /**
     * <li>说明：自动入段机车（车号识别）
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 车型车号字符串，后四位为车号，前面字符为车型全称
     * @param inTime 入段时间
     * @return 操作成功或操作错误信息
     */
    public String intoAccessByCHSB(String trainInfo, String inTime);
    
    /**
     * <li>说明：机车出段（台位图）
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainList 出段的机车信息列表
     * @return 操作成功或操作错误信息
     */
    public String outAccessByTWT(String trainList);
    
    /**
     * <li>说明：机车出段（车号识别）
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 车型车号字符串，后四位为车号，前面字符为车型全称
     * @param outTime 出段时间
     * @return 操作成功或操作错误信息
     */
    public String outAccessByCHSB(String trainInfo, String outTime);
    
    /**
     * <li>说明：台位图通过台位图服务子系统获取当前站点的所有在段机车列表
     * <li>创建人：程锐
     * <li>创建日期：2014-5-29
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param siteID 站点标识
     * @return 所有在段机车列表
     */
    public String getInAccountTrainList(String siteID);
    
    /**
     * <li>说明：删除机车入段信息及终止相关流程
     * <li>创建人：程锐
     * <li>创建日期：2014-6-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 由A,B部分组成A部分为前两位，是车型（事先约定，如H3,S3,S7），B部分为车号，如不足4位，则在B部分前面补0，直到满足4位。例如: “H30355”
     * @return String
     */
    public String updateStatusForTerminateProcess(String trainInfo);
    
    /**
     * <li>说明：获取机车入段去向数据字典列表
     * <li>创建人：程锐
     * <li>创建日期：2015-3-18
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @return 机车入段去向数据字典列表
     */
    public String getTrainToGo();
    
    /**
     * <li>说明：根据操作员id获取在段机车列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-2
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作员id
     * @return 在段机车列表
     */
    public String getInAccountTrainListByUser(Long operatorid);
    
    /**
     * <li>说明：根据车型获取车号列表
     * <li>创建人：程锐
     * <li>创建日期：2015-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainTypeShortName 车型简称
     * @return 车号列表JSON字符串
     */
    @SuppressWarnings("unchecked")
    public String getTrainNoByTrainType(String trainTypeShortName);
    
    /**
     * <li>说明：获取台位图-确定入段去向接口-机车出入段台账信息
     * <li>创建人：程锐
     * <li>创建日期：2015-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 机车信息JSON字符串
     * @return 机车出入段台账信息
     */
    public String getTrainInfoForToGo(String trainInfo);
    
    /**
     * <li>说明：台位图-确定入段去向
     * <li>创建人：程锐
     * <li>创建日期：2015-4-8
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonData 机车入段台账实体json字符串
     * @return 操作成功与否
     */
    public String confirmTogo(String jsonData);
    
    /**
     * <li>说明：自动入段机车（车号识别）
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainInfo 车型车号字符串，后四位为车号，前面字符为车型全称
     * @param inTime 入段时间
     * @param siteID 站场标示
     * @param trackName 股道名称
     * @return 操作成功或操作错误信息
     */
    public String intoAccessByCHSB(String trainInfo, String inTime, String siteID, String trackName);
    
    /**
     * <li>说明：台位图-设置车头方向
     * <li>创建人：程锐
     * <li>创建日期：2016-5-7
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAliasName 机车简称
     * @param ctfx 车头方向
     * @return 操作成功与否
     */
    public String setCtfx(String trainAliasName, String ctfx);
}
