package com.yunda.jx.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 性能监控类
 * <li>创建人：程锐
 * <li>创建日期：2014-6-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class PerformanceMonitor {
	private static Map<String, MethodPerformance> performanceMap = new HashMap<String, MethodPerformance>();
	/**
	 * 是否开启性能监控
	 */
	private static boolean OPEND = true;
	
	/**
	 * 
	 * <li>说明：开始性能监控时打印信息
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param logger 日志对象
	 * @param isBegin true 打印"开始监控性能.." false不打印
	 * @param serviceMethod 性能监控对应的方法
	 */
	public static void begin(Logger logger, boolean isBegin, String serviceMethod) {
	    if(OPEND == false){
	        return;
	    }
		if (logger == null) {
			logger = Logger.getLogger(PerformanceMonitor.class);
		}
		if (isBegin) {
			logger.info("................开始监控性能..【" + serviceMethod + "】");
		}		
		MethodPerformance mp = new MethodPerformance();
		performanceMap.put(serviceMethod + Thread.currentThread().getId(), mp);
	}
	/**
	 * 
	 * <li>说明：结束性能监控时打印信息
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param logger 日志对象
	 * @param desc 性能监控内容描述
	 * @param isEnd true 打印"结束监控性能.." false不打印
	 * @param serviceMethod 性能监控对应的方法
	 */
	public static void end(Logger logger, String desc, boolean isEnd, String serviceMethod) {
	    if(OPEND == false){
            return;
        }
		if (logger == null) {
			logger = Logger.getLogger(PerformanceMonitor.class);
		}
		MethodPerformance mp = performanceMap.get(serviceMethod + Thread.currentThread().getId());
		mp.printPerformance(logger, desc);
		if (isEnd) {
			logger.info("................结束监控性能..【" + serviceMethod + "】");
		}
		release(serviceMethod);
	}
	/**
	 * 
	 * <li>说明：释放性能监控Map对象
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param serviceMethod 性能监控对应的方法
	 */
	public static void release(String serviceMethod) {
		performanceMap.remove(serviceMethod + Thread.currentThread().getId());
	}
}
