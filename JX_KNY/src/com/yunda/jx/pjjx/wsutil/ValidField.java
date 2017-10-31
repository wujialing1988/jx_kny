package com.yunda.jx.pjjx.wsutil;

import com.yunda.jx.pjjx.webservice.IService;

/**
 * <li>说明： 验证字段
 * <li>创建人： 张凡
 * <li>创建日期：2015-10-15
 * <li>成都运达科技股份有限公司
 */
public class ValidField {
	/**
	 * <li>方法说明：空指针验证
	 * <li>方法名：nullPointer
	 * @param field
	 * <li>创建人： 张凡
	 * <li>创建日期：2015-10-15
	 * <li>修改人：
	 * <li>修改内容：
	 * <li>修改日期：
	 */
	public  static void nullPointer(Object field){
		if(field == null)
			throw new NullPointerException(IService.MSG_ERROR_ARGS_NULL);
	}
}
