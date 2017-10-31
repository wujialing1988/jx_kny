package com.yunda.jx.third.edo.entity;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 描述项目进度的数据，项目日历实体类，适用于易度项目管理（甘特图）控件从服务端返回给客户端的json数据结构
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
public class Calendar{
	/** 日历UID */
	private String UID;
	/** 日历名称 */
	private String Name;
	/** 是否基准日历 */
	private Integer IsBaseCalendar;
	/** 基准日历UID */
	private Double BaseCalendarUID;
	/** 工作天描述 */
	private WeekDay[] WeekDays;
	
	public Double getBaseCalendarUID() {
		return BaseCalendarUID;
	}
	public void setBaseCalendarUID(Double baseCalendarUID) {
		BaseCalendarUID = baseCalendarUID;
	}
	public Integer getIsBaseCalendar() {
		return IsBaseCalendar;
	}
	public void setIsBaseCalendar(Integer isBaseCalendar) {
		IsBaseCalendar = isBaseCalendar;
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
	public WeekDay[] getWeekDays() {
		return WeekDays;
	}
	public void setWeekDays(WeekDay[] weekDays) {
		WeekDays = weekDays;
	}
    public Calendar(){
        
    }
    public Calendar(String uid, String name, Integer isBaseCalendar, Double baseCalendarUID, WeekDay[] weekDays) {
        super();
        UID = uid;
        Name = name;
        IsBaseCalendar = isBaseCalendar;
        BaseCalendarUID = baseCalendarUID;
        WeekDays = weekDays;
    }
    
}