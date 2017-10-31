package com.yunda.jx.util;

import java.util.Date;
/**
 * 
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 工作日历接口
 * <li>创建人：程锐
 * <li>创建日期：2014-3-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0 
 */
public interface IWorkCalendar {
	/**
	 * 
	 * <li>说明：根据开始日期，工期（分钟）及默认工作日历返回结束日期
	 * <li>创建人：程锐
	 * <li>创建日期：2014-3-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param startDate 开始日期
	 * @param ratedWorkMinutes 工期（分钟）
	 * @return 结束日期
	 */
	public Date getFinalDate(Date startDate, Double ratedWorkMinutes) throws Exception;
	/**
	 * 
	 * <li>说明：根据默认日历获取超时时间
	 * <li>创建人：程锐
	 * <li>创建日期：2014-3-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param startTime 开始时间
	 * @param timeInterval 经过时间
	 * @return 超时时间
	 */
	public long getFinalTime(long startTime, long timeInterval) throws Exception;
    /**
     * 
     * <li>说明：根据开始日期，工期（分钟）及特定工作日历返回结束日期
     * <li>创建人：程梅
     * <li>创建日期：2015-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param startDate 开始日期
     * @param ratedWorkMinutes 工期（分钟）
     * @param infoIdx 工作日历id
     * @return 结束日期
     */
    public Date getFinalDate(Date startDate, Double ratedWorkMinutes , String infoIdx) throws Exception;
    /**
     * 
     * <li>说明：根据特定日历获取超时时间
     * <li>创建人：程梅
     * <li>创建日期：2015-4-17
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param startTime 开始时间
     * @param timeInterval 经过时间
     * @param infoIdx 工作日历id
     * @return 超时时间
     */
    public long getFinalTime(long startTime, long timeInterval, String infoIdx) throws Exception;
}
