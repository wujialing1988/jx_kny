package com.yunda.jx.jxgc.webservice;

import com.yunda.jx.pjjx.webservice.IService;

/**
 * <li>标题: 机车检修整备管理信息系统
 * <li>说明: 机车年计划修程webservice接口
 * <li>创建人：陈志刚
 * <li>创建日期：2016-11-09
 * <li>修改人:
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部检修整备系统项目组
 * @version 1.0
 */
public interface ITrainYearPlanService extends IService{
    /**
     * <li>说明：根据年份获取修程中机车计划台数、进车台数、竣车台数
     * <li>创建人：陈志刚
     * <li>创建日期：2016-11-09
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param year 年份
     * @return 修程中机车计划数量、进车数量、竣车数量的集合
     */
    public String getClassAndWorkPlan(String year);
}
