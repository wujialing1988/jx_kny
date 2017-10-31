package com.yunda.jx.third.edo.entity;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 描述项目进度的数据，工作天实体类，适用于易度项目管理（甘特图）控件从服务端返回给客户端的json数据结构
 * <li>创建人：刘晓斌
 * <li>创建日期：2013-1-14
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
@JsonAutoDetect(fieldVisibility=Visibility.ANY,getterVisibility=Visibility.NONE)
public class WeekDay{
	/** 1(星期日),...7(星期六),0例外日期 */
	private Integer DayType;
	/** 非工作日0,工作日1 */
	private String DayWorking;
	/** 例外日期 */
	private TimePeriod TimePeriod;
	/** 工作时间:当为工作日具备 */
	private WorkingTime[] WorkingTimes;
	
	public Integer getDayType() {
		return DayType;
	}
	public void setDayType(Integer dayType) {
		DayType = dayType;
	}
	public String getDayWorking() {
		return DayWorking;
	}
	public void setDayWorking(String dayWorking) {
		DayWorking = dayWorking;
	}
	public TimePeriod getTimePeriod() {
		return TimePeriod;
	}
	public void setTimePeriod(TimePeriod timePeriod) {
		TimePeriod = timePeriod;
	}
	public WorkingTime[] getWorkingTimes() {
		return WorkingTimes;
	}
	public void setWorkingTimes(WorkingTime[] workingTimes) {
		WorkingTimes = workingTimes;
	}
    public WeekDay(){
        
    }
    public WeekDay(Integer dayType, String dayWorking, WorkingTime[] workingTimes) {
        super();
        DayType = dayType;
        DayWorking = dayWorking;
        WorkingTimes = workingTimes;
    }
    public WeekDay(Integer dayType, String dayWorking) {
        super();
        DayType = dayType;
        DayWorking = dayWorking;
    }
	
}