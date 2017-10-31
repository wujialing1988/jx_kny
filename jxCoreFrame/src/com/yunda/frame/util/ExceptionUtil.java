package com.yunda.frame.util;

import java.util.Map;
import org.apache.log4j.Logger;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 异常处理实用工具类
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-4-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class ExceptionUtil {

	/**
	 * <li>说明：提供控制层捕获异常的统一处理方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-15
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public final static void process(Exception e, Logger logger, Map<String,Object> map){
//		e.printStackTrace();
		map.put("success", false);
//      map.put("errMsg", "异常：" + e + "<br/>原因：" + e.getCause());
        map.put("errMsg", e.getMessage());
		logger.error("控制层捕获异常：", e);
	}
	/**
	 * <li>说明：提供业务层捕获异常的统一处理方法
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2013-4-16
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return 返回值说明
	 * @throws 抛出异常列表
	 */
	public final static void process(Exception e, Logger logger){
//		e.printStackTrace();
		logger.error("业务层捕获异常：", e);
	}
    
    /**
     * <li>说明：提供控制层捕获异常的统一处理方法,增加对前台错误信息的自定义处理
     * <li>创建人：程锐
     * <li>创建日期：2015-4-27
     * <li>修改人：
     * <li>修改日期：
     * <li>修改内容：
     * @param e 异常对象
     * @param logger 日志对象
     * @param map 返回至前台map
     * @param errorMessage 错误自定义信息
     */
    public final static void process(Exception e, Logger logger, Map<String, Object> map, String errorMessage) {
        map.put("success", false);
        map.put("errMsg", StringUtil.isNullOrBlank(errorMessage) ? e.toString() : errorMessage);
        logger.error("控制层捕获异常：", e);
    }
}
