package com.yunda.jx.pjjx.repairline.webservice;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 此接口用于工位绑定
 * <li>创建人：程梅
 * <li>创建日期：2015-6-2
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修系统项目组
 * @version 1.0
 */
public interface IPartsWorkStationService {
    
    /**
     * 
     * <li>说明：查询配件检修工位列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 过滤条件
     * @param start 起始页
     * @param limit 每页条数
     * @return String 列表json
     */
    @SuppressWarnings("unchecked")
    public String getPageList(String searchJson, int start, int limit);
    /**
     * 
     * <li>说明：查询配件检修流水线列表
     * <li>创建人：程梅
     * <li>创建日期：2015-10-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 过滤条件
     * @param start 起始页
     * @param limit 每页条数
     * @return String 列表json
     */
    @SuppressWarnings("unchecked")
    public String getPartsRepairLineList(String searchJson, int start, int limit) ;
}
