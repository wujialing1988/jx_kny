package com.yunda.zb.trainonsand.webservice;

/**
 * <li>标题: 机车整备管理信息系统
 * <li>说明: 机车上砂接口
 * <li>创建人：王利成
 * <li>创建日期：2015-2-5
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface IZbglSandingService {
    
    
    /**
     * <li>说明：查询整备上砂分页列表
     * <li>创建人：王利成
     * <li>创建日期：2015-2-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param searchJson 查询条件JSON字符串
     * @param start 分页起始页
     * @param limit 每页分页大小
     * @return 整备上砂分页列表JSON字符串
     */
    public String querySandingList(String searchJson,int start, int limit);
    
    /**
     * <li>说明：开始整备上砂任务
     * <li>创建人：王利成
     * <li>创建日期：2015-2-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param trainAccessAccountIDX 机车出入库台账主键
     * @param operatorid 操作者ID
     * @return 操作成功与否
     */
    public String startSanding(String trainAccessAccountIDX,Long operatorid);
    
    /**
     * <li>说明：结束整备上砂任务
     * <li>创建人：王利成
     * <li>创建日期：2015-2-4
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param sandingIdx 上砂记录主键
     * @param sandNum 上砂量
     * @return 操作成功与否
     */
    public String endSanding(String sandingIdx, String sandNum);

}
