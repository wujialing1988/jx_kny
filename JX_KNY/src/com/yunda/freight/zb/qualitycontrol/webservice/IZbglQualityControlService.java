package com.yunda.freight.zb.qualitycontrol.webservice;

import com.yunda.jx.pjjx.webservice.IService;

/**
 * <li>标题: 肯尼亚综合管理系统
 * <li>说明: 质量检验webservice接口
 * <li>创建人：何东
 * <li>创建日期：2017-04-20
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部整备系统项目组
 * @version 1.0
 */
public interface IZbglQualityControlService extends IService{

    /**
     * <li>说明：根据用户ID查询质量检查任务
     * <li>创建人：何东
     * <li>创建日期：2017-04-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》根据用户ID查询质量检查任务
     * @return 质量检验任务单列表
     */
    public String queryQualityControl(String jsonObject);
    
    /**
     * <li>说明：完成质量检查
     * <li>创建人：何东
     * <li>创建日期：2017-04-20
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param jsonObject 参考接口文档《JX_KNY_V1.0内部接口规范》完成质量检查
     * @return 操作结果
     */
    public String finishQualityControl(String jsonObject);
    
}
