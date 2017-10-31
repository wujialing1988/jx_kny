package com.yunda.frame.common;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * <li>标题: 公共查询Base_comboTree和Base_multyComboTree接口
 * <li>说明: 类的功能描述
 * <li>创建人：程锐
 * <li>创建日期：2014-1-20
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public interface IbaseComboTree {
	/**
	 * 
	 * <li>说明：公共查询Base_comboTree和Base_multyComboTree方法，子类重写该方法返回查询数据
	 * <li>创建人：程锐
	 * <li>创建日期：2014-1-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param req HttpServletRequest对象
	 * @return List<HashMap> 前台树所需数据列表
	 * @throws Exception
	 */
	public List<HashMap> getBaseComboTree(HttpServletRequest req) throws Exception;
}
