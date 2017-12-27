package com.yunda.frame.util;

import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 日期时间工具类，针对日期的一些常用的处理方法。
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-10-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public final class DateUtil {
	/** 日期格式“年-月-日”，yyyy-MM-dd（如2012-12-31）  */
	public static final SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
	/** 默认日期格式“年-月-日”  */
    public static final SimpleDateFormat DEFAULT_FORMAT = yyyy_MM_dd;
    /** 日期格式“年-月-日 时:分”，yyyy-MM-dd HH:mm:ss（如2012-12-31 20:31:18）  */
    public static final SimpleDateFormat yyyy_MM_dd_HH_mm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/** 日期格式“年-月-日 时:分:秒”，yyyy-MM-dd HH:mm:ss（如2012-12-31 20:31:18）  */
	public static final SimpleDateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** 日期格式“年月日时分秒”，yyyyMMddHHmmss（如20121231203118）  */
	public static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");	
	/** 日期格式“年月日时分秒毫秒”，yyyyMMddHHmmssSSS（如20121231203118978）  */
	public static final SimpleDateFormat yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");	
	/** 日期格式“年月日时分秒毫秒”，yyyy-MM-dd HH:mm:ss SSS（如2012-12-31 20:31:18 333）  */
	public static final SimpleDateFormat yyyy_MM_dd_HH_mm_ss_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");	
    

	/**
	 * <li>说明：禁止实例化该类
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 */
    private DateUtil() {}
    
    /**
     * <li>说明：使用默认日期格式（yyyy-MM-dd）解析日期字符串
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String date：日期字符串
     * @return Date 解析成功返回的日期对象
     * @throws ParseException
     */
    public static Date parse(String date) throws ParseException{
        return DEFAULT_FORMAT.parse(date);
    }
    /**
     * <li>说明：使用指定日期格式解析日期字符串
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-12-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String date：日期字符串
     * @param String format:日期格式
     * @return Date 解析成功返回的日期对象
     * @throws ParseException
     */
    public static Date parse(String date, String format) throws ParseException{
        return new SimpleDateFormat(format).parse(date);
    }    

    /**
     * <li>说明：根据格式化字符串，返回当前系统时间的字符串
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String format：日期时间格式化字符串
     * @return String 当前系统时间的字符串
     * @throws 
     */
    public static String getToday(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }
    
    /**
     * <li>说明：根据格式化对象，返回当前系统时间的字符串
     * <li>创建人：程锐
     * <li>创建日期：2015-2-11
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param format 日期时间格式化对象
     * @return String 当前系统时间的字符串
     */
    public static String getToday(SimpleDateFormat format) {
        return format.format(new Date());
    }
    
    /**
     * <li>说明：时间转换为字符串
     * <li>创建人：伍佳灵
     * <li>创建日期：2017-2-20
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param format 转换格式
     * @param fromDate 需要转换的时间
     * @return 转换后的字符串
     */
    public static String date2String(SimpleDateFormat format,Date fromDate){
        return format.format(fromDate);
    }

    /**
     * <li>说明：默认返回当前系统时间字符串，格式为“yyyy-MM-dd”。
     * <li>创建人：刘晓斌
     * <li>创建日期：2012-10-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return String 当前系统时间字符串，格式为“yyyy-MM-dd
     * @throws 
     */
    public static String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
  
    /**
	 * <li>说明：获得两个日期的月差
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Calendar one：第一个日历对象
	 * @param Calendar two：第二个日历对象
	 * @return int 相差的月数
	 * @throws 
	 */
	public static int monthDifference(Calendar one, Calendar two) {
		if (null == one || null == two) {
			throw new NullPointerException("参数对象为空。");
		}
		Calendar after = one;
		Calendar before = two;
		if (one.before(two)) {
			after = two;
			before = one;
		}
		int deffYear = Math.abs(after.get(Calendar.YEAR) - before.get(Calendar.YEAR));
		int deffMonth = after.get(Calendar.MONTH) - before.get(Calendar.MONTH);
		/*if (deffMonth < 0) {
			deffYear = deffYear - 1;
			deffMonth = Math.abs(deffMonth);
		}*/  //错误的逻辑块
		return deffYear * 12 + deffMonth;
	}

	/**
	 * <li>说明：获得两个日期的月差
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Date one：第一个日期
	 * @param Date two：第二个日期 
	 * @return int 相差的月数
	 * @throws 
	 */
	public static int monthDifference(Date one, Date two) {
		Calendar first = new GregorianCalendar();
		first.setTime(one);
		Calendar second = new GregorianCalendar();
		second.setTime(two);
		return monthDifference(first, second);
	}

	/**
	 * <li>说明：获得两个日期的月差
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param String one：第一个日期字符串，格式必须为“yyyy-MM-dd”
	 * @param String two：第二个日期字符串，格式必须为“yyyy-MM-dd”
	 * @return int 相差的月数
	 * @throws ParseException
	 */
	public static int monthDifference(String one, String two)
			throws ParseException {
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		Date first = (java.util.Date) format.parseObject(one);
		Date second = (java.util.Date) format.parseObject(two);
		return monthDifference(first, second);
	}

	/**
	 * <li>说明：是否为月的最后一天
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Calendar calendar：日历对象
	 * @return boolean true=是，false=否
	 * @throws 
	 */
	public static boolean isLastDayOfMonth(Calendar calendar) {
		Calendar today = calendar;
		Calendar tomorrow = (Calendar) calendar.clone();
		tomorrow.add(Calendar.DAY_OF_MONTH, 1);
		int todayYear = today.get(Calendar.YEAR);
		int todayMonth = today.get(Calendar.MONTH) + 1;
		int tomorrowYear = tomorrow.get(Calendar.YEAR);
		int tomorrowMonth = tomorrow.get(Calendar.MONTH) + 1;
		//是否为当月最后一天
		if (tomorrowYear > todayYear || (tomorrowYear == todayYear && tomorrowMonth > todayMonth)) {
			return true;
		}
		return false;
	}

	/**
	 * <li>说明： 是否为月的最后一天
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param Date date：日期对象 
	 * @return boolean true=是，false=否
	 * @throws 
	 */
	public static boolean isLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return isLastDayOfMonth(calendar);
	}

	/**
	 * <li>说明：当前系统时间当天是否为月的最后一天
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 
	 * @return boolean true=是，false=否
	 * @throws 
	 */
	public static boolean isLastDayOfMonth() {
		return isLastDayOfMonth(Calendar.getInstance());
	}
	
	/**
	 * <li>说明：将数字表示的月份转换位成中文表示的月份
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param int month：数字月份
	 * @return String 中文月份
	 * @throws 
	 */
	public static String convertMonth(int month) {
		switch (month) {
		case Calendar.JANUARY:
			return "一月";
		case Calendar.FEBRUARY:
			return "二月";
		case Calendar.MARCH:
			return "三月";
		case Calendar.APRIL:
			return "四月";
		case Calendar.MAY:
			return "五月";
		case Calendar.JUNE:
			return "六月";
		case Calendar.JULY:
			return "七月";
		case Calendar.AUGUST:
			return "八月";
		case Calendar.SEPTEMBER:
			return "九月";
		case Calendar.OCTOBER:
			return "十月";
		case Calendar.NOVEMBER:
			return "十一月";
		case Calendar.DECEMBER:
			return "十二月";
		default:
			throw new IllegalArgumentException("表示月份的参数无效：" + month);
		}
	}

	/**
	 * <li>说明：将数字表示的周天转换位成中文表示的周天
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param int dayOfWeek：该天在一周内的数字序号，从0开始（周日0-周六6）
	 * @return String 返回具体周天名称
	 * @throws 
	 */
	public static String convertDayOfWeek(int dayOfWeek) {
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			return "周日";
		case Calendar.MONDAY:
			return "周一";
		case Calendar.TUESDAY:
			return "周二";
		case Calendar.WEDNESDAY:
			return "周三";
		case Calendar.THURSDAY:
			return "周四";
		case Calendar.FRIDAY:
			return "周五";
		case Calendar.SATURDAY:
			return "周六";
		default:
			throw new IllegalArgumentException("参数无效：" + dayOfWeek);
		}
	}

	/**
	 * <li>说明：将数字表示的周天转换位成中文表示的星期
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param int dayOfWeek：该天在一星期内的数字序号，从0开始（星期天0-星期六6）
	 * @return String 星期几名称
	 * @throws 
	 */
	public static String convertDayOfWeek2(int dayOfWeek) {
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			return "星期天";
		case Calendar.MONDAY:
			return "星期一";
		case Calendar.TUESDAY:
			return "星期二";
		case Calendar.WEDNESDAY:
			return "星期三";
		case Calendar.THURSDAY:
			return "星期四";
		case Calendar.FRIDAY:
			return "星期五";
		case Calendar.SATURDAY:
			return "星期六";
		default:
			throw new IllegalArgumentException("参数无效：" + dayOfWeek);
		}
	}

	/**
	 * <li>说明：获取当天是星期几。
	 * 注意：不能使用new Date().getDay()获取当天在星期中的位置，应该使用Calendar.getInstance().get(Calendar.DAY_OF_WEEK)获取当天在星期中的位置
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return String 星期几名称
	 * @throws 
	 */
	public static String getTodayOfWeek2() {
		return convertDayOfWeek2(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
	}

	/**
	 * <li>说明：获取当天是周几。
	 * 注意：不能使用new Date().getDay()获取当天在星期中的位置，应该使用Calendar.getInstance().get(Calendar.DAY_OF_WEEK)获取当天在星期中的位置
	 * <li>创建人：刘晓斌
	 * <li>创建日期：2012-10-27
	 * <li>修改人： 
	 * <li>修改日期：
	 * <li>修改内容：
	 * @param 参数名：参数说明
	 * @return String 返回具体周天名称
	 * @throws 
	 */
	public static String getTodayOfWeek() {
		return convertDayOfWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
	}  
    /**
     * 
     * <li>说明：将毫秒数转换为日期格式的字符串
     * <li>创建人：程锐
     * <li>创建日期：2013-3-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param millSeconds 毫秒数
     * @param parseStr 日期格式化字符串 如"yyyy-MM-dd hh:mm:ss"
     * @return 日期格式的字符串
     */
    public static String getDateByMillSeconds(long millSeconds, String parseStr){        
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(parseStr);  
        String sb=format.format(getDateByMillSeconds(millSeconds));  
        return sb;
    }
    /**
     * 
     * <li>说明：将毫秒数转换为日期
     * <li>创建人：程锐
     * <li>创建日期：2013-3-21
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param millSeconds 毫秒数
     * @return 日期
     */
    public static Date getDateByMillSeconds(long millSeconds){
        Date date = new Date(millSeconds);  
        GregorianCalendar gc = new GregorianCalendar();   
        gc.setTime(date);  
        return gc.getTime();
    }
    /**
     * 
     * <li>说明：获取服务器时间,本月的第一天
     * <li>创建人：谭诚
     * <li>创建日期：2013-5-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return 获取服务器时间,本月的第一天
     */
    public static String getFirstDayByCurrentMonth(){
        Calendar calendar  =   new  GregorianCalendar();
        calendar.set( Calendar.DATE,  1 );
        SimpleDateFormat simpleFormate  =   new  SimpleDateFormat( "yyyy-MM-dd" );
        return simpleFormate.format(calendar.getTime());
    }
    
    /**
     * 
     * <li>说明：获取服务器时间, 本月的最后一天
     * <li>创建人：谭诚
     * <li>创建日期：2013-5-23
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param 
     * @return 获取服务器时间, 本月的最后一天
     */
    public static String getLastDayByCurrentMonth(){
        Calendar calendar  =   new  GregorianCalendar();
        calendar.set( Calendar.DATE,  1 );
        calendar.roll(Calendar.DATE,  - 1 );
        SimpleDateFormat simpleFormate  =   new  SimpleDateFormat( "yyyy-MM-dd" );
        return simpleFormate.format(calendar.getTime());
    }
    /**
     * 
     * <li>说明：获取实际工期（分钟数）临时使用，以后有工作日历再做修改
     * <li>创建人：程锐
     * <li>创建日期：2013-5-24
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param realStartDate 实际开工时间
     * @param realEndDate 实际完工时间
     * @return 实际工期（分钟数）
     * @throws Exception
     */
    public static Long getRealWorkminutes(Date realStartDate, Date realEndDate) throws Exception{
        BigDecimal realWorkminutes = new BigDecimal("0");
        long startTime = 0l;
        long endTime = 0l;
        long timeInterval = 0l; 
        if(realStartDate != null && realEndDate != null) {
            startTime = realStartDate.getTime();
            endTime = realEndDate.getTime();
            timeInterval = endTime - startTime;
            if(timeInterval > 0){
                int day = (int)timeInterval/(24*60*60*1000);        
                int hour = (int)timeInterval/(60*60*1000)-day*24;          
                int min = (int)(timeInterval/(60*1000))-day*24*60-hour*60;
                if(day >= 1){
                    realWorkminutes = new BigDecimal(day*8*60);
                    if(hour >= 1){
                        realWorkminutes = realWorkminutes.add(new BigDecimal(hour*20));
                    }
                    if(min >=1){
                        realWorkminutes = realWorkminutes.add(new BigDecimal(min));
                    }
                }else{
                    if(hour >= 1){
                        realWorkminutes = realWorkminutes.add(new BigDecimal(hour*60>=480?480:hour*60));
                    }
                    if(min >=1){
                        realWorkminutes = realWorkminutes.add(new BigDecimal(min));
                    }
                    if(realWorkminutes.compareTo(new BigDecimal(480)) > 0) realWorkminutes = new BigDecimal(480);
                }
            }
        }
        return Long.valueOf(String.valueOf(realWorkminutes));
    }
    /**
     * <li>说明：得到两个日期间隔的天数
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param String beginDate 开始日期"yyyy-MM-dd"
     * @param String endDate 结束日期"yyyy-MM-dd"
     * @return int 相差天数
     * @throws ParseException
     */
    public static int getDaysBetween(String beginDate, String endDate)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date bDate = format.parse(beginDate);
		Date eDate = format.parse(endDate);
		return getDaysBetween(bDate, eDate);
	}
    /**
     * <li>说明：得到两个日期间隔的天数
     * <li>创建人：刘晓斌
     * <li>创建日期：2013-5-27
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param Date beginDate 开始日期
     * @param Date endDate 结束日期
     * @return int 相差天数
     * @throws ParseException
     */
    public static int getDaysBetween(Date beginDate, Date endDate)
			throws ParseException {
		Calendar g1 = new GregorianCalendar();
		g1.setTime(beginDate);
		Calendar g2 = new GregorianCalendar();
		g2.setTime(endDate);
		
		int elapsed = 0;
		  GregorianCalendar gc1, gc2;

		  if (g2.after(g1)) {
		   gc2 = (GregorianCalendar) g2.clone();
		   gc1 = (GregorianCalendar) g1.clone();
		  } else {
		   gc2 = (GregorianCalendar) g1.clone();
		   gc1 = (GregorianCalendar) g2.clone();
		  }

		  gc1.clear(Calendar.MILLISECOND);
		  gc1.clear(Calendar.SECOND);
		  gc1.clear(Calendar.MINUTE);
		  gc1.clear(Calendar.HOUR_OF_DAY);

		  gc2.clear(Calendar.MILLISECOND);
		  gc2.clear(Calendar.SECOND);
		  gc2.clear(Calendar.MINUTE);
		  gc2.clear(Calendar.HOUR_OF_DAY);

		  while (gc1.before(gc2)) {
		   gc1.add(Calendar.DATE, 1);
		   elapsed++;
		  }
		  return elapsed;
	}    
    
    /**
     * <li>说明：根据开始时间和时长获取完成时间
     * <li>创建人：程锐
     * <li>创建日期：2016-3-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param startTime 开始时间
     * @param timeInterval 时长
     * @return 完成时间
     */
    public static long getFinalTime(long startTime, long timeInterval) {
        return startTime + timeInterval;
    }
    
    /**
     * <li>说明：指定一个时间相加
     * <li>创建人：程锐
     * <li>创建日期：2016-3-14
     * <li>修改人： 
     * <li>修改日期：
     * <li>修改内容：
     * @param startTime 开始时间
     * @param timeInterval 时长
     * @return 完成时间
     */
    public static Date plusDay(Date SourceDate,int day){
    	Calendar cal = Calendar.getInstance();  
        cal.setTime(SourceDate);  
        cal.add(Calendar.DATE, day);  
        return cal.getTime();
    }
    
    public static void main(String[] args){
    	String beginDate = "2013-1-2";
    	String endDate = "2012-12-30";
    	System.out.println("beginDate:");
    	System.out.println("endDate:");
    	
    	
    	try {
			System.out.println(getDaysBetween(beginDate, endDate));
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		 /*try { 
             Date realStartDate = parse("2013-05-25 17:07:44", "yyyy-MM-dd HH:mm:ss"); 
             Date realEndDate = parse("2013-05-27 10:27:00", "yyyy-MM-dd HH:mm:ss");
             System.out.println(getRealWorkminutes(realStartDate, realEndDate)); 
         }catch (Exception e) { 
              e.printStackTrace(); 
          }*/
		 
        
    }
}
