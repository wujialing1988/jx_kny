package com.yunda.sb.base.common;

/**
 * <li>标题: 设备管理信息系统
 * <li>说明: IEditorGridManager，可编辑表格进行编辑时的验证接口
 * <li>创建人：何涛
 * <li>创建日期：2017年3月30日
 * <li>修改人：
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 信息系统事业部设备管理系统项目组
 * @version 3.0.1
 */
public interface IEditorGridManager<T> {

	/**
	 * <li>说明：可编辑表格保存前的验证方法，如果验证不通过则返回错误消息
	 * <li>创建人：何涛
	 * <li>创建日期：2017年3月30日
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 * @param fieldName 更新的字段名称
	 * @param t 被更新的实体对象
	 * @return 如果验证不通过则返回错误消息，通过则返回null
	 */
	public String validateSaveChange(String fieldName, T t);
	
}
