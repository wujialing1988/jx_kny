package com.yunda.frame.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: OrderIgnore，使用SQL进行实体查询时，如果字段添加了该注解，则不处理此字段的排序
 * <li>创建人：何涛
 * <li>创建日期：2017年3月3日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreOrder {

}
