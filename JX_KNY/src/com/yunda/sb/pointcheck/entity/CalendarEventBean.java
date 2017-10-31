package com.yunda.sb.pointcheck.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * <li>标题: 机车设备管理信息系统
 * <li>说明：CalendarEventBean 日期控件事件对象
 * <li>创建人：黄杨
 * <li>创建日期：2017-5-15
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 系统集成事业部设备系统项目组
 * @version 1.0
 */
@Entity
public class CalendarEventBean implements java.io.Serializable {
	/** 使用默认序列版本ID */
	private static final long serialVersionUID = 1L;

	/** id */
	@Id
	private String id;
	/** 标题 */
	private String title;
	/** 开始时间 */
	private String start;
	/** 结束时间 */
	private String end;
	/** 是否全天 */
	private boolean allDay;
	/** 背景和边框颜色 */
	private String color;
	/** 字体颜色 */
	@Column(name = "TEXT_COLOR")
	private String textColor;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

}
