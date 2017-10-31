package com.yunda.jx.util;

import java.util.Date;

import com.yunda.frame.util.DateUtil;

/**
 * <li>标题: 机务检修
 * <li>说明：该工具类用于数据库查询时，对日期字段进行筛选过滤
 * <li>创建人： 何涛
 * <li>创建日期： 2014-8-13 下午03:59:44
 * <li>修改人: 
 * <li>修改日期： 
 * <li>修改内容： 
 * <li>版权: Copyright (c) 2008 运达科技公司
 * 
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class DateTools {

	/** Oracle 格式化日期字符串 - 24小时制 */
	public static final String DATE_FMT_24H = "yyyy-MM-dd HH24:mi:ss";
	
	/** Oracle 格式化日期字符串 - 12小时制 */
	public static final String DATE_FMT_12H = "yyyy-MM-dd HH:mm:ss";
	
	/**
     * <li>说明：对日期字段进行过滤的sql组装方法
     * <li>创建人：何涛
     * <li>创建日期：2014-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
	 * <li>@param sb 形参StringBuilder
	 * <li>@param columnName 数据表日期字段名称
	 * <li>@param startDate 查询起日
	 * <li>@param endDate 查询止日
	 */
	public static void filterDate(StringBuilder sb, String columnName, Date startDate, Date endDate) {
		if (null != startDate && null != endDate) {
			sb.append(" and " + columnName + " >= " + toQracleSqlDate(startDate, true));
			sb.append(" and " + columnName + " <= " + toQracleSqlDate(endDate, false));
		} else if (null != startDate && null == endDate) {
			sb.append(" and " + columnName + " >= "+ toQracleSqlDate(startDate, true));
		} else if (null == startDate && null != endDate) {
			sb.append(" and " + columnName + " <= "+ toQracleSqlDate(endDate, false));
		}
	}
	
	/**
     * <li>说明：将日期转换为oracle数据库查询语句样式to_date()
     * <li>创建人：何涛
     * <li>创建日期：2014-6-30
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * 
	 * <li>@param oDate 形参日期
	 * <li>@param isStart 是否是开始日期， 如果是，在日期字符串默认以00:00:00结尾；否则，在日期字符串默认以23:59:59结尾
	 * <li>@return
	 */
	public static String toQracleSqlDate(Date oDate, boolean isStart) {
		String sDate = DateUtil.DEFAULT_FORMAT.format(oDate);
		if (isStart) {
			sDate += " 00:00:00";
		} else {
			sDate += " 23:59:59";
		}
		return "to_date('" + sDate + "', '"+ DATE_FMT_24H +"')";
	}
	
}
