package com.yunda.sb.base.combo;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: IPyFilter，实现按首拼查询过滤的接口，该接口主要用于DynamicCombo动态combo组件按首拼查询过滤的功能，详见：com.yunda.cmp.biz.combo.DynamicCombo.findPageData()方法
 * <li>创建人：何涛
 * <li>创建日期：2016年10月14日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
public interface IPyFilter {
	
	/**
	 * 首拼字段后缀
	 */
	String PY_FIELD_SUFFIX = "PY";

	/**
	 * <li>说明：获取字段首拼过滤字段名称，首拼字段需要在数据库中有存储
	 * <li>创建人：何涛
	 * <li>创建日期：2016年10月14日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param xfield 字段名称
	 * @return 首拼过滤字段名称，该字段需在数据库中有存储
	 */
	public String getFieldName4PY(String xfield);
}
