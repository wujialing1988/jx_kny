package com.yunda.jx.third.edo.entity;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 描述项目进度的数据，工作时间实体类（工作时间:当为工作日具备），适用于易度项目管理（甘特图）控件从服务端返回给客户端的json数据结构
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
public class WorkingTime{
	/** 工作时间开始 */
	private String FromTime;
	/** 工作时间结束 */
	private String ToTime;
	
	public String getFromTime() {
		return FromTime;
	}
	public void setFromTime(String fromTime) {
		FromTime = fromTime;
	}
	public String getToTime() {
		return ToTime;
	}
	public void setToTime(String toTime) {
		ToTime = toTime;
	}
    public WorkingTime(){
        
    }
    public WorkingTime(String fromTime, String toTime) {
        FromTime = fromTime;
        ToTime = toTime;
    }
	
}	