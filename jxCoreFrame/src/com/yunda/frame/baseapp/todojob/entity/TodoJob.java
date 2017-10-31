package com.yunda.frame.baseapp.todojob.entity;


/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明： 待办项实体
 * <li>创建人：谭诚
 * <li>创建日期：2014-01-10
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class TodoJob {
	
	/* 待办类型 */
	private String jobType;
	
	/* 待办内容标题*/
	private String jobText;
	
	/* 待办项的业务页面链接*/
	private String jobUrl;
	
	/* 待办项的数量*/
	private String jobNum;
	
	/* 返回对象主键数组*/
	private String[] objcetIDXString;
	
	public String[] getObjcetIDXString() {
		return objcetIDXString;
	}

	public void setObjcetIDXString(String[] objcetIDXString) {
		this.objcetIDXString = objcetIDXString;
	}

	/** @param 获取待办项数量*/
	public String getJobNum() {
		return jobNum;
	}

	/** @param 设置待办项数量*/
	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}

	/** @param 获取待办项内容*/
	public String getJobText() {
		return jobText;
	}

	/** @param 设置待办项内容*/
	public void setJobText(String jobText) {
//		//限制长度
//		if(!com.yunda.frame.util.StringUtil.isNullOrBlank(jobText)){
//			if(jobText.length()<20) this.jobText = jobText;
//			else this.jobText = jobText.substring(0, 20);
//		}
		this.jobText = jobText;
	}

	/** @param 获取待办项类型*/
	public String getJobType() {
		return jobType;
	}

	/** @param 设置待办项类型*/
	public void setJobType(String jobType) {
		//限制长度
		if(!com.yunda.frame.util.StringUtil.isNullOrBlank(jobType)){
			if(jobType.length()<10) this.jobType = jobType; 
			else this.jobType = jobType.substring(0, 10);
		}
//		this.jobType = jobType;
	}

	/** @param 获取待办项链接URL*/
	public String getJobUrl() {
		return jobUrl;
	}

	/** @param 设置待办项链接URL*/
	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}
}
