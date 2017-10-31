package com.yunda.frame.util;

import org.hibernate.criterion.Order;

/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 获取常用hql拼接字符串
 * <li>创建人：程锐
 * <li>创建日期：2013-4-30
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class HqlUtil {
    /**
     * 
     * <li>说明：根据排序对象数组获取排序hql字符串
     * <li>创建人：程锐
     * <li>创建日期：2013-4-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param orders 排序对象数组
     * @return 排序hql字符串
     */
    public static String getOrderHql(Order[] orders){
        StringBuilder sortString = new StringBuilder("");//排序hql字符串
        if (orders != null) {
            for (Order order : orders) {
                sortString.append(" order by ").append(order);
            }
        }
        return sortString.toString();
    }
}
