package com.yunda.frame.baseapp.todojobforpad.entity;
/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明：pad移动终端待办事项实体类
 * <li>创建人：程锐
 * <li>创建日期：2014-12-8
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 * 
 */ 
public class TodoJobForPad {
	/* 模块名称 */
	private String name;
	
	/* 待办内容一*/
	private String info1;
	
	/* 待办内容二*/
	private String info2;
	
	/* 图标 */
	private String icon;
	
	/* 模块id*/
	private String mid;
	
	/* 待办项的总数量*/
	private String totalCount;

	public String getInfo1() {
		return info1;
	}

	public void setInfo1(String info1) {
		this.info1 = info1;
	}

	public String getInfo2() {
		return info2;
	}

	public void setInfo2(String info2) {
		this.info2 = info2;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
}
