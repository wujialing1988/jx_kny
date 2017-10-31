package com.yunda.zb.trainhandover.webservice;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车交接webservice接口
 * <li>创建人：程梅
 * <li>创建日期：2015-2-11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface IZbglHoService {
    
    /**
     * <li>说明：查询机车交接任务列表
     * <li>创建人：程梅
     * <li>创建日期：2015-2-11
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 机车整备交接任务分页列表
     */
    public String getTrainHandOverTasks(String searchJson, Long operatorid, int start, int limit);
    /**
     * 
     * <li>说明：查询交接项列表
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 交接项列表
     */
    public String getHoModelItem();
    /**
     * 
     * <li>说明：获取与机车交接项关联的机车交接情况模板
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param handOverItemIDX 交接项id
     * @return 与机车交接项关联的机车交接情况模板
     */
    public String getHoModelItemResultByItemID(String handOverItemIDX);
    /**
     * 
     * <li>说明：完成交接工作，保存数据
     * <li>创建人：程梅
     * <li>创建日期：2015-2-12
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpJson 交接单信息
     * @param itemJson 交接项信息
     * @return 操作提示信息
     */
    public String finishHoTask(String rdpJson, String itemJson);
}
