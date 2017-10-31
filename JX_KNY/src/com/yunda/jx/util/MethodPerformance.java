package com.yunda.jx.util;

import org.apache.log4j.Logger;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 记录性能监控信息类
 * <li>创建人：程锐
 * <li>创建日期：2014-6-24
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class MethodPerformance {
	private long beginTimeMillis;//开始性能监控毫秒数
	private long endTimeMills;//结束性能监控毫秒数
	
	public MethodPerformance() {
		this.beginTimeMillis = System.currentTimeMillis();
	}
	/**
	 * 
	 * <li>说明：结束性能监控时打印耗时信息
	 * <li>创建人：程锐
	 * <li>创建日期：2014-6-24
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param logger 日志对象
	 * @param desc 性能监控内容描述
	 */
	public void printPerformance(Logger logger, String desc) {
		endTimeMills = System.currentTimeMillis();
		long elapse = endTimeMills - beginTimeMillis;
		logger.info(desc + "耗时:" + elapse + "毫秒");
	}
	public long getBeginTimeMillis() {
		return beginTimeMillis;
	}
	public void setBeginTimeMillis(long beginTimeMillis) {
		this.beginTimeMillis = beginTimeMillis;
	}
	public long getEndTimeMills() {
		return endTimeMills;
	}
	public void setEndTimeMills(long endTimeMills) {
		this.endTimeMills = endTimeMills;
	}
	
}
