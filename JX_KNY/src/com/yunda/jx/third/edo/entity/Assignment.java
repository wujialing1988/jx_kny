package com.yunda.jx.third.edo.entity;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 描述项目进度的数据，任务与资源分配关系实体类，适用于易度项目管理（甘特图）控件从服务端返回给客户端的json数据结构
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
public class Assignment{
	/** 任务UID */
	private String TaskUID;
	/** 资源UID */
	private String ResourceUID;
	/** 资源占用单位 */
	private Double Units;
	
	public String getResourceUID() {
		return ResourceUID;
	}
	public void setResourceUID(String resourceUID) {
		ResourceUID = resourceUID;
	}
	public String getTaskUID() {
		return TaskUID;
	}
	public void setTaskUID(String taskUID) {
		TaskUID = taskUID;
	}
	public Double getUnits() {
		return Units;
	}
	public void setUnits(Double units) {
		Units = units;
	}
}