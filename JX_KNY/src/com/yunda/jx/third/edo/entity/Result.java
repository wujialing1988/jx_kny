package com.yunda.jx.third.edo.entity;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 描述项目进度的数据实体类，适用于易度项目管理（甘特图）控件从服务端返回给客户端的json数据结构
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
public class Result{
	/** 项目唯一标识符 */
	private String UID;
	/** 项目名称 */
	private String Name;
	/** 项目开始日期 */
	private String StartDate;
	/** 项目完成日期 */
	private String FinishDate;
	/** 每天工时，单位：分钟 */
	private Integer MinutesPerDay;
	/** 每周工时，单位：分钟 */
	private Integer MinutesPerWeek;
	/** 每月工作日 */
	private String DaysPerMonth;
	/** 每周开始工作日 */
	private Integer WeekStartDay;
	/** 资源数组 */
	private Resource[] Resources;
	/** 日历数组 */
	private Calendar[] Calendars;
	/** 任务数组 */
	private Task[] Tasks;
    
    private boolean enableDurationLimit;
	
	public String getDaysPerMonth() {
		return DaysPerMonth;
	}

	public void setDaysPerMonth(String daysPerMonth) {
		DaysPerMonth = daysPerMonth;
	}

	public Integer getMinutesPerDay() {
		return MinutesPerDay;
	}

	public void setMinutesPerDay(Integer minutesPerDay) {
		MinutesPerDay = minutesPerDay;
	}

	public Integer getMinutesPerWeek() {
		return MinutesPerWeek;
	}

	public void setMinutesPerWeek(Integer minutesPerWeek) {
		MinutesPerWeek = minutesPerWeek;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uid) {
		UID = uid;
	}

	public Resource[] getResources() {
		return Resources;
	}

	public void setResources(Resource[] resources) {
		Resources = resources;
	}

	public Integer getWeekStartDay() {
		return WeekStartDay;
	}

	public void setWeekStartDay(Integer weekStartDay) {
		WeekStartDay = weekStartDay;
	}


	public Calendar[] getCalendars() {
		return Calendars;
	}


	public void setCalendars(Calendar[] calendars) {
		Calendars = calendars;
	}


	public Task[] getTasks() {
		return Tasks;
	}


	public void setTasks(Task[] tasks) {
		Tasks = tasks;
	}


	public String getFinishDate() {
		return FinishDate;
	}


	public void setFinishDate(String finishDate) {
		FinishDate = finishDate;
	}


	public String getStartDate() {
		return StartDate;
	}


	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

    
    public boolean isEnableDurationLimit() {
        return enableDurationLimit;
    }

    
    public void setEnableDurationLimit(boolean enableDurationLimit) {
        this.enableDurationLimit = enableDurationLimit;
    }
    
    
}