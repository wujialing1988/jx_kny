package com.yunda.zb.rdp.webservice;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 整备任务活处理webservice接口
 * <li>创建人：程锐
 * <li>创建日期：2015-2-2
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface IZbglRdpService {
    
    /**
     * <li>说明：查询整备任务活分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-2-2
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param wiStatus 待接活/待销活，见ZbglRdpWi常量STATUS_TODO/STATUS_HANDLING
     * @param operatorid 操作者ID
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 整备任务活分页列表JSON字符串
     */
    public String queryRdpWiList(String searchJson,  
                                 String wiStatus,
                                 Long operatorid,
                                 int start, 
                                 int limit,
                                 String workStationIDX);
    /**
     * <li>说明：领取整备任务活
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 整备任务活idx，多个idx用,分隔
     * @return 领取成功与否
     */
    public String receiveRdp(Long operatorid, String idxs);
    /**
     * <li>说明：撤销领取整备任务活
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param idxs 整备任务活idx，多个idx用,分隔
     * @return 领取成功与否
     */
    public String cancelReceivedRdp(Long operatorid, String idxs);
    
    /**
     * <li>说明：查询机车整备任务单数据项分页列表
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 机车整备任务单数据项分页列表
     */
    public String queryRdpWidiList(String searchJson, int start, int limit);
    
    /**
     * <li>说明：整备任务活-销活
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param operatorid 操作者ID
     * @param rdpWiIDX 机车整备任务单IDX
     * @param isHg 是否合格
     * @param widiDatas 机车整备任务单数据项实体数组JSON字符串
     * @param worker 联合作业人员
     * @return 销活成功与否
     */
    public String handleRdp(Long operatorid, String rdpWiIDX, String isHg, String widiDatas,String worker);
    
    /**
     * <li>说明：整备任务活-暂存
     * <li>创建人：程锐
     * <li>创建日期：2015-2-3
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param rdpWiIDX 机车整备任务单IDX
     * @param isHg 是否合格
     * @param widiDatas 机车整备任务单数据项实体数组JSON字符串
     * @return 暂存成功与否
     */
    public String updateForRdpWidi(String rdpWiIDX, String widiDatas, String isHg,String worker);
}
