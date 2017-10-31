package com.yunda.twt.webservice.service;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 此接口提供更新机车位置信息
 * <li>创建人：程锐
 * <li>创建日期：2015-2-6
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface ITrainPositionManageService {
    
    /**
     * <li>说明：台位图服务端获取到台位图上位置变化的所有的“机车别名”、“台位图上机车GUID”、“机车位置（设备编号、设备类型、设备名称、上设备时间）”信息，然后调用更新机车位置的接口
     * <li>创建人：程锐
     * <li>创建日期：2015-2-6
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainList 台位图位置变化的机车信息列表
     * @return 位置更新成功或失败信息
     */
    public String updateTrainPosition(String trainList);
    
    /** 
     * <li>说明：更新所有在轨道上的在段机车的设备位置信息
     * <li>创建人：程锐
     * <li>创建日期：2015-2-10
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param trackGUIDS 以,分隔的轨道GUID字符串
     * @return 操作成功或操作错误信息
     */
    public String updatePostionByTrackGUID(String trackGUIDS);
}
