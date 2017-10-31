package com.yunda.frame.common;

/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明: 该类提供检修系统相关配置信息，对应属性文件JXSystem.properties
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-10-27
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */
public class JXSystemProperties{
	/** 用于数据同步，设置当前运行系统每条数据记录的站点标识 */
	public final static String SYN_SITEID = JXConfig.getInstance().getSynSiteID();
	/** 设置系统当前机务段机构代码 */
	public final static String OVERSEA_ORGCODE = JXConfig.getInstance().getOverseaOrgcode();	
}