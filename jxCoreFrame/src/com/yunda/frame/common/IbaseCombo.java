package com.yunda.frame.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 公共查询Base_combo组件接口
 * <li>创建人：程锐
 * <li>创建日期：2014-1-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IbaseCombo {
	/**
     * <li>方法说明：公共查询Base_combo方法，实现类重写该方法返回查询数据
     * <li>方法名称：getBaseComboData
     * <li>@param HttpServletRequest req HttpServletRequest对象
     * <li>@param start
     * <li>@param limt
     * <li>@return
     * <li>return: Map<String,Object>
     * <li>创建人：张凡
     * <li>创建时间：2013-7-12 下午07:37:56
     * <li>修改人：
     * <li>修改内容：
     */
    public Map<String, Object> getBaseComboData(HttpServletRequest req, int start, int limit) throws Exception;
}
