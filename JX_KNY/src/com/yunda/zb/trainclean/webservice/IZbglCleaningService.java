package com.yunda.zb.trainclean.webservice;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车保洁webservice接口
 * <li>创建人：程梅
 * <li>创建日期：2015-2-11
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface IZbglCleaningService {
    
    /**
     * <li>说明：查询机车保洁任务列表
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
    public String getTrainCleaningTasks(String searchJson,Long operatorid, int start, int limit);
    /**
     * 
     * <li>说明：获取机车等级数据字典
     * <li>创建人：程梅
     * <li>创建日期：2015-3-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 机车等级数据字典
     */
    public String getTrainLevel();
    /**
     * 
     * <li>说明：获取保洁等级数据字典
     * <li>创建人：程梅
     * <li>创建日期：2015-3-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @return 保洁等级数据字典
     */
    public String getCleaningLevel();
    /**
     * 
     * <li>说明：完成保洁工作，保存数据
     * <li>创建人：程梅
     * <li>创建日期：2015-3-1
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpJson 保洁信息
     * @return 操作提示信息
     */
    public String finishTrainCleaningTask(String rdpJson);
}
